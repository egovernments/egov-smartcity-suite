package org.egov.pims.client;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;

public class BeforeListAction extends DispatchAction
{
	private static  final Logger LOGGER = Logger.getLogger(BeforeListAction.class.getName());
	private EmployeeService employeeService;
	
	public ActionForward beforeCreate(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String compOffId = req.getParameter("compOffId");
		System.out.println(compOffId);
		String target = "";
		try
	    {
			String appened ="";
			if(req.getParameter("master")!=null)
			{
				appened = req.getParameter("master").trim();
				if(req.getParameter("master").equalsIgnoreCase("LeaveApplication")){
					PersonalInformation employee=employeeService.getEmpForUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
					req.setAttribute("Id", employee==null?employee:employee.getIdPersonalInformation().toString());
				}
			}
			target = "success"+appened;

		}
		catch(Exception ex)
		{

			target = "error";
			LOGGER.error(ex.getMessage());
		   throw new EGOVRuntimeException("Exception:" + ex.getMessage(),ex);
		}
		return mapping.findForward(target);
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	
}