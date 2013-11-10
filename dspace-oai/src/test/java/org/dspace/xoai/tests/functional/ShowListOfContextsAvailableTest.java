package org.dspace.xoai.tests.functional;

import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ShowListOfContextsAvailableTest extends AbstractDSpaceTest {

    public static final String ROOT_URL = "/";

    @Test
    public void requestToRootShouldGiveListOfContextsWithBadRequestError() throws Exception {
        theMvc().perform(get(ROOT_URL))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeExists("contexts"));
    }

    @Test
    public void requestForUnknownContextShouldGiveListOfContextsWithBadRequestError() throws Exception {
        theMvc().perform(get("/unexistentContext"))
                .andDo(print())
                .andExpect(redirectedUrl(ROOT_URL))
                .andExpect(status().isMovedTemporarily());
    }
}
