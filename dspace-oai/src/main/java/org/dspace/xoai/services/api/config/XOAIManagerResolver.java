package org.dspace.xoai.services.api.config;

import com.lyncode.xoai.dataprovider.core.XOAIManager;

public interface XOAIManagerResolver {
    XOAIManager getManager () throws XOAIManagerResolverException;
}
