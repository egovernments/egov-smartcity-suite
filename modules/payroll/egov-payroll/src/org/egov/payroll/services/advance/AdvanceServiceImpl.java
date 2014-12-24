package org.egov.payroll.services.advance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.infstr.workflow.Action;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.ScriptService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.model.advance.EgAdvanceReqPayeeDetails;
import org.egov.model.advance.EgAdvanceRequisitionDetails;
import org.egov.model.advance.EgAdvanceRequisitionMis;
import org.egov.payroll.dao.AdvanceDAO;
import org.egov.payroll.dao.PayrollDAOFactory;
import org.egov.payroll.dao.SalaryCodesDAO;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.AdvanceSchedule;
import org.egov.payroll.model.Deductions;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.SalaryARF;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.model.Assignment;


public class AdvanceServiceImpl implements AdvanceService {

	private final static Logger LOGGER=Logger.getLogger(AdvanceServiceImpl.class);
	private final static String CREATEDSALADVANCES="pay-createdSalAdvances";
	private final static String ARF_APPROVED_CODE = "Approved";

	

	public List<SalaryCodes> getSalaryCodesByCategoryName(String categoryName)throws EGOVRuntimeException{
		SalaryCodesDAO salarycodeDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
		return salarycodeDAO.getSalaryCodesByCategoryName(categoryName);
	}

	public List<SalaryCodes> getSalaryCodesByCategoryNames(String categoryName1, String categoryName2)throws EGOVRuntimeException{
		SalaryCodesDAO salarycodeDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
		return salarycodeDAO.getSalaryCodesByCategoryNames(categoryName1, categoryName2);
	}

