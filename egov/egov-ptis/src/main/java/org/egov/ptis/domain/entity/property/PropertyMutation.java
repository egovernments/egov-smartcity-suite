/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
/*
 * PropertyMutation.java
 * Created on May 11, 2007
 *
 * Copyright 2006 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.EgwStatus;
import org.egov.infstr.models.BaseModel;

public class PropertyMutation extends BaseModel {
	private PropertyMutationMaster propMutationMstr;
	private String mutationNo;
	private Date mutationDate;
	private BigDecimal mutationFee;
	private BigDecimal otherFee;
	private String receiptNum;
	private Date noticeDate;
	private String applicationNo;
	private String applicantName;
	private BasicProperty basicProperty;
	private EgwStatus applnStatus;
	private String refPid;
	private Set<PropertyMutationOwner> mutationOwnerSet = new HashSet<PropertyMutationOwner>();
	private Date lastUpdatedTimeStamp;
	private Date createTimeStamp;
	private Integer userId;
	private BigDecimal consTransfee;
	private Character isTfPayable;
	private String deedNo;
	private Date deedDate;
	private String extraField1;
	private String extraField2;
	private String extraField3;
	private String extraField4;
	private String documentNumber;
	private String ownerNameOld;

	public PropertyMutationMaster getPropMutationMstr() {
		return propMutationMstr;
	}

	public void setPropMutationMstr(PropertyMutationMaster propMutationMstr) {
		this.propMutationMstr = propMutationMstr;
	}

	public String getMutationNo() {
		return mutationNo;
	}

	public void setMutationNo(String mutationNo) {
		this.mutationNo = mutationNo;
	}

	public Date getMutationDate() {
		return mutationDate;
	}

	public void setMutationDate(Date mutationDate) {
		this.mutationDate = mutationDate;
	}

	public Date getNoticeDate() {
		return noticeDate;
	}

	public void setNoticeDate(Date noticeDate) {
		this.noticeDate = noticeDate;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}

	public EgwStatus getApplnStatus() {
		return applnStatus;
	}

	public void setApplnStatus(EgwStatus applnStatus) {
		this.applnStatus = applnStatus;
	}

	public String getRefPid() {
		return refPid;
	}

	public void setRefPid(String refPid) {
		this.refPid = refPid;
	}

	public Set<PropertyMutationOwner> getMutationOwnerSet() {
		return mutationOwnerSet;
	}

	public void setMutationOwnerSet(Set<PropertyMutationOwner> mutationOwnerSet) {
		this.mutationOwnerSet = mutationOwnerSet;
	}

	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	public Date getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(Date createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getConsTransfee() {
		return consTransfee;
	}

	public void setConsTransfee(BigDecimal consTransfee) {
		this.consTransfee = consTransfee;
	}

	public Character getIsTfPayable() {
		return isTfPayable;
	}

	public void setIsTfPayable(Character isTfPayable) {
		this.isTfPayable = isTfPayable;
	}

	public String getDeedNo() {
		return deedNo;
	}

	public void setDeedNo(String deedNo) {
		this.deedNo = deedNo;
	}

	public Date getDeedDate() {
		return deedDate;
	}

	public void setDeedDate(Date deedDate) {
		this.deedDate = deedDate;
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

	public String getExtraField4() {
		return extraField4;
	}

	public void setExtraField4(String extraField4) {
		this.extraField4 = extraField4;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public BigDecimal getMutationFee() {
		return mutationFee;
	}

	public void setMutationFee(BigDecimal mutationFee) {
		this.mutationFee = mutationFee;
	}

	public String getReceiptNum() {
		return receiptNum;
	}

	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}

	public String getOwnerNameOld() {
		return ownerNameOld;
	}

	public void setOwnerNameOld(String ownerNameOld) {
		this.ownerNameOld = ownerNameOld;
	}

	
	public BigDecimal getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(BigDecimal otherFee) {
		this.otherFee = otherFee;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getId()).append("ApplicantName: ").append(getApplicantName()).append("|MutationMaster: ");
		objStr = (getPropMutationMstr() != null) ? objStr.append(getPropMutationMstr().getMutationName()) : objStr
				.append("");
		objStr.append("|MutationNo: " + getMutationNo()).append("|MutationFee: " + getMutationFee()).append(
				"|ReceiptNum: ").append(getReceiptNum()).append("|AppNo: ").append(getApplicationNo()).append(
				"|BasicProperty: ");
		objStr = (getBasicProperty() != null) ? objStr.append(getBasicProperty().getUpicNo()) : objStr.append("");
		objStr.append("|AppStatus: ").append(getApplnStatus());

		return objStr.toString();
	}
}
