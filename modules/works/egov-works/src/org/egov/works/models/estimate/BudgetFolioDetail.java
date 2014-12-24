package org.egov.works.models.estimate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.egov.infstr.models.Money;

public class BudgetFolioDetail implements Serializable{
	private Integer srlNo;
	private String budgetApprNo;
	private String estimateNo;
	private String nameOfWork;
	private String estimateDate;
	private Double workValue;	
	private Double cumulativeTotal;
	private BigDecimal balanceAvailable;
	
	
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

}
