/* eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.infra.web.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.security.authentication.SecureUser;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.infra.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

@SuppressWarnings("all")
public class SessionAttributeHandlerFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionAttributeHandlerFilter.class);

    @Autowired
    private SecurityUtils securityUtils;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CityService cityService;

    @Override
    public void init(final FilterConfig config) {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpSession httpSession = httpRequest.getSession();

        if (httpSession.getAttribute("cityBoundaryId") == null) {
            final City city = cityService.getCityByURL(WebUtils.extractRequestedDomainName(httpRequest));
            httpSession.setAttribute("cityBoundaryId", city.getBoundary().getId().toString());
            httpSession.setAttribute("cityurl", city.getDomainURL());
            httpSession.setAttribute("cityname", city.getName());
            httpSession.setAttribute("citylogo", city.getLogo());
            httpSession.setAttribute("citynamelocal", city.getLocalName());
            httpSession.setAttribute("cityCode", city.getCode());
            httpSession.setAttribute("cityRecaptchaPK", city.getRecaptchaPK());
        }

        final Principal principal = httpRequest.getUserPrincipal();
        if (principal != null) {
            final String prinName = principal.getName();
            final String attr = (String) httpSession.getAttribute("username");
            if (attr == null || prinName != null && !prinName.equalsIgnoreCase(attr)) {
                final Authentication authentication = securityUtils.getCurrentAuthentication().get();
                final HashMap<String, String> credentials = (HashMap<String, String>) authentication.getCredentials();
                httpSession.setAttribute("locationId", credentials.get("locationId"));
                httpSession.setAttribute("counterId", credentials.get("counterId"));
                httpSession.setAttribute("userid", ((SecureUser) authentication.getPrincipal()).getUserId());
                httpSession.setAttribute("username", authentication.getName());
            }
            if (httpSession.getAttribute("userid") != null) {
                LOGGER.info("Setting User Id to EgovThreadLocal");
                EgovThreadLocals.setUserId((Long) httpSession.getAttribute("userid"));
            }

        } else
            EgovThreadLocals.setUserId(null);

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
    }

}