package org.dspace.orm.dao.api;

import java.util.List;

public interface IDSpaceDao<T> {
	int save(T c);

    T selectById(int id);

    boolean delete(T c);

    // Listing
    List<T> selectAll();
}
