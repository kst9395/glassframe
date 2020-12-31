package com.ethopia.tradecabinet.app.auth.control;

import com.ethopia.tradecabinet.app.auth.dto.RegisterUserInput;
import com.ethopia.tradecabinet.app.auth.dto.RegisterUserResponse;
import com.ethopia.tradecabinet.app.auth.repository.UserRepository;
import com.ethopia.tradecabinet.app.common.ApiException;
import com.ethopia.tradecabinet.app.common.ErrorMessage;
import com.ethopia.tradecabinet.app.common.Errors;
import com.ethopia.tradecabinet.security.RxPasswordEncoder;
import com.google.inject.Inject;
import io.reactivex.Maybe;

import java.util.ArrayList;
import java.util.List;

public class UserAuthControlImpl implements UserAuthControl {

    private UserRepository userRepository;
    private RxPasswordEncoder passwordEncoder;

    @Inject
    public void setPasswordEncoder(RxPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Inject
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Maybe<Boolean> checkRegistrationEligibility(RegisterUserInput registerUserInput) {

        return userRepository.existsByUsername(registerUserInput.getUsername())
                .zipWith(userRepository.existsByEmail(registerUserInput.getEmail()), (usernameUsed, emailUsed) -> {
                    if (usernameUsed || emailUsed) {
                        throw new ApiException(Errors.API_USER_REGISTRATION_USERNAME_OR_EMAIL_USED_ERROR);
                    }
                    return true;
                });

    }

    @Override
    public Maybe<RegisterUserResponse> registerUser(RegisterUserInput input) {
        return passwordEncoder.encodePassword(input.getPassword())
                .flatMap(hash ->
                        userRepository.createAppUser(input, hash)
                )
                .flatMap(appUser -> Maybe.just(RegisterUserResponse.success()));
    }
}
