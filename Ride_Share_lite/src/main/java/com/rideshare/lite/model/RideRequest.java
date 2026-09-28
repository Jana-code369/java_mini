package com.rideshare.lite.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ride_requests")
public class RideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ride_offer_id", nullable = false)
    private RideOffer rideOffer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rider_id", nullable = false)
    private User rider;

    @Column(name = "seats_requested", nullable = false)
    private Integer seatsRequested = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RideRequestStatus status = RideRequestStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public RideRequest() {
    }

    public RideRequest(RideOffer rideOffer, User rider, Integer seatsRequested) {
        this.rideOffer = rideOffer;
        this.rider = rider;
        this.seatsRequested = seatsRequested != null ? seatsRequested : 1;
        this.status = RideRequestStatus.PENDING;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RideOffer getRideOffer() {
        return rideOffer;
    }

    public void setRideOffer(RideOffer rideOffer) {
        this.rideOffer = rideOffer;
    }

    public User getRider() {
        return rider;
    }

    public void setRider(User rider) {
        this.rider = rider;
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
