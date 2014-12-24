/*
 * @(#)LicenseSubType.java 3.0, 29 Jul, 2013 1:24:26 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

import org.egov.infstr.models.BaseModel;

public class LicenseSubType extends BaseModel {
	private static final long serialVersionUID = 1L;
	private String name;
	private String code;
	private LicenseType licenseType;

	public LicenseType getLicenseType() {
		return this.licenseType;
	}

	public void setLicenseType(final LicenseType licenseType) {
		this.licenseType = licenseType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.models.BaseModel#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("LicenseSubType= { ");
		str.append("serialVersionUID=").append(serialVersionUID);
		str.append("name=").append(this.name == null ? "null" : this.name);
		str.append("code=").append(this.name == null ? "null" : this.code);
		str.append("licenseType=").append(this.licenseType == null ? "null" : this.licenseType.toString());
		str.append("}");
		return str.toString();
	}
}
