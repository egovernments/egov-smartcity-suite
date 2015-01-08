/*
 * @(#)UserCounterMapForm.java 3.0, 14 Jun, 2013 3:56:21 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security.terminal.client;

import org.apache.struts.action.ActionForm;

public class UserCounterMapForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private String locationId;
	private String counterName[];
	private String userId[];
	private String userName[];
	private String counter[];
	private String forward;
	private String loginType;
	private String fromDate[];
	private String toDate[];
	private String selCheck[];
	private String userCounterId[];

	/**
	 * @return the counter
	 */
	public String[] getCounter() {
		return this.counter;
	}

	/**
	 * @param counter the counter to set
	 */
	public void setCounter(final String[] counter) {
		this.counter = counter;
	}

	/**
	 * @return the counterName
	 */
	public String[] getCounterName() {
		return this.counterName;
	}

	/**
	 * @param counterName the counterName to set
	 */
	public void setCounterName(final String[] counterName) {
		this.counterName = counterName;
	}

	/**
	 * @return the forward
	 */
	public String getForward() {
		return this.forward;
	}

	/**
	 * @param forward the forward to set
	 */
	public void setForward(final String forward) {
		this.forward = forward;
	}

	/**
	 * @return the fromDate
	 */
	public String[] getFromDate() {
		return this.fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(final String[] fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the locationId
	 */
	public String getLocationId() {
		return this.locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(final String locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the loginType
	 */
	public String getLoginType() {
		return this.loginType;
	}

	/**
	 * @param loginType the loginType to set
	 */
	public void setLoginType(final String loginType) {
		this.loginType = loginType;
	}

	/**
	 * @return the toDate
	 */
	public String[] getToDate() {
		return this.toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(final String[] toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the userId
	 */
	public String[] getUserId() {
		return this.userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(final String[] userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String[] getUserName() {
		return this.userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(final String[] userName) {
		this.userName = userName;
	}

	/**
	 * @return the selCheck
	 */
	public String[] getSelCheck() {
		return this.selCheck;
	}

	/**
	 * @param selCheck the selCheck to set
	 */
	public void setSelCheck(final String[] selCheck) {
		this.selCheck = selCheck;
	}

	/**
	 * @return the userCounterId
	 */
	public String[] getUserCounterId() {
		return this.userCounterId;
	}

	/**
	 * @param userCounterId the userCounterId to set
	 */
	public void setUserCounterId(final String[] userCounterId) {
		this.userCounterId = userCounterId;
	}

}
