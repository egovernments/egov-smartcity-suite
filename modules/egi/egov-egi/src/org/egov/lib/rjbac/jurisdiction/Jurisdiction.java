/*
 * @(#)Jurisdiction.java 3.0, 16 Jun, 2013 10:06:50 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.jurisdiction;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.rjbac.user.User;

public class Jurisdiction implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id = null;
	private BoundaryType jurisdictionLevel = null;
	private Set<JurisdictionValues> jurisdictionValues = new HashSet();
	private User user = null;
	private Date updateTime;

	/**
	 * @return Returns the jurisdictionLevel.
	 */
	public BoundaryType getJurisdictionLevel() {
		return jurisdictionLevel;
	}

	/**
	 * @param jurisdictionLevel The jurisdictionLevel to set.
	 */
	public void setJurisdictionLevel(BoundaryType jurisdictionLevel) {
		this.jurisdictionLevel = jurisdictionLevel;
	}

	/**
	 * @return Returns the jurisdictionValues.
	 */
	public Set getJurisdictionValues() {
		return jurisdictionValues;
	}

	/**
	 * @param jurisdictionValues The jurisdictionValues to set.
	 */
	public void setJurisdictionValues(Set jurisdictionValues) {
		this.jurisdictionValues = jurisdictionValues;
	}

	public void addJurisdictionValue(JurisdictionValues bndry) {
		getJurisdictionValues().add(bndry);
	}

	public void removeJurisdictionValue(JurisdictionValues bndry) {
		if (this.jurisdictionValues.contains(bndry))
			this.jurisdictionValues.remove(bndry);
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the user.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user The user to set.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return Returns the updateTime.
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime The updateTime to set.
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Jurisdiction)) {
			return false;
		}

		final Jurisdiction other = (Jurisdiction) obj;
		if (other.getUser().equals(getUser())) {
			if (other.getJurisdictionLevel().equals(getJurisdictionLevel())) {
				return true;

			} else {
				return false;
			}

		} else {
			return false;
		}

	}

	public int hashCode() {
		int hashCode = 0;
		if (this.getUser() != null) {
			hashCode = hashCode + this.getUser().hashCode();
		}

		if (this.getJurisdictionLevel() != null) {
			hashCode = hashCode + this.getJurisdictionLevel().hashCode();
		}

		return hashCode;

	}

	public boolean validate() {
		BoundaryType bt = this.jurisdictionLevel;
		Set bndries = this.jurisdictionValues;
		for (Iterator iter = bndries.iterator(); iter.hasNext();) {
			JurisdictionValues element = (JurisdictionValues) iter.next();
			// System.out.println("element.getBoundaryType()"+element.getBoundaryType());
			if (!element.getBoundary().getBoundaryType().equals(bt))
				throw new EGOVRuntimeException("Invalid Boundary " + element.getBoundary().getName() + " for Boundary Type " + bt.getName() + ".");
		}

		return true;
	}
}
