/*
 * @(#)UserValidateForm.java 3.0, 14 Jun, 2013 3:57:59 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.security.terminal.client;

import org.apache.struts.action.ActionForm;

public class UserValidateForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String ipAddress;
	private String locationId;
	private String counterId;
	private String loginType;

	public String getLoginType() {
		return this.loginType;
	}

	public void setLoginType(final String loginType) {
		this.loginType = loginType;
	}

	public String getCounterId() {
		return this.counterId;
	}

	public void setCounterId(final String counterId) {
		this.counterId = counterId;
	}

	public String getLocationId() {
		return this.locationId;
	}

	public void setLocationId(final String locationId) {
		this.locationId = locationId;
	}

	public String getIpAddress() {
		return this.ipAddress;
	}

	public void setIpAddress(final String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

}
