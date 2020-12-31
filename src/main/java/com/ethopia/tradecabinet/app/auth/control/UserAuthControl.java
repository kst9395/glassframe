package com.ethopia.tradecabinet.app.auth.control;

import com.ethopia.tradecabinet.app.auth.dto.RegisterUserInput;
import com.ethopia.tradecabinet.app.auth.dto.RegisterUserResponse;
import com.ethopia.tradecabinet.app.common.ErrorMessage;
import io.reactivex.Maybe;

import java.util.List;

public interface UserAuthControl {

    Maybe<Boolean> checkRegistrationEligibility(RegisterUserInput registerUserInput);

    Maybe<RegisterUserResponse> registerUser(RegisterUserInput input);


}
