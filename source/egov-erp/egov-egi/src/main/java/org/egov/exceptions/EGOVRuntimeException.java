/*
 * @(#)EGOVRuntimeException.java 3.0, 6 Jun, 2013 2:00:54 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class EGOVRuntimeException.
 * Generic unchecked exception class
 */
public class EGOVRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(EGOVRuntimeException.class);
	
	/**
	 * Instantiates a new eGOV runtime exception.
	 * @param msg the msg
	 */
	public EGOVRuntimeException(final String msg) {
		super(msg);
		LOG.error(msg);
	}
	
	/**
	 * Instantiates a new eGOV runtime exception.
	 * @param msg the msg
	 * @param throwable the throwable
	 */
	public EGOVRuntimeException(final String msg, final Throwable throwable) {
		super(msg, throwable);
		LOG.error(msg, throwable);
	}
}
