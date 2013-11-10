package org.dspace.xoai.tests.helpers.stubs;

import org.dspace.xoai.services.api.config.ConfigurationService;

import java.util.HashMap;
import java.util.Map;

public class StubbedConfigurationService implements ConfigurationService {
    private Map<String, Object> values = new HashMap<String, Object>();

    @Override
    public String getProperty(String module, String key) {
        return (String) values.get(module + "." + key);
    }

    @Override
    public boolean getBooleanProperty(String module, String key, boolean defaultValue) {
        Boolean value = (Boolean) values.get(module + "." + key);
        if (value == null) return defaultValue;
        else return value;
    }
}
