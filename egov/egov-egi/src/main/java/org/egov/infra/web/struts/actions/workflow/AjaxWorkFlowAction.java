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

package org.egov.infra.web.struts.actions.workflow;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.matrix.service.CustomizedWorkFlowService;
import org.egov.infstr.services.EISServeable;
import org.egov.pims.commons.Designation;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@ParentPackage("egov")
public class AjaxWorkFlowAction extends BaseFormAction {

	private static final long serialVersionUID = 1L;
	private static final String WF_DESIGNATIONS = "designations";
	private static final String WF_APPROVERS = "approvers";
	private List<Designation> designationList;
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
			this.designationList = this.persistenceService.findAllBy("from Designation");
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

	public List<Designation> getDesignationList() {
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
