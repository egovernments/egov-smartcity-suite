/*
 * @(#)Location.java 3.0, 14 Jun, 2013 3:10:28 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security.terminal.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class Location implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String desc;
	private Integer isActive;
	private Integer isLocation;
	private Location locationId;
	private Date createdDate;
	private Timestamp lastModifiedDate;
	private Set<LocationIPMap> locationIPMapSet = new HashSet<LocationIPMap>();

	/**
	 * Gets the created date.
	 * @return the created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the desc.
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Sets the desc.
	 * @param desc the new desc
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * Gets the last modified date.
	 * @return the last modified date
	 */
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * Sets the last modified date.
	 * @param lastModifiedDate the new last modified date
	 */
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * Gets the id.
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the location id.
	 * @return the location id
	 */
	public Location getLocationId() {
		return locationId;
	}

	/**
	 * Sets the location id.
	 * @param counter the new location id
	 */
	public void setLocationId(Location counter) {
		this.locationId = counter;
	}

	/**
	 * Gets the checks if is active.
	 * @return the checks if is active
	 */
	public Integer getIsActive() {
		return isActive;
	}

	/**
	 * Sets the checks if is active.
	 * @param isActive the new checks if is active
	 */
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	/**
	 * Gets the location ip map set.
	 * @return the locationIPMapSet
	 */
	public Set<LocationIPMap> getLocationIPMapSet() {
		return locationIPMapSet;
	}

	/**
	 * Sets the location ip map set.
	 * @param locationIPMapSet the locationIPMapSet to set
	 */
	public void setLocationIPMapSet(Set<LocationIPMap> locationIPMapSet) {
		this.locationIPMapSet = locationIPMapSet;
	}

	/**
	 * Gets the checks if is location.
	 * @return the isLocation
	 */
	public Integer getIsLocation() {
		return isLocation;
	}

	/**
	 * Sets the checks if is location.
	 * @param isLocation the isLocation to set
	 */
	public void setIsLocation(Integer isLocation) {
		this.isLocation = isLocation;
	}

	/**
	 * Adds the location ip map.
	 * @param locationIPMap the location ip map
	 */
	public void addLocationIPMap(Set locationIPMap) {
		getLocationIPMapSet().addAll(locationIPMap);
	}

}
