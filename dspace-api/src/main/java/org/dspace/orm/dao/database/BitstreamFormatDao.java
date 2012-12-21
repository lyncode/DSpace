/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.orm.dao.database;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dspace.orm.dao.api.IBitstreamFormatDao;
import org.dspace.orm.entity.BitstreamFormat;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository("IBitstreamFormatDao")
public class BitstreamFormatDao extends DSpaceDao<BitstreamFormat> implements IBitstreamFormatDao {
	private static Logger log = LogManager.getLogger(BitstreamFormatDao.class);
	
    public BitstreamFormatDao() {
		super(BitstreamFormat.class);
	}
}
