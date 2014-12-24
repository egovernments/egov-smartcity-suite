/*
 * @(#)NoSuchObjectException.java 3.0, 6 Jun, 2013 2:29:21 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class NoSuchObjectException.
 * Checked exception for Object existence
 */
public class NoSuchObjectException extends EGOVException {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(NoSuchObjectException.class);

	/**
	 * Instantiates a new no such object exception.
	 * @param msg the msg
	 */
	public NoSuchObjectException(final String msg) {
		super(msg);
		LOGGER.error(msg);
	}

	/**
	 * Instantiates a new no such object exception.
	 * @param msg the msg
	 * @param e the e
	 */
	public NoSuchObjectException(final String msg, final Throwable e) {
		super(msg, e);
		LOGGER.error(msg, e);
	}

}
