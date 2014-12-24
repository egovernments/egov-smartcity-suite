package org.egov.payroll.services.payslip;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.recoveries.EgDeductionDetails;
import org.egov.payroll.dao.AdvanceDAO;
import org.egov.payroll.dao.BatchFailureDetailsDAO;
import org.egov.payroll.dao.BatchGenDetailsDAO;
import org.egov.payroll.dao.EmpPayrollDAO;
import org.egov.payroll.dao.ExceptionDAO;
import org.egov.payroll.dao.IncrementDetailsDAO;
import org.egov.payroll.dao.PayScaleDetailsDAO;
import org.egov.payroll.dao.PayScaleHeaderDAO;
import org.egov.payroll.dao.PayStructureDAO;
import org.egov.payroll.dao.PayTypeMasterDAO;
import org.egov.payroll.dao.PayrollDAOFactory;
import org.egov.payroll.dao.SalaryCodesDAO;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.AdvanceSchedule;
import org.egov.payroll.model.BatchGenDetails;
import org.egov.payroll.model.Deductions;
import org.egov.payroll.model.Earnings;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.IncrementDetails;
import org.egov.payroll.model.IncrementSlabsForPayScale;
import org.egov.payroll.model.PayGenUpdationRule;
import org.egov.payroll.model.PayScaleDetails;
import org.egov.payroll.model.PayScaleHeader;
import org.egov.payroll.model.PayStructure;
import org.egov.payroll.model.PayTypeMaster;
import org.egov.payroll.model.PayheadRule;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.rules.PayheadRuleUtil;
import org.egov.payroll.services.payhead.PayheadService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.payroll.utils.PayslipAlreadyExistException;
import org.egov.pims.empLeave.model.EmployeeAttendenceReport;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeServiceImpl;
import org.hibernate.Query;






public class PayRollServiceImpl implements PayRollService{

	private static final Logger logger = Logger.getLogger(PayRollServiceImpl.class);
	private static final String CLOSEDSTR ="Closed";
	private static final String MONTHLYFLATERATESTR ="MonthlyFlatRate";
	private static final String SALARYADVANCESTR ="Salaryadvance";
	private static final String SLABBASEDSTR ="SlabBased";
	private static final String DATEFORMATESTR1 = "dd/MM/yyyy";
	private static final String DATEFORMATESTR2 = "dd-MMM-yyyy";	
	

	private PayrollExternalInterface getPayrollExterInterface(){
		PayrollExternalInterface payrollExternalInterface = PayrollManagersUtill.getPayrollExterInterface();
		/*RecoveryService recoveryService = new RecoveryService();
		recoveryService.setType(Recovery.class);
		recoveryService.setSessionFactory(new SessionFactory());
		recoveryService.setEgDeductionDetHibernateDao(new EgDeductionDetailsHibernateDAO(EgDeductionDetails.class,recoveryService.getSession()));
		recoveryService.setTdsHibernateDAO(new TdsHibernateDAO(Recovery.class,recoveryService.getSession()));
		payrollExternalImpl.setRecoveryService(recoveryService);*/
		return payrollExternalInterface;
	}

