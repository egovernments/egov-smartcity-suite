/*
 * @(#)Escalation.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.entities;

import org.egov.infstr.models.BaseModel;
import org.egov.lib.rjbac.role.Role;

public class Escalation extends BaseModel {

	private static final long serialVersionUID = 1L;
	private ComplaintTypes complaintType;
	private Role role;
	private Integer noOfDays;

	/**
	 * @return the complaintType
	 */
	public ComplaintTypes getComplaintType() {
		return this.complaintType;
	}

	/**
	 * @param complaintType the complaintType to set
	 */
	public void setComplaintType(final ComplaintTypes complaintType) {
		this.complaintType = complaintType;
	}

	/**
	 * @return the role
	 */
	public Role getRole() {
		return this.role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(final Role role) {
		this.role = role;
	}

	/**
	 * @return the noOfDays
	 */
	public Integer getNoOfDays() {
		return this.noOfDays;
	}

	/**
	 * @param noOfDays the noOfDays to set
	 */
	public void setNoOfDays(final Integer noOfDays) {
		this.noOfDays = noOfDays;
	}
}