/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@DependsOn("dspaceServices")
public class SearchController {
    @RequestMapping(value = "/search/{query}")
    public String searchAction(@PathVariable("query") String query,
            HttpServletRequest request) {
        // TODO Search

        return "search/index";
    }
}
