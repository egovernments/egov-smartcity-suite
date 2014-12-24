package org.egov.payroll.client.payhead;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.ObjectHistory;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.user.User;
import org.egov.model.recoveries.Recovery;
import org.egov.payroll.model.SalaryCategoryMaster;
import org.egov.payroll.model.SalaryCodes;
import org.egov.payroll.services.payhead.PayheadService;
import org.egov.payroll.utils.PayrollConstants;
import org.egov.payroll.utils.PayrollExternalInterface;
import org.egov.payroll.utils.PayrollManagersUtill;

public class ModifyPayheadAction extends Action{
	private static final Logger LOGGER = Logger.getLogger(ModifyPayheadAction.class);	
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
			HttpSession session = request.getSession();
			PayheadDefineForm payheadForm = (PayheadDefineForm)form;
			PayheadService payheadService = PayrollManagersUtill.getPayheadService();
			String userName=payrollExternalInterface.getUserByID(Integer.valueOf(EGOVThreadLocals.getUserId())).getUserName();
			SalaryCategoryMaster salCategoryMaster;
			SalaryCodes pctBasis = null;
			
			CChartOfAccounts chartOfAccount = payrollExternalInterface.getCChartOfAccountsByGlCode(payheadForm.getGlcode());		
			salCategoryMaster = payheadService.getSalCategoryMaster(payheadForm.getCategoryE());			
			if(payheadForm.getPctBasis()!=null)
			{
				pctBasis = payheadService.getSalaryCodeByHead(payheadForm.getPctBasis());			
			}
			
			User createdBy = payrollExternalInterface.getUserByUserName(userName);
			User lastModifiedby = payrollExternalInterface.getUserByUserName(userName);						
			SalaryCodes salaryCode = payheadService.getSalaryCodeByHead(payheadForm.getName());		
			salaryCode.setHead(payheadForm.getName());
			salaryCode.setOrderId(Long.valueOf(payheadForm.getOrderNo()));
			salaryCode.setDescription(payheadForm.getDescription());
			salaryCode.setCalType(payheadForm.getCalType());
			salaryCode.setSalaryCodes(pctBasis);		
			salaryCode.setLocalLangDesc(payheadForm.getLocalLangDesc());
			if(payheadForm.getIsTaxable()==null)
			{
				salaryCode.setIsTaxable('N');
			}
			else
			{
				salaryCode.setIsTaxable('Y');		
			}
			if(payheadForm.getIsAttendanceBased()==null)
			{
				salaryCode.setIsAttendanceBased('N');
			}
			else
			{
				salaryCode.setIsAttendanceBased(payheadForm.getIsAttendanceBased().trim().toCharArray()[0]);
			}
			if(payheadForm.getIsRecomputed()==null)
			{
				salaryCode.setIsRecomputed('N');
			}
			else
			{
				salaryCode.setIsRecomputed(payheadForm.getIsRecomputed().trim().toCharArray()[0]);
			}
			if(payheadForm.getIsRecurring()==null)
			{
				salaryCode.setIsRecurring('N');
			}
			else
			{
				salaryCode.setIsRecurring(payheadForm.getIsRecurring().trim().toCharArray()[0]);
			}
			if(payheadForm.getCaptureRate()==null)
			{
				salaryCode.setCaptureRate('N');
			}
			else
			{
				salaryCode.setCaptureRate(payheadForm.getCaptureRate().trim().toCharArray()[0]);
			}
			
			
			//Setting advance validation rule script
			if(payheadForm.getRuleScript()!=null && !("").equals(payheadForm.getRuleScript().trim()))
			{
				String query = " from org.egov.infstr.workflow.Action act where act.id = ? ";
				org.egov.infstr.workflow.Action ruleScript = (org.egov.infstr.workflow.Action)actionService.find(query,Long.parseLong(payheadForm.getRuleScript()));
				salaryCode.setValidationRuleScript(ruleScript);
			}else{
				salaryCode.setValidationRuleScript(null);
			}
			
			//salaryCode.setChartofaccounts(chartOfAccount);		
			salaryCode.setCategoryMaster(salCategoryMaster);		
			salaryCode.setCreateddate(new Date());		
			salaryCode.setLastmodifiedby(lastModifiedby);
			salaryCode.setCreatedby(createdBy);
			if( null != payheadForm.getRecoveryAC() && !("").equals(payheadForm.getRecoveryAC())){
				Recovery tds = payrollExternalInterface.getTdsById(Integer.parseInt(payheadForm.getRecoveryAC()));
				salaryCode.setTdsId(tds);
				salaryCode.setChartofaccounts(null);
			}
			else if(null != payheadForm.getSlabAC() && !("").equals(payheadForm.getSlabAC())){
				Recovery tds = payrollExternalInterface.getTdsById(Integer.parseInt(payheadForm.getSlabAC()));
				salaryCode.setTdsId(tds);
				salaryCode.setChartofaccounts(null);
			}
			else{		
				salaryCode.setTdsId(null);
				salaryCode.setChartofaccounts(chartOfAccount);
			}	
			LOGGER.info("isinteres----------"+payheadForm.getIsInterest());
			if(payheadForm.getIsInterest() != null){
				CChartOfAccounts interestAccount = payrollExternalInterface.getCChartOfAccountsByGlCode(payheadForm.getInterestGlcode());
				salaryCode.setInterestAccount(interestAccount);
			}
			else
			{
				salaryCode.setInterestAccount(null);
			}
			payrollExternalInterface.doAuditing(AuditEntity.PAYROLL_PAYHEADMASTER, PayrollConstants.MODIFY_PAYHEAD, salaryCode);
			payheadService.createPayHead(salaryCode);
			ObjectHistory objHistory = new ObjectHistory();
			objHistory.setModifiedBy(createdBy);
			objHistory.setObjectId(salaryCode.getId());
			objHistory.setObjectType(payrollExternalInterface.getObjectTypeByType("payhead"));
			objHistory.setRemarks(payheadForm.getModifyRemarks());
			payrollExternalInterface.createObjectHistory(objHistory);
			
			session.setAttribute("payhead", salaryCode);	
			target = "success";
			
		}
		catch(EGOVRuntimeException ex)
	    {
			LOGGER.error("EGOVRuntimeException Encountered!!!"+ ex.getMessage());
	        target = "error";
	        HibernateUtil.rollbackTransaction();
	        LOGGER.error(ex.getMessage());
	    }
		catch(Exception e)
		{
			LOGGER.error("Error while getting data>>>>>"+e.getMessage());
			target = "error";
			HibernateUtil.rollbackTransaction();
			LOGGER.error(e.getMessage());
		}	
		return actionMapping.findForward(target);
	}

	public void setPayrollExternalInterface(
			PayrollExternalInterface payrollExternalInterface) {
		this.payrollExternalInterface = payrollExternalInterface;
	}
}
