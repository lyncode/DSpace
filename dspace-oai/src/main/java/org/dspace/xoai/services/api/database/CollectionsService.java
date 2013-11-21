package org.dspace.xoai.services.api.database;

import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.Item;

import java.sql.SQLException;
import java.util.List;

public interface CollectionsService {
    List<Integer> getAllSubCollections(int communityId) throws SQLException;
    List<Community> flatParentCommunities(Collection collection) throws SQLException;
    List<Community> flatParentCommunities(Community community) throws SQLException;
    List<Community> flatParentCommunities(Item item) throws SQLException;
}
