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
package org.egov.infra.workflow.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.script.entity.Script;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.StringUtils;
import org.egov.infstr.workflow.Action;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 *  This is a generic bean so do not use this to do transition of your own StateAware objects<br/>
 *  For your own StateAware object transition declare a separate bean definition
 *   like<br/>
 *   <pre> 
 *       &lt;bean id="myStateAwareWorkflowService" parent="workflowService"&gt;
 *               &lt;constructor-arg index="0" ref="myStateAwarePersistenceService"/&gt;
 *       &lt;/bean&gt;
 *   </pre>
 **/
@SuppressWarnings("unchecked")
public class SimpleWorkflowService<T extends StateAware> implements WorkflowService<T> {

    private final PersistenceService<T, Long> stateAwarePersistenceService;
    private PersistenceService<Action, Long> actionPersistenceService;
    
    @Autowired
    private ScriptService scriptService;

    public SimpleWorkflowService(final PersistenceService<T, Long> stateAwarePersistenceService) {
        this.stateAwarePersistenceService = stateAwarePersistenceService;
    }

    public void setActionPersistenceService(final PersistenceService<Action, Long> actionPersistenceService) {
        this.actionPersistenceService = actionPersistenceService;
    }

    @Override
    public T transition(final Action action, final T stateAware, final String comments) {
        scriptService.executeScript(getScript(stateAware, action.getName()), ScriptService.createContext("action", this,
                "wfItem", stateAware, "persistenceService", this.stateAwarePersistenceService, "workflowService", this,
                "comments", comments));
        return this.stateAwarePersistenceService.persist(stateAware);
    }

    @Override
    public T transition(final String actionName, final T stateAware, final String comment) {
        final List<Action> actions = this.actionPersistenceService.findAllByNamedQuery(Action.BY_NAME_AND_TYPE,
                actionName, stateAware.getStateType());
        Action action = new Action(actionName, stateAware.getStateType(), actionName);
        if (!actions.isEmpty())
            action = actions.get(0);
        return transition(action, stateAware, comment);
    }

    @Override
    public List<Action> getValidActions(final T stateAware) {
        final String scriptName = stateAware.getStateType() + ".workflow.validactions";
        final Script trasitionScript = this.scriptService.getByName(scriptName);
        final List<String> actionNames = (List<String>) scriptService.executeScript(trasitionScript,
                ScriptService.createContext("wfItem", stateAware, "workflowService", this, "persistenceService",
                        this.stateAwarePersistenceService));
        final List<Action> savedActions = this.actionPersistenceService.findAllByNamedQuery(Action.IN_NAMES_AND_TYPE,
                stateAware.getStateType(), actionNames);
        return savedActions.isEmpty() ? createActions(stateAware, actionNames) : savedActions;
    }

    public Object execute(final T stateAware) {
        final Script script = getScript(stateAware, "");
        return scriptService.executeScript(script, ScriptService.createContext("action", this, "wfItem", stateAware,
                "persistenceService", this.stateAwarePersistenceService));
    }

    public Object execute(final T stateAware, final String comments) {
        final Script script = getScript(stateAware, "");
        return scriptService.executeScript(script, ScriptService.createContext("action", this, "wfItem", stateAware,
                "persistenceService", this.stateAwarePersistenceService, "comments", comments));
    }

    private Script getScript(final T stateAware, final String actionName) {
        Script script = null;
        
        if(!actionName.isEmpty())
            script = this.scriptService.getByName(stateAware.getStateType()+ ".workflow." + actionName);
        
        if (script == null)
            script = scriptService.getByName(stateAware.getStateType()+ ".workflow");

        if (script == null)
            throw new ApplicationRuntimeException("workflow.script.notfound");
        
        return script;
    }

    private List<Action> createActions(final T stateAware, final List<String> actionNames) {
        final List<Action> actions = new ArrayList<Action>();
        for (final String action : actionNames)
            actions.add(new Action(action, stateAware.getStateType(), action));
        return actions;
    }

    @Override
    public WorkFlowMatrix getWfMatrix(final String type, final String department, final BigDecimal amountRule,
            final String additionalRule, final String currentState, final String pendingActions) {
        final Criteria wfMatrixCriteria = createWfMatrixAdditionalCriteria(type, department, amountRule,
                additionalRule, currentState, pendingActions);

        return getWorkflowMatrixObj(type, additionalRule, currentState, pendingActions, wfMatrixCriteria);

    }

    @Override
    public WorkFlowMatrix getWfMatrix(final String type, final String department, final BigDecimal amountRule,
            final String additionalRule, final String currentState, final String pendingActions, Date date) {
        final Criteria wfMatrixCriteria = createWfMatrixAdditionalCriteria(type, department, amountRule,
                additionalRule, currentState, pendingActions);
        if (null == date)
            date = new Date();
        final Criterion crit1 = Restrictions.le("fromDate", date);
        final Criterion crit2 = Restrictions.ge("toDate", date);
        final Criterion crit3 = Restrictions.conjunction().add(crit1).add(crit2);
        wfMatrixCriteria.add(Restrictions.or(crit3, crit1));

        return getWorkflowMatrixObj(type, additionalRule, currentState, pendingActions, wfMatrixCriteria);

    }

