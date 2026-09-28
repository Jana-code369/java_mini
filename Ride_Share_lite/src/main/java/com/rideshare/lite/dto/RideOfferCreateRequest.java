package com.rideshare.lite.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class RideOfferCreateRequest {

    @NotNull(message = "Driver ID is required")
    private Long driverId;

    @NotBlank(message = "Origin location is required")
    private String origin;

    @NotBlank(message = "Destination location is required")
    private String destination;

    @NotNull(message = "Departure time is required")
    @Future(message = "Departure time must be in the future")
    private LocalDateTime departureTime;

    @NotNull(message = "Total seats is required")
    @Min(value = 1, message = "Available seats must be at least 1")
    private Integer totalSeats;

    public RideOfferCreateRequest() {
    }

    public RideOfferCreateRequest(Long driverId, String origin, String destination, LocalDateTime departureTime, Integer totalSeats) {
        this.driverId = driverId;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.totalSeats = totalSeats;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
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
}
