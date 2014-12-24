/*
 * @(#)TrimInterceptor.java 3.0, 14 Jun, 2013 12:30:30 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.interceptors;

import static org.egov.infstr.security.utils.VirtualSanitizer.sanitize;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class TrimInterceptor extends AbstractInterceptor implements StrutsStatics {

	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(final ActionInvocation invocation) throws Exception {
		// Get the action context from the invocation so we can access the
		// HttpServletRequest and HttpSession objects.
		final HttpServletRequest request = (HttpServletRequest) invocation.getInvocationContext().get(HTTP_REQUEST);
		Map parameters = invocation.getInvocationContext().getParameters();
		parameters = this.getTrimmedParameters(request, parameters);
		invocation.getInvocationContext().setParameters(parameters);
		return invocation.invoke();
	}

	/**
	 * @param request
	 * @param parameters
	 */
	public Map getTrimmedParameters(final HttpServletRequest request, final Map parameters) {
		for (final Iterator paramIter = parameters.entrySet().iterator(); paramIter.hasNext();) {
			final Map.Entry entry = (Map.Entry) paramIter.next();
			final String[] values = request.getParameterValues(entry.getKey().toString());
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					values[i] = sanitize(values[i].trim());
				}
			}
			parameters.put(entry.getKey(), values);
		}
		return parameters;
	}
}
