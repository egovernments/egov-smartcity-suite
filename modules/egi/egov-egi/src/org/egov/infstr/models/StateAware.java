/*
 * @(#)StateAware.java 3.0, 17 Jun, 2013 2:56:20 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models;

import java.util.Collections;
import java.util.List;

import org.egov.pims.commons.Position;

public abstract class StateAware extends BaseModel {

	private static final long serialVersionUID = 1L;
	private State state;
	protected Integer approverPositionId;

	public Integer getApproverPositionId() {
		return approverPositionId;
	}

	public void setApproverPositionId(Integer approverPositionId) {
		this.approverPositionId = approverPositionId;
	}

	/**
	 * To get the Current State attach to Object
	 * @return State
	 */
	public State getCurrentState() {
		return state;
	}

	/**
	 * Used to Change the State by passing a State itself
	 * @param State nextState
	 */
	public void changeState(State nextState) {
		if (this.state != null) {
			state.setNext(nextState);
			nextState.setPrevious(state);
		}
		state = nextState;
	}

	/**
	 * Used to change the State by giving the State name and the User Position
	 * @param String stateName
	 * @param Position
	 * @param comments
	 */
	public void changeState(String stateName, Position owner, String comments) {
		State state = new State(getStateType(), stateName, owner, comments);
		changeState(state);
	}

	/**
	 * Used to change the State by giving the State name and the User Position
	 * @param String stateName
	 * @param String actionPerformed
	 * @param Position
	 * @param comments
	 */
	public void changeState(String stateName, String nextAction, Position owner, String comments) {
		State state = new State(getStateType(), stateName, nextAction, owner, comments);
		changeState(state);
	}

	/**
	 * To get the History State of this Object
	 * @return List <State> list of States
	 */
	public List<State> getHistory() {
		if (state == null)
			return Collections.emptyList();
		return state.getHistory();
	}

	/**
	 * A State utility method to get the Type name
	 * @return String TypeName
	 */
	public String getStateType() {
		return this.getClass().getSimpleName();
	}

	/**
	 * To find the Current State is in NEW
	 * @return boolean isNew
	 */
	public boolean isNew() {
		return this.getCurrentState() != null && this.getCurrentState().isNew();
	}

	/**
	 * Need to overridden by the implementing class to give details about the State <I>Used by Inbox to fetch the State Detail</I>
	 * @return String Detail
	 */
	public abstract String getStateDetails();

	/**
	 * To get the State
	 */
	public State getState() {
		return state;
	}

	/**
	 * Avoid using setState for State Change Could lead to Object State instability To set the State/ Used by Hibernate
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * To set the Group Link, Any State Aware Object which needs Grouping should override this method
	 **/
	public String myLinkId() {
		return this.getId().toString();
	}

}
