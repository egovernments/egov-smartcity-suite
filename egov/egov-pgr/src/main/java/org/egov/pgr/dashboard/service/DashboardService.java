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

package org.egov.pgr.dashboard.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.dashboard.repository.DashboardRepository;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.egov.infra.utils.DateUtils.defaultDateFormatter;
import static org.egov.infra.utils.DateUtils.endOfGivenDate;
import static org.egov.infra.utils.DateUtils.startOfGivenDate;
import static org.egov.pgr.utils.constants.PGRConstants.COLOR;
import static org.egov.pgr.utils.constants.PGRConstants.COUNT;
import static org.egov.pgr.utils.constants.PGRConstants.DISPOSALPERC;
import static org.egov.pgr.utils.constants.PGRConstants.WARDID;
import static org.egov.pgr.utils.constants.PGRConstants.WARDNAME;

@Transactional(readOnly = true)
@Service
public class DashboardService {

    private static final Map<String, String[]> COLOR_GRADIENTS = new HashMap<>();

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

    private static Map<String, Integer> constructDatePlaceHolder(DateTime startDate, DateTime endDate,
                                                                 String pattern) {
        Map<String, Integer> currentYearTillDays = new LinkedHashMap<>();
        for (DateTime date = startDate; date.isBefore(endDate); date = date.plusDays(1))
            currentYearTillDays.put(date.toString(pattern), Integer.valueOf(0));
        currentYearTillDays.put(endDate.toString(pattern), Integer.valueOf(0));
        return currentYearTillDays;
    }

    private static List<Map<String, Object>> constructMonthPlaceHolder(DateTime startDate, DateTime endDate,
                                                                       String pattern) {
        List<Map<String, Object>> dataHolder = new LinkedList<>();
        for (DateTime date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusMonths(1)) {
            Map<String, Object> currentYearTillDays = new LinkedHashMap<>();
            currentYearTillDays.put("name", date.toString(pattern));
            currentYearTillDays.put("y", Double.valueOf(0));
            dataHolder.add(currentYearTillDays);
        }
        return dataHolder;
    }

    public static List<Object> constructListOfMonthPlaceHolder(DateTime startDate, DateTime endDate,
                                                               String pattern) {
        List<Object> dataHolder = new LinkedList<>();
        for (DateTime date = startDate; date.isBefore(endDate); date = date.plusMonths(1))
            dataHolder.add(date.toString(pattern));
        return dataHolder;
    }

    private static void sortData(List<Map<String, Object>> dataList, String key) {
        Collections.sort(dataList, Comparator.comparing(map -> Double.valueOf(map.get(key).toString()))
        );
    }

    private static void assignRank(List<Map<String, Object>> dataList, String key) {
        int counter = 1;
        for (Map<String, Object> map : dataList)
            map.put(key, counter++);
    }

    public Collection<Integer> getComplaintRegistrationTrend() {
        DateTime currentDate = new DateTime();
        Map<String, Integer> currentYearTillDays = constructDatePlaceHolder(currentDate.minusDays(6), currentDate, "MM-dd");
        for (Object[] compDtl : dashboardRepository
                .fetchComplaintRegistrationTrendBetween(startOfGivenDate(currentDate.minusDays(6)).toDate(),
                        endOfGivenDate(currentDate).toDate()))
            currentYearTillDays.put(String.valueOf(compDtl[0]), Integer.valueOf(String.valueOf(compDtl[1])));
        return currentYearTillDays.values();
    }

    public Collection<Integer> getComplaintResolutionTrend() {
        DateTime currentDate = new DateTime();
        Map<String, Integer> currentYearTillDays = constructDatePlaceHolder(currentDate.minusDays(6), currentDate, "MM-dd");
        for (Object[] compDtl : dashboardRepository.fetchComplaintResolutionTrendBetween(
                startOfGivenDate(currentDate.minusDays(6)).toDate(),
                endOfGivenDate(currentDate).toDate()))
            currentYearTillDays.put(String.valueOf(compDtl[1]), Integer.valueOf(String.valueOf(compDtl[0])));
        return currentYearTillDays.values();
    }

    public List<Map<String, Object>> getMonthlyAggregate() {
        DateTime currentDate = new DateTime();
        List<Map<String, Object>> dataHolder = constructMonthPlaceHolder(currentDate.minusMonths(6), currentDate,
                "MMM-yyyy");
        for (Object[] compCnt : dashboardRepository.fetchMonthlyAggregateBetween(
                startOfGivenDate(currentDate.minusMonths(6).withDayOfMonth(1)).toDate(), endOfGivenDate(currentDate).toDate()))
            for (Map<String, Object> mapdata : dataHolder)
                if (mapdata.containsValue(StringUtils.capitalize(String.valueOf(compCnt[0]).toLowerCase())))
                    mapdata.put("y", Integer.valueOf(String.valueOf(compCnt[1])));
        return dataHolder;
    }

