package com.ethopia.tradecabinet.app.common;

import io.vertx.reactivex.ext.web.RoutingContext;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Optional;

public class ParameterUtils {
    private RoutingContext routingContext;

    public ParameterUtils(RoutingContext routingContext) {
        this.routingContext = routingContext;
    }

    public Optional<Integer> optionalInteger(String name) {
        String value = routingContext.request().getParam(name);
        if (!NumberUtils.isParsable(value)) {
            return Optional.empty();
        }
        return Optional.of(NumberUtils.createInteger(value));
    }

    public Optional<String> optionalString(String name) {
        return Optional.ofNullable(routingContext.request().getParam(name));
    }


}
