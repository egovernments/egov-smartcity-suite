/*
 * @(#)CompReceivingModes.java 3.0, 23 Jul, 2013 3:29:44 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.pgr.domain.entities;

import org.egov.infstr.models.BaseModel;

public class CompReceivingModes extends BaseModel {

	private static final long serialVersionUID = 1L;
	private String compMode;

	public String getCompMode() {
		return this.compMode;
	}

	public void setCompMode(final String compMode) {
		this.compMode = compMode;
	}

}