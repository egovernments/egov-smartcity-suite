/*
 * @(#)WorkFlowAdditionalRule.java 3.0, 17 Jun, 2013 4:36:16 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.workflow;

import org.egov.infstr.models.WorkflowTypes;

public class WorkFlowAdditionalRule {

	private Long id;
	private WorkflowTypes objecttypeid;
	private String additionalRule;
	private String states;
	private String status;
	private String buttons;
	private String workFlowActions;

	public String getWorkFlowActions() {
		return workFlowActions;
	}

	public void setWorkFlowActions(String workFlowActions) {
		this.workFlowActions = workFlowActions;
	}

	public String getButtons() {
		return buttons;
	}

	public void setButtons(String buttons) {
		this.buttons = buttons;
	}

	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public WorkflowTypes getObjecttypeid() {
		return objecttypeid;
	}

	public void setObjecttypeid(WorkflowTypes objecttypeid) {
		this.objecttypeid = objecttypeid;
	}

	public String getAdditionalRule() {
		return additionalRule;
	}

	public void setAdditionalRule(String additionalRule) {
		this.additionalRule = additionalRule;
	}

}
