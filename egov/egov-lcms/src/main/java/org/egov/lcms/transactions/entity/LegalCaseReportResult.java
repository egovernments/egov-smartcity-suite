package org.egov.lcms.transactions.entity;

import java.util.Date;

public class LegalCaseReportResult {
	private String caseNumber;
	private String lcNumber;
	private Date caseFromDate;
	private Date caseToDate;
	private String standingCouncil;
	private String casecategory;
	private String courttype;
	private String petitionType;
	private String courtType;
	private String courtName;
	private String govtDept;
	private String petName;
	private String caseStatus;

	public String getStandingCouncil() {
		return standingCouncil;
	}

	public void setStandingCouncil(String standingCouncil) {
		this.standingCouncil = standingCouncil;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getLcNumber() {
		return lcNumber;
	}

	public void setLcNumber(String lcNumber) {
		this.lcNumber = lcNumber;
	}

	public Date getCaseFromDate() {
		return caseFromDate;
	}

	public void setCaseFromDate(Date caseFromDate) {
		this.caseFromDate = caseFromDate;
	}

	public Date getCaseToDate() {
		return caseToDate;
	}

	public void setCaseToDate(Date caseToDate) {
		this.caseToDate = caseToDate;
	}

	public String getCasecategory() {
		return casecategory;
	}

	public void setCasecategory(String casecategory) {
		this.casecategory = casecategory;
	}

	public String getCourttype() {
		return courttype;
	}

	public void setCourttype(String courttype) {
		this.courttype = courttype;
	}

	public String getPetitionType() {
		return petitionType;
	}

	public void setPetitionType(String petitionType) {
		this.petitionType = petitionType;
	}

	public String getCourtType() {
		return courtType;
	}

	public void setCourtType(String courtType) {
		this.courtType = courtType;
	}

	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	public String getGovtDept() {
		return govtDept;
	}

	public void setGovtDept(String govtDept) {
		this.govtDept = govtDept;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

}
