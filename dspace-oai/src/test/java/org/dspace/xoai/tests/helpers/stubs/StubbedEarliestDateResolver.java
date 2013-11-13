package org.dspace.xoai.tests.helpers.stubs;

import org.dspace.core.Context;
import org.dspace.xoai.exceptions.InvalidMetadataFieldException;
import org.dspace.xoai.services.api.database.EarliestDateResolver;

import java.sql.SQLException;
import java.util.Date;

public class StubbedEarliestDateResolver implements EarliestDateResolver {
    private Date date = new Date();

    public StubbedEarliestDateResolver is (Date date) {
        this.date = date;
        return this;
    }

    @Override
    public Date getEarliestDate(Context context) throws InvalidMetadataFieldException, SQLException {
        return date;
    }
}
