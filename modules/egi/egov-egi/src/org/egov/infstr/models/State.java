/*
 * @(#)State.java 3.0, 17 Jun, 2013 2:56:03 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.pims.commons.Position;
import org.hibernate.validator.constraints.NotEmpty;

public class State extends BaseModel implements Cloneable {

	private static final long serialVersionUID = 1L;
	public static final String NEW = "NEW";
	public static final String END = "END";
	public static final String WORKFLOWTYPES_QRY = "WORKFLOWTYPES";
	public static final String WORKFLOWTYPES_BY_ID = "WORKFLOWTYPES_BY_ID";
	@NotEmpty
	private String type;
	@NotEmpty
	private String value;
	@NotNull
	private Position owner;

	private String nextAction;
	private State previous;
	private State next;
	private String text1;
	private String text2;
	private Date date1;
	private Date date2;

	State() {
	}

	public State(String type, String value, Position owner, String comment) {
		this.type = type;
		this.value = value;
		this.owner = owner;
		this.text1 = comment;
	}

	public State(String type, String value, String nextAction, Position owner, String comment) {
		this.type = type;
		this.value = value;
		this.owner = owner;
		this.text1 = comment;
		this.nextAction = nextAction;
	}

	public State getPrevious() {
		return previous;
	}

	public void setPrevious(State previous) {
		if (previous != null) {
			verifySelfReference(previous);
			this.previous = previous;
			this.previous.next = this;
		}
	}

	public State getNext() {
		return next;
	}

	public void setNext(State next) {
		if (next != null) {
			verifySelfReference(next);
			this.next = next;
			this.next.previous = this;
		}
	}

	public Position getOwner() {
		return owner;
	}

	public void setOwner(Position owner) {
		this.owner = owner;
	}

	public String getNextAction() {
		return nextAction;
	}

	public void setNextAction(String nextAction) {
		this.nextAction = nextAction;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public String getText2() {
		return text2;
	}

	public void setText2(String text2) {
		this.text2 = text2;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public List<State> getHistory() {
		List<State> history = new ArrayList<State>();
		State state = this;
		do {
			history.add(state);
			state = state.getPrevious();
		} while (state != null);

		return history;
	}

	public boolean isNew() {
		return NEW.equalsIgnoreCase(value);
	}

	public State clone() {
		return new State(this.type, this.value, this.nextAction, this.owner, this.text1);
	}

	public String toString() {
		return "{ State >> Owner : " + owner.getName() + ", Type : " + type + ", Value : " + value + "}";
	}

	private void verifySelfReference(State state) {
		if (this.equals(state)) {
			throw new EGOVRuntimeException("state.self_reference");
		}
	}

}
