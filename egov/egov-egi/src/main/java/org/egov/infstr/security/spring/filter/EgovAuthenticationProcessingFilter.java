/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infstr.security.spring.filter;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.config.security.authentication.SecureUser;
import org.egov.infra.security.utils.SecurityConstants;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.egov.infra.utils.ApplicationConstant.USERID_KEY;
import static org.egov.infra.utils.ApplicationConstant.USERNAME_KEY;

public class EgovAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter {

    private List<String> credentialFields = new ArrayList<>();

    public void setCredentialFields(List<String> credentialFields) {
        this.credentialFields = credentialFields;
    }

    @Override
    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authToken) {
        authToken.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authResult) throws IOException, ServletException {
        // Add information to session variables
        String location = request.getParameter(SecurityConstants.LOCATION_FIELD);
        HttpSession session = request.getSession();
        if (StringUtils.isNotBlank(location))
            session.setAttribute(SecurityConstants.LOCATION_FIELD, location);

        if (authResult != null) {
            SecureUser principal = (SecureUser) authResult.getPrincipal();
            session.setAttribute(USERID_KEY, principal.getUserId());
            session.setAttribute(USERNAME_KEY, principal.getUsername());
        }
        super.successfulAuthentication(request, response, filterChain, authResult);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        HashMap<String, String> credentials = new HashMap<>();
        for (String credential : credentialFields) {
            String field = request.getParameter(credential) == null ? "" : request.getParameter(credential);
            credentials.put(credential, field);
        }
        String username = request.getParameter(SecurityConstants.USERNAME_FIELD);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, credentials);
        request.getSession().setAttribute(SecurityConstants.USERNAME_FIELD, username);
        setDetails(request, authToken);

        return getAuthenticationManager().authenticate(authToken);
    }

}
