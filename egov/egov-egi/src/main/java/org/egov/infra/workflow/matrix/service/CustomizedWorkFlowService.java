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

package org.egov.infra.workflow.matrix.service;

import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomizedWorkFlowService {

    @Autowired
    @Qualifier("workflowService")
    private WorkflowService<? extends StateAware> workflowService;

    public List<String> getNextDesignationsForActiveAssignments(String type, String department, BigDecimal businessRule,
                                                                String additionalRule, String currentState,
                                                                String pendingAction, Date date) {

        return getDesignationNames(workflowService.getWfMatrix(type, department, businessRule, additionalRule, currentState,
                pendingAction, date));
    }


    public List<String> getNextDesignationsForActiveAssignments(String type, String department, BigDecimal businessRule,
                                                                String additionalRule, String currentState,
                                                                String pendingAction, Date date, String designation) {

        return getDesignationNames(workflowService.getWfMatrix(type, department, businessRule, additionalRule, currentState,
                pendingAction, date, designation));
    }

    public List<String> getNextDesignations(String type, String department, BigDecimal businessRule,
                                            String additionalRule, String currentState, String pendingAction, Date date) {
        return getDesignationNames(workflowService
                .getWfMatrix(type, department, businessRule, additionalRule, currentState, pendingAction, date));
    }

    public List<String> getNextDesignations(String type, String department, BigDecimal businessRule,
                                            String additionalRule, String currentState,
                                            String pendingAction, Date date, String designation) {

        return getDesignationNames(workflowService.getWfMatrix(type, department, businessRule, additionalRule, currentState,
                pendingAction, date, designation));
    }

    public List<String> getNextValidActions(String type, String departmentName, BigDecimal businessRule,
                                            String additionalRule, String currentState, String pendingAction) {

        WorkFlowMatrix wfMatrix = workflowService.getWfMatrix(type, departmentName, businessRule, additionalRule,
                currentState, pendingAction);
        List<String> validActions = Collections.emptyList();
        if (wfMatrix != null && wfMatrix.getValidActions() != null)
            validActions = Arrays.asList(wfMatrix.getValidActions().split(","));
        return validActions;
    }

    public List<String> getNextValidActions(String type, String departmentName, BigDecimal businessRule,
                                            String additionalRule, String currentState, String pendingAction, Date date) {

        WorkFlowMatrix wfMatrix = workflowService.getWfMatrix(type, departmentName, businessRule, additionalRule,
                currentState, pendingAction, date);
        List<String> validActions = Collections.emptyList();

        if (wfMatrix != null && wfMatrix.getValidActions() != null)
            validActions = Arrays.asList(wfMatrix.getValidActions().split(","));
        return validActions;
    }

    public List<String> getNextValidActions(String type, String departmentName, BigDecimal businessRule,
                                            String additionalRule, String currentState, String pendingAction, Date date,
                                            String currentDesignation) {

        WorkFlowMatrix wfMatrix = workflowService.getWfMatrix(type, departmentName, businessRule, additionalRule,
                currentState, pendingAction, date, currentDesignation);
        List<String> validActions = Collections.emptyList();

        if (wfMatrix != null && wfMatrix.getValidActions() != null)
            validActions = Arrays.asList(wfMatrix.getValidActions().split(","));
        return validActions;
    }

    public WorkFlowMatrix getWfMatrix(String type, String department, BigDecimal businessRule,
                                      String additionalRule, String currentState, String pendingAction, Date date) {
        return workflowService.getWfMatrix(type, department, businessRule, additionalRule, currentState, pendingAction, date);
    }

    public WorkFlowMatrix getWfMatrix(String type, String department, BigDecimal businessRule,
                                      String additionalRule, String currentState, String pendingAction, Date date,
                                      String currentDesignation) {
        return workflowService.getWfMatrix(type, department, businessRule, additionalRule, currentState, pendingAction, date,
                currentDesignation);
    }

    public WorkFlowMatrix getWfMatrix(String type, String department, BigDecimal businessRule,
                                      String additionalRule, String currentState, String pendingAction) {
        return workflowService.getWfMatrix(type, department, businessRule, additionalRule, currentState, pendingAction);
    }

    private List<String> getDesignationNames(WorkFlowMatrix wfMatrix) {
        List<String> designationNames = new ArrayList<>();
        if (wfMatrix != null && wfMatrix.getNextDesignation() != null) {
            List<String> tempDesignationName = Arrays.asList(wfMatrix.getNextDesignation().split(","));
            for (String desgName : tempDesignationName)
                if (desgName != null && !"".equals(desgName.trim()))
                    designationNames.add(desgName.toUpperCase());
        }
        return designationNames;
    }

}
