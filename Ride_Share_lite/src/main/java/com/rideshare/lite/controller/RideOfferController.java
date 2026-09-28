package com.rideshare.lite.controller;

import com.rideshare.lite.dto.RideOfferCreateRequest;
import com.rideshare.lite.dto.RideOfferResponse;
import com.rideshare.lite.model.RideOfferStatus;
import com.rideshare.lite.service.RideOfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rides")
@Tag(name = "Ride Offers", description = "APIs for drivers publishing rides, searching matching rides, and managing ride offers")
public class RideOfferController {

    private final RideOfferService rideOfferService;

    public RideOfferController(RideOfferService rideOfferService) {
        this.rideOfferService = rideOfferService;
    }

    @PostMapping
    @Operation(summary = "Publish a ride offer", description = "Core Feature 1: Driver publishes a route with origin, destination, departure time, and available seats.")
    public ResponseEntity<RideOfferResponse> createRideOffer(@Valid @RequestBody RideOfferCreateRequest request) {
        RideOfferResponse createdOffer = rideOfferService.createRideOffer(request);
        return new ResponseEntity<>(createdOffer, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for matching rides", description = "Core Feature 2: Rider searches for matching rides by route (origin, destination) and time window.")
    public ResponseEntity<List<RideOfferResponse>> searchRides(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false, defaultValue = "1") Integer minSeats
    ) {
        List<RideOfferResponse> matchingRides = rideOfferService.searchRides(origin, destination, startTime, endTime, minSeats);
        return ResponseEntity.ok(matchingRides);
    }

    @GetMapping("/search/page")
    @Operation(summary = "Search matching rides with pagination and sorting")
    public ResponseEntity<Page<RideOfferResponse>> searchRidesPaginated(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false, defaultValue = "1") Integer minSeats,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "departureTime") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<RideOfferResponse> pagedRides = rideOfferService.searchRidesPaginated(origin, destination, startTime, endTime, minSeats, pageable);
        return ResponseEntity.ok(pagedRides);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ride offer details by ID")
    public ResponseEntity<RideOfferResponse> getRideOfferById(@PathVariable Long id) {
        RideOfferResponse rideOffer = rideOfferService.getRideOfferById(id);
        return ResponseEntity.ok(rideOffer);
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get all rides published by a specific driver")
    public ResponseEntity<List<RideOfferResponse>> getRideOffersByDriver(@PathVariable Long driverId) {
        List<RideOfferResponse> rides = rideOfferService.getRideOffersByDriver(driverId);
        return ResponseEntity.ok(rides);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update ride offer status", description = "Driver updates ride status (PLANNED, IN_PROGRESS, COMPLETED, CANCELLED).")
    public ResponseEntity<RideOfferResponse> updateRideOfferStatus(
            @PathVariable Long id,
            @RequestParam Long driverId,
            @RequestParam RideOfferStatus status
    ) {
        RideOfferResponse updatedOffer = rideOfferService.updateRideOfferStatus(id, driverId, status);
        return ResponseEntity.ok(updatedOffer);
    }
}
