package org.egov.payroll.services.payslip;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.EgwStatus;
import org.egov.commons.Functionary;
import org.egov.commons.dao.FunctionDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FunctionaryDAO;
import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.models.Script;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.StringUtils;
import org.egov.infstr.workflow.Action;
import org.egov.infstr.workflow.SimpleWorkflowService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.user.User;
import org.egov.payroll.dao.EmpPayrollDAO;
import org.egov.payroll.dao.IncrementDetailsDAO;
import org.egov.payroll.dao.PayStructureDAO;
import org.egov.payroll.dao.PayrollDAOFactory;
import org.egov.payroll.model.BatchFailureDetails;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.IncrementDetails;
import org.egov.payroll.model.IncrementSlabsForPayScale;
import org.egov.payroll.model.PayStructure;
import org.egov.payroll.model.PayTypeMaster;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.payroll.workflow.payslip.PayslipService;
import org.egov.pims.commons.Position;
import org.egov.pims.empLeave.model.EmployeeAttendenceReport;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.hibernate.HibernateException;

/**
 * @author surya
 */
public class PayslipProcessImpl implements IPayslipProcess{
	
	private static final Logger LOGGER = Logger.getLogger(PayslipProcessImpl.class);
	
	private SimpleWorkflowService<EmpPayroll> payslipWorkflowService;
	private PayrollExternalInterface payrollExternalInterface;
	private PayslipService payslipService;
	
	public PayslipService getPayslipService() {
		return payslipService;
	}
	public void setPayslipService(PayslipService payslipService) {
		this.payslipService = payslipService;
	}
	public SimpleWorkflowService<EmpPayroll> getPayslipWorkflowService() {
		return payslipWorkflowService;
	}
	public void setPayslipWorkflowService(SimpleWorkflowService<EmpPayroll> payslipWorkflowService) {
		this.payslipWorkflowService = payslipWorkflowService;
	}
	public void setPayrollExternalInterface(PayrollExternalInterface payrollExternalInterface)
	{
		this.payrollExternalInterface=payrollExternalInterface;
	}
	public PayrollExternalInterface getPayrollExternalInterface()
	{
		return payrollExternalInterface;
	}
	protected void beginTransaction() {
		HibernateUtil.beginTransaction();
	}
	protected void commitTransaction() {
		HibernateUtil.commitTransaction();
	}
	
	 public List<BatchFailureDetails> generateBatchPayslips(GregorianCalendar fromDate,GregorianCalendar toDate,Integer deptid,String username,Integer functionaryId, boolean persist, Position approverPos, Integer billNumberId) throws Exception
	  {
		 	List<BatchFailureDetails> listBatchFailure = new ArrayList<BatchFailureDetails>();
		 	listBatchFailure.addAll(generateBatchPayslipByPayType(fromDate, toDate, deptid, username, functionaryId, persist, PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL, approverPos,null,billNumberId));
		 	listBatchFailure.addAll(generateBatchPayslipByPayType(fromDate, toDate, deptid, username, functionaryId, persist, PayrollConstants.EMP_PAYSLIP_PAYTYPE_EXCEPTION, approverPos,null,billNumberId));
			return listBatchFailure;
	  }
	 
	 public List<BatchFailureDetails> generateBatchPayslips(GregorianCalendar fromDate,GregorianCalendar toDate,Integer deptid,String username,Integer functionaryId, boolean persist, Position approverPos,Long functionId, Integer billNumberId) throws Exception
	  {
		 	List<BatchFailureDetails> listBatchFailure = new ArrayList<BatchFailureDetails>();
		 	listBatchFailure.addAll(generateBatchPayslipByPayType(fromDate, toDate, deptid, username, functionaryId, persist, PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL, approverPos, functionId,billNumberId));
		 	listBatchFailure.addAll(generateBatchPayslipByPayType(fromDate, toDate, deptid, username, functionaryId, persist, PayrollConstants.EMP_PAYSLIP_PAYTYPE_EXCEPTION, approverPos, functionId,billNumberId));
			return listBatchFailure;
	  }
	 
