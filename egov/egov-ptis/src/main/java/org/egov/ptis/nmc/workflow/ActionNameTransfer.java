package org.egov.ptis.nmc.workflow;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_TRANSFER;

import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.WorkflowBean;

public class ActionNameTransfer extends WorkflowDetails {

	public ActionNameTransfer() {}

	public ActionNameTransfer(PropertyImpl propertyModel, WorkflowBean workflowBean, Integer loggedInUserId) {
		super(propertyModel, workflowBean, loggedInUserId);
	}

	@Override
	public String getActionName() {
		return WFLOW_ACTION_NAME_TRANSFER;
	}

	@Override
	public String getStateValue() {
		return getActionName();
	}

	@Override
	public String toString() {
		return "ActionNameTransfer [actionName=" + getActionName() + "]";
	}
}
