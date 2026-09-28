package com.rideshare.lite.service.impl;

import com.rideshare.lite.dto.RideOfferResponse;
import com.rideshare.lite.dto.RideRequestResponse;
import com.rideshare.lite.dto.UserCreateRequest;
import com.rideshare.lite.dto.UserResponse;
import com.rideshare.lite.dto.UserRideHistoryResponse;
import com.rideshare.lite.exception.InvalidRideOperationException;
import com.rideshare.lite.exception.ResourceNotFoundException;
import com.rideshare.lite.model.User;
import com.rideshare.lite.repository.RideOfferRepository;
import com.rideshare.lite.repository.RideRequestRepository;
import com.rideshare.lite.repository.UserRepository;
import com.rideshare.lite.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RideOfferRepository rideOfferRepository;
    private final RideRequestRepository rideRequestRepository;

    public UserServiceImpl(UserRepository userRepository,
                           RideOfferRepository rideOfferRepository,
                           RideRequestRepository rideRequestRepository) {
        this.userRepository = userRepository;
        this.rideOfferRepository = rideOfferRepository;
        this.rideRequestRepository = rideRequestRepository;
    }

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidRideOperationException("User with email '" + request.getEmail() + "' already exists.");
        }
        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPhone(request.getPhone().trim());

        User savedUser = userRepository.save(user);
        return UserResponse.fromEntity(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = getEntityById(id);
        return UserResponse.fromEntity(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserRideHistoryResponse getUserRideHistory(Long userId) {
        User user = getEntityById(userId);

        List<RideOfferResponse> driverOffers = rideOfferRepository.findByDriverId(userId).stream()
                .map(RideOfferResponse::fromEntity)
                .collect(Collectors.toList());

        List<RideRequestResponse> riderRequests = rideRequestRepository.findByRiderId(userId).stream()
                .map(RideRequestResponse::fromEntity)
                .collect(Collectors.toList());

        return new UserRideHistoryResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                driverOffers,
                riderRequests
        );
    }
}
