package org.dspace.xoai.services.impl.xoai;

import com.lyncode.xoai.dataprovider.data.AbstractSetRepository;
import org.dspace.xoai.data.DSpaceSetRepository;
import org.dspace.xoai.services.api.context.ContextService;
import org.dspace.xoai.services.api.context.ContextServiceException;
import org.dspace.xoai.services.api.xoai.SetRepositoryResolver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class DSpaceSetRepositoryResolver implements SetRepositoryResolver {
    @Autowired
    private ContextService contextService;

    @Override
    public AbstractSetRepository getSetRepository(HttpServletRequest request) throws ContextServiceException {
        return new DSpaceSetRepository(contextService.getContext(request));
    }
}
