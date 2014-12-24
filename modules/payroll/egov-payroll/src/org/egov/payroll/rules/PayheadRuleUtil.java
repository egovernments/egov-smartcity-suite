package org.egov.payroll.rules;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.payroll.dao.BulkRuleMstrDAO;
import org.egov.payroll.dao.BulkRuleMstrHibernateDAO;
import org.egov.payroll.dao.EmpPayrollDAO;
import org.egov.payroll.dao.PayStructureDAO;
import org.egov.payroll.dao.PayrollDAOFactory;
import org.egov.payroll.model.Deductions;
import org.egov.payroll.model.Earnings;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.PayGenUpdationRule;
import org.egov.payroll.model.PayStructure;
import org.egov.payroll.model.PayheadRule;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.services.payslip.PayslipFailureException;
import org.egov.payroll.utils.PayrollExternalImpl;
import org.egov.payroll.workflow.payslip.PayslipService;
import org.egov.pims.empLeave.dao.AttendenceDAO;
import org.egov.pims.empLeave.dao.LeaveDAOFactory;
import org.egov.pims.model.EmployeeGroupMaster;
import org.egov.pims.model.PersonalInformation;

/**
 * @author surya
 */
public class PayheadRuleUtil {
	private static final Logger logger = Logger.getLogger(PayheadRuleUtil.class);
	private static PersistenceService persistenceService;
	public static BigDecimal computeRuleBasedEarning(EmpPayroll currPayslip,Earnings currEarning,EmpPayroll lastPayslip, Set<Earnings> earningsSet, PayheadRule payheadRule,PayStructure payStructure) throws Exception{
		currPayslip.setEarningses(earningsSet);
		PayheadRuleUtil payheadRuleUtil = new PayheadRuleUtil();
		BigDecimal calcEarnAmt=BigDecimal.ZERO;				

		logger.info("Before change------------"+currEarning.getAmount());		
		if(payheadRule.getRuleScript()!=null && !payheadRule.getRuleScript().getName().equals(""))
		{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			
			String actionName = payheadRule.getRuleScript().getName();
			String scriptName = "Payroll.PayHeadRule."+actionName;
			Script script =empPayrollDAO.getScript(scriptName);

			if(script ==null){
				
				throw new PayslipFailureException("Script not found for "+scriptName);
				
			}else{
				try{				
					//calcEarnAmt =new BigDecimal( script.eval(Script.createContext("currPayslip",currPayslip,"lastPayslip",lastPayslip,"currEarning",currEarning,"earningsSet",earningsSet,"payheadRuleUtil",payheadRuleUtil,"payStructure",payStructure)).toString());
					//TODO - commenting above line for time being and need to fix Script.createContext
			    }catch (ValidationException e) {
			    	String errorMsg = "";
			    	for(ValidationError vr : e.getErrors()){
			    		errorMsg += vr.getMessage(); 				  
			    	}
			    	throw new PayslipFailureException(errorMsg);
			    }
			}
		}
		
		logger.info("After change------------"+calcEarnAmt);
		
		return calcEarnAmt;
	}
	
	
	
	public static Double computeRuleBasedDeduction(EmpPayroll currPayslip,Deductions currDed ,EmpPayroll lastPayslip, PayheadRule payheadRule,PayStructure payStructure ) throws Exception
	{
		Double calcDedAmt = 0.0;
		PayslipService payslipService = new PayslipService();
		payslipService.setSessionFactory(new SessionFactory());
	    payslipService.setPayrollExternalInterface(new PayrollExternalImpl());
	    PayheadRuleUtil payheadRuleUtil = new PayheadRuleUtil();	   
		Integer empid = null;
		if(null!=payheadRule && !"".equals(payheadRule.getRuleScript().getName()))
		{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			String actionName = payheadRule.getRuleScript().getName();
			String scriptName = "Payroll.PayHeadRule."+actionName;
			Script script =empPayrollDAO.getScript(scriptName);
			if(script ==null){
				
				throw new PayslipFailureException("Script not found for "+scriptName);
				
			}else{
				try{					
					//Object obj=script.eval(Script.createContext("empPayslipForSixMonths",empPayslipForSixMonths,"currDed",currDed,"currPayslip",currPayslip,"payslipService",payslipService));
					//Object obj=script.eval(Script.createContext("currPayslip",currPayslip,"lastPayslip",lastPayslip,"payslipService",payslipService,"payheadRuleUtil",payheadRuleUtil,"payStructure",payStructure));
					//TODO - commenting above line for time being and need to fix Script.createContext
					Object obj=script.getCompiledScript();
					
					String strScript = "";
					if(obj!=null)
					{
						strScript = obj.toString();
					}
					if(!"".equals(strScript))
					{
					  calcDedAmt =new Double(strScript);
					}
			    }catch (ValidationException e) {
			    	String errorMsg = "";
			    	for(ValidationError vr : e.getErrors()){
			    		errorMsg += vr.getMessage(); 				  
			    	}
			    	throw new PayslipFailureException(errorMsg);
			    }
			}

		}
		logger.info("amt Ded from script--"+calcDedAmt);
		return calcDedAmt;
	}
	/**
	 * getting earning object from payslip by earnig
	 * @throws EGOVException 
	 */
	public Earnings getEarningByNameFormPayslip(EmpPayroll payslip, String payheadname) {
		for(Earnings earning : payslip.getEarningses()){
			if(payheadname.equalsIgnoreCase(earning.getSalaryCodes().getHead())){
				return earning; 
			}
		}		
		return null;
	}
	