	public Advance createSaladvance(Advance saladvance,ScriptService scriptService)throws EGOVRuntimeException{
		AdvanceDAO saladvanceDAO = PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO();
		EgovMasterDataCaching.getInstance().removeFromCache("pay-salAdvances");
		EgovMasterDataCaching.getInstance().removeFromCache(CREATEDSALADVANCES);
		
		//check if the advance rule permits creation of this advance
		if (!isValidAdvance(saladvance, scriptService)) {
			throw new EGOVRuntimeException("User is not eligible for this advance. Please contact concerned authority");
		}
		//To set the salaryARF values
		try {
			saladvance.setSalaryARF(generateAdvanceRequisitionForAdvance(saladvance,scriptService));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (Advance)saladvanceDAO.create(saladvance);
	}

	/**
	 * create advance for bankLoan
	 * @param saladvance
	 * @return Advance
	 * @throws EGOVRuntimeException
	 */
	public Advance createSaladvanceForBankLoan(Advance saladvance,ScriptService scriptService)throws EGOVRuntimeException{
		AdvanceDAO saladvanceDAO = PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO();
		EgovMasterDataCaching.getInstance().removeFromCache("pay-salAdvances");
		EgovMasterDataCaching.getInstance().removeFromCache(CREATEDSALADVANCES);
		//check if the advance rule permits creation of this advance
		if (!isValidAdvance(saladvance, scriptService)) {
			throw new EGOVRuntimeException("User is not eligible for this advance. Please contact concerned authority");
		}
		return (Advance)saladvanceDAO.create(saladvance);
	}
	public List<Advance> getSalaryadvancesByEmp(Integer empId )throws EGOVRuntimeException{
		AdvanceDAO saladvanceDAO = PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO();
		return saladvanceDAO.getAdvancesByEmpId(empId);
	}
	public List<Advance> getAllSalaryadvances()throws EGOVRuntimeException{
		AdvanceDAO saladvanceDAO = PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO();
		return saladvanceDAO.getAllSaladvances();
	}
	public List<Advance> getAllSalaryadvancesByStatus(EgwStatus status)throws EGOVRuntimeException{
		AdvanceDAO saladvanceDAO = PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO();
		return saladvanceDAO.getAllSalaryadvancesbyStatus(status);
	}

	public List<Advance> getAllSalAdvancesByEmpAndStatus(Integer empId,EgwStatus status)throws EGOVRuntimeException{
		AdvanceDAO salaAdvDAO = PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO();
		return salaAdvDAO.getAllSalAdvancesByEmpAndStatus(empId, status);
	}

	public Advance getSalaryadvanceById(Long id)throws EGOVRuntimeException{
		AdvanceDAO saladvanceDAO = PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO();
		return (Advance)saladvanceDAO.findById(id, false);
	}
	public Advance updateSalaryadvance(Advance salaryadvance)throws EGOVRuntimeException{
		AdvanceDAO saladvanceDAO = PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO();
		EgovMasterDataCaching.getInstance().removeFromCache("pay-salAdvances");
		EgovMasterDataCaching.getInstance().removeFromCache(CREATEDSALADVANCES);
		
		if(!PayrollConstants.Deduction_BankLoan.equals(salaryadvance.getSalaryCodes().getCategoryMaster().getName())){
			modifyAdvanceRequisitionForAdvance(salaryadvance.getSalaryARF());
		}
		return (Advance)saladvanceDAO.update(salaryadvance);
	}

	protected SalaryCodes getSalaryCodesById(SalaryCodesDAO salaryCodeDao, Integer id)throws EGOVRuntimeException{
		return (SalaryCodes)salaryCodeDao.findById(id, false);		
	}
	
	public SalaryCodes getSalaryCodesById(Integer id)throws EGOVRuntimeException
	{
			//SalaryCodesDAO salarycodeDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
			return getSalaryCodesById(PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO(), id);
	}

	public List<Advance> getAllCreatedSalAdvancesCache()throws EGOVRuntimeException{
		List<Advance> advances = new ArrayList<Advance>();
		advances = EgovMasterDataCaching.getInstance().get(CREATEDSALADVANCES);
		return advances;
	}

	public List<Advance> getAllCreatedSalAdvancesByEmpCache(Integer empId)throws EGOVRuntimeException{
		List<Advance> advances = new ArrayList<Advance>();
		List<Advance> tempAdvances = getAllCreatedSalAdvancesCache();
		for(Advance tempAdv : tempAdvances ){
			if(tempAdv.getEmployee().getIdPersonalInformation().equals(empId)){
				advances.add(tempAdv);
			}
		}
		return advances;
	}
	public List<Advance> getAllEligibleAdvancesForEmp(Integer empId)throws EGOVRuntimeException
	{
		AdvanceDAO advanceDAO = PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO();
		List<Advance> advances ;
		advances = advanceDAO.getAllEligibleAdvancesForEmp(empId);
		return advances;
	}

	public List<String> getListOfRepaidAdvanceAmount(Integer idEmployee, CChartOfAccounts accountCode, Date fromDate, Date toDate)throws EGOVRuntimeException{
			AdvanceDAO advDAO = PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO();
			return advDAO.getListOfRepaidAdvanceAmount(idEmployee, accountCode, fromDate, toDate);
	}

	public List<String> getListOfSanctionedAdvanceAmount(Integer idEmployee, CChartOfAccounts accountCode, Date fromDate, Date toDate)throws EGOVRuntimeException{
			AdvanceDAO advDAO = PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO();
			return advDAO.getListOfSanctionedAdvanceAmount(idEmployee, accountCode, fromDate, toDate);
	}

	/**
	 * @param advance
	 * @param scriptService
	 * @return
	 * @throws Exception
	 */
	private SalaryARF generateAdvanceRequisitionForAdvance(Advance advance,ScriptService scriptService) throws Exception,EGOVRuntimeException
	{
		SalaryARF salARF = new SalaryARF();
		
		PayrollExternalInterface payrollExternalInterface=PayrollManagersUtill.getPayrollExterInterface();
		Assignment empAssignment = payrollExternalInterface.getLatestAssignmentForEmployee(advance.getEmployee().getIdPersonalInformation());
		
		salARF.setAdvance(advance);
		salARF.setAdvanceRequisitionNumber(getArfNumber(advance,scriptService));
		salARF.setAdvanceRequisitionDate(new Date());
		salARF.setAdvanceRequisitionAmount(advance.getAdvanceAmt());
		salARF.setArftype("Employee");

		salARF.setNarration("Advance for employee");
		
		EgwStatus status = payrollExternalInterface.getStatusByModuleAndDescription("ARF", "Created");
		salARF.setStatus(status);

		EgAdvanceRequisitionMis advanceReqMis = new EgAdvanceRequisitionMis();
		
		advanceReqMis.setEgAdvanceRequisition(salARF);
		advanceReqMis.setEgDepartment((DepartmentImpl)empAssignment.getDeptId());
		advanceReqMis.setFunctionaryId(empAssignment.getFunctionary());
		advanceReqMis.setFund(empAssignment.getFundId());
		advanceReqMis.setPayto(""+advance.getEmployee().getEmployeeName().toString());
		salARF.setEgAdvanceReqMises(advanceReqMis);
		
		//Populating advance requistion details for advance account debiting
		EgAdvanceRequisitionDetails advanceRequisitionDtls = new EgAdvanceRequisitionDetails();
		
		CChartOfAccounts advanceAccount = null;
		if(advance.getSalaryCodes().getChartofaccounts() != null){
			advanceAccount = advance.getSalaryCodes().getChartofaccounts();
		}
		else if(advance.getSalaryCodes().getTdsId() != null){
			advanceAccount = advance.getSalaryCodes().getTdsId().getChartofaccounts();
		}
		
		advanceRequisitionDtls.setChartofaccounts(advanceAccount);
		advanceRequisitionDtls.setDebitamount(advance.getAdvanceAmt());
		advanceRequisitionDtls.setEgAdvanceRequisition(salARF);
		advanceRequisitionDtls.setNarration("Debit Narration");
		advanceRequisitionDtls.setLastupdatedtime(new Date());
		advanceRequisitionDtls.setFunction(empAssignment.getFunctionId());
		
		Accountdetailtype accountDetailTypeEmp = payrollExternalInterface.getAccountdetailtypeByName(PayrollConstants.EMPLOYEE_MODULE);
		Accountdetailtype advanceAccountDetailType = payrollExternalInterface.getAccountDetailTypeIdByName(advanceAccount.getGlcode().toString(),accountDetailTypeEmp.getName().toString());
		if(advanceAccountDetailType!=null && advanceAccountDetailType.getId() != 0){
			EgAdvanceReqPayeeDetails advanceReqPayeeDtls = new EgAdvanceReqPayeeDetails();
			advanceReqPayeeDtls.setAccountdetailKeyId(advance.getEmployee().getIdPersonalInformation());
			advanceReqPayeeDtls.setAccountDetailType(advanceAccountDetailType);
			advanceReqPayeeDtls.setDebitAmount(advance.getAdvanceAmt());

			if(advance.getSalaryCodes().getTdsId()!=null)
			{
				advanceReqPayeeDtls.setRecovery(advance.getSalaryCodes().getTdsId());
			}
			advanceReqPayeeDtls.setNarration(advanceRequisitionDtls.getNarration());
			advanceReqPayeeDtls.setEgAdvanceRequisitionDetails(advanceRequisitionDtls);
			advanceRequisitionDtls.getEgAdvanceReqpayeeDetailses().add(advanceReqPayeeDtls);
		}
		salARF.getEgAdvanceReqDetailses().add(advanceRequisitionDtls);
		
		return salARF;
		
	}
	
	/**
	 * @param salARF
	 */
	private void modifyAdvanceRequisitionForAdvance(SalaryARF salARF)
	{
		try {

			salARF.setAdvanceRequisitionAmount(salARF.getAdvance().getAdvanceAmt());
			
			for(Iterator<EgAdvanceRequisitionDetails> advanceReqDtlsIter = salARF.getEgAdvanceReqDetailses().iterator(); advanceReqDtlsIter.hasNext(); )
			{
				EgAdvanceRequisitionDetails advanceReqDetails = (EgAdvanceRequisitionDetails)advanceReqDtlsIter.next();

				advanceReqDetails.setDebitamount(salARF.getAdvance().getAdvanceAmt());
				for(Iterator<EgAdvanceReqPayeeDetails> advReqPayeeDtlsItr = advanceReqDetails.getEgAdvanceReqpayeeDetailses().iterator(); advReqPayeeDtlsItr.hasNext(); ){
					EgAdvanceReqPayeeDetails advReqPayeeDtls = (EgAdvanceReqPayeeDetails)advReqPayeeDtlsItr.next();
					advReqPayeeDtls.setDebitAmount(salARF.getAdvance().getAdvanceAmt());
				}
			}

		} catch (NumberFormatException e) {
			LOGGER.error(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	public String getArfNumber(Advance advance,ScriptService scriptService) throws EGOVRuntimeException
	{
		List<Script> scripts = scriptService.findAllByNamedQuery("SCRIPT",PayrollConstants.PAYROLL_ARF_NUMBER_GENERATER_SCRIPT);
		if(scripts==null || scripts.size()==0)
			throw new EGOVRuntimeException("There is no script to generate ARF No");
		else
		{
			return (String)scriptService.executeScript(PayrollConstants.PAYROLL_ARF_NUMBER_GENERATER_SCRIPT,ScriptService.createContext("advanceHeader",advance) );
		}
	}
	
	/**
	 *	get advance scheduler by month,financials year for a particular advance
	 *@param Advance
	 *@param month
	 *@param finYear
	 *@return AdvanceSchedule
	 */
	public AdvanceSchedule getAdvSchedulerByMonthYear(Advance adv, BigDecimal month, CFinancialYear finYear){
		return PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO().getAdvSchedulerByMonthYear(adv, month, finYear);
	}
	
	public void cancelAdvanceEmiIfPayslipReject(EmpPayroll payslip){
		for(Deductions tempDed : payslip.getDeductionses()){
			if(tempDed.getSalAdvances() != null){
				tempDed.getSalAdvances().setPendingAmt(tempDed.getSalAdvances().getPendingAmt().add(tempDed.getAmount()));
				if(tempDed.getAdvanceScheduler() != null){
					tempDed.getAdvanceScheduler().setRecover(null);
				}
			}
		}
	}
	
	/**
	 * Get correct advance  schedule which is to be recovered  
	 */
	public AdvanceSchedule getRecoverableScheduler(Advance adv){
		return PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO().getRecoverableScheduler(adv);
	}
	
	/**
	 * Get advance schedule by id   
	 */
	public AdvanceSchedule getAdvSchedulerById(Integer advSchedulerId){
		return PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO().getAdvSchedulerById(advSchedulerId);
	}
	
	/**
	 * This API executes the validation rule, if present, associated with an advance.
	 * The rule typically checks if the employee is eligible for advance or not
	 * @param advance
	 * @param scriptService
	 * @return true or false depending on the validation rule
	 */
	public Boolean isValidAdvance(Advance advance, ScriptService scriptService) {
		if (advance.getSalaryCodes().getValidationRuleScript() != null) {
			Action action = advance.getSalaryCodes().getValidationRuleScript();
			String scriptName = action.getType() + "." + action.getName();
			Object result = scriptService.executeScript(scriptName, ScriptService.createContext("advance", advance, "advanceManagerBean", this));
			if (result instanceof Boolean) {
				return (Boolean)result;
			} else if (result instanceof String) {
				return Boolean.valueOf((String)result);
			} else
				return false;
		}
		return true;
	}

}
