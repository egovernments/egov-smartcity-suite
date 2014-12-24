/*
 * @(#)LicenseTransfer.java 3.0, 29 Jul, 2013 1:24:28 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity.transfer;

import java.util.Date;

import org.egov.infstr.models.StateAware;
import org.egov.lib.address.model.Address;
import org.egov.lib.admbndry.Boundary;
import org.egov.license.domain.entity.License;

public class LicenseTransfer extends StateAware {
	private static final long serialVersionUID = 1L;
	private License license;
	private String oldApplicantName;
	private String oldApplicationNumber;
	private Date oldApplicationDate;
	private String oldNameOfEstablishment;
	private Address oldAddress;
	private String oldPhoneNumber;
	private String oldHomePhoneNumber;
	private String oldMobileNumber;
	private String oldEmailId;
	private String oldUid;
	protected Boundary boundary;

	public Boundary getBoundary() {
		return this.boundary;
	}

	public void setBoundary(final Boundary boundary) {
		this.boundary = boundary;
	}

	private boolean approved;

	public String getOldNameOfEstablishment() {
		return this.oldNameOfEstablishment;
	}

	public void setOldNameOfEstablishment(final String oldNameOfEstablishment) {
		this.oldNameOfEstablishment = oldNameOfEstablishment;
	}

	public String getOldPhoneNumber() {
		return this.oldPhoneNumber;
	}

	public void setOldPhoneNumber(final String oldPhoneNumber) {
		this.oldPhoneNumber = oldPhoneNumber;
	}

	public boolean isApproved() {
		return this.approved;
	}

	public void setApproved(final boolean approved) {
		this.approved = approved;
	}

	private String type;

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public License getLicense() {
		return this.license;
	}

	public void setLicense(final License license) {
		this.license = license;
	}

	public String getOldApplicantName() {
		return this.oldApplicantName;
	}

	public void setOldApplicantName(final String oldApplicantName) {
		this.oldApplicantName = oldApplicantName;
	}

	public String getOldApplicationNumber() {
		return this.oldApplicationNumber;
	}

	public void setOldApplicationNumber(final String oldApplicationNumber) {
		this.oldApplicationNumber = oldApplicationNumber;
	}

	public Date getOldApplicationDate() {
		return this.oldApplicationDate;
	}

	public void setOldApplicationDate(final Date oldApplicationDate) {
		this.oldApplicationDate = oldApplicationDate;
	}

	public Address getOldAddress() {
		return this.oldAddress;
	}

	public void setOldAddress(final Address oldAddress) {
		this.oldAddress = oldAddress;
	}

	@Override
	public String getStateDetails() {
		return this.license.getApplicationNumber();
	}

	public String getOldHomePhoneNumber() {
		return this.oldHomePhoneNumber;
	}

	public void setOldHomePhoneNumber(final String oldHomePhoneNumber) {
		this.oldHomePhoneNumber = oldHomePhoneNumber;
	}

	public String getOldMobileNumber() {
		return this.oldMobileNumber;
	}

	public void setOldMobileNumber(final String oldMobileNumber) {
		this.oldMobileNumber = oldMobileNumber;
	}

	public String getOldEmailId() {
		return this.oldEmailId;
	}

	public void setOldEmailId(final String oldEmailId) {
		this.oldEmailId = oldEmailId;
	}

	public String getOldUid() {
		return this.oldUid;
	}

	public void setOldUid(final String oldUid) {
		this.oldUid = oldUid;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("LicenseTransfer={");
		str.append("oldApplicantName=").append(this.oldApplicantName == null ? "null" : this.oldApplicantName.toString());
		str.append("oldApplicationNumber=").append(this.oldApplicationNumber == null ? "null" : this.oldApplicationNumber.toString());
		str.append("oldApplicationDate=").append(this.oldApplicationDate == null ? "null" : this.oldApplicationDate.toString());
		str.append("oldNameOfEstablishment=").append(this.oldNameOfEstablishment == null ? "null" : this.oldNameOfEstablishment.toString());
		str.append("oldAddress=").append(this.oldAddress == null ? "null" : this.oldAddress.toString());
		str.append("oldPhoneNumber=").append(this.oldPhoneNumber == null ? "null" : this.oldPhoneNumber.toString());
		str.append("oldHomePhoneNumber=").append(this.oldHomePhoneNumber == null ? "null" : this.oldHomePhoneNumber.toString());
		str.append("oldMobileNumber=").append(this.oldMobileNumber == null ? "null" : this.oldMobileNumber.toString());
		str.append("oldEmailId=").append(this.oldEmailId == null ? "null" : this.oldEmailId.toString());
		str.append("oldUid=").append(this.oldUid == null ? "null" : this.oldUid.toString());
		str.append("boundary=").append(this.boundary == null ? "null" : this.boundary.toString());
		str.append("}");
		return str.toString();
	}
}
