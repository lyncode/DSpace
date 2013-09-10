package org.dspace.xoai.services.impl.cache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.stax2.XMLOutputFactory2;
import org.dspace.core.ConfigurationManager;
import org.dspace.xoai.services.api.cache.XOAICacheService;

import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.exceptions.WrittingXmlException;
import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMH;
import com.lyncode.xoai.util.Base64Utils;
import com.lyncode.xoai.util.DateUtils;


public class DSpaceXOAICacheService implements XOAICacheService {
    private static XMLOutputFactory factory = XMLOutputFactory2.newFactory();

    private static final String REQUESTDIR = File.separator + "requests";
    private static String baseDir;
    private static String getBaseDir()
    {
        if (baseDir == null)
        {
            String dir = ConfigurationManager.getProperty("oai", "cache.dir") + REQUESTDIR;
            baseDir = dir;
        }
        return baseDir;
    }

    private static String staticHead;
    private static String getStaticHead(Date date)
    {
        if (staticHead == null)
            staticHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + ((XOAIManager.getManager().hasStyleSheet()) ? ("<?xml-stylesheet type=\"text/xsl\" href=\""
                        + XOAIManager.getManager().getStyleSheet() + "\"?>")
                        : "")
                + "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\">";
        
        return staticHead+"<responseDate>" + DateUtils.format(date) + "</responseDate>";
    }
    
    private File getCacheFile (String id) {
        File dir = new File(getBaseDir());
        if (!dir.exists())
            dir.mkdirs();

        String name = File.separator + Base64Utils.encode(id);
        return new File(getBaseDir() + name);
    }
    
    @Override
    public boolean hasCache(String requestID) {
        return this.getCacheFile(requestID).exists();
    }





    @Override
    public void handle(String requestID, OutputStream out) throws IOException {
        InputStream in = new FileInputStream(this.getCacheFile(requestID));
        IOUtils.write(getStaticHead(new Date()), out);
        IOUtils.copy(in, out);
        in.close();
    }





    @Override
    public void store(String requestID, OAIPMH response) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLStreamWriter writer;
        try {
            writer = factory.createXMLStreamWriter(output);
            response.write(writer);
            writer.flush();
            writer.close();
            
            //System.out.println(output.toString());
            
            String xoaiResponse = output.toString();

            // Cutting the header (to allow one to change the response time)
            String end = "</responseDate>";
            int pos = xoaiResponse.indexOf(end);
            if (pos > 0)
                xoaiResponse = xoaiResponse.substring(pos + (end.length()));
            
            FileUtils.write(this.getCacheFile(requestID), xoaiResponse);
        } catch (XMLStreamException e) {
            throw new IOException(e);
        } catch (WrittingXmlException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void delete(String requestID) {
        FileUtils.deleteQuietly(this.getCacheFile(requestID));
    }

    @Override
    public void deleteAll() {
        FileUtils.deleteQuietly(new File(getBaseDir()));
    }

}
