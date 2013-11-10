package org.dspace.xoai.services.impl.context;

import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;
import org.dspace.xoai.services.api.config.XOAIManagerResolver;
import org.dspace.xoai.services.api.config.XOAIManagerResolverException;
import org.springframework.beans.factory.annotation.Autowired;

import static com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.readConfiguration;

public class DSpaceXOAIManagerResolver implements XOAIManagerResolver {
    public static final String XOAI_CONFIGURATION_FILE = "xoai.xml";
    @Autowired ResourceResolver resourceResolver;

    private XOAIManager manager;

    @Override
    public XOAIManager getManager() throws XOAIManagerResolverException {
        if (manager == null) {
            try {
                manager = new XOAIManager(resourceResolver, readConfiguration(resourceResolver.getResource(XOAI_CONFIGURATION_FILE)));
            } catch (Exception e) {
                throw new XOAIManagerResolverException(e);
            }
        }
        return manager;
    }
}
