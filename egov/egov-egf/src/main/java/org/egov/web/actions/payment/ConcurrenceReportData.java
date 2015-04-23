package org.egov.web.actions.payment;

import java.math.BigDecimal;
import java.sql.Date;

import org.egov.utils.Constants;

public class ConcurrenceReportData {
	String departmentName;
	String functionCode;
	String billNumber;
	Date billDate;
	String bpvNumber;
	Date bpvDate;
	String uac;
	String bankName;
	String bankAccountNumber;
	String bpvAccountCode;
	BigDecimal fundId;
	BigDecimal amount;
	
	public ConcurrenceReportData(){};
	
	public ConcurrenceReportData( String bpvAccountCode, BigDecimal amount,String bpvNumber) {
		super();
		this.amount = amount;
		this.bpvAccountCode = bpvAccountCode;
		this.bpvNumber=bpvNumber;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	public String getUac() {
		return uac;
	}
	public void setUac(String uac) {
		this.uac = uac;
	}
	public BigDecimal getFundId() {
		return fundId;
	}
	public void setFundId(BigDecimal fundId) {
		this.fundId = fundId;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public String getBpvNumber() {
		return bpvNumber;
	}
	public void setBpvNumber(String bpvNumber) {
		this.bpvNumber = bpvNumber;
	}
	public Date getBpvDate() {
		return bpvDate;
	}
	public void setBpvDate(Date bpvDate) {
		this.bpvDate = bpvDate;
	}
	public String getBpvAccountCode() {
		return bpvAccountCode;
	}
	public void setBpvAccountCode(String bpvAccountCode) {
		this.bpvAccountCode = bpvAccountCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