	 protected List<BatchFailureDetails> generateBatchPayslipByPayType(GregorianCalendar fromDate,GregorianCalendar toDate,Integer deptid,String username,Integer functionaryId, boolean persist,String payType, Position approverPos,Long functionId, Integer billNumberId) throws Exception{
		 	List<BatchFailureDetails> listBatchFailure = new ArrayList<BatchFailureDetails>();
		 	beginTransaction();
			LOGGER.debug("fromdate >>>>> " + fromDate.getTime()  +" todate="+toDate.getTime()+" deptid = "+deptid+" username="+username+"functionaryId"+functionaryId);
			PayRollService payRollService = PayrollManagersUtill.getPayRollService();
			User user = getPayrollExternalInterface().getUserByUserName(username);				
			CFinancialYear fy = payRollService.getFinancialYearByDate(toDate.getTime());
			PayTypeMaster payTypeMstr = payRollService.getPayTypeMasterByPaytype(payType);
			List<Integer> empIdList = null;
			if(PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL.equals(payType))
			{
				empIdList = payRollService.getNormalPayEligibleEmpListBetweenFDateAndTDateByDeptAndFunctionary(fromDate,toDate,deptid,functionaryId,functionId,billNumberId);
			
			}else
			{
				empIdList = payRollService.getSuppPayEligibleEmpListBetweenFDateAndTDateByDept(fromDate,toDate,deptid,functionaryId,functionId,billNumberId);							
			
			}for (Integer empId : empIdList){		
				String failureRemarks = null;
				beginTransaction();
				//reattaching the user  object is not possible,due to transient[any exceition throws in this block tx will get rollback,session will get close and object becomes transient] so getting from db
					user = getPayrollExternalInterface().getUserByUserName(username);				
				PersonalInformation employee = getPayrollExternalInterface().getEmloyeeById(empId);
				Boolean payPossible = false;
				try{
					//TODO: paytype to be passed. For supp. don't check assignment
					payPossible = payRollService.checkPayslipProcessAbilityByPayType(empId,fromDate,toDate,payType);												
				}catch (PayslipFailureException e) {
					failureRemarks = e.getMessage();
				}
				if(payPossible){
					try{
						if(approverPos != null){
							HibernateUtil.getCurrentSession().refresh(approverPos);
						}
						if(createPayslipForBatch(payRollService, empId, fromDate, toDate, user, fy, payTypeMstr.getId(), persist, approverPos)){
							 commitTransaction();
						}
					}catch (PayslipFailureException e) {
						failureRemarks = e.getMessage();
						HibernateUtil.rollbackTransaction();
					}catch (HibernateException e) {
						failureRemarks = e.getMessage();
						HibernateUtil.rollbackTransaction();
					}catch(EGOVException e){
						failureRemarks = e.getMessage();
						HibernateUtil.rollbackTransaction();
					}catch(Exception e){
						failureRemarks = e.getMessage();
						HibernateUtil.rollbackTransaction();
					}
				}
				if(failureRemarks != null){
					  beginTransaction();
					  BatchFailureDetails batchFailureDetail= new BatchFailureDetails();	
					  //TODO: reattach the user object
							user = getPayrollExternalInterface().getUserByUserName(username);	
					  batchFailureDetail.setEmployee(employee);
					  batchFailureDetail.setFinancialyear(fy);
					  batchFailureDetail.setFromDate(fromDate.getTime());
					  batchFailureDetail.setToDate(toDate.getTime());
					  batchFailureDetail.setMonth(new BigDecimal(toDate.get(Calendar.MONTH)+1));
					  batchFailureDetail.setPayType(payTypeMstr);	
					  if(deptid == null || deptid.intValue() == 0){
						  Assignment assignment = getPayrollExternalInterface().getAssignmentByEmpAndDate(toDate.getTime(),empId);
						  if(assignment==null)
						  {
							  batchFailureDetail.setDepartment(null);
							  
						  }
						  else
						  {
							  batchFailureDetail.setDepartment((DepartmentImpl)(assignment.getDeptId()));
						  }
					  }else{
					      batchFailureDetail.setDepartment((DepartmentImpl)getPayrollExternalInterface().getDepartment(Integer.valueOf(deptid)));
					  }
					  if(functionaryId == null || functionaryId.intValue()==0){
						  LOGGER.debug("inside failure in batch payslip>>><<<<<<<<<<<<<<");
						  batchFailureDetail.setFunctionary(null);  			  
					  }else{
						FunctionaryDAO funcDAO= new FunctionaryDAO(Functionary.class,HibernateUtil.getCurrentSession());
					    batchFailureDetail.setFunctionary(funcDAO.functionaryById(Integer.valueOf(functionaryId)));							 
					  }
					  
					  if(functionId == null || functionId.intValue()==0){
						  LOGGER.debug("inside failure in batch payslip>>><<<<<<<<<<<<<<");
						  batchFailureDetail.setFunction(null);  			  
					  }else{
						  FunctionDAO funcDAO= new FunctionHibernateDAO(CFunction.class,HibernateUtil.getCurrentSession());
						  batchFailureDetail.setFunction((CFunction)funcDAO.findById(functionId,false));							 
					  }
					  batchFailureDetail.setRemarks(failureRemarks);
					  batchFailureDetail.setCreatedby(user);
					  batchFailureDetail.setCreateddate(new Date());
					  batchFailureDetail.setStatus(PayrollConstants.BATCH_FAILURE_DETAILS_STATUS_OPEN);
					  batchFailureDetail.setIsHistory("N");	
					  batchFailureDetail.setBillNumber(payslipService.getBillNumberMasterService().findByNamedQuery("BILLNUMBER_BY_ID",billNumberId));
					  LOGGER.error(failureRemarks);
					  HibernateUtil.getCurrentSession().save(batchFailureDetail); 
					 listBatchFailure.add(batchFailureDetail);
					  commitTransaction();
				}				
			}
			return listBatchFailure;
	 }
	 
	 protected Boolean createPayslipForBatch(PayRollService payRollService, Integer empid,GregorianCalendar fromdate,GregorianCalendar todate,User user, CFinancialYear fy,Integer payType,boolean persist, Position approverPos) throws Exception 
	 {
		 PayTypeMaster paytype =  getPayTypeById(payType);
		 //FIXME need not get the EmpPayroll object - as it is only an exists check	
		 LOGGER.debug("first check payslip exist for given  month todate"+todate+" empid "+empid+" payType "+payType);
		 if(!isPayslipExistForEmpAndFromdate(empid,fromdate.getTime(),paytype.getId()) )
		 {
			 if(getPayslipForEmpByMonthAndYear(empid, todate, fy, payType) == null){
				 LOGGER.debug("regular pay not exists=" + empid);
				 //Check prev approved normal payslip exists for that emp
				 EmpPayroll lastPay = getPrevMonthApprovedPayslipForEmpByDates(paytype, fromdate, todate, empid);
				 LOGGER.debug("last pay="+lastPay);
				 if(lastPay!=null){
					 if (PayrollConstants.EMP_PAYSLIP_PAYTYPE_EXCEPTION.equals(paytype.getPaytype()) && !isEmpHasExceptionForFullMonth(lastPay,fromdate,todate)){
						 throw new PayslipFailureException("Exception ends during this pay period");						 
					 }
					 else{						 
						 EmpPayroll currPay = new EmpPayroll();
						 currPay.setApproverPos(approverPos);
						 LOGGER.debug("second check payslip exist for given  month fromdate"+fromdate+" todate "+todate+" empid "+empid+" payType "+payType);
						 createCurrentPay(payRollService,currPay,lastPay, user, fromdate, todate, fy, persist);
					 }
				 }else{
					 //If not exists - flag failure - no prev payslip found					 
					 throw new PayslipFailureException("Previous Approved Payslip does not exist");
				 }
			 }
			 else{
				 //If already exists - flag failure - payslip found				 
				 throw new PayslipFailureException("Payslip already exists for current month");				  
			 }	
		 }
		 else
		 {
			 throw new PayslipFailureException(PayrollConstants.PAYSLIP_PERIOD_OVERLAP_MSG);	
		 }

		 return true;
	 }
	  
	  
	  protected void createCurrentPay(PayRollService payRollService, EmpPayroll currPay, EmpPayroll lastPay, User user, GregorianCalendar fromdate, GregorianCalendar todate,CFinancialYear fy,boolean persist) throws Exception{
		  currPay.setEmployee(lastPay.getEmployee());
		  currPay.setCreatedBy(user);
		  currPay.setCreatedDate(new Date());		
		  currPay.setFromDate(fromdate.getTime());
		  currPay.setToDate(todate.getTime());
		  currPay.setFinancialyear(fy);					 
		  EmployeeAttendenceReport empatcreport = getEmpAttendanceReport(fromdate, todate, currPay);
		  float noofdaysmonth = empatcreport.getDaysInMonth()==null ? 0 : empatcreport.getDaysInMonth();		  
		  float noofpaiddays = empatcreport.getNoOfPaidDays()==null?0: empatcreport.getNoOfPaidDays();
		  LOGGER.debug("noofdaysmonth="+noofdaysmonth+"noofpaiddays="+noofpaiddays);		 
		  int month=todate.get(Calendar.MONTH)+1;					  						  	
		 
		  setAssignmentInCurrPay(currPay, lastPay, todate);
		
		  currPay.setNumdays(new Double(noofpaiddays));
		  currPay.setWorkingDays(new Double(noofdaysmonth));		  		  
		  currPay.setStatus(getPayslipCreatedStatus());
		  currPay.setPayType(lastPay.getPayType());
		  currPay.setMonth(new BigDecimal(month));
		  
		  //Add bill number to payslip from employee's primary position
		  setBillNumber(currPay);
		  
		  populateEarningDeduction(lastPay, currPay, empatcreport, persist);				
		  if(currPay.getNetPay()!=null && currPay.getNetPay().floatValue()>=0){
			  if(persist){
				  payRollService.createPayslip(currPay);
				  LOGGER.debug("payroll emp code "+currPay.getEmployee().getCode());
				  createWorkFlow(currPay);
				  //payrollmgr.createPayslip(currPay);
			  }
		  }
		  else{					 
			  throw new PayslipFailureException("Net Pay is Negative");					  
		  }
		 
	 }
	  
	  
	  
	  protected PayTypeMaster getPayTypeById(Integer payType) throws Exception{
		  PayRollService payRollService = PayrollManagersUtill.getPayRollService();
		  return payRollService.getPayTypeById(payType) ;
	  }
	  
	  protected boolean isEmpHasExceptionForFullMonth(EmpPayroll lastPay, GregorianCalendar fromdate, GregorianCalendar todate) throws Exception{
		  return getPayrollExternalInterface().isEmpHasExceptionForFullMonth(lastPay.getEmployee().getIdPersonalInformation(),fromdate,todate);
	  }
	  
	  protected EmpPayroll getPrevMonthApprovedPayslipForEmpByDates(PayTypeMaster paytype,GregorianCalendar fromdate,GregorianCalendar todate,Integer empid) throws Exception{
		  PayRollService payRollService = PayrollManagersUtill.getPayRollService();
		  if(PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL.equals(paytype.getPaytype()))
		  {
			  return payRollService.getPrevApprovedPayslipForEmpByDates(fromdate,todate, empid);
		  }
		  else
		  {
			  return payRollService.getPrevApprovedSuppPayslipForEmpByDates(fromdate, todate, empid);	
		  }
	  }
	  
	  protected EmpPayroll getPayslipForEmpByMonthAndYear(Integer empid,GregorianCalendar todate,CFinancialYear fy,Integer payType) throws Exception{
		  PayRollService payRollService = PayrollManagersUtill.getPayRollService();
		  return payRollService.getPayslipForEmpByMonthAndYear(empid, (todate.get(Calendar.MONTH)+1),fy.getId().intValue(),payType);
	  }
	  
