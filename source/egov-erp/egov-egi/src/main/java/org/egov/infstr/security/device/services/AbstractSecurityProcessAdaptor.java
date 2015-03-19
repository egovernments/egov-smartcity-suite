/*
 * @(#)AbstractSecurityProcessAdaptor.java 3.0, 18 Jun, 2013 4:03:38 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.device.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.security.utils.CryptoHelper;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AbstractSecurityProcessAdaptor.
 * This is base class for SecurityProcess, Client specific class needs to sub class this<br/>
 * All global logic to the security process has to be done at this place.
 */
public abstract class AbstractSecurityProcessAdaptor implements SecurityProcessAdaptor {
	
	protected static final Logger LOG = LoggerFactory.getLogger(AbstractSecurityProcessAdaptor.class);
	
	protected UserService userService;
	protected String contentType;
	
	/**
	 * Sets the response content type.
	 * @param contentType the new content type
	 */
	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}
	
	/**
	 * Sets the user manager.
	 * @param userManager the new user manager
	 */
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContentType() {
		return this.contentType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doBeforeFilterChain(final HttpServletRequest request, final HttpServletResponse response) {
	}
	
	/**
	 * Authenticate login request, checks user is valid.
	 * @param userName the user name
	 * @param userPwd the user pwd
	 * @return the user
	 */
	protected User authenticateLogin(final String userName, final String userPwd) {
		User user = null;
		if (userName != null && userPwd != null) {
			user = this.userService.getUserByUserName(userName);
			if (user == null || !userPwd.equals(CryptoHelper.decrypt(user.getPassword())) || user.getIsActive().equals(0) ) {
				user = null;
			} else {
				EGOVThreadLocals.setUserId(String.valueOf(user.getId()));
			}
		}
		return user;
	}	
}
