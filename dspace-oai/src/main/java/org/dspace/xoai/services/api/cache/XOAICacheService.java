package org.dspace.xoai.services.api.cache;

import java.io.IOException;
import java.io.OutputStream;

import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMH;


public interface XOAICacheService {
    boolean hasCache (String requestID);
    void handle (String requestID, OutputStream out) throws IOException;
    void store (String requestID, OAIPMH response) throws IOException;
    void delete (String requestID);
    void deleteAll ();
}
