/*
 * @(#)AuthenticationSuccessEventAction.java 3.0, 12 Jul, 2013 6:07:34 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.spring.event.actions;

import java.util.Date;
import java.util.HashMap;

import org.egov.infstr.commons.EgLoginLog;
import org.egov.infstr.security.utils.SecurityConstants;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.StringUtils;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.lib.security.terminal.model.Location;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

import com.opensymphony.xwork2.util.location.LocationImpl;

/**
 * This class will get called when Authentication is successful. 
 * Now this class only Logs the User Login information.
 **/
public class AuthenticationSuccessEventAction implements ApplicationSecurityEventAction<InteractiveAuthenticationSuccessEvent> {
	
	private UserService userService;
	
	@Override
	public void doAction(final InteractiveAuthenticationSuccessEvent authorizedEvent) {
			final Authentication authentication = authorizedEvent.getAuthentication();
			final HashMap<String, String> credentials = (HashMap<String, String>)authentication.getCredentials();
			final EgLoginLog login = new EgLoginLog();
			login.setLoginTime(new Date(authorizedEvent.getTimestamp()));
			final User user = this.userService.getUserByUserName(authentication.getName());
			login.setUser(user);
			if (StringUtils.isNotBlank(credentials.get(SecurityConstants.COUNTER_FIELD))) {
				final Location location = (Location) HibernateUtil.getCurrentSession().load(LocationImpl.class, Integer.valueOf(credentials.get(SecurityConstants.COUNTER_FIELD)));
				login.setLocation(location);
			}
			final String loginLogID = String.valueOf((Integer) HibernateUtil.getCurrentSession().save(login));
			((HashMap<String, String>)authentication.getCredentials()).put(SecurityConstants.LOGIN_LOG_ID, loginLogID);
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
