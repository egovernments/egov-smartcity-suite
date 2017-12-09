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

package org.egov.infra.config.security.authentication.listener;

import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.security.audit.entity.LoginAudit;
import org.egov.infra.security.audit.service.LoginAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Date;

import static org.egov.infra.security.utils.SecurityConstants.LOGIN_IP;
import static org.egov.infra.security.utils.SecurityConstants.LOGIN_TIME;
import static org.egov.infra.security.utils.SecurityConstants.LOGIN_USER_AGENT;
import static org.egov.infra.utils.ApplicationConstant.TENANTID_KEY;
import static org.egov.infra.utils.ApplicationConstant.USERID_KEY;

public class UserSessionDestroyListener implements HttpSessionListener {

    @Autowired
    private LoginAuditService loginAuditService;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("entityValidator")
    private LocalValidatorFactoryBean entityValidator;

    @Value("${master.server}")
    private boolean masterServer;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        //do nothing
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        if (masterServer)
            auditUserLogin(event.getSession());
    }

    private void auditUserLogin(final HttpSession session) {
        if (session.getAttribute(LOGIN_IP) != null) {
            try {
                ApplicationThreadLocals.setTenantID((String) session.getAttribute(TENANTID_KEY));
                LoginAudit loginAudit = new LoginAudit();
                loginAudit.setLoginTime((Date) session.getAttribute(LOGIN_TIME));
                loginAudit.setUser(userService.getUserById((Long) session.getAttribute(USERID_KEY)));
                loginAudit.setIpAddress((String) session.getAttribute(LOGIN_IP));
                loginAudit.setUserAgent((String) session.getAttribute(LOGIN_USER_AGENT));
                loginAudit.setLogoutTime(new Date());
                if (entityValidator.validate(loginAudit).isEmpty())
                    loginAuditService.auditLogin(loginAudit);
            } finally {
                ApplicationThreadLocals.clearValues();
            }
        }
    }
}
