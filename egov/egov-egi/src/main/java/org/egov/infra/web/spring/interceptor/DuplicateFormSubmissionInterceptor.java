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

package org.egov.infra.web.spring.interceptor;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.egov.infra.web.spring.annotation.DuplicateRequestToken;
import org.egov.infra.web.spring.annotation.ValidateToken;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class DuplicateFormSubmissionInterceptor extends HandlerInterceptorAdapter {
    private static final String TOKEN_NAME = "tokenName";
    private static final String ERROR_PAGE = "/error/409";
    private String errorPage = ERROR_PAGE;

    public void setErrorPage(final String errorPage) {
        this.errorPage = errorPage;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        if (handler != null && handler instanceof HandlerMethod
                && ((HandlerMethod) handler).getMethodAnnotation(ValidateToken.class) != null) {
            final HttpSession session = request.getSession();
            synchronized (session) {
                if (havingValidToken(request, session))
                    removeToken(request, session);
                else {
                    response.sendRedirect(request.getContextPath() + errorPage);
                    return false;
                }
            }
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
            final ModelAndView modelAndView) throws Exception {
        if (handler != null && handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;
            final HttpSession session = request.getSession();
            if (handlerMethod.getMethodAnnotation(DuplicateRequestToken.class) != null)
                addToken(request, session);
            else if (handlerMethod.getMethodAnnotation(ValidateToken.class) != null) {
                final BindingResult bindingResult = getBindingResult(modelAndView);
                if (bindingResult != null && bindingResult.hasErrors())
                    addToken(request, session);
            }
        }
    }

    private static BindingResult getBindingResult(final ModelAndView modelAndView) {
        final Set<String> paramNames = modelAndView.getModelMap().keySet();
        return (BindingResult) modelAndView.getModelMap().get(
                paramNames.parallelStream().filter(e -> e.startsWith(BindingResult.MODEL_KEY_PREFIX)).findFirst().get());
    }

    private void addToken(final HttpServletRequest request, final HttpSession session) {
        final String tokenName = RandomStringUtils.randomAlphanumeric(10);
        final String tokenValue = UUID.randomUUID().toString();
        scheduleForRemoval(session, tokenName);
        request.setAttribute(TOKEN_NAME, tokenName);
        request.setAttribute(tokenName, tokenValue);
        session.setAttribute(tokenName, tokenValue);
    }

    private boolean havingValidToken(final HttpServletRequest request, final HttpSession session) {
        final String tokenName = StringUtils.defaultString(request.getParameter(TOKEN_NAME), "NOTOKEN");
        final String tokenValue = request.getParameter(tokenName);
        return !(tokenValue == null || session.getAttribute(tokenName) == null || !session.getAttribute(tokenName)
                .equals(tokenValue));
    }

    private void removeToken(final HttpServletRequest request, final HttpSession session) {
        session.removeAttribute(request.getParameter(TOKEN_NAME));
    }

    private final static long ORPHEN_TOKEN_REMOVAL_DELAY = 30 * 60 * 1000; // 30 minutes
    private final static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private static void scheduleForRemoval(final HttpSession session, final String tokenName) {
        final Runnable tokenRemoverTask = () -> {
            try {session.removeAttribute(tokenName);}catch(Exception e) {}
        };
        executor.schedule(tokenRemoverTask, ORPHEN_TOKEN_REMOVAL_DELAY, TimeUnit.MILLISECONDS);
    }

}