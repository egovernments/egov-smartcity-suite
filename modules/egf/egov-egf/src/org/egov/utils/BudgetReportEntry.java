package org.egov.utils;

import java.math.BigDecimal;

public class BudgetReportEntry {
	String glCode;
	BigDecimal budgetedAmtForYear = BigDecimal.ZERO;
	BigDecimal soFarAppropriated = BigDecimal.ZERO;
	BigDecimal balance = BigDecimal.ZERO;
	BigDecimal cumilativeIncludingCurrentBill = BigDecimal.ZERO;
	BigDecimal currentBalanceAvailable = BigDecimal.ZERO;
	BigDecimal currentBillAmount = BigDecimal.ZERO;
	String fundName;
	String functionName;
	String departmentName;
	String financialYear;
	String AccountCode;
	String budgetApprNumber;
	public String getGlCode() {
		return glCode;
	}
	public void setGlCode(String glCode) {
		this.glCode = glCode;
	}
	public BigDecimal getBudgetedAmtForYear() {
		return budgetedAmtForYear;
	}
	public void setBudgetedAmtForYear(BigDecimal budgetedAmtForYear) {
		this.budgetedAmtForYear = budgetedAmtForYear;
	}
	public BigDecimal getSoFarAppropriated() {
		return soFarAppropriated;
	}
	public void setSoFarAppropriated(BigDecimal soFarAppropriated) {
		this.soFarAppropriated = soFarAppropriated;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getCumilativeIncludingCurrentBill() {
		return cumilativeIncludingCurrentBill;
	}
	public void setCumilativeIncludingCurrentBill(
			BigDecimal cumilativeIncludingCurrentBill) {
		this.cumilativeIncludingCurrentBill = cumilativeIncludingCurrentBill;
	}
	public BigDecimal getCurrentBalanceAvailable() {
		return currentBalanceAvailable;
	}
	public void setCurrentBalanceAvailable(BigDecimal currentBalanceAvailable) {
		this.currentBalanceAvailable = currentBalanceAvailable;
	}
	public BigDecimal getCurrentBillAmount() {
		return currentBillAmount;
	}
	public void setCurrentBillAmount(BigDecimal currentBillAmount) {
		this.currentBillAmount = currentBillAmount;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}
	public String getAccountCode() {
		return AccountCode;
	}
	public void setAccountCode(String accountCode) {
		AccountCode = accountCode;
	}
	public String getBudgetApprNumber() {
		return budgetApprNumber;
	}
	public void setBudgetApprNumber(String budgetApprNumber) {
		this.budgetApprNumber = budgetApprNumber;
	}

}
