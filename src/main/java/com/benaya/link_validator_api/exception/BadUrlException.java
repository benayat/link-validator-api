package com.benaya.link_validator_api.exception;

public class BadUrlException extends RuntimeException{
    public BadUrlException() {
        super("URL wrong url structure, please try again with a valid url");
    }
    public BadUrlException(String message, Throwable cause) {
        super(message, cause);
    }
    public BadUrlException(Exception e) {
        super(e);
    }
}
