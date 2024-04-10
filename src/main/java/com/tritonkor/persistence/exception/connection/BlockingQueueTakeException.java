package com.tritonkor.persistence.exception.connection;

public class BlockingQueueTakeException extends RuntimeException {

    public BlockingQueueTakeException(String reason) {
        super(reason);
    }
}
