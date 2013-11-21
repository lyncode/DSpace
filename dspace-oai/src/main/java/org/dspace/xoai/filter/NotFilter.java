package org.dspace.xoai.filter;

import org.dspace.core.Context;
import org.dspace.xoai.data.DSpaceItem;
import org.dspace.xoai.filter.results.DatabaseFilterResult;
import org.dspace.xoai.filter.results.SolrFilterResult;

public class NotFilter extends DSpaceFilter {
    private DSpaceFilter inFilter;

    public NotFilter(DSpaceFilter inFilter) {
        this.inFilter = inFilter;
    }


    @Override
    public DatabaseFilterResult buildDatabaseQuery(Context context) {
        DatabaseFilterResult result = inFilter.buildDatabaseQuery(context);
        return new DatabaseFilterResult("NOT ("+result.getQuery()+")", result.getParameters());
    }

    @Override
    public SolrFilterResult buildSolrQuery() {
        return new SolrFilterResult("NOT("+inFilter.buildSolrQuery()+")");
    }

    @Override
    public boolean isShown(DSpaceItem item) {
        return !inFilter.isShown(item);
    }
}
