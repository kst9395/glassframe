package com.ethopia.tradecabinet.app.web;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class PebbleHttpResponse {
    private HttpServerResponse delegate;
    private RoutingContext routingContext;

    public PebbleHttpResponse(RoutingContext routingContext) {
        this.routingContext = routingContext;
        this.delegate = routingContext.response();
    }

    public PebbleHttpResponse ok(Buffer result) {
        this.delegate.setStatusCode(200).putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.TEXT_HTML)
                .end(result);
        return this;
    }

    public PebbleHttpResponse error(Throwable throwable) {
        this.delegate.setStatusCode(500).putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.TEXT_HTML)
                .end(ExceptionUtils.getStackTrace(throwable));
        return this;
    }


}
