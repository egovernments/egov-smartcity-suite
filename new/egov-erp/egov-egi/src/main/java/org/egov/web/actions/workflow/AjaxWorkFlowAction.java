/*
 * @(#)AjaxWorkFlowAction.java 3.0, 14 Jun, 2013 1:20:30 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.actions.workflow;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.workflow.CustomizedWorkFlowService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.DesignationMaster;
import org.egov.web.actions.BaseFormAction;

@ParentPackage("egov")
public class AjaxWorkFlowAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private static final String WF_DESIGNATIONS = "designations";
	private static final String WF_APPROVERS = "approvers";
	private List<DesignationMaster> designationList;
	private List<Object> approverList;
	private Integer designationId;
	private Integer approverDepartmentId;
	private EISServeable eisService;

	// properties required to get workflow matrix
	private CustomizedWorkFlowService customizedWorkFlowService;
	private String type;
	private BigDecimal amountRule;
	private String additionalRule;
	private String currentState;
	private String pendingAction;
	private String departmentRule;
	private Date date;

	public Date getDate() {
		return this.date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public String getPositionByPassingDesigId() {
		if (this.designationId != null && this.designationId != -1) {

			final HashMap<String, String> paramMap = new HashMap<String, String>();
			if (this.approverDepartmentId != null && this.approverDepartmentId != -1) {
				paramMap.put("departmentId", this.approverDepartmentId.toString());
			}
			paramMap.put("designationId", this.designationId.toString());
			this.approverList = new ArrayList<Object>();
			final List<? extends Object> empList = this.eisService.getEmployeeInfoList(paramMap);
			for (final Object emp : empList) {
				this.approverList.add(emp);
			}
		}
		return WF_APPROVERS;
	}

	public String getDesignationsByObjectType() {
		this.designationList = this.customizedWorkFlowService.getNextDesignations(this.type, this.departmentRule, this.amountRule, this.additionalRule, this.currentState, this.pendingAction, this.date);
		if (this.designationList.isEmpty()) {
			this.designationList = this.persistenceService.findAllBy("from DesignationMaster");
		}
		return WF_DESIGNATIONS;
	}

	/**
	 * For Struts 1.x This method is called to get valid actions(Approve,Reject) and nextaction(END)
	 * @throws IOException
	 */

	public void getAjaxValidButtonsAndNextAction() throws IOException {
		final StringBuffer actionString = new StringBuffer("");
		final WorkFlowMatrix matrix = getWfMatrix();
		if (this.currentState == null || "".equals(this.currentState)) {

			if (matrix != null && "END".equals(matrix.getNextAction())) {
				actionString.append("Save,Approve");
			} else {
				actionString.append("Save,Forward");
			}
			actionString.append('@');
			if (matrix != null) {
				actionString.append(matrix.getNextAction());
			} else {
				actionString.append(' ');
			}
		} else {
			if (matrix != null) {
				actionString.append(matrix.getValidActions());
				actionString.append('@');
				actionString.append(matrix.getNextAction());
			}
		}
		ServletActionContext.getResponse().getWriter().write(actionString.toString());
	}

	private WorkFlowMatrix getWfMatrix() {
		return this.customizedWorkFlowService.getWfMatrix(this.type, this.departmentRule, this.amountRule, this.additionalRule, this.currentState, this.pendingAction, this.date);
	}

	public List<DesignationMaster> getDesignationList() {
		return this.designationList;
	}

	public List<? extends Object> getApproverList() {
		return this.approverList;
	}

	public void setDesignationId(final Integer designationId) {
		this.designationId = designationId;
	}

	public void setEisService(final EISServeable eisService) {
		this.eisService = eisService;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public void setAmountRule(final BigDecimal amountRule) {
		this.amountRule = amountRule;
	}

	public void setAdditionalRule(final String additionalRule) {
		this.additionalRule = additionalRule;
	}

	public void setCurrentState(final String currentState) {
		this.currentState = currentState;
	}

	public void setApproverDepartmentId(final Integer approverDepartmentId) {
		this.approverDepartmentId = approverDepartmentId;
	}

	/*
	 * public void setDesignationRule(String designationRule) { this.designationRule = designationRule; }
	 */
	public void setDepartmentRule(final String departmentRule) {
		this.departmentRule = departmentRule;
	}

	@Override
	public Object getModel() {
		return null;
	}

	public void setCustomizedWorkFlowService(final CustomizedWorkFlowService customizedWorkFlowService) {
		this.customizedWorkFlowService = customizedWorkFlowService;
	}

	public void setPendingAction(final String pendingAction) {
		this.pendingAction = pendingAction;
	}

}
