/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.infra.utils;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {

    public static final DateTimeFormatter FORMAT_DATE_TO_YEAR = DateTimeFormat.forPattern("yyyy");
    public static final DateTimeFormatter TO_DEFAULT_DATE_FORMAT = DateTimeFormat.forPattern("dd/MM/yyyy");

    public static String currentDateToYearFormat() {
        return toYearFormat(new LocalDate());
    }

    public static String toYearFormat(final LocalDate date) {
        return FORMAT_DATE_TO_YEAR.print(date);
    }

    public static String currentDateToDefaultDateFormat() {
        return toDefaultDateFormat(new LocalDate());
    }

    public static String toDefaultDateFormat(final LocalDate date) {
        return TO_DEFAULT_DATE_FORMAT.print(date);
    }

    public static Date endOfDay(final Date date) {
        return new DateTime(date).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toDate();
    }

    public static DateTime endOfToday() {
        return new DateTime().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
    }

    public static DateTime endOfGivenDate(final DateTime dateTime) {
        return dateTime.withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
    }

    public static DateTime startOfGivenDate(final DateTime dateTime) {
        return dateTime.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
    }

    public static Date startOfDay(final Date date) {
        return new DateTime(date).withTimeAtStartOfDay().toDate();
    }

    public static Date[] getStartAndEndOfDay(final Date startDate, final Date endDate) {
        return new Date[] { startOfDay(startDate), endOfDay(endDate) };
    }

    public static int getNumberOfYearPassesed(final Date startDate, final Date endDate) {
        return new DateTime(endDate).getYear() - new DateTime(startDate).getYear();
    }

    public static int noOfMonths(final Date startDate, final Date endDate) {
        final DateTime sDate = new DateTime(startDate);
        final DateTime eDate = new DateTime(endDate);
        return Months.monthsBetween(sDate.withDayOfMonth(sDate.getDayOfMonth()), eDate.withDayOfMonth(eDate.getDayOfMonth()))
                .getMonths();
    }
    
    public static int noOfDays(final Date startDate, final Date endDate) {
      return (int)( (endDate.getTime() - startDate.getTime())
                / (1000 * 60 * 60 * 24) );
    }
}
