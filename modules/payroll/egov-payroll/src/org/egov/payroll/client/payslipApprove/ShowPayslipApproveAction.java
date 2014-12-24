package org.egov.payroll.client.payslipApprove;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * @author eGov
 *
 */
public class ShowPayslipApproveAction extends Action {
	private static final Logger LOGGER = Logger.getLogger(ShowPayslipApproveAction.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		try
		{
			HttpSession session = request.getSession();
			ShowPayslipForm showPayslipForm = (ShowPayslipForm)form;		
			
			session.setAttribute("year", showPayslipForm.getYear());
			session.setAttribute("month", showPayslipForm.getMonth());	
			session.setAttribute("deptId", showPayslipForm.getDeptId());
		}
		catch(Exception e)
		{
			LOGGER.error("Error while getting data in ShowPayslipApproveAction>>>>>"+e.getMessage());
		}
		return actionMapping.findForward("success");
	}
}
