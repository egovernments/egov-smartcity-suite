/*
 * @(#)FeeType.java 3.0, 29 Jul, 2013 1:24:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

import org.egov.infstr.models.BaseModel;

public class FeeType extends BaseModel {
	private static final long serialVersionUID = 1L;
	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("FeeType={");
		str.append("name=").append(this.name == null ? "null" : this.name.toString());
		str.append("}");
		return str.toString();
	}
}
