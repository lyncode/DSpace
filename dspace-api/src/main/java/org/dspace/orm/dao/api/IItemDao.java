/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.orm.dao.api;

import java.util.List;

import org.dspace.orm.entity.Collection;
import org.dspace.orm.entity.Community;
import org.dspace.orm.entity.Item;

public interface IItemDao extends IDSpaceDao<Item>{
	List<Item> selectLastItems(Collection collection, int max);
	
	List<Item> selectLastItemsFromCommutity(Community community, int max);
}
