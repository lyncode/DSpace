package org.dspace.xoai.services.api.xoai;

import com.lyncode.xoai.dataprovider.data.AbstractSetRepository;
import org.dspace.core.Context;
import org.dspace.xoai.services.api.context.ContextServiceException;

import javax.servlet.http.HttpServletRequest;

public interface SetRepositoryResolver {
    AbstractSetRepository getSetRepository (HttpServletRequest context) throws ContextServiceException;
}
