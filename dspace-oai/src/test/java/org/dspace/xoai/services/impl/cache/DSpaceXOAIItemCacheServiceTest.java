package org.dspace.xoai.services.impl.cache;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.dspace.content.Item;
import org.dspace.core.ConfigurationManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.xml.xoai.Element;
import com.lyncode.xoai.dataprovider.xml.xoai.Element.Field;
import com.lyncode.xoai.dataprovider.xml.xoai.Metadata;


@PrepareForTest({ ConfigurationManager.class, XOAIManager.class })
@RunWith(PowerMockRunner.class)
public class DSpaceXOAIItemCacheServiceTest {
    Item item;
    Metadata metadata;
    
    @Before
    public void setup () {
        item = mock(Item.class);
        when(item.getHandle()).thenReturn("123456789/4");
        
        metadata = new Metadata();
        metadata.getElement().add(new Element());
        metadata.getElement().get(0).getField().add(new Field());
        metadata.getElement().get(0).getField().get(0).setName("A");
        metadata.getElement().get(0).getField().get(0).setValue("B");
    }

    @Test
    public void shouldHaveNoRequest() throws IOException {
        PowerMockito.mockStatic(ConfigurationManager.class);
        
        File f = FileUtils.getTempDirectory();
        f.mkdirs();
        
        PowerMockito.when(ConfigurationManager.getProperty(eq("oai"), eq("cache.dir"))).thenReturn(f.getAbsolutePath());
        
        DSpaceXOAIItemCacheService cache = new DSpaceXOAIItemCacheService();
        assertFalse(cache.hasCache(item));
        
        FileUtils.deleteQuietly(f);
    }


    @Test
    public void shouldHaveRequest() throws IOException {
        PowerMockito.mockStatic(ConfigurationManager.class);
        
        File f = FileUtils.getTempDirectory();
        f.mkdirs();
        
        PowerMockito.when(ConfigurationManager.getProperty(eq("oai"), eq("cache.dir"))).thenReturn(f.getAbsolutePath());
        
        DSpaceXOAIItemCacheService cache = new DSpaceXOAIItemCacheService();
        cache.put(item, metadata);
        assertTrue(cache.hasCache(item));

        if (f.exists())
            FileUtils.deleteQuietly(f);
    }
    

    @Test
    public void shouldStoreItGood() throws IOException {
        PowerMockito.mockStatic(ConfigurationManager.class);
        
        File f = FileUtils.getTempDirectory();
        f.mkdirs();
        
        PowerMockito.when(ConfigurationManager.getProperty(eq("oai"), eq("cache.dir"))).thenReturn(f.getAbsolutePath());
        
        DSpaceXOAIItemCacheService cache = new DSpaceXOAIItemCacheService();
        cache.put(item, metadata);
        
        Metadata m = cache.get(item);
        
        assertEquals(metadata.toString(), m.toString());

        if (f.exists())
            FileUtils.deleteQuietly(f);
    }
}
