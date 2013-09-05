package org.dspace.xoai.filter;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.xoai.data.DSpaceItem;
import org.dspace.xoai.exceptions.InvalidMetadataFieldException;
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
public class DSpaceMetadataExistsFilterTest {
    DSpaceMetadataExistsFilter filter;
    Context ctx;
    DSpaceItem item;
    List<Parameter> parameters;
    
    @Before
    public void setup () throws InvalidMetadataFieldException, SQLException {
        PowerMockito.mockStatic(ConfigurationManager.class);
        when(ConfigurationManager.getProperty(anyString())).thenReturn("hello");
        
        PowerMockito.mockStatic(MetadataFieldManager.class);
        when(MetadataFieldManager.getFieldID(any(Context.class), anyString())).thenReturn(1);
        
        ctx = mock(Context.class);
        
        item = mock(DSpaceItem.class);
        
        filter = new DSpaceMetadataExistsFilter();
        filter.initialize(ctx);
        
        parameters = new ArrayList<Parameter>();
        parameters.add(new Parameter());
        parameters.get(0).setKey("fields");
    }

    
    @Test
    public void testIsShown() {
        when(item.getMetadata("dc.title")).thenReturn(Arrays.asList(new String[]{ "ola" }));
        parameters.get(0).getValue().add("dc.title");
        filter.load(parameters);
        assertTrue(filter.isItemShown(item));
    }
    
    @Test
    public void testNotShown() {
        when(item.getMetadata("dc.titl")).thenReturn(Arrays.asList(new String[]{ "ola" }));
        parameters.get(0).getValue().add("dc.title");
        filter.load(parameters);
        assertFalse(filter.isItemShown(item));
    }

    @Test
    public void testGetWhere() {
        when(item.getMetadata("dc.titl")).thenReturn(Arrays.asList(new String[]{ "ola" }));
        parameters.get(0).getValue().add("dc.title");
        filter.load(parameters);
        assertEquals("(EXISTS (SELECT tmp.* FROM metadatavalue tmp WHERE tmp.item_id=i.item_id AND tmp.metadata_field_id=?))", filter.getWhere(ctx).getWhere());
    }
    @Test
    public void testGetQuery() {
        when(item.getMetadata("dc.titl")).thenReturn(Arrays.asList(new String[]{ "ola" }));
        parameters.get(0).getValue().add("dc.title");
        filter.load(parameters);
        assertEquals("(metadata.dc.title:[* TO *])", filter.getQuery().getQuery());
    }

}
