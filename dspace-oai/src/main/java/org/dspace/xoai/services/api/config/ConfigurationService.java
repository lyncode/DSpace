package org.dspace.xoai.services.api.config;

public interface ConfigurationService {

    public String getProperty (String module, String key);
    boolean getBooleanProperty(String module, String key, boolean defaultValue);
}
