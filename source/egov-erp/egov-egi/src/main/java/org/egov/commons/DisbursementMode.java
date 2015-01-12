/*
 * @(#)DisbursementMode.java 3.0, 6 Jun, 2013 3:18:20 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.commons;

import org.egov.infstr.models.BaseModel;

public class DisbursementMode extends BaseModel {
	private static final long serialVersionUID = 1L;
	private String type;

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}