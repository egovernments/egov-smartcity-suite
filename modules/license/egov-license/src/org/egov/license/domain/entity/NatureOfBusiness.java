/*
 * @(#)NatureOfBusiness.java 3.0, 29 Jul, 2013 1:24:25 PM
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

@Unique(fields = { "tradeNature" }, id = "id", tableName = "EGTL_TRADE_NATURE", columnName = { "TRADE_NATURE" }, message = "masters.tradenature.isunique")
public class NatureOfBusiness extends BaseModel {
	private static final long serialVersionUID = 1L;
	@Required(message = "tradelic.master.tradenature.null")
	@Length(max = 256, message = "masters.tradenature.length")
	@OptionalPattern(regex = ValidatorConstants.alphaNumericwithSpace, message = "tradelicense.error.tradenature.text")
	private String name;
	public static final String BY_NAME = "NATUREOFBUSINESS_BY_NAME";

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		str.append("NatureOfBusiness={");
		str.append("  name=").append(this.name == null ? "null" : this.name.toString());
		str.append("}");
		return str.toString();
	}
}