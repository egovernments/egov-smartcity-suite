/*
 * @(#)AclObjClass.java 3.0, 18 Jun, 2013 3:53:14 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.spring.acl.models;

import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;

public class AclObjClass extends BaseModel {
	private static final long serialVersionUID = 1L;
	private String className;

	@Required(message = "className is required")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
