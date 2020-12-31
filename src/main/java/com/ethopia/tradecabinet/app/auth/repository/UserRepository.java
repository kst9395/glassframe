package com.ethopia.tradecabinet.app.auth.repository;

import com.ethopia.tradecabinet.app.auth.dto.RegisterUserInput;
import com.ethopia.tradecabinet.model.AppUser;
import com.ethopia.tradecabinet.model.AppUserGroup;
import io.reactivex.Maybe;

import java.util.List;

public interface UserRepository {

    Maybe<Boolean> existsByUsername(String username);

    Maybe<Boolean> existsByEmail(String email);

    Maybe<AppUser> createAppUser(RegisterUserInput appUser, String hashed);

    Maybe<AppUser> retrieveAppUserByUsername(String username);

    Maybe<List<AppUser>> retrieveAppUserWithoutGroup();



}
