/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.xoai.data;

import com.lyncode.xoai.common.dataprovider.core.ListItemIdentifiersResult;
import com.lyncode.xoai.common.dataprovider.core.ListItemsResults;
import com.lyncode.xoai.common.dataprovider.data.AbstractItem;
import com.lyncode.xoai.common.dataprovider.data.AbstractItemIdentifier;
import com.lyncode.xoai.common.dataprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.common.dataprovider.filter.Filter;
import com.lyncode.xoai.common.dataprovider.filter.FilterScope;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.content.ItemIterator;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.storage.rdbms.TableRowIterator;
import org.dspace.xoai.filter.DSpaceFilter;
import org.dspace.xoai.filter.DatabaseFilterResult;

/**
 *
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class DSpaceItemDatabaseRepository extends DSpaceItemRepository {

    private static Logger log = LogManager.getLogger(DSpaceItemDatabaseRepository.class);
    private Context _context;

    public DSpaceItemDatabaseRepository(Context context) {
        _context = context;
    }

    @Override
    public AbstractItem getItem(String id) throws IdDoesNotExistException {
        try {
            String parts[] = id.split(Pattern.quote(":"));
            if (parts.length == 3) {
                DSpaceObject obj = HandleManager.resolveToObject(_context, parts[2]);
                if (obj == null) throw new IdDoesNotExistException();
                if (!(obj instanceof Item)) throw new IdDoesNotExistException();
                return new DSpaceItem((Item) obj);
            }
        } catch (NumberFormatException e) {
            log.debug(e.getMessage(), e);
            throw new IdDoesNotExistException();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new IdDoesNotExistException();
        }
        throw new IdDoesNotExistException();
    }

    private ListItemIdentifiersResult getIdentifierResult(String query, List<Object> parameters, int length) {
        boolean hasMore = false;
        List<AbstractItemIdentifier> list = new ArrayList<AbstractItemIdentifier>();
        TableRowIterator rows;
        try {
            parameters.add(length + 1);
            rows = DatabaseManager.queryTable(_context, "item", query, parameters.toArray());
            ItemIterator iterator = new ItemIterator(_context, rows);
            int i = 0;
            while (iterator.hasNext() && i < length) {
                list.add(new DSpaceItem(iterator.next()));
                i++;
            }
            hasMore = iterator.hasNext();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return new ListItemIdentifiersResult(hasMore, list);
    }


    private ListItemsResults getResult(String query, List<Object> parameters, int length) {
        boolean hasMore = false;
        List<AbstractItem> list = new ArrayList<AbstractItem>();
        TableRowIterator rows;
        try {
            parameters.add(length + 1);
            rows = DatabaseManager.queryTable(_context, "item", query, parameters.toArray());
            ItemIterator iterator = new ItemIterator(_context, rows);
            int i = 0;
            while (iterator.hasNext() && i < length) {
                list.add(new DSpaceItem(iterator.next()));
                i++;
            }
            hasMore = iterator.hasNext();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return new ListItemsResults(hasMore, list);
    }


    @Override
    protected ListItemsResults getItems(List<Filter> filters,
            int offset, int length) {
        List<Object> parameters = new ArrayList<Object>();
        String query = "SELECT i.* FROM item i ";
        List<String> whereCond = new ArrayList<String>();
        for (Filter filter : filters) {
            if (filter.getFilter() instanceof DSpaceFilter) {
                DSpaceFilter dspaceFilter = (DSpaceFilter) filter.getFilter();
                DatabaseFilterResult result = dspaceFilter.getWhere(_context);
                if (result.hasResult()) {
                    if (filter.getScope() == FilterScope.MetadataFormat)
                        whereCond.add("(i.withdrawn=TRUE OR (" + result.getWhere() + "))");
                    else
                        whereCond.add("(" + result.getWhere() + ")");
                    parameters.addAll(result.getParameters());
                }
            }
        }
        String where = StringUtils.join(whereCond.iterator(), " AND ");
        if (!where.equals(""))
            query += " WHERE " + where;
        query += " ORDER BY i.item_id OFFSET ? LIMIT ?";
        parameters.add(offset);
        return this.getResult(query, parameters, length);
    }


    @Override
    protected ListItemIdentifiersResult getItemIdentifiers(
            List<Filter> filters, int offset, int length) {
        List<Object> parameters = new ArrayList<Object>();
        String query = "SELECT i.* FROM item i";
        List<String> whereCond = new ArrayList<String>();
        for (Filter filter : filters) {
            if (filter.getFilter() instanceof DSpaceFilter) {
                DSpaceFilter dspaceFilter = (DSpaceFilter) filter.getFilter();
                DatabaseFilterResult result = dspaceFilter.getWhere(_context);
                if (result.hasResult()) {
                    if (filter.getScope() == FilterScope.MetadataFormat)
                        whereCond.add("(i.withdrawn=TRUE OR (" + result.getWhere() + "))");
                    else
                        whereCond.add("(" + result.getWhere() + ")");
                    parameters.addAll(result.getParameters());
                }
            }
        }
        String where = StringUtils.join(whereCond.iterator(), " AND ");
        if (!where.equals(""))
            query += " WHERE " + where;
        query += " ORDER BY i.item_id OFFSET ? LIMIT ?";
        parameters.add(offset);
        return this.getIdentifierResult(query, parameters, length);
    }
}
