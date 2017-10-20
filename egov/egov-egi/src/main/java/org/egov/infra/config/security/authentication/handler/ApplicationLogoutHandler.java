/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
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

package org.egov.infra.config.security.authentication.handler;

import org.egov.infra.security.audit.service.LoginAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.security.utils.SecurityConstants.LOGIN_LOG_ID;
import static org.egov.infra.security.utils.SecurityConstants.SESSION_COOKIE_PATH;

@Component
public class ApplicationLogoutHandler implements LogoutHandler {

    @Autowired
    private LoginAuditService loginAuditService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        auditLogout(authentication);
        clearAllCookies(request, response);
    }

    private void clearAllCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length < 1)
            return;
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setPath(SESSION_COOKIE_PATH);
            cookie.setValue(null);
            response.addCookie(cookie);
        }
    }

    private void auditLogout(Authentication authentication) {
        if (authentication != null) {
            Map<String, String> authCredentials = ((HashMap<String, String>) authentication.getCredentials());
            if (authCredentials != null && isNotBlank(authCredentials.get(LOGIN_LOG_ID))) {
                loginAuditService.auditLogout(Long.valueOf(authCredentials.get(LOGIN_LOG_ID)));
            }
        }
    }

}
