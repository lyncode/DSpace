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

import org.springframework.beans.factory.annotation.Configurable;

@Entity
@Table(name = "handle")
@Configurable
public class Handle {
    private int id;
    private String handle;
    private int resourceType;
    private int resourceId;

    @Id
    @Column(name = "handle_id")
    @GeneratedValue
    public int getId() {
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

    public void setId(int id) {
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
}
