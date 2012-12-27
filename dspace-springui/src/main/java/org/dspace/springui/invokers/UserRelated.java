/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.invokers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dspace.springui.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRelated {
    @Autowired
    HttpServletRequest request;

    public void getUserInfo(Map<String, Object> model) {
        model.put("user", RequestUtils.getCurrentUser(request));
    }

}
