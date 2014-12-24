/*
 * @(#)RedressalDetails.java 3.0, 23 Jul, 2013 3:29:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.entities;

import java.util.Date;

import org.egov.infstr.models.BaseModel;
import org.egov.lib.rjbac.user.User;
import org.egov.pims.commons.Position;

public class RedressalDetails extends BaseModel {

	private static final long serialVersionUID = 1L;

	private ComplaintDetails complaintDetails;
	private User redressalOfficer;
	private ComplaintStatus complaintStatus;
	private Date responsedate;
	private String response;
	private String extrafield1;
	private String extrafield2;
	private String extrafield3;
	private Integer actiontype;
	private String responseLocal;
	private Position position;

	public Date getResponsedate() {
		return this.responsedate;
	}

	public void setResponsedate(final Date responsedate) {
		this.responsedate = responsedate;
	}

	public ComplaintDetails getComplaintDetails() {
		return this.complaintDetails;
	}

	public void setComplaintDetails(final ComplaintDetails complaintDetails) {
		this.complaintDetails = complaintDetails;
	}

	public ComplaintStatus getComplaintStatus() {
		return this.complaintStatus;
	}

	public void setComplaintStatus(final ComplaintStatus complaintStatus) {
		this.complaintStatus = complaintStatus;
	}

	public String getResponse() {
		return this.response;
	}

	public void setResponse(final String response) {
		this.response = response;
	}

	public String getExtrafield1() {
		return this.extrafield1;
	}

	public void setExtrafield1(final String extrafield1) {
		this.extrafield1 = extrafield1;
	}

	public String getExtrafield2() {
		return this.extrafield2;
	}

	public void setExtrafield2(final String extrafield2) {
		this.extrafield2 = extrafield2;
	}

	public String getExtrafield3() {
		return this.extrafield3;
	}

	public void setExtrafield3(final String extrafield3) {
		this.extrafield3 = extrafield3;
	}

	public Integer getActiontype() {
		return this.actiontype;
	}

	public void setActiontype(final Integer actiontype) {
		this.actiontype = actiontype;
	}

	public String getResponseLocal() {
		return this.responseLocal;
	}

	public void setResponseLocal(final String responseLocal) {
		this.responseLocal = responseLocal;
	}

	/**
	 * @return the redressalOfficer
	 */
	public User getRedressalOfficer() {
		return this.redressalOfficer;
	}

	/**
	 * @param redressalOfficer the redressalOfficer to set
	 */
	public void setRedressalOfficer(final User redressalOfficer) {
		this.redressalOfficer = redressalOfficer;
	}

	/**
	 * @return the position
	 */
	public Position getPosition() {
		return this.position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(final Position position) {
		this.position = position;
	}

}