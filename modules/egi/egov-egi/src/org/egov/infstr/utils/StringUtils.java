/*
 * @(#)StringUtils.java 3.0, 18 Jun, 2013 12:15:57 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONObject;

public class StringUtils extends org.apache.commons.lang.StringUtils {

	public static final Pattern SPL_CHAR_PATRN = Pattern.compile("([&;,+=\\[\\]\\{\\}><^\\(\\)#:~`/\\\\!\'\"])");

	/**
	 * Helper method to remove special characters like new line, space and single quote
	 * @return String
	 */
	public static String escapeSpecialChars(final String str) {
		return str.replaceAll("\\s\\s+|\\r\\n", "<br/>").replaceAll("\'", "\\\\'");
	}

	/**
	 * Escape the given string so that it can be safely used inside javascript
	 * @return String the escaped string
	 */
	public static String escapeJavaScript(final String str) {
		return StringEscapeUtils.escapeJavaScript(str);
	}

	public static String escapeJSON(final String str) {
		return JSONObject.escape(str);
	}

	/**
	 * Checks if the given String value contains special characters ([,&,;,,,+,=,{,},>,<,^,(,),#,:,~,`,/,\,!,',",])
	 * @param str a String value
	 * @return boolean hasSpecialChars
	 */
	public static boolean hasSpecialChars(final String str) {
		final Matcher matcher = SPL_CHAR_PATRN.matcher(str);
		return matcher.find();
	}

	public static String emptyIfNull(final String value) {
		return value == null ? EMPTY : value;
	}

	public static String[] toStringArray(final String... values) {
		return values;
	}

	public static List<String> toList(final String... values) {
		return Arrays.asList(values);
	}
}
