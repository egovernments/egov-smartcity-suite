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
package org.egov.ptis.workflow.filter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.Action;
import org.egov.infra.admin.master.service.ActionService;
import org.egov.infra.exception.AuthorizationException;
import org.egov.infra.script.service.ScriptService;
import org.egov.infstr.services.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.script.ScriptContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 This Filter is used to put rules on actions based workflow.
 The request parameters expected here are:
 1. object id with parameter name : ENTITY_ID
 2. action id with parameter name : actionid
 */
public class ActionsBasedOnWorkFlowFilter implements Filter {
	private static final Logger LOGGER = Logger.getLogger(ActionsBasedOnWorkFlowFilter.class);
	@Autowired
	private ScriptService scriptService;
	private PersistenceService workFlowPerService;
	
	@Autowired
	private ActionService actionService;

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Action action = null;
		boolean authorized = true;
		// from action, for which actions restricted for properties under work
		// flow should be applied expecting parameter with name ENTITY_ID and
		// value is bill no of property
		if (request.getParameter("ENTITY_ID") != null) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			List authResList = new ArrayList();
			String billNo = httpRequest.getParameter("ENTITY_ID");
			action = getAction(httpRequest);
			authResList = getWorkFlowActionAuth(action.getUrl(), billNo);
			authorized = Boolean.valueOf(authResList.get(0).toString());
			if (!authorized) { // if authorization fails throwing
								// AuthorizationException
				request.setAttribute("AuthRuleErrMsgKey", authResList.get(1).toString());
				throw new AuthorizationException(authResList.get(1).toString());
			}
		}
		chain.doFilter(request, response);
	}

	private List getWorkFlowActionAuth(String actionUrl, String properrtyId) {
		ScriptContext scriptContext = ScriptService.createContext("ActionName", actionUrl,
				"properrtyId", properrtyId, "persistService", workFlowPerService);
		return (List) scriptService.executeScript("WorkFlowBasedActions", scriptContext);
	}

	@Override
	public void destroy() {
		LOGGER.info("destroying filter");
	}

	private Action getAction(HttpServletRequest request) {
		Action action;
		String actionId = request.getParameter("actionid");
		if (actionId == null || actionId.length() == 0) {
			String requestURI = request.getRequestURI();
			String contextPath = request.getContextPath();
			requestURI = StringUtils.remove(requestURI, contextPath);
			action = actionService.getActionByUrlAndContextRoot(requestURI, StringUtils.remove(contextPath, '/'));
		} else {
			action = actionService.getActionById(Long.valueOf(actionId));
		}
		return action;
	}

	public void setWorkFlowPerService(PersistenceService authRuleService) {
		this.workFlowPerService = authRuleService;
	}

}
