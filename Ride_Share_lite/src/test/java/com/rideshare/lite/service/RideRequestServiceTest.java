package com.rideshare.lite.service;

import com.rideshare.lite.dto.RideRequestCreateRequest;
import com.rideshare.lite.dto.RideRequestResponse;
import com.rideshare.lite.exception.InvalidRideOperationException;
import com.rideshare.lite.exception.SeatUnavailableException;
import com.rideshare.lite.model.RideOffer;
import com.rideshare.lite.model.RideRequest;
import com.rideshare.lite.model.RideRequestStatus;
import com.rideshare.lite.model.User;
import com.rideshare.lite.repository.RideOfferRepository;
import com.rideshare.lite.repository.RideRequestRepository;
import com.rideshare.lite.service.impl.RideRequestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideRequestServiceTest {

    @Mock
    private RideRequestRepository rideRequestRepository;

    @Mock
    private RideOfferRepository rideOfferRepository;

    @Mock
    private RideOfferService rideOfferService;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private RideRequestServiceImpl rideRequestService;

    private User driver;
    private User rider;
    private RideOffer offer;

    @BeforeEach
    void setUp() {
        driver = new User(1L, "Driver Dave", "driver@test.com", "1234567890");
        rider = new User(2L, "Rider Rachel", "rider@test.com", "0987654321");
        offer = new RideOffer(driver, "City A", "City B", LocalDateTime.now().plusDays(1), 2);
        offer.setId(10L);
    }

    @Test
    @DisplayName("Rule Violation: User cannot request a seat on their own published ride")
    void testCreateRideRequest_SelfRequestProhibited() {
        when(rideOfferService.getEntityById(10L)).thenReturn(offer);
        when(userService.getEntityById(1L)).thenReturn(driver); // Driver attempting to request own ride

        RideRequestCreateRequest request = new RideRequestCreateRequest(10L, 1L, 1);

        InvalidRideOperationException exception = assertThrows(
                InvalidRideOperationException.class,
                () -> rideRequestService.createRideRequest(request)
        );

        assertTrue(exception.getMessage().contains("cannot request a seat on your own published ride"));
        verify(rideRequestRepository, never()).save(any());
    }

    @Test
    @DisplayName("Rule Enforcement: Approving request reduces available seats")
    void testApproveRideRequest_ReducesAvailableSeats() {
        RideRequest req = new RideRequest(offer, rider, 1);
        req.setId(100L);

        when(rideRequestRepository.findById(100L)).thenReturn(Optional.of(req));
        when(rideOfferRepository.save(any(RideOffer.class))).thenReturn(offer);
        when(rideRequestRepository.save(any(RideRequest.class))).thenReturn(req);

        RideRequestResponse response = rideRequestService.approveRideRequest(100L, 1L);

        assertNotNull(response);
        assertEquals(1, offer.getAvailableSeats()); // reduced from 2 to 1
        assertEquals(RideRequestStatus.APPROVED, req.getStatus());
        verify(notificationService).notifyRideRequestStatusChanged(req);
    }

    @Test
    @DisplayName("Rule Violation: Cannot approve request if insufficient seats remain")
    void testApproveRideRequest_InsufficientSeats() {
        offer.setAvailableSeats(1);
        RideRequest req = new RideRequest(offer, rider, 2); // Requested 2, only 1 available
        req.setId(101L);

        when(rideRequestRepository.findById(101L)).thenReturn(Optional.of(req));

        SeatUnavailableException exception = assertThrows(
                SeatUnavailableException.class,
                () -> rideRequestService.approveRideRequest(101L, 1L)
        );

        assertTrue(exception.getMessage().contains("Insufficient seats remaining"));
        verify(rideRequestRepository, never()).save(any());
    }
}
