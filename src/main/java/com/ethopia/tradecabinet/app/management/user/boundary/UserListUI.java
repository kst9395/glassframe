package com.ethopia.tradecabinet.app.management.user.boundary;

import com.ethopia.tradecabinet.app.common.ParameterUtils;
import com.ethopia.tradecabinet.app.management.user.control.UserManagementControl;
import com.ethopia.tradecabinet.app.web.PebbleHttpResponse;
import com.ethopia.tradecabinet.web.WebHandler;
import com.google.inject.Inject;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.common.template.TemplateEngine;

@WebHandler(path = "/tradecabinet/admin/users", name = "UserListUI", method = HttpMethod.GET)
public class UserListUI implements Handler<RoutingContext> {

    private TemplateEngine templateEngine;

    private UserManagementControl userManagementControl;

    private static final int DEFAULT_PAGE_SIZE = 30;


    @Inject
    public void setUserManagementControl(UserManagementControl userManagementControl) {
        this.userManagementControl = userManagementControl;
    }

    @Inject
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


    @Override
    public void handle(RoutingContext routingContext) {
        final ParameterUtils parameters = new ParameterUtils(routingContext);
        final PebbleHttpResponse response = new PebbleHttpResponse(routingContext);
        try {
            int page = parameters.optionalInteger("page").orElse(1);
            int pageSize = parameters.optionalInteger("pageSize").orElse(DEFAULT_PAGE_SIZE);
            userManagementControl.listUsers(page, pageSize)
                    .toSingle()
                    .flatMap(userListResponse -> {
                        routingContext.put("users", userListResponse.getData())
                                .put("totalPage", userListResponse.getTotalPage())
                                .put("page", userListResponse.getPage());
                        return templateEngine.rxRender(routingContext.data(), "webroot/management/users.peb");
                    })
                    .doOnSuccess(response::ok)
                    .doOnError(response::error)
                    .subscribe();

        } catch (Exception e) {
            if (!routingContext.response().ended()) {
                response.error(e);
            }
        }
    }
}
