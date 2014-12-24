/*
 * @(#)LicenseStatusValues.java 3.0, 29 Jul, 2013 1:24:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

import java.util.Date;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.models.BaseModel;

public class LicenseStatusValues extends BaseModel {

	private static final long serialVersionUID = 1L;
	private License license;
	private LicenseStatus licenseStatus;
	private Date referenceDate;
	private String referenceNo;
	private String remarks;
	private boolean active;
	private String docNumber;

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return this.active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(final boolean active) {
		this.active = active;
	}

	private String extraField1;
	private String extraField2;
	private String extraField3;
	private LicenseStatusValues previousStatusVal;
	private Integer reason;

	/**
	 * @return Returns if the given Object is equal to PropertyStatusValues
	 */

	@Override
	public boolean equals(final Object that) {
		if (that == null) {
			return false;
		}

		if (this == that) {
			return true;
		}

		if (that.getClass() != this.getClass()) {
			return false;
		}
		final LicenseStatusValues thatPropStatus = (LicenseStatusValues) that;

		if (this.getId() != null && thatPropStatus.getId() != null) {
			if (getId().equals(thatPropStatus.getId())) {
				return true;
			} else {
				return false;
			}
		} else if (this.getLicense() != null && thatPropStatus.getLicense() != null) {
			if (getLicense().equals(thatPropStatus.getLicense())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * @return Returns the hashCode
	 */
	@Override
	public int hashCode() {
		int hashCode = 0;
		if (this.getId() != null) {
			hashCode += this.getId().hashCode();
		} else if (this.getLicense() != null) {
			hashCode += this.getLicense().hashCode();
		}
		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validatePropStatusValues() {
		if (getLicense() == null) {
			throw new EGOVRuntimeException("In PropertyStatusValues Validate : 'ID_Property' Attribute is Not Set, Please Check !!");
		}
		if (getLicenseStatus() == null) {
			throw new EGOVRuntimeException("In PropertyStatusValues Validate : 'ID_Status' Attribute is Not Set, Please Check !!");
		}
		return true;
	}

	public License getLicense() {
		return this.license;
	}

	public void setLicense(final License license) {
		this.license = license;
	}

	public LicenseStatus getLicenseStatus() {
		return this.licenseStatus;
	}

	public void setLicenseStatus(final LicenseStatus licenseStatus) {
		this.licenseStatus = licenseStatus;
	}

	public Date getReferenceDate() {
		return this.referenceDate;
	}

	public void setReferenceDate(final Date referenceDate) {
		this.referenceDate = referenceDate;
	}

	public String getReferenceNo() {
		return this.referenceNo;
	}

	public void setReferenceNo(final String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(final String remarks) {
		this.remarks = remarks;
	}

	public String getExtraField1() {
		return this.extraField1;
	}

	public void setExtraField1(final String extraField1) {
		this.extraField1 = extraField1;
	}

	public String getExtraField2() {
		return this.extraField2;
	}

	public void setExtraField2(final String extraField2) {
		this.extraField2 = extraField2;
	}

	public String getExtraField3() {
		return this.extraField3;
	}

	public void setExtraField3(final String extraField3) {
		this.extraField3 = extraField3;
	}

	/**
	 * @return the previousStatusVal
	 */
	public LicenseStatusValues getPreviousStatusVal() {
		return this.previousStatusVal;
	}

	/**
	 * @param previousStatusVal the previousStatusVal to set
	 */
	public void setPreviousStatusVal(final LicenseStatusValues previousStatusVal) {
		this.previousStatusVal = previousStatusVal;
	}

	/**
	 * @return the reason
	 */
	public Integer getReason() {
		return this.reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(final Integer reason) {
		this.reason = reason;
	}

	public String getDocNumber() {
		return this.docNumber;
	}

	public void setDocNumber(final String docNumber) {
		this.docNumber = docNumber;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("LicenseStatusValues={");
		str.append("license=").append(this.license == null ? "null" : this.license.toString());
		str.append("licenseStatus=").append(this.licenseStatus == null ? "null" : this.licenseStatus.toString());
		str.append("referenceDate=").append(this.referenceDate == null ? "null" : this.referenceDate.toString());
		str.append("referenceNo=").append(this.referenceNo == null ? "null" : this.referenceNo.toString());
		str.append("remarks=").append(this.remarks == null ? "null" : this.remarks.toString());
		str.append("active=").append(this.active);
		str.append("docNumber=").append(this.docNumber == null ? "null" : this.docNumber.toString());
		str.append("extraField1=").append(this.extraField1 == null ? "null" : this.extraField1.toString());
		str.append("extraField2=").append(this.extraField2 == null ? "null" : this.extraField2.toString());
		str.append("extraField3=").append(this.extraField3 == null ? "null" : this.extraField3.toString());
		str.append("previousStatusVal=").append(this.previousStatusVal == null ? "null" : this.previousStatusVal.toString());
		str.append("reason=").append(this.reason == null ? "null" : this.reason.toString());
		str.append("}");
		return str.toString();
	}
}
