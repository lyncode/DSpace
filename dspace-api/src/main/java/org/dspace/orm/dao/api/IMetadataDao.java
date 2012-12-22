package org.dspace.orm.dao.api;

import org.dspace.orm.entity.Metadata;

public interface IMetadataDao extends IDSpaceDao<Metadata> {

	long updatePrefix(String oldH, String newH);

}
