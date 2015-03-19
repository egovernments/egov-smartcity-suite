/*
 * @(#)EISServeable.java 3.0, 17 Jun, 2013 3:05:07 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.egov.infra.admin.master.entity.User;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;

/**
 * For all EIS dependent Service 
 **/
public interface EISServeable {
	
	public static enum DATE_ORDER {PRIOR,AFTER};
    
	/**
     * Used get all Positions for a User Id for a particular Date.
     * @param user id
     * @param forDate
     * @return List of Position
     **/
    List <Position> getPositionsForUser (Integer userId, Date forDate);
   
    /**
     * Used get User for a Position Id for a particular Date.
     * @param position Id
     * @param forDate
     * @return User
     **/
    User getUserForPosition (Integer positionId, Date forDate);

     /**
     * Used get Primary Position for a given User Id for a particular Date.
     * @param position Id
     * @param forDate
     * @return Position
     **/
    Position getPrimaryPositionForUser (Integer userId, Date forDate);
    
    
    /**
     * This API is to get a list of employeeView from HashMap values that are passed.
     * @param paramMap
     * <p>
	 * HashMap<String, String> paramMap will have the data required for the search criteria for employeeView list
	 * <p>departmentId -This will be the id of the department in String
	 * <p>designationId -This will be the id of the designation in String
	 * <p>functionaryId -This will be the id of the functionary in String
	 * <p>code -This will be the code of the employee in String
	 * <p>name -This will be the name of the employee in String
	 * <p>status -This will be the id of employee status in String
	 * <p>empType -This will be the id of employee type in String
	 * <p>searchAll -This will be the "Y" if you want to get all employees / "N" if you don't want
     * @return list of employeeView     * 
     */
    public List<? extends Object> getEmployeeInfoList(HashMap<String,String> paramMap);
    
    /**
     * This API is to get a list of employeeView from HashMap values that are passed.
     * @param paramMap
     * <p>
	 * HashMap<String, String> paramMap will have the data required for the search criteria for position list
	 * 	<p>departmentId -This will be the id of the department in String
	 * 	<p>designationId -This will be the id of the designation in String
	 * 	<p>functionaryId -This will be the id of the functionary in String
	 * 	<p>functionId -This will be the id of the function in String
	 * 	<p>fundId -This will be the id of the fund in String
	 *  <p>boundaryId -This will be the id of the boundary in String	 
	 * <p>
	 * date
     * @return list of unique position 
     */
    public List<Position> getUniquePositionList(HashMap<String,String> paramMap,Date date);
    
    /**
     * @param givenDate - you can pass any date
     * @param noOfDays - no of days prior or after.
     * @param priorOrAfter - "PRIOR" for prior or "AFTER" for after.
     * @return
     */
    public Date getPriorOrAfterWorkingDate(Date givenDate,int noOfDays,DATE_ORDER orderType);
    
    /**
     * return all distinct Designations to which employees are assigned in the given department for given date. 
     * This list includes primary as well as secondary assignments.
     * If there is No Designation for the given department then returns the empty list
     * @param departmentId
     * @param givenDate
     * @return DesignationMaster List
     */
    
    public List <DesignationMaster> getAllDesignationByDept(Integer departmentId, Date givenDate);
    
    /**
     * Get all users for the given department and designation id's for the given date
     *@param deptId the Department Id
     *@param desigId the Designation Id
     *@param date the java.util.Date
     *@return List of Users
     */
     public List<User> getUsersByDeptAndDesig(Integer deptId, Integer desigId, Date date);
     /**
 	 * Based on the ISFILTERBYDEPT flag api returns departmentlist 
 	 * @return List of DepartmentImpl
 	 */
     public List<DepartmentImpl> getDeptsForUser();
}
