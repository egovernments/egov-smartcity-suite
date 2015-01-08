/*
 * @(#)ValidationInterceptor.java 3.0, 14 Jun, 2013 12:31:02 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.interceptors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.egov.infstr.ValidationError;
import org.egov.infstr.ValidationException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.web.actions.BaseFormAction;
import org.egov.web.annotation.ValidationErrorPage;
import org.egov.web.annotation.ValidationErrorPageExt;
import org.egov.web.annotation.ValidationErrorPageForward;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ValidationInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(final ActionInvocation invocation) throws Exception {
		String form = "edit";
		Method actionMethod = null;
		final Object action = invocation.getAction();
		boolean isInvokeAndForward = false;
		try {
			final String method = invocation.getProxy().getMethod();
			if ("create".equals(method)) {
				form = "new";
			}
			final Method m = action.getClass().getMethod(method);

			if (m.isAnnotationPresent(ValidationErrorPage.class)) {
				form = m.getAnnotation(ValidationErrorPage.class).value();
			} else if (m.isAnnotationPresent(ValidationErrorPageExt.class)) {
				final ValidationErrorPageExt validationErrorPageExt = m.getAnnotation(ValidationErrorPageExt.class);
				form = validationErrorPageExt.action();
				if (validationErrorPageExt.makeCall()) {
					actionMethod = action.getClass().getMethod(validationErrorPageExt.toMethod());
				}
			} else if (m.isAnnotationPresent(ValidationErrorPageForward.class)) {
				final ValidationErrorPageForward forwarder = m.getAnnotation(ValidationErrorPageForward.class);
				actionMethod = action.getClass().getDeclaredMethod(forwarder.forwarderMethod());
				isInvokeAndForward = true;
			}
			final ValidationAware validationAwareAction = (ValidationAware) invocation.getAction();
			if (validationAwareAction.hasErrors()) {
				HibernateUtil.markForRollback();
				if (isInvokeAndForward) {
					return (String) actionMethod.invoke(action);
				} else {
					this.invokeActionMethod(actionMethod, action);
					return form;
				}
			}
			return invocation.invoke();
		} catch (final ValidationException e) {
			HibernateUtil.markForRollback();
			if (BaseFormAction.class.isAssignableFrom(invocation.getAction().getClass())) {
				this.transformValidationErrors(invocation, e);
				if (isInvokeAndForward) {
					return (String) actionMethod.invoke(action);
				} else {
					this.invokeActionMethod(actionMethod, action);
					return form;
				}
			}
			throw e;
		}
	}

	private void invokeActionMethod(final Method actionMethod, final Object action) throws IllegalAccessException, InvocationTargetException, RuntimeException {
		if (actionMethod != null) {
			actionMethod.setAccessible(true);
			actionMethod.invoke(action);
		}
	}

	private void transformValidationErrors(final ActionInvocation invocation, final ValidationException e) {
		final BaseFormAction action = (BaseFormAction) invocation.getAction();
		final List<ValidationError> errors = e.getErrors();
		for (final ValidationError error : errors) {
			if (error.getArgs() == null || error.getArgs().length == 0) {
				action.addFieldError("model." + error.getKey(), action.getText(error.getMessage(), error.getMessage()));
			} else {
				action.addFieldError("model." + error.getKey(), action.getText(error.getMessage(), error.getMessage(), error.getArgs()));
			}
		}
	}

}