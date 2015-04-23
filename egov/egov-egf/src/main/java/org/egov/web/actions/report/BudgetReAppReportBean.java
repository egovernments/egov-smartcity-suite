package org.egov.web.actions.report;


import java.math.BigDecimal;
import java.util.Date;

import org.egov.utils.Constants;

/*
 * Bean used for the BudgetAppropriation Report 
 */

public class BudgetReAppReportBean {
	private Integer slNo;
	private String department;    
	private String fund;
	private String function;
	private String budgetHead;
	private String budgetAppropriationNo;
	private String appDate;
	private Date appropriationDate;
	private BigDecimal additionAmount = new BigDecimal("0.0");
	private BigDecimal deductionAmount = new BigDecimal("0.0");
	private BigDecimal actualAmount = new BigDecimal("0.0");
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getFund() {
		return fund;
	}
	public void setFund(String fund) {
		this.fund = fund;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getBudgetHead() {
		return budgetHead;
	}
	public void setBudgetHead(String budgetHead) {
		this.budgetHead = budgetHead;
	}
	public String getBudgetAppropriationNo() {
		return budgetAppropriationNo;
	}
	public void setBudgetAppropriationNo(String budgetAppropriationNo) {
		this.budgetAppropriationNo = budgetAppropriationNo;
	}
	public Date getAppropriationDate() {
		return appropriationDate;
	}
	public void setAppropriationDate(Date appropriationDate) {
		this.appropriationDate = appropriationDate;
	}
	public BigDecimal getAdditionAmount() {
		return additionAmount;
	}
	public void setAdditionAmount(BigDecimal additionAmount) {
		this.additionAmount = additionAmount;
	}
	public BigDecimal getDeductionAmount() {
		return deductionAmount;
	}
	public void setDeductionAmount(BigDecimal deductionAmount) {
		this.deductionAmount = deductionAmount;
	}
	public BigDecimal getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}
	public Integer getSlNo() {
		return slNo;
	}
	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}
	public String getAppDate() {
		return appDate;
	}
	public void setAppDate(String appDate) {
		this.appDate = appDate;
	}    
}
