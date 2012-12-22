/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.orm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.dspace.content.DSpaceObject;
import org.dspace.core.Constants;
import org.dspace.orm.dao.api.IBitstreamDao;
import org.dspace.orm.dao.api.IBitstreamFormatDao;
import org.dspace.orm.dao.api.IBundleDao;
import org.dspace.orm.dao.api.ICollectionDao;
import org.dspace.orm.dao.api.ICommunityDao;
import org.dspace.orm.dao.api.IEpersonDao;
import org.dspace.orm.dao.api.IEpersonGroupDao;
import org.dspace.orm.dao.api.IHandleDao;
import org.dspace.orm.dao.api.IItemDao;
import org.dspace.orm.dao.api.IMetadataDao;
import org.dspace.orm.dao.api.IVersionItemDao;
import org.dspace.orm.dao.api.IWorkFlowItemDao;
import org.dspace.orm.dao.api.IWorkSpaceItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Entity
@Table(name = "handle")
@Configurable
public class Handle implements IDSpaceObject {
    private int id;
    private String handle;
    private int resourceType;
    private int resourceId;

    @Id
    @Column(name = "handle_id")
    @GeneratedValue
    public int getID() {
        return id;
    }

    @Column(name = "handle", unique = true)
    public String getHandle() {
        return handle;
    }

    @Column(name = "resource_type_id")
    public int getResourceType() {
        return resourceType;
    }

    @Column(name = "resource_id")
    public int getResourceId() {
        return resourceId;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

	@Override
	public int getType() {
		return Constants.HANDLE;
	}
	
	@Autowired IBitstreamDao bitstreamDao;
	@Autowired IBundleDao bundleDao;
	@Autowired IHandleDao handleDao;
	@Autowired ICommunityDao commDao;
	@Autowired ICollectionDao collDao;
	@Autowired IBitstreamFormatDao bitstreamFormatDao;
	@Autowired IEpersonDao personDao;
	@Autowired IEpersonGroupDao groupDao;
	@Autowired IItemDao itemDao;
	@Autowired IMetadataDao metadataDao;
	@Autowired IVersionItemDao versionItemDao;
	@Autowired IWorkFlowItemDao workflowitemDao;
	@Autowired IWorkSpaceItemDao workspaceitemDao;

	public IDSpaceObject toObject() {
		switch (this.getResourceType()) {
			case Constants.BITSTREAM:
				return bitstreamDao.selectById(getResourceId());
			case Constants.BUNDLE:
				return bundleDao.selectById(getResourceId());
			case Constants.HANDLE:
				return handleDao.selectById(getResourceId());
			case Constants.COLLECTION:
				return collDao.selectById(getResourceId());
			case Constants.COMMUNITY:
				return commDao.selectById(getResourceId());
			case Constants.BITSTREAM_FORMAT:
				return bitstreamFormatDao.selectById(getResourceId());
			case Constants.EPERSON:
				return personDao.selectById(getResourceId());
			case Constants.EPERSONGROUP:
				return groupDao.selectById(getResourceId());
			case Constants.ITEM:
				return itemDao.selectById(getResourceId());
			case Constants.METADATA:
				return 
		}
	}
}
