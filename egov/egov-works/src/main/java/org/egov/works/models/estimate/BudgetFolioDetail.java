package org.egov.works.models.estimate;

import java.io.Serializable;
import java.math.BigDecimal;

public class BudgetFolioDetail implements Serializable{
	private Integer srlNo;
	private String budgetApprNo;
	private String estimateNo;
	private String nameOfWork;
	private String estimateDate;
	private Double workValue;
	private Double appropriatedValue;
	private String appDate;
	private String appType;
	private Double cumulativeTotal;
	private BigDecimal balanceAvailable;
	private Double expensesIncurred;
	private Double cumulativeExpensesIncurred;
	private Double actualBalanceAvailable;
	
	public String getBudgetApprNo() {
		return budgetApprNo;
	}
	public void setBudgetApprNo(String budgetApprNo) {
		this.budgetApprNo = budgetApprNo;
	}
	public String getEstimateNo() {
		return estimateNo;
	}
	public void setEstimateNo(String estimateNo) {
		this.estimateNo = estimateNo;
	}
	public String getNameOfWork() {
		return nameOfWork;
	}
	public void setNameOfWork(String nameOfWork) {
		this.nameOfWork = nameOfWork;
	}
	public String getEstimateDate() {
		return estimateDate;
	}
	public void setEstimateDate(String estimateDate) {
		this.estimateDate = estimateDate;
	}
	public Double getWorkValue() {
		return workValue;
	}
	public void setWorkValue(Double workValue) {
		this.workValue = workValue;
	}
	public Double getAppropriatedValue() {
		return appropriatedValue;
	}
	public void setAppropriatedValue(Double appropriatedValue) {
		this.appropriatedValue = appropriatedValue;
	}
	public Double getCumulativeTotal() {
		return cumulativeTotal;
	}
	public BigDecimal getBalanceAvailable() {
		return balanceAvailable;
	}
	public void setCumulativeTotal(Double cumulativeTotal) {
		this.cumulativeTotal = cumulativeTotal;
	}
	public void setBalanceAvailable(BigDecimal balanceAvailable) {
		this.balanceAvailable = balanceAvailable;
	}
	public Integer getSrlNo() {
		return srlNo;
	}
	public void setSrlNo(Integer srlNo) {
		this.srlNo = srlNo;
	}
	public String getAppDate() {
		return appDate;
	}
	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public Double getExpensesIncurred() {
		return expensesIncurred;
	}
	public void setExpensesIncurred(Double expensesIncurred) {
		this.expensesIncurred = expensesIncurred;
	}
	public Double getCumulativeExpensesIncurred() {
		return cumulativeExpensesIncurred;
	}
	public void setCumulativeExpensesIncurred(Double cumulativeExpensesIncurred) {
		this.cumulativeExpensesIncurred = cumulativeExpensesIncurred;
	}
	public Double getActualBalanceAvailable() {
		return actualBalanceAvailable;
	}
	public void setActualBalanceAvailable(Double actualBalanceAvailable) {
		this.actualBalanceAvailable = actualBalanceAvailable;
	}
}
