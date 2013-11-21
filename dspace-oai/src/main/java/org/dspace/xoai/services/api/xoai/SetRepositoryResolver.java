package org.dspace.xoai.services.api.xoai;

import com.lyncode.xoai.dataprovider.services.api.SetRepository;
import org.dspace.xoai.services.api.context.ContextServiceException;

public interface SetRepositoryResolver {
    SetRepository getSetRepository () throws ContextServiceException;
}
