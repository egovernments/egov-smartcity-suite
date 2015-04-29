package org.egov.works.models.tender;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.egov.infstr.models.BaseModel;

public class Retender extends BaseModel{
	
	private static final long serialVersionUID = 1L;
	private WorksPackage worksPackage;
	private List<RetenderHistory> retenderHistoryDetails = new LinkedList<RetenderHistory>();
	private String reason;
	private Date date;
	private Integer iterationNumber;
	
	public WorksPackage getWorksPackage() {
		return worksPackage;
	}
	public void setWorksPackage(WorksPackage worksPackage) {
		this.worksPackage = worksPackage;
	}
	public String getReason() {
		return reason;
	}
	public Date getDate() {
		return date;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getIterationNumber() {
		return iterationNumber;
	}
	public void setIterationNumber(Integer iterationNumber) {
		this.iterationNumber = iterationNumber;
	}
	public List<RetenderHistory> getRetenderHistoryDetails() {
		return retenderHistoryDetails;
	}
	public void setRetenderHistoryDetails(
			List<RetenderHistory> retenderHistoryDetails) {
		this.retenderHistoryDetails = retenderHistoryDetails;
	}
	
}
