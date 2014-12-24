package org.egov.erpcollection.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.infstr.models.BaseModel;

/**
 * CollectionStgReceipt entity. @author MyEclipse Persistence Tools
 */

public class CollectionStgReceipt extends BaseModel {
	private static final long serialVersionUID = 1L;
	
	private String billingSysId;
	private String module;
	private Integer wardNo;
	private String rcptNo;
	private Date rcptDate;
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

	public Date getRcptDate() {
		return this.rcptDate;
	}

	public void setRcptDate(Date rcptDate) {
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