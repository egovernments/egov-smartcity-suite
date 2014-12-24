/*
 * @(#)ComplaintGroup.java 3.0, 23 Jul, 2013 3:29:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.entities;

import org.egov.infstr.models.BaseModel;

public class ComplaintGroup extends BaseModel {
	private static final long serialVersionUID = 1L;

	private String complaintGroupName;
	private String isActive;

	public String getComplaintGroupName() {
		return this.complaintGroupName;
	}

	public void setComplaintGroupName(final String complaintGroupName) {
		this.complaintGroupName = complaintGroupName;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(final String isActive) {
		this.isActive = isActive;
	}

}