/*
 * @(#)AuthorizationException.java 3.0, 6 Jun, 2013 1:54:08 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class AuthorizationException.
 * For Capturing Authorization Related Exception 
 */
public class AuthorizationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(AuthorizationException.class);
	
	/**
	 * Instantiates a new authorization exception.
	 * @param msg the message
	 */
	public AuthorizationException(final String msg) {
		super(msg);
		LOG.error(msg);
	}
	
	/**
	 * Instantiates a new authorization exception.
	 * @param msg the message
	 * @param throwable the Throwable
	 */
	public AuthorizationException(final String msg, final Throwable throwable) {
		super(msg, throwable);
		LOG.error(msg, throwable);
	}
}
