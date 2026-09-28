package com.rideshare.lite.service;

import com.rideshare.lite.dto.RideOfferCreateRequest;
import com.rideshare.lite.dto.RideOfferResponse;
import com.rideshare.lite.model.RideOffer;
import com.rideshare.lite.model.RideOfferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface RideOfferService {

    RideOfferResponse createRideOffer(RideOfferCreateRequest request);

    RideOfferResponse getRideOfferById(Long id);

    RideOffer getEntityById(Long id);

    List<RideOfferResponse> searchRides(String origin, String destination, LocalDateTime startTime, LocalDateTime endTime, Integer minSeats);

    Page<RideOfferResponse> searchRidesPaginated(String origin, String destination, LocalDateTime startTime, LocalDateTime endTime, Integer minSeats, Pageable pageable);

    List<RideOfferResponse> getRideOffersByDriver(Long driverId);

    RideOfferResponse updateRideOfferStatus(Long offerId, Long driverId, RideOfferStatus status);
}
