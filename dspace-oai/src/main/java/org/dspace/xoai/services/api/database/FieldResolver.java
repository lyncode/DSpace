package org.dspace.xoai.services.api.database;

import org.dspace.core.Context;
import org.dspace.xoai.exceptions.InvalidMetadataFieldException;

import java.sql.SQLException;

public interface FieldResolver {
    int getFieldID(Context context, String field) throws InvalidMetadataFieldException, SQLException;
}
