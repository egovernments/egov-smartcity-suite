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

package org.egov.infra.workflow.service;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.script.entity.Script;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infra.workflow.entity.WorkflowAction;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This is a generic bean so do not use this to do transition of your own StateAware objects<br/>
 * For your own StateAware object transition declare a separate bean definition like<br/>
 * <p>
 * <pre>
 *
 *       &lt;bean id="myStateAwareWorkflowService" parent="workflowService"&gt;
 *               &lt;constructor-arg index="0" ref="myStateAwarePersistenceService"/&gt;
 *       &lt;/bean&gt;
 * </pre>
 **/
public class SimpleWorkflowService<T extends StateAware> implements WorkflowService<T> {

    private static final String WF_ACTION_ARG = "action";
    private static final String WF_ITEM_ARG = "wfItem";
    private static final String PERSISTENCE_SERVICE_ARG = "persistenceService";
    private static final String CURRENT_DESIGNATION = "currentDesignation";
    private static final String DEPARTMENT = "department";
    private static final String FROM_QTY = "fromQty";
    private static final String TO_QTY = "toQty";
    private static final String ANY = "ANY";
    private final PersistenceService<T, Long> stateAwarePersistenceService;

    @Autowired
    private WorkflowActionService workflowActionService;

    @Autowired
    private ScriptService scriptService;

    public SimpleWorkflowService(PersistenceService<T, Long> stateAwarePersistenceService) {
        this.stateAwarePersistenceService = stateAwarePersistenceService;
    }

    @Override
    public T transition(WorkflowAction workflowAction, T stateAware, String comments) {
        scriptService.executeScript(getScript(stateAware, workflowAction.getName()), ScriptService.createContext(WF_ACTION_ARG, this,
                WF_ITEM_ARG, stateAware, PERSISTENCE_SERVICE_ARG, this.stateAwarePersistenceService, "workflowService", this,
                "comments", comments));
        return this.stateAwarePersistenceService.persist(stateAware);
    }

    @Override
    public T transition(String actionName, T stateAware, String comment) {
        WorkflowAction workflowAction = workflowActionService.getWorkflowActionByNameAndType(actionName,
                stateAware.getStateType());
        if (workflowAction == null)
            workflowAction = new WorkflowAction(actionName, stateAware.getStateType(), actionName);
        return transition(workflowAction, stateAware, comment);
    }

    @Override
    public List<WorkflowAction> getValidActions(T stateAware) {
        String scriptName = stateAware.getStateType() + ".workflow.validactions";
        Script transitionScript = this.scriptService.getByName(scriptName);
        List<String> actionNames = (List<String>) scriptService.executeScript(transitionScript,
                ScriptService.createContext(WF_ITEM_ARG, stateAware, "workflowService", this, PERSISTENCE_SERVICE_ARG,
                        this.stateAwarePersistenceService));
        List<WorkflowAction> savedWorkflowActions = workflowActionService
                .getAllWorkflowActionByTypeAndActionNames(stateAware.getStateType(), actionNames);
        return savedWorkflowActions.isEmpty() ? createActions(stateAware, actionNames) : savedWorkflowActions;
    }

    public Object execute(T stateAware) {
        return scriptService
                .executeScript(getScript(stateAware, EMPTY), ScriptService.createContext(WF_ACTION_ARG, this, WF_ITEM_ARG,
                        stateAware, PERSISTENCE_SERVICE_ARG, this.stateAwarePersistenceService));
    }

    public Object execute(T stateAware, String comments) {
        return scriptService
                .executeScript(getScript(stateAware, EMPTY), ScriptService.createContext(WF_ACTION_ARG, this, WF_ITEM_ARG,
                        stateAware, PERSISTENCE_SERVICE_ARG, this.stateAwarePersistenceService, "comments", comments));
    }

    private Script getScript(T stateAware, String actionName) {
        Script script = null;
        if (isNotBlank(actionName))
            script = this.scriptService.getByName(new StringBuilder(10).append(stateAware.getStateType())
                    .append(".workflow.").append(actionName).toString());
        if (script == null)
            script = scriptService.getByName(stateAware.getStateType() + ".workflow");
        if (script == null)
            throw new ApplicationRuntimeException("workflow.script.notfound");
        return script;
    }

