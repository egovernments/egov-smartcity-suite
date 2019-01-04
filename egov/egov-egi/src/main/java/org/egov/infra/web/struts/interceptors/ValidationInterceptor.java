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

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import static org.apache.struts2.StrutsStatics.HTTP_REQUEST;
import static org.egov.infra.security.utils.XSSValidator.validate;

public class ValidationInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        String form = "edit";
        Method actionMethod = null;
        Object action = invocation.getAction();
        boolean isInvokeAndForward = false;
        try {
            String method = invocation.getProxy().getMethod();
            if ("create".equals(method)) {
                form = "new";
            }
            Method m = action.getClass().getMethod(method);

            if (m.isAnnotationPresent(ValidationErrorPage.class)) {
                form = m.getAnnotation(ValidationErrorPage.class).value();
            } else if (m.isAnnotationPresent(ValidationErrorPageExt.class)) {
                ValidationErrorPageExt validationErrorPageExt = m.getAnnotation(ValidationErrorPageExt.class);
                form = validationErrorPageExt.action();
                if (validationErrorPageExt.makeCall()) {
                    actionMethod = action.getClass().getMethod(validationErrorPageExt.toMethod());
                }
            } else if (m.isAnnotationPresent(ValidationErrorPageForward.class)) {
                ValidationErrorPageForward forwarder = m.getAnnotation(ValidationErrorPageForward.class);
                actionMethod = action.getClass().getDeclaredMethod(forwarder.forwarderMethod());
                isInvokeAndForward = true;
            }

            HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext().get(HTTP_REQUEST);
            invocation.getInvocationContext().getParameters().keySet().stream().forEach(fieldName -> {
                String[] paramValues = request.getParameterValues(fieldName);
                if (paramValues != null)
                    Stream.of(paramValues).forEach(value -> validate(fieldName, value));
            });

            ValidationAware validationAwareAction = (ValidationAware) invocation.getAction();
            if (validationAwareAction.hasErrors()) {
                if (isInvokeAndForward) {
                    return (String) actionMethod.invoke(action);
                } else {
                    this.invokeActionMethod(actionMethod, action);
                    return form;
                }
            }
            return invocation.invoke();
        } catch (ValidationException e) {
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

    private void invokeActionMethod(Method actionMethod, Object action) throws IllegalAccessException,
            InvocationTargetException {
        if (actionMethod != null) {
            actionMethod.setAccessible(true);
            actionMethod.invoke(action);
        }
    }

    private void transformValidationErrors(ActionInvocation invocation, ValidationException ve) {
        BaseFormAction action = (BaseFormAction) invocation.getAction();
        List<ValidationError> errors = ve.getErrors();
        for (ValidationError error : errors) {
            if (error.getArgs() == null || error.getArgs().length == 0) {
                if (error.isNonFieldError())
                    action.addActionError(action.getText(error.getMessage(), error.getMessage()));
                else
                    action.addFieldError("model." + error.getKey(), action.getText(error.getMessage(), error.getMessage()));
            } else {
                if (error.isNonFieldError())
                    action.addActionError(action.getText(error.getMessage(), error.getMessage(), error.getArgs()));
                else
                    action.addFieldError("model." + error.getKey(), action.getText(error.getMessage(), error.getMessage(), error.getArgs()));
            }
        }
    }

}