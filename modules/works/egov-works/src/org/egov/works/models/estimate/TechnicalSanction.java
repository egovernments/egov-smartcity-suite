package org.egov.works.models.estimate;

import java.util.Date;

import org.egov.infstr.models.BaseModel;


public class TechnicalSanction extends BaseModel{
	
	private AbstractEstimate abstractEstimate;
	private String  techSanctionNumber;
	private Date techSanctionDate; 
	
	public AbstractEstimate getAbstractEstimate() { 
		return abstractEstimate;
	}
	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}
	public String getTechSanctionNumber() {
		return techSanctionNumber;
	}
	public void setTechSanctionNumber(String techSanctionNumber) {
		this.techSanctionNumber = techSanctionNumber;
	}
	public Date getTechSanctionDate() {
		return techSanctionDate;
	}
	public void setTechSanctionDate(Date techSanctionDate) {
		this.techSanctionDate = techSanctionDate;
	}
}
