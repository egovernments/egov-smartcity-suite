/*
 * @(#)WorkFlowMatrix.java 3.0, 17 Jun, 2013 4:37:33 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.workflow;

import java.math.BigDecimal;
import java.util.Date;

public class WorkFlowMatrix implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	public WorkFlowMatrix() {

	}

	public WorkFlowMatrix(String department, String objectType, String currentState, String currentStatus, String pendingActions, String currentDesignation, String additionalRule, String nextState, String nextAction, String nextDesignation, String nextStatus,
			String validActions, BigDecimal fromQty, BigDecimal toQty, Date fromDate, Date toDate) {
		super();
		this.department = department;
		this.objectType = objectType;
		this.currentState = currentState;
		this.currentStatus = currentStatus;
		this.pendingActions = pendingActions;
		this.currentDesignation = currentDesignation;
		this.additionalRule = additionalRule;
		this.nextState = nextState;
		this.nextAction = nextAction;
		this.nextDesignation = nextDesignation;
		this.nextStatus = nextStatus;
		this.validActions = validActions;
		this.fromQty = fromQty;
		this.toQty = toQty;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	private Long id;
	private String department;
	private String objectType;
	private String currentState;
	private String currentStatus;
	private String pendingActions;
	private String currentDesignation;
	private String additionalRule;
	private String nextState;
	private String nextAction;
	private String nextDesignation;
	private String nextStatus;
	private String validActions;
	private BigDecimal fromQty;
	private BigDecimal toQty;
	private Date fromDate;
	private Date toDate;

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getCurrentState() {
		return currentState;
	}

	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getPendingActions() {
		return pendingActions;
	}

	public void setPendingActions(String pendingActions) {
		this.pendingActions = pendingActions;
	}

	public String getCurrentDesignation() {
		return currentDesignation;
	}

	public void setCurrentDesignation(String currentDesignation) {
		this.currentDesignation = currentDesignation;
	}

	public String getAdditionalRule() {
		return additionalRule;
	}

	public void setAdditionalRule(String additionalRule) {
		this.additionalRule = additionalRule;
	}

	public String getNextState() {
		return nextState;
	}

	public void setNextState(String nextState) {
		this.nextState = nextState;
	}

	public String getNextAction() {
		return nextAction;
	}

	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}

	public String getNextDesignation() {
		return nextDesignation;
	}

	public void setNextDesignation(String nextDesignation) {
		this.nextDesignation = nextDesignation;
	}

	public String getNextStatus() {
		return nextStatus;
	}

	public void setNextStatus(String nextStatus) {
		this.nextStatus = nextStatus;
	}

	public String getValidActions() {
		return validActions;
	}

	public void setValidActions(String validActions) {
		this.validActions = validActions;
	}

	/*
	 * public WorkFlowAmountRule getAmountRule() { return amountRule; } public void setAmountRule(WorkFlowAmountRule amountRule) { this.amountRule = amountRule; }
	 */
	public BigDecimal getFromQty() {
		return fromQty;
	}

	public void setFromQty(BigDecimal fromQty) {
		this.fromQty = fromQty;
	}

	public BigDecimal getToQty() {
		return toQty;
	}

	public void setToQty(BigDecimal toQty) {
		this.toQty = toQty;
	}

	public WorkFlowMatrix clone() {
		return new WorkFlowMatrix(this.department, this.objectType, this.currentState, this.currentStatus, this.pendingActions, this.currentDesignation, this.additionalRule, this.nextState, this.nextAction, this.nextDesignation, this.nextStatus,
				this.validActions, this.fromQty, this.toQty, this.fromDate, this.toDate);
	}
}