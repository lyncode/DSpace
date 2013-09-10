package org.dspace.xoai.services.api.cache;

import java.io.IOException;
import java.util.Date;


public interface XOAILastCompilationCacheService {
    boolean hasCache ();
    void put (Date date) throws IOException;
    Date get () throws IOException;
}
