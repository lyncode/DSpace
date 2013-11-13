package org.dspace.xoai.services.impl.config;

import com.lyncode.xoai.dataprovider.data.AbstractItemRepository;
import org.apache.solr.client.solrj.SolrServerException;
import org.dspace.xoai.data.DSpaceItemDatabaseRepository;
import org.dspace.xoai.data.DSpaceItemSolrRepository;
import org.dspace.xoai.services.api.config.ConfigurationService;
import org.dspace.xoai.services.api.config.ItemRepositoryResolver;
import org.dspace.xoai.services.api.context.ContextService;
import org.dspace.xoai.services.api.context.ContextServiceException;
import org.dspace.xoai.services.api.solr.SolrServerResolver;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class DSpaceItemRepositoryResolver implements ItemRepositoryResolver {
    @Autowired
    ContextService contextService;
    @Autowired
    ConfigurationService configurationService;
    @Autowired
    SolrServerResolver solrServerResolver;

    private AbstractItemRepository itemRepository;

    @Override
    public AbstractItemRepository getItemRepository(HttpServletRequest request) throws ContextServiceException {
        if (itemRepository == null) {
            String storage = configurationService.getProperty("oai", "storage");
            if (storage == null || !storage.trim().toLowerCase().equals("database")) {
                try {
                    itemRepository = new DSpaceItemSolrRepository(solrServerResolver.getServer());
                } catch (SolrServerException e) {
                    throw new ContextServiceException(e.getMessage(), e);
                }
            } else
                itemRepository = new DSpaceItemDatabaseRepository(contextService.getContext(request));
        }

        return itemRepository;
    }
}
