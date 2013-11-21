package org.dspace.xoai.services.api.database;

public class DatabaseQueryException extends Exception {
    public DatabaseQueryException() {
    }

    public DatabaseQueryException(String message) {
        super(message);
    }

    public DatabaseQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseQueryException(Throwable cause) {
        super(cause);
    }
}
