/*
 * @(#)LabelValueBean.java 3.0, 18 Jun, 2013 12:06:46 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.io.Serializable;

public class LabelValueBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String name;

	/**
	 * Gets the id.
	 * @return Returns the id.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 * @param id The id to set.
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name.
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}
}
