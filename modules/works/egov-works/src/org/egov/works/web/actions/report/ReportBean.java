package org.egov.works.web.actions.report;

import java.math.BigDecimal;

/**
 * @author Sathish P
 *
 */
public class ReportBean {
	
	private String contractorNameAndAddress;
	
	private BigDecimal workOrderValue;
	
	private BigDecimal paymentAmount;
	
	private String paymentDate;
	
	private String billDate;
	
	private BigDecimal taxDeductedAmt;
	
	private String remittedDate;
	
	private String estNoAndDate;
	
	private String jurisdiction;
	
	private String budgetHead;
	
	private String nameOfWork;
	
	private BigDecimal estimateAmount;
	
	private String woNoAndDate;
	
	private BigDecimal woAmount;
	
	private BigDecimal totalRecordedMbAmount;
	
	private String contractorName;
	
	private Long woId;
	
	public String getContractorNameAndAddress() {
		return contractorNameAndAddress;
	}

	public void setContractorNameAndAddress(String contractorNameAndAddress) {
		this.contractorNameAndAddress = contractorNameAndAddress;
	}

	public BigDecimal getWorkOrderValue() {
		return workOrderValue;
	}

	public void setWorkOrderValue(BigDecimal workOrderValue) {
		this.workOrderValue = workOrderValue;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public BigDecimal getTaxDeductedAmt() {
		return taxDeductedAmt;
	}

	public void setTaxDeductedAmt(BigDecimal taxDeductedAmt) {
		this.taxDeductedAmt = taxDeductedAmt;
	}

	public String getRemittedDate() {
		return remittedDate;
	}

	public void setRemittedDate(String remittedDate) {
		this.remittedDate = remittedDate;
	}

	public String getEstNoAndDate() {
		return estNoAndDate;
	}

	public void setEstNoAndDate(String estNoAndDate) {
		this.estNoAndDate = estNoAndDate;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getBudgetHead() {
		return budgetHead;
	}

	public void setBudgetHead(String budgetHead) {
		this.budgetHead = budgetHead;
	}

	public String getNameOfWork() {
		return nameOfWork;
	}

	public void setNameOfWork(String nameOfWork) {
		this.nameOfWork = nameOfWork;
	}

	public BigDecimal getEstimateAmount() {
		return estimateAmount;
	}

	public void setEstimateAmount(BigDecimal estimateAmount) {
		this.estimateAmount = estimateAmount;
	}

	public String getWoNoAndDate() {
		return woNoAndDate;
	}

	public void setWoNoAndDate(String woNoAndDate) {
		this.woNoAndDate = woNoAndDate;
	}

	public BigDecimal getWoAmount() {
		return woAmount;
	}

	public void setWoAmount(BigDecimal woAmount) {
		this.woAmount = woAmount;
	}

	public String getContractorName() {
		return contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	public BigDecimal getTotalRecordedMbAmount() {
		return totalRecordedMbAmount;
	}

	public void setTotalRecordedMbAmount(BigDecimal totalRecordedMbAmount) {
		this.totalRecordedMbAmount = totalRecordedMbAmount;
	}

	public Long getWoId() {
		return woId;
	}

	public void setWoId(Long woId) {
		this.woId = woId;
	}

}
