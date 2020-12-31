package com.ethopia.tradecabinet.app.common;

public enum Errors {
    API_INPUT_MISSING_REQUIRED_FIELD_ERROR("1001", "Field(s) %s is required."),
    API_USER_REGISTRATION_USERNAME_OR_EMAIL_USED_ERROR("2001", "Username or email already used."),
    API_USER_MANAGEMENT_USER_NOT_FOUND_ERROR("2101","User not found");

    private String errorCode;
    private String errorMsg;

    Errors(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String code() {
        return errorCode;
    }

    public String message() {
        return errorMsg;
    }

}