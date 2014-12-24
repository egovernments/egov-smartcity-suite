package org.egov.payroll.client.payslip;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.commons.CFinancialYear;
import org.egov.payroll.client.exception.BeforeExceptionAction;
import org.egov.payroll.model.PayTypeMaster;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.utils.PayrollExternalImpl;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.empLeave.model.LeaveApplication;

public class CreateSupplementaryPaySlips extends DispatchAction{

	private static final Logger logger = Logger.getLogger(BeforeExceptionAction.class);
	private PayrollExternalInterface payrollExternalInterface;

	// this is for Exception payslip ::: shifted to before manual action ::: currently not using this session
	/*public ActionForward supplementary(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		/*ExceptionManager exceptionManager = PayrollManagersUtill.getExceptionManager();		
		CommonsManager commonManager = PayrollManagersUtill.getCommonsManager();
		UserManager userManager = PayrollManagersUtill.getUserManager();
		EmpLeaveManager emplvmgr = PayrollManagersUtill.getEmpLeaveManager();	
		EisManager eisManager = PayrollManagersUtill.getEisManager();
		EmployeeAttendenceReport empatcreport = new EmployeeAttendenceReport();
		PayRollManager payRollManager = PayrollManagersUtill.getPayRollManager();
		Integer empid;
		String fromdate;
		String todate;
		HttpSession session = request.getSession();
		logger.info("form--------"+form);
	//	logger.info("searchform----"+(SearchForm)session.getAttribute("searchForm"));
		try{
			String userName =(String) session.getAttribute("com.egov.user.LoginUserName");
			User user = userManager.getUserByUserName(userName);
			PayTypeMaster expPaytype = manualGenManager.getPayTypeMasterByPaytype(PayrollConstants.EMP_PAYSLIP_PAYTYPE_EXCEPTION);
			PayTypeMaster normPaytype = manualGenManager.getPayTypeMasterByPaytype(PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL);
			PersonalInformation emp=null;		

			empid=new Integer((String)request.getParameter("empid"));
			fromdate = (String)request.getParameter("fromdate");
			todate=(String)request.getParameter("todate");
			EisManager eismgr = PayrollManagersUtill.getEisManager();
			emp = eismgr.getEmloyeeById(new Integer(empid));
			logger.info("empid="+empid+":::::month="+fromdate+":::::todate"+todate);
			GregorianCalendar cal=new GregorianCalendar();
			cal.set(Integer.parseInt(fromdate.split("/")[2]),(Integer.parseInt(fromdate.split("/")[1])-1),Integer.parseInt(fromdate.split("/")[0]));
			GregorianCalendar fromdate1 = new GregorianCalendar(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
			Date fromdatetemp=fromdate1.getTime();

			cal.set(Integer.parseInt(todate.split("/")[2]),(Integer.parseInt(todate.split("/")[1])-1),Integer.parseInt(todate.split("/")[0]));
			GregorianCalendar todate1 = new GregorianCalendar(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));

			SalaryPaySlipForm payslipform= new SalaryPaySlipForm();
			String frdwType = (String)request.getParameter("frwdType");
			payslipform.setFrwdType(frdwType);
			ArrayList<EmpException> expList =(ArrayList)exceptionManager.getActiveExceptionsForEmp(empid,fromdate1.getTime(),todate1.getTime());
			float NRMnoofwrkdays=0;
			float NRMnoofabsents=0;
			float NRMnoofUnpaidleaves=0;
			float SUPPnoofwrkdays=0;
			float SUPPnoofabsents=0;
			float SUPPnoofUnpaidleaves=0;
			cal.getActualMinimum(Calendar.DATE);
			String status;
			boolean supppay=true;
			Date srfromdate = null;
			Date nrfromdate = null;
			Date srtodate = new Date();
			Date nrtodate = null;
			
			if (expList!= null && expList.size() > 0)
			{
				for(EmpException exp:expList)
				{
					status="open";
					srfromdate = null;
					nrfromdate = null;
					srtodate = new Date();
					if(exp.getFromDate()==null)
					{
						throw new Exception("Employee Has UNResolved Exception");
					}					

//					exception starts on the payslip starting date
					if(exp.getFromDate()!=null && exp.getFromDate().compareTo(fromdate1.getTime())==0)
					{
						srfromdate=exp.getFromDate();

					}else if(exp.getFromDate()!=null && exp.getFromDate().compareTo(fromdate1.getTime()) < 0)					
					{
//						means exp started in prev month
						srfromdate=fromdate1.getTime();
					}else if(exp.getFromDate()!=null)
					{
						//exception started on middle of the month
						srfromdate=exp.getFromDate();
						nrfromdate=fromdate1.getTime();					
						nrtodate=exp.getFromDate();
						supppay=false;
					}
					if(exp.getToDate()!=null)
					{

						if (exp.getToDate().compareTo(todate1.getTime()) == 0 )
							//ending at the end of month
						{
							srtodate=todate1.getTime();
							status="close";
						}else if(exp.getToDate().compareTo(todate1.getTime()) < 0)
							// end in the middle
						{
							srtodate=exp.getToDate();
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(exp.getToDate());
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
//							reassigning the start date for next comparision
							logger.info(fromdate1.getTime());
							fromdate1=new GregorianCalendar(Integer.parseInt(formatter.format(exp.getToDate()).split("/")[2]),Integer.parseInt(formatter.format(exp.getToDate()).split("/")[1])-1,Integer.parseInt(formatter.format(exp.getToDate()).split("/")[0]));						

							status="close";
						}else
						{
							srtodate=todate1.getTime();
							//srtodate=exp.getToDate();
						}


					}else 
						// not at end date is entered
					{
						srtodate=todate1.getTime();					
					}
					if(status.equals("close"))
					{
						EgwStatus toStatus = commonManager.getStatusByModuleAndDescription(PayrollConstants.EMP_EXCEPTION_MODILE, PayrollConstants.EMP_EXCEPTION_CLOSED_STATUS);
						EgwStatus fromStatus = exp.getStatus();
						exp.setStatus(toStatus);
						EgwSatuschange statusChanges = new EgwSatuschange();
						statusChanges.setCreatedby(user.getId());
						statusChanges.setFromstatus(fromStatus.getId());
						statusChanges.setModuleid(3);
						statusChanges.setModuletype(PayrollConstants.EMP_EXCEPTION_MODILE);
						statusChanges.setTostatus(toStatus.getId());
						commonManager.createEgwSatuschange(statusChanges);	
						exceptionManager.createException(exp);
						//exceptionManager.createException(exp);

					}



					logger.info("nrfromdate="+nrfromdate+"nrtodate="+nrtodate);
					if(srfromdate !=null )
					{					
//						this normal report we need to get as per from date to end date
						empatcreport = emplvmgr.getEmployeeAttendenceReportBetweenTwoDates(srfromdate,srtodate,emp);
						SUPPnoofabsents=SUPPnoofabsents+empatcreport.getNoOfAbsents();
						SUPPnoofwrkdays=SUPPnoofwrkdays+empatcreport.getDaysInMonth();
						SUPPnoofUnpaidleaves=SUPPnoofUnpaidleaves+empatcreport.getNoOfUnPaidleaves();
						
					}
					if(nrfromdate!=null)
					{
	 					empatcreport = emplvmgr.getEmployeeAttendenceReportBetweenTwoDates(nrfromdate,nrtodate,emp);
	 					NRMnoofwrkdays=NRMnoofwrkdays+empatcreport.getDaysInMonth();
						NRMnoofabsents=NRMnoofabsents+empatcreport.getNoOfAbsents();
						NRMnoofUnpaidleaves=NRMnoofUnpaidleaves+empatcreport.getNoOfUnPaidleaves();
					}
					//handle case where exceptions fall between the month
					//if exception to date > nrTodate and before pay processing end- need to consider normal pay for rest of the month
						if (nrtodate!=null && srtodate.after(nrtodate) && srtodate.before(todate1.getTime())) {
							nrfromdate = srtodate; //FIXME: will need to start from next day - add 1 day
							nrtodate = todate1.getTime();
							//till end of the pay processing period
							NRMnoofwrkdays=NRMnoofwrkdays+empatcreport.getDaysInMonth();
							NRMnoofabsents=NRMnoofabsents+empatcreport.getNoOfAbsents();
							NRMnoofUnpaidleaves=NRMnoofUnpaidleaves+empatcreport.getNoOfUnPaidleaves();
						}
					}
			} else {
				// all employee exceptions could have been closed before resolving
				empatcreport = emplvmgr.getEmployeeAttendenceReportBetweenTwoDates(fromdate1.getTime(),todate1.getTime(),emp);
				NRMnoofwrkdays= empatcreport.getDaysInMonth();
				NRMnoofabsents= empatcreport.getNoOfAbsents();
				NRMnoofUnpaidleaves= empatcreport.getNoOfUnPaidleaves();
				int year=PayrollManagersUtill.getPayRollManager().getFinancialYearByDate(srtodate).getId().intValue();
				//PayrollManagersUtill.getPayRollManager().resolvePaySlipFailure(empid,srtodate.getMonth(),year);
				payslipform.setWorkingDays(String.valueOf(NRMnoofwrkdays));
				payslipform.setNumDays(String.valueOf(NRMnoofwrkdays - (NRMnoofabsents + NRMnoofUnpaidleaves)));
				payslipform.setEmployeeCodeId(empid.toString());
				payslipform.setPayType(normPaytype.getId().toString());
				request.setAttribute("payslipform",payslipform);	
				request.setAttribute("mode","reGenerate");
				request.setAttribute("payType",normPaytype.getId().toString());
				return actionMapping.findForward("reGeneratePaySlip");

			}
			
			EmpPayroll payslip=manualGenManager.getPrevApprovedPayslipForEmpByMonthAndYear(empid,todate1.get(Calendar.MONTH)+1,manualGenManager.getFinancialYearByDate(todate1.getTime()).getId().intValue());
			
			if(payslip!=null && NRMnoofwrkdays>0)
			{
				EmpPayroll currPay=new EmpPayroll();
				currPay.setCreatedBy(user);
				currPay.setCreatedDate(new Date());
				currPay.setEmpAssignment(payslip.getEmpAssignment());
				currPay.setEmployee(payslip.getEmployee());
				currPay.setFinancialyear(manualGenManager.getFinancialYearByDate(todate1.getTime()));
				currPay.setMonth(new BigDecimal(todate1.get(Calendar.MONTH)+1));
				currPay.setWorkingDays(new Double(NRMnoofwrkdays));
				currPay.setNumdays(new Double(NRMnoofwrkdays-(NRMnoofabsents+NRMnoofUnpaidleaves)));
				currPay.setPayType(normPaytype);
				//Getting from config table instead of config xml file				
				EgwStatus egwStatus = (EgwStatus) commonManager.getStatusByModuleAndDescription(
						PayrollConstants.PAYSLIP_MODULE,  EGovConfig.getProperty("payroll_egov_config.xml","CREATED_STATUS","",EGOVThreadLocals.getDomainName()+".PaySlip"));*/
				/*String createdStatus = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO().getAppConfigValueByDate("Payslip","PayslipCreatedStatus",new Date()).getValue();
				EgwStatus egwStatus = (EgwStatus) commonManager.getStatusByModuleAndDescription(PayrollConstants.PAYSLIP_MODULE,createdStatus);
				currPay.setStatus(egwStatus);
				
				EmployeeAttendenceReport report=new EmployeeAttendenceReport();
				report.setDaysInMonth(currPay.getWorkingDays().intValue()); 
				int numdays=(int)currPay.getNumdays();				
				report.setNoOfPaidDays(new Float(numdays));
				//TODO: We have to check whether we want to save data or not
				currPay=manualGenManager.setEarningsForSuppPayslip(payslip,currPay,report,false);
				currPay=manualGenManager.setDeductions(payslip,currPay,false);
				manualGenManager.createPayslip(currPay);
			}
			else{
			// this block will load the increments details if the emp has increment for the month 
			// in case of exception payslip increments will be blocked. it will load this data to incrementdetails table
				PayStructure payStructure =payRollManager.getPayStructureForEmpByDate(empid,fromdatetemp);
				GregorianCalendar calendar= new GregorianCalendar();
				calendar.setTime(payStructure.getAnnualIncrement());
				BigDecimal incrementMonth = new BigDecimal(calendar.get(Calendar.MONTH)).add(new BigDecimal(1));
				calendar.setTime(payStructure.getAnnualIncrement());
				HibernateUtil.getCurrentSession().enableFilter("payScaleslIncFilter").setParameter("basicAmount",new Long(payStructure.getCurrBasicPay().longValue()));
				if (incrementMonth.intValue()==(todate1.get(Calendar.MONTH)+1))
				{
					IncrementDetails incrementDet = new IncrementDetails();
					Iterator itr=payStructure.getPayHeader().getIncrSlabs().iterator();
					IncrementSlabsForPayScale incrementslab=(IncrementSlabsForPayScale)itr.next();
					incrementDet.setAmount(incrementslab.getIncSlabAmt());
					incrementDet.setCreatedby(user);
					incrementDet.setCreateddate(new Date());
					incrementDet.setEmployee(emp);
					incrementDet.setFinancialyear(payRollManager.getFinancialYearByDate(todate1.getTime()));
					incrementDet.setMonth(new BigDecimal(todate1.get(Calendar.MONTH)+1));
					incrementDet.setIncrementDate(calendar.getTime());
					incrementDet.setRemarks("Increment is Pending BZ of Supplimentary Pay");
					incrementDet.setStatus(PayrollConstants.EMP_INCREMENT_DETAILS_STATUS_PENDING);
					HibernateUtil.getCurrentSession().save(incrementDet);
				}  
			}
			int year=PayrollManagersUtill.getPayRollManager().getFinancialYearByDate(srtodate).getId().intValue();
			//PayrollManagersUtill.getPayRollManager().resolvePaySlipFailure(empid,srtodate.getMonth(),year);
			payslipform.setWorkingDays(SUPPnoofwrkdays+"");
			payslipform.setNumDays((SUPPnoofwrkdays-(SUPPnoofabsents+SUPPnoofUnpaidleaves))+"");
			payslipform.setEmployeeCodeId(empid.toString());
			payslipform.setPayType(expPaytype.getId().toString());
			request.setAttribute("payslipform",payslipform);	
			request.setAttribute("mode","reGenerate");
			//request.setAttribute("payslliptype","supp");
			request.removeAttribute("fromdate");
			request.removeAttribute("todate");
			return actionMapping.findForward("reGeneratePaySlip");
		}catch(EGOVRuntimeException egovExp)
		{
			logger.error(egovExp.getMessage());
			
			request.setAttribute("alertMessage",egovExp.getMessage());
			HibernateUtil.rollbackTransaction();
			return actionMapping.findForward("error");	
		}catch(Exception e)
		{
			logger.error(e.getMessage());
			
			request.setAttribute("alertMessage",e.getMessage());
			HibernateUtil.rollbackTransaction();
			return actionMapping.findForward("error");
		}	
		
	}*/

	// This is for selection of different type of payslips
	public ActionForward beforeSuppPayslip(ActionMapping actionMapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)throws Exception{

		try {
			List<CFinancialYear> financialYears = payrollExternalInterface.getAllActivePostingFinancialYear();
			List<PayTypeMaster>  paytypelist=PayrollManagersUtill.getPayRollService().getAllPayTypes();			
			List<LeaveApplication> leaveApplList = payrollExternalInterface.getEncashmentLeaveApplicationByStatus("Approved");
			request.getSession().setAttribute("paytypelist",paytypelist);
			request.getSession().setAttribute("financialYears", financialYears);		
			request.setAttribute("currFinYr",payrollExternalInterface.getCurrYearFiscalId());
			request.setAttribute("currentFinancialYear",payrollExternalInterface.findFinancialYearById(new Long(payrollExternalInterface.getCurrYearFiscalId())));
			request.getSession().setAttribute("leaveApplList", leaveApplList);
			return actionMapping.findForward("beforeSuppPayslip");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return actionMapping.findForward("failure");
		}
	}
	
	
	//This is for leave_encashment type of payslip creation
	public ActionForward leaveEncashmentPayslip(ActionMapping actionMapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)throws Exception{
		
		logger.info("Leave encashment");
		return actionMapping.findForward("leaveEncashment");
	}


	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}


}
