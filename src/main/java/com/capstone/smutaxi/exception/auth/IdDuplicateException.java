package com.capstone.smutaxi.exception.auth;

public class IdDuplicateException extends RuntimeException {
    public IdDuplicateException(String message) {
        super(message);
    }

    public IdDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
