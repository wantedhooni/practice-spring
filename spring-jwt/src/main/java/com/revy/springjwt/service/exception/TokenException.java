package com.revy.springjwt.service.exception;

public class TokenException extends RuntimeException{

    public TokenException(String message) {
        super(message);
    }


    public TokenException(String token, String message) {
        super("Failed for [%s]: %s".formatted(token, message));
    }
}
