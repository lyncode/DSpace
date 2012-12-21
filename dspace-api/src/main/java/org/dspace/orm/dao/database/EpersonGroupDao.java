/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.orm.dao.database;

import org.dspace.orm.dao.api.IEpersonGroupDao;

import org.dspace.orm.entity.EpersonGroup;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("IEpersonGroup")
public class EpersonGroupDao extends DSpaceDao<EpersonGroup> implements IEpersonGroupDao {
    
	public EpersonGroupDao(Class<EpersonGroup> clazz) {
		super(clazz);
		// TODO Auto-generated constructor stub
	}
}
