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

package org.egov.infra.config.security.authentication.handler;

import org.egov.infra.config.security.authentication.userdetail.CurrentUser;
import org.egov.infra.security.audit.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import static org.egov.infra.security.utils.SecurityConstants.IPADDR_FIELD;
import static org.egov.infra.security.utils.SecurityConstants.LOGIN_IP;
import static org.egov.infra.security.utils.SecurityConstants.LOGIN_TIME;
import static org.egov.infra.security.utils.SecurityConstants.LOGIN_USER_AGENT;
import static org.egov.infra.security.utils.SecurityConstants.USERAGENT_FIELD;
import static org.springframework.util.StringUtils.hasText;

public class ApplicationAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private LoginAttemptService loginAttemptService;

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
        auditLoginDetails(request, authentication);
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

    private void auditLoginDetails(HttpServletRequest request, Authentication authentication) {
        HashMap<String, String> credentials = (HashMap<String, String>) authentication.getCredentials();
        HttpSession session = request.getSession(false);
        session.setAttribute(LOGIN_TIME, new Date());
        session.setAttribute(LOGIN_IP, credentials.get(IPADDR_FIELD));
        session.setAttribute(LOGIN_USER_AGENT, credentials.get(USERAGENT_FIELD));
    }

    private void resetFailedLoginAttempt(Authentication authentication) {
        loginAttemptService.resetFailedAttempt(((CurrentUser) authentication.getPrincipal()).getUsername());
    }
}
