package org.egov.pims.empLeave.client;

import java.util.Date;

import org.apache.struts.action.ActionForm;

public class LeaveForm extends ActionForm{
	
	private String fromDate;
	private String toDate;
	private String workingDays;
	private String reason;
	private String availableLeaves;
	private String applicationNumber;
	private String[] holidayId;
	private String month;
	private String twoHdLeaves;
	 
	private String leaveAppId;
	private String compOffDate;
	private String compOffId;
	private String statusId;
	
	
	private String sanctionNo;
	private String statusName;
	private String[] ulb;
	private String[] financialId;
	private String[] holidayDate;
	
	
	private String payElegible;
	private String typeOfLeaveMstr;
	private int encashment;
	
	private String employeeName;
	private String empCode;
	private String workedOnHolidayDate;
	private String empId;
	
	private String workedonFromDate;
	private String workedonToDate;
	
	private String approverDept;
	private String approverDesig;
	
	private String approverEmpAssignmentId;
	
	private String desigId; 
	private String ess;//to check it is requested from Employee self Service
	
	private Date dateFromDate;
	private Date dateToDate;
	public String getApproverEmpAssignmentId() {
		return approverEmpAssignmentId;
	}
	public void setApproverEmpAssignmentId(String approverEmpAssignmentId) {
		this.approverEmpAssignmentId = approverEmpAssignmentId;
	}
	public String getTypeOfLeaveMstr() {
		return typeOfLeaveMstr;
	}
	public void setTypeOfLeaveMstr(String typeOfLeaveMstr) {
		this.typeOfLeaveMstr = typeOfLeaveMstr;
	}
	public String getAvailableLeaves() {
		return availableLeaves;
	}
	public void setAvailableLeaves(String availableLeaves) {
		this.availableLeaves = availableLeaves;
	}
	
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getWorkingDays() {
		return workingDays;
	}
	public void setWorkingDays(String workingDays) {
		this.workingDays = workingDays;
	}
	public String getPayElegible() {
		return payElegible;
	}
	public void setPayElegible(String payElegible) {
		this.payElegible = payElegible;
	}
	
	
	public String getLeaveAppId() {
		return leaveAppId;
	}
	public void setLeaveAppId(String leaveAppId) {
		this.leaveAppId = leaveAppId;
	}
	public String getSanctionNo() {
		return sanctionNo;
	}
	public void setSanctionNo(String sanctionNo) {
		this.sanctionNo = sanctionNo;
	}
	
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getApplicationNumber() {
		return applicationNumber;
	}
	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}
	public String[] getFinancialId() {
		return financialId;
	}
	public void setFinancialId(String[] financialId) {
		this.financialId = financialId;
	}
	public String[] getHolidayDate() {
		return holidayDate;
	}
	public void setHolidayDate(String[] holidayDate) {
		this.holidayDate = holidayDate;
	}
	public String[] getUlb() {
		return ulb;
	}
	public void setUlb(String[] ulb) {
		this.ulb = ulb;
	}
	public String[] getHolidayId() {
		return holidayId;
	}
	public void setHolidayId(String[] holidayId) {
		this.holidayId = holidayId;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getCompOffDate() {
		return compOffDate;
	}
	public void setCompOffDate(String compOffDate) {
		this.compOffDate = compOffDate;
	}
	public String getCompOffId() {
		return compOffId;
	}
	public void setCompOffId(String compOffId) {
		this.compOffId = compOffId;
	}
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	public String getTwoHdLeaves() {
		return twoHdLeaves;
	}
	public void setTwoHdLeaves(String twoHdLeaves) {
		this.twoHdLeaves = twoHdLeaves;
	}
	public int getEncashment() {
		return encashment;
	}
	public void setEncashment(int encashment) {
		this.encashment = encashment;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getWorkedOnHolidayDate() {
		return workedOnHolidayDate;
	}
	public void setWorkedOnHolidayDate(String workedOnHolidayDate) {
		this.workedOnHolidayDate = workedOnHolidayDate;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getWorkedonFromDate() {
		return workedonFromDate;
	}
	public void setWorkedonFromDate(String workedonFromDate) {
		this.workedonFromDate = workedonFromDate;
	}
	public String getWorkedonToDate() {
		return workedonToDate;
	}
	public void setWorkedonToDate(String workedonToDate) {
		this.workedonToDate = workedonToDate;
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
	public String getDesigId() {
		return desigId;
	} 
	public void setDesigId(String desigId) {
		this.desigId = desigId;
	}
	public String getEss() {
		return ess;
	}
	public void setEss(String ess) {
		this.ess = ess;
	}
	public Date getDateFromDate() {
		return dateFromDate;
	}
	public void setDateFromDate(Date dateFromDate) {
		this.dateFromDate = dateFromDate;
	}
	public Date getDateToDate() {
		return dateToDate;
	}
	public void setDateToDate(Date dateToDate) {
		this.dateToDate = dateToDate;
	}
	
	
}
