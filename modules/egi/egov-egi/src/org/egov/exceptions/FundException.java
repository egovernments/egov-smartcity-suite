/*
 * @(#)FundException.java 3.0, 6 Jun, 2013 2:02:21 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.exceptions;

/**
 * The Class FundException.
 * For Fund related checked exception
 */
public class FundException extends RBACException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new fund exception.
	 */
	public FundException() {
		super();
	}
	
	/**
	 * Instantiates a new fund exception.
	 * @param msg the msg
	 */
	public FundException(final String msg) {
		super(msg);
	}
	
	/**
	 * Instantiates a new fund exception.
	 * @param msg the msg
	 * @param throwable the throwable
	 */
	public FundException(final String msg, final Throwable throwable) {
		super(msg, throwable);
	}
}
