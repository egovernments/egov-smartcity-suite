/*
 * @(#)DepartmentForm.java 3.0, 18 Jun, 2013 3:23:57 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.administration.rjbac.dept;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.egov.infstr.client.EgovActionForm;

public class DepartmentForm extends EgovActionForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String deptid = "";
	private String deptName = "";
	private String deptDetails = "";
	private String deptCode = "";
	private String billingLocation = "";
	private String deptAddress = "";

	/**
	 * @return Returns the deptCode.
	 */
	public String getDeptCode() {
		return this.deptCode;
	}

	/**
	 * @param deptCode The deptCode to set.
	 */
	public void setDeptCode(final String deptCode) {
		this.deptCode = deptCode;
	}

	/**
	 * @return Returns the deptid.
	 */
	public String getDeptid() {
		return this.deptid;
	}

	/**
	 * @param deptid The deptid to set.
	 */
	public void setDeptid(final String deptid) {
		this.deptid = deptid;
	}

	/**
	 * @return Returns the deptDetails.
	 */
	public String getDeptDetails() {
		return this.deptDetails;
	}

	/**
	 * @param deptDetails The deptDetails to set.
	 */

	public void setDeptDetails(final String deptDetails) {
		this.deptDetails = deptDetails;
	}

	/**
	 * @return Returns the deptName.
	 */
	public String getDeptName() {
		return this.deptName;
	}

	/**
	 * @param deptName The deptName to set.
	 */
	public void setDeptName(final String deptName) {
		this.deptName = deptName;
	}

	/**
	 * @return Returns the deptAddress.
	 */
	public String getDeptAddress() {
		return this.deptAddress;
	}

	/**
	 * @param deptAddress The deptAddress to set.
	 */
	public void setDeptAddress(final String deptAddress) {
		this.deptAddress = deptAddress;
	}

	/**
	 * @return Returns the billingLocation.
	 */
	public String getBillingLocation() {
		return this.billingLocation;
	}

	/**
	 * @param billingLocation The billingLocation to set.
	 */
	public void setBillingLocation(final String billingLocation) {
		this.billingLocation = billingLocation;
	}

	/**
	 * resets the properties of the form
	 */
	@Override
	public void reset(final ActionMapping mapping, final HttpServletRequest req) {
		this.deptid = "";
		this.deptName = "";
		this.deptDetails = "";
		this.deptCode = "";
		// this.parentId = "";
		// this.isLeaf = "";
		this.billingLocation = "";
		this.deptAddress = "";
	}

}
