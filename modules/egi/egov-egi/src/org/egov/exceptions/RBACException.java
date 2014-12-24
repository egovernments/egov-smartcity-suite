/*
 * @(#)RBACException.java 3.0, 6 Jun, 2013 2:05:15 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.exceptions;

/**
 * The Class RBACException.
 * Checked exception for RBAC operations
 */
public class RBACException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new rBAC exception.
	 */
	public RBACException() {
		super();
	}
	
	/**
	 * Instantiates a new rBAC exception.
	 * @param msg the msg
	 */
	public RBACException(final String msg) {
		super(msg);
	}
	
	/**
	 * Instantiates a new rBAC exception.
	 * @param msg the msg
	 * @param throwable the throwable
	 */
	public RBACException(final String msg, final Throwable throwable) {
		super(msg, throwable);
	}
}
