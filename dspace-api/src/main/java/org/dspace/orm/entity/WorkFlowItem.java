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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.dspace.core.Constants;

@Entity
@Table(name = "workflowitem")
public class WorkFlowItem implements IDSpaceObject{
    private int id;
    private Item item;
    private Collection collection;
    private Integer state;
    private Eperson owner;
    private boolean multipleTitles;
    private boolean publishedBefore;
    private boolean multipleFiles;
    
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = true)
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id", nullable = true)
	public Collection getCollection() {
		return collection;
	}
	

	public void setCollection(Collection collection) {
		this.collection = collection;
	}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner", nullable = true)
	public Eperson getOwner() {
		return owner;
	}

	public void setOwner(Eperson owner) {
		this.owner = owner;
	}

	@Column(name = "state", nullable = true)
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Column(name = "published_before", nullable = true)
	public boolean isPublishedBefore() {
		return publishedBefore;
	}

	public void setPublishedBefore(boolean publishedBefore) {
		this.publishedBefore = publishedBefore;
	}

	@Column(name = "multiple_titles", nullable = true)
	public boolean isMultipleTitles() {
		return multipleTitles;
	}

	public void setMultipleTitles(boolean multipleTitles) {
		this.multipleTitles = multipleTitles;
	}

	@Column(name = "multiple_files", nullable = true)
	public boolean isMultipleFiles() {
		return multipleFiles;
	}

	public void setMultipleFiles(boolean multipleFiles) {
		this.multipleFiles = multipleFiles;
	}

	

   
}
