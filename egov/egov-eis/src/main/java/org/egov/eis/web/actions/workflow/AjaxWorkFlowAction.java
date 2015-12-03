package org.egov.eis.web.actions.workflow;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infstr.workflow.CustomizedWorkFlowService;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.pims.commons.Designation;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author subhash
 *
 */
@ParentPackage("egov")
@SuppressWarnings("serial")
@ResultPath("/WEB-INF/jsp/")
@Results({
		@Result(name = "designations", location = "/WEB-INF/jsp/workflow/ajaxWorkFlow-designations.jsp"),
		@Result(name = "approvers", location = "/WEB-INF/jsp/workflow/ajaxWorkFlow-approvers.jsp") })
public class AjaxWorkFlowAction extends BaseFormAction {

	private static final String WF_DESIGNATIONS = "designations";
	private static final String WF_APPROVERS = "approvers";
	private List<Designation> designationList;
	private List<Object> approverList;
	private Long designationId;
	private Long approverDepartmentId;

	private CustomizedWorkFlowService customizedWorkFlowService;
	private String type;
	private BigDecimal amountRule;
	private String additionalRule;
	private String currentState;
	private String pendingAction;
	// private String designationRule;
	private String departmentRule;
	private List<String> roleList;
	@Autowired
	private AssignmentService assignmentService;

	@Action(value = "/workflow/ajaxWorkFlow-getPositionByPassingDesigId")
	public String getPositionByPassingDesigId() {
		if (this.designationId != null && this.designationId != -1) {

			final HashMap<String, String> paramMap = new HashMap<String, String>();
			if (this.approverDepartmentId != null && this.approverDepartmentId != -1) {
				paramMap.put("departmentId", this.approverDepartmentId.toString());
			}
			paramMap.put("designationId", this.designationId.toString());
			this.approverList = new ArrayList<Object>();
			List<Assignment> assignmentList = assignmentService
					.findAllAssignmentsByDeptDesigAndDates(this.approverDepartmentId,
							this.designationId, new Date());
			for (final Assignment assignment : assignmentList) {
				this.approverList.add(assignment);
			}
		}
		return WF_APPROVERS;
	}

	@SuppressWarnings("unchecked")
	@Action(value = "/workflow/ajaxWorkFlow-getDesignationsByObjectType")
	public String getDesignationsByObjectType() {
		if("END".equals(this.currentState))
				this.currentState="";
		this.designationList = this.customizedWorkFlowService.getNextDesignations(this.type,
				this.departmentRule, this.amountRule, this.additionalRule, this.currentState,
				this.pendingAction, new Date());
		if (this.designationList.isEmpty()) {
			this.designationList = this.persistenceService.findAllBy("from Designation");
		}
		return WF_DESIGNATIONS;
	}

	/**
	 * For Struts 1.x This method is called to get valid actions(Approve,Reject)
	 * and nextaction(END)
	 * 
	 * @throws IOException
	 */

	public void getAjaxValidButtonsAndNextAction() throws IOException {
		final StringBuffer actionString = new StringBuffer("");
		final WorkFlowMatrix matrix = this.getWfMatrix();
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
		return this.customizedWorkFlowService.getWfMatrix(this.type, this.departmentRule,
				this.amountRule, this.additionalRule, this.currentState, this.pendingAction);
	}

	public List<Designation> getDesignationList() {
		return this.designationList;
	}

	public List<? extends Object> getApproverList() {
		return this.approverList;
	}

	public void setDesignationId(final Long designationId) {
		this.designationId = designationId;
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

	public void setApproverDepartmentId(final Long approverDepartmentId) {
		this.approverDepartmentId = approverDepartmentId;
	}

	/*
	 * public void setDesignationRule(String designationRule) {
	 * this.designationRule = designationRule; }
	 */
	public void setDepartmentRule(final String departmentRule) {
		this.departmentRule = departmentRule;
	}

	@Override
	public Object getModel() {
		return null;
	}

	public void setCustomizedWorkFlowService(
			final CustomizedWorkFlowService customizedWorkFlowService) {
		this.customizedWorkFlowService = customizedWorkFlowService;
	}

	public void setPendingAction(final String pendingAction) {
		this.pendingAction = pendingAction;
	}

	public List<String> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

}
