package com.ethopia.tradecabinet.app.auth;

import com.ethopia.tradecabinet.app.auth.control.UserAuthControl;
import com.ethopia.tradecabinet.app.auth.control.UserAuthControlImpl;
import com.ethopia.tradecabinet.app.auth.repository.UserRepository;
import com.ethopia.tradecabinet.app.auth.repository.UserRepositoryImpl;
import com.google.inject.AbstractModule;

public class AuthModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserAuthControl.class).to(UserAuthControlImpl.class);
        bind(UserRepository.class).to(UserRepositoryImpl.class);
    }
}
