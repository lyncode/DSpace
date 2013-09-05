package org.dspace.xoai.filter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.xoai.data.DSpaceItem;
import org.dspace.xoai.exceptions.InvalidMetadataFieldException;
import org.dspace.xoai.filter.data.DSpaceMetadataFilterOperator;
import org.dspace.xoai.util.MetadataFieldManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Parameter;

@PrepareForTest({ ConfigurationManager.class, MetadataFieldManager.class })
@RunWith(PowerMockRunner.class)
public class DSpaceAtLeastOneMetadataFilterTest {
    DSpaceItem item;
    DSpaceAtLeastOneMetadataFilter filter;
    Context ctx;
    List<Parameter> parameters;
    
    @Before
    public void setup () throws InvalidMetadataFieldException, SQLException {
        PowerMockito.mockStatic(ConfigurationManager.class);
        when(ConfigurationManager.getProperty(anyString())).thenReturn("hello");
        
        PowerMockito.mockStatic(MetadataFieldManager.class);
        when(MetadataFieldManager.getFieldID(any(Context.class), anyString())).thenReturn(1);
        
        item = mock(DSpaceItem.class);
        ctx = mock(Context.class);
        
        parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter());
        parameters.get(0).setKey("operator");
        
        parameters.add(new Parameter());
        parameters.get(1).setKey("field");
        parameters.get(1).getValue().add("dc.title");
        
