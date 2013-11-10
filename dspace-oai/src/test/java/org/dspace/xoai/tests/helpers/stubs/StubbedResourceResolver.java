package org.dspace.xoai.tests.helpers.stubs;

import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class StubbedResourceResolver implements ResourceResolver {
    private Map<String, InputStream> inputStreamMap = new HashMap<String, InputStream>();
    private Map<String, Transformer> transformerMap = new HashMap<String, Transformer>();

    @Override
    public InputStream getResource(String path) throws IOException {
        return inputStreamMap.get(path);
    }

    @Override
    public Transformer getTransformer(String path) throws IOException, TransformerConfigurationException {
        return transformerMap.get(path);
    }
}
