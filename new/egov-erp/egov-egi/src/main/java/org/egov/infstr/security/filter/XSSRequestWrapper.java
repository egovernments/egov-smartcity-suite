/*
 * @(#)XSSRequestWrapper.java 3.0, 18 Jun, 2013 4:02:10 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.filter;

import static org.egov.infstr.security.utils.VirtualSanitizer.sanitize;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Request wrapper where it sanitize user inputs
 **/
public final class XSSRequestWrapper extends HttpServletRequestWrapper {

	public XSSRequestWrapper(final HttpServletRequest request) {
		super(request);
	}

	@Override
	public String[] getParameterValues(final String paramName) {
		final String[] values = super.getParameterValues(paramName);
		if (values != null) {
			final String[] cleanValues = new String[values.length];
			int index = 0;
			for (final String value : values) {
				cleanValues[index++] = sanitize(value);
			}
			return cleanValues;
		}
		return null;
	}

	@Override
	public String getParameter(final String paramName) {
		return sanitize(super.getParameter(paramName));
	}

	@Override
	public String getHeader(final String headerName) {
		return sanitize(super.getHeader(headerName));
	}
}