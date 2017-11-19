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
package org.egov.works.utils;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConversionUtil {
    private static final Logger logger = Logger.getLogger(DateConversionUtil.class);

    public DateConversionUtil() {

    }

    public void dummy() {
        // do nothing
    }

    public static boolean isWithinDateRange(final Date dateToSearch, final Date startdate, final Date enddate) {

        if (enddate == null) {
            if (startdate.before(dateToSearch) || dateToSearch.compareTo(startdate) == 0
                    || dateToSearch.compareTo(startdate) > 0)
                return true;
        } else if (startdate.before(dateToSearch) && enddate.after(dateToSearch)
                || dateToSearch.compareTo(startdate) == 0 || dateToSearch.compareTo(enddate) == 0)
            return true;
        return false;
    }

    /**
     * Check the date is before or not ignoring time parameters. This method is similar to java.util.Date.before, only it will
     * ignore the timestamp.
     *
     * @param actualDate - Date that has to check
     * @param when - Date that has to refer as limit
     */
    public static boolean isBeforeByDate(final Date actualDate, final Date when) {
        final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "IN"));
        final String strFromDate = df.format(actualDate);
        final String strToDate = df.format(when);
        Date tFromDate = null;
        Date tToDate = null;
        try {
            // Retain the date without time
            tFromDate = df.parse(strFromDate);
            tToDate = df.parse(strToDate);
            return tFromDate.before(tToDate);
        } catch (final ParseException pe) {
            logger.error("Exp in isBeforeByDate() >>>" + pe);
            return false;
        } catch (final IllegalArgumentException ie) {
            logger.error("Exp in isBeforeByDate() >>>" + ie);
            return false;
        }
    }
}
