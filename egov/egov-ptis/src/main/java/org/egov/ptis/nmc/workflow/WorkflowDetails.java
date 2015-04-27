package org.egov.ptis.nmc.workflow;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_TRANSFER;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_STEP_NOTICE_GENERATED;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_STEP_SAVE;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.WorkflowBean;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.util.PropertyTaxUtil;

/**
 *
 * @author nayeem
 *
 */
public abstract class WorkflowDetails {

	private static final Logger LOGGER = Logger.getLogger(WorkflowDetails.class);

	protected WorkflowActionStep workflowActionStep;

	private PropertyImpl propertyModel;
	private WorkflowBean workflowBean;
	private Integer loggedInUserId;

	public WorkflowDetails() {
	}

	public WorkflowDetails(PropertyImpl propertyModel, WorkflowBean workflowBean,
			Integer loggedInUserId) {
		this.propertyModel = propertyModel;
		this.workflowBean = workflowBean;
		this.loggedInUserId = loggedInUserId;
	}

	/**
	 * Gives the Action name
	 *
	 * @return String representing the action name
	 */
	public abstract String getActionName();

	/**
	 * Gives the next state value
	 *
	 * @return String representing the next state
	 */
	public abstract String getStateValue();

	/**
	 * Sets up the workflow action step, this can be Save or Approve or Forward
	 * or Reject
	 *
	 * @param propertyTaxUtil
	 * @param eisCommonsManager
	 */
	public void setWorkflowActionStep(PropertyTaxUtil propertyTaxUtil,
			EisCommonsService eisCommonsService) {
		LOGGER.debug("Entered into setWorkflowActionStep");

		String beanActionName[] = null;
		String actionStep = null;
		String comments = null;

		if (workflowBean == null) {
			LOGGER.debug("setWorkflowActionStep - workflowBean is NULL");
		} else {
			beanActionName = workflowBean.getActionName().split(":");

			if (beanActionName.length > 1) {
				actionStep = beanActionName[1];
			}

			comments = workflowBean.getComments();
		}

		LOGGER.debug("setWorkflowActionStep - actionStep=" + actionStep);

		Integer nextStateUserId = getNextStateUserId(propertyModel, workflowBean);

		if (nextStateUserId == null) {
			LOGGER.debug("setWorkflowActionStep - nextStateUserId is NULL");
		} else {

			if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(actionStep)) {
				workflowActionStep = new StepApprove(propertyModel, nextStateUserId, comments);
			} else if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(actionStep)) {
				workflowActionStep = new StepSave(propertyModel, nextStateUserId, comments);
			} else if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(actionStep)) {
				workflowActionStep = new StepForward(propertyModel, nextStateUserId, comments);
			} else if (WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(actionStep)) {
				workflowActionStep = new StepReject(propertyModel, nextStateUserId, comments);
			}

			workflowActionStep.setActionName(beanActionName[0] + ":");
			workflowActionStep.setPropertyTaxUtil(propertyTaxUtil);
			workflowActionStep.setEisCommonsService(eisCommonsService);
		}

		LOGGER.debug("Exiting from setWorkflowActionStep");
	}

	private Integer getNextStateUserId(PropertyImpl propertyModel, WorkflowBean workflowBean) {
		LOGGER.debug("Entered into getNextStateUserId, propertyModel=" + propertyModel);

		if (workflowBean == null) {
			LOGGER.debug("getNextStateUserId - workflowBean is NULL");
		} else {
			LOGGER.debug("getNextStateUserId - workflowBean.getActionName="
					+ workflowBean.getActionName());
		}

		String step = workflowBean.getActionName().split(":")[1];
		String action = workflowBean.getActionName().split(":")[0];
		Property oldProperty = PropertyTaxUtil.getLatestProperty(propertyModel.getBasicProperty(),
				PropertyTaxConstants.STATUS_ISHISTORY);

		LOGGER.debug("getNextStateUserId - workflow step=" + step);

		User workflowInitiater = getWorkflowInitiator(propertyModel);

		/*
		 * Integer createdUserId = propertyModel.getCreatedBy() == null ?
		 * ((oldProperty == null || (oldProperty .getRemarks() != null &&
		 * oldProperty.getRemarks().startsWith(STR_MIGRATED))) ? loggedInUserId
		 * : oldProperty.getCreatedBy().getId()) :
		 * propertyModel.getCreatedBy().getId();
		 */

		/*
		 * Integer createdUserId = propertyModel.getCreatedBy() == null ?
		 * (oldProperty.getRemarks() != null && oldProperty
		 * .getRemarks().startsWith(STR_MIGRATED)) ? loggedInUserId :
		 * oldProperty.getCreatedBy().getId() :
		 * propertyModel.getCreatedBy().getId();
		 */

		Integer nextUserId = null;

		if (WFLOW_ACTION_NAME_TRANSFER.equalsIgnoreCase(action)
				&& WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(step)) {
			nextUserId = loggedInUserId;
		} else if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(step)
				|| WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(step)) {
			nextUserId = (workflowInitiater == null ? loggedInUserId : Integer
					.valueOf(workflowInitiater.getId().intValue()));
		} else if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(step)) {
			nextUserId = isApproverUserIdAvail() ? workflowBean.getApproverUserId() : null;
		} else if (WFLOW_ACTION_STEP_SAVE.equalsIgnoreCase(step)) {
			nextUserId = loggedInUserId;
		}

		LOGGER.debug("getNextStateUserId - nextUserId=" + nextUserId);
		LOGGER.debug("Exiting from getNextStateUserId");
		return nextUserId;
	}

	/**
	 * 
	 * User who initiated the workflow
	 * 
	 * @param stateAware
	 * @return User who initiated the workflow
	 */
	public User getWorkflowInitiator(StateAware stateAware) {
		User wfInitiatorUser = null;
		List<StateHistory> states = stateAware.getStateHistory();
		if (states.size() >= 2) {
			for (StateHistory state : states) {
				if (state.getValue().equalsIgnoreCase(NMCPTISConstants.WF_STATE_NEW)) {
					wfInitiatorUser = state.getOwnerUser();
				}
			}
		}
		return wfInitiatorUser;
	}

	/**
	 *
	 *
	 * @return true if the workflow step is SAVE or APPROVE
	 */
	public boolean isApproveOrSave() {
		return WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(this.getWorkflowActionStep()
				.getStepName())
				|| WFLOW_ACTION_STEP_SAVE.equals(this.getWorkflowActionStep().getStepName());
	}

	public boolean isStepRejectAndOwnerNextPositionSame() {
		return this.getWorkflowActionStep().getStepName()
				.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)
				&& propertyModel.getState().getOwnerPosition()
						.equals(this.getWorkflowActionStep().getPosition());
	}

	/**
	 * @return
	 */
	private boolean isApproverUserIdAvail() {
		return workflowBean.getApproverUserId() != null
				&& !workflowBean.getApproverUserId().equals(new Integer(-1));
	}

	public void changeState() {
		workflowActionStep.changeState();
	}

	/**
	 * @return
	 */
	public boolean isNoWorkflow() {
		return propertyModel.getState() == null;
	}

	/**
	 * @return
	 */
	public boolean isNoticeGenerated() {
		return WFLOW_ACTION_STEP_NOTICE_GENERATED.equalsIgnoreCase(workflowBean.getActionName()
				.split(":")[1]);
	}

	public WorkflowActionStep getWorkflowActionStep() {
		return workflowActionStep;
	}

	public void setWorkflowActionStep(WorkflowActionStep workflowActionStep) {
		this.workflowActionStep = workflowActionStep;
	}

	public PropertyImpl getPropertyModel() {
		return propertyModel;
	}

	public void setPropertyModel(PropertyImpl propertyModel) {
		this.propertyModel = propertyModel;
	}

}
