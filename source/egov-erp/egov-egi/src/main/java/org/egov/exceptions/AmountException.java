/*
 * @(#)AmountException.java 3.0, 6 Jun, 2013 1:50:38 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.exceptions;

/**
 * The Class AmountException.
 * For Capturing Amount Based Exception
 */
public class AmountException extends RBACException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new amount exception.
	 */
	public AmountException() {
		super();
	}
	
	/**
	 * Instantiates a new amount exception.
	 * @param msg the message
	 */
	public AmountException(final String msg) {
		super(msg);
	}
	
	/**
	 * Instantiates a new amount exception.
	 * @param msg the message
	 * @param throwable the Throwable
	 */
	public AmountException(final String msg, final Throwable throwable) {
		super(msg, throwable);
	}
}
