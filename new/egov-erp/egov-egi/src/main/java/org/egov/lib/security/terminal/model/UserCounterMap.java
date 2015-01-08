/*
 * @(#)UserCounterMap.java 3.0, 14 Jun, 2013 3:12:09 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security.terminal.model;

import java.util.Date;

import org.egov.lib.rjbac.user.UserImpl;

public class UserCounterMap implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Location counterId;
	private UserImpl userId;
	private Date fromDate;
	private Date toDate;
	private Date modifiedDate;
	private Integer modifiedBy;

	public Location getCounterId() {
		return counterId;
	}

	public void setCounterId(Location counterId) {
		this.counterId = counterId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserImpl getUserId() {
		return userId;
	}

	public void setUserId(UserImpl userId) {
		this.userId = userId;
	}

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
	 * @return the modifiedDate
	 */
	public Date getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the modofiedBy
	 */
	public Integer getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modofiedBy the modofiedBy to set
	 */
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
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

}
