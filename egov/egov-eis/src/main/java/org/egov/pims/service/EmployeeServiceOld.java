/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */


package org.egov.pims.service;

//@author deepak

import org.egov.commons.exception.NoSuchObjectException;
import org.egov.commons.exception.TooManyValuesException;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.EmployeeView;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationException;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.pims.model.EmployeeNamePoJo;
import org.egov.pims.model.GenericMaster;
import org.egov.pims.model.LangKnown;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.ServiceHistory;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
@Deprecated
public interface EmployeeServiceOld 
{

	public abstract boolean checkDuplication(String name,String className);

	public abstract PersonalInformation createEmloyee(PersonalInformation egpimsPersonalInformation);
	public abstract void updateEmloyee(PersonalInformation egpimsPersonalInformation);
	public abstract PersonalInformation getEmloyeeById(Integer employeeId);
	public abstract GenericMaster getGenericMaster(Integer masterId,String masterName);

	public abstract ServiceHistory getServiceId(Integer serviceId)throws Exception;
	public abstract PersonalInformation getEmpForUserId(Long userId);

	public abstract Assignment getAssignmentById(Integer assignmentId);
	public abstract void updateAssignment(Assignment assignment);
	public abstract void addLangKnown(PersonalInformation personalInformation,LangKnown egpimsLangKnown);
	public abstract List getListOfEmpforDept(Integer deptId);
	public abstract List getListOfEmpforDesignation(Integer desigId);
	public abstract Assignment getLatestAssignmentForEmployee(Integer empId);
	public abstract Assignment getAssignmentByEmpAndDate(Date date,Integer empId);
	public abstract EmployeeNamePoJo getNameOfEmployee(Integer empId);
	public  abstract Map getAllPIMap();
	public abstract Map getMapForList(List list);
	public abstract Map getMapForList(List list, String fieldName1, String fieldName2);

	public abstract List searchEmployee(Integer departmentId,Integer designationId,String code,String name,Integer status)throws Exception;
	/*
	 * search employee by department,designation,functionary,code and name
	 */
	public abstract List searchEmployee(Integer departmentId,Integer designationId,Integer functionaryId,String code,String name,Integer status)throws Exception;
	@Deprecated
	public abstract List searchEmployee(Integer departmentId,Integer designationId,String code,String name,String searchAll)throws Exception;
	public abstract List searchEmployee(Integer empId)throws Exception;
	public abstract void deleteLangKnownForEmp(PersonalInformation personalInformation);
	public abstract Integer getNextVal();
	public abstract boolean checkPos(Integer posId,Date fromDate,Date toDate,Integer empId,String isPrimary);
	public abstract PersonalInformation getEmployeeforPosition(Position higherpos);
	public abstract Position getPositionforEmp(Integer empId);
	public abstract String getEmployeeCode();
	public abstract List getListOfPersonalInformationByEmpIdsList(List empIdsList);
	public List getListOfEmployeeWithoutAssignment(Date fromdate);/*new*/
	public Assignment getLatestAssignmentForEmployeeByToDate(Integer empId,Date todate) throws Exception;
	public PersonalInformation getEmployee(Integer deptId, Integer designationId, Long Boundaryid)throws TooManyValuesException, NoSuchObjectException;
	public PersonalInformation getEmployeeByFunctionary(Long deptId, Long designationId, Long Boundaryid,Integer functionaryId)throws TooManyValuesException, NoSuchObjectException;
	public Assignment getLastAssignmentByEmp(Integer empId);

	//to delete grade master


	public abstract List<PersonalInformation> getAllEmpByGrade(Integer gradeId);

	/*
	 * search employee by department,designation,functionary,code and name and employee type
	 */
	@Deprecated
	public abstract List searchEmployee(Integer departmentId,Integer designationId,Integer functionaryId,String code,String name,Integer status,Integer empType)throws Exception;
	/*
	 * Api to get Employee based on Position Id and Date
	 * toDate will take sysdate if it is not provided.
	 */
	public abstract PersonalInformation  getEmpForPositionAndDate(Date date,Integer posId)throws Exception;


	public abstract List searchEmployeeByGrouping(LinkedList<String> groupingByOrder)throws Exception;


	public abstract List getAllDesignationByDept(Integer deptId);

	public abstract List getAllActiveUsersByGivenDesg(Integer DesgId)throws Exception;

	public abstract List<EmployeeView> getEmployeeWithTempAssignment(Date givenDate,Integer posId);

	public abstract List<EmployeeView> getEmployeeWithTempAssignment(String code,Date givenDate,Integer posId);

	public abstract List getEmpTempAssignment(String code,Date givenDate,Integer posId);

	 public List<Integer> getAssignmentsForEmp(Integer empId,Date givenDate) throws ApplicationException;

	 /**
	  * API that will return all positions for a user(temporary and permanent) for a date.
	  * consider system date if date is not provided
	  */

	 public List<Position> getPositionsForUser(User user, Date date)throws ApplicationException;

	 public abstract List getEmpPrimaryAssignment(String code,Date givenDate,Integer posId);
	 public  List searchEmployee(Integer status,Date fromDate,Date toDate)throws Exception;

		/*
		 * Api to get the department for the employee who has logged in
		 *
		 */
		public abstract List getListOfDeptBasedOnUserDept(String userName);

		public abstract boolean isFilterByDept();

		/**
		 * API to fetch employeeInfo based on department and designation
		 */
		public abstract List<EmployeeView> getEmployeeInfoBasedOnDeptAndDesg(Integer deptId ,Integer desgId);
		public List<EmployeeView> getEmployeeInfoBasedOnDeptAndDate(Integer deptId, Date date);
		public List<EmployeeView> searchEmployee(Integer designationId,String code,String name,
				Integer statusId,Integer empTypeId,Map<String,Integer> finParams)throws Exception;
		public List<PersonalInformation> getAllEmployees();
		public List getListOfUsersNotMappedToEmp() throws Exception;
		public List <PersonalInformation>getEmpListForPositionAndDate(Date dateEntered,Integer posId)throws Exception;
		public Designation getPresentDesignation(Integer  idPersonalInformation);

}


