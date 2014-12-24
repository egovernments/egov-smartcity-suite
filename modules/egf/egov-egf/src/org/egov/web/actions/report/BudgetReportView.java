package org.egov.web.actions.report;

import java.math.BigDecimal;

public class BudgetReportView {
	private Long id;
	String departmentCode = "";
    String functionCode = "";
    String budgetGroupName = "";
    private BigDecimal actualsLastYear = BigDecimal.ZERO;
    private BigDecimal beCurrentYearApproved = BigDecimal.ZERO;
    private BigDecimal reCurrentYearApproved = BigDecimal.ZERO;
    private BigDecimal beNextYearApproved = BigDecimal.ZERO;
    private BigDecimal reCurrentYearOriginal = BigDecimal.ZERO;
    private BigDecimal beNextYearOriginal = BigDecimal.ZERO;
    
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getBudgetGroupName() {
		return budgetGroupName;
	}
	public void setBudgetGroupName(String budgetGroupName) {
		this.budgetGroupName = budgetGroupName;
	}
	public BigDecimal getActualsLastYear() {
		return actualsLastYear;
	}
	public void setActualsLastYear(BigDecimal actuals) {
		this.actualsLastYear = actuals;
	}
	public BigDecimal getBeCurrentYearApproved() {
		return beCurrentYearApproved;
	}
	public void setBeCurrentYearApproved(BigDecimal beCurrentYear) {
		this.beCurrentYearApproved = beCurrentYear;
	}
	public BigDecimal getReCurrentYearApproved() {
		return reCurrentYearApproved;
	}
	public void setReCurrentYearApproved(BigDecimal reCurrentYear) {
		this.reCurrentYearApproved = reCurrentYear;
	}
	public BigDecimal getBeNextYearApproved() {
		return beNextYearApproved;
	}
	public void setBeNextYearApproved(BigDecimal beNextYear) {
		this.beNextYearApproved = beNextYear;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setReCurrentYearOriginal(BigDecimal reCurrentYearOriginal) {
		this.reCurrentYearOriginal = reCurrentYearOriginal;
	}
	public BigDecimal getReCurrentYearOriginal() {
		return reCurrentYearOriginal;
	}
	public void setBeNextYearOriginal(BigDecimal beNextYearOriginal) {
		this.beNextYearOriginal = beNextYearOriginal;
	}
	public BigDecimal getBeNextYearOriginal() {
		return beNextYearOriginal;
	}
}
