/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.pgr.repository.dashboard;

import static org.egov.infra.utils.DateUtils.endOfGivenDate;
import static org.egov.infra.utils.DateUtils.startOfGivenDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("all")
public class DashboardRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    @Qualifier("dashboardSQLSource")
    private ReloadableResourceBundleMessageSource dashboardSQLSource;

    public List<Object[]> fetchComplaintResolutionTrendBetween(final Date fromDate, final Date toDate) {
        return fetchDateRangeData("pgr.comp.resolution.weekly.trend", fromDate, toDate);
    }

    public List<Object[]> fetchComplaintRegistrationTrendBetween(final Date fromDate, final Date toDate) {
        return fetchDateRangeData("pgr.comp.reg.weekly.trend", fromDate, toDate);
    }

    public List<Object[]> fetchMonthlyAggregateBetween(final Date fromDate, final Date toDate) {
        return fetchDateRangeData("pgr.comp.six.month.aggr", fromDate, toDate);
    }

    public List<Object[]> fetchComplaintTypeWiseBetween(final Date fromDate, final Date toDate) {
        return fetchDateRangeData("pgr.comp.type.wise.perc", fromDate, toDate);
    }

    public List<Object[]> fetchWardwisePerformanceTill(final DateTime toDate) {
        final SQLQuery qry = getQuery("pgr.wardwiseperformance");
        qry.setParameter("thirteenDaysBefore", endOfGivenDate(toDate.minusDays(13)).toDate());
        qry.setParameter("fourteenDaysBefore", startOfGivenDate(toDate.minusDays(14)).toDate());
        qry.setParameter("currentDate", endOfGivenDate(toDate).toDate());
        return qry.list();
    }

    public Object[] fetchComplaintAgeing(final String querykey, final String wardName) {
        final SQLQuery qry = getQuery(querykey);
        final DateTime currentDate = new DateTime();
        qry.setParameter("grtthn90", endOfGivenDate(currentDate.minusDays(90)).toDate());
        qry.setParameter("lsthn90", endOfGivenDate(currentDate.minusDays(90)).toDate());
        qry.setParameter("grtthn45", endOfGivenDate(currentDate.minusDays(45)).toDate());
        qry.setParameter("grtthn15", endOfGivenDate(currentDate.minusDays(15)).toDate());
        qry.setParameter("lsthn45", endOfGivenDate(currentDate.minusDays(45)).toDate());
        qry.setParameter("lsthn15", endOfGivenDate(currentDate.minusDays(15)).toDate());
        qry.setParameter("currdate", endOfGivenDate(currentDate).toDate());
        if (wardName != null)
            qry.setParameter("wardName", wardName);
        return (Object[]) qry.uniqueResult();
    }

    private List<Object[]> fetchDateRangeData(final String query, final Date fromDate, final Date toDate) {
        final SQLQuery qry = getQuery(query);
        qry.setParameter("fromDate", fromDate);
        qry.setParameter("toDate", toDate);
        return qry.list();
    }

    public List<List<Object>> getAgeingData(final String querykey, final String zoneName) {
        final SQLQuery qry = getQuery(querykey);
        qry.setParameter("grtthn90", endOfDay().minusDays(90).toDate());
        qry.setParameter("lsthn90", endOfDay().minusDays(90).toDate());
        qry.setParameter("grtthn45", endOfDay().minusDays(45).toDate());
        qry.setParameter("grtthn15", endOfDay().minusDays(15).toDate());
        qry.setParameter("lsthn45", endOfDay().minusDays(45).toDate());
        qry.setParameter("lsthn15", endOfDay().minusDays(15).toDate());
        qry.setParameter("currdate", endOfDay().toDate());
        if (zoneName != null)
            qry.setParameter("zoneName", zoneName);
        final Object[] compData = (Object[]) qry.uniqueResult();

        final List<Object> cntabv90 = new LinkedList<Object>();
        cntabv90.add("> 90 Days");
        cntabv90.add(compData[0]);
        final List<Object> cntbtw45to90 = new LinkedList<Object>();
        cntbtw45to90.add("90-45 Days");
        cntbtw45to90.add(compData[1]);
        final List<Object> cntbtw15to45 = new LinkedList<Object>();
        cntbtw15to45.add("44-15 Days");
        cntbtw15to45.add(compData[2]);
        final List<Object> cntlsthn15 = new LinkedList<Object>();
        cntlsthn15.add("< 15 Days");
        cntlsthn15.add(compData[3]);
        final List<List<Object>> dataHolder = new LinkedList<List<Object>>();
        dataHolder.add(cntabv90);
        dataHolder.add(cntbtw45to90);
        dataHolder.add(cntbtw15to45);
        dataHolder.add(cntlsthn15);

        return dataHolder;
    }

    public List<Map<String, Object>> getOpenComplaintAggregate() {
        final SQLQuery qry = getQuery("pgr.open.comp.aggr");
        final DateTime startOfTheYear = new LocalDate().minusYears(1).toDateTimeAtStartOfDay();
        final DateTime tillDate = LocalTime.MIDNIGHT.toDateTimeToday();
        qry.setParameter("startOfYear", startOfTheYear.toDate());
        qry.setParameter("tillDate", endOfDay().toDate());
        final List<Object[]> complaints = qry.list();
        final List<Map<String, Object>> compAggrData = new ArrayList<Map<String, Object>>();
        final Map<String, Object> complaintData = new HashMap<String, Object>();
        String lastZone = null;
        double regComplaint = 0;
        double openFrm90Days = 0;
        double totalOpen = 0;
        double pecentage = 0;
        final Date dateBefore90Days = new LocalDate().minusDays(90).toDateTimeAtStartOfDay().toDate();
        final DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
        final String formattedFrm = startOfTheYear.toString(dtf);
        final String formattedTo = tillDate.toString(dtf);
        for (final Object[] compData : complaints) {
            final String currentZone = String.valueOf(compData[0]);
            if (lastZone != null && !currentZone.equals(lastZone)) {
                pecentage = Math.round(100 * (openFrm90Days / regComplaint));
                complaintData.put("pecentage", pecentage);
                complaintData.put("regComp", regComplaint);
                complaintData.put("open90Comp", openFrm90Days);
                complaintData.put("openComp", totalOpen);
                complaintData.put("zone", lastZone);
                compAggrData.add(new HashMap<String, Object>(complaintData));
                pecentage = 0;
                regComplaint = 0;
                totalOpen = 0;
                openFrm90Days = 0;
            }
            lastZone = currentZone;
            final String statusName = String.valueOf(compData[4]);
            final long count = Long.valueOf(String.valueOf(compData[5]));
            if ("REGISTERED FORWARDED PROCESSING REOPENED".contains(statusName)) {
                if (((Date) compData[6]).before(dateBefore90Days))
                    openFrm90Days += count;
                totalOpen += count;
            }
            regComplaint += count;
            complaintData.put("lat", compData[3]);
            complaintData.put("lng", compData[2]);
            complaintData.put("startDt", formattedFrm);
            complaintData.put("endDt", formattedTo);
            complaintData.put("zoneID", compData[1]);
        }

        // SORT BASED ON TOTAL NO. OF OPEN COMP > 90
        sortData(compAggrData, "open90Comp");

        return compAggrData;
    }

    public static DateTime endOfDay() {
        return new DateTime().withTime(23, 59, 59, 999).toDateTime();
    }

    public static void sortData(final List<Map<String, Object>> dataList, final String key) {
        Collections.sort(dataList, (map1, map2) -> {
            final double firstElem = Double.valueOf(map1.get(key).toString());
            final double secondElem = Double.valueOf(map2.get(key).toString());
            return firstElem <= secondElem ? 1 : -1;
        });
    }

    private SQLQuery getQuery(final String sqlKey) {
        return entityManager.unwrap(Session.class)
                .createSQLQuery(dashboardSQLSource.getMessage(sqlKey, null, Locale.getDefault()));
    }

}
