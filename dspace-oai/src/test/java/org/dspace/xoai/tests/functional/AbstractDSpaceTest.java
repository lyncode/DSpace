package org.dspace.xoai.tests.functional;

import com.lyncode.xoai.builders.dataprovider.ConfigurationBuilder;
import org.dspace.xoai.controller.DSpaceOAIDataProvider;
import org.dspace.xoai.services.api.config.XOAIManagerResolver;
import org.dspace.xoai.tests.DSpaceTestConfiguration;
import org.dspace.xoai.tests.helpers.stubs.StubbedXOAIManagerResolver;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { DSpaceTestConfiguration.class, DSpaceOAIDataProvider.class })
public abstract class AbstractDSpaceTest {

    @Autowired WebApplicationContext wac;
    private MockMvc mockMvc;

    private StubbedXOAIManagerResolver xoaiManagerResolver;

    @Before
    public void setup() {
        xoaiManagerResolver = (StubbedXOAIManagerResolver) this.wac.getBean(XOAIManagerResolver.class);
        xoaiManagerResolver.configuration().withDefaults();
    }

    public MockMvc theMvc () {
        if (this.mockMvc == null) this.mockMvc = webAppContextSetup(this.wac).build();
        return this.mockMvc;
    }

    public ConfigurationBuilder theConfiguration () {
        return xoaiManagerResolver.configuration();
    }
}