/*
 * PropertyNotFoundException.java Created on Dec 09, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.exceptions;

/**
 * @author Gayathri
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PropertyNotFoundException extends Exception {

	public PropertyNotFoundException()
	{
		super();
	}

	public PropertyNotFoundException(String msg)
	{
		super(msg);
	}

	public PropertyNotFoundException(String msg, Throwable th)
	{
		super(msg,th);
	}

}
