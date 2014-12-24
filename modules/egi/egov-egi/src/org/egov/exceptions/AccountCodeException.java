/*
 * @(#)AccountCodeException.java 3.0, 6 Jun, 2013 1:47:41 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.exceptions;


/**
 * The Class AccountCodeException.
 * For Capturing Account Code Exception
 */
public class AccountCodeException extends RBACException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new account code exception.
	 */
	public AccountCodeException() {
		super();
	}
	
	/**
	 * Instantiates a new account code exception.
	 * @param msg the msg
	 */
	public AccountCodeException(final String msg) {
		super(msg);
	}
	
	/**
	 * Instantiates a new account code exception.
	 * @param msg the msg
	 * @param throwable the throwable
	 */
	public AccountCodeException(final String msg, final Throwable throwable) {
		super(msg, throwable);
	}
}