    private List<WorkflowAction> createActions(T stateAware, List<String> actionNames) {
        List<WorkflowAction> workflowActions = new ArrayList<>();
        for (String action : actionNames)
            workflowActions.add(new WorkflowAction(action, stateAware.getStateType(), action));
        return workflowActions;
    }

    @Override
    public WorkFlowMatrix getWfMatrix(String type, String department, BigDecimal amountRule,
                                      String additionalRule, String currentState, String pendingActions) {
        Criteria wfMatrixCriteria = createWfMatrixAdditionalCriteria(type, department, amountRule,
                additionalRule, currentState, pendingActions, null);
        return getWorkflowMatrixObj(type, additionalRule, currentState, pendingActions, null, wfMatrixCriteria);
    }

    @Override
    public WorkFlowMatrix getWfMatrix(String type, String department, BigDecimal amountRule,
                                      String additionalRule, String currentState, String pendingActions, Date date) {
        Criteria wfMatrixCriteria = createWfMatrixAdditionalCriteria(type, department, amountRule,
                additionalRule, currentState, pendingActions, null);
        Criterion fromDateCriteria = Restrictions.le("fromDate", date == null ? new Date() : date);
        Criterion toDateCriteria = Restrictions.ge("toDate", date == null ? new Date() : date);
        Criterion dateCriteria = Restrictions.conjunction().add(fromDateCriteria).add(toDateCriteria);
        wfMatrixCriteria.add(Restrictions.or(dateCriteria, fromDateCriteria));
        return getWorkflowMatrixObj(type, additionalRule, currentState, pendingActions, null, wfMatrixCriteria);
    }

    @Override
    public WorkFlowMatrix getWfMatrix(String type, String department, BigDecimal amountRule,
                                      String additionalRule, String currentState, String pendingActions, Date date,
                                      String designation) {
        Criteria wfMatrixCriteria = createWfMatrixAdditionalCriteria(type, department, amountRule,
                additionalRule, currentState, pendingActions, designation);
        Criterion fromDateCriteria = Restrictions.le("fromDate", date == null ? new Date() : date);
        Criterion toDateCriteria = Restrictions.ge("toDate", date == null ? new Date() : date);
        Criterion dateCriteria = Restrictions.conjunction().add(fromDateCriteria).add(toDateCriteria);
        Criterion criteriaDesignation = Restrictions.ilike(CURRENT_DESIGNATION, isNotBlank(designation) ? designation : EMPTY);
        wfMatrixCriteria.add(criteriaDesignation);
        wfMatrixCriteria.add(Restrictions.or(dateCriteria, fromDateCriteria));
        return getWorkflowMatrixObj(type, additionalRule, currentState, pendingActions, designation, wfMatrixCriteria);
    }

    private WorkFlowMatrix getWorkflowMatrixObj(String type, String additionalRule,
                                                String currentState, String pendingActions,
                                                String designation, Criteria wfMatrixCriteria) {
        List<WorkFlowMatrix> workflowMatrix = wfMatrixCriteria.list();
        if (workflowMatrix.isEmpty()) {
            Criteria defaultCriteria = commonWorkFlowMatrixCriteria(type, additionalRule, currentState, pendingActions);
            defaultCriteria.add(Restrictions.eq(DEPARTMENT, ANY));
            if (isNotBlank(designation))
                defaultCriteria.add(Restrictions.ilike(CURRENT_DESIGNATION, designation));
            List<WorkFlowMatrix> defaultMatrix = defaultCriteria.list();
            return defaultMatrix.isEmpty() ? null : defaultMatrix.get(0);
        } else {
            for (WorkFlowMatrix matrix : workflowMatrix)
                if (matrix.getToDate() == null)
                    return matrix;
            return workflowMatrix.get(0);
        }
    }

