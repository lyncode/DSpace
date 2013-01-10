package org.dspace.springui.utils;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;

public class DSpaceContextLoaderListener extends ContextLoaderListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);
	}
	
}
