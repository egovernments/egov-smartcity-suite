/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pims.model;
import java.util.Date;

import org.egov.commons.CFunction;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.EmployeeType;
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
	private org.egov.infra.admin.master.entity.Department deptId;
	private org.egov.commons.Functionary functionary;
	private Date dateOfFirstAppointment;
	private org.egov.commons.EgwStatus employeeStatus;
	private User userMaster;
	private EmployeeType employeeType;
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
	public EmployeeType getEmployeeType() {
		return employeeType;
	}
	public void setEmployeeType(EmployeeType employeeType) {
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
	public org.egov.infra.admin.master.entity.Department getDeptId() {
		return deptId;
	}
	public void setDeptId(org.egov.infra.admin.master.entity.Department deptId) {
		this.deptId = deptId;
	}

}
