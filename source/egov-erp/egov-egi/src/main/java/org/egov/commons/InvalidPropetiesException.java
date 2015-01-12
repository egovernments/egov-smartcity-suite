/*
 * @(#)InvalidPropetiesException.java 3.0, 6 Jun, 2013 3:54:35 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

/**
 * The Class InvalidPropetiesException.
 */
public class InvalidPropetiesException extends Exception {
	
	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new invalid propeties exception.
	 */
	public InvalidPropetiesException() {
		super();
	}

	/**
	 * Instantiates a new invalid propeties exception.
	 * @param message the message
	 */
	public InvalidPropetiesException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new invalid propeties exception.
	 * @param message the message
	 * @param cause the cause
	 */
	public InvalidPropetiesException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new invalid propeties exception.
	 * @param cause the cause
	 */
	public InvalidPropetiesException(Throwable cause) {
		super(cause);
	}

}
