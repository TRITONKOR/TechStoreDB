package com.tritonkor.domain.exception;

public class AuthException extends RuntimeException {

    /**
     * Constructs a new AuthException with the default error message. The default message is "Wrong
     * username or password."
     */
    public AuthException() {
        super("Wrong username or password");
    }
}
