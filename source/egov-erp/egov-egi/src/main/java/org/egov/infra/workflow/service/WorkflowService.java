/*
 * @(#)WorkflowService.java 3.0, 17 Jun, 2013 4:42:49 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infra.workflow.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.workflow.Action;
import org.egov.infstr.workflow.WorkFlowMatrix;

/**
 * The Interface WorkflowService.
 * @param <T> the generic type
 */
public interface WorkflowService<T extends StateAware> {

	/**
	 * Moves the stateAware from the current state to the next state. The actual logic of moving the stateAware
	 * depends on the state of the stateAware, and the Action
	 * @param action the action
	 * @param stateAware the state aware stateAware
	 * @param comment the comment
	 * @return stateAware after state transition.
	 */
	T transition(Action action,T stateAware, String comment);
	
	/**
	 * Moves the stateAware from the current state to the next state. The actual logic of moving the stateAware
	 * depends on the state of the stateAware, the Action and the corresponding business rules
	 * encapsulated in a Script. The script name should be '<stateAware's workflowtype>.workflow.<action
	 * name>'. If this script was not found, the implementation will look for a script named '<stateAware's
	 * workflowtype>.workflow'. If this is not found either, then an EgovRuntimeException is thrown
	 * with the message key as 'workflow.script.notfound'
	 * @param actionName the action name
	 * @param stateAware the state aware stateAware
	 * @param comment the comment
	 * @return stateAware after state transition.
	 */
	T transition(String actionName,T stateAware, String comment);
	
	/**
	 * Returns a set of valid actions that can be executed on the stateAware. The actions are determined
	 * using the current state of the stateAware
	 * @param stateAware the state aware
	 * @return List of valid Actions
	 */
	List<Action> getValidActions(T stateAware);	
	
		
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