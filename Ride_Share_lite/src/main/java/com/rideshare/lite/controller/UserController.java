package com.rideshare.lite.controller;

import com.rideshare.lite.dto.UserCreateRequest;
import com.rideshare.lite.dto.UserResponse;
import com.rideshare.lite.dto.UserRideHistoryResponse;
import com.rideshare.lite.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for user registration, user profile, and user ride history")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Register a new user", description = "Creates a new user profile with name, email, and phone number.")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse createdUser = userService.createUser(request);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user details by ID")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @Operation(summary = "List all registered users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}/history")
    @Operation(summary = "View user ride history", description = "Core Feature 5: View a user's ride history as both driver (published offers) and rider (seat requests).")
    public ResponseEntity<UserRideHistoryResponse> getUserRideHistory(@PathVariable Long id) {
        UserRideHistoryResponse history = userService.getUserRideHistory(id);
        return ResponseEntity.ok(history);
    }
}
