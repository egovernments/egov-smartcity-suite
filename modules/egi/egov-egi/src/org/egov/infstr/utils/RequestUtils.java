/*
 * @(#)RequestUtils.java 3.0, 18 Jun, 2013 12:11:01 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.thoughtworks.xstream.XStream;

public class RequestUtils {

	public static String getRequestParamsAsXMLString(final ServletRequest req) {
		final HttpServletRequest request = (HttpServletRequest) req;
		// Iterator of parameter names
		Enumeration<String> names = null;
		// Build a list of relevant request parameters from this request
		final HashMap<String, Object> properties = new HashMap<String, Object>();
		names = request.getParameterNames();
		while (names.hasMoreElements()) {
			final String name = names.nextElement();
			Object value = null;
			value = request.getParameterValues(name);
			properties.put(name, value);
		}
		final XStream xstream = new XStream();
		return xstream.toXML(properties);
	}

}
