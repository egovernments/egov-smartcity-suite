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

package org.egov.infra.config.core;

import org.joda.time.DateTimeZone;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.TimeZone;

import static java.lang.System.getProperty;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

public final class LocalizationSettings {

    public static final String DEFAULT_TIME_ZONE_KEY = "default.time.zone";
    public static final String DEFAULT_COUNTRY_CODE_KEY = "default.country.code";
    public static final String DEFAULT_CURRENCY_CODE_KEY = "default.currency.code";
    public static final String DEFAULT_CURRENCY_NAME_KEY = "default.currency.name";
    public static final String DEFAULT_CURRENCY_NAME_PLURAL_KEY = "default.currency.name.plural";
    public static final String DEFAULT_CURRENCY_UNIT_NAME_KEY = "default.currency.unit.name";
    public static final String DEFAULT_CURRENCY_UNIT_NAME_PLURAL_KEY = "default.currency.unit.name.plural";
    public static final String DEFAULT_CURRENCY_NAME_SHORT_KEY = "default.currency.name.short";
    public static final String DEFAULT_CURRENCY_SYMBOL_UTF8_KEY = "default.currency.symbol.utf-8";
    public static final String DEFAULT_CURRENCY_SYMBOL_HEX_KEY = "default.currency.symbol.hex";
    public static final String DEFAULT_LOCALE_KEY = "default.locale";
    public static final String DEFAULT_ENCODING_KEY = "default.encoding";
    public static final String DEFAULT_DATE_PATTERN_KEY = "default.date.pattern";
    public static final String DEFAULT_DATE_TIME_PATTERN_KEY = "default.date.time.pattern";

    public static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";
    public static final String DEFAULT_DATE_TIME_PATTERN = "dd/MM/yyyy hh:mm a";
    public static final String DEFAULT_TIME_ZONE = "IST";
    public static final String DEFAULT_COUNTRY_CODE = "91";
    public static final String DEFAULT_CURRENCY_CODE = "INR";
    public static final String DEFAULT_CURRENCY_NAME = "Rupee";
    public static final String DEFAULT_CURRENCY_NAME_PLURAL = "Rupees";
    public static final String DEFAULT_CURRENCY_UNIT_NAME = "Paisa";
    public static final String DEFAULT_CURRENCY_UNIT_NAME_PLURAL = "Paise";
    public static final String DEFAULT_CURRENCY_NAME_SHORT = "Rs.";
    public static final String DEFAULT_CURRENCY_SYMBOL_UTF8 = "\u20B9";
    public static final String DEFAULT_CURRENCY_SYMBOL_HEX = "&#x20b9;";
    public static final String DEFAULT_LOCALE = "en_IN";
    public static final String DEFAULT_ENCODING = "UTF-8";


    private LocalizationSettings() {
        //only static methods
    }

    public static DateTimeZone jodaTimeZone() {
        return DateTimeZone.forTimeZone(timeZone());
    }

    public static TimeZone timeZone() {
        return TimeZone.getTimeZone(defaultIfBlank(getProperty(DEFAULT_TIME_ZONE_KEY), DEFAULT_TIME_ZONE));
    }

    public static String countryCode() {
        return defaultIfBlank(getProperty(DEFAULT_COUNTRY_CODE_KEY), DEFAULT_COUNTRY_CODE);
    }

    public static String currencyCode() {
        return defaultIfBlank(getProperty(DEFAULT_CURRENCY_CODE_KEY), DEFAULT_CURRENCY_CODE);
    }

    public static String currencyName() {
        return defaultIfBlank(getProperty(DEFAULT_CURRENCY_NAME_KEY), DEFAULT_CURRENCY_NAME);
    }

    public static String currencyNamePlural() {
        return defaultIfBlank(getProperty(DEFAULT_CURRENCY_NAME_PLURAL_KEY), DEFAULT_CURRENCY_NAME_PLURAL);
    }

    public static String currencyUnitName() {
        return defaultIfBlank(getProperty(DEFAULT_CURRENCY_UNIT_NAME_KEY), DEFAULT_CURRENCY_UNIT_NAME);
    }

    public static String currencyUnitNamePlural() {
        return defaultIfBlank(getProperty(DEFAULT_CURRENCY_UNIT_NAME_PLURAL_KEY), DEFAULT_CURRENCY_UNIT_NAME_PLURAL);
    }

    public static String currencyNameShort() {
        return defaultIfBlank(getProperty(DEFAULT_CURRENCY_NAME_SHORT_KEY), DEFAULT_CURRENCY_NAME_SHORT);
    }

    public static String currencySymbolUtf8() {
        return defaultIfBlank(getProperty(DEFAULT_CURRENCY_SYMBOL_UTF8_KEY), DEFAULT_CURRENCY_SYMBOL_UTF8);
    }

    public static String currencySymbolHex() {
        return defaultIfBlank(getProperty(DEFAULT_CURRENCY_SYMBOL_HEX_KEY), DEFAULT_CURRENCY_SYMBOL_HEX);
    }

    public static Locale locale() {
        return Locale.forLanguageTag(defaultIfBlank(getProperty(DEFAULT_LOCALE_KEY), DEFAULT_LOCALE));
    }

    public static Charset encoding() {
        return Charset.forName(defaultIfBlank(getProperty(DEFAULT_ENCODING_KEY), DEFAULT_ENCODING));
    }

    public static String datePattern() {
        return defaultIfBlank(getProperty(DEFAULT_DATE_PATTERN_KEY), DEFAULT_DATE_PATTERN);
    }

    public static String dateTimePattern() {
        return defaultIfBlank(getProperty(DEFAULT_DATE_TIME_PATTERN_KEY), DEFAULT_DATE_TIME_PATTERN);
    }
}
