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
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtil {

    private static final int AMOUNT_PRECISION_DEFAULT = 2;

    /**
     * Converts given amount to words with default decimal precision of 2.
     *
     * @param amount Amount to be converted to words
     * @return The amount in words with default decimal precision of 2.
     */
    public static String amountInWords(final BigDecimal amount) {
        return NumberToWordConverter.amountInWordsWithCircumfix(amount);
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
                throw new ApplicationRuntimeException("Invalid number format [" + format + "]");
        }
    }

    /**
     * Formats given number in Indian format (CRORE format). e.g. 1234567890.5 will be formatted as 1,23,45,67,890.50
     *
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
     *
     * @param number         The number to be formatted
     * @param fractionDigits Number of fraction digits to be used for formatting
     * @param useGrouping    Flag indicating whether grouping is to be used while formatting the number
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
     *
     * @param value       the value applicable for padding
     * @param paddingSize the minimum padding size of the value
     * @return paddedValue
     */
    public static String prefixZero(final long value, final int paddingSize) {
        return String.format("%0" + paddingSize + "d", value);
    }

    public enum NumberFormatStyle {
        CRORES, MILLIONS
    }
}
