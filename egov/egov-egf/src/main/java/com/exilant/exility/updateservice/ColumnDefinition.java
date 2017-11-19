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

import java.util.Date;

/*
 *
 * @author raghu.bhandi, Exilant Consulting
 *
 * Defines teh attributes of a column in the table.
 *  Object is instantaited and populated with the XML Loader.
 * Used excelusively by ColumnDefinition Class
 */

public class ColumnDefinition {
    public String name;
    public String dataType;
    public boolean isRequired;
    public String min;
    public String max;
    public int maxLength = 0; // 0 inplies no max limit
    public String defaultValue;
    public String descriptionId;

    // DataType has overloaded methods with dataType as string or int.
    // however, int is faster, hence the int is stored in the beginning.
    // Well why the H are we getting String?? Simple. Let prgrammers use string
    // in XML lest they make mistakes..
    // TableDefinition sets this and the other ones after loading the class
    int dataTypeInt = 0;
    int dataSubType = 0; // 0=String/boolean, 1=number, 2 = date
    double dblMin;
    double dblMax;
    boolean requiresQuote = true;

    // Know what? I did optimize the date min/max fields as well, but, before I started
    // testing, I realized that 'today' will be stale if cached..
    // Shoudl we cache that, and change when day passes ?? I will pass that..
    // dateMin and date Max are only the parsed Date objects from min/max String
    Date dateMin;
    Date dateMax;

    public ColumnDefinition() {
        super();
    }

    public boolean isValid(final String value) {
        if (null == value || value.length() == 0) {
            // value not specified. if isRequired, it is invalid
            if (defaultValue == null && isRequired)
                return false;
            return true;
        }

        if (!DataType.isValid(dataTypeInt, value))
            return false;

        if (maxLength != 0 && value.length() > maxLength)
            return false;

        // do type specific validations on min/max etc..
        if (DataType.isNumericType(dataTypeInt)) {
            // we have already validated that the string contains numeric data type.
            // So, unless the routine has a bug, following parse should not genreate exceptions
            if (!isValidNumericField(value))
                return false;
        } else if (DataType.isDateType(dataTypeInt))
            if (!isValidDateField(value))
                return false;

        if (descriptionId != null && descriptionId.length() > 0) {
            // use DescriptionService to validate whether this value is OK
            // valid = (SomeMethod(descriptionId, value) != null);
        }

        return true;
    }

    private boolean isValidNumericField(final String value) {
        if (null == min && null == max)
            return true;
        // instead of getting into teh exact type of value, let us use double
        final double doubleValue = Double.parseDouble(value);
        ;

        if (null != min && doubleValue < dblMin)
            return false;
        if (null != max && doubleValue > dblMax)
            return false;

        return true;
    }

    private boolean isValidDateField(final String value) {

        if (null == dateMin && null == dateMax && dataTypeInt == DataType.ANYDATE)
            return true;

        final Date date = DataType.getDate(value);
        final Date today = DataType.getToday();
        Date mdate;
        // I refactored the maze of conditions, and am not sure whether one of the
        // earlier structures was better than this one..
        // any which way.. it is looking complex..

        // first check lower bound. Lower bound is decided by dateMin and FUTUREDATE
        if (null != dateMin) {
            mdate = dateMin;
            if (dataTypeInt == DataType.FUTUREDATE && dateMin.before(today))
                mdate = today;
            if (date.before(mdate))
                return false;
        } else if (dataTypeInt == DataType.FUTUREDATE && date.before(today))
            return false;

        // check upper bound
        if (null != dateMax) {
            mdate = dateMax;
            if (dataTypeInt == DataType.PASTDATE && dateMax.after(today))
                mdate = today;
            if (date.after(mdate))
                return false;
        } else if (dataTypeInt == DataType.PASTDATE && date.after(today))
            return false;

        return true;
    }

    // optimize is called in the beginning by TableDefinition;
    // note that other routines like isValid are executed only after this one executes..
    public void optimize() {
        dataTypeInt = DataType.getTypeInt(dataType);

        if (DataType.isNumericType(dataTypeInt)) {
            dataSubType = 1;
            if (null != min || null != max) {
                if (null == min)
                    dblMin = Double.MIN_VALUE;
                else
                    dblMin = Double.parseDouble(min);
                if (null == max)
                    dblMax = Double.MAX_VALUE;
                else
                    dblMax = Double.parseDouble(max);
            }
        } else if (DataType.isDateType(dataTypeInt)) {
            dataSubType = 2;
            // DataType.getDate() returns null if max is not parsable
            if (null != max)
                dateMax = DataType.getDate(max);
            if (null != min)
                dateMin = DataType.getDate(min);
        } else
            dataSubType = 0;

        if (dataSubType == 1 || dataTypeInt == DataType.BOOLEAN)
            requiresQuote = false;
        else
            requiresQuote = true;
    }
}
