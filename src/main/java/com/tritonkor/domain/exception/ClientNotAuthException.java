package com.tritonkor.domain.exception;

public class ClientNotAuthException extends RuntimeException{
    /**
     * Constructs a new ClientAlreadyAuthException with the specified error message.
     *
     * @param message the detail message (which is saved for later retrieval by the
     *                {@link #getMessage()} method).
     */
    public ClientNotAuthException(String message) {
        super(message);
    }
}
