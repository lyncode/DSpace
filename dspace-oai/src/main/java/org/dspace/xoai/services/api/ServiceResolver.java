package org.dspace.xoai.services.api;

public interface ServiceResolver {
    <T> T getService (Class<T> type);
}
