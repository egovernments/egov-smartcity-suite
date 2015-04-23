package org.egov.web.actions.report;

import java.math.BigDecimal;
import java.util.Date;

public class BankAdviceReportInfo {
	
	private String partyName;
	private String accountNumber;
	private String ifscCode;
	private String micrCode;
	private String bank;
	private String bankBranch;
	private BigDecimal amount;
	private String rtgsNumber;
	private Date rtgsDate;
	private String department;
	private BigDecimal paymentAmount;
	private String status;
	private BigDecimal vhId;
	private BigDecimal ihId;
	private String paymentNumber;
	private String paymentDate;
	private BigDecimal dtId;
	private BigDecimal dkId;
	
	
	public BigDecimal getDtId() {
		return dtId;
	}
	public void setDtId(BigDecimal dtId) {
		this.dtId = dtId;
	}
	public BigDecimal getDkId() {
		return dkId;
	}
	public void setDkId(BigDecimal dkId) {
		this.dkId = dkId;
	}
	public String getPartyName() {
		return partyName;
	}
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public BigDecimal getIhId() {
		return ihId;
	}
	public void setIhId(BigDecimal ihId) {
		this.ihId = ihId;
	}
	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getMicrCode() {
		return micrCode;
	}
	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getBank() {
		return bank;
	}
	public String getBankBranch() {
		return bankBranch;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	public String getRtgsNumber() {
		return rtgsNumber;
	}
	public Date getRtgsDate() {
		return rtgsDate;
	}
	public String getDepartment() {
		return department;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setRtgsNumber(String rtgsNumber) {
		this.rtgsNumber = rtgsNumber;
	}
	public void setRtgsDate(Date rtgsDate) {
		this.rtgsDate = rtgsDate;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPaymentNumber() {
		return paymentNumber;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public BigDecimal getVhId() {
		return vhId;
	}
	public void setVhId(BigDecimal vhId) {
		this.vhId = vhId;
	}

}
