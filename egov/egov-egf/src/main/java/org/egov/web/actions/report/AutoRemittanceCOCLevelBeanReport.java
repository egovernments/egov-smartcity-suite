package org.egov.web.actions.report;

import java.math.BigDecimal;



public class AutoRemittanceCOCLevelBeanReport{


	private String departmentCode;
	private BigDecimal incomeTaxRemittedAmt;
	private BigDecimal salesTaxRemittedAmt;
	private BigDecimal mwgwfRemittedAmt;
	private BigDecimal serviceTaxRemittedAmt;
	private BigDecimal grandTotal;
	private BigDecimal departmentTotal;
	
	public BigDecimal getIncomeTaxRemittedAmt() {
		return incomeTaxRemittedAmt;
	}
	public void setIncomeTaxRemittedAmt(BigDecimal incomeTaxRemittedAmt) {
		this.incomeTaxRemittedAmt = incomeTaxRemittedAmt;
	}
	public BigDecimal getSalesTaxRemittedAmt() {
		return salesTaxRemittedAmt;
	}
	public void setSalesTaxRemittedAmt(BigDecimal salesTaxRemittedAmt) {
		this.salesTaxRemittedAmt = salesTaxRemittedAmt;
	}
	public BigDecimal getMwgwfRemittedAmt() {
		return mwgwfRemittedAmt;
	}
	public void setMwgwfRemittedAmt(BigDecimal mwgwfRemittedAmt) {
		this.mwgwfRemittedAmt = mwgwfRemittedAmt;
	}

	public BigDecimal getGrandTotal() {
		return grandTotal;
	}
	public void setGrandTotal(BigDecimal grandTotal) {
		this.grandTotal = grandTotal;
	}
	public BigDecimal getDepartmentTotal() {
		return departmentTotal;
	}
	public void setDepartmentTotal(BigDecimal departmentTotal) {
		this.departmentTotal = departmentTotal;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public BigDecimal getServiceTaxRemittedAmt() {
		return serviceTaxRemittedAmt;
	}
	public void setServiceTaxRemittedAmt(BigDecimal serviceTaxRemittedAmt) {
		this.serviceTaxRemittedAmt = serviceTaxRemittedAmt;
	}

}
