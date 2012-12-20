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
import org.dspace.orm.dao.api.IBitstreamFormatDao;
import org.dspace.orm.entity.BitstreamFormat;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("IBitstreamFormatDao")
public class BitstreamFormatDao implements IBitstreamFormatDao {
    private static Logger log = LogManager.getLogger(BitstreamFormatDao.class);

    @Autowired
    SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    public int save(BitstreamFormat c) {
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
    public BitstreamFormat selectById(int id) {
        return (BitstreamFormat) getSession().get(BitstreamFormat.class, id);
    }

    @Override
    public boolean delete(BitstreamFormat c) {
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
    public List<BitstreamFormat> selectAll() {
        return (List<BitstreamFormat>) getSession().createCriteria(
                BitstreamFormat.class).list();
    }

}
