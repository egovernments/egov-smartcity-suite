/*
 * @(#)LicenseType.java 3.0, 29 Jul, 2013 1:24:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

import org.egov.infstr.commons.Module;
import org.egov.infstr.models.BaseModel;

public class LicenseType extends BaseModel {
	private static final long serialVersionUID = 1L;
	private String name;
	private Module module;

	public Module getModule() {
		return this.module;
	}

	public void setModule(final Module module) {
		this.module = module;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.models.BaseModel#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("LicenseType= { ");
		str.append("serialVersionUID=").append(serialVersionUID);
		str.append("name=").append(this.name == null ? "null" : this.name.toString());
		str.append("module=").append(this.module == null ? "null" : this.module.toString());
		str.append("}");
		return str.toString();
	}
}
