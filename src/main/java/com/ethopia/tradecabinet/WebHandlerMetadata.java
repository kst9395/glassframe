package com.ethopia.tradecabinet;

import com.ethopia.tradecabinet.web.WebHandler;
import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;

public class WebHandlerMetadata {
    private Class<? extends Handler<RoutingContext>> impl;
    private WebHandler route;

    public Class<? extends Handler> getImpl() {
        return impl;
    }

    public void setImpl(Class impl) {
        this.impl = impl;
    }

    public WebHandler getRoute() {
        return route;
    }

    public void setRoute(WebHandler route) {
        this.route = route;
    }
}
