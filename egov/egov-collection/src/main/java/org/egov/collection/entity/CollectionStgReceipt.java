/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.infstr.models.BaseModel;
import org.joda.time.DateTime;

/**
 * CollectionStgReceipt entity. @author MyEclipse Persistence Tools
 */

public class CollectionStgReceipt extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	private String billingSysId;
	private String module;
	private Integer wardNo;
	private String rcptNo;
	private DateTime rcptDate;
	private BigDecimal rcptAmount;
	private Character collType;
	private Character isCollSysUpdated;
	private Character isBillSysUpdated;
	private String paidBy;
	private String additionalInfo1;
	private String additionalInfo2;
	private String additionalInfo3;
	private String additionalInfo4;
	private String collReceiptNo;
	private String collMessage;
	private String billMessage;
	private Set<CollectionStgAccAmount> collectionStgAccAmounts = new HashSet<CollectionStgAccAmount>();
	private Set<CollectionStgInstrument> collectionStgInstruments = new HashSet<CollectionStgInstrument>();
	private Character status;

	public String getBillingSysId() {
		return this.billingSysId;
	}

	public void setBillingSysId(String billingSysId) {
		this.billingSysId = billingSysId;
	}

	public String getModule() {
		return this.module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Integer getWardNo() {
		return this.wardNo;
	}

	public void setWardNo(Integer wardNo) {
		this.wardNo = wardNo;
	}

	public String getRcptNo() {
		return this.rcptNo;
	}

	public void setRcptNo(String rcptNo) {
		this.rcptNo = rcptNo;
	}

	public DateTime getRcptDate() {
		return this.rcptDate;
	}

	public void setRcptDate(DateTime rcptDate) {
		this.rcptDate = rcptDate;
	}

	public BigDecimal getRcptAmount() {
		return this.rcptAmount;
	}

	public void setRcptAmount(BigDecimal rcptAmount) {
		this.rcptAmount = rcptAmount;
	}

	public Character getCollType() {
		return this.collType;
	}

	public void setCollType(Character collType) {
		this.collType = collType;
	}

	public Character getIsCollSysUpdated() {
		return this.isCollSysUpdated;
	}

	public void setIsCollSysUpdated(Character isCollSysUpdated) {
		this.isCollSysUpdated = isCollSysUpdated;
	}

	public Character getIsBillSysUpdated() {
		return this.isBillSysUpdated;
	}

	public void setIsBillSysUpdated(Character isBillSysUpdated) {
		this.isBillSysUpdated = isBillSysUpdated;
	}

	public Set<CollectionStgAccAmount> getCollectionStgAccAmounts() {
		return this.collectionStgAccAmounts;
	}

	public void setCollectionStgAccAmounts(Set<CollectionStgAccAmount> collectionStgAccAmounts) {
		this.collectionStgAccAmounts = collectionStgAccAmounts;
	}

	public Set<CollectionStgInstrument> getCollectionStgInstruments() {
		return this.collectionStgInstruments;
	}

	public void setCollectionStgInstruments(Set<CollectionStgInstrument> collectionStgInstruments) {
		this.collectionStgInstruments = collectionStgInstruments;
	}

	public String getPaidBy() {
		return paidBy;
	}

	public void setPaidBy(String paidBy) {
		this.paidBy = paidBy;
	}

	public String getAdditionalInfo1() {
		return additionalInfo1;
	}

	public void setAdditionalInfo1(String additionalInfo1) {
		this.additionalInfo1 = additionalInfo1;
	}

	public String getAdditionalInfo2() {
		return additionalInfo2;
	}

	public void setAdditionalInfo2(String additionalInfo2) {
		this.additionalInfo2 = additionalInfo2;
	}

	public String getAdditionalInfo3() {
		return additionalInfo3;
	}

	public void setAdditionalInfo3(String additionalInfo3) {
		this.additionalInfo3 = additionalInfo3;
	}

	public String getAdditionalInfo4() {
		return additionalInfo4;
	}

	public void setAdditionalInfo4(String additionalInfo4) {
		this.additionalInfo4 = additionalInfo4;
	}

	public String getCollReceiptNo() {
		return collReceiptNo;
	}

	public void setCollReceiptNo(String collReceiptNo) {
		this.collReceiptNo = collReceiptNo;
	}

	public String getCollMessage() {
		return collMessage;
	}

	public void setCollMessage(String collMessage) {
		this.collMessage = collMessage;
	}

	public String getBillMessage() {
		return billMessage;
	}

	public void setBillMessage(String billMessage) {
		this.billMessage = billMessage;
	}

	/**
	 * @return the status
	 */
	public Character getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Character status) {
		this.status = status;
	}

}