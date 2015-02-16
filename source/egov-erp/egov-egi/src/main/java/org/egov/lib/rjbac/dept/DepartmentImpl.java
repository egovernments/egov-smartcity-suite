/*
 * @(#)DepartmentImpl.java 3.0, 16 Jun, 2013 10:17:22 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.dept;

import org.egov.search.domain.Searchable;

import java.util.Date;

@Searchable
public class DepartmentImpl implements Department {

	@Searchable(name = "name")
	private String deptName;
	private Integer id;
	@Searchable(name = "code")
	private String deptCode;
	private Date updateTime;

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

		if (this.getDeptName().equals(other.getDeptName())) {
			return true;
		} else {
			return false;
		}

	}

	public int hashCode() {
		int hashCode = 0;
		// assumes department name is never null.
		if (getDeptName() != null)
			hashCode = hashCode + this.getDeptName().hashCode();

		return hashCode;
	}

	@Override
	public String getDeptDetails() {
		// TODO Auto-generated method stub
		return "deptDetails";
	}

	@Override
	public void setDeptDetails(String deptDetails) {
		// TODO Auto-generated method stub
		
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

	@Override
	public String getBillingLocation() {
		// TODO Auto-generated method stub
		return "location";
	}

	@Override
	public void setBillingLocation(String billingLocation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDeptAddress() {
		// TODO Auto-generated method stub
		return "deptAddress";
	}

	@Override
	public void setDeptAddress(String deptAddress) {
		// TODO Auto-generated method stub
		
	}

}
