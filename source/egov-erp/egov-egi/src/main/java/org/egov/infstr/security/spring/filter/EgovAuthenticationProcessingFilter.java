/*
 * @(#)EgovAuthenticationProcessingFilter.java 3.0, 18 Jun, 2013 4:13:58 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.spring.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egov.infstr.security.utils.SecurityConstants;
import org.egov.infstr.utils.StringUtils;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class EgovAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter {
	
	private List<String> credentialFields = new ArrayList<String>();
	private UserService userService;

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	public void setCredentialFields(final List<String> credentialFields) {
		this.credentialFields = credentialFields;
	}

	@Override
	protected void setDetails(final HttpServletRequest request, final UsernamePasswordAuthenticationToken authToken) {
		authToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}

	@Override
	protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain, final Authentication authResult) throws IOException, ServletException {
		// Add information to session variables
		final String location = request.getParameter(SecurityConstants.LOCATION_FIELD);
		final String counter = request.getParameter(SecurityConstants.COUNTER_FIELD);
		if (StringUtils.isNotBlank(location)) {
			request.getSession().setAttribute(SecurityConstants.LOCATION_FIELD, location);
		}
		if (StringUtils.isNotBlank(counter)) {
			request.getSession().setAttribute(SecurityConstants.COUNTER_FIELD, counter);
		}
		if (authResult != null) {
			final User principal = (User) authResult.getPrincipal();
			final org.egov.infra.admin.master.entity.User usr = this.userService.getUserByUserName(principal.getUsername());
			request.getSession().setAttribute("com.egov.user.LoginUserId", usr.getId());
		}
		super.successfulAuthentication(request, response, filterChain, authResult);
	}

	@Override
	public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
		final HashMap<String, String> credentials = new HashMap<String, String>();
		for (final String credential : this.credentialFields) {
			final String field = request.getParameter(credential) == null ? "" : request.getParameter(credential);
			credentials.put(credential, field);
		}
		final String username = request.getParameter(SecurityConstants.USERNAME_FIELD);
		final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, credentials);
		request.getSession().setAttribute(SecurityConstants.USERNAME_FIELD, username);
		this.setDetails(request, authToken);

		return this.getAuthenticationManager().authenticate(authToken);
	}

}