    private Criteria createWfMatrixAdditionalCriteria(String type, String department,
                                                      BigDecimal amountRule, String additionalRule, String currentState,
                                                      String pendingActions, String designation) {
        Criteria wfMatrixCriteria = commonWorkFlowMatrixCriteria(type, additionalRule, currentState,
                pendingActions);
        if (isNotBlank(department))
            wfMatrixCriteria.add(Restrictions.eq(DEPARTMENT, department));

        // Added restriction for amount rule
        if (amountRule != null && BigDecimal.ZERO.compareTo(amountRule) != 0) {
            Criterion amount1st = Restrictions.conjunction().add(Restrictions.le(FROM_QTY, amountRule))
                    .add(Restrictions.ge(TO_QTY, amountRule));

            Criterion amount2nd = Restrictions.conjunction().add(Restrictions.le(FROM_QTY, amountRule))
                    .add(Restrictions.isNull(TO_QTY));
            wfMatrixCriteria.add(Restrictions.disjunction().add(amount1st).add(amount2nd));

        }

        if (isNotBlank(designation))
            wfMatrixCriteria.add(Restrictions.ilike(CURRENT_DESIGNATION, designation));

        return wfMatrixCriteria;
    }

    public WorkFlowMatrix getPreviousStateFromWfMatrix(String type, String department, BigDecimal amountRule,
                                                       String additionalRule, String currentState, String pendingActions) {

        Criteria wfMatrixCriteria = previousWorkFlowMatrixCriteria(type, additionalRule, currentState, pendingActions);
        if (department != null && !"".equals(department))
            wfMatrixCriteria.add(Restrictions.eq(DEPARTMENT, department));
        else
            wfMatrixCriteria.add(Restrictions.eq(DEPARTMENT, ANY));

        // Added restriction for amount rule
        if (amountRule != null && BigDecimal.ZERO.compareTo(amountRule) != 0) {
            Criterion amount1st = Restrictions.conjunction()
                    .add(Restrictions.le(FROM_QTY, amountRule))
                    .add(Restrictions.ge(TO_QTY, amountRule));
            Criterion amount2nd = Restrictions.conjunction()
                    .add(Restrictions.le(FROM_QTY, amountRule))
                    .add(Restrictions.isNull(TO_QTY));
            wfMatrixCriteria.add(Restrictions.disjunction().add(amount1st)
                    .add(amount2nd));

        }

        List<WorkFlowMatrix> workflowMatrix = wfMatrixCriteria.list();

        return workflowMatrix.isEmpty() ? null : workflowMatrix.get(0);

    }

    private Criteria previousWorkFlowMatrixCriteria(String type, String additionalRule, String currentState,
                                                    String pendingActions) {
        Criteria commonWfMatrixCriteria = this.stateAwarePersistenceService.getSession()
                .createCriteria(WorkFlowMatrix.class);
        commonWfMatrixCriteria.add(Restrictions.eq("objectType", type));

        if (isNotBlank(additionalRule))
            commonWfMatrixCriteria.add(Restrictions.eq("additionalRule", additionalRule));

        if (isNotBlank(pendingActions))
            commonWfMatrixCriteria.add(Restrictions.ilike("nextAction", pendingActions, MatchMode.EXACT));

        if (isNotBlank(currentState))
            commonWfMatrixCriteria.add(Restrictions.ilike("nextState", currentState, MatchMode.EXACT));
        return commonWfMatrixCriteria;
    }

    private Criteria commonWorkFlowMatrixCriteria(String type, String additionalRule,
                                                  String currentState, String pendingActions) {

        Criteria commonWfMatrixCriteria = this.stateAwarePersistenceService.getSession().createCriteria(
                WorkFlowMatrix.class);

        commonWfMatrixCriteria.add(Restrictions.eq("objectType", type));

        if (isNotBlank(additionalRule))
            commonWfMatrixCriteria.add(Restrictions.eq("additionalRule", additionalRule));

        if (isNotBlank(pendingActions))
            commonWfMatrixCriteria.add(Restrictions.ilike("pendingActions", pendingActions, MatchMode.ANYWHERE));

        if (isNotBlank(currentState))
            commonWfMatrixCriteria.add(Restrictions.ilike("currentState", currentState, MatchMode.EXACT));
        else
            commonWfMatrixCriteria.add(Restrictions.ilike("currentState", "NEW", MatchMode.EXACT));

        return commonWfMatrixCriteria;
    }

}
