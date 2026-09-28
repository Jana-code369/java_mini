package com.rideshare.lite.service;

import com.rideshare.lite.dto.UserCreateRequest;
import com.rideshare.lite.dto.UserResponse;
import com.rideshare.lite.dto.UserRideHistoryResponse;
import com.rideshare.lite.model.User;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserCreateRequest request);

    UserResponse getUserById(Long id);

    User getEntityById(Long id);

    List<UserResponse> getAllUsers();

    UserRideHistoryResponse getUserRideHistory(Long userId);
}
