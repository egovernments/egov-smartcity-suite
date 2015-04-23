package org.egov.ptis.nmc.workflow;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WF_STATE_NOTICE_GENERATION_PENDING;

import org.egov.ptis.domain.entity.property.PropertyImpl;

public class StepApprove extends WorkflowActionStep {


	public StepApprove() {}

	public StepApprove(PropertyImpl propertyModel, Integer userId, String comments) {
		super(propertyModel, userId, comments);
	}

	@Override
	public String getStepName() {
		return WFLOW_ACTION_STEP_APPROVE;
	}

	@Override
	public String getStepValue() {
		return actionName + WF_STATE_NOTICE_GENERATION_PENDING;
	}
}