	public Deductions getDeductionByNameFormPayslip(EmpPayroll payslip, String payheadname) {
		for(Deductions ded : payslip.getDeductionses()){
			if(payheadname.equalsIgnoreCase(ded.getSalaryCodes().getHead())){
				return ded; 
			}
		}		
		return null;
	}
	
	/**
	 * Get payhead rule by salarycode and effectiveDate
	 */
	public static PayheadRule getPayheadRule(SalaryCodes salarycode, Date effectiveFrom){		
		return PayrollDAOFactory.getDAOFactory().getPayheadRuleDAO().getEffectivePayheadRule(salarycode, effectiveFrom);
	}
	
	/**
	 * Get calculated earing amount by attendance
	 */
	public BigDecimal getCalculatedAmountbasedOnAttendance(BigDecimal amount, EmpPayroll currPayslip){
		double calculatedAmt = (amount.doubleValue()*currPayslip.getNumdays())/currPayslip.getWorkingDays();		
		return new BigDecimal(calculatedAmt);
	}

	/**
	 * Returns List of dates of Overtime for an employee between two given dates.
	 * @param fromDate
	 * @param toDate
	 * @param personalInformation
	 * @return
	 */
	public static List getListOfOverTimeForAnEmployeeBetweenDates(java.util.Date fromDate,java.util.Date toDate,PersonalInformation personalInformation)
	{		
		AttendenceDAO attendenceDAO = LeaveDAOFactory.getDAOFactory().getAttendenceDAO();
		List list=attendenceDAO.getListOfOverTimeForAnEmployeeBetweenDates(fromDate,toDate, personalInformation);
		return list;
	}

	/**
	 * Returns employee's current paystructure.
	 * @param empid
	 * @return
	 */
	private static PayStructure getCurrentPayStructureForEmp(Integer empid)
	{
		PayStructure payStructure = null;
		if(empid != null)
		{
			PayStructureDAO payStructureDAO = PayrollDAOFactory.getDAOFactory().getPayStructureDAO();
			payStructure  = (PayStructure) payStructureDAO.getCurrentPayStructureForEmp(empid);	
		}
		return payStructure;
	}
	
	public BigDecimal getPercentagebyBulkRule(String payheadname,Date payslipGenDate,Integer empGrpMstrs)
	{		
		SalaryCodes salaryCodes=null;
		PayGenUpdationRule payGenUpdationRule = null;
		double percentage=0.0;
		if(payslipGenDate != null)
		{
			BulkRuleMstrDAO ruleMstrDAO = PayrollDAOFactory.getDAOFactory().getPayGenRuleUpdation();
			salaryCodes = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO().getSalaryCodesByHead(payheadname);
			if(salaryCodes != null)
			{
				payGenUpdationRule = (PayGenUpdationRule)ruleMstrDAO.getLatestRulemasterAsOnDate(salaryCodes.getId(),payslipGenDate,empGrpMstrs);
				logger.info("payGenUpdationRule = "+payGenUpdationRule);
			}
			if(payGenUpdationRule != null && payGenUpdationRule.getPercentage() != null)
			{
				percentage=payGenUpdationRule.getPercentage().doubleValue();
				logger.info("percentage = "+percentage);
			}
		}
		return (new BigDecimal(percentage));
	}
	
	public PersistenceService getPersistenceService() {
		return persistenceService;
	}



	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
}
