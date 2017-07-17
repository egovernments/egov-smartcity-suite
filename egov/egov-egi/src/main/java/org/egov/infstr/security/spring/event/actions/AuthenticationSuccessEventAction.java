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

package org.egov.infstr.security.spring.event.actions;

import org.egov.infra.config.security.authentication.SecureUser;
import org.egov.infra.security.audit.entity.SystemAudit;
import org.egov.infra.security.audit.service.LoginAttemptService;
import org.egov.infra.security.audit.service.SystemAuditService;
import org.egov.infra.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

import static org.egov.infra.security.utils.SecurityConstants.IPADDR_FIELD;
import static org.egov.infra.security.utils.SecurityConstants.LOGIN_LOG_ID;
import static org.egov.infra.security.utils.SecurityConstants.USERAGENT_FIELD;

@Service
public class AuthenticationSuccessEventAction implements ApplicationSecurityEventAction<InteractiveAuthenticationSuccessEvent> {

    @Autowired
    private SystemAuditService systemAuditService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private SecurityUtils securityUtils;

    @Override
    public void doAction(final InteractiveAuthenticationSuccessEvent authorizedEvent) {
        auditLoginDetails(authorizedEvent);
        resetFailedLoginAttempt(authorizedEvent);
    }

    private void auditLoginDetails(InteractiveAuthenticationSuccessEvent authorizedEvent) {
        HashMap<String, String> creds = ((HashMap<String, String>) authorizedEvent.getAuthentication().getCredentials());
        final SystemAudit systemAudit = new SystemAudit();
        systemAudit.setLoginTime(new Date(authorizedEvent.getTimestamp()));
        systemAudit.setUser(securityUtils.getCurrentUser());
        systemAudit.setIpAddress(creds.get(IPADDR_FIELD));
        systemAudit.setUserAgentInfo(creds.get(USERAGENT_FIELD));
        systemAuditService.createOrUpdateSystemAudit(systemAudit);
        final String loginLogID = systemAudit.getId().toString();
        creds.put(LOGIN_LOG_ID, loginLogID);
    }

    private void resetFailedLoginAttempt(InteractiveAuthenticationSuccessEvent authorizedEvent) {
        loginAttemptService.resetFailedAttempt(((SecureUser) authorizedEvent.getAuthentication().getPrincipal()).getUsername());
    }
}
