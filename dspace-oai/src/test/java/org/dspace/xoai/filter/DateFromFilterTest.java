package org.dspace.xoai.filter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.dspace.core.Context;
import org.dspace.xoai.data.DSpaceItem;
import org.junit.Test;

import com.ibm.icu.util.Calendar;


public class DateFromFilterTest {

    @Test
    public void testGetWhere() {
        Context ctx = mock(Context.class);
        
        Calendar c = Calendar.getInstance();
        c.set(2000, 0, 1, 0, 0, 0);
        DateFromFilter filter = new DateFromFilter(c.getTime());
        
        assertEquals("i.last_modified >= ?", filter.getWhere(ctx).getWhere());
        assertEquals(c.getTime().getTime(), ((Date) filter.getWhere(ctx).getParameters().get(0)).getTime());
    }





    @Test
    public void testGetQuery() {
        Calendar c = Calendar.getInstance();
        c.set(2000, 0, 1, 0, 0, 0);
        DateFromFilter filter = new DateFromFilter(c.getTime());
        assertEquals(filter.getQuery().getQuery(), "item.lastmodified:[2000\\-01\\-01T00\\:00\\:00.000Z TO *]");
    }





    @Test
    public void testIsShown() {
        DSpaceItem item = mock(DSpaceItem.class);
        when(item.getDatestamp()).thenReturn(new Date());
        
        Calendar c = Calendar.getInstance();
        c.set(2000, 0, 1, 0, 0, 0);
        DateFromFilter filter = new DateFromFilter(c.getTime());
        assertTrue(filter.isShown(item));
    }
    @Test
    public void testNotShown() {
        Calendar c = Calendar.getInstance();
        c.set(2000, 0, 1, 0, 0, 0);
        DSpaceItem item = mock(DSpaceItem.class);
        when(item.getDatestamp()).thenReturn(c.getTime());
        
        DateFromFilter filter = new DateFromFilter(new Date());
        assertFalse(filter.isShown(item));
    }

}
