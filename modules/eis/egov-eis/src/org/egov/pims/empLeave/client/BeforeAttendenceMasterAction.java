package org.egov.pims.empLeave.client;


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
import org.egov.infstr.utils.HibernateUtil;

public class BeforeAttendenceMasterAction extends DispatchAction
{
	private static  final Logger LOGGER = Logger.getLogger(BeforeHolidaysMasterAction.class);
	public ActionForward beforeCreate(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
	    {
			req.getSession().setAttribute("viewMode","create");
			target = "createScreen";
			
		}
		catch(Exception ex)
		{

			target = "error";
			LOGGER.error("Exception Encountered!!!"+ex.getMessage());
			throw new EGOVRuntimeException("Exception:" + ex.getMessage(),ex);
		}
		return mapping.findForward(target);
	}

	public ActionForward setIdForDetailsModify(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
		{
			req.getSession().setAttribute("viewMode","modify");
			target = "modify";
		}
		catch(Exception e)
		{
			target = "error";
			LOGGER.debug("Exception Encountered!!!"+e.getMessage());
			throw new EGOVRuntimeException("Exception:" + e.getMessage(),e);
		}

		return mapping.findForward(target);

	}
	public ActionForward setIdForDetailsView(ActionMapping mapping,ActionForm form,HttpServletRequest req,HttpServletResponse res)
	throws IOException,ServletException
	{
		String target = "";
		try
		{

			req.getSession().setAttribute("viewMode","view");
			target = "view";

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


	
}