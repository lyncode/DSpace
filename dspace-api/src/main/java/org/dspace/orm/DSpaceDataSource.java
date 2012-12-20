/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.orm;

import org.dspace.core.ConfigurationManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DSpaceDataSource extends DriverManagerDataSource {

    public DSpaceDataSource() {
        super.setDriverClassName(ConfigurationManager.getProperty("db.driver"));
    }

    @Override
    public String getPassword() {
        return ConfigurationManager.getProperty("db.password");
    }

    @Override
    public String getUrl() {
        return ConfigurationManager.getProperty("db.url");
    }

    @Override
    public String getUsername() {
        return ConfigurationManager.getProperty("db.username");
    }

}
