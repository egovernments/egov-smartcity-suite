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

package org.egov.pgr.service.dashboard;

import static org.egov.infra.utils.DateUtils.endOfGivenDate;
import static org.egov.infra.utils.DateUtils.startOfGivenDate;
import static org.egov.pgr.utils.constants.PGRConstants.DISPOSALPERC;
import static org.egov.pgr.utils.constants.PGRConstants.WARDNAME;
import static org.egov.pgr.utils.constants.PGRConstants.WARDID;
import static org.egov.pgr.utils.constants.PGRConstants.COUNT;
import static org.egov.pgr.utils.constants.PGRConstants.COLOR;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.repository.dashboard.DashboardRepository;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class DashboardService {

    public static final Map<String, String[]> COLOR_GRADIENTS = new HashMap<>();
    private static final DateTimeFormatter DFLT_DATE_FRMTR = DateTimeFormat.forPattern("dd/MM/yyyy");

    static {
        COLOR_GRADIENTS.put("#5B94CB", new String[]{"#00285F", "#1D568D", "#4B84BB", "#79B2E9", "#A7E0FF"});
        COLOR_GRADIENTS.put("#938250", new String[]{"#584e30", "#665b38", "#756840", "#847548", "#9d8e61"});
        COLOR_GRADIENTS.put("#f9f107", new String[]{"#BBB300", "#E9E100", "#FFFF25", "#FFFF53", "#FFFF79"});
        COLOR_GRADIENTS.put("#6AC657", new String[]{"#005A00", "#0E6A00", "#2C8819", "#5AB647", "#88E475"});
        COLOR_GRADIENTS.put("#4F54B8", new String[]{"#00004C", "#11167A", "#3F44A8", "#6D72D6", "#9BA0FF"});
        COLOR_GRADIENTS.put("#B15D16", new String[]{"#450000", "#731F00", "#A14D06", "#CF7B34", "#FDA962"});
        COLOR_GRADIENTS.put("#C00000", new String[]{"#540000", "#B00000", "#DE1E1E", "#FF4C4C", "#FF5454"});
    }

    @Autowired
    private DashboardRepository dashboardRepository;

    private static Map<String, Integer> constructDatePlaceHolder(final DateTime startDate, final DateTime endDate,
                                                                 final String pattern) {
        final Map<String, Integer> currentYearTillDays = new LinkedHashMap<>();
        for (DateTime date = startDate; date.isBefore(endDate); date = date.plusDays(1))
            currentYearTillDays.put(date.toString(pattern), Integer.valueOf(0));
        currentYearTillDays.put(endDate.toString(pattern), Integer.valueOf(0));
        return currentYearTillDays;
    }

    private static List<Map<String, Object>> constructMonthPlaceHolder(final DateTime startDate, final DateTime endDate,
                                                                       final String pattern) {
        final List<Map<String, Object>> dataHolder = new LinkedList<>();
        for (DateTime date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusMonths(1)) {
            final Map<String, Object> currentYearTillDays = new LinkedHashMap<>();
            currentYearTillDays.put("name", date.toString(pattern));
            currentYearTillDays.put("y", Double.valueOf(0));
            dataHolder.add(currentYearTillDays);
        }
        return dataHolder;
    }

    public static List<Object> constructListOfMonthPlaceHolder(final DateTime startDate, final DateTime endDate,
                                                               final String pattern) {
        final List<Object> dataHolder = new LinkedList<>();
        for (DateTime date = startDate; date.isBefore(endDate); date = date.plusMonths(1))
            dataHolder.add(date.toString(pattern));
        return dataHolder;
    }

    private static void sortData(final List<Map<String, Object>> dataList, final String key) {
        Collections.sort(dataList, (map1, map2) -> Double.valueOf(map1.get(key).toString()).compareTo(Double.valueOf(map2.get(key).toString()))
        );
    }

    private static void assignRank(final List<Map<String, Object>> dataList, final String key) {
        int counter = 1;
        for (final Map<String, Object> map : dataList)
            map.put(key, counter++);
    }

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
        final List<Map<String, Object>> compTypeWiseData = new LinkedList<>();
        long totalOthersCount = 0;
        int topCount = 1;
        for (final Object[] complaint : dashboardRepository.fetchComplaintTypeWiseBetween(
                startOfGivenDate(currentDate.minusMonths(6).withDayOfMonth(1)).toDate(), endOfGivenDate(currentDate).toDate())) {
            final Map<String, Object> compTypewiseCnt = new HashMap<>();
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
            final Map<String, Object> othersData = new HashMap<>();
            othersData.put("name", "Others");
            othersData.put("ctId", "");
            othersData.put("y", totalOthersCount);
            compTypeWiseData.add(othersData);
        }
        return compTypeWiseData;
    }

    public List<List<Map<String, Object>>> getWardwisePerformance() {
        final DateTime currentDate = new DateTime();
        final List<Object[]> wardwisePerformanceData = dashboardRepository.fetchWardwisePerformanceTill(currentDate);
        final List<List<Map<String, Object>>> datas = new LinkedList<>();
        datas.add(performanceAnalysis(wardwisePerformanceData, currentDate));
        datas.add(performanceProjection(wardwisePerformanceData));
        return datas;
    }

    public List<List<Object>> getAgeingByWard(final String wardName) {
        return getAgeingData("pgr.wardwise.ageing", wardName);
    }

    private List<Map<String, Object>> performanceProjection(final List<Object[]> wardwisePerformanceData) {
        final DecimalFormat df = new DecimalFormat("####0.00");
        final List<Map<String, Object>> compAggrData = new ArrayList<>();
        for (final Object[] compData : wardwisePerformanceData) {
            final Map<String, Object> complaintData = new HashMap<>();
            complaintData.put("name", compData[0]);
            final BigInteger compData1 = (BigInteger) compData[1];
            final BigInteger compData3 = (BigInteger) compData[3];
            final BigInteger compData4 = (BigInteger) compData[4];
            final double noOfCompAsOnDate = compData1.doubleValue();
            final double noOfCompReceivedBtw = compData3.doubleValue();
            final double noOfCompPenAsonDate = compData4.doubleValue();
            final Double yValue = 100 * (noOfCompAsOnDate + noOfCompReceivedBtw - noOfCompPenAsonDate)
                    / (noOfCompAsOnDate + noOfCompReceivedBtw);
            if (yValue.isNaN() || yValue.isInfinite())
                complaintData.put("y", BigDecimal.ZERO);
            else
                complaintData.put("y", new BigDecimal(df.format(yValue)));
            compAggrData.add(complaintData);
        }

        // SORT ZONEWISE PERFORMANCE BY REDRESSAL %
        sortData(compAggrData, "y");
        Collections.reverse(compAggrData);
        return compAggrData;
    }

    public Map<String, Object> topComplaints() {
        final DateTime currentDate = new DateTime();

        final List<Object> dataHolderNumber = constructListOfMonthPlaceHolder(currentDate.minusMonths(5),
                currentDate.plusMonths(1),
                "MM");
        final List<Object> dataHolderString = constructListOfMonthPlaceHolder(currentDate.minusMonths(5),
                currentDate.plusMonths(1), "MMM");
        final List<Object[]> topFiveCompTypeData = dashboardRepository.fetchTopComplaintsBetween(
                startOfGivenDate(currentDate.minusMonths(5).withDayOfMonth(1)).toDate(), endOfGivenDate(currentDate).toDate());
        final List<Object[]> topFiveCompTypeCurrentMonth = dashboardRepository.fetchTopComplaintsForCurrentMonthBetween(
                startOfGivenDate(currentDate.minusMonths(5).withDayOfMonth(1)).toDate(), endOfGivenDate(currentDate).toDate());
        final Map<Object, Object> constructResultPlaceholder = new LinkedHashMap<>();
        final Map<Object, Object> actualdata = new LinkedHashMap<>();
        for (final Object complaintType : topFiveCompTypeCurrentMonth)
            for (final Object month : dataHolderNumber)
                constructResultPlaceholder.put(month + "-" + complaintType, BigInteger.ZERO);
        for (final Object[] top5CompType : topFiveCompTypeData)
            actualdata.put(top5CompType[0] + "-" + top5CompType[2], top5CompType[1]);
        final Map<Object, Object> newdata = new LinkedHashMap<>();
        for (final Object placeholderMapKey : constructResultPlaceholder.keySet())
            if (actualdata.get(placeholderMapKey) == null)
                newdata.put(placeholderMapKey, BigInteger.ZERO);
            else
                newdata.put(placeholderMapKey, actualdata.get(placeholderMapKey));
        final Map<String, Object> topFiveCompDataHolder = new LinkedHashMap<>();

        final List<Object> dataHolder = new LinkedList<>();
        final List<Object> compCount = new ArrayList<>();
        final Iterator<Entry<Object, Object>> entries = newdata.entrySet().iterator();
        int index = 0;
        while (entries.hasNext()) {
            final Map<String, Object> tmpdata = new LinkedHashMap<>();
            final Entry<Object, Object> entry = entries.next();
            if (index < 5) {
                compCount.add(entry.getValue());
                index++;
            } else if (index == 5) {
                compCount.add(entry.getValue());
                final String[] parts = entry.getKey().toString().split("-");
                tmpdata.put("name", parts[1]);
                tmpdata.put("data", new LinkedList<Object>(compCount));
                final HashMap<String, Object> ctypeCountMap = new LinkedHashMap<>();
                ctypeCountMap.putAll(tmpdata);
                dataHolder.add(ctypeCountMap);
                index = 0;
                compCount.clear();
                tmpdata.clear();
            }
        }
        topFiveCompDataHolder.put("year", dataHolderString);
        topFiveCompDataHolder.put("series", dataHolder);

        return topFiveCompDataHolder;
    }

    private List<List<Object>> getAgeingData(final String querykey, final String wardName) {
        final Object[] compData = dashboardRepository.fetchComplaintAgeing(querykey, wardName);
        final List<Object> cntabv90 = new LinkedList<>();
        cntabv90.add("&gt; 90 Days");
        cntabv90.add(((BigInteger) compData[0]).intValue());
        final List<Object> cntbtw45to90 = new LinkedList<>();
        cntbtw45to90.add("90-45 Days");
        cntbtw45to90.add(((BigInteger) compData[1]).intValue());
        final List<Object> cntbtw15to45 = new LinkedList<>();
        cntbtw15to45.add("44-15 Days");
        cntbtw15to45.add(((BigInteger) compData[2]).intValue());
        final List<Object> cntlsthn15 = new LinkedList<>();
        cntlsthn15.add("&lt; 15 Days");
        cntlsthn15.add(((BigInteger) compData[3]).intValue());
        final List<List<Object>> dataHolder = new LinkedList<>();
        dataHolder.add(cntabv90);
        dataHolder.add(cntbtw45to90);
        dataHolder.add(cntbtw15to45);
        dataHolder.add(cntlsthn15);

        return dataHolder;
    }

    private List<Map<String, Object>> performanceAnalysis(final List<Object[]> wardwisePerformanceData,
                                                          final DateTime currentDate) {
        final List<Map<String, Object>> compAggrData = new ArrayList<>();
        final String formattedFrm = endOfGivenDate(currentDate.minusDays(14)).toString(DFLT_DATE_FRMTR);
        final String formattedDayAfterFrm = startOfGivenDate(currentDate.minusDays(13)).toString(DFLT_DATE_FRMTR);
        final String formattedTo = currentDate.toString(DFLT_DATE_FRMTR);
        final DecimalFormat df = new DecimalFormat("####0.00");
        for (final Object[] compData : wardwisePerformanceData) {
            final Map<String, Object> complaintData = new HashMap<>();
            complaintData.put("zone", compData[0]);
            final BigInteger compData1 = (BigInteger) compData[1];
            final BigInteger compData3 = (BigInteger) compData[3];
            final BigInteger compData4 = (BigInteger) compData[4];
            final double noOfCompAsOnDate = compData1.doubleValue();
            final double noOfCompReceivedBtw = compData3.doubleValue();
            final double noOfCompPenAsonDate = compData4.doubleValue();
            complaintData.put("dateAsOn2WeekBack", formattedFrm);
            complaintData.put("noOfCompAsOnDate", noOfCompAsOnDate);
            complaintData.put("dateAsOnDayAfter", formattedDayAfterFrm);
            complaintData.put("noOfCompReceivedBtw", noOfCompReceivedBtw);
            complaintData.put("dateAsOn", formattedTo);
            complaintData.put("noOfCompPenAsonDate", noOfCompPenAsonDate);
            final Double disposalPerc = 100 * (noOfCompAsOnDate + noOfCompReceivedBtw - noOfCompPenAsonDate)
                    / (noOfCompAsOnDate + noOfCompReceivedBtw);
            if (disposalPerc.isNaN() || disposalPerc.isInfinite())
                complaintData.put(DISPOSALPERC, "0.00");
            else
                complaintData.put(DISPOSALPERC, df.format(disposalPerc));
            complaintData.put("lat", compData[6]);
            complaintData.put("lng", compData[7]);
            complaintData.put("zoneId", compData[8]);
            compAggrData.add(complaintData);
        }

        // SORT ZONEWISE PERFORMANCE BY REDRESSAL %
        sortData(compAggrData, DISPOSALPERC);
        Collections.reverse(compAggrData);
        // ASSIGN A RANK BASED ON ORDER
        assignRank(compAggrData, "rank");
        return compAggrData;
    }

    public List<List<Object>> getComplaintSLA() {
        return getAgeingData("pgr.comp.count.sla.breakup", null);
    }

    public List<Map<String, Object>> getOpenComplaintSLA() {
        final DateTime startOfTheYear = new LocalDate().minusYears(1).toDateTimeAtStartOfDay();
        final DateTime tillDate = new LocalDate().toDateTimeAtCurrentTime();
        final List<Object[]> openComplaints = dashboardRepository.fetchOpenComplaintAggregateBetween(startOfTheYear, tillDate);

        final List<Map<String, Object>> compAggrData = new ArrayList<>();
        final Map<String, Object> complaintData = new HashMap<>();
        String lastZone = null;
        double regComplaint = 0;
        double openFrm90Days = 0;
        double totalOpen = 0;
        double pecentage = 0;
        final Date dateBefore90Days = new LocalDate().minusDays(90).toDateTimeAtStartOfDay().toDate();
        final DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
        final String formattedFrm = startOfTheYear.toString(dtf);
        final String formattedTo = tillDate.toString(dtf);
        for (final Object[] compData : openComplaints) {
            final String statusName = String.valueOf(compData[4]);
            final long count = Long.parseLong(String.valueOf(compData[5]));
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
            final String currentZone = String.valueOf(compData[0]);
            if (openComplaints.size() == 1 || lastZone != null && !currentZone.equals(lastZone)) {
                pecentage = Math.round(100 * (totalOpen / regComplaint));
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
        }

        // SORT BASED ON TOTAL NO. OF OPEN COMP > 90
        sortData(compAggrData, "open90Comp");

        return compAggrData;
    }

    public List<Map<String, Object>> getWardwiseComplaintByComplaintType(final Long complaintTypeId,
                                                                         final String currentChartColor) {
        final DateTime currentDate = new DateTime();
        final List<Map<String, Object>> wardWiseData = new LinkedList<>();
        double topCount = -1;
        for (final Object[] complaint : dashboardRepository.fetchComplaintsByComplaintTypeGroupByWard(complaintTypeId,
                startOfGivenDate(currentDate.minusMonths(6).withDayOfMonth(1).withTimeAtStartOfDay()),
                endOfGivenDate(currentDate))) {
            final Map<String, Object> wardwiseCnt = new HashMap<>();
            wardwiseCnt.put(WARDNAME, String.valueOf(complaint[0]));
            wardwiseCnt.put(WARDID, ((BigInteger) complaint[1]).intValue());
            final double count = ((BigInteger) complaint[2]).doubleValue();
            wardwiseCnt.put(COUNT, count);
            if (topCount == -1)
                topCount = count;
            final double perc = count * 100 / topCount;
            final String[] colors = COLOR_GRADIENTS.get(currentChartColor);
            if (perc <= 20)
                wardwiseCnt.put(COLOR, colors[4]);
            else if (perc > 20.0 && perc <= 40.0)
                wardwiseCnt.put(COLOR, colors[3]);
            else if (perc > 40.0 && perc <= 60.0)
                wardwiseCnt.put(COLOR, colors[2]);
            else if (perc > 60.0 && perc <= 80.0)
                wardwiseCnt.put(COLOR, colors[1]);
            else
                wardwiseCnt.put(COLOR, colors[0]);

            wardWiseData.add(wardwiseCnt);
        }
        return wardWiseData;
    }

    public Map<String, List<Map<String, Object>>> getGISWardWiseAnalysis() {

        // final List<Object[]> top3CompTypes = getQuery("pgr.top3.comptype").list();
        final Map<String, List<Map<String, Object>>> gisAnalysisData = new HashMap<>();
        /*
         * final String [] top3Colors = {"#5B94CB","#938250","#6AC657"}; int colorCount = 0; for (final Object[] top3CompType :
         * top3CompTypes) { final SQLQuery qry = getQuery("pgr.bndry.wise.perc"); qry.setParameter("fromDate", new
         * LocalDate().minusMonths(6).toDateTimeAtStartOfDay().toDate()); qry.setParameter("toDate", endOfDay().toDate());
         * qry.setParameter("compTypeId",((BigDecimal)top3CompType[0]).intValue()); final List<Object[]> complaints = qry.list();
         * final List<Map<String, Object>> wardWiseData = new LinkedList<Map<String, Object>>(); double topCount = -1; for (final
         * Object[] complaint : complaints) { final Map<String, Object> wardwiseCnt = new HashMap<String, Object>();
         * wardwiseCnt.put("wardName", String.valueOf(complaint[0])); wardwiseCnt.put("wardId",
         * ((BigDecimal)complaint[1]).intValue()); wardwiseCnt.put("compType",
         * WordUtils.capitalizeFully(String.valueOf(top3CompType[1]))); double count = ((BigDecimal) complaint[2]).doubleValue();
         * wardwiseCnt.put("count", count); if(topCount == -1) { topCount = count; } double perc = ((count*100)/topCount); String
         * [] colors = colorGradients.get(top3Colors[colorCount]); if(perc <= 20) { wardwiseCnt.put("color", colors[4]); } else
         * if(perc > 20.0 && perc <= 40.0) { wardwiseCnt.put("color", colors[3]); } else if(perc > 40.0 && perc <= 60.0) {
         * wardwiseCnt.put("color", colors[2]); } else if(perc > 60.0 && perc <= 80.0) { wardwiseCnt.put("color", colors[1]); }
         * else { wardwiseCnt.put("color", colors[0]); } wardWiseData.add(wardwiseCnt); } colorCount++;
         * gisAnalysisData.put("top"+colorCount,wardWiseData); }
         */
        gisAnalysisData.put("registered", getGISRegCompWardWise());
        // gisAnalysisData.put("complaintPerProperty", getGISCompPerPropertyWardWise());
        // gisAnalysisData.put("redressed", getGISCompRedressedWardWise());

        return gisAnalysisData;
    }

    public List<Map<String, Object>> getGISCompPerPropertyWardWise() {
        final List<Object[]> compCount = dashboardRepository.fetchGISCompPerPropertyWardWise();
        final List<Map<String, Object>> wardWiseData = new LinkedList<>();
        double topCount = -1;
        for (final Object[] wardData : compCount) {
            final Map<String, Object> wardwiseCompPerProp = new HashMap<>();
            wardwiseCompPerProp.put(WARDNAME, String.valueOf(wardData[0]));
            wardwiseCompPerProp.put(WARDID, ((BigDecimal) wardData[1]).intValue());
            final double count = ((BigDecimal) wardData[2]).doubleValue();
            wardwiseCompPerProp.put(COUNT, count);
            if (topCount == -1)
                topCount = count;
            final double perc = count * 100 / topCount;
            final String[] colors = COLOR_GRADIENTS.get("#B15D16");
            if (perc <= 20)
                wardwiseCompPerProp.put(COLOR, colors[4]);
            else if (perc > 20.0 && perc <= 40.0)
                wardwiseCompPerProp.put(COLOR, colors[3]);
            else if (perc > 40.0 && perc <= 60.0)
                wardwiseCompPerProp.put(COLOR, colors[2]);
            else if (perc > 60.0 && perc <= 80.0)
                wardwiseCompPerProp.put(COLOR, colors[1]);
            else
                wardwiseCompPerProp.put(COLOR, colors[0]);
            wardWiseData.add(wardwiseCompPerProp);
        }
        return wardWiseData;
    }

    public List<Map<String, Object>> getGISCompRedressedWardWise() {
        final List<Object[]> compRedrsdCount = dashboardRepository.fetchGISCompRedressedWardWise();
        final List<Map<String, Object>> wardWiseData = new LinkedList<>();
        double topCount = -1;
        for (final Object[] wardData : compRedrsdCount) {
            final Map<String, Object> wardwiseCompRedressed = new HashMap<>();
            wardwiseCompRedressed.put(WARDNAME, String.valueOf(wardData[0]));
            wardwiseCompRedressed.put(WARDID, ((BigDecimal) wardData[1]).intValue());
            final double count = ((BigDecimal) wardData[2]).doubleValue();
            wardwiseCompRedressed.put(COUNT, count);
            if (topCount == -1)
                topCount = count;
            final double perc = count * 100 / topCount;
            final String[] colors = COLOR_GRADIENTS.get("#4F54B8");
            if (perc <= 20)
                wardwiseCompRedressed.put(COLOR, colors[4]);
            else if (perc > 20.0 && perc <= 40.0)
                wardwiseCompRedressed.put(COLOR, colors[3]);
            else if (perc > 40.0 && perc <= 60.0)
                wardwiseCompRedressed.put(COLOR, colors[2]);
            else if (perc > 60.0 && perc <= 80.0)
                wardwiseCompRedressed.put(COLOR, colors[1]);
            else
                wardwiseCompRedressed.put(COLOR, colors[0]);
            wardWiseData.add(wardwiseCompRedressed);
        }
        return wardWiseData;
    }

    public List<Map<String, Object>> getGISRegCompWardWise() {

        final List<Object[]> compCount = dashboardRepository.fetchGISRegCompWardWise();
        final List<Map<String, Object>> wardWiseData = new LinkedList<>();
        double topCount = -1;
        for (final Object[] wardData : compCount) {
            final Map<String, Object> wardwiseCnt = new HashMap<>();
            wardwiseCnt.put(WARDNAME, String.valueOf(wardData[0]));
            wardwiseCnt.put(WARDID, ((BigInteger) wardData[1]).intValue());
            final double count = ((BigInteger) wardData[2]).doubleValue();
            wardwiseCnt.put(COUNT, count);
            if (topCount == -1)
                topCount = count;
            final double perc = count * 100 / topCount;
            final String[] colors = COLOR_GRADIENTS.get("#C00000");
            if (perc <= 20)
                wardwiseCnt.put(COLOR, colors[4]);
            else if (perc > 20.0 && perc <= 40.0)
                wardwiseCnt.put(COLOR, colors[3]);
            else if (perc > 40.0 && perc <= 60.0)
                wardwiseCnt.put(COLOR, colors[2]);
            else if (perc > 60.0 && perc <= 80.0)
                wardwiseCnt.put(COLOR, colors[1]);
            else
                wardwiseCnt.put(COLOR, colors[0]);
            wardWiseData.add(wardwiseCnt);
        }
        return wardWiseData;
    }
}
