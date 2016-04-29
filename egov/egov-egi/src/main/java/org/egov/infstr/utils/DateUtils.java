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
package org.egov.infstr.utils;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.NumberToWord;

/**
 * The Class DateUtils. Utility API's for Date related operation
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {

    /** The Constant DATE_FRMTR_CACHE. */
    private static final WeakHashMap<String, SimpleDateFormat> DATE_FRMTR_CACHE = new WeakHashMap<String, SimpleDateFormat>();
    public static final String DFT_DATE_FORMAT = "dd/MM/yyyy";

    /**
     * Adds given number of days/months/years to given date and returns the
     * resulting date.
     * 
     * @param inputDate
     *            Input date
     * @param addType
     *            type to be added
     *            (Calendar.DAY_OF_MONTH/Calendar.MONTH/Calendar.YEAR)
     * @param addAmount
     *            Number of days/months/years to be added to the input date
     * @return Date after adding given number of days/months/years to the input
     *         date
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
     *            and To Date
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
     *            Date and To Date
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
        final Map<Integer, String> monthMap = new HashMap<Integer, String>();
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
        final Map<Integer, String> monthMap = new HashMap<Integer, String>();
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
     * Gets the financial year.
     * 
     * @return the financial year
     */
    public static FinancialYear getFinancialYear() {
        FinancialYear obj = null;
        final java.sql.Date date = new java.sql.Date(new Date().getTime());
        String end = "";
        String firstd = " ";
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final String firsty = Integer.toString(calendar.get(Calendar.YEAR));
        final String endy = Integer.toString(getSecondYear(date));
        if (calendar.get(Calendar.YEAR) > getSecondYear(date)) {
            firstd = endy + "-04-01";// firstDate
            end = firsty + "-03-31";// endDate
        } else {
            firstd = firsty + "-04-01";
            end = endy + "-03-31";
        }
        final java.sql.Date first = java.sql.Date.valueOf(firstd);
        final java.sql.Date second = java.sql.Date.valueOf(end);
        obj = new FinancialYearImpl(first, second, Integer.valueOf(firsty));
        return obj;
    }

    /**
     * Gets the second year.
     * 
     * @param date
     *            the date
     * @return the second year
     */
    private static int getSecondYear(final java.sql.Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int currenty = calendar.get(Calendar.YEAR);
        int Year2 = 0;
        final int currentm = calendar.get(Calendar.MONTH);
        final int lstmonth = Calendar.APRIL;
        if (currentm >= lstmonth)
            Year2 = currenty + 1;
        else
            Year2 = currenty - 1;
        return Year2;
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
     * Returns the last financial year.
     * 
     * @return the last fin year
     */
    public static Integer getLastFinYear() {
        final Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) - 1;
    }

    /**
     * Returns the last financial year.
     * 
     * @return the last fin year start date
     */
    public static Date getLastFinYearStartDate() {
        final Calendar calendar = Calendar.getInstance();
        int lastYear = 0;
        if (calendar.get(Calendar.MONTH) >= 3)
            lastYear = calendar.get(Calendar.YEAR) - 1;
        else
            lastYear = calendar.get(Calendar.YEAR) - 2;
        return new GregorianCalendar(lastYear, 3, 1).getTime();
    }

    /**
     * Returns the number of months between the the 2 given dates.
     * 
     * @param startDate
     *            the start date
     * @param endDate
     *            the end date
     * @return the number of months
     * @author Sapna
     * @return
     */
    public static int getNumberOfMonths(final Date startDate, final Date endDate) {
        assert startDate.before(endDate);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        final int startMonth = calendar.get(Calendar.MONTH) + 1;
        final int startYear = calendar.get(Calendar.YEAR);
        calendar.setTime(endDate);
        int endMonth = calendar.get(Calendar.MONTH) + 1;
        final int endYear = calendar.get(Calendar.YEAR);
        int diffMonth = 0;
        if (startYear < endYear)
            endMonth += (endYear - startYear) * 12;
        diffMonth = endMonth - startMonth;
        if (startDate.getTime() > endDate.getTime())
            diffMonth--;
        // The payment date is 31st or 30th of that month decrement by 1
        if (endDate.getTime() >= startDate.getTime())
            diffMonth--;
        return diffMonth;
    }

    /**
     * Returns the number of months between passed in the current financial year
     * starting April.
     * 
     * @return the number of months passedin curr fin year
     * @return
     */
    public static int getNumberOfMonthsPassedinCurrFinYear() {
        final Calendar calendar = Calendar.getInstance();
        final int startMonth = calendar.get(Calendar.MONTH);
        return startMonth < 3 ? startMonth + 9 : startMonth - 3;
    }

    /**
     * This method returns the number of months passesed in between dates.
     * 
     * @param fromDate
     *            the from date
     * @param toDate
     *            the to date
     * @return the number of months passes in year
     * @return
     */
    public static int getNumberOfMonthsPassesInYear(final Date fromDate, final Date toDate) {
        final Calendar cal1 = Calendar.getInstance();
        final Calendar cal2 = Calendar.getInstance();
        cal1.setTime(fromDate);
        cal2.setTime(toDate);
        final int yearDiff = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
        return yearDiff * 12 + cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
    }

    /**
     * This method returns the number of years passesed in between dates.
     * 
     * @param fromDate
     *            the from date
     * @param toDate
     *            the to date
     * @return the number of year passesed
     * @return
     */
    public static int getNumberOfYearPassesed(final Date fromDate, final Date toDate) {
        final Calendar cal1 = Calendar.getInstance();
        final Calendar cal2 = Calendar.getInstance();
        cal1.setTime(fromDate);
        cal2.setTime(toDate);
        return cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
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
     * Yesterday
     * 
     * @return Date object representing yesterday
     */
    public static Date yesterday() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * Checks the date is in dd/mm/yyyy format.
     * 
     * @param strDate
     *            the str date
     * @return true, if successful
     */
    public boolean checkDateFormat(final String strDate) {
        boolean isMatching = true;
        if (isNotBlank(strDate)) {
            final Pattern pattern = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)[0-9]{2}");
            final Matcher matcher = pattern.matcher(strDate);
            isMatching = matcher.matches();
        }
        return isMatching;
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
        final String[] DATE_WORD = { "First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth", "Ninth",
                "Tenth", "Eleventh", "Twelfth", "Thirteenth", "Fourteenth", "Fifteenth", "Sixteenth", "Seventeenth", "Eighteenth",
                "Nineteenth", "Twentieth", "Twenty first", "Twenty second", "Twenty third", "Twenty fourth", "Twenty fifth",
                "Twenty sixth", "Twenty seventh", "Twenty eighth", "Twenty ninth", "Thirtieth", "Thirty first" };
        final Calendar cal = Calendar.getInstance();
        cal.setTime(dateToConvert);
        final StringBuilder dateInWord = new StringBuilder();
        dateInWord.append(DATE_WORD[cal.get(Calendar.DATE) - 1]).append(" ");
        dateInWord.append(getDateFormatter("dd-MMMMM-yyyy").format(dateToConvert).split("-")[1]).append(" ");
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
    @Deprecated
    public static SimpleDateFormat getDateFormatter(final String pattern) {
        SimpleDateFormat dateFormatter;
        if (DATE_FRMTR_CACHE.containsKey(pattern))
            dateFormatter = DATE_FRMTR_CACHE.get(pattern);
        else {
            dateFormatter = new SimpleDateFormat(pattern, Locale.getDefault());
            DATE_FRMTR_CACHE.put(pattern, dateFormatter);
        }
        return dateFormatter;
    }
}