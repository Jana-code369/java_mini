package com.rideshare.lite.dto;

import com.rideshare.lite.model.RideRequest;
import com.rideshare.lite.model.RideRequestStatus;
import java.time.LocalDateTime;

public class RideRequestResponse {

    private Long id;
    private Long rideOfferId;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private Long riderId;
    private String riderName;
    private String riderEmail;
    private Integer seatsRequested;
    private RideRequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RideRequestResponse() {
    }

    public static RideRequestResponse fromEntity(RideRequest request) {
        if (request == null) return null;
        RideRequestResponse response = new RideRequestResponse();
        response.setId(request.getId());
        if (request.getRideOffer() != null) {
            response.setRideOfferId(request.getRideOffer().getId());
            response.setOrigin(request.getRideOffer().getOrigin());
            response.setDestination(request.getRideOffer().getDestination());
            response.setDepartureTime(request.getRideOffer().getDepartureTime());
        }
        if (request.getRider() != null) {
            response.setRiderId(request.getRider().getId());
            response.setRiderName(request.getRider().getName());
            response.setRiderEmail(request.getRider().getEmail());
        }
        response.setSeatsRequested(request.getSeatsRequested());
        response.setStatus(request.getStatus());
        response.setCreatedAt(request.getCreatedAt());
        response.setUpdatedAt(request.getUpdatedAt());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRideOfferId() {
        return rideOfferId;
    }

    public void setRideOfferId(Long rideOfferId) {
        this.rideOfferId = rideOfferId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Long getRiderId() {
        return riderId;
    }

    public void setRiderId(Long riderId) {
        this.riderId = riderId;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderEmail() {
        return riderEmail;
    }

    public void setRiderEmail(String riderEmail) {
        this.riderEmail = riderEmail;
    }

    public Integer getSeatsRequested() {
        return seatsRequested;
    }

    public void setSeatsRequested(Integer seatsRequested) {
        this.seatsRequested = seatsRequested;
    }

    public RideRequestStatus getStatus() {
        return status;
    }

    public void setStatus(RideRequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
