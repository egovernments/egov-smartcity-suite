package org.egov.ptis.nmc.workflow;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_AMALGAMATE;

import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.WorkflowBean;

public class ActionAmalgmate extends WorkflowDetails {

	public ActionAmalgmate() {}

	public ActionAmalgmate(PropertyImpl propertyModel, WorkflowBean workflowBean, Integer loggedInUserId) {
		super(propertyModel, workflowBean, loggedInUserId);
	}

	@Override
	public String getActionName() {
		return WFLOW_ACTION_NAME_AMALGAMATE;
	}

	@Override
	public String getStateValue() {
		return getActionName();
	}

}
