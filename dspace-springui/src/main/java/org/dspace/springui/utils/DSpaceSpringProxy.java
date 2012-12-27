/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.utils;

import org.dspace.kernel.ServiceManager;
import org.dspace.servicemanager.config.DSpaceConfigurationService;
import org.dspace.services.ConfigurationService;
import org.dspace.utils.DSpace;

public class DSpaceSpringProxy {
    private static DSpace dspace;
    
    private <T> T getService (Class<T> clazz) {
        if (dspace == null) dspace = new DSpace();
        return dspace.getSingletonService(clazz);
    }
    
    public DSpaceConfigurationService getDSpaceConfigurationService () {
        return (DSpaceConfigurationService) this.getService(ConfigurationService.class);
    }

    public ConfigurationService getConfigurationService () {
        return this.getService(ConfigurationService.class);
    }
    
    public ServiceManager getServiceManager () {
        return dspace.getServiceManager();
    }
}
