package org.egov.works.models.measurementbook;

public class MeasurementBookPDF {
	private String scheduleNo;
	private String workDescription;
	private Double completedMeasurement;
	private Double unitRate;
	private String uom;
	private Double completedCost;
	private String pageNo;
	private Double prevMeasurement;
	private Double currentMeasurement;
	private Double currentCost;
	private String revisionType;
	
	public String getScheduleNo() {
		return scheduleNo;
	}
	public String getWorkDescription() {
		return workDescription;
	}
	public Double getCompletedMeasurement() {
		return completedMeasurement;
	}
	public Double getUnitRate() {
		return unitRate;
	}
	public String getUom() {
		return uom;
	}
	public Double getCompletedCost() {
		return completedCost;
	}
	public String getPageNo() {
		return pageNo;
	}
	public Double getCurrentMeasurement() {
		return currentMeasurement;
	}
	public Double getCurrentCost() {
		return currentCost;
	}
	public void setScheduleNo(String scheduleNo) {
		this.scheduleNo = scheduleNo;
	}
	public void setWorkDescription(String workDescription) {
		this.workDescription = workDescription;
	}
	public void setCompletedMeasurement(Double completedMeasurement) {
		this.completedMeasurement = completedMeasurement;
	}
	public void setUnitRate(Double unitRate) {
		this.unitRate = unitRate;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public void setCompletedCost(Double completedCost) {
		this.completedCost = completedCost;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public void setCurrentMeasurement(Double currentMeasurement) {
		this.currentMeasurement = currentMeasurement;
	}
	public void setCurrentCost(Double currentCost) {
		this.currentCost = currentCost;
	}
	public String getRevisionType() {
		return revisionType;
	}
	public void setRevisionType(String revisionType) {
		this.revisionType = revisionType;
	}
	public Double getPrevMeasurement() {
		return prevMeasurement;
	}
	public void setPrevMeasurement(Double prevMeasurement) {
		this.prevMeasurement = prevMeasurement;
	}

}
