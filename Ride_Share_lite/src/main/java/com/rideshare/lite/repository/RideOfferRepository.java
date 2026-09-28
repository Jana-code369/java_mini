package com.rideshare.lite.repository;

import com.rideshare.lite.model.RideOffer;
import com.rideshare.lite.model.RideOfferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RideOfferRepository extends JpaRepository<RideOffer, Long> {

    List<RideOffer> findByDriverId(Long driverId);

    Page<RideOffer> findByDriverId(Long driverId, Pageable pageable);

    @Query("SELECT r FROM RideOffer r WHERE " +
           "(:origin IS NULL OR LOWER(r.origin) LIKE LOWER(CONCAT('%', :origin, '%'))) AND " +
           "(:destination IS NULL OR LOWER(r.destination) LIKE LOWER(CONCAT('%', :destination, '%'))) AND " +
           "(:startTime IS NULL OR r.departureTime >= :startTime) AND " +
           "(:endTime IS NULL OR r.departureTime <= :endTime) AND " +
           "(:minSeats IS NULL OR r.availableSeats >= :minSeats) AND " +
           "(r.status = 'PLANNED') " +
           "ORDER BY r.departureTime ASC")
    List<RideOffer> searchRides(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("minSeats") Integer minSeats
    );

    @Query("SELECT r FROM RideOffer r WHERE " +
           "(:origin IS NULL OR LOWER(r.origin) LIKE LOWER(CONCAT('%', :origin, '%'))) AND " +
           "(:destination IS NULL OR LOWER(r.destination) LIKE LOWER(CONCAT('%', :destination, '%'))) AND " +
           "(:startTime IS NULL OR r.departureTime >= :startTime) AND " +
           "(:endTime IS NULL OR r.departureTime <= :endTime) AND " +
           "(:minSeats IS NULL OR r.availableSeats >= :minSeats) AND " +
           "(r.status = 'PLANNED')")
    Page<RideOffer> searchRidesWithPagination(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("minSeats") Integer minSeats,
            Pageable pageable
    );
}
