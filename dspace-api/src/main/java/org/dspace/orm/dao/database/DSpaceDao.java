package org.dspace.orm.dao.database;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dspace.orm.dao.api.IDSpaceDao;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DSpaceDao<T> implements IDSpaceDao<T> {
	private static Logger log = LogManager.getLogger(DSpaceDao.class);
	
	private Class<T> clazz;
	@Autowired
    SessionFactory sessionFactory;
	
	protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
	
	public DSpaceDao (Class<T> clazz) {
		this.clazz = clazz;
	}
	

    @SuppressWarnings("unchecked")
	@Override
    public T selectById(int id) {
        return (T) getSession().get(clazz, id);
    }
	
	@Override
    public int save(T c) {
        Session session = getSession();
        Transaction tx = null;
        Integer id = null;
        try {
            tx = session.beginTransaction();
            id = (Integer) session.save(c);
            tx.commit();
            log.debug(c.getClass().getSimpleName() + " saved");
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return id;
    }
	
	@Override
    public boolean delete(T c) {
        boolean result = false;
        Session session = getSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(c);
            tx.commit();
            log.debug(c.getClass().getSimpleName() + " deleted");
            result = true;
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }
	
    @SuppressWarnings("unchecked")
    @Override
    public List<T> selectAll() {
        return (List<T>) getSession().createCriteria(
                clazz).list();
    }
}
