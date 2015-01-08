/*
 * @(#)EGOVException.java 3.0, 6 Jun, 2013 1:57:47 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class EGOVException.
 * Generic checked exception class. 
 */
public class EGOVException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(EGOVException.class);
	
	/**
	 * Instantiates a new eGOV exception.
	 * @param msg the msg
	 */
	public EGOVException(final String msg) {
		super(msg);
		LOG.error(msg);
	}
	
	/**
	 * Instantiates a new eGOV exception.
	 * @param msg the msg
	 * @param throwable the throwable
	 */
	public EGOVException(final String msg, final Throwable throwable) {
		super(msg, throwable);
		LOG.error(msg, throwable);
	}
}
