/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
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
