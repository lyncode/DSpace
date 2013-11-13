package org.dspace.xoai.tests.helpers.stubs;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.lyncode.xoai.builders.Builder;
import com.lyncode.xoai.builders.ListBuilder;
import com.lyncode.xoai.builders.dataprovider.ElementBuilder;
import com.lyncode.xoai.builders.dataprovider.ItemMetadataBuilder;
import com.lyncode.xoai.builders.dataprovider.MetadataBuilder;
import com.lyncode.xoai.dataprovider.core.ItemMetadata;
import com.lyncode.xoai.dataprovider.core.ListItemIdentifiersResult;
import com.lyncode.xoai.dataprovider.core.ListItemsResults;
import com.lyncode.xoai.dataprovider.core.ReferenceSet;
import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier;
import com.lyncode.xoai.dataprovider.data.AbstractItemRepository;
import com.lyncode.xoai.dataprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.filter.FilterScope;
import com.lyncode.xoai.dataprovider.filter.ScopedFilter;
import com.sun.istack.Nullable;
import org.dspace.xoai.data.DSpaceItem;
import org.dspace.xoai.filter.DateFromFilter;
import org.dspace.xoai.filter.DateUntilFilter;
import org.dspace.xoai.filter.DspaceSetSpecFilter;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static java.lang.Math.min;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StubbedItemRepository extends AbstractItemRepository {
    private List<DSpaceItem> list = new ArrayList<DSpaceItem>();

    @Override
    public AbstractItem getItem(String identifier) throws IdDoesNotExistException, OAIException {
        for (DSpaceItem item : list)
            if (item.getIdentifier().equals(identifier))
                return item;

        throw new IdDoesNotExistException();
    }

    private static boolean passesFilters (DSpaceItem item, List<ScopedFilter> filters) {
        for (ScopedFilter filter : filters) {
            if (!filter.getCondition().isItemShown(item))
                return false;
        }
        return true;
    }

    private List<DSpaceItem> filter (final List<ScopedFilter> filters) {
        return new ArrayList<DSpaceItem>(Collections2.filter(this.list, new Predicate<DSpaceItem>() {
            @Override
            public boolean apply(@Nullable DSpaceItem dSpaceItem) {
                return passesFilters(dSpaceItem, filters);
            }
        }));
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(List<ScopedFilter> filters, int offset, int length) throws OAIException {
        List<DSpaceItem> filtered = this.filter(filters);
        return new ListItemIdentifiersResult(offset + length < filtered.size(),
                new ListBuilder<DSpaceItem>().add(filtered.subList(offset, min(offset + length, filtered.size()))).build(new ListBuilder.Transformer<DSpaceItem, AbstractItemIdentifier>() {
                    @Override
                    public AbstractItemIdentifier transform(DSpaceItem elem) {
                        return elem;
                    }
                }),
                filtered.size());
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(List<ScopedFilter> filters, int offset, int length, Date from) throws OAIException {
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        return getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiersUntil(List<ScopedFilter> filters, int offset, int length, Date until) throws OAIException {
        filters.add(new ScopedFilter(new DateUntilFilter(until), FilterScope.Query));
        return getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(List<ScopedFilter> filters, int offset, int length, Date from, Date until) throws OAIException {
        filters.add(new ScopedFilter(new DateUntilFilter(until), FilterScope.Query));
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        return getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(List<ScopedFilter> filters, int offset, int length, String setSpec) throws OAIException {
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec), FilterScope.Query));
        return getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(List<ScopedFilter> filters, int offset, int length, String setSpec, Date from) throws OAIException {
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec), FilterScope.Query));
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        return getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiersUntil(List<ScopedFilter> filters, int offset, int length, String setSpec, Date until) throws OAIException {
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec), FilterScope.Query));
        filters.add(new ScopedFilter(new DateUntilFilter(until), FilterScope.Query));
        return getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(List<ScopedFilter> filters, int offset, int length, String setSpec, Date from, Date until) throws OAIException {
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec), FilterScope.Query));
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        filters.add(new ScopedFilter(new DateUntilFilter(until), FilterScope.Query));
        return getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset, int length) throws OAIException {
        List<DSpaceItem> filtered = this.filter(filters);
        return new ListItemsResults(offset + length < filtered.size(),
                new ListBuilder<DSpaceItem>().add(filtered.subList(offset, min(offset + length, filtered.size()))).build(new ListBuilder.Transformer<DSpaceItem, AbstractItem>() {
                    @Override
                    public AbstractItem transform(DSpaceItem elem) {
                        return elem;
                    }
                }),
                filtered.size());
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset, int length, Date from) throws OAIException {
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        return getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItemsUntil(List<ScopedFilter> filters, int offset, int length, Date until) throws OAIException {
        filters.add(new ScopedFilter(new DateUntilFilter(until), FilterScope.Query));
        return getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset, int length, Date from, Date until) throws OAIException {
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        filters.add(new ScopedFilter(new DateUntilFilter(until), FilterScope.Query));
        return getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset, int length, String setSpec) throws OAIException {
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec), FilterScope.Query));
        return getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset, int length, String setSpec, Date from) throws OAIException {
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec), FilterScope.Query));
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        return getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItemsUntil(List<ScopedFilter> filters, int offset, int length, String setSpec, Date until) throws OAIException {
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec), FilterScope.Query));
        filters.add(new ScopedFilter(new DateUntilFilter(until), FilterScope.Query));
        return getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset, int length, String setSpec, Date from, Date until) throws OAIException {
        filters.add(new ScopedFilter(new DspaceSetSpecFilter(setSpec), FilterScope.Query));
        filters.add(new ScopedFilter(new DateUntilFilter(until), FilterScope.Query));
        filters.add(new ScopedFilter(new DateFromFilter(from), FilterScope.Query));
        return getItems(filters, offset, length);
    }

    public StubbedItemRepository withItem (DSpaceItemBuilder builder) {
        this.list.add(builder.build());
        return this;
    }

    public static class DSpaceItemBuilder implements Builder<DSpaceItem> {
        private DSpaceItem item = mock(DSpaceItem.class);
        private List<ReferenceSet> sets = new ArrayList<ReferenceSet>();
        private MetadataBuilder metadataBuilder = new MetadataBuilder();

        public DSpaceItemBuilder withIdentifier (String identifier) {
            when(item.getIdentifier()).thenReturn(identifier);
            return this;
        }

        public DSpaceItemBuilder withLastModifiedDate (Date lastModifiedDate) {
            when(item.getDatestamp()).thenReturn(lastModifiedDate);
            return this;
        }

        public DSpaceItemBuilder withSet (String spec) {
            sets.add(new ReferenceSet(spec));
            return this;
        }

        public DSpaceItemBuilder isDeleted () {
            when(item.isDeleted()).thenReturn(true);
            return this;
        }

        public DSpaceItemBuilder isNotDeleted () {
            when(item.isDeleted()).thenReturn(false);
            return this;
        }

        public DSpaceItemBuilder withMetadata (String schema, String element, String value) {
            metadataBuilder.withElement(new ElementBuilder().withName(schema).withField(element, value).build());
            return this;
        }

        @Override
        public DSpaceItem build() {
            when(item.getMetadata()).thenReturn(new ItemMetadataBuilder().withMetadata(metadataBuilder).build());
            when(item.getSets()).thenReturn(sets);
            return item;
        }
    }
}
