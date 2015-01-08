/*
 * @(#)BoundryTypeForm.java 3.0, 18 Jun, 2013 2:20:48 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.client.adminBoundry;

import java.io.Serializable;

import org.egov.infstr.client.EgovActionForm;

public class BoundryTypeForm extends EgovActionForm implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = "";
	private String parentName = "";
	private String heirarchyType = "";
	private String bndryTypeLocal = "";

	/**
	 * This method returns Name of the Boundry Type
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * This method sets Name of the Boundry Type
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * This method returns Name of the Parent Boundry Type
	 * @return Returns the parentName.
	 */
	public String getParentName() {
		return this.parentName;
	}

	/**
	 * This method sets Name of the Parent Boundry Type
	 * @param parentName The parentName to set.
	 */
	public void setParentName(final String parentName) {
		this.parentName = parentName;
	}

	/**
	 * @return the bndryTypeLocal
	 */
	public String getBndryTypeLocal() {
		return this.bndryTypeLocal;
	}

	/**
	 * @param bndryTypeLocal the bndryTypeLocal to set
	 */
	public void setBndryTypeLocal(final String bndryTypeLocal) {
		this.bndryTypeLocal = bndryTypeLocal;
	}

	/**
	 * @return the hierarchyType
	 */
	public String getHeirarchyType() {
		return this.heirarchyType;
	}

	/**
	 * @param hierarchyType the hierarchyType to set
	 */
	public void setHeirarchyType(final String heirarchyType) {
		this.heirarchyType = heirarchyType;
	}
}
