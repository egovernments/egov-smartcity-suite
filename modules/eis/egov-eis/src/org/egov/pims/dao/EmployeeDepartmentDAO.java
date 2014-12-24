/*
 * PropertyDAO.java Created on Oct 05 , 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.pims.dao;

import java.util.List;

import org.egov.pims.model.Assignment;
import org.egov.pims.model.EmployeeDepartment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.EmployeeView;

/**
 * <p>This is an interface which would be implemented by the
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data
 * access operations for Property
 *
 * @author deepak
 * @version 2.00
 */
public interface EmployeeDepartmentDAO extends org.egov.infstr.dao.GenericDAO
{
	public EmployeeDepartment getEmployeeDepartmentByID(Integer ID) ;
	public void deleteByAss(Assignment assignment);
	public  List<PersonalInformation>  getAllHodEmpByDept(Integer deptId) throws Exception;
	public  List<EmployeeView>  getAllHodEmpViewByDept(Integer deptId) throws Exception;
}
