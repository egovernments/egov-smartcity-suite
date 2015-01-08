/*
 * @(#)SetSessionVarFilter.java 3.0, 18 Jun, 2013 1:12:11 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.filter;

import static org.egov.infstr.utils.EgovUtils.getDomainName;
import static org.egov.infstr.utils.EgovUtils.getPrincipalName;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.CityWebsiteImpl;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

/*
 This Filter is used to put Session Variables in place which are used by the JSP's for various functions.
 These Session variables were earlier put in the LoginManager Bean. SInce we have started using JAAS, we
 need to implement a filter to set these variables.
 The variables it sets are:
 1. com.egov.user.LoginUserName
 2. com.egov.user.LoginUserId
 3. Various Maps which contain the zone/range/ward accesses for the user.

 It can also be used to set session varibale from the web.xml passed in as initilization parameters.
 The format for the parameter name should be the following:
 egov:setVariable:<param_name>

 */

public class SetSessionVarFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SetSessionVarFilter.class);
	private UserService userService;

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public void init(final FilterConfig config) {
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		final HttpSession httpSession = httpRequest.getSession();
		EGOVThreadLocals.setServletContext(httpSession.getServletContext());// setting current ServletContext to the ThreadLocale	

		// check for request parameter for cityID.
		if (httpRequest.getParameter("cityID") != null && httpRequest.getParameter("egov.city.url") != null) {
			httpSession.setAttribute("org.egov.topBndryID", httpRequest.getParameter("cityID"));
			httpSession.setAttribute("egov.city.url", httpRequest.getParameter("egov.city.url"));
			httpSession.setAttribute("topBndryObject", httpRequest.getParameter("topBndryObject"));
		} else if (httpSession.getAttribute("org.egov.topBndryID") == null) {
			final Query query = HibernateUtil.getCurrentSession().getNamedQuery(CityWebsiteImpl.QUERY_CITY_BY_URL);
			query.setString("url", getDomainName(httpRequest.getRequestURL().toString()));
			CityWebsiteImpl city = (CityWebsiteImpl)query.uniqueResult();
			httpSession.setAttribute("egov.city.url", "http://" + city.getCityBaseURL());
			httpSession.setAttribute("org.egov.topBndryID", city.getBoundaryId().getId().toString());
			httpSession.setAttribute("cityurl", city.getCityBaseURL());
			httpSession.setAttribute("cityname", city.getCityName());
			httpSession.setAttribute("citylogo", city.getLogo());
			httpSession.setAttribute("citynamelocal", city.getCityNameLocal());			
		}
		// The following code sets the
		final Principal principal = httpRequest.getUserPrincipal();
		if (principal != null) {
			final String prinName = getPrincipalName(principal.getName());
			final String attr = (String) httpSession.getAttribute("com.egov.user.LoginUserName");
			if (attr == null || prinName != null && !prinName.equalsIgnoreCase(attr)) {
				final HashMap<String, String> credentials = (HashMap<String, String>) SecurityContextHolder.getContext().getAuthentication().getCredentials();
				httpSession.setAttribute("locationId", credentials.get("locationId"));
				httpSession.setAttribute("counterId", credentials.get("counterId"));
				final User user = this.userService.getUserByUserName(prinName);
				Integer userID = null;
				if (user != null) {
					userID = user.getId();
					httpSession.setAttribute("com.egov.user.LoginUserId", userID);
					httpSession.setAttribute("com.egov.user.LoginUserName",user.getFirstName());
				}
			}
			if (httpSession.getAttribute("com.egov.user.LoginUserId") != null) {
				LOGGER.info("Setting User Id to EgovThreadLocal");
				EGOVThreadLocals.setUserId(String.valueOf(httpSession.getAttribute("com.egov.user.LoginUserId")));
			}

		} else {
			EGOVThreadLocals.setUserId(null);
		}
		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
	}

}