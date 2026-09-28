package com.example.finance.common.api;

import java.util.Map;

public class ApiError {
    int status;
    String message;
    String code;
    Map<String,String> fieldErrors;

    public ApiError(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public ApiError(int status, String code, String message,
                    Map<String, String> fieldErrors) {
        this(status, code, message);
        this.fieldErrors = fieldErrors;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
