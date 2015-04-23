/*
 * VoucherException.java  Created on May 17, 2006
 *
 *  Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.exilant.eGov.src.transactions;

/**
 * @author Tilak
 *  @Version 1.00
 *  Used for integration module, VoucherException is thrown when the received voucher
 *  is null
 */
public class VoucherException extends Exception 
{
	
	public VoucherException()
	{
		super();
	}

	public VoucherException(String msg)
	{
		super(msg);
	}
	
	public VoucherException(String msg, Throwable th)
	{
		super(msg,th);
	}
	
}


