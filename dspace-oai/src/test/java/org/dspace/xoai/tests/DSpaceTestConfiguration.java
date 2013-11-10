package org.dspace.xoai.tests;

import com.lyncode.xoai.dataprovider.data.AbstractItemRepository;
import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;
import org.dspace.core.Context;
import org.dspace.xoai.services.api.cache.XOAICacheService;
import org.dspace.xoai.services.api.config.ConfigurationService;
import org.dspace.xoai.services.api.config.XOAIItemRepositoryResolver;
import org.dspace.xoai.services.api.config.XOAIManagerResolver;
import org.dspace.xoai.services.api.context.ContextService;
import org.dspace.xoai.services.api.context.ContextServiceException;
import org.dspace.xoai.services.impl.cache.DSpaceEmptyCacheService;
import org.dspace.xoai.tests.helpers.stubs.StubbedConfigurationService;
import org.dspace.xoai.tests.helpers.stubs.StubbedItemRepository;
import org.dspace.xoai.tests.helpers.stubs.StubbedResourceResolver;
import org.dspace.xoai.tests.helpers.stubs.StubbedXOAIManagerResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.http.HttpServletRequest;

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

    private StubbedItemRepository itemRepository = new StubbedItemRepository();

    @Bean
    public XOAIItemRepositoryResolver xoaiItemRepositoryResolver() {
        return new XOAIItemRepositoryResolver() {
            @Override
            public AbstractItemRepository getItemRepository(HttpServletRequest request) throws ContextServiceException {
                return itemRepository;
            }
        };
    }

}
