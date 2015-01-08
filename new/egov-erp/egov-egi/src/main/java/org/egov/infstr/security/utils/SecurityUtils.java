/*
 * @(#)SecurityUtils.java 3.0, 18 Jun, 2013 4:00:06 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.utils;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityUtils implements SecurityConstants {

	private final static Logger LOG = LoggerFactory.getLogger(SecurityUtils.class);
	private final static WeakHashMap<String, Properties> SQL_INJ_WHITE_LIST = new WeakHashMap<String, Properties>();
	private final static String SQL_WHITE_LST_PROP_FILE = "/WEB-INF/sqlwhitelist.properties";

	public static String checkSQLInjection(final String input) {
		if (isNotBlank(input)) {
			try {
				feedSQLWhiteList(SQL_WHITE_LST_PROP_FILE);
				final Properties whiteList = SQL_INJ_WHITE_LIST.get(EGOVThreadLocals.getServletContext().getServletContextName());
				if (whiteList.containsValue(input)) {
					return input;
				}
			} catch (final Exception e) {
				LOG.warn("SQL White Listed Properties is not loaded or unavailable, This will cause strict SQL Injection checking");
			}
			for (final String blkVal : SQL_INJ_BLK_LIST) {
				final StringTokenizer tokenValue = new StringTokenizer(input, " ");
				while (tokenValue.hasMoreTokens()) {
					if (tokenValue.nextToken().toLowerCase().equals(blkVal)) {
						LOG.error("Found SQL Injection attack, Domain Name : {} User ID : {}", EGOVThreadLocals.getDomainName(), EGOVThreadLocals.getUserId());
						throw new EGOVRuntimeException("Invalid user input found, possible SQL Injection!");
					}
				}
			}
		}
		return input;
	}

	public static String checkXSSAttack(final String input) {
		return VirtualSanitizer.sanitize(input);
	}

	private static void feedSQLWhiteList(final String fileLocation) throws FileNotFoundException, IOException {
		if (!SQL_INJ_WHITE_LIST.containsKey(EGOVThreadLocals.getServletContext().getServletContextName())) {
			final Properties property = new Properties();
			property.load(EGOVThreadLocals.getServletContext().getResourceAsStream(fileLocation));
			SQL_INJ_WHITE_LIST.put(EGOVThreadLocals.getServletContext().getServletContextName(), property);
		}
	}
}
