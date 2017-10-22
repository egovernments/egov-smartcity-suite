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

import org.egov.infra.config.security.authentication.userdetail.CurrentUser;
import org.egov.infra.security.audit.entity.LoginAudit;
import org.egov.infra.security.audit.service.LoginAttemptService;
import org.egov.infra.security.audit.service.LoginAuditService;
import org.egov.infra.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import static org.egov.infra.security.utils.SecurityConstants.IPADDR_FIELD;
import static org.egov.infra.security.utils.SecurityConstants.LOGIN_LOG_ID;
import static org.egov.infra.security.utils.SecurityConstants.USERAGENT_FIELD;
import static org.springframework.util.StringUtils.hasText;

public class ApplicationAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private LoginAuditService loginAuditService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private SecurityUtils securityUtils;

    private RequestCache requestCache = new HttpSessionRequestCache();

    private Pattern excludedUrls;

    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }

    public void setExcludedUrlRegex(String excludedUrlRegex) {
        this.excludedUrls = Pattern.compile(excludedUrlRegex);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        auditLoginDetails(authentication);
        resetFailedLoginAttempt(authentication);
        redirectToSuccessPage(request, response, authentication);
    }

    private void redirectToSuccessPage(HttpServletRequest request, HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }
        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl()
                || (targetUrlParameter != null && hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        clearAuthenticationAttributes(request);
        String targetUrl = savedRequest.getRedirectUrl();
        if (excludedUrls.matcher(targetUrl).find())
            targetUrl = getDefaultTargetUrl();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void auditLoginDetails(Authentication authentication) {
        HashMap<String, String> credentials = (HashMap<String, String>) authentication.getCredentials();
        LoginAudit loginAudit = new LoginAudit();
        loginAudit.setLoginTime(new Date());
        loginAudit.setUser(securityUtils.getCurrentUser());
        loginAudit.setIpAddress(credentials.get(IPADDR_FIELD));
        loginAudit.setUserAgentInfo(credentials.get(USERAGENT_FIELD));
        loginAuditService.auditLogin(loginAudit);
        credentials.put(LOGIN_LOG_ID, loginAudit.getId().toString());
    }

    private void resetFailedLoginAttempt(Authentication authentication) {
        loginAttemptService.resetFailedAttempt(((CurrentUser) authentication.getPrincipal()).getUsername());
    }
}
