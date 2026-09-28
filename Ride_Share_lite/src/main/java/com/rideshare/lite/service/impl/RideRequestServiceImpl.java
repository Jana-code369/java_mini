package com.rideshare.lite.service.impl;

import com.rideshare.lite.dto.RideRequestCreateRequest;
import com.rideshare.lite.dto.RideRequestResponse;
import com.rideshare.lite.exception.InvalidRideOperationException;
import com.rideshare.lite.exception.ResourceNotFoundException;
import com.rideshare.lite.exception.SeatUnavailableException;
import com.rideshare.lite.model.RideOffer;
import com.rideshare.lite.model.RideOfferStatus;
import com.rideshare.lite.model.RideRequest;
import com.rideshare.lite.model.RideRequestStatus;
import com.rideshare.lite.model.User;
import com.rideshare.lite.repository.RideOfferRepository;
import com.rideshare.lite.repository.RideRequestRepository;
import com.rideshare.lite.service.NotificationService;
import com.rideshare.lite.service.RideOfferService;
import com.rideshare.lite.service.RideRequestService;
import com.rideshare.lite.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RideRequestServiceImpl implements RideRequestService {

    private final RideRequestRepository rideRequestRepository;
    private final RideOfferRepository rideOfferRepository;
    private final RideOfferService rideOfferService;
    private final UserService userService;
    private final NotificationService notificationService;

    public RideRequestServiceImpl(RideRequestRepository rideRequestRepository,
                                  RideOfferRepository rideOfferRepository,
                                  RideOfferService rideOfferService,
                                  UserService userService,
                                  NotificationService notificationService) {
        this.rideRequestRepository = rideRequestRepository;
        this.rideOfferRepository = rideOfferRepository;
        this.rideOfferService = rideOfferService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Override
    public RideRequestResponse createRideRequest(RideRequestCreateRequest request) {
        RideOffer rideOffer = rideOfferService.getEntityById(request.getRideOfferId());
        User rider = userService.getEntityById(request.getRiderId());

        // Business Rule 1: A user cannot request a seat on their own published ride.
        if (rideOffer.getDriver().getId().equals(rider.getId())) {
            throw new InvalidRideOperationException("Invalid Request: You cannot request a seat on your own published ride.");
        }

        // Validate ride offer status
        if (rideOffer.getStatus() != RideOfferStatus.PLANNED) {
            throw new InvalidRideOperationException("Cannot request seats for a ride that is " + rideOffer.getStatus());
        }

        // Check seat availability at time of request submission
        if (rideOffer.getAvailableSeats() < request.getSeatsRequested()) {
            throw new SeatUnavailableException("Requested seats (" + request.getSeatsRequested() + 
                    ") exceed available seats (" + rideOffer.getAvailableSeats() + ").");
        }

        // Prevent duplicate pending requests from same rider for same ride
        boolean existingPending = rideRequestRepository.existsByRideOfferIdAndRiderIdAndStatusIn(
                rideOffer.getId(), rider.getId(), List.of(RideRequestStatus.PENDING, RideRequestStatus.APPROVED));
        if (existingPending) {
            throw new InvalidRideOperationException("You already have an active or pending request for this ride.");
        }

        RideRequest rideRequest = new RideRequest(rideOffer, rider, request.getSeatsRequested());
        RideRequest savedRequest = rideRequestRepository.save(rideRequest);

        notificationService.notifyRideRequestCreated(savedRequest);

        return RideRequestResponse.fromEntity(savedRequest);
    }

    @Override
    public RideRequestResponse approveRideRequest(Long requestId, Long driverId) {
        RideRequest request = getEntityById(requestId);
        RideOffer rideOffer = request.getRideOffer();

        // Validate driver authority
        if (!rideOffer.getDriver().getId().equals(driverId)) {
            throw new InvalidRideOperationException("Unauthorized: Only the driver who published the ride can approve requests.");
        }

        if (request.getStatus() == RideRequestStatus.APPROVED) {
            throw new InvalidRideOperationException("Ride request is already approved.");
        }

        // Business Rule 2: A ride request cannot be approved if no seats remain.
        if (rideOffer.getAvailableSeats() < request.getSeatsRequested()) {
            throw new SeatUnavailableException("Approval Failed: Insufficient seats remaining. Requested: " + 
                    request.getSeatsRequested() + ", Available: " + rideOffer.getAvailableSeats());
        }

        // Business Rule 3: Approved requests reduce available seats.
        rideOffer.setAvailableSeats(rideOffer.getAvailableSeats() - request.getSeatsRequested());
        rideOfferRepository.save(rideOffer);

        request.setStatus(RideRequestStatus.APPROVED);
        RideRequest updatedRequest = rideRequestRepository.save(request);

        notificationService.notifyRideRequestStatusChanged(updatedRequest);

        return RideRequestResponse.fromEntity(updatedRequest);
    }

    @Override
    public RideRequestResponse rejectRideRequest(Long requestId, Long driverId) {
        RideRequest request = getEntityById(requestId);
        RideOffer rideOffer = request.getRideOffer();

        // Validate driver authority
        if (!rideOffer.getDriver().getId().equals(driverId)) {
            throw new InvalidRideOperationException("Unauthorized: Only the driver who published the ride can reject requests.");
        }

        // If previously approved, restore available seats
        if (request.getStatus() == RideRequestStatus.APPROVED) {
            rideOffer.setAvailableSeats(rideOffer.getAvailableSeats() + request.getSeatsRequested());
            rideOfferRepository.save(rideOffer);
        }

        request.setStatus(RideRequestStatus.REJECTED);
        RideRequest updatedRequest = rideRequestRepository.save(request);

        notificationService.notifyRideRequestStatusChanged(updatedRequest);

        return RideRequestResponse.fromEntity(updatedRequest);
    }

    @Override
    public RideRequestResponse cancelRideRequest(Long requestId, Long riderId) {
        RideRequest request = getEntityById(requestId);

        if (!request.getRider().getId().equals(riderId)) {
            throw new InvalidRideOperationException("Unauthorized: Only the rider who created the request can cancel it.");
        }

        // If previously approved, restore available seats
        if (request.getStatus() == RideRequestStatus.APPROVED) {
            RideOffer rideOffer = request.getRideOffer();
            rideOffer.setAvailableSeats(rideOffer.getAvailableSeats() + request.getSeatsRequested());
            rideOfferRepository.save(rideOffer);
        }

        request.setStatus(RideRequestStatus.CANCELLED);
        RideRequest updatedRequest = rideRequestRepository.save(request);

        notificationService.notifyRideRequestStatusChanged(updatedRequest);

        return RideRequestResponse.fromEntity(updatedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public RideRequestResponse getRideRequestById(Long id) {
        RideRequest request = getEntityById(id);
        return RideRequestResponse.fromEntity(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideRequestResponse> getRequestsByRider(Long riderId) {
        userService.getEntityById(riderId);
        return rideRequestRepository.findByRiderId(riderId).stream()
                .map(RideRequestResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideRequestResponse> getRequestsForRideOffer(Long rideOfferId, Long driverId) {
        RideOffer rideOffer = rideOfferService.getEntityById(rideOfferId);
        if (!rideOffer.getDriver().getId().equals(driverId)) {
            throw new InvalidRideOperationException("Unauthorized: Only the driver who published the ride can view its requests.");
        }

        return rideRequestRepository.findByRideOfferId(rideOfferId).stream()
                .map(RideRequestResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private RideRequest getEntityById(Long id) {
        return rideRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ride request not found with ID: " + id));
    }
}
