package org.dspace.xoai.services.impl.xoai;

import com.lyncode.xoai.dataprovider.data.AbstractIdentify;
import org.dspace.xoai.data.DSpaceIdentify;
import org.dspace.xoai.services.api.config.ConfigurationService;
import org.dspace.xoai.services.api.context.ContextService;
import org.dspace.xoai.services.api.context.ContextServiceException;
import org.dspace.xoai.services.api.database.EarliestDateResolver;
import org.dspace.xoai.services.api.database.FieldResolver;
import org.dspace.xoai.services.api.xoai.IdentifyResolver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class DSpaceIdentifyResolver implements IdentifyResolver {
    @Autowired
    private EarliestDateResolver earliestDateResolver;
    @Autowired
    private FieldResolver fieldResolver;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private ContextService contextService;

    @Override
    public AbstractIdentify getIdentify(HttpServletRequest request) throws ContextServiceException {
        return new DSpaceIdentify(earliestDateResolver, fieldResolver, configurationService, contextService.getContext(request), request);
    }
}
