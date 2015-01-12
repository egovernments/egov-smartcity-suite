/*
 * @(#)NoSuchObjectTypeException.java 3.0, 6 Jun, 2013 2:03:50 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class NoSuchObjectTypeException.
 * Unchecked exception for Object existence.
 */
public class NoSuchObjectTypeException extends EGOVRuntimeException {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(NoSuchObjectTypeException.class);
	
	/**
	 * Instantiates a new no such object type exception.
	 * @param msg the msg
	 */
	public NoSuchObjectTypeException(final String msg) {
		super(msg);
		LOGGER.error(msg);
	}
	
	/**
	 * Instantiates a new no such object type exception.
	 * @param msg the msg
	 * @param throwable the throwable
	 */
	public NoSuchObjectTypeException(final String msg, final Throwable throwable) {
		super(msg, throwable);
		LOGGER.error(msg, throwable);
	}
}
