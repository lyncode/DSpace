package org.dspace.xoai.services.impl.config;

import org.dspace.core.ConfigurationManager;
import org.dspace.xoai.services.api.config.ConfigurationService;

public class DSpaceConfigurationService implements ConfigurationService {
    @Override
    public String getProperty(String module, String key)  {
        return ConfigurationManager.getProperty(module, key);
    }

    @Override
    public boolean getBooleanProperty(String module, String key, boolean defaultValue) {
        return ConfigurationManager.getBooleanProperty(module, key, defaultValue);
    }
}
