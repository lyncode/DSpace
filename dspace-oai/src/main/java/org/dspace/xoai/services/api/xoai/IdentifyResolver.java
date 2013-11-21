package org.dspace.xoai.services.api.xoai;

import com.lyncode.xoai.dataprovider.services.api.RepositoryConfiguration;
import org.dspace.xoai.services.api.context.ContextServiceException;

public interface IdentifyResolver {
    RepositoryConfiguration getIdentify() throws ContextServiceException;
}
