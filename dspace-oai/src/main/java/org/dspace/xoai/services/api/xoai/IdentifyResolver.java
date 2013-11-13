package org.dspace.xoai.services.api.xoai;

import com.lyncode.xoai.dataprovider.data.AbstractIdentify;
import org.dspace.core.Context;
import org.dspace.xoai.services.api.context.ContextServiceException;

import javax.servlet.http.HttpServletRequest;

public interface IdentifyResolver {
    AbstractIdentify getIdentify(HttpServletRequest request) throws ContextServiceException;
}
