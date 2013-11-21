package org.dspace.xoai.services.api.solr;

import com.lyncode.xoai.dataprovider.filter.ScopedFilter;

import java.util.List;

public interface SolrQueryResolver {
    String buildQuery (List<ScopedFilter> filters);
}
