package com.rideshare.lite.repository;

import com.rideshare.lite.model.RideRequest;
import com.rideshare.lite.model.RideRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {

    List<RideRequest> findByRiderId(Long riderId);

    Page<RideRequest> findByRiderId(Long riderId, Pageable pageable);

    List<RideRequest> findByRideOfferId(Long rideOfferId);

    List<RideRequest> findByRideOfferDriverId(Long driverId);

    Optional<RideRequest> findByRideOfferIdAndRiderId(Long rideOfferId, Long riderId);

    boolean existsByRideOfferIdAndRiderIdAndStatusIn(Long rideOfferId, Long riderId, List<RideRequestStatus> statuses);
}
