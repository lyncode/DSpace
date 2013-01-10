/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.springui.session;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The session committer deals with the session committing and rollback
 * 
 * @author João Melo <jmelo@lyncode.com>
 */
public class SessionCommitter {
	@Autowired SessionFactory session;
	private boolean commit = false;
	
	public boolean isCommit() {
		return commit;
	}
	public void setCommit(boolean commit) {
		this.commit = commit;
	}
	
	public void run () {
		if (session.getCurrentSession() != null &&
				session.getCurrentSession().isOpen() && 
				session.getCurrentSession().getTransaction() != null &&
				session.getCurrentSession().getTransaction().isActive()) {
			if (this.isCommit())
				session.getCurrentSession().getTransaction().commit();
			else
				session.getCurrentSession().getTransaction().rollback();
		}
	}
}
