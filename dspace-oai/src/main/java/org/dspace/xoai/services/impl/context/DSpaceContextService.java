package org.dspace.xoai.services.impl.context;

import org.dspace.core.Context;
import org.dspace.xoai.services.api.context.ContextService;
import org.dspace.xoai.services.api.context.ContextServiceException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public class DSpaceContextService implements ContextService {
    private static final String OAI_CONTEXT = "OAI_CONTEXT";

    @Override
    public Context getContext(HttpServletRequest request) throws ContextServiceException {
        Object value = request.getAttribute(OAI_CONTEXT);
        if (value == null || !(value instanceof Context)) {
            try {
                request.setAttribute(OAI_CONTEXT, new Context());
            } catch (SQLException e) {
                throw new ContextServiceException(e);
            }
        }
        return (Context) request.getAttribute(OAI_CONTEXT);
    }
}
