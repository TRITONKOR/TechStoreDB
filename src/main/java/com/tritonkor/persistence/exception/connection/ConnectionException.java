package com.tritonkor.persistence.exception.connection;

import java.sql.SQLException;

public class ConnectionException extends SQLException {
    public ConnectionException(String reason) {
        super(reason);
    }
}
