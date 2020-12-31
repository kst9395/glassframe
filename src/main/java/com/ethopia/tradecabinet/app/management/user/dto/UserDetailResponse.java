package com.ethopia.tradecabinet.app.management.user.dto;

import com.ethopia.tradecabinet.model.AppUser;

public class UserDetailResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static UserDetailResponse from(AppUser appUser) {
        UserDetailResponse response = new UserDetailResponse();
        response.setEmail(appUser.getEmail());
        response.setFirstName(appUser.getFirstName());
        response.setLastName(appUser.getLastName());
        response.setUsername(appUser.getUsername());
        return response;

    }
}
