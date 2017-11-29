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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.text.RandomStringGenerator;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.toEncodedString;
import static org.egov.infra.utils.ApplicationConstant.NA;
import static org.egov.infra.utils.ApplicationConstant.NO;
import static org.egov.infra.utils.ApplicationConstant.UNDERSCORE;
import static org.egov.infra.utils.ApplicationConstant.WHITESPACE;
import static org.egov.infra.utils.ApplicationConstant.YES;
import static org.egov.infra.utils.DateUtils.currentDateToFileNameFormat;
import static org.egov.infra.validation.regex.Constants.UNSIGNED_NUMERIC;

public class StringUtils extends org.apache.commons.lang.StringUtils {

    public static final RandomStringGenerator UNIQUE_STRING_GENERATOR = new RandomStringGenerator.Builder()
            .withinRange('a', 'z').build();

    public static String escapeSpecialChars(final String str) {
        return str.replaceAll("\\s\\s+|\\r\\n", "<br/>").replaceAll("\'", "\\\\'");
    }

    public static String escapeJavaScript(final String str) {
        return StringEscapeUtils.escapeJavaScript(str);
    }

    public static String emptyIfNull(final String value) {
        return value == null ? EMPTY : value;
    }

    public static List<String> toList(final String... values) {
        return Arrays.asList(values);
    }

    public static String encodeString(String string) {
        return toEncodedString(string.getBytes(), Charset.forName("UTF-8"));
    }

    public static String[] listToStringArray(List<String> values) {
        return values.stream().toArray(String[]::new);
    }

    public static String toYesOrNo(boolean value) {
        return value ? YES : NO;
    }

    public static String defaultIfBlank(String value) {
        return defaultIfBlank(value, NA);
    }

    public static String appendTimestamp(String name) {
        return new StringBuilder().append(name).append(UNDERSCORE).append(currentDateToFileNameFormat()).toString();
    }

    public static String append(String value, String append) {
        return new StringBuilder().append(value).append(append).toString();
    }

    public static String prepend(String value, String prepend) {
        return new StringBuilder().append(prepend).append(value).toString();
    }

    public static boolean isUnsignedNumber(String value) {
        return isNotBlank(value) && value.matches(UNSIGNED_NUMERIC);
    }

    public static String stripExtraSpaces(String value) {
        return value.trim().replaceAll("\\s{2,}", WHITESPACE);
    }

    public static String uniqueString(int codePoint) {
        return UNIQUE_STRING_GENERATOR.generate(codePoint);
    }
}
