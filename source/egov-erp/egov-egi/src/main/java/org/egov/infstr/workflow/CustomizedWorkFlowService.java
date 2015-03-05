/*
 * @(#)CustomizedWorkFlowService.java 3.0, 17 Jun, 2013 4:29:00 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.workflow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.DesignationMaster;

public class CustomizedWorkFlowService extends PersistenceService<WorkFlowMatrix, Long> {

	private static final String DESGQUERY = "getDesignationForListOfDesgNames";
	private PersistenceService persistenceService;
	private WorkflowService<? extends StateAware> workflowService;

	public List<DesignationMaster> getNextDesignations(final String type, final String department, final BigDecimal businessRule, final String additionalRule, final String currentState, final String pendingAction, final Date date) {

		final WorkFlowMatrix wfMatrix = this.workflowService.getWfMatrix(type, department, businessRule, additionalRule, currentState, pendingAction, date);
		final List<String> designationNames = new ArrayList<String>();
		if (wfMatrix != null && wfMatrix.getNextDesignation() != null) {
			final List<String> tempDesignationName = Arrays.asList(wfMatrix.getNextDesignation().split(","));
			for (final String desgName : tempDesignationName) {
				if (desgName != null && !"".equals(desgName.trim())) {
					designationNames.add(desgName.toUpperCase());
				}
			}
		}
		List<DesignationMaster> designationList = Collections.EMPTY_LIST;
		if (!designationNames.isEmpty()) {
			designationList = this.persistenceService.findAllByNamedQuery(DESGQUERY, designationNames);
		}
		return designationList;
	}

	public List<String> getNextValidActions(final String type, final String departmentName, final BigDecimal businessRule, final String additionalRule, final String currentState, final String pendingAction) {

		final WorkFlowMatrix wfMatrix = this.workflowService.getWfMatrix(type, departmentName, businessRule, additionalRule, currentState, pendingAction);
		List<String> validActions = Collections.EMPTY_LIST;

		if (wfMatrix != null && wfMatrix.getValidActions() != null) {
			validActions = Arrays.asList(wfMatrix.getValidActions().split(","));
		}
		return validActions;
	}

	public List<String> getNextValidActions(final String type, final String departmentName, final BigDecimal businessRule, final String additionalRule, final String currentState, final String pendingAction, final Date date) {

		final WorkFlowMatrix wfMatrix = this.workflowService.getWfMatrix(type, departmentName, businessRule, additionalRule, currentState, pendingAction, date);
		List<String> validActions = Collections.EMPTY_LIST;

		if (wfMatrix != null && wfMatrix.getValidActions() != null) {
			validActions = Arrays.asList(wfMatrix.getValidActions().split(","));
		}
		return validActions;
	}

	public WorkFlowMatrix getWfMatrix(final String type, final String department, final BigDecimal businessRule, final String additionalRule, final String currentState, final String pendingAction, final Date date) {
		return this.workflowService.getWfMatrix(type, department, businessRule, additionalRule, currentState, pendingAction, date);
	}

	public WorkFlowMatrix getWfMatrix(final String type, final String department, final BigDecimal businessRule, final String additionalRule, final String currentState, final String pendingAction) {
		return this.workflowService.getWfMatrix(type, department, businessRule, additionalRule, currentState, pendingAction);
	}

	public void setPersistenceService(final PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public void setWorkflowService(final WorkflowService<StateAware> workflowService) {
		this.workflowService = workflowService;
	}

}
