/*
 * @(#)GenericWorkFlowAction.java 3.0, 14 Jun, 2013 1:18:18 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.workflow;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.search.SearchQuery;
import org.egov.infstr.workflow.CustomizedWorkFlowService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.web.actions.SearchFormAction;

/**
 * Generic WorkFlow Action. Can be extended by any action class that intends to provide 
 * Work flow functionality.
 */
public abstract class GenericWorkFlowAction extends SearchFormAction {

	private static final long serialVersionUID = 1L;
	private final static String FORWARD = "Forward";
	protected CustomizedWorkFlowService customizedWorkFlowService;

	// place holder to Set actionValue that will be used to call workflow script
	protected String workFlowAction;

	// place holder to set approver comments
	protected String approverComments;

	@Override
	public abstract StateAware getModel();

	/**
	 * @inherit doc Implementations must override this method to achieve search functionality with pagination
	 */

	@Override
	public SearchQuery prepareQuery(final String sortField, final String sortOrder) {
		return null;
	}

	@Override
	public void prepare() {
		super.prepare();
		addDropdownData("approverDepartmentList", this.persistenceService.findAllBy("from Department order by deptName"));
		addDropdownData("approverList", Collections.EMPTY_LIST);
		addDropdownData("desgnationList", Collections.EMPTY_LIST);
	}

	/**
	 * Implementations must override this method based on their object's value that needs to be used in workflow
	 * @return the value that needs to be compared in the Amount rule table against FromAmount and ToAmount
	 */

	protected BigDecimal getAmountRule() {
		return null;
	}

	/**
	 * Implementations must override this method to get additional rule for workflow.
	 * @return the value that needs to be compared in the matrix table against Additional rule
	 */

	protected String getAdditionalRule() {
		return null;
	}

	/**
	 * Implementations must override this method to achieve department wise workflow.
	 * @return the value that needs to be compared in the matrix table against Department.
	 */

	protected String getWorkFlowDepartment() {
		return null;
	}

	/**
	 * Used to get valid actions that needs to be performed Based on these value workflow buttons will be rendered
	 */

	public List<String> getValidActions() {
		List<String> validActions = Collections.emptyList();
		if (getModel().getId() == null) {
			validActions = Arrays.asList(FORWARD);

		} else {
			if (getModel().getCurrentState() != null) {
				validActions = this.customizedWorkFlowService.getNextValidActions(getModel().getStateType(), getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(), getModel().getCurrentState().getValue(), getPendingActions(), getModel()
						.getCreatedDate().toDate());
			} else {
			  //FIXME This May not work
				validActions = this.customizedWorkFlowService.getNextValidActions(getModel().getStateType(), getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(), State.DEFAULT_STATE_VALUE_CREATED, getPendingActions(), getModel().getCreatedDate().toDate());
			}
		}
		return validActions;
	}

	/**
	 * Used to get next action If the nextAction value is END then approval Information won't be shown on the UI.
	 */

	public String getNextAction() {
		WorkFlowMatrix wfMatrix = null;
		if (getModel().getId() != null) {
			if (getModel().getCurrentState() != null) {
				wfMatrix = this.customizedWorkFlowService.getWfMatrix(getModel().getStateType(), getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(), getModel().getCurrentState().getValue(), getPendingActions(), getModel().getCreatedDate().toDate());
			} else {
				wfMatrix = this.customizedWorkFlowService.getWfMatrix(getModel().getStateType(), getWorkFlowDepartment(), getAmountRule(), getAdditionalRule(), State.DEFAULT_STATE_VALUE_CREATED, getPendingActions(), getModel().getCreatedDate().toDate());
			}
		}
		return wfMatrix == null ? "" : wfMatrix.getNextAction();
	}

	public void setCustomizedWorkFlowService(final CustomizedWorkFlowService customizedWorkFlowService) {
		this.customizedWorkFlowService = customizedWorkFlowService;
	}

	/**
	 * Used to Set actionValue that will be used to call workflow script.
	 * @param workFlowAction
	 */

	public void setWorkFlowAction(final String workFlowAction) {
		this.workFlowAction = workFlowAction;
	}

	/**
	 * This parameter is used to get matrix object Implementations must override this method to get pendingActions
	 * @return the value needs to be compared against matrix table pendingActions
	 */

	protected String getPendingActions() {
		return null;
	}

	public String getApproverComments() {
		return this.approverComments;
	}

	public void setApproverComments(final String approverComments) {
		this.approverComments = approverComments;
	}
}
