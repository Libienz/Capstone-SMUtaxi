package com.capstone.smutaxi.exception.auth;

public class TokenInvalidFormException extends RuntimeException{
    public TokenInvalidFormException() {
        super("Invalid token format");
    }

    public TokenInvalidFormException(Throwable cause) {
        super("Invalid token format", cause);
    }
}
