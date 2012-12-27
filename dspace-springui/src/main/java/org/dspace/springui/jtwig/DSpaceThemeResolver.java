/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.jtwig;

import org.dspace.core.ConfigurationManager;

import com.lyncode.jtwig.mvc.DefaultThemeResolver;

public class DSpaceThemeResolver extends DefaultThemeResolver {

    @Override
    public String getTheme() {
        String defaultValue = super.getTheme();
        String defined = ConfigurationManager.getProperty("springui", "theme");
        if (defined == null)
            return defaultValue;
        else
            return defined;
    }
}
