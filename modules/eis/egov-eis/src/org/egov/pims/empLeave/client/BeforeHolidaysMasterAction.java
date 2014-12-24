package org.egov.pims.empLeave.client;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.pims.service.EmployeeService;
import org.egov.pims.utils.EisManagersUtill;


public class BeforeHolidaysMasterAction extends DispatchAction
{
	private static  final Logger LOGGER = Logger.getLogger(BeforeHolidaysMasterAction.class);
	private EmployeeService employeeService;
	

	public ActionForward beforeCreate(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
	    {
			populate(req);
			req.getSession().setAttribute("viewMode","create");
			target = "createScreen";


		}
		catch(Exception ex)
		{

			target = "error";
			LOGGER.debug("Exception Encountered!!!"+ex.getMessage());
			 throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
		}
		return mapping.findForward(target);
	}

	public ActionForward modify(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
	    {
			populate(req);
			req.getSession().setAttribute("viewMode","modify");
			target = "modifyScreen";
		}
		catch(Exception ex)
		{
			target = "error";
			LOGGER.debug("Exception Encountered!!!"+ex.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + ex.getMessage(),ex);
		}
		return mapping.findForward(target);
	}

	public ActionForward setIdForDetailsView(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
		{
			populate(req);
			req.getSession().setAttribute("viewMode","view");
			target = "view";

		}
		catch(Exception e)
		{
			target = "error";

			LOGGER.debug("Exception Encountered!!!"+e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}

		return mapping.findForward(target);

	}
	private void populate(HttpServletRequest req)
	{
		try {
			ArrayList wdaysconstntsList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-Wdaysconstnts");
			req.getSession().setAttribute("wdaysconstntsMap",EisManagersUtill.getFinMap(wdaysconstntsList,null,null));
			List fYMasterList=EisManagersUtill.getCommonsService().getAllActiveFinancialYearList();
			req.getSession().setAttribute("finMap",EisManagersUtill.getFinMap(fYMasterList,"id","finYearRange"));
		} catch (EGOVRuntimeException e) {
			// TODO Auto-generated catch block
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		
	}
	
	private final static String STR_EXCEPTION = "Exception:";


	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	
	

}