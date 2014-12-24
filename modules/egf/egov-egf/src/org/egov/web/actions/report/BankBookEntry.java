package org.egov.web.actions.report;

import java.math.BigDecimal;
import java.util.Date;


public class BankBookEntry {
	String voucherNumber;
	Date voucherDate;
	String particulars;
	BigDecimal amount;
	private BigDecimal creditAmount;
	private BigDecimal debitAmount;
	String chequeNumber;
	String chequeDate;
	String type;
	private String chequeDetail;
	private String glCode;
	private BigDecimal receiptAmount;
	private BigDecimal paymentAmount;
	private String instrumentStatus;
	private BigDecimal voucherId;

	public BankBookEntry(){};
	
	public BankBookEntry(String particulars, BigDecimal amount, String type,
			BigDecimal receiptAmount, BigDecimal paymentAmount) {
		super();
		this.particulars = particulars;
		this.amount = amount;
		this.type = type;
		this.receiptAmount = receiptAmount;
		this.paymentAmount = paymentAmount;
	}

	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public Date getVoucherDate() {
		return voucherDate;
	}
	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}
	public String getParticulars() {
		return particulars;
	}
	public void setParticulars(String particulars) {
		this.particulars = particulars;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getChequeNumber() {
		return chequeNumber;
	}
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setChequeDetail(String chequeDetail) {
		this.chequeDetail = chequeDetail;
	}
	public String getChequeDetail() {
		return chequeDetail;
	}
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}
	public BigDecimal getDebitAmount() {
		return debitAmount;
	}
	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}
	public String getGlCode() {
		return glCode;
	}
	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setInstrumentStatus(String instrumentStatus) {
		this.instrumentStatus = instrumentStatus;
	}

	public String getInstrumentStatus() {
		return instrumentStatus;
	}

	public void setVoucherId(BigDecimal voucherId) {
		this.voucherId = voucherId;
	}

	public BigDecimal getVoucherId() {
		return voucherId;
	}
}
