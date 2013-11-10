package org.dspace.xoai.services.api.config;

import com.lyncode.xoai.dataprovider.data.AbstractItemRepository;
import org.dspace.xoai.services.api.context.ContextServiceException;

import javax.servlet.http.HttpServletRequest;

public interface XOAIItemRepositoryResolver {
    AbstractItemRepository getItemRepository (HttpServletRequest request) throws ContextServiceException;
}
