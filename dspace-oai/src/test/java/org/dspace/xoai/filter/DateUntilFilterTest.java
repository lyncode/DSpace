package org.dspace.xoai.filter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.dspace.core.Context;
import org.dspace.xoai.data.DSpaceItem;
import org.junit.Test;

import com.ibm.icu.util.Calendar;


public class DateUntilFilterTest {

    @Test
    public void testGetWhere() {
        Context ctx = mock(Context.class);
        
        Calendar c = Calendar.getInstance();
        c.set(2000, 0, 1, 0, 0, 0);
        DateUntilFilter filter = new DateUntilFilter(c.getTime());
        
        assertEquals("i.last_modified <= ?", filter.getWhere(ctx).getWhere());
        assertEquals(c.getTime().getTime(), ((Date) filter.getWhere(ctx).getParameters().get(0)).getTime());
    }





    @Test
    public void testGetQuery() {
        Calendar c = Calendar.getInstance();
        c.set(2000, 0, 1, 0, 0, 0);
        DateUntilFilter filter = new DateUntilFilter(c.getTime());
        assertEquals(filter.getQuery().getQuery(), "item.lastmodified:[* TO 2000\\-01\\-01T00\\:00\\:00.999Z]");
        
    }





    @Test
    public void testIsShown() {
        Calendar c = Calendar.getInstance();
        c.set(2000, 0, 1, 0, 0, 0);
        DSpaceItem item = mock(DSpaceItem.class);
        when(item.getDatestamp()).thenReturn(c.getTime());
        DateUntilFilter filter = new DateUntilFilter(new Date());
        assertTrue(filter.isShown(item));
    }
    @Test
    public void testNotShown() {
        Calendar c = Calendar.getInstance();
        c.set(2000, 0, 1, 0, 0, 0);
        DSpaceItem item = mock(DSpaceItem.class);
        when(item.getDatestamp()).thenReturn(new Date());
        DateUntilFilter filter = new DateUntilFilter(c.getTime());
        assertFalse(filter.isShown(item));
    }
}
