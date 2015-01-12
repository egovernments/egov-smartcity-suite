/*
 * @(#)InvalidPropertyException.java 3.0, 6 Jun, 2013 2:02:51 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.exceptions;

/**
 * The Class InvalidPropertyException.
 * Checked exception class for Invalid Property
 */
public class InvalidPropertyException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new invalid property exception.
	 */
	public InvalidPropertyException() {
		super();
	}
	
	/**
	 * Instantiates a new invalid property exception.
	 * @param msg the msg
	 */
	public InvalidPropertyException(final String msg) {
		super(msg);
	}
	
	/**
	 * Instantiates a new invalid property exception.
	 * @param msg the msg
	 * @param throwable the throwable
	 */
	public InvalidPropertyException(final String msg, final Throwable throwable) {
		super(msg, throwable);
	}
}
