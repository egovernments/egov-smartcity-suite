/*
 * @(#)RuleInterceptorFilter.java 3.0, 14 Jun, 2013 5:09:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rrbac.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.AuthorizationException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.models.Script;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.ScriptService;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.lib.rrbac.dao.ActionDAO;
import org.egov.lib.rrbac.model.Action;
import org.egov.lib.rrbac.model.AuthorizationRule;

/**
 * This Filter is used to put rules on actions based on Authentication. 
 * The request parameters expected here are: 
 * 1. object id with parameter name : AUTHRULE_OBJECT_ID 
 * 2. action id with parameter name : actionid
 */
public class RuleInterceptorFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(RuleInterceptorFilter.class);
	private transient PersistenceService daoService;
	private transient PersistenceService<AuthorizationRule, Long> authRuleService;
	private transient UserService userService;
	private transient ActionDAO actionDao;
	private transient ScriptService scriptExecuter;

	@Override
	public void init(final FilterConfig config) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info("In RuleInterceptorFilter init");
		}
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		// from action, for which actions rule should be applied expecting parameter with name
		// AUTHRULE_OBJECT_ID and value is id of the
		// product entity. Eg : In case of PTIS BasicPoperty primary key
		if (request.getParameter("AUTHRULE_OBJECT_ID") != null) {
			final HttpServletRequest httpRequest = (HttpServletRequest) request;
			final Action action = this.getAction(httpRequest);
			final List<AuthorizationRule> authRuleList = this.authRuleService.findAllByNamedQuery("authRulesByAction", action);
			for (final AuthorizationRule authRule : authRuleList) {
				final Object object = this.getEntity(httpRequest, authRule);
				final List authResList = this.getRuleAuthentication(this.getCurrentUser(Long.valueOf(EGOVThreadLocals.getUserId())), authRule, object);
				final boolean authorized = Boolean.valueOf(authResList.get(0).toString());
				if (!authorized) {
					// if authorization fails throwing AuthorizationException
					// setting message key to request, from script when authorization failed
					request.setAttribute("AuthRuleErrMsgKey", authResList.get(1).toString());
					throw new AuthorizationException(authResList.get(1).toString());
				}
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * Gets the rule authentication.
	 * @param currUser the curr user
	 * @param ruleDef the rule def
	 * @param authRule the auth rule
	 * @param object the object
	 * @return the rule authentication
	 */
	private List getRuleAuthentication(final User currUser, final AuthorizationRule authRule, final Object object) {
		return (List) this.scriptExecuter.executeScript(authRule.getScript().getName(), ScriptService.createContext("object", object, "user", currUser));
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info("In RuleInterceptorFilter Destroy");
		}
	}

	/**
	 * Gets the current user.
	 * @param useId the use id
	 * @return the current user
	 */
	public User getCurrentUser(final Long useId) {
		return this.userService.getUserByID(useId);
	}

	/**
	 * Gets the action.
	 * @param request the request
	 * @return the action
	 */
	private Action getAction(final HttpServletRequest request) {
		Action action;
		final String actionId = request.getParameter("actionid");
		if ((actionId == null) || (actionId.length() == 0)) {
			final String contextPath = request.getContextPath();
			final String requestURI = StringUtils.remove(request.getRequestURI(), contextPath);
			action = this.actionDao.findActionByURL(StringUtils.remove(contextPath, '/'), requestURI);
		} else {
			action = (Action) this.actionDao.findById(Integer.getInteger(actionId), false);
		}
		return action;
	}

	/**
	 * Gets the entity.
	 * @param httpRequest the http request
	 * @param authRule the auth rule
	 * @return the entity
	 */
	private Object getEntity(final HttpServletRequest httpRequest, final AuthorizationRule authRule) {
		final Long objectId = Long.valueOf(httpRequest.getParameter("AUTHRULE_OBJECT_ID"));
		final List objects = this.daoService.findAllBy("from " + authRule.getObjectType() + " where id=?", objectId);
		if (objects.isEmpty()) {
			throw new EGOVRuntimeException("Object id is null to get AuthorizationRule");
		}
		return objects.get(0);
	}

	/**
	 * Sets the script service.
	 * @param scriptService the persistence service
	 */
	public void setDaoService(final PersistenceService<Script, Long> daoService) {
		this.daoService = daoService;
	}

	/**
	 * Sets the script executer.
	 * @param scriptExecuter the new script executer
	 */
	public void setScriptExecuter(final ScriptService scriptExecuter) {
		this.scriptExecuter = scriptExecuter;
	}

	/**
	 * Sets the user manager.
	 * @param userManager the new user manager
	 */
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	/**
	 * Sets the action dao.
	 * @param actionDao the new action dao
	 */
	public void setActionDao(final ActionDAO actionDao) {
		this.actionDao = actionDao;
	}

	/**
	 * Sets the auth rule service.
	 * @param authRuleService the auth rule service
	 */
	public void setAuthRuleService(final PersistenceService<AuthorizationRule, Long> authRuleService) {
		this.authRuleService = authRuleService;
	}
}
