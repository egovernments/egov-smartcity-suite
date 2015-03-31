/*
 * @(#)Department.java 3.0, 16 Jun, 2013 10:17:59 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.dept;

import java.io.Serializable;
import java.util.Date;

public interface Department extends Serializable{

	String getDeptDetails();

	/**
	 * @param deptDetails The deptDetails to set.
	 */

	void setDeptDetails(String deptDetails);

	/**
	 * @return Returns the deptName.
	 */
	String getDeptName();

	/**
	 * @param deptName The deptName to set.
	 */
	void setDeptName(String deptName);

	/**
	 * @return Returns the deptName.
	 */
	String getDeptCode();

	/**
	 * @param deptName The deptName to set.
	 */
	void setDeptCode(String deptCode);

	/**
	 * @return Returns the id.
	 */
	Integer getId();

	/**
	 * @param id The id to set.
	 */
	void setId(Integer id);

	/**
	 * @return Returns the updateTime.
	 */
	Date getUpdateTime();

	/**
	 * @param updateTime The updateTime to set.
	 */
	void setUpdateTime(Date updateTime);

	String getBillingLocation();

	void setBillingLocation(String billingLocation);

	String getDeptAddress();

	void setDeptAddress(String deptAddress);

}