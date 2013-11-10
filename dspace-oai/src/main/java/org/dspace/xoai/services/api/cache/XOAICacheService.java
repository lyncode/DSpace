package org.dspace.xoai.services.api.cache;

import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMH;

import java.io.IOException;
import java.io.OutputStream;


public interface XOAICacheService {
    boolean isActive ();
    boolean hasCache (String requestID);
    void handle (String requestID, OutputStream out) throws IOException;
    void store (String requestID, OAIPMH response) throws IOException;
    void delete (String requestID);
    void deleteAll ();
}
