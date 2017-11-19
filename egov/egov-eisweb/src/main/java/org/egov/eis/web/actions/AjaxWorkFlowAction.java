/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.eis.web.actions;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.ResultPath;
import org.apache.struts2.convention.annotation.Results;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.matrix.service.CustomizedWorkFlowService;
import org.egov.pims.commons.Designation;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 *
 * @author subhash
 *
 */
@ParentPackage("egov")
@ResultPath("/WEB-INF/jsp/")
@Results({
        @Result(name = "designations", location = "/WEB-INF/jsp/workflow/ajaxWorkFlow-designations.jsp"),
        @Result(name = "approvers", location = "/WEB-INF/jsp/workflow/ajaxWorkFlow-approvers.jsp")})
public class AjaxWorkFlowAction extends BaseFormAction {

    private static final long serialVersionUID = -4816498948951535977L;
    private static final String WF_DESIGNATIONS = "designations";
    private static final String WF_APPROVERS = "approvers";
    private transient List<Designation> designationList;
    private transient List approverList;
    private Long designationId;
    private Long approverDepartmentId;

    private transient CustomizedWorkFlowService customizedWorkFlowService;
    private String type;
    private BigDecimal amountRule;
    private String additionalRule;
    private String currentState;
    private String pendingAction;
    private String departmentRule;
    private String designation;
    private transient List<String> roleList;

    @Autowired
    private transient AssignmentService assignmentService;

    @Autowired
    private transient DesignationService designationService;

    @Action(value = "/workflow/ajaxWorkFlow-getPositionByPassingDesigId")
    public String getPositionByPassingDesigId() {
        if (designationId != null && designationId != -1) {

            final HashMap<String, String> paramMap = new HashMap<>();
            if (approverDepartmentId != null && approverDepartmentId != -1)
                paramMap.put("departmentId", approverDepartmentId.toString());
            paramMap.put("designationId", designationId.toString());
            approverList = new ArrayList<>();
            final List<Assignment> assignmentList = assignmentService
                    .findAllAssignmentsByDeptDesigAndDates(approverDepartmentId,
                            designationId, new Date());
            for (final Assignment assignment : assignmentList)
                approverList.add(assignment);
        }
        return WF_APPROVERS;
    }

    @Action(value = "/workflow/ajaxWorkFlow-getDesignationsByObjectType")
    public String getDesignationsByObjectType() {
        if ("END".equals(currentState))
            currentState = "";
        if (StringUtils.isNotBlank(designation))
            designationList = designationService.getDesignationsByNames(customizedWorkFlowService.getNextDesignations(type,
                    departmentRule, amountRule, additionalRule, currentState,
                    pendingAction, new Date(), designation));
        else
            designationList = designationService.getDesignationsByNames(customizedWorkFlowService.getNextDesignations(type,
                    departmentRule, amountRule, additionalRule, currentState,
                    pendingAction, new Date()));
        if (designationList.isEmpty())
            designationList = persistenceService.findAllBy("from Designation");
        return WF_DESIGNATIONS;
    }

    public void getAjaxValidButtonsAndNextAction() throws IOException {
        final StringBuilder actionString = new StringBuilder();
        final WorkFlowMatrix matrix = getWfMatrix();
        if (isBlank(currentState)) {

            if (matrix != null && "END".equals(matrix.getNextAction()))
                actionString.append("Save,Approve");
            else
                actionString.append("Save,Forward");
            actionString.append('@');
            if (matrix != null)
                actionString.append(matrix.getNextAction());
            else
                actionString.append(' ');
        } else if (matrix != null) {
            actionString.append(matrix.getValidActions());
            actionString.append('@');
            actionString.append(matrix.getNextAction());
        }
        ServletActionContext.getResponse().getWriter().write(actionString.toString());
    }

    private WorkFlowMatrix getWfMatrix() {
        return customizedWorkFlowService.getWfMatrix(type, departmentRule,
                amountRule, additionalRule, currentState, pendingAction);
    }

    public List<Designation> getDesignationList() {
        return designationList;
    }

    public List getApproverList() {
        return approverList;
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

    public void setRoleList(final List<String> roleList) {
        this.roleList = roleList;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(final String designation) {
        this.designation = designation;
    }

}
