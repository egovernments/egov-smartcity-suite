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


public class PayheadDefineAction extends Action {
	private static final Logger logger = Logger.getLogger(PayheadDefineAction.class);
	private PayrollExternalInterface payrollExternalInterface;
	private PersistenceService actionService;
	
	public void setActionService(PersistenceService actionService) {
		this.actionService = actionService;
	}
	
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		
		HttpSession session = request.getSession();
		String target = "";
		try{
			PayheadDefineForm payHeadForm = (PayheadDefineForm) form;	
			logger.info("TESTTESTTEST");
			String userName=payrollExternalInterface.getUserByID(Integer.valueOf(EGOVThreadLocals.getUserId())).getUserName();
			logger.info("USER NAME---"+userName);
			logger.info(payHeadForm.getIsTaxable());
			
			PayheadService payheadService = PayrollManagersUtill.getPayheadService();
				
			SalaryCategoryMaster salCategoryMaster;
			SalaryCodes pctBasis = null;
			
			CChartOfAccounts chartOfAccount = payrollExternalInterface.getCChartOfAccountsByGlCode(payHeadForm.getGlcode());
			if(payHeadForm.getType().equals("E")){
				logger.info("EARNING Type --- "+payHeadForm.getCategoryE());
				salCategoryMaster = payheadService.getSalCategoryMaster(payHeadForm.getCategoryE());
			}	
			else{
				logger.info("DEDUCTION Type --- "+payHeadForm.getCategoryD());
				salCategoryMaster = payheadService.getSalCategoryMaster(payHeadForm.getCategoryD());
			}		
			if(payHeadForm.getPctBasis()!=null)
			{
				 pctBasis = payheadService.getSalaryCodeByHead(payHeadForm.getPctBasis());		
			}	
			User createdBy = payrollExternalInterface.getUserByUserName(userName);
			User lastModifiedby = payrollExternalInterface.getUserByUserName(userName);						
			SalaryCodes salaryCode = new SalaryCodes();		
			salaryCode.setHead(payHeadForm.getName());
			salaryCode.setOrderId(Long.valueOf(payHeadForm.getOrderNo()));
			salaryCode.setDescription(payHeadForm.getDescription());
			salaryCode.setCalType(payHeadForm.getCalType());
			salaryCode.setSalaryCodes(pctBasis);	
			salaryCode.setLocalLangDesc(payHeadForm.getLocalLangDesc());
			if(payHeadForm.getIsTaxable()==null)
			{
				salaryCode.setIsTaxable('N');
			}
			else
			{
				salaryCode.setIsTaxable('Y');
			}
			if(payHeadForm.getIsAttendanceBased()==null)
			{
				salaryCode.setIsAttendanceBased('N');
			}
			else
			{
				salaryCode.setIsAttendanceBased(payHeadForm.getIsAttendanceBased().trim().toCharArray()[0]);
			}
			if(payHeadForm.getIsRecomputed()==null)
			{
				salaryCode.setIsRecomputed('N');
			}
			else
			{
				salaryCode.setIsRecomputed(payHeadForm.getIsRecomputed().trim().toCharArray()[0]);
			}
			if(payHeadForm.getIsRecurring()==null)
			{
				salaryCode.setIsRecurring('N');
			}
			else
			{
				salaryCode.setIsRecurring(payHeadForm.getIsRecurring().trim().toCharArray()[0]);
			}
			if(payHeadForm.getCaptureRate()==null)
			{
				salaryCode.setCaptureRate('N');
			}
			else
			{
				salaryCode.setCaptureRate(payHeadForm.getCaptureRate().trim().toCharArray()[0]);
			}
			
			//Setting advance validation rule script
			if(payHeadForm.getRuleScript()!=null && !("").equals(payHeadForm.getRuleScript().trim()))
			{
				String query = " from org.egov.infstr.workflow.Action act where act.id = ? ";
				org.egov.infstr.workflow.Action ruleScript = (org.egov.infstr.workflow.Action)actionService.find(query,Long.parseLong(payHeadForm.getRuleScript()));
				salaryCode.setValidationRuleScript(ruleScript);
			}else{
				salaryCode.setValidationRuleScript(null);
			}
			
			//salaryCode.setChartofaccounts(chartOfAccount);		
			salaryCode.setCategoryMaster(salCategoryMaster);		
			salaryCode.setCreateddate(new Date());		
			salaryCode.setLastmodifiedby(lastModifiedby);
			salaryCode.setCreatedby(createdBy);
			if(payHeadForm.getRecoveryAC() != null && payHeadForm.getRecoveryAC() != "" ){
				Recovery tds = payrollExternalInterface.getTdsById(Integer.parseInt(payHeadForm.getRecoveryAC()));
				salaryCode.setTdsId(tds);
				salaryCode.setChartofaccounts(null);
			}
			else if(payHeadForm.getSlabAC() != null && payHeadForm.getSlabAC() != ""){
				Recovery tds = payrollExternalInterface.getTdsById(Integer.parseInt(payHeadForm.getSlabAC()));
				salaryCode.setTdsId(tds);
				salaryCode.setChartofaccounts(null);
			}
			else{		
				salaryCode.setTdsId(null);
				salaryCode.setChartofaccounts(chartOfAccount);
			}
			logger.info("isinteres--------"+payHeadForm.getIsInterest());
			if(payHeadForm.getIsInterest() != null){
				CChartOfAccounts interestAccount = payrollExternalInterface.getCChartOfAccountsByGlCode(payHeadForm.getInterestGlcode());
				salaryCode.setInterestAccount(interestAccount);
			}
			
			session.setAttribute("payhead", salaryCode);				
			payheadService.createPayHead(salaryCode);
			payrollExternalInterface.doAuditing(AuditEntity.PAYROLL_PAYHEADMASTER, PayrollConstants.CREATE_PAYHEAD, salaryCode);
			if(salaryCode.getTdsId() != null)
			{
				logger.info(salaryCode.getTdsId().getChartofaccounts().getGlcode());
			}
			if(salaryCode.getChartofaccounts() != null)
			{
				logger.info(salaryCode.getChartofaccounts().getGlcode());
			}
			target = "payheadDetails";
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
	

