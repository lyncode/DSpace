package org.dspace.xoai.services.impl.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.io.FileUtils;
import org.codehaus.stax2.XMLOutputFactory2;
import org.dspace.content.Item;
import org.dspace.core.ConfigurationManager;
import org.dspace.xoai.services.api.cache.XOAIItemCacheService;

import com.lyncode.xoai.dataprovider.exceptions.WrittingXmlException;
import com.lyncode.xoai.dataprovider.xml.xoai.Metadata;
import com.lyncode.xoai.dataprovider.xml.xoai.XOAIParser;


public class DSpaceXOAIItemCacheService implements XOAIItemCacheService {
    private static XMLOutputFactory factory = XMLOutputFactory2.newFactory();

    private static final String ITEMDIR = File.separator + "items";
    private static String baseDir;
    private static String getBaseDir()
    {
        if (baseDir == null)
        {
            String dir = ConfigurationManager.getProperty("oai", "cache.dir") + ITEMDIR;
            baseDir = dir;
        }
        return baseDir;
    }


    private File getMetadataCache(Item item)
    {
        File dir = new File(getBaseDir());
        if (!dir.exists())
            dir.mkdirs();

        String name = File.separator + item.getHandle().replace('/', '_');
        return new File(getBaseDir() + name);
    }




    @Override
    public boolean hasCache(Item item) {
        return getMetadataCache(item).exists();
    }





    @Override
    public Metadata get(Item item) throws IOException {
        System.out.println(FileUtils.readFileToString(getMetadataCache(item)));
        Metadata metadata;
        FileInputStream input = new FileInputStream(getMetadataCache(item));
        try {
            metadata = XOAIParser.parse(input);
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
        input.close();
        
        return metadata;
    }





    @Override
    public void put(Item item, Metadata metadata) throws IOException {
        FileOutputStream output;
        output = new FileOutputStream(getMetadataCache(item));
        try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(output);
            metadata.write(writer);
            writer.flush();
            writer.close();
            
            output.close();
        } catch (XMLStreamException e1) {
            throw new IOException(e1);
        } catch (WrittingXmlException e1) {
            throw new IOException(e1);
        }
    }


    @Override
    public void delete(Item item) {
        FileUtils.deleteQuietly(this.getMetadataCache(item));
    }


    @Override
    public void deleteAll() {
        FileUtils.deleteQuietly(new File(getBaseDir()));
    }

}
