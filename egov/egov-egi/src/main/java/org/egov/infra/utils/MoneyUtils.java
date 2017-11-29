/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.infra.utils;

import org.egov.infra.exception.ApplicationRuntimeException;

import java.math.BigDecimal;

/**
 * Utility methods for money manipulation.
 *
 * @author Sunil D'Monte
 */
public final class MoneyUtils {

    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final int DECIMALS = 0;
    private static final int DECIMALS2 = 2;
    private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    private MoneyUtils() {
        //All static util api's
    }

    /**
     * Splits up an amount into multiple amounts according to the specified
     * weightage.
     * <p>
     * E.g. An amount of 100 and weights (3, 7) will give (30, 70).
     * <p>
     * The algorithm used is the one described at
     * http://nida.se/patterns/poeaa/money.html. It guarantees that the sum of
     * the splits will exactly equal the input amount, even for fractional
     * cases:
     * <p>
     * E.g. An amount of 100 and weights (3, 8) will give (28, 72).
     * <p>
     * In such cases, the individual split amounts might not exactly match the
     * number you'd get from floating-point calculation (27.272 and 72.727 in
     * the example above). With the input amount in paise, the individual split
     * amounts will be at most 1 paisa off.
     * <p>
     * It is the job of the caller to do the "massaging" of rupee amounts into
     * paise amounts, and vice-versa for the output.
     * <p>
     * See the unit tests for more examples.
     *
     * @param amountInPaise Has to be whole number, so if your amount is Rs. 100.342,
     *                      you'll have to convert that to 10034 paise.
     * @param weights       Numbers that specify the weight given to each split. They need
     *                      not add up to 100% (e.g. 3, 3, 4) - any arbitrary weights will
     *                      do. Also note that (2, 10, 16) is identical to (1, 5, 8).
     * @return The split amounts. If no weights or a single weight is input, the
     * input amount will be returned.
     */
    public static long[] allocate(final long amountInPaise, final long[] weights) {
        if (weights.length == 0)
            return new long[]{amountInPaise};
        final long[] splits = new long[weights.length];

        long totalWeight = 0;
        for (final long weight : weights)
            totalWeight += weight;

        if (totalWeight == 0)
            throw new ApplicationRuntimeException("Total of weight can not be zero");

        long remainder = amountInPaise;
        for (int i = 0; i < splits.length; i++) {
            // Compute the allocation using long arithmetic - e.g.
            // 100 * 3 / 7 will give 42, not 42.857
            splits[i] = amountInPaise * weights[i] / totalWeight;
            remainder = remainder - splits[i];
        }

        // Distribute the remainder amongst the splits, one paisa at a time.
        // Note that the
        // remainder will always be in the range [0, number-of-splits]. This is
        // because of the long
        // arithmetic - each allocation computed above will be < 1 off the real
        // value,
        // and there are (number-of-splits) allocations.
        for (int i = 0; i < remainder; i++)
            splits[i] = splits[i] + 1;

        return splits;
    }

    /**
     * This variant of the allocate method allows the original amount to be
     * input as a rupee amount i.e. as a BigDecimal, and receive the output
     * splits as BigDecimal amounts too. If the input amount is more than 2
     * decimal places e.g. Rs. 150.632, it gets rounded to 2 decimal places
     * (using "half-up" rounding) and it is the rounded amount that gets
     * allocated.
     *
     * @param amountInRupees
     * @param weights
     * @return The splits, also as BigDecimals i.e. rupee amounts. Returns null
     * if the input amount is null. If no weights or a single weight is
     * input, the input amount will be returned.
     */
    public static BigDecimal[] allocate(final BigDecimal amountInRupees, final long[] weights) {
        if (amountInRupees == null)
            return new BigDecimal[0];

        final BigDecimal[] resultInRupees = new BigDecimal[weights.length];

        final long amountInPaise = roundAndConvertToPaise(amountInRupees);
        final long[] resultsInPaise = allocate(amountInPaise, weights);

        for (int i = 0; i < resultsInPaise.length; i++)
            resultInRupees[i] = BigDecimal.valueOf(resultsInPaise[i]).divide(HUNDRED);
        return resultInRupees;
    }

    /**
     * Converts a rupee amount in BigDecimal to a paise amount as an integer
     * (long). E.g. 150 -> 15000 150.23 -> 15023 150.234 -> 15023 150.235 ->
     * 15024 150.236 -> 15024
     *
     * @param rupees
     * @return
     */
    public static long roundAndConvertToPaise(BigDecimal rupees) {
        return roundOffTwo(rupees).multiply(HUNDRED).longValueExact();
    }

    /**
     * Round off to two decimal big decimal value.
     *
     * @param amount the amount
     * @return the big decimal
     */
    public static BigDecimal roundOffTwo(final BigDecimal amount) {
        return amount.setScale(DECIMALS2, ROUNDING_MODE);
    }

    /**
     * This method is a utility method, which takes a BigDecimal and rounds that
     * to 0 places.
     *
     * @param amount the amount
     * @return BigDecimal rounded off to 0 places
     */
    public static BigDecimal roundOff(final BigDecimal amount) {
        return amount.setScale(DECIMALS, ROUNDING_MODE);
    }

    public static boolean isInteger(final String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (final NumberFormatException nfe) {
            return false;
        }
    }
}
