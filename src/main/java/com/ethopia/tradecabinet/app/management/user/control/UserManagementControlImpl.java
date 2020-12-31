package com.ethopia.tradecabinet.app.management.user.control;

import com.ethopia.tradecabinet.app.auth.repository.UserRepository;
import com.ethopia.tradecabinet.app.management.user.dto.UserDetailResponse;
import com.google.inject.Inject;
import io.reactivex.Maybe;

public class UserManagementControlImpl implements UserManagementControl {


    private UserRepository userRepository;

    @Inject
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Maybe<UserDetailResponse> userDetailByUsername(String username) {
        return userRepository.retrieveAppUserByUsername(username).map(UserDetailResponse::from);
    }
}
