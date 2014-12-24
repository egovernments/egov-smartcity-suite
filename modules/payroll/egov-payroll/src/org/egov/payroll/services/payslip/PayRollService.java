/*
 * ManualGenerationPaySlipManager.java Created on Sep 30, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.payroll.services.payslip;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.BatchGenDetails;
import org.egov.payroll.model.Deductions;
import org.egov.payroll.model.Earnings;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.IncrementSlabsForPayScale;
import org.egov.payroll.model.PayScaleDetails;
import org.egov.payroll.model.PayScaleHeader;
import org.egov.payroll.model.PayStructure;
import org.egov.payroll.model.PayTypeMaster;
import org.egov.payroll.model.SalaryCodes;
import org.egov.pims.empLeave.model.EmployeeAttendenceReport;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;

public interface PayRollService
{
	
	public void createPayslip(EmpPayroll payslip) throws Exception; 
	
	public List getSalaryCodesByCategoryId(Integer categoryId) throws Exception;
	/**
	 * @param empId
	 * @return list Advances For employee
	 */
	public List getAdvancesByEmpId(Integer empId) throws Exception;
	/**
	 * 
	 * @return salary codes sorted by orderid asc 
	 */
	public List getOrderedSalaryCodes() throws Exception;
	/**
	 * @param empId
	 * @param month
	 * @param year
	 * @return normal payroll for employee for the  month and year passed 
	 */
	public EmpPayroll getPayslipForEmpByMonthAndYear(Integer empId,Integer month,Integer year) throws Exception;
	
	public EmpPayroll getPayslipForEmpByMonthAndYear(Integer empId,Integer month,Integer year,Integer paytype) throws Exception;
	
	
	public List<EmpPayroll> getAllPayslipByEmpMonthYear(Integer empId, Integer month, Integer year) throws Exception;
	
	/**
     * @param empId
     * @param month
     * @param year
     * @return list of payroll object's for employee for the month and year passed with YTD values populated
     */
    public EmpPayroll getPayslipForEmpByMonthAndYearWithYTD(Integer empId,Integer month,Integer year) throws Exception;
	
	/**
	 * @param status
	 * @return list of advances for the status passed 
	 */
	public List getAllAdvancesByStatus(EgwStatus status) throws Exception;
	/**
	 * @param empId
	 * @return list of PayStructure for employee
	 */
	public List getPayStructureByEmp(Integer empId) throws Exception;
	/**
	 * delete the earnings obj 
	 * @param earnings
	 */
	public void deleteEarnings(Earnings earnings) throws Exception;
	/**
	 * delete the payslip obj
	 * @param paySlip
	 */
	public void deletePayslip(EmpPayroll paySlip)throws Exception;
	/**
	 * delete the deduction obj
	 * @param deductions
	 */
	public void deleteDeductions(Deductions deductions) throws Exception;
	/**
	 * it will update the payslip
	 * @param paySlip
	 */
	public void updatePayslip(EmpPayroll paySlip) throws Exception;
	
	/**
	 * @param empId
	 * @param month
	 * @param year
	 * @return list of payslips for employee previous to the month and year passed 
	 */
	public List getAllPrevPayslipsForEmpByMonthAndYear(Integer empId,Integer month, Integer year) throws Exception;
		
	/**
	 * @param month
	 * @param finYear
	 * @param status
	 * @return list of Exceptions by status for the month and year passed
	 */
	public List getAllExceptionsInStatusByMonthAndYear(Integer month, Integer finYear,EgwStatus status) throws Exception;	
	
	/**
	 * @param payHeader
	 */
	public void createPayHeader(PayScaleHeader payHeader) throws Exception;	
	
	/**
	 * @param payHeader
	 */
	public void updatePayHeader(PayScaleHeader payHeader) throws Exception;
	/**
	 * @param name
	 * @return PayScaleHeader object for the name passed
	 */
	public PayScaleHeader getPayScaleHeaderByName(String name) throws Exception;
	/**
	 * @param payStructure
	 */
	public void createPayStructure(PayStructure payStructure) throws Exception;
	/**
	 * @param payScaleDetails
	 */
	public void deletePayScaleDetails(PayScaleDetails payScaleDetails) throws Exception;
	/**
	 * 
	 * @param incrSlabDetails
	 * @throws Exception
	 */
	public void deleteIncrSlabDetails(IncrementSlabsForPayScale incrSlabDetails) throws Exception;
	/**
	 * @param Id
	 * @return PayScaleHeader
	 */
	public PayScaleHeader getPayScaleHeaderForEmp(Integer Id) throws Exception;
	/**
	 * @param payStructure
	 */
    public void updatePayStructure(PayStructure payStructure) throws Exception;
    /**
     * @param Id
     * @return PayStructure
     */
    public PayStructure getPayStructureById(Integer Id) throws Exception;
    /**
     * @return list of PayScaleHeader objects
     */
    public List getAllPayScaleHeaders() throws Exception;
    /**
     * @param Id
     * @return PayScaleHeader
     */
    public PayScaleHeader getPayScaleHeaderById(Integer Id) throws Exception;
    /**
     * @param Id
     * @return SalaryCodes
     */
    public SalaryCodes getSalaryCodesById(Integer Id) throws Exception;
    /**
     * @param Id
     * @return Advance
     */
    public Advance getAdvanceById(Long Id) throws Exception;
    /**
     * @param payStructure
     */
    public void deletePayStructure(PayStructure payStructure) throws Exception;
    /**
     * it will return the current paystructure for employee
     * @param empid
     * @return PayStructure
     */
    public PayStructure getCurrentPayStructureForEmp(Integer empid) throws Exception;
    
    /**
     * it will return the paystructure for employee for a entered date
     * @param empid
     * @return PayStructure
     */
    public PayStructure getPayStructureForEmpByDate(Integer empid,Date date) throws Exception;
    
    /** 
	 * Gets list of all approved normal payslip from previous months for employee that have no exception for current month 
	 * @param month
	 * @param finYear
	 * @return List of previous EmpPayrolls which are approved and where Employee is not an exception in given month and year
	 */
    public List getPrevApprovedPayslipsByMonthAndYear(GregorianCalendar fromdate, GregorianCalendar todate,Integer deptid) throws Exception;
                   
    /**
     * this api will generate the normal payslips for employees whose don't have exception in the current month 
     * and have a normal approved payslips previous month .
     * if deptid is not null it will consider only employees of that perticular department.     
     * @param fromDate
     * @param toDate
     * @param depetid
     * @throws Exception
     */
   // public String generateBatchNormalPayslips(GregorianCalendar fromDate,GregorianCalendar toDate,Integer depetid,String username) throws Exception;
    
    public EmpPayroll getPrevApprovedSuppPayslipForEmpByDates(GregorianCalendar fromDate,GregorianCalendar toDate,Integer empid) throws Exception;
    
    public EmpPayroll getPrevApprovedPayslipForEmpByDates(GregorianCalendar fromDate,GregorianCalendar toDate,Integer empid) throws Exception;
    
    //public void createWorkFlow(EmpPayroll payslip) throws Exception;
    
    public EmpPayroll getPayslipById(Long id) throws Exception;
    
    public EmpPayroll getPayslipByIdWithYTD(Long payslipId) throws Exception;
    
    // this is for JBPM workflow takes only String parameter	  
	public EmpPayroll getPayslipById(String payslipId) throws EGOVRuntimeException;
	
	
	public BatchGenDetails insertBatchGenDetails(BatchGenDetails batchgenobj) throws Exception;

    /**
     * this api will generate the supplimentary payslips for employees who have exception in the current month 
     * and have a supplimentary approved payslips previous month and exception todate is not in current month .
     * if deptid is not null it will consider only employees of that perticular department.     
     * @param fromDate
     * @param toDate
     * @param depetid
     * @throws Exception
     */
   // public String generateBatchSuppPayslips(GregorianCalendar fromDate,GregorianCalendar toDate,Integer depetid,String username) throws Exception;
    
    public CFinancialYear getFinancialYearByDate(Date date) throws Exception;
    /**
     * 
     * @param finyr
     * @param month
     * @param deptid
     * @param empid
     * @param type
     * @return list of payslipfailure details as per given data all fields are optional (just pass in case of ignore parameter)
     */
    public List getPendingPaySlipsList(Integer finyr,Integer month,Integer deptid,Integer empid,Integer type,Integer functionaryId,Integer billNo,Integer errPay) throws Exception;
    /*
     * it will update the ishistiry field value to 'Y' where ishistory='N'
     */
    //public void updateIsHistory(Integer month,Integer year,Integer deptid,Integer functionaryId,Integer billNumberId) throws Exception;
    
    
    public void updateIsHistory(Integer month,Integer year,List empIdsList);
    
    /*
     * this api will return the emp code,emp name,emp id,emp designation name,emp department name,emp date of joing
     * separated by delimeter '~' for entered employee with in the given dates
     */
    public String getEmpInfoByLastAssignment(Integer empid) throws Exception;
    
    /**
     * it will update the batchfailure status to resolved(2)
     * @param empid
     * @param month
     * @param year
     * @return
     */
    public boolean resolvePaySlipFailure(Integer empid,Integer month,Integer year,Integer paytype) throws Exception;
    
    /**
     * this will return the deductionamount as per recovery 
     * @param salaryCode
     * @param basicsalary
     * @param date
     * @return
     */
    public double getSlabBasedAmount(SalaryCodes salaryCode,BigDecimal basicsalary,String date) throws Exception;
    
    /**
	 * this api will set the earnings for exception payslip 
	 * @param lastPay
	 * @param currPay
	 * @param empatcreport
	 * @return
	 */
    
	public EmpPayroll setEarningsForSuppPayslip(EmpPayroll lastPay,EmpPayroll currPay,EmployeeAttendenceReport empatcreport,boolean persists) throws Exception;
    /**
     * 
     * @param lastPay
     * @param currPay
     * @param empatcreport
     * @return
     */
    public EmpPayroll setEarnings(EmpPayroll lastPay,EmpPayroll currPay,EmployeeAttendenceReport empatcreport,boolean persists) throws Exception;
    /**
     * 
     * @param lastPay
     * @param currPay
     * @return
     */
    public EmpPayroll setDeductions(EmpPayroll lastPay,EmpPayroll currPay, boolean persist) throws Exception;
    
    public EmpPayroll setNormalDeductions(EmpPayroll lastPay,EmpPayroll currPay, boolean persist)  throws Exception;
    /**
     * it will return the starting date for the given month and financial year id
     * @param month
     * @param finyr
     * @return
     */
    public Date getStartDateOfMonthByMonthAndFinYear(int month,int finyr) throws Exception;
    /**
     * it will return the ending date for the given month and financial year id
     * @param month
     * @param finyr
     * @return
     */
    public Date getEndDateOfMonthByMonthAndFinYear(int month,int finyr) throws Exception;
    /**
	 * this api will return the last approved payslip for the employeee for the month and finYear passed
	 * @param empId
	 * @param month
	 * @param finYear
	 * @return EmpPayroll 
	 * @throws Exception
	 */
	public EmpPayroll getPrevApprovedPayslipForEmpByMonthAndYear(Integer empId, Integer month,Integer finYear) throws Exception;

	/**
	 * this api will return the increment pending amount for the employeee
	 * @param empid
	 * @param month
	 * @param finYear
	 * @return
	 */
	public BigDecimal getPendingIncrementAmount(Integer empid,Integer month,Integer finYear) throws Exception;
	
	public boolean resolvePendingIncr(Integer empid,Integer month,Integer year) throws Exception;
	
	public BatchGenDetails getBatchGenDetailsById(Long id) throws Exception;

	public void updateBatchGenDetals(BatchGenDetails batchgenobj) throws Exception;
	
	public String checkExistingPayscaleInPayslip(PayScaleHeader payScaleHeader) throws Exception;
	/**
	 * this api will return the PayTypeMaster based on entered paytype 
	 * @param paytype
	 * @return
	 */
	public PayTypeMaster getPayTypeMasterByPaytype(String paytype) throws Exception;
    
	public List getAllPayTypes() throws Exception;
	
	public PayTypeMaster getPayTypeById(Integer id) throws Exception;
	/**
	 *  this api will return true ,if employee has failed payslip for the entered month,financialyear and paytype
	 * @param empid
	 * @param month
	 * @param year
	 * @param paytype
	 * @return
	 */
	public boolean isEmphasFailureForMonthFinyrPaytype(Integer empid,Integer month,Integer year,Integer paytype) throws Exception;
	
	/*
	 * Return total deduction amount for passing glcode,financial year
	 */
	public BigDecimal getTotalDeductionAmount(Integer employeeId, CChartOfAccounts accountCode, Date toDate) throws Exception;
	
	/*
	 * Return list of sanctioned advance amount in passing duration(not applicable for interest bearing advance)
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
	
	public List<HashMap> getEarningsAndDeductionsForEmpByMonthAndYearRange(Integer employeeid,Date fromDate,Date toDate) throws Exception;
	
	public List<HashMap> getDeductionsForEmployeeForFinYear(Integer employeeid,Integer finyearid) throws Exception;
	/**
	 * if the given payhead is gross pay based api will return true
	 * @param payhead
	 * @return
	 */
	 public boolean isPayHeadGrossBased(String payhead);
	
	/*
	 *	Return latest drawn payslip for passing employee  
	 */
	public EmpPayroll getLatestPayslipByEmp(PersonalInformation employee)throws Exception;
	
	/*
	 * Return last payscale for employee
	 */
	public PayScaleHeader getLastPayscaleByEmp(Integer empId)throws Exception;
	
	/**
	  * Get list of employee id who are normal eligible for pay 
	  * @param fromDate
	  * @param toDate
	  * @param deptId
	  * @param functionnaeryId
	  * @return listEmpId
	  */
	 public List<Integer> getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid,Integer functionaryId);
	 
	 
	 public List<Integer> getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid,Integer functionaryId,Long functionId, Integer billNumberId);
	 
	 /**
	  * Get list of employee id who are supplementary eligible for pay 
	  * @param fromdate
	  * @param todate
	  * @param deptid
	  * @param functionaryId
	  * @return
	  */	 
	 public List<Integer> getSuppPayEligibleEmpListBetweenFDateAndTDateByDept(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid,Integer functionaryId);
	 
	 public List<Integer> getSuppPayEligibleEmpListBetweenFDateAndTDateByDept(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid,Integer functionaryId,Long functionId, Integer billNumberId);
	 
	 public Boolean checkPayslipProcessAbilityByPayType(Integer empId,GregorianCalendar fromDate,GregorianCalendar toDate,String payType) throws Exception;
	 
	 /*
     * this api will return the emp code,emp name,emp id,emp designation name,emp department name,emp date of joing
     * separated by delimeter '~' for entered employee with in the given dates
     */
	 public String getEmpInfoByFDateAndTDate(Integer empid,GregorianCalendar fromdate,GregorianCalendar todate) throws Exception;
	    
	 public Assignment getAssignmentByDateOrByLast(Integer empId,Date date);
	 /*
	  * Return payscale based on grade and effective date
	 */
	@Deprecated
	public List getPayScaleByGradeAndEffectiveDate(Integer gradeId,Date effectiveDate)throws Exception;
	
	public List getPayScaleByEffectiveDate(Date effectiveDate)throws Exception;
	 
    /* returns the calculated basic amount from the current payslip 
     * 
     */
	public BigDecimal getBasicAmount(Set currEarningses);  
	
	public List<EmpPayroll> getAllPayslipByEmpMonthYearBasedOnDept(Integer deptId, Integer month, Integer year,Integer billNo) throws Exception;
	
	/**
	 * Get department payhead summary by date range
	 * @param FromDate 
	 * @param ToDate
	 * @param DeptId[]
	 */
	 public List<HashMap> getDeptPayheadSummary(Integer month,Integer finYear,Integer deptIds[],Integer billNumberId) throws Exception;
	 
	/**
	 * @param month
	 * @param year
	 * @param functionaryId
	 * @param deptId
	 * @return
	 * @throws Exception
	 */
	public List<HashMap> getFunctionaryDeptWisePayBillSummary(Integer month, Integer year, Integer functionaryId,Integer deptId) throws Exception;
	
	public List<HashMap> getFunctionaryPayheadSummary(Integer month, Integer year, Integer functionaryIds[],Integer billNumberId) throws Exception;
	
	public List<HashMap> getBankAdviceReportByBillIds(Integer[] billIds)throws Exception;
	public Set<Earnings> removeEarningsWithAmtZero(Set <Earnings> earningsSet);
}
