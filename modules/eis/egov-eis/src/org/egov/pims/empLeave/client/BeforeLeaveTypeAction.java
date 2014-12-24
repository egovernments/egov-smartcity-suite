package org.egov.pims.empLeave.client;


import java.io.IOException;
import java.util.ArrayList;

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
import org.egov.pims.empLeave.service.EmpLeaveService;
import org.egov.pims.service.EmployeeService;


public class BeforeLeaveTypeAction extends DispatchAction
{
	private static  final Logger LOGGER = Logger.getLogger(BeforeLeaveTypeAction.class);
	private EmployeeService employeeService;
	private EmpLeaveService eisLeaveService;
	public ActionForward beforCreate(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
	    {
			saveToken(req);
			req.getSession().setAttribute(STR_VIEWMODE,"create");
			target = "createScreen";
		}
		catch(Exception e)
		{

			target = STR_ERROR;
			LOGGER.debug(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return mapping.findForward(target);
	}
	public ActionForward beforeModify(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
		{
			LOGGER.info(">>> inside beforeModify");
			target = "modify";
			
			req.getSession().setAttribute("mode","modify");
			populate(req);
		}
		catch(Exception e)
		{
			target = STR_ERROR;
			 LOGGER.debug(e.getMessage());
			 throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return mapping.findForward(target);
	}
	public ActionForward beforDelete(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
		{
			LOGGER.info(">>> inside beforeDelete");
			target = "delete";
			req.getSession().setAttribute("mode","delete");
			populate(req);
		}
		catch(Exception e)
		{
			target = STR_ERROR;
			LOGGER.debug("Exception Encountered!!!"+e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return mapping.findForward(target);
	}
	public ActionForward beforView(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
		{
			LOGGER.info(">>> inside beforView");
			target = "deleteFin";
			req.getSession().setAttribute("mode","view");
			populate(req);
		}
		catch(Exception e)
		{
			target = STR_ERROR;
			LOGGER.debug("Exception Encountered!!!"+e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return mapping.findForward(target);
	}
	public ActionForward setIdForDetails(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
		{
			LOGGER.info(">>> req.getParameter(Id) "+req.getParameter("Id") + " mode  "+req.getParameter(STR_VIEWMODE));
			req.getSession().setAttribute("Id",req.getParameter("Id"));
			req.getSession().setAttribute(STR_VIEWMODE,req.getParameter(STR_VIEWMODE));
			populate(req);
			target = "createScreen";
		}
		catch(Exception e)
		{
			target = STR_ERROR;
			LOGGER.debug("Exception Encountered!!!"+e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		return mapping.findForward(target);
	}
	private void populate(HttpServletRequest req)
	{
		
		try {
			ArrayList typeLeaveMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-TypeOfLeaveMaster");
			req.getSession().setAttribute("mapOfLeaveMaster",employeeService.getMapForList(typeLeaveMasterList));
		} catch (EGOVRuntimeException e) {
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		
		
	}
	
	
	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	public EmpLeaveService getEisLeaveService() {
		return eisLeaveService;
	}
	public void setEisLeaveService(EmpLeaveService eisLeaveService) {
		this.eisLeaveService = eisLeaveService;
	}


	private final static String STR_VIEWMODE="viewMode";
	private final static String STR_EXCEPTION = "Exception:";
	private final static String STR_ERROR ="error";
}