package org.egov.pims.empLeave.client;


import java.util.ArrayList;

import org.apache.struts.action.ActionForm;

public class LeaveBalanceForm extends ActionForm 
{
	public String departmentId = "0";
	public String designationId = "0";
	public String code = "";
	public String name = "";
	public String finYear = "";
	private String typeOfLeaveMstr;
	private String[] availableLeaves;
	private String[] leaveid;
	private String[] empId;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public String getDesignationId() {
		return designationId;
	}
	public void setDesignationId(String designationId) {
		this.designationId = designationId;
	}
	public String getFinYear() {
		return finYear;
	}
	public void setFinYear(String finYear) {
		this.finYear = finYear;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTypeOfLeaveMstr() {
		return typeOfLeaveMstr;
	}
	public void setTypeOfLeaveMstr(String typeOfLeaveMstr) {
		this.typeOfLeaveMstr = typeOfLeaveMstr;
	}
	public String[] getAvailableLeaves() {
		return availableLeaves;
	}
	public void setAvailableLeaves(String[] availableLeaves) {
		this.availableLeaves = availableLeaves;
	}
	
	public String[] getLeaveid() {
		return leaveid;
	}
	public void setLeaveid(String[] leaveid) {
		this.leaveid = leaveid;
	}
	public String[] getEmpId() {
		return empId;
	}
	public void setEmpId(String[] empId) {
		this.empId = empId;
	}
	
	
}
