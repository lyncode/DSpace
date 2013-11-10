package org.dspace.xoai.app;

import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;
import org.apache.log4j.Logger;
import org.dspace.xoai.services.api.cache.XOAICacheService;
import org.dspace.xoai.services.api.cache.XOAIItemCacheService;
import org.dspace.xoai.services.api.cache.XOAILastCompilationCacheService;
import org.dspace.xoai.services.api.config.ConfigurationService;
import org.dspace.xoai.services.api.config.XOAIManagerResolver;
import org.dspace.xoai.services.api.config.XOAIManagerResolverException;
import org.dspace.xoai.services.api.context.ContextService;
import org.dspace.xoai.services.api.solr.SolrServerResolver;
import org.dspace.xoai.services.impl.cache.DSpaceEmptyCacheService;
import org.dspace.xoai.services.impl.cache.DSpaceXOAICacheService;
import org.dspace.xoai.services.impl.cache.DSpaceXOAIItemCacheService;
import org.dspace.xoai.services.impl.cache.DSpaceXOAILastCompilationCacheService;
import org.dspace.xoai.services.impl.config.DSpaceConfigurationService;
import org.dspace.xoai.services.impl.context.DSpaceContextService;
import org.dspace.xoai.services.impl.context.DSpaceXOAIManagerResolver;
import org.dspace.xoai.services.impl.resources.DSpaceResourceResolver;
import org.dspace.xoai.services.impl.solr.DSpaceSolrServerResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicConfiguration {
    private static final Logger log = Logger.getLogger(BasicConfiguration.class);

    @Bean
    public ConfigurationService configurationService() {
        return new DSpaceConfigurationService();
    }

    @Bean
    public ContextService contextService() {
        return new DSpaceContextService();
    }


    @Bean
    public SolrServerResolver solrServerResolver () {
        return new DSpaceSolrServerResolver();
    }


    @Bean
    public XOAIManagerResolver xoaiManagerResolver() {
        return new DSpaceXOAIManagerResolver();
    }

    @Bean
    public XOAICacheService xoaiCacheService() {
        if (configurationService().getBooleanProperty("oai", "cache.enabled", true)) {
            try {
                return new DSpaceXOAICacheService(xoaiManagerResolver().getManager());
            } catch (XOAIManagerResolverException e) {
                log.error("Not able to start XOAI normal cache service.", e);
                return new DSpaceEmptyCacheService();
            }
        } else
            return new DSpaceEmptyCacheService();
    }

    @Bean
    public XOAILastCompilationCacheService xoaiLastCompilationCacheService () {
        return new DSpaceXOAILastCompilationCacheService();
    }

    @Bean
    public XOAIItemCacheService xoaiItemCacheService () {
        return new DSpaceXOAIItemCacheService();
    }


    @Bean
    public ResourceResolver resourceResolver() {
        return new DSpaceResourceResolver();
    }
}
