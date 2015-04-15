/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infstr.client.filter;

import static org.egov.infstr.utils.EgovUtils.getDomainName;
import static org.egov.infstr.utils.EgovUtils.getPrincipalName;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egov.infra.admin.master.entity.CityWebsite;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityWebsiteService;
import org.egov.infra.admin.master.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private CityWebsiteService cityWebsiteService;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

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
			CityWebsite city = cityWebsiteService.getCityWebSiteByURL(getDomainName(httpRequest.getRequestURL().toString()));
		        //CityWebsite city = cityWebsiteService.getCityWebsiteByCityName("Corporation of Chennai");
			httpSession.setAttribute("egov.city.url", "http://" + city.getCityBaseURL());
			httpSession.setAttribute("org.egov.topBndryID", city.getBoundary().getId().toString());
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
				final User user = this.userService.getUserByUsername(prinName);
				Long userID = null;
				if (user != null) {
					userID = user.getId();
					httpSession.setAttribute("com.egov.user.LoginUserId", userID);
					httpSession.setAttribute("com.egov.user.LoginUserName",user.getName());
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