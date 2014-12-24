/*
 * @(#)CacheControlFilter.java 3.0, 18 Jun, 2013 12:52:55 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * This Filter is used to improve ui performance by setting Cache-Control header 
 * to static resources like js,css,jpg,gif,etc.
 */
public class CacheControlFilter implements Filter {

	private static final String EXPIRE_HEADER = "Expires";
	private static final String ETAG_HEADER = "ETag";
	private static final String CACHE_CONTROL_HEADER = "Cache-Control";
	private static final String PRAGMA_HEADER = "Pragma";
	public static final long DEFAULT_EXPIRES_SECONDS = 30 * 24 * 60 * 60;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {

		final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setHeader(CACHE_CONTROL_HEADER, "private,max-age=" + this.expireInSeconds);
		httpServletResponse.setDateHeader(EXPIRE_HEADER, System.currentTimeMillis() + this.expireInSeconds * 1000L);
		httpServletResponse.setHeader(PRAGMA_HEADER, null);
		httpServletResponse.setHeader(ETAG_HEADER, null);
		chain.doFilter(request, httpServletResponse);
	}

	@Override
	public void destroy() {
	}

	private long expireInSeconds = 0;

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		if (filterConfig.getInitParameter("expireInSeconds") == null) {
			this.expireInSeconds = DEFAULT_EXPIRES_SECONDS;
		} else {
			this.expireInSeconds = Long.valueOf(filterConfig.getInitParameter("expireInSeconds"));
		}
	}

}
