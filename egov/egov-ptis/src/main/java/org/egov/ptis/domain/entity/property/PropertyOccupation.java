/*
 * PropertyOccupation.java Created on Oct 21, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import java.util.Date;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.models.BaseModel;

/**
 * <p>
 * This class defines Property Occupation i.e how is a Property been occupied
 * PropertyOccupation can be Self Occupied, Tenanted etc.
 * </p>
 * 
 * @author Gayathri Joshi
 * @version 2.00
 * @since 2.00
 */
public class PropertyOccupation extends BaseModel {

	private String occupation;
	private Float occupancyFactor;
	private String occupancyCode;
	private Date fromDate;
	private Date toDate;
	private PropertyUsage propertyUsage;

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
		final PropertyOccupation thatPropOcc = (PropertyOccupation) that;

		if (this.getId() != null && thatPropOcc.getId() != null) {
			if (getId().equals(thatPropOcc.getId())) {
				return true;
			} else
				return false;
		} else if (this.getOccupation() != null && thatPropOcc.getOccupation() != null) {
			if (getOccupation().equals(thatPropOcc.getOccupation())) {
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
		if (this.getId() != null) {
			hashCode += this.getId().hashCode();
		} else if (this.getOccupation() != null) {
			hashCode += this.getOccupation().hashCode();
		}

		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validatePropOccupation() {
		if (getOccupation() == null)
			throw new EGOVRuntimeException(
					"In PropertyOccupation Validate :'Occupation' Attribute is no set, Please Check !!");
		if (getOccupancyFactor() == null)
			throw new EGOVRuntimeException(
					"In PropertyOccupation Validate :'Occupancy Factor' Attribute is no set, Please Check !!");

		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Id: ").append(getId()).append("Occupation: ").append(occupation).append("|OccupFactor").append(
				occupancyFactor).append("|PropertyUsage").append(propertyUsage);

		return sb.toString();
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public Float getOccupancyFactor() {
		return occupancyFactor;
	}

	public void setOccupancyFactor(Float occupancyFactor) {
		this.occupancyFactor = occupancyFactor;
	}

	public String getOccupancyCode() {
		return occupancyCode;
	}

	public void setOccupancyCode(String occupancyCode) {
		this.occupancyCode = occupancyCode;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public PropertyUsage getPropertyUsage() {
		return propertyUsage;
	}

	public void setPropertyUsage(PropertyUsage propertyUsage) {
		this.propertyUsage = propertyUsage;
	}

}
