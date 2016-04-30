/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.bnd.utils;

import org.egov.bnd.model.Registration;
import org.egov.infstr.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public abstract class BndDateUtils {

    public static int getCurrentYear(final Date date) {
        int currentYear = 0;
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        currentYear = calendar.get(Calendar.YEAR);
        return currentYear;
    }

    /*
     * calendar.set(1stparameter,2ndparameter,3rdparameter)1stparameter - year:
     * 4 digit number ex:20112ndparameter - month 0-11(jan- dec)
     * 3rdparameter-date(1-31)
     */

    public static Date getStartDateFromYear(final Integer year) {

        final GregorianCalendar calendar = new GregorianCalendar();
        /*
         * set calendar to 31/dec/year-1
         */

        calendar.set(year - 1, 11, 31);
        final Date date = calendar.getTime();
        return date;
    }

    public static Date getEndDateFromYear(final Integer year) {
        final GregorianCalendar calendar = new GregorianCalendar();
        /*
         * set calendar to 31/dec/year
         */
        calendar.set(year, 11, 31);
        // calendar.set(year, 11, 31, 23, 59);
        final Date date = calendar.getTime();
        return date;
    }

    @SuppressWarnings("unchecked")
    public static List<Date> calenderutils(final int year, final int month) {

        Date fromDate = null;
        Date toDate = null;
        final List dateList = new ArrayList();
        final SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fromDate = sf.parse("01/" + month + "/" + year);
        } catch (final ParseException e) {

            e.printStackTrace();
        }
        int endDate;
        if (month == 2 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
            endDate = 31;
        else if (month == 2) {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(String.valueOf(year)), Calendar.FEBRUARY, 1);
            endDate = calendar.getActualMaximum(Calendar.DATE);
        } else
            endDate = 30;
        try {
            toDate = sf.parse(endDate + "/" + month + "/" + year);
        } catch (final ParseException e) {

            e.printStackTrace();
        }
        dateList.add(fromDate);
        dateList.add(toDate);

        return dateList;
    }

    public static Boolean isCurrentyear(final Date strDate) {

        // Date date=DateUtils.today();
        long diff = DateUtils.today().getTime() - strDate.getTime();
        diff = diff / (1000 * 60 * 60 * 24);
        if (diff <= 365)
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    /*
     * API to check whether side letter can be issued. SideLetter can be issued
     * only after 15 years from date of registration
     */
    public static Boolean isSideLetter(final Date strDate) {

        // Date date=DateUtils.today();
        long diff = DateUtils.today().getTime() - strDate.getTime();
        diff = diff / (1000 * 60 * 60 * 24);
        if (diff <= 15 * 365)
            return Boolean.FALSE;
        return Boolean.TRUE;
    }

    /*
     * API to check whether record is entered by hospital Registrar and is
     * within 1year
     */
    public static Boolean hasJurisdiction(final Registration reg, final Date eventDate) {
        Boolean bJurisdiction = Boolean.FALSE;

        if (reg.getEstablishment() != null && reg.getEstablishment().getIsAuth() && isCurrentyear(eventDate))
            bJurisdiction = Boolean.TRUE;
        else
            bJurisdiction = Boolean.FALSE;

        return bJurisdiction;
    }

}
