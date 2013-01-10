/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.dspace.springui.session.SessionCommitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Used to commit or rollback the current session.
 * With this is possible to use the same session even
 * at the view.
 * 
 * @author João Melo <jmelo@lyncode.com>
 */
public class HibernateSessionFilter implements Filter {
	@Autowired ApplicationContext context;
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		chain.doFilter(request, response);
		// after all!
		context.getBean(SessionCommitter.class).run();
	}

	@Override
	public void destroy() {
		
	}

}
