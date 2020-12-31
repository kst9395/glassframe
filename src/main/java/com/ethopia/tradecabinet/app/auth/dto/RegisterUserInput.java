package com.ethopia.tradecabinet.app.auth.dto;

import com.ethopia.tradecabinet.app.common.ErrorMessage;
import com.ethopia.tradecabinet.app.common.Errors;
import com.ethopia.tradecabinet.app.common.Normalizable;
import com.ethopia.tradecabinet.app.common.Validatable;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RegisterUserInput implements Validatable, Normalizable {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public List<ErrorMessage> validate() {
        List<ErrorMessage> validationError = new ArrayList<>();
        List<String> missingFields = new ArrayList<>();
        if (StringUtils.isEmpty(this.username)) {
            missingFields.add("username");
        }
        if (StringUtils.isEmpty(this.firstName)) {
            missingFields.add("first name");
        }
        if (StringUtils.isEmpty(this.lastName)) {
            missingFields.add("last name");
        }
        if (StringUtils.isEmpty(this.email)) {
            missingFields.add("email");
        }
        if (StringUtils.isEmpty(this.password)) {
            missingFields.add("password");
        }
        if (!missingFields.isEmpty()) {
            String joinedFields = "[" + missingFields.stream().collect(Collectors.joining(",")) + "]";
            validationError.add(new ErrorMessage(Errors.API_INPUT_MISSING_REQUIRED_FIELD_ERROR.code(), String.format(Errors.API_INPUT_MISSING_REQUIRED_FIELD_ERROR.message(), joinedFields)));
        }

        return validationError;
    }

    @Override
    public void normalize() {
        this.email = this.email.trim().toLowerCase();
        this.username = this.username.trim();
        this.firstName = this.firstName.trim();
        this.lastName = this.lastName.trim();
    }
}
