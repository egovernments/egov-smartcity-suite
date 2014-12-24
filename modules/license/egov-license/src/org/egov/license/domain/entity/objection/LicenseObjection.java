/*
 * @(#)LicenseObjection.java 3.0, 29 Jul, 2013 1:24:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity.objection;

import java.util.Date;
import java.util.List;

import org.egov.infstr.models.StateAware;
import org.egov.license.domain.entity.License;

public class LicenseObjection extends StateAware {
	private static final long serialVersionUID = 1L;
	public static final String BY_ID = "LISENSEOBJECTION_BY_ID";
	private License license;
	private String number;
	private Integer reason;
	private String name; // Name of the Objectioner
	private String address;// address of the Objectioner
	private String details;
	private Date objectionDate;
	private String docNumber;
	private List<Activity> activities;
	private List<Notice> notices;

	public List<Notice> getNotices() {
		return this.notices;
	}

	public void setNotices(final List<Notice> notices) {
		this.notices = notices;
	}

	public License getLicense() {
		return this.license;
	}

	public void setLicense(final License license) {
		this.license = license;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(final String details) {
		this.details = details;
	}

	public Date getObjectionDate() {
		return this.objectionDate;
	}

	public void setObjectionDate(final Date objectionDate) {
		this.objectionDate = objectionDate;
	}

	public String getDocNumber() {
		return this.docNumber;
	}

	public void setDocNumber(final String docNumber) {
		this.docNumber = docNumber;
	}

	@Override
	public String getStateDetails() {
		return this.getState().getText1();
	}

	public Integer getReason() {
		return this.reason;
	}

	public void setReason(final Integer reason) {
		this.reason = reason;
	}

	public List<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(final List<Activity> activities) {
		this.activities = activities;
	}

	public String generateNumber(final String runningNumber) {
		this.number = "OBJ" + runningNumber;
		return this.number;
	}

	@Override
	public String myLinkId() {
		return this.getCurrentState().getText2() + "/objection/objection!showForApproval.action?model.id=" + this.getId();
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("LicenseObjection={");
		str.append("number=").append(this.number == null ? "null" : this.number.toString());
		str.append("name=").append(this.name == null ? "null" : this.name.toString());
		str.append("address=").append(this.address == null ? "null" : this.address.toString());
		str.append("details=").append(this.details == null ? "null" : this.details.toString());
		str.append("objectionDate=").append(this.objectionDate == null ? "null" : this.objectionDate.toString());
		str.append("docNumber=").append(this.docNumber == null ? "null" : this.docNumber.toString());
		str.append("}");
		return str.toString();
	}
}
