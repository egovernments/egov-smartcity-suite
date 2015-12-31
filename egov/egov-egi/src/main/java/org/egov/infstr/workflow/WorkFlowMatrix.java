/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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