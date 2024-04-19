package com.tritonkor.domain.exception;

import java.util.List;

public class ValidationException extends RuntimeException{
    public ValidationException(List<String> validationMessages) {
        super(String.join("\n", validationMessages));
    }
}
