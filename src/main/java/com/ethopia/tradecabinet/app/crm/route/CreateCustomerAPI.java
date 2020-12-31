package com.ethopia.tradecabinet.app.crm.route;

import com.ethopia.tradecabinet.web.WebHandler;
import com.google.inject.Inject;
import io.reactivex.functions.Consumer;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.RoutingContext;


@WebHandler(name = "CreateCustomerAPI", method = HttpMethod.POST, path = "/tradecabinet/crm/customers")
public class CreateCustomerAPI implements Handler<RoutingContext> {
    private Vertx vertx;
    private static final Logger logger = LoggerFactory.getLogger(CreateCustomerAPI.class.getName());

    @Inject
    public CreateCustomerAPI(Vertx vertx) {
        this.vertx = vertx;
    }

    private Consumer<Throwable> handleError(HttpServerResponse response) {
        return t -> {
            if (!response.ended()) {
                response.setStatusCode(500);
                response.end();
            }
        };
    }

    @Override
    public void handle(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        try {
            response.setStatusCode(200).end();
        } catch (Exception e) {
            if (!response.ended()) {
                response.setStatusCode(500);
                response.setStatusMessage(e.getLocalizedMessage());
                response.end();
            }
        }

    }
}
