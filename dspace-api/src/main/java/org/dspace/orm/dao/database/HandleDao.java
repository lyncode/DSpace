/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.orm.dao.database;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dspace.orm.dao.api.IHandleDao;
import org.dspace.orm.entity.Handle;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("IHandleDao")
public class HandleDao implements IHandleDao {
    private static Logger log = LogManager.getLogger(HandleDao.class);

    @Autowired
    SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    public int save(Handle c) {
        Session session = getSession();
        Transaction tx = null;
        Integer employeeID = null;
        try {
            tx = session.beginTransaction();
            employeeID = (Integer) session.save(c);
            tx.commit();
            log.debug(c.getClass().getSimpleName() + " saved");
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employeeID;
    }

    @Override
    public Handle selectById(int id) {
        return (Handle) getSession().get(Handle.class, id);
    }

    @Override
    public boolean delete(Handle c) {
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
    public List<Handle> selectAll() {
        return (List<Handle>) getSession().createCriteria(Handle.class).list();
    }

    @Override
    public Handle selectByResourceId(int resourseType, int id) {
        List<?> l = getSession()
                .createCriteria(Handle.class)
                .add(Restrictions.and(
                        Restrictions.eq("resourceType", resourseType),
                        Restrictions.eq("resourceId", id))).list();

        if (l == null || l.isEmpty())
            return null;
        return (Handle) l.get(0);
    }

    @Override
    public Handle selectByHandle(String handle) {
        return (Handle) getSession().createCriteria(Handle.class)
                .add(Restrictions.eq("handle", handle)).uniqueResult();
    }

}
