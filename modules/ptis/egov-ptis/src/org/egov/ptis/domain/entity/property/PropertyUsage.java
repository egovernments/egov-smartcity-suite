/*
 * PropertyUsage.java Created on Oct 20, 2005
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
 * This class defines Property Usage i.e A Property is linked to a PropertyUsage
 * indicating its current usage. Property Usage can be Residential,
 * Non-Residential, Industrial etc.
 * </p>
 * 
 * @author Gayathri Joshi
 * @version 2.00
 * @since 2.00
 */
public class PropertyUsage extends BaseModel {

	private String usageName;
	private String usageCode;

	private Integer orderId;

	private Float usagePercentage;

	private Date lastUpdatedTimeStamp;
	private Date fromDate;
	private Date toDate;
	private Integer isEnabled;

	/**
	 * @return Returns if the given Object is equal to PropertyUsage
	 */
	public boolean equals(Object that) {

		if (that == null)
			return false;

		if (this == that)
			return true;

		if (that.getClass() != this.getClass())
			return false;
		final PropertyUsage thatPropUsage = (PropertyUsage) that;

		if (this.getId() != null && thatPropUsage.getId() != null) {
			if (getId().equals(thatPropUsage.getId()))
				return true;
			else
				return false;
		} else if (this.getUsageName() != null && thatPropUsage.getUsageName() != null) {
			if (getUsageName().equals(thatPropUsage.getUsageName()))
				return true;
			else
				return false;
		} else {
			return false;
		}
	}

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {

		int hashCode = 0;
		if (this.getId() != null)
			hashCode += this.getId().hashCode();

		if (this.getUsageName() != null)
			hashCode += this.getUsageName().hashCode();
		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validatePropUsage() {

		if (getUsageName() == null)
			throw new EGOVRuntimeException(
					"In PropertyUsage Validate :Attribute 'Usage Name' is not set, Please Check !!");
		if (getUsagePercentage() == null)
			throw new EGOVRuntimeException(
					"In PropertyUsage Validate :Attribute 'Usage Percentage / Factor' is not set, Please Check !!");
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id).append("|").append(usageCode).append("|").append(usagePercentage);
		return sb.toString();
	}

	public String getUsageName() {
		return usageName;
	}

	public void setUsageName(String usageName) {
		this.usageName = usageName;
	}

	public String getUsageCode() {
		return usageCode;
	}

	public void setUsageCode(String usageCode) {
		this.usageCode = usageCode;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Float getUsagePercentage() {
		return usagePercentage;
	}

	public void setUsagePercentage(Float usagePercentage) {
		this.usagePercentage = usagePercentage;
	}

	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
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

	public Integer getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
}
