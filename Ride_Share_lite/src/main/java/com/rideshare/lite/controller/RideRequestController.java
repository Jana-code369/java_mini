package com.rideshare.lite.controller;

import com.rideshare.lite.dto.RideRequestCreateRequest;
import com.rideshare.lite.dto.RideRequestResponse;
import com.rideshare.lite.service.RideRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@Tag(name = "Ride Requests", description = "APIs for riders requesting seats, drivers approving/rejecting requests, and managing request lifecycle")
public class RideRequestController {

    private final RideRequestService rideRequestService;

    public RideRequestController(RideRequestService rideRequestService) {
        this.rideRequestService = rideRequestService;
    }

    @PostMapping
    @Operation(summary = "Request a seat on a ride", description = "Core Feature 3: Rider requests a seat on a published ride offer.")
    public ResponseEntity<RideRequestResponse> createRideRequest(@Valid @RequestBody RideRequestCreateRequest request) {
        RideRequestResponse response = rideRequestService.createRideRequest(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve ride request", description = "Core Feature 3 & 4: Driver approves a seat request, reducing available seats.")
    public ResponseEntity<RideRequestResponse> approveRideRequest(
            @PathVariable Long id,
            @RequestParam Long driverId
    ) {
        RideRequestResponse response = rideRequestService.approveRideRequest(id, driverId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject ride request", description = "Core Feature 3: Driver rejects a seat request.")
    public ResponseEntity<RideRequestResponse> rejectRideRequest(
            @PathVariable Long id,
            @RequestParam Long driverId
    ) {
        RideRequestResponse response = rideRequestService.rejectRideRequest(id, driverId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel ride request", description = "Rider cancels their pending or approved seat request.")
    public ResponseEntity<RideRequestResponse> cancelRideRequest(
            @PathVariable Long id,
            @RequestParam Long riderId
    ) {
        RideRequestResponse response = rideRequestService.cancelRideRequest(id, riderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ride request details by ID")
    public ResponseEntity<RideRequestResponse> getRideRequestById(@PathVariable Long id) {
        RideRequestResponse response = rideRequestService.getRideRequestById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rider/{riderId}")
    @Operation(summary = "Get requests submitted by a specific rider")
    public ResponseEntity<List<RideRequestResponse>> getRequestsByRider(@PathVariable Long riderId) {
        List<RideRequestResponse> requests = rideRequestService.getRequestsByRider(riderId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/ride/{rideOfferId}")
    @Operation(summary = "Get requests submitted for a specific ride offer (Driver action)")
    public ResponseEntity<List<RideRequestResponse>> getRequestsForRideOffer(
            @PathVariable Long rideOfferId,
            @RequestParam Long driverId
    ) {
        List<RideRequestResponse> requests = rideRequestService.getRequestsForRideOffer(rideOfferId, driverId);
        return ResponseEntity.ok(requests);
    }
}
