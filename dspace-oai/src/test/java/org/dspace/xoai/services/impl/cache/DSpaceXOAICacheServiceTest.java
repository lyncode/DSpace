package org.dspace.xoai.services.impl.cache;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.dspace.core.ConfigurationManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMH;
import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMHerrorType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMHerrorcodeType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMHtype;
import com.lyncode.xoai.dataprovider.xml.oaipmh.RequestType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.VerbType;


@PrepareForTest({ ConfigurationManager.class, XOAIManager.class })
@RunWith(PowerMockRunner.class)
public class DSpaceXOAICacheServiceTest {
    static final String REQUEST_ID = "test";

    @Test
    public void shouldHaveNoRequest() {
        PowerMockito.mockStatic(ConfigurationManager.class);
        
        File f = FileUtils.getTempDirectory();
        f.mkdirs();
        
        PowerMockito.when(ConfigurationManager.getProperty(eq("oai"), eq("cache.dir"))).thenReturn(f.getAbsolutePath());
        
        DSpaceXOAICacheService cache = new DSpaceXOAICacheService();
        assertFalse(cache.hasCache(REQUEST_ID));
        
        FileUtils.deleteQuietly(f);
    }

    @Test
    public void shouldHaveRequest() throws IOException {
        PowerMockito.mockStatic(ConfigurationManager.class);
        
        File f = FileUtils.getTempDirectory();
        f.mkdirs();
        
        PowerMockito.when(ConfigurationManager.getProperty(eq("oai"), eq("cache.dir"))).thenReturn(f.getAbsolutePath());
        
        DSpaceXOAICacheService cache = new DSpaceXOAICacheService();
        cache.store(REQUEST_ID, new OAIPMH());
        assertTrue(cache.hasCache(REQUEST_ID));
        
        FileUtils.deleteQuietly(f);
    }
    
    @Test
    public void storeItGood() throws IOException {
        PowerMockito.mockStatic(XOAIManager.class);
        PowerMockito.mockStatic(ConfigurationManager.class);
        XOAIManager instance = mock(XOAIManager.class);
        when(instance.hasStyleSheet()).thenReturn(false);
        
        File f = FileUtils.getTempDirectory(); f.mkdirs();
        PowerMockito.when(ConfigurationManager.getProperty(eq("oai"), eq("cache.dir"))).thenReturn(f.getAbsolutePath());
        PowerMockito.when(XOAIManager.getManager()).thenReturn(instance);
        

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OAIPMH data = new OAIPMH();
        data.setInfo(new OAIPMHtype());
        data.getInfo().setResponseDate(new Date());
        data.getInfo().setRequest(new RequestType());
        data.getInfo().getRequest().setVerb(VerbType.GET_RECORD);
        data.getInfo().getError().add(new OAIPMHerrorType());
        data.getInfo().getError().get(0).setCode(OAIPMHerrorcodeType.BAD_ARGUMENT);
        data.getInfo().getError().get(0).setValue("SAD");
        
        DSpaceXOAICacheService cache = new DSpaceXOAICacheService();
        cache.store(REQUEST_ID, data);
        cache.handle(REQUEST_ID, out);
        
        assertTrue(out.toString().contains(OAIPMHerrorcodeType.BAD_ARGUMENT.value()));
        
        FileUtils.deleteQuietly(f);
    }

}
