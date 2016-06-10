package org.egov.lcms.transactions.entity;

import java.util.Date;

import org.egov.infstr.models.BaseModel;

public class PaperBook extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	private Legalcase legalcase;
	private Date lastDateToDepositAmt;
	private Double depositedAmount;
	private String concernedOfficerName;
	private String remarks;
	private boolean isPaperBookRequired;

	public Legalcase getLegalcase() {
		return legalcase;
	}

	public void setLegalcase(Legalcase legalcase) {
		this.legalcase = legalcase;
	}

	public Date getLastDateToDepositAmt() {
		return lastDateToDepositAmt;
	}

	public void setLastDateToDepositAmt(Date lastDateToDepositAmt) {
		this.lastDateToDepositAmt = lastDateToDepositAmt;
	}

	public Double getDepositedAmount() {
		return depositedAmount;
	}

	public void setDepositedAmount(Double depositedAmount) {
		this.depositedAmount = depositedAmount;
	}

	public String getConcernedOfficerName() {
		return concernedOfficerName;
	}

	public void setConcernedOfficerName(String concernedOfficerName) {
		this.concernedOfficerName = concernedOfficerName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public boolean getIsPaperBookRequired() {
		return isPaperBookRequired;
	}

	public void setIsPaperBookRequired(boolean isPaperBookRequired) {
		this.isPaperBookRequired = isPaperBookRequired;
	}
	
}
