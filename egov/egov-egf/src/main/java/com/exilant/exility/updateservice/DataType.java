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
package com.exilant.exility.updateservice;

/*
 * Provides predefined names for data types. These are identical to ones used in PageManager on the cient side
 * Called quite frequently, and can be completely static.
 * All methods are static. constructor is private to ensure that no one instantiates it
 */

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataType {

    // pnemonic for convinience
    private final static Logger LOGGER = Logger.getLogger(DataType.class);
    public static final int ANYCHAR = 0;
    public static final int ALPHA = 1;
    public static final int ALPHANUMERIC = 2;
    public static final int UNSIGNEDINT = 3;
    public static final int SIGNEDINT = 4;
    public static final int UNSIGNEDDECIMAL = 5;
    public static final int SIGNEDDECIMAL = 6;
    public static final int ANYDATE = 7;
    public static final int PASTDATE = 8;
    public static final int FUTUREDATE = 9;
    public static final int EMAIL = 10;
    public static final int BOOLEAN = 11;
    public static final int REGEX = 12;

    public static final Date unknownDate = new Date(0);

    // Allow use of strings instead of numbers
    private static final String[] dataTypes = { "ANYCHAR",
        "ALPHA",
        "ALPHANUMERIC",
        "UNSIGNEDINT",
        "SIGNEDINT",
        "UNSIGNEDDECIMAL",
        "SIGNEDDECIMAL",
        "ANYDATE",
        "PASTDATE",
        "FUTUREDATE",
        "EMAIL",
        "BOOLEAN",
        "REGEX"
    };

    // Regular Expresions for each data type.
    private static final String[] regexStrings = {
        "^.*$",
        "^[a-zA-Z]*$",
        "^\\w*$",
        "^\\+?\\d*$",
        "^[+-]?\\d*$",
        "^\\+?\\d*\\.?\\d*$",
        "^[+-]?\\d*\\.?\\d*$",
        "^\\d\\d?-\\w\\w\\w-\\d\\d\\d\\d$",
        "^\\d\\d?-\\w\\w\\w-\\d\\d\\d\\d$",
        "^\\d\\d?-\\w\\w\\w-\\d\\d\\d\\d$",
        "^.*^", // complex to write, let us do it easily in string comparison
    "^\\w+(\\.?\\w+)?@[\\w-]+(\\.[\\w-]+)*$" };

    // for effeciency, regexes are compiled and saved in static array
    private static final Pattern[] dataTypePatterns = new Pattern[regexStrings.length];
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

    static {
        for (int i = 0; i < regexStrings.length; i++)
            dataTypePatterns[i] = Pattern.compile(regexStrings[i]);
        simpleDateFormat.setLenient(false);
    }

    private DataType() {
        super();
    }

    public static boolean isNumericType(final String dataType) {
        return isNumericType(getTypeInt(dataType));
    }

    public static boolean isNumericType(final int dataType) {
        if (dataType > 2 && dataType < 7)
            return true;
        return false;
    }

    public static boolean isDateType(final String dataType) {
        return isDateType(getTypeInt(dataType));
    }

    public static boolean isDateType(final int dataType) {
        if (dataType > 2 && dataType < 7)
            return true;
        return false;
    }

    public static boolean isValid(final String dataType, final String value) {
        return isValid(getTypeInt(dataType), value);
    }

    public static boolean isValid(final int dataType, String value) {

        if (dataType < 0 || dataType >= dataTypes.length)
            return false; // invalid dataType
        boolean valid;
        if (dataType == DataType.BOOLEAN) {
            value = value.toUpperCase();
            if (value.equals("Yes") || value.equals("NO")
                    || value.equals("TRUE") || value.equals("FALSE")
                    || value.equals("0") || value.equals("1"))
                return true;
            return false;
        }
        final Matcher matcher = dataTypePatterns[dataType].matcher(value);
        valid = matcher.find();
        if (!valid)
            return false;
        // date formats
        if (dataType == DataType.ANYDATE ||
                dataType == DataType.FUTUREDATE || dataType == DataType.PASTDATE) { // it is a date field
            Date date;
            try {
                date = simpleDateFormat.parse(value);
            } catch (final ParseException e) {
                LOGGER.error("Error while simpleDateFormat.parse(value)", e);
                return false;
            }

            // but then it would have parsed 35-may-2003 !!!!
            final Date today = getToday();
            if (dataType == DataType.FUTUREDATE && today.after(date))
                return false;
            if (dataType == DataType.PASTDATE && today.before(date))
                return false;
            return true;
        }
        return true;
    }

    public static int getTypeInt(String type) {
        type = type.toUpperCase();
        for (int i = 0; i < dataTypes.length; i++)
            if (dataTypes[i].equals(type))
                return i;
        return -1; // indicates an invalid datatype string
    }

    public static Date getToday() {
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(simpleDateFormat.format(date));
        } catch (final ParseException e) {
            LOGGER.error("Error in getToday", e);
        }
        return date;
    }

    public static Date getDate(final String value) {
        if (null == value)
            return null;
        try {
            return simpleDateFormat.parse(value);
        } catch (final ParseException e) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("Error in getDate", e);
            return null;
        }
    }

    public static String getDateString(final Date value) {
        return simpleDateFormat.format(value);
    }

}
