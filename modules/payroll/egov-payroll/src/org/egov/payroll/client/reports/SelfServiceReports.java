package org.egov.payroll.client.reports;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.commons.service.CommonsService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.payroll.reports.SearchForm;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;

public class SelfServiceReports extends DispatchAction {
	private EmployeeService employeeService;
	private CommonsService commonsService;


	public ActionForward showPFReport(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response)throws Exception {
		
		PersonalInformation employee = employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		if (employee == null) 
			return actionMapping.findForward("essError");
		request.setAttribute("ess", "1");
		request.setAttribute("empId",employee.getId());
		request.setAttribute("empCode",employee.getCode());
		request.setAttribute("employeeName", employee.getName());
		
		return actionMapping.findForward("pfReport");
	}
	
	public ActionForward showPaySummaryReport(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response)throws Exception {
		
		PersonalInformation employee = employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		if (employee == null) 
			return actionMapping.findForward("essError");
		SearchForm searchForm = (SearchForm)form;
		request.setAttribute("ess", "1");
		request.setAttribute("empId",employee.getId());
		if (searchForm.getFinYr() != null)
			request.setAttribute("finyr",Integer.valueOf(searchForm.getFinYr()));
		else if (request.getParameter("finyr") == null)
			request.setAttribute("finyr",Integer.valueOf(commonsService.getCurrYearFiscalId()));
		else
			request.setAttribute("finyr",Integer.valueOf(request.getParameter("finyr")));
		
		return actionMapping.findForward("paySummaryReport");
	}
	
	public ActionForward showPayslipHistory(ActionMapping actionMapping,ActionForm form,HttpServletRequest request,
			HttpServletResponse response)throws Exception {
		PersonalInformation employee = employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		request.setAttribute("ess", "1");
		request.setAttribute("employeeName",employee.getEmployeeName());
		request.setAttribute("empcode",employee.getEmployeeCode());
		request.setAttribute("yearOfJoining",employee.getDateOfFirstAppointment());
		if (employee == null) 
			return actionMapping.findForward("essError");
		SearchForm searchForm = (SearchForm)form;
		searchForm.setEmpid(String.valueOf(employee.getId()));
		if (searchForm.getFromFinYr() == null) {
			//will provide default values
			Calendar dt = Calendar.getInstance();
			searchForm.setFromFinYr(commonsService.getCurrYearFiscalId());
			searchForm.setToFinYr(commonsService.getCurrYearFiscalId());
			Integer mon = dt.get(Calendar.MONTH) + 1; //Calendar month is zero-based
			searchForm.setToMonth(String.valueOf(mon));
			searchForm.setFromMonth(String.valueOf(mon));
		}
		
		return actionMapping.findForward("payslipHistory");
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public CommonsService getCommonsService() {
		return commonsService;
	}

	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}
	
	

}
