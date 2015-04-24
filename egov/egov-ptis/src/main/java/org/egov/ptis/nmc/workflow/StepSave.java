package org.egov.ptis.nmc.workflow;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_MODIFY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_STEP_SAVE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WF_STATE_NOTICE_GENERATION_PENDING;

import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.nmc.constants.NMCPTISConstants;

/**
 *
 * @author nayeem
 *
 */
public class StepSave extends WorkflowActionStep {

	public StepSave() {
	}

	public StepSave(PropertyImpl propertyModel, Integer userId, String comments) {
		super(propertyModel, userId, comments);
	}

	@Override
	public String getStepName() {
		return WFLOW_ACTION_STEP_SAVE;
	}

	@Override
	public String getStepValue() {

		StringBuilder stepValue = new StringBuilder();

		stepValue.append(actionName);

		if (isAllChangesCompleted()) {
			stepValue = stepValue.append(WF_STATE_NOTICE_GENERATION_PENDING);
		} else {
			stepValue = stepValue
					.append(propertyTaxUtil.getDesignationName(Long
							.valueOf(userId))).append("_")
					.append(NMCPTISConstants.WF_STATE_APPROVAL_PENDING);
		}

		return stepValue.toString();
	}

	/**
	 * @return true if Data Entry is completed false if not
	 */
	private boolean isAllChangesCompleted() {
		return propertyModel.getBasicProperty().getAllChangesCompleted() == null ? true
				: propertyModel.getBasicProperty().getAllChangesCompleted();
	}

	/**
	 * @return true if workflow action is <code> Create </code> or
	 *         <code> Modify </code>
	 */
	private boolean isActionCreateOrModify() {
		return WFLOW_ACTION_NAME_MODIFY.equalsIgnoreCase(actionName)
				|| NMCPTISConstants.WFLOW_ACTION_NAME_CREATE
						.equalsIgnoreCase(actionName);
	}
}
