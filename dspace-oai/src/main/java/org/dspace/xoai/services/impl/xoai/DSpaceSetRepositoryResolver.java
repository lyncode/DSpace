package org.dspace.xoai.services.impl.xoai;

import com.lyncode.xoai.dataprovider.services.api.SetRepository;
import org.dspace.xoai.services.api.context.ContextService;
import org.dspace.xoai.services.api.context.ContextServiceException;
import org.dspace.xoai.services.api.xoai.SetRepositoryResolver;
import org.springframework.beans.factory.annotation.Autowired;

public class DSpaceSetRepositoryResolver implements SetRepositoryResolver {
    @Autowired
    private ContextService contextService;

    @Override
    public SetRepository getSetRepository() throws ContextServiceException {
        return new DSpaceSetRepository(contextService.getContext());
    }
}
