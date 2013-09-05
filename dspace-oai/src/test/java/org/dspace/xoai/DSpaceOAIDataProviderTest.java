package org.dspace.xoai;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Context;
import org.dspace.handle.HandleManager;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.storage.rdbms.TableRowIterator;
import org.dspace.xoai.exceptions.InvalidMetadataFieldException;
import org.dspace.xoai.solr.DSpaceSolrSearch;
import org.dspace.xoai.solr.DSpaceSolrServer;
import org.dspace.xoai.solr.exceptions.DSpaceSolrException;
import org.dspace.xoai.solr.exceptions.SolrSearchEmptyException;
import org.dspace.xoai.util.MetadataFieldManager;
import org.dspace.xoai.util.XOAIDatabaseManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lyncode.xoai.dataprovider.core.ReferenceSet;
import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.exceptions.XSLTransformationException;
import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMHerrorcodeType;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;
import com.lyncode.xoai.util.DateUtils;
import com.lyncode.xoai.util.XSLTUtils;


@PrepareForTest({ ConfigurationManager.class, XSLTUtils.class, DSpaceSolrSearch.class, DatabaseManager.class, Context.class, MetadataFieldManager.class, HandleManager.class, XOAIDatabaseManager.class, com.lyncode.xoai.dataprovider.configuration.ConfigurationManager.class, DSpaceSolrServer.class  })
@RunWith(PowerMockRunner.class)
public class DSpaceOAIDataProviderTest {
    DSpaceOAIDataProvider provider;
    HttpServletRequest request;
    HttpServletResponse response;
    ServletOutputStream output;
    
    public class StubServletOutputStream extends ServletOutputStream {
        public ByteArrayOutputStream baos = new ByteArrayOutputStream();
          public void write(int i) throws IOException {
           baos.write(i);
        }
          
          public String toString () {
              return baos.toString();
          }
       }
    
    @Before
    public void setup () throws ConfigurationException, SolrServerException, SQLException, InvalidMetadataFieldException, IOException, SolrSearchEmptyException, DSpaceSolrException, XSLTransformationException {
        SolrServer server = mock(SolrServer.class);
        Connection connection = mock(Connection.class);
        PowerMockito.mockStatic(DSpaceSolrSearch.class);
        PowerMockito.mockStatic(XSLTUtils.class);

        when(XSLTUtils.transform(any(File.class), any(AbstractItem.class))).thenReturn("<hello></hello>");
        when(XSLTUtils.transform(any(File.class), any(File.class), any(AbstractItem.class))).thenReturn("<hello></hello>");
        
        SolrDocument doc1 = mock(SolrDocument.class);
        when(doc1.getFieldValue(eq("item.id"))).thenReturn("one:simple:id");
        when(doc1.getFieldValue(eq("item.compile"))).thenReturn("<test></test>");
        when(doc1.getFieldValue(eq("item.handle"))).thenReturn("123/12");
        when(doc1.getFieldValue(eq("item.lastmodified"))).thenReturn(new Date());
        when(doc1.getFieldValue(eq("item.communities"))).thenReturn(new ArrayList<String>());
        when(doc1.getFieldValue(eq("item.collections"))).thenReturn(new ArrayList<String>());
        when(doc1.getFieldValue(eq("item.deleted"))).thenReturn(false);
        
        SolrDocumentList documentList = new SolrDocumentList();
        documentList.add(doc1);
        
        when(DSpaceSolrSearch.querySingle(any(SolrQuery.class))).thenReturn(doc1);
        when(DSpaceSolrSearch.query(any(SolrQuery.class))).thenReturn(documentList);
        
        output = new StubServletOutputStream();
        
        
        Configuration config = com.lyncode.xoai.dataprovider.configuration.ConfigurationManager.readConfiguration(this.getClass().getResourceAsStream("xoai.xml"));
        
        PowerMockito.mockStatic(ConfigurationManager.class);
        PowerMockito.mockStatic(HandleManager.class);
        PowerMockito.mockStatic(DSpaceSolrServer.class);
        PowerMockito.mockStatic(DatabaseManager.class);
        PowerMockito.mockStatic(com.lyncode.xoai.dataprovider.configuration.ConfigurationManager.class);

        PowerMockito.mockStatic(MetadataFieldManager.class);
        when(MetadataFieldManager.getFieldID(any(Context.class), anyString())).thenReturn(1);

        when(ConfigurationManager.getProperty(eq("oai"), eq("description.file"))).thenReturn(null);
        when(ConfigurationManager.getProperty(anyString())).thenReturn("hello");
        when(ConfigurationManager.getProperty(anyString(), anyString())).thenReturn("hello");
        when(ConfigurationManager.getBooleanProperty(eq("oai"), eq("cache.enabled"), anyBoolean())).thenReturn(false);
        when(com.lyncode.xoai.dataprovider.configuration.ConfigurationManager.readConfiguration(anyString())).thenReturn(config);
        when(DSpaceSolrServer.getServer()).thenReturn(server);
        when(DatabaseManager.getConnection()).thenReturn(connection);
        
        TableRowIterator iterator = mock(TableRowIterator.class);
        TableRow row = mock(TableRow.class);
        
        when(row.getStringColumn("value")).thenReturn(DateUtils.format(new Date()));
        when(iterator.next()).thenReturn(row);
        
        when(DatabaseManager.query(any(Context.class), anyString(), eq(1))).thenReturn(iterator);
        
        provider = new DSpaceOAIDataProvider();
        provider.init();
        
        request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("/request");
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/oai/request"));
        response = mock(HttpServletResponse.class);
        
        when(response.getOutputStream()).thenReturn(output);
    }

