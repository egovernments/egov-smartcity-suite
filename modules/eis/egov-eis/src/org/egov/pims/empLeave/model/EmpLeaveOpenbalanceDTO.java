package org.egov.pims.empLeave.model;

import java.util.ArrayList;

public class EmpLeaveOpenbalanceDTO implements java.io.Serializable 
{
	Integer leaveId=null;
	String empName=null;
	String empCode=null;
	String empId=null;
	String availableLeaves=null;
	
	
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public Integer getLeaveId() {
		return leaveId;
	}
	public void setLeaveId(Integer leaveId) {
		this.leaveId = leaveId;
	}
	
	
	public void setAvailableLeaves(String availableLeaves) {
		this.availableLeaves = availableLeaves;
	}
	public String getAvailableLeaves() {
		return availableLeaves;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	
	
}
