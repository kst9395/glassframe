package com.ethopia.tradecabinet.app.common;

public class ErrorMessage {
    private String errorCode;
    private String errorMessage;

    public ErrorMessage(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorMessage(Errors errors) {
        this.errorCode = errors.code();
        this.errorMessage = errors.message();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", this.errorCode, this.errorMessage);
    }
}
