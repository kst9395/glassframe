package com.ethopia.tradecabinet.app.common;

public class ApiException extends RuntimeException {
    private String code;

    private String message;

    public ApiException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiException(Errors errors) {
        this.code = errors.code();
        this.message = errors.message();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
