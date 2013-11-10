package org.dspace.xoai.services.api.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;

public interface SolrServerResolver {
    SolrServer getServer () throws SolrServerException;
}
