/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.jtwig;

import org.dspace.core.ConfigurationManager;

import com.lyncode.jtwig.api.PropertyReader;

public class DSpacePropertyReader implements PropertyReader {

    @Override
    public String getString(String key, String defaultValue) {
        String prop = ConfigurationManager.getProperty(key);
        if (prop == null)
            return defaultValue;
        else
            return prop;
    }

    @Override
    public String getString(String module, String key, String defaultValue) {
        String prop = ConfigurationManager.getProperty(module, key);
        if (prop == null)
            return defaultValue;
        else
            return prop;
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return ConfigurationManager.getIntProperty(key, defaultValue);
    }

    @Override
    public int getInt(String module, String key, int defaultValue) {
        return ConfigurationManager.getIntProperty(module, key, defaultValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return ConfigurationManager.getBooleanProperty(key, defaultValue);
    }

    @Override
    public boolean getBoolean(String module, String key, boolean defaultValue) {
        return ConfigurationManager.getBooleanProperty(module, key,
                defaultValue);
    }

}
