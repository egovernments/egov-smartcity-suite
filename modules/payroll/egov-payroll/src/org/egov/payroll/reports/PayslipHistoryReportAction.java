package org.egov.payroll.reports;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.utils.PayrollExternalImpl;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.model.PersonalInformation;

/**
 * @author eGov
 * Purpose : Payslip History Report
 *
 */
public class PayslipHistoryReportAction extends Action {

	private static final Logger logger = Logger.getLogger(PayslipHistoryReportAction.class);
	//private static final CommonsManager commonsManager=PayrollManagersUtill.getCommonsManager();
	private static final PayrollExternalInterface payrollExternalInterface=PayrollManagersUtill.getPayrollExterInterface();
	
	public ActionForward execute(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		try{	
			Integer empid = null;
			Integer fromMonthid = null;
			Integer toMonthid = null;
			Integer fromyear = null;
			Integer toyear = null;
			SearchForm searchform = null;
			String alertMessage=null;
			
			searchform = (SearchForm)form; 
			
			empid=(searchform.getEmpid()!=null && !( searchform.getEmpid().trim().equals("")||searchform.getEmpid().equals("-1")))?Integer.parseInt(searchform.getEmpid()):null;
			fromMonthid=(searchform.getFromMonth()!=null && !searchform.getFromMonth().trim().equals("")&&!searchform.getFromMonth().equals("-1"))?Integer.parseInt(searchform.getFromMonth()):null;			
			toMonthid=(searchform.getToMonth()!=null && !searchform.getToMonth().trim().equals("")&&!searchform.getToMonth().equals("-1"))?Integer.parseInt(searchform.getToMonth()):null;			
			fromyear=(searchform.getFromFinYr()!=null && !searchform.getFromFinYr().trim().equals("")&&!searchform.getFromFinYr().equals("-1"))?Integer.parseInt(searchform.getFromFinYr()):null;			
			toyear=(searchform.getToFinYr()!=null && !searchform.getToFinYr().trim().equals("")&&!searchform.getToFinYr().equals("-1"))?Integer.parseInt(searchform.getToFinYr()):null;			

			if(empid!=null && fromMonthid!=null && toMonthid!=null && fromyear!=null && toyear!=null)
			{
				PayRollService payRollService = PayrollManagersUtill.getPayRollService();
				Date fromDate = payRollService.getStartDateOfMonthByMonthAndFinYear(fromMonthid, fromyear);
				Date toDate = payRollService.getStartDateOfMonthByMonthAndFinYear(toMonthid, toyear);
				
				if(fromDate.compareTo(toDate) >0 )
				{
					alertMessage = "From Date should not be greater than To Date";
				}
				else
				{
					List<HashMap> payslipHistoryDetails = PayrollManagersUtill.getPayRollService().getEarningsAndDeductionsForEmpByMonthAndYearRange(empid,fromDate,toDate);
					
					//CFinancialYear finyrobj=commonsManager.findFinancialYearById(new Long(toyear));
					//fromdate.setTime(finyrobj.getStartingDate());
					//todate.setTime(finyrobj.getEndingDate());
					// getting the employee latest assignment				
					//Assignment assign = eismgr.getLatestAssignmentForEmployeeByToDate(empid,todate.getTime());
					PersonalInformation emp = payrollExternalInterface.getEmloyeeById(empid);
					
					request.setAttribute("employeeName",emp.getEmployeeName());
					request.setAttribute("empcode",emp.getEmployeeCode());
			
					//if(assign!=null && assign.getDesigId()!=null){
						//request.setAttribute("designation",assign.getDesigId().getDesignationName());		
					//}
					request.setAttribute("yearOfJoining",emp.getDateOfFirstAppointment());
					request.setAttribute("empid", searchform.getEmpid());
					request.setAttribute("payslipHistoryDetails", payslipHistoryDetails);
					//When no data found
					if(payslipHistoryDetails.isEmpty()){
						alertMessage="No Data Found to display";
						/* searchform.setFromMonth("-1");
						searchform.setToMonth("-1");
						searchform.setFromFinYr("-1");
						searchform.setToFinYr("-1"); */
					}
				}
			}
			else
			{
				alertMessage="Please select the value for mandatory fields";
			}
			request.setAttribute("alertMessage", alertMessage);
			return actionMapping.findForward("success");	
	
		}catch(Exception e){
			logger.error(e.getMessage());
			HibernateUtil.rollbackTransaction();
			return actionMapping.findForward("error");
		}
	}
}