package org.dspace.xoai.services.impl.solr;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.dspace.xoai.services.api.config.ConfigurationService;
import org.dspace.xoai.services.api.solr.SolrServerResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;

public class DSpaceSolrServerResolver implements SolrServerResolver {
    private static Logger log = LogManager.getLogger(DSpaceSolrServerResolver.class);
    private static SolrServer server = null;

    @Autowired
    private ConfigurationService configurationService;

    @Override
    public SolrServer getServer() throws SolrServerException
    {
        if (server == null)
        {
            try
            {
                server = new CommonsHttpSolrServer(configurationService.getProperty("oai", "solr.url"));
                log.debug("Solr Server Initialized");
            }
            catch (MalformedURLException e)
            {
                throw new SolrServerException(e);
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
            }
        }
        return server;
    }
}
