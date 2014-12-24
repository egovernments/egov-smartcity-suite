package org.egov.works.web.actions.report;

import java.math.BigDecimal;

public class WOReportBean {
	
	private String estNumAndDate;
	
	private String nameOfWork;
	
	private String jurisdiction;
	
	private String budgetHead;
	
	private BigDecimal estAmt;
	
	private String WONumAndDate;
	
	private String status;
	
	private BigDecimal woAmt;
	
	private String days;
	
	private Long woId;
	
	private String contractorName;
	
	private String projectCode;
	
	private String billNumAndDate;
	
	private String typeOfBill;
	
	private BigDecimal billAmount;

	public String getEstNumAndDate() {
		return estNumAndDate;
	}

	public void setEstNumAndDate(String estNumAndDate) {
		this.estNumAndDate = estNumAndDate;
	}

	public String getNameOfWork() {
		return nameOfWork;
	}

	public void setNameOfWork(String nameOfWork) {
		this.nameOfWork = nameOfWork;
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

	public BigDecimal getEstAmt() {
		return estAmt;
	}

	public void setEstAmt(BigDecimal estAmt) {
		this.estAmt = estAmt;
	}

	public String getWONumAndDate() {
		return WONumAndDate;
	}

	public void setWONumAndDate(String wONumAndDate) {
		WONumAndDate = wONumAndDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getWoAmt() {
		return woAmt;
	}

	public void setWoAmt(BigDecimal woAmt) {
		this.woAmt = woAmt;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getContractorName() {
		return contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	public Long getWoId() {
		return woId;
	}

	public void setWoId(Long woId) {
		this.woId = woId;
	}
	
	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	
	public String getBillNumAndDate() {
		return billNumAndDate;
	}

	public void setBillNumAndDate(String billNumAndDate) {
		this.billNumAndDate = billNumAndDate;
	}

	public String getTypeOfBill() {
		return typeOfBill;
	}

	public void setTypeOfBill(String typeOfBill) {
		this.typeOfBill = typeOfBill;
	}
	
	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}
}
