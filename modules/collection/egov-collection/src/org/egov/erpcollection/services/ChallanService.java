package org.egov.erpcollection.services;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.erpcollection.models.Challan;
import org.egov.erpcollection.util.CollectionsUtil;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.workflow.WorkflowService;
import org.egov.pims.commons.Position;

/**
 * Provides services related to receipt header
 */
public class ChallanService extends
		PersistenceService<Challan, Long> {
	
	private static final Logger LOGGER = Logger.getLogger(ChallanService.class);
	
	private WorkflowService<Challan> challanWorkflowService;
	
	private CollectionsUtil collectionsUtil;
	
	public void setCollectionsUtil(CollectionsUtil collectionsUtil) {
		this.collectionsUtil = collectionsUtil;
	}

	public void setChallanWorkflowService(
			WorkflowService<Challan> challanWorkflowService) {
		this.challanWorkflowService = challanWorkflowService;
	}


	/**
	 * This method performs the Challan workflow transition. 
	 * The challan status is updated and transitioned to the next state.
	 * At the end of the transition the challan will be available in the inbox 
	 * of the user of the position specified.
	 * 
	 * @param challan the <code>Challan</code> instance which has to under go the 
	 * 					workflow transition
	 * 
	 * @param nextPosition the position of the user to whom the challan must next 
	 * 						be assigned to.
	 * 
	 * @param actionName a <code>String</code> representing the state to which the 
	 * 					challan has to transition.
	 * 
	 * @param remarks a <code>String</code> representing the remarks for the 
	 * 				current action
	 * 
	 * @throws EGOVRuntimeException 
	 */
	public void workflowtransition(Challan challan,Position nextPosition, 
			String actionName, String remarks)
			throws EGOVRuntimeException {
		// to initiate the workflow
		if(challan.getState()==null){
			challanWorkflowService.start(challan, collectionsUtil
					.getPositionOfUser(challan.getCreatedBy()),
					"Challan Workflow Started");
			LOGGER.debug("Challan Workflow Started.");
				
		}
		
		if(CollectionConstants.WF_ACTION_NAME_NEW_CHALLAN.equals(actionName) || 
				CollectionConstants.WF_ACTION_NAME_MODIFY_CHALLAN.equals(actionName)){
			challan.setStatus(collectionsUtil.getEgwStatusForModuleAndCode(
					CollectionConstants.MODULE_NAME_CHALLAN, 
					CollectionConstants.CHALLAN_STATUS_CODE_CREATED));
			challan.changeState(CollectionConstants.WF_STATE_CREATE_CHALLAN, 
					CollectionConstants.WF_ACTION_NAME_CHECK_CHALLAN,
					nextPosition, CollectionConstants.CHALLAN_CREATION_REMARKS);
		}
		
		if(CollectionConstants.WF_ACTION_NAME_CHECK_CHALLAN.equals(actionName)){
			challan.setStatus(collectionsUtil.getEgwStatusForModuleAndCode(
					CollectionConstants.MODULE_NAME_CHALLAN, 
					CollectionConstants.CHALLAN_STATUS_CODE_CHECKED));
			challan.changeState(CollectionConstants.WF_STATE_CHECK_CHALLAN, 
					CollectionConstants.WF_ACTION_NAME_APPROVE_CHALLAN,
					nextPosition, remarks);
		}
		
		if(CollectionConstants.WF_ACTION_NAME_APPROVE_CHALLAN.equals(actionName)){
			challan.setStatus(collectionsUtil.getEgwStatusForModuleAndCode(
					CollectionConstants.MODULE_NAME_CHALLAN, 
					CollectionConstants.CHALLAN_STATUS_CODE_APPROVED));
			challan.changeState(CollectionConstants.WF_STATE_APPROVE_CHALLAN, 
					CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN,
					nextPosition, remarks);
		}
		
		if(CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN.equals(actionName)){
			challan.setStatus(collectionsUtil.getEgwStatusForModuleAndCode(
					CollectionConstants.MODULE_NAME_CHALLAN, 
					CollectionConstants.CHALLAN_STATUS_CODE_VALIDATED));
			challan.changeState(CollectionConstants.WF_STATE_VALIDATE_CHALLAN, 
					CollectionConstants.WF_ACTION_NAME_END_CHALLAN,
					nextPosition, remarks);
		}
		
		//on reject, the challan has to go to inbox of the creator
		if(CollectionConstants.WF_ACTION_NAME_REJECT_CHALLAN.equals(actionName)){
			Position createdByPosition=collectionsUtil.getPositionOfUser(
					challan.getCreatedBy());
			challan.setStatus(collectionsUtil.getEgwStatusForModuleAndCode(
					CollectionConstants.MODULE_NAME_CHALLAN, 
					CollectionConstants.CHALLAN_STATUS_CODE_REJECTED));
			//the next action can be modify or cancel challan
			challan.changeState(CollectionConstants.WF_STATE_REJECTED_CHALLAN, 
					createdByPosition, remarks);
		}
		
		if(CollectionConstants.WF_ACTION_NAME_CANCEL_CHALLAN.equals(actionName)){
			challan.setStatus(collectionsUtil.getEgwStatusForModuleAndCode(
					CollectionConstants.MODULE_NAME_CHALLAN, 
					CollectionConstants.CHALLAN_STATUS_CODE_CANCELLED));
			challan.changeState(CollectionConstants.WF_STATE_CANCEL_CHALLAN, 
					CollectionConstants.WF_ACTION_NAME_END_CHALLAN, nextPosition, 
					challan.getReasonForCancellation());
		}
		
		persist(challan);
		
		LOGGER.debug("Challan workflow transition completed. Challan transitioned to : " 
				+ challan.getCurrentState().getValue());
		
		if(CollectionConstants.WF_ACTION_NAME_CANCEL_CHALLAN.equals(actionName) ||
				CollectionConstants.WF_ACTION_NAME_VALIDATE_CHALLAN.equals(actionName)){
			challanWorkflowService.end(challan, collectionsUtil
					.getPositionOfUser(challan.getModifiedBy()), 
					"End of challan worklow");
			LOGGER.debug("End of Challan Workflow.");
		}
	}
}
