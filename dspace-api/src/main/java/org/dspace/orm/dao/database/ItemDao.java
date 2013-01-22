/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.orm.dao.database;

import java.util.List;

import org.dspace.orm.dao.api.IItemDao;

import org.dspace.orm.entity.Collection;
import org.dspace.orm.entity.Item;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author João Melo <jmelo@lyncode.com>
 * @author Miguel Pinto <mpinto@lyncode.com>
 */
@Transactional
@Repository("org.dspace.orm.dao.api.IItemDao")
public class ItemDao extends DSpaceDao<Item> implements IItemDao {
    
	public ItemDao() {
		super(Item.class);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Item> selectLastItems(Collection collection, int max) {
		return (List<Item>) super.getSession().createCriteria(Item.class)
				.add(Restrictions.eq("owningCollection", collection))
				.addOrder(Order.desc("lastModified"))
				.setMaxResults(max)
				.list();
	}
}

