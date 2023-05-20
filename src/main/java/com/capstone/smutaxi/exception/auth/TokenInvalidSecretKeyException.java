package com.capstone.smutaxi.exception.auth;

public class TokenInvalidSecretKeyException extends  RuntimeException{
    public TokenInvalidSecretKeyException(String token) {
        super("Invalid secret key for token: " + token);
    }

    public TokenInvalidSecretKeyException(String token, Throwable cause) {
        super("Invalid secret key for token: " + token, cause);
    }
}
