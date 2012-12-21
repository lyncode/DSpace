package org.dspace.orm.dao.api;

import org.dspace.orm.entity.EPerson;

public interface IEPersonDao {
	EPerson findById(int id);
	EPerson findByEmail(String email);
}
