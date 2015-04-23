/*
 * DishonoredViewEntries.java  Created on May 31, 2008
 *
 *  Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions.brs;

/**
 * @author Iliyaraja S
 *
 * @Version 1.00
 */
public class DishonoredViewEntries
{
	//For voucher header details
	private String vouHName;
	private String voucherNumber;
	private String vouDate;
	private String fund;
	private String refNo;// chqno
	private String refDate;//chqdate
	private String reason;

	//For Account code details
	private String reversalAccCode;
	private String reversalDescn;
	private String reversalDebitAmount;
	private String reversalCreditAmount;


	public String getFund() {
		return fund;
	}
	public void setFund(String fund) {
		this.fund = fund;
	}

	public String getRefDate() {
		return refDate;
	}
	public void setRefDate(String refDate) {
		this.refDate = refDate;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public String getVouDate() {
		return vouDate;
	}
	public void setVouDate(String vouDate) {
		this.vouDate = vouDate;
	}
	public String getReversalAccCode() {
		return reversalAccCode;
	}
	public void setReversalAccCode(String reversalAccCode) {
		this.reversalAccCode = reversalAccCode;
	}
	public String getReversalCreditAmount() {
		return reversalCreditAmount;
	}
	public void setReversalCreditAmount(String reversalCreditAmount) {
		this.reversalCreditAmount = reversalCreditAmount;
	}
	public String getReversalDebitAmount() {
		return reversalDebitAmount;
	}
	public void setReversalDebitAmount(String reversalDebitAmount) {
		this.reversalDebitAmount = reversalDebitAmount;
	}
	public String getReversalDescn() {
		return reversalDescn;
	}
	public void setReversalDescn(String reversalDescn) {
		this.reversalDescn = reversalDescn;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getVouHName() {
		return vouHName;
	}
	public void setVouHName(String vouHName) {
		this.vouHName = vouHName;
	}


}
