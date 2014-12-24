/*
 * PropertyDAO.java Created on Oct 05 , 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;
import java.util.Date;
import java.util.List;

import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeView;


/**
 * <p>This is an interface which would be implemented by the
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data
 * access operations for Property
 *
 * @author deepak
 * @version 2.00
 */
public interface AssignmentDAO extends org.egov.infstr.dao.GenericDAO
{
	public Assignment getAssignmentById(Integer id);
	public boolean getHodById(Integer id);
	public List getListOfEmployeeWithoutAssignment(Date fromdate);
	public Assignment getLatestAssignmentForEmployeeByToDate(Integer empId,Date todate) throws Exception;
	public abstract List<EmployeeView> getEmployeeWithTempAssignment(Date givenDate,Integer posId);
	public List<EmployeeView> getEmployeeWithTempAssignment(String code,Date givenDate,Integer posId);
	
}
