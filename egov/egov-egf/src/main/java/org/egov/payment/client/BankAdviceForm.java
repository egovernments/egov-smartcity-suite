package org.egov.payment.client;

import java.math.BigDecimal;

public class BankAdviceForm 
{
	private String contractorCode;
	private String contractorName;
	private String accountType;
	private String bankName;
	private String bankAccountNo;
	private String ifscCode;
	private BigDecimal netAmount;
	public String getContractorCode() {
		return contractorCode;
	}
	public void setContractorCode(final String contractorCode) {
		this.contractorCode = contractorCode;
	}
	public String getContractorName() {
		return contractorName;
	}
	public void setContractorName(final  String contractorName) {
		this.contractorName = contractorName;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(final  String accountType) {
		this.accountType = accountType;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(final  String bankName) {
		this.bankName = bankName;
	}
	public String getBankAccountNo() {
		return bankAccountNo;
	}
	public void setBankAccountNo(final  String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}
	public String getIfscCode() {
		return ifscCode;
	}
	public void setIfscCode(final  String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public BigDecimal getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(final  BigDecimal netAmount) {
		this.netAmount = netAmount;
	}
}
