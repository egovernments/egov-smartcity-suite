/*
 * @(#)ComplaintStatus.java 3.0, 23 Jul, 2013 3:29:42 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.entities;

import org.egov.infstr.models.BaseModel;

public class ComplaintStatus extends BaseModel {

	private static final long serialVersionUID = 1L;
	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}