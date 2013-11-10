package org.dspace.xoai.tests.helpers.stubs;

import com.lyncode.xoai.builders.dataprovider.ConfigurationBuilder;
import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;
import org.dspace.xoai.services.api.config.XOAIManagerResolver;
import org.dspace.xoai.services.api.config.XOAIManagerResolverException;
import org.springframework.beans.factory.annotation.Autowired;

public class StubbedXOAIManagerResolver implements XOAIManagerResolver {
    @Autowired ResourceResolver resourceResolver;

    private ConfigurationBuilder builder = new ConfigurationBuilder();

    public ConfigurationBuilder configuration () {
        return builder;
    }

    @Override
    public XOAIManager getManager() throws XOAIManagerResolverException {
        try {
            return new XOAIManager(resourceResolver, builder.build());
        } catch (ConfigurationException e) {
            throw new XOAIManagerResolverException(e);
        }
    }
}
