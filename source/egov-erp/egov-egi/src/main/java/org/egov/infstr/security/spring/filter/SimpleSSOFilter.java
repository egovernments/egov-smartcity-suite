/*
 * @(#)SimpleSSOFilter.java 3.0, 18 Jun, 2013 3:00:59 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.spring.filter;

import org.egov.infstr.commons.EgLoginLog;
import org.egov.infstr.security.utils.CryptoHelper;
import org.egov.infstr.security.utils.SecurityConstants;
import org.egov.infstr.security.utils.SessionCache;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The filter automatically logs in the user with the credential found in the encrypted 
 * cookie named 'egovegov'. The application using which the user first logs in, 
 * sets a cookie valid for 30 minutes If the current request has a valid cookie, 
 * the application logs the user in automatically.
 */
@Transactional
public class SimpleSSOFilter implements Filter, LogoutHandler {
	private static final String SSO_COOKIE = "egovegov";
	private UserService userService;
	private AuthenticationProvider authenticationProvider;
	private String logoutUrl;
	private SessionCache sessionCache;
	private SessionFactory sessionFactory;
	
	public void setSessionCache(final SessionCache sessionCache) {
		this.sessionCache = sessionCache;
	}

	public void setSessionTimeout(final Integer timeoutInMinutes) {
		SSOPrincipal.setSessionTimeout(timeoutInMinutes);
	}

	private String getPasswordForUser(final String userName) {
		return CryptoHelper.decrypt(this.userService.getUserByUserName(userName).getPwd());
	}

	private Cookie getSSOCookie(final HttpServletRequest request) {
		final Cookie cookies[] = request.getCookies();
		if (cookies == null || cookies.length < 1) {
			return null;
		}
		Cookie ssoCookie = null;
		for (final Cookie cookie : cookies) {
			if (cookie.getName().equals(SSO_COOKIE)) {
				ssoCookie = cookie;
			}
		}
		return ssoCookie;
	}

	private void clearCookies(final HttpServletRequest request, final HttpServletResponse response) {
		final Cookie cookies[] = request.getCookies();
		if (cookies == null || cookies.length < 1) {
			return;
		}
		for (final Cookie cookie : cookies) {
			cookie.setMaxAge(0);
			cookie.setPath("/");
			// cookie.setDomain(getDomainWithoutPort(request.getHeader("host")));
			cookie.setValue(null);
			response.addCookie(cookie);
		}
	}

	@Override
	public void destroy() {
	}

