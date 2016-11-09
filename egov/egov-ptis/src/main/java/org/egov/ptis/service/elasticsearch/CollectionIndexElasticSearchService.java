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

package org.egov.ptis.service.elasticsearch;

import static org.egov.ptis.constants.PropertyTaxConstants.COLLECION_BILLING_SERVICE_PT;
import static org.egov.ptis.constants.PropertyTaxConstants.COLLECTION_INDEX_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_BILLCOLLECTORWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_DISTRICTWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_GRADEWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_REGIONWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_ULBWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_WARDWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DATEFORMATTER_YYYY_MM_DD;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_YYYYMMDD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_TAX_INDEX_NAME;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.bean.dashboard.CollReceiptDetails;
import org.egov.ptis.bean.dashboard.CollTableData;
import org.egov.ptis.bean.dashboard.CollectionDetails;
import org.egov.ptis.bean.dashboard.CollectionDetailsRequest;
import org.egov.ptis.bean.dashboard.CollectionTrend;
import org.egov.ptis.bean.dashboard.ReceiptTableData;
import org.egov.ptis.bean.dashboard.ReceiptsTrend;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.elasticsearch.model.BillCollectorIndex;
import org.elasticsearch.action.search.SearchResponse;
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
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class CollectionIndexElasticSearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionIndexElasticSearchService.class);

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
    public BigDecimal getConsolidatedCollForYears(Date fromDate, Date toDate, String billingService) {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("receiptDate").gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                .must(QueryBuilders.matchQuery("billingService", billingService))
                .mustNot(QueryBuilders.matchQuery("status", "Cancelled"));
        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(queryBuilder).addAggregation(AggregationBuilders.sum("collectiontotal").field("totalAmount"))
                .build();

        Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        Sum aggr = collAggr.get("collectiontotal");
        return BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Gives the consolidated collection for the current Fin year and last fin
     * year
     * 
     * @param billingService
     * @return Map
     */
    public Map<String, BigDecimal> getFinYearsCollByService(String billingService) {
        /**
         * As per Elastic Search functionality, to get the total collections
         * between 2 dates, add a day to the endDate and fetch the results
         */
        Map<String, BigDecimal> consolidatedCollValues = new HashMap<>();
        CFinancialYear currFinYear = cFinancialYearService.getFinancialYearByDate(new Date());
        // For current year results
        consolidatedCollValues.put("cytdColln", getConsolidatedCollForYears(currFinYear.getStartingDate(),
                DateUtils.addDays(new Date(), 1), billingService));
        // For last year results
        consolidatedCollValues.put("lytdColln",
                getConsolidatedCollForYears(DateUtils.addYears(currFinYear.getStartingDate(), -1),
                        DateUtils.addDays(DateUtils.addYears(new Date(), -1), 1), billingService));
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
    private BoolQueryBuilder prepareWhereClause(CollectionDetailsRequest collectionDetailsRequest, String indexName) {
        BoolQueryBuilder boolQuery = null;
        if (indexName.equals(PROPERTY_TAX_INDEX_NAME))
            boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("totalDemand").from(0).to(null));
        else if (indexName.equals(COLLECTION_INDEX_NAME))
            boolQuery = QueryBuilders.boolQuery()
                    .filter(QueryBuilders.matchQuery("billingService", COLLECION_BILLING_SERVICE_PT));
        if (StringUtils.isNotBlank(collectionDetailsRequest.getRegionName()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery("regionName", collectionDetailsRequest.getRegionName()));
        if (StringUtils.isNotBlank(collectionDetailsRequest.getDistrictName()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery("districtName", collectionDetailsRequest.getDistrictName()));
        if (StringUtils.isNotBlank(collectionDetailsRequest.getUlbGrade()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("cityGrade", collectionDetailsRequest.getUlbGrade()));
        if(StringUtils.isNotBlank(collectionDetailsRequest.getUlbCode()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery("cityCode", collectionDetailsRequest.getUlbCode()));
         
        return boolQuery;
    }

    /**
     * API sets the consolidated collections for single day and between the 2
     * dates
     * 
     * @param collectionDetailsRequest
     * @param collectionIndexDetails
     */
    public void getCompleteCollectionIndexDetails(CollectionDetailsRequest collectionDetailsRequest,
            CollectionDetails collectionIndexDetails) {
        Date fromDate;
        Date toDate;
        BigDecimal todayColl = BigDecimal.ZERO;
        BigDecimal tillDateColl = BigDecimal.ZERO;
        Long startTime = System.currentTimeMillis();
        /**
         * As per Elastic Search functionality, to get the total collections
         * between 2 dates, add a day to the endDate and fetch the results For
         * Current day's collection if dates are sent in the request, consider
         * the toDate, else take date range between current date +1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        } else {
            fromDate = new Date();
            toDate = DateUtils.addDays(fromDate, 1);
        }
        // Today’s collection
        todayColl = getCollectionBetweenDates(collectionDetailsRequest, fromDate, toDate, null);
        collectionIndexDetails.setTodayColl(todayColl);

        // Last year Today’s day collection
        todayColl = getCollectionBetweenDates(collectionDetailsRequest, DateUtils.addYears(fromDate, -1),
                DateUtils.addYears(toDate, -1), null);
        collectionIndexDetails.setLyTodayColl(todayColl);

        /**
         * For collections between the date ranges if dates are sent in the
         * request, consider the same, else calculate from current year start
         * date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = DateUtils.addDays(new Date(), 1);
        }
        // Current Year till today collection
        tillDateColl = getCollectionBetweenDates(collectionDetailsRequest, fromDate, toDate, null);
        collectionIndexDetails.setCytdColl(tillDateColl);

        // Last year till same date of today’s date collection
        tillDateColl = getCollectionBetweenDates(collectionDetailsRequest, DateUtils.addYears(fromDate, -1),
                DateUtils.addYears(toDate, -1), null);
        collectionIndexDetails.setLytdColl(tillDateColl);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getCompleteCollectionIndexDetails() is : " + timeTaken + " (millisecs) ");
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
    public BigDecimal getCollectionBetweenDates(CollectionDetailsRequest collectionDetailsRequest, Date fromDate,
            Date toDate, String cityName) {
        Long startTime = System.currentTimeMillis();
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest, COLLECTION_INDEX_NAME);
        boolQuery = boolQuery
                .filter(QueryBuilders.rangeQuery("receiptDate").gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                .mustNot(QueryBuilders.matchQuery("status", "Cancelled"));
        if (StringUtils.isNotBlank(cityName))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("cityName", cityName));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(boolQuery).addAggregation(AggregationBuilders.sum("collectiontotal").field("totalAmount"))
                .build();

        Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        Sum aggr = collAggr.get("collectiontotal");
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getCollectionBetweenDates() is : " + timeTaken + " (millisecs) ");
        return BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Prepares Collection Table Data
     * 
     * @param collectionDetailsRequest
     * @return List
     */
    public List<CollTableData> getResponseTableData(CollectionDetailsRequest collectionDetailsRequest) {
        List<CollTableData> collIndDataList = new ArrayList<>();
        Date fromDate;
        Date toDate;
        String name;
        CollTableData collIndData;
        BigDecimal cytdDmd = BigDecimal.ZERO;
        BigDecimal performance = BigDecimal.ZERO;
        BigDecimal balance = BigDecimal.ZERO;
        BigDecimal variance = BigDecimal.ZERO;
        String aggregationField = "regionName";

        /**
         * Select the grouping based on the type parameter, by default the
         * grouping is done based on Regions. If type is region, group by
         * Region, if type is district, group by District, if type is ulb, group
         * by ULB
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType())) {
            if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_REGIONWISE))
                aggregationField = "regionName";
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_DISTRICTWISE))
                aggregationField = "districtName";
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ULBWISE))
                aggregationField = "cityName";
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_GRADEWISE))
                aggregationField = "cityGrade";
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_WARDWISE) 
                    || collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_BILLCOLLECTORWISE))
                aggregationField = "revenueWard";
        }

        /**
         * As per Elastic Search functionality, to get the total collections
         * between 2 dates, add a day to the endDate and fetch the results For
         * Current day's collection if dates are sent in the request, consider
         * the toDate, else take date range between current date +1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        } else {
            fromDate = new Date();
            toDate = DateUtils.addDays(fromDate, 1);
        }

        Long startTime = System.currentTimeMillis();
        // For today collection
        Map<String, BigDecimal> todayCollMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate, toDate,
                COLLECTION_INDEX_NAME, "totalAmount", aggregationField);

        /**
         * For collection and demand between the date ranges if dates are sent
         * in the request, consider fromDate and toDate+1 , else calculate from
         * current year start date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = DateUtils.addDays(new Date(), 1);
        }
        int noOfMonths = DateUtils.noOfMonths(fromDate, toDate) + 1;

        // For current year's till date collection
        Map<String, BigDecimal> cytdCollMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate, toDate,
                COLLECTION_INDEX_NAME, "totalAmount", aggregationField);
        // For total demand
        Map<String, BigDecimal> totalDemandMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate,
                toDate, PROPERTY_TAX_INDEX_NAME, "totalDemand", aggregationField);
        // For current year demand
        Map<String, BigDecimal> currYrTotalDemandMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate,
                toDate, PROPERTY_TAX_INDEX_NAME, "totalDemand", aggregationField);
        // For last year's till today's date collections
        Map<String, BigDecimal> lytdCollMap = getCollectionAndDemandValues(collectionDetailsRequest,
                DateUtils.addYears(fromDate, -1), DateUtils.addYears(toDate, -1), COLLECTION_INDEX_NAME, "totalAmount",
                aggregationField);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getCollectionAndDemandValues() is : " + timeTaken + " (millisecs) ");

        startTime = System.currentTimeMillis();
        for (Map.Entry<String, BigDecimal> entry : cytdCollMap.entrySet()) {
            collIndData = new CollTableData();
            name = entry.getKey();
            if (aggregationField.equals("regionName"))
                collIndData.setRegionName(name);
            else if (aggregationField.equals("districtName")) {
                collIndData.setRegionName(collectionDetailsRequest.getRegionName());
                collIndData.setDistrictName(name);
            } else if (aggregationField.equals("cityName")) {
                collIndData.setUlbName(name);
                collIndData.setDistrictName(collectionDetailsRequest.getDistrictName());
                collIndData.setUlbGrade(collectionDetailsRequest.getUlbGrade());
            } else if (aggregationField.equals("cityGrade"))
                collIndData.setUlbGrade(name);
              else if(aggregationField.equals("revenueWard"))
                collIndData.setWardName(name);

            collIndData.setTodayColl(todayCollMap.get(name) == null ? BigDecimal.ZERO : todayCollMap.get(name));
            collIndData.setCytdColl(entry.getValue());
            // Proportional Demand = (totalDemand/12)*noOfmonths
            BigDecimal currentYearTotalDemand = (currYrTotalDemandMap.get(name) == null ? BigDecimal.valueOf(0)
                    : currYrTotalDemandMap.get(name));
            cytdDmd = (currentYearTotalDemand.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP))
                    .multiply(BigDecimal.valueOf(noOfMonths));
            collIndData.setCytdDmd(cytdDmd);
            if (cytdDmd != BigDecimal.valueOf(0)) {
                balance = cytdDmd.subtract(collIndData.getCytdColl());
                performance = (collIndData.getCytdColl().multiply(PropertyTaxConstants.BIGDECIMAL_100)).divide(cytdDmd,
                        1, BigDecimal.ROUND_HALF_UP);
                collIndData.setPerformance(performance);
                collIndData.setCytdBalDmd(balance);
            }
            collIndData.setTotalDmd(totalDemandMap.get(name) == null ? BigDecimal.ZERO : totalDemandMap.get(name));
            collIndData.setLytdColl(lytdCollMap.get(name) == null ? BigDecimal.ZERO : lytdCollMap.get(name));
            // variance = ((currentYearCollection -
            // lastYearCollection)*100)/lastYearCollection
            if (collIndData.getLytdColl().compareTo(BigDecimal.ZERO) == 0)
                variance = PropertyTaxConstants.BIGDECIMAL_100;
            else
                variance = ((collIndData.getCytdColl().subtract(collIndData.getLytdColl()))
                        .multiply(PropertyTaxConstants.BIGDECIMAL_100)).divide(collIndData.getLytdColl(), 1,
                                BigDecimal.ROUND_HALF_UP);
            collIndData.setLyVar(variance);
            collIndDataList.add(collIndData);
        }
        timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken for setting values in getResponseTableData() is : " + timeTaken + " (millisecs) ");
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
    public Map<String, BigDecimal> getCollectionAndDemandValues(CollectionDetailsRequest collectionDetailsRequest,
            Date fromDate, Date toDate, String indexName, String fieldName, String aggregationField) {
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest, indexName);
        if (indexName.equals(COLLECTION_INDEX_NAME))
            boolQuery = boolQuery
                    .filter(QueryBuilders.rangeQuery("receiptDate").gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                            .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                    .mustNot(QueryBuilders.matchQuery("status", "Cancelled"));

        AggregationBuilder aggregation = AggregationBuilders.terms("by_city").field(aggregationField).size(120)
                .subAggregation(AggregationBuilders.sum("total").field(fieldName));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(indexName).withQuery(boolQuery)
                .addAggregation(aggregation).build();

        Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        StringTerms cityAggr = collAggr.get("by_city");
        Map<String, BigDecimal> cytdCollMap = new HashMap<>();
        for (Terms.Bucket entry : cityAggr.getBuckets()) {
            Sum aggr = entry.getAggregations().get("total");
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
    public List<CollectionTrend> getMonthwiseCollectionDetails(CollectionDetailsRequest collectionDetailsRequest) {
        List<CollectionTrend> collTrendsList = new ArrayList<>();
        CollectionTrend collTrend;
        Date fromDate;
        Date toDate;
        Date dateForMonth;
        String[] dateArr;
        Integer month;
        Sum aggregateSum;
        CFinancialYear financialYear = cFinancialYearService.getFinancialYearByDate(new Date());
        Date finYearStartDate = financialYear.getStartingDate();
        Date finYearEndDate = financialYear.getEndingDate();
        Map<Integer, String> monthValuesMap = DateUtils.getAllMonthsWithFullNames();
        String monthName;
        List<Map<String, BigDecimal>> yearwiseMonthlyCollList = new ArrayList<>();
        Map<String, BigDecimal> monthwiseColl;
        /**
         * For month-wise collections between the date ranges if dates are sent
         * in the request, consider fromDate and toDate+1 , else calculate from
         * current year start date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = DateUtils.addDays(new Date(), 1);
        }
        Long startTime = System.currentTimeMillis();
        for (int count = 0; count <= 2; count++) {
            monthwiseColl = new LinkedHashMap<>();
            Aggregations collAggr = getMonthwiseCollectionsForConsecutiveYears(collectionDetailsRequest, fromDate,
                    toDate);
            Histogram dateaggs = collAggr.get("date_agg");

            for (Histogram.Bucket entry : dateaggs.getBuckets()) {
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
                fromDate = DateUtils.addYears(fromDate, -1);
                toDate = DateUtils.addYears(toDate, -1);
            } else {
                fromDate = DateUtils.addYears(finYearStartDate, -1);
                toDate = DateUtils.addYears(finYearEndDate, -1);
            }
            finYearStartDate = DateUtils.addYears(finYearStartDate, -1);
            finYearEndDate = DateUtils.addYears(finYearEndDate, -1);
        }
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getMonthwiseCollectionsForConsecutiveYears() for 3 consecutive years is : "
                + timeTaken + " (millisecs) ");

        startTime = System.currentTimeMillis();
        /**
         * If dates are passed in request, get result for the date range, else
         * get results for all 12 months
         */
        if (StringUtils.isBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isBlank(collectionDetailsRequest.getToDate())) {
            for (Map.Entry<Integer, String> entry : DateUtils.getAllFinancialYearMonthsWithFullNames().entrySet()) {
                collTrend = new CollectionTrend();
                collTrend.setMonth(entry.getValue());
                collTrend.setCyColl(yearwiseMonthlyCollList.get(0).get(collTrend.getMonth()) == null ? BigDecimal.ZERO
                        : yearwiseMonthlyCollList.get(0).get(collTrend.getMonth()));
                collTrend.setLyColl(yearwiseMonthlyCollList.get(1).get(collTrend.getMonth()) == null ? BigDecimal.ZERO
                        : yearwiseMonthlyCollList.get(1).get(collTrend.getMonth()));
                collTrend.setPyColl(yearwiseMonthlyCollList.get(2).get(collTrend.getMonth()) == null ? BigDecimal.ZERO
                        : yearwiseMonthlyCollList.get(2).get(collTrend.getMonth()));
                collTrendsList.add(collTrend);
            }
        } else {
            for (Map.Entry<String, BigDecimal> entry : yearwiseMonthlyCollList.get(0).entrySet()) {
                collTrend = new CollectionTrend();
                collTrend.setMonth(entry.getKey());
                collTrend.setCyColl(entry.getValue());
                collTrend.setLyColl(yearwiseMonthlyCollList.get(1).get(collTrend.getMonth()) == null ? BigDecimal.ZERO
                        : yearwiseMonthlyCollList.get(1).get(collTrend.getMonth()));
                collTrend.setPyColl(yearwiseMonthlyCollList.get(2).get(collTrend.getMonth()) == null ? BigDecimal.ZERO
                        : yearwiseMonthlyCollList.get(2).get(collTrend.getMonth()));
                collTrendsList.add(collTrend);
            }
        }
        timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug(
                "Time taken setting values in getMonthwiseCollectionDetails() is : " + timeTaken + " (millisecs) ");
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
    private Aggregations getMonthwiseCollectionsForConsecutiveYears(CollectionDetailsRequest collectionDetailsRequest,
            Date fromDate, Date toDate) {
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest, COLLECTION_INDEX_NAME);
        boolQuery = boolQuery.mustNot(QueryBuilders.matchQuery("status", "Cancelled"));
        AggregationBuilder monthAggregation = AggregationBuilders.dateHistogram("date_agg").field("receiptDate")
                .interval(DateHistogramInterval.MONTH)
                .subAggregation(AggregationBuilders.sum("current_total").field("totalAmount"));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(boolQuery
                        .filter(QueryBuilders.rangeQuery("receiptDate").gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                                .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false)))
                .addAggregation(monthAggregation).build();

        Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });
        return collAggr;
    }

    /**
     * Provides receipts count
     * 
     * @param collectionDetailsRequest
     * @param receiptDetails
     */
    public void getTotalReceiptsCount(CollectionDetailsRequest collectionDetailsRequest,
            CollReceiptDetails receiptDetails) {
        Date fromDate;
        Date toDate;
        /**
         * As per Elastic Search functionality, to get the total collections
         * between 2 dates, add a day to the endDate and fetch the results For
         * Current day's collection if dates are sent in the request, consider
         * the dates as toDate and toDate+1, else take date range between
         * current date +1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        } else {
            fromDate = new Date();
            toDate = DateUtils.addDays(fromDate, 1);
        }
        Long startTime = System.currentTimeMillis();
        // Today’s receipts count
        Long receiptsCount = getTotalReceiptCountsForDates(collectionDetailsRequest, fromDate, toDate);
        receiptDetails.setTodayRcptsCount(receiptsCount);

        /**
         * For collections between the date ranges if dates are sent in the
         * request, consider the same, else calculate from current year start
         * date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = DateUtils.addDays(new Date(), 1);
        }
        // Current Year till today receipt count
        receiptsCount = getTotalReceiptCountsForDates(collectionDetailsRequest, fromDate, toDate);
        receiptDetails.setCytdRcptsCount(receiptsCount);

        // Receipts count for last year's same date
        receiptsCount = getTotalReceiptCountsForDates(collectionDetailsRequest, DateUtils.addYears(fromDate, -1),
                DateUtils.addYears(toDate, -1));
        receiptDetails.setLytdRcptsCount(receiptsCount);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getTotalReceiptCountsForDates() for all dates is : " + timeTaken + " (millisecs) ");
    }

    /**
     * Gives the total count of receipts
     * 
     * @param collectionDetailsRequest
     * @param fromDate
     * @param toDate
     * @return receipt count
     */
    private Long getTotalReceiptCountsForDates(CollectionDetailsRequest collectionDetailsRequest, Date fromDate,
            Date toDate) {
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest, COLLECTION_INDEX_NAME);
        boolQuery = boolQuery
                .filter(QueryBuilders.rangeQuery("receiptDate").gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                .mustNot(QueryBuilders.matchQuery("status", "Cancelled"));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(boolQuery).addAggregation(AggregationBuilders.count("receipt_count").field("consumerCode"))
                .build();

        Aggregations collCountAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        ValueCount aggr = collCountAggr.get("receipt_count");
        return Long.valueOf(aggr.getValue());
    }

    /**
     * Gives month-wise receipts count
     * 
     * @param collectionDetailsRequest
     * @return list
     */
    public List<ReceiptsTrend> getMonthwiseReceiptsTrend(CollectionDetailsRequest collectionDetailsRequest) {
        List<ReceiptsTrend> rcptTrendsList = new ArrayList<>();
        ReceiptsTrend rcptsTrend;
        Date fromDate;
        Date toDate;
        Date dateForMonth;
        String[] dateArr;
        Integer month;
        Long rcptCount;
        String monthName;
        Map<String, Long> monthwiseCount;
        CFinancialYear financialYear = cFinancialYearService.getFinancialYearByDate(new Date());
        Date finYearStartDate = financialYear.getStartingDate();
        Date finYearEndDate = financialYear.getEndingDate();
        Map<Integer, String> monthValuesMap = DateUtils.getAllMonthsWithFullNames();
        List<Map<String, Long>> yearwiseMonthlyCountList = new ArrayList<>();
        /**
         * For month-wise collections between the date ranges if dates are sent
         * in the request, consider fromDate and toDate+1 , else calculate from
         * current year start date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = DateUtils.addDays(new Date(), 1);
        }

        Long startTime = System.currentTimeMillis();
        for (int count = 0; count <= 2; count++) {
            monthwiseCount = new LinkedHashMap<>();
            Aggregations collAggregation = getReceiptsCountForConsecutiveYears(collectionDetailsRequest, fromDate,
                    toDate);
            Histogram dateaggs = collAggregation.get("date_agg");

            for (Histogram.Bucket entry : dateaggs.getBuckets()) {
                dateArr = entry.getKeyAsString().split("T");
                dateForMonth = DateUtils.getDate(dateArr[0], DATE_FORMAT_YYYYMMDD);
                month = Integer.valueOf(dateArr[0].split("-", 3)[1]);
                monthName = monthValuesMap.get(month);
                rcptCount = entry.getDocCount();
                // If the receipt count is greater than 0 and the month belongs
                // to respective financial year, add values to the map
                if (DateUtils.between(dateForMonth, finYearStartDate, finYearEndDate) && rcptCount > 0)
                    monthwiseCount.put(monthName, rcptCount);
            }
            yearwiseMonthlyCountList.add(monthwiseCount);

            /**
             * If dates are passed in request, get result for the date range,
             * else get results for entire financial year
             */
            if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                    && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
                fromDate = DateUtils.addYears(fromDate, -1);
                toDate = DateUtils.addYears(toDate, -1);
            } else {
                fromDate = DateUtils.addYears(finYearStartDate, -1);
                toDate = DateUtils.addYears(finYearEndDate, -1);
            }
            finYearStartDate = DateUtils.addYears(finYearStartDate, -1);
            finYearEndDate = DateUtils.addYears(finYearEndDate, -1);
        }

        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getReceiptsCountForConsecutiveYears() for 3 consecutive years is : " + timeTaken
                + " (millisecs) ");

        startTime = System.currentTimeMillis();
        /**
         * If dates are passed in request, get result for the date range, else
         * get results for all 12 months
         */
        if (StringUtils.isBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isBlank(collectionDetailsRequest.getToDate())) {
            for (Map.Entry<Integer, String> entry : DateUtils.getAllFinancialYearMonthsWithFullNames().entrySet()) {
                rcptsTrend = new ReceiptsTrend();
                rcptsTrend.setMonth(entry.getValue());
                rcptsTrend.setCyRcptsCount(yearwiseMonthlyCountList.get(0).get(rcptsTrend.getMonth()) == null ? 0L
                        : yearwiseMonthlyCountList.get(0).get(rcptsTrend.getMonth()));
                rcptsTrend.setLyRcptsCount(yearwiseMonthlyCountList.get(1).get(rcptsTrend.getMonth()) == null ? 0L
                        : yearwiseMonthlyCountList.get(1).get(rcptsTrend.getMonth()));
                rcptsTrend.setPyRcptsCount(yearwiseMonthlyCountList.get(2).get(rcptsTrend.getMonth()) == null ? 0L
                        : yearwiseMonthlyCountList.get(2).get(rcptsTrend.getMonth()));
                rcptTrendsList.add(rcptsTrend);
            }
        } else {
            for (Map.Entry<String, Long> entry : yearwiseMonthlyCountList.get(0).entrySet()) {
                rcptsTrend = new ReceiptsTrend();
                rcptsTrend.setMonth(entry.getKey());
                rcptsTrend.setCyRcptsCount(entry.getValue());
                rcptsTrend.setLyRcptsCount(yearwiseMonthlyCountList.get(1).get(rcptsTrend.getMonth()) == null ? 0L
                        : yearwiseMonthlyCountList.get(1).get(rcptsTrend.getMonth()));
                rcptsTrend.setPyRcptsCount(yearwiseMonthlyCountList.get(2).get(rcptsTrend.getMonth()) == null ? 0L
                        : yearwiseMonthlyCountList.get(2).get(rcptsTrend.getMonth()));
                rcptTrendsList.add(rcptsTrend);
            }
        }
        timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug(
                "Time taken foro setting values in getMonthwiseReceiptsTrend() is : " + timeTaken + " (millisecs) ");
        return rcptTrendsList;
    }

    /**
     * Provides month-wise receipts count for consecutive years
     * 
     * @param collectionDetailsRequest
     * @param fromDate
     * @param toDate
     * @return SearchResponse
     */
    private Aggregations getReceiptsCountForConsecutiveYears(CollectionDetailsRequest collectionDetailsRequest,
            Date fromDate, Date toDate) {
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest, COLLECTION_INDEX_NAME);
        boolQuery = boolQuery.mustNot(QueryBuilders.matchQuery("status", "Cancelled"));
        AggregationBuilder monthAggregation = AggregationBuilders.dateHistogram("date_agg").field("receiptDate")
                .interval(DateHistogramInterval.MONTH)
                .subAggregation(AggregationBuilders.count("receipt_count").field("receiptNumber"));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(boolQuery
                        .filter(QueryBuilders.rangeQuery("receiptDate").gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                                .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false)))
                .addAggregation(monthAggregation).build();

        Aggregations collCountAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        return collCountAggr;
    }

    /**
     * Populates Receipt Table Details
     * 
     * @param collectionDetailsRequest
     * @return list
     */
    public List<ReceiptTableData> getReceiptTableData(CollectionDetailsRequest collectionDetailsRequest) {
        ReceiptTableData receiptData;
        List<ReceiptTableData> receiptDataList = new ArrayList<>();
        Date fromDate;
        Date toDate;
        String name;
        BigDecimal variance = BigDecimal.ZERO;
        String aggregationField = "regionName";
        /**
         * Select the grouping based on the type parameter, by default the
         * grouping is done based on Regions. If type is region, group by
         * Region, if type is district, group by District, if type is ulb, group
         * by ULB
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType())) {
            if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_REGIONWISE))
                aggregationField = "regionName";
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_DISTRICTWISE))
                aggregationField = "districtName";
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ULBWISE))
                aggregationField = "cityName";
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_GRADEWISE))
                aggregationField = "cityGrade";
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_WARDWISE))
                aggregationField = "revenueWard";
        }
        /**
         * For Current day's collection if dates are sent in the request,
         * consider the toDate, else take date range between current date +1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        } else {
            fromDate = new Date();
            toDate = DateUtils.addDays(fromDate, 1);
        }
        Long startTime = System.currentTimeMillis();
        Map<String, BigDecimal> currDayCollMap = getCollectionAndDemandCountResults(collectionDetailsRequest, fromDate,
                toDate, COLLECTION_INDEX_NAME, "consumerCode", aggregationField);
        /**
         * For collections between the date ranges if dates are sent in the
         * request, consider the same, else calculate from current year start
         * date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = DateUtils.addDays(new Date(), 1);
        }
        Map<String, BigDecimal> cytdCollMap = getCollectionAndDemandCountResults(collectionDetailsRequest, fromDate,
                toDate, COLLECTION_INDEX_NAME, "consumerCode", aggregationField);

        // For last year's till date collections
        Map<String, BigDecimal> lytdCollMap = getCollectionAndDemandCountResults(collectionDetailsRequest,
                DateUtils.addYears(fromDate, -1), DateUtils.addYears(toDate, -1), COLLECTION_INDEX_NAME, "consumerCode",
                aggregationField);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getCollectionAndDemandCountResults() is : " + timeTaken + " (millisecs) ");

        startTime = System.currentTimeMillis();
        for (Map.Entry<String, BigDecimal> entry : cytdCollMap.entrySet()) {
            receiptData = new ReceiptTableData();
            name = entry.getKey();
            if (aggregationField.equals("regionName"))
                receiptData.setRegionName(name);
            else if (aggregationField.equals("districtName")) {
                receiptData.setRegionName(collectionDetailsRequest.getRegionName());
                receiptData.setDistrictName(name);
            } else if (aggregationField.equals("cityName")) {
                receiptData.setUlbName(name);
                receiptData.setDistrictName(collectionDetailsRequest.getDistrictName());
                receiptData.setUlbGrade(collectionDetailsRequest.getUlbGrade());
            } else if (aggregationField.equals("cityGrade"))
                receiptData.setUlbGrade(name);
              else if(aggregationField.equals("revenueWard"))
                receiptData.setWardName(name);
            
            receiptData.setCytdColl(entry.getValue());
            receiptData.setCurrDayColl(
                    currDayCollMap.get(name) == null ? BigDecimal.valueOf(0) : currDayCollMap.get(name));
            receiptData.setLytdColl(lytdCollMap.get(name) == null ? BigDecimal.valueOf(0) : lytdCollMap.get(name));
            // variance = ((currentYearCollection
            // -lastYearCollection)*100)/lastYearCollection
            if (receiptData.getLytdColl().compareTo(BigDecimal.ZERO) == 0)
                variance = PropertyTaxConstants.BIGDECIMAL_100;
            else
                variance = ((receiptData.getCytdColl().subtract(receiptData.getLytdColl()))
                        .multiply(PropertyTaxConstants.BIGDECIMAL_100)).divide(receiptData.getLytdColl(), 1,
                                BigDecimal.ROUND_HALF_UP);
            receiptData.setLyVar(variance);
            receiptDataList.add(receiptData);
        }
        timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken for setting values in getReceiptTableData() is : " + timeTaken + " (millisecs) ");
        return receiptDataList;
    }

    public Map<String, BigDecimal> getCollectionAndDemandCountResults(CollectionDetailsRequest collectionDetailsRequest,
            Date fromDate, Date toDate, String indexName, String fieldName, String aggregationField) {
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest, indexName);
        if (indexName.equals(COLLECTION_INDEX_NAME))
            boolQuery = boolQuery
                    .filter(QueryBuilders.rangeQuery("receiptDate").gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                            .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                    .mustNot(QueryBuilders.matchQuery("status", "Cancelled"));

        AggregationBuilder aggregation = AggregationBuilders.terms("by_city").field(aggregationField).size(120)
                .subAggregation(AggregationBuilders.count("total_count").field(fieldName));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(indexName).withQuery(boolQuery)
                .addAggregation(aggregation).build();

        Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        StringTerms cityAggr = collAggr.get("by_city");
        Map<String, BigDecimal> cytdCollMap = new HashMap<>();
        for (Terms.Bucket entry : cityAggr.getBuckets()) {
            ValueCount aggr = entry.getAggregations().get("total_count");
            cytdCollMap.put(String.valueOf(entry.getKey()),
                    BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
        }
        return cytdCollMap;
    }
    
    /**
     * Populates Receipt Table Details for type - Bill Collector
     * @param collectionDetailsRequest
     * @return list
     */
    public List<CollTableData> getResponseTableDataForBillCollector(CollectionDetailsRequest collectionDetailsRequest) {
        Map<String, CollTableData> wardReceiptDetails = new HashMap<>();
        Map<String, List<CollTableData>> billCollectorWiseMap = new LinkedHashMap<>(); 
        List<CollTableData> collDetails = new ArrayList<>();
        List<CollTableData> billCollectorWiseTableData = new ArrayList<>();
        BigDecimal currDayColl = BigDecimal.ZERO;
        BigDecimal cytdColl = BigDecimal.ZERO;
        BigDecimal lytdColl = BigDecimal.ZERO;
        BigDecimal cytdDmd = BigDecimal.ZERO;
        BigDecimal performance = BigDecimal.ZERO;
        BigDecimal totalDmd = BigDecimal.ZERO;
        BigDecimal variance = BigDecimal.ZERO;
        CollTableData collTableData;
        /**
         * Fetch the Ward-wise data 
         */
        List<CollTableData> wardWiseData = getResponseTableData(collectionDetailsRequest);
        for(CollTableData tableData : wardWiseData){
            wardReceiptDetails.put(tableData.getWardName(), tableData);
        }
        
        /**
         * prepare bill collector wise collection table data
         */
        List<BillCollectorIndex> billCollectorsList = getBillCollectorDetails(collectionDetailsRequest);
        for(BillCollectorIndex billCollIndex : billCollectorsList){
            if(billCollectorWiseMap.isEmpty()){
                collDetails.add(wardReceiptDetails.get(billCollIndex.getRevenueWard()));
                billCollectorWiseMap.put(billCollIndex.getBillCollector(), collDetails);
            } else {
                if(!billCollectorWiseMap.containsKey(billCollIndex.getBillCollector())){
                    collDetails = new ArrayList<>();
                    collDetails.add(wardReceiptDetails.get(billCollIndex.getRevenueWard()));
                    billCollectorWiseMap.put(billCollIndex.getBillCollector(), collDetails);
                } else {
                    if(wardReceiptDetails.get(billCollIndex.getRevenueWard()) != null)
                        billCollectorWiseMap.get(billCollIndex.getBillCollector()).add(wardReceiptDetails.get(billCollIndex.getRevenueWard()));
                }
            }
        }
        
        for(Entry<String, List<CollTableData>> entry : billCollectorWiseMap.entrySet()){
            collTableData = new CollTableData();
            currDayColl = BigDecimal.ZERO;
            cytdColl = BigDecimal.ZERO;
            lytdColl = BigDecimal.ZERO;
            cytdDmd = BigDecimal.ZERO;
            performance = BigDecimal.ZERO;
            totalDmd = BigDecimal.ZERO;
            variance = BigDecimal.ZERO;
            for(CollTableData tableData : entry.getValue()){
                currDayColl = currDayColl.add(tableData.getTodayColl() == null ? BigDecimal.ZERO : tableData.getTodayColl());
                cytdColl = cytdColl.add(tableData.getCytdColl() == null ? BigDecimal.ZERO : tableData.getCytdColl());
                cytdDmd = cytdDmd.add(tableData.getCytdDmd() == null ? BigDecimal.ZERO : tableData.getCytdDmd());
                totalDmd = totalDmd.add(tableData.getTotalDmd() == null ? BigDecimal.ZERO : tableData.getTotalDmd());
                lytdColl = lytdColl.add(tableData.getLytdColl() == null ? BigDecimal.ZERO : tableData.getLytdColl());
            }
            collTableData.setBillCollector(entry.getKey());
            collTableData.setTodayColl(currDayColl);
            collTableData.setCytdColl(cytdColl);
            collTableData.setCytdDmd(cytdDmd);
            collTableData.setCytdBalDmd(cytdDmd.subtract(cytdColl));
            collTableData.setTotalDmd(totalDmd);
            collTableData.setLytdColl(lytdColl);
            if (cytdDmd != BigDecimal.valueOf(0)) {
                performance = (collTableData.getCytdColl().multiply(PropertyTaxConstants.BIGDECIMAL_100)).divide(cytdDmd,
                        1, BigDecimal.ROUND_HALF_UP);
                collTableData.setPerformance(performance);
            }
            if (collTableData.getLytdColl().compareTo(BigDecimal.ZERO) == 0)
                variance = PropertyTaxConstants.BIGDECIMAL_100;
            else
                variance = ((collTableData.getCytdColl().subtract(collTableData.getLytdColl()))
                        .multiply(PropertyTaxConstants.BIGDECIMAL_100)).divide(collTableData.getLytdColl(), 1,
                                BigDecimal.ROUND_HALF_UP);
            collTableData.setLyVar(variance);
            billCollectorWiseTableData.add(collTableData);
        }
        return billCollectorWiseTableData;
    }

    /**
     * Fetches BillCollector and revenue ward details for thgiven ulbCode
     * @param collectionDetailsRequest
     * @return List
     */
    private List<BillCollectorIndex> getBillCollectorDetails(CollectionDetailsRequest collectionDetailsRequest) {
        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(PropertyTaxConstants.BILL_COLLECTOR_INDEX_NAME)
                .withFields("billCollector","revenueWard")
                .withQuery(QueryBuilders.boolQuery()
                .filter(QueryBuilders.matchQuery("cityCode", collectionDetailsRequest.getUlbCode())))
                .withSort(new FieldSortBuilder("billCollector").order(SortOrder.ASC))
                .withPageable(new PageRequest(0, 200))
                .build();
        List<BillCollectorIndex> billCollectorsList = elasticsearchTemplate.queryForList(searchQueryColl,BillCollectorIndex.class);
        return billCollectorsList;
    }
    
}