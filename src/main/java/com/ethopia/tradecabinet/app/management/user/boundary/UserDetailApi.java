package com.ethopia.tradecabinet.app.management.user.boundary;

import com.ethopia.tradecabinet.app.management.user.control.UserManagementControl;
import com.ethopia.tradecabinet.app.web.JsonHttpResponse;
import com.ethopia.tradecabinet.web.WebHandler;
import com.google.inject.Inject;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;

@WebHandler(name = "UserDetailApi", method = HttpMethod.GET, path = "/tradecabinet/api/management/users/:username")
public class UserDetailApi implements Handler<RoutingContext> {

    private UserManagementControl userManagementControl;

    @Inject
    public void setUserManagementControl(UserManagementControl userManagementControl) {
        this.userManagementControl = userManagementControl;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        JsonHttpResponse response = new JsonHttpResponse(routingContext);
        String username = StringUtils.trim(routingContext.pathParam("username"));
        userManagementControl.userDetailByUsername(username)
                .doOnSuccess(userDetailResponse -> response.ok(userDetailResponse))
                .doOnError(response::error)
                .subscribe();
    }
}
