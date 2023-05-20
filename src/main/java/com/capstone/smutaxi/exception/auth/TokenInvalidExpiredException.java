package com.capstone.smutaxi.exception.auth;

public class TokenInvalidExpiredException extends RuntimeException{

    public TokenInvalidExpiredException() {
        super("Token has expired");
    }

    public TokenInvalidExpiredException(Throwable cause) {
        super("Token has expired", cause);
    }
}
