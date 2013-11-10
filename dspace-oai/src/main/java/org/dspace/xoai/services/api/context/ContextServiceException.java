package org.dspace.xoai.services.api.context;

public class ContextServiceException extends Exception {
    public ContextServiceException() {
    }

    public ContextServiceException(String message) {
        super(message);
    }

    public ContextServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContextServiceException(Throwable cause) {
        super(cause);
    }

    public ContextServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
