package org.dspace.xoai.tests.functional;

import com.lyncode.xoai.builders.dataprovider.FormatBuilder;
import org.dspace.xoai.tests.helpers.stubs.StubbedItemRepository;
import org.dspace.xoai.tests.helpers.stubs.StubbedResourceResolver;
import org.junit.Test;

import java.util.Date;

import static org.dspace.xoai.tests.helpers.SyntacticSugar.and;
import static org.dspace.xoai.tests.helpers.SyntacticSugar.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ListIdentifiersTest extends AbstractDSpaceTest {
    private static final Date LAST_MODIFIED_DATE = new Date();
    private static final String XOAI_XSLT = "XOAI_XSLT";

    @Test
    public void check () throws Exception {
        given(theConfiguration()
                .withMaxListRecords(100)
                .withFormats(aFormat().withId("xoai").withXslt(XOAI_XSLT).withPrefix("xoai"))
                .withContexts(aContext().withBaseUrl("request").withFormats("xoai")));

        and(given(theResourseResolver().hasIdentityTransformerFor("xoai")));

//        and(given(theSolrServer()
//                .withItem(anItem()
//                        .withIdentifier("a:b:c")
//                        .withLastModifiedDate(LAST_MODIFIED_DATE)
//                        .isNotDeleted().build())));


        againstTheDataProvider().perform(get("/request?verb=ListIdentifiers&metadataPrefix=xoai"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
