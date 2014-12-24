package org.egov.ptis.actions.workflow;

import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_AMALGAMATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_BIFURCATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_CREATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_DEACTIVATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_MODIFY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_TRANSFER;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFOWNER;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WF_STATE_NOTICE_GENERATION_PENDING;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.ptis.actions.common.PropertyTaxBaseAction;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.web.utils.ServletActionRedirectResult;

@ParentPackage("egov")
@Results( {
		@Result(name = "createView", type="redirect", location = "createProperty", params = {
				"namespace", "/create", "method", "view", "modelId", "${workFlowPropId}" }),
		@Result(name = "modifyView", type="redirect", location = "modifyProperty", params = {
				"namespace", "/modify", "method", "view", "modelId", "${workFlowPropId}"  }),
		@Result(name = "deactivateView", type="redirect", location = "deactivateProperty", params = {
				"namespace", "/deactivate", "method", "viewForm", "modelId", "${workFlowPropId}"  }),
		@Result(name = "transferView", type="redirect", location = "transferProperty", params = {
				"namespace", "/transfer", "method", "view", "modelId", "${workFlowPropId}"  }),
		@Result(name = "changePropAddressView", type="redirect", location = "changePropertyAddress", params = {
				"namespace", "/modify", "method", "view", "modelId", "${workFlowPropId}"  }) })
public class WorkflowAction extends PropertyTaxBaseAction {

	private final Logger LOGGER = Logger.getLogger(getClass());
	protected String modelId;
	private String workFlowPropId;
	private PropertyImpl propertyModel;
	private String genWaterRate;
	private String amenity;
	private String wfErrorMsg;
	String actionName = "";

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
					|| (propertyModel.getBasicProperty().getAllChangesCompleted() != null 
					    && propertyModel.getBasicProperty().getAllChangesCompleted())) {
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
		} else if (currState.contains(NMCPTISConstants.WFLOW_ACTION_NAME_CHANGEADDRESS)) {
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
	
}
