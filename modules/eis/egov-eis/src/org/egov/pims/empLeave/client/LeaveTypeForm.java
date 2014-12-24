package org.egov.pims.empLeave.client;

import org.apache.struts.action.ActionForm;

public class LeaveTypeForm extends ActionForm
{
	
	public String name = "";
	public String accumulate = "";
	public String payElegible = "";
	public String fromDate ="";
	public String toDate ="";
	public String typeId ="";
	public String isHalfDay ="";
	public String isEncashable="";
	public String getAccumulate() {
		return accumulate;
	}
	public void setAccumulate(String accumulate) {
		this.accumulate = accumulate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPayElegible() {
		return payElegible;
	}
	public void setPayElegible(String payElegible) {
		this.payElegible = payElegible;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getIsHalfDay() {
		return isHalfDay;
	}
	public void setIsHalfDay(String isHalfDay) {
		this.isHalfDay = isHalfDay;
	}
	public String getIsEncashable() {
		return isEncashable;
	}
	public void setIsEncashable(String isEncashable) {
		this.isEncashable = isEncashable;
	}
	
	
	
}
