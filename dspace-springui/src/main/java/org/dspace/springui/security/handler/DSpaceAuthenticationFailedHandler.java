/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * The DSpace failure handler
 * 
 * @author João Melo <jmelo@lyncode.com>
 */
public class DSpaceAuthenticationFailedHandler implements AuthenticationFailureHandler {
	private String redirectUrl;
	
	public DSpaceAuthenticationFailedHandler() {}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		
		response.sendRedirect(request.getContextPath() + redirectUrl + "?exception=" + exception.getClass().getSimpleName());
	}

	public void setRedirectUrl (String redir) {
		redirectUrl = redir;
	}
}
