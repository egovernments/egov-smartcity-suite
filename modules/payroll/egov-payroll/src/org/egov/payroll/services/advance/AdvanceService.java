package org.egov.payroll.services.advance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.infstr.services.ScriptService;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.AdvanceSchedule;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.SalaryCodes;

public interface AdvanceService {

	public List<SalaryCodes> getSalaryCodesByCategoryName(String categoryName);
	public List<SalaryCodes> getSalaryCodesByCategoryNames(String categoryName1, String categoryName2);
	public Advance createSaladvance(Advance saladvance,ScriptService scriptService);
	/**
	 * create advance for bankLoan
	 * @param saladvance
	 * @return Advance
	 */
	public Advance createSaladvanceForBankLoan(Advance saladvance,ScriptService scriptService);
	public List<Advance> getSalaryadvancesByEmp(Integer empId );
	public List<Advance> getAllSalaryadvances();
	public List<Advance> getAllSalaryadvancesByStatus(EgwStatus status);
	public List<Advance> getAllSalAdvancesByEmpAndStatus(Integer empId,EgwStatus status);
	public Advance getSalaryadvanceById(Long id);
	public Advance updateSalaryadvance(Advance salaryadvance);
	public SalaryCodes getSalaryCodesById(Integer id);
	public List<Advance> getAllCreatedSalAdvancesCache();
	public List<Advance> getAllCreatedSalAdvancesByEmpCache(Integer empId);	
	public List<Advance> getAllEligibleAdvancesForEmp(Integer empId);

	/*
	 * Return list of repaid advance amount in passing duration(not applicable for interest bearing advance)
	 */
	public List<String> getListOfRepaidAdvanceAmount(Integer idEmployee, CChartOfAccounts accountCode, Date fromDate, Date toDate);
	
	/*
	 * Return list of sanctioned advance amount in passing duration(not applicable for interest bearing advance)
	 */
	public List<String> getListOfSanctionedAdvanceAmount(Integer idEmployee, CChartOfAccounts accountCode, Date fromDate, Date toDate);
		
	/**
	 *	get advance scheduler by month,financials year for a particular advance
	 *@param Advance
	 *@param month
	 *@param finYear
	 *@return AdvanceSchedule 
	 */
	public AdvanceSchedule getAdvSchedulerByMonthYear(Advance adv, BigDecimal month, CFinancialYear finYear);
	
	/**
	 * Cancel advance EMI deduction if we reject payslip
	 */
	public void cancelAdvanceEmiIfPayslipReject(EmpPayroll payslip);
	
	/**
	 * Get advance correct schedule which is to be recovered  
	 */
	public AdvanceSchedule getRecoverableScheduler(Advance adv);
	
	/**
	 * Get advance correct schedule which is to be recovered  
	 */
	public AdvanceSchedule getAdvSchedulerById(Integer advSchedulerId);
	
	/**
	 * This API executes the validation rule, if present, associated with an advance.
	 * The rule typically checks if the employee is eligible for advance or not
	 * @param advance
	 * @param scriptService
	 * @return true or false depending on the validation rule
	 */
	public Boolean isValidAdvance(Advance advance, ScriptService scriptService);
}
