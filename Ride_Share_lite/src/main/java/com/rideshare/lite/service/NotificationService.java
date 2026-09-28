package com.rideshare.lite.service;

import com.rideshare.lite.model.RideOffer;
import com.rideshare.lite.model.RideRequest;

public interface NotificationService {

    void notifyRideOfferCreated(RideOffer rideOffer);

    void notifyRideRequestCreated(RideRequest rideRequest);

    void notifyRideRequestStatusChanged(RideRequest rideRequest);

    void notifyRideOfferStatusChanged(RideOffer rideOffer);
}
