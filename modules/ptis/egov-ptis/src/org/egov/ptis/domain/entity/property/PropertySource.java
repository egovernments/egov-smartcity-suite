/*
 * PropertySource.java Created on Oct 21, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import java.io.Serializable;
import java.util.Date;

import org.egov.exceptions.EGOVRuntimeException;

/**
 * <p>
 * This class defines Property Source i.e Source from which Property got entered
 * in to the system. PropertySource can be SAS, PRF etc.
 * </p>
 * 
 * @author Gayathri Joshi
 * @version 2.00
 * @since 2.00
 */
public class PropertySource implements Serializable {

	private Integer ID = null;

	private String name = null;

	private Date lastUpdatedTimeStamp = null;

	private String propSrcCode = null;

	/**
	 * @return Returns the iD.
	 */
	public Integer getID() {
		return ID;
	}

	/**
	 * @param id
	 *            The iD to set.
	 */
	public void setID(Integer id) {
		ID = id;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the lastUpdatedTimeStamp.
	 */
	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	/**
	 * @param lastUpdatedTimeStamp
	 *            The lastUpdatedTimeStamp to set.
	 */
	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	/**
	 * @return the propSrcCode
	 */
	public String getPropSrcCode() {
		return propSrcCode;
	}

	/**
	 * @param propSrcCode
	 *            the propSrcCode to set
	 */
	public void setPropSrcCode(String propSrcCode) {
		this.propSrcCode = propSrcCode;
	}

	/**
	 * @return Returns if the given Object is equal to PropertyOccupation
	 */
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (this == that)
			return true;

		if (that.getClass() != this.getClass())
			return false;
		final PropertySource thatPropSrc = (PropertySource) that;

		if (this.getID() != null && thatPropSrc.getID() != null) {
			if (getID().equals(thatPropSrc.getID())) {
				return true;
			} else
				return false;
		} else if (this.getName() != null && thatPropSrc.getName() != null) {
			if (getName().equals(thatPropSrc.getName())) {
				return true;
			} else
				return false;
		} else
			return false;

	}

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {
		int hashCode = 0;
		if (this.getID() != null) {
			hashCode += this.getID().hashCode();
		}
		if (this.getName() != null) {
			hashCode += this.getName().hashCode();
		}

		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validate() {
		if (getName() == null)
			throw new EGOVRuntimeException(
					"In PropertySource Validate :'Source Name' Attribute is no set, Please Check !!");

		return true;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getID()).append("|Name: ").append(getName());

		return objStr.toString();
	}
}
