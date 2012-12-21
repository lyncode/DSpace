/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.orm.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "eperson")
public class Eperson {
    private int id;
    private String email;
    private String password;
    private String salt;
    private String digestAlgorithm;
    private String firstName;
    private String lastName;
    private boolean canLogIn;
    private boolean requireCertificate;
    private boolean selfRegistered;
    private Date lastActive;
    private Integer subFrequency;
    private String phone;
    private String netid;
    private List<Collection> collections;
    private List<EpersonGroup> epersonGroups;
   
    
    @Id
    @Column(name = "eperson_id")
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "email", nullable = true)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
    @Column(name = "password", nullable = true)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the salt
	 */
    @Column(name = "salt", nullable = true)
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * @return the digestAlgorithm
	 */
    @Column(name = "digest_algorithm", nullable = true)
	public String getDigestAlgorithm() {
		return digestAlgorithm;
	}

	/**
	 * @param digestAlgorithm the digestAlgorithm to set
	 */
	public void setDigestAlgorithm(String digestAlgorithm) {
		this.digestAlgorithm = digestAlgorithm;
	}

	/**
	 * @return the firstName
	 */
    @Column(name = "firstname", nullable = true)
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
    @Column(name = "lastname", nullable = true)
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the canLogIn
	 */
    @Column(name = "can_log_in", nullable = true)
	public boolean isCanLogIn() {
		return canLogIn;
	}

	/**
	 * @param canLogIn the canLogIn to set
	 */
	public void setCanLogIn(boolean canLogIn) {
		this.canLogIn = canLogIn;
	}

	/**
	 * @return the requireCertificate
	 */
    @Column(name = "require_certificate", nullable = true)
	public boolean isRequireCertificate() {
		return requireCertificate;
	}

	/**
	 * @param requireCertificate the requireCertificate to set
	 */
	public void setRequireCertificate(boolean requireCertificate) {
		this.requireCertificate = requireCertificate;
	}

	/**
	 * @return the selfRegistered
	 */
    @Column(name = "self_registered", nullable = true)
	public boolean isSelfRegistered() {
		return selfRegistered;
	}

	/**
	 * @param selfRegistered the selfRegistered to set
	 */
	public void setSelfRegistered(boolean selfRegistered) {
		this.selfRegistered = selfRegistered;
	}

	/**
	 * @return the lastActive
	 */
    @Column(name = "last_active", nullable = true)
	public Date getLastActive() {
		return lastActive;
	}

	/**
	 * @param lastActive the lastActive to set
	 */
	public void setLastActive(Date lastActive) {
		this.lastActive = lastActive;
	}

	/**
	 * @return the subFrequency
	 */
    @Column(name = "sub_frequency", nullable = true)
	public Integer getSubFrequency() {
		return subFrequency;
	}

	/**
	 * @param subFrequency the subFrequency to set
	 */
	public void setSubFrequency(Integer subFrequency) {
		this.subFrequency = subFrequency;
	}

	/**
	 * @return the phone
	 */
    @Column(name = "phone", nullable = true)
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the netid
	 */
    @Column(name = "netid", nullable = true)
	public String getNetid() {
		return netid;
	}

	/**
	 * @param netid the netid to set
	 */
	public void setNetid(String netid) {
		this.netid = netid;
	}
	
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinTable(name = "subscription", joinColumns = { @JoinColumn(name = "eperson_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "collection_id", nullable = false) })
    public List<Collection> getCollections() {
        return collections;
    }
    
    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinTable(name = "epersongroup2eperson", joinColumns = { @JoinColumn(name = "eperson_group_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "eperson_id", nullable = false) })
    public List<EpersonGroup> getEpersonGroups() {
        return epersonGroups;
    }
    
    public void setEpersonGroups(List<EpersonGroup> epersonGroups) {
        this.epersonGroups = epersonGroups;
    }
}
