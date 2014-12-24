package org.egov.pims.model;
import java.util.Date;

import org.egov.commons.CFunction;
import org.egov.lib.rjbac.user.UserImpl;

// Generated Aug 8, 2007 6:26:41 PM by Hibernate Tools 3.2.0.b9


/**
 * EmployeeView
 */
public class EmployeeView implements java.io.Serializable
{


	private Integer assignId;
	private Integer id;
	private AssignmentPrd assignmentPrd= null;
	private Assignment assignment= null;
	private String employeeCode;
	private String employeeName;
	private org.egov.pims.commons.DesignationMaster desigId;
	private Date fromDate;
	private Date toDate;
	private org.egov.pims.model.PersonalInformation reportsTo;
	private org.egov.pims.commons.Position position;
	private org.egov.lib.rjbac.dept.Department deptId;
	private org.egov.commons.Functionary functionary;
	private Date dateOfFirstAppointment;
	private org.egov.commons.EgwStatus employeeStatus;
	private UserImpl userMaster;
	private EmployeeStatusMaster employeeType;
	private char isPrimary;
	private CFunction functionId;
	
	public CFunction getFunctionId() {
		return functionId;
	}
	public void setFunctionId(CFunction functionId) {
		this.functionId = functionId;
	}
	public char getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(char isPrimary) {
		this.isPrimary = isPrimary;
	}
	public EmployeeStatusMaster getEmployeeType() {
		return employeeType;
	}
	public void setEmployeeType(EmployeeStatusMaster employeeType) {
		this.employeeType = employeeType;
	}
	public org.egov.commons.EgwStatus getEmployeeStatus() {
		return employeeStatus;
	}
	public void setEmployeeStatus(
			org.egov.commons.EgwStatus employeeStatus) {
		this.employeeStatus = employeeStatus;
	}
	private Integer isActive= Integer.valueOf(0);
	public Assignment getAssignment() {
		return assignment;
	}
	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}
	public AssignmentPrd getAssignmentPrd() {
		return assignmentPrd;
	}
	public void setAssignmentPrd(AssignmentPrd assignmentPrd) {
		this.assignmentPrd = assignmentPrd;
	}
	public Date getDateOfFirstAppointment() {
		return dateOfFirstAppointment;
	}
	public void setDateOfFirstAppointment(Date dateOfFirstAppointment) {
		this.dateOfFirstAppointment = dateOfFirstAppointment;
	}
	public org.egov.pims.commons.DesignationMaster getDesigId() {
		return desigId;
	}
	public void setDesigId(org.egov.pims.commons.DesignationMaster desigId) {
		this.desigId = desigId;
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
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public org.egov.pims.model.PersonalInformation getReportsTo() {
		return reportsTo;
	}
	public void setReportsTo(org.egov.pims.model.PersonalInformation reportsTo) {
		this.reportsTo = reportsTo;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	public org.egov.lib.rjbac.dept.Department getDeptId() {
		return deptId;
	}
	public void setDeptId(org.egov.lib.rjbac.dept.Department deptId) {
		this.deptId = deptId;
	}
	public org.egov.pims.commons.Position getPosition() {
		return position;
	}
	public void setPosition(org.egov.pims.commons.Position position) {
		this.position = position;
	}
	public org.egov.commons.Functionary getFunctionary() {
		return functionary;
	}
	public void setFunctionary(org.egov.commons.Functionary functionary) {
		this.functionary = functionary;
	}
	public UserImpl getUserMaster() {
		return userMaster;
	}
	public void setUserMaster(UserImpl userMaster) {
		this.userMaster = userMaster;
	}
	public Integer getAssignId() {
		return assignId;
	}
	public void setAssignId(Integer assignId) {
		this.assignId = assignId;
	}




}
