/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.orm.dao.database;

import org.dspace.orm.dao.api.IEpersonDao;

import org.dspace.orm.entity.Eperson;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("IItemDao")
public class EpersonDao extends DSpaceDao<Eperson> implements IEpersonDao {
    
	public EpersonDao(Class<Eperson> clazz) {
		super(clazz);
		// TODO Auto-generated constructor stub
	}
}
