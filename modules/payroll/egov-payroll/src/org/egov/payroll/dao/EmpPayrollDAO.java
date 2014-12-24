/*
 * EmpPayrollDAO.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.payroll.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.infstr.models.Script;
import org.egov.lib.rjbac.dept.Department;
import org.egov.payroll.model.EmpPayroll;
import org.egov.pims.model.PersonalInformation;





/**
 * <p>This is an interface which would be implemented by the
 * Individual Frameworks  for all the CRUD (create, read, update, delete) basic data
 * access operations for emppayroll
 *
 * @author Lokesh
 * @version 2.00
 *
 */

public interface EmpPayrollDAO extends org.egov.infstr.dao.GenericDAO
{

	/**
	 * @param empId
	 * @param month
	 * @param year
	 * @return normal payroll for employee for the  month and year passed 
	 */
	public EmpPayroll getPayslipForEmpByMonthAndYear(Integer empId,Integer month,Integer year);
	
	public List<EmpPayroll> getAllPayslipByEmpMonthYear(Integer empId, Integer month, Integer year);
	
	/**
	 * @param empId
	 * @param month
	 * @param year
	 * @return normal payroll for employee for the  month and year passed 
	 */
	public EmpPayroll getPayslipForEmpByMonthAndYear(Integer empId,Integer month,Integer year,Integer paytype);
	/**
	 * @param status
	 * @return list payslip by status
	 */
    public List getAllPayslipByStatus(EgwStatus status);
    
    /**
     * 
     * @param year
     * @param month
     * @param department
     * @param status
     * @return list of payslip of given month,year and status for department passed
     */
	public List getPayslipsOfYearMonthStatus(String year, BigDecimal month, Department department, EgwStatus status)throws Exception;
	
	/**
	 * @param empId
	 * @param month
	 * @param year
	 * @return list of payslips for employee previous to the month and year passed 
	 */
	public List getAllPrevPayslipsForEmpByMonthAndYear(Integer empId,Integer month,Integer year);
	
	 /** 
	 * Gets list of all approved normal payslip from previous months for employee that have no exception for current month 
	 * @param month
	 * @param finYear
	 * @return List of previous EmpPayrolls which are approved and where Employee is not an exception in given month and year
	 */
	public List getPrevApprovedPayslipsByMonthAndYear(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid);
			
	/**
	 * this api will return the last approved payslip for the employeee for the month and finYear passed
	 * @param empId
	 * @param month
	 * @param finYear
	 * @return EmpPayroll 
	 * @throws Exception GregorianCalendar fromDate,GregorianCalendar toDate,Integer empid
	 */
	public EmpPayroll getPrevApprovedPayslipForEmpByDates(GregorianCalendar fromDate,GregorianCalendar toDate,Integer empid) throws Exception;
	
	/**
	 * Gets list of all employees who has exception in given month and 
	 * has approved supplementary payslip for previous month and todate is more than current month.
	 * @param month
	 * @param finYear
	 * @return List of EmpPayrolls
	 */
	public List getPrevSuppPayslipsForEmpExceptionsInMonthAndYear(GregorianCalendar fromDate,GregorianCalendar toDate,Integer deptid);
	
	public List<Integer> getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid,Integer functionaryId);
	public List<Integer> getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid,Integer functionaryId,Long functionId,Integer billNumberId);
	public List<Integer> getSuppPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid,Integer functionaryId);
	public List<Integer> getSuppPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid,Integer functionaryId,Long functionId,Integer billNumberId);
	public EmpPayroll getPrevApprovedSuppPayslipForEmpByDates(GregorianCalendar fromDate,GregorianCalendar toDate,Integer empid) throws Exception;
	public String getEmpInfoByLastAssignment(Integer empid) throws Exception;
	public String getEmpInfoByFDateAndTDate(Integer empid,GregorianCalendar fromdate,GregorianCalendar todate) throws Exception;
	/**
	 * this api will return the last approved payslip for the employeee for the month and finYear passed
	 * @param empId
	 * @param month
	 * @param finYear
	 * @return EmpPayroll 
	 * @throws Exception
	 */
	public EmpPayroll getPrevApprovedPayslipForEmpByMonthAndYear(Integer empId, Integer month,Integer finYear) throws Exception;
	
	public List getPayslipsOfYearMonthStatusInternal(String year, BigDecimal month, EgwStatus status);
	
	/*
	 * Return total deduction amount for passing glcode,financial year
	 */
	public BigDecimal getTotalDeductionAmount(Integer employeeId, CChartOfAccounts accountCode, Date toDate ,CFinancialYear finYear) throws Exception;
	
	/*
	 * Return list of deduction amount in passing duration (Applicable for only non-interest bearing deduction)
	 */
	public List<String> GetListOfDeductionAmount(Integer idEmployee, CChartOfAccounts accountCode, Date fromDate, Date toDate)throws Exception;
	
	/**
	 *  Return List of HaspMap Objects contain earnings data
	 *  Hash Map contains  'from April to Mar',type,salhead,YTD as Keys  And concern amounts,type,salcode,YTD as values  
	 * @param employeeid
	 * @param finyearid
	 * @return List<HashMap>
	 * @throws Exception
	 */
	public List<HashMap> getEarningsForEmployeeForFinYear(Integer employeeid,Integer finyearid) throws Exception;
	
	/**
	 *  Return List of HaspMap Objects contain deductions data
	 *  Hash Map contains  'from April to Mar',type,salhead,YTD as Keys  And concern amounts,type,salcode,YTD as values
	 * @param employeeid
	 * @param finyearid
	 * @return List<HashMap>
	 * @throws Exception
	 */

	public List<HashMap> getDeductionsForEmployeeForFinYear(Integer employeeid,Integer finyearid) throws Exception;
	
	/**
	 * @param employeeid
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws Exception
	 */
	public List<HashMap> getEarningsAndDeductionsForEmpByMonthAndYearRange(Integer employeeid,Date fromDate,Date toDate) throws Exception;
	   
	/*
	 *	Return latest drawn payslip for passing employee  
	 */
	 public EmpPayroll getLatestPayslipByEmp(PersonalInformation employee)throws Exception;

	 public Script getScript(String scriptName);
	 
	public List<EmpPayroll> getAllPayslipByEmpMonthYearBasedOnDept(Integer deptId, Integer month, Integer year,Integer billNo);
	
	/**
	 * Get department payhead summary by date range
	 */
	public List<HashMap> getDeptPayheadSummary(Integer month,Integer finYear, Integer deptIds[],Integer billNumberId) throws Exception;

	/**
	 * @param month
	 * @param year
	 * @param functionaryId
	 * @param deptId
	 * @return
	 * @throws Exception
	 */
	public List<HashMap> getFunctionaryDeptWisePayBillSummary(Integer month, Integer year,Integer functionaryId,Integer deptId) throws Exception;
	
	/**
	 * @param month
	 * @param year
	 * @param functionaryIds
	 * @return
	 * @throws Exception
	 */
	public List<HashMap> getFunctionaryPayheadSummary(Integer month, Integer year,Integer functionaryIds[],Integer billNumberId) throws Exception;
	
	public List<HashMap> getBankAdviceReportByBillIds(Integer[] billIds) throws Exception;
}
