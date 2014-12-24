package org.egov.payroll.services.exception;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


import org.egov.exceptions.DuplicateElementException;
import org.egov.commons.EgwStatus;
import org.egov.lib.rjbac.dept.Department;
import org.egov.payroll.dao.ExceptionDAO;
import org.egov.payroll.dao.PayrollDAOFactory;
import org.egov.payroll.model.EmpException;
import org.egov.payroll.model.ExceptionMstr;

public interface ExceptionService {
	
	
	public List getAllDistinctTypeExceptionMstr() throws Exception;
	public List<ExceptionMstr> getAllExceptionMstr() throws Exception;
	public ExceptionMstr getExceptionMstrById(Integer id) throws Exception;
	public EmpException createException(EmpException empException) throws Exception;
	public EmpException getExceptionById(Long exceptionId) throws Exception;
	public List<EmpException> getAllExceptions() throws Exception;
	public List<EmpException> getAllExceptionByFinYearIdMonth(Long finYearId,BigDecimal month) throws Exception;
	public List<EmpException> getAllExceptionByEmpIdFinYearIdMonth(Integer empId,Long finYearId,BigDecimal month) throws Exception;
	public List<EmpException> getAllExceptionByDeptForFinYear(Department dept,Long finYearId,BigDecimal month) throws Exception;
	public List<EmpException> getAllExceptionByDeptAndStatusForFinYear(Department dept,EgwStatus status,Long finYearId,BigDecimal month) throws Exception;
	public Boolean checkExistingEmpExceptionForMonthYear(EmpException empException) throws Exception;
	public EmpException checkAndCreateEmpException(EmpException empException) throws DuplicateElementException,Exception;
	/**
	 * this api will return the current active exception(whitch status is not closesd) 
	 * @param empid
	 * @param fromdate
	 * @param todate
	 * @return
	 */
	public List<EmpException> getActiveExceptionsForEmp(Integer empid,Date fromdate,Date todate) throws Exception;
	/**
	 * if the employee has exception for full month means excption is not ending in the middle of the month then it will return true
	 * otherwise it will return false
	 * @param empid
	 * @param fromdate
	 * @param todate
	 * @return
	 */
	public boolean isEmpHasExceptionForFullMonth(Integer empid,GregorianCalendar fromdate,GregorianCalendar todate) throws Exception;
	/**
	 * // this api will return the list of exceptions whose reports as login user
	 * @param userid
	 * @param finyrid
	 * @param month
	 * @return
	 */
	public List<EmpException> getAllExceptionsForUserByFinYrAndMonth(Integer userid,Integer finyrid,Integer month) throws Exception;
	/**
	 * // this api will return the list of exceptions whose reports to as login user
	 * @param userid
	 * @param finyrid
	 * @param month
	 * @return
	 */
	public List<EmpException> getAllExceptionsForUserByFinYrAndMonthAndStatus(Integer userid,Integer finyrid,Integer month,EgwStatus status) throws Exception;
	
	public List<EmpException> getAllEmpException(Long finYearId, BigDecimal month, String status);
}
