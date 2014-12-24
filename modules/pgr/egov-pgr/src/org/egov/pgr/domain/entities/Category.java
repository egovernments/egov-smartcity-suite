/*
 * @(#)Category.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.entities;

import org.egov.infstr.models.BaseModel;

public class Category extends BaseModel {

	private static final long serialVersionUID = 1L;

	private String categoryName;

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(final String categoryName) {
		this.categoryName = categoryName;
	}

}