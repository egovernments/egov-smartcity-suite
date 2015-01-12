/*
 * @(#)DateToStringConverter.java 3.0, 10 Jun, 2013 11:26:45 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public class DateToStringConverter implements Converter {
	@Override
	public Object convert(final Class type, final Object value) {

		if (value == null) {
			return null;
		} else if (type == String.class && value instanceof Date) {
			return convertToString(type, value);
		} else if (type == String.class) {
			return value.toString();
		}
		throw new ConversionException("Could not convert " + value.getClass().getName() + " to " + type.getName());
	}

	protected Object convertToString(final Class type, final Object value) {
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Object _obj_date = null;
		if (value instanceof Date) {
			_obj_date = df.format(value);
		}
		return _obj_date;
	}

}
