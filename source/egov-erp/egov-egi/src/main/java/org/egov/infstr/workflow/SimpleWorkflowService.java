/*
 * @(#)SimpleWorkflowService.java 3.0, 17 Jun, 2013 4:33:14 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.workflow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.models.Script;
import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * SimpleWorkflowService implements WorkflowService Used for Workflow state transitions, 
 * supports script based & manual Workflow state transitions
 */
public class SimpleWorkflowService<T extends StateAware> implements WorkflowService<T> {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleWorkflowService.class);
	private static final String WF_STARTED = "workflow.already.started";
	private static final String WF_ENDED = "workflow.already.ended";
	private static final String WF_START = "workflow.start";
	private static final String WF_END = "workflow.end";

	private transient final PersistenceService persistenceService;
	private transient ScriptService scriptExecutionService;
	
	/**
	 * Constructor
	 * @param persistence the persistence service
	 * @param type the type
	 */
	public SimpleWorkflowService(final PersistenceService persistence, final Class type) {
		this.persistenceService = persistence;
		this.persistenceService.setType(type);
		LOG.debug("Created " + type + " Workflow Service ");
	}

	/**
	 * Instantiates a new simple workflow service.
	 * @param persistenceService the persistence service
	 */
	public SimpleWorkflowService(final PersistenceService persistence) {
		this.persistenceService = persistence;
		LOG.debug("Created Default Workflow Service with Type Injected Persistence Service");
	}

	public void setScriptExecutionService(ScriptService scriptExecutionService) {
		this.scriptExecutionService = scriptExecutionService;
	}

	/**
	 * Sets the type to persistence service.
	 * @param type the new type
	 */
	@Override
	public void setType(final Class type) {
		this.persistenceService.setType(type);
		LOG.debug("Setting Persistence Service Type to " + type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T start(final StateAware stateAwareItem, final Position owner) {
		return this.start(stateAwareItem, owner, WF_START);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T end(final StateAware stateAwareItem, final Position owner) {
		return this.end(stateAwareItem, owner, WF_END);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T start(final StateAware stateAwareItem, final Position owner, final String comment) {
		if (stateAwareItem.getCurrentState() != null) {
			throw new EGOVRuntimeException(WF_STARTED);
		}
		stateAwareItem.changeState(new State(stateAwareItem.getStateType(), State.NEW, null, owner, comment));
		return (T) this.persistenceService.persist(stateAwareItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T end(final StateAware stateAwareItem, final Position owner, final String comment) {
		if (stateAwareItem.getCurrentState() != null && stateAwareItem.getCurrentState().getValue().equals(State.END)) {
			throw new EGOVRuntimeException(WF_ENDED);
		}
		stateAwareItem.changeState(new State(stateAwareItem.getStateType(), State.END, null, owner, comment));
		return (T) this.persistenceService.persist(stateAwareItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T transition(final StateAware stateAwareItem, final State state) {
		stateAwareItem.changeState(state);
		return (T) this.persistenceService.persist(stateAwareItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T transition(final StateAware stateAwareItem, final Position nextOwner, final String comment) {
		final State nextState = stateAwareItem.getCurrentState().clone();
		nextState.setOwner(nextOwner);
		nextState.setText1(comment);
		stateAwareItem.changeState(nextState);
		return (T) this.persistenceService.persist(stateAwareItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T transition(final StateAware stateAwareItem, final Position nextOwner, final String nextAction, final String comment) {
		final State nextState = stateAwareItem.getCurrentState().clone();
		nextState.setOwner(nextOwner);
		nextState.setNextAction(nextAction);
		nextState.setText1(comment);
		stateAwareItem.changeState(nextState);
		return (T) this.persistenceService.persist(stateAwareItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T transition(final Action action, final StateAware stateAwareItem, final String comment) {
		action.execute(stateAwareItem, this.persistenceService, this, comment);
		return (T) this.persistenceService.persist(stateAwareItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T transition(final String actionName, final StateAware stateAwareItem, final String comment) {
		final List actions = this.persistenceService.findAllByNamedQuery(Action.BY_NAME_AND_TYPE, actionName, stateAwareItem.getStateType());
		Action action = new Action(actionName, stateAwareItem.getStateType(), actionName);
		if (!actions.isEmpty()) {
			action = (Action) actions.get(0);
		}
		return transition(action, stateAwareItem, comment);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Action> getValidActions(final T stateAwareItem) {
		final String scriptName = stateAwareItem.getStateType() + ".workflow.validactions";
		final Script trasitionScript = (Script) this.persistenceService.findAllByNamedQuery(Script.BY_NAME, scriptName).get(0);
		final List<String> actionNames = (List<String>) scriptExecutionService.executeScript(trasitionScript,ScriptService.createContext("wfItem", stateAwareItem, "workflowService", this, "persistenceService", this.persistenceService));
		final List<Action> savedActions = this.persistenceService.findAllByNamedQuery(Action.IN_NAMES_AND_TYPE, stateAwareItem.getStateType(), actionNames);
		return savedActions.isEmpty() ? createActions(stateAwareItem, actionNames) : savedActions;
	}

	private List<Action> createActions(final T stateAwareItem, final List<String> actionNames) {
		final List<Action> actions = new ArrayList<Action>();
		for (final String action : actionNames) {
			actions.add(new Action(action, stateAwareItem.getStateType(), action));
		}
		return actions;
	}

	/**
	 * {@inheritDoc} If no Matrix row exist for the particular department, then it expects the Matrix row with department as ANY otherwise it returns null
	 */

	@Override
	public WorkFlowMatrix getWfMatrix(final String type, final String department, final BigDecimal amountRule, final String additionalRule, final String currentState, final String pendingActions) {
		final Criteria wfMatrixCriteria = createWfMatrixAdditionalCriteria(type, department, amountRule, additionalRule, currentState, pendingActions);

		return getWorkflowMatrixObj(type, additionalRule, currentState, pendingActions, wfMatrixCriteria);

	}

	private WorkFlowMatrix getWorkflowMatrixObj(final String type, final String additionalRule, final String currentState, final String pendingActions, final Criteria wfMatrixCriteria) {
		final List<WorkFlowMatrix> objectTypeList = wfMatrixCriteria.list();

		if (objectTypeList.isEmpty()) {
			final Criteria defaulfWfMatrixCriteria = commonWorkFlowMatrixCriteria(type, additionalRule, currentState, pendingActions);
			defaulfWfMatrixCriteria.add(Restrictions.eq("department", "ANY"));
			final List<WorkFlowMatrix> defaultObjectTypeList = defaulfWfMatrixCriteria.list();
			if (defaultObjectTypeList.isEmpty()) {
				return null;
			} else {
				return defaultObjectTypeList.get(0);
			}
		} else {
			if (objectTypeList.size() > 0) {
				for (final WorkFlowMatrix matrix : objectTypeList) {
					if (matrix.getToDate() == null) {
						return matrix;
					}
				}
			}
			return objectTypeList.get(0);
		}
	}

	private Criteria createWfMatrixAdditionalCriteria(final String type, final String department, final BigDecimal amountRule, final String additionalRule, final String currentState, final String pendingActions) {
		final Criteria wfMatrixCriteria = commonWorkFlowMatrixCriteria(type, additionalRule, currentState, pendingActions);

		if (department != null && !"".equals(department.trim())) {
			wfMatrixCriteria.add(Restrictions.eq("department", department));
		}

		// Added restriction for amount rule
		if (amountRule != null && !BigDecimal.ZERO.equals(amountRule)) {
			final Criterion amount1st = Restrictions.conjunction().add(Restrictions.le("fromQty", amountRule)).add(Restrictions.ge("toQty", amountRule));

			final Criterion amount2nd = Restrictions.conjunction().add(Restrictions.le("fromQty", amountRule)).add(Restrictions.isNull("toQty"));
			wfMatrixCriteria.add(Restrictions.disjunction().add(amount1st).add(amount2nd));

		}
		return wfMatrixCriteria;
	}

	private Criteria commonWorkFlowMatrixCriteria(final String type, final String additionalRule, final String currentState, final String pendingActions) {

		final Criteria commonWfMatrixCriteria = this.persistenceService.getSession().createCriteria(WorkFlowMatrix.class);

		commonWfMatrixCriteria.add(Restrictions.eq("objectType", type));

		if (additionalRule != null && !"".equals(additionalRule.trim())) {
			commonWfMatrixCriteria.add(Restrictions.eq("additionalRule", additionalRule));
		}

		if (pendingActions != null && !"".equals(pendingActions.trim())) {
			commonWfMatrixCriteria.add(Restrictions.ilike("pendingActions", pendingActions, MatchMode.ANYWHERE));
		}

		if (currentState != null && !"".equals(currentState.trim())) {
			commonWfMatrixCriteria.add(Restrictions.ilike("currentState", currentState, MatchMode.ANYWHERE));
		} else {
			commonWfMatrixCriteria.add(Restrictions.ilike("currentState", "NEW", MatchMode.ANYWHERE));
		}

		return commonWfMatrixCriteria;
	}

	@Override
	public WorkFlowMatrix getWfMatrix(final String type, final String department, final BigDecimal amountRule, final String additionalRule, final String currentState, final String pendingActions, Date date) {
		final Criteria wfMatrixCriteria = createWfMatrixAdditionalCriteria(type, department, amountRule, additionalRule, currentState, pendingActions);
		if (null == date) {
			date = new Date();
		}
		final Criterion crit1 = Restrictions.le("fromDate", date);
		final Criterion crit2 = Restrictions.ge("toDate", date);
		final Criterion crit3 = Restrictions.conjunction().add(crit1).add(crit2);
		wfMatrixCriteria.add(Restrictions.or(crit3, crit1));

		return getWorkflowMatrixObj(type, additionalRule, currentState, pendingActions, wfMatrixCriteria);

	}

}
