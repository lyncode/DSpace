package org.dspace.xoai;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
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
    public static String XOAI_CFG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
    		"<!-- \r\n" + 
    		"\r\n" + 
    		"    The contents of this file are subject to the license and copyright\r\n" + 
    		"    detailed in the LICENSE and NOTICE files at the root of the source\r\n" + 
    		"    tree and available online at\r\n" + 
    		"\r\n" + 
    		"    http://www.dspace.org/license/\r\n" + 
    		"\r\n" + 
    		"    Developed by DSpace @ Lyncode <dspace@lyncode.com>\r\n" + 
    		" -->\r\n" + 
    		"<Configuration indentation=\"false\" maxListIdentifiersSize=\"100\" maxListRecordsSize=\"100\"\r\n" + 
    		"    maxListSetsSize=\"100\" stylesheet=\"static/style.xsl\" \r\n" + 
    		"    >\r\n" + 
    		"\r\n" + 
    		"    <Contexts>\r\n" + 
    		"        <Context baseurl=\"request\">\r\n" + 
    		"            <Format refid=\"oaidc\" />\r\n" + 
    		"            <Format refid=\"mets\" />\r\n" + 
    		"            <Format refid=\"xoai\" />\r\n" + 
    		"            <Format refid=\"didl\" />\r\n" + 
    		"            <Format refid=\"dim\" />\r\n" + 
    		"            <Format refid=\"ore\" />\r\n" + 
    		"            <Format refid=\"rdf\" />\r\n" + 
    		"            <Format refid=\"etdms\" />\r\n" + 
    		"            <Format refid=\"mods\" />\r\n" + 
    		"            <Format refid=\"qdc\" />\r\n" + 
    		"            <Format refid=\"marc\" />\r\n" + 
    		"            <Format refid=\"uketd_dc\" />\r\n" + 
    		"        </Context> \r\n" + 
    		"        \r\n" + 
    		"        <!--\r\n" + 
    		"            Driver Guidelines:\r\n" + 
    		"         \r\n" + 
    		"            - http://www.driver-support.eu/documents/DRIVER_Guidelines_v2_Final_2008-11-13.pdf\r\n" + 
    		"            \r\n" + 
    		"            Page 57 - 58\r\n" + 
    		"         -->\r\n" + 
    		"        <Context baseurl=\"driver\">\r\n" + 
    		"            <!-- Date format, field prefixes, etc are ensured by the transformer -->\r\n" + 
    		"            <Transformer refid=\"driverTransformer\"/>\r\n" + 
    		"            <!-- Title : Mandatory -->\r\n" + 
    		"            <Filter refid=\"titleexistsFilter\" />\r\n" + 
    		"            <!-- Author : Mandatory -->\r\n" + 
    		"            <Filter refid=\"authorexistsFilter\"/>\r\n" + 
    		"            <!-- Possible Document Type -->\r\n" + 
    		"            <Filter refid=\"driverdocumenttypeFilter\"/>\r\n" + 
    		"            <!-- dc.rights must contain openAccess token -->\r\n" + 
    		"            <Filter refid=\"driveraccessFilter\"/>\r\n" + 
    		"            <!-- Open Access (Downloadable) -->\r\n" + 
    		"            <Filter refid=\"bitstreamaccessFilter\"/>\r\n" + 
    		"            <!-- Just an alias, if fact it returns all items within the driver context -->\r\n" + 
    		"            <Set refid=\"driverSet\" />\r\n" + 
    		"            <!-- Metadata Formats -->\r\n" + 
    		"            <Format refid=\"oaidc\"/>\r\n" + 
    		"            <Format refid=\"mets\" />\r\n" + 
    		"            <Format refid=\"didl\" />\r\n" + 
    		"        </Context>\r\n" + 
    		"        \r\n" + 
    		"        <!-- \r\n" + 
    		"            OpenAIRE Guidelines 1.1:\r\n" + 
    		"            \r\n" + 
    		"            - http://www.openaire.eu/en/component/attachments/download/79%E2%8C%A9=en\r\n" + 
    		"            \r\n" + 
    		"            There is a limitation over the embargoedEndDate parameter:\r\n" + 
    		"            \r\n" + 
    		"            - Predefined DSpace fields don't allow to set this up with a default.\r\n" + 
    		"         -->\r\n" + 
    		"        <Context baseurl=\"openaire\">\r\n" + 
    		"            <!-- Date format, field prefixes, etc are ensured by the transformer -->\r\n" + 
    		"            <Transformer refid=\"openaireTransformer\" />\r\n" + 
    		"            <!-- Title : Mandatory -->\r\n" + 
    		"            <Filter refid=\"titleexistsFilter\" />\r\n" + 
    		"            <!-- Author : Mandatory -->\r\n" + 
    		"            <Filter refid=\"authorexistsFilter\"/>\r\n" + 
    		"            <!-- Possible Document Type (same as Driver)-->\r\n" + 
    		"            <Filter refid=\"driverdocumenttypeFilter\"/>\r\n" + 
    		"            <!-- dc.relation must has the specified prefix -->\r\n" + 
    		"            <Filter refid=\"openairerelationFilter\" />\r\n" + 
    		"            <!-- Just an alias, if fact it returns all items within the driver context -->\r\n" + 
    		"            <Set refid=\"openaireSet\" />\r\n" + 
    		"            <!-- Metadata Formats -->\r\n" + 
    		"            <Format refid=\"oaidc\" />\r\n" + 
    		"            <Format refid=\"mets\" />\r\n" + 
    		"        </Context>\r\n" + 
    		"    </Contexts>\r\n" + 
    		"    \r\n" + 
    		"    \r\n" + 
    		"    <Formats>\r\n" + 
    		"        <Format id=\"oaidc\">\r\n" + 
    		"            <Prefix>oai_dc</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/oai_dc.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.openarchives.org/OAI/2.0/oai_dc/</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.openarchives.org/OAI/2.0/oai_dc.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"mets\">\r\n" + 
    		"            <Prefix>mets</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/mets.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.loc.gov/METS/</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.loc.gov/standards/mets/mets.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <!-- Shows the XOAI internal generated XML -->\r\n" + 
    		"        <Format id=\"xoai\">\r\n" + 
    		"            <Prefix>xoai</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/xoai.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.lyncode.com/xoai</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.lyncode.com/schemas/xoai.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"didl\">\r\n" + 
    		"            <Prefix>didl</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/didl.xsl</XSLT>\r\n" + 
    		"            <Namespace>urn:mpeg:mpeg21:2002:02-DIDL-NS</Namespace>\r\n" + 
    		"            <SchemaLocation>http://standards.iso.org/ittf/PubliclyAvailableStandards/MPEG-21_schema_files/did/didl.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"dim\">\r\n" + 
    		"            <Prefix>dim</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/dim.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.dspace.org/xmlns/dspace/dim</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.dspace.org/schema/dim.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"ore\">\r\n" + 
    		"            <Prefix>ore</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/ore.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.w3.org/2005/Atom</Namespace>\r\n" + 
    		"            <SchemaLocation>http://tweety.lanl.gov/public/schemas/2008-06/atom-tron.sch</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"rdf\">\r\n" + 
    		"            <Prefix>rdf</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/rdf.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.openarchives.org/OAI/2.0/rdf/</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.openarchives.org/OAI/2.0/rdf.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"etdms\">\r\n" + 
    		"            <Prefix>etdms</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/etdms.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.ndltd.org/standards/metadata/etdms/1.0/</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.ndltd.org/standards/metadata/etdms/1.0/etdms.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"mods\">\r\n" + 
    		"            <Prefix>mods</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/mods.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.loc.gov/mods/v3</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.loc.gov/standards/mods/v3/mods-3-1.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"qdc\">\r\n" + 
    		"            <Prefix>qdc</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/qdc.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://purl.org/dc/terms/</Namespace>\r\n" + 
    		"            <SchemaLocation>http://dublincore.org/schemas/xmls/qdc/2006/01/06/dcterms.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"marc\">\r\n" + 
    		"            <Prefix>marc</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/marc.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://www.loc.gov/MARC21/slim</Namespace>\r\n" + 
    		"            <SchemaLocation>http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"        <Format id=\"uketd_dc\">\r\n" + 
    		"            <Prefix>uketd_dc</Prefix>\r\n" + 
    		"            <XSLT>metadataFormats/uketd_dc.xsl</XSLT>\r\n" + 
    		"            <Namespace>http://naca.central.cranfield.ac.uk/ethos-oai/2.0/</Namespace>\r\n" + 
    		"            <SchemaLocation>http://naca.central.cranfield.ac.uk/ethos-oai/2.0/uketd_dc.xsd</SchemaLocation>\r\n" + 
    		"        </Format>\r\n" + 
    		"    </Formats>\r\n" + 
    		"    \r\n" + 
    		"    <Transformers>\r\n" + 
    		"        <Transformer id=\"driverTransformer\">\r\n" + 
    		"            <XSLT>transformers/driver.xsl</XSLT>\r\n" + 
    		"        </Transformer>\r\n" + 
    		"        <Transformer id=\"openaireTransformer\">\r\n" + 
    		"            <XSLT>transformers/openaire.xsl</XSLT>\r\n" + 
    		"        </Transformer>\r\n" + 
    		"    </Transformers>\r\n" + 
    		"    \r\n" + 
    		"\r\n" + 
    		"    <Filters>\r\n" + 
    		"        <CustomFilter id=\"authorexistsCondition\">\r\n" + 
    		"            <Class>org.dspace.xoai.filter.DSpaceMetadataExistsFilter</Class>\r\n" + 
    		"            <Parameter key=\"fields\">\r\n" + 
    		"                <Value>dc.contributor.author</Value>\r\n" + 
    		"            </Parameter>\r\n" + 
    		"        </CustomFilter>\r\n" + 
    		"        <CustomFilter id=\"titleexistsCondition\">\r\n" + 
    		"            <Class>org.dspace.xoai.filter.DSpaceMetadataExistsFilter</Class>\r\n" + 
    		"            <Parameter key=\"fields\">\r\n" + 
    		"                <Value>dc.title</Value>\r\n" + 
    		"            </Parameter>\r\n" + 
    		"        </CustomFilter>\r\n" + 
    		"        <CustomFilter id=\"driverdocumenttypeCondition\">\r\n" + 
    		"            <Class>org.dspace.xoai.filter.DSpaceAtLeastOneMetadataFilter</Class>\r\n" + 
    		"            <Parameter key=\"field\">\r\n" + 
    		"                <Value>dc.type</Value>\r\n" + 
    		"            </Parameter>\r\n" + 
    		"            <Parameter key=\"operator\">\r\n" + 
    		"                <Value>ends_with</Value>\r\n" + 
    		"            </Parameter>\r\n" + 
    		"            <Parameter key=\"value\">\r\n" + 
    		"                <Value>article</Value>\r\n" + 
    		"                <Value>bachelorThesis</Value>\r\n" + 
    		"                <Value>masterThesis</Value>\r\n" + 
    		"                <Value>doctoralThesis</Value>\r\n" + 
    		"                <Value>book</Value>\r\n" + 
    		"                <Value>bookPart</Value>\r\n" + 
    		"                <Value>review</Value>\r\n" + 
    		"                <Value>conferenceObject</Value>\r\n" + 
    		"                <Value>lecture</Value>\r\n" + 
    		"                <Value>workingPaper</Value>\r\n" + 
    		"                <Value>preprint</Value>\r\n" + 
    		"                <Value>report</Value>\r\n" + 
    		"                <Value>annotation</Value>\r\n" + 
    		"                <Value>contributionToPeriodical</Value>\r\n" + 
    		"                <Value>patent</Value>\r\n" + 
    		"                <Value>other</Value>\r\n" + 
    		"            </Parameter>\r\n" + 
    		"        </CustomFilter>\r\n" + 
    		"        <CustomFilter id=\"driveraccessCondition\">\r\n" + 
    		"            <Class>org.dspace.xoai.filter.DSpaceAtLeastOneMetadataFilter</Class>\r\n" + 
    		"            <Parameter key=\"field\">\r\n" + 
    		"                <Value>dc.rights</Value>\r\n" + 
    		"            </Parameter>\r\n" + 
    		"            <Parameter key=\"operator\">\r\n" + 
    		"                <Value>contains</Value>\r\n" + 
    		"            </Parameter>\r\n" + 
    		"            <Parameter key=\"value\">\r\n" + 
    		"                <Value>open access</Value>\r\n" + 
    		"                <Value>openAccess</Value>\r\n" + 
    		"            </Parameter>\r\n" + 
    		"        </CustomFilter>\r\n" + 
    		"        <CustomFilter id=\"bitstreamaccessCondition\">\r\n" + 
    		"            <Class>org.dspace.xoai.filter.DSpaceAuthorizationFilter</Class>\r\n" + 
    		"        </CustomFilter>\r\n" + 
    		"        <CustomFilter id=\"openairerelationCondition\">\r\n" + 
    		"            <Class>org.dspace.xoai.filter.DSpaceAtLeastOneMetadataFilter</Class>\r\n" + 
    		"            <Parameter key=\"field\">\r\n" + 
    		"                <Value>dc.relation</Value>\r\n" + 
    		"            </Parameter>\r\n" + 
    		"            <Parameter key=\"operator\">\r\n" + 
    		"                <Value>starts_with</Value>\r\n" + 
    		"            </Parameter>\r\n" + 
    		"            <Parameter key=\"value\">\r\n" + 
    		"                <Value>info:eu-repo/grantAgreement/EC/FP</Value>\r\n" + 
    		"            </Parameter>\r\n" + 
    		"        </CustomFilter>\r\n" + 
    		"        \r\n" + 
    		"        <Filter id=\"authorexistsFilter\">\r\n" + 
    		"            <definition>\r\n" + 
    		"                <Custom refid=\"authorexistsCondition\" />\r\n" + 
    		"            </definition>\r\n" + 
    		"        </Filter>\r\n" + 
    		"        \r\n" + 
    		"        <Filter id=\"titleexistsFilter\">\r\n" + 
    		"            <definition>\r\n" + 
    		"                <Custom refid=\"titleexistsCondition\"/>\r\n" + 
    		"            </definition>\r\n" + 
    		"        </Filter>\r\n" + 
    		"        \r\n" + 
    		"        <Filter id=\"driverdocumenttypeFilter\">\r\n" + 
    		"            <definition>\r\n" + 
    		"                <Custom refid=\"driverdocumenttypeCondition\"/>\r\n" + 
    		"            </definition>\r\n" + 
    		"        </Filter>\r\n" + 
    		"        \r\n" + 
    		"        <Filter id=\"driveraccessFilter\">\r\n" + 
    		"            <definition>\r\n" + 
    		"                <Custom refid=\"driveraccessCondition\"/>\r\n" + 
    		"            </definition>\r\n" + 
    		"        </Filter>\r\n" + 
    		"        <Filter id=\"bitstreamaccessFilter\">\r\n" + 
    		"            <definition>\r\n" + 
    		"                <Custom refid=\"bitstreamaccessCondition\"/>\r\n" + 
    		"            </definition>\r\n" + 
    		"        </Filter>\r\n" + 
    		"        <Filter id=\"openairerelationFilter\">\r\n" + 
    		"            <definition>\r\n" + 
    		"                <Custom refid=\"openairerelationCondition\"/>\r\n" + 
    		"            </definition>\r\n" + 
    		"        </Filter>\r\n" + 
    		"    </Filters>\r\n" + 
    		"    \r\n" + 
    		"    <Sets>\r\n" + 
    		"        <Set id=\"driverSet\">\r\n" + 
    		"            <Pattern>driver</Pattern>\r\n" + 
    		"            <Name>Open Access DRIVERset</Name>\r\n" + 
    		"            <!-- Just an alias -->\r\n" + 
    		"        </Set>\r\n" + 
    		"        <Set id=\"openaireSet\">\r\n" + 
    		"            <Pattern>ec_fundedresources</Pattern>\r\n" + 
    		"            <Name>EC_fundedresources set</Name>\r\n" + 
    		"            <!-- Just an alias -->\r\n" + 
    		"        </Set>\r\n" + 
    		"    </Sets>\r\n" + 
    		"</Configuration>\r\n" + 
    		"";
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
        
        
        Configuration config = com.lyncode.xoai.dataprovider.configuration.ConfigurationManager.readConfiguration(new ByteArrayInputStream(XOAI_CFG.getBytes()));
        
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
