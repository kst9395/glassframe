package com.ethopia.tradecabinet.security;

import io.reactivex.Maybe;

public interface RxPasswordEncoder {
    Maybe<String> encodePassword(String password);


    Maybe<Boolean> matchPassword(String password, String goodHash);

}
