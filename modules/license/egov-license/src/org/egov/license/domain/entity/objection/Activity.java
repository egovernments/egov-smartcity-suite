/*
 * @(#)Activity.java 3.0, 29 Jul, 2013 1:24:28 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity.objection;

import java.util.Date;

import org.egov.infstr.models.BaseModel;

public class Activity extends BaseModel {
	private static final long serialVersionUID = -6369641818759075953L;
	private LicenseObjection objection;
	private String details;
	private Date activityDate;
	private String remarks;
	private Date expectedDateOfResponse;
	// for uploaded document references
	private String docNumber;
	private String type;

	public LicenseObjection getObjection() {
		return this.objection;
	}

	public void setObjection(final LicenseObjection objection) {
		this.objection = objection;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(final String details) {
		this.details = details;
	}

	public Date getActivityDate() {
		return this.activityDate;
	}

	public void setActivityDate(final Date activityDate) {
		this.activityDate = activityDate;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(final String remarks) {
		this.remarks = remarks;
	}

	public Date getExpectedDateOfResponse() {
		return this.expectedDateOfResponse;
	}

	public void setExpectedDateOfResponse(final Date expectedDateOfResponse) {
		this.expectedDateOfResponse = expectedDateOfResponse;
	}

	public String getDocNumber() {
		return this.docNumber;
	}

	public void setDocNumber(final String docNumber) {
		this.docNumber = docNumber;
	}

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("Activity={");
		str.append("objection=").append(this.objection == null ? "null" : this.objection.toString());
		str.append("details=").append(this.details == null ? "null" : this.details.toString());
		str.append("activityDate=").append(this.activityDate == null ? "null" : this.activityDate.toString());
		str.append("remarks=").append(this.remarks == null ? "null" : this.remarks.toString());
		str.append("expectedDateOfResponse=").append(this.expectedDateOfResponse == null ? "null" : this.expectedDateOfResponse.toString());
		str.append("docNumber=").append(this.docNumber == null ? "null" : this.docNumber.toString());
		str.append("type=").append(this.type == null ? "null" : this.type.toString());
		str.append("}");
		return str.toString();
	}

}
