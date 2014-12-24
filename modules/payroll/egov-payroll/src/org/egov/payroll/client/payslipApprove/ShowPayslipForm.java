package org.egov.payroll.client.payslipApprove;

import org.apache.struts.action.ActionForm;

public class ShowPayslipForm extends ActionForm {
	
	String year;
	String month;
	String deptId;
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	
}
