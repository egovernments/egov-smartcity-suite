/*
 * @(#)UserCounterReportsForm.java 3.0, 14 Jun, 2013 3:56:55 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security.terminal.client;

import org.apache.struts.action.ActionForm;

public class UserCounterReportsForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private String isLoc;
	private String fromDate;
	private String toDate;

	/**
	 * @return the fromDate
	 */
	public String getFromDate() {
		return this.fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(final String fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the isLoc
	 */
	public String getIsLoc() {
		return this.isLoc;
	}

	/**
	 * @param isLoc the isLoc to set
	 */
	public void setIsLoc(final String isLoc) {
		this.isLoc = isLoc;
	}

	/**
	 * @return the toDate
	 */
	public String getToDate() {
		return this.toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(final String toDate) {
		this.toDate = toDate;
	}

}
