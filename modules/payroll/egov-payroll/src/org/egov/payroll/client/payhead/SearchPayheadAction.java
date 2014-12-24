package org.egov.payroll.client.payhead;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.recoveries.Recovery;
import org.egov.payroll.model.SalaryCategoryMaster;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.services.payhead.PayheadService;
import org.egov.payroll.utils.PayrollExternalImpl;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;

public class SearchPayheadAction extends Action{
private static final Logger logger = Logger.getLogger(SearchPayheadAction.class);	
	private static final String MODE="mode";
	private static final String SUCCESS="success";
	private static final String SALARYCODES="salarycodes";
    private PayrollExternalInterface payrollExternalInterface;
    private PersistenceService actionService;

	
	public void setActionService(PersistenceService actionService) {
		this.actionService = actionService;
	} 
	
	
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String target = "";
		try{
			PayheadService payheadService = PayrollManagersUtill.getPayheadService();			
			List<SalaryCodes> salarycodes = payheadService.getAllSalaryCodesByCache();
			List<SalaryCategoryMaster> salaryCategorysE = payheadService.getAllCategoryMasterByType("E");
			List<SalaryCategoryMaster> salaryCategorysD = payheadService.getAllCategoryMasterByType("D");
			PayheadDefineForm payheadDefineForm = (PayheadDefineForm)form;
			String mode = request.getParameter(MODE);
			//SalaryCodes salarycode=null;
			//payheadDefineForm.setAction(mode);
			String action = payheadDefineForm.getAction();
			SalaryCodes salarycode = payheadService.getSalaryCodeByHead(payheadDefineForm.getName());
			List<Recovery> tdses = payrollExternalInterface.getAllTdsByPartyType("Employee");
			List<Action> wfActionList = actionService.findAllByNamedQuery("BY_TYPE", "AdvSalarycode");
			for(Recovery tds : tdses){
				logger.info("TESTSSSSSSSSSSSSS--------"+tds.getId());	
			}
			if("Modify".equalsIgnoreCase(action)){			
				request.setAttribute("salarycode", salarycode);		
				request.setAttribute("salaryCategorysE", salaryCategorysE);
				request.setAttribute("salaryCategorysD", salaryCategorysD);
				request.setAttribute("tdses", tdses);
				request.setAttribute("wfActionList", wfActionList);
				target = action;				
			}
			else if("View".equalsIgnoreCase(action)){
				request.setAttribute("salarycode", salarycode);	
				target = action;
			}
			else if("update".equalsIgnoreCase(action))
			{
				request.setAttribute("salarycode", salarycode);
				request.setAttribute(SALARYCODES, salarycodes);
				request.setAttribute(MODE, mode);
				target = SUCCESS;	
			}
			
			else{		
				request.setAttribute(SALARYCODES, salarycodes);
				request.setAttribute(MODE, mode);
				target = SUCCESS;				
			}
			if("PayRule".equals(request.getParameter("master")))
			{
				request.setAttribute(SALARYCODES, salarycodes);	
				request.setAttribute(MODE,"create");
				target = SUCCESS;	
				
			}
			if("PayRule".equals(request.getParameter("master"))&&"view".equals(request.getParameter(MODE)))
			{
				request.getSession().removeAttribute("id");	
				request.getSession().removeAttribute("salarycodeList");	
				request.getSession().removeAttribute("wfActionList");	
				request.getSession().removeAttribute("ruleList");	
				request.setAttribute(SALARYCODES, salarycodes);	
				request.setAttribute(MODE,"viewRule");
				target = SUCCESS;	
				
			}
		}catch(EGOVRuntimeException ex)
        {
            logger.error("EGOVRuntimeException Encountered!!!"+ ex.getMessage());
            target = "error";
            HibernateUtil.rollbackTransaction();
        }
		catch(Exception e)
		{
			logger.error("Error while getting data>>>>>"+e.getMessage());
			target = "error";
			HibernateUtil.rollbackTransaction();
		}	
		return actionMapping.findForward(target);
	}



	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}
}
