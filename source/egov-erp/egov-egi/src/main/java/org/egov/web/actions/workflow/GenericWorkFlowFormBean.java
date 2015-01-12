/*
 * @(#)GenericWorkFlowFormBean.java 3.0, 14 Jun, 2013 1:17:57 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.workflow;

import java.math.BigDecimal;

public class GenericWorkFlowFormBean extends org.apache.struts.action.ActionForm {

	private static final long serialVersionUID = 1L;
	protected String currentState;
	protected String currentDesignation;
	protected Integer approverPositionId;
	protected String additionalRule;
	protected BigDecimal amountRule;
	protected String pendingAction;
	protected String actionName;
	protected String approverComments;

	public String getPendingAction() {
		return this.pendingAction;
	}

	public String getActionName() {
		return this.actionName;
	}

	public void setActionName(final String actionName) {
		this.actionName = actionName;
	}

	public void setPendingAction(final String pendingAction) {
		this.pendingAction = pendingAction;
	}

	protected String workFlowDepartment;

	public String getCurrentState() {
		return this.currentState;
	}

	public void setCurrentState(final String currentState) {
		this.currentState = currentState;
	}

	public String getWorkFlowDepartment() {
		return this.workFlowDepartment;
	}

	public String getCurrentDesignation() {
		return this.currentDesignation;
	}

	public void setCurrentDesignation(final String currentDesignation) {
		this.currentDesignation = currentDesignation;
	}

	public String getAdditionalRule() {
		return this.additionalRule;
	}

	public void setAdditionalRule(final String additionalRule) {
		this.additionalRule = additionalRule;
	}

	public BigDecimal getAmountRule() {
		return this.amountRule;
	}

	public void setAmountRule(final BigDecimal amountRule) {
		this.amountRule = amountRule;
	}

	public void setWorkFlowDepartment(final String workFlowDepartment) {
		this.workFlowDepartment = workFlowDepartment;
	}

	public String getApproverComments() {
		return this.approverComments;
	}

	public void setApproverComments(final String approverComments) {
		this.approverComments = approverComments;
	}

	public Integer getApproverPositionId() {
		return this.approverPositionId;
	}

	public void setApproverPositionId(final Integer approverPositionId) {
		this.approverPositionId = approverPositionId;
	}

}
