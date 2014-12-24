/*
 * @(#)StatusTracker.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.entities;

import java.util.Date;

import org.egov.infstr.models.BaseModel;
import org.egov.lib.rjbac.user.User;

public class StatusTracker extends BaseModel {

	private static final long serialVersionUID = 1L;

	private User fromUserId;
	private RedressalDetails redressalDetails;
	private User toUserId;
	private ComplaintStatus complaintStatus;
	private Communication communication;
	private Integer orderId;
	private Date timestamp;

	public User getFromUserId() {
		return this.fromUserId;
	}

	public void setFromUserId(final User fromUserId) {
		this.fromUserId = fromUserId;
	}

	public User getToUserId() {
		return this.toUserId;
	}

	public void setToUserId(final User toUserId) {
		this.toUserId = toUserId;
	}

	public Integer getOrderId() {
		return this.orderId;
	}

	public void setOrderId(final Integer orderId) {
		this.orderId = orderId;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}

	public RedressalDetails getRedressalDetails() {
		return this.redressalDetails;
	}

	public void setRedressalDetails(final RedressalDetails redressalDetails) {
		this.redressalDetails = redressalDetails;
	}

	public ComplaintStatus getComplaintStatus() {
		return this.complaintStatus;
	}

	public void setComplaintStatus(final ComplaintStatus complaintStatus) {
		this.complaintStatus = complaintStatus;
	}

	public Communication getCommunication() {
		return this.communication;
	}

	public void setCommunication(final Communication communication) {
		this.communication = communication;
	}

}