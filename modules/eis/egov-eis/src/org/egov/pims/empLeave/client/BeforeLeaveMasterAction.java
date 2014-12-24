package org.egov.pims.empLeave.client;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

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
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.dao.DesignationMasterDAO;
import org.egov.pims.service.EmployeeService;


public class BeforeLeaveMasterAction extends DispatchAction
{
	private static  final Logger LOGGER = Logger.getLogger(BeforeLeaveMasterAction.class);
	private EmployeeService employeeService;
	

	public ActionForward view(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
	    {
			populate(req);
			String mode ="";
			if(req.getParameter("mode")==null)
			{
				mode = ((String)req.getSession().getAttribute("mode")).trim();
				
				
			}
			else
			{
				mode = req.getParameter("mode").trim();
			}
			
			req.getSession().setAttribute("viewMode",mode);
			target = "viewjsp";
		}
		catch(Exception e)
		{
			target = "error";
			LOGGER.debug("Exception Encountered!!!"+e.getMessage());
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		return mapping.findForward(target);
	}

	public ActionForward forward(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
	    {
			saveToken(req);
			populate(req);
			target = "createScreen";
		}
		catch(Exception e)
		{
			target = "error";
			LOGGER.debug("Exception Encountered!!!"+e.getMessage());
			HibernateUtil.rollbackTransaction();
		   throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		return mapping.findForward(target);
	}
	
	private void populate(HttpServletRequest req)
	{
		try {
			DesignationMasterDAO designationMasterDAO = new DesignationMasterDAO();
			Map mapOfDesignation = designationMasterDAO.getAllDesignationMaster();
			req.getSession().setAttribute("mapOfDesignation",mapOfDesignation);
			ArrayList typeMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-TypeOfLeaveMaster");
			req.getSession().setAttribute("typeOfLeaveMap",employeeService.getMapForList(typeMasterList));
		} catch (EGOVRuntimeException e) {
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}
		
	}

	public EmployeeService getEmployeeService() {
		return employeeService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	
	
	
}