/*
 * @(#)WorkflowService.java 3.0, 17 Jun, 2013 4:42:49 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.workflow;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.pims.commons.Position;

/**
 * The Interface WorkflowService.
 * @param <T> the generic type
 */
public interface WorkflowService<T extends StateAware> {

	
	/**
	 * Starts the workflow with 'owner' as the item's owner. The State of the item is 'NEW'
	 * @param item the item
	 * @param owner - Current position of the item
	 * @return the StateAware
	 */
	T start(StateAware item, Position owner);
	
	/**
	 * Ends the workflow with position as the item's position. The State of the item is 'END'
	 * @param item the item
	 * @param owner - Current position of the item
	 * @return the StateAware
	 */
	T end (StateAware item,Position owner);
	
	/**
	 * Starts the workflow with 'position' as the item's position. The State of the item is 'NEW'
	 * @param item the item
	 * @param owner - Current position of the item
	 * @param comment the comment
	 * @return the StateAware
	 */
	T start(StateAware item, Position owner, String comment);
	
	/**
	 * Ends the workflow with 'position' as the item's position. The State of the item is 'END'
	 * @param item the item
	 * @param owner - Current position of the item
	 * @param comment the comment
	 * @return the StateAware
	 */
	T end (StateAware item,Position owner, String comment);
	
	/**
	 * Manually transition the StateAware item from the current state to the new state.
	 * @param item - item to transition
	 * @param state - the new state
	 * @return - the transitioned item *
	 */
	T transition(StateAware item, State state);

	
	/**
	 * Manually change the position, while keeping the state as is.
	 * @param stateAwareItem the state aware item
	 * @param nextOwner - next position
	 * @param comment the comment
	 * @return - the transitioned item
	 */
	T transition(StateAware stateAwareItem, Position nextOwner, String comment);
	
	/**
	 * Manually change the position, while keeping the state as is.
	 * @param stateAwareItem the state aware item
	 * @param nextOwner - next position
	 * @param nextAction the next action
	 * @param comment the comment
	 * @return - the transitioned item
	 */
	T transition(StateAware stateAwareItem, Position nextOwner, String nextAction, String comment);
	
	/**
	 * Moves the item from the current state to the next state. The actual logic of moving the item
	 * depends on the state of the item, and the Action
	 * @param action the action
	 * @param stateAwareItem the state aware item
	 * @param comment the comment
	 * @return item after state transition.
	 */
	T transition(Action action,StateAware stateAwareItem, String comment);
	
	/**
	 * Moves the item from the current state to the next state. The actual logic of moving the item
	 * depends on the state of the item, the Action and the corresponding business rules
	 * encapsulated in a Script. The script name should be '<item's workflowtype>.workflow.<action
	 * name>'. If this script was not found, the implementation will look for a script named '<item's
	 * workflowtype>.workflow'. If this is not found either, then an EgovRuntimeException is thrown
	 * with the message key as 'workflow.script.notfound'
	 * @param actionName the action name
	 * @param stateAwareItem the state aware item
	 * @param comment the comment
	 * @return item after state transition.
	 */
	T transition(String actionName,StateAware stateAwareItem, String comment);
	
	/**
	 * Returns a set of valid actions that can be executed on the item. The actions are determined
	 * using the current state of the item
	 * @param stateAware the state aware
	 * @return List of valid Actions
	 */
	List<Action> getValidActions(T stateAware);	
	
	/**
	 * Dynamically sets the type for PersistenceService used in the WorkflowService
	 **/
	void setType (Class type);
	
	/**
	 * Returns WorkFlowMatrix for following combination of arguments
	 * @param type the Object type of object
	 * @param department Name of the department
	 * @param amountRule the amount
	 * @param additionalRule the additional rule defined for the objecttype
	 * @param currentState  the current state of the object
	 * @param pendingAction the pendingActions for the objecttype 
	 * @return WorkFlowMatrix Object
	 */
	
	WorkFlowMatrix getWfMatrix(String type,String department,BigDecimal amountRule,String additionalRule,String currentState,String pendingAction);

	
	/**
	 * Returns WorkFlowMatrix for following combination of arguments
	 * @param type the Object type of object
	 * @param department Name of the department
	 * @param amountRule the amount
	 * @param additionalRule the additional rule defined for the objecttype
	 * @param currentState  the current state of the object
	 * @param pendingAction the pendingActions for the objecttype 
	 * @return WorkFlowMatrix Object
	 */
	
	WorkFlowMatrix getWfMatrix(String type,String department,BigDecimal amountRule,String additionalRule,String currentState,String pendingAction,Date date);
}