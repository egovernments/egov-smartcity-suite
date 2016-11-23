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
import org.egov.infra.security.audit.entity.SystemAudit;
import org.egov.infra.security.audit.service.SystemAuditService;
import org.egov.infra.security.utils.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    private SystemAuditService systemAuditService;

    @Override
    public void logout(final HttpServletRequest request, final HttpServletResponse response,
            final Authentication authentication) {
        clearAllCookies(request, response);
        auditLogout(authentication);
    }

    private void clearAllCookies(final HttpServletRequest request, final HttpServletResponse response) {
        final Cookie cookies[] = request.getCookies();
        if (cookies == null || cookies.length < 1)
            return;
        for (final Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            cookie.setValue(null);
            response.addCookie(cookie);
        }
    }

    private void auditLogout(final Authentication authentication) {
        if (authentication != null) {
            final String systemAuditId = ((HashMap<String, String>) authentication.getCredentials())
                    .get(SecurityConstants.LOGIN_LOG_ID);
            if (StringUtils.isNotBlank(systemAuditId)) {
                final SystemAudit systemAudit = systemAuditService.getSystemAuditById(Long.valueOf(systemAuditId));
                systemAudit.setLogoutTime(new Date());
                systemAuditService.createOrUpdateSystemAudit(systemAudit);
            }
        }
    }

}
