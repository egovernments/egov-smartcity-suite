/*
 * @(#)LicenseAppType.java 3.0, 29 Jul, 2013 1:24:27 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.license.domain.entity;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.OptionalPattern;
import org.egov.infstr.models.validator.Required;
import org.egov.infstr.models.validator.Unique;
import org.egov.infstr.models.validator.constants.ValidatorConstants;
import org.hibernate.validator.constraints.Length;

@Unique(fields = { "licenseApplicationType" }, id = "id", tableName = "EGTL_MSTR_APP_TYPE", columnName = { "name" }, message = "masters.licenseApplicationType.isunique")
public class LicenseAppType extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final String BY_NAME = "LICENSE_APPTYPE_BY_NAME";

	@Required(message = "masters.licenseApplicationType.name.null")
	@Length(max = 256, message = "masters.licenseApplicationType.name.length")
	@OptionalPattern(regex = ValidatorConstants.alphaNumericwithSpace, message = "tradelicense.error.licenseapptype.text")
	private String name;

	public LicenseAppType() {
		super();
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("LicenseAppType={");
		str.append("serialVersionUID=").append(serialVersionUID);
		str.append("name=").append(this.name == null ? "null" : this.name.toString());
		str.append("}");
		return str.toString();
	}
}