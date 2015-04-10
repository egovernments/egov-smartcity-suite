/*
 * @(#)JurisdictionValues.java 3.0, 16 Jun, 2013 10:09:19 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.jurisdiction;

import java.io.Serializable;
import java.util.Date;

import org.egov.infra.admin.master.entity.Boundary;

public class JurisdictionValues implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id = null;
	private Date fromDate = null;
	private Date toDate = null;
	private Boundary boundary;
	private Character isHistory;
	private Jurisdiction userJurLevel;

	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the boundary
	 */
	public Boundary getBoundary() {
		return boundary;
	}

	/**
	 * @param boundary the boundary to set
	 */
	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}

	/**
	 * @return the userJurLevel
	 */
	public Jurisdiction getUserJurLevel() {
		return userJurLevel;
	}

	/**
	 * @param userJurLevel the userJurLevel to set
	 */
	public void setUserJurLevel(Jurisdiction userJurLevel) {
		this.userJurLevel = userJurLevel;
	}

	/**
	 * @return the isHistory
	 */
	public Character getIsHistory() {
		return isHistory;
	}

	/**
	 * @param isHistory the isHistory to set
	 */
	public void setIsHistory(Character isHistory) {
		this.isHistory = isHistory;
	}

}
