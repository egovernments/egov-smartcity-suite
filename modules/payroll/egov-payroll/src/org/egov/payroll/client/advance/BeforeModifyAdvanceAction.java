package org.egov.payroll.client.advance;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.payroll.model.Advance;
import org.egov.payroll.services.advance.AdvanceService;
import org.egov.payroll.utils.PayrollManagersUtill;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;

public class BeforeModifyAdvanceAction extends Action{
	private static final Logger LOGGER = Logger.getLogger(BeforeModifyAdvanceAction.class);
	
	private EmployeeService employeeService;
	
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)throws IOException,ServletException {
		
		String target = "";
		try{
			AdvanceService salaryAdvanceService = PayrollManagersUtill.getAdvanceService();
			AdvanceForm salAdvanceform = (AdvanceForm)form;		
			if (request.getAttribute("ess") !=null || (request.getParameter("ess") != null)) {
				PersonalInformation employee = employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
				salAdvanceform.setEmployeeCodeId(String.valueOf(employee.getId()));
				salAdvanceform.setEmployeeCode(employee.getCode());
				salAdvanceform.setEmployeeName(employee.getEmployeeName());
			}
			List<Advance> salaryadvances;
			if(salAdvanceform.getEmployeeCodeId()== null || "".equals(salAdvanceform.getEmployeeCodeId())){
				salaryadvances = salaryAdvanceService.getAllSalaryadvances();
			}
			else{
				salaryadvances = salaryAdvanceService.getSalaryadvancesByEmp(Integer.parseInt(salAdvanceform.getEmployeeCodeId()));
			}
			request.setAttribute("salaryadvances", salaryadvances);
			target = salAdvanceform.getMode();
		}catch(EGOVRuntimeException ex)
	    {
			LOGGER.error("EGOVRuntimeException Encountered!!!"+ ex.getMessage());
	        target = "error";
	    }
		catch(Exception e)
		{
			LOGGER.error("Error while getting data>>>>>"+e.getMessage());
			target = "error";
		}			
		return actionMapping.findForward(target);		
	}
	
	

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
}
