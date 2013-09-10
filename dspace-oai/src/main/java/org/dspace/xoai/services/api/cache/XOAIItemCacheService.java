package org.dspace.xoai.services.api.cache;

import java.io.IOException;

import org.dspace.content.Item;

import com.lyncode.xoai.dataprovider.xml.xoai.Metadata;


public interface XOAIItemCacheService {
    boolean hasCache (Item item);
    Metadata get (Item item) throws IOException;
    void put (Item item, Metadata metadata) throws IOException;
    void delete (Item item);
    void deleteAll();
}
