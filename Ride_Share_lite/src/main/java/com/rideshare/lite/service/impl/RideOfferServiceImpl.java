package com.rideshare.lite.service.impl;

import com.rideshare.lite.dto.RideOfferCreateRequest;
import com.rideshare.lite.dto.RideOfferResponse;
import com.rideshare.lite.exception.InvalidRideOperationException;
import com.rideshare.lite.exception.ResourceNotFoundException;
import com.rideshare.lite.model.RideOffer;
import com.rideshare.lite.model.RideOfferStatus;
import com.rideshare.lite.model.User;
import com.rideshare.lite.repository.RideOfferRepository;
import com.rideshare.lite.service.NotificationService;
import com.rideshare.lite.service.RideOfferService;
import com.rideshare.lite.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RideOfferServiceImpl implements RideOfferService {

    private final RideOfferRepository rideOfferRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    public RideOfferServiceImpl(RideOfferRepository rideOfferRepository,
                                 UserService userService,
                                 NotificationService notificationService) {
        this.rideOfferRepository = rideOfferRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Override
    public RideOfferResponse createRideOffer(RideOfferCreateRequest request) {
        User driver = userService.getEntityById(request.getDriverId());

        if (request.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new InvalidRideOperationException("Departure time cannot be in the past.");
        }

        RideOffer rideOffer = new RideOffer(
                driver,
                request.getOrigin().trim(),
                request.getDestination().trim(),
                request.getDepartureTime(),
                request.getTotalSeats()
        );

        RideOffer savedOffer = rideOfferRepository.save(rideOffer);
        notificationService.notifyRideOfferCreated(savedOffer);

        return RideOfferResponse.fromEntity(savedOffer);
    }

    @Override
    @Transactional(readOnly = true)
    public RideOfferResponse getRideOfferById(Long id) {
        RideOffer rideOffer = getEntityById(id);
        return RideOfferResponse.fromEntity(rideOffer);
    }

    @Override
    @Transactional(readOnly = true)
    public RideOffer getEntityById(Long id) {
        return rideOfferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ride offer not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideOfferResponse> searchRides(String origin, String destination, LocalDateTime startTime, LocalDateTime endTime, Integer minSeats) {
        List<RideOffer> rides = rideOfferRepository.searchRides(
                origin != null && !origin.isBlank() ? origin.trim() : null,
                destination != null && !destination.isBlank() ? destination.trim() : null,
                startTime,
                endTime,
                minSeats != null ? minSeats : 1
        );
        return rides.stream()
                .map(RideOfferResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RideOfferResponse> searchRidesPaginated(String origin, String destination, LocalDateTime startTime, LocalDateTime endTime, Integer minSeats, Pageable pageable) {
        Page<RideOffer> ridesPage = rideOfferRepository.searchRidesWithPagination(
                origin != null && !origin.isBlank() ? origin.trim() : null,
                destination != null && !destination.isBlank() ? destination.trim() : null,
                startTime,
                endTime,
                minSeats != null ? minSeats : 1,
                pageable
        );
        return ridesPage.map(RideOfferResponse::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideOfferResponse> getRideOffersByDriver(Long driverId) {
        // Ensure user exists
        userService.getEntityById(driverId);
        return rideOfferRepository.findByDriverId(driverId).stream()
                .map(RideOfferResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public RideOfferResponse updateRideOfferStatus(Long offerId, Long driverId, RideOfferStatus status) {
        RideOffer rideOffer = getEntityById(offerId);

        if (!rideOffer.getDriver().getId().equals(driverId)) {
            throw new InvalidRideOperationException("Only the driver who published this ride can update its status.");
        }

        rideOffer.setStatus(status);
        RideOffer updatedOffer = rideOfferRepository.save(rideOffer);
        notificationService.notifyRideOfferStatusChanged(updatedOffer);

        return RideOfferResponse.fromEntity(updatedOffer);
    }
}
