/*
 * @(#)StringToDateConverter.java 3.0, 18 Jun, 2013 12:13:39 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public class StringToDateConverter implements Converter {

	@Override
	public Object convert(final Class type, final Object obj) {
		if (obj == null) {
			return null;
		} else if (type == Date.class && obj instanceof String) {
			return convertToDate(type, obj);
		} else if (type == String.class && obj instanceof Date) {
			return convertToString(type, obj);
		} else if (type == String.class && obj instanceof String) {
			return obj;
		}
		throw new ConversionException("Could not convert " + obj.getClass().getName() + " to " + type.getName());
	}

	protected Object convertToString(final Class type, final Object value) {
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		if (value instanceof Date) {
			return df.format(value);
		}
		throw new ConversionException("Could not convert " + value.getClass().getName() + " to " + type.getName());
	}

	protected Object convertToDate(final Class type, final Object value) {
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		if (value instanceof String) {
			try {
				return df.parse((String) value);
			} catch (final Exception pe) {
				throw new ConversionException("Error converting String to Date ");
			}
		}
		throw new ConversionException("Could not convert " + value.getClass().getName() + " to " + type.getName());
	}

}
