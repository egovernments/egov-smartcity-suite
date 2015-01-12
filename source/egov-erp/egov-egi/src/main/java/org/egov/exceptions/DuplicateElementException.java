/*
 * @(#)DuplicateElementException.java 3.0, 6 Jun, 2013 1:56:01 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DuplicateElementException.
 * For Capturing all type of duplicate elements
 */
public class DuplicateElementException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DuplicateElementException.class);
	
	/**
	 * Instantiates a new duplicate element exception.
	 */
	public DuplicateElementException() {
		super();
	}
	
	/**
	 * Instantiates a new duplicate element exception.
	 * @param message the message
	 */
	public DuplicateElementException(final String message) {
		super(message);
		LOG.error(message);
	}
	
	/**
	 * Instantiates a new duplicate element exception.
	 * @param message the message
	 * @param cause the cause
	 */
	public DuplicateElementException(final String message, final Throwable cause) {
		super(message, cause);
		LOG.error(message, cause);
	}
	
	/**
	 * Instantiates a new duplicate element exception.
	 * @param cause the throwable
	 */
	public DuplicateElementException(final Throwable cause) {
		super(cause);
		LOG.error(cause.toString());
	}
}
