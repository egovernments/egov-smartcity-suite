/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *     accountability and the service delivery of the government  organizations.
 *  
 *      Copyright (C) <2015>  eGovernments Foundation
 *  
 *      The updated version of eGov suite of products as by eGovernments Foundation 
 *      is available at http://www.egovernments.org
 *  
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *  
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *  
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or 
 *      http://www.gnu.org/licenses/gpl.html .
 *  
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *  
 *  	1) All versions of this program, verbatim or modified must carry this 
 *  	   Legal Notice.
 *  
 *  	2) Any misrepresentation of the origin of the material is prohibited. It 
 *  	   is required that all modified versions of this material be marked in 
 *  	   reasonable ways as different from the original version.
 *  
 *  	3) This license does not grant any rights to any user of the program 
 *  	   with regards to rights under trademark law for use of the trade names 
 *  	   or trademarks of eGovernments Foundation.
 *  
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tradelicense.domain.entity;

import java.util.Date;

import org.egov.EGOVRuntimeException;
import org.egov.infstr.models.BaseModel;


/*
 * PropertyStatusValues.java Created on Oct 20, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */




/**
 * <p>
 * This class defines Property Status i.e A Property has a Status indicating its
 * current state.
 * </p>
 * PropertyStatusValues can be Assessed, UnAssessed etc.
 * 
 * @author Gayathri Joshi
 * @version 2.00
 * @see
 * @see
 * @since 1.00
 */
public class LicenseStatusValues extends BaseModel {

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
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
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

	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (this == that)
			return true;

		if (that.getClass() != this.getClass())
			return false;
		final LicenseStatusValues thatPropStatus = (LicenseStatusValues) that;

		if (this.getId() != null && thatPropStatus.getId() != null) {
			if (getId().equals(thatPropStatus.getId())) {
				return true;
			} else
				return false;
		} else if (this.getLicense() != null && thatPropStatus.getLicense() != null) {
			if (getLicense().equals(thatPropStatus.getLicense())) {
				return true;
			} else
				return false;
		} else
			return false;
	}

	/**
	 * @return Returns the hashCode
	 */
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
		if (getLicense() == null)
			throw new EGOVRuntimeException(
					"In PropertyStatusValues Validate : 'ID_Property' Attribute is Not Set, Please Check !!");
		if (getLicenseStatus() == null)
			throw new EGOVRuntimeException(
					"In PropertyStatusValues Validate : 'ID_Status' Attribute is Not Set, Please Check !!");
		return true;
	}

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public LicenseStatus getLicenseStatus() {
		return licenseStatus;
	}

	public void setLicenseStatus(LicenseStatus licenseStatus) {
		this.licenseStatus = licenseStatus;
	}

	public Date getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	

	public String getExtraField1() {
		return extraField1;
	}

	public void setExtraField1(String extraField1) {
		this.extraField1 = extraField1;
	}

	public String getExtraField2() {
		return extraField2;
	}

	public void setExtraField2(String extraField2) {
		this.extraField2 = extraField2;
	}

	public String getExtraField3() {
		return extraField3;
	}

	public void setExtraField3(String extraField3) {
		this.extraField3 = extraField3;
	}

	/**
	 * @return the previousStatusVal
	 */
	public LicenseStatusValues getPreviousStatusVal() {
		return previousStatusVal;
	}

	/**
	 * @param previousStatusVal the previousStatusVal to set
	 */
	public void setPreviousStatusVal(LicenseStatusValues previousStatusVal) {
		this.previousStatusVal = previousStatusVal;
	}

	/**
	 * @return the reason
	 */
	public Integer getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(Integer reason) {
		this.reason = reason;
	}

	public String getDocNumber() {
	    return docNumber;
	}

	public void setDocNumber(String docNumber) {
	    this.docNumber = docNumber;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("LicenseStatusValues={");
		str.append("license=").append(license == null ? "null" : license.toString());
		str.append("licenseStatus=").append(licenseStatus == null ? "null" : licenseStatus.toString());
		str.append("referenceDate=").append(referenceDate == null ? "null" : referenceDate.toString());
		str.append("referenceNo=").append(referenceNo == null ? "null" : referenceNo.toString());
		str.append("remarks=").append(remarks == null ? "null" : remarks.toString());
		str.append("active=").append(active);
		str.append("docNumber=").append(docNumber == null ? "null" : docNumber.toString());
		str.append("extraField1=").append(extraField1 == null ? "null" : extraField1.toString());
		str.append("extraField2=").append(extraField2 == null ? "null" : extraField2.toString());
		str.append("extraField3=").append(extraField3 == null ? "null" : extraField3.toString());
		str.append("previousStatusVal=").append(previousStatusVal == null ? "null" : previousStatusVal.toString());
		str.append("reason=").append(reason == null ? "null" : reason.toString());
		str.append("}");
		return str.toString();
	}
}
