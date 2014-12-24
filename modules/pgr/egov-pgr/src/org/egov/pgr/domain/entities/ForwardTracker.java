/*
 * @(#)ForwardTracker.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.entities;

import java.util.Date;

import org.egov.infstr.models.BaseModel;
import org.egov.lib.rjbac.user.User;

public class ForwardTracker extends BaseModel {

	private static final long serialVersionUID = 1L;
	private User fromOfficer;
	private User toOfficer;
	private ComplaintDetails complaintDetails;
	private Date dateForwarded;
	private String comments;
	private String notes;

	public Date getDateForwarded() {
		return this.dateForwarded;
	}

	public void setDateForwarded(final Date dateForwarded) {
		this.dateForwarded = dateForwarded;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(final String comments) {
		this.comments = comments;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(final String notes) {
		this.notes = notes;
	}

	public ComplaintDetails getComplaintDetails() {
		return this.complaintDetails;
	}

	public void setComplaintDetails(final ComplaintDetails complaintDetails) {
		this.complaintDetails = complaintDetails;
	}

	public User getFromOfficer() {
		return this.fromOfficer;
	}

	public void setFromOfficer(final User fromOfficer) {
		this.fromOfficer = fromOfficer;
	}

	public User getToOfficer() {
		return this.toOfficer;
	}

	public void setToOfficer(final User toOfficer) {
		this.toOfficer = toOfficer;
	}

}