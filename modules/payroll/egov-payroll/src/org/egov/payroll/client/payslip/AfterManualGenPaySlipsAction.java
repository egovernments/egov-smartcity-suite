package org.egov.payroll.client.payslip;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwStatus;
import org.egov.commons.ObjectHistory;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.user.User;
import org.egov.payroll.model.Advance;
import org.egov.payroll.model.AdvanceSchedule;
import org.egov.payroll.model.Deductions;
import org.egov.payroll.model.Earnings;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.PayStructure;
import org.egov.payroll.model.PayTypeMaster;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.services.payslip.IPayslipProcess;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.services.payslip.PayslipProcessImpl;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.payroll.utils.PayslipAlreadyExistException;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.empLeave.model.EmployeeAttendenceReport;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.springframework.orm.hibernate3.HibernateTemplate;




/**
 * @author Lokesh ,Soumen	
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AfterManualGenPaySlipsAction extends Action{
	private static final Logger LOGGER = Logger.getLogger(AfterManualGenPaySlipsAction.class);    
    private PayrollExternalInterface payrollExternalInterface;
    
    private IPayslipProcess payslipProcess;
	
	public IPayslipProcess getPayslipProcess() {
		return payslipProcess;
	}

	public void setPayslipProcess(IPayslipProcess payslipProcess) {
		this.payslipProcess = payslipProcess;
	}
    
   
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	throws Exception
	{
		LOGGER.info("INSIDE THE ACTION CLASS     >>>>>>>>>>>>>  ");
		SalaryPaySlipForm salPaySlipForm = (SalaryPaySlipForm)form;
	    javax.servlet.http.HttpSession session=request.getSession();
		String target =" ";
		String exceptionStr="";
		
			try
			{
				if(salPaySlipForm.getEmployeeCodeId()!=null && !salPaySlipForm.getEmployeeCodeId().trim().equals("") )
				{
					PersonalInformation employee= payrollExternalInterface.getEmloyeeById(Integer.valueOf(salPaySlipForm.getEmployeeCodeId()));
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
					Date date = formatter.parse(salPaySlipForm.getEffectiveTo());
					LOGGER.info("date---------"+date);
					
					Assignment assignment =null;
					String paytype = salPaySlipForm.getPayType();
					PayTypeMaster normalPaytype = PayrollManagersUtill.getPayRollService().getPayTypeMasterByPaytype(PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL);
					PayTypeMaster expPaytype = PayrollManagersUtill.getPayRollService().getPayTypeMasterByPaytype(PayrollConstants.EMP_PAYSLIP_PAYTYPE_EXCEPTION);
					
					
					if(paytype.equalsIgnoreCase(normalPaytype.getId().toString()) || paytype.equalsIgnoreCase(expPaytype.getId().toString()))
					{
						assignment =(Assignment)payrollExternalInterface.getAssignmentByEmpAndDate(date,Integer.valueOf(salPaySlipForm.getEmployeeCodeId()));
					}
					else
					{
						assignment = PayrollManagersUtill.getPayRollService().getAssignmentByDateOrByLast(Integer.valueOf(salPaySlipForm.getEmployeeCodeId()),date);
					}
					
					EmpPayroll paySlip=null;
	
					//paySlip =new EmpPayroll();
					if(employee==null || assignment==null)
					{
						throw new EGOVException("Either employee id is incorrect or employee is not assigned for this period !!");		
					}
					else
					{
						if(request.getParameter("type")!=null && !request.getParameter("type").trim().equals("")
								&& request.getParameter("type").equalsIgnoreCase("modify")) {
							paySlip = modifyPayslip(request,salPaySlipForm);
							session.setAttribute("paySlip", paySlip);
							LOGGER.info("designation----"+paySlip.getEmpAssignment().getDesigId().getDesignationName());
						}
						else {
							try{
								paySlip = createPayslip(request, employee, assignment,salPaySlipForm);
								Assignment ass= paySlip.getEmpAssignment();
								Department dep=ass.getDeptId();
								DesignationMaster des= ass.getDesigId();
								HibernateTemplate htm = new HibernateTemplate();
								htm.initialize(des);
								htm.initialize(dep);
								String nam= dep.getDeptName();
								LOGGER.info("designation----"+ass+"dep"+dep+"nam"+nam);
								session.setAttribute("depart", dep);
								session.setAttribute("desig", des);
								session.setAttribute("paySlip", paySlip);
							
							}catch (PayslipAlreadyExistException e) {
								target="error";
								LOGGER.error("Payslip already exists-----"+e.getMessage());
							}
						}							
						//session.setAttribute("paySlip", paySlip);
						//logger.info("designation----"+paySlip.getEmpAssignment().getDesigId().getDesignationName());
						target="success";
					}
				}
				String frwdType = salPaySlipForm.getFrwdType();
				if("resolve".equalsIgnoreCase(frwdType))
				{
					target = "successAndResolve";
				}
				if("supplementary".equalsIgnoreCase(frwdType))
				{
					target = "successAndSupplement";
					session.setAttribute("successMessage", "Supplementary payslip Created Successfully");
				}
				if("newPayslip".equalsIgnoreCase(frwdType))
				{
					target = "successAndNew";
				}
			}
			catch(EGOVException ex)
	        {
	            //We are rolling back here since in create/modifyPayslip, objects get updated (PayStructure, IncrementDetails)
				LOGGER.error("EGOVException Encountered!!!"+ ex.getMessage());
	            target = "success";
	            request.setAttribute("alertMessage",ex.getMessage());
	            HibernateUtil.rollbackTransaction();
	        }
			catch(EGOVRuntimeException ex)
	        {
	            LOGGER.error("EGOVRuntimeException Encountered!!!"+ ex.getMessage());
	            exceptionStr=""+ ex.getMessage();
	            target = "failure";
	            HibernateUtil.rollbackTransaction();
	        }
			catch(Exception e)
			{
				LOGGER.error("Error while getting data>>>>>"+e.getMessage());
				exceptionStr=""+e.getMessage();
				target = "failure";
				HibernateUtil.rollbackTransaction();
			}
			
			if(!"".equals(exceptionStr)){
				request.setAttribute("exceptionStr", exceptionStr);
			}
		return mapping.findForward(target);
	}
    
   
    /*
     * @return the no.of paiddays in this date period and the total calendar days in this date period
     */
    private Float[] getWorkingDaysForPayslip(HttpServletRequest request, PersonalInformation employee, String fromDate, String toDate)  throws ParseException {
    	//TODO: check if required. This would have been done in BeforePayslipAction and populated in SalaryForm
        final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
    	EmployeeAttendenceReport empatcreport=payrollExternalInterface.getEmployeeAttendenceReportBetweenTwoDates(FORMATTER.parse(fromDate),FORMATTER.parse(toDate),employee);
		
		float noofdaysmonth =empatcreport.getDaysInMonth();
		float noofpaiddays =empatcreport.getNoOfPaidDays()==null?0:empatcreport.getNoOfPaidDays();
		//float noofunpaidleaves = empatcreport.getNoOfUnPaidleaves()==null?0: empatcreport.getNoOfUnPaidleaves();
		request.getSession().setAttribute("noofdaysmonth",noofdaysmonth);
		LOGGER.info("noofdaysmonth="+noofdaysmonth+"noofpaiddays="+noofpaiddays);
		Float[] attDays = new Float[2];
		attDays[0] = noofpaiddays;
		attDays[1] = noofdaysmonth;
    	return attDays ;
    }
    
    private EmpPayroll createPayslip(HttpServletRequest request, PersonalInformation employee, Assignment assignment,SalaryPaySlipForm salPaySlipForm) throws ParseException,Exception {
    	final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
    	try{
    	EmpPayroll paySlip = new EmpPayroll();
    	//Setting approver postion to the payslip for the next level
    	
    	/*
    	String approverEmpAssgnId = salPaySlipForm.getApproverEmpAssignmentId();
    	if(approverEmpAssgnId != null && !"".equals(approverEmpAssgnId)){
    		Assignment approverAssignment = PayrollManagersUtill.getEisManager().getAssignmentById(Integer.parseInt(approverEmpAssgnId));
    		paySlip.setApproverPos(approverAssignment.getPosition());
    	}
    	*/
    	//Set Bill Number   
    	LOGGER.info("Setting Bill number..");
    	if(null != assignment && null != assignment.getPosition())
    	{
    		if( null == assignment.getPosition().getBillNumber())    	
    			throw new EGOVException("Position is not mapped to bill number.");
    		else
    			paySlip.setBillNumber(assignment.getPosition().getBillNumber());
    	}
    	else
    		throw new EGOVException("Employee does not have primary assignment.");
    	LOGGER.info("Setting Approver Position..");
    	if(null == assignment.getPosition().getBillNumber().getPosition())
    		throw new EGOVException("Bill Number is not mapped to Approver Position.");
    	else
    		paySlip.setApproverPos(assignment.getPosition().getBillNumber().getPosition());
    	paySlip.setEmployee(employee);
		paySlip.setEmpAssignment(assignment);
		paySlip.setGrossPay(new BigDecimal(salPaySlipForm.getGrossPay()));
		paySlip.setNetPay(new BigDecimal(salPaySlipForm.getNetPay()));
		
		PayTypeMaster normalPaytype = PayrollManagersUtill.getPayRollService().getPayTypeMasterByPaytype(PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL);
		CFinancialYear financialYear = (CFinancialYear)payrollExternalInterface.findFinancialYearById(Long.valueOf(salPaySlipForm.getYear()));
		
		String fromDate=salPaySlipForm.getEffectiveFrom();
		String toDate=salPaySlipForm.getEffectiveTo();
		String payType=salPaySlipForm.getPayType();
		Float[] attDaysForPayslip ;
		Float paidDaysForPayslip ;
		Float wDaysForPayslip ;
		
		if (payType != null & !"".equals(payType)) {
			PayTypeMaster paytype=PayrollManagersUtill.getPayRollService().getPayTypeById(Integer.valueOf(payType));
			paySlip.setPayType(paytype);
		} else
			{
			paySlip.setPayType(normalPaytype);
			}
		
		//independent of paytype we will get attendence from payslip screen
		if(salPaySlipForm.getNumDays()!=null &&! "".equals(salPaySlipForm.getNumDays()))
		{
			paidDaysForPayslip = Float.parseFloat(salPaySlipForm.getNumDays());
			wDaysForPayslip = Float.parseFloat(salPaySlipForm.getWorkingDays());
		}else if(!PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL.equalsIgnoreCase(paySlip.getPayType().getPaytype()))
		{
			attDaysForPayslip = getWorkingDaysForPayslip(request, employee, fromDate, toDate);
			paidDaysForPayslip = attDaysForPayslip[0];
			wDaysForPayslip = attDaysForPayslip[1];
		}else
			{
			throw new EGOVException("Paid Days/Total days not Populated");
			}
			/**
			 * if(!PayrollConstants.EMP_PAYSLIP_PAYTYPE_EXCEPTION.equalsIgnoreCase(paySlip.getPayType().getPaytype()) && !PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL.equalsIgnoreCase(paySlip.getPayType().getPaytype()) ){
				// Attendence is not considered for the supplementary payments
			//else if("3".equals(salPaySlipForm.getPayType()) || "4".equals(salPaySlipForm.getPayType())){
				paidDaysForPayslip = Float.parseFloat(salPaySlipForm.getNumDays());
				wDaysForPayslip = Float.parseFloat(salPaySlipForm.getWorkingDays());
				if(PayrollConstants.EMP_PAYSLIP_PAYTYPE_LEAVE_ENCASHMENT.equalsIgnoreCase(paySlip.getPayType().getPaytype())){
					LeaveApplication leaveApplication = emplvmgr.getLeaveApplicationById(Integer.parseInt(salPaySlipForm.getLeaveApplication()));
					paySlip.setExceptionComments(leaveApplication.getApplicationNumber());
					StatusMaster encashedStatus = emplvmgr.getStatusMasterByName("Encashed");
					leaveApplication.setStatusId(encashedStatus);
					emplvmgr.updateLeaveApplication(leaveApplication);
					//EisManagersUtill.getEmpLeaveManager().updateLeaveApplication(leaveApplication);
					//emplvmgr.createLeaveApplication(Integer.parseInt(salPaySlipForm.getNumDays()), employee,"EL", salPaySlipForm.getSupplComment());
				}
			}
			else{
				attDaysForPayslip = getWorkingDaysForPayslip(request, employee, fromDate, toDate);
				paidDaysForPayslip = attDaysForPayslip[0];
				wDaysForPayslip = attDaysForPayslip[1];
			}
		*/
		float basicpay=0;
		//basicpay = (new Float(salPaySlipForm.getBasicPay())/paidDaysForPayslip)*wDaysForPayslip;
		basicpay = (new Float(salPaySlipForm.getBasicPay()));
		
		LOGGER.info("basicpay="+basicpay);
		paySlip.setBasicPay(new BigDecimal(Math.round(basicpay)));
		//FIXME: read from config file
		EgwStatus egwStatus = (EgwStatus)payrollExternalInterface.getStatusByModuleAndDescription("PaySlip","Created");
		paySlip.setStatus(egwStatus);
		if(fromDate==null || fromDate.equals(""))
		{
			Date temp=PayrollManagersUtill.getPayRollService().getEndDateOfMonthByMonthAndFinYear(paySlip.getMonth().intValue(),paySlip.getFinancialyear().getId().intValue());
			GregorianCalendar datetemp=new GregorianCalendar();
			//datetemp.setTime(temp);
			GregorianCalendar fromdate1=new GregorianCalendar(datetemp.get(Calendar.YEAR),datetemp.get(Calendar.MONTH),1);
			//GregorianCalendar todate1=new GregorianCalendar(datetemp.get(Calendar.YEAR),datetemp.get(Calendar.MONTH),datetemp.get(Calendar.DATE));
			paySlip.setFromDate(fromdate1.getTime());
			paySlip.setToDate(temp);
		}else
		{
			paySlip.setFromDate(FORMATTER.parse(fromDate));
			paySlip.setToDate(FORMATTER.parse(toDate));
		}
		String userName=(String)request.getSession().getAttribute("com.egov.user.LoginUserName");
		User user = payrollExternalInterface.getUserByUserName(userName);
		paySlip.setCreatedBy(user);
		//paidDays should not be rounded. DB should accept 23.5 as valid days
		paySlip.setNumdays(paidDaysForPayslip.doubleValue());
		paySlip.setWorkingDays(wDaysForPayslip.doubleValue());
		paySlip.setFinancialyear(financialYear);
		Date currDate = new Date();
		paySlip.setMonth(new BigDecimal(salPaySlipForm.getMonth()));
		paySlip.setCreatedDate(currDate);
		
		///////////Earnings start//////////
		String pct[] = salPaySlipForm.getPct();
		String payHead[] = salPaySlipForm.getPayHead();
		String payHeadAmount[] = salPaySlipForm.getPayHeadAmount();
		
		Set earningSet = new HashSet();
		if(payHead!=null && payHead.length!=0)
		{
			for(int i=0;i<payHead.length;i++)
			{
				if(!payHeadAmount[i].trim().equals("") 
						&& payHeadAmount[i]!=null)
				{
					
					String salCodeId = payHead[i];
					SalaryCodes salaryCodes= (SalaryCodes)PayrollManagersUtill.getPayRollService().getSalaryCodesById(Integer.valueOf(salCodeId));
					BigDecimal amountForPayHead = new BigDecimal(payHeadAmount[i]);
					if(amountForPayHead.intValue()>0 || (amountForPayHead.intValue()==0 && salaryCodes.getCaptureRate()=='Y'))
					{
						Earnings earnings = new Earnings();
						
						
						/*BigDecimal pendingamt=manualGenManager.getPendingIncrementAmount(Integer.parseInt(salPaySlipForm.getEmployeeCodeId()),paySlip.getMonth().intValue(),paySlip.getFinancialyear().getId().intValue());
						if(pendingamt!=null && paySlip.getPayType().getId().intValue() == normalPaytype.getId().intValue())
						{
							PayStructure payStructure = manualGenManager.getCurrentPayStructureForEmp(paySlip.getEmployee().getIdPersonalInformation());
							payStructure.setCurrBasicPay(paySlip.getBasicPay());
							if(paySlip.getBasicPay().doubleValue() > payStructure.getPayHeader().getAmountTo().doubleValue())
								throw new EGOVException("Basic after increment is greater than current paystructure limit. ");
							manualGenManager.resolvePendingIncr(paySlip.getEmployee().getIdPersonalInformation(),paySlip.getMonth().intValue(),paySlip.getFinancialyear().getId().intValue());
						}
						*/
						PayslipProcessImpl payslipProcessImpl = new PayslipProcessImpl();
						PayStructure payStructure = PayrollManagersUtill.getPayRollService().getCurrentPayStructureForEmp(paySlip.getEmployee().getIdPersonalInformation());
						//To apply the increment for emp,emp should have worked for atleast one month
						if(payStructure.getEmployee().getDateOfFirstAppointment()!=null){
							GregorianCalendar firstApp = new GregorianCalendar();
							firstApp.setTime(payStructure.getEmployee().getDateOfFirstAppointment());
							LOGGER.info("First App------"+firstApp.getTime());
							LOGGER.info("Days in month------"+firstApp.getActualMaximum(Calendar.DATE));
							GregorianCalendar endDate = new GregorianCalendar();
							endDate.setTime(FORMATTER.parse(salPaySlipForm.getEffectiveTo()));
							Long totalNumOfWrkingDays = ((endDate.getTime().getTime() - payStructure.getEmployee().getDateOfFirstAppointment().getTime())/(1000*60*60*24))+1;
							LOGGER.info("totalNumOfWrkingDays------"+totalNumOfWrkingDays);
							if((totalNumOfWrkingDays.intValue()  > firstApp.getActualMaximum(Calendar.DATE)) && 
												normalPaytype.equals(paySlip.getPayType()))
							{						
								payStructure = payslipProcessImpl.applyPayscaleIncrement(paySlip, payStructure, true);
							}
						}
						earnings.setEmpPayroll(paySlip);
						earnings.setSalaryCodes(salaryCodes);
						if(pct[i]!=null &&  !pct[i].trim().equals(""))
						{
							earnings.setPct(new BigDecimal(pct[i]));
						}
						earnings.setAmount(amountForPayHead);
						earningSet.add(earnings);
					}
				}
			}
			if(!earningSet.isEmpty())
			{
				Set <Earnings>earnSet = new HashSet();
				earnSet=PayrollManagersUtill.getPayRollService().removeEarningsWithAmtZero(earningSet);
				paySlip.setEarningses(earnSet);
			}
				///////////Earnings End//////////
		}
			///////////Other Dedcutions start//////////
		    String accountId[] =salPaySlipForm.getAccountCodeId();
			String deductionAmount[]= salPaySlipForm.getOtherDeductionsAmount();
			Set deductionSet = new HashSet();
			if(accountId!=null && accountId.length!=0)
			{
				for(int j=0;j<accountId.length;j++)
				{
					if(accountId[j]!=null && !accountId[j].trim().equals("") && 
							deductionAmount[j]!=null && !deductionAmount[j].trim().equals(""))
					{
						String accId = accountId[j];
						CChartOfAccounts chartOfAccounts= (CChartOfAccounts)payrollExternalInterface.getCChartOfAccountsById(Long.valueOf(accId));
						BigDecimal dedAmount = new BigDecimal(deductionAmount[j]);
						if(dedAmount.intValue()>0)
						{
							Deductions deductions = new Deductions();
							deductions.setEmpPayroll(paySlip);
							deductions.setAmount(dedAmount);
							deductions.setChartofaccounts(chartOfAccounts);
							deductionSet.add(deductions);
						}
					}
				}
			}
			///////////Other Deductions End//////////

			///////////Advancess start//////////

			String salAdvances[] = salPaySlipForm.getSalaryAdvances();
			String advSchedule[] = salPaySlipForm.getAdvanceSchedule();
			String salAdavnceAmount[] = salPaySlipForm.getSalaryAdvancesAmount();
			if(salAdvances!=null && salAdvances.length!=0)
			{
				for(int k=0;k<salAdvances.length;k++)
				{
					if(salAdvances[k]!=null && !salAdvances[k].trim().equals("") && 
							salAdavnceAmount[k]!=null && !salAdavnceAmount[k].trim().equals(""))
					{
						String salAdvancesId = salAdvances[k];
						String advScheduleId = advSchedule[k];
						BigDecimal salAdavncesAmount = new BigDecimal(salAdavnceAmount[k]);
						
						Advance salAdnces= (Advance)PayrollManagersUtill.getPayRollService().getAdvanceById(Long.valueOf((salAdvancesId)));
						AdvanceSchedule advanceScheduler = null;
						if(!"".equals(advScheduleId)){
							advanceScheduler = PayrollManagersUtill.getAdvanceService().getAdvSchedulerById(Integer.parseInt(advScheduleId));
						}
						if(salAdavncesAmount.intValue()>0)
						{
							Deductions deductions = new Deductions();
							BigDecimal pendingAmount = salAdnces.getPendingAmt().subtract(salAdavncesAmount);
							if (pendingAmount.compareTo(BigDecimal.ZERO) == -1) {
								salAdavncesAmount = salAdnces.getPendingAmt() ;	
								pendingAmount = BigDecimal.ZERO;
							}
							if(advanceScheduler != null){
								advanceScheduler.setRecover("Y");
								deductions.setAdvanceScheduler(advanceScheduler);
							}
							salAdnces.setPendingAmt(pendingAmount);							
							deductions.setEmpPayroll(paySlip);
							deductions.setSalaryCodes(salAdnces.getSalaryCodes());
							deductions.setSalAdvances(salAdnces);
							deductions.setAmount(salAdavncesAmount);
							deductionSet.add(deductions);
						}
					}
				}
			}
			///////////Advances end//////////

			///////////Master driven dedcutions start//////////

			String taxPayHeadId[] = salPaySlipForm.getTaxTypeName();
			String taxTypeAmount[] = salPaySlipForm.getTaxTypeAmount();
			String referenceNo[] = salPaySlipForm.getReferenceno();
			if(taxPayHeadId!=null && taxPayHeadId.length!=0)
			{
				for(int h=0;h<taxPayHeadId.length;h++)
				{
					//TODO:change for reference number  
					if(taxPayHeadId[h]!="" && taxPayHeadId[h]!=null && Integer.valueOf(taxPayHeadId[h])!=0 && !("").equals(taxPayHeadId[h]) && 
							taxTypeAmount[h]!="0" && taxTypeAmount[h]!=null && Integer.valueOf(taxTypeAmount[h])!= 0 && !("0").equals(taxTypeAmount[h]))
					{
						String taxId = taxPayHeadId[h];
						BigDecimal taxAmount = new BigDecimal(taxTypeAmount[h]);
						SalaryCodes salaryCodes= (SalaryCodes)PayrollManagersUtill.getPayRollService().getSalaryCodesById( Integer.valueOf(taxId));
						
						if(taxAmount.intValue()>0 || (salaryCodes.getTdsId()!=null && salaryCodes.getTdsId().getBank()== null && payrollExternalInterface.findByTds(salaryCodes.getTdsId())!=null && payrollExternalInterface.findByTds(salaryCodes.getTdsId()).size()>0))
						{
							Deductions deductions = new Deductions();
							deductions.setAmount(taxAmount);
							deductions.setSalaryCodes(salaryCodes);
							deductions.setReferenceno(referenceNo[h]);
							deductions.setEmpPayroll(paySlip);
							deductionSet.add(deductions);
						}
					
					}
				}
				String dedTaxId[] = salPaySlipForm.getDedOthrName();
				String dedTxAmount[] = salPaySlipForm.getDedOthrAmount();    	
				String dedRef[] = salPaySlipForm.getDedRefNo();
				String DedTaxIds[]=salPaySlipForm.getDedOthrTxId();
	    		if(dedTaxId!=null && dedTaxId.length!=0)
	    		{
	    			for(int h=0;h<dedTaxId.length;h++)
	    			{
	    				if(dedTaxId[h]!=null && !dedTaxId[h].trim().equals("") && 
	    						dedTxAmount[h]!=null && !dedTxAmount[h].trim().equals("0") &&!dedTxAmount[h].trim().equals("")
	    						&&dedRef[h]!=null && !dedRef[h].trim().equals("0"))
	    				{
	    					String taxId = dedTaxId[h];
	    					BigDecimal taxAmount = new BigDecimal(dedTxAmount[h]);
	    					SalaryCodes salaryCodes= (SalaryCodes)PayrollManagersUtill.getPayRollService().getSalaryCodesById(Integer.valueOf(taxId));
	    					
	    					if("0".equals(DedTaxIds[h])){
	    						Deductions deductions = new Deductions();
								deductions.setSalaryCodes(salaryCodes);
								deductions.setReferenceno(dedRef[h]);
								deductions.setEmpPayroll(paySlip);
								deductions.setAmount(taxAmount);
								deductionSet.add(deductions);
							}
							
	    				}
	    			  }
	    			}
				if(!deductionSet.isEmpty())
				{
					paySlip.setDeductionses(deductionSet);
				}
				///////////Master driven dedcutions ENd//////////	
			}
			
				LOGGER.info("payslip updated   !!! " );
				//HibernateUtil.getCurrentSession().flush();
				PayrollManagersUtill.getPayRollService().createPayslip(paySlip);
				payslipProcess.createWorkFlow(paySlip);
				
				/**
				 * this block will create the normal payslip in case a month has both exception and normal payslips :
				 * for cases where exception starts or ends in the middle of the month.
				 * updates increment details::: if any increments are due it will insert to increment details table
				 * and closes the processed exceptions
				 */
				if(PayrollConstants.EMP_PAYSLIP_PAYTYPE_EXCEPTION.equalsIgnoreCase(paySlip.getPayType().getPaytype()))
				{					
					PayProcessingUtil payutil=new PayProcessingUtil();
					GregorianCalendar fromdate1=new GregorianCalendar();
					GregorianCalendar todate1=new GregorianCalendar();
					fromdate1.setTime(paySlip.getFromDate());
					todate1.setTime(paySlip.getToDate());
					
					EmpPayroll prevPayslip=PayrollManagersUtill.getPayRollService().getPrevApprovedPayslipForEmpByMonthAndYear(paySlip.getEmployee().getIdPersonalInformation(),todate1.get(Calendar.MONTH)+1,PayrollManagersUtill.getPayRollService().getFinancialYearByDate(todate1.getTime()).getId().intValue());
					HashMap map=payutil.getPaidDaysDetailsForNormalAndSuppPayslips(paySlip.getEmployee().getIdPersonalInformation(),fromdate1,todate1);
					//employee don't have previous approved payslip we are skipping his normal payslip
					if(prevPayslip!=null && map!=null && ((Float)map.get("NRMnooftotaldays"))>0)
					{					
										
						EmpPayroll currPay=new EmpPayroll();
						currPay.setCreatedBy(user);
						currPay.setCreatedDate(new Date());
						currPay.setEmpAssignment(paySlip.getEmpAssignment());
						currPay.setEmployee(paySlip.getEmployee());
						currPay.setFinancialyear(paySlip.getFinancialyear());
						currPay.setMonth(paySlip.getMonth());					
						
						PayTypeMaster normPaytype = PayrollManagersUtill.getPayRollService().getPayTypeMasterByPaytype(PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL);
						currPay.setPayType(normPaytype);
						//getting the days for which employee does not have exception
					    float totaldays=(Float)map.get("NRMnooftotaldays");
					    float noofpaiddays=(Float)map.get("NRMnoofpaiddays");
					    currPay.setWorkingDays(new Double(totaldays));
					    currPay.setNumdays(new Double(noofpaiddays));  
					    //String status = EGovConfig.getProperty("payroll_egov_config.xml","CREATED_STATUS","",EGOVThreadLocals.getDomainName()+".PaySlip");
					    String status = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","PayslipCreatedStatus",new Date()).getValue();
						EgwStatus payStatus = (EgwStatus) payrollExternalInterface.getStatusByModuleAndDescription(
								  PayrollConstants.PAYSLIP_MODULE, status);
						currPay.setStatus(payStatus);
						currPay.setFromDate(paySlip.getFromDate());
						currPay.setToDate(paySlip.getToDate());
						EmployeeAttendenceReport report=new EmployeeAttendenceReport();
						report.setDaysInMonth(currPay.getWorkingDays().intValue());
						report.setNoOfPaidDays(new Float(currPay.getNumdays()));
						//TODO: Cross check if there is a normal payslip, increments is being applied
						//TODO: we have to change the code for where to save and where not to save						
						currPay=PayrollManagersUtill.getPayRollService().setEarnings(prevPayslip,currPay,report,true);
						currPay=PayrollManagersUtill.getPayRollService().setDeductions(prevPayslip,currPay,true);
						PayrollManagersUtill.getPayRollService().createPayslip(currPay);
					}else if( map == null )
						{
						throw new EGOVException("Attendence is not Available");
						}
					
//					 close processed excepitons
					payutil.closeExceptionsForEmp(paySlip.getEmployee().getIdPersonalInformation(),fromdate1,todate1,paySlip.getCreatedBy());
					
		
				// inserts pending increments 
					payutil.updateIncrementDetails(paySlip.getEmployee(),todate1,paySlip.getCreatedBy(),paySlip.getPayType().getPaytype());
					 
				}
			//FIXME: check paytype that needs to be resolved
				if(PayrollManagersUtill.getPayRollService().isEmphasFailureForMonthFinyrPaytype(paySlip.getEmployee().getIdPersonalInformation(),paySlip.getMonth().intValue(),paySlip.getFinancialyear().getId().intValue(),paySlip.getPayType().getId()))
				  {
					PayrollManagersUtill.getPayRollService().resolvePaySlipFailure(paySlip.getEmployee().getIdPersonalInformation(),paySlip.getMonth().intValue(),paySlip.getFinancialyear().getId().intValue(),paySlip.getPayType().getId());
				  }
				return paySlip;
    	}catch(EGOVException e)
    	{
    		LOGGER.error(e.getMessage());
    		throw e;
    	}
    }   
   
    //this will be used for Deductions and SalaryAdvances
    /*private Deductions getExistingDeductinFromPayslip(EmpPayroll payslip, SalaryCodes newDeduction) {    	
    	for(Deductions tempDed : payslip.getDeductionses()){
			if((tempDed.getSalaryCodes() != null) && newDeduction.equals(tempDed.getSalaryCodes())){
             //	if yes - update existing earning
				return tempDed;
			}
		}
    	return null;
    }*/
  
    // this will return existing earning  
    private Earnings getExistingEarningFromPayslip(EmpPayroll payslip, Integer earningId) {    	
    	for(Earnings tempEar : payslip.getEarningses()){
			if(earningId.equals(tempEar.getId())){
            //	if yes - update existing earning
				return tempEar;
			}
		}
    	return null;
    }
    
   // this will return existing deduction 
    private Deductions getExistingDeductionFromPayslip(EmpPayroll payslip, Integer delDedId){
    	for(Deductions tempDeduction : payslip.getDeductionses()){
    		if(delDedId.equals(tempDeduction.getId())){
    			return tempDeduction;
    		}
    	}    	
    	return null;
    }
    
    
    
  /*
   *	Modfify payslip 
   */      
    protected EmpPayroll modifyPayslip(HttpServletRequest request,SalaryPaySlipForm salPaySlipForm) throws ParseException,Exception{    
    	EmpPayroll paySlip = null;

    	//paySlip = manualGenManager.getPayslipForEmpByMonthAndYear(new Integer(salPaySlipForm.getEmployeeCodeId()),new Integer(salPaySlipForm.getMonth()),new Integer(salPaySlipForm.getYear()));
    	paySlip = PayrollManagersUtill.getPayRollService().getPayslipById(salPaySlipForm.getPayslipId());
    	if ( paySlip !=null) {
    		LOGGER.info("the payslip >>>>>>>>>>>>>>>>>>>>>>>  " + paySlip);
    		
    		// setting the Basic Pay Amount on payslip - WILL GO as it will be in Emp-Payscale Info
    		//FIXME: refactor - create a method to set basicpay
    		float basicpay=0;
    		//FIXME : need to get workindDays and Paid days from form
    		//Float[] attDaysForPayslip = getWorkingDaysForPayslip(request, employee, fromDate, toDate);
    		//Float[] attDaysForPayslip = getWorkingDaysForPayslip(request, employee, fromDate, toDate);
    		LOGGER.info("salPaySlipForm.getNumDays()=="+salPaySlipForm.getNumDays()+"salPaySlipForm.getWorkingDays()=="+salPaySlipForm.getWorkingDays());
    		//Float paidDaysForPayslip = attDaysForPayslip[0];
    		Float paidDaysForPayslip = Float.parseFloat(salPaySlipForm.getNumDays());
    		//Float wDaysForPayslip = attDaysForPayslip[1];
    		Float wDaysForPayslip =  Float.parseFloat(salPaySlipForm.getWorkingDays());
    	    	basicpay = (new Float(salPaySlipForm.getBasicPay())/paidDaysForPayslip)*wDaysForPayslip;
    		
    	   	LOGGER.info("basicpay="+basicpay);
    		//FIXME :  round to 3 decimal
    		paySlip.setBasicPay(new BigDecimal(Math.round(basicpay)));
    		paySlip.setWorkingDays(wDaysForPayslip.doubleValue());
    		paySlip.setNumdays(paidDaysForPayslip.doubleValue());
    		String userName=(String)request.getSession().getAttribute("com.egov.user.LoginUserName");
    		User user = payrollExternalInterface.getUserByUserName(userName);
    		//FIXME : need to set modified by and modified date on payslip
    		
    		paySlip.setGrossPay(new BigDecimal(salPaySlipForm.getGrossPay()));
    		paySlip.setNetPay(new BigDecimal(salPaySlipForm.getNetPay()));
    		paySlip.setModifyRemarks(salPaySlipForm.getModifyRemarks());
    		
    		ObjectHistory objHistory = new ObjectHistory();
    		objHistory.setModifiedBy(user);
    		objHistory.setObjectId(Long.valueOf(paySlip.getId()).intValue());
    		objHistory.setObjectType(payrollExternalInterface.getObjectTypeByType("payslip"));
    		objHistory.setRemarks(salPaySlipForm.getModifyRemarks());
    		payrollExternalInterface.createObjectHistory(objHistory);

    		
    		
    		//////////  /Earnings start//////////
    		String pct[] = salPaySlipForm.getPct();
    		String payHead[] = salPaySlipForm.getPayHead();
    		String payHeadAmount[] = salPaySlipForm.getPayHeadAmount();
    		String earningIds[] = salPaySlipForm.getEarningId();
    		Set earningSet = paySlip.getEarningses();
    		//add and update earnings
    		if(earningIds!=null && earningIds.length!=0)
    		{
    			for(int i=0;i<earningIds.length;i++)
    			{
    			    if(payHeadAmount[i] != ""){
    			    	BigDecimal amountForPayHead = new BigDecimal(payHeadAmount[i]);    			    	
						Earnings earnings;
						String salCodeId = payHead[i];
	    				SalaryCodes salaryCodes= (SalaryCodes)PayrollManagersUtill.getPayRollService().getSalaryCodesById(Integer.valueOf(salCodeId));
	    				if ("0".equals(earningIds[i])) {
	    					// add new earning	    					
	    					earnings = new Earnings();	    						
							LOGGER.info("the payslip >>>>>>>>>>>>>>>>>>>>>  " + paySlip);
							earnings.setEmpPayroll(paySlip);
							earnings.setSalaryCodes(salaryCodes);
							if(pct[i]!=null &&  !pct[i].trim().equals(""))
							{
								earnings.setPct(new BigDecimal(pct[i]));
							}
							earnings.setAmount(amountForPayHead);
							if (!earnings.getAmount().equals(BigDecimal.ZERO) || (earnings.getAmount().equals(BigDecimal.ZERO) && earnings.getSalaryCodes().getCaptureRate()=='Y'))
		    				{
	    						earningSet.add(earnings); //only add non-zero earnings when new
		    				}
	    				} else {
	    					earnings = getExistingEarningFromPayslip(paySlip,Integer.valueOf(earningIds[i]));
	    					if (earnings!= null) {
    							if(pct[i]!=null &&  !pct[i].trim().equals("")){
	    							earnings.setPct(new BigDecimal(pct[i]));
	    						}
	    						earnings.setAmount(amountForPayHead);
    						}
	    					if (earnings.getAmount().equals(BigDecimal.ZERO) && earnings.getSalaryCodes().getCaptureRate()=='N')
		    				{
	    						earningSet.remove(earnings); //remove from existing earnings
		    				}else{
		    					earningSet.add(earnings);
		    				}
	    			   }		
    			   }
    			}
    		}
    		//delete earnings from set
    		Set<Integer> delEarnings = (Set<Integer>)request.getSession().getAttribute("delEarnings");
    		if (delEarnings != null)
    		{
    			for (Integer delId:delEarnings) {
    				if (delId!=0)
    				{
    					Earnings delEarning = getExistingEarningFromPayslip(paySlip,delId);
    					if (delEarning !=null)
    						{
    						earningSet.remove(delEarning);
    						}
    				}
    			}
    		}
    	    		
    		//////////  /Earnings End//////////
			
    		////////    Master driven dedcutions Tax start//////////
    		Set deductionSet = paySlip.getDeductionses();
			String taxPayHeadId[] = salPaySlipForm.getTaxTypeName();
			String taxTypeAmount[] = salPaySlipForm.getTaxTypeAmount();    	
			String referenceno[] = salPaySlipForm.getReferenceno();
			String otherDedTaxIds[]=salPaySlipForm.getOtherDedTxDedId();
    		if(taxPayHeadId!=null && taxPayHeadId.length!=0)
    		{
    			for(int h=0;h<taxPayHeadId.length;h++)
    			{
    				if(taxPayHeadId[h]!=null && !taxPayHeadId[h].trim().equals("") && 
    						taxTypeAmount[h]!=null && !taxTypeAmount[h].trim().equals("0") &&!taxTypeAmount[h].trim().equals("")
    						&&referenceno[h]!=null && !referenceno[h].trim().equals("0"))
    				{
    					String taxId = taxPayHeadId[h];
    					BigDecimal taxAmount = new BigDecimal(taxTypeAmount[h]);
    					SalaryCodes salaryCodes= (SalaryCodes)PayrollManagersUtill.getPayRollService().getSalaryCodesById(Integer.valueOf(taxId));
    					
    					if("0".equals(otherDedTaxIds[h])){
    						Deductions deductions = new Deductions();
							deductions.setSalaryCodes(salaryCodes);
							deductions.setReferenceno(referenceno[h]);
							deductions.setEmpPayroll(paySlip);
							deductions.setAmount(taxAmount);
							deductionSet.add(deductions);
						}
						else{
							Deductions deductions = getExistingDeductionFromPayslip(paySlip, Integer.parseInt(otherDedTaxIds[h]));
							if(deductions != null)
							{
								
								deductions.setSalaryCodes(salaryCodes);
								deductions.setReferenceno(referenceno[h]);
								deductions.setAmount(taxAmount);
								
						}
    				}
    			  }
    			}
    		}
    		String dedTaxId[] = salPaySlipForm.getDedOthrName();
			String dedTxAmount[] = salPaySlipForm.getDedOthrAmount();    	
			String dedRef[] = salPaySlipForm.getDedRefNo();
			String DedTaxIds[]=salPaySlipForm.getDedOthrTxId();
    		if(dedTaxId!=null && dedTaxId.length!=0)
    		{
    			for(int h=0;h<dedTaxId.length;h++)
    			{
    				if(dedTaxId[h]!=null && !dedTaxId[h].trim().equals("") && 
    						dedTxAmount[h]!=null && !dedTxAmount[h].trim().equals("0") &&!dedTxAmount[h].trim().equals("")
    						&&dedRef[h]!=null && !dedRef[h].trim().equals("0"))
    				{
    					String taxId = dedTaxId[h];
    					BigDecimal taxAmount = new BigDecimal(dedTxAmount[h]);
    					SalaryCodes salaryCodes= (SalaryCodes)PayrollManagersUtill.getPayRollService().getSalaryCodesById(Integer.valueOf(taxId));
    					
    					if("0".equals(DedTaxIds[h])){
    						Deductions deductions = new Deductions();
							deductions.setSalaryCodes(salaryCodes);
							deductions.setReferenceno(dedRef[h]);
							deductions.setEmpPayroll(paySlip);
							deductions.setAmount(taxAmount);
							deductionSet.add(deductions);
						}
						else{
							Deductions deductions = getExistingDeductionFromPayslip(paySlip, Integer.parseInt(DedTaxIds[h]));
							if(deductions != null)
							{
								
								deductions.setSalaryCodes(salaryCodes);
								deductions.setReferenceno(dedRef[h]);
								deductions.setAmount(taxAmount);
								
						     }
    				}
    			  }
    			}
    		}
    		Set<Integer> delDeductionsTax = (Set<Integer>)request.getSession().getAttribute("delDedsTax");
    		if (delDeductionsTax != null)
    		{
    			for (Integer delId : delDeductionsTax) {
    				if (delId!=0){
    					Deductions delDeduction = getExistingDeductionFromPayslip(paySlip, delId);
    					if (delDeduction !=null)
    					{
    					deductionSet.remove(delDeduction);
    					//manualGenManager.deleteDeductions(delDeduction);
    					}
    					
    				}
    			}
    		}
    		Set<Integer> delDedsOther = (Set<Integer>)request.getSession().getAttribute("delDedsOther");
    		if (delDedsOther != null)
    		{
    			for (Integer delId : delDedsOther) {
    				if (delId!=0){
    					Deductions delDeduction = getExistingDeductionFromPayslip(paySlip, delId);
    					if (delDeduction !=null)
    					{
    					deductionSet.remove(delDeduction);
    					//manualGenManager.deleteDeductions(delDeduction);
    					}
    				}
    			}
    		}
									
    		///////////Master driven dedcutions ENd//////////
    		
    		///////////Advancess start//////////    		
    		    		
    		
			String advDedIds[] = salPaySlipForm.getAdvDedId();
			String salAdvances[] = salPaySlipForm.getSalaryAdvances();
			String advSchedule[] = salPaySlipForm.getAdvanceSchedule();
			String salAdavnceAmount[] = salPaySlipForm.getSalaryAdvancesAmount();
			Set<AdvanceSchedule> updatedAdvSchedulerSet = new HashSet<AdvanceSchedule>();
			Set<Advance> updatedAdvanceSet = new HashSet<Advance>();
			if(advDedIds!=null && advDedIds.length!=0)
			{
				for(int k=0;k<advDedIds.length;k++)
				{
					if(salAdvances[k]!=null && !salAdvances[k].trim().equals("") && 
							salAdavnceAmount[k]!=null && !salAdavnceAmount[k].trim().equals("") && !"0".equals(salAdvances[k]))
					{
						String salAdvancesId = salAdvances[k];
						String advanceSchedulerId = advSchedule[k];
						BigDecimal salAdavncesAmount = new BigDecimal(salAdavnceAmount[k]);
						Advance salAdnces= (Advance)PayrollManagersUtill.getPayRollService().getAdvanceById(Long.valueOf(salAdvancesId));
						AdvanceSchedule advanceScheduler = null;
						if(!"".equals(advanceSchedulerId)){
							advanceScheduler = PayrollManagersUtill.getAdvanceService().getAdvSchedulerById(Integer.parseInt(advanceSchedulerId));
						}
						//SalaryCodes advanceSalCode = salAdnces.getSalaryCodes();
						BigDecimal currPendingAmount = salAdnces.getPendingAmt();
						BigDecimal pendingAmount;
						Deductions deductions;						
						if ("0".equals(advDedIds[k])) {
	    					//add new deduction	    					
	    					deductions = new Deductions();	    						
							LOGGER.info("the payslip >>>>>>>>>>>>>>>>>>>>>  " + paySlip);
							pendingAmount = currPendingAmount.subtract(salAdavncesAmount);
						    //if pendingAmount is -ve, set Deduction as total pendingAmount and pendingAmount to 0
							if (pendingAmount.compareTo(BigDecimal.ZERO) == -1) {
								salAdavncesAmount = currPendingAmount ;	
								pendingAmount = BigDecimal.ZERO;
							}								
							deductions.setEmpPayroll(paySlip);
							deductions.setSalaryCodes(salAdnces.getSalaryCodes());
	    				}
						else{
							deductions = getExistingDeductionFromPayslip(paySlip,Integer.valueOf(advDedIds[k]));
						    //already existing deduction amount has been deducted - so factor that here
							pendingAmount = currPendingAmount.add(deductions.getAmount()).subtract(salAdavncesAmount);
							//FIXME: if PendingAmount = 0 , set status of Advance to closed??
							//if pendingAmount is -ve, set Deduction as total pendingAmount and pendingAmount to 0
							if (pendingAmount.compareTo(BigDecimal.ZERO) == -1) {
								salAdavncesAmount = currPendingAmount.add(deductions.getAmount()) ;	
								pendingAmount = BigDecimal.ZERO;
							}
	    				}
						if(advanceScheduler != null){
							advanceScheduler.setRecover("Y");
							deductions.setAdvanceScheduler(advanceScheduler);
							updatedAdvSchedulerSet.add(advanceScheduler);
						}
						salAdnces.setPendingAmt(pendingAmount);
						deductions.setSalAdvances(salAdnces);
						updatedAdvanceSet.add(salAdnces);
						deductions.setAmount(salAdavncesAmount);
						if (!"0".equals(advDedIds[k]) && salAdavncesAmount.intValue() <= 0)
						{
							deductionSet.remove(deductions); //if existing deduction changed to 0, remove
						}
						else if (salAdavncesAmount.intValue() > 0)
						{
							deductionSet.add(deductions);
						}
					}
					else //if the existing advance is unselected
					{
						Deductions deductions = getExistingDeductionFromPayslip(paySlip,Integer.valueOf(advDedIds[k]));
						if(deductions!=null)
						{	
							AdvanceSchedule advanceScheduler = null;
							if(null!=deductions.getAdvanceScheduler()){
								advanceScheduler = PayrollManagersUtill.getAdvanceService().getAdvSchedulerById(deductions.getAdvanceScheduler().getId());
							}
							Advance salAdnces= (Advance)PayrollManagersUtill.getPayRollService().getAdvanceById(deductions.getSalAdvances().getId());
							BigDecimal currPendingAmount = null;
							BigDecimal pendingAmount = null;
							if(advanceScheduler != null){
								advanceScheduler.setRecover(null);//set isRecover to null from 'Y'
								deductions.setAdvanceScheduler(null);//remove the reference of advance scheduler from deduction before removing the deduction
								updatedAdvSchedulerSet.add(advanceScheduler);
							}
							if(null!=salAdnces)
							{
								currPendingAmount = salAdnces.getPendingAmt();
							}
							pendingAmount = currPendingAmount.add(salAdnces.getInstAmt());//Update the pendingamount
	
							salAdnces.setPendingAmt(pendingAmount);
							deductions.setSalAdvances(null);//remove the reference of advance from deduction before removing the deduction
							updatedAdvanceSet.add(salAdnces);
							deductionSet.remove(deductions);//remove the deduction
						}	
					}
				}
			}
			
			//delete advance deduction from set
    		Set<Integer> delAdvDeductions = (Set<Integer>)request.getSession().getAttribute("delAdvDeds");
    		Boolean updateAdvance = true;
    		Boolean updateAdvanceSchedule = true;
    		if (delAdvDeductions != null){
    			for (Integer delId : delAdvDeductions) {
    				if (delId!=0){
    					Deductions delDeduction = getExistingDeductionFromPayslip(paySlip, delId);
    					if (delDeduction !=null){
    						deductionSet.remove(delDeduction);
    						for(Advance advObj : updatedAdvanceSet){
    							if(advObj.equals(delDeduction.getSalAdvances())){
    								updateAdvance = false;
    								updateAdvanceSchedule = false;
    								break;
    							}
    						}
    						for(AdvanceSchedule advScheduleObj : updatedAdvSchedulerSet){
    							if(advScheduleObj.equals(delDeduction.getAdvanceScheduler())){
    								updateAdvance = false;
    								updateAdvanceSchedule = false;
    								break;
    							}
    						}
    						if(updateAdvance){
    							delDeduction.getSalAdvances().setPendingAmt(delDeduction.getSalAdvances().getPendingAmt().add(delDeduction.getAmount()));
    						}
    						if(updateAdvanceSchedule && delDeduction.getAdvanceScheduler() != null){
    								delDeduction.getAdvanceScheduler().setRecover(null);
    						}
    					}
    				}
    			}
    		}
							
			//////////	/Advances end//////////			
			    		
    		///////////Other Dedcutions start//////////    		
		    String accountId[] =salPaySlipForm.getAccountCodeId();
			String deductionAmount[]= salPaySlipForm.getOtherDeductionsAmount();	
			String otherDedIds[] = salPaySlipForm.getOtherDedId();
			if(otherDedIds!=null && otherDedIds.length!=0)
			{
				for(int j=0;j<deductionAmount.length;j++)
				{
					if(accountId[j]!=null && !accountId[j].trim().equals("") && 
							deductionAmount[j]!=null && !deductionAmount[j].trim().equals(""))
					{
						String accId = accountId[j];
						CChartOfAccounts chartOfAccounts= (CChartOfAccounts)payrollExternalInterface.getCChartOfAccountsById(Long.valueOf(accId));
						BigDecimal dedAmount = new BigDecimal(deductionAmount[j]);
						Deductions deductions ;
						if("0".equals(otherDedIds[j])){
							deductions = new Deductions();
							deductions.setAmount(dedAmount);
							deductions.setEmpPayroll(paySlip);	
							deductions.setChartofaccounts(chartOfAccounts);
							deductionSet.add(deductions);
						}
						else{
							deductions = getExistingDeductionFromPayslip(paySlip, Integer.parseInt(otherDedIds[j]));
							if(deductions != null)
								{
								deductions.setAmount(dedAmount);
								}
						}						
					}
				}
			}
			
			
			//	delete other deduction from set
			Set<Integer> delOtherDeductions = (Set<Integer>)request.getSession().getAttribute("delOtherDeds");
    		if (delOtherDeductions != null)
    		{
    			for (Integer delId : delOtherDeductions) {
    				if (delId!=0){
    					Deductions delDeduction = getExistingDeductionFromPayslip(paySlip, delId);
    					if (delDeduction !=null)
    						{
    						deductionSet.remove(delDeduction);
    						}
    				}
    			}
    		}
			///////////Other Deductions End//////////
    		
    		
    		
       		//////////  /Earnings start prev version code//////////
    	    /*Set earningSet = paySlip.getEarningses();
    		List<Earnings> deletedEarnings = new ArrayList<Earnings>();
    		for(Earnings tempEar : paySlip.getEarningses()){
    			if("notExist".equals(getDeletedEarningfromPayslip(payHead, tempEar))){
    				deletedEarnings.add(tempEar);
    			}
    		}
    		for(Earnings tempEar : deletedEarnings)
    		{
    			earningSet.remove(tempEar);  
    		}
    		if(payHead!=null && payHead.length!=0)
    		{
    			for(int i=0;i<payHead.length;i++)
    			{
    				if(!payHeadAmount[i].trim().equals("") 
    						&& payHeadAmount[i]!=null)
    				{
    					// getting earning which to be deleted    					
    					String salCodeId = payHead[i];
    					SalaryCodes salaryCodes= (SalaryCodes)manualGenManager.getSalaryCodesById(new Integer(salCodeId));
    					BigDecimal amountForPayHead = new BigDecimal(payHeadAmount[i]);
    					if(amountForPayHead.intValue()>0)
    					{ 
    						Earnings earnings = getExistingEarningFromPayslip(paySlip, salaryCodes);
    						// check if this earning already exists
    						if (earnings!= null) {
    							if(pct[i]!=null &&  !pct[i].trim().equals("")){
    								earnings.setPct(new BigDecimal(pct[i]));
    							}
    							earnings.setAmount(amountForPayHead);
    						}
    						else{
    							earnings = new Earnings();	    						
    							System.out.println("the payslip >>>>>>>>>>>>>>>>>>>>>  " + paySlip);
    							earnings.setEmpPayroll(paySlip);
    							earnings.setSalaryCodes(salaryCodes);
    							if(pct[i]!=null &&  !pct[i].trim().equals(""))
    							{
    								earnings.setPct(new BigDecimal(pct[i]));
    							}
    							earnings.setAmount(amountForPayHead);
    							earningSet.add(earnings);
    						}
    						
    					}
    				}
    			}
    			if(!earningSet.isEmpty())
    			{
    				paySlip.setEarningses(earningSet);
    			}	
    		}
    		//////////  /Earnings End//////////
    		
    	    //////////	/Advances start prev verion code//////////			
    		String salAdvances[] = salPaySlipForm.getSalaryAdvances();
    		String salAdavnceAmount[] = salPaySlipForm.getSalaryAdvancesAmount();
    		if(salAdvances!=null && salAdvances.length!=0)
    		{
    			for(int k=0;k<salAdvances.length;k++)
    			{
    				if(salAdvances[k]!=null && !salAdvances[k].trim().equals("") && 
    						salAdavnceAmount[k]!=null && !salAdavnceAmount[k].trim().equals(""))
    				{
    					String salAdvancesId = salAdvances[k];
    					BigDecimal salAdavncesAmount = new BigDecimal(salAdavnceAmount[k]);									
    					
    					if(salAdavncesAmount.intValue()>0)
    					{
    						Advance salAdnces= (Advance)manualGenManager.getAdvanceById(new Integer(salAdvancesId));
    					
    						SalaryCodes advanceSalCode = salAdnces.getSalaryCodes();
    						//	check if this already exists in payslip
    						Deductions deductions = getExistingDeductinFromPayslip(paySlip, advanceSalCode);
    						BigDecimal currPendingAmount = salAdnces.getPendingAmt();
    						BigDecimal pendingAmount;
    						if (deductions!= null) {
    							//already existing deduction amount has been deducted - so factor that here
    							pendingAmount = currPendingAmount.add(deductions.getAmount()).subtract(salAdavncesAmount);
    							// FIXME: if PendingAmount = 0 , set status of Advance to closed??
    							// if pendingAmount is -ve, set Deduction as total pendingAmount and pendingAmount to 0
    							if (pendingAmount.compareTo(new BigDecimal(0)) == -1) {
    								salAdavncesAmount = currPendingAmount.add(deductions.getAmount()) ;	
    								pendingAmount = new BigDecimal(0);
    							}
    						} 
    						else {
    							pendingAmount = currPendingAmount.subtract(salAdavncesAmount);
    						 //if pendingAmount is -ve, set Deduction as total pendingAmount and pendingAmount to 0
    							if (pendingAmount.compareTo(new BigDecimal(0)) == -1) {
    								salAdavncesAmount = currPendingAmount ;	
    								pendingAmount = new BigDecimal(0);
    							}
    							deductions = new Deductions();
    							deductions.setEmpPayroll(paySlip);
    							deductions.setSalaryCodes(salAdnces.getSalaryCodes());
    						}
    							salAdnces.setPendingAmt(pendingAmount);
    							deductions.setSalAdvances(salAdnces);
    							deductions.setAmount(salAdavncesAmount);
    							deductionSet.add(deductions);
    						}
    					}
    				}
    			}
    		//////////	/Advances end//////////
    	    
///////////Master driven dedcutions Start prev version //////////	
    		String taxTypeAmount[] = salPaySlipForm.getTaxTypeAmount();
    		String otherDedIds[] = salPaySlipForm.getOtherDedId();
    		if(taxPayHeadId!=null && taxPayHeadId.length!=0)
    		{
    			for(int h=0;h<taxPayHeadId.length;h++)
    			{
    				if(taxPayHeadId[h]!=null && !taxPayHeadId[h].trim().equals("") && 
    						taxTypeAmount[h]!=null && !taxTypeAmount[h].trim().equals(""))
    				{
    					String taxId = taxPayHeadId[h];
    					BigDecimal taxAmount = new BigDecimal(taxTypeAmount[h]);
    					if(taxAmount.intValue()>0)
    					{
    						SalaryCodes salaryCodes= (SalaryCodes)manualGenManager.getSalaryCodesById(new Integer(taxId));
    						Deductions deductions = getExistingDeductinFromPayslip(paySlip, salaryCodes);
    						if(deductions != null){
    							deductions.setAmount(taxAmount);
    						}
    						else{
    							deductions = new Deductions();
    							deductions.setSalaryCodes(salaryCodes);
    							deductions.setEmpPayroll(paySlip);
    							deductions.setAmount(taxAmount);
    						}							
    						deductionSet.add(deductions);
    					}					
    				}
    			}
    			if(!deductionSet.isEmpty())
    			{
    				paySlip.setDeductionses(deductionSet);
    			}
    			///////////Master driven dedcutions ENd//////////		*/	
    		
    		
			if(!deductionSet.isEmpty())
			{
				paySlip.setDeductionses(deductionSet);
			}
			
						
			  
			LOGGER.info("payslip updated   !!! " );
			//HibernateUtil.getCurrentSession().flush();
			PayrollManagersUtill.getPayRollService().updatePayslip(paySlip);
			return paySlip;
    	}
    	return paySlip;
  }

	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}

	
 
    
}
