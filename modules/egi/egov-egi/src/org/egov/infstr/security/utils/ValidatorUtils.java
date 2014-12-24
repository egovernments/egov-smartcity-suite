/*
 * @(#)ValidatorUtils.java 3.0, 18 Jun, 2013 3:59:55 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.security.utils;

import java.util.regex.Pattern;

public class ValidatorUtils {

	private static final Pattern PASSWORD_PATTERN = Pattern.compile("(?=^.{8,}$)(?=.*\\d)(?!.*[&<>#%\\'\\\"\\\\\\/])(?!.*\\s)(?=.*[A-Z])(?=.*[a-z]).*$");

	public static boolean isValidPassword(final String passwd, final boolean isStrong) {
		return (org.apache.commons.lang.StringUtils.isBlank(passwd) || passwd.length() < 8) ? false : (isStrong ? (passwd.length() < 31 && PASSWORD_PATTERN.matcher(passwd).find()) : true);

	}
}
