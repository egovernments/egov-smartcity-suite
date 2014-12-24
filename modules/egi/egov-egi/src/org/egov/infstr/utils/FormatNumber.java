/*
 * @(#)FormatNumber.java 3.0, 18 Jun, 2013 12:04:53 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.text.DecimalFormat;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.egov.exceptions.EGOVRuntimeException;



public class FormatNumber {
	public static final Logger LOG = LoggerFactory.getLogger(FormatNumber.class);

	/**
	 * Formats the Float number to two decimal places
	 * @param amount
	 * @return String
	 */
	public static String strRoundoff(String amtstr) {
		if (amtstr.equals("") || amtstr.equals(" ")) {
			amtstr += "0.00";
		} else if (amtstr.indexOf(".") > -1) {
			final int strLength = amtstr.length() - amtstr.indexOf(".");

			if (strLength > 2) {
				amtstr = amtstr.substring(0, amtstr.indexOf(".") + 3);
			}
			if (strLength == 2) {
				amtstr += "0";
			}
			if (strLength == 1) {
				amtstr += "00";
			}
		} else {
			amtstr += ".00";
		}

		return amtstr;
	}

	/**
	 * Removes Commas in a given string
	 * @param s
	 * @return String
	 */
	public static String removeComma(final String s) {
		final StringBuffer methodName = new StringBuffer(15);
		final StringTokenizer st = new StringTokenizer(s);
		try {
			if (s == null) {
				return s;
			}

			while (st.hasMoreTokens()) {
				methodName.append(st.nextToken(","));
			}
		}

		catch (final Exception e) {
			LOG.error(" The exception is" + e.getMessage());
			throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());
		}
		return methodName.toString();

	}

	public static StringBuffer numberToString(final String strNumberToConvert) {

		final DecimalFormat dft = new DecimalFormat("##########");
		final String strtemp = "" + dft.format(Double.parseDouble(strNumberToConvert));
		// System.out.println("strtemp:: "+strtemp);
		final StringBuffer strbNumber = new StringBuffer(strtemp);

		try {

			final int intLen = strbNumber.length();
			// System.out.println("strNumberToConvert : "+strbNumber);
			// System.out.println("length is : "+intLen);
			if (intLen == 1 || intLen == 2 || intLen == 3) {
			}

			else if (intLen > 10) {

				throw new Exception();
			}

			else if (intLen == 10) {
				for (int i = 3; i < intLen; i = i + 3) {
					strbNumber.insert(i, ',');
				}
			}

			else if (intLen % 2 == 0) {

				for (int i = 1; i < intLen; i = i + 3) {
					strbNumber.insert(i, ',');
				}
			} else {
				for (int i = 2; i < intLen; i = i + 3) {
					strbNumber.insert(i, ',');
				}

			}

		} catch (final Exception e) {
			LOG.error(" The exception is" + e.getMessage());
			throw new EGOVRuntimeException("Exception occured -----> " + e.getMessage());
		}

		return strbNumber;

	}

	public static void main(final String[] args) {

		if (args.length < 1) {
			// logger.error("Usage:Pls enter a Valid Number <decimal/integer value>");
		} else {
			// System.out.println("Value  is :: "+ args[0]);
			final int intDecimalIndex = args[0].indexOf(".");
			// System.out.println("Decimal Index is :: "+ intDecimalIndex);

			if (intDecimalIndex > 0) {

				final String intStr = args[0].substring(0, intDecimalIndex);
				// System.out.println("intStr is :: "+ intStr);

				args[0].substring(intDecimalIndex);

				FormatNumber.numberToString(intStr);

			}

			else {
				FormatNumber.numberToString(args[0]);
			}

		}
	}
}
