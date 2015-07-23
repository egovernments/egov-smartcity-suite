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
package org.egov.pgr.service.dashboard;

import static org.egov.infra.utils.DateUtils.endOfGivenDate;
import static org.egov.infra.utils.DateUtils.startOfGivenDate;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.repository.dashboard.DashboardRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class DashboardService {

    @Autowired
    private DashboardRepository dashboardRepository;

    public Collection<Integer> getComplaintRegistrationTrend() {
        final DateTime currentDate = new DateTime();
        final Map<String, Integer> currentYearTillDays = constructDatePlaceHolder(currentDate.minusDays(6), currentDate, "MM-dd");
        for (final Object[] compDtl : dashboardRepository
                .fetchComplaintRegistrationTrendBetween(startOfGivenDate(currentDate.minusDays(6)).toDate(),
                        endOfGivenDate(currentDate).toDate()))
            currentYearTillDays.put(String.valueOf(compDtl[0]), Integer.valueOf(String.valueOf(compDtl[1])));
        return currentYearTillDays.values();
    }

    public Collection<Integer> getComplaintResolutionTrend() {
        final DateTime currentDate = new DateTime();
        final Map<String, Integer> currentYearTillDays = constructDatePlaceHolder(currentDate.minusDays(6), currentDate, "MM-dd");
        for (final Object[] compDtl : dashboardRepository.fetchComplaintResolutionTrendBetween(
                startOfGivenDate(currentDate.minusDays(6)).toDate(),
                endOfGivenDate(currentDate).toDate()))
            currentYearTillDays.put(String.valueOf(compDtl[1]), Integer.valueOf(String.valueOf(compDtl[0])));
        return currentYearTillDays.values();
    }

    public List<Map<String, Object>> getMonthlyAggregate() {
        final DateTime currentDate = new DateTime();
        final List<Map<String, Object>> dataHolder = constructMonthPlaceHolder(currentDate.minusMonths(6), currentDate,
                "MMM-yyyy");
        for (final Object[] compCnt : dashboardRepository.fetchMonthlyAggregateBetween(
                startOfGivenDate(currentDate.minusMonths(6).withDayOfMonth(1)).toDate(), endOfGivenDate(currentDate).toDate()))
            for (final Map<String, Object> mapdata : dataHolder)
                if (mapdata.containsValue(StringUtils.capitalize(String.valueOf(compCnt[0]).toLowerCase())))
                    mapdata.put("y", Integer.valueOf(String.valueOf(compCnt[1])));
        return dataHolder;
    }

    public List<Map<String, Object>> getCompTypewiseAggregate() {
        final DateTime currentDate = new DateTime();
        final List<Map<String, Object>> compTypeWiseData = new LinkedList<Map<String, Object>>();
        long totalOthersCount = 0;
        int topCount = 1;
        for (final Object[] complaint : dashboardRepository.fetchComplaintTypeWiseBetween(
                startOfGivenDate(currentDate.minusMonths(6).withDayOfMonth(1)).toDate(), endOfGivenDate(currentDate).toDate())) {
            final Map<String, Object> compTypewiseCnt = new HashMap<String, Object>();
            final Integer complaintCount = Integer.valueOf(String.valueOf(complaint[2]));
            if (topCount < 9) {
                compTypewiseCnt.put("name", String.valueOf(complaint[0]));
                compTypewiseCnt.put("ctId", complaint[1]);
                compTypewiseCnt.put("y", complaintCount);
                compTypeWiseData.add(compTypewiseCnt);
                topCount++;
            } else
                totalOthersCount += complaintCount;
        }

        if (totalOthersCount > 0) {
            final Map<String, Object> othersData = new HashMap<String, Object>();
            othersData.put("name", "Others");
            othersData.put("ctId", "");
            othersData.put("y", totalOthersCount);
            compTypeWiseData.add(othersData);
        }
        return compTypeWiseData;
    }

    private static Map<String, Integer> constructDatePlaceHolder(final DateTime startDate, final DateTime endDate,
            final String pattern) {
        final Map<String, Integer> currentYearTillDays = new LinkedHashMap<String, Integer>();
        for (DateTime date = startDate; date.isBefore(endDate); date = date.plusDays(1))
            currentYearTillDays.put(date.toString(pattern), Integer.valueOf(0));
        currentYearTillDays.put(endDate.toString(pattern), Integer.valueOf(0));
        return currentYearTillDays;
    }

    private static List<Map<String, Object>> constructMonthPlaceHolder(final DateTime startDate, final DateTime endDate,
            final String pattern) {
        final List<Map<String, Object>> dataHolder = new LinkedList<Map<String, Object>>();
        for (DateTime date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusMonths(1)) {
            final Map<String, Object> currentYearTillDays = new LinkedHashMap<String, Object>();
            currentYearTillDays.put("name", date.toString(pattern));
            currentYearTillDays.put("y", Double.valueOf(0));
            dataHolder.add(currentYearTillDays);
        }
        return dataHolder;
    }
}