    @Test
    public void testIdentifyVerb() throws ServletException, IOException {
        when(request.getParameterNames()).thenReturn(Collections.enumeration(Arrays.asList(new String[]{ "verb" })));
        when(request.getParameterValues("verb")).thenReturn(new String[] { "Identify" });
        
        provider.doGet(request, response);
        
        assertTrue(response.getOutputStream().toString().contains("Identify"));
    }
    
    @Test
    public void testBadVerb() throws ServletException, IOException {
        when(request.getParameterNames()).thenReturn(Collections.enumeration(Arrays.asList(new String[]{ "verb" })));
        when(request.getParameterValues("verb")).thenReturn(new String[] { "wrongVerb" });
        
        provider.doGet(request, response);
        
        assertTrue(response.getOutputStream().toString().contains(OAIPMHerrorcodeType.BAD_VERB.value()));
    }
    
    @Test
    public void testDuplicatedVerb() throws ServletException, IOException {
        when(request.getParameterNames()).thenReturn(Collections.enumeration(Arrays.asList(new String[]{ "verb" })));
        when(request.getParameterValues("verb")).thenReturn(new String[] { "wrongVerb", "Identify" });
        
        provider.doGet(request, response);
        
        assertTrue(response.getOutputStream().toString().contains(OAIPMHerrorcodeType.BAD_VERB.value()));
    }
    
    @Test
    public void testMetadataFormats() throws ServletException, IOException {
        when(request.getParameterNames()).thenReturn(Collections.enumeration(Arrays.asList(new String[]{ "verb" })));
        when(request.getParameterValues("verb")).thenReturn(new String[] { "ListMetadataFormats" });
        
        provider.doGet(request, response);
        
        // System.out.println(response.getOutputStream().toString());
        
        assertTrue(response.getOutputStream().toString().contains("oai_dc"));
    }

    @Test
    public void testMetadataFormatsForItem() throws ServletException, IOException {
        when(request.getParameterNames()).thenReturn(Collections.enumeration(Arrays.asList(new String[]{ "verb", "identifier" })));
        when(request.getParameterValues("verb")).thenReturn(new String[] { "ListMetadataFormats" });
        when(request.getParameterValues("identifier")).thenReturn(new String[] { "one:simple:id" });
        
        provider.doGet(request, response);
        
        //System.out.println(response.getOutputStream().toString());
        
        assertTrue(response.getOutputStream().toString().contains("oai_dc"));
    }
    
    @Test
    public void testListRecords() throws ServletException, IOException {
        when(request.getParameterNames()).thenReturn(Collections.enumeration(Arrays.asList(new String[]{ "verb", "metadataPrefix" })));
        when(request.getParameterValues("verb")).thenReturn(new String[] { "ListRecords" });
        when(request.getParameterValues("metadataPrefix")).thenReturn(new String[] { "oai_dc" });
        
        provider.doGet(request, response);
        
        //System.out.println(response.getOutputStream().toString());
        
        assertTrue(response.getOutputStream().toString().contains("oai_dc"));
    }

}
