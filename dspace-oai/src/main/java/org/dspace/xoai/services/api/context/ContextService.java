package org.dspace.xoai.services.api.context;

import org.dspace.core.Context;

import javax.servlet.http.HttpServletRequest;

public interface ContextService {
    Context getContext(HttpServletRequest request) throws ContextServiceException;
}
