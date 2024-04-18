package com.tritonkor.domain.exception;

public class SignUpException extends RuntimeException{
    /**
     * Constructs a new SignUpException with the specified error message.
     *
     * @param message the detail message (which is saved for later retrieval by the
     *                {@link #getMessage()} method).
     */
    public SignUpException(String message) {
        super(message);
    }
}
