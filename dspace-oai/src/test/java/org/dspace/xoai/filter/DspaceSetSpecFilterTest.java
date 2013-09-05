package org.dspace.xoai.filter;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;
import org.dspace.xoai.data.DSpaceItem;
import org.dspace.xoai.util.MetadataFieldManager;
import org.dspace.xoai.util.XOAIDatabaseManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lyncode.xoai.dataprovider.core.ReferenceSet;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Parameter;

@PrepareForTest({ ConfigurationManager.class, MetadataFieldManager.class, HandleManager.class, XOAIDatabaseManager.class  })
@RunWith(PowerMockRunner.class)
public class DspaceSetSpecFilterTest {

    DSpaceItem item;
    DspaceSetSpecFilter filter;
    Context ctx;
    List<Parameter> parameters;
    
    @Before
    public void setup () throws SQLException {
        PowerMockito.mockStatic(ConfigurationManager.class);
        PowerMockito.mockStatic(HandleManager.class);
        PowerMockito.mockStatic(XOAIDatabaseManager.class);
        when(ConfigurationManager.getProperty(anyString())).thenReturn("hello");
        when(XOAIDatabaseManager.getAllSubCollections(any(Context.class), anyInt())).thenReturn(Arrays.asList(new Integer[] { 1 }));
        

        Item dsitem = mock(Item.class);
        when(dsitem.getBundles()).thenReturn(new Bundle[]{});
        
        
        when(HandleManager.resolveToObject(any(Context.class), anyString())).thenReturn(dsitem);
        
        item = mock(DSpaceItem.class);
    }

    @Test
    public void testNotShown() {
        
        filter = new DspaceSetSpecFilter("col_set");
        assertFalse(filter.isItemShown(item));
    }

    @Test
    public void testIsShown() {
        
        filter = new DspaceSetSpecFilter("col_set");
        when(item.getSets()).thenReturn(Arrays.asList(new ReferenceSet[] {
            new ReferenceSet("col_set")                                                                  
        }));
        assertTrue(filter.isItemShown(item));
    }
    
    @Test
    public void testGetQuery() {
        
        filter = new DspaceSetSpecFilter("col_set");
        assertEquals("item.collections:col_set", filter.getQuery().getQuery());
    }
    @Test
    public void testGetWhere() {
        
        filter = new DspaceSetSpecFilter("col_set");
        Context ctx = mock(Context.class);
        assertEquals("EXISTS (SELECT tmp.* FROM collection2item tmp WHERE tmp.item_id=i.item_id AND collection_id = ?)", filter.getWhere(ctx).getWhere());
    }

    
    @Test
    public void testGetQuery2() {
        
        filter = new DspaceSetSpecFilter("com_set");
        assertEquals("item.communities:com_set", filter.getQuery().getQuery());
    }
    @Test
    public void testGetWhere2() {
        
        filter = new DspaceSetSpecFilter("com_set");
        Context ctx = mock(Context.class);
        assertEquals("EXISTS (SELECT tmp.* FROM collection2item tmp WHERE tmp.item_id=i.item_id AND collection_id IN (1))", filter.getWhere(ctx).getWhere());
    }

}
