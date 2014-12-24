package org.egov.payroll.client.advance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.payroll.model.Advance;
import org.egov.payroll.services.advance.AdvanceService;
import org.egov.payroll.utils.PayrollManagersUtill;

public class AdvanceInfoAction extends Action{
	
	private static final Logger LOGGER = Logger.getLogger(AdvanceInfoAction.class);
	public ActionForward execute(ActionMapping actionMapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException {

		String target = "";
		try{
			AdvanceService advanceService = PayrollManagersUtill.getAdvanceService();
			AdvanceForm salaryadvanceForm = (AdvanceForm)form;
			Long id = Long.parseLong(request.getParameter("salAdvId"));	
			String selectedEmp = request.getParameter("selectedEmp");
			String mode = request.getParameter("mode");
			salaryadvanceForm.setMode(mode);
			salaryadvanceForm.setEmployeeCodeId(selectedEmp);
			LOGGER.info("SALADVANCE  ID----"+id);		
			Advance salaryadvance = advanceService.getSalaryadvanceById(id);
			LOGGER.info("FROM OBJECT"+salaryadvance.getId());
			LOGGER.info("FROM OBJECT"+salaryadvance.getSalaryCodes().getHead());
			request.setAttribute("salaryadvance", salaryadvance);		
			if("view".equals(mode) || "modify".equals(mode)){
				target = mode;		
			}
			else
			{
				target = "success";
			}
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
}
