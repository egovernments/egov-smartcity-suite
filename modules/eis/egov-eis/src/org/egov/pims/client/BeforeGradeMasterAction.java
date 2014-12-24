package org.egov.pims.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.pims.service.EmployeeService;

public class BeforeGradeMasterAction extends DispatchAction 
{
	public final static Logger LOGGER = Logger.getLogger(BeforeGradeMasterAction.class.getClass());
	private EmployeeService employeeService;
	public ActionForward beforeCreate(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res)
    throws IOException, ServletException
    {
			String target = "";
		    try
		    {
		        saveToken(req);
		        req.setAttribute(STR_MODE, "create");
		        target = "createScreen";
		       
		    }
		    catch(Exception e)
		    {
		        target = STR_ERROR;
		        LOGGER.error((new StringBuilder("Exception Encountered!!!")).append(e.getMessage()).toString());
		        throw new EGOVRuntimeException((new StringBuilder(STR_EXCEPTION)).append(e.getMessage()).toString(), e);
		    }
    return mapping.findForward(target);
}
	 public ActionForward beforeView(ActionMapping mapping,ActionForm form, HttpServletRequest req,HttpServletResponse res)
	   {
		   String target="";
		   try {
			   populate(req);
			   target="view";
			   req.setAttribute(STR_MODE, "view");
		} catch(Exception e)
	    {
	        target = STR_ERROR;
	        LOGGER.error((new StringBuilder("Exception Encountered!!!")).append(e.getMessage()).toString());
	        throw new EGOVRuntimeException((new StringBuilder(STR_EXCEPTION)).append(e.getMessage()).toString(), e);
	    }
		  return mapping.findForward(target);
	   }
	 
	 public ActionForward beforeModify(ActionMapping mapping,ActionForm form, HttpServletRequest req,HttpServletResponse res)
	   {
		   String target="";
		   try {
			   populate(req);
			   target="modify";
			   req.setAttribute(STR_MODE, "modify");
		} catch(Exception e)
	    {
	        target = STR_ERROR;
	        LOGGER.error((new StringBuilder("Exception Encountered!!!")).append(e.getMessage()).toString());
	        throw new EGOVRuntimeException((new StringBuilder(STR_EXCEPTION)).append(e.getMessage()).toString(), e);
	    }
		  return mapping.findForward(target);
	   }
	 public ActionForward beforeDelete(ActionMapping mapping,ActionForm form, HttpServletRequest req,HttpServletResponse res)
	   {
		   String target="";
		   try {
			   populate(req);
			   target="delete";
			   req.setAttribute(STR_MODE, "delete");
		} catch(Exception e)
	    {
	        target = STR_ERROR;
	        LOGGER.error(e.getMessage());
	       	throw new EGOVRuntimeException((new StringBuilder(STR_EXCEPTION)).append(e.getMessage()).toString(), e);
	    }
		  return mapping.findForward(target);
	   }

	
	
	private void populate(HttpServletRequest req)
	    {
		 	HashMap genericMap = new HashMap();
		 	try {
		 		
		 		ArrayList gradeMasterList = (ArrayList) employeeService.getAllGradeMstr();
		 		req.getSession().setAttribute("GradeMasters",gradeMasterList);
				genericMap.put("GradeMaster", employeeService.getMapForList(gradeMasterList));
				//ArrayList gradeMasterList = (ArrayList)EgovMasterDataCaching.getInstance().get("egEmp-GradeMaster");
				//genericMap.put("GradeMaster", employeeService.getMapForList(gradeMasterList));
				req.getSession().setAttribute("genericMap", genericMap);
			} catch (EGOVRuntimeException e) {
				LOGGER.error(e.getMessage());
				throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
				
			}
			catch (Exception e) {
				LOGGER.error(e.getMessage());
				throw new EGOVRuntimeException(STR_EXCEPTION + e.getMessage(),e);
				
			}
		 	
	    }
	
	public EmployeeService getEmployeeService() {
		return employeeService;
	}
	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}


	private final static String STR_MODE="mode";
	private final static String STR_ERROR="error";
	private final static String STR_EXCEPTION="Exception:";
}
