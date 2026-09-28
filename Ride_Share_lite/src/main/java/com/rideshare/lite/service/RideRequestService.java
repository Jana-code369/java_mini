package com.rideshare.lite.service;

import com.rideshare.lite.dto.RideRequestCreateRequest;
import com.rideshare.lite.dto.RideRequestResponse;

import java.util.List;

public interface RideRequestService {

    RideRequestResponse createRideRequest(RideRequestCreateRequest request);

    RideRequestResponse approveRideRequest(Long requestId, Long driverId);

    RideRequestResponse rejectRideRequest(Long requestId, Long driverId);

    RideRequestResponse cancelRideRequest(Long requestId, Long riderId);

    RideRequestResponse getRideRequestById(Long id);

    List<RideRequestResponse> getRequestsByRider(Long riderId);

    List<RideRequestResponse> getRequestsForRideOffer(Long rideOfferId, Long driverId);
}
