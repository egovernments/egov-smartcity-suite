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
import java.text.FieldPosition;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.egov.infra.config.core.LocalizationSettings.currencyName;
import static org.egov.infra.config.core.LocalizationSettings.currencyUnitName;
import static org.egov.infra.utils.ApplicationConstant.WHITESPACE;
import static org.egov.infra.utils.StringUtils.isUnsignedNumber;
import static org.egov.infra.utils.StringUtils.stripExtraSpaces;

/**
 * @deprecated use {@link NumberToWordConverter#amountInWordsWithCircumfix(BigDecimal)} instead.
 */
@Deprecated
public final class NumberToWord {

    private static final long ZEROS = 0;
    private static final long UNITS = 1;
    private static final long TENS = 10 * UNITS;
    private static final long HUNDREDS = 10 * TENS;
    private static final long THOUSANDS = 10 * HUNDREDS;
    private static final long TENTHOUSANDS = 10 * THOUSANDS;
    private static final long LAKHS = 10 * TENTHOUSANDS;
    private static final long TENLAKHS = 10 * LAKHS;
    private static final long CRORES = 10 * TENLAKHS;
    private static final long TENCRORES = 10 * CRORES;
    private static final long HUNDREDCRORES = 10 * TENCRORES;
    private static final long THOUSANDCRORES = 10 * HUNDREDCRORES;
    private static final long TENTHOUSANDCRORES = 10 * THOUSANDCRORES;

    private static final String[] CARDINAL = {"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty", "Thirty",
            "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety", "Hundred"};

    private static final String THOUSAND_CRORES = " Thousand Crore ";
    private static final String HUNDRED_CRORES = " Hundred Crore ";
    private static final String HUNDRED = " Hundred ";
    private static final String THOUSAND = " Thousand ";
    private static final String LAKH = " Lakh ";
    private static final String CRORE = " Crore ";

    private NumberToWord() {
        //Util class with static methods
    }

    public static String amountInWords(Double amount) {
        StringBuffer formattedAmount = new StringBuffer();
        new DecimalFormat("###0.00").format(amount, formattedAmount, new FieldPosition(0));
        return stripExtraSpaces(NumberToWord.convertToWord(formattedAmount.toString()));
    }

    public static String convertToWord(String number) {

        StringBuilder currencyUnitSuffix = new StringBuilder();
        String[] currencyAndUnit = number.split("[.]");
        if (currencyAndUnit.length == 2 && !currencyAndUnit[1].equals("00")) {
            currencyUnitSuffix.append(" and ").append(paiseInWords(currencyAndUnit[1]))
                    .append(WHITESPACE).append(currencyUnitName()).append(" Only");
        }

        String returnValue = translateToWord(currencyAndUnit[0]);

        return currencyUnitSuffix.length() < 1 ? new StringBuilder()
                .append(currencyName()).append(WHITESPACE)
                .append(returnValue).append(" Only").toString()
                : new StringBuilder()
                .append(currencyName()).append(WHITESPACE)
                .append(returnValue).append(currencyUnitSuffix).toString();

    }

