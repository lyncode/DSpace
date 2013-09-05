/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.xoai.data;

import java.util.Date;
import java.util.List;

import org.dspace.xoai.filter.DateFromFilter;
import org.dspace.xoai.filter.DateUntilFilter;
import org.dspace.xoai.filter.DspaceSetSpecFilter;

import com.lyncode.xoai.dataprovider.core.ListItemIdentifiersResult;
import com.lyncode.xoai.dataprovider.core.ListItemsResults;
import com.lyncode.xoai.dataprovider.data.AbstractItemRepository;
import com.lyncode.xoai.dataprovider.filter.FilterScope;
import com.lyncode.xoai.dataprovider.filter.ScopedFilter;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public abstract class DSpaceItemRepository extends AbstractItemRepository
{

    // private static Logger log =
    // LogManager.getLogger(DSpaceItemRepository.class);
    @Override
    public ListItemIdentifiersResult getItemIdentifiers(
            List<ScopedFilter> filters, int offset, int length, Date from)
    {
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        return this.getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(
            List<ScopedFilter> filters, int offset, int length, String setSpec)
    {
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec),
                FilterScope.Query));
        return this.getItemIdentifiers(filters, offset, length);
    }

    @Override
	public ListItemIdentifiersResult getItemIdentifiers(
            List<ScopedFilter> filters, int offset, int length, Date from, Date until)
    {
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        filters.add(new ScopedFilter(new DateUntilFilter(until), FilterScope.Query));
        return this.getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(
            List<ScopedFilter> filters, int offset, int length, String setSpec,
            Date from)
    {
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec),
                FilterScope.Query));
        return this.getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(
            List<ScopedFilter> filters, int offset, int length, String setSpec,
            Date from, Date until)
    {
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        filters.add(new ScopedFilter(new DateUntilFilter(until), FilterScope.Query));
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec),
                FilterScope.Query));
        return this.getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiersUntil(
            List<ScopedFilter> filters, int offset, int length, Date until)
    {
        filters.add(new ScopedFilter(new DateUntilFilter(until), FilterScope.Query));
        return this.getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiersUntil(
            List<ScopedFilter> filters, int offset, int length, String setSpec,
            Date until)
    {
        filters.add(new ScopedFilter(new DateUntilFilter(until), FilterScope.Query));
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec),
                FilterScope.Query));
        return this.getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset,
            int length, Date from)
    {
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        return this.getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset,
            int length, String setSpec)
    {
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec),
                FilterScope.Query));
        return this.getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset,
            int length, Date from, Date until)
    {
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        filters.add(new ScopedFilter(new DateUntilFilter(until), FilterScope.Query));
        return this.getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset,
            int length, String setSpec, Date from)
    {
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec),
                FilterScope.Query));
        return this.getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset,
            int length, String setSpec, Date from, Date until)
    {
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        filters.add(new ScopedFilter(new DateUntilFilter(until), FilterScope.Query));
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec),
                FilterScope.Query));
        return this.getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItemsUntil(List<ScopedFilter> filters, int offset,
            int length, Date until)
    {
        filters.add(new ScopedFilter(new DateUntilFilter(until), FilterScope.Query));
        return this.getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItemsUntil(List<ScopedFilter> filters, int offset,
            int length, String setSpec, Date from)
    {
        filters.add(new ScopedFilter(new DateUntilFilter(from), FilterScope.Query));
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec),
                FilterScope.Query));
        return this.getItems(filters, offset, length);
    }
}
