package org.egov.payroll.client.payslipApprove;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.payroll.model.EmpPayroll;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.services.payhead.PayheadService;
import org.egov.payroll.services.payslipApprove.PayslipApproveService;
import org.egov.payroll.utils.PayrollManagersUtill;

/**
 * @author eGov
 *
 */
public class PayslipInfoAction extends Action {
private static final Logger LOGGER = Logger.getLogger(PayslipInfoAction.class);
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		try
		{
			PayslipInfoForm payslipInfoForm = (PayslipInfoForm) form;
			Integer id = Integer.parseInt(request.getParameter("payslipId"));				
			PayslipApproveService payslipApproveService = PayrollManagersUtill.getPayslipApproveService();
			PayheadService payheadService = PayrollManagersUtill.getPayheadService();
			List<SalaryCodes> salaryCodes = payheadService.getAllSalaryCodesByCache(); 
			EmpPayroll empPayroll = payslipApproveService.getPayslipById(id);
			HttpSession session = request.getSession();
			session.setAttribute("payslip", empPayroll);
			session.setAttribute("salaryCodes", salaryCodes);
			payslipInfoForm.setEmpPayroll(empPayroll);
		}catch(Exception e)
		{
			LOGGER.error("Error while getting data in PayslipInfoAction>>>>>"+e.getMessage());
		}
		return actionMapping.findForward("success");
	}
}
