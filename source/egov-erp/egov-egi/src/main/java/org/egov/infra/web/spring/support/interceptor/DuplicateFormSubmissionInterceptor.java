package org.egov.infra.web.spring.support.interceptor;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.egov.infra.web.spring.support.annotation.DuplicateRequestToken;
import org.egov.infra.web.spring.support.annotation.ValidateToken;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

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