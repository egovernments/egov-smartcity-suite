/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.lib.rrbac.filter;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.admin.master.entity.Action;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.ActionService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.exception.AuthorizationException;
import org.egov.infra.script.entity.Script;
import org.egov.infra.script.service.ScriptService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.rrbac.model.AuthorizationRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * This Filter is used to put rules on actions based on Authentication. 
 * The request parameters expected here are: 
 * 1. object id with parameter name : AUTHRULE_OBJECT_ID 
 * 2. action id with parameter name : actionid
 */
public class RuleInterceptorFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(RuleInterceptorFilter.class);

	private PersistenceService daoService;
	private PersistenceService<AuthorizationRule, Long> authRuleService;
	@Autowired
	private ActionService actionService;
	@Autowired
	private ScriptService scriptExecuter;
    @Autowired
    private SecurityUtils securityUtils;

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
				final List authResList = this.getRuleAuthentication(securityUtils.getCurrentUser(), authRule, object);
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
			action = this.actionService.getActionByUrlAndContextRoot(requestURI, StringUtils.remove(contextPath, '/'));
		} else {
			action = (Action) this.actionService.getActionById(Long.valueOf(actionId));
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
			throw new ApplicationRuntimeException("Object id is null to get AuthorizationRule");
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
	 * Sets the auth rule service.
	 * @param authRuleService the auth rule service
	 */
	public void setAuthRuleService(final PersistenceService<AuthorizationRule, Long> authRuleService) {
		this.authRuleService = authRuleService;
	}
}
