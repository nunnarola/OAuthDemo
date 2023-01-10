package com.narola.exception;

public class OAuthException extends RuntimeException{
    public OAuthException(String message) {
        super(message);
    }
    public OAuthException(String message, Throwable ex) {
        super(message, ex);
    }
}
