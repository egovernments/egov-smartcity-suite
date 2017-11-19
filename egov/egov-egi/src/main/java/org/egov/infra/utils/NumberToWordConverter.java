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

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.egov.infra.config.core.LocalizationSettings.currencyName;
import static org.egov.infra.config.core.LocalizationSettings.currencyNamePlural;
import static org.egov.infra.config.core.LocalizationSettings.currencyUnitName;
import static org.egov.infra.config.core.LocalizationSettings.currencyUnitNamePlural;
import static org.egov.infra.utils.ApplicationConstant.WHITESPACE;

public final class NumberToWordConverter {

    private static final String ZERO = " Zero ";
    private static final String HUNDRED = " Hundred ";
    private static final String THOUSAND = " Thousand ";
    private static final String LAKH = " Lakh ";
    private static final String CRORE = " Crore ";

    private static final String[] WORDS_FOR_MULTIPLES_OF_TENS = {"Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty",
            "Seventy", "Eighty", "Ninety"};
    private static final String[] WORDS_FOR_TENS = {"Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
            "Seventeen", "Eighteen", "Nineteen"};
    private static final String[] WORDS_FOR_NUMBER = {"One", "Two", "Three", "Four", "Five", "Six", "Seven",
            "Eight", "Nine"};

    private NumberToWordConverter() {
        //Only static API's
    }

    public static String amountInWordsWithCircumfix(BigDecimal amount) {
        return numberToWords(amount, true, true);
    }

    public static String numberToWords(BigDecimal number, boolean prefix, boolean suffix) {
        StringBuilder numberInWords = new StringBuilder();
        if (prefix) {
            if (number.intValue() < 2) {
                numberInWords.append(currencyName()).append(WHITESPACE).append(convertToWords(number));
            } else {
                numberInWords.append(currencyNamePlural()).append(WHITESPACE).append(convertToWords(number));
            }
        } else {
            numberInWords.append(convertToWords(number));
        }

        if (suffix) {
            numberInWords.append(" Only");
        }
        return numberInWords.toString();
    }

    private static String convertToWords(BigDecimal value) {
        BigDecimal givenNumber = value;
        boolean negativeNumber = givenNumber.signum() == -1;
        if (negativeNumber) {
            givenNumber = givenNumber.abs();
        }

        String numberString = givenNumber.setScale(2, RoundingMode.HALF_UP).toPlainString();
        StringBuilder numberInWord = convertIntegerPartToWord(Double.parseDouble(numberString));

        if (numberInWord.toString().trim().length() == 0) {
            numberInWord.append(ZERO);
        }

        String result = numberInWord.toString().trim();
        if (negativeNumber) {
            result = "Minus " + result;
        }

        return result;

    }

    private static StringBuilder convertIntegerPartToWord(double number) {
        StringBuilder word = new StringBuilder();
        int quotient = (int) (number / 10000000);
        if (quotient > 0) {
            word.append(convertToWords(new BigDecimal(quotient))).append(CRORE);
        }

        number = number % 10000000;
        quotient = (int) (number / 100000);
        if (quotient > 0) {
            word.append(numberToWord(quotient)).append(LAKH);
        }

        number = number % 100000;
        quotient = (int) (number / 1000);
        if (quotient > 0) {
            word.append(numberToWord(quotient)).append(THOUSAND);
        }

        number = number % 1000;
        quotient = (int) (number / 100);
        if (quotient > 0) {
            word.append(numberToWord(quotient)).append(HUNDRED);
        }

        number = number % 100;
        if (number != 0) {
            word.append(numberToWord((int) number)).append(WHITESPACE);
        }

        convertFractionalPartToWord(word, number);

        return word;
    }

    private static void convertFractionalPartToWord(StringBuilder word, double number) {
        int fractionalPart;
        if (number % 1 != 0) {
            String fraction = Double.toString(number).split("\\.")[1];
            if (fraction.length() > 2) {
                fractionalPart = Integer.parseInt(fraction.substring(0, 2));
                if (Integer.parseInt(fraction.substring(2, 3)) > 5) {
                    fractionalPart++;
                }
            } else {
                fractionalPart = Integer.parseInt(fraction);
            }
            if (fraction.length() == 1) {
                fractionalPart *= 10;
            }
            if (word.toString().trim().length() > 0) {
                word.append(("and "));
            }

            word.append(numberToWord(fractionalPart));

            if (fractionalPart <= 1) {
                word.append(WHITESPACE).append(currencyUnitName());
            } else {
                word.append(WHITESPACE).append(currencyUnitNamePlural());
            }
        }
    }

    private static String numberToWord(int number) {
        int quotient = (number / 10);
        StringBuilder word = new StringBuilder();
        if (quotient > 0) {
            if (quotient == 1 && (number % 10) > 0) {
                word.append(WORDS_FOR_TENS[(number % 10) - 1]);
                return word.toString();
            }
            word.append(WORDS_FOR_MULTIPLES_OF_TENS[quotient - 1]);
        }
        int remainder = number % 10;
        if (remainder > 0) {
            if (word.length() > 0) {
                word.append(WHITESPACE);
            }
            word.append(WORDS_FOR_NUMBER[remainder - 1]);
        }
        return word.toString();
    }
}
