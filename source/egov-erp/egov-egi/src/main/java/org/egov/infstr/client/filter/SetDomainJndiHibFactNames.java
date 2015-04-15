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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.EGovConfig;
import org.egov.infstr.utils.EgovUtils;

/*
 * This Filter is used to put Session Variables in place which are used by the JSP's for various functions. 
 * These Session variables were earlier put in the LoginManager Bean. 
 * SInce we have started using JAAS, we need to implement a filter to set these variables. 
 * The variables it sets are: 
 * 1. com.egov.user.LoginUserName 
 * 2. com.egov.user.LoginUserId 
 * 3. Various Maps which contain the zone/range/ward accesses for the user. 
 * It can also be used to set session varibale from the web.xml passed in as initilization parameters. 
 * The format for the parameter name should be the following:
 * egov:setVariable:<param_name>
 */

public class SetDomainJndiHibFactNames {

	private static Logger LOG = LoggerFactory.getLogger(SetDomainJndiHibFactNames.class);

	public static void setThreadLocals(final String cityURL, final String jndi, final String hibFactName) {
		EGOVThreadLocals.setDomainName(cityURL);
		EGOVThreadLocals.setJndiName(jndi);
		EGOVThreadLocals.setHibFactName(hibFactName);
	}

	public static void setThreadLocals(final ServletRequest request) {
		try {

			final HttpServletRequest h_request = ((HttpServletRequest) request);
			final HttpSession h_session = h_request.getSession();

			if (request.getParameter("egov.city.url") != null) {
				h_session.setAttribute("egov.city.url", request.getParameter("egov.city.url"));
			} else {
				String urlStr = ((HttpServletRequest) request).getRequestURL().toString();
				String userService = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
				userService = userService + (h_request.getContextPath() == null || h_request.getContextPath().equals("") ? "/" : (h_request.getContextPath() + "/")) + "j_acegi_cas_security_check";
				EGOVThreadLocals.setUserService(userService);
				urlStr = EgovUtils.getDomainName(urlStr);
				h_session.setAttribute("egov.city.url", "http://" + urlStr);
			}

			final String urL = (String) h_session.getAttribute("egov.city.url");
			final String mainUrL = urL.substring(7, urL.length());
			EGOVThreadLocals.setDomainName(mainUrL);

			final String jndiName = EGovConfig.getProperty(mainUrL, "", "JNDIURL");
			if (jndiName != null && !jndiName.equals("")) {
				EGOVThreadLocals.setJndiName(jndiName);
			}

			final String factoryName = EGovConfig.getProperty(mainUrL, "", "HibernateFactory");
			if (factoryName != null && !factoryName.equals("")) {
				EGOVThreadLocals.setHibFactName(factoryName);
			}

		} catch (final Exception e) {
			LOG.error("Error occurred while setting EgovThreadLocal ", e);
			throw new EGOVRuntimeException("Internal Server Error", e);
		}
	}

	public static void clearThreadLocals() {
		EGOVThreadLocals.setHibFactName(null);
		EGOVThreadLocals.setJndiName(null);
		EGOVThreadLocals.setDomainName(null);
		EGOVThreadLocals.setUserId(null);
	}

}
