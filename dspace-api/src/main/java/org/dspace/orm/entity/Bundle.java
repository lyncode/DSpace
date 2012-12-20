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

@Entity
@Table(name = "bundle")
public class Bundle {
    private int id;
    private String name;
    private Integer primary;

    @Id
    @Column(name = "bundle_id")
    @GeneratedValue
    public int getId() {
        return id;
    }

    @Column(name = "name", nullable = true)
    public String getName() {
        return name;
    }

    @Column(name = "primary_bitstream_id", nullable = true)
    public Integer getPrimary() {
        return primary;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrimary(Integer primary) {
        this.primary = primary;
    }

}
