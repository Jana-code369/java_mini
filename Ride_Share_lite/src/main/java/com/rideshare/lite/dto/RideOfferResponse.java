package com.rideshare.lite.dto;

import com.rideshare.lite.model.RideOffer;
import com.rideshare.lite.model.RideOfferStatus;
import java.time.LocalDateTime;

public class RideOfferResponse {

    private Long id;
    private Long driverId;
    private String driverName;
    private String driverEmail;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private Integer totalSeats;
    private Integer availableSeats;
    private RideOfferStatus status;
    private LocalDateTime createdAt;

    public RideOfferResponse() {
    }

    public static RideOfferResponse fromEntity(RideOffer rideOffer) {
        if (rideOffer == null) return null;
        RideOfferResponse response = new RideOfferResponse();
        response.setId(rideOffer.getId());
        if (rideOffer.getDriver() != null) {
            response.setDriverId(rideOffer.getDriver().getId());
            response.setDriverName(rideOffer.getDriver().getName());
            response.setDriverEmail(rideOffer.getDriver().getEmail());
        }
        response.setOrigin(rideOffer.getOrigin());
        response.setDestination(rideOffer.getDestination());
        response.setDepartureTime(rideOffer.getDepartureTime());
        response.setTotalSeats(rideOffer.getTotalSeats());
        response.setAvailableSeats(rideOffer.getAvailableSeats());
        response.setStatus(rideOffer.getStatus());
        response.setCreatedAt(rideOffer.getCreatedAt());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
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

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public RideOfferStatus getStatus() {
        return status;
    }

    public void setStatus(RideOfferStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
