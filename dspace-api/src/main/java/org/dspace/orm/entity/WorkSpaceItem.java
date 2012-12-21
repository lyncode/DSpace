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

import org.dspace.core.Constants;

@Entity
@Table(name = "workspaceitem")
public class WorkSpaceItem implements IDSpaceObject{
    private int id;
    
    private Item item;
    private Collection collection;
    private boolean publishedBefore;
    private boolean multipleFiles;
    private Integer stageReached;
    private Integer pageReached;
    
    @Id
    @Column(name = "workspace_item_id")
    @GeneratedValue
    public int getId() {
        return id;
    }
    
    @Override
    public int getID()
    {
    	return this.id;
    }
    
    @Override
    public int getType()
    {
    	return Constants.WORKSPACEITEM;
    }

    @Column(name = "item_id", nullable = true)
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

    @Column(name = "collection_id", nullable = true)
	public Collection getCollection() {
		return collection;
	}
	

	public void setCollection(Collection collection) {
		this.collection = collection;
	}

	@Column(name = "multiple_titles", nullable = true)
	public boolean isMultipleFiles() {
		return multipleFiles;
	}

	public void setMultipleFiles(boolean multipleFiles) {
		this.multipleFiles = multipleFiles;
	}

    @Column(name = "published_before", nullable = true)
	public boolean isPublishedBefore() {
		return publishedBefore;
	}

	public void setPublishedBefore(boolean publishedBefore) {
		this.publishedBefore = publishedBefore;
	}
	
    @Column(name = "stage_reached", nullable = true)
	public Integer getStageReached() {
		return stageReached;
	}

	public void setStageReached(Integer stageReached) {
		this.stageReached = stageReached;
	}

	@Column(name = "page_reached", nullable = true)
	public Integer getPageReached() {
		return pageReached;
	}

	public void setPageReached(Integer pageReached) {
		this.pageReached = pageReached;
	}

   
}
