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

package org.egov.wtms.service.es;

import static org.egov.wtms.utils.constants.WaterTaxConstants.COLLECION_BILLING_SERVICE_WTMS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DASHBOARD_GROUPING_DISTRICTWISE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DASHBOARD_GROUPING_GRADEWISE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DASHBOARD_GROUPING_REGIONWISE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DASHBOARD_GROUPING_ULBWISE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DASHBOARD_GROUPING_WARDWISE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DATE_FORMAT_YYYYMMDD;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATER_TAX_INDEX_NAME;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.es.BillCollectorIndex;
import org.egov.wtms.bean.dashboard.WaterChargeDashBoardRequest;
import org.egov.wtms.bean.dashboard.WaterChargeDashBoardResponse;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class WaterChargeCollectionDocService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaterChargeCollectionDocService.class);

    private static final String consumerCodeIndex = "consumerCode";
    private static final String receiptcountIndex = "receipt_count";
    private static final String receiptDateIndex = "receiptDate";
    private static final String collectiontotal = "collectiontotal";

    @Autowired
    private CFinancialYearService cFinancialYearService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * Gives the consolidated collection for the dates and billing service
     *
     * @param fromDate
     * @param toDate
     * @param billingService
     * @return BigDecimal
     */
    public BigDecimal getConsolidatedCollForYears(final Date fromDate, final Date toDate, final String billingService) {
        final QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery(receiptDateIndex)
                        .gte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                .must(QueryBuilders.matchQuery("billingService", billingService))
                .mustNot(QueryBuilders.matchQuery("status", "Cancelled"));
        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder()
                .withIndices(WaterTaxConstants.COLLECTION_INDEX_NAME).withQuery(queryBuilder)
                .addAggregation(AggregationBuilders.sum(collectiontotal).field("totalAmount")).build();

        final Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());

        final Sum aggr = collAggr.get(collectiontotal);
        return BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Gives the consolidated collection for the current Fin year and last fin
     * year
     *
     * @param billingService
     * @return Map
     */
    public Map<String, BigDecimal> getFinYearsCollByService(final String billingService) {
        /**
         * As per Elastic Search functionality, to get the total collections
         * between 2 dates, add a day to the endDate and fetch the results
         */
        final Map<String, BigDecimal> consolidatedCollValues = new HashMap<>();
        final CFinancialYear currFinYear = cFinancialYearService.getFinancialYearByDate(new Date());
        // For current year results
        consolidatedCollValues.put("cytdColln", getConsolidatedCollForYears(currFinYear.getStartingDate(),
                org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1), billingService));
        // For last year results
        consolidatedCollValues
                .put("lytdColln",
                        getConsolidatedCollForYears(
                                org.apache.commons.lang3.time.DateUtils.addYears(currFinYear.getStartingDate(), -1),
                                org.apache.commons.lang3.time.DateUtils
                                        .addDays(org.apache.commons.lang3.time.DateUtils.addYears(new Date(), -1), 1),
                                billingService));
        return consolidatedCollValues;
    }

    /**
     * Builds query based on the input parameters sent
     *
     * @param collectionDetailsRequest
     * @param indexName
     * @param ulbCodeField
     * @return BoolQueryBuilder
     */
    public BoolQueryBuilder prepareWhereClause(final WaterChargeDashBoardRequest collectionDetailsRequest,
            final String indexName) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (indexName == null)
            boolQuery = boolQuery
                    .filter(QueryBuilders.rangeQuery(WaterTaxConstants.WATERCHARGETOTALDEMAND).from(0).to(null));
        else if (WaterTaxConstants.WATER_TAX_INDEX_NAME.equals(indexName))
            boolQuery = boolQuery
                    .filter(QueryBuilders.rangeQuery(WaterTaxConstants.WATERCHARGETOTALDEMAND).from(0).to(null));
        else if (indexName.equals(WaterTaxConstants.COLLECTION_INDEX_NAME))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("billingService", COLLECION_BILLING_SERVICE_WTMS));
        if (boolQuery != null) {
            if (StringUtils.isNotBlank(collectionDetailsRequest.getRegionName()))
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery(WaterTaxConstants.REGIONNAMEAGGREGATIONFIELD,
                        collectionDetailsRequest.getRegionName()));
            if (StringUtils.isNotBlank(collectionDetailsRequest.getDistrictName()))
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery(WaterTaxConstants.DISTRICTNAMEAGGREGATIONFIELD,
                        collectionDetailsRequest.getDistrictName()));

            if (StringUtils.isNotBlank(collectionDetailsRequest.getUlbGrade()))
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery(WaterTaxConstants.CITYGRADEAGGREGATIONFIELD,
                        collectionDetailsRequest.getUlbGrade()));

            if (StringUtils.isNotBlank(collectionDetailsRequest.getUlbCode()))
                boolQuery = boolQuery
                        .filter(QueryBuilders.matchQuery("cityCode", collectionDetailsRequest.getUlbCode()));
        }
        return boolQuery;
    }

    /**
     * Returns total demand from WaterCharge index, based on input filters
     *
     * @param collectionDetailsRequest
     * @return
     */
    public BigDecimal getTotalDemandBasedOnInputFilters(final WaterChargeDashBoardRequest collectionDetailsRequest) {
        final BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest, null);
        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder()
                .withIndices(WATER_TAX_INDEX_NAME).withQuery(boolQuery).addAggregation(AggregationBuilders
                        .sum(WaterTaxConstants.WATERCHARGETOTALDEMAND).field(WaterTaxConstants.WATERCHARGETOTALDEMAND))
                .build();

        final Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());

        final Sum aggr = collAggr.get(WaterTaxConstants.WATERCHARGETOTALDEMAND);
        return BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * API sets the consolidated collections for single day and between the 2
     * dates
     *
     * @param collectionDetailsRequest
     * @param collectionIndexDetails
     */
    public List<WaterChargeDashBoardResponse> getCompleteCollectionIndexDetails(
            final WaterChargeDashBoardRequest collectionDetailsRequest) {

        final List<WaterChargeDashBoardResponse> collectionIndexDetailsList = new ArrayList<WaterChargeDashBoardResponse>();
        final WaterChargeDashBoardResponse collectionIndexDetails = new WaterChargeDashBoardResponse();
        Date fromDate;
        Date toDate;
        BigDecimal todayColl = BigDecimal.ZERO;
        BigDecimal tillDateColl = BigDecimal.ZERO;
        final Long startTime = System.currentTimeMillis();
        /**
         * As per Elastic Search functionality, to get the total collections
         * between 2 dates, add a day to the endDate and fetch the results For
         * Current day's collection if dates are sent in the request, consider
         * the toDate, else take date range between current date +1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD);
            toDate = org.apache.commons.lang3.time.DateUtils
                    .addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD), 1);
        } else {
            fromDate = new Date();
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(fromDate, 1);
        }
        // Today’s collection
        todayColl = getCollectionBetweenDates(collectionDetailsRequest, fromDate, toDate, null);
        collectionIndexDetails.setTodayColl(todayColl);

        // Last year Today’s day collection
        todayColl = getCollectionBetweenDates(collectionDetailsRequest,
                org.apache.commons.lang3.time.DateUtils.addYears(fromDate, -1),
                org.apache.commons.lang3.time.DateUtils.addYears(toDate, -1), null);
        collectionIndexDetails.setLastYearTodayColl(todayColl);

        /**
         * For collections between the date ranges if dates are sent in the
         * request, consider the same, else calculate from current year start
         * date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = org.apache.commons.lang3.time.DateUtils
                    .addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD), 1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1);
        }
        // Current Year till today collection
        tillDateColl = getCollectionBetweenDates(collectionDetailsRequest, fromDate, toDate, null);
        collectionIndexDetails.setCurrentYearTillDateColl(tillDateColl);

        // Last year till same date of today’s date collection
        tillDateColl = getCollectionBetweenDates(collectionDetailsRequest,
                org.apache.commons.lang3.time.DateUtils.addYears(fromDate, -1),
                org.apache.commons.lang3.time.DateUtils.addYears(toDate, -1), null);
        collectionIndexDetails.setLastYearTillDateColl(tillDateColl);
        final Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken by getCompleteCollectionIndexDetails() is (millisecs) : " + timeTaken);
        /**
         * For fetching total demand between the date ranges if dates are sent
         * in the request, consider fromDate and toDate+1 , else calculate from
         * current year start date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), "yyyy-MM-dd");
            toDate = org.apache.commons.lang3.time.DateUtils
                    .addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), "yyyy-MM-dd"), 1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1);
        }
        // starts from
        final BigDecimal totalDemand = getTotalDemandBasedOnInputFilters(collectionDetailsRequest);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken by getTotalDemandBasedOnInputFilters() is (millisecs): " + timeTaken);
        final int noOfMonths = DateUtils.noOfMonths(fromDate, toDate) + 1;
        collectionIndexDetails.setTotalDmd(totalDemand);

        // Proportional Demand = (totalDemand/12)*noOfmonths
        final BigDecimal proportionalDemand = totalDemand.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(noOfMonths));
        if (proportionalDemand.compareTo(BigDecimal.ZERO) > 0)
            collectionIndexDetails.setCurrentYearTillDateDmd(proportionalDemand.setScale(0, BigDecimal.ROUND_HALF_UP));
        // performance = (current year tilldate collection * 100)/(proportional
        // demand)
        if (proportionalDemand.compareTo(BigDecimal.ZERO) > 0)
            collectionIndexDetails.setPerformance(
                    collectionIndexDetails.getCurrentYearTillDateColl().multiply(WaterTaxConstants.BIGDECIMAL_100)
                            .divide(proportionalDemand, 1, BigDecimal.ROUND_HALF_UP));
        // variance = ((currentYearCollection -
        // lastYearCollection)*100)/lastYearCollection
        BigDecimal variation;
        if (collectionIndexDetails.getLastYearTillDateColl().compareTo(BigDecimal.ZERO) == 0)
            variation = WaterTaxConstants.BIGDECIMAL_100;
        else
            variation = collectionIndexDetails.getCurrentYearTillDateColl()
                    .subtract(collectionIndexDetails.getLastYearTillDateColl())
                    .multiply(WaterTaxConstants.BIGDECIMAL_100)
                    .divide(collectionIndexDetails.getLastYearTillDateColl(), 1, BigDecimal.ROUND_HALF_UP);
        collectionIndexDetails.setLastYearVar(variation);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken for setting values in getConsolidatedDemandInfo() is (millisecs): " + timeTaken);
        collectionIndexDetailsList.add(collectionIndexDetails);
        return collectionIndexDetailsList;
    }

    /**
     * Returns the consolidated collections for single day and between the 2
     * dates
     *
     * @param collectionDetailsRequest
     * @param fromDate
     * @param toDate
     * @param cityName
     * @return BigDecimal
     */
    public BigDecimal getCollectionBetweenDates(final WaterChargeDashBoardRequest collectionDetailsRequest,
            final Date fromDate, final Date toDate, final String cityName) {
        final Long startTime = System.currentTimeMillis();

        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest,
                WaterTaxConstants.COLLECTION_INDEX_NAME);
        boolQuery = boolQuery
                .filter(QueryBuilders.rangeQuery(receiptDateIndex)
                        .gte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                .mustNot(QueryBuilders.matchQuery("status", "Cancelled"));
        if (StringUtils.isNotBlank(cityName))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(WaterTaxConstants.CITYNAMEAGGREGATIONFIELD, cityName));

        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder()
                .withIndices(WaterTaxConstants.COLLECTION_INDEX_NAME).withQuery(boolQuery)
                .addAggregation(AggregationBuilders.sum(collectiontotal).field("totalAmount")).build();

        final Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());

        final Sum aggr = collAggr.get(collectiontotal);
        final Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken by getCollectionBetweenDates() is (millisecs) : " + timeTaken);
        return BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Prepares Collection Table Data
     *
     * @param collectionDetailsRequest
     * @return List
     */
    public List<WaterChargeDashBoardResponse> getResponseTableData(
            final WaterChargeDashBoardRequest collectionDetailsRequest) {
        final List<WaterChargeDashBoardResponse> collIndDataList = new ArrayList<>();
        Date fromDate;
        Date toDate;
        String name;
        WaterChargeDashBoardResponse collIndData;
        BigDecimal cytdDmd = BigDecimal.ZERO;
        BigDecimal performance = BigDecimal.ZERO;
        BigDecimal balance = BigDecimal.ZERO;
        BigDecimal variance = BigDecimal.ZERO;
        String aggregationField = WaterTaxConstants.REGIONNAMEAGGREGATIONFIELD;

        /**
         * Select the grouping based on the type parameter, by default the
         * grouping is done based on Regions. If type is region, group by
         * Region, if type is district, group by District, if type is ulb, group
         * by ULB
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType()))
            if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_REGIONWISE))
                aggregationField = WaterTaxConstants.REGIONNAMEAGGREGATIONFIELD;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_DISTRICTWISE))
                aggregationField = WaterTaxConstants.DISTRICTNAMEAGGREGATIONFIELD;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ULBWISE))
                aggregationField = WaterTaxConstants.CITYNAMEAGGREGATIONFIELD;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_GRADEWISE))
                aggregationField = WaterTaxConstants.CITYGRADEAGGREGATIONFIELD;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_WARDWISE))
                aggregationField = WaterTaxConstants.REVENUEWARDAGGREGATIONFIELD;

        /**
         * As per Elastic Search functionality, to get the total collections
         * between 2 dates, add a day to the endDate and fetch the results For
         * Current day's collection if dates are sent in the request, consider
         * the toDate, else take date range between current date +1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD);
            toDate = org.apache.commons.lang3.time.DateUtils
                    .addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD), 1);
        } else {
            fromDate = new Date();
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(fromDate, 1);
        }

        Long startTime = System.currentTimeMillis();
        // For today collection
        final Map<String, BigDecimal> todayCollMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate,
                toDate, WaterTaxConstants.COLLECTION_INDEX_NAME, "totalAmount", aggregationField);

        /**
         * For collection and demand between the date ranges if dates are sent
         * in the request, consider fromDate and toDate+1 , else calculate from
         * current year start date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = org.apache.commons.lang3.time.DateUtils
                    .addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD), 1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1);
        }
        final int noOfMonths = DateUtils.noOfMonths(fromDate, toDate) + 1;

        // For current year's till date collection
        final Map<String, BigDecimal> cytdCollMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate,
                toDate, WaterTaxConstants.COLLECTION_INDEX_NAME, "totalAmount", aggregationField);
        // For total demand
        final Map<String, BigDecimal> totalDemandMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate,
                toDate, WATER_TAX_INDEX_NAME, WaterTaxConstants.WATERCHARGETOTALDEMAND, aggregationField);
        // For current year demand
        final Map<String, BigDecimal> currYrTotalDemandMap = getCollectionAndDemandValues(collectionDetailsRequest,
                fromDate, toDate, WATER_TAX_INDEX_NAME, WaterTaxConstants.WATERCHARGETOTALDEMAND, aggregationField);
        // For last year's till today's date collections
        final Map<String, BigDecimal> lytdCollMap = getCollectionAndDemandValues(collectionDetailsRequest,
                org.apache.commons.lang3.time.DateUtils.addYears(fromDate, -1),
                org.apache.commons.lang3.time.DateUtils.addYears(toDate, -1), WaterTaxConstants.COLLECTION_INDEX_NAME,
                "totalAmount", aggregationField);
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken by getCollectionAndDemandValues() is (millisecs) : " + timeTaken);
        startTime = System.currentTimeMillis();
        for (final Map.Entry<String, BigDecimal> entry : cytdCollMap.entrySet()) {
            collIndData = new WaterChargeDashBoardResponse();
            name = entry.getKey();
            if (aggregationField.equals(WaterTaxConstants.REGIONNAMEAGGREGATIONFIELD))
                collIndData.setRegionName(name);
            else if (aggregationField.equals(WaterTaxConstants.DISTRICTNAMEAGGREGATIONFIELD)) {
                collIndData.setRegionName(collectionDetailsRequest.getRegionName());
                collIndData.setDistrictName(name);
            } else if (WaterTaxConstants.CITYNAMEAGGREGATIONFIELD.equals(aggregationField)) {
                collIndData.setUlbName(name);
                collIndData.setDistrictName(collectionDetailsRequest.getDistrictName());
                collIndData.setUlbGrade(collectionDetailsRequest.getUlbGrade());
            } else if (aggregationField.equals(WaterTaxConstants.CITYGRADEAGGREGATIONFIELD))
                collIndData.setUlbGrade(name);
            else if (WaterTaxConstants.REVENUEWARDAGGREGATIONFIELD.equals(aggregationField))
                collIndData.setWardName(name);

            collIndData.setTodayColl(todayCollMap.get(name) == null ? BigDecimal.ZERO : todayCollMap.get(name));
            collIndData.setCurrentYearTillDateColl(entry.getValue());
            // Proportional Demand = (totalDemand/12)*noOfmonths
            final BigDecimal currentYearTotalDemand = currYrTotalDemandMap.get(name) == null ? BigDecimal.valueOf(0)
                    : currYrTotalDemandMap.get(name);
            cytdDmd = currentYearTotalDemand.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(noOfMonths));
            collIndData.setCurrentYearTillDateDmd(cytdDmd);
            if (cytdDmd != BigDecimal.valueOf(0)) {
                balance = cytdDmd.subtract(collIndData.getCurrentYearTillDateColl());
                performance = collIndData.getCurrentYearTillDateColl().multiply(WaterTaxConstants.BIGDECIMAL_100)
                        .divide(cytdDmd, 1, BigDecimal.ROUND_HALF_UP);
                collIndData.setPerformance(performance);
                collIndData.setCurrentYearTillDateBalDmd(balance);

            }
            collIndData.setTotalDmd(totalDemandMap.get(name) == null ? BigDecimal.ZERO : totalDemandMap.get(name));
            collIndData
                    .setLastYearTillDateColl(lytdCollMap.get(name) == null ? BigDecimal.ZERO : lytdCollMap.get(name));
            // variance = ((currentYearCollection -
            // lastYearCollection)*100)/lastYearCollection
            if (collIndData.getLastYearTillDateColl().compareTo(BigDecimal.ZERO) == 0)
                variance = WaterTaxConstants.BIGDECIMAL_100;
            else
                variance = collIndData.getCurrentYearTillDateColl().subtract(collIndData.getLastYearTillDateColl())
                        .multiply(WaterTaxConstants.BIGDECIMAL_100)
                        .divide(collIndData.getLastYearTillDateColl(), 1, BigDecimal.ROUND_HALF_UP);
            collIndData.setLastYearVar(variance);
            collIndDataList.add(collIndData);
        }

        timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken for setting values in getResponseTableData() is (millisecs): " + timeTaken);
        return collIndDataList;
    }

    /**
     * Provides collection and demand results
     *
     * @param collectionDetailsRequest
     * @param fromDate
     * @param toDate
     * @param indexName
     * @param fieldName
     * @param ulbCodeField
     * @param aggregationField
     * @return Map
     */
    public Map<String, BigDecimal> getCollectionAndDemandValues(
            final WaterChargeDashBoardRequest collectionDetailsRequest, final Date fromDate, final Date toDate,
            final String indexName, final String fieldName, final String aggregationField) {
        final String by_city = "by_city";
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest, indexName);
        if (indexName.equals(WaterTaxConstants.COLLECTION_INDEX_NAME))
            boolQuery = boolQuery
                    .filter(QueryBuilders.rangeQuery(receiptDateIndex)
                            .gte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                            .lte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                    .mustNot(QueryBuilders.matchQuery("status", "Cancelled"));

        final AggregationBuilder aggregation = AggregationBuilders.terms(by_city).field(aggregationField).size(120)
                .subAggregation(AggregationBuilders.sum("total").field(fieldName));

        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(indexName).withQuery(boolQuery)
                .addAggregation(aggregation).build();

        final Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());

        final StringTerms cityAggr = collAggr.get(by_city);
        final Map<String, BigDecimal> cytdCollMap = new HashMap<>();
        for (final Terms.Bucket entry : cityAggr.getBuckets()) {
            final Sum aggr = entry.getAggregations().get("total");
            cytdCollMap.put(String.valueOf(entry.getKey()),
                    BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
        }
        return cytdCollMap;
    }

    /**
     * Prepares month-wise collections for 3 consecutive years - from current
     * year
     *
     * @param collectionDetailsRequest
     * @return List
     */
    public List<WaterChargeDashBoardResponse> getMonthwiseCollectionDetails(
            final WaterChargeDashBoardRequest collectionDetailsRequest) {
        final List<WaterChargeDashBoardResponse> collTrendsList = new ArrayList<>();
        WaterChargeDashBoardResponse collTrend;
        Date fromDate;
        Date toDate;
        Date dateForMonth;
        String[] dateArr;
        Integer month;
        Sum aggregateSum;
        final CFinancialYear financialYear = cFinancialYearService.getFinancialYearByDate(new Date());
        Date finYearStartDate = financialYear.getStartingDate();
        Date finYearEndDate = financialYear.getEndingDate();
        final Map<Integer, String> monthValuesMap = DateUtils.getAllMonthsWithFullNames();
        String monthName;
        final List<Map<String, BigDecimal>> yearwiseMonthlyCollList = new ArrayList<>();
        Map<String, BigDecimal> monthwiseColl;
        /**
         * For month-wise collections between the date ranges if dates are sent
         * in the request, consider fromDate and toDate+1 , else calculate from
         * current year start date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = org.apache.commons.lang3.time.DateUtils
                    .addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD), 1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1);
        }
        Long startTime = System.currentTimeMillis();
        for (int count = 0; count <= 2; count++) {
            monthwiseColl = new LinkedHashMap<>();
            final Aggregations collAggr = getMonthwiseCollectionsForConsecutiveYears(collectionDetailsRequest, fromDate,
                    toDate);
            final Histogram dateaggs = collAggr.get("date_agg");

            for (final Histogram.Bucket entry : dateaggs.getBuckets()) {
                dateArr = entry.getKeyAsString().split("T");
                dateForMonth = DateUtils.getDate(dateArr[0], DATE_FORMAT_YYYYMMDD);
                month = Integer.valueOf(dateArr[0].split("-", 3)[1]);
                monthName = monthValuesMap.get(month);
                aggregateSum = entry.getAggregations().get("current_total");
                // If the total amount is greater than 0 and the month belongs
                // to respective financial year, add values to the map
                if (DateUtils.between(dateForMonth, finYearStartDate, finYearEndDate)
                        && BigDecimal.valueOf(aggregateSum.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)
                                .compareTo(BigDecimal.ZERO) > 0)
                    monthwiseColl.put(monthName,
                            BigDecimal.valueOf(aggregateSum.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            }
            yearwiseMonthlyCollList.add(monthwiseColl);

            /**
             * If dates are passed in request, get result for the date range,
             * else get results for entire financial year
             */
            if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                    && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
                fromDate = org.apache.commons.lang3.time.DateUtils.addYears(fromDate, -1);
                toDate = org.apache.commons.lang3.time.DateUtils.addYears(toDate, -1);
            } else {
                fromDate = org.apache.commons.lang3.time.DateUtils.addYears(finYearStartDate, -1);
                toDate = org.apache.commons.lang3.time.DateUtils.addYears(finYearEndDate, -1);
            }
            finYearStartDate = org.apache.commons.lang3.time.DateUtils.addYears(finYearStartDate, -1);
            finYearEndDate = org.apache.commons.lang3.time.DateUtils.addYears(finYearEndDate, -1);
        }
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(
                    "Time taken by getMonthwiseCollectionsForConsecutiveYears() for 3 consecutive years is (millisecs): "
                            + timeTaken);

        startTime = System.currentTimeMillis();
        /**
         * If dates are passed in request, get result for the date range, else
         * get results for all 12 months
         */
        if (StringUtils.isBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isBlank(collectionDetailsRequest.getToDate()))
            for (final Map.Entry<Integer, String> entry : DateUtils.getAllFinancialYearMonthsWithFullNames()
                    .entrySet()) {
                collTrend = new WaterChargeDashBoardResponse();
                collTrend.setMonth(entry.getValue());
                collTrend.setCurrentYearColl(yearwiseMonthlyCollList.get(0).get(collTrend.getMonth()) == null
                        ? BigDecimal.ZERO : yearwiseMonthlyCollList.get(0).get(collTrend.getMonth()));
                collTrend.setLastYearColl(yearwiseMonthlyCollList.get(1).get(collTrend.getMonth()) == null
                        ? BigDecimal.ZERO : yearwiseMonthlyCollList.get(1).get(collTrend.getMonth()));
                collTrend.setPreviousYearColl(yearwiseMonthlyCollList.get(2).get(collTrend.getMonth()) == null
                        ? BigDecimal.ZERO : yearwiseMonthlyCollList.get(2).get(collTrend.getMonth()));
                collTrendsList.add(collTrend);
            }
        else
            for (final Map.Entry<String, BigDecimal> entry : yearwiseMonthlyCollList.get(0).entrySet()) {
                collTrend = new WaterChargeDashBoardResponse();
                collTrend.setMonth(entry.getKey());
                collTrend.setCurrentYearColl(entry.getValue());
                collTrend.setLastYearColl(yearwiseMonthlyCollList.get(1).get(collTrend.getMonth()) == null
                        ? BigDecimal.ZERO : yearwiseMonthlyCollList.get(1).get(collTrend.getMonth()));
                collTrend.setPreviousYearColl(yearwiseMonthlyCollList.get(2).get(collTrend.getMonth()) == null
                        ? BigDecimal.ZERO : yearwiseMonthlyCollList.get(2).get(collTrend.getMonth()));
                collTrendsList.add(collTrend);
            }
        timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken setting values in getMonthwiseCollectionDetails() is (millisecs) : " + timeTaken);
        return collTrendsList;
    }

    /**
     * Provides month-wise collections for consecutive years
     *
     * @param collectionDetailsRequest
     * @param fromDate
     * @param toDate
     * @return SearchResponse
     */
    private Aggregations getMonthwiseCollectionsForConsecutiveYears(
            final WaterChargeDashBoardRequest collectionDetailsRequest, final Date fromDate, final Date toDate) {
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest,
                WaterTaxConstants.COLLECTION_INDEX_NAME);
        boolQuery = boolQuery.mustNot(QueryBuilders.matchQuery("status", "Cancelled"));
        @SuppressWarnings("rawtypes")
        final AggregationBuilder monthAggregation = AggregationBuilders.dateHistogram("date_agg")
                .field(receiptDateIndex).interval(DateHistogramInterval.MONTH)
                .subAggregation(AggregationBuilders.sum("current_total").field("totalAmount"));

        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder()
                .withIndices(WaterTaxConstants.COLLECTION_INDEX_NAME)
                .withQuery(boolQuery.filter(QueryBuilders.rangeQuery(receiptDateIndex)
                        .gte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false)))
                .addAggregation(monthAggregation).build();

        return elasticsearchTemplate.query(searchQueryColl, response -> response.getAggregations());
    }

    /**
     * Provides receipts count
     *
     * @param collectionDetailsRequest
     * @param receiptDetails
     */
    public List<WaterChargeDashBoardResponse> getTotalReceiptsCount(
            final WaterChargeDashBoardRequest collectionDetailsRequest) {
        Date fromDate;
        Date toDate;
        final List<WaterChargeDashBoardResponse> receiptDetailsList = new ArrayList<WaterChargeDashBoardResponse>();
        final WaterChargeDashBoardResponse receiptDetails = new WaterChargeDashBoardResponse();
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {

            fromDate = DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD);

            toDate = org.apache.commons.lang3.time.DateUtils
                    .addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD), 1);
        } else {
            fromDate = new Date();
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(fromDate, 1);
        }
        final Long startTime = System.currentTimeMillis(); // Today’s receipts
                                                           // count Long
                                                           // receiptsCount

        Long receiptsCount = getTotalReceiptCountsForDates(collectionDetailsRequest, fromDate, toDate);
        receiptDetails.setTodayRcptsCount(receiptsCount);

        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {

            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = org.apache.commons.lang3.time.DateUtils
                    .addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD), 1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1);
        } // Current Year till today
        receiptsCount = getTotalReceiptCountsForDates(collectionDetailsRequest, fromDate, toDate);
        receiptDetails.setCurrentYearTillDateRcptsCount(receiptsCount);
        // count for last year's same date
        receiptsCount = getTotalReceiptCountsForDates(collectionDetailsRequest,
                org.apache.commons.lang3.time.DateUtils.addYears(fromDate, -1),
                org.apache.commons.lang3.time.DateUtils.addYears(toDate, -1));

        receiptDetails.setLastYearTillDateRcptsCount(receiptsCount);
        final Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken by getTotalReceiptCountsForDates() for all dates is (millisecs) : " + timeTaken);
        receiptDetailsList.add(receiptDetails);
        return receiptDetailsList;
    }

    /**
     * Gives the total count of receipts
     *
     * @param collectionDetailsRequest
     * @param fromDate
     * @param toDate
     * @return receipt count
     */
    private Long getTotalReceiptCountsForDates(final WaterChargeDashBoardRequest collectionDetailsRequest,
            final Date fromDate, final Date toDate) {

        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest,
                WaterTaxConstants.COLLECTION_INDEX_NAME);
        boolQuery = boolQuery
                .filter(QueryBuilders.rangeQuery(receiptDateIndex)
                        .gte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                .mustNot(QueryBuilders.matchQuery("status", "Cancelled"));

        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder()
                .withIndices(WaterTaxConstants.COLLECTION_INDEX_NAME).withQuery(boolQuery)
                .addAggregation(AggregationBuilders.count(receiptcountIndex).field(consumerCodeIndex)).build();

        final Aggregations collCountAggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());
        final ValueCount aggr = collCountAggr.get(receiptcountIndex);
        return Long.valueOf(aggr.getValue());
    }

    /**
     * Gives month-wise receipts count
     *
     * @param collectionDetailsRequest
     * @return list
     */

    public List<WaterChargeDashBoardResponse> getMonthwiseReceiptsTrend(
            final WaterChargeDashBoardRequest collectionDetailsRequest) {

        final List<WaterChargeDashBoardResponse> rcptTrendsList = new ArrayList<>();
        WaterChargeDashBoardResponse rcptsTrend;
        Date fromDate;
        Date toDate;
        Date dateForMonth;
        String[] dateArr;
        Integer month;
        Long rcptCount;
        String monthName;
        Map<String, Long> monthwiseCount;

        final CFinancialYear financialYear = cFinancialYearService.getFinancialYearByDate(new Date());

        Date finYearStartDate = financialYear.getStartingDate();

        Date finYearEndDate = financialYear.getEndingDate();

        final Map<Integer, String> monthValuesMap = DateUtils.getAllMonthsWithFullNames();

        final List<Map<String, Long>> yearwiseMonthlyCountList = new ArrayList<>();

        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(),
                    WaterTaxConstants.DATE_FORMAT_YYYYMMDD);
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(
                    DateUtils.getDate(collectionDetailsRequest.getToDate(), WaterTaxConstants.DATE_FORMAT_YYYYMMDD), 1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1);
        }
        Long startTime = System.currentTimeMillis();
        for (int count = 0; count <= 2; count++) {
            monthwiseCount = new LinkedHashMap<>();

            final Aggregations collAggregation = getReceiptsCountForConsecutiveYears(collectionDetailsRequest, fromDate,
                    toDate);
            final Histogram dateaggs = collAggregation.get("date_agg");

            for (final Histogram.Bucket entry : dateaggs.getBuckets()) {
                dateArr = entry.getKeyAsString().split("T");
                dateForMonth = DateUtils.getDate(dateArr[0], WaterTaxConstants.DATE_FORMAT_YYYYMMDD);
                month = Integer.valueOf(dateArr[0].split("-", 3)[1]);
                monthName = monthValuesMap.get(month);
                rcptCount = entry.getDocCount();
                // If the receipt count is greater than 0 and the month belongs
                // to respective financial year, add values to the map
                if (DateUtils.between(dateForMonth, finYearStartDate, finYearEndDate) && rcptCount > 0)
                    monthwiseCount.put(monthName, rcptCount);
            }
            yearwiseMonthlyCountList.add(monthwiseCount);

            if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                    && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
                fromDate = org.apache.commons.lang3.time.DateUtils.addYears(fromDate, -1);
                toDate = org.apache.commons.lang3.time.DateUtils.addYears(toDate, -1);
            } else {
                fromDate = org.apache.commons.lang3.time.DateUtils.addYears(finYearStartDate, -1);
                toDate = org.apache.commons.lang3.time.DateUtils.addYears(finYearEndDate, -1);
            }
            finYearStartDate = org.apache.commons.lang3.time.DateUtils.addYears(finYearStartDate, -1);
            finYearEndDate = org.apache.commons.lang3.time.DateUtils.addYears(finYearEndDate, -1);
        }
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken by getReceiptsCountForConsecutiveYears() for 3 consecutive years is (millisecs) : "
                    + timeTaken);
        startTime = System.currentTimeMillis();
        /**
         * If dates are passed in request, get result for the date range, else
         * get results for all 12 months
         */

        if (StringUtils.isBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isBlank(collectionDetailsRequest.getToDate()))
            for (final Map.Entry<Integer, String> entry : DateUtils.getAllFinancialYearMonthsWithFullNames()
                    .entrySet()) {
                rcptsTrend = new WaterChargeDashBoardResponse();

                rcptsTrend.setMonth(entry.getValue());
                rcptsTrend.setCurrentYearRcptsCount(yearwiseMonthlyCountList.get(0).get(rcptsTrend.getMonth()) == null
                        ? 0L : yearwiseMonthlyCountList.get(0).get(rcptsTrend.getMonth()));

                rcptsTrend.setLastYearRcptsCount(yearwiseMonthlyCountList.get(1).get(rcptsTrend.getMonth()) == null ? 0L
                        : yearwiseMonthlyCountList.get(1).get(rcptsTrend.getMonth()));
                rcptsTrend.setPreviousYearRcptsCount(yearwiseMonthlyCountList.get(2).get(rcptsTrend.getMonth()) == null
                        ? 0L : yearwiseMonthlyCountList.get(2).get(rcptsTrend.getMonth()));

                rcptTrendsList.add(rcptsTrend);
            }
        else
            for (final Map.Entry<String, Long> entry : yearwiseMonthlyCountList.get(0).entrySet()) {
                rcptsTrend = new WaterChargeDashBoardResponse();
                rcptsTrend.setMonth(entry.getKey());

                rcptsTrend.setCurrentYearRcptsCount(entry.getValue());

                rcptsTrend.setLastYearRcptsCount(yearwiseMonthlyCountList.get(1).get(rcptsTrend.getMonth()) == null ? 0L
                        : yearwiseMonthlyCountList.get(1).get(rcptsTrend.getMonth()));

                rcptsTrend.setPreviousYearRcptsCount(yearwiseMonthlyCountList.get(2).get(rcptsTrend.getMonth()) == null
                        ? 0L : yearwiseMonthlyCountList.get(2).get(rcptsTrend.getMonth()));

                rcptTrendsList.add(rcptsTrend);
            }

        timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken foro setting values in getMonthwiseReceiptsTrend() is (millisecs): " + timeTaken);
        return rcptTrendsList;

    }

    /*
     * /** Provides month-wise receipts count for consecutive years
     * @param collectionDetailsRequest
     * @param fromDate
     * @param toDate
     * @return SearchResponse
     */
    private Aggregations getReceiptsCountForConsecutiveYears(final WaterChargeDashBoardRequest collectionDetailsRequest,
            final Date fromDate, final Date toDate) {
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest,
                WaterTaxConstants.COLLECTION_INDEX_NAME);
        boolQuery = boolQuery.mustNot(QueryBuilders.matchQuery("status", "Cancelled"));

        final AggregationBuilder monthAggregation = AggregationBuilders.dateHistogram("date_agg")
                .field(receiptDateIndex).interval(DateHistogramInterval.MONTH)
                .subAggregation(AggregationBuilders.count(receiptcountIndex).field("receiptNumber"));
        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder()
                .withIndices(WaterTaxConstants.COLLECTION_INDEX_NAME)
                .withQuery(boolQuery.filter(QueryBuilders.rangeQuery(receiptDateIndex)
                        .gte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false)))
                .addAggregation(monthAggregation).build();
        return elasticsearchTemplate.query(searchQueryColl, response -> response.getAggregations());

    }

    /**
     * Populates Receipt Table Details
     *
     * @param collectionDetailsRequest
     * @return list
     */
    public List<WaterChargeDashBoardResponse> getReceiptTableData(
            final WaterChargeDashBoardRequest collectionDetailsRequest) {
        WaterChargeDashBoardResponse receiptData;
        final List<WaterChargeDashBoardResponse> receiptDataList = new ArrayList<>();
        Date fromDate;
        Date toDate;
        String name;
        BigDecimal variance = BigDecimal.ZERO;
        String aggregationField = WaterTaxConstants.REGIONNAMEAGGREGATIONFIELD;
        /**
         * Select the grouping based on the type parameter, by default the
         * grouping is done based on Regions. If type is region, group by
         * Region, if type is district, group by District, if type is ulb, group
         * by ULB
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType()))
            if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_REGIONWISE))
                aggregationField = WaterTaxConstants.REGIONNAMEAGGREGATIONFIELD;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_DISTRICTWISE))
                aggregationField = WaterTaxConstants.DISTRICTNAMEAGGREGATIONFIELD;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ULBWISE))
                aggregationField = WaterTaxConstants.CITYNAMEAGGREGATIONFIELD;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_GRADEWISE))
                aggregationField = WaterTaxConstants.CITYGRADEAGGREGATIONFIELD;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_WARDWISE))
                aggregationField = WaterTaxConstants.REVENUEWARDAGGREGATIONFIELD;
        /**
         * For Current day's collection if dates are sent in the request,
         * consider the toDate, else take date range between current date +1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD);
            toDate = org.apache.commons.lang3.time.DateUtils
                    .addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD), 1);
        } else {
            fromDate = new Date();
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(fromDate, 1);
        }
        Long startTime = System.currentTimeMillis();
        final Map<String, BigDecimal> currDayCollMap = getCollectionAndDemandCountResults(collectionDetailsRequest,
                fromDate, toDate, WaterTaxConstants.COLLECTION_INDEX_NAME, consumerCodeIndex, aggregationField);
        /**
         * For collections between the date ranges if dates are sent in the
         * request, consider the same, else calculate from current year start
         * date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = org.apache.commons.lang3.time.DateUtils
                    .addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD), 1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1);
        }
        final Map<String, BigDecimal> cytdCollMap = getCollectionAndDemandCountResults(collectionDetailsRequest,
                fromDate, toDate, WaterTaxConstants.COLLECTION_INDEX_NAME, consumerCodeIndex, aggregationField);

        // For last year's till date collections
        final Map<String, BigDecimal> lytdCollMap = getCollectionAndDemandCountResults(collectionDetailsRequest,
                org.apache.commons.lang3.time.DateUtils.addYears(fromDate, -1),
                org.apache.commons.lang3.time.DateUtils.addYears(toDate, -1), WaterTaxConstants.COLLECTION_INDEX_NAME,
                consumerCodeIndex, aggregationField);
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken by getCollectionAndDemandCountResults() is : (millisecs) " + timeTaken);
        startTime = System.currentTimeMillis();
        for (final Map.Entry<String, BigDecimal> entry : cytdCollMap.entrySet()) {
            receiptData = new WaterChargeDashBoardResponse();
            name = entry.getKey();
            if (WaterTaxConstants.REGIONNAMEAGGREGATIONFIELD.equals(aggregationField))
                receiptData.setRegionName(name);
            else if (WaterTaxConstants.DISTRICTNAMEAGGREGATIONFIELD.equals(aggregationField)) {
                receiptData.setRegionName(collectionDetailsRequest.getRegionName());
                receiptData.setDistrictName(name);
            } else if (WaterTaxConstants.CITYNAMEAGGREGATIONFIELD.equals(aggregationField)) {
                receiptData.setUlbName(name);
                receiptData.setDistrictName(collectionDetailsRequest.getDistrictName());
                receiptData.setUlbGrade(collectionDetailsRequest.getUlbGrade());
            } else if (WaterTaxConstants.CITYGRADEAGGREGATIONFIELD.equals(aggregationField))
                receiptData.setUlbGrade(name);
            else if (WaterTaxConstants.REVENUEWARDAGGREGATIONFIELD.equals(aggregationField))
                receiptData.setWardName(name);

            receiptData.setCurrentYearTillDateColl(entry.getValue());
            receiptData.setCurrDayColl(
                    currDayCollMap.get(name) == null ? BigDecimal.valueOf(0) : currDayCollMap.get(name));
            receiptData.setLastYearTillDateColl(
                    lytdCollMap.get(name) == null ? BigDecimal.valueOf(0) : lytdCollMap.get(name));
            // variance = ((currentYearCollection
            // -lastYearCollection)*100)/lastYearCollection
            if (receiptData.getLastYearTillDateColl().compareTo(BigDecimal.ZERO) == 0)
                variance = WaterTaxConstants.BIGDECIMAL_100;
            else
                variance = receiptData.getCurrentYearTillDateColl().subtract(receiptData.getLastYearTillDateColl())
                        .multiply(WaterTaxConstants.BIGDECIMAL_100)
                        .divide(receiptData.getLastYearTillDateColl(), 1, BigDecimal.ROUND_HALF_UP);
            receiptData.setLastYearVar(variance);
            receiptDataList.add(receiptData);
        }
        timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken for setting values in getReceiptTableData() is (millisecs): " + timeTaken);
        return receiptDataList;
    }

    public Map<String, BigDecimal> getCollectionAndDemandCountResults(
            final WaterChargeDashBoardRequest collectionDetailsRequest, final Date fromDate, final Date toDate,
            final String indexName, final String fieldName, final String aggregationField) {
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest, indexName);
        if (indexName.equals(WaterTaxConstants.COLLECTION_INDEX_NAME))
            boolQuery = boolQuery
                    .filter(QueryBuilders.rangeQuery(receiptDateIndex)
                            .gte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                            .lte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                    .mustNot(QueryBuilders.matchQuery("status", "Cancelled"));

        final AggregationBuilder aggregation = AggregationBuilders.terms("by_city").field(aggregationField).size(120)
                .subAggregation(AggregationBuilders.count("total_count").field(fieldName));

        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(indexName).withQuery(boolQuery)
                .addAggregation(aggregation).build();

        final Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());

        final StringTerms cityAggr = collAggr.get("by_city");
        final Map<String, BigDecimal> cytdCollMap = new HashMap<>();
        for (final Terms.Bucket entry : cityAggr.getBuckets()) {
            final ValueCount aggr = entry.getAggregations().get("total_count");
            cytdCollMap.put(String.valueOf(entry.getKey()),
                    BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
        }
        return cytdCollMap;
    }

    /**
     * Fetches BillCollector and revenue ward details for thgiven ulbCode
     *
     * @param collectionDetailsRequest
     * @return List
     */
    public List<BillCollectorIndex> getBillCollectorDetails(
            final WaterChargeDashBoardRequest collectionDetailsRequest) {
        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder()
                .withIndices(PropertyTaxConstants.BILL_COLLECTOR_INDEX_NAME).withFields("billCollector", "revenueWard")
                .withQuery(QueryBuilders.boolQuery()
                        .filter(QueryBuilders.matchQuery("cityCode", collectionDetailsRequest.getUlbCode())))
                .withSort(new FieldSortBuilder("billCollector").order(SortOrder.ASC))
                .withPageable(new PageRequest(0, 250)).build();
        return elasticsearchTemplate.queryForList(searchQueryColl, BillCollectorIndex.class);
    }

}