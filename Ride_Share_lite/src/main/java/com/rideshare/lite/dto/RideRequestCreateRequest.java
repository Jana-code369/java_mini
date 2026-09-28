package com.rideshare.lite.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RideRequestCreateRequest {

    @NotNull(message = "Ride Offer ID is required")
    private Long rideOfferId;

    @NotNull(message = "Rider ID is required")
    private Long riderId;

    @NotNull(message = "Seats requested is required")
    @Min(value = 1, message = "Seats requested must be at least 1")
    private Integer seatsRequested = 1;

    public RideRequestCreateRequest() {
    }

    public RideRequestCreateRequest(Long rideOfferId, Long riderId, Integer seatsRequested) {
        this.rideOfferId = rideOfferId;
        this.riderId = riderId;
        this.seatsRequested = seatsRequested;
    }

    public Long getRideOfferId() {
        return rideOfferId;
    }

    public void setRideOfferId(Long rideOfferId) {
        this.rideOfferId = rideOfferId;
    }

    public Long getRiderId() {
        return riderId;
    }

    public void setRiderId(Long riderId) {
        this.riderId = riderId;
    }

    public Integer getSeatsRequested() {
        return seatsRequested;
    }

    public void setSeatsRequested(Integer seatsRequested) {
        this.seatsRequested = seatsRequested;
    }
}
