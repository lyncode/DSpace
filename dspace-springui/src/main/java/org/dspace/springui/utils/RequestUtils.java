/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dspace.springui.wrappers.EPersonWrapper;

public class RequestUtils {
    private static final String USER_KEY = "user.current";
    
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
}
