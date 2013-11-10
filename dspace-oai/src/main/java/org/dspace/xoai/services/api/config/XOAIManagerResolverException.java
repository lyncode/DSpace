package org.dspace.xoai.services.api.config;

public class XOAIManagerResolverException extends Exception {
    public XOAIManagerResolverException() {}

    public XOAIManagerResolverException(String message) {
        super(message);
    }

    public XOAIManagerResolverException(String message, Throwable cause) {
        super(message, cause);
    }

    public XOAIManagerResolverException(Throwable cause) {
        super(cause);
    }

    public XOAIManagerResolverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
