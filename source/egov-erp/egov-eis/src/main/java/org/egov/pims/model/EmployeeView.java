package org.egov.pims.model;
import java.util.Date;

import org.egov.commons.CFunction;
import org.egov.infra.admin.master.entity.User;

// Generated Aug 8, 2007 6:26:41 PM by Hibernate Tools 3.2.0.b9


/**
 * EmployeeView
 */
public class EmployeeView implements java.io.Serializable
{


	private Integer assignId;
	private Integer id;
	private Assignment assignment= null;
	private String employeeCode;
	private String employeeName;
	private org.egov.pims.commons.DesignationMaster desigId;
	private Date fromDate;
	private Date toDate;
	//private org.egov.pims.model.PersonalInformation reportsTo;
	private org.egov.pims.commons.Position position;
	private org.egov.lib.rjbac.dept.DepartmentImpl deptId;
	private org.egov.commons.Functionary functionary;
	private Date dateOfFirstAppointment;
	private org.egov.commons.EgwStatus employeeStatus;
	private User userMaster;
	private EmployeeStatusMaster employeeType;
	private char isPrimary;
	private CFunction functionId;
	private Integer isActive= Integer.valueOf(0);
	private Integer isUserActive = Integer.valueOf(0);
	private String userName;
	
	public Integer getIsUserActive() {
		return isUserActive;
	}
	public void setIsUserActive(Integer isUserActive) {
		this.isUserActive = isUserActive;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
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

	public Assignment getAssignment() {
		return assignment;
	}
	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
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
	/*public org.egov.pims.model.PersonalInformation getReportsTo() {
		return reportsTo;
	}
	public void setReportsTo(org.egov.pims.model.PersonalInformation reportsTo) {
		this.reportsTo = reportsTo;
	}*/
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
	public User getUserMaster() {
		return userMaster;
	}
	public void setUserMaster(User userMaster) {
		this.userMaster = userMaster;
	}
	public Integer getAssignId() {
		return assignId;
	}
	public void setAssignId(Integer assignId) {
		this.assignId = assignId;
	}
	public org.egov.lib.rjbac.dept.DepartmentImpl getDeptId() {
		return deptId;
	}
	public void setDeptId(org.egov.lib.rjbac.dept.DepartmentImpl deptId) {
		this.deptId = deptId;
	}

}