	  protected EmployeeAttendenceReport getEmpAttendanceReport(GregorianCalendar fromdate,GregorianCalendar todate,EmpPayroll currPay){
		  //EmployeeAttendenceReport empatcreport =
		  return  getPayrollExternalInterface().getEmployeeAttendenceReportBetweenTwoDates(fromdate.getTime(),todate.getTime(),currPay.getEmployee());
	  }
	  
	  protected EgwStatus getPayslipCreatedStatus(){
		  PayrollExternalInterface payrollExternalInterface=PayrollManagersUtill.getPayrollExterInterface();
		  //Getting from config table instead of config xml file
		  /*EgwStatus payslipCreatedStstus = (EgwStatus) comMgr.getStatusByModuleAndDescription(
				  PayrollConstants.PAYSLIP_MODULE, EGovConfig.getProperty("payroll_egov_config.xml","CREATED_STATUS","",EGOVThreadLocals.getDomainName()+".PaySlip"));*/
		  EgwStatus payslipCreatedStstus = (EgwStatus) payrollExternalInterface.getStatusByModuleAndDescription(
				  PayrollConstants.PAYSLIP_MODULE, GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","PayslipCreatedStatus",new Date()).getValue());
		  
		  return payslipCreatedStstus;
	  }
	  
	  protected void setBillNumber(EmpPayroll currPay) throws Exception
	  {
		  //Employee assignment is already set in current payslip before coming here.			  
		  if(null == currPay.getEmpAssignment().getPosition().getBillNumber())
			  throw new PayslipFailureException("There is no bill number mapped to Position : "+currPay.getEmpAssignment().getPosition().getName());		
		  LOGGER.debug("pos id = "+currPay.getEmpAssignment().getPosition().getId()+" : bill number = "+currPay.getEmpAssignment().getPosition().getBillNumber().getBillNumber());
		  currPay.setBillNumber(currPay.getEmpAssignment().getPosition().getBillNumber());
	  }
	  
	  /*
	   *   in case of supplimentary pay slip the assignment will be previous one only it won't change 
	   *   &&& check we are considering last pay istead of current pay		
	   */
	  protected void setAssignmentInCurrPay(EmpPayroll currPay, EmpPayroll lastPay, GregorianCalendar todate){
		  if(lastPay.getPayType().getPaytype().equals(PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL))
		  {
			  currPay.setEmpAssignment(getPayrollExternalInterface().getAssignmentByEmpAndDate(todate.getTime(),lastPay.getEmployee().getIdPersonalInformation()));
		  
		  }else
		  {
			  currPay.setEmpAssignment(lastPay.getEmpAssignment());
		  }
	  }
	  
	  protected void populateEarningDeduction(EmpPayroll lastPay, EmpPayroll currPay, EmployeeAttendenceReport empatcreport, boolean persist) throws Exception{
		  PayRollService payRollService = PayrollManagersUtill.getPayRollService();
		  if(lastPay.getPayType().getPaytype().equals(PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL)){
			  currPay=payRollService.setEarnings(lastPay,currPay,empatcreport,persist);
			  currPay=payRollService.setNormalDeductions(lastPay,currPay,persist);
		  }
		  else{
			  currPay=payRollService.setEarningsForSuppPayslip(lastPay,currPay,empatcreport,persist);
			  currPay=payRollService.setDeductions(lastPay,currPay,persist);
		  }
	  }
	  
	  /**
	   * Insert an paystructure entry if paystructure basic amount is changed
	   * @param payslip
	   * @param paystructure
	   * @return
	   */
	  public PayStructure insertPaystructureHistory(EmpPayroll payslip,PayStructure paystructure,BigDecimal incrementedBasic, Boolean persists) throws Exception{
		  try
		  {			  
			//PayStructure tempPaystructure = null;
			PayStructureDAO paystrDao = PayrollDAOFactory.getDAOFactory().getPayStructureDAO();
			PayStructure newPaystructure = new PayStructure();
			newPaystructure.setAnnualIncrement(paystructure.getAnnualIncrement());			
			//Setting effective from date to 1st of the payslip generation month
			Date effFromdate = PayrollManagersUtill.getPayRollService().getStartDateOfMonthByMonthAndFinYear(payslip.getMonth().intValue(),payslip.getFinancialyear().getId().intValue());
			if(effFromdate != null)
				newPaystructure.setEffectiveFrom(effFromdate);
			else
				newPaystructure.setEffectiveFrom(payslip.getFromDate());
			newPaystructure.setEmployee(paystructure.getEmployee());
			newPaystructure.setPayHeader(paystructure.getPayHeader());
			newPaystructure.setStagnantPay(paystructure.getStagnantPay());
			if(paystructure.getCurrBasicPay() != null){
				newPaystructure.setCurrBasicPay(incrementedBasic);
			}
			else if(paystructure.getDailyPay() != null){
				newPaystructure.setDailyPay(incrementedBasic);
			}
			if(persists){
				newPaystructure = (PayStructure)paystrDao.create(newPaystructure);
			}
			return newPaystructure;
		  }catch (Exception e)
		  {
			  throw new PayslipFailureException("Error in insertPaystructureHistory");
		  }		
	 }
	  
	 /**
	  * Apply incement on payscale
	  * @throws Exception 
	  */
	  public PayStructure applyPayscaleIncrement(EmpPayroll currPay,PayStructure payStructure,boolean persists) throws Exception{
		  	PayStructure newPayStructure = payStructure;
		  	IncrementDetailsDAO incrDetDao = PayrollDAOFactory.getDAOFactory().getIncrementDetailsDAO();
		  	PayRollService payRollService = PayrollManagersUtill.getPayRollService();
		  	GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(payStructure.getAnnualIncrement());
			
			//PersonalInformation employee = 
			//date =employee.getDateOfFirstAppointment()
			int increMonth = calendar.get(Calendar.MONTH);
			//adding 1 as Calendar is zero-based
			BigDecimal incrementMonth = new BigDecimal(increMonth).add(BigDecimal.ONE);
		    BigDecimal pendingamt = payRollService.getPendingIncrementAmount(currPay.getEmployee().getIdPersonalInformation(),
																currPay.getMonth().intValue(),currPay.getFinancialyear().getId().intValue());
		    
		    BigDecimal basicAmount = BigDecimal.ZERO;
		    if( payStructure.getCurrBasicPay()!= null){
		    	basicAmount=payStructure.getCurrBasicPay();
			}
			else if(payStructure.getDailyPay()!= null){
				basicAmount=payStructure.getDailyPay();
			}
		    //If pending increment is there and current month is not increment month then create paystructure history with new incremented basic
		    //Otherwise increment only basic amount by pending increment then append this to current month increment
			if(pendingamt!=null && !incrementMonth.equals(currPay.getMonth())){
				basicAmount = basicAmount.add(pendingamt);
				if(basicAmount.doubleValue() > payStructure.getPayHeader().getAmountTo().doubleValue() && payStructure.getStagnantPay() != 'Y'){
					throw new PayslipFailureException("Basic after increment is greater than current paystructure limit. ");
				}
				//This is done to avoid persisting data in case of resolving payslip
				newPayStructure = insertPaystructureHistory(currPay, payStructure, basicAmount,persists);
				if(persists){
					//newPayStructure = insertPaystructureHistory(currPay, payStructure, basicAmount);
					payRollService.resolvePendingIncr(currPay.getEmployee().getIdPersonalInformation(),currPay.getMonth().intValue(),
																								currPay.getFinancialyear().getId().intValue());
				}
			}
			else if(pendingamt != null && incrementMonth.equals(currPay.getMonth())){
				basicAmount = basicAmount.add(pendingamt);
				if(persists){
					//newPayStructure = insertPaystructureHistory(currPay, payStructure, basicAmount);
					payRollService.resolvePendingIncr(currPay.getEmployee().getIdPersonalInformation(),currPay.getMonth().intValue(),
																								currPay.getFinancialyear().getId().intValue());
				}
			}
			if (incrementMonth.equals(currPay.getMonth()) && 
					!incrDetDao.checkingForIncrementApplied(payStructure.getEmployee().getIdPersonalInformation(), currPay.getMonth().intValue(), currPay.getFinancialyear().getId().intValue())){
				
				BigDecimal incrAmt=BigDecimal.ZERO;
				
				//If rulebased increment exist for payscale,then follow
				if(payStructure.getPayHeader().getRuleScript()!=null && !payStructure.getPayHeader().getRuleScript().getName().equals(""))
				{
					EmpPayrollDAO empPayrollDAO = PayrollDAOFactory.getDAOFactory().getEmpPayrollDAO();
					
					String actionName = payStructure.getPayHeader().getRuleScript().getName();
					String scriptName = "Payroll.Payscale."+actionName;
					Script script =empPayrollDAO.getScript(scriptName);

					if(script !=null){
						try{
							//incrAmt = new BigDecimal(script.eval(Script.createContext("paystrObj",payStructure,"currPay" ,currPay )).toString());
							//TODO - commenting above line for time being and need to fix Script.createContext
					    }catch (ValidationException e) {
					    	String errorMsg = "";
					    	for(ValidationError vr : e.getErrors()){
					    		errorMsg += vr.getMessage(); 				  
					    	}
					    	throw new PayslipFailureException(errorMsg);
					    }
					}else{
						throw new PayslipFailureException("Script not found for "+scriptName);
					}
				}
				else
				{
				//Enabling the increment slab filter ::: it will return the correct increment slab based on basic amount,
				//and we always consider the 1st object only
				HibernateUtil.getCurrentSession().enableFilter("payScaleslIncFilter").
														setParameter("basicAmount",basicAmount.longValue());

				//Here payStructure.getPayHeader().getIncrSlabs() always returns only one object Because of payScaleslIncFilter enabled
				Set temp = payStructure.getPayHeader().getIncrSlabs();
				Iterator itr=temp.iterator();
				IncrementSlabsForPayScale incrementslab = null;
				if(itr.hasNext()){
					incrementslab = (IncrementSlabsForPayScale)itr.next();
						incrAmt = incrementslab.getIncSlabAmt();
				}else{
					throw new EGOVException("Increment slab is not defined for payscale that matches employee's basic pay");
				}				
				}
				
				basicAmount = basicAmount.add(incrAmt);
				
				if(basicAmount.doubleValue() > payStructure.getPayHeader().getAmountTo().doubleValue() && payStructure.getStagnantPay() != 'Y'){
					throw new PayslipFailureException("Basic after increment is greater than current paystructure limit. ");
				}
				newPayStructure = insertPaystructureHistory(currPay, payStructure, basicAmount,persists);
				if(persists){
					//newPayStructure = insertPaystructureHistory(currPay, payStructure, basicAmount);
					IncrementDetails incrementDet = new IncrementDetails();
					incrementDet.setCreatedby(currPay.getCreatedBy());
					incrementDet.setCreateddate(currPay.getCreatedDate());
					incrementDet.setEmployee(currPay.getEmployee());
					incrementDet.setFinancialyear(currPay.getFinancialyear());
					incrementDet.setMonth(currPay.getMonth());
					incrementDet.setIncrementDate(currPay.getToDate());
					incrementDet.setAmount(incrAmt);
					incrementDet.setStatus(PayrollConstants.EMP_INCREMENT_DETAILS_STATUS_RESOLVED);
					HibernateUtil.getCurrentSession().save(incrementDet);
				}
			}
			
			/*if(persists)
			{
			    if(payStructure.getCurrBasicPay()!=null)
			    {
				payStructure.setCurrBasicPay(basicAmount);
			    }else if(payStructure.getDailyPay()!=null)
			    {
			    	payStructure.setDailyPay(basicAmount);
			    }
			}*/
			
			return newPayStructure;
	  }
	

	  /**
	   * Creating workflow
	   * @param payslip
	   */
	  public void createWorkFlow(EmpPayroll payslip)throws Exception{
		  try{
			  Position position = getPayrollExternalInterface().getPositionByUserId(payslip.getCreatedBy().getId());		
			  String desigName = "";
			  String actionName = "";
			  
			  if(position==null)
			  {
				  throw new PayslipFailureException("Logged in User have no Position defined for currentdate .");
			  }
			  else
			  {
				  if(null==payslip.getCurrentState() || payslip.getCurrentState().equals(""))
				  { 
					  if(null!=payslip.getEmpAssignment().getPosition().getBillNumber().getId())
					  {
						  payslip.setApproverPos(payslipService.getApproverPositionByBillId(payslip.getEmpAssignment().getPosition().getBillNumber().getId()));
						  payslip.setApproverPositionId(payslip.getApproverPos().getId());
						  actionName = "forward";
						  fireWorkFlow(payslip, actionName, "Payslip Work flow started");
					  }
					  else
					  {
						  throw new PayslipFailureException("Position is not mapped to any bill number");
					  }
				  } 
				 
				  
				  			 
				  LOGGER.debug(" Test Pay Slip Work Flow End ----> ");
			  }
			  				  
		  }catch (ValidationException e) {
			  String errorMsg = "";
			  for(ValidationError vr : e.getErrors()){
				  errorMsg += vr.getMessage(); 				  
			  }
			  throw new PayslipFailureException(errorMsg+"-During workflow creation");
		  }
	  }
	  
	  
	  
	  
	  public Date getDateBySubOrAddForGivenMonth(Date givenDate,int numOfPrvMonth)
	  {
		Date dateForGivenNoOfDayOrMonOrYr = null;
		DateUtils dateUtilObj = new DateUtils();
		dateForGivenNoOfDayOrMonOrYr=dateUtilObj.add(givenDate, Calendar.MONTH, numOfPrvMonth);
		return dateForGivenNoOfDayOrMonOrYr;
	  }
	  
	  public List<Action> getValidActions(EmpPayroll payslip) {
		  return payslipWorkflowService.getValidActions(payslip);
	  }
	  
	  public void fireWorkFlow(EmpPayroll monthlyPayslip,String workFlowAction,String approverComments)
		{
			if (null == monthlyPayslip.getState()) {
				Position pos = payrollExternalInterface.getPositionByUserId(monthlyPayslip.getCreatedBy().getId());
				monthlyPayslip = (EmpPayroll) payslipWorkflowService.start(monthlyPayslip, pos,approverComments);
			}
			if (null != workFlowAction && StringUtils.isNotEmpty(workFlowAction) && StringUtils.isNotBlank(workFlowAction)) {
				String comments = (null == approverComments || "".equals(approverComments.trim())) ? "" : approverComments;
				payslipWorkflowService.transition(workFlowAction.toLowerCase(),	monthlyPayslip, comments);
				payslipService.updatePayslipStatus(monthlyPayslip);
			}
		}

		 /*
		   * returns true when payslip exist for given employee,fromdate and paytype
		   */
		  private boolean isPayslipExistForEmpAndFromdate(Integer empid,Date fromdate,Integer payType) throws Exception{
			  PayRollService payRollService = PayrollManagersUtill.getPayRollService();
			List payrollList=  HibernateUtil.getCurrentSession().getNamedQuery(PayrollConstants.ISPAYSLIPEXIST_FOREMP_FROMDATE).
			  setInteger("param_0", empid).
			  setDate("param_1", fromdate).
			  setInteger("param_2", payType).
			  setString("param_3", GenericDaoFactory.getDAOFactory()
						.getAppConfigValuesDAO().getAppConfigValueByDate("Payslip",
								"PayslipCancelledStatus", new Date()).getValue()).list();
			
			return (!payrollList.isEmpty());
		  }
}