	public void createPayslip(EmpPayroll payslip) throws Exception{
		try{
			//EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			if(PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL.equals(payslip.getPayType().getPaytype()) || PayrollConstants.EMP_PAYSLIP_PAYTYPE_EXCEPTION.equals(payslip.getPayType().getPaytype()) ){
				EmpPayroll existPayslip = getPayslipForEmpByMonthAndYear(payslip.getEmployee().getIdPersonalInformation(),payslip.getMonth().intValue(),payslip.getFinancialyear().getId().intValue(),payslip.getPayType().getId());
				if(existPayslip != null){
					throw new PayslipAlreadyExistException("Payslip already there");
				}
			}
			payslip.setNetPay();
			//EmpPayroll tempPayslip = (EmpPayroll)empPayrollDAO.create(payslip);
			//HibernateUtil.getCurrentSession().flush();
			//createWorkFlow(tempPayslip);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			logger.debug("sdatest---------------");
			logger.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public void deleteEarnings(Earnings earnings){
		try{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			empPayrollDAO.delete(earnings);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}
	public void deleteDeductions(Deductions deductions){
		try{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			empPayrollDAO.delete(deductions);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}
	public void deletePayslip(EmpPayroll paySlip)
	{
		try{
			EgovMasterDataCaching.getInstance().removeFromCache("pay-salAdvances");
			EgovMasterDataCaching.getInstance().removeFromCache("pay-createdSalAdvances");
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			empPayrollDAO.delete(paySlip);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}
	public void updatePayslip(EmpPayroll paySlip){
		try{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			paySlip.setNetPay();
			empPayrollDAO.update(paySlip);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}
	public List getSalaryCodesByCategoryId(Integer categoryId)
	{
		try{
			List salaryCodesList=new ArrayList();
			SalaryCodesDAO salaryCodesDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
			salaryCodesList = (ArrayList) salaryCodesDAO.getSalaryCodesByCategoryId(categoryId);
			return(salaryCodesList);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public SalaryCodes getSalaryCodesById(Integer Id)
	{
		try{
			SalaryCodesDAO salaryCodesDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
			return (SalaryCodes)salaryCodesDAO.findById(Id,false);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public Advance getAdvanceById(Long Id)
	{
		try{
			AdvanceDAO advancesDAO = PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO();
			return (Advance)advancesDAO.findById(Id,false);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public List getAdvancesByEmpId(Integer employeeId)
	{
		try{
			List advancesList=new ArrayList();
			AdvanceDAO advancesDAO = PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO();
			advancesList  = (ArrayList) advancesDAO.getAdvancesByEmpId(employeeId);
			return(advancesList);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public List getPayStructureByEmp(Integer empId)
	{
		try{
			List payStructureList=new ArrayList();
			PayStructureDAO payStructureDAO = PayrollDAOFactory.getDAOFactory().getPayStructureDAO();
			payStructureList  = (ArrayList) payStructureDAO.getPayStructureByEmp(empId);
			return(payStructureList);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	public EmpPayroll getPayslipForEmpByMonthAndYear(Integer empId,Integer month,Integer year) throws Exception{
		try{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();

			return empPayrollDAO.getPayslipForEmpByMonthAndYear(empId, month, year);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	public List<EmpPayroll> getAllPayslipByEmpMonthYear(Integer empId, Integer month, Integer year){
		try{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			return empPayrollDAO.getAllPayslipByEmpMonthYear(empId, month, year);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}


	public EmpPayroll getPayslipForEmpByMonthAndYear(Integer empId,Integer month,Integer year,Integer paytype){
		try{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			return empPayrollDAO.getPayslipForEmpByMonthAndYear(empId, month, year,paytype);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	public EmpPayroll getPayslipForEmpByMonthAndYearWithYTD(Integer empId,Integer month,Integer year){
		try{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			EmpPayroll empPayroll = empPayrollDAO.getPayslipForEmpByMonthAndYear(empId, month, year);

			empPayroll = populateYTDForPayslip(empPayroll);
			return empPayroll;
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	public EmpPayroll getPayslipByIdWithYTD(Long payslipId){
		try{
			//EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			EmpPayroll empPayroll = getPayslipById(payslipId);
			empPayroll = populateYTDForPayslip(empPayroll);
			return empPayroll;
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	public List getOrderedSalaryCodes()
	{
		try{
			List salCodesList=new ArrayList();
			SalaryCodesDAO salaryCodesDAO = PayrollDAOFactory.getDAOFactory().getSalaryCodesDAO();
			salCodesList = (ArrayList) salaryCodesDAO.getSalaryCodesByOrderId();
			return(salCodesList);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public List getAllAdvancesByStatus(EgwStatus status)
	{
		try{
			List salAdvList=new ArrayList();
			AdvanceDAO salAdvancesDAO = PayrollDAOFactory.getDAOFactory().getSalAdvancesDAO();
			salAdvList = (ArrayList) salAdvancesDAO.getAllAdvancesByStatus(status);
			return(salAdvList);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public List getAllPrevPayslipsForEmpByMonthAndYear(Integer empId,Integer month,Integer year)
	{
		try{
			List empPayrollList=new ArrayList();
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			empPayrollList = (ArrayList) empPayrollDAO.getAllPrevPayslipsForEmpByMonthAndYear(empId,month,year);
			return(empPayrollList);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public PayStructure getPayStructureById(Integer Id)
	{
		try{
			PayStructureDAO payStructureDAO = PayrollDAOFactory.getDAOFactory().getPayStructureDAO();
			return payStructureDAO.getPayStructureById(Id);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public PayScaleHeader getPayScaleHeaderById(Integer Id)
	{
		try{
			PayScaleHeaderDAO payScaleHeadersDAO = PayrollDAOFactory.getDAOFactory().getPayScaleHeaderDAO();
			return payScaleHeadersDAO.getPayScaleHeaderById(Id);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public List getAllPayScaleHeaders()
	{
		try{
			List payScaleHeaderList=new ArrayList();
			PayScaleHeaderDAO payScaleHeadersDAO = PayrollDAOFactory.getDAOFactory().getPayScaleHeaderDAO();
			payScaleHeaderList=(ArrayList) payScaleHeadersDAO.findAll();
			return(payScaleHeaderList);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public List getAllExceptionsInStatusByMonthAndYear(Integer empId,Integer year,EgwStatus status)
	{
		try{
			List empPayrollList=new ArrayList();
			ExceptionDAO exceptionDAO = PayrollDAOFactory.getDAOFactory().getExceptionDAO();
			empPayrollList = (ArrayList) exceptionDAO.getAllExceptionsInStatusByMonthAndYear(empId,year,status);
			return(empPayrollList);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public void createPayHeader(PayScaleHeader payHeader){
		try{
			PayScaleHeaderDAO payScaleHeadersDAO = PayrollDAOFactory.getDAOFactory().getPayScaleHeaderDAO();
			payScaleHeadersDAO.create(payHeader);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			logger.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}
	public void updatePayHeader(PayScaleHeader payHeader){
		try{
			PayScaleHeaderDAO payScaleHeadersDAO = PayrollDAOFactory.getDAOFactory().getPayScaleHeaderDAO();
			payScaleHeadersDAO.update(payHeader);
		} catch (Exception e) {
			logger.error(e.getMessage());
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}
	public void createPayStructure(PayStructure payStructure){
		PayStructureDAO payStructureDAO = PayrollDAOFactory.getDAOFactory().getPayStructureDAO();
		payStructureDAO.create(payStructure);

	}
	public void updatePayStructure(PayStructure payStructure){
		PayStructureDAO payStructureDAO = PayrollDAOFactory.getDAOFactory().getPayStructureDAO();
		payStructureDAO.update(payStructure);

	}
	public void deletePayStructure(PayStructure payStructure){
		PayStructureDAO payStructureDAO = PayrollDAOFactory.getDAOFactory().getPayStructureDAO();
		payStructureDAO.delete(payStructure);

	}
	public PayScaleHeader getPayScaleHeaderByName(String name){
		PayScaleHeaderDAO payScaleHeadersDAO = PayrollDAOFactory.getDAOFactory().getPayScaleHeaderDAO();
		return payScaleHeadersDAO.getPayScaleHeaderByName(name);
	}
	public PayScaleHeader getPayScaleHeaderForEmp(Integer Id)
	{
		PayScaleHeaderDAO payScaleHeadersDAO = PayrollDAOFactory.getDAOFactory().getPayScaleHeaderDAO();
		return payScaleHeadersDAO.getPayScaleHeaderForEmp(Id);
	}
	public void deletePayScaleDetails(PayScaleDetails payScaleDetails){
		PayScaleDetailsDAO payScaleDetailsDAO = PayrollDAOFactory.getDAOFactory().getPayScaleDetailsDAO();
		payScaleDetailsDAO.delete(payScaleDetails);

	}
	public void deleteIncrSlabDetails(IncrementSlabsForPayScale incrSlabDetails){
		PayScaleDetailsDAO payScaleDetailsDAO = PayrollDAOFactory.getDAOFactory().getPayScaleDetailsDAO();
		payScaleDetailsDAO.delete(incrSlabDetails);

	}
	public PayStructure getCurrentPayStructureForEmp(Integer empid)
	{
		PayStructure payStructure;
		PayStructureDAO payStructureDAO = PayrollDAOFactory.getDAOFactory().getPayStructureDAO();
		payStructure  = (PayStructure) payStructureDAO.getCurrentPayStructureForEmp(empid);
		return(payStructure);
	}

	/*  protected PayStructure getPayStructureForEmpByDate(PayStructureDAO payStructureDAO, Integer empid,Date date)
	  {
		  try{
		    PayStructure payStructure;
			PayStructureDAO payStructureDAO = PayrollDAOFactory.getDAOFactory().getPayStructureDAO();
			payStructure  = payStructureDAO.getPayStructureForEmpByDate(empid,date);
			return(payStructure);
		  } catch (RuntimeException e) {
	          HibernateUtil.rollbackTransaction();
	          throw new EGOVRuntimeException(e.getMessage(),e);
	      }
	  }*/

	public PayStructure getPayStructureForEmpByDate(Integer empid,Date date){
		try{
			PayStructure payStructure;
			PayStructureDAO payStructureDAO = PayrollDAOFactory.getDAOFactory().getPayStructureDAO();
			payStructure  = (PayStructure) payStructureDAO.getPayStructureForEmpByDate(empid,date);
			return(payStructure);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	public List getPrevApprovedPayslipsByMonthAndYear(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid)
	{
		try{
			List empPayrollList=new ArrayList();
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			empPayrollList = (ArrayList) empPayrollDAO.getPrevApprovedPayslipsByMonthAndYear(fromdate,todate,deptid);
			return(empPayrollList);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	public EmpPayroll getPrevApprovedPayslipForEmpByDates(GregorianCalendar fromDate,GregorianCalendar toDate,Integer empid) throws Exception
	{
		try{
			EmpPayroll empPayroll=new EmpPayroll();
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			empPayroll = (EmpPayroll) empPayrollDAO.getPrevApprovedPayslipForEmpByDates(fromDate,toDate,empid);
			return (empPayroll);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	///	Create work flow	\\\\\\
	/*
	  public void createWorkFlow(EmpPayroll payslip){
		  try{
		  	logger.debug(" Test Pay Slip Work Flow Start ----> ");
		  	logger.debug("Payslip created by------>"+payslip.getCreatedBy().getUserName());
			EgovWorkFlow workflow = new EgovWorkFlow();

			HashMap infrastructureHashMap = new HashMap();
			HashMap applContextInstanceHashMap = new HashMap();
			HashMap applExecutionContextHashMap = new HashMap();

			infrastructureHashMap.put("xmlResourceName","org/egov/payroll/workflow/processdefinations/PayrollApprovalPD.xml");
			infrastructureHashMap.put("processDefinitionName","Approve Payslip");

			applContextInstanceHashMap.put("userId",payslip.getCreatedBy().getId().toString());
			applContextInstanceHashMap.put("userName",payslip.getCreatedBy().getUserName());
			applContextInstanceHashMap.put("objPK",payslip.getId().toString());
			applContextInstanceHashMap.put("objIdentifier",payslip.getEmployee().getEmployeeCode()+"-"+payslip.getMonth());

			applExecutionContextHashMap.put("userId",payslip.getCreatedBy().getId().toString());
			applExecutionContextHashMap.put("payslipId",payslip.getId().toString());
			String month = EisManagersUtill.getMonthsStrVsDays(payslip.getMonth().intValue());
			String groupingKey;
			groupingKey = month + " " + payslip.getFinancialyear().getFinYearRange() + "-" + payslip.getEmpAssignment().getDeptId().getDeptName() ;
		 	String functionaryGroup= GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","GROUP_PAYSLIPS_FUNCTIONARY",new Date()).getValue();
		 	if("true".equalsIgnoreCase(functionaryGroup))
		 	{
			groupingKey = groupingKey  + "-" + payslip.getEmpAssignment().getFunctionary().getName();
		 	}
		 	String fundGroup= GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","GROUP_PAYSLIPS_FUND",new Date()).getValue();
		 	if("true".equalsIgnoreCase(fundGroup))
		 	{
			groupingKey = groupingKey  + "-" + payslip.getEmpAssignment().getFundId().getName();
		 	}
		 	String functionGroup= GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","GROUP_PAYSLIPS_FUNCTION",new Date()).getValue();
		 	if("true".equalsIgnoreCase(functionGroup))
		 	{
			groupingKey = groupingKey  + "-" + payslip.getEmpAssignment().getFunctionId().getName();
		 	}
			applExecutionContextHashMap.put("month",groupingKey);
			applExecutionContextHashMap.put("objectType", "payslip");

			workflow.workFlowImplementation(infrastructureHashMap,applContextInstanceHashMap,applExecutionContextHashMap);
			logger.debug(" Test Pay Slip Work Flow End ----> ");
		  } catch (RuntimeException e) {
	         //HibernateUtil.rollbackTransaction
	          throw new EGOVRuntimeException(e.getMessage(),e);
	      }
	  }
	 */

	public EmpPayroll getPayslipById(Long id)
	{
		try{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();

			return (EmpPayroll)empPayrollDAO.findById(id, false);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	// this is for JBPM workflow takes only String parameter
	public EmpPayroll getPayslipById(String payslipId){
		try{
			return getPayslipById(Long.parseLong(payslipId));
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	public List getPrevSuppPayslipsForEmpExceptionsInMonthAndYear(GregorianCalendar fromDate,GregorianCalendar toDate,Integer deptid)
	{
		try{
			List empPayrollList=new ArrayList();
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			empPayrollList = (ArrayList) empPayrollDAO.getPrevSuppPayslipsForEmpExceptionsInMonthAndYear(fromDate,toDate,deptid);
			return(empPayrollList);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}



	/**
	 * this api will set the deductions to current payslip and returns currpayslip
	 * this api for exception payslip
	 * here new deduction will not be added to curr payslip
	 * @param lastPay
	 * @param currPay
	 * @return
	 */
	public EmpPayroll setDeductions(EmpPayroll lastPay,EmpPayroll currPay, boolean persist) throws Exception
	{
		try{
			PayrollExternalInterface payrollExternalInterface = getPayrollExterInterface();
			BigDecimal deductionTotal = BigDecimal.ZERO;
			
			Date enddate;
			if(currPay.getFromDate()==null){
				enddate = getEndDateOfMonthByMonthAndFinYear(currPay.getMonth().intValue(),currPay.getFinancialyear().getId().intValue());
			}else{
				enddate = currPay.getToDate();
			}		
			PayStructure payStructure = getPayStructureForEmpByDate(currPay.getEmployee().getIdPersonalInformation(),enddate);

			BigDecimal netPay =BigDecimal.ZERO;
			Set currDeductionses = new HashSet();
			Set deductionses = new HashSet(lastPay.getDeductionses());
			if (deductionses != null && !deductionses.isEmpty()) {
				for (Iterator itr1 = deductionses.iterator(); itr1.hasNext();) {
					Deductions lastPayDed = (Deductions) itr1.next();
					Deductions currPayDed = new Deductions();
					//	Date date=getEndDateOfMonthByMonthAndFinYear(currPay.getMonth().intValue(),currPay.getFinancialyear().getId().intValue());
					SimpleDateFormat fmt = new SimpleDateFormat(DATEFORMATESTR2,Locale.getDefault());
					// Only recurring pay heads should copy to payslip
					//TODO: handle slabbased and rulebased type
					if(lastPayDed.getSalaryCodes()!=null && lastPayDed.getSalaryCodes().getHead()!=null && lastPayDed.getSalaryCodes().getIsRecurring()=='Y')
					{
						if(lastPayDed.getSalaryCodes().getCategoryMaster().getId()==3 || lastPayDed.getSalaryCodes().getCategoryMaster().getId()==5)
						{
						if("SlabBased".equalsIgnoreCase(lastPayDed.getSalaryCodes().getCalType()))
						{
							if(lastPayDed.getSalaryCodes().getTdsId()!=null && lastPayDed.getSalaryCodes().getTdsId().getBank()==null)
								{
									if(lastPayDed.getSalaryCodes().getIsRecomputed()=='N')
									{
										currPayDed.setAmount(lastPayDed.getAmount());
									}else
									{
	//									double curerrentbasic=Math.round((currPay.getBasicPay().doubleValue()*currPay.getNumdays())/currPay.getWorkingDays());
										double amount=0.0;
										if(isPayHeadGrossBased(lastPayDed.getSalaryCodes().getHead()))
										{
											amount=Math.round(getSlabBasedAmount(lastPayDed.getSalaryCodes(),currPay.getGrossPay(),fmt.format(currPay.getToDate())));
										}
										else {
											amount=Math.round(getSlabBasedAmount(lastPayDed.getSalaryCodes(),currPay.getBasicPay(),fmt.format(currPay.getToDate())));
										}
										if(lastPayDed.getSalaryCodes().getIsAttendanceBased()=='Y')
										{
											amount=Math.round((amount*currPay.getNumdays())/currPay.getWorkingDays());
										}
										currPayDed.setAmount(new BigDecimal(Math.round(amount)));
									}
	
	
								}else
								{
									double amount=Math.round(lastPayDed.getAmount().doubleValue());
									if(lastPayDed.getSalaryCodes().getIsAttendanceBased()=='Y' && lastPayDed.getSalaryCodes().getIsRecomputed()=='Y' )
									{
										amount=Math.round((amount*currPay.getNumdays())/currPay.getWorkingDays());
									}
									currPayDed.setAmount(new BigDecimal(amount));
								}
							
						//  }
							
						}
						else if ("RuleBased".equalsIgnoreCase(lastPayDed.getSalaryCodes().getCalType()))
						{
							double amount = 0.0;
							if(lastPayDed.getSalaryCodes().getIsRecomputed()=='Y'){
								PayheadRule payheadRule = PayheadRuleUtil.getPayheadRule(lastPayDed.getSalaryCodes(), currPay.getFromDate());
								if(payheadRule != null)
								{									
									amount = PayheadRuleUtil.computeRuleBasedDeduction(currPay, currPayDed, lastPay, payheadRule,payStructure);
									if(lastPayDed.getSalaryCodes().getIsAttendanceBased() == 'Y')
									{
										amount=Math.round((amount*currPay.getNumdays())/currPay.getWorkingDays());
									}
									currPayDed.setAmount(new BigDecimal(amount));									
								}
							}
						}
						else
						{
							double amount=Math.round(lastPayDed.getAmount().doubleValue());
							if(lastPayDed.getSalaryCodes().getIsAttendanceBased()=='Y' &&lastPayDed.getSalaryCodes().getIsRecomputed()=='Y' )
							{
								amount=Math.round((amount*currPay.getNumdays())/currPay.getWorkingDays());
							}
							currPayDed.setAmount(new BigDecimal(amount));
							//currPayDed.setAmount(new BigDecimal(Math.round(lastPayDed.getAmount().doubleValue())));
						}
						}
						else
						{
							logger.debug("inside else block");
							//No deductions- checks for advance deduction
							
						}
						if (lastPayDed.getSalaryCodes() != null) {
							currPayDed.setSalaryCodes(lastPayDed.getSalaryCodes());
						}
						if (lastPayDed.getSalAdvances() != null	&& lastPayDed.getSalAdvances().getPendingAmt().intValue() > 0) {
							//if PendingAmount is < installment, set SalAdvance pending amount to 0
							AdvanceSchedule advScheduler = PayrollManagersUtill.getAdvanceService().getRecoverableScheduler(lastPayDed.getSalAdvances());
							if(advScheduler != null){
								if(lastPayDed.getSalAdvances().getPendingAmt().floatValue() < advScheduler.getPrincipalAmt().add(advScheduler.getInterestAmt()).floatValue()){
									currPayDed.setAmount(lastPayDed.getSalAdvances().getPendingAmt());
									if(persist){
										lastPayDed.getSalAdvances().setPendingAmt(BigDecimal.ZERO);
										lastPayDed.getSalAdvances().setStatus(payrollExternalInterface.getStatusByModuleAndDescription(SALARYADVANCESTR,CLOSEDSTR));
										advScheduler.setRecover("Y");
									}
								}
								else {
									currPayDed.setAmount(advScheduler.getPrincipalAmt().add(advScheduler.getInterestAmt()));
									if(persist){
										lastPayDed.getSalAdvances().setPendingAmt(lastPayDed.getSalAdvances().getPendingAmt().subtract(currPayDed.getAmount()));
										advScheduler.setRecover("Y");
										//adv.setPendingAmt(new BigDecimal(Math.round(adv.getPendingAmt().doubleValue()-currPayDed.getAmount().doubleValue())));
									}
								}
							}
							else{ 
								if(lastPayDed.getSalAdvances().getPendingAmt().floatValue() < lastPayDed.getSalAdvances().getInstAmt().floatValue()){
									currPayDed.setAmount(lastPayDed.getSalAdvances().getPendingAmt());
									if(persist){
										lastPayDed.getSalAdvances().setPendingAmt(BigDecimal.ZERO);
										lastPayDed.getSalAdvances().setStatus(payrollExternalInterface.getStatusByModuleAndDescription(SALARYADVANCESTR,CLOSEDSTR));
									}
								}else {
									currPayDed.setAmount(lastPayDed.getSalAdvances().getInstAmt());
									if(persist){
										lastPayDed.getSalAdvances().setPendingAmt(new BigDecimal(Math.round(lastPayDed.getSalAdvances().getPendingAmt().doubleValue()-currPayDed.getAmount().doubleValue())));
									}
									//currPayDed.setAmount(new BigDecimal(Math.round(lastPayDed.getSalAdvances().getPendingAmt().doubleValue()-currPayDed.getAmount().doubleValue())));
								}
							}
							currPayDed.setSalAdvances(lastPayDed.getSalAdvances());
						}
						deductionTotal = deductionTotal.add(currPayDed.getAmount());
						currPayDed.setReferenceno(lastPayDed.getReferenceno()); 
						currPayDed.setEmpPayroll(currPay);
						currDeductionses.add(currPayDed);

					}
					if (lastPayDed.getChartofaccounts() != null) {
						currPayDed.setChartofaccounts(lastPayDed.getChartofaccounts());
						currPayDed.setAmount(lastPayDed.getAmount());
						currPayDed.setReferenceno(lastPayDed.getReferenceno()); 
						deductionTotal = deductionTotal.add(currPayDed.getAmount());
						currPayDed.setEmpPayroll(currPay);
						currPayDed.setReferenceno(lastPayDed.getReferenceno());
						currDeductionses.add(currPayDed);
					}

				}
				if (!currDeductionses.isEmpty()) {
					currPay.setDeductionses(currDeductionses);
				}
			}
			netPay = currPay.getGrossPay().subtract(deductionTotal);
			currPay.setNetPay(netPay);
			return currPay;
		} catch (RuntimeException e) {
			////HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	/**
	 * this api will set the deductions to for normal payslip and returns currpay slip obj
	 * @param lastPay
	 * @param currPay
	 * @return
	 */
	public EmpPayroll setNormalDeductions(EmpPayroll lastPay,EmpPayroll currPay,boolean persist) throws Exception
	{

		PayrollExternalInterface payrollExternalInterface = getPayrollExterInterface(); 
		BigDecimal deductionTotal = BigDecimal.ZERO;
		Set<Deductions> currDeductions = currPay.getDeductionses()!=null? currPay.getDeductionses() : new HashSet();		
		try{
			List<SalaryCodes> salaryCodes=getOrderedSalaryCodes();
			Set<Deductions> otherDeductions = new HashSet();
			currPay.setDeductionses(currDeductions);
			//flag will be used to make sure that other deductions will copy once
			int copiedOtherDeductions=0;
			Date fromdate; 			
			if(currPay.getFromDate()==null){
				fromdate = getStartDateOfMonthByMonthAndFinYear(currPay.getMonth().intValue(),currPay.getFinancialyear().getId().intValue());
			}else{
				fromdate = currPay.getFromDate();
			}
			Date enddate;
			if(currPay.getFromDate()==null){
				enddate = getEndDateOfMonthByMonthAndFinYear(currPay.getMonth().intValue(),currPay.getFinancialyear().getId().intValue());
			}else{
				enddate = currPay.getToDate();
			}		
			PayStructure payStructure = getPayStructureForEmpByDate(currPay.getEmployee().getIdPersonalInformation(),enddate);
			Date effFromDate = null;
			effFromDate=payStructure.getEffectiveFrom();	
			boolean isPayscaleChanged		= 	false;			
			Map daysMap = null;
			PayStructure prevPayStructure = null;
			prevPayStructure = getPrevPayStructureIfPayscaleChanged(payStructure, currPay, fromdate,effFromDate, enddate);
			if(prevPayStructure != null)
			{
				isPayscaleChanged = true;
				daysMap = getNoOfDaysMap(currPay, fromdate, effFromDate, enddate);
			}
			// this loop will load the eligible advance,loans and it will update the pending amt
			ArrayList<Advance> list = (ArrayList)(PayrollManagersUtill.getAdvanceService().getAllEligibleAdvancesForEmp(lastPay.getEmployee().getIdPersonalInformation()));
			Iterator itr =  list.iterator();
			while(itr.hasNext() && !list.isEmpty() )
			{
				Advance adv = (Advance)itr.next();

				if (adv != null	&& adv.getPendingAmt().intValue() > 0) {
					Deductions currPayDed = new Deductions();
					//TODO If schedule is the for this advance get installment amount from scheduler otherwise from advance
					//if PendingAmount is < installment, set SalAdvance pending amount to 0
					//AdvanceSchedule advScheduler = PayrollManagersUtill.getSalaryadvanceManager().getAdvSchedulerByMonthYear(adv, currPay.getMonth(),currPay.getFinancialyear());
					if("Y".equals(adv.getMaintainSchedule())){
						AdvanceSchedule advanceScheduler = PayrollManagersUtill.getAdvanceService().getRecoverableScheduler(adv);
						if(advanceScheduler != null){
							if(adv.getPendingAmt().floatValue() < advanceScheduler.getPrincipalAmt().add(advanceScheduler.getInterestAmt()).floatValue()){
								currPayDed.setAmount(adv.getPendingAmt());
								if(persist){
									adv.setPendingAmt(BigDecimal.ZERO);
									adv.setStatus(payrollExternalInterface.getStatusByModuleAndDescription(SALARYADVANCESTR,CLOSEDSTR));
									advanceScheduler.setRecover("Y");
									currPayDed.setAdvanceScheduler(advanceScheduler);
								}
							}
							else {
								currPayDed.setAmount(advanceScheduler.getPrincipalAmt().add(advanceScheduler.getInterestAmt()));
								if(persist){
									adv.setPendingAmt(adv.getPendingAmt().subtract(currPayDed.getAmount()));
									advanceScheduler.setRecover("Y");
									currPayDed.setAdvanceScheduler(advanceScheduler);
									//adv.setPendingAmt(new BigDecimal(Math.round(adv.getPendingAmt().doubleValue()-currPayDed.getAmount().doubleValue())));
								}
							}
						}
						else{
							throw new PayslipFailureException("For this advance-"+adv.getSalaryCodes().getHead()+" scheduler not defined"); 
						}
					}
					else{
						if(adv.getPendingAmt().floatValue()<adv.getInstAmt().floatValue()){
							currPayDed.setAmount(adv.getPendingAmt());
							if(persist){
								adv.setPendingAmt(BigDecimal.ZERO);
								adv.setStatus(payrollExternalInterface.getStatusByModuleAndDescription(SALARYADVANCESTR,CLOSEDSTR));
							}
						} else {
							currPayDed.setAmount(adv.getInstAmt());
							if(persist){
								adv.setPendingAmt(new BigDecimal(Math.round(adv.getPendingAmt().doubleValue()-currPayDed.getAmount().doubleValue())));
							}
						}
					}

					currPayDed.setSalAdvances(adv);
					currPayDed.setEmpPayroll(currPay);
					currPayDed.setSalaryCodes(adv.getSalaryCodes());
					deductionTotal=deductionTotal.add(currPayDed.getAmount());
					currDeductions.add(currPayDed);
				}

			}
			currPay.setDeductionses(currDeductions);
			for(SalaryCodes salaryCode:salaryCodes)
			{
				if(salaryCode!=null && salaryCode.getHead()!=null && salaryCode.getIsRecurring()=='Y')
				{
					//we are loading deduction taxes and deduction others here
					// all deduction-taxes and others are added to payslip even if it does not exist in previous payslip
					if(salaryCode.getCategoryMaster().getId() == 3 ||salaryCode.getCategoryMaster().getId() == 5 )
					{
						double amount=0.0;
						Deductions currPayDed = new Deductions();
						currPayDed.setEmpPayroll(currPay);
						currPayDed.setSalaryCodes(salaryCode);

						SimpleDateFormat fmt = new SimpleDateFormat(DATEFORMATESTR2,Locale.getDefault());
						// copySlabPayheadFromPrev will be used to find out whether slab payhead should be copyed from last payslip .
						// if copySlabPayheadFromPrev is true we need to copy from old payslip
						boolean copySlabPayheadFromPrev = true ;
						//We are adding new check for whetehr calculation type is rulebased/slabbased
						if("SlabBased".equals(salaryCode.getCalType())){
							if(salaryCode.getTdsId()!=null && salaryCode.getTdsId().getBank()==null && payrollExternalInterface.findByTds(salaryCode.getTdsId())!=null && payrollExternalInterface.findByTds(salaryCode.getTdsId()).size()>0)
							{
								copySlabPayheadFromPrev= false ;
								if(salaryCode.getIsRecomputed()=='Y')
								{
									if(isPayHeadGrossBased(salaryCode.getHead()))
									{
										amount=Math.round(getSlabBasedAmount(salaryCode,currPay.getGrossPay(),fmt.format(currPay.getToDate())));
									}
									else {
										amount=Math.round(getSlabBasedAmount(salaryCode,currPay.getBasicPay(),fmt.format(currPay.getToDate())));
									}
									if(salaryCode.getIsAttendanceBased()=='Y')
									{
										amount=Math.round((amount*currPay.getNumdays())/currPay.getWorkingDays());
									}
									currPayDed.setAmount(new BigDecimal(amount));
									currDeductions.add(currPayDed);
									deductionTotal=deductionTotal.add(currPayDed.getAmount());

								}else{
									copySlabPayheadFromPrev = true;
								}

								//TODO: throw exception if slabbased and no TDS
								//double curerrentbasic=Math.round((currPay.getBasicPay().doubleValue()*currPay.getNumdays())/currPay.getWorkingDays());


							}
							else
							{
								throw new PayslipFailureException("Recovery Master is not setUp for "+salaryCode.getHead());
							}
						}else if("RuleBased".equalsIgnoreCase(salaryCode.getCalType())){

							copySlabPayheadFromPrev= false ;
							if(salaryCode.getIsRecomputed()=='Y'){
								PayheadRule payheadRule = PayheadRuleUtil.getPayheadRule(salaryCode, currPay.getFromDate());
								if(payheadRule != null){									
									if(!isPayscaleChanged)
									{
										amount = PayheadRuleUtil.computeRuleBasedDeduction(currPay, currPayDed, lastPay, payheadRule,payStructure);
										if(salaryCode.getIsAttendanceBased() == 'Y')
										{
											amount=Math.round((amount*currPay.getNumdays())/currPay.getWorkingDays());
										}
									}
									else
									{
										double amount1 = PayheadRuleUtil.computeRuleBasedDeduction(currPay, currPayDed, lastPay, payheadRule,payStructure);
										double amount2 = PayheadRuleUtil.computeRuleBasedDeduction(currPay, currPayDed, lastPay, payheadRule,prevPayStructure);
										if(salaryCode.getIsAttendanceBased() == 'Y')
										{
											amount1 = Math.round(amount1*((Double)daysMap.get("noOfPaidDays_after")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue());
											amount2 = Math.round(amount2*((Double)daysMap.get("noOfPaidDays_before")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue());
											amount	= Math.round(amount1+amount2);
										}
										else
										{
											amount1 = Math.round(amount1*((Double)daysMap.get("noOfWorkingDays_after")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue());
											amount2 = Math.round(amount2*((Double)daysMap.get("noOfWorkingDays_before")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue());
											amount	= Math.round(amount1+amount2);
										}
									}
									currPayDed.setAmount(new BigDecimal(amount));
									currDeductions.add(currPayDed);
									deductionTotal=deductionTotal.add(currPayDed.getAmount());
								}
							}
							else{
								copySlabPayheadFromPrev= true ;
							}

						}


						if(copySlabPayheadFromPrev)//else
						{
							Set deductionses = new HashSet(lastPay.getDeductionses());
							if (deductionses != null && !deductionses.isEmpty()) {

								for (Iterator itr1 = deductionses.iterator(); itr1.hasNext();) {
									Deductions lastDed=(Deductions)itr1.next();
									if(lastDed.getSalaryCodes() != null && lastDed.getSalaryCodes().getId().equals(salaryCode.getId()))

									{
										currPayDed = new Deductions();
										currPayDed.setEmpPayroll(currPay);
										currPayDed.setSalaryCodes(salaryCode);
										PayGenUpdationRule payRule=new PayGenUpdationRule();
										PayheadService payHead=PayrollManagersUtill.getPayheadService();
										if(salaryCode.getHead().equalsIgnoreCase("DA"))
										{
											payRule=payHead.checkRuleBasedOnSalCodeMonFnYrEmpGrp(salaryCode.getId(), currPay.getMonth().intValue(),currPay.getFinancialyear().getId().intValue(),currPay.getEmployee().getGroupCatMstr().getId().intValue());
										}	
										else
										{
											payRule=payHead.checkRuleBasedOnSalCodeMonFnYrEmpGrp(salaryCode.getId(), currPay.getMonth().intValue(),currPay.getFinancialyear().getId().intValue(),null);
										}
										if(payRule==null)
										{
											amount=lastDed.getAmount().doubleValue();
										}
										else
										{
											amount=payRule.getMonthlyAmt().intValue();
										}
										if(lastDed.getSalaryCodes().getIsAttendanceBased()=='Y' && lastDed.getSalaryCodes().getIsRecomputed()=='Y')
										{
											amount=Math.round((amount*currPay.getNumdays())/currPay.getWorkingDays());
										}
										currPayDed.setAmount(new BigDecimal(amount));
										currPayDed.setReferenceno(lastDed.getReferenceno()); 
										currDeductions.add(currPayDed);
										deductionTotal=deductionTotal.add(currPayDed.getAmount());
									}
									if(lastDed.getSalaryCodes() == null && copiedOtherDeductions==0)
									{
										Deductions otherPayDed = new Deductions();
										otherPayDed.setEmpPayroll(currPay);
										//otherPayDed.setSalaryCodes(salaryCode);
										otherPayDed.setChartofaccounts(lastDed.getChartofaccounts());
										otherPayDed.setAmount(lastDed.getAmount());
										otherDeductions.add(otherPayDed);
										deductionTotal=deductionTotal.add(otherPayDed.getAmount());
									}
								}
								copiedOtherDeductions=1;
							}else
							{
								logger.debug("inside else block");
								// it is not slab/rulebased value, and it does not exist in old payslip, then we will not add that deduction
							}

						}
					}else{
						logger.debug("inside else block");
						// check is it advance and check this advance is applyed for emp r not
					}
				}
				

			}
			
			currDeductions.addAll(otherDeductions);
			//remove deductions with Zero Amount
			Set<Deductions> deductionsSet =	removeDeducionsWithAmtZero(currDeductions);
			currPay.setDeductionses(deductionsSet);
			BigDecimal netPay = currPay.getGrossPay().subtract(deductionTotal);
			currPay.setNetPay(netPay);
			return currPay;
		}catch(Exception e)
		{
			logger.error(e.getMessage());
			throw e;
			//			/	return null;
		}
	}
	/**
	 * this api will set the earnings to the current Exception payslip and returns the current payslip
	 * @param lastPay
	 * @param currPay
	 * @param empatcreport
	 * @return
	 */
	//exception payslip
	public EmpPayroll setEarningsForSuppPayslip(EmpPayroll lastPay,EmpPayroll currPay,EmployeeAttendenceReport empatcreport,boolean persists) throws Exception
	{
		try{
			// we are not considering the paystructure defferently for exception payslip and normal payslip
			GregorianCalendar calendar = new GregorianCalendar();
			List<SalaryCodes> salaryCodesList =getOrderedSalaryCodes();

			BigDecimal grossPayTotal =BigDecimal.ZERO;
			BigDecimal basicAmount = BigDecimal.ZERO;
			BigDecimal headAmount = BigDecimal.ZERO;
			BigDecimal pct = BigDecimal.ZERO;
			float earAmount = 0;


//			This will get the Paystructure Object
			//Assumption :: if employee in exception in paystructure will not change
			Date enddate;
			if(currPay.getFromDate()==null)
			{
				enddate=getEndDateOfMonthByMonthAndFinYear(currPay.getMonth().intValue(),currPay.getFinancialyear().getId().intValue());
			}else
			{
				enddate=currPay.getToDate();
			}

			PayStructure  payStructure = getPayStructureForEmpByDate(currPay.getEmployee().getIdPersonalInformation(),enddate);
			if (payStructure == null){
				throw new EGOVException("Employee does not have a valid paystructure for the current period");
			}
			Set earningsSet = new HashSet();
			Set<Earnings> lastPayearningses = new HashSet<Earnings>(lastPay.getEarningses());
			//Set<PayScaleDetails> paydetails=payStructure.getPayHeader().getPayscaleDetailses();
			/**
			 * this will get earnings from current pay structure object,
			 * and then copy the values based on payhead defination properties like isattendence,isrecurring,isrecompute
			 */
			/*
			 * Assumption here is that Basic paycomponent is always present in the payscale
			 */
			if (salaryCodesList != null	&& !salaryCodesList.isEmpty()) {
				Date date=enddate;//getEndDateOfMonthByMonthAndFinYear(currPay.getMonth().intValue(),currPay.getFinancialyear().getId().intValue());
				SimpleDateFormat fmt = new SimpleDateFormat(DATEFORMATESTR2,Locale.getDefault());
				// looping all ordered salary codes list
				for (SalaryCodes sal : salaryCodesList) {

					// copying the earnings from previous earnings
					for (Earnings ear:  lastPayearningses) {
						if(sal.getId().equals(ear.getSalaryCodes().getId()))
						{
							if(sal.getIsRecurring()=='Y'||sal.getCaptureRate()=='Y')	
							{		
								Earnings currEar = new Earnings();
								currEar.setEmpPayroll(currPay);
								currEar.setSalaryCodes(sal);
								//initializing the earning amount to 0 so that null amount is not saved
								currEar.setAmount(BigDecimal.ZERO);
								if ('Y'==sal.getCaptureRate()&& sal.getCalType().equalsIgnoreCase(MONTHLYFLATERATESTR)) {
									//CALLING THE API FOR SETTING THE BASIC 
									basicAmount=calExceptionBasic(lastPay,payStructure,calendar,currPay,persists);
									grossPayTotal = grossPayTotal.add(basicAmount);
									currEar.setAmount(basicAmount);
								}
								else if (sal.getId().equals(ear.getSalaryCodes().getId()) && sal.getIsRecurring()=='Y' 
									&& sal.getCalType().equalsIgnoreCase(MONTHLYFLATERATESTR)) {

									// for basic  isRecurring and isRecompute are always true

									earAmount = Math.round(ear.getAmount().floatValue());
									grossPayTotal = grossPayTotal.add(new BigDecimal(earAmount));
									currEar.setAmount(new BigDecimal(earAmount));
								}
								else if (sal.getId().equals(ear.getSalaryCodes().getId()) && sal.getIsRecurring()=='Y' 
									&& ear.getPct() != null && !ear.getPct().toString().equals("") && basicAmount.intValue() >= 0) {
									//Computed earnings	should get the percenatge values always from last payslip
									pct = ear.getPct().divide(new BigDecimal(100));
									currEar.setPct(ear.getPct());
									double baseamt=0.0;
									if('Y'==ear.getSalaryCodes().getSalaryCodes().getCaptureRate())
									{
										baseamt=currPay.getBasicPay().doubleValue();
									}
									else
									{
										baseamt=getEarningAmountBySalarycode(earningsSet,ear.getSalaryCodes().getSalaryCodes().getId());
									}

									headAmount = new BigDecimal(Math.round(pct.doubleValue()*baseamt));

									// if salary code is attendence base prorate the earnings amount as per attendence
									/*if(sal.getIsAttendanceBased()=='Y')
									headAmount=new BigDecimal((headAmount.doubleValue()*currPay.getNumdays())/currPay.getWorkingDays());*/
									currEar.setAmount(headAmount);
									grossPayTotal = grossPayTotal.add(headAmount);
									/* else if (sal.getId().equals(ear.getSalaryCodes().getId()) && sal.getIsRecurring()=='Y' && ear.getPct() != null && !ear.getPct().equals("") && basicAmount.intValue() >= 0) {
						//Computed earnings	should get the percenatge values always from last payslip
							pct = ear.getPct().divide(new BigDecimal(100));
							currEar.setPct(ear.getPct());
							double baseamt=0.0;
							if('Y'==ear.getSalaryCodes().getSalaryCodes().getCaptureRate())
								baseamt=currPay.getBasicPay().doubleValue();
							else
								baseamt=getEarningAmountBySalarycode(earningsSet,ear.getSalaryCodes().getSalaryCodes().getId());

									// Here payStructure.getPayHeader().getIncrSlabs() always returns only one object Because of payScaleslIncFilter enabled
									Set temp=payStructure.getPayHeader().getIncrSlabs();
									Iterator itr=temp.iterator();
									IncrementSlabsForPayScale incrementslab = null;
									if (itr.hasNext())
									{
										incrementslab=(IncrementSlabsForPayScale)itr.next();
										incrementDet.setAmount(incrementslab.getIncSlabAmt());
									}else 
										incrementDet.setAmount(new BigDecimal(0)); //Increment slab amount cannot be found as employee on a reduced basic pay
									//IncrementSlabsForPayScale incrementslab=(IncrementSlabsForPayScale)itr.next();
									incrementDet.setAmount(incrementslab.getIncSlabAmt());
									incrementDet.setRemarks("Increment is Pending BZ of Supplimentary Pay");
									incrementDet.setStatus(PayrollConstants.EMP_INCREMENT_DETAILS_STATUS_PENDING);
									HibernateUtil.getCurrentSession().save(incrementDet);
								}
								earAmount = basicAmount.floatValue();
								currPay.setBasicPay(new BigDecimal(Math.round(basicAmount.doubleValue())));
								logger.debug("Basic Pay set as " + earAmount + " for emp id :" + currPay.getEmployee().getIdPersonalInformation() );
								//TODO: Cross check if this is needed? Would exception have attendance?
								/* if(sal.getIsAttendanceBased()=='Y')
								{
									double temp1=earAmount/ noofdaysmonth;
									basicAmount = new BigDecimal(Math.round(temp1*(currPay.getNumdays())));
								} */

									// if salary code is attendence base prorate the earnings amount as per attendence
									/*if(sal.getIsAttendanceBased()=='Y')
								headAmount=new BigDecimal((headAmount.doubleValue()*currPay.getNumdays())/currPay.getWorkingDays());
							currEar.setAmount(headAmount);
							grossPayTotal = grossPayTotal.add(headAmount); */
								}else if(sal.getId().equals(ear.getSalaryCodes().getId()) && sal.getIsRecurring()=='Y' && 
										sal.getCalType().equalsIgnoreCase(SLABBASEDSTR)){
									if(sal.getTdsId()!=null && sal.getTdsId().getBank()==null)
									{
										//Slab based earnings
										double amount=0.0;
										if(sal.getIsRecomputed()=='Y')
										{
											amount=getSlabBasedAmount(sal,new BigDecimal(currPay.getBasicPay().doubleValue()),fmt.format(date));
										}
										else
										{
											amount=ear.getAmount().doubleValue();
										}
										/*if(sal.getIsRecomputed()=='Y' && sal.getIsAttendanceBased()=='Y')
									amount=(amount*currPay.getNumdays())/currPay.getWorkingDays();*/
										BigDecimal temp3 = new BigDecimal(amount);
										currEar.setAmount(temp3);
										grossPayTotal = grossPayTotal.add(temp3);
									}else
									{
										throw new EGOVException("TDS Not Setted for "+sal.getHead());
									}
								}
								/** moved to starting of loop
							//TODO: check if this check can be moved to beginning of block - can avoid unnecessary computations
							if(sal.getIsRecurring()=='Y')
							{
							  earningsSet.add(currEar);
							} */
								earningsSet.add(currEar);
							}
						}
					}
				}

			}else {
				logger.error("No salarycodes have been defined");
				throw new EGOVException("No salarycodes have been defined");
				//return false;
			}
			if(!earningsSet.isEmpty())
			{
				currPay.setEarningses(earningsSet);
			}
			currPay.setGrossPay(new BigDecimal(Math.round(grossPayTotal.doubleValue())));
			return currPay;
		} catch (RuntimeException e) {
			////HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	private BigDecimal calExceptionBasic(EmpPayroll lastPay,PayStructure payStructure,Calendar calendar,EmpPayroll currPay,Boolean persists)
	{
		BigDecimal basicAmount = BigDecimal.ZERO;
		float earAmount = 0;
		basicAmount = lastPay.getBasicPay();
		calendar.setTime(payStructure.getAnnualIncrement());
		int increMonth = calendar.get(Calendar.MONTH);
		//adding 1 as Calendar is zero-based
		BigDecimal incrementMonth = new BigDecimal(increMonth).add(BigDecimal.ONE);

		if (incrementMonth.equals(currPay.getMonth()) && persists)
		{
			IncrementDetails incrementDet = new IncrementDetails();

			incrementDet.setCreatedby(currPay.getCreatedBy());
			incrementDet.setCreateddate(currPay.getCreatedDate());
			incrementDet.setEmployee(currPay.getEmployee());
			incrementDet.setFinancialyear(currPay.getFinancialyear());
			incrementDet.setMonth(currPay.getMonth());
			incrementDet.setIncrementDate(calendar.getTime());
//			//Enabling the increment slab filter ::: it will return the correct increment slab based on basic amount,and we always consider the 1st object only
			HibernateUtil.getCurrentSession().enableFilter("payScaleslIncFilter").setParameter
			("basicAmount",Long.valueOf(basicAmount.toString()));

			// Here payStructure.getPayHeader().getIncrSlabs() always returns only one object Because of payScaleslIncFilter enabled
			Set temp=payStructure.getPayHeader().getIncrSlabs();
			Iterator itr=temp.iterator();
			IncrementSlabsForPayScale incrementslab = null;
			if (itr.hasNext())
			{
				incrementslab=(IncrementSlabsForPayScale)itr.next();
				incrementDet.setAmount(incrementslab.getIncSlabAmt());
			}else 
			{
				incrementDet.setAmount(BigDecimal.ZERO); //Increment slab amount cannot be found as employee on a reduced basic pay
			}

			//incrementDet.setAmount(incrementslab.getIncSlabAmt());
			incrementDet.setRemarks("Increment is Pending BZ of Supplimentary Pay");
			incrementDet.setStatus(PayrollConstants.EMP_INCREMENT_DETAILS_STATUS_PENDING);
			HibernateUtil.getCurrentSession().save(incrementDet);
		}
		earAmount = basicAmount.floatValue();
		currPay.setBasicPay(new BigDecimal(Math.round(basicAmount.doubleValue())));
		logger.debug("Basic Pay set as " + earAmount + " for emp id :" + currPay.getEmployee().getIdPersonalInformation() );
		return basicAmount;  
		//TODO: Cross check if this is needed? Would exception have attendance?
		/* if(sal.getIsAttendanceBased()=='Y')
			{
				double temp1=earAmount/ noofdaysmonth;
				basicAmount = new BigDecimal(Math.round(temp1*(currPay.getNumdays())));
			} */
	}


	protected void filterBasicAmount(BigDecimal basicAmount){
		HibernateUtil.getCurrentSession().enableFilter("payScaleslIncFilter").
		setParameter("basicAmount",Long.valueOf(basicAmount.toString()));
	}


	/**
	 * this api will set the earnings to the current payslip and returns the current payslip
	 * this api will expect the values for following properties::
	 * fromdate,todate,month,financialyear,createdby,createddate,numdays,workingdays,employee
	 * @param lastPay
	 * @param currPay
	 * @param empatcreport
	 * @param persists - if true will persist incremental history data and update employee's payscale
	 * @return
	 */
	public EmpPayroll setEarnings(EmpPayroll lastPay,EmpPayroll currPay,EmployeeAttendenceReport empatcreport,boolean persists) throws Exception
	{
		try
		{
			//Integer type=lastPay.getPayType().getId(); 
			//PayTypeMaster normalPaytype = getPayTypeMasterByPaytype(PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL);
			// we are not considering the paystructure defferently for exception payslip and normal payslip
			List<SalaryCodes> salaryCodesList = getOrderedSalaryCodes();
			BigDecimal grossPayTotal = BigDecimal.ZERO;
			BigDecimal basicAmount = BigDecimal.ZERO;					
			//BigDecimal headAmount = new BigDecimal(0);
			//BigDecimal pct = new BigDecimal(0);

			//float earAmount = 0;
			//This will get the Paystructure Object
			//Assumption :: if employee in exception in paystructure will not change
			Date fromdate; 			
			if(currPay.getFromDate()==null){
				fromdate = getStartDateOfMonthByMonthAndFinYear(currPay.getMonth().intValue(),currPay.getFinancialyear().getId().intValue());
			}else{
				fromdate = currPay.getFromDate();
			}
			Date enddate;
			if(currPay.getFromDate()==null){
				enddate = getEndDateOfMonthByMonthAndFinYear(currPay.getMonth().intValue(),currPay.getFinancialyear().getId().intValue());
			}else{
				enddate = currPay.getToDate();
			}
			//TODO Check if paystructure is null (But this checking is not needed because we already checked before calling this API)????
			PayStructure payStructure = getPayStructureForEmpByDate(currPay.getEmployee().getIdPersonalInformation(),enddate);
			Date effFromDate = null;
			effFromDate=payStructure.getEffectiveFrom();	
			boolean isPayscaleChanged	= 	false;				
			Map daysMap = null;
			PayStructure prevPayStructure = null;
			prevPayStructure = getPrevPayStructureIfPayscaleChanged(payStructure, currPay, fromdate,effFromDate, enddate);
			if(prevPayStructure != null)
			{
				isPayscaleChanged = true;
				daysMap = getNoOfDaysMap(currPay, fromdate, effFromDate, enddate);
			}
			/*if (payStructure == null){
					throw new EGOVException("Employee does not have a valid paystructure for the current period");
				}*/							
			PayslipProcessImpl payslipProcessInmp = new PayslipProcessImpl();		
			//Apply increment and get new Payscale, other wise return old payscale
			payStructure = payslipProcessInmp.applyPayscaleIncrement(currPay, payStructure, persists);	
			logger.debug("effFromDate"+effFromDate);
			logger.debug("payStructure.getEffectiveFrom()"+payStructure.getEffectiveFrom());
			if(payStructure.getEffectiveFrom().compareTo(effFromDate) != 0)
			{					
				isPayscaleChanged = true;
				logger.debug("After Apply Increment : isPayscaleChanged : "+isPayscaleChanged);
				prevPayStructure = getPayStructureForEmpByDate(currPay.getEmployee().getIdPersonalInformation(),fromdate);
				//get updated no. of days map based on new payscale.
				daysMap = getNoOfDaysMap(currPay, fromdate, payStructure.getEffectiveFrom(), enddate);
			}	
			Set earningsSet = new HashSet();
			Set<Earnings> lastPayearningses = new HashSet<Earnings>(lastPay.getEarningses());
			Set<PayScaleDetails> paydetails = payStructure.getPayHeader().getPayscaleDetailses();
			//This will get earnings from current pay structure object,
			//And then copy the values based on payhead defination properties like isattendence,isrecurring,isrecompute
			//Assumption here is that Basic paycomponent is always present in the payscale
			if(salaryCodesList != null	&& !salaryCodesList.isEmpty()) 
			{
				Date date=enddate;
				//SimpleDateFormat fmt = new SimpleDateFormat(DATEFORMATESTR2,Locale.getDefault());
				//Looping all ordered salary codes list
				for (SalaryCodes sal : salaryCodesList) 
				{
					/**
					 * whether payhead already copied from paystructure.
					 */
					boolean payheadcopied=false;
					/**
					 *  copying the earnings from paystructure-payscale datails
					 */
					for (PayScaleDetails ear:  paydetails) 
					{
						if (sal.getId().equals(ear.getSalaryCodes().getId()))
						{
							if(sal.getIsRecurring()=='Y'||sal.getCaptureRate()=='Y')	
							{
								if( 'Y' == sal.getCaptureRate()&& sal.getCalType().equalsIgnoreCase(MONTHLYFLATERATESTR))
								{
									Earnings currEar = new Earnings();
									currEar.setEmpPayroll(currPay);
									currEar.setSalaryCodes(sal);
									//initialising the earning amount to 0 so that null amount is not saved
									currEar.setAmount(BigDecimal.ZERO);
									//basicAmount = calculateBasic(payStructure,currPay,persists,sal);
									if(isPayscaleChanged)
									{
										//get basic amount calculated based on two payscales
										basicAmount = calculateBasic(payStructure,currPay,persists,sal,daysMap,prevPayStructure,true,fromdate,enddate);																					
									}			
									else
									{
										//calculate for single payscale
										basicAmount = calculateBasic(payStructure,currPay,persists,sal,null, null,false,fromdate,enddate);
									}									
									grossPayTotal = grossPayTotal.add(basicAmount);
									currEar.setAmount(basicAmount);
									earningsSet.add(currEar);
									payheadcopied = true;
								}	
								else if(ear.getSalaryCodes().getIsRecurring()== 'Y' && sal.getCalType().equalsIgnoreCase(SLABBASEDSTR))
								{
									Earnings currEar = new Earnings();
									currEar.setEmpPayroll(currPay);
									currEar.setSalaryCodes(sal);
									//initialising the earning amount to 0 so that null amount is not saved
									currEar.setAmount(BigDecimal.ZERO);
									double amount=0.0;
									if(isPayscaleChanged)
									{
										amount=getearningAmountForSlabBased(payStructure,lastPay,sal,ear,currPay,earningsSet,date,daysMap,prevPayStructure,true);										
									}			
									else
									{
										amount=getearningAmountForSlabBased(payStructure,lastPay,sal,ear,currPay,earningsSet,date,null,null,false);
									}	
									BigDecimal temp3 = new BigDecimal(amount);
									currEar.setAmount(temp3);
									grossPayTotal = grossPayTotal.add(temp3);
									earningsSet.add(currEar);
									payheadcopied = true;
								}
								else if(ear.getSalaryCodes().getIsRecurring()== 'Y' && "RuleBased".equalsIgnoreCase(sal.getCalType()))
								{
									//compute amount by python
									Earnings currEar = new Earnings();
									BigDecimal calcEarnAmt	=	BigDecimal.ZERO;
									BigDecimal calcEarnAmt1	=	BigDecimal.ZERO;
									BigDecimal calcEarnAmt2	=	BigDecimal.ZERO;
									currEar.setEmpPayroll(currPay);
									currEar.setSalaryCodes(sal);
									//initialising the earning amount to 0 so that null amount is not saved
									currEar.setAmount(BigDecimal.ZERO);
									PayheadRule payheadRule = PayheadRuleUtil.getPayheadRule(sal, currPay.getFromDate());
									if(payheadRule != null)
									{
										if(isPayscaleChanged)
										{
											calcEarnAmt1 = PayheadRuleUtil.computeRuleBasedEarning(currPay, currEar,lastPay, earningsSet,payheadRule,payStructure);
											calcEarnAmt2 = PayheadRuleUtil.computeRuleBasedEarning(currPay, currEar,lastPay, earningsSet,payheadRule,prevPayStructure);
											if( ear.getSalaryCodes().getIsAttendanceBased() != 'Y')
											{
												calcEarnAmt1 	=	new BigDecimal(Math.round(calcEarnAmt1.doubleValue()*((Double)daysMap.get("noOfWorkingDays_after")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue()));
												calcEarnAmt2 	=	new BigDecimal(Math.round(calcEarnAmt2.doubleValue()*((Double)daysMap.get("noOfWorkingDays_before")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue()));
												calcEarnAmt		= 	new BigDecimal(Math.round(calcEarnAmt1.doubleValue()+calcEarnAmt2.doubleValue()));
											}
											else
											{
												calcEarnAmt1 	=	new BigDecimal(Math.round(calcEarnAmt1.doubleValue()*((Double)daysMap.get("noOfPaidDays_after")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue()));
												calcEarnAmt2 	=	new BigDecimal(Math.round(calcEarnAmt2.doubleValue()*((Double)daysMap.get("noOfPaidDays_before")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue()));
												calcEarnAmt		= 	new BigDecimal(Math.round(calcEarnAmt1.doubleValue()+calcEarnAmt2.doubleValue()));
											}
										}
										else
										{
											calcEarnAmt = PayheadRuleUtil.computeRuleBasedEarning(currPay, currEar,lastPay, earningsSet,payheadRule,payStructure);
											if(ear.getSalaryCodes() != null && ear.getSalaryCodes().getIsAttendanceBased() == 'Y')
											{	
												logger.debug("ear.getSalaryCodes()="+ear.getSalaryCodes().getHead()+" : ear.getSalaryCodes().getIsAttendanceBased() ="+ear.getSalaryCodes().getIsAttendanceBased());
												logger.debug("currPay.getNumdays() = "+currPay.getNumdays());
												logger.debug("currPay.getWorkingDays() = "+currPay.getWorkingDays());												
												calcEarnAmt 	=	new BigDecimal(Math.round(calcEarnAmt.doubleValue()*currPay.getNumdays()/currPay.getWorkingDays()));
											}											
										}
										
										currEar.setAmount(calcEarnAmt);
									}
									grossPayTotal = grossPayTotal.add(currEar.getAmount());
									earningsSet.add(currEar);
									payheadcopied = true;
								}
								else if(ear.getSalaryCodes().getIsRecurring()== 'Y')
								{
									Earnings currEar = new Earnings();
									currEar.setEmpPayroll(currPay);
									currEar.setSalaryCodes(sal);
									//initialising the earning amount to 0 so that null amount is not saved
									currEar.setAmount(BigDecimal.ZERO);
									//Pro-rate the amount for previous and current payscale, for payscale change process
									BigDecimal earAmt = BigDecimal.ZERO;
									if(isPayscaleChanged)
									{
										earAmt = getEarningAmountForPayheadByDate(payStructure,lastPay,sal,ear,currPay,currEar,earningsSet,daysMap,prevPayStructure,true);										
									}
									else
									{
										earAmt = getEarningAmountForPayheadByDate(payStructure,lastPay,sal,ear,currPay,currEar,earningsSet,null,null,false);
									}
									grossPayTotal = grossPayTotal.add(earAmt);
									earningsSet.add(currEar);
									payheadcopied = true;
								}
							}
						}
					}
					//Iterating all earning from last month payslip
					for (Earnings ear:  lastPayearningses) 
					{
						if (sal.getId().equals(ear.getSalaryCodes().getId()) && 
								!payheadcopied && ear.getSalaryCodes().getIsRecurring()== 'Y' ) 
						{

							if(sal.getCalType().equalsIgnoreCase("RuleBased"))

							{
								Earnings currEar = new Earnings();
								BigDecimal calcEarnAmt=BigDecimal.ZERO;
								BigDecimal calcEarnAmt1	=	BigDecimal.ZERO;
								BigDecimal calcEarnAmt2	=	BigDecimal.ZERO;
								currEar.setEmpPayroll(currPay);
								//initializing the earning amount to 0 so that null amount is not saved
								currEar.setAmount(BigDecimal.ZERO);
								currEar.setSalaryCodes(sal);
								//TODO: compute amount by drool
								PayheadRule payheadRule = PayheadRuleUtil.getPayheadRule(sal, currPay.getFromDate());
								if(payheadRule != null)
								{
									if(isPayscaleChanged)
									{
										calcEarnAmt1 = PayheadRuleUtil.computeRuleBasedEarning(currPay, currEar,lastPay, earningsSet,payheadRule,payStructure);
										calcEarnAmt2 = PayheadRuleUtil.computeRuleBasedEarning(currPay, currEar,lastPay, earningsSet,payheadRule,prevPayStructure);
										if( ear.getSalaryCodes().getIsAttendanceBased() != 'Y')
										{
											calcEarnAmt1 	=	new BigDecimal(Math.round(calcEarnAmt1.doubleValue()*((Double)daysMap.get("noOfWorkingDays_after")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue()));
											calcEarnAmt2 	=	new BigDecimal(Math.round(calcEarnAmt2.doubleValue()*((Double)daysMap.get("noOfWorkingDays_before")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue()));
											calcEarnAmt		= 	new BigDecimal(Math.round(calcEarnAmt1.doubleValue()+calcEarnAmt2.doubleValue()));
										}
										else
										{
											calcEarnAmt1 	=	new BigDecimal(Math.round(calcEarnAmt1.doubleValue()*((Double)daysMap.get("noOfPaidDays_after")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue()));
											calcEarnAmt2 	=	new BigDecimal(Math.round(calcEarnAmt2.doubleValue()*((Double)daysMap.get("noOfPaidDays_before")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue()));
											calcEarnAmt		= 	new BigDecimal(Math.round(calcEarnAmt1.doubleValue()+calcEarnAmt2.doubleValue()));
										}
									}
									else
									{ 
										calcEarnAmt = PayheadRuleUtil.computeRuleBasedEarning(currPay, currEar,lastPay, earningsSet,payheadRule,payStructure);
										if(ear.getSalaryCodes() != null && ear.getSalaryCodes().getIsAttendanceBased() == 'Y')
										{												
											calcEarnAmt 	=	new BigDecimal(Math.round(calcEarnAmt.doubleValue()*currPay.getNumdays()/currPay.getWorkingDays()));
										}											
									}
									currEar.setAmount(calcEarnAmt);
								}
								grossPayTotal=grossPayTotal.add(currEar.getAmount());
								earningsSet.add(currEar);
								payheadcopied=true;
								//TODO: if there is no rule specified for this date then what to do
							}
							else if(!sal.getCalType().equalsIgnoreCase(SLABBASEDSTR)&& sal.getCaptureRate()!='Y')
							{
								Earnings currEar = new Earnings();
								currEar.setEmpPayroll(currPay);
								//initializing the earning amount to 0 so that null amount is not saved
								currEar.setAmount(BigDecimal.ZERO);
								currEar.setSalaryCodes(sal);
								BigDecimal earAmt = BigDecimal.ZERO;
								if(isPayscaleChanged)
								{
									earAmt = getEarningAmountForPayheadByDate(payStructure,lastPay,sal,null,currPay,currEar,earningsSet,daysMap,prevPayStructure,true);
								}
								else
								{
									earAmt = getEarningAmountForPayheadByDate(payStructure,lastPay,sal,null,currPay,currEar,earningsSet,null,null,false);
								}
								grossPayTotal=grossPayTotal.add(earAmt);
								earningsSet.add(currEar);
								payheadcopied=true;
							}
							else if(sal.getCalType().equalsIgnoreCase(SLABBASEDSTR))
							{
								Earnings currEar = new Earnings();
								currEar.setEmpPayroll(currPay);
								//initializing the earning amount to 0 so that null amount is not saved
								currEar.setAmount(BigDecimal.ZERO);
								currEar.setSalaryCodes(sal);
								double amount=0.0;
								if(isPayscaleChanged)
								{
									amount=getearningAmountForSlabBasedFromEarning(payStructure,sal,currPay,ear,date,lastPay,earningsSet,daysMap,prevPayStructure,true);
								}
								else
								{
									amount=getearningAmountForSlabBasedFromEarning(payStructure,sal,currPay,ear,date,lastPay,earningsSet,null,null,false);
								}
									
								BigDecimal temp3 = new BigDecimal(amount);
								currEar.setAmount(temp3);
								grossPayTotal = grossPayTotal.add(temp3);
								earningsSet.add(currEar);
								payheadcopied=true;
							}

						}
					}
				}
			}
			else
			{
				logger.debug("No salarycodes defined");
				throw new PayslipFailureException("No salarycodes have been defined");
			}

			if(!earningsSet.isEmpty())
			{
				Set <Earnings> earnSet = new HashSet();
				earnSet = removeEarningsWithAmtZero(earningsSet);
				if(!earnSet.isEmpty())
					currPay.setEarningses(earnSet);
			}
			currPay.setGrossPay(new BigDecimal(Math.round(grossPayTotal.doubleValue())));
			logger.debug("currPayz Gross pay ="+currPay.getGrossPay()+"new BigDecimal(Math.round(grossPayTotal.doubleValue())"+new BigDecimal(Math.round(grossPayTotal.doubleValue())));
			return currPay;									
		}
		catch (RuntimeException e)
		{
			logger.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	public Set<Earnings> removeEarningsWithAmtZero(Set <Earnings> earningsSet)
	{
		Set <Earnings>earnSet = new HashSet();
		if(earningsSet != null)
			logger.debug("Inside removeEarningsWithAmtZero earningsSet.size() before: " +earningsSet.size());
		for(Earnings currEar : earningsSet )
		{
			if(currEar.getAmount() != null && currEar.getAmount().doubleValue() > 0)
			{
				earnSet.add(currEar);
			}
		}
		if(earnSet != null)
			logger.debug("Inside removeEarningsWithAmtZero earnSet.size() after :" +earnSet.size());
		return earnSet;
	}
	
	private Set<Deductions> removeDeducionsWithAmtZero(Set <Deductions> deductionsSet)
	{
		Set <Deductions>dedSet = new HashSet();
		if(deductionsSet != null)
			logger.debug("Inside removeDeducionsWithAmtZero deductionsSet.size() before: " +deductionsSet.size());
		for(Deductions currDed : deductionsSet )
		{
			if(currDed.getAmount() != null && currDed.getAmount().doubleValue() > 0)
			{
				dedSet.add(currDed);
			}
		}
		if(dedSet != null)
			logger.debug("Inside removeDeducionsWithAmtZero deductionsSet.size() after :" +dedSet.size());
		return dedSet;
	}
	private BigDecimal calculateBasic(PayStructure payStructure,EmpPayroll currPay,
			Boolean persists,SalaryCodes sal, Map daysMap,PayStructure prevPayStructure, boolean isPayscaleChanged, Date fromdate,Date enddate) throws Exception
			{
				BigDecimal basicAmount = BigDecimal.ZERO;
				BigDecimal basicAmount1 = BigDecimal.ZERO;
				BigDecimal basicAmount2 = BigDecimal.ZERO;
				float earAmount = 0;
				//incrementApplied	=	false;
		
				//Call Apply increment API and then use that payscale
				//Retrun the updated payscale if incremented otherwise return old payscale
				//Moving this code to setEarnings() method... before for loop 
				/*
				PayslipProcessImpl payslipProcessInmp = new PayslipProcessImpl();	
				Date effFromDate = payStructure.getEffectiveFrom();				
				payStructure = payslipProcessInmp.applyPayscaleIncrement(currPay, payStructure, persists);		
				if(payStructure.getEffectiveFrom().compareTo(effFromDate) != 0)
				{					
					incrementApplied = true;
					logger.debug("Inside calculateBasic: Increment Applied : "+incrementApplied);
					//get updated no. of days map based on new payscale.
					daysMap = getNoOfDaysMap(currPay, fromdate, payStructure.getEffectiveFrom(), enddate);
				}				
				*/
				//Then use this paystructure for payhead calculation
				if(!isPayscaleChanged)
				{
					if(payStructure.getCurrBasicPay()!= null){
						basicAmount=payStructure.getCurrBasicPay();
					}
					else if(payStructure.getDailyPay()!= null){
						basicAmount=payStructure.getDailyPay();
					}
				}
				else
				//(for Payscalechange)Calculate basic based on two payscales.
				{					
					//Calculate basic amount for current payscale
					if(payStructure.getCurrBasicPay()!= null)
					{
						double temp=payStructure.getCurrBasicPay().doubleValue();
						basicAmount1=new BigDecimal(Math.round(temp*((Double)daysMap.get("noOfWorkingDays_after")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue()));
						//Calculate basic amount for previous payscale
						if(prevPayStructure.getCurrBasicPay()!= null){
							double temp2=prevPayStructure.getCurrBasicPay().doubleValue();
							basicAmount2=new BigDecimal(Math.round(temp2*((Double)daysMap.get("noOfWorkingDays_before")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue()));						
						}
					}
					else if(payStructure.getDailyPay()!= null)
					{
						double temp=payStructure.getDailyPay().doubleValue();
						basicAmount1=new BigDecimal(Math.round(temp*((Double)daysMap.get("noOfWorkingDays_after")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue()));
						 if(prevPayStructure.getDailyPay()!= null){
								double temp2=prevPayStructure.getDailyPay().doubleValue();
								basicAmount2=new BigDecimal(Math.round(temp2*((Double)daysMap.get("noOfWorkingDays_before")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue()));
							}
					}
					//Sum up both pro-rated values
					basicAmount=new BigDecimal(Math.round(basicAmount1.doubleValue()+basicAmount2.doubleValue()));
				}
				earAmount = basicAmount.floatValue();
				currPay.setBasicPay(new BigDecimal(Math.round(basicAmount.doubleValue())));
				logger.debug("Basic Pay set as " + earAmount + " for emp id :" + currPay.getEmployee().getIdPersonalInformation() );
				if(sal.getIsAttendanceBased()=='Y')
				{		
					logger.debug("sal.getIsAttendanceBased()="+sal.getIsAttendanceBased());
					if(!isPayscaleChanged)
					{
						if( payStructure.getCurrBasicPay()!= null)
						{
							double temp1=earAmount/currPay.getWorkingDays();			
							basicAmount = new BigDecimal(Math.round(temp1*currPay.getNumdays()));
						}
						else if(payStructure.getDailyPay()!= null)
						{
							double temp = earAmount*currPay.getNumdays();
							basicAmount = new BigDecimal(temp);
							//basicAmount=payStructure.getDailyPay();
						}	
					}
					else
					{		
						//get the pro-rated amounts seperately for two periods
						if(payStructure.getCurrBasicPay()!= null)
						{							
							double temp1=basicAmount1.doubleValue()/((Double)daysMap.get("noOfWorkingDays_after")).doubleValue();			
							basicAmount1 = new BigDecimal(Math.round(temp1*((Double)daysMap.get("noOfPaidDays_after")).doubleValue()));
							double temp2=basicAmount2.doubleValue()/((Double)daysMap.get("noOfWorkingDays_before")).doubleValue();			
							basicAmount2 = new BigDecimal(Math.round(temp2*((Double)daysMap.get("noOfPaidDays_before")).doubleValue()));
							basicAmount = new BigDecimal(Math.round(basicAmount1.doubleValue()+basicAmount2.doubleValue()));
						}
						else if(payStructure.getDailyPay()!= null)
						{
							double temp1 = basicAmount1.doubleValue()*((Double)daysMap.get("noOfPaidDays_after")).doubleValue();
							double temp2 = basicAmount2.doubleValue()*((Double)daysMap.get("noOfPaidDays_before")).doubleValue();
							basicAmount = new BigDecimal(Math.round(temp1+temp2));							
						}
					}
				}		
				return basicAmount;				
	}



	public BigDecimal getBasicAmount(Set currEarningses)
	{
		BigDecimal basic =BigDecimal.ZERO;
		//Set<Earnings> earningSet = new HashSet<Earnings>();
		for(Iterator it = currEarningses.iterator();it.hasNext();)
		{
			Earnings currEar = (Earnings)it.next();

			if('Y' ==currEar.getSalaryCodes().getCaptureRate())
			{
				basic = currEar.getAmount();
			}
		}
		return basic;
	}

	private double getearningAmountForSlabBased(PayStructure payStructure,EmpPayroll lastPay,SalaryCodes sal,
			PayScaleDetails payscaleEar,EmpPayroll currPay,Set earningSet,Date date,Map daysMap,PayStructure prevPayStructure, boolean isPayscaleChanged)throws Exception
			{ 

		SimpleDateFormat fmt = new SimpleDateFormat(DATEFORMATESTR2,Locale.getDefault());
		Set<Earnings> lastPayearningses = new HashSet<Earnings>(lastPay.getEarningses());
		//Set<Earnings> currEarningses = new HashSet<Earnings>(currPay.getEarningses());
		BigDecimal basicPy = BigDecimal.ZERO;
		if(sal.getTdsId()!=null && sal.getTdsId().getBank()==null){
			double calAmount=0.0;
			double calAmount1=0.0;
			double calAmount2=0.0;

			//If recomputed, we retieve value from slab, otherwise from previous payslip
			if(sal.getIsRecomputed()=='Y' && sal.getIsAttendanceBased()=='Y')
			{
				if(!isPayscaleChanged)
				{
					if(payStructure.getCurrBasicPay()!=null)
					{
						basicPy = payStructure.getCurrBasicPay();
					}
					else if(payStructure.getDailyPay()!=null)
					{
						basicPy =payStructure.getDailyPay();
					}
					calAmount=getSlabBasedAmount(sal,basicPy,fmt.format(date));
					
					if(payStructure.getCurrBasicPay()!=null)
					{
						calAmount = calAmount*currPay.getNumdays()/currPay.getWorkingDays();					
					}
					else if(payStructure.getDailyPay()!=null)
					{
						calAmount = calAmount*currPay.getNumdays();					
					}
				}
				else //(for Payscalechange)Calculate based on two payscales.				
				{
					double temp1 = 0.0;
					double temp2 = 0.0;
					BigDecimal basicPy1 = BigDecimal.ZERO;
					BigDecimal basicPy2 = BigDecimal.ZERO;
					//Calculate basic for current payscale
					if(payStructure.getCurrBasicPay()!=null)
					{
						basicPy1 = payStructure.getCurrBasicPay();											
						if(prevPayStructure.getCurrBasicPay()!=null)
						{
							basicPy2 = prevPayStructure.getCurrBasicPay();							
						}
					}					
					else if(payStructure.getDailyPay()!=null)
					{
						basicPy1 =payStructure.getDailyPay();											
						if(prevPayStructure.getDailyPay()!=null)
						{
							basicPy2 =prevPayStructure.getDailyPay();													
						}
					}
					//get the slab for two different basic amounts seperately, pro-rate and sum up		
					calAmount1=getSlabBasedAmount(sal,basicPy1,fmt.format(date));					
					calAmount2=getSlabBasedAmount(sal,basicPy2,fmt.format(date));
					if(payStructure.getCurrBasicPay()!=null)
					{							
						calAmount1 	=	calAmount1*(((Double)daysMap.get("noOfPaidDays_after")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue());
						calAmount2 	=	calAmount2*(((Double)daysMap.get("noOfPaidDays_before")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue());
						calAmount	= 	calAmount1+calAmount2;
					}
					else if(payStructure.getDailyPay()!=null)
					{							
						temp1  = calAmount1*((Double)daysMap.get("noOfPaidDays_after")).doubleValue();
						temp2  = calAmount2*((Double)daysMap.get("noOfPaidDays_before")).doubleValue();
						calAmount	= 	calAmount1+calAmount2;
					}
				}
			}
			if(sal.getIsRecomputed()=='Y' && sal.getIsAttendanceBased()!='Y')
			{
				basicPy=getBasicAmount(earningSet); //basic amount is already pro-rated
				calAmount=getSlabBasedAmount(sal,basicPy,fmt.format(date));
			}
			//here we are copying from the previous month payslip, no changes based on payscale change
			else if(sal.getIsRecomputed()!='Y')
			{						
				calAmount=getEarningAmountBySalarycode(lastPayearningses,payscaleEar.getSalaryCodes().getId());
				Date enddate;
				if(lastPay.getFromDate()==null){
					enddate = getEndDateOfMonthByMonthAndFinYear(lastPay.getMonth().intValue(),lastPay.getFinancialyear().getId().intValue());
				}
				else
				{
					enddate = lastPay.getToDate();
				}
				//TODO Check if paystructure is null (But this checking is not needed because we already checked before calling this API)????
				PayStructure lastPayStructure = getPayStructureForEmpByDate(currPay.getEmployee().getIdPersonalInformation(),enddate);
				if(null == lastPayStructure)
					throw new EGOVRuntimeException("Payscale not defined for previous month.");
				if(sal.getIsAttendanceBased()=='Y')
				{
					double tempAmount =0.0;
					if(lastPayStructure.getCurrBasicPay()!=null)
					{
						tempAmount=calAmount*lastPay.getWorkingDays()/lastPay.getNumdays();
						calAmount=tempAmount*currPay.getNumdays()/currPay.getWorkingDays();
					}
					else if(lastPayStructure.getDailyPay()!=null)
					{
						tempAmount=calAmount/lastPay.getNumdays();
						calAmount=tempAmount*currPay.getNumdays();

					}
				}					
				
			}
			
			//we wnt consider attendance for daily rate 
			/*if(sal.getIsRecomputed()=='Y' && sal.getIsAttendanceBased()=='Y'&& payStructure.getCurrBasicPay()!=null){
					calAmount=(calAmount*currPay.getNumdays())/currPay.getWorkingDays();
				}
				else if(sal.getIsRecomputed()=='Y' && sal.getIsAttendanceBased()=='Y'&& payStructure.getDailyPay()!=null)
				{
					calAmount=(calAmount*currPay.getNumdays());
				}*/

			return calAmount;
			//grossPayTotal = grossPayTotal.add(temp3);
			}
			else{
				throw new PayslipFailureException("Slab definition not defined for "+sal.getHead());
			}		
	}

	private double getearningAmountForSlabBasedFromEarning(PayStructure payStructure,SalaryCodes sal
			,EmpPayroll currPay,Earnings ear,Date date,EmpPayroll lastPay,Set earningsSet,Map daysMap,PayStructure prevPayStructure, boolean isPayscaleChanged)throws Exception
			{ 

		BigDecimal basicPy = BigDecimal.ZERO;
		SimpleDateFormat fmt = new SimpleDateFormat(DATEFORMATESTR2,Locale.getDefault());
		double amount=0.0;

		//If recomputed, we retieve value from slab, otherwise from previous payslip
		if(sal.getIsRecomputed()=='Y' && sal.getIsAttendanceBased()=='Y')
		{
			if(!isPayscaleChanged)
			{
				if(payStructure.getCurrBasicPay()!=null)
				{
					basicPy = payStructure.getCurrBasicPay();
				}
				else if(payStructure.getDailyPay()!=null)
				{
					basicPy =payStructure.getDailyPay();
				}
	
				amount=getSlabBasedAmount(sal,basicPy,fmt.format(date));
				if(payStructure.getCurrBasicPay()!=null)
				{
					amount = amount*currPay.getNumdays()/currPay.getWorkingDays();
				}
				else if(payStructure.getDailyPay()!=null)
				{
					amount = amount*currPay.getNumdays();
				}
			}
			else//(for Payscalechange)Calculate based on two payscales.
			{
				double temp1 = 0.0;
				double temp2 = 0.0;
				BigDecimal basicPy1 = BigDecimal.ZERO;
				BigDecimal basicPy2 = BigDecimal.ZERO;
				//Calculate basic for current payscale
				if(payStructure.getCurrBasicPay()!=null)
				{
					basicPy1 = payStructure.getCurrBasicPay();											
					if(prevPayStructure.getCurrBasicPay()!=null)
					{
						basicPy2 = prevPayStructure.getCurrBasicPay();							
					}
				}					
				else if(payStructure.getDailyPay()!=null)
				{
					basicPy1 =payStructure.getDailyPay();											
					if(prevPayStructure.getDailyPay()!=null)
					{
						basicPy2 =prevPayStructure.getDailyPay();													
					}
				}
				//get the slab for two different basic amounts seperately, pro-rate and sum up		
				double calAmount1 = getSlabBasedAmount(sal,basicPy1,fmt.format(date));					
				double calAmount2 = getSlabBasedAmount(sal,basicPy2,fmt.format(date));
				if(payStructure.getCurrBasicPay()!=null)
				{							
					calAmount1 	=	calAmount1*(((Double)daysMap.get("noOfPaidDays_after")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue());
					calAmount2 	=	calAmount2*(((Double)daysMap.get("noOfPaidDays_before")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue());
					amount	= 	calAmount1+calAmount2;
				}
				else if(payStructure.getDailyPay()!=null)
				{							
					temp1  = calAmount1*((Double)daysMap.get("noOfPaidDays_after")).doubleValue();
					temp2  = calAmount2*((Double)daysMap.get("noOfPaidDays_before")).doubleValue();
					amount	= 	calAmount1+calAmount2;
				}
			}
		}
		if(sal.getIsRecomputed()=='Y' && sal.getIsAttendanceBased()!='Y')
		{
			basicPy=getBasicAmount(earningsSet); //basic amount is already pro-rated
			amount=getSlabBasedAmount(sal,basicPy,fmt.format(date));
		}
		//Slab based earnings
		//Double curerrentbasic=Math.round((currPay.getBasicPay().doubleValue()*currPay.getNumdays())/currPay.getWorkingDays());
		/*double amount=0.0;
			if(sal.getIsRecomputed()=='Y')
				amount=getSlabBasedAmount(sal,new BigDecimal(currPay.getBasicPay().doubleValue()),fmt.format(date));*/
		//here we are copying from the previous month payslip, no changes based on payscale change
		else if(sal.getIsRecomputed()!='Y')
		{
			amount=ear.getAmount().doubleValue();
			//calAmount=getEarningAmountBySalarycode(lastPayearningses,payscaleEar.getSalaryCodes().getId());
			Date enddate;
			if(lastPay.getFromDate()==null){
				enddate = getEndDateOfMonthByMonthAndFinYear(lastPay.getMonth().intValue(),lastPay.getFinancialyear().getId().intValue());
			}else
			{
				enddate = lastPay.getToDate();
			}
			//TODO Check if paystructure is null (But this checking is not needed because we already checked before calling this API)????
			PayStructure lastPayStructure = getPayStructureForEmpByDate(currPay.getEmployee().getIdPersonalInformation(),enddate);
			if(null == lastPayStructure)
				throw new EGOVRuntimeException("Payscale not defined for previous month.");
			if(sal.getIsAttendanceBased()=='Y')
			{
				double tempAmount =0.0;
				if(lastPayStructure.getCurrBasicPay()!=null)
				{
					tempAmount=amount*lastPay.getWorkingDays()/lastPay.getNumdays();
					amount=tempAmount*currPay.getNumdays()/currPay.getWorkingDays();
				}
				else if(lastPayStructure.getDailyPay()!=null)
				{
					tempAmount=amount/lastPay.getNumdays();
					amount=tempAmount*currPay.getNumdays();

				}
			}			
		}
		return amount;
	}

	private BigDecimal getEarningAmountForPayheadByDate(PayStructure paystructre,EmpPayroll lastPay,SalaryCodes sal,PayScaleDetails payscaleEar,
			EmpPayroll currPay,Earnings currEar,Set earningsSet,Map daysMap,PayStructure prevPayStructure, boolean isPayscaleChanged)
	{
		try
		{
			PayGenUpdationRule payRule=null;				
			BigDecimal pct = BigDecimal.ZERO;
			BigDecimal pct1 = BigDecimal.ZERO;
			BigDecimal pct2 = BigDecimal.ZERO;
			BigDecimal computedPayheadAmount = BigDecimal.ZERO;

			Date lastPayslipDate = null;

			Boolean usePayscaleDetails = false;
			Boolean usePayrule	= false;
			Date payStructreDate = paystructre.getEffectiveFrom();
			Date payRuleEffDate = null;
			double daysbefore = 0.0;
			double daysafter = 0.0;
			double paidDaysBefore = 0.0;
			double paidDaysAfter = 0.0;
			if(lastPay!=null)
			{
				lastPayslipDate = lastPay.getFromDate();
			}
			usePayscaleDetails = payStructreDate.compareTo(lastPayslipDate) > 0 ? true:false;
			//Check whether bulk updation rule is the latest
			PayheadService payheadService = PayrollManagersUtill.getPayheadService();
			if(sal.getHead().equalsIgnoreCase("DA"))
			{
				payRule=payheadService.checkRuleBasedOnSalCodeMonFnYrEmpGrp(sal.getId(), currPay.getMonth().intValue(),currPay.getFinancialyear().getId().intValue(),currPay.getEmployee().getGroupCatMstr().getId().intValue());
			}	
			else
			{
				payRule=payheadService.checkRuleBasedOnSalCodeMonFnYrEmpGrp(sal.getId(), currPay.getMonth().intValue(),currPay.getFinancialyear().getId().intValue(),null);
			}
			if(payRule != null)
			{
				//Payrule effective date is assumed as start date of the payrule month and financial year.
				//For Ex. If month=september and finyear=2011-12 then payRuleEffDate = 01/09/2011
				//TODO: We need to capture effective date in EGPAY_PAYGENRULESSETUP_MSTR table instead of month	
				payRuleEffDate = getStartDateOfMonthByMonthAndFinYear(payRule.getMonth().intValue(),payRule.getFinancialyear().getId().intValue());
				Calendar cal = Calendar.getInstance();
				cal.setTime(payRuleEffDate);
				//Subtract one day from effective date to get proper count of days before and after
				cal.add(Calendar.DATE, -1);
				Date oneDayBefore_payRuleEffDate = cal.getTime();	
				//if payRuleEffDate is greater than fromdate calculate daysbefore effective date
				if(payRuleEffDate.compareTo(currPay.getFromDate()) > 0)
					daysbefore =  getWorkingDaysbetweenTwoDates(currPay.getFromDate(), oneDayBefore_payRuleEffDate,currPay);
				daysafter =  getWorkingDaysbetweenTwoDates(payRuleEffDate,currPay.getToDate(),currPay);
				paidDaysBefore	= 	getNoOfDaysbetweenTwoDates(currPay.getFromDate(), oneDayBefore_payRuleEffDate,currPay);
				paidDaysAfter	=	getNoOfDaysbetweenTwoDates(payRuleEffDate,currPay.getToDate(),currPay);
				usePayrule=payRuleEffDate.compareTo(payStructreDate) > 0 ? true:false;
			}

			Set<Earnings> lastPayearningses = new HashSet<Earnings>(lastPay.getEarningses());
			Double earAmount = 0d;
			Double earAmount1 = 0d;
			Double earAmount2 = 0d;
			//Previously "For mothly flat rates isattendencebased,isrecompute not considered and it always copies the values from old payslip"
			//Presently "For mothly flat rates isattendencebased is considered and in case of lastpayslip 
			//it will calculate original value by last month attendance"				
			if(sal.getCalType().equalsIgnoreCase(MONTHLYFLATERATESTR) && sal.getCaptureRate()!='Y')
			{									
				//If pay head is not available in old payslip load data from paystructure
				//TODO: if usePayscaleDetails, then earAmount from payscale otherwise this
				if(usePayrule)
				{
					//Geeting amount from payRule
					logger.debug("inside payroll loop>>>>>>>>>>>>>>>>"+payRule.getMonthlyAmt());
					//pro-rate the amount for two periods depending on rule effective date irrespective of payscale change																				
					//get the pro-rated amount for the no.of days before the payRuleEffDate
					if(getEarningAmountBySalarycode(lastPayearningses,sal.getId()) != 0)
					{
						//Getting amount from lastPayslip if that payhead is exists in lastPayslip
						earAmount1 = getEarningAmountBySalarycode(lastPayearningses,sal.getId());
						double tempAmount =0.0;
						tempAmount=earAmount1*lastPay.getWorkingDays()/lastPay.getNumdays();																
						earAmount1=tempAmount*daysbefore/currPay.getWorkingDays();					
					}
					earAmount2 = payRule.getMonthlyAmt().doubleValue()*daysafter/currPay.getWorkingDays();
					if(sal.getIsAttendanceBased()=='Y') 
					{
						earAmount1 = earAmount1*paidDaysBefore/daysbefore;
						earAmount2 = earAmount2*paidDaysAfter/daysafter;
					}
					earAmount = (double)Math.round(earAmount1.doubleValue()+earAmount2.doubleValue());
				}
				else if(usePayscaleDetails && payscaleEar!=null && payscaleEar.getAmount() != null)//i>1
				{
					//getting amount from  payscale if payscale is latest than lastpayslip 
					//TODO: (for PayscaleChange)pro-rate the amount for two payscales depending on effective from date		
					//FIXME : Why not checking whether payhead is attendance based here? Check with Satish
					if(!isPayscaleChanged)
					{
						earAmount = payscaleEar.getAmount().doubleValue();
						if(sal.getIsAttendanceBased()=='Y') 
						{
							earAmount = (double)Math.round(earAmount*currPay.getNumdays()/currPay.getWorkingDays());
						}
					}
					else
					{
						earAmount1 = payscaleEar.getAmount().doubleValue()*(((Double)daysMap.get("noOfWorkingDays_after")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue());
						PayScaleHeader payheader = prevPayStructure.getPayHeader();
						Set<PayScaleDetails> payscaledetailsSet = payheader.getPayscaleDetailses();	
						if(payscaledetailsSet != null && !payscaledetailsSet.isEmpty())
						{
							for(PayScaleDetails payScaleDetails : payscaledetailsSet)
							{
								if(payScaleDetails.getSalaryCodes().getId() == sal.getId())
								{
									earAmount2 	=	payScaleDetails.getAmount().doubleValue();			
									earAmount2	=	earAmount2*(((Double)daysMap.get("noOfWorkingDays_before")).doubleValue()/((Double)daysMap.get("noOfWorkingDays")).doubleValue());
								}
							}
						}
						if(sal.getIsAttendanceBased()=='Y') 
						{
							earAmount1	=	earAmount1*(((Double)daysMap.get("noOfPaidDays_after")).doubleValue()/((Double)daysMap.get("noOfWorkingDays_after")).doubleValue());
							earAmount2	=	earAmount2*(((Double)daysMap.get("noOfPaidDays_before")).doubleValue()/((Double)daysMap.get("noOfWorkingDays_before")).doubleValue());
						}
						earAmount 	=	(double) Math.round(earAmount1.doubleValue()+earAmount2.doubleValue());
					}
				}
				//Poornima  (for PayscaleChange)- I think no need to change here as per payscale change. Becoz this payhead exists only in last month payslip.
				// This code has nothing to do with payscale change. It is handled in above usePayscaleDetails block.
				else if(getEarningAmountBySalarycode(lastPayearningses,sal.getId()) != 0)
				{
					//Getting amount from lastPayslip if that payhead is exists in lastPayslip
					earAmount = getEarningAmountBySalarycode(lastPayearningses,sal.getId());
					//Recompute full monthlyflatrate based on prev payslip's attendance
					if(sal.getIsAttendanceBased()=='Y') {
						//earAmount = (earAmount/lastPay.getNumdays()) * lastPay.getWorkingDays();
						Date enddate;
						if(lastPay.getFromDate()==null){
							enddate = getEndDateOfMonthByMonthAndFinYear(lastPay.getMonth().intValue(),lastPay.getFinancialyear().getId().intValue());
						}else
						{
							enddate = lastPay.getToDate();
						}
						//TODO Check if paystructure is null (But this checking is not needed because we already checked before calling this API)????
						PayStructure lastPayStructure = getPayStructureForEmpByDate(currPay.getEmployee().getIdPersonalInformation(),enddate);
						if(null == lastPayStructure)
							throw new EGOVRuntimeException("Payscale not defined for previous month.");
						//if(sal.getIsAttendanceBased()=='Y')
						//{
							double tempAmount =0.0;
							if(lastPayStructure.getCurrBasicPay()!=null)
							{
								tempAmount=earAmount*lastPay.getWorkingDays()/lastPay.getNumdays();
								earAmount=tempAmount*currPay.getNumdays()/currPay.getWorkingDays();
							}
							else if(lastPayStructure.getDailyPay()!=null)
							{
								tempAmount=earAmount/lastPay.getNumdays();
								earAmount=tempAmount*currPay.getNumdays();
							}
						//}
					}						
				}
				else{
					//Getting amount from payscale if particular payhead is not there in lastPayslip
					if(payscaleEar != null){
						earAmount = payscaleEar.getAmount().doubleValue();
					}
				}
				Double attendanceBasedAmt = earAmount;
				//if(sal.getIsAttendanceBased()=='Y') {

				//attendanceBasedAmt = (earAmount*currPay.getNumdays())/currPay.getWorkingDays(); 
				//}
				currEar.setAmount(new BigDecimal(Math.round(attendanceBasedAmt)));
			}
			else if(sal.getCalType().equalsIgnoreCase("ComputedValue"))
			{
				double baseamt = 0.0;

				BigDecimal pctValue=null;
				BigDecimal pctValue1=null;
				BigDecimal pctValue2=null;				
				if(usePayrule)
				{
					//Getting pctValue from payRule
					//TODO: calculate the pct for two periods depending on rule effective date						
					logger.debug("inside payroll loop>>>>>>>>>>>>>>>>"+payRule.getMonthlyAmt());
					//get the pro-rated amount for the no.of days before the payRuleEffDate
					if(getEarningAmountBySalarycode(lastPayearningses,sal.getId()) != 0)
					{
						//Getting percent value from lastPayslip if that payhead exists in lastPayslip
						pctValue1 =  getPctValueforSalaryCode(sal.getId(),lastPayearningses);														
					}
					pctValue2=payRule.getPercentage();						
				}
				else if(usePayscaleDetails && payscaleEar!=null && payscaleEar.getPct() != null && payscaleEar.getPct().intValue() != 0 )
				{
					//Computed earnings will get the percentage value from  payscale if payscale is latest than lastpayslip 
					//TODO: (for PayscaleChange)get the pct for two payscales depending on effective from date	
					if(!isPayscaleChanged)
					{
						pctValue=payscaleEar.getPct();
					}
					else
					{
						if(getEarningAmountBySalarycode(lastPayearningses,sal.getId()) != 0)
						{
							//Getting percent value from lastPayslip if that payhead exists in lastPayslip
							pctValue1 = getPctValueforSalaryCode(sal.getId(),lastPayearningses);														
						}
						pctValue2=payscaleEar.getPct();
					}
				}
				else if(getPctValueforSalaryCode(sal.getId(),lastPayearningses) != null)
				{
					//Getting pctValue from lastPayslip if that payhead is exists in lastPayslip
					pctValue = getPctValueforSalaryCode(sal.getId(),lastPayearningses);
				}
				else
				{
					//Getting pctValue from payscale if particular payhead is not there in lastPayslip  
					//TODO: (for PayscaleChange) pro-rate the amount based on effective from date
					pctValue = payscaleEar.getPct();
				}
				if(pctValue != null)
				{
					pct = pctValue.divide(new BigDecimal(100));
				}
				else if(pctValue1 != null || pctValue2 != null)
				{
					if(pctValue1 != null)
						pct1 = pctValue1.divide(new BigDecimal(100));
					if(pctValue2 != null)
						pct2 = pctValue2.divide(new BigDecimal(100));
				}
				else
				{
					//If pctValue not there in any of the place,throwing exception 
					throw new PayslipFailureException("Percentage value not defined for-------- "+sal.getHead());
				}
				logger.debug(">>>>>>>getEarningAmountBySalarycode(earningsSet,sal.getSalaryCodes().getId())"+getEarningAmountBySalarycode(earningsSet,sal.getSalaryCodes().getId()));
				logger.debug(">>>>>>>>>>sal.getSalaryCodes().getId()"+sal.getSalaryCodes().getId());			
				if(pctValue2 != null)
				{
					currEar.setPct(pctValue2); //this is the latest percentgae, if two payscales exists
				}
				else
					currEar.setPct(pctValue);
				//for nt attendance based we will get the calculated value from current earning.
				//TODO: (for PayscaleChange)calculate pct for two payscales depending on effective from date	
				if(sal.getIsAttendanceBased()!='Y') 
				{
					//FIXME: How do we get the baseamt from earningsSet ? current earningsSet is empty rt?
					baseamt = getEarningAmountBySalarycode(earningsSet,sal.getSalaryCodes().getId());
					computedPayheadAmount = new BigDecimal(Math.round(pct.doubleValue()*baseamt));
				}
				//If salary code is attendence base prorate the earnings amount as per attendence nd mnthhly or daily
				//can we take the attendance check out we r already gettng the calculated basic amount 
				//TODO: (for PayscaleChange)calculate pct for two payscales depending on effective from date	
				if (sal.getIsAttendanceBased()=='Y' && paystructre.getDailyPay()!=null)
				{
					if(!isPayscaleChanged && !usePayrule)
					{
						baseamt = paystructre.getDailyPay().doubleValue();
						double tempAmt=0.0;
						tempAmt=Math.round(pct.doubleValue()*baseamt);
						computedPayheadAmount= new BigDecimal(Math.round(tempAmt*currPay.getNumdays()));																								
					}
					else if(usePayrule)
					{
						//pro-rate and sum up for two periods.
						baseamt = paystructre.getDailyPay().doubleValue();						
						double tempAmt1=0.0;
						double tempAmt2=0.0;
						tempAmt1=Math.round(pct1.doubleValue()*baseamt);
						tempAmt2=Math.round(pct2.doubleValue()*baseamt);
						computedPayheadAmount= new BigDecimal(Math.round(tempAmt1*daysbefore+tempAmt2*daysafter));	
					}
					else
					{
						//pro-rate and sum up for two periods.
						double tempAmt1=0.0;
						double tempAmt2=0.0;							
						if(prevPayStructure.getDailyPay() != null)
						{
							double baseamt1 = prevPayStructure.getDailyPay().doubleValue();
							tempAmt2=Math.round(pct1.doubleValue()*baseamt1);
						}
						double baseamt2 = paystructre.getDailyPay().doubleValue();	
						tempAmt1=Math.round(pct2.doubleValue()*baseamt2);					
						computedPayheadAmount= new BigDecimal(Math.round(tempAmt1*((Double)daysMap.get("noOfPaidDays_before")).doubleValue()+tempAmt2*((Double)daysMap.get("noOfPaidDays_after")).doubleValue()));	
					}

				}
				else  if(sal.getIsAttendanceBased()=='Y' && paystructre.getCurrBasicPay()!=null)
				{
					if(!isPayscaleChanged && !usePayrule)
					{
						baseamt = paystructre.getCurrBasicPay().doubleValue();
						double tempAmt=0.0;
						tempAmt=Math.round(pct.doubleValue()*baseamt);
						computedPayheadAmount= new BigDecimal(Math.round(tempAmt*currPay.getNumdays()/currPay.getWorkingDays()));
					}
					else if(usePayrule)
					{
						baseamt = paystructre.getCurrBasicPay().doubleValue();
						double tempAmt1=0.0;
						double tempAmt2=0.0;
						tempAmt1=Math.round(pct1.doubleValue()*baseamt);
						tempAmt2=Math.round(pct2.doubleValue()*baseamt);
						computedPayheadAmount= new BigDecimal(Math.round(tempAmt1*daysbefore/currPay.getWorkingDays()+tempAmt2*daysafter/currPay.getWorkingDays()));
					}
					else
					{						
						double tempAmt1=0.0;
						double tempAmt2=0.0;
						if(prevPayStructure.getCurrBasicPay() != null)
						{
							double baseamt1 = prevPayStructure.getCurrBasicPay().doubleValue();
							tempAmt1=Math.round(pct1.doubleValue()*baseamt1);
						}
						double baseamt2 = paystructre.getCurrBasicPay().doubleValue();						
						tempAmt2=Math.round(pct2.doubleValue()*baseamt2);
						computedPayheadAmount= new BigDecimal(Math.round(tempAmt1*((Double)daysMap.get("noOfPaidDays_before")).doubleValue()/((Double)daysMap.get("noOfWorkingDays_before")).doubleValue()+tempAmt2*((Double)daysMap.get("noOfPaidDays_after")).doubleValue()/((Double)daysMap.get("noOfWorkingDays_after")).doubleValue()));
					}
				}

				currEar.setAmount(computedPayheadAmount);
			}

		}
		catch(Exception e)
		{
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
		return currEar.getAmount();
	}


	public BigDecimal getPctValueforSalaryCode(Integer id,Set<Earnings> lastearnings)
	{
		for (Earnings ear:  lastearnings) {
			if(ear.getSalaryCodes().getId().intValue()==id.intValue())
			{
				return ear.getPct();
			}
		}
		return null;
	}

	public CFinancialYear getFinancialYearByDate(Date date)
	{
		try{
			String query="from CFinancialYear c where c.startingDate<= :date and c.endingDate>= :date ";
			Query qry = HibernateUtil.getCurrentSession().createQuery(query);
			qry.setDate("date",date);
			return (CFinancialYear)qry.uniqueResult();
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}

	public EmpPayroll getPrevApprovedSuppPayslipForEmpByDates(GregorianCalendar fromDate,GregorianCalendar toDate,Integer empid) throws Exception
	{
		try{
			EmpPayroll empPayroll=new EmpPayroll();
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			empPayroll = (EmpPayroll) empPayrollDAO.getPrevApprovedSuppPayslipForEmpByDates(fromDate,toDate,empid);
			return (empPayroll);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public List getPendingPaySlipsList(Integer finyr,Integer month,Integer deptid,Integer empid,Integer type,Integer functionaryId,Integer billNo,Integer errPay)
	{
		try{
			List failuredetails=new ArrayList();
			BatchFailureDetailsDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getBatchFailureDetailsDAO();
			failuredetails = (ArrayList) empPayrollDAO.getPendingPaySlipsList(finyr,month,deptid,empid,type,functionaryId,billNo,errPay);
			return (failuredetails);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			logger.error("Excepton in Bean ::: "+e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	/*
	public void updateIsHistory(Integer month,Integer year,Integer deptid,Integer functionaryId, Integer billNumberId)
	{
		try{
			BatchFailureDetailsDAO batchFailurDao = PayrollDAOFactory.getDAOFactory().getBatchFailureDetailsDAO();
			batchFailurDao.updateIsHistory(month,year,deptid,functionaryId,billNumberId);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}*/
	public void updateIsHistory(Integer month,Integer year,List empIdsList)
	{
		try{
			BatchFailureDetailsDAO batchFailurDao = PayrollDAOFactory.getDAOFactory().getBatchFailureDetailsDAO();
			batchFailurDao.updateIsHistory(month,year,empIdsList);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public String getEmpInfoByLastAssignment(Integer empid)
	{
		try{

			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			return (String) empPayrollDAO.getEmpInfoByLastAssignment(empid);
		} catch (Exception e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public boolean resolvePaySlipFailure(Integer empid,Integer month,Integer year,Integer paytype)
	{
		try{
			BatchFailureDetailsDAO batchFailureDAO = PayrollDAOFactory.getDAOFactory().getBatchFailureDetailsDAO();
			return (boolean) batchFailureDAO.resolvePaySlipFailure(empid,month,year,paytype);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public double getSlabBasedAmount(SalaryCodes salaryCode,BigDecimal basicsalary,String date) throws Exception
	{
		try{
			PayrollExternalInterface payrollExternalInterface = getPayrollExterInterface();
			ArrayList<org.egov.model.recoveries.EgDeductionDetails> deductionlist;
			double amount=0;
			/*SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMATESTR1);
			Calendar d = Calendar.getInstance();
			d.setTime(date)*/
			deductionlist= (ArrayList)payrollExternalInterface.getEgDeductionDetailsFilterBy(salaryCode.getTdsId(),basicsalary,date,null,null);
			//int j=0;
			// it will load the tax amount
			for(EgDeductionDetails deduction:deductionlist)
			{
				if(deduction.getFlatAmount()!=null)
				{
					//dedTaxamountlist.append(Math.round(deduction.getFlatAmount().doubleValue()));
					amount=deduction.getFlatAmount().doubleValue();
					if(deduction.getRecovery().getCaplimit()!=null && deduction.getRecovery().getCaplimit().doubleValue() < amount )
					{
						amount = deduction.getRecovery().getCaplimit().doubleValue() ;
					}
					break;
				}
				else
				{
					if (basicsalary == null) {
						logger.debug("Basic salary null!!! --" + salaryCode.getHead());
						throw new PayslipFailureException("Basic salary not defined");
					}
					double amt=(deduction.getIncometax()==null?0:deduction.getIncometax().doubleValue())+
					(deduction.getEducation()==null?0:deduction.getEducation().doubleValue())+
					(deduction.getSurcharge()==null?0:deduction.getSurcharge().doubleValue());
					amt=(amt * basicsalary.doubleValue() )/ 100 ;
					//dedTaxamountlist.append(Math.round(amt));
					amount=amt;
					if(deduction.getRecovery().getCaplimit()!=null && deduction.getRecovery().getCaplimit().doubleValue() < amount )
					{
						amount = deduction.getRecovery().getCaplimit().doubleValue() ;
					}
					break;
				}

			}
			return Math.round(amount);
		} catch (Exception e) {
			//HibernateUtil.rollbackTransaction();
			//throw new EGOVRuntimeException(e.getMessage(),e);
			logger.debug("Problem in Slab definition!! --"+salaryCode.getHead());
			throw new PayslipFailureException("Problem in Slab definition!! --"+salaryCode.getHead());
		}
	}
	public Date getStartDateOfMonthByMonthAndFinYear(int month,int finyr)
	{
		try {
			PayrollExternalInterface payrollExternalInterface = getPayrollExterInterface();
			SimpleDateFormat formatter = new SimpleDateFormat(DATEFORMATESTR1,Locale.getDefault());
			CFinancialYear financialYear=payrollExternalInterface.
			findFinancialYearById(Long.valueOf(finyr));

			String yeartemp="0";
			if(month<4)
			{
				yeartemp = formatter.format(financialYear.getEndingDate()).split("/")[2];
			}
			else
			{
				yeartemp = formatter.format(financialYear.getStartingDate()).split("/")[2];
			}
			GregorianCalendar fromdate1=new GregorianCalendar(Integer.parseInt(yeartemp), month-1, 1);
			//date=fmt.format(fromdate1.getTime());
			return fromdate1.getTime();
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public Date getEndDateOfMonthByMonthAndFinYear(int month,int finyr)
	{
		try {
			PayrollExternalInterface   payrollExternalInterface = getPayrollExterInterface();
			SimpleDateFormat formatter = new SimpleDateFormat(DATEFORMATESTR1,Locale.getDefault());
			CFinancialYear financialYear=payrollExternalInterface.findFinancialYearById(Long.valueOf(finyr));

			String yeartemp="0";
			if(month<4)
			{
				yeartemp = formatter.format(financialYear.getEndingDate()).split("/")[2];
			}
			else
			{
				yeartemp = formatter.format(financialYear.getStartingDate()).split("/")[2];
			}
			GregorianCalendar fromdate1=new GregorianCalendar(Integer.parseInt(yeartemp), month-1, 1);
			GregorianCalendar fromdate2=new GregorianCalendar(fromdate1.get(Calendar.YEAR), fromdate1.get(Calendar.MONTH), fromdate1.getActualMaximum(Calendar.DATE));
			//date=fmt.format(fromdate1.getTime());
			return fromdate2.getTime();
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	/*
	 * updates the Payslip object with YTD values
	 */
	private EmpPayroll populateYTDForPayslip(EmpPayroll empPayroll){
		try{
			if(empPayroll!=null)
			{
				// compute Previous YTD for each earning type
				List<EmpPayroll> prevPayrollList = getAllPrevPayslipsForEmpByMonthAndYear(empPayroll.getEmployee().getIdPersonalInformation(),
						empPayroll.getMonth().intValue(),
						empPayroll.getFinancialyear().getId().intValue());
				//iterate through each earningType in current payslip
				for(Earnings currentEarning : empPayroll.getEarningses()){
					//initialize YTD value for this earningType
					BigDecimal prevYTDValue = BigDecimal.ZERO;
					//iterate through previous payslips to find the corresponding earning value
					for(EmpPayroll proll : prevPayrollList){
						for(Earnings prevEarning : proll.getEarningses()){
							//add earning from previous payslips to YTD if this earning type
							if(currentEarning.getSalaryCodes().equals(prevEarning.getSalaryCodes()))
							{
								prevYTDValue = prevYTDValue.add(prevEarning.getAmount());
							}
						}
					}
					currentEarning.setPrevYtdAmount(prevYTDValue);
				}
				// repeat for deductions
				for(Deductions currentDeduc : empPayroll.getDeductionses()){
					BigDecimal prevYTDValue =BigDecimal.ZERO;
					for(EmpPayroll proll : prevPayrollList){
						for(Deductions prevDeduc : proll.getDeductionses()){
							if(currentDeduc.getSalaryCodes() != null){
								if(currentDeduc.getSalaryCodes().equals(prevDeduc.getSalaryCodes()))
								{
									prevYTDValue = prevYTDValue.add(prevDeduc.getAmount());
								}
							}
							// handle for other deductions - where salaryCode is not defined
							else if(currentDeduc.getChartofaccounts() != null){
								if(currentDeduc.getChartofaccounts().equals(prevDeduc.getChartofaccounts()))
								{
									prevYTDValue = prevYTDValue.add(prevDeduc.getAmount());
								}
							}
						}
					}
					currentDeduc.setPrevYtdAmount(prevYTDValue);
				}
			}
			return empPayroll;
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public EmpPayroll getPrevApprovedPayslipForEmpByMonthAndYear(Integer empId, Integer month,Integer finYear) throws Exception
	{
		try{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();

			return empPayrollDAO.getPrevApprovedPayslipForEmpByMonthAndYear(empId, month, finYear);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public BigDecimal getPendingIncrementAmount(Integer empid,Integer month,Integer finYear)
	{
		try{
			IncrementDetailsDAO incrementDAO = PayrollDAOFactory.getDAOFactory().getIncrementDetailsDAO();

			return incrementDAO.getPendingIncrementAmount(empid, month, finYear);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public boolean resolvePendingIncr(Integer empid,Integer month,Integer year)
	{
		try{
			IncrementDetailsDAO incrementDAO = PayrollDAOFactory.getDAOFactory().getIncrementDetailsDAO();
			incrementDAO.resolvePendingIncr(empid, month, year);
			return true;
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	public BatchGenDetails insertBatchGenDetails(BatchGenDetails batchgenobj)
	{
		try{
			BatchGenDetailsDAO batchDAO = PayrollDAOFactory.getDAOFactory().getBatchGenDetailsDAO();
			//incrementDAO.create(batchgenobj);
			return (BatchGenDetails)batchDAO.create(batchgenobj);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			logger.error(e.getMessage());
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public BatchGenDetails getBatchGenDetailsById(Long id)
	{
		try{
			BatchGenDetailsDAO batchDAO = PayrollDAOFactory.getDAOFactory().getBatchGenDetailsDAO();
			//incrementDAO.create(batchgenobj);
			return (BatchGenDetails)batchDAO.findById(id,false);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	public void updateBatchGenDetals(BatchGenDetails batchgenobj)
	{
		try{
			BatchGenDetailsDAO batchDAO = PayrollDAOFactory.getDAOFactory().getBatchGenDetailsDAO();
			//incrementDAO.create(batchgenobj);
			batchDAO.update(batchgenobj);
			return ;
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	public String checkExistingPayscaleInPayslip(PayScaleHeader payScaleHeader)throws Exception{
		try{
			PayStructureDAO paystrucDAO = PayrollDAOFactory.getDAOFactory().getPayStructureDAO();

			return paystrucDAO.checkExistingPayStructureInPayslip(payScaleHeader);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	public PayTypeMaster getPayTypeMasterByPaytype(String paytype)
	{
		try{
			PayTypeMasterDAO paymstrDAO = PayrollDAOFactory.getDAOFactory().getPayTypeMasterDAO();

			return paymstrDAO.getPayTypeMasterByPaytype(paytype);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	public List getAllPayTypes()
	{
		try{
			PayTypeMasterDAO paymstrDAO = PayrollDAOFactory.getDAOFactory().getPayTypeMasterDAO();
			return paymstrDAO.findAll();
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	protected PayTypeMaster getPayTypeById(PayTypeMasterDAO paymstrDAO, Integer id){
		//PayTypeMasterDAO paymstrDAO = PayrollDAOFactory.getDAOFactory().getPayTypeMasterDAO();
		return (PayTypeMaster)paymstrDAO.findById(id,false);
	}

	public PayTypeMaster getPayTypeById(Integer id){
		return getPayTypeById(PayrollDAOFactory.getDAOFactory().getPayTypeMasterDAO(), id);
	}

	public boolean isEmphasFailureForMonthFinyrPaytype(Integer empid,Integer month,Integer year,Integer paytype)
	{
		try{
			BatchFailureDetailsDAO batchFailureDAO = PayrollDAOFactory.getDAOFactory().getBatchFailureDetailsDAO();
			return (boolean) batchFailureDAO.isEmphasFailureForMonthFinyrPaytype(empid,month,year,paytype);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	/**
	 * this api will return the earning amount for given salarycode id
	 * @param earningsset
	 * @param salcodeId
	 * @return
	 */
	private double getEarningAmountBySalarycode(Set earningsset,Integer salcodeId)
	{
		// double amount=0;
		Iterator itr=earningsset.iterator();
		while(itr.hasNext())
		{
			Earnings ear=(Earnings)itr.next();
			if(ear.getSalaryCodes().getId().intValue()==salcodeId.intValue())
			{
				return ear.getAmount().doubleValue();
			}
		}
		return 0;

	}


	public BigDecimal getTotalDeductionAmount(Integer employeeId, CChartOfAccounts accountCode, Date toDate) throws Exception{
		try{
			CFinancialYear finYear =  PayrollManagersUtill.getPayRollService().getFinancialYearByDate(toDate);
			EmpPayrollDAO payrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			return payrollDAO.getTotalDeductionAmount(employeeId, accountCode, toDate, finYear);
		}catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	public List<String> GetListOfDeductionAmount(Integer idEmployee, CChartOfAccounts accountCode, Date fromDate, Date toDate)throws Exception{
		try{
			EmpPayrollDAO payrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			return payrollDAO.GetListOfDeductionAmount(idEmployee, accountCode, fromDate, toDate);
		}catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	/*
	 *	Return latest drawn payslip for passing employee
	 */
	public EmpPayroll getLatestPayslipByEmp(PersonalInformation employee)throws Exception{
		try{
			return PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO().getLatestPayslipByEmp(employee);
		}catch(Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}

	/*
	 * Return last payscale for employee
	 */
	public PayScaleHeader getLastPayscaleByEmp(Integer empId)throws Exception{
		try{
			return PayrollDAOFactory.getDAOFactory().getPayScaleHeaderDAO().getLastPayscaleByEmp(empId);
		}catch(Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}
	public List<HashMap> getEarningsForEmployeeForFinYear(Integer employeeid,Integer finyearid) throws Exception
	{
		try{
			EmpPayrollDAO payrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			return payrollDAO.getEarningsForEmployeeForFinYear(employeeid, finyearid);
		}catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}
	public List<HashMap> getEarningsAndDeductionsForEmpByMonthAndYearRange(Integer employeeid,Date fromDate,Date toDate) throws Exception
	{
		try{
			EmpPayrollDAO payrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			return payrollDAO.getEarningsAndDeductionsForEmpByMonthAndYearRange(employeeid, fromDate,toDate);
		}catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}
	public List<HashMap> getDeductionsForEmployeeForFinYear(Integer employeeid,Integer finyearid) throws Exception
	{
		try{
			EmpPayrollDAO payrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			return payrollDAO.getDeductionsForEmployeeForFinYear(employeeid, finyearid);
		}catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}
	public boolean isPayHeadGrossBased(String payhead)
	{
		//getting from config table instead of config xml file
		//String grossPayheads= EGovConfig.getProperty("payroll_egov_config.xml","DeductionsGrossBased", "", EGOVThreadLocals.getDomainName()+".PaySlip");
		String grossPayheads= GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","DeductionsGrossBased",new Date()).getValue();
		String grosspayheadslist[]=grossPayheads.split(",");
		for(int i=0;i<grosspayheadslist.length;i++)
		{
			if((payhead.trim()).equalsIgnoreCase(grosspayheadslist[i]))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Get list of employee id who are normal eligible for pay
	 * @param fromDate
	 * @param toDate
	 * @param deptId
	 * @param functionnaeryId
	 * @return listEmpId
	 */
	public List<Integer> getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid,Integer functionaryId){
		EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();

		return empPayrollDAO.getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(fromdate,todate,deptid,functionaryId);
	}
	
	
	public List<Integer> getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid,Integer functionaryId,Long functionId,Integer billNumberId){
		EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
		return empPayrollDAO.getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(fromdate,todate,deptid,functionaryId,functionId,billNumberId);
	}

	/**
	 *
	 * @param fromdate
	 * @param todate
	 * @param deptid
	 * @param functionaryId
	 * @return
	 */
	public List<Integer> getSuppPayEligibleEmpListBetweenFDateAndTDateByDept(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid,Integer functionaryId)
	{
		EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
		List<Integer> empList = empPayrollDAO.getSuppPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(fromdate,todate,deptid,functionaryId);
		return (empList);
	}
	
	public List<Integer> getSuppPayEligibleEmpListBetweenFDateAndTDateByDept(GregorianCalendar fromdate,GregorianCalendar todate,Integer deptid,Integer functionaryId,Long functionId, Integer billNumberId)
	{
		EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
		List<Integer> empList = empPayrollDAO.getSuppPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(fromdate,todate,deptid,functionaryId,functionId,billNumberId);
		return (empList);
	}

	//added daily pay check also 
	protected Boolean checkPayslipProcessAbilityByPayType(PayStructure payStructure,Assignment assignment, Integer empId,GregorianCalendar fromDate,GregorianCalendar toDate,String payType) throws Exception{
		Boolean processAble = false;
		logger.debug("empid="+empId);
		if(PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL.equalsIgnoreCase(payType)){
			if(payStructure!=null && (payStructure.getCurrBasicPay()!= null || payStructure.getDailyPay()!=null))
			{
				if(assignment !=null  && assignment.getId()!= null ){
					processAble = true;
				}
				else{
					throw new PayslipFailureException("Employee Doesn't Have Assignment for current Period");
				}
			}else{
				throw new PayslipFailureException("Employee Doesn't Have PayStructure for current Period");
			}
		}
		else if(PayrollConstants.EMP_PAYSLIP_PAYTYPE_EXCEPTION.equalsIgnoreCase(payType)){
			if(payStructure!=null && payStructure.getCurrBasicPay()!= null )
			{
				processAble = true;
			}
			else
			{
				throw new PayslipFailureException("Employee Doesn't Have PayStructure for current Period");
			}
		}
		else{ //For supplementary payslips.
			
			if(payStructure!=null )
			{
				if(assignment !=null  && assignment.getId()!= null ){
					processAble = true;
				}
				else{
					throw new PayslipFailureException("Employee Doesn't Have Last Assignment");
				}
			}else{
				throw new PayslipFailureException("Employee Doesn't Have PayStructure for current Period");
			}
		}
		return processAble;
	}

	public Boolean checkPayslipProcessAbilityByPayType(Integer empId,GregorianCalendar fromDate,GregorianCalendar toDate,String payType) throws Exception{
		PayrollExternalInterface payrollExternalInterface=getPayrollExterInterface();
		PayStructure payStructure = getPayStructureForEmpByDate(empId,toDate.getTime());
		Assignment assignment = null;
		
		if(PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL.equalsIgnoreCase(payType) || PayrollConstants.EMP_PAYSLIP_PAYTYPE_EXCEPTION.equalsIgnoreCase(payType)){
			assignment = payrollExternalInterface.getAssignmentByEmpAndDate(toDate.getTime(),empId);
		}
		else //For supplementary payslip.
		{
			assignment = getAssignmentByDateOrByLast(empId,toDate.getTime());
		}
		return checkPayslipProcessAbilityByPayType(payStructure, assignment, empId, fromDate, toDate, payType);
	}
	
	public String getEmpInfoByFDateAndTDate(Integer empid,GregorianCalendar fromdate,GregorianCalendar todate)
	{
		try{

			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			return (String) empPayrollDAO.getEmpInfoByFDateAndTDate(empid,fromdate,todate);
		} catch (Exception e) {
			//HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	//Will return the assignment by date , if not there, then will return last assignment.
	public Assignment getAssignmentByDateOrByLast(Integer empId,Date date)
	{
		Assignment assignment =null;
		assignment =(Assignment)PayrollManagersUtill.getEmployeeService().getAssignmentByEmpAndDate(date,empId);
							
		//To get the last assignment if no assignment there for todate.
		if(assignment==null){
			assignment = PayrollManagersUtill.getEmployeeService().getLastAssignmentByEmp(empId);
		}

		return assignment;
	}
	
	@Deprecated
	public List getPayScaleByGradeAndEffectiveDate(Integer gradeId,Date effectiveDate) throws Exception
	{
		try
		{
			PayScaleHeaderDAO payscaleDao=PayrollDAOFactory.getDAOFactory().getPayScaleHeaderDAO();
			return payscaleDao.getPayScaleByGradeAndEffectiveDate(gradeId, effectiveDate);
		}catch (RuntimeException e) {
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}


	public List getPayScaleByEffectiveDate(Date effectiveDate) throws Exception
	{
		try
		{
			PayScaleHeaderDAO payscaleDao=PayrollDAOFactory.getDAOFactory().getPayScaleHeaderDAO();
			return payscaleDao.getPayScaleByEffectiveDate(effectiveDate);
		}catch (RuntimeException e) {
			HibernateUtil.rollbackTransaction();
			throw new EGOVRuntimeException(e.getMessage(),e);
		}

	}
	public List<EmpPayroll> getAllPayslipByEmpMonthYearBasedOnDept(Integer deptId, Integer month, Integer year,Integer billNo){
		try{
			EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
			return empPayrollDAO.getAllPayslipByEmpMonthYearBasedOnDept(deptId, month, year,billNo);
		} catch (RuntimeException e) {
			//HibernateUtil.rollbackTransaction
			throw new EGOVRuntimeException(e.getMessage(),e);
		}
	}

	/**
	 * 
	 * @param fromDate
	 * @param toDate
	 * @param deptIds
	 * @return
	 * @throws Exception
	 */  	  
	public List<HashMap> getDeptPayheadSummary(Integer month,Integer finYear, Integer deptIds[],Integer billNumberId) throws Exception{
		return PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO().getDeptPayheadSummary( month, finYear,  deptIds,billNumberId);	 
	}

	/**
	 * @param month
	 * @param year
	 * @param functionaryId
	 * @param deptId
	 * @return
	 * @throws Exception
	 */
	public List<HashMap> getFunctionaryDeptWisePayBillSummary(Integer month, Integer year, Integer functionaryId,Integer deptId) throws Exception{
		return PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO().getFunctionaryDeptWisePayBillSummary(month, year, functionaryId,deptId);	 
	}

	public List<HashMap> getFunctionaryPayheadSummary(Integer month, Integer year, Integer functionaryIds[],Integer billNumberId) throws Exception{
		return PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO().getFunctionaryPayheadSummary(month, year, functionaryIds,billNumberId);	 
	}
	
	public List<HashMap> getBankAdviceReportByBillIds(Integer[] billIds) throws Exception{
		return PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO().getBankAdviceReportByBillIds(billIds);
	}

	
	private Double getWorkingDaysbetweenTwoDates(Date fromdate, Date todate, EmpPayroll currpay)
	{
		Float noOfWorkDays = 0.0f;
		if(currpay != null)
		{
			EmployeeAttendenceReport empatcreport = getPayrollExterInterface().getEmployeeAttendenceReportBetweenTwoDates(fromdate,todate,currpay.getEmployee());			  
			noOfWorkDays = empatcreport.getDaysInMonth()==null ? 0 : empatcreport.getDaysInMonth().floatValue();
		}
		return (noOfWorkDays.doubleValue());
	}
	private Double getNoOfDaysbetweenTwoDates(Date fromdate, Date todate, EmpPayroll currpay)
	{
		Float noOfDays = 0.0f;
		if(currpay != null)
		{
			EmployeeAttendenceReport empatcreport = getPayrollExterInterface().getEmployeeAttendenceReportBetweenTwoDates(fromdate,todate,currpay.getEmployee());			  
			noOfDays =  empatcreport.getNoOfPaidDays()==null?0: empatcreport.getNoOfPaidDays(); 					
		}
		return (noOfDays.doubleValue());
	}	
	
	private PayStructure getPrevPayStructureIfPayscaleChanged(PayStructure payStructure, EmpPayroll currPay, Date fromdate,Date effFromDate, Date enddate)
	{
		PayStructure prevPayStructure = null;			
		if(effFromDate.after(fromdate) && effFromDate.before(enddate))
		{
			prevPayStructure = getPayStructureForEmpByDate(currPay.getEmployee().getIdPersonalInformation(),fromdate);				
		}
		return prevPayStructure;
	}
	
	
	private Map getNoOfDaysMap(EmpPayroll currPay, Date fromdate, Date effFromDate, Date enddate)
	{
		Map daysMap = new HashMap();
		Double noOfWorkingDays			=	0.0d;
		Double noOfWorkingDays_before	= 	0.0d;
		Double noOfWorkingDays_after	= 	0.0d;
		Double noOfPaidDays_before		=	0.0d;			
		Double noOfPaidDays_after		=	0.0d;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(effFromDate);
		//Subtract one day from effective date to get proper count of days before and after
		cal.add(Calendar.DATE, -1);
		Date oneDayBefore_effFromDate = cal.getTime();	
			
		noOfWorkingDays	 		= 	currPay.getWorkingDays();
		if(effFromDate.compareTo(fromdate) > 0)
		{
			noOfWorkingDays_before	=	getWorkingDaysbetweenTwoDates(fromdate, oneDayBefore_effFromDate, currPay);
			noOfPaidDays_before		=	getNoOfDaysbetweenTwoDates(fromdate, oneDayBefore_effFromDate, currPay);
		}
		noOfWorkingDays_after	=	getWorkingDaysbetweenTwoDates(effFromDate, enddate, currPay);		
		noOfPaidDays_after		=	getNoOfDaysbetweenTwoDates(effFromDate, enddate, currPay);	
		
		logger.debug("fromdate : "+fromdate+": effFromDate : "+effFromDate+" enddate : "+enddate);
		logger.debug("oneDayBefore_effFromDate = "+oneDayBefore_effFromDate);
		logger.debug("Total noOfWorkingDays = "+noOfWorkingDays);
		logger.debug("Total noOfPaidDays = "+currPay.getNumdays());
		logger.debug("noOfWorkingDays_before = "+noOfWorkingDays_before);
		logger.debug("noOfWorkingDays_after = "+noOfWorkingDays_after);
		logger.debug("noOfPaidDays_before = "+noOfPaidDays_before);
		logger.debug("noOfPaidDays_after = "+noOfPaidDays_after);
	
		daysMap.put("noOfWorkingDays", noOfWorkingDays);
		daysMap.put("noOfWorkingDays_before", noOfWorkingDays_before);
		daysMap.put("noOfWorkingDays_after", noOfWorkingDays_after);
		daysMap.put("noOfPaidDays_before", noOfPaidDays_before);
		daysMap.put("noOfPaidDays_after", noOfPaidDays_after);					
			
		return daysMap;
	}
}
