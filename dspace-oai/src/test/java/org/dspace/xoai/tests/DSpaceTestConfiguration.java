package org.dspace.xoai.tests;

import com.lyncode.xoai.dataprovider.data.AbstractItemRepository;
import com.lyncode.xoai.dataprovider.data.AbstractSetRepository;
import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.CoreDescriptor;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrResourceLoader;
import org.dspace.core.Context;
import org.dspace.xoai.data.DSpaceItemSolrRepository;
import org.dspace.xoai.services.api.cache.XOAICacheService;
import org.dspace.xoai.services.api.config.ConfigurationService;
import org.dspace.xoai.services.api.config.ItemRepositoryResolver;
import org.dspace.xoai.services.api.config.XOAIManagerResolver;
import org.dspace.xoai.services.api.context.ContextService;
import org.dspace.xoai.services.api.context.ContextServiceException;
import org.dspace.xoai.services.api.database.EarliestDateResolver;
import org.dspace.xoai.services.api.database.FieldResolver;
import org.dspace.xoai.services.api.xoai.IdentifyResolver;
import org.dspace.xoai.services.api.xoai.SetRepositoryResolver;
import org.dspace.xoai.services.impl.cache.DSpaceEmptyCacheService;
import org.dspace.xoai.services.impl.xoai.DSpaceIdentifyResolver;
import org.dspace.xoai.tests.helpers.stubs.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableWebMvc
public class DSpaceTestConfiguration extends WebMvcConfigurerAdapter {
    private static final String TWIG_HTML_EXTENSION = ".twig.html";
    private static final String VIEWS_LOCATION = "/WEB-INF/views/";


    @Bean
    public ContextService contextService() {
        return new ContextService() {
            @Override
            public Context getContext(HttpServletRequest request) throws ContextServiceException {
                return null;
            }
        };
    }

    private StubbedResourceResolver resourceResolver = new StubbedResourceResolver();

    @Bean
    public ResourceResolver resourceResolver() {
        return resourceResolver;
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix(VIEWS_LOCATION);
        viewResolver.setSuffix(TWIG_HTML_EXTENSION);
//        viewResolver.setCached(true);
//        viewResolver.setTheme(null);

        return viewResolver;
    }

    private StubbedConfigurationService configurationService = new StubbedConfigurationService();

    @Bean
    public ConfigurationService configurationService() {
        return configurationService;
    }

    @Bean
    public XOAIManagerResolver xoaiManagerResolver() {
        return new StubbedXOAIManagerResolver();
    }

    @Bean
    public XOAICacheService xoaiCacheService() {
        return new DSpaceEmptyCacheService();
    }

    private StubbedSetRepository setRepository = new StubbedSetRepository();

    @Bean StubbedSetRepository setRepository () {
        return setRepository;
    }

    @Bean
    public ItemRepositoryResolver itemRepositoryResolver() {
        return new ItemRepositoryResolver() {
            @Override
            public AbstractItemRepository getItemRepository(HttpServletRequest request) throws ContextServiceException {
                try {
                    return new DSpaceItemSolrRepository(solrServer());
                } catch (Exception e) {
                    throw new ContextServiceException(e);
                }
            }
        };
    }
    @Bean
    public SetRepositoryResolver setRepositoryResolver () {
        return new SetRepositoryResolver() {
            @Override
            public AbstractSetRepository getSetRepository(HttpServletRequest request) throws ContextServiceException {
                return setRepository;
            }
        };
    }
    @Bean
    public IdentifyResolver identifyResolver () {
        return new DSpaceIdentifyResolver();
    }

    @Bean
    public FieldResolver databaseService () {
        return new StubbedFieldResolver();
    }

    @Bean
    public EarliestDateResolver earliestDateResolver () {
        return new StubbedEarliestDateResolver();
    }

    @Bean
    public SolrServer solrServer() throws IOException, SAXException, ParserConfigurationException {
        String coreName = "oai";
        String solrHome = "classpath:/org/dspace/xoai/tests/solr";
        CoreContainer container = new CoreContainer();
        CoreDescriptor coreDescriptor = new CoreDescriptor(container, coreName, solrHome);
        SolrCore core = container.create(coreDescriptor);
        container.register(core, false);

        return new EmbeddedSolrServer(container, coreName);
    }
}
