/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.orm.dao.api;

import java.util.List;

import org.dspace.orm.entity.BitstreamFormat;

public interface IBitstreamFormatDao {
    // CRUD
    int save(BitstreamFormat c);

    BitstreamFormat selectById(int id);

    boolean delete(BitstreamFormat c);

    // Listing
    List<BitstreamFormat> selectAll();
}