    public static String translateToWord(String value) {

        if (!isUnsignedNumber(value))
            throw new ApplicationRuntimeException("Provided value is not a valid number");

        StringBuilder numberInWords = new StringBuilder();
        long number = Long.parseLong(value);
        if (number == ZEROS || value.length() > 12) {
            numberInWords.append(getWord(number));
        }

        Long subNum;
        String numericPart = Long.toString(number);
        while (number > 0 && numericPart.length() < 13) {
            numericPart = Long.toString(number);
            long place = getPlace(numericPart);

            if (place == HUNDREDCRORES || place == THOUSANDCRORES || place == TENTHOUSANDCRORES) {
                subNum = Long.parseLong(Character.toString(numericPart.charAt(0)));
                numberInWords.append(getWord(subNum));
                if (place == HUNDREDCRORES) {
                    number -= subNum * HUNDREDCRORES;
                    if (number == 0) {
                        numberInWords.append(HUNDRED_CRORES);
                    } else {
                        numberInWords.append(HUNDRED);
                    }
                } else if (place == THOUSANDCRORES) {
                    number -= subNum * THOUSANDCRORES;
                    if (number == 0) {
                        numberInWords.append(THOUSAND_CRORES);
                    } else {
                        numberInWords.append(THOUSAND);
                    }
                } else {
                    numberInWords.setLength(0);
                    subNum = Long.parseLong(Character.toString(numericPart.charAt(0)) + numericPart.charAt(1));
                    number -= subNum * THOUSANDCRORES;
                    if (subNum >= 21 && (subNum % 10) != 0 && number == 0) {
                        numberInWords.append(getWord(Long.parseLong(String.valueOf(numericPart.charAt(0))) * 10))
                                .append(WHITESPACE).append(getWord(subNum % 10)).append(THOUSAND_CRORES);
                    } else if (number == 0) {
                        numberInWords.append(getWord(Long.parseLong(String.valueOf(numericPart.charAt(0))) * 10))
                                .append(THOUSAND_CRORES);
                    } else {
                        numberInWords.append(getWord(Long.parseLong(String.valueOf(numericPart.charAt(0))) * 10))
                                .append(WHITESPACE).append(getWord(subNum % 10)).append(THOUSAND);
                    }
                }

            } else if (place == TENS || place == TENTHOUSANDS || place == TENLAKHS || place == TENCRORES) {
                subNum = Long.parseLong(String.valueOf(numericPart.charAt(0)) + String.valueOf(numericPart.charAt(1)));

                if (subNum >= 21 && (subNum % 10) != 0) {
                    numberInWords.append(getWord(Long.parseLong(String.valueOf(numericPart.charAt(0))) * 10))
                            .append(WHITESPACE).append(getWord(subNum % 10));
                } else {
                    numberInWords.append(getWord(subNum));
                }

                if (place == TENS) {
                    number = 0;
                } else if (place == TENTHOUSANDS) {
                    number -= subNum * THOUSANDS;
                    numberInWords.append(THOUSAND);
                } else if (place == TENLAKHS) {
                    number -= subNum * LAKHS;
                    numberInWords.append(LAKH);
                } else if (place == TENCRORES) {
                    number -= subNum * CRORES;
                    numberInWords.append(CRORE);
                }
            } else {
                subNum = Long.parseLong(String.valueOf(numericPart.charAt(0)));
                numberInWords.append(getWord(subNum));
                if (place == UNITS) {
                    number = 0;
                } else if (place == HUNDREDS) {
                    number -= subNum * HUNDREDS;
                    numberInWords.append(HUNDRED);
                } else if (place == THOUSANDS) {
                    number -= subNum * THOUSANDS;
                    numberInWords.append(THOUSAND);
                } else if (place == LAKHS) {
                    number -= subNum * LAKHS;
                    numberInWords.append(LAKH);
                } else if (place == CRORES) {
                    number -= subNum * CRORES;
                    numberInWords.append(CRORE);
                }
            }
        }
        return numberInWords.toString();
    }

    private static long getPlace(String number) {
        switch (number.length()) {
            case 1:
                return UNITS;
            case 2:
                return TENS;
            case 3:
                return HUNDREDS;
            case 4:
                return THOUSANDS;
            case 5:
                return TENTHOUSANDS;
            case 6:
                return LAKHS;
            case 7:
                return TENLAKHS;
            case 8:
                return CRORES;
            case 9:
                return TENCRORES;
            case 10:
                return HUNDREDCRORES;
            case 11:
                return THOUSANDCRORES;
            case 12:
                return TENTHOUSANDCRORES;
            default:
                return ZEROS;
        }
    }

    private static String getWord(Long number) {
        int value = number.intValue();
        if (value < 0)
            throw new ApplicationRuntimeException("Number is out of bound");

        switch (value) {
            case 30:
                return CARDINAL[21];
            case 40:
                return CARDINAL[22];
            case 50:
                return CARDINAL[23];
            case 60:
                return CARDINAL[24];
            case 70:
                return CARDINAL[25];
            case 80:
                return CARDINAL[26];
            case 90:
                return CARDINAL[27];
            case 100:
                return CARDINAL[28];
            default:
                if (value < 21) {
                    return CARDINAL[value];
                } else {
                    return EMPTY;
                }
        }

    }

    private static String paiseInWords(String paise) {

        Long subNum;
        StringBuilder returnValue = new StringBuilder();

        if (paise.length() >= 2) {
            subNum = Long.parseLong(Character.toString(paise.charAt(0)) + paise.charAt(1));
        } else {
            subNum = Long.parseLong(Character.toString(paise.charAt(0)));
        }

        if (subNum >= 21 && (subNum % 10) != 0) {
            returnValue.append(getWord(Long.parseLong(Character.toString(paise.charAt(0))) * 10))
                    .append(WHITESPACE).append(getWord(subNum % 10));

        } else {
            returnValue.append(getWord(subNum));

        }

        return returnValue.toString();
    }
}
