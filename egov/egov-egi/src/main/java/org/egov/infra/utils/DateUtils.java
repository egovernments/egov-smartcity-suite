/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
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
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isNotBlank;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final String DFT_DATE_FORMAT = "dd/MM/yyyy";
    public static final DateTimeFormatter FORMAT_DATE_TO_YEAR = DateTimeFormat.forPattern("yyyy");
    public static final DateTimeFormatter TO_DEFAULT_DATE_FORMAT = DateTimeFormat.forPattern(DFT_DATE_FORMAT);
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

    public static String toYearFormat(final LocalDate date) {
        return FORMAT_DATE_TO_YEAR.print(date);
    }

    public static String toYearFormat(final Date date) {
        return FORMAT_DATE_TO_YEAR.print(new LocalDate(date));
    }

    public static String currentDateToDefaultDateFormat() {
        return toDefaultDateFormat(new LocalDate());
    }

    public static String toDefaultDateFormat(final LocalDate date) {
        return TO_DEFAULT_DATE_FORMAT.print(date);
    }

    public static String toDefaultDateFormat(final Date date) {
        return toDefaultDateFormat(new LocalDate(date));
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
        return new Date[]{startOfDay(startDate), endOfDay(endDate)};
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
        return (int) ((endDate.getTime() - startDate.getTime())
                / (1000 * 60 * 60 * 24));
    }

    public static int noOfYears(final Date startDate, final Date endDate) {
        final DateTime sDate = new DateTime(startDate);
        final DateTime eDate = new DateTime(endDate);
        Years years = Years.yearsBetween(sDate, eDate);
        return years.getYears();
    }

    /**
     * Adds given number of days/months/years to given date and returns the
     * resulting date.
     *
     * @param inputDate
     *            Input date
     * @param addType
     *            type to be added
     *                  (Calendar.DAY_OF_MONTH/Calendar.MONTH/Calendar.YEAR)
     * @param addAmount
     *            Number of days/months/years to be added to the input date
     * @return Date after adding given number of days/months/years to the input
     * date
     */
    public static Date add(final Date inputDate, final int addType, final int addAmount) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.add(addType, addAmount);
        return calendar.getTime();
    }

    /**
     * This method will return true<br/>
     * if any of the given date is null or firstDate comes before the secondDate
     * or both dates are same<br/>
     * will return false<br/>
     * if firstDate comes after secondDate
     *
     * @param firstDate
     * @param secondDate
     * @return boolean
     */
    public static boolean compareDates(final Date firstDate, final Date secondDate) {
        return firstDate == null || secondDate == null ? true : firstDate.before(secondDate) ? false : true;
    }

    /**
     * Constructs the Date range for the given From Date and To Date The given
     * dates will change like following<br/>
     * From date will construct time as 0:0:0 AM<br/>
     * To Date will construct time as To Date + 1 [one day advance] 0:0:0.
     *
     * @param fromDate
     *            Date
     * @param toDate
     *            Date. return Date[] converted Date String values of From Date
     *                 and To Date
     * @return the java.util. date[]
     */
    public static Date[] constructDateRange(final Date fromDate, final Date toDate) {
        final Date[] dates = new Date[2];
        final Calendar calfrom = Calendar.getInstance();
        calfrom.setTime(fromDate);
        calfrom.set(Calendar.HOUR, 0);
        calfrom.set(Calendar.MINUTE, 0);
        calfrom.set(Calendar.SECOND, 0);
        calfrom.set(Calendar.AM_PM, Calendar.AM);
        dates[0] = calfrom.getTime();
        final Calendar calto = Calendar.getInstance();
        calto.setTime(toDate);
        calto.set(Calendar.HOUR, 0);
        calto.set(Calendar.MINUTE, 0);
        calto.set(Calendar.SECOND, 0);
        calto.add(Calendar.DAY_OF_MONTH, 1);
        dates[1] = calto.getTime();
        return dates;
    }

    /**
     * Constructs the Date range for the given From Date and To Date value using
     * default Date Range formatting using DATEFORMATTER value.<br/>
     * The given dates will change like following<br/>
     * From date will construct time as 0:0:0 AM<br/>
     * To Date will construct time as To Date + 1 [one day advance] 0:0:0.
     *
     * @param fromDate
     *            String
     * @param toDate
     *            String. return Date[] converted Date String values of From
     *                 Date and To Date
     * @return the java.util. date[]
     * @throws ParseException
     *             the parse exception
     */
    public static Date[] constructDateRange(final String fromDate, final String toDate) throws ParseException {
        return constructDateRange(getDateFormatter(DFT_DATE_FORMAT).parse(fromDate),
                getDateFormatter(DFT_DATE_FORMAT).parse(toDate));
    }

    /**
     * Creates the date.
     *
     * @param year
     *            the year
     * @return the java.util. date
     */
    public static Date createDate(final int year) {
        final Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, 0);
        date.set(Calendar.DATE, 1);
        return date.getTime();
    }

    /**
     * Gets the all months.
     *
     * @return the all months
     */
    public static Map<Integer, String> getAllMonths() {
        final Map<Integer, String> monthMap = new HashMap<>();
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

    /**
     * Gets all months with full names.
     *
     * @return all months
     */
    public static Map<Integer, String> getAllMonthsWithFullNames() {
        final Map<Integer, String> monthMap = new HashMap<>();
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

    /**
     * Get the java.util.Date for the given date string value in given pattern.
     **/
    public static Date getDate(final String date, final String pattern) {
        try {
            return isNotBlank(date) && isNotBlank(pattern) ? getDateFormatter(pattern).parse(date) : null;
        } catch (final Exception e) {
            throw new ApplicationRuntimeException("Date or Pattern value is not valid", e);
        }
    }

    /**
     * Gets the date.
     *
     * @param year
     *            the year
     * @param month
     *            the month
     * @param date
     *            the date
     * @return date object representing given year, month and date
     */
    public static Date getDate(final int year, final int month, final int date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        return calendar.getTime();
    }

    /**
     * Will format the given Date by dd/MM/yyyy pattern.
     *
     * @param date
     *            the date
     * @return the default formatted date
     */
    public static String getDefaultFormattedDate(final Date date) {
        return getDateFormatter(DFT_DATE_FORMAT).format(date);
    }

    /**
     * Will format the given Date by given pattern.
     *
     * @param date
     *            the date
     * @param pattern
     *            the pattern
     * @return the formatted date
     */
    public static String getFormattedDate(final Date date, final String pattern) {
        return getDateFormatter(pattern).format(date);
    }

    /**
     * Now.
     *
     * @return Date object representing current time
     */
    public static Date now() {
        return new Date();
    }

    /**
     * Today.
     *
     * @return Date object representing today
     */
    public static Date today() {
        final Calendar calendar = Calendar.getInstance();
        return getDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Tomorrow.
     *
     * @return Date object representing tomorrow
     */
    public static Date tomorrow() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * Converts the given Date to word representation <br/>
     * eg: for 08/12/2008 date converted in to Eighth December Two Thousand
     * Eight<br/>
     * .
     *
     * @param dateToConvert
     *            the date to convert
     * @return String word rep of date
     */
    public static String convertToWords(final Date dateToConvert) {
        /** Word representation for dates */

        final Calendar cal = Calendar.getInstance();
        cal.setTime(dateToConvert);
        final StringBuilder dateInWord = new StringBuilder();
        dateInWord.append(DATE_IN_WORDS[cal.get(Calendar.DATE) - 1]).append(' ');
        dateInWord.append(getDateFormatter("dd-MMMMM-yyyy").format(dateToConvert).split("-")[1]).append(' ');
        dateInWord.append(NumberToWord.translateToWord(String.valueOf(cal.get(Calendar.YEAR))));
        return dateInWord.toString();
    }

    /**
     * Gets the date formatter.
     *
     * @param pattern
     *            the pattern
     * @return the date formatter This is not threadsafe
     */
    public static SimpleDateFormat getDateFormatter(final String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault());
    }

    /**
     * Checks if the given date is between the 2 dates
     * @param date
     * @param fromDate
     * @param toDate
     * @return boolean
     */
    public static boolean between(final Date date, final Date fromDate, final Date toDate) {
        return (date.after(fromDate) || date.equals(fromDate)) && date.before(toDate) || date.equals(toDate);
    }

    /**
     * Gets all months for a financial year with full names.
     *
     * @return all months
     */
    public static Map<Integer, String> getAllFinancialYearMonthsWithFullNames() {
        final Map<Integer, String> monthMap = new HashMap<>();
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
}
