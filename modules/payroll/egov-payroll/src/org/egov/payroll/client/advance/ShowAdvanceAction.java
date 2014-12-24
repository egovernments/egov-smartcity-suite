/*package org.egov.payroll.client.advance;

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
import org.egov.payroll.utils.PayrollManagersUtill;

public class ShowAdvanceAction extends Action{
	
	private static final Logger LOGGER = Logger.getLogger(ShowAdvanceAction.class);
	public ActionForward execute(ActionMapping actionMapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws IOException,ServletException {
		
		String target = "";
		try{
			AdvanceForm advanceForm = (AdvanceForm)form;
			Long advanceId = null;
			if(advanceForm == null || advanceForm.getAdvanceId() == null){
				advanceId = (Long)request.getSession().getAttribute("advanceId");
			}
			else{
				advanceId = Long.parseLong(advanceForm.getAdvanceId());
			}
			Advance advance = PayrollManagersUtill.getSalaryadvanceManager().getSalaryadvanceById(advanceId);
			advanceForm.setAdvance(advance);
			target = "success";
		}catch(EGOVRuntimeException ex){
			LOGGER.error("EGOVRuntimeException Encountered!!!"+ ex.getMessage());            
            throw ex;
        }
		catch(Exception e){
			LOGGER.error("Error while getting data>>>>>"+e.getMessage());			
		}	
		return actionMapping.findForward(target);
	}

}
*/