/*
 * @(#)Terminal.java 3.0, 15 Jun, 2013 11:32:35 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.user;

import org.egov.lib.security.JurisdictionData;

public interface Terminal extends JurisdictionData {
	/**
	 * @return Returns the id.
	 */
	Integer getId();

	/**
	 * @param id The id to set.
	 */
	void setId(Integer id);

	/**
	 * @return Returns the ipAddress.
	 */
	String getIpAddress();

	/**
	 * @param ipAddress The ipAddress to set.
	 */
	void setIpAddress(String ipAddress);

	/**
	 * @return Returns the terminalDesc.
	 */
	String getTerminalDesc();

	/**
	 * @param terminalDesc The terminalDesc to set.
	 */
	void setTerminalDesc(String terminalDesc);

	/**
	 * @return Returns the terminalName.
	 */
	String getTerminalName();

	/**
	 * @param terminalName The terminalName to set.
	 */
	void setTerminalName(String terminalName);
}