    public List<Map<String, Object>> getCompTypewiseAggregate() {
        DateTime currentDate = new DateTime();
        List<Map<String, Object>> compTypeWiseData = new LinkedList<>();
        long totalOthersCount = 0;
        int topCount = 1;
        for (Object[] complaint : dashboardRepository.fetchComplaintTypeWiseBetween(
                startOfGivenDate(currentDate.minusMonths(6).withDayOfMonth(1)).toDate(), endOfGivenDate(currentDate).toDate())) {
            Map<String, Object> compTypewiseCnt = new HashMap<>();
            Integer complaintCount = Integer.valueOf(String.valueOf(complaint[2]));
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
            Map<String, Object> othersData = new HashMap<>();
            othersData.put("name", "Others");
            othersData.put("ctId", "");
            othersData.put("y", totalOthersCount);
            compTypeWiseData.add(othersData);
        }
        return compTypeWiseData;
    }

    public List<List<Map<String, Object>>> getWardwisePerformance() {
        DateTime currentDate = new DateTime();
        List<Object[]> wardwisePerformanceData = dashboardRepository.fetchWardwisePerformanceTill(currentDate);
        List<List<Map<String, Object>>> datas = new LinkedList<>();
        datas.add(performanceAnalysis(wardwisePerformanceData, currentDate));
        datas.add(performanceProjection(wardwisePerformanceData));
        return datas;
    }

    public List<List<Object>> getAgeingByWard(String wardName) {
        return getAgeingData("pgr.wardwise.ageing", wardName);
    }