    private WorkFlowMatrix getWorkflowMatrixObj(final String type, final String additionalRule,
            final String currentState, final String pendingActions, final Criteria wfMatrixCriteria) {
        final List<WorkFlowMatrix> objectTypeList = wfMatrixCriteria.list();

        if (objectTypeList.isEmpty()) {
            final Criteria defaulfWfMatrixCriteria = commonWorkFlowMatrixCriteria(type, additionalRule, currentState,
                    pendingActions);
            defaulfWfMatrixCriteria.add(Restrictions.eq("department", "ANY"));
            final List<WorkFlowMatrix> defaultObjectTypeList = defaulfWfMatrixCriteria.list();
            if (defaultObjectTypeList.isEmpty())
                return null;
            else
                return defaultObjectTypeList.get(0);
        } else {
            if (objectTypeList.size() > 0)
                for (final WorkFlowMatrix matrix : objectTypeList)
                    if (matrix.getToDate() == null)
                        return matrix;
            return objectTypeList.get(0);
        }
    }

    private Criteria createWfMatrixAdditionalCriteria(final String type, final String department,
            final BigDecimal amountRule, final String additionalRule, final String currentState,
            final String pendingActions) {
        final Criteria wfMatrixCriteria = commonWorkFlowMatrixCriteria(type, additionalRule, currentState,
                pendingActions);
        if (department != null && !"".equals(department.trim()))
            wfMatrixCriteria.add(Restrictions.eq("department", department));

        // Added restriction for amount rule
        if (amountRule != null && BigDecimal.ZERO.compareTo(amountRule) != 0) {
            final Criterion amount1st = Restrictions.conjunction().add(Restrictions.le("fromQty", amountRule))
                    .add(Restrictions.ge("toQty", amountRule));

            final Criterion amount2nd = Restrictions.conjunction().add(Restrictions.le("fromQty", amountRule))
                    .add(Restrictions.isNull("toQty"));
            wfMatrixCriteria.add(Restrictions.disjunction().add(amount1st).add(amount2nd));

        }
        return wfMatrixCriteria;
    }
    public WorkFlowMatrix getPreviousStateFromWfMatrix(final String type, final String department, final BigDecimal amountRule,
            final String additionalRule, final String currentState, final String pendingActions) {

    final Criteria wfMatrixCriteria = previousWorkFlowMatrixCriteria(type,additionalRule, currentState, pendingActions);
    if (department!=null && !"".equals(department)) {
            wfMatrixCriteria.add(Restrictions.eq("department", department));
    }else
            wfMatrixCriteria.add(Restrictions.eq("department", "ANY"));

    // Added restriction for amount rule
    if (amountRule != null && BigDecimal.ZERO.compareTo(amountRule) != 0) {
            final Criterion amount1st = Restrictions.conjunction()
                            .add(Restrictions.le("fromQty", amountRule))
                            .add(Restrictions.ge("toQty", amountRule));
            final Criterion amount2nd = Restrictions.conjunction()
                            .add(Restrictions.le("fromQty", amountRule))
                            .add(Restrictions.isNull("toQty"));
            wfMatrixCriteria.add(Restrictions.disjunction().add(amount1st)
                            .add(amount2nd));

    }

    final List<WorkFlowMatrix> objectTypeList = wfMatrixCriteria.list();

    if (!objectTypeList.isEmpty()) {
            return objectTypeList.get(0);
    } 
    
    return null;

}
    private Criteria previousWorkFlowMatrixCriteria(String type,String additionalRule,String currentState,String pendingActions){
        final Criteria commonWfMatrixCriteria=this.stateAwarePersistenceService.getSession().createCriteria(WorkFlowMatrix.class);
        commonWfMatrixCriteria.add(Restrictions.eq("objectType", type));

        if(StringUtils.isNotBlank(additionalRule)){
                commonWfMatrixCriteria.add(Restrictions.eq("additionalRule", additionalRule));
        }
        
        if(StringUtils.isNotBlank(pendingActions)){
                commonWfMatrixCriteria.add(Restrictions.ilike("nextAction", pendingActions,MatchMode.EXACT));
        }

        if(StringUtils.isNotBlank(currentState)){
                commonWfMatrixCriteria.add(Restrictions.ilike("nextState", currentState,MatchMode.EXACT));
        }
        return commonWfMatrixCriteria;
}

    private Criteria commonWorkFlowMatrixCriteria(final String type, final String additionalRule,
            final String currentState, final String pendingActions) {

        final Criteria commonWfMatrixCriteria = this.stateAwarePersistenceService.getSession().createCriteria(
                WorkFlowMatrix.class);

        commonWfMatrixCriteria.add(Restrictions.eq("objectType", type));

        if (additionalRule != null && !"".equals(additionalRule.trim()))
            commonWfMatrixCriteria.add(Restrictions.eq("additionalRule", additionalRule));

        if (pendingActions != null && !"".equals(pendingActions.trim()))
            commonWfMatrixCriteria.add(Restrictions.ilike("pendingActions", pendingActions, MatchMode.ANYWHERE));

        if (currentState != null && !"".equals(currentState.trim()))
            commonWfMatrixCriteria.add(Restrictions.ilike("currentState", currentState, MatchMode.ANYWHERE));
        else
            commonWfMatrixCriteria.add(Restrictions.ilike("currentState", "NEW", MatchMode.ANYWHERE));

        return commonWfMatrixCriteria;
    }
}
