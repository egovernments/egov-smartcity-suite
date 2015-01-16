/*
 * @(#)DepartmentImpl.java 3.0, 16 Jun, 2013 10:17:22 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.dept;

import java.util.Date;

public class DepartmentImpl implements Department {

	private String deptName;
	private String deptDetails;
	private Integer id;
	private Date updateTime;
	private String deptCode;
	private String deptAddress;
	private String billingLocation;

	/**
	 * @return Returns the billingLocation.
	 */
	public String getBillingLocation() {
		return billingLocation;
	}

	/**
	 * @param billingLocation The billingLocation to set.
	 */
	public void setBillingLocation(String billingLocation) {
		this.billingLocation = billingLocation;
	}

	/**
	 * @return Returns the deptAddress.
	 */
	public String getDeptAddress() {
		return deptAddress;
	}

	/**
	 * @param deptAddress The deptAddress to set.
	 */
	public void setDeptAddress(String deptAddress) {
		this.deptAddress = deptAddress;
	}

	/**
	 * @return Returns the deptDetails.
	 */
	public String getDeptDetails() {
		return deptDetails;
	}

	/**
	 * @param deptDetails The deptDetails to set.
	 */
	public void setDeptDetails(String deptDetails) {
		this.deptDetails = deptDetails;
	}

	/**
	 * @return Returns the deptName.
	 */
	public String getDeptName() {
		return deptName;
	}

	/**
	 * @param deptName The deptName to set.
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
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

	/**
	 * @return Returns the deptCode.
	 */
	public String getDeptCode() {
		return deptCode;
	}

	/**
	 * @param deptCode The deptCode to set.
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof DepartmentImpl)) {
			return false;
		}

		final Department other = (Department) obj;

		return this.getDeptName().equals(other.getDeptName());

	}

	public int hashCode() {
		int hashCode = 0;
		// assumes department name is never null.
		if (getDeptName() != null)
			hashCode = hashCode + this.getDeptName().hashCode();

		return hashCode;
	}

}
