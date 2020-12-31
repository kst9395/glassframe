package com.ethopia.tradecabinet.app.web;

import com.ethopia.tradecabinet.app.common.ApiException;
import com.ethopia.tradecabinet.app.common.ErrorMessage;
import com.ethopia.tradecabinet.app.common.ResponseWrapper;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.http.HttpServerResponse;
import io.vertx.reactivex.ext.web.RoutingContext;

public class JsonHttpResponse {
    private HttpServerResponse delegate;

    public JsonHttpResponse(RoutingContext routingContext) {
        this.delegate = routingContext.response();
    }

    public JsonHttpResponse ok(Object jsonObject) {
        ResponseWrapper wrapper = new ResponseWrapper();
        wrapper.setData(jsonObject);
        wrapper.setCode("0");
        wrapper.setSuccess(true);
        delegate.setStatusCode(200).putHeader(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                .end(JsonObject.mapFrom(wrapper).encode());
        return this;
    }

    public JsonHttpResponse error(ErrorMessage errorMessage) {
        ResponseWrapper wrapper = new ResponseWrapper();
        wrapper.setCode(errorMessage.getErrorCode());
        wrapper.setMessage(errorMessage.getErrorMessage());
        delegate.setStatusCode(200).putHeader(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()).end(JsonObject.mapFrom(wrapper).encode());
        return this;
    }

    public JsonHttpResponse error(Throwable throwable) {
        ResponseWrapper wrapper = new ResponseWrapper();
        if (throwable instanceof ApiException) {
            ApiException exception = (ApiException) throwable;
            wrapper.setCode(exception.getCode());
        } else {
            wrapper.setCode("999");
        }
        wrapper.setSuccess(false);
        wrapper.setMessage(throwable.getMessage());

        delegate.setStatusCode(200).putHeader(HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString()).end(JsonObject.mapFrom(wrapper).encode());

        return this;
    }
}
