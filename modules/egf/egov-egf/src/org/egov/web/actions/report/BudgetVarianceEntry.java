package org.egov.web.actions.report;

import java.math.BigDecimal;

public class BudgetVarianceEntry {
	String departmentCode;
	String departmentName;
	String functionCode;
	String fundCode;
	String budgetCode;
	String budgetHead;
	BigDecimal estimate;
	BigDecimal additionalAppropriation;
	BigDecimal actual;
	BigDecimal variance;
	private Long detailId;
	
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
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
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	public String getBudgetCode() {
		return budgetCode;
	}
	public void setBudgetCode(String budgetCode) {
		this.budgetCode = budgetCode;
	}
	public String getBudgetHead() {
		return budgetHead;
	}
	public void setBudgetHead(String budgetHead) {
		this.budgetHead = budgetHead;
	}
	public BigDecimal getEstimate() {
		return estimate;
	}
	public void setEstimate(BigDecimal budgetEstimate) {
		this.estimate = budgetEstimate;
	}
	public BigDecimal getAdditionalAppropriation() {
		return additionalAppropriation;
	}
	public void setAdditionalAppropriation(BigDecimal additionalAppropriation) {
		this.additionalAppropriation = additionalAppropriation;
	}
	public BigDecimal getActual() {
		return actual;
	}
	public void setActual(BigDecimal actual) {
		this.actual = actual;
	}
	public BigDecimal getVariance() {
		return variance;
	}
	public void setVariance(BigDecimal variance) {
		this.variance = variance;
	}
	public void setDetailId(Long id) {
		this.detailId = id;
	}
	public Long getDetailId() {
		return detailId;
	}
}

