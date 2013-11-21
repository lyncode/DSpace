package org.dspace.xoai.services.api.database;

import com.lyncode.xoai.dataprovider.filter.ScopedFilter;

import java.util.List;

public interface DatabaseQueryResolver {
    DatabaseQuery buildQuery (List<ScopedFilter> filters, int offset, int length) throws DatabaseQueryException;
}
