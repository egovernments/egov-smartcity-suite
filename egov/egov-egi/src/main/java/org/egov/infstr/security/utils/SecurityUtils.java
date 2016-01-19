/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infstr.security.utils;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.EgovThreadLocals;
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
				final Properties whiteList = SQL_INJ_WHITE_LIST.get(EgovThreadLocals.getServletContext().getServletContextName());
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
						LOG.error("Found SQL Injection attack, Domain Name : {} User ID : {}", EgovThreadLocals.getDomainName(), EgovThreadLocals.getUserId());
						throw new ApplicationRuntimeException("Invalid user input found, possible SQL Injection!");
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
		if (!SQL_INJ_WHITE_LIST.containsKey(EgovThreadLocals.getServletContext().getServletContextName())) {
			final Properties property = new Properties();
			property.load(EgovThreadLocals.getServletContext().getResourceAsStream(fileLocation));
			SQL_INJ_WHITE_LIST.put(EgovThreadLocals.getServletContext().getServletContextName(), property);
		}
	}
}
