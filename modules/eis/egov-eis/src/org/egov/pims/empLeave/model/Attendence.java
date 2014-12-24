package org.egov.pims.empLeave.model;

import java.util.Date;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.StateAware;
import org.egov.pims.model.PersonalInformation;

public class Attendence extends BaseModel implements java.io.Serializable
{
	
	public AttendenceType attendenceType ;
	public Date attDate;
	private PersonalInformation employee;
	public org.egov.commons.CFinancialYear financialId;
	private Integer month;
	
	
	public Date getAttDate() {
		return attDate;
	}
	public void setAttDate(Date attDate) {
		this.attDate = attDate;
	}
	public PersonalInformation getEmployee() {
		return employee;
	}
	public void setEmployee(PersonalInformation employee) {
		this.employee = employee;
	}
	public AttendenceType getAttendenceType() {
		return attendenceType;
	}
	public void setAttendenceType(AttendenceType attendenceType) {
		this.attendenceType = attendenceType;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public org.egov.commons.CFinancialYear getFinancialId() {
		return financialId;
	}
	public void setFinancialId(org.egov.commons.CFinancialYear financialId) {
		this.financialId = financialId;
	}
	


}
