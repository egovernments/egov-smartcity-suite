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

package org.egov.infra.web.struts.interceptors;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.ValidationAware;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.infra.web.struts.annotation.ValidationErrorPage;
import org.egov.infra.web.struts.annotation.ValidationErrorPageExt;
import org.egov.infra.web.struts.annotation.ValidationErrorPageForward;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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
				if (isInvokeAndForward) {
					return (String) actionMethod.invoke(action);
				} else {
					this.invokeActionMethod(actionMethod, action);
					return form;
				}
			}
			return invocation.invoke();
		} catch (final ValidationException e) {
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