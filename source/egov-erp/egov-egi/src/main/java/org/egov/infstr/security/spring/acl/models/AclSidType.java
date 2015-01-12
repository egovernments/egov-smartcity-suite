/*
 * @(#)AclSidType.java 3.0, 18 Jun, 2013 3:58:43 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.spring.acl.models;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;

public class AclSidType extends BaseModel {

	private static final long serialVersionUID = -7248239425888457790L;
	private String type;

	@Required(message = "sidtype is required")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
