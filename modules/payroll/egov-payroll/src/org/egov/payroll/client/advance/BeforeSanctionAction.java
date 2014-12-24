/*package org.egov.payroll.client.advance;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.EgwStatus;
import org.egov.lib.rjbac.user.User;
import org.egov.payroll.model.Advance;
import org.egov.payroll.services.advance.AdvanceService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalImpl;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;

public class BeforeSanctionAction extends Action{
	private static final Logger LOGGER = Logger.getLogger(BeforeSanctionAction.class);
	private static final PayrollExternalInterface payrollExternalInterface=new PayrollExternalImpl();
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm form, HttpServletRequest request,
													HttpServletResponse response)throws IOException,ServletException {
		
		String target = "";
		try{
			AdvanceManager salaryadvanceManager = PayrollManagersUtill.getSalaryadvanceManager();
			HttpSession session = request.getSession();
			String userName =(String) session.getAttribute("com.egov.user.LoginUserName");
			User user = payrollExternalInterface.getUserByUserName(userName);
			EgwStatus status = payrollExternalInterface.getStatusByModuleAndDescription(PayrollConstants.SAL_ADVANCE_MODULE,PayrollConstants.SAL_ADVANCE_CREATE);
			//List<Advance> salaryadvances = salaryadvanceManager.getAllSalaryadvancesByStatus(status);
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
			String sanctiondate = sdf.format(date);
			List<Advance> salaryadvances = salaryadvanceManager.getAllSalaryAdvancesOfSuperiorByStatus(user, date, status);
			request.setAttribute("salaryadvances", salaryadvances);						
			session.setAttribute("sanctionBy", userName);	
			session.setAttribute("sanctionDate", sanctiondate);
			target = "success";
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
*/