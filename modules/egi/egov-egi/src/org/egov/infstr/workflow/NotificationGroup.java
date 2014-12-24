/*
 * @(#)NotificationGroup.java 3.0, 17 Jun, 2013 4:30:21 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.workflow;

import java.util.Date;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infstr.models.BaseModel;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.Length;

public class NotificationGroup extends BaseModel {

	private static final long serialVersionUID = 1L;

	@Length(min = 1, max = 100, message = "err.grpname")
	private String groupName;
	@Length(max = 250, message = "err.grpdesc")
	private String groupDesc;
	@NotNull(message = "err.effdt")
	private Date effectiveDate;
	private char active;
	@Valid
	private Set<Position> members;

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the groupDesc
	 */
	public String getGroupDesc() {
		return groupDesc;
	}

	/**
	 * @param groupDesc the groupDesc to set
	 */
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return the members
	 */
	public Set<Position> getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(Set<Position> members) {
		this.members = members;
	}

	/**
	 * @return the active
	 */
	public char getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(char active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return this.groupName;
	}
}
