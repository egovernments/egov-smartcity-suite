/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.infra.utils;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.infra.config.core.GlobalSettings.datePattern;
import static org.egov.infra.config.core.GlobalSettings.dateTimePattern;
import static org.egov.infra.config.core.GlobalSettings.jodaTimeZone;
import static org.egov.infra.config.core.GlobalSettings.locale;

public final class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static final String DEFAULT_YEAR_PATTERN = "yyyy";
    private static final String FILE_NAME_DATE_PATTERN = "yyyyMMddhhmm";
    private static final Map<String, DateTimeFormatter> DATE_FORMATTER_HOLDER = new ConcurrentHashMap<>(3);

    private static final String[] DATE_IN_WORDS = {
            "First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth", "Ninth", "Tenth", "Eleventh",
            "Twelfth", "Thirteenth", "Fourteenth", "Fifteenth", "Sixteenth", "Seventeenth", "Eighteenth", "Nineteenth",
            "Twentieth", "Twenty first", "Twenty second", "Twenty third", "Twenty fourth", "Twenty fifth", "Twenty sixth",
            "Twenty seventh", "Twenty eighth", "Twenty ninth", "Thirtieth", "Thirty first"
    };

    private DateUtils() {
        //Should not be initialized
    }

    public static String currentDateToYearFormat() {
        return toYearFormat(new LocalDate());
    }

    public static String toYearFormat(LocalDate date) {
        return formatter(DEFAULT_YEAR_PATTERN).print(date);
    }

    public static String toYearFormat(Date date) {
        return toYearFormat(new LocalDate(date));
    }

    public static String currentDateToDefaultDateFormat() {
        return toDefaultDateFormat(new LocalDate());
    }

    public static String toDefaultDateFormat(LocalDate date) {
        return formatter(datePattern()).print(date);
    }

    public static String toDefaultDateFormat(Date date) {
        return toDefaultDateFormat(new LocalDate(date));
    }

    public static DateTime toDateTimeUsingDefaultPattern(String date) {
        return formatter(datePattern()).parseDateTime(date);
    }

    public static Date toDateUsingDefaultPattern(String date) {
        return toDateTimeUsingDefaultPattern(date).toDate();
    }

    public static String toDefaultDateTimeFormat(Date date) {
        return formatter(dateTimePattern()).print(new DateTime(date, jodaTimeZone()));
    }

    public static String currentDateToFileNameFormat() {
        return formatter(FILE_NAME_DATE_PATTERN).print(new DateTime(now(), jodaTimeZone()));
    }

    public static Date endOfDay(Date date) {
        return endOfGivenDate(new DateTime(date)).toDate();
    }

    public static DateTime endOfToday() {
        return endOfGivenDate(new DateTime());
    }

    public static DateTime endOfGivenDate(DateTime dateTime) {
        return dateTime.millisOfDay().withMaximumValue();
    }

    public static DateTime startOfGivenDate(DateTime dateTime) {
        return dateTime.withTimeAtStartOfDay();
    }

    public static Date startOfDay(Date date) {
        return startOfGivenDate(new DateTime(date)).toDate();
    }

    public static int noOfMonthsBetween(Date startDate, Date endDate) {
        return monthsBetween(new LocalDate(startDate), new LocalDate(endDate));
    }

    public static int monthsBetween(LocalDate startDate, LocalDate endDate) {
        return Months.monthsBetween(startDate, endDate).getMonths();
    }

    public static int daysBetween(Date startDate, Date endDate) {
        return daysBetween(new LocalDate(startDate), new LocalDate(endDate));
    }

    public static int daysBetween(LocalDate startDate, LocalDate endDate) {
        return Days.daysBetween(startDate, endDate).getDays();
    }

    public static int noOfYearsBetween(Date startDate, Date endDate) {
        return Years.yearsBetween(new LocalDate(startDate), new LocalDate(endDate)).getYears();
    }

    public static Date add(Date inputDate, int addType, int addAmount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.add(addType, addAmount);
        return calendar.getTime();
    }

    public static boolean compareDates(Date firstDate, Date secondDate) {
        return firstDate == null || secondDate == null || !firstDate.before(secondDate);
    }

    public static Date[] constructDateRange(Date fromDate, Date toDate) {
        Date[] dates = new Date[2];
        Calendar calfrom = Calendar.getInstance();
        calfrom.setTime(fromDate);
        calfrom.set(Calendar.HOUR, 0);
        calfrom.set(Calendar.MINUTE, 0);
        calfrom.set(Calendar.SECOND, 0);
        calfrom.set(Calendar.AM_PM, Calendar.AM);
        dates[0] = calfrom.getTime();
        Calendar calto = Calendar.getInstance();
        calto.setTime(toDate);
        calto.set(Calendar.HOUR, 0);
        calto.set(Calendar.MINUTE, 0);
        calto.set(Calendar.SECOND, 0);
        calto.add(Calendar.DAY_OF_MONTH, 1);
        dates[1] = calto.getTime();
        return dates;
    }

    public static Date createDate(int year) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, 0);
        date.set(Calendar.DATE, 1);
        return date.getTime();
    }

    public static Map<Integer, String> getAllMonths() {
        Map<Integer, String> monthMap = new HashMap<>();
        monthMap.put(1, "Jan");
        monthMap.put(2, "Feb");
        monthMap.put(3, "Mar");
        monthMap.put(4, "Apr");
        monthMap.put(5, "May");
        monthMap.put(6, "Jun");
        monthMap.put(7, "July");
        monthMap.put(8, "Aug");
        monthMap.put(9, "Sep");
        monthMap.put(10, "Oct");
        monthMap.put(11, "Nov");
        monthMap.put(12, "Dec");
        return monthMap;
    }

    public static Map<Integer, String> getAllMonthsWithFullNames() {
        Map<Integer, String> monthMap = new HashMap<>();
        monthMap.put(1, "January");
        monthMap.put(2, "Feburary");
        monthMap.put(3, "March");
        monthMap.put(4, "April");
        monthMap.put(5, "May");
        monthMap.put(6, "June");
        monthMap.put(7, "July");
        monthMap.put(8, "August");
        monthMap.put(9, "September");
        monthMap.put(10, "October");
        monthMap.put(11, "November");
        monthMap.put(12, "December");
        return monthMap;
    }

    public static Date getDate(String date, String pattern) {
        try {
            return isNotBlank(date) && isNotBlank(pattern) ? getDateFormatter(pattern).parse(date) : null;
        } catch (Exception e) {
            throw new ApplicationRuntimeException("Date or Pattern value is not valid", e);
        }
    }

    public static Date getDate(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        return calendar.getTime();
    }

    public static String getDefaultFormattedDate(Date date) {
        return toDefaultDateFormat(date);
    }

    public static String getFormattedDate(Date date, String pattern) {
        return formatter(pattern).print(new DateTime(date));
    }

    public static Date now() {
        return new Date();
    }

    public static Date today() {
        Calendar calendar = Calendar.getInstance();
        return getDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static Date tomorrow() {
        return new DateTime().plusDays(1).toDate();
    }

    public static String convertToWords(Date dateToConvert) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateToConvert);
        StringBuilder dateInWord = new StringBuilder();
        dateInWord.append(DATE_IN_WORDS[cal.get(Calendar.DATE) - 1]).append(' ');
        dateInWord.append(formatter("dd-MMMMM-yyyy").print(new DateTime(dateToConvert)).split("-")[1]).append(' ');
        dateInWord.append(NumberToWord.translateToWord(String.valueOf(cal.get(Calendar.YEAR))));
        return dateInWord.toString();
    }

    public static boolean between(Date date, Date fromDate, Date toDate) {
        return (date.after(fromDate) || date.equals(fromDate)) && date.before(toDate) || date.equals(toDate);
    }

    public static Map<Integer, String> getAllFinancialYearMonthsWithFullNames() {
        Map<Integer, String> monthMap = new HashMap<>();
        monthMap.put(1, "April");
        monthMap.put(2, "May");
        monthMap.put(3, "June");
        monthMap.put(4, "July");
        monthMap.put(5, "August");
        monthMap.put(6, "September");
        monthMap.put(7, "October");
        monthMap.put(8, "November");
        monthMap.put(9, "December");
        monthMap.put(10, "January");
        monthMap.put(11, "Feburary");
        monthMap.put(12, "March");
        return monthMap;
    }

    public static SimpleDateFormat getDateFormatter(String pattern) {
        return new SimpleDateFormat(pattern, locale());
    }

    public static DateTimeFormatter defaultDateFormatter() {
        return formatter(datePattern());
    }

    public static DateTimeFormatter formatter(String pattern) {
        DateTimeFormatter formatter = DATE_FORMATTER_HOLDER.get(pattern);
        if (formatter == null) {
            formatter = DateTimeFormat.forPattern(pattern).withLocale(locale());
            DATE_FORMATTER_HOLDER.putIfAbsent(pattern, formatter);
        }
        return formatter;
    }
}
