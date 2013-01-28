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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Member controller
 * 
 * @author João Melo <jmelo@lyncode.com>
 *
 */
@Controller
@DependsOn("dspaceServices")
public class MemberController {
	@RequestMapping(value="/login")
	public String loginAction (HttpServletRequest req, ModelMap model) {
		if (req.getParameter("exception") != null) {
			String exp = req.getParameter("exception");
			model.addAttribute("error", exp);
		}
		return "member/login";
	}
}
