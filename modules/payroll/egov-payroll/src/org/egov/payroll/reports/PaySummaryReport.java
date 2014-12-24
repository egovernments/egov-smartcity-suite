/**
 * PaySummaryReport.java created on 10 Aug, 2008 10:16:51 PM 
 */
package org.egov.payroll.reports;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.commons.CFinancialYear;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.payroll.services.payslip.PayRollService;
import org.egov.payroll.utils.PayrollExternalImpl;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;

/**
 * @author Eluri
 *
 */
public class PaySummaryReport extends Action {

	private static final Logger logger = Logger.getLogger(SearchAction.class);
	private static final PayrollExternalInterface payrollExternalInterface=PayrollManagersUtill.getPayrollExterInterface();
	
	public ActionForward execute(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		try{	
			Integer finyr = null;
			Integer empid = null;
			SearchForm searchform = null;
			String alertMessage=null;
			
			searchform = (SearchForm)form;
			
			finyr=(searchform.getFinYr()!=null && !searchform.getFinYr().trim().equals("")&&!searchform.getFinYr().equals("-1"))?Integer.parseInt(searchform.getFinYr()):null;			
			empid=(searchform.getEmpid()!=null && !( searchform.getEmpid().trim().equals("")||searchform.getEmpid().equals("-1")))?Integer.parseInt(searchform.getEmpid()):null;
			if (request.getAttribute("ess") !=null || (request.getParameter("ess") != null) && (finyr == null)) {
				empid = (Integer)request.getAttribute("empId");
				finyr = (Integer)request.getAttribute("finyr");
				searchform.setEmpid(empid.toString());
				searchform.setFinYr(finyr.toString());
			}
			if( empid == null)
			{
				alertMessage="Please Select Employee";
			}
			if( finyr == null )
			{
				alertMessage="Please Select Financial Year";
			}
			
			if(empid!=null && finyr != null )
			{
				List<HashMap> paysummarydetails=new ArrayList();
			    
			    List<HashMap> eardetails = PayrollManagersUtill.getPayRollService().getEarningsForEmployeeForFinYear(empid,finyr);
			    List<HashMap> deddetails = PayrollManagersUtill.getPayRollService().getDeductionsForEmployeeForFinYear(empid,finyr);
				
				// copying earnings and deductions to result map
				paysummarydetails.addAll(eardetails);
			    paysummarydetails.addAll(deddetails);
			//	paysummarydetails.add(temp) ;	
							
				CFinancialYear finyrobj=payrollExternalInterface.findFinancialYearById(Long.valueOf(finyr));
				GregorianCalendar fromdate=new GregorianCalendar();
				GregorianCalendar todate=new GregorianCalendar();
				fromdate.setTime(finyrobj.getStartingDate());
				todate.setTime(finyrobj.getEndingDate());
				// getting the employee latest assignment				
				Assignment assign = payrollExternalInterface.getLatestAssignmentForEmployeeByToDate(empid,todate.getTime());
				PersonalInformation emp = payrollExternalInterface.getEmloyeeById(empid);
				
		
				
				request.setAttribute("employeeName",emp.getEmployeeName());
				request.setAttribute("empcode",emp.getEmployeeCode());
				if(assign!=null && assign.getDesigId()!=null)
				{
				request.setAttribute("designation",assign.getDesigId().getDesignationName());	
				}
				request.setAttribute("yearOfJoining",emp.getDateOfFirstAppointment());
				
				request.setAttribute("paysummarydetails",paysummarydetails);
							
				request.setAttribute("finYear", searchform.getFinYr());			
				request.setAttribute("empid", searchform.getEmpid());
				if(paysummarydetails.isEmpty())
				{
					alertMessage="No Data Found";
				}
				
			}
			request.setAttribute("alertMessage", alertMessage);
			return actionMapping.findForward("success");	
	
		}catch(Exception e)
		{
			logger.error(e.getMessage());
			
			HibernateUtil.rollbackTransaction();
			return actionMapping.findForward("error");
		}
	
	}

}
