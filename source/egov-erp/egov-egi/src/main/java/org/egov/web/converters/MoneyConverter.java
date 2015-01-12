/*
 * @(#)MoneyConverter.java 3.0, 14 Jun, 2013 12:36:33 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.web.converters;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.egov.infstr.models.Money;

public class MoneyConverter extends StrutsTypeConverter {

	@Override
	public Object convertFromString(final Map context, final String[] value, final Class toClass) {
		if (value == null || value.length == 0 || value[0] == null || "".equals(value[0].trim())) {
			return new Money(0.0);
		}
		return new Money(Double.parseDouble(value[0]));
	}

	@Override
	public String convertToString(final Map context, final Object money) {
		return money.toString();
	}

}
