package org.dspace.xoai.services.api.database;


public class HandleResolverException extends Exception {
    public HandleResolverException() {
    }

    public HandleResolverException(String message) {
        super(message);
    }

    public HandleResolverException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandleResolverException(Throwable cause) {
        super(cause);
    }
}
