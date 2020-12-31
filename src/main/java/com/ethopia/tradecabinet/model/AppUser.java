package com.ethopia.tradecabinet.model;

import com.ethopia.tradecabinet.app.auth.dto.RegisterUserInput;
import com.ethopia.tradecabinet.model.auto._AppUser;

public class AppUser extends _AppUser {

    private static final long serialVersionUID = 1L;

    public void populate(RegisterUserInput userInput, String hash) {
        this.setEmail(userInput.getEmail());
        this.setFirstName(userInput.getFirstName());
        this.setUsername(userInput.getUsername());
        this.setLastName(userInput.getLastName());
        this.setHash(hash);

    }

}
