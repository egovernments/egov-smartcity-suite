package org.egov.pims.empLeave.client;

import org.apache.struts.action.ActionForm;

public class AttendenceForm  extends ActionForm
{
	public String  month;
	public String[]  employeeId;
	public String[] getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String[] employeeId) {
		this.employeeId = employeeId;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	
}
