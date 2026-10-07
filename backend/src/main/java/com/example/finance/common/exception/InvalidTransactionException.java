package com.example.finance.common.exception;

public class InvalidTransactionException extends RuntimeException {

    private final String code;

    public InvalidTransactionException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}