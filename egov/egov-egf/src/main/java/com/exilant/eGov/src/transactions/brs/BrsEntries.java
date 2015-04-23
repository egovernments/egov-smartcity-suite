/*
 * BrsEntries.java  Created on Aug 25, 2006
 *
 *  Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions.brs;

/**
 * @author Tilak
 *
 * @Version 1.00
 */
public class BrsEntries
{

	public String getPayCheque() {
		return payCheque;
	}
	public void setPayCheque(String payCheque) {
		this.payCheque = payCheque;
	}
	/**
	 * @return Returns the voucherHeaderId.
	 */
	public String getVoucherHeaderId() {
		return voucherHeaderId;
	}
	/**
	 * @param voucherHeaderId The voucherHeaderId to set.
	 */
	public void setVoucherHeaderId(String voucherHeaderId) {
		this.voucherHeaderId = voucherHeaderId;
	}
	private String id="";
	private String refNo="";
	private String type="";
	private String txnDate="";
	private String txnAmount="";
	private String remarks="";
	private String glCodeId="";
	private String instrumentHeaderId;
	/**
	 * @return the instrumentHeaderId
	 */
	public String getInstrumentHeaderId() {
		return instrumentHeaderId;
	}
	/**
	 * @param instrumentHeaderId the instrumentHeaderId to set
	 */
	public void setInstrumentHeaderId(String instrumentHeaderId) {
		this.instrumentHeaderId = instrumentHeaderId;
	}
	public String getCgnum() {
		return cgnum;
	}
	public void setCgnum(String cgnum) {
		this.cgnum = cgnum;
	}
	public String getAccNumber() {
		return accNumber;
	}
	public void setAccNumber(String accNumber) {
		this.accNumber = accNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getPayTo() {
		return payTo;
	}
	public void setPayTo(String payTo) {
		this.payTo = payTo;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
// added for DishonoredCheque
	private String voucherNumber="";
	private String cgnum="";
	private String voucherHeaderId="";
	private String payinSlipVHeaderId="";
	private String fundId="";
	private String fundSourceId="";
	private String	chequeNumber="";
	private String	chequeDate="";
	private String	amount="";
	private String	lotNumber="";
	private String	lotType="";
	private String	field="";

	private String	voucherType="";
	private String	bankName="";
	private String	accNumber="";
	private String	accIdParam="";
	private String	payTo="";
	private String	payCheque="";
	private String departmentId;
	private String functionaryId;
	private String functionId="";

	/**
	 * @return Returns the field.
	 */
	public String getField() {
		return field;
	}
	/**
	 * @param field The field to set.
	 */
	public void setField(String field) {
		this.field = field;
	}
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Returns the refNo.
	 */
	public String getRefNo() {
		return refNo;
	}
	/**
	 * @param refNo The refNo to set.
	 */
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	/**
	 * @return Returns the remarks.
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks The remarks to set.
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return Returns the txnAmount.
	 */
	public String getTxnAmount() {
		return txnAmount;
	}
	/**
	 * @param txnAmount The txnAmount to set.
	 */
	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}
	/**
	 * @return Returns the txnDate.
	 */
	public String getTxnDate() {
		return txnDate;
	}
	/**
	 * @param txnDate The txnDate to set.
	 */
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
		 * @return Returns the glCodeId.
		 */
		public String getGlCodeId() {
			return glCodeId;
		}
		/**
		 * @param type The glCodeId to set.
		 */
		public void setGlCodeId(String glCodeId) {
			this.glCodeId = glCodeId;
	}
	/**
	 * @return Returns the amount.
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * @param amount The amount to set.
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}
	/**
	 * @return Returns the chequeDate.
	 */
	public String getChequeDate() {
		return chequeDate;
	}
	/**
	 * @param chequeDate The chequeDate to set.
	 */
	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}
	/**
	 * @return Returns the chequeNumber.
	 */
	public String getChequeNumber() {
		return chequeNumber;
	}
	/**
	 * @param chequeNumber The chequeNumber to set.
	 */
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	/**
	 * @return Returns the fundId.
	 */
	public String getFundId() {
		return fundId;
	}
	/**
	 * @param fundId The fundId to set.
	 */
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	/**
	 * @return Returns the fundSourceId.
	 */
	public String getFundSourceId() {
		return fundSourceId;
	}
	/**
	 * @param fundSourceId The fundSourceId to set.
	 */
	public void setFundSourceId(String fundSourceId) {
		this.fundSourceId = fundSourceId;
	}
	/**
	 * @return Returns the lotNumber.
	 */
	public String getLotNumber() {
		return lotNumber;
	}
	/**
	 * @param lotNumber The lotNumber to set.
	 */
	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}
	/**
	 * @return Returns the voucherNumber.
	 */
	public String getVoucherNumber() {
		return voucherNumber;
	}
	/**
	 * @param voucherNumber The voucherNumber to set.
	 */
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	/**
	 * @return Returns the payinSlipVHeaderId.
	 */
	public String getPayinSlipVHeaderId() {
		return payinSlipVHeaderId;
	}
	/**
	 * @param payinSlipVHeaderId The payinSlipVHeaderId to set.
	 */
	public void setPayinSlipVHeaderId(String payinSlipVHeaderId) {
		this.payinSlipVHeaderId = payinSlipVHeaderId;
	}
	/**
	 * @return Returns the lotType.
	 */
	public String getLotType() {
		return lotType;
	}
	/**
	 * @param lotType The lotType to set.
	 */
	public void setLotType(String lotType) {
		this.lotType = lotType;
	}
	public String getAccIdParam() {
		return accIdParam;
	}
	public void setAccIdParam(String accIdParam) {
		this.accIdParam = accIdParam;
	}
	/**
	 * @return the departmentId
	 */
	public String getDepartmentId() {
		return departmentId;
	}
	/**
	 * @param departmentId the departmentId to set
	 */
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	/**
	 * @return the functionaryId
	 */
	public String getFunctionaryId() {
		return functionaryId;
	}
	/**
	 * @param functionaryId the functionaryId to set
	 */
	public void setFunctionaryId(String functionaryId) {
		this.functionaryId = functionaryId;
	}
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
}
