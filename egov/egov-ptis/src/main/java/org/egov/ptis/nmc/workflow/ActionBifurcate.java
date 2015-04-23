package org.egov.ptis.nmc.workflow;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_BIFURCATE;

import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.WorkflowBean;

public class ActionBifurcate extends WorkflowDetails {

	public ActionBifurcate() {}

	public  ActionBifurcate(PropertyImpl propertyModel, WorkflowBean workflowBean, Integer loggedInUserId) {
		super(propertyModel, workflowBean, loggedInUserId);
	}

	@Override
	public String getActionName() {
		return WFLOW_ACTION_NAME_BIFURCATE;
	}

	@Override
	public String getStateValue() {
		return getActionName();
	}

}
