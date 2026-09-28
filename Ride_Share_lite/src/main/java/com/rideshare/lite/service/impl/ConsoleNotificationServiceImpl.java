package com.rideshare.lite.service.impl;

import com.rideshare.lite.model.RideOffer;
import com.rideshare.lite.model.RideRequest;
import com.rideshare.lite.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ConsoleNotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(ConsoleNotificationServiceImpl.class);

    @Override
    public void notifyRideOfferCreated(RideOffer rideOffer) {
        log.info("[NOTIFICATION] New Ride Published! Driver: {} (ID: {}) | Route: {} -> {} | Departure: {} | Seats: {}",
                rideOffer.getDriver().getName(), rideOffer.getDriver().getId(),
                rideOffer.getOrigin(), rideOffer.getDestination(),
                rideOffer.getDepartureTime(), rideOffer.getAvailableSeats());
    }

    @Override
    public void notifyRideRequestCreated(RideRequest rideRequest) {
        log.info("[NOTIFICATION] New Ride Request Submitted! Request ID: {} | Rider: {} (ID: {}) requested {} seat(s) on Ride #{}",
                rideRequest.getId(), rideRequest.getRider().getName(), rideRequest.getRider().getId(),
                rideRequest.getSeatsRequested(), rideRequest.getRideOffer().getId());
    }

    @Override
    public void notifyRideRequestStatusChanged(RideRequest rideRequest) {
        log.info("[NOTIFICATION] Status Update for Request #{}: Status changed to [{}] for Rider: {} | Ride #{}, Remaining Seats: {}",
                rideRequest.getId(), rideRequest.getStatus(), rideRequest.getRider().getName(),
                rideRequest.getRideOffer().getId(), rideRequest.getRideOffer().getAvailableSeats());
    }

    @Override
    public void notifyRideOfferStatusChanged(RideOffer rideOffer) {
        log.info("[NOTIFICATION] Status Update for Ride Offer #{}: Status changed to [{}]",
                rideOffer.getId(), rideOffer.getStatus());
    }
}
