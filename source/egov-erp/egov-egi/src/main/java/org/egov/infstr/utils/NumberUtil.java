/*
 * @(#)NumberUtil.java 3.0, 18 Jun, 2013 12:10:47 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.egov.exceptions.EGOVRuntimeException;

/**
 * Number Utility methods
 */
public class NumberUtil {

	private static final int AMOUNT_PRECISION_DEFAULT = 2;

	public static enum NumberFormatStyle {
		CRORES, MILLIONS
	};

	/**
	 * Converts given amount to words with default decimal precision of 2.
	 * @param amount Amount to be converted to words
	 * @return The amount in words with default decimal precision of 2.
	 */
	public static String amountInWords(final BigDecimal amount) {
		return NumberToWord.amountInWords(amount.doubleValue());
	}

	/**
	 * @param number The number to be formatted
	 * @return The number formatted in Indian (CRORES) style
	 */
	public static String formatNumber(final BigDecimal number) {
		return formatNumber(number, NumberFormatStyle.CRORES);
	}

	/**
	 * @param number Number to be formatted
	 * @param format {@code NumberFormatStyle} in which the number is to be formatted
	 * @return The number formatted in given style (MILLION/CRORES)
	 */
	public static String formatNumber(final BigDecimal number, final NumberFormatStyle format) {
		switch (format) {
		case MILLIONS:
			return formatNumber(number, AMOUNT_PRECISION_DEFAULT, true);
		case CRORES:
			return (Math.abs(number.doubleValue()) >= 100000 ? formatNumberCroreFormat(number) : formatNumber(number, AMOUNT_PRECISION_DEFAULT, true));
		default:
			throw new EGOVRuntimeException("Invalid number format [" + format + "]");
		}
	}

	/**
	 * Formats given number in Indian format (CRORE format). e.g. 1234567890.5 will be formatted as 1,23,45,67,890.50
	 * @param num Number to be formatted
	 * @return The number formatted as per CRORE format
	 */
	private static String formatNumberCroreFormat(final BigDecimal num) {
		final double absAmount = num.abs().doubleValue(); // e.g. 1234567890.5
		final long numLakhs = (long) (absAmount / 100000); // 12345
		final double numThousands = absAmount - (numLakhs * 100000); // 67890.5
		// format first part with 2 digit grouping e.g. 1,23,45,
		final DecimalFormat formatter = new DecimalFormat("#,##");
		final String firstPart = (num.doubleValue() < 0 ? "-" : "") + (numLakhs > 0 ? formatter.format(numLakhs) + "," : "");
		// format second part with three digit grouping and decimal e.g.
		// 67,890.50
		formatter.applyPattern("00,000.00");
		return (firstPart + formatter.format(numThousands));
	}

	/**
	 * Formats a given number with given number of fraction digits <br>
	 * e.g. formatNumber(1000, 2, false) will return 1000.00 <br>
	 * formatNumber(1000, 2, true) will return 1,000.00 <br>
	 * @param number The number to be formatted
	 * @param fractionDigits Number of fraction digits to be used for formatting
	 * @param useGrouping Flag indicating whether grouping is to be used while formatting the number
	 * @return Formatted number with given number of fraction digits
	 */
	public static String formatNumber(final BigDecimal number, final int fractionDigits, final boolean useGrouping) {
		final NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMinimumFractionDigits(fractionDigits);
		numberFormat.setMaximumFractionDigits(fractionDigits);
		numberFormat.setGroupingUsed(useGrouping);
		return numberFormat.format(number.doubleValue());
	}

	/**
	 * Prefix the given value with zero, if the value's length is less than given paddingSize
	 * @param value the value applicable for padding
	 * @param paddingSize the minimum padding size of the value
	 * @return paddedValue
	 */
	public static String prefixZero(final long value, final int paddingSize) {
		final String prefixedValue = String.format("%0" + paddingSize + "d", value);
		return prefixedValue;
	}
}
