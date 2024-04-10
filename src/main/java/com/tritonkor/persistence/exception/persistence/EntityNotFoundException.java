package com.tritonkor.persistence.exception.persistence;

public class EntityNotFoundException extends PersistenceException {
    public EntityNotFoundException() {
        super();
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
