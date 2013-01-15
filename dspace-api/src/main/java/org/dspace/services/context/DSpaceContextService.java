/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.services.context;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dspace.core.DSpaceContext;
import org.dspace.orm.dao.api.IEpersonDao;
import org.dspace.services.ContextService;
import org.dspace.services.RequestService;
import org.dspace.services.model.Request;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class implements a DSpace Context Service.
 * 
 * @author João Melo <jmelo@lyncode.com>
 */
public class DSpaceContextService implements ContextService {
	private static Logger log = LogManager.getLogger(DSpaceContextService.class);
	private static final String CONTEXT_ATTR = "dspace.context";
	
	private SessionFactory sessionFactory;
	@Autowired(required=false) RequestService requestService;
	@Autowired(required=false) IEpersonDao epersonDao;

	@Override
	public DSpaceContext newContext() {
		log.debug("New session created");
		Session session = sessionFactory.getCurrentSession();
		if (session == null) session = sessionFactory.openSession();
		if (!session.isOpen() || session.getTransaction() == null || !session.getTransaction().isActive())
			session.beginTransaction();
		DSpaceContext ctx = new DSpaceContext(session);

		if (requestService != null) {
			Request r = requestService.getCurrentRequest();
			if (r != null)  {// There is one request running on this thread!
				String userID = r.getSession().getUserId();
				if (userID != null && epersonDao != null)  
					ctx.setCurrentEperson(epersonDao.selectById(Integer.parseInt(userID)));
				r.setAttribute(CONTEXT_ATTR, ctx);
			}
		}
		return ctx;
	}
	
	@Override
	public DSpaceContext getContext() {
		if (requestService != null) {
			Request r = requestService.getCurrentRequest();
			if (r != null) {// There is one request running on this thread!
				DSpaceContext ctx = (DSpaceContext) r.getAttribute(CONTEXT_ATTR);
				if (ctx != null) return ctx;
			}
		}
		return newContext();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
