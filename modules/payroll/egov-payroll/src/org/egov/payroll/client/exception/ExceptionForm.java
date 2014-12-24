package org.egov.payroll.client.exception;

import org.apache.struts.action.ActionForm;

public class ExceptionForm extends ActionForm{
	
	String empCode[];
	String empName[];
	String type[];
	String reason[];
	String comments[];
	String srEntry[];
	String financialYear;
	String month;
	String empId;
	String exceptionStatus;
	String employeeCode;
	String employeeName;
	String exType;
	String exReason;
	String exComments;
	String exceptionId;
	String actionType;
	String isSaved;
	String status;
	String fromDate;
	String toDate;
	String createdOn;
	String disciplinaryId;
	private String approverDept;
	private String approverDesig;
	private String approverEmpAssignmentId;
	
	
	
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
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
	public String[] getType() {
		return type;
	}
	public void setType(String[] type) {
		this.type = type;
	}
	public String[] getComments() {
		return comments;
	}
	public void setComments(String[] comments) {
		this.comments = comments;
	}
	public String[] getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String[] empCode) {
		this.empCode = empCode;
	}
	public String[] getEmpName() {
		return empName;
	}
	public void setEmpName(String[] empName) {
		this.empName = empName;
	}
	public String[] getReason() {
		return reason;
	}
	public void setReason(String[] reason) {
		this.reason = reason;
	}
	public String getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getExceptionStatus() {
		return exceptionStatus;
	}
	public void setExceptionStatus(String exceptionStatus) {
		this.exceptionStatus = exceptionStatus;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getExComments() {
		return exComments;
	}
	public void setExComments(String exComments) {
		this.exComments = exComments;
	}
	public String getExReason() {
		return exReason;
	}
	public void setExReason(String exReason) {
		this.exReason = exReason;
	}
	public String getExType() {
		return exType;
	}
	public void setExType(String exType) {
		this.exType = exType;
	}
	public String getExceptionId() {
		return exceptionId;
	}
	public void setExceptionId(String exceptionId) {
		this.exceptionId = exceptionId;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getIsSaved() {
		return isSaved;
	}
	public void setIsSaved(String isSaved) {
		this.isSaved = isSaved;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String[] getSrEntry() {
		return srEntry;
	}
	public void setSrEntry(String[] srEntry) {
		this.srEntry = srEntry;
	}
	
	public String getApproverDept() {
		return approverDept;
	}
	public void setApproverDept(String approverDept) {
		this.approverDept = approverDept;
	}
	public String getApproverDesig() {
		return approverDesig;
	}
	public void setApproverDesig(String approverDesig) {
		this.approverDesig = approverDesig;
	}
	public String getApproverEmpAssignmentId() {
		return approverEmpAssignmentId;
	}
	public void setApproverEmpAssignmentId(String approverEmpAssignmentId) {
		this.approverEmpAssignmentId = approverEmpAssignmentId;
	}
	public String getDisciplinaryId() {
		return disciplinaryId;
	}
	public void setDisciplinaryId(String disciplinaryId) {
		this.disciplinaryId = disciplinaryId;
	}
	
}
