/*
 * @(#)XSSFilter.java 3.0, 18 Jun, 2013 4:01:56 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Filter which handles possible XSS attack
 **/
public class XSSFilter implements Filter {

	@Override
	public void init(final javax.servlet.FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(final javax.servlet.ServletRequest request, final javax.servlet.ServletResponse response, final javax.servlet.FilterChain filterChain) throws IOException, ServletException {
		final XSSRequestWrapper xssRequest = new XSSRequestWrapper((HttpServletRequest) request);
		filterChain.doFilter(xssRequest, response);
	}
}