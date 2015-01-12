/*
 * @(#)TooManyValuesException.java 3.0, 6 Jun, 2013 2:30:58 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class TooManyValuesException.
 * Checked exception for values.
 */
public class TooManyValuesException extends EGOVException {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(TooManyValuesException.class);

	/**
	 * Instantiates a new too many values exception.
	 * @param msg the msg
	 */
	public TooManyValuesException(final String msg) {
		super(msg);
		LOGGER.error(msg);
	}

	/**
	 * Instantiates a new too many values exception.
	 * @param msg the msg
	 * @param e the e
	 */
	public TooManyValuesException(final String msg, final Throwable e) {
		super(msg, e);
		LOGGER.error(msg, e);
	}

}
