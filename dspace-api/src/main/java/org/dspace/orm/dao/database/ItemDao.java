/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.orm.dao.database;

import org.dspace.orm.dao.api.IItemDao;

import org.dspace.orm.entity.Item;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("IItemDao")
public class ItemDao extends DSpaceDao<Item> implements IItemDao {
    
	public ItemDao() {
		super(Item.class);
	}
}
