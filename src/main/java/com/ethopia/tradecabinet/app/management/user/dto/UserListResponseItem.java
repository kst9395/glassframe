package com.ethopia.tradecabinet.app.management.user.dto;

import com.ethopia.tradecabinet.model.AppUser;

public class UserListResponseItem {
    private String firstName;
    private String lastName;
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static UserListResponseItem from(AppUser user) {
        UserListResponseItem item = new UserListResponseItem();
        item.setEmail(user.getEmail());
        item.setFirstName(user.getFirstName());
        item.setLastName(user.getLastName());

        return item;

    }
}