        parameters.add(new Parameter());
        parameters.get(2).setKey("value");
        parameters.get(2).getValue().add("test");
        
        
        filter = new DSpaceAtLeastOneMetadataFilter();
        filter.initialize(ctx);
    }

    @Test
    public void testIsShown() {
        when(item.getMetadata("dc.title")).thenReturn(Arrays.asList(new String[] { "test" }));
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.CONTAINS.name());
        filter.load(parameters);
        assertTrue(filter.isShown(item));
    }

    @Test
    public void testIsShownEqual() {
        when(item.getMetadata("dc.title")).thenReturn(Arrays.asList(new String[] { "test" }));
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.EQUAL.name());
        filter.load(parameters);
        assertTrue(filter.isShown(item));
    }

    @Test
    public void testIsShownGreater() {
        when(item.getMetadata("dc.title")).thenReturn(Arrays.asList(new String[] { "testa" }));
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.GREATER.name());
        filter.load(parameters);
        assertTrue(filter.isShown(item));
    }

    @Test
    public void testIsShownGreaterEQ() {
        when(item.getMetadata("dc.title")).thenReturn(Arrays.asList(new String[] { "testa" }));
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.GREATER_OR_EQUAL.name());
        filter.load(parameters);
        assertTrue(filter.isShown(item));
    }
    @Test
    public void testIsShownLowerEQ() {
        when(item.getMetadata("dc.title")).thenReturn(Arrays.asList(new String[] { "tes" }));
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.LOWER_OR_EQUAL.name());
        filter.load(parameters);
        assertTrue(filter.isShown(item));
    }

    @Test
    public void testIsShownLower() {
        when(item.getMetadata("dc.title")).thenReturn(Arrays.asList(new String[] { "tes" }));
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.LOWER.name());
        filter.load(parameters);
        assertTrue(filter.isShown(item));
    }

    @Test
    public void testIsShownEndsWith() {
        when(item.getMetadata("dc.title")).thenReturn(Arrays.asList(new String[] { "test" }));
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.ENDS_WITH.name());
        filter.load(parameters);
        assertTrue(filter.isShown(item));
    }

    @Test
    public void testIsShownStartsWith() {
        when(item.getMetadata("dc.title")).thenReturn(Arrays.asList(new String[] { "atest" }));
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.STARTS_WITH.name());
        filter.load(parameters);
        assertFalse(filter.isShown(item));
    }
    
    @Test
    public void testIsShown2() {
        when(item.getMetadata("dc.title")).thenReturn(Arrays.asList(new String[] { "testing" }));
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.CONTAINS.name());
        filter.load(parameters);
        assertTrue(filter.isShown(item));
    }
    
    @Test
    public void testNotShown () {
        when(item.getMetadata("dc.title")).thenReturn(Arrays.asList(new String[] { "tes" }));
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.CONTAINS.name());
        filter.load(parameters);
        assertFalse(filter.isShown(item));
    }


    @Test
    public void testGetWhereContains() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.CONTAINS.name());
        filter.load(parameters);
        assertEquals("EXISTS (SELECT tmp.* FROM metadatavalue tmp WHERE tmp.item_id=i.item_id AND tmp.metadata_field_id=? AND ((tmp.text_value LIKE ?)))", filter.getWhere(ctx).getWhere());
    }

    @Test
    public void testGetWhereEQUALS() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.EQUAL.name());
        filter.load(parameters);
        assertEquals("EXISTS (SELECT tmp.* FROM metadatavalue tmp WHERE tmp.item_id=i.item_id AND tmp.metadata_field_id=? AND ((tmp.text_value LIKE ?)))", filter.getWhere(ctx).getWhere());
    }

    @Test
    public void testGetWhereEnds() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.ENDS_WITH.name());
        filter.load(parameters);
        assertEquals("EXISTS (SELECT tmp.* FROM metadatavalue tmp WHERE tmp.item_id=i.item_id AND tmp.metadata_field_id=? AND ((tmp.text_value LIKE ?)))", filter.getWhere(ctx).getWhere());
    }

    @Test
    public void testGetWhereSTARTS() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.STARTS_WITH.name());
        filter.load(parameters);
        assertEquals("EXISTS (SELECT tmp.* FROM metadatavalue tmp WHERE tmp.item_id=i.item_id AND tmp.metadata_field_id=? AND ((tmp.text_value LIKE ?)))", filter.getWhere(ctx).getWhere());
    }

    @Test
    public void testGetWhereGT() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.GREATER.name());
        filter.load(parameters);
        assertEquals("EXISTS (SELECT tmp.* FROM metadatavalue tmp WHERE tmp.item_id=i.item_id AND tmp.metadata_field_id=? AND ((tmp.text_value > ?)))", filter.getWhere(ctx).getWhere());
    }

    @Test
    public void testGetWhereLT() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.LOWER.name());
        filter.load(parameters);
        assertEquals("EXISTS (SELECT tmp.* FROM metadatavalue tmp WHERE tmp.item_id=i.item_id AND tmp.metadata_field_id=? AND ((tmp.text_value < ?)))", filter.getWhere(ctx).getWhere());
    }

    @Test
    public void testGetWhereGTE() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.GREATER_OR_EQUAL.name());
        filter.load(parameters);
        assertEquals("EXISTS (SELECT tmp.* FROM metadatavalue tmp WHERE tmp.item_id=i.item_id AND tmp.metadata_field_id=? AND ((tmp.text_value >= ?)))", filter.getWhere(ctx).getWhere());
    }

    @Test
    public void testGetWhereLTE() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.LOWER_OR_EQUAL.name());
        filter.load(parameters);
        assertEquals("EXISTS (SELECT tmp.* FROM metadatavalue tmp WHERE tmp.item_id=i.item_id AND tmp.metadata_field_id=? AND ((tmp.text_value <= ?)))", filter.getWhere(ctx).getWhere());
    }





    @Test
    public void testGetQuery() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.CONTAINS.name());
        filter.load(parameters);
        assertEquals("(metadata.dc.title:*test*)", filter.getQuery().getQuery());
    }


    @Test
    public void testGetQuery1() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.EQUAL.name());
        filter.load(parameters);
        assertEquals("(metadata.dc.title:test)", filter.getQuery().getQuery());
    }


    @Test
    public void testGetQuery2() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.STARTS_WITH.name());
        filter.load(parameters);
        assertEquals("(metadata.dc.title:test*)", filter.getQuery().getQuery());
    }


    @Test
    public void testGetQuery3() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.ENDS_WITH.name());
        filter.load(parameters);
        assertEquals("(metadata.dc.title:*test)", filter.getQuery().getQuery());
    }


    @Test
    public void testGetQuery4() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.GREATER.name());
        filter.load(parameters);
        assertEquals("(metadata.dc.title:[test TO *])", filter.getQuery().getQuery());
    }


    @Test
    public void testGetQuery5() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.LOWER.name());
        filter.load(parameters);
        assertEquals("(metadata.dc.title:[* TO test])", filter.getQuery().getQuery());
    }


    @Test
    public void testGetQuery6() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.LOWER_OR_EQUAL.name());
        filter.load(parameters);
        assertEquals("(-(metadata.dc.title:[test TO *]))", filter.getQuery().getQuery());
    }


    @Test
    public void testGetQuery7() {
        parameters.get(0).getValue().add(DSpaceMetadataFilterOperator.GREATER_OR_EQUAL.name());
        filter.load(parameters);
        assertEquals("(-(metadata.dc.title:[* TO test]))", filter.getQuery().getQuery());
    }

}