    private List<Map<String, Object>> performanceProjection(List<Object[]> wardwisePerformanceData) {
        DecimalFormat df = new DecimalFormat("####0.00");
        List<Map<String, Object>> compAggrData = new ArrayList<>();
        for (Object[] compData : wardwisePerformanceData) {
            Map<String, Object> complaintData = new HashMap<>();
            complaintData.put("name", compData[0]);
            BigInteger compData1 = (BigInteger) compData[1];
            BigInteger compData3 = (BigInteger) compData[3];
            BigInteger compData4 = (BigInteger) compData[4];
            double noOfCompAsOnDate = compData1.doubleValue();
            double noOfCompReceivedBtw = compData3.doubleValue();
            double noOfCompPenAsonDate = compData4.doubleValue();
            Double yValue = 100 * (noOfCompAsOnDate + noOfCompReceivedBtw - noOfCompPenAsonDate)
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
        DateTime currentDate = new DateTime();

        List<Object> dataHolderNumber = constructListOfMonthPlaceHolder(currentDate.minusMonths(5),
                currentDate.plusMonths(1),
                "MM");
        List<Object> dataHolderString = constructListOfMonthPlaceHolder(currentDate.minusMonths(5),
                currentDate.plusMonths(1), "MMM");
        List<Object[]> topFiveCompTypeData = dashboardRepository.fetchTopComplaintsBetween(
                startOfGivenDate(currentDate.minusMonths(5).withDayOfMonth(1)).toDate(), endOfGivenDate(currentDate).toDate());
        List<Object[]> topFiveCompTypeCurrentMonth = dashboardRepository.fetchTopComplaintsForCurrentMonthBetween(
                startOfGivenDate(currentDate.minusMonths(5).withDayOfMonth(1)).toDate(), endOfGivenDate(currentDate).toDate());
        Map<Object, Object> constructResultPlaceholder = new LinkedHashMap<>();
        Map<Object, Object> actualdata = new LinkedHashMap<>();
        for (Object complaintType : topFiveCompTypeCurrentMonth)
            for (Object month : dataHolderNumber)
                constructResultPlaceholder.put(month + "-" + complaintType, BigInteger.ZERO);
        for (Object[] top5CompType : topFiveCompTypeData)
            actualdata.put(top5CompType[0] + "-" + top5CompType[2], top5CompType[1]);
        Map<Object, Object> newdata = new LinkedHashMap<>();
        for (Object placeholderMapKey : constructResultPlaceholder.keySet())
            if (actualdata.get(placeholderMapKey) == null)
                newdata.put(placeholderMapKey, BigInteger.ZERO);
            else
                newdata.put(placeholderMapKey, actualdata.get(placeholderMapKey));
        Map<String, Object> topFiveCompDataHolder = new LinkedHashMap<>();

        List<Object> dataHolder = new LinkedList<>();
        List<Object> compCount = new ArrayList<>();
        Iterator<Entry<Object, Object>> entries = newdata.entrySet().iterator();
        int index = 0;
        while (entries.hasNext()) {
            Map<String, Object> tmpdata = new LinkedHashMap<>();
            Entry<Object, Object> entry = entries.next();
            if (index < 5) {
                compCount.add(entry.getValue());
                index++;
            } else if (index == 5) {
                compCount.add(entry.getValue());
                String[] parts = entry.getKey().toString().split("-");
                tmpdata.put("name", parts[1]);
                tmpdata.put("data", new LinkedList<Object>(compCount));
                HashMap<String, Object> ctypeCountMap = new LinkedHashMap<>();
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

    private List<List<Object>> getAgeingData(String querykey, String wardName) {
        Object[] compData = dashboardRepository.fetchComplaintAgeing(querykey, wardName);
        List<Object> cntabv90 = new LinkedList<>();
        cntabv90.add("&gt; 90 Days");
        cntabv90.add(((BigInteger) compData[0]).intValue());
        List<Object> cntbtw45to90 = new LinkedList<>();
        cntbtw45to90.add("90-45 Days");
        cntbtw45to90.add(((BigInteger) compData[1]).intValue());
        List<Object> cntbtw15to45 = new LinkedList<>();
        cntbtw15to45.add("44-15 Days");
        cntbtw15to45.add(((BigInteger) compData[2]).intValue());
        List<Object> cntlsthn15 = new LinkedList<>();
        cntlsthn15.add("&lt; 15 Days");
        cntlsthn15.add(((BigInteger) compData[3]).intValue());
        List<List<Object>> dataHolder = new LinkedList<>();
        dataHolder.add(cntabv90);
        dataHolder.add(cntbtw45to90);
        dataHolder.add(cntbtw15to45);
        dataHolder.add(cntlsthn15);

        return dataHolder;
    }

    private List<Map<String, Object>> performanceAnalysis(List<Object[]> wardwisePerformanceData,
                                                          DateTime currentDate) {
        List<Map<String, Object>> compAggrData = new ArrayList<>();
        String formattedFrm = endOfGivenDate(currentDate.minusDays(14)).toString(defaultDateFormatter());
        String formattedDayAfterFrm = startOfGivenDate(currentDate.minusDays(13)).toString(defaultDateFormatter());
        String formattedTo = currentDate.toString(defaultDateFormatter());
        DecimalFormat df = new DecimalFormat("####0.00");
        for (Object[] compData : wardwisePerformanceData) {
            Map<String, Object> complaintData = new HashMap<>();
            complaintData.put("zone", compData[0]);
            BigInteger compData1 = (BigInteger) compData[1];
            BigInteger compData3 = (BigInteger) compData[3];
            BigInteger compData4 = (BigInteger) compData[4];
            double noOfCompAsOnDate = compData1.doubleValue();
            double noOfCompReceivedBtw = compData3.doubleValue();
            double noOfCompPenAsonDate = compData4.doubleValue();
            complaintData.put("dateAsOn2WeekBack", formattedFrm);
            complaintData.put("noOfCompAsOnDate", noOfCompAsOnDate);
            complaintData.put("dateAsOnDayAfter", formattedDayAfterFrm);
            complaintData.put("noOfCompReceivedBtw", noOfCompReceivedBtw);
            complaintData.put("dateAsOn", formattedTo);
            complaintData.put("noOfCompPenAsonDate", noOfCompPenAsonDate);
            Double disposalPerc = 100 * (noOfCompAsOnDate + noOfCompReceivedBtw - noOfCompPenAsonDate)
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
        DateTime startOfTheYear = new LocalDate().minusYears(1).toDateTimeAtStartOfDay();
        DateTime tillDate = new LocalDate().toDateTimeAtCurrentTime();
        List<Object[]> openComplaints = dashboardRepository.fetchOpenComplaintAggregateBetween(startOfTheYear, tillDate);

        List<Map<String, Object>> compAggrData = new ArrayList<>();
        Map<String, Object> complaintData = new HashMap<>();
        String lastZone = null;
        double regComplaint = 0;
        double openFrm90Days = 0;
        double totalOpen = 0;
        double pecentage = 0;
        Date dateBefore90Days = new LocalDate().minusDays(90).toDateTimeAtStartOfDay().toDate();
        String formattedFrm = startOfTheYear.toString(defaultDateFormatter());
        String formattedTo = tillDate.toString(defaultDateFormatter());
        for (Object[] compData : openComplaints) {
            String statusName = String.valueOf(compData[4]);
            long count = Long.parseLong(String.valueOf(compData[5]));
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
            String currentZone = String.valueOf(compData[0]);
            if (openComplaints.size() == 1 || lastZone != null && !currentZone.equals(lastZone)) {
                pecentage = Math.round(100 * (totalOpen / regComplaint));
                complaintData.put("pecentage", pecentage);
                complaintData.put("regComp", regComplaint);
                complaintData.put("open90Comp", openFrm90Days);
                complaintData.put("openComp", totalOpen);
                complaintData.put("zone", lastZone);
                compAggrData.add(new HashMap<>(complaintData));
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

    public List<Map<String, Object>> getWardwiseComplaintByComplaintType(Long complaintTypeId,
                                                                         String currentChartColor) {
        DateTime currentDate = new DateTime();
        List<Map<String, Object>> wardWiseData = new LinkedList<>();
        double topCount = -1;
        for (Object[] complaint : dashboardRepository.fetchComplaintsByComplaintTypeGroupByWard(complaintTypeId,
                startOfGivenDate(currentDate.minusMonths(6).withDayOfMonth(1).withTimeAtStartOfDay()),
                endOfGivenDate(currentDate))) {
            Map<String, Object> wardwiseCnt = new HashMap<>();
            wardwiseCnt.put(WARDNAME, String.valueOf(complaint[0]));
            wardwiseCnt.put(WARDID, ((BigInteger) complaint[1]).intValue());
            double count = ((BigInteger) complaint[2]).doubleValue();
            wardwiseCnt.put(COUNT, count);
            if (topCount < 0)
                topCount = count;
            applyColorCodes(wardwiseCnt, count * 100 / topCount, currentChartColor);
            wardWiseData.add(wardwiseCnt);
        }
        return wardWiseData;
    }

    public Map<String, List<Map<String, Object>>> getGISWardWiseAnalysis() {
        Map<String, List<Map<String, Object>>> gisAnalysisData = new HashMap<>();
        gisAnalysisData.put("registered", getGISRegCompWardWise());
        return gisAnalysisData;
    }

    public List<Map<String, Object>> getGISCompPerPropertyWardWise() {
        List<Object[]> compCount = dashboardRepository.fetchGISCompPerPropertyWardWise();
        List<Map<String, Object>> wardWiseData = new LinkedList<>();
        double topCount = -1;
        for (Object[] wardData : compCount) {
            Map<String, Object> wardwiseCompPerProp = new HashMap<>();
            wardwiseCompPerProp.put(WARDNAME, String.valueOf(wardData[0]));
            wardwiseCompPerProp.put(WARDID, ((BigDecimal) wardData[1]).intValue());
            double count = ((BigDecimal) wardData[2]).doubleValue();
            wardwiseCompPerProp.put(COUNT, count);
            if (topCount < 0)
                topCount = count;
            applyColorCodes(wardwiseCompPerProp, count * 100 / topCount, "#B15D16");
            wardWiseData.add(wardwiseCompPerProp);
        }
        return wardWiseData;
    }

    public List<Map<String, Object>> getGISCompRedressedWardWise() {
        List<Object[]> compRedrsdCount = dashboardRepository.fetchGISCompRedressedWardWise();
        List<Map<String, Object>> wardWiseData = new LinkedList<>();
        double topCount = -1;
        for (Object[] wardData : compRedrsdCount) {
            Map<String, Object> wardwiseCompRedressed = new HashMap<>();
            wardwiseCompRedressed.put(WARDNAME, String.valueOf(wardData[0]));
            wardwiseCompRedressed.put(WARDID, ((BigDecimal) wardData[1]).intValue());
            double count = ((BigDecimal) wardData[2]).doubleValue();
            wardwiseCompRedressed.put(COUNT, count);
            if (topCount < 0)
                topCount = count;
            applyColorCodes(wardwiseCompRedressed, count * 100 / topCount, "#4F54B8");
            wardWiseData.add(wardwiseCompRedressed);
        }
        return wardWiseData;
    }

    public List<Map<String, Object>> getGISRegCompWardWise() {

        List<Object[]> compCount = dashboardRepository.fetchGISRegCompWardWise();
        List<Map<String, Object>> wardWiseData = new LinkedList<>();
        double topCount = -1;
        for (Object[] wardData : compCount) {
            Map<String, Object> wardwiseCnt = new HashMap<>();
            wardwiseCnt.put(WARDNAME, String.valueOf(wardData[0]));
            wardwiseCnt.put(WARDID, ((BigInteger) wardData[1]).intValue());
            double count = ((BigInteger) wardData[2]).doubleValue();
            wardwiseCnt.put(COUNT, count);
            if (topCount < 0)
                topCount = count;
            applyColorCodes(wardwiseCnt, count * 100 / topCount, "#C00000");
            wardWiseData.add(wardwiseCnt);
        }
        return wardWiseData;
    }

    private void applyColorCodes(Map<String, Object> dataMap, double perc, String colorCode) {
        String[] colors = COLOR_GRADIENTS.get(colorCode);
        if (perc <= 20)
            dataMap.put(COLOR, colors[4]);
        else if (perc <= 40.0)
            dataMap.put(COLOR, colors[3]);
        else if (perc <= 60.0)
            dataMap.put(COLOR, colors[2]);
        else if (perc <= 80.0)
            dataMap.put(COLOR, colors[1]);
        else
            dataMap.put(COLOR, colors[0]);
    }
}
