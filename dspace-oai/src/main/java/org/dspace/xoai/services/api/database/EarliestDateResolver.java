package org.dspace.xoai.services.api.database;

import org.dspace.core.Context;
import org.dspace.xoai.exceptions.InvalidMetadataFieldException;

import java.sql.SQLException;
import java.util.Date;

public interface EarliestDateResolver {
    public Date getEarliestDate (Context context) throws InvalidMetadataFieldException, SQLException;
}
