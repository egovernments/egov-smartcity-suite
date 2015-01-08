/*
 * @(#)Bidder.java 3.0, 6 Jun, 2013 2:48:58 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

public interface Bidder {

	
	public Integer getBidderId();
	
	public String getName();
	
	public String getAddress();
	
	public String getCode();
	
	//return RELATION,CONTRACTOR or OWNER
	public String getBidderType();
}
