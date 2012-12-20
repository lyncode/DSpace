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
import org.dspace.orm.dao.api.IBitstreamDao;
import org.dspace.orm.entity.Bitstream;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("IBitstreamDao")
public class BitstreamDao implements IBitstreamDao {
    private static Logger log = LogManager.getLogger(BitstreamDao.class);

    @Autowired
    SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    public int save(Bitstream c) {
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
    public Bitstream selectById(int id) {
        return (Bitstream) getSession().get(Bitstream.class, id);
    }

    @Override
    public boolean delete(Bitstream c) {
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
    public List<Bitstream> selectAll() {
        return (List<Bitstream>) getSession().createCriteria(Bitstream.class)
                .list();
    }

}
