package org.egov.ptis.nmc.workflow;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WF_STATE_APPROVAL_PENDING;

import org.egov.ptis.domain.entity.property.PropertyImpl;

public class StepReject extends WorkflowActionStep {

	public StepReject() {
	}

	public StepReject(PropertyImpl propertyModel, Integer userId,
			String comments) {
		super(propertyModel, userId, comments);
	}

	@Override
	public String getStepName() {
		return WFLOW_ACTION_STEP_REJECT;
	}

	@Override
	public String getStepValue() {
		return actionName
				+ propertyTaxUtil.getDesignationName(Long.valueOf(userId))
				+ "_" + WF_STATE_APPROVAL_PENDING;
	}
}
