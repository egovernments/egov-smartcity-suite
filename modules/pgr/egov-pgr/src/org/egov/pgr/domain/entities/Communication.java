/*
 * @(#)Communication.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.entities;

import java.util.Date;

import org.egov.infstr.models.BaseModel;
import org.egov.lib.rjbac.user.User;

public class Communication extends BaseModel {

	private static final long serialVersionUID = 1L;
	private User fromUser;
	private ComplaintDetails complaintDetails;
	private User toUser;
	private String message;
	private Date commDate;
	private Integer communicationType;
	private String messageLocal;

	public User getFromUser() {
		return this.fromUser;
	}

	public void setFromUser(final User fromUser) {
		this.fromUser = fromUser;
	}

	public User getToUser() {
		return this.toUser;
	}

	public void setToUser(final User toUser) {
		this.toUser = toUser;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public Date getCommDate() {
		return this.commDate;
	}

	public void setCommDate(final Date commDate) {
		this.commDate = commDate;
	}

	public Integer getCommunicationType() {
		return this.communicationType;
	}

	public void setCommunicationType(final Integer communicationType) {
		this.communicationType = communicationType;
	}

	public String getMessageLocal() {
		return this.messageLocal;
	}

	public void setMessageLocal(final String messageLocal) {
		this.messageLocal = messageLocal;
	}

	public ComplaintDetails getComplaintDetails() {
		return this.complaintDetails;
	}

	public void setComplaintDetails(final ComplaintDetails complaintDetails) {
		this.complaintDetails = complaintDetails;
	}

}