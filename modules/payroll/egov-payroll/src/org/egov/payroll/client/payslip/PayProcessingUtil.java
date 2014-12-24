/**
 * PayProcessingForPayslip.java created on 23 Sep, 2008 11:13:16 AM 
 */
package org.egov.payroll.client.payslip;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVException;
import org.egov.commons.CFinancialYear;
import org.egov.commons.EgwSatuschange;
import org.egov.commons.EgwStatus;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.User;
import org.egov.payroll.model.Deductions;
import org.egov.payroll.model.Earnings;
import org.egov.payroll.model.EmpException;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.IncrementDetails;
import org.egov.payroll.model.IncrementSlabsForPayScale;
import org.egov.payroll.model.PayScaleDetails;
import org.egov.payroll.model.PayScaleHeader;
import org.egov.payroll.model.PayStructure;
import org.egov.payroll.model.PayTypeMaster;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.services.exception.ExceptionService;
import org.egov.payroll.services.payhead.PayheadService;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.services.payslip.PayslipProcessImpl;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalImpl;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.empLeave.model.EmployeeAttendenceReport;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
/**
 * before control comes to this util class we will set empid,month,year,fromdate,todate,numdays,totalworkingdays values 
 */
/**
 * @author Eluri
 *
 */
public class PayProcessingUtil {
	
	private static final Logger LOGGER = Logger.getLogger(PayScaleDetailsAction.class);
	private static final String DATEFORMATESTR = "dd/MM/yyyy";
	ExceptionService exceptionService = PayrollManagersUtill.getExceptionService();		
	PayrollExternalInterface payrollExternalInterface= PayrollManagersUtill.getPayrollExterInterface();
	
	/**
	 * we are assuming empid,fromdate,todate,month,financialyear and paytype values are setted to form object 
	 * @param salPaySlipForm	 
	 * @return Map of "dedTaxlist" with deduction Tax list
		"dedTaxamountlist" deductions Tax amount list
		"dedOtheramountlist" deduction Other amount list
		"dedOtherlist" deduction Other list
		"deductionsAdvList",deductions Advances List
		"otherDedList" other Deductions List(chart of accounts)
	 * @throws Exception
	 */
	public Map loadPayslipForNormalPayslip(SalaryPaySlipForm salPaySlipForm) throws Exception
	{	
		final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATEFORMATESTR,Locale.getDefault());
		GregorianCalendar fromDate= new GregorianCalendar();
		fromDate.setTime(FORMATTER.parse(salPaySlipForm.getEffectiveFrom()));
		
		GregorianCalendar toDate=new GregorianCalendar();
		toDate.setTime(FORMATTER.parse(salPaySlipForm.getEffectiveTo()));
		Map deductionslist=null;
		
		try{
			Integer empid= Integer.parseInt(salPaySlipForm.getEmployeeCodeId());						
			EmpPayroll payslip =  PayrollManagersUtill.getPayRollService().getPrevApprovedPayslipForEmpByDates(fromDate,toDate,empid);
			if( payslip==null )
			{
				deductionslist=(HashMap)setEarningsAndDeductions(salPaySlipForm);
			}else
			{
				deductionslist=loadPayslipFromLastPayslip(payslip,salPaySlipForm);	
			}
		}catch(Exception e)
		{
			LOGGER.debug("exception "+e.getMessage());
			throw e;
		}
		return deductionslist;
	}
	
	public Map loadPayslipForExpPayslip(SalaryPaySlipForm salPaySlipForm) throws Exception
	{
		final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATEFORMATESTR,Locale.getDefault());
		GregorianCalendar fromDate= new GregorianCalendar();
		fromDate.setTime(FORMATTER.parse(salPaySlipForm.getEffectiveFrom()));
		
		GregorianCalendar toDate=new GregorianCalendar();
		toDate.setTime(FORMATTER.parse(salPaySlipForm.getEffectiveTo()));
	
		Map list=new HashMap();
		try{				
			Integer empid= Integer.parseInt(salPaySlipForm.getEmployeeCodeId());			
			EmpPayroll payslip =  PayrollManagersUtill.getPayRollService().getPrevApprovedSuppPayslipForEmpByDates(fromDate,toDate,empid);
			if( payslip==null )
			{
				list=(Map)setEarningsAndDeductions(salPaySlipForm);
			}else
			{
				list=loadPayslipFromLastPayslip(payslip,salPaySlipForm);
				LOGGER.info("form data setting over"+salPaySlipForm.getEffectiveTo());
			}
		}catch(Exception e)
		{
			LOGGER.error(e.getMessage());
			throw e;
		}
		return list;
	}
	/**
	 * we are assuming empid,fromdate,todate,month,financialyear and paytype are setted to form obj
	 * and assignment will not changes in case of exception also
	 * @param prevPayslip
	 * @param salPaySlipForm
	 * @return Map of "dedTaxlist" with deduction Tax list
		"dedTaxamountlist" deductions Tax amount list
		"dedOtheramountlist" deduction Other amount list
		"dedOtherlist" deduction Other list
		"deductionsAdvList",deductions Advances List
		"otherDedList" other Deductions List(chart of accounts)
	 */
	public Map loadPayslipFromLastPayslip(EmpPayroll lastPay,SalaryPaySlipForm salPaySlipForm) throws Exception
	{
		final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATEFORMATESTR,Locale.getDefault());
		try{  
		
		GregorianCalendar fromdate= new GregorianCalendar();
		fromdate.setTime(FORMATTER.parse(salPaySlipForm.getEffectiveFrom()));
		
		GregorianCalendar todate=new GregorianCalendar();
		todate.setTime(FORMATTER.parse(salPaySlipForm.getEffectiveTo()));
		
 
	  	EmpPayroll currPay = new EmpPayroll();
		CFinancialYear finyr=payrollExternalInterface.findFinancialYearById(new Long(salPaySlipForm.getYear()));
		currPay.setEmployee(lastPay.getEmployee());
		currPay.setCreatedDate(new Date());		
		currPay.setFromDate(fromdate.getTime());
		currPay.setToDate(todate.getTime());
		currPay.setFinancialyear(finyr);
		currPay.setMonth(new BigDecimal(todate.get(Calendar.MONTH)+1));
		String userName=salPaySlipForm.getUserName();
		User user = payrollExternalInterface.getUserByUserName(userName);
		currPay.setCreatedBy(user);
		  
		//would assignment have changed for this employee. Chances are it has! Need to get current assignment and set
		Assignment assignment = payrollExternalInterface.getAssignmentByEmpAndDate(todate.getTime(),Integer.parseInt(salPaySlipForm.getEmployeeCodeId()));
		currPay.setEmpAssignment(assignment);	
		  
		//Getting Attendance data from the form
		EmployeeAttendenceReport empatcreport = new EmployeeAttendenceReport();
		empatcreport.setNoOfPaidDays(new Float(salPaySlipForm.getNumDays()));
		empatcreport.setDaysInMonth(new Float(salPaySlipForm.getWorkingDays()).intValue());
		  
		currPay.setNumdays(new Double(salPaySlipForm.getNumDays()));
		currPay.setWorkingDays(new Double(salPaySlipForm.getWorkingDays()));
	    if(lastPay.getPayType().getPaytype().equalsIgnoreCase( PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL))
		{
	    	 //TODO: We have to check whether we want to save data or not
			 currPay=PayrollManagersUtill.getPayRollService().setEarnings(lastPay,currPay,empatcreport,false);
			 currPay=PayrollManagersUtill.getPayRollService().setNormalDeductions(lastPay,currPay,false);
		}
		else
		{
			 //TODO: We have to check whether we want to save data or not
			 currPay=PayrollManagersUtill.getPayRollService().setEarningsForSuppPayslip(lastPay,currPay,empatcreport,false);
			 currPay=PayrollManagersUtill.getPayRollService().setDeductions(lastPay,currPay,false);
		}
		
	    return copyFromBeanToForm(currPay,salPaySlipForm);
		}catch(Exception e)
		{
			LOGGER.error(e.getMessage());
			throw e;
		}
	}
	
	private Map copyFromBeanToForm(EmpPayroll currPay,SalaryPaySlipForm salPaySlipForm) throws Exception
	{
		try{
			
		salPaySlipForm.setBasicPay(currPay.getBasicPay().toString());
		salPaySlipForm.setEarningId(null);
		salPaySlipForm.setAccountCodeId(null);
		salPaySlipForm.setAdvDedId(null);
		salPaySlipForm.setCalculationType(null);
		//salPaySlipForm.set	
		
		
		Set<Earnings> earnings=currPay.getEarningses();
		Set<Deductions> deductions=currPay.getDeductionses();
		//BigDecimal basicsalary=new BigDecimal(0);
		double grosspay = 0.0 ;
		SortedSet<Earnings> earningsOrderedSet=new TreeSet(Earnings.SalarycodeComparator);
		for(Iterator iter = earnings.iterator(); iter.hasNext();)
		{
			Earnings ear = (Earnings)iter.next();
			earningsOrderedSet.add(ear);
   		}
		
		String[] caltype=new String[earnings.size()];
		String[] payhead=new String[earnings.size()];
		String[] pctBasis=new String[earnings.size()];
		String[] payHeadAmount=new String[earnings.size()];
		String[] pct=new String[earnings.size()]; 
		
		int i=-1;
		// pay heads are 3 types monthly,computed,slab based
		for(Earnings ear:earningsOrderedSet)
	    {
			caltype[++i]=(ear.getSalaryCodes()!=null && ear.getSalaryCodes().getCalType()!=null)?ear.getSalaryCodes().getCalType():"";
			payhead[i]=ear.getSalaryCodes()!=null?ear.getSalaryCodes().getId()+"":"";
			pctBasis[i]=(ear.getSalaryCodes()!=null && ear.getSalaryCodes().getSalaryCodes()!=null)?ear.getSalaryCodes().getSalaryCodes().getHead():"";
			pct[i]=ear.getPct()!=null?ear.getPct().toString():"";
			payHeadAmount[i]=Math.round(ear.getAmount().floatValue())+"";
			grosspay=grosspay+ear.getAmount().doubleValue();
		}
				
		ArrayList<SalaryCodes> dedTaxlist= new ArrayList();
		ArrayList dedTaxamountlist=new ArrayList();
		ArrayList<SalaryCodes> dedOtherlist= new ArrayList();
		ArrayList dedOtheramountlist=new ArrayList();		
		ArrayList deductionsAdvList=new ArrayList();
		ArrayList otherDedList=new ArrayList();
		
		ArrayList <Deductions> dedListWithAmount = new ArrayList<Deductions>();
		
		ArrayList <Deductions> dedOtherListWithAmount = new ArrayList<Deductions>();
		
		for(Deductions ded:deductions)
	    {
			if(ded.getChartofaccounts()!=null)
			{
				otherDedList.add(ded);
			}
			else if(ded.getSalAdvances()!=null)
			{
				deductionsAdvList.add(ded);
			}else if(ded.getSalaryCodes().getCategoryMaster().getName().equalsIgnoreCase(PayrollConstants.Deduction_Other))
			{
				dedOtherlist.add(ded.getSalaryCodes());
				dedOtheramountlist.add(ded.getAmount().doubleValue()+"");
				
				dedOtherListWithAmount.add(ded);
				
			}
			else if(ded.getSalaryCodes().getCategoryMaster().getName().equalsIgnoreCase(PayrollConstants.Deduction_Tax))
			{
				dedTaxlist.add(ded.getSalaryCodes());
				dedTaxamountlist.add(ded.getAmount().doubleValue()+"");
				
				dedListWithAmount.add(ded);
			}
	    }	
		
		// to copy the other deductions which is not there in payslip 
		List<SalaryCodes> salcodes1 = PayrollManagersUtill.getPayheadService().getAllSalarycodesByCategoryName("Deduction-Tax");
		List<SalaryCodes> salcodes2 = PayrollManagersUtill.getPayheadService().getAllSalarycodesByCategoryName("Deduction-Other");
		List<SalaryCodes> temListForChk = new ArrayList<SalaryCodes>();
		List<SalaryCodes> temListForRmv = new ArrayList<SalaryCodes>();
		for(SalaryCodes s1: salcodes1){
			temListForChk.add(s1);
			temListForRmv.add(s1);
		}
		for(SalaryCodes s2: salcodes2){
			temListForChk.add(s2);
			temListForRmv.add(s2);
		}
		for(SalaryCodes t1 : temListForChk){
			for(SalaryCodes ded : dedTaxlist){
				if(ded.getId().intValue()==t1.getId().intValue())
				{
					temListForRmv.remove(t1);
				}
			}
			for(SalaryCodes ded : dedOtherlist){
				if(ded.getId().intValue()==t1.getId().intValue())
				{
					temListForRmv.remove(t1);
				}
			}
		}
		for(SalaryCodes temp : temListForRmv){
			if(temp.getCategoryMaster().getName().equalsIgnoreCase(PayrollConstants.Deduction_Other))
			{
				dedOtherlist.add(temp);
			}else if(temp.getCategoryMaster().getName().equalsIgnoreCase(PayrollConstants.Deduction_Tax))
			{
				dedTaxlist.add(temp);
			}
		}
		//Cross Check:: dedtaxlist,dedotherlist  scope is session because both are using in ajax stuff. add to session scope ::: need to change as per new changes
		//request.getSession().setAttribute("dedTaxlist",dedTaxlist);
		//request.getSession().setAttribute("dedTaxamountlist", dedTaxamountlist.toArray());
		//request.getSession().setAttribute("dedOtheramountlist", dedOtheramountlist.toArray());
		//request.getSession().setAttribute("dedOtherlist", dedOtherlist);
		
		HashMap map=new HashMap();
		map.put("dedTaxlist",dedTaxlist);
		map.put("dedTaxamountlist",dedTaxamountlist);
		map.put("dedOtheramountlist",dedOtheramountlist);
		map.put("dedOtherlist",dedOtherlist);
		map.put("deductionsAdvList",deductionsAdvList);
		map.put("otherDedList",otherDedList);
		
		//Setting New Map of Deduction.
		map.put("dedListWithAmount",dedListWithAmount);
		map.put("dedOtherListWithAmount",dedOtherListWithAmount);
		salPaySlipForm.setCalculationType(caltype);
		salPaySlipForm.setPayHead(payhead);
		salPaySlipForm.setPct(pct);
		salPaySlipForm.setPctBasis(pctBasis);
		salPaySlipForm.setPayHeadAmount(payHeadAmount);
		return map;
		}catch(Exception e)
		{
			LOGGER.error(e.getMessage());
			throw e;
		}
	}
	
	
	
	/**
	 * @param empid
	 * @param fromdate
	 * @param todate
	 * @return
	 * @throws Exception
	 */
// get number of working days and paid days of  supplimentary and normal payslip 
	// we need to lock the rows
	public HashMap getPaidDaysDetailsForNormalAndSuppPayslips(Integer empid,GregorianCalendar fromdate,GregorianCalendar todate) throws Exception
	{
		EmployeeAttendenceReport empatcreport = null;
		HashMap paiddaysdetails=null;
		float NRMnooftotaldays=0;
		float NRMnoofpaiddays=0;
		float SUPPnooftotaldays=0;
		float SUPPnoofpaiddays=0;
		try{		
			PersonalInformation emp= payrollExternalInterface.getEmloyeeById(empid);
			ArrayList<EmpException> expList =(ArrayList)exceptionService.getActiveExceptionsForEmp(empid,fromdate.getTime(),todate.getTime());
			 
			Date srfromdate = null;
			Date nrfromdate = null;
			Date srtodate = new Date();
			Date nrtodate = null;
			
			if (expList!= null && !expList.isEmpty())
			{
				for(EmpException exp:expList)
				{
					srfromdate = null;
					nrfromdate = null;
					srtodate = new Date();					
					if(exp.getFromDate()==null)
					{
						throw new EGOVException("Employee Has UNResolved Exception");
					}					
	
					//exception starts on the payslip starting date
					if(exp.getFromDate()!=null && exp.getFromDate().compareTo(fromdate.getTime())==0)
					{
						srfromdate=exp.getFromDate();
	
					}
					else if(exp.getFromDate()!=null && exp.getFromDate().compareTo(fromdate.getTime()) < 0)					
					{
					    //means exp started in prev month
						srfromdate=fromdate.getTime();
					}
					else if(exp.getFromDate()!=null)
					{
						//exception started on middle of the month
						srfromdate=exp.getFromDate();
						nrfromdate=fromdate.getTime();
						GregorianCalendar temp=new GregorianCalendar();
						temp.setTime(exp.getFromDate());
						temp.set(Calendar.DATE,temp.get(Calendar.DATE)-1);
						nrtodate=temp.getTime();
					}
					if(exp.getToDate()!=null)
					{
						if (exp.getToDate().compareTo(todate.getTime()) == 0 )//ending at the end of month
						{
							srtodate=todate.getTime();
						}else if(exp.getToDate().compareTo(todate.getTime()) < 0)// end in the middle
						{
							srtodate=exp.getToDate();
							//Calendar calendar = Calendar.getInstance();
							//calendar.setTime(exp.getToDate());
							//SimpleDateFormat formatter = new SimpleDateFormat(DATEFORMATESTR,Locale.getDefault());
							//reassigning the start date for next comparision
							LOGGER.info(fromdate.getTime());
						    //fromdate=new GregorianCalendar(Integer.parseInt(formatter.format(exp.getToDate()).split("/")[2]),Integer.parseInt(formatter.format(exp.getToDate()).split("/")[1])-1,Integer.parseInt(formatter.format(exp.getToDate()).split("/")[0]));						
							fromdate.setTime(exp.getToDate());
							fromdate.set(Calendar.DATE,fromdate.get(Calendar.DATE)+1);
						}else
						{
							//exception is not ending at this month
							srtodate=todate.getTime();
							//srtodate=exp.getToDate();
						}
	
	
					}else // not yet end date is entered
					{
						srtodate=todate.getTime();					
					}	
	
	
					LOGGER.info("nrfromdate="+nrfromdate+"nrtodate="+nrtodate);
					if(srfromdate !=null )
					{					
						//this normal report we need to get as per from date to end date
						empatcreport = payrollExternalInterface.getEmployeeAttendenceReportBetweenTwoDates(srfromdate,srtodate,emp);						
						SUPPnooftotaldays=SUPPnooftotaldays+empatcreport.getDaysInMonth();
						SUPPnoofpaiddays=SUPPnoofpaiddays+empatcreport.getNoOfPaidDays();
						
					}
					if(nrfromdate!=null)
					{
	 					empatcreport = payrollExternalInterface.getEmployeeAttendenceReportBetweenTwoDates(nrfromdate,nrtodate,emp);
	 					NRMnooftotaldays=NRMnooftotaldays+empatcreport.getDaysInMonth();						
						NRMnoofpaiddays=NRMnoofpaiddays+empatcreport.getNoOfPaidDays();
					}
					//handle case where exceptions fall between the month
					//if exception to date > nrTodate and before pay processing end- need to consider normal pay for rest of the month
						if (nrtodate!=null && srtodate.after(nrtodate) && srtodate.before(todate.getTime())) {
							nrfromdate = srtodate; //FIXME: will need to start from next day - add 1 day
							nrfromdate.setDate(nrfromdate.getDate()+1);
							nrtodate = todate.getTime();
							//till end of the pay processing period
							NRMnooftotaldays=NRMnooftotaldays+empatcreport.getDaysInMonth();							
							NRMnoofpaiddays=NRMnoofpaiddays+empatcreport.getNoOfPaidDays();
						}
					}
			} else {
				// all employee exceptions could have been closed before resolving
				empatcreport = payrollExternalInterface.getEmployeeAttendenceReportBetweenTwoDates(fromdate.getTime(),todate.getTime(),emp);
				NRMnooftotaldays= empatcreport.getDaysInMonth();		
				NRMnoofpaiddays= empatcreport.getNoOfUnPaidleaves();
			}			
			paiddaysdetails=new HashMap();
			paiddaysdetails.put("NRMnooftotaldays",NRMnooftotaldays);
			paiddaysdetails.put("NRMnoofpaiddays",NRMnoofpaiddays);
			paiddaysdetails.put("SUPPnooftotaldays",SUPPnooftotaldays);
			paiddaysdetails.put("SUPPnoofpaiddays",SUPPnoofpaiddays);
			return paiddaysdetails;
		}catch(Exception e)
		{
			LOGGER.error(e.getMessage());
			throw e;
		}
		
	}
	
	public boolean closeExceptionsForEmp(Integer empid,GregorianCalendar fromdate,GregorianCalendar todate,User user) throws Exception
	{
		
		ExceptionService exceptionService = PayrollManagersUtill.getExceptionService();
		

		try {
			
			ArrayList<EmpException> expList = (ArrayList) exceptionService.getActiveExceptionsForEmp(empid, fromdate.getTime(),todate.getTime());

			String status = null;
			
			if (expList != null && !expList.isEmpty()) {
				for (EmpException exp : expList) {
					status = "open";

					if (exp.getFromDate() == null) {
						throw new Exception("Employee Has UNResolved Exception");
					}
					// we won't consider exception from date
					// exp can start at any time, means:::: in same month,prev
					// months,etc ...
					// based on the exception todate we will decide to close exception
					if (exp.getToDate() != null  && exp.getToDate().compareTo(todate.getTime()) <= 0 ) {  
						// end in the middle 
						status = "close";
					}
					// need to include in afte action
					if ("close".equals(status)) {
						EgwStatus toStatus = payrollExternalInterface
								.getStatusByModuleAndDescription(PayrollConstants.EMP_EXCEPTION_MODILE,PayrollConstants.EMP_EXCEPTION_CLOSED_STATUS);
						EgwStatus fromStatus = exp.getStatus();
						exp.setStatus(toStatus);
						EgwSatuschange statusChanges = new EgwSatuschange();
						statusChanges.setCreatedby(user.getId());
						statusChanges.setFromstatus(fromStatus.getId());
						statusChanges.setModuleid(3);
						statusChanges
								.setModuletype(PayrollConstants.EMP_EXCEPTION_MODILE);
						statusChanges.setTostatus(toStatus.getId());
						payrollExternalInterface.createEgwSatuschange(statusChanges);
						exceptionService.createException(exp);
						// exceptionManager.createException(exp);

					}

				}
			}
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}
	public List updateExceptionStatusAndEnterRecordToStatusChange()
	{
		return null;
	}
	public boolean checkEmpHasOpenException()
	{
		return false;
	}
	
	
	/**
	 * while setting the earnings and deductions we won't consider isrecurring & isrecomputed bz 1st time those will be always true(including resolve payslip also)  
	 */
	//supp payslip
	public Map setEarningsAndDeductions(SalaryPaySlipForm salPaySlipForm) throws Exception
	{
		final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATEFORMATESTR,Locale.getDefault());
		PayStructure payst;
		
		
			SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());		
		//PayTypeMaster normPaytype = MANUALGENMANAGER.getPayTypeMasterByPaytype(PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL);
		
		//Assumption : emp paystructure will not change if he is under exception
		//Paystructure is based on effectiveTO. Assumption: Effective to has been set in form already
		Date endDate=FORMATTER.parse(salPaySlipForm.getEffectiveTo());
		//manualGenManager.getEndDateOfMonthByMonthAndFinYear(Integer.parseInt(salPaySlipForm.getMonth()),Integer.parseInt(salPaySlipForm.getYear()));
		LOGGER.info("?????"+salPaySlipForm.getEmployeeCodeId()+">>>>>>>"+endDate);
		payst=PayrollManagersUtill.getPayRollService().getPayStructureForEmpByDate(Integer.parseInt(salPaySlipForm.getEmployeeCodeId()),endDate);
			
		
		PayScaleHeader payheader = payst.getPayHeader();
		Set<PayScaleDetails> paydetails=payheader.getPayscaleDetailses();

		BigDecimal basicsalary=BigDecimal.ZERO;
		double grosspay = 0.0 ;
		String[] caltype=new String[paydetails.size()];
		String[] payhead=new String[paydetails.size()];
	    String[] pctBasis=new String[paydetails.size()];
		String[] payHeadAmount=new String[paydetails.size()];
		String[] pct=new String[paydetails.size()]; 
		ArrayList dedTaxlist= new ArrayList();
		ArrayList<String> dedTaxamountlist=new ArrayList<String>();
		ArrayList dedOtherlist= new ArrayList();
		ArrayList<String> dedOtheramountlist=new ArrayList<String>();
		ArrayList <Deductions> dedListWithAmount = new ArrayList<Deductions>();
		ArrayList <Deductions> dedOtherListWithAmount = new ArrayList<Deductions>();
		List<SalaryCodes> salaryCodes=PayrollManagersUtill.getPayRollService().getOrderedSalaryCodes();
		

		int i=-1;
		
		SortedSet<PayScaleDetails> paydetailsOrderedSet=new TreeSet(PayScaleDetails.SalarycodeOrderIdComparator);
		for(Iterator iter = paydetails.iterator(); iter.hasNext();)
		{
			PayScaleDetails payDet = (PayScaleDetails)iter.next();
			paydetailsOrderedSet.add(payDet);
   		}
		
		// this block will load the earnings and deductions
		for (SalaryCodes salaryCode: salaryCodes)
		{
			Double amount=0d;
			// this loop is to set the earnigns
			for(PayScaleDetails paydetail:paydetailsOrderedSet)
			{
				if(salaryCode.getId()==paydetail.getSalaryCodes().getId())
				{
					String saltype=paydetail.getSalaryCodes().getCalType();
					if("MonthlyFlatRate".equalsIgnoreCase(saltype))
					{
						caltype[++i]=paydetail.getSalaryCodes().getCalType()+"";
						payhead[i]=paydetail.getSalaryCodes().getId().toString();
						pctBasis[i]="";
						pct[i]="";
						BigDecimal currBasicPay=BigDecimal.ZERO;
						// basic value checking across payscale range check will be done while saving the payslip (aftermanualpayslipaction)
						if('Y'==paydetail.getSalaryCodes().getCaptureRate())
						{	/*if(sal.getIsAttendanceBased()=='Y')
							{
								if( payStructure.getCurrBasicPay()!= null)
								{
								double temp1=earAmount/ currPay.getWorkingDays().doubleValue();
								basicAmount = new BigDecimal(Math.round(temp1*currPay.getNumdays()));
								
								}
								else if(payStructure.getDailyPay()!= null)
								{
								double temp1=earAmount*currPay.getWorkingDays();
								basicAmount = new BigDecimal(Math.round(temp1*currPay.getNumdays()/currPay.getWorkingDays()));
								//basicAmount=payStructure.getDailyPay();
								}
							
							}*/
							currBasicPay=calculateBasicForResolve(payst,endDate,salPaySlipForm,payheader,paydetail);
							basicsalary=currBasicPay;
							payHeadAmount[i]=basicsalary.toString();
							grosspay=grosspay+basicsalary.doubleValue();
						}
						else 
						{
							//Previously it was "Monthly flat rates won't depend upon isattendancebase,isrecompute,isrecurring flags"
							//Changed to "Monthly Flate Rate payhead may be attendanceBased"
							amount = paydetail.getAmount().doubleValue();
							if(paydetail.getSalaryCodes().getIsAttendanceBased()=='Y')
							{
								amount = amount /Float.parseFloat(salPaySlipForm.getWorkingDays());							
								amount = new Double(Math.round(amount*(Float.parseFloat(salPaySlipForm.getNumDays()))));
							}							
							payHeadAmount[i] = amount+"";
							grosspay = grosspay+amount;	
							/*//Monthly flat rates won't depend upon isattendancebase,isrecompute,isrecurring flags
							payHeadAmount[i]=Math.round(paydetail.getAmount().doubleValue())+"";
							grosspay = grosspay + Math.round(paydetail.getAmount().doubleValue());*/
						}
					}
					else if(saltype.equalsIgnoreCase("ComputedValue"))
					{
						Double d=0d;
						double baseamt=0;
						caltype[++i]=paydetail.getSalaryCodes().getCalType()+"";
						payhead[i]=paydetail.getSalaryCodes().getId().toString();						
						pctBasis[i]=paydetail.getSalaryCodes().getSalaryCodes().getHead();
						//it will return the cyclic parent payhead which current payhead depends
						int index=getPayHeadIndex(payhead,paydetail.getSalaryCodes().getSalaryCodes().getId().toString());
						pct[i]=paydetail.getPct().toString();
						/*if(payst.getCurrBasicPay()!=null)
						{
						 baseamt= payst.getCurrBasicPay().doubleValue();
						}
						else if(payst.getDailyPay()!=null)
						{
							baseamt= payst.getDailyPay().doubleValue();
						}*/
						if(index==0)
						{
						if(paydetail.getSalaryCodes().getIsAttendanceBased()!='Y')
						{
						baseamt = new Double(payHeadAmount[0]);
						//if(index!=0 && !pctBasis[i].equalsIgnoreCase("Basic"))
						 d = baseamt*paydetail.getPct().doubleValue()/100;
						//d=d/100;
						
						//d=d;
						}
						else if (paydetail.getSalaryCodes().getIsAttendanceBased()=='Y' && payst.getDailyPay()!=null)
						{
							baseamt = payst.getDailyPay().doubleValue();
							//if(index!=0 && !pctBasis[i].equalsIgnoreCase("Basic"))
							 d = baseamt*paydetail.getPct().doubleValue()/100;
							d = d*Float.parseFloat(salPaySlipForm.getNumDays());
							
						}
						else  if(paydetail.getSalaryCodes().getIsAttendanceBased()=='Y' && payst.getCurrBasicPay()!=null)
						{
							baseamt = payst.getCurrBasicPay().doubleValue();
							//if(index!=0 && !pctBasis[i].equalsIgnoreCase("Basic"))
							 d = baseamt*paydetail.getPct().doubleValue()/100;
							d = d*Float.parseFloat(salPaySlipForm.getNumDays())/Float.parseFloat(salPaySlipForm.getWorkingDays());
						}
						}
						else if(index!=0&& !pctBasis[i].equalsIgnoreCase("Basic"))
						{
							baseamt = new Double(payHeadAmount[index]).doubleValue();
							d = baseamt*paydetail.getPct().doubleValue()/100;
							//baseamt=getEarningAmountFromPayScale(payst,paydetail.getSalaryCodes().getId());
							
							//on what basis the calculation should be done .
							
						}
						//If salary code is attendence base prorate the earnings amount as per attendence nd mnthhly or daily
						//can we take the attendance check out we r already gettng the calculated basic amount 
						
						/*if(paydetail.getSalaryCodes().getIsAttendanceBased()=='Y' && payst.getCurrBasicPay()!=null )
						{
							d = d /Float.parseFloat(salPaySlipForm.getWorkingDays());							
						    d = d*Float.parseFloat(salPaySlipForm.getNumDays());
						}*/
						/*else if(paydetail.getSalaryCodes().getIsAttendanceBased()=='Y' && payst.getDailyPay()!=null)
						{
							d = d*Float.parseFloat(salPaySlipForm.getNumDays());
						}*/
						d = new Double(Math.round(d));
						payHeadAmount[i]=d.toString();
						grosspay=grosspay+d;
					}
					else if(saltype.equalsIgnoreCase("SlabBased"))
					 {
						BigDecimal basicPy=BigDecimal.ZERO;
						caltype[++i]=paydetail.getSalaryCodes().getCalType()+"";
						payhead[i]=paydetail.getSalaryCodes().getId().toString();
						pctBasis[i]="";
						pct[i]="";						
						if(paydetail.getSalaryCodes().getTdsId()!=null && 
								paydetail.getSalaryCodes().getTdsId().getBank()==null &&
								payrollExternalInterface.findByTds(paydetail.getSalaryCodes().getTdsId())!=null && 
								payrollExternalInterface.findByTds(paydetail.getSalaryCodes().getTdsId()).size()>0)
						{
						if(paydetail.getSalaryCodes().getIsAttendanceBased() =='Y' )
						{
							if(payst.getCurrBasicPay()!=null)
							 {
								 basicPy=payst.getCurrBasicPay(); 
							 }
							 else if(payst.getDailyPay()!=null)
							 {
								basicPy=payst.getDailyPay(); 
							 }
							//basicPy=new BigDecimal((payHeadAmount[0]));
							amount=PayrollManagersUtill.getPayRollService().getSlabBasedAmount(salaryCode,basicPy,
									fmt.format(FORMATTER.parse(salPaySlipForm.getEffectiveTo())));
							if(payst.getCurrBasicPay()!=null)
							{
								amount= new Double(Math.round(amount*(Float.parseFloat(salPaySlipForm.getNumDays())
										/Float.parseFloat(salPaySlipForm.getWorkingDays()))));
							}
							else if(payst.getDailyPay()!=null)
							{
								amount= amount*Float.parseFloat(salPaySlipForm.getNumDays());
							}
						}
						else if(paydetail.getSalaryCodes().getIsAttendanceBased() !='Y')
						{
							basicPy=new BigDecimal((payHeadAmount[0]));
							amount=PayrollManagersUtill.getPayRollService().getSlabBasedAmount(salaryCode,basicPy,
									fmt.format(FORMATTER.parse(salPaySlipForm.getEffectiveTo())));
						}
						}
						else
							{
							throw new EGOVException("TDS Not Setted for "+salaryCode.getHead());
							}
						    
						/*if(paydetail.getSalaryCodes().getIsAttendanceBased()=='Y' && payst.getCurrBasicPay()!=null)
						{
							amount = amount /Float.parseFloat(salPaySlipForm.getWorkingDays());							
							amount= new Double(Math.round(amount*(Float.parseFloat(salPaySlipForm.getNumDays()))));
						}*/
						/*else if(paydetail.getSalaryCodes().getIsAttendanceBased()=='Y' && payst.getDailyPay()!=null)
						{
							amount = amount*Float.parseFloat(salPaySlipForm.getNumDays());
						}*/
						payHeadAmount[i]=amount.toString();
						grosspay=grosspay+amount;
					
				}

			}
			}
			
			// it will load the deduction-taxes 						
			if(salaryCode.getCategoryMaster().getId() == 3)
			{							
				dedTaxlist.add(salaryCode);
                //creating deduction with amount 
				Deductions ded =new Deductions();
				if(salaryCode.getTdsId()!=null && salaryCode.getTdsId().getBank()==null)
				{	
					// we are formating the date BZ getSlabBasedAmount will expect dd-MMM-yyyy date format 
					if(PayrollManagersUtill.getPayRollService().isPayHeadGrossBased(salaryCode.getHead().trim()))
					{
						amount=	PayrollManagersUtill.getPayRollService().getSlabBasedAmount(salaryCode,new BigDecimal(grosspay),fmt.format(FORMATTER.parse(salPaySlipForm.getEffectiveTo())));
					}
					else {
						{
							amount = PayrollManagersUtill.getPayRollService().getSlabBasedAmount(salaryCode,payst.getCurrBasicPay(),fmt.format(FORMATTER.parse(salPaySlipForm.getEffectiveTo())));
						}
					}						
					
				}else
				{
					amount=0d;
				}
				if(salaryCode.getIsAttendanceBased()=='Y' && amount!= 0)
				{
					amount = amount /Float.parseFloat(salPaySlipForm.getWorkingDays());							
					amount = new Double(Math.round(amount*(Float.parseFloat(salPaySlipForm.getNumDays()))));
				}
				ded.setSalaryCodes(salaryCode);
				ded.setAmount(new BigDecimal(amount));
				dedListWithAmount.add(ded);
				dedTaxamountlist.add(amount.toString());

			}else if(salaryCode.getCategoryMaster().getId()==5)
				//it will load the deduction others
			{
				Deductions ded =new Deductions();
				dedOtherlist.add(salaryCode);
				
				if(salaryCode.getTdsId()!=null && salaryCode.getTdsId().getBank()==null)
				{
					if(PayrollManagersUtill.getPayRollService().isPayHeadGrossBased(salaryCode.getHead().trim()))
						 {
						amount=	PayrollManagersUtill.getPayRollService().getSlabBasedAmount(salaryCode,new BigDecimal(grosspay),fmt.format(FORMATTER.parse(salPaySlipForm.getEffectiveTo())));
						 }
					else {
						amount=PayrollManagersUtill.getPayRollService().getSlabBasedAmount(salaryCode,payst.getCurrBasicPay(),fmt.format(FORMATTER.parse(salPaySlipForm.getEffectiveTo())));	
					}
				}else
				{								
					amount=0d;
				}
				if(salaryCode.getIsAttendanceBased()=='Y' && amount!= 0)
				{
					amount = amount /Float.parseFloat(salPaySlipForm.getWorkingDays());							
					amount = new Double(Math.round(amount*(Float.parseFloat(salPaySlipForm.getNumDays()))));
				}
				dedOtheramountlist.add(amount.toString());
				ded.setSalaryCodes(salaryCode);
				ded.setAmount(new BigDecimal(amount));
				dedOtherListWithAmount.add(ded);
			}
		}
		LOGGER.info("dedTaxamountlist="+dedTaxamountlist.toString());
		LOGGER.info("dedOtheramountlist="+dedOtheramountlist.toString());
	
		HashMap map=new HashMap();
		map.put("dedTaxlist",dedTaxlist);
		map.put("dedTaxamountlist",dedTaxamountlist.toArray());
		map.put("dedOtheramountlist",dedOtheramountlist.toArray());
		map.put("dedOtherlist",dedOtherlist);
		map.put("dedListWithAmount",dedListWithAmount);
		map.put("dedOtherListWithAmount",dedOtherListWithAmount);
		
		//TODO: Try setting in form instead of returning a map
		//salPaySlipForm.setDedOtheramountlist((String[])dedOtheramountlist.toArray());
		//salPaySlipForm.setDeductionsAdvList(deductionsAdvList);
		salPaySlipForm.setCalculationType(caltype);
		salPaySlipForm.setPayHead(payhead);
		salPaySlipForm.setPct(pct);
		salPaySlipForm.setPctBasis(pctBasis);
		salPaySlipForm.setPayHeadAmount(payHeadAmount);
		return map;
	}
	//API TO RETURN THE NOT CALCULATED AMOUNT 
	 /*private double getEarningAmountFromPayScale(PayStructure paySt,Integer salcodeId)
	 {
		// double amount=0;
		Set<PayScaleDetails> payDet = paySt.getPayHeader().getPayscaleDetailses();
		Iterator<PayScaleDetails> itr = payDet.iterator();
		 while(itr.hasNext())
		 {
			 PayScaleDetails psd=(PayScaleDetails)itr.next();
			 if(psd.getSalaryCodes().getId().intValue()==salcodeId.intValue())
			 {
				 return psd.getAmount().doubleValue();
			 }
		 }
		 return 0;

	 }*/
	private BigDecimal calculateBasicForResolve(PayStructure payst,Date endDate,
			SalaryPaySlipForm salPaySlipForm,PayScaleHeader payheader,PayScaleDetails paydetail) throws Exception
	{
		final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATEFORMATESTR,Locale.getDefault());
		BigDecimal currBasicPay= BigDecimal.valueOf(0);
		//Basic pay is set from the Employee's Pay Structure - currentBasicPay
		if(payst.getCurrBasicPay()!=null)
		{
			currBasicPay = payst.getCurrBasicPay();
		}
		else if(payst.getDailyPay()!=null)
		{
			//double temp = payst.getDailyPay().doubleValue();
			//currBasicPay=new BigDecimal(temp*new Double(salPaySlipForm.getNumDays()));
			double temp=payst.getDailyPay().doubleValue();	
			currBasicPay =new BigDecimal(temp);
		}
		
		
		// This is to handle use case where employee becomes normal after an exception period
		// - this method is for first time payslips
		CFinancialYear finyr1=PayrollManagersUtill.getPayRollService().getFinancialYearByDate(endDate);
		LOGGER.info(">>>>"+finyr1);
		int month1=Integer.valueOf(salPaySlipForm.getMonth());
		BigDecimal pendingamt=PayrollManagersUtill.getPayRollService().getPendingIncrementAmount
		(Integer.parseInt(salPaySlipForm.getEmployeeCodeId()),month1,finyr1.getId().intValue());

		/**
		 * this loop will take care of increments :
		 * if any pending increments are there it will apply 
		 * and if the current month is increment month it will apply that increment
		 */
		
			if(pendingamt!=null)
			{
				currBasicPay=currBasicPay.add(pendingamt);	
			}
			GregorianCalendar calendar= new GregorianCalendar();
			calendar.setTime(payst.getAnnualIncrement());

			//To apply the increment for emp,emp should have worked for atleast one month
			if(payst.getEmployee().getDateOfFirstAppointment()!=null){
				GregorianCalendar firstApp = new GregorianCalendar();
				firstApp.setTime(payst.getEmployee().getDateOfFirstAppointment());
				LOGGER.info("First App------"+firstApp.getTime());
				LOGGER.info("Days in month------"+firstApp.getActualMaximum(Calendar.DATE));
				Long totalNumOfWrkingDays = ((endDate.getTime() - payst.getEmployee().getDateOfFirstAppointment().getTime())/(1000*60*60*24))+1;
				LOGGER.info("totalNumOfWrkingDays------"+totalNumOfWrkingDays);				
				if(totalNumOfWrkingDays.intValue()  > firstApp.getActualMaximum(Calendar.DATE) )
				{
					EmpPayroll tempPayslip = new EmpPayroll();
					tempPayslip.setEmployee(payst.getEmployee());
					tempPayslip.setFromDate(FORMATTER.parse(salPaySlipForm.getEffectiveFrom()));
					tempPayslip.setToDate(endDate);
					/*GregorianCalendar g = new GregorianCalendar();
					g.setTime(endDate);
					g.get(field)*/
					tempPayslip.setMonth(new BigDecimal(salPaySlipForm.getMonth()));
					tempPayslip.setFinancialyear(PayrollManagersUtill.getCommonsService().getFinancialYearById(Long.parseLong(salPaySlipForm.getYear())));
					PayslipProcessImpl payslipProcessImpl = new PayslipProcessImpl();
					PayTypeMaster normalPaytype = PayrollManagersUtill.getPayRollService().getPayTypeMasterByPaytype(PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL);
					if(normalPaytype.getId().toString().equals(salPaySlipForm.getPayType())){
						payst = payslipProcessImpl.applyPayscaleIncrement(tempPayslip, payst, false);
					}
					if(payst.getCurrBasicPay()!=null)
					{
						currBasicPay = payst.getCurrBasicPay();
					}
					else if(payst.getDailyPay()!=null)
					{
						double temp=payst.getDailyPay().doubleValue();	
						currBasicPay =new BigDecimal(temp);
					}
					/*if (incrementMonth.intValue()==(Integer.parseInt(salPaySlipForm.getMonth())))
					{
					//	IncrementDetails incrementDet = new IncrementDetails();
						HibernateUtil.getCurrentSession().enableFilter("payScaleslIncFilter").setParameter
						("basicAmount",currBasicPay.longValue());
						
						Set incrSlabs=payheader.getIncrSlabs();
						Iterator itr1=incrSlabs.iterator();
						IncrementSlabsForPayScale incrementSlab = null;
						if(itr1.hasNext())
						{
							incrementSlab=(IncrementSlabsForPayScale)itr1.next();
						}else{
							throw new EGOVException("Increment slab is not defined for payscale that matches employee's basic pay");
						}
						
						
						//		incrementDet.setAmount(incrementSlab.getIncSlabAmt());	
						//UserManager userManager = PayrollManagersUtill.getUserManager();
						String userName =salPaySlipForm.getUserName();
						//User user = userManager.getUserByUserName(userName);
						
						//		incrementDet.setCreatedby(user);
						//		incrementDet.setCreateddate(new Date());
						//		PersonalInformation emp = eismgr.getEmloyeeById(Integer.parseInt(salPaySlipForm.getEmployeeCodeId()));
						//		incrementDet.setEmployee(emp);
						//		incrementDet.setFinancialyear(commonsManager.findFinancialYearById(new Long(salPaySlipForm.getYear())));
						//		incrementDet.setMonth(new BigDecimal(salPaySlipForm.getMonth()));
						//		incrementDet.setIncrementDate(calendar.getTime());
						//		incrementDet.setRemarks("Increment is Applied");
						//		incrementDet.setStatus(PayrollConstants.EMP_INCREMENT_DETAILS_STATUS_RESOLVED);
						//		HibernateUtil.getCurrentSession().save(incrementDet);
						
						currBasicPay=currBasicPay.add(incrementSlab.getIncSlabAmt());	
					}*/
				}
			}
							
		
	
		if(paydetail.getSalaryCodes().getIsAttendanceBased()=='Y')
		{
		
			double d=0.0;
			if(payst.getCurrBasicPay()!=null)
			{
				d = currBasicPay.doubleValue()*
				Float.parseFloat(salPaySlipForm.getNumDays()) /Float.parseFloat(salPaySlipForm.getWorkingDays());							
			    //d = Math.round(d*(Float.parseFloat(salPaySlipForm.getNumDays())));	
			}
			else if(payst.getDailyPay()!=null)
			{
				d=currBasicPay.doubleValue()*new Double(salPaySlipForm.getNumDays());
				//d=Math.round(d*(Float.parseFloat(salPaySlipForm.getNumDays())));
			}
		    
		    currBasicPay=new BigDecimal(Math.round(d));
		    salPaySlipForm.setBasicPay(currBasicPay.toString());
		    
		}
		return currBasicPay;
	
	}
	private int getPayHeadIndex(String[] payheads,String payhead)
	{
		for(int i=0;i<payheads.length;i++)
		{
			if(payheads[i].equals(payhead))
				{
				return i;
				}
		}
		return 0;
	}
	
	/*
	 * @return Float[]
	 * attDays[0] = no.of paiddays in pay processing period
	 * attDays[1] = total number of calendar days in pay processing period	
	 */	
	public Float[] getWorkingDaysForPayslip(SalaryPaySlipForm salPaySlipForm)  throws ParseException, Exception {
		final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATEFORMATESTR,Locale.getDefault());
		PersonalInformation emp = payrollExternalInterface.getEmloyeeById( Integer.valueOf(salPaySlipForm.getEmployeeCodeId()));
		Float[] attDays=null ;
		float noofdaysmonth=0;
		float noofpaiddays=0;		
		if(salPaySlipForm.getEffectiveFrom()!=null && !salPaySlipForm.getEffectiveFrom().equals(""))
		{
			LOGGER.debug("emp code"+emp.getEmployeeCode());
			EmployeeAttendenceReport empatcreport=payrollExternalInterface.getEmployeeAttendenceReportBetweenTwoDates(FORMATTER.parse(salPaySlipForm.getEffectiveFrom()),FORMATTER.parse(salPaySlipForm.getEffectiveTo()),emp);
			noofdaysmonth =empatcreport.getDaysInMonth();
			//	request.getSession().setAttribute("noofdaysmonth",noofdaysmonth);			
			noofpaiddays=empatcreport.getNoOfPaidDays()==null?0:empatcreport.getNoOfPaidDays();
			LOGGER.info("noofdaysmonth="+noofdaysmonth+"noofpaiddays="+noofpaiddays);
			
		}
		/***
		 * 
		 *else
		{ //In case of Supplementary payslips - Arrear, Final Settlement etc - the paid days and number of days are set in form directly 
			//FIXME: naming convention is wrong . Non Intuitive
			noofdaysmonth=Float.parseFloat(salPaySlipForm.getWorkingDays());
			noofpaiddays=Float.parseFloat(salPaySlipForm.getNumDays());			
		} **/
		attDays = new Float[2];		
		attDays[0] = noofpaiddays;
		attDays[1] = noofdaysmonth;
		return attDays ;
	}	
	public void updateIncrementDetails(PersonalInformation emp,GregorianCalendar todate,User user,String type) throws Exception
	{
	
	    PayStructure payStructure =PayrollManagersUtill.getPayRollService().getPayStructureForEmpByDate(emp.getIdPersonalInformation(),todate.getTime());
		GregorianCalendar calendar= new GregorianCalendar();
		calendar.setTime(payStructure.getAnnualIncrement());
		BigDecimal incrementMonth = new BigDecimal(calendar.get(Calendar.MONTH)).add(BigDecimal.ONE);
		calendar.setTime(payStructure.getAnnualIncrement());
		HibernateUtil.getCurrentSession().enableFilter("payScaleslIncFilter").setParameter("basicAmount",
				 Long.valueOf(payStructure.getCurrBasicPay().longValue()));
		if (incrementMonth.intValue()==(todate.get(Calendar.MONTH)+1))
		{
			IncrementDetails incrementDet = new IncrementDetails();
			Iterator itr=payStructure.getPayHeader().getIncrSlabs().iterator();
			IncrementSlabsForPayScale incrementslab=(IncrementSlabsForPayScale)itr.next();
			incrementDet.setAmount(incrementslab.getIncSlabAmt());
			incrementDet.setCreatedby(user);
			incrementDet.setCreateddate(new Date());
			incrementDet.setEmployee(emp);
			incrementDet.setFinancialyear(PayrollManagersUtill.getPayRollService().getFinancialYearByDate(todate.getTime()));
			incrementDet.setMonth(new BigDecimal(todate.get(Calendar.MONTH)+1));
			incrementDet.setIncrementDate(calendar.getTime());
			if(type.equals(PayrollConstants.EMP_PAYSLIP_PAYTYPE_NORMAL))
			{
				incrementDet.setAmount(incrementslab.getIncSlabAmt());
				incrementDet.setStatus(PayrollConstants.EMP_INCREMENT_DETAILS_STATUS_RESOLVED);
			}else
			{
				incrementDet.setAmount(incrementslab.getIncSlabAmt());
				incrementDet.setRemarks("Increment is Pending BZ of Supplimentary Pay");
				incrementDet.setStatus(PayrollConstants.EMP_INCREMENT_DETAILS_STATUS_PENDING);
			}
			HibernateUtil.getCurrentSession().save(incrementDet);
		}
	 
	}
}
