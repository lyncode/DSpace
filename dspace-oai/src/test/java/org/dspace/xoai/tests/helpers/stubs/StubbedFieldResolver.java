package org.dspace.xoai.tests.helpers.stubs;

import org.dspace.core.Context;
import org.dspace.xoai.exceptions.InvalidMetadataFieldException;
import org.dspace.xoai.services.api.database.FieldResolver;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class StubbedFieldResolver implements FieldResolver {
    private Map<String, Integer> fieldsMap = new HashMap<String, Integer>();

    @Override
    public int getFieldID(Context context, String field) throws InvalidMetadataFieldException, SQLException {
        return fieldsMap.get(field);
    }
}
