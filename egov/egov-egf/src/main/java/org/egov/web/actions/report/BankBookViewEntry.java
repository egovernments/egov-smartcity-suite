package org.egov.web.actions.report;

import java.math.BigDecimal;
import java.util.List;

import org.egov.model.instrument.InstrumentVoucher;

public class BankBookViewEntry {
	String receiptVoucherDate;
	String receiptVoucherNumber;
	String receiptParticulars;
	BigDecimal receiptAmount;
	String receiptChequeDetail;
	String paymentVoucherDate;
	String paymentVoucherNumber;
	String paymentParticulars;
	BigDecimal paymentAmount;
	String paymentChequeDetail;
	String instrumentStatus;
	String glCode;
	private Long voucherId;
	//string of cheque number and dates 
	private String chequeNumber;
	

	private List<InstrumentVoucher>	instrumentVouchers;
	
  
	public List<InstrumentVoucher> getInstrumentVouchers() {
		return instrumentVouchers;
	}

	public void setInstrumentVouchers(List<InstrumentVoucher> instrumentVouchers) {
		this.instrumentVouchers = instrumentVouchers;
	}

	public BankBookViewEntry(){};
	
	public BankBookViewEntry(String voucherNumber,String voucherDate, String particulars,BigDecimal amount, String chequeDetail,String type) {
		super();
		if("Payment".equalsIgnoreCase(type)){
			this.paymentVoucherDate = voucherDate;
			this.paymentVoucherNumber = voucherNumber;
			this.paymentParticulars = particulars;
			this.paymentAmount = amount;
			this.paymentChequeDetail = chequeDetail;
		}else{
			this.receiptVoucherDate = voucherDate;
			this.receiptVoucherNumber = voucherNumber;
			this.receiptParticulars = particulars;
			this.receiptAmount = amount;
			this.receiptChequeDetail = chequeDetail;
		}
	}
	
	public BankBookViewEntry(String voucherNumber,String voucherDate, String particulars,BigDecimal amount, String chequeDetail,String type,String chequeNumber) {
		super();
		if("Payment".equalsIgnoreCase(type)){
			this.paymentVoucherDate = voucherDate;
			this.paymentVoucherNumber = voucherNumber;
			this.paymentParticulars = particulars;
			this.paymentAmount = amount;
			this.paymentChequeDetail = chequeDetail;
			this.instrumentVouchers=instrumentVouchers;
			this.chequeNumber=chequeNumber;
		}else{
			this.receiptVoucherDate = voucherDate;
			this.receiptVoucherNumber = voucherNumber;
			this.receiptParticulars = particulars;
			this.receiptAmount = amount;
			this.receiptChequeDetail = chequeDetail;
			this.instrumentVouchers=instrumentVouchers;
			this.chequeNumber=chequeNumber;
		}
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	
	public String getReceiptVoucherDate() {
		return receiptVoucherDate;
	}
	public void setReceiptVoucherDate(String receiptVoucherDate) {
		this.receiptVoucherDate = receiptVoucherDate;
	}
	public String getReceiptVoucherNumber() {
		return receiptVoucherNumber;
	}
	public void setReceiptVoucherNumber(String receiptVoucherNumber) {
		this.receiptVoucherNumber = receiptVoucherNumber;
	}
	public String getReceiptParticulars() {
		return receiptParticulars;
	}
	public void setReceiptParticulars(String receiptParticulars) {
		this.receiptParticulars = receiptParticulars;
	}
	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}
	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}
	public String getReceiptChequeDetail() {
		return receiptChequeDetail;
	}
	public void setReceiptChequeDetail(String receiptChequeDetail) {
		this.receiptChequeDetail = receiptChequeDetail;
	}
	public String getPaymentVoucherDate() {
		return paymentVoucherDate;
	}
	public void setPaymentVoucherDate(String paymentVoucherDate) {
		this.paymentVoucherDate = paymentVoucherDate;
	}
	public String getPaymentVoucherNumber() {
		return paymentVoucherNumber;
	}
	public void setPaymentVoucherNumber(String paymentVoucherNumber) {
		this.paymentVoucherNumber = paymentVoucherNumber;
	}
	public String getPaymentParticulars() {
		return paymentParticulars;
	}
	public void setPaymentParticulars(String paymentParticulars) {
		this.paymentParticulars = paymentParticulars;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getPaymentChequeDetail() {
		return paymentChequeDetail;
	}
	public void setPaymentChequeDetail(String paymentChequeDetail) {
		this.paymentChequeDetail = paymentChequeDetail;
	}
	public String getInstrumentStatus() {
		return instrumentStatus;
	}
	public void setInstrumentStatus(String instrumentStatus) {
		this.instrumentStatus = instrumentStatus;
	}
	public String getGlCode() {
		return glCode;
	}
	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}

	public void setVoucherId(Long voucherId) {
		this.voucherId = voucherId;
	}

	public Long getVoucherId() {
		return voucherId;
	}
}
