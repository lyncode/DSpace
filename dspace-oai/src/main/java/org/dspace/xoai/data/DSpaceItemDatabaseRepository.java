/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.xoai.data;

import com.lyncode.xoai.dataprovider.core.ListItemIdentifiersResult;
import com.lyncode.xoai.dataprovider.core.ListItemsResults;
import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier;
import com.lyncode.xoai.dataprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.filter.FilterScope;
import com.lyncode.xoai.dataprovider.filter.ScopedFilter;
import com.lyncode.xoai.dataprovider.filter.conditions.AbstractCondition;
import com.lyncode.xoai.dataprovider.filter.conditions.AndCondition;
import com.lyncode.xoai.dataprovider.filter.conditions.NotCondition;
import com.lyncode.xoai.dataprovider.filter.conditions.OrCondition;
import com.lyncode.xoai.dataprovider.xml.xoai.Metadata;

import java.io.IOException;
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
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.storage.rdbms.TableRowIterator;
import org.dspace.xoai.filter.DSpaceFilter;
import org.dspace.xoai.filter.DatabaseFilterResult;
import org.dspace.xoai.services.api.cache.XOAIItemCacheService;
import org.dspace.xoai.services.impl.cache.DSpaceXOAIItemCacheService;
import org.dspace.xoai.util.ItemUtils;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 * @author Domingo Iglesias <diglesias@ub.edu>
 */
public class DSpaceItemDatabaseRepository extends DSpaceItemRepository
{

    private static Logger log = LogManager.getLogger(DSpaceItemDatabaseRepository.class);
    private XOAIItemCacheService cacheService;
    private boolean useCache;

    private Context _context;

    public DSpaceItemDatabaseRepository(Context context)
    {
        _context = context;
        cacheService = new DSpaceXOAIItemCacheService();
        useCache = ConfigurationManager.getBooleanProperty("oai", "cache.enabled", true);
    }
    
    private Metadata getMetadata (Item item) throws IOException {
        if (this.useCache) {
            if (!cacheService.hasCache(item))
                cacheService.put(item, ItemUtils.retrieveMetadata(item));
            
            return cacheService.get(item);
        } else return ItemUtils.retrieveMetadata(item);
    }

    @Override
    public AbstractItem getItem(String id) throws IdDoesNotExistException, OAIException
    {
        try
        {
            String parts[] = id.split(Pattern.quote(":"));
            if (parts.length == 3)
            {
                DSpaceObject obj = HandleManager.resolveToObject(_context,
                        parts[2]);
                if (obj == null)
                    throw new IdDoesNotExistException();
                if (!(obj instanceof Item))
                    throw new IdDoesNotExistException();
                return new DSpaceDatabaseItem((Item) obj, this.getMetadata((Item) obj));
            }
        }
        catch (NumberFormatException e)
        {
            log.debug(e.getMessage(), e);
            throw new IdDoesNotExistException();
        }
        catch (SQLException e)
        {
            throw new OAIException(e);
        } catch (IOException e) {
            throw new OAIException(e);
        }
        throw new IdDoesNotExistException();
    }

    private ListItemIdentifiersResult getIdentifierResult(String query, String countQuery, 
            List<Object> countParameters,
            List<Object> parameters, int length) throws IOException
    {
        boolean hasMore = false;
        List<AbstractItemIdentifier> list = new ArrayList<AbstractItemIdentifier>();
        TableRowIterator rows;
        int count = -1;
        try
        {
            count = DatabaseManager.querySingle(_context, countQuery, countParameters).getIntColumn("count");
        }
        catch (SQLException e1)
        {
            log.error("Unable to retrieve number of items that match");
        }
        try
        {
            parameters.add(length + 1);
            rows = DatabaseManager.queryTable(_context, "item", query,
                    parameters.toArray());
            ItemIterator iterator = new ItemIterator(_context, rows);
            int i = 0;
            while (iterator.hasNext() && i < length)
            {
                Item it = iterator.next();
                list.add(new DSpaceDatabaseItem(it, this.getMetadata(it)));
                i++;
            }
            hasMore = iterator.hasNext();
        }
        catch (SQLException e)
        {
            log.error(e.getMessage(), e);
        }
        return new ListItemIdentifiersResult(hasMore, list, count);
    }

