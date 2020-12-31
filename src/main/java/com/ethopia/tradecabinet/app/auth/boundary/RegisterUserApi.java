package com.ethopia.tradecabinet.app.auth.boundary;

import com.ethopia.tradecabinet.app.auth.control.UserAuthControl;
import com.ethopia.tradecabinet.app.auth.dto.RegisterUserInput;
import com.ethopia.tradecabinet.app.common.ErrorMessage;
import com.ethopia.tradecabinet.app.web.JsonHttpResponse;
import com.ethopia.tradecabinet.web.WebHandler;
import com.google.inject.Inject;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.RoutingContext;

import java.util.List;

@WebHandler(path = "/tradecabinet/api/users/register", name = "RegisterUserApi", method = HttpMethod.POST)
public class RegisterUserApi implements Handler<RoutingContext> {

    private UserAuthControl userAuthControl;

    @Inject
    public void setUserAuthControl(UserAuthControl userAuthControl) {
        this.userAuthControl = userAuthControl;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        final JsonHttpResponse response = new JsonHttpResponse(routingContext);
        final JsonObject jsonObject = routingContext.getBodyAsJson();
        final RegisterUserInput registerUserInput = jsonObject.mapTo(RegisterUserInput.class);
        registerUserInput.normalize();
        final List<ErrorMessage> errorMessages = registerUserInput.validate();

        if (!errorMessages.isEmpty()) {
            response.error(errorMessages.get(0));
        } else {
            userAuthControl.checkRegistrationEligibility(registerUserInput)
                    .flatMap(eligibilityErrorMessages -> userAuthControl.registerUser(registerUserInput))
                    .doOnSuccess(registerUserResponse -> response.ok(registerUserResponse))
                    .doOnError(throwable -> response.error(throwable))
                    .subscribe();
        }

    }
}
