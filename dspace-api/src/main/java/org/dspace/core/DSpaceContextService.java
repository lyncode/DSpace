package org.dspace.core;

import org.dspace.services.ContextService;
import org.dspace.services.model.Context;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DSpaceContextService implements ContextService {

	@Autowired SessionFactory sessionFactory;
	
	@Override
	public Context newContext() {
		Session session = sessionFactory.getCurrentSession();
		if (session == null || !session.isOpen()) session = sessionFactory.openSession();
		return new org.dspace.core.Context(session);
	}
	
	@Override
	public Context getContext() {
		return new org.dspace.core.Context(sessionFactory.openSession());
	}
	
}
