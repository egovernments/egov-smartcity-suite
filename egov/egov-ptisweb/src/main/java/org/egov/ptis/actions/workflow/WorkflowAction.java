/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.ptis.actions.workflow;

import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_AMALGAMATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_BIFURCATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_CREATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_DEACTIVATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_MODIFY;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_TRANSFER;
import static org.egov.ptis.constants.PropertyTaxConstants.WFOWNER;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_NOTICE_GENERATION_PENDING;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.infra.workflow.service.WorkflowService;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.pims.commons.Position;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.client.workflow.WorkflowDetails;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.web.utils.ServletActionRedirectResult;

@ParentPackage("egov")

public class WorkflowAction extends PropertyTaxBaseAction {

	private final Logger LOGGER = Logger.getLogger(getClass());
	protected String modelId;
	private String workFlowPropId;
	private PropertyImpl propertyModel;
	private String genWaterRate;
	private String amenity;
	private String wfErrorMsg;
	String actionName = "";
	protected WorkflowDetails workflowAction;
	private WorkflowService<PropertyImpl> propertyWorkflowService;

	@SkipValidation
	@Override
	public Object getModel() {
		return propertyModel;
	}

	@SkipValidation
	public void prepare() {
		LOGGER.debug("prepare : Entered into method prepare");
		LOGGER.debug("prepare : Workflow Property id : " + workFlowPropId);
		setupWorkflowDetails();
		if (workFlowPropId != null && !workFlowPropId.isEmpty()) {
			propertyModel = (PropertyImpl) getPersistenceService().findByNamedQuery(QUERY_PROPERTYIMPL_BYID,
					Long.valueOf(workFlowPropId));
		}
		LOGGER.debug("prepare : Property in Workflow : " + propertyModel);
		LOGGER.debug("prepare : Exit from method prepare");
	}

	@SkipValidation
	public String viewProperty() {
		LOGGER.debug("Entered into method viewProperty");
		LOGGER.debug("viewProperty : Property : " + propertyModel);
		String target = "";
		String currState = propertyModel.getState().getValue();
		
		if (currState.contains(WFLOW_ACTION_NAME_CREATE)) {
			target = "createView";
		} else if (currState.contains(WFLOW_ACTION_NAME_MODIFY)) {
			if (currState.contains(WF_STATE_NOTICE_GENERATION_PENDING)
					|| (propertyModel.getBasicProperty().getAllChangesCompleted() != null && propertyModel
							.getBasicProperty().getAllChangesCompleted())) {
				target = "modifyView";
			} else {
				target = "modifyForm";
			}
		} else if (currState.contains(WFLOW_ACTION_NAME_BIFURCATE) || currState.contains(WFLOW_ACTION_NAME_AMALGAMATE)) {
			target = "modifyView";
		} else if (currState.contains(WFLOW_ACTION_NAME_DEACTIVATE)) {
			target = "deactivateView";
		} else if (currState.contains(WFLOW_ACTION_NAME_TRANSFER)) {
			target = "transferView";
		} else if (currState.contains(PropertyTaxConstants.WFLOW_ACTION_NAME_CHANGEADDRESS)) {
			target = "changePropAddressView";
		}
		LOGGER.debug("Exit from method viewProperty with return value : " + target);
		return target;
	}

	public String workFlowError() {
		LOGGER.debug("Entered into method WorkFlowError");
		getSession().get(WFOWNER);
		setWfErrorMsg("This Property Under Work flow in " + getSession().get(WFOWNER)
				+ "'s inbox. Please finish pending work flow before do any transactions on it.");
		LOGGER.debug("workFlowError : WorkFlow Error message : " + getWfErrorMsg());
		LOGGER.debug("Exit from methd WorkFlowError");
		return "error";
	}

	
	public String inboxItemViewErrorUserInvalid() {
		LOGGER.debug("Entered into method inboxItemViewErrorUserInvalid");
		getSession().get(WFOWNER);
		setWfErrorMsg("The User is not authenticated to view the inbox item " );
		LOGGER.debug("WorkFlow error message : " + getWfErrorMsg());
		LOGGER.debug("Exit from method inboxItemViewErrorUserInvalid");
		return "error";
	}
	
	public void startWorkFlow() {
		LOGGER.debug("Entered into startWorkFlow, UserId: " + EGOVThreadLocals.getUserId());
		LOGGER.debug("startWorkFlow: Workflow is starting for Property: " + workflowAction.getPropertyModel());
		//Position position = eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		Position position = null;
		//propertyWorkflowService.start(workflowAction.getPropertyModel(), position, "Property Workflow Started");
		workflowAction.getPropertyModel().transition(true).start().withOwner(position).withComments("Property Workflow Started");
		
		LOGGER.debug("Exiting from startWorkFlow, Workflow started");
	}

	/**
	 * This method ends the workflow. The Property is transitioned to END state.
	 */
	public void endWorkFlow() {
		LOGGER.debug("Enter method endWorkFlow, UserId: " + EGOVThreadLocals.getUserId());
		LOGGER.debug("endWorkFlow: Workflow will end for Property: " + workflowAction.getPropertyModel());
		//Position position = eisCommonsManager.getPositionByUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
		Position position = null;
		//propertyWorkflowService.end(workflowAction.getPropertyModel(), position, "Property Workflow End");
		workflowAction.getPropertyModel().transition(true).end().withOwner(position).withComments("Property Workflow End");
		LOGGER.debug("Exit method endWorkFlow, Workflow ended");
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public PropertyImpl getProperty() {
		return propertyModel;
	}

	public void setProperty(PropertyImpl property) {
		this.propertyModel = property;
	}

	public String getGenWaterRate() {
		return genWaterRate;
	}

	public void setGenWaterRate(String genWaterRate) {
		this.genWaterRate = genWaterRate;
	}

	public String getAmenity() {
		return amenity;
	}

	public void setAmenity(String amenity) {
		this.amenity = amenity;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getWfErrorMsg() {
		return wfErrorMsg;
	}

	public void setWfErrorMsg(String wfErrorMsg) {
		this.wfErrorMsg = wfErrorMsg;
	}

	public String getWorkFlowPropId() {
		return workFlowPropId;
	}

	public void setWorkFlowPropId(String workFlowPropId) {
		this.workFlowPropId = workFlowPropId;
	}

	public WorkflowService<PropertyImpl> getPropertyWorkflowService() {
		return propertyWorkflowService;
	}

	public void setPropertyWorkflowService(WorkflowService<PropertyImpl> propertyWorkflowService) {
		this.propertyWorkflowService = propertyWorkflowService;
	}
}
