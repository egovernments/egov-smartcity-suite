package org.egov.web.actions.report;

import java.math.BigDecimal;
import java.util.Date;

public class TDSEntry {
	String natureOfDeduction = "";
	private String remittedOn;
	String voucherNumber = "";
	String voucherDate;
	String partyName = "";
	String partyCode = "";
	private String panNo = "";
	private String paymentVoucherNumber = "";
	private String chequeNumber = "";
	private String drawnOn;
	BigDecimal chequeAmount;
	private BigDecimal amount;
	private String month;
	private BigDecimal totalDeduction = BigDecimal.ZERO;
	private BigDecimal totalRemitted = BigDecimal.ZERO;
	
	public String getNatureOfDeduction() {
		return natureOfDeduction;
	}
	public void setNatureOfDeduction(String departmentCode) {
		this.natureOfDeduction = departmentCode;
	}
	public String getRemittedOn() {
		return remittedOn;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String functionCode) {
		this.voucherNumber = functionCode;
	}
	public void setVoucherDate(String voucherDate) {
		this.voucherDate = voucherDate;
	}
	public String getVoucherDate() {
		return voucherDate;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName == null?"":partyName;
	}
	public String getPartyCode() {
		return partyCode;
	}
	public void setPartyCode(String partyCode) {
		this.partyCode = partyCode == null?"":partyCode;
	}
	public BigDecimal getChequeAmount() {
		return chequeAmount;
	}
	public void setChequeAmount(BigDecimal budgetEstimate) {
		this.chequeAmount = budgetEstimate;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	public String getChequeNumber() {
		return chequeNumber;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setRemittedOn(String remittedOn) {
		this.remittedOn = remittedOn;
	}
	public void setPaymentVoucherNumber(String paymentVoucherNumber) {
		this.paymentVoucherNumber = paymentVoucherNumber;
	}
	public String getPaymentVoucherNumber() {
		return paymentVoucherNumber;
	}
	public void setDrawnOn(String drawnOn) {
		this.drawnOn = drawnOn;
	}
	public String getDrawnOn() {
		return drawnOn;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getMonth() {
		return month;
	}
	public void setTotalDeduction(BigDecimal totalDeduction) {
		this.totalDeduction = totalDeduction;
	}
	public BigDecimal getTotalDeduction() {
		return totalDeduction;
	}
	public void setTotalRemitted(BigDecimal totalRemitted) {
		this.totalRemitted = totalRemitted;
	}
	public BigDecimal getTotalRemitted() {
		return totalRemitted;
	}
}

