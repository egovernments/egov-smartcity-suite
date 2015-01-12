/*
 * @(#)SetThreadLocals.java 3.0, 18 Jun, 2013 1:12:06 PM
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.AuthorizationException;
import org.egov.exceptions.EGOVRuntimeException;

/**
 * The Class SetThreadLocals. Used to set the EgovThreadLocal values
 */
public class SetThreadLocals implements Filter {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(SetThreadLocals.class);

	/**
	 * Initialize the filter
	 * @param config the config
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(final FilterConfig config) {

	}

	/**
	 * This will seta the EgovThreadLocal with various data passed through request
	 * @param request the request
	 * @param response the response
	 * @param chain the chain
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		try {
			setConnectionJndi(request, response);
			chain.doFilter(request, response);
			SetDomainJndiHibFactNames.clearThreadLocals();
		} catch (final AuthorizationException e) {
			throw e;
		} catch (final Throwable e) {
			LOG.error("Error occurred in SetThreadLocals Filter", e);
			throw new EGOVRuntimeException("Internal Server Error", e);
		}

	}

	/**
	 * Destroy.
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {

	}

	/**
	 * Sets the connection jndi.
	 * @param request the request
	 * @param response the response
	 */
	public static void setConnectionJndi(final ServletRequest request, final ServletResponse response) {
		SetDomainJndiHibFactNames.setThreadLocals(request);
	}

}
