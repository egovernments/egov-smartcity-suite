/*
 * InsufficientAmountException.java  Created on Sep 20, 2006
 *
 *  Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions;

/**
 * @author Tilak
 *  InsufficientAmountException is thrown when bank has insufficient balance for payments
 */
public class InsufficientAmountException extends Exception
{

	public InsufficientAmountException()
	{
		super();

	}
	public InsufficientAmountException(String msg)
	{
		super(msg);
	}

	public InsufficientAmountException(String msg, Throwable th)
	{
		super(msg,th);
	}

}


