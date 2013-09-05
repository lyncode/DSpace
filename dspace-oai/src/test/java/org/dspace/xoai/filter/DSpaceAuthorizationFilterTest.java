package org.dspace.xoai.filter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;

import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.content.Bundle;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;
import org.dspace.xoai.data.DSpaceItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@PrepareForTest({ AuthorizeManager.class, ConfigurationManager.class, HandleManager.class })
@RunWith(PowerMockRunner.class)
public class DSpaceAuthorizationFilterTest {
    DSpaceAuthorizationFilter filter;
    DSpaceItem item;
    Item dsitem;
    
    @Before
    public void setUp () throws AuthorizeException, SQLException {

        PowerMockito.mockStatic(AuthorizeManager.class);
        PowerMockito.mockStatic(ConfigurationManager.class);
        PowerMockito.mockStatic(HandleManager.class);
        
        item = mock(DSpaceItem.class);
        dsitem = mock(Item.class);
        when(dsitem.getBundles()).thenReturn(new Bundle[]{});
        
        
        when(HandleManager.resolveToObject(any(Context.class), anyString())).thenReturn(dsitem);
        when(ConfigurationManager.getProperty(anyString())).thenReturn("hello");
        //PowerMockito.doNothing().when(AuthorizeManager.class);
        //AuthorizeManager.authorizeAction(any(Context.class), any(DSpaceObject.class), Constants.READ);
        
        filter = new DSpaceAuthorizationFilter();
    }

    @Test
    public void testIsShown() {
        when(item.getIdentifier()).thenReturn("test");
        assertTrue(filter.isShown(item));
    }

    @Test
    public void testIsNotShown() throws AuthorizeException, SQLException {
        when(item.getIdentifier()).thenReturn("test");
        PowerMockito.doThrow(new AuthorizeException()).when(AuthorizeManager.class);
        AuthorizeManager.authorizeAction(any(Context.class), any(DSpaceObject.class), anyInt());
        assertFalse(filter.isShown(item));
    }

    @Test
    public void testIsGetQuery() throws AuthorizeException, SQLException {
        assertEquals("item.public:true", filter.getQuery().getQuery());
    }

    @Test
    public void testIsGetWhere() throws AuthorizeException, SQLException {
        Context ctx = mock(Context.class);
        assertTrue(filter.getWhere(ctx).getWhere().startsWith("EXISTS (SELECT p.action_id FROM "));
    }

}