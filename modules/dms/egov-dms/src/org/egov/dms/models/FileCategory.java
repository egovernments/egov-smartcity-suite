/*
 * @(#)FileCategory.java 3.0, 15 Jul, 2013 9:36:38 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.dms.models;

import javax.validation.Valid;


/**
 * The Class FileCategory.
 */
public class FileCategory extends FileProperty {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The parent. */
	@Valid
	private FileCategory parent;
	
	/**
	 * Gets the parent.
	 * @return the parent
	 */
	public FileCategory getParent() {
		return parent;
	}
	
	/**
	 * Sets the parent.
	 * @param parent the parent to set
	 */
	public void setParent(FileCategory parent) {
		this.parent = parent;
	}
}
