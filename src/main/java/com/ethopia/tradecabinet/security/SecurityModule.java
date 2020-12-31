package com.ethopia.tradecabinet.security;

import com.google.inject.AbstractModule;

public class SecurityModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PasswordEncoder.class).to(PBKDF2PasswordEncoder.class);
        bind(RxPasswordEncoder.class).to(RxPasswordEncoderImpl.class);
    }
}
