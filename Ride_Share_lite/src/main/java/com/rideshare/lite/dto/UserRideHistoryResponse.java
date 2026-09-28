package com.rideshare.lite.dto;

import java.util.List;

public class UserRideHistoryResponse {

    private Long userId;
    private String userName;
    private String userEmail;
    private List<RideOfferResponse> driverOffers;
    private List<RideRequestResponse> riderRequests;

    public UserRideHistoryResponse() {
    }

    public UserRideHistoryResponse(Long userId, String userName, String userEmail,
                                   List<RideOfferResponse> driverOffers,
                                   List<RideRequestResponse> riderRequests) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.driverOffers = driverOffers;
        this.riderRequests = riderRequests;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<RideOfferResponse> getDriverOffers() {
        return driverOffers;
    }

    public void setDriverOffers(List<RideOfferResponse> driverOffers) {
        this.driverOffers = driverOffers;
    }

    public List<RideRequestResponse> getRiderRequests() {
        return riderRequests;
    }

    public void setRiderRequests(List<RideRequestResponse> riderRequests) {
        this.riderRequests = riderRequests;
    }
}
