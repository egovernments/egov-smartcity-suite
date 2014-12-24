/*
 * ExceptionDAO.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.payroll.dao;


import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.payroll.model.EmpException;




/**
 * <p>This is an interface which would be implemented by the
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data
 * access operations for exception
 *
 * @author Lokesh
 * @version 2.00
 * 
 */

public interface ExceptionDAO extends org.egov.infstr.dao.GenericDAO
{
	public List getAllExceptionsInStatusByMonthAndYear(Integer month, Integer finYear,EgwStatus status);
	public List<EmpException> getAllExceptionByEmpIdFinYearMonth(Integer empId,Long finYearId,BigDecimal month);
	public List<EmpException> getAllExceptionByFinYearIdAndMonth(Long financialYearId,BigDecimal month);
	public boolean isEmpHasExceptionForFullMonth(Integer empid,GregorianCalendar fromdate,GregorianCalendar todate);
	public List<EmpException> getActiveExceptionsForEmp(Integer empid,Date fromdate,Date todate);
	public List<EmpException> getAllExceptionsForUserByFinYrAndMonth(Integer userid,Integer finyrid,Integer month) throws Exception;
	public List<EmpException> getAllExceptionsForUserByFinYrAndMonthAndStatus(Integer userid,Integer finyrid,Integer month,EgwStatus status) throws Exception;
	public List<EmpException> getAllEmpException(Long finYearId, BigDecimal month, String status);
	
}