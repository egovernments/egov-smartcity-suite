package org.egov.ptis.nmc.workflow;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_CREATE;

import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.WorkflowBean;

public class ActionCreate  extends WorkflowDetails {

	public ActionCreate() {}

	public ActionCreate(PropertyImpl propertyModel, WorkflowBean workflowBean, Integer loggedInUserId) {
		super(propertyModel, workflowBean, loggedInUserId);
	}

	@Override
	public String getActionName() {
		return WFLOW_ACTION_NAME_CREATE;
	}

	@Override
	public String getStateValue() {
		return getActionName();
	}

	@Override
	public String toString() {
		return "ActionCreate [actionName=" + getActionName() + "]";
	}
}
