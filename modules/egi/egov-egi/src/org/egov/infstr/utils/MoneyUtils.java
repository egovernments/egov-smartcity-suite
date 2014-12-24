/*
 * @(#)MoneyUtils.java 3.0, 18 Jun, 2013 12:07:16 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.infstr.utils;

import java.math.BigDecimal;

/**
 * Utility methods for money manipulation.
 * @author Sunil D'Monte
 */
public class MoneyUtils {

	private static final BigDecimal HUNDRED = new BigDecimal("100");

	/**
	 * Splits up an amount into multiple amounts according to the specified weightage.
	 * <P>
	 * E.g. An amount of 100 and weights (3, 7) will give (30, 70).
	 * <P>
	 * The algorithm used is the one described at http://nida.se/patterns/poeaa/money.html. 
	 * It guarantees that the sum of the splits will exactly equal the input amount, even for fractional cases:
	 * <P>
	 * E.g. An amount of 100 and weights (3, 8) will give (28, 72).
	 * <P>
	 * In such cases, the individual split amounts might not exactly match the number you'd get 
	 * from floating-point calculation (27.272 and 72.727 in the example above). 
	 * With the input amount in paise, the individual split amounts will be at most 1 paisa off.
	 * <P>
	 * It is the job of the caller to do the "massaging" of rupee amounts into paise amounts, and vice-versa for the output.
	 * <P>
	 * See the unit tests for more examples.
	 * @param amountInPaise Has to be whole number, so if your amount is Rs. 100.342, you'll have to convert that to 10034 paise.
	 * @param weights Numbers that specify the weight given to each split. 
	 * They need not add up to 100% (e.g. 3, 3, 4) - any arbitrary weights will do. 
	 * Also note that (2, 10, 16) is identical to (1, 5, 8).
	 * @return The split amounts. If no weights or a single weight is input, the input amount will be returned.
	 */
	public static long[] allocate(long amountInPaise, long[] weights) {
		if (weights.length == 0) {
			return new long[] { amountInPaise };
		}
		long[] splits = new long[weights.length];

		long totalWeight = 0;
		for (long w : weights) {
			totalWeight = totalWeight + w;
		}

		long remainder = amountInPaise;
		for (int i = 0; i < splits.length; i++) {
			// Compute the allocation using long arithmetic - e.g.
			// 100 * 3 / 7 will give 42, not 42.857
			splits[i] = amountInPaise * weights[i] / totalWeight;
			remainder = remainder - splits[i];
		}

		// Distribute the remainder amongst the splits, one paisa at a time. Note that the
		// remainder will always be in the range [0, number-of-splits]. This is because of the long
		// arithmetic - each allocation computed above will be < 1 off the real value,
		// and there are (number-of-splits) allocations.
		for (int i = 0; i < remainder; i++) {
			splits[i] = splits[i] + 1;
		}

		return splits;
	}

	/**
	 * This variant of the allocate method allows the original amount to be input as a rupee amount i.e. as a BigDecimal, 
	 * and receive the output splits as BigDecimal amounts too. 
	 * If the input amount is more than 2 decimal places e.g. Rs. 150.632, it gets rounded to 2 decimal places (using "half-up"
	 * rounding) and it is the rounded amount that gets allocated.
	 * @param amountInRupees
	 * @param weights
	 * @return The splits, also as BigDecimals i.e. rupee amounts. Returns null if the input amount is null. 
	 * If no weights or a single weight is input, the input amount will be returned.
	 */
	public static BigDecimal[] allocate(BigDecimal amountInRupees, long[] weights) {
		if (amountInRupees == null) {
			return null;
		}

		BigDecimal[] resultInRupees = new BigDecimal[weights.length];

		long amountInPaise = roundAndConvertToPaise(amountInRupees);
		long[] resultsInPaise = allocate(amountInPaise, weights);

		for (int i = 0; i < resultsInPaise.length; i++) {
			resultInRupees[i] = BigDecimal.valueOf(resultsInPaise[i]).divide(HUNDRED);
		}
		return resultInRupees;
	}

	/**
	 * Converts a rupee amount in BigDecimal to a paise amount as an integer (long). 
	 * E.g. 150 -> 15000 150.23 -> 15023 150.234 -> 15023 150.235 -> 15024 150.236 -> 15024
	 * @param rupees
	 * @return
	 */
	static long roundAndConvertToPaise(BigDecimal rupees) {
		rupees = EgovUtils.roundOffTwo(rupees);
		long amountInPaise = rupees.multiply(HUNDRED).longValueExact();
		return amountInPaise;
	}

}