    private ListItemsResults getResult(String query, String countQuery, List<Object> countParameters, List<Object> parameters, int length) throws IOException
    {
        boolean hasMore = false;
        List<AbstractItem> list = new ArrayList<AbstractItem>();
        TableRowIterator rows;
        int count = -1;
        try
        {
            count = DatabaseManager.querySingle(_context, countQuery, countParameters).getIntColumn("count");
        }
        catch (SQLException e1)
        {
            log.error("Unable to retrieve number of items that match");
        }
        try
        {
            
            parameters.add(length + 1);
            rows = DatabaseManager.queryTable(_context, "item", query,
                    parameters.toArray());
            ItemIterator iterator = new ItemIterator(_context, rows);
            int i = 0;
            while (iterator.hasNext() && i < length)
            {
                Item it = iterator.next();
                list.add(new DSpaceDatabaseItem(it, this.getMetadata(it)));
                i++;
            }
            hasMore = iterator.hasNext();
        }
        catch (SQLException e)
        {
            log.error(e.getMessage(), e);
        }
        return new ListItemsResults(hasMore, list, count);
    }
    
    private String buildQuery (FilterScope scope, AbstractCondition condition, List<Object> parameters) {
        if (condition instanceof DSpaceFilter) {
            DSpaceFilter filter = (DSpaceFilter) condition;
            DatabaseFilterResult result = filter.getWhere(_context);
            if (result.hasResult())
            {
                parameters.addAll(result.getParameters());
                if (scope == FilterScope.MetadataFormat)
                    return "(i.withdrawn=TRUE OR ("
                            + result.getWhere() + "))";
                else
                    return "(" + result.getWhere() + ")";
                
                //countParameters.addAll(result.getParameters());
            }
        } else if (condition instanceof AndCondition) {
            AndCondition and = (AndCondition) condition;
            return  "("+buildQuery(scope, and.getLeft(), parameters)+") AND ("+buildQuery(scope, and.getRight(), parameters)+")";
        } else if (condition instanceof OrCondition) {
            OrCondition or = (OrCondition) condition;
            return "("+buildQuery(scope, or.getLeft(), parameters)+") AND ("+buildQuery(scope, or.getRight(), parameters)+")";
        } else if (condition instanceof NotCondition) {
            NotCondition not = (NotCondition) condition;
            return "NOT ("+buildQuery(scope, not.getCondition(), parameters)+")";
        }
        return "true";
    }
    
    private String buildCondition (List<ScopedFilter> filters, int offset, int length, List<Object> parameters) {
        List<String> whereCond = new ArrayList<String>();
        for (ScopedFilter filter : filters)
            whereCond.add(this.buildQuery(filter.getScope(), filter.getCondition(), parameters));

        return StringUtils.join(whereCond.iterator(), " AND ");
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset,
            int length) throws OAIException
    {
        List<Object> parameters = new ArrayList<Object>();
        List<Object> countParameters = new ArrayList<Object>();
        String query = "SELECT i.* FROM item i ";
        String countQuery = "SELECT COUNT(*) as count FROM item i";
        
        String where = this.buildCondition(filters, offset, length, parameters);
        countParameters.addAll(parameters);

        if (!where.equals("")) {
            query += " WHERE " + where;
            countQuery += " WHERE " + where;
        }
        
        query += " ORDER BY i.item_id";
        String db = ConfigurationManager.getProperty("db.name");
        boolean postgres = true;
        // Assuming Postgres as default
        if ("oracle".equals(db))  postgres = false;
        if (postgres)
        {
            query += " OFFSET ? LIMIT ?";
        }
        else
        {
            // Oracle
            query = "SELECT * FROM (" + query
                  + ") WHERE ROWNUM BETWEEN ? AND ?";
            length = length + offset;
        }
        parameters.add(offset);
        parameters.add(length);
        try {
            return this.getResult(query, countQuery, countParameters, parameters, length);
        } catch (IOException e) {
            throw new OAIException(e);
        }
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(
            List<ScopedFilter> filters, int offset, int length) throws OAIException
    {
        List<Object> parameters = new ArrayList<Object>();
        List<Object> countParameters = new ArrayList<Object>();
        String query = "SELECT i.* FROM item i ";
        String countQuery = "SELECT COUNT(*) as count FROM item i";
        
        String where = this.buildCondition(filters, offset, length, parameters);
        countParameters.addAll(parameters);
        
        if (!where.equals("")) {
            query += " WHERE " + where;
            countQuery += " WHERE "+ where;
        }
        query += " ORDER BY i.item_id";
        String db = ConfigurationManager.getProperty("db.name");
        boolean postgres = true;
        // Assuming Postgres as default
        if ("oracle".equals(db))
            postgres = false;
        if (postgres)
        {
            query += " OFFSET ? LIMIT ?";
        }
        else
        {
            // Oracle
            query = "SELECT *, ROWNUM r FROM (" + query
                    + ") WHERE r BETWEEN ? AND ?";
            length = length + offset;
        }
        parameters.add(offset);
        parameters.add(length);
        try {
            return this.getIdentifierResult(query, countQuery, countParameters, parameters, length);
        } catch (IOException e) {
            throw new OAIException(e);
        }
    }
}
