/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.utils;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.dspace.core.Context;
import org.dspace.springui.wrappers.EPersonWrapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

public class RequestUtils {
    private static final String USER_KEY = "user.current";
    private static final String DSPACE_CONTEXT_KEY = "dspace.context";

    public static EPersonWrapper getCurrentUser(HttpServletRequest request) {
        if (request.getAttribute(USER_KEY) == null) {
            HttpSession session = request.getSession();
            if (session != null) {
                Object ob = session.getAttribute(USER_KEY);
                if (ob != null && ob instanceof EPersonWrapper) {
                    request.setAttribute(USER_KEY, ob);
                    return (EPersonWrapper) ob;
                } else {
                    EPersonWrapper u = new EPersonWrapper();
                    request.setAttribute(USER_KEY, u);
                    return u;
                }

            } else {
                EPersonWrapper u = new EPersonWrapper();
                request.setAttribute(USER_KEY, u);
                return u;
            }
        } else
            return (EPersonWrapper) request.getAttribute(USER_KEY);
    }

    public static Context getDSpaceContext(HttpServletRequest request) {
        Context c = (Context) request.getAttribute(DSPACE_CONTEXT_KEY);
        if (c == null) {
            try {
                ApplicationContext ctx = RequestContextUtils
                        .getWebApplicationContext(request);
                SessionFactory f = (SessionFactory) ctx
                        .getBean(SessionFactory.class);
                SessionImplementor session = (SessionImplementor) f
                        .openSession();
                c = new Context();
                request.setAttribute(DSPACE_CONTEXT_KEY, c);
            } catch (SQLException e) {
                // Uhhh..
                throw new RuntimeException("Unable to create a DSpace Context",
                        e);
            }
        }
        return c;
    }
}
