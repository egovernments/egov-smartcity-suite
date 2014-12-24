/*
 * PropertyDAO.java Created on Oct 05 , 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.pims.model.PersonalInformation;



/**
 * <p>This is an interface which would be implemented by the
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data
 * access operations for Property
 *
 * @author deepak
 * @version 2.00
 */
public interface PersonalInformationDAO extends org.egov.infstr.dao.GenericDAO
{
	public PersonalInformation getPersonalInformationByID(Integer ID) ;
	public Set getDisciplinaryPunishmentByEmployeeID(Integer ID);
	public Map getAllPIMap();
	public PersonalInformation getPersonalInformationByUserId(Integer userId);
	public void deleteLangKnownForEmp(PersonalInformation personalInformation);
	public List getListOfPersonalInformationByEmpIdsList(List empIdsList);
	public PersonalInformation getEmployee(Integer deptId, Integer designationId, Integer Boundaryid)throws TooManyValuesException, NoSuchObjectException;
	public PersonalInformation getEmployeeByFunctionary(Integer deptId, Integer designationId, Integer Boundaryid,Integer functionaryId)throws TooManyValuesException, NoSuchObjectException;
	public List getListOfUsersByBoundaryId(Integer boundaryId) throws NoSuchObjectException;
	public List getListOfUsersForGivenBoundaryId(Integer boundaryId) throws NoSuchObjectException;
	/**
	  * Returning temporary  assigned employee object by pepartment,designation,functionary,date 
	  * @param deptId
	  * @param DesigId
	  * @param functionaryId
	  * @param onDate
	  * @return Employee
	  * @throws Exception 
	  */
	 public PersonalInformation getTempAssignedEmployeeByDeptDesigFunctionaryDate(Integer deptId, Integer desigId, Integer functionaryId, Date onDate) throws Exception;
	 
	 public abstract List getAllDesignationByDept(Integer deptId)throws TooManyValuesException, NoSuchObjectException;
	 public abstract List getAllActiveUsersByGivenDesg(Integer DesgId)throws Exception;
	 public List<PersonalInformation> getAllEmpByGrade(Integer gradeId) throws Exception;
	 public List getListOfUsersNotMappedToEmp() throws Exception;
}
