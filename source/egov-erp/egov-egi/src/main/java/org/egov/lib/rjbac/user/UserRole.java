/*
 * @(#)UserRole.java 3.0, 16 Jun, 2013 12:35:33 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.user;

import java.util.Date;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;

public class UserRole {

	private Date fromDate;
	private Date toDate;
	private Role role;
	private User user;
	private Integer id;
	private Character isHistory;

	/**
	 * @return Returns the isHistory.
	 */
	public Character getIsHistory() {
		return isHistory;
	}

	/**
	 * @param isHistory The isHistory to set.
	 */
	public void setIsHistory(Character isHistory) {
		this.isHistory = isHistory;
	}

	/**
	 * @return Returns the fromDate.
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate The fromDate to set.
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return Returns the toDate.
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate The toDate to set.
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
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
	 * @return Returns the role.
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * @param role The role to set.
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * @return Returns the user.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user The user to set.
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
