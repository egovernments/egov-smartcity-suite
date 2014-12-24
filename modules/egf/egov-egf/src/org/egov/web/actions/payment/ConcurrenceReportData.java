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
	String bpvAccountCode;
	BigDecimal amount;
	
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
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getBillDate() {
		return Constants.DDMMYYYYFORMAT2.format(billDate);
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
	public String getBpvDate() {
		return Constants.DDMMYYYYFORMAT2.format(bpvDate);
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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
