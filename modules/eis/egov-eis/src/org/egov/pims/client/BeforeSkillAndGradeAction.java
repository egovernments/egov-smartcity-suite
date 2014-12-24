/*
 * @(#)BeforeRegAction.java	1.00 Jan 31 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package org.egov.pims.client;
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
import org.egov.pims.service.EmployeeService;


public class BeforeSkillAndGradeAction extends DispatchAction
{

	private static  final Logger LOGGER = Logger.getLogger(BeforeSkillAndGradeAction.class);
	private EmployeeService employeeService;
	public ActionForward beforeCreate(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
	    {
			saveToken(req);
			LOGGER.info(">>> inside beforCreate");
			req.getSession().setAttribute(STR_VIEWMODE,"create");
			target = "createScreen";
		}
		catch(Exception e)
		{

			target = STR_ERROR;
			LOGGER.error(e.getMessage());
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
			populate(req);
			
			target = "modify";
			req.getSession().setAttribute("mode","modify");
		}
		catch(Exception e)
		{

			target = STR_ERROR;
			LOGGER.error(e.getMessage());
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}

		return mapping.findForward(target);


	}
	public ActionForward beforeDelete(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
		{
			LOGGER.info(">>> inside beforView");
			
			target = "delete";
			populate(req);
			req.getSession().setAttribute("mode","delete");
		}
		catch(Exception e)
		{

			target = STR_ERROR;
			LOGGER.error(e.getMessage());
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
			
			target = "modifyScreen";
		}
		catch(Exception e)
		{

			target = STR_ERROR;
			LOGGER.error(e.getMessage());
			 throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}

		return mapping.findForward(target);

	}

	private void populate(HttpServletRequest req)
	{
		try {
			ArrayList skillMasterList=(ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-SkillMaster");
			req.getSession().setAttribute("skillMap",employeeService.getMapForList(skillMasterList));
		} catch (EGOVRuntimeException e) {
			
			throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
		}
		
		

	}
	
	/**
	 * @return the eisManagr
	 */
	
	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	
	/**
	 * @param eisManagr the eisManagr to set
	 */
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	private final static String STR_VIEWMODE="viewMode";
	
	private final static String STR_ERROR = "error";
	
	private static final String STR_EXCEPTION= "Exception:";
}