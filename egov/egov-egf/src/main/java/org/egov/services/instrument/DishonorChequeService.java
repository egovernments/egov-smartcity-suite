package org.egov.services.instrument;

import java.util.Date;

import org.apache.log4j.Logger;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.DishonorCheque;
import org.egov.pims.commons.Position;
import org.egov.pims.service.EisUtilService;

public class DishonorChequeService extends PersistenceService<DishonorCheque, Long> {

	private static final Logger LOGGER = Logger.getLogger("DishonorChequeService.class");
	private SimpleWorkflowService<DishonorCheque> dishonorChqWorkflowService;
	
	private EisUtilService eisService;
	private PersistenceService persistenceService;
	private FinancialIntegrationService financialIntegrationService;
	
	public DishonorCheque approve(DishonorCheque dishonorChq,String workFlowAction,String approverComments)
	{
		startWorkflow(dishonorChq,workFlowAction,approverComments);
		return dishonorChq;          
	}
	public void startWorkflow(DishonorCheque dishonorCheque,String workFlowAction,String approverComments)
	{
		// Get cheque creator details                     
		
		if(null == dishonorCheque.getState()){                     
			Position pos = null;//This fix is for Phoenix Migration.eisService.getPrimaryPositionForUser(dishonorCheque.getPayinSlipCreator(),new Date());
			if(LOGGER.isDebugEnabled())
			LOGGER.error(pos.getName()+""+pos.getId());  
			//TODO call the updateSourceInstrumentVoucher here
			if(null!=financialIntegrationService)
			{ 
					financialIntegrationService.updateSourceInstrumentVoucher(financialIntegrationService.EVENT_INSTRUMENT_DISHONOR_INITIATED,dishonorCheque.getInstrumentHeader().getId());
			}
			//This fix is for Phoenix Migration.dishonorCheque = dishonorChqWorkflowService.start(dishonorCheque, pos, "DishonorCheque Work flow started");
			if(LOGGER.isDebugEnabled())
			LOGGER.debug("---------"+dishonorCheque);         
			dishonorChqWorkflowService.transition("forward", dishonorCheque, "Created by SM"); 
		}

		if((null != workFlowAction) && !"".equals(workFlowAction) ){
			String comments= ((null == approverComments) || "".equals(approverComments.trim()))?"":approverComments;
			dishonorChqWorkflowService.transition(workFlowAction.toLowerCase(),dishonorCheque, comments);
		}
		if(LOGGER.isDebugEnabled())
		LOGGER.error("---------"+dishonorCheque.getState().getId());
		
	}
	/*public void startChequeWorkflow(DishonorCheque dishonorCheque,String workFlowAction,String approverComments)
	{
		// Get cheque creator details                     
		
		if(null == dishonorCheque.getState()){     
			Position pos = eisService.getPrimaryPositionForUser(dishonorCheque.getApproverPositionId(),new Date());
			dishonorCheque =  (DishonorCheque)dishonorChequeWorkflowService.start(dishonorCheque, pos, "DishonorCheque Work flow started");
			dishonorChequeWorkflowService.transition("forward", dishonorCheque, "Created by SM");
		}

		if((null != workFlowAction) && !"".equals(workFlowAction) ){
			String comments= ((null == approverComments) || "".equals(approverComments.trim()))?"":approverComments;
			dishonorChequeWorkflowService.transition(workFlowAction.toLowerCase(),dishonorCheque, comments);
		}
	}
	*/
	

	
	
	
	public SimpleWorkflowService<DishonorCheque> getDishonorChqWorkflowService() {
		return dishonorChqWorkflowService;
	}
	public void setDishonorChqWorkflowService(
			SimpleWorkflowService<DishonorCheque> dishonorChqWorkflowService) {
		this.dishonorChqWorkflowService = dishonorChqWorkflowService;
	}

	public EisUtilService getEisService() {
		return eisService;
	}
	public void setEisService(EisUtilService eisService) {
		this.eisService = eisService;
	}
	public FinancialIntegrationService getFinancialIntegrationService() {
		return financialIntegrationService;
	}
	public void setFinancialIntegrationService(
			FinancialIntegrationService financialIntegrationService) {
		this.financialIntegrationService = financialIntegrationService;
	}

}
