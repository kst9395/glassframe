package com.ethopia.tradecabinet.app.auth.dto;

import com.ethopia.tradecabinet.app.common.ErrorMessage;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

public class RegisterUserResponse {
    private boolean success;
    private String errorCode;
    private String errorMsg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


    public static RegisterUserResponse error(List<ErrorMessage> errorMessages) {
        RegisterUserResponse response = new RegisterUserResponse();
        response.setSuccess(false);
        response.setErrorCode(errorMessages.stream().map(ErrorMessage::getErrorCode).collect(Collectors.joining(",")));
        response.setErrorMsg(errorMessages.stream().map(ErrorMessage::getErrorMessage).collect(Collectors.joining("\n")));
        return response;

    }

    public static RegisterUserResponse success() {
        RegisterUserResponse response = new RegisterUserResponse();
        response.success = true;

        return response;
    }

    public static RegisterUserResponse error(Throwable t) {
        RegisterUserResponse response = new RegisterUserResponse();
        response.setSuccess(false);
        response.setErrorCode("500");
        response.setErrorMsg(t.getMessage());
        return response;
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }
}
