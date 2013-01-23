/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.orm.dao.database;

import java.util.List;

import org.dspace.orm.dao.api.ICollectionDao;
import org.dspace.orm.entity.Collection;
import org.dspace.orm.entity.Community;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author João Melo <jmelo@lyncode.com>
 * @author Miguel Pinto <mpinto@lyncode.com>
 */
@Transactional
@Repository("org.dspace.orm.dao.api.ICollectionDao")
public class CollectionDao extends DSpaceDao<Collection> implements ICollectionDao {
//    private static Logger log = LogManager.getLogger(CollectionDao.class);

    public CollectionDao () {
    	super(Collection.class);
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public List<Collection> selectLastCollection(Community community, int max) {
		return (List<Collection>) super.getSession().createCriteria(Community.class)
				.add(Restrictions.eq("owningCollection", community))
				.addOrder(Order.desc("lastModified"))
				.setMaxResults(max)
				.list();
	}
}
