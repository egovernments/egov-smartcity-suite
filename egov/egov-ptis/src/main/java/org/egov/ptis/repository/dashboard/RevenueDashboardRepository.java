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
package org.egov.ptis.repository.dashboard;

import org.egov.ptis.config.PTISApplicationProperties;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.egov.infra.utils.DateUtils.endOfGivenDate;
import static org.egov.infra.utils.DateUtils.startOfGivenDate;

/**
 *
 * @author subhash
 *
 */
@Repository
@SuppressWarnings("all")
public class RevenueDashboardRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PTISApplicationProperties ptisApplicationProperties;

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<Map<String, Object>> getRevenueZonewisePerformance() {
        final List<Object[]> overallData = getQuery("revenue.ptis.zonewise.overall.performance").list();
        final Map<String, Map<String, Object>> revenueDataHolder = new HashMap<String, Map<String, Object>>();
        for (final Object[] revenueObj : overallData) {
            final Map<String, Object> revnData = new HashMap<String, Object>();
            revnData.put("zone", String.valueOf(revenueObj[0]));
            final BigDecimal collectionPerc = (BigDecimal) revenueObj[1];
            revnData.put("collectionPerc", collectionPerc != null ? collectionPerc.doubleValue() : "0");
            revenueDataHolder.put(String.valueOf(revenueObj[0]), revnData);
        }

        final List<Object[]> monthlyData = getQuery("revenue.ptis.zonewise.monthly.performance").list();
        for (final Object[] revenuData : monthlyData) {
            final Map<String, Object> revnData = revenueDataHolder.get(String.valueOf(revenuData[0]));
            final BigDecimal amtTargeted = (BigDecimal) revenuData[1];
            revnData.put("amtTargeted", amtTargeted != null ? amtTargeted.doubleValue() : "0");
            final BigDecimal amt_collectd = (BigDecimal) revenuData[2];
            revnData.put("amt_collectd", amt_collectd != null ? amt_collectd.doubleValue() : "0");
            final BigDecimal percCollections = (BigDecimal) revenuData[3];
            revnData.put("percCollections", percCollections != null ? percCollections.setScale(2, RoundingMode.CEILING)
                    .doubleValue() : "0");
        }

        final List<Map<String, Object>> revenueAggrData = new ArrayList<Map<String, Object>>(revenueDataHolder.values());
        // SORT BY ZONEWISE MONTHLY COLLECTION %
        sortData(revenueAggrData, "percCollections");

        // ASSIGN MONTHLY RANK BASED ON SORT ORDER
        assignRank(revenueAggrData, "rank");

        // SORT BY ZONEWISE OVERALL COLLECTION %
        sortData(revenueAggrData, "collectionPerc");

        // ASSIGN OVERALL RANK BASED ON SORT ORDER
        assignRank(revenueAggrData, "overallrank");

        return revenueAggrData;
    }

    public Map<String, Object> getRevenueZonewiseBar() {
        final List<Object[]> overAllData = getQuery("revenue.ptis.zonewise.overall.performance").list();
        final DecimalFormat df = new DecimalFormat("####0.00");
        final List<Map<String, Object>> overAllDataHolder = new LinkedList<Map<String, Object>>();
        final Map<String, BigDecimal> overallCollPercHolder = new HashMap<String, BigDecimal>();
        for (final Object[] revenueObj : overAllData) {
            final Map<String, Object> revnOverAllData = new HashMap<String, Object>();
            revnOverAllData.put("name", String.valueOf(revenueObj[0]));
            final BigDecimal collectionPerc = (BigDecimal) revenueObj[1];
            overallCollPercHolder.put(String.valueOf(revenueObj[0]), collectionPerc != null ? collectionPerc : BigDecimal.ZERO);
            revnOverAllData.put("y", collectionPerc != null ? new BigDecimal(df.format(collectionPerc.doubleValue())) : 0);
            overAllDataHolder.add(revnOverAllData);
        }

        final List<Object[]> monthlyData = getQuery("revenue.ptis.zonewise.monthly.performance").list();
        final List<Map<String, Object>> monthlyDataHolder = new LinkedList<Map<String, Object>>();
        for (final Object[] revenueObj : monthlyData) {
            final Map<String, Object> revnMonthlyData = new HashMap<String, Object>();
            revnMonthlyData.put("name", String.valueOf(revenueObj[0]));
            final BigDecimal mnthlyCollectionPerc = (BigDecimal) revenueObj[3];
            revnMonthlyData.put("y", mnthlyCollectionPerc != null ? new BigDecimal(df.format(mnthlyCollectionPerc.doubleValue()))
            : 0);
            revnMonthlyData.put("overAllPerc",
                    new BigDecimal(df.format(overallCollPercHolder.get(String.valueOf(revenueObj[0])).doubleValue())));
            monthlyDataHolder.add(revnMonthlyData);
        }

        // SORT BY ZONEWISE MONTHLY COLLECTION %
        sortData(monthlyDataHolder, "y");

        // SORT BY ZONEWISE OVERALL COLLECTION %
        sortData(overAllDataHolder, "y");

        // SORT BACK MONTHLY BY ZONEWISE OVERALL COLLECTION %
        sortData(monthlyDataHolder, "overAllPerc");

        final Map<String, Object> revenueAggrData = new HashMap<String, Object>();
        revenueAggrData.put("overallPerc", overAllDataHolder);
        revenueAggrData.put("monthlyPerc", monthlyDataHolder);

        return revenueAggrData;
    }

    public List<Map<String, Object>> getWardwisePerformanceTab(final String zoneName) {
        final SQLQuery overAllQry = getQuery("revenue.ptis.wardwise.overall.performance");
        overAllQry.setParameter("zoneName", zoneName);
        final List<Object[]> overAllData = overAllQry.list();
        final Map<String, Map<String, Object>> revenueDataHolder = new HashMap<String, Map<String, Object>>();
        for (final Object[] revenueObj : overAllData) {
            final Map<String, Object> revnData = new HashMap<String, Object>();
            revnData.put("ward", String.valueOf(revenueObj[0]));
            final BigDecimal collectionPerc = (BigDecimal) revenueObj[2];
            revnData.put("collectionPerc", collectionPerc != null ? collectionPerc.doubleValue() : "0");
            revenueDataHolder.put(String.valueOf(revenueObj[0]), revnData);
        }

        final SQLQuery monthlyQry = getQuery("revenue.ptis.wardwise.monthly.performance");
        monthlyQry.setParameter("zoneName", zoneName);
        final List<Object[]> monthlyData = monthlyQry.list();
        for (final Object[] revenueObj : monthlyData) {
            final Map<String, Object> revnData = revenueDataHolder.get(String.valueOf(revenueObj[0]));
            final BigDecimal amtTargeted = (BigDecimal) revenueObj[1];
            revnData.put("amtTargeted", amtTargeted != null ? amtTargeted.doubleValue() : "0");
            final BigDecimal amt_collectd = (BigDecimal) revenueObj[2];
            revnData.put("amt_collectd", amt_collectd != null ? amt_collectd.doubleValue() : "0");
            final BigDecimal percCollections = (BigDecimal) revenueObj[3];
            revnData.put("percCollections", percCollections != null ? percCollections.setScale(2, RoundingMode.CEILING)
                    .doubleValue() : "0");
        }

        final List<Map<String, Object>> revenueAggrData = new ArrayList<Map<String, Object>>(revenueDataHolder.values());

        // SORT BY WARDWISE MONTHLY COLLECTION %
        sortData(revenueAggrData, "percCollections");

        // ASSIGN MONTHLY RANK BASED ON SORT ORDER
        assignRank(revenueAggrData, "rank");

        // SORT BY WARDWISE OVERALL COLLECTION %
        sortData(revenueAggrData, "collectionPerc");

        // ASSIGN OVERALL RANK BASED ON SORT ORDER
        assignRank(revenueAggrData, "overallrank");

        return revenueAggrData;
    }

    public List<Map<String, Object>> revenueTrendForTheWeek() {
        final Query qry = getQuery("revenue.ptis.collectiontrend");
        final DateTime currentDate = new DateTime();
        qry.setParameter("fromDate", startOfGivenDate(currentDate.minusDays(6)).toDate());
        qry.setParameter("toDate", endOfGivenDate(currentDate).toDate());
        final List<Object[]> revenueData = qry.list();
        final List<Map<String, Object>> currentYearTillDays = constructDayPlaceHolder(currentDate.minusDays(6),
                currentDate, "E-dd", "EEEE, dd MMM yyyy");
        for (final Object[] revnueObj : revenueData)
            for (final Map<String, Object> mapdata : currentYearTillDays)
                if (mapdata.containsValue(org.apache.commons.lang.StringUtils.capitalize(String.valueOf(revnueObj[0])
                        .toLowerCase())))
                    mapdata.put("y", Double.valueOf(String.valueOf(revnueObj[1])));
        return currentYearTillDays;
    }

    public Map<String, Collection<Double>> targetVsAchieved() {
        final DateTime currentDate = new DateTime();
        final List<Object[]> targets = getQuery("revenue.ptis.target").list();
        final List<Object[]> finyear = getCurrentFinYear().list();
        final DateTime finYearStartDate = new DateTime(finyear.get(0)[0]);
        final DateTime finYearEndDate = new DateTime(finyear.get(0)[1]);
        final DateTime lastFinYearStartDate = finYearStartDate.minusMonths(12);
        final DateTime lastFinYearEndDate = finYearEndDate.minusMonths(12);
        final Map<String, Double> currentTarget = constructDatePlaceHolderForDouble(finYearStartDate, finYearEndDate, "MM-yyyy");
        final Map<String, Double> cumilativeTarget = constructDatePlaceHolderForDouble(finYearStartDate, finYearEndDate,
                "MM-yyyy");
        for (final Object[] target : targets) {
            currentTarget.put(String.valueOf(target[0]), Double.valueOf(String.valueOf(target[1])));
            cumilativeTarget.put(String.valueOf(target[0]), Double.valueOf(String.valueOf(target[2])));
        }
        final Map<String, Double> currentAcheived = constructDatePlaceHolderForDouble(finYearStartDate, currentDate, "MM-yyyy");
        final Map<String, Double> cumilativeAcheived = constructDatePlaceHolderForDouble(finYearStartDate, currentDate, "MM-yyyy");
        final List<Object[]> achieved = getQuery("revenue.ptis.achieved").list();
        for (final Object[] achieve : achieved) {
            currentAcheived.put(String.valueOf(achieve[0]), Double.valueOf(String.valueOf(achieve[1])));
            cumilativeAcheived.put(String.valueOf(achieve[0]), Double.valueOf(String.valueOf(achieve[2])));
        }
        final Map<String, Double> lastAcheived = constructDatePlaceHolderForDouble(lastFinYearStartDate, lastFinYearEndDate,
                "MM-yyyy");
        final Map<String, Double> lastcumilativeAcheived = constructDatePlaceHolderForDouble(lastFinYearStartDate,
                lastFinYearEndDate, "MM-yyyy");
        final Query qry = getQuery("revenue.ptis.last.achieved");
        qry.setParameter("finStartDate", startOfGivenDate(lastFinYearStartDate).toDate());
        qry.setParameter("finEndDate", startOfGivenDate(lastFinYearEndDate).toDate());
        final List<Object[]> lastAchieved = qry.list();
        for (final Object[] lastachieved : lastAchieved) {
            lastAcheived.put(String.valueOf(lastachieved[0]), Double.valueOf(String.valueOf(lastachieved[1])));
            lastcumilativeAcheived.put(String.valueOf(lastachieved[0]), Double.valueOf(String.valueOf(lastachieved[2])));
        }

        final Map<String, Collection<Double>> targetVsAchieved = new HashMap<String, Collection<Double>>();
        targetVsAchieved.put("target", currentTarget.values());
        targetVsAchieved.put("achieved", currentAcheived.values());
        targetVsAchieved.put("lastAcheived", lastAcheived.values());
        targetVsAchieved.put("cumilativetarget", cumilativeTarget.values());
        targetVsAchieved.put("cumilativeachieved", cumilativeAcheived.values());
        targetVsAchieved.put("lastcumilativeachieved", lastcumilativeAcheived.values());
        return targetVsAchieved;
    }

    public Map<String, Object> collectionsPaymentMode() {
        final List<Object[]> typeCollection = getQuery("revenue.ptis.collecion.payment.type").list();
        final BigDecimal totalTransactions = BigDecimal.valueOf((Double) getQuery("revenue.ptis.collecion.total").uniqueResult());
        final DecimalFormat df = new DecimalFormat("####0.00");
        final List<Map<String, Object>> overAllCollHolder = new LinkedList<Map<String, Object>>();
        final Map<String, BigDecimal> overallCollPercHolder = new HashMap<String, BigDecimal>();
        for (final Object[] collObj : typeCollection) {
            final Map<String, Object> collPaymentType = new HashMap<String, Object>();
            collPaymentType.put("name", String.valueOf(collObj[0]));
            BigDecimal collectionPerc = collObj[1] != null ? new BigDecimal(collObj[1].toString()) : BigDecimal.ZERO;
            collectionPerc = collectionPerc.multiply(BigDecimal.valueOf(100)).divide(totalTransactions, 2, RoundingMode.HALF_UP);
            overallCollPercHolder.put(String.valueOf(collObj[0]), collectionPerc != null ? collectionPerc : BigDecimal.ZERO);
            collPaymentType.put("y", collectionPerc != null ? new BigDecimal(df.format(collectionPerc.doubleValue())) : 0);
            overAllCollHolder.add(collPaymentType);
        }

        final List<Object[]> totalNoTransactions = getQuery("revenue.ptis.collecion.total.type").list();
        final List<Map<String, Object>> totalPercTrans = new LinkedList<Map<String, Object>>();
        final BigDecimal totalCount = new BigDecimal(getQuery("revenue.ptis.collecion.total.count").uniqueResult().toString());
        for (final Object[] revenueObj : totalNoTransactions) {
            final Map<String, Object> revnTotalTransData = new HashMap<String, Object>();
            revnTotalTransData.put("name", String.valueOf(revenueObj[0]));
            BigDecimal numberTransactions = revenueObj[1] != null ? new BigDecimal(revenueObj[1].toString()) : BigDecimal.ZERO;
            numberTransactions = numberTransactions.multiply(BigDecimal.valueOf(100)).divide(totalCount, 2, RoundingMode.HALF_UP);
            revnTotalTransData.put("y", numberTransactions != null ? new BigDecimal(df.format(numberTransactions.doubleValue()))
            : 0);
            revnTotalTransData.put("overAllPerc",
                    new BigDecimal(df.format(overallCollPercHolder.get(String.valueOf(revenueObj[0])).doubleValue())));
            totalPercTrans.add(revnTotalTransData);
        }
        // SORT BY TOTAL COUNT %
        sortData(totalPercTrans, "y");

        // SORT BY COLLECTION %
        sortData(overAllCollHolder, "y");

        // SORT BACK BY OVER ALL COUNT %
        sortData(totalPercTrans, "overAllPerc");

        final Map<String, Object> revenueAggrData = new HashMap<String, Object>();
        revenueAggrData.put("overallColl", overAllCollHolder);
        revenueAggrData.put("totalTransPerc", totalPercTrans);

        return revenueAggrData;
    }

    public Map<String, Object> coverageEfficiency() {
        final List<Object[]> overallData = getQuery("revenue.ptis.coverage.efficiency").list();
        final Map<String, Map<String, Object>> coverageDataHolder = new HashMap<String, Map<String, Object>>();
        for (final Object[] revenueObj : overallData) {
            final Map<String, Object> revnData = new HashMap<String, Object>();
            revnData.put("name", String.valueOf(revenueObj[0]));
            final BigDecimal noOfProps = revenueObj[1] != null ? new BigDecimal(revenueObj[1].toString()) : BigDecimal.ZERO;
            revnData.put("noOfProps", noOfProps != null ? noOfProps.doubleValue() : "0");
            final BigDecimal noOfTaxProps = revenueObj[2] != null ? new BigDecimal(revenueObj[2].toString()) : BigDecimal.ZERO;
            revnData.put("noOfTaxProps", noOfTaxProps != null ? noOfTaxProps.doubleValue() : "0");
            final BigDecimal coverageEfficiency = (BigDecimal) revenueObj[3];
            revnData.put("y", coverageEfficiency != null ? coverageEfficiency.doubleValue() : "0");
            coverageDataHolder.put(String.valueOf(revenueObj[0]), revnData);
        }

        final List<Map<String, Object>> revenueCovEfficiency = new ArrayList<Map<String, Object>>(coverageDataHolder.values());
        // SORT BY ZONEWISE Coverage Efficiency %
        sortData(revenueCovEfficiency, "y");

        final Map<String, Object> revenueAggrData = new HashMap<String, Object>();
        revenueAggrData.put("overallCoverage", revenueCovEfficiency);

        return revenueAggrData;
    }

    public Map<String, Object> coverageEfficiencyWard(final String zoneName) {
        final SQLQuery overAllQry = getQuery("revenue.ptis.coverage.efficiency.ward");
        overAllQry.setParameter("zoneName", zoneName);
        final List<Object[]> overAllData = overAllQry.list();
        final Map<String, Map<String, Object>> coverageDataHolder = new HashMap<String, Map<String, Object>>();
        for (final Object[] revenueObj : overAllData) {
            final Map<String, Object> revnData = new HashMap<String, Object>();
            revnData.put("name", String.valueOf(revenueObj[0]));
            final BigDecimal noOfProps = revenueObj[1] != null ? new BigDecimal(revenueObj[1].toString())
                    : BigDecimal.ZERO;
            revnData.put("noOfProps", noOfProps != null ? noOfProps.doubleValue() : "0");
            final BigDecimal noOfTaxProps = revenueObj[2] != null ? new BigDecimal(revenueObj[2].toString())
                    : BigDecimal.ZERO;
            revnData.put("noOfTaxProps", noOfTaxProps != null ? noOfTaxProps.doubleValue() : "0");
            final BigDecimal coverageEfficiency = revenueObj[3] != null ? new BigDecimal(revenueObj[3].toString())
                    : BigDecimal.ZERO;
            revnData.put("y", coverageEfficiency != null ? coverageEfficiency.doubleValue() : "0");
            coverageDataHolder.put(String.valueOf(revenueObj[0]), revnData);
        }

        final List<Map<String, Object>> revenueCovEfficiency = new ArrayList<Map<String, Object>>(coverageDataHolder.values());
        // SORT BY WARDWISE Coverage Efficiency %
        sortData(revenueCovEfficiency, "y");

        final Map<String, Object> revenueAggrData = new HashMap<String, Object>();
        revenueAggrData.put("overallCoverage", revenueCovEfficiency);

        return revenueAggrData;
    }

    public Map<String, List<Object>> getAnnualZonewiseBar() {
        final List<Object[]> annualData = getQuery("revenue.annual.performance").list();
        final List<Object> dataholder1 = new LinkedList<Object>();
        final List<Object> dataholder2 = new LinkedList<Object>();
        final Map<String, List<Object>> revenueAggrData = new HashMap<String, List<Object>>();
        for (final Object[] revenueObj : annualData) {
            final Map<String, Object> targetData = new LinkedHashMap<String, Object>();
            final Map<String, Object> actualData = new LinkedHashMap<String, Object>();
            targetData.put("name", String.valueOf(revenueObj[0]));
            actualData.put("name", String.valueOf(revenueObj[0]));
            final BigDecimal targeted = (BigDecimal) revenueObj[1];
            final BigDecimal actual = (BigDecimal) revenueObj[2];
            targetData.put("y", targeted != null ? targeted.doubleValue() : "0");
            actualData.put("y", actual != null ? actual.doubleValue() : "0");

            dataholder1.add(targetData);
            dataholder2.add(actualData);
        }
        revenueAggrData.put("targeted", dataholder1);
        revenueAggrData.put("actual", dataholder2);
        return revenueAggrData;
    }

    private static void sortData(final List<Map<String, Object>> dataList, final String key) {
        Collections.sort(dataList, (map1, map2) -> {
            return Double.valueOf(map1.get(key).toString()) <= Double.valueOf(map2.get(key).toString()) ? 1 : -1;
        });
    }

    private static void assignRank(final List<Map<String, Object>> dataList, final String key) {
        int counter = 1;
        for (final Map<String, Object> map : dataList)
            map.put(key, counter++);
    }

    private SQLQuery getQuery(final String sqlKey) {
        return entityManager.unwrap(Session.class)
                .createSQLQuery(ptisApplicationProperties.getValue(sqlKey));
    }

    public static Map<String, Double> constructDatePlaceHolderForDouble(final DateTime startDate, final DateTime endDate,
            final String pattern) {
        final Map<String, Double> currentYearTillDays = new LinkedHashMap<String, Double>();
        for (DateTime date = startDate; date.isBefore(endDate); date = date.plusDays(1))
            currentYearTillDays.put(date.toString(pattern), Double.valueOf(0));
        currentYearTillDays.put(endDate.toString(pattern), Double.valueOf(0));
        return currentYearTillDays;
    }

    public static List<Map<String, Object>> constructDayPlaceHolder(final DateTime startDate, final DateTime endDate,
            final String pattern, final String toolTipPattern) {
        final Map<String, Object> currentYearEndDays = new LinkedHashMap<String, Object>();
        final List<Map<String, Object>> dataHolder = new LinkedList<Map<String, Object>>();
        for (DateTime date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            final Map<String, Object> currentYearTillDays = new LinkedHashMap<String, Object>();
            currentYearTillDays.put("name", date.toString(pattern));
            currentYearTillDays.put("y", Double.valueOf(0));
            currentYearTillDays.put("tooltipFormat", date.toString(toolTipPattern));
            dataHolder.add(currentYearTillDays);
        }
        currentYearEndDays.put("name", endDate.toString(pattern));
        currentYearEndDays.put("y", Double.valueOf(0));
        currentYearEndDays.put("tooltipFormat", endDate.toString(toolTipPattern));
        dataHolder.add(currentYearEndDays);
        return dataHolder;
    }

    protected SQLQuery getCurrentFinYear() {
        return getSession().createSQLQuery(ptisApplicationProperties.getValue("revenue.ptis.finyear"));
    }
}
