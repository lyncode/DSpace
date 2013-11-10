package org.dspace.xoai.tests.helpers;

import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpRequestBuilder {
    private MockHttpServletRequest request = new MockHttpServletRequest();

    public HttpRequestBuilder withUrl (String url) {
        try {
            URI uri = new URI(url);
        } catch (URISyntaxException e) {
            // ASD
        }
        return this;
    }

    public HttpRequestBuilder usingGetMethod () {
        request.setMethod("GET");
        return this;
    }

    public HttpServletRequest build () {

        return request;
    }
}
