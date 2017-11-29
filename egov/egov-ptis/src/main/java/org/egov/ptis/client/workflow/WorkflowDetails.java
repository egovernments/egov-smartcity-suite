/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.ptis.client.workflow;

import org.apache.log4j.Logger;
import org.egov.eis.service.EisCommonService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.WorkflowBean;

import java.util.List;

import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_TRANSFER;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SAVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_NOTICE_GENERATED;

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
	private Long loggedInUserId;

	public WorkflowDetails() {
	}

	public WorkflowDetails(PropertyImpl propertyModel, WorkflowBean workflowBean,
			Long loggedInUserId) {
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
			EisCommonService eisCommonService) {
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

		Long nextStateUserId = getNextStateUserId(propertyModel, workflowBean);

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
			workflowActionStep.setEisCommonService(eisCommonService);
		}

		LOGGER.debug("Exiting from setWorkflowActionStep");
	}

	private Long getNextStateUserId(PropertyImpl propertyModel, WorkflowBean workflowBean) {
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

		Long nextUserId = null;

		if (WFLOW_ACTION_NAME_TRANSFER.equalsIgnoreCase(action)
				&& WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(step)) {
			nextUserId = loggedInUserId;
		} else if (WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(step)
				|| WFLOW_ACTION_STEP_REJECT.equalsIgnoreCase(step)) {
			nextUserId = (workflowInitiater == null ? loggedInUserId : Integer
					.valueOf(workflowInitiater.getId().intValue()));
		} else if (WFLOW_ACTION_STEP_FORWARD.equalsIgnoreCase(step)) {
			nextUserId = isApproverUserIdAvail() ? workflowBean.getApproverUserId().longValue() : null;
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
				if (state.getValue().equalsIgnoreCase(PropertyTaxConstants.WF_STATE_NEW)) {
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
		return WF_STATE_NOTICE_GENERATED.equalsIgnoreCase(workflowBean.getActionName()
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
