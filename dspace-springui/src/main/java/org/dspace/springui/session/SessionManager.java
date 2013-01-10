/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * SessionCommiter is request scoped, as so, this is just a wrapper
 * to access it.
 * 
 * @author João Melo <jmelo@lyncode.com>
 */
public class SessionManager {
	@Autowired ApplicationContext context;
	
	public void commit () {
		context.getBean(SessionCommitter.class).setCommit(true);
	}
	
	public void rollback () {
		context.getBean(SessionCommitter.class).setCommit(false);
	}
}
