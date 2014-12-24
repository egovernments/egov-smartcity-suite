/*
 * @(#)TerminalImpl.java 3.0, 15 Jun, 2013 11:33:31 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.user;

import java.io.Serializable;

public class TerminalImpl implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String terminalName;
	private String terminalDesc;
	private String ipAddress;

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the ipAddress.
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress The ipAddress to set.
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return Returns the terminalDesc.
	 */
	public String getTerminalDesc() {
		return terminalDesc;
	}

	/**
	 * @param terminalDesc The terminalDesc to set.
	 */
	public void setTerminalDesc(String terminalDesc) {
		this.terminalDesc = terminalDesc;
	}

	/**
	 * @return Returns the terminalName.
	 */
	public String getTerminalName() {
		return terminalName;
	}

	/**
	 * @param terminalName The terminalName to set.
	 */
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
}
