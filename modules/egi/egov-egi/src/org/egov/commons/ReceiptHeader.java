/*
 * @(#)ReceiptHeader.java 3.0, 6 Jun, 2013 4:29:08 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import java.math.BigDecimal;

public class ReceiptHeader implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private CVoucherHeader voucherHeaderId;
	private String type;
	private Integer wardId;
	private Bank bankId;
	private Bankbranch bankBranchId;
	private Bankaccount bankAccNoId;
	private String modeOfCollection;
	private Chequedetail chequeId;
	private BigDecimal cashAmount;

	private String narration;
	private String revenueSource;
	private int isReversed;
	private String cashier;
	private String receiptNo;
	private String manualReceiptNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CVoucherHeader getVoucherHeaderId() {
		return voucherHeaderId;
	}

	public void setVoucherHeaderId(CVoucherHeader voucherHeaderId) {
		this.voucherHeaderId = voucherHeaderId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Bank getBankId() {
		return bankId;
	}

	public void setBankId(Bank bankId) {
		this.bankId = bankId;
	}

	public Bankbranch getBankBranchId() {
		return bankBranchId;
	}

	public void setBankBranchId(Bankbranch bankBranchId) {
		this.bankBranchId = bankBranchId;
	}

	public Bankaccount getBankAccNoId() {
		return bankAccNoId;
	}

	public void setBankAccNoId(Bankaccount bankAccNoId) {
		this.bankAccNoId = bankAccNoId;
	}

	public String getModeOfCollection() {
		return modeOfCollection;
	}

	public void setModeOfCollection(String modeOfCollection) {
		this.modeOfCollection = modeOfCollection;
	}

	public Chequedetail getChequeId() {
		return chequeId;
	}

	public void setChequeId(Chequedetail chequeId) {
		this.chequeId = chequeId;
	}

	public BigDecimal getCashAmount() {
		return cashAmount;
	}

	public void setCashAmount(BigDecimal cashAmount) {
		this.cashAmount = cashAmount;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getRevenueSource() {
		return revenueSource;
	}

	public void setRevenueSource(String revenueSource) {
		this.revenueSource = revenueSource;
	}

	public int getIsReversed() {
		return isReversed;
	}

	public void setIsReversed(int isReversed) {
		this.isReversed = isReversed;
	}

	public String getCashier() {
		return cashier;
	}

	public void setCashier(String cashier) {
		this.cashier = cashier;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getManualReceiptNo() {
		return manualReceiptNo;
	}

	public void setManualReceiptNo(String manualReceiptNo) {
		this.manualReceiptNo = manualReceiptNo;
	}

}