	/**
	 * check if the SSO cookie is available and has not expired
	 */
	@Override
	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest) req;
		final HttpServletResponse httpResponse = (HttpServletResponse) res;
		final HttpSession session = httpRequest.getSession(false);
		final SSOPrincipal ssoPrincipal = this.getSSOPrincipal(httpRequest);
		

		if (this.sessionCache != null) {
			if (!this.sessionCache.isSessionValid(session.getId()) && !this.logoutUrl.equals(httpRequest.getRequestURI())) {
				this.doLogout(httpRequest, httpResponse);
				return;
			}			
		}
		/*
		 * If the sessionId in cookie is not same as user's sessionId, this is not a valid request. Logout. 
		 * This will work only if domains share the same cookie. In case of Tomcat, 
		 * the emptySessionPath is set to true in server.xml (of web deployer)
		 */
		if (session != null && ssoPrincipal != null && (!session.getId().equals(ssoPrincipal.getSessionId()))) {
			this.doLogout(httpRequest, httpResponse);
			return;
		}

		// If the cookie is expired, logout
		if (this.ssoSessionExpired(ssoPrincipal)) {
			this.doLogout(httpRequest, httpResponse);
			return;
		}
		// No principal, but the sso was done some time back. So some other app has logged out the
		// user.
		// logout from this app as well.
		if (ssoPrincipal == null && this.isSSOCompletedForThisSession(session)) {
			this.doLogout(httpRequest, httpResponse);
			return;
		}
		// update the cookie timestamp to current time, the cookie is valid for 30 more minutes
		if (ssoPrincipal != null) {
			this.setPrincipalWithCurrentTimestamp(httpRequest, httpResponse, ssoPrincipal);
		}
		// cookie is present, but not logged on in this app. So log in
		if (ssoPrincipal != null && !this.isAuthenticated()) {
			this.setAuthentication(ssoPrincipal);
			this.setSSSOCompletedForThisSession(httpRequest);
		}
		// no cookie, but user has manually logged in, so set the cookie and mark SSO as completed
		// for this session
		if (ssoPrincipal == null && this.isAuthenticated() && !this.isSSOCompletedForThisSession(session)) {
			this.setPrincipalWithCurrentTimestamp(httpRequest, httpResponse, this.getSSOPrincipalFromAuthentication());
			this.setSSSOCompletedForThisSession(httpRequest);
		}
		chain.doFilter(req, res);
	}

	private void setSSSOCompletedForThisSession(final HttpServletRequest httpRequest) {
		httpRequest.getSession(false).setAttribute(SecurityConstants.SSO_COMPLEATED, true);
	}

	private void setPrincipalWithCurrentTimestamp(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse, final SSOPrincipal principal) {
		httpResponse.addCookie(this.makeCookie(httpRequest, principal));
	}

	private SSOPrincipal getSSOPrincipal(final HttpServletRequest request) {
		final Cookie cookie = this.getSSOCookie(request);
		if (cookie == null) {
			return null;
		}
		return SSOPrincipal.valueOf(cookie.getValue());
	}

	private boolean ssoSessionExpired(final SSOPrincipal principal) {
		return principal != null && principal.hasExpired();
	}

	private void doLogout(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		response.sendRedirect(request.getContextPath() + this.logoutUrl);
	}

	private boolean isSSOCompletedForThisSession(final HttpSession session) {
		return session.getAttribute(SecurityConstants.SSO_COMPLEATED) != null;
	}

	private boolean isAuthenticated() {
		return SecurityContextHolder.getContext().getAuthentication() != null;
	}

	protected SSOPrincipal getSSOPrincipalFromAuthentication() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final SSOPrincipal principal = new SSOPrincipal();
		principal.setUserName(((User) authentication.getPrincipal()).getUsername());
		principal.setTimestamp(System.currentTimeMillis());
		final HashMap<String, String> creds = (HashMap<String, String>) authentication.getCredentials();
		for (final Entry<String, String> cred : creds.entrySet()) {
			if (!cred.getKey().equals(SecurityConstants.PWD_FIELD)) {
				principal.addCredential(cred.getKey(), cred.getValue());
			}
		}
		return principal;
	}

	private Cookie makeCookie(final HttpServletRequest request, final SSOPrincipal principal) {
		String value = null;
		if (principal != null) {
			principal.setTimestamp(System.currentTimeMillis());
			principal.setSessionId(request.getSession(false).getId());
			value = principal.toString();
		}
		if (this.sessionCache != null) {
			this.sessionCache.addSession(request.getSession(false));
		}
		final Cookie ssoCookie = new Cookie(SSO_COOKIE, value);
		ssoCookie.setPath("/");
		ssoCookie.setHttpOnly(true);
		ssoCookie.setMaxAge(-1);
		return ssoCookie;
	}

	public boolean isLocalhostOrIp(final String domain) {
		return domain.toLowerCase().startsWith("localhost") && domain.indexOf(".") < 0 || this.isIP(domain);
	}

	public boolean isIP(final String domain) {
		try {
			Long.valueOf(domain.replace(".", ""));
			return domain.split("\\.").length == 4;
		} catch (final NumberFormatException e) {
			return false;
		}
	}

	public String getDomainWithoutPort(final String domain) {
		String domainExcludingPort = domain;
		if (domain.indexOf(':') > -1) {
			domainExcludingPort = domain.substring(0, domain.indexOf(':'));
		}
		return domainExcludingPort;
	}

	protected void setAuthentication(final SSOPrincipal principal) {
		final String password = this.getPasswordForUser(principal.getUserName());
		final Map<String, String> credentials = principal.getCredentials();
		credentials.put(SecurityConstants.PWD_FIELD, password);
		final HashMap<String, String> creds = new HashMap<String, String>();
		creds.putAll(credentials);
		final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal.getUserName(), creds);
		Authentication authentication;
		try {
			authentication = this.authenticationProvider.authenticate(authToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (final AuthenticationException e) {
			// do nothing.Auth has failed. Filters further in the chain will take care of authn and
			// authx
		}
	}

	@Override
	public void init(final FilterConfig arg0) throws ServletException {
	}

	public void setAuthenticationProvider(final AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	@Override
	public void logout(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) {
		if (request.getSession(false) != null) {
			this.updateLoginLog(authentication);
			if (this.sessionCache != null) {
				this.sessionCache.invalidateSession(request.getSession(false).getId());
			}
			request.getSession(false).invalidate();

		}
		this.clearCookies(request, response);
	}

	private void updateLoginLog(final Authentication authentication) {
		if (authentication != null) {
			final HashMap<String, String> credentials = (HashMap<String, String>) authentication.getCredentials();
			if (credentials.containsKey(SecurityConstants.LOGIN_LOG_ID)) {
				final EgLoginLog loginLog =  (EgLoginLog) getCurrentSession().load(EgLoginLog.class, Integer.valueOf(credentials.get(SecurityConstants.LOGIN_LOG_ID)));
				loginLog.setLogoutTime(new Date());
				getCurrentSession().update(loginLog);
			}
		}
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public void setLogoutUrl(final String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}

