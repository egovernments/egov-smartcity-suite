package org.egov.pims.web.actions.report;

public class TempAssignDto {

	private String empCode="";
	private String empFirstName="";
	private String empDesgName="";
	private String deptName="";
	private String fromDate="";
	private String toDate="";
	private String tempPos="";
	private String holdingPriPos="";
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getEmpFirstName() {
		return empFirstName;
	}
	public void setEmpFirstName(String empFirstName) {
		this.empFirstName = empFirstName;
	}
	public String getEmpDesgName() {
		return empDesgName;
	}
	public void setEmpDesgName(String empDesgName) {
		this.empDesgName = empDesgName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getTempPos() {
		return tempPos;
	}
	public void setTempPos(String tempPos) {
		this.tempPos = tempPos;
	}
	public String getHoldingPriPos() {
		return holdingPriPos;
	}
	public void setHoldingPriPos(String holdingPriPos) {
		this.holdingPriPos = holdingPriPos;
	}
}
