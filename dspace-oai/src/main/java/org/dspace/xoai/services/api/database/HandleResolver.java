package org.dspace.xoai.services.api.database;

import org.dspace.content.DSpaceObject;

public interface HandleResolver {
    DSpaceObject resolve (String handle) throws HandleResolverException;
    String getHandle (DSpaceObject object) throws HandleResolverException;
}
