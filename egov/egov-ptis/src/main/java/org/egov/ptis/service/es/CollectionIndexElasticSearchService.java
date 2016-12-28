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

package org.egov.ptis.service.es;

import static org.egov.ptis.constants.PropertyTaxConstants.COLLECION_BILLING_SERVICE_PT;
import static org.egov.ptis.constants.PropertyTaxConstants.COLLECION_BILLING_SERVICE_VLT;
import static org.egov.ptis.constants.PropertyTaxConstants.COLLECION_BILLING_SERVICE_WTMS;
import static org.egov.ptis.constants.PropertyTaxConstants.COLLECTION_INDEX_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_BILLCOLLECTORWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_DISTRICTWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_GRADEWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_REGIONWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_ULBWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_WARDWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_PROPERTY_TYPE_CENTRAL_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_PROPERTY_TYPE_CENTRAL_GOVT_LIST;
import static org.egov.ptis.constants.PropertyTaxConstants.DATEFORMATTER_YYYY_MM_DD;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_YYYYMMDD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_TAX_INDEX_NAME;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.egov.ptis.domain.entity.es.BillCollectorIndex;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
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

    private static final String INTEREST_AMOUNT = "interestAmount";
    private static final String CURRENT_CESS = "currentCess";
    private static final String ARREAR_CESS = "arrearCess";
    private static final String CURRENT_AMOUNT = "currentAmount";
    private static final String ARREAR_AMOUNT = "arrearAmount";
    private static final String CONSUMER_CODE = "consumerCode";
    private static final String RECEIPT_COUNT = "receipt_count";
    private static final String DATE_AGG = "date_agg";
    private static final String BY_CITY = "by_city";
    private static final String MILLISECS = " (millisecs) ";
    private static final String REVENUE_WARD = "revenueWard";
    private static final String CITY_NAME = "cityName";
    private static final String CITY_CODE = "cityCode";
    private static final String CITY_GRADE = "cityGrade";
    private static final String DISTRICT_NAME = "districtName";
    private static final String REGION_NAME = "regionName";
    private static final String BILLING_SERVICE = "billingService";
    private static final String TOTAL_DEMAND = "totalDemand";
    private static final String TOTAL_AMOUNT = "totalAmount";
    private static final String COLLECTIONTOTAL = "collectiontotal";
    private static final String CANCELLED = "Cancelled";
    private static final String STATUS = "status";
    private static final String RECEIPT_DATE = "receiptDate";
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
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery(RECEIPT_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                .mustNot(QueryBuilders.matchQuery(STATUS, CANCELLED));

        if (COLLECION_BILLING_SERVICE_WTMS.equalsIgnoreCase(billingService))
            boolQuery = boolQuery.must(QueryBuilders.matchQuery(BILLING_SERVICE, billingService));
        else
            boolQuery = boolQuery.must(QueryBuilders.boolQuery()
                    .filter(QueryBuilders.termsQuery(BILLING_SERVICE,
                            Arrays.asList(COLLECION_BILLING_SERVICE_PT, COLLECION_BILLING_SERVICE_VLT))));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(boolQuery).addAggregation(AggregationBuilders.sum(COLLECTIONTOTAL).field(TOTAL_AMOUNT))
                .build();

        Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        Sum aggr = collAggr.get(COLLECTIONTOTAL);
        return BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Gives the consolidated collection for the current Fin year and last fin year
     * 
     * @param billingService
     * @return Map
     */
    public Map<String, BigDecimal> getFinYearsCollByService(String billingService) {
        /**
         * As per Elastic Search functionality, to get the total collections between 2 dates, add a day to the endDate and fetch
         * the results
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
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        if (indexName.equals(PROPERTY_TAX_INDEX_NAME))
            boolQuery = QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery(TOTAL_DEMAND).from(0).to(null));
        else if (indexName.equals(COLLECTION_INDEX_NAME))
            boolQuery = QueryBuilders.boolQuery()
                    .filter(QueryBuilders.termsQuery(BILLING_SERVICE,
                            Arrays.asList(COLLECION_BILLING_SERVICE_PT, COLLECION_BILLING_SERVICE_VLT)));
        if (StringUtils.isNotBlank(collectionDetailsRequest.getRegionName()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(REGION_NAME, collectionDetailsRequest.getRegionName()));
        if (StringUtils.isNotBlank(collectionDetailsRequest.getDistrictName()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(DISTRICT_NAME, collectionDetailsRequest.getDistrictName()));
        if (StringUtils.isNotBlank(collectionDetailsRequest.getUlbGrade()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(CITY_GRADE, collectionDetailsRequest.getUlbGrade()));
        if (StringUtils.isNotBlank(collectionDetailsRequest.getUlbCode()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(CITY_CODE, collectionDetailsRequest.getUlbCode()));
        if (StringUtils.isNotBlank(collectionDetailsRequest.getPropertyType())) {
            if (DASHBOARD_PROPERTY_TYPE_CENTRAL_GOVT.equalsIgnoreCase(collectionDetailsRequest.getPropertyType()))
                boolQuery = boolQuery
                        .filter(QueryBuilders.termsQuery("consumerType", DASHBOARD_PROPERTY_TYPE_CENTRAL_GOVT_LIST));
            else
                boolQuery = boolQuery
                        .filter(QueryBuilders.matchQuery("consumerType", collectionDetailsRequest.getPropertyType()));
        }
        return boolQuery;
    }

    /**
     * API sets the consolidated collections for single day and between the 2 dates
     * 
     * @param collectionDetailsRequest
     * @param collectionIndexDetails
     */
    public void getCompleteCollectionIndexDetails(CollectionDetailsRequest collectionDetailsRequest,
            CollectionDetails collectionIndexDetails) {
        Date fromDate;
        Date toDate;
        BigDecimal todayColl;
        BigDecimal tillDateColl;
        Long startTime = System.currentTimeMillis();
        /**
         * As per Elastic Search functionality, to get the total collections between 2 dates, add a day to the endDate and fetch
         * the results For Current day's collection if dates are sent in the request, consider the toDate, else take date range
         * between current date +1 day
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
         * For collections between the date ranges if dates are sent in the request, consider the same, else calculate from
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
        // Current Year till today collection
        tillDateColl = getCollectionBetweenDates(collectionDetailsRequest, fromDate, toDate, null);
        collectionIndexDetails.setCytdColl(tillDateColl);

        // Last year till same date of today’s date collection
        tillDateColl = getCollectionBetweenDates(collectionDetailsRequest, DateUtils.addYears(fromDate, -1),
                DateUtils.addYears(toDate, -1), null);
        collectionIndexDetails.setLytdColl(tillDateColl);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getCompleteCollectionIndexDetails() is : " + timeTaken + MILLISECS);
    }

    /**
     * Returns the consolidated collections for single day and between the 2 dates
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
                .filter(QueryBuilders.rangeQuery(RECEIPT_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                .mustNot(QueryBuilders.matchQuery(STATUS, CANCELLED));
        if (StringUtils.isNotBlank(cityName))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(CITY_NAME, cityName));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(boolQuery).addAggregation(AggregationBuilders.sum(COLLECTIONTOTAL).field(TOTAL_AMOUNT))
                .build();

        Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        Sum aggr = collAggr.get(COLLECTIONTOTAL);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getCollectionBetweenDates() is : " + timeTaken + MILLISECS);
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
        String aggregationField = REGION_NAME;
        Map<String, BillCollectorIndex> wardWiseBillCollectors = new HashMap<>();

        /**
         * Select the grouping based on the type parameter, by default the grouping is done based on Regions. If type is region,
         * group by Region, if type is district, group by District, if type is ulb, group by ULB
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType())) {
            if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_REGIONWISE))
                aggregationField = REGION_NAME;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_DISTRICTWISE))
                aggregationField = DISTRICT_NAME;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ULBWISE))
                aggregationField = CITY_NAME;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_GRADEWISE))
                aggregationField = CITY_GRADE;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_WARDWISE)
                    || collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_BILLCOLLECTORWISE))
                aggregationField = REVENUE_WARD;
        }

        /**
         * As per Elastic Search functionality, to get the total collections between 2 dates, add a day to the endDate and fetch
         * the results For Current day's collection if dates are sent in the request, consider the toDate, else take date range
         * between current date +1 day
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
        // For today's collection
        Map<String, BigDecimal> todayCollMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate, toDate,
                COLLECTION_INDEX_NAME, TOTAL_AMOUNT, aggregationField);

        /**
         * For collection and demand between the date ranges if dates are sent in the request, consider fromDate and toDate+1 ,
         * else calculate from current year start date till current date+1 day
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

        Map<String, BigDecimal> cytdCollMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate, toDate,
                COLLECTION_INDEX_NAME, TOTAL_AMOUNT, aggregationField);
        // For total demand
        Map<String, BigDecimal> totalDemandMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate,
                toDate, PROPERTY_TAX_INDEX_NAME, TOTAL_DEMAND, aggregationField);
        // For current year demand
        Map<String, BigDecimal> currYrTotalDemandMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate,
                toDate, PROPERTY_TAX_INDEX_NAME, TOTAL_DEMAND, aggregationField);
        // For last year's till today's date collections
        Map<String, BigDecimal> lytdCollMap = getCollectionAndDemandValues(collectionDetailsRequest,
                DateUtils.addYears(fromDate, -1), DateUtils.addYears(toDate, -1), COLLECTION_INDEX_NAME, TOTAL_AMOUNT,
                aggregationField);
        // For fetching individual collections
        StringTerms individualCollDetails = getIndividualCollections(collectionDetailsRequest, fromDate, toDate,
                COLLECTION_INDEX_NAME, aggregationField);
        Map<String, Map<String, BigDecimal>> collectionDivisionMap = new HashMap<>();
        Map<String, BigDecimal> individualCollMap;
        Sum arrearAmt;
        Sum currentAmt;
        Sum arrearCess;
        Sum currentCess;
        Sum interestAmt;
        if (individualCollDetails != null) {
            for (Terms.Bucket entry : individualCollDetails.getBuckets()) {
                individualCollMap = new HashMap<>();
                arrearAmt = entry.getAggregations().get("arrear_amount");
                currentAmt = entry.getAggregations().get("curr_amount");
                arrearCess = entry.getAggregations().get("arrear_cess");
                currentCess = entry.getAggregations().get("curr_cess");
                interestAmt = entry.getAggregations().get("interest");

                individualCollMap.put(ARREAR_AMOUNT,
                        BigDecimal.valueOf(arrearAmt.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualCollMap.put(CURRENT_AMOUNT,
                        BigDecimal.valueOf(currentAmt.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualCollMap.put(ARREAR_CESS,
                        BigDecimal.valueOf(arrearCess.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualCollMap.put(CURRENT_CESS,
                        BigDecimal.valueOf(currentCess.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualCollMap.put(INTEREST_AMOUNT,
                        BigDecimal.valueOf(interestAmt.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));

                collectionDivisionMap.put(String.valueOf(entry.getKey()), individualCollMap);
            }
        }

        // Fetch ward wise Bill Collector details for ward based grouping
        if (DASHBOARD_GROUPING_WARDWISE.equalsIgnoreCase(collectionDetailsRequest.getType()))
            wardWiseBillCollectors = getWardWiseBillCollectors(collectionDetailsRequest);

        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getCollectionAndDemandValues() is : " + timeTaken + MILLISECS);

        startTime = System.currentTimeMillis();
        for (Map.Entry<String, BigDecimal> entry : cytdCollMap.entrySet()) {
            collIndData = new CollTableData();
            name = entry.getKey();
            if (aggregationField.equals(REGION_NAME))
                collIndData.setRegionName(name);
            else if (aggregationField.equals(DISTRICT_NAME)) {
                collIndData.setRegionName(collectionDetailsRequest.getRegionName());
                collIndData.setDistrictName(name);
            } else if (aggregationField.equals(CITY_NAME)) {
                collIndData.setUlbName(name);
                collIndData.setDistrictName(collectionDetailsRequest.getDistrictName());
                collIndData.setUlbGrade(collectionDetailsRequest.getUlbGrade());
            } else if (aggregationField.equals(CITY_GRADE))
                collIndData.setUlbGrade(name);
            else if (aggregationField.equals(REVENUE_WARD)) {
                collIndData.setWardName(name);
                // If the grouping is based on ward, set the Bill Collector name and number
                if (DASHBOARD_GROUPING_WARDWISE.equalsIgnoreCase(collectionDetailsRequest.getType())
                        && !wardWiseBillCollectors.isEmpty()) {
                    collIndData.setBillCollector(wardWiseBillCollectors.get(name) == null ? StringUtils.EMPTY
                            : wardWiseBillCollectors.get(name).getBillCollector());
                    collIndData.setMobileNumber(wardWiseBillCollectors.get(name) == null ? StringUtils.EMPTY
                            : wardWiseBillCollectors.get(name).getMobileNumber());
                }
            }
            collIndData.setTodayColl(todayCollMap.get(name) == null ? BigDecimal.ZERO : todayCollMap.get(name));
            collIndData.setCytdColl(entry.getValue());
            if (!collectionDivisionMap.isEmpty()) {
                collIndData.setArrearColl((collectionDivisionMap.get(name).get(ARREAR_AMOUNT) == null ? BigDecimal.ZERO
                        : collectionDivisionMap.get(name).get(ARREAR_AMOUNT))
                                .add(collectionDivisionMap.get(name).get(ARREAR_CESS) == null ? BigDecimal.ZERO
                                        : collectionDivisionMap.get(name).get(ARREAR_CESS)));
                collIndData.setCurrentColl((collectionDivisionMap.get(name).get(CURRENT_AMOUNT) == null ? BigDecimal.ZERO
                        : collectionDivisionMap.get(name).get(CURRENT_AMOUNT))
                                .add(collectionDivisionMap.get(name).get(CURRENT_CESS) == null ? BigDecimal.ZERO
                                        : collectionDivisionMap.get(name).get(CURRENT_CESS)));
                collIndData.setInterest(collectionDivisionMap.get(name).get(INTEREST_AMOUNT) == null ? BigDecimal.ZERO
                        : collectionDivisionMap.get(name).get(INTEREST_AMOUNT));
            }
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
        LOGGER.debug("Time taken for setting values in getResponseTableData() is : " + timeTaken + MILLISECS);
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
                    .filter(QueryBuilders.rangeQuery(RECEIPT_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                            .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                    .mustNot(QueryBuilders.matchQuery(STATUS, CANCELLED));
        else
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("isActive", true))
                    .filter(QueryBuilders.matchQuery("isExempted", false));

        AggregationBuilder aggregation = AggregationBuilders.terms(BY_CITY).field(aggregationField).size(120)
                .subAggregation(AggregationBuilders.sum("total").field(fieldName));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(indexName).withQuery(boolQuery)
                .addAggregation(aggregation).build();

        Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        StringTerms cityAggr = collAggr.get(BY_CITY);
        Map<String, BigDecimal> cytdCollMap = new HashMap<>();
        for (Terms.Bucket entry : cityAggr.getBuckets()) {
            Sum aggr = entry.getAggregations().get("total");
            cytdCollMap.put(String.valueOf(entry.getKey()),
                    BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
        }
        return cytdCollMap;
    }

    /**
     * Provides collection break-up of total amount
     * @param collectionDetailsRequest
     * @param fromDate
     * @param toDate
     * @param indexName
     * @param aggregationField
     * @return StringTerms
     */
    public StringTerms getIndividualCollections(CollectionDetailsRequest collectionDetailsRequest,
            Date fromDate, Date toDate, String indexName, String aggregationField) {
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest, indexName);
        if (indexName.equals(COLLECTION_INDEX_NAME))
            boolQuery = boolQuery
                    .filter(QueryBuilders.rangeQuery(RECEIPT_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                            .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                    .mustNot(QueryBuilders.matchQuery(STATUS, CANCELLED));
        else
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("isActive", true))
                    .filter(QueryBuilders.matchQuery("isExempted", false));

        AggregationBuilder aggregation = AggregationBuilders.terms(BY_CITY).field(aggregationField).size(120)
                .subAggregation(AggregationBuilders.sum("arrear_amount").field(ARREAR_AMOUNT))
                .subAggregation(AggregationBuilders.sum("curr_amount").field(CURRENT_AMOUNT))
                .subAggregation(AggregationBuilders.sum("arrear_cess").field(ARREAR_CESS))
                .subAggregation(AggregationBuilders.sum("curr_cess").field(CURRENT_CESS))
                .subAggregation(AggregationBuilders.sum("interest").field("latePaymentCharges"));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(indexName).withQuery(boolQuery)
                .addAggregation(aggregation).build();

        Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        return collAggr.get(BY_CITY);
    }

    /**
     * Prepares month-wise collections for 3 consecutive years - from current year
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
         * For month-wise collections between the date ranges if dates are sent in the request, consider fromDate and toDate+1 ,
         * else calculate from current year start date till current date+1 day
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
            Histogram dateaggs = collAggr.get(DATE_AGG);

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
             * If dates are passed in request, get result for the date range, else get results for entire financial year
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
                + timeTaken + MILLISECS);

        startTime = System.currentTimeMillis();
        /**
         * If dates are passed in request, get result for the date range, else get results for all 12 months
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
                "Time taken setting values in getMonthwiseCollectionDetails() is : " + timeTaken + MILLISECS);
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
        boolQuery = boolQuery.filter(QueryBuilders.rangeQuery(RECEIPT_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                .mustNot(QueryBuilders.matchQuery(STATUS, CANCELLED));
        AggregationBuilder monthAggregation = AggregationBuilders.dateHistogram(DATE_AGG).field(RECEIPT_DATE)
                .interval(DateHistogramInterval.MONTH)
                .subAggregation(AggregationBuilders.sum("current_total").field(TOTAL_AMOUNT));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(boolQuery)
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
         * As per Elastic Search functionality, to get the total collections between 2 dates, add a day to the endDate and fetch
         * the results For Current day's collection if dates are sent in the request, consider the dates as toDate and toDate+1,
         * else take date range between current date +1 day
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
         * For collections between the date ranges if dates are sent in the request, consider the same, else calculate from
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
        // Current Year till today receipt count
        receiptsCount = getTotalReceiptCountsForDates(collectionDetailsRequest, fromDate, toDate);
        receiptDetails.setCytdRcptsCount(receiptsCount);

        // Receipts count for last year's same date
        receiptsCount = getTotalReceiptCountsForDates(collectionDetailsRequest, DateUtils.addYears(fromDate, -1),
                DateUtils.addYears(toDate, -1));
        receiptDetails.setLytdRcptsCount(receiptsCount);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getTotalReceiptCountsForDates() for all dates is : " + timeTaken + MILLISECS);
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
                .filter(QueryBuilders.rangeQuery(RECEIPT_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                .mustNot(QueryBuilders.matchQuery(STATUS, CANCELLED));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(boolQuery).addAggregation(AggregationBuilders.count(RECEIPT_COUNT).field(CONSUMER_CODE))
                .build();

        Aggregations collCountAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        ValueCount aggr = collCountAggr.get(RECEIPT_COUNT);
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
         * For month-wise collections between the date ranges if dates are sent in the request, consider fromDate and toDate+1 ,
         * else calculate from current year start date till current date+1 day
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
            Histogram dateaggs = collAggregation.get(DATE_AGG);

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
             * If dates are passed in request, get result for the date range, else get results for entire financial year
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
                + MILLISECS);

        startTime = System.currentTimeMillis();
        /**
         * If dates are passed in request, get result for the date range, else get results for all 12 months
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
                "Time taken foro setting values in getMonthwiseReceiptsTrend() is : " + timeTaken + MILLISECS);
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
        boolQuery = boolQuery.mustNot(QueryBuilders.matchQuery(STATUS, CANCELLED));
        AggregationBuilder monthAggregation = AggregationBuilders.dateHistogram(DATE_AGG).field(RECEIPT_DATE)
                .interval(DateHistogramInterval.MONTH)
                .subAggregation(AggregationBuilders.count(RECEIPT_COUNT).field("receiptNumber"));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(boolQuery
                        .filter(QueryBuilders.rangeQuery(RECEIPT_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
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
        String aggregationField = REGION_NAME;
        /**
         * Select the grouping based on the type parameter, by default the grouping is done based on Regions. If type is region,
         * group by Region, if type is district, group by District, if type is ulb, group by ULB
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType())) {
            if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_REGIONWISE))
                aggregationField = REGION_NAME;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_DISTRICTWISE))
                aggregationField = DISTRICT_NAME;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ULBWISE))
                aggregationField = CITY_NAME;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_GRADEWISE))
                aggregationField = CITY_GRADE;
            else if (collectionDetailsRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_WARDWISE))
                aggregationField = REVENUE_WARD;
        }
        /**
         * For Current day's collection if dates are sent in the request, consider the toDate, else take date range between
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
        Map<String, BigDecimal> currDayCollMap = getCollectionAndDemandCountResults(collectionDetailsRequest, fromDate,
                toDate, COLLECTION_INDEX_NAME, CONSUMER_CODE, aggregationField);
        /**
         * For collections between the date ranges if dates are sent in the request, consider the same, else calculate from
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
        Map<String, BigDecimal> cytdCollMap = getCollectionAndDemandCountResults(collectionDetailsRequest, fromDate,
                toDate, COLLECTION_INDEX_NAME, CONSUMER_CODE, aggregationField);

        // For last year's till date collections
        Map<String, BigDecimal> lytdCollMap = getCollectionAndDemandCountResults(collectionDetailsRequest,
                DateUtils.addYears(fromDate, -1), DateUtils.addYears(toDate, -1), COLLECTION_INDEX_NAME, CONSUMER_CODE,
                aggregationField);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getCollectionAndDemandCountResults() is : " + timeTaken + MILLISECS);

        startTime = System.currentTimeMillis();
        for (Map.Entry<String, BigDecimal> entry : cytdCollMap.entrySet()) {
            receiptData = new ReceiptTableData();
            name = entry.getKey();
            if (aggregationField.equals(REGION_NAME))
                receiptData.setRegionName(name);
            else if (aggregationField.equals(DISTRICT_NAME)) {
                receiptData.setRegionName(collectionDetailsRequest.getRegionName());
                receiptData.setDistrictName(name);
            } else if (aggregationField.equals(CITY_NAME)) {
                receiptData.setUlbName(name);
                receiptData.setDistrictName(collectionDetailsRequest.getDistrictName());
                receiptData.setUlbGrade(collectionDetailsRequest.getUlbGrade());
            } else if (aggregationField.equals(CITY_GRADE))
                receiptData.setUlbGrade(name);
            else if (aggregationField.equals(REVENUE_WARD))
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
        LOGGER.debug("Time taken for setting values in getReceiptTableData() is : " + timeTaken + MILLISECS);
        return receiptDataList;
    }

    public Map<String, BigDecimal> getCollectionAndDemandCountResults(CollectionDetailsRequest collectionDetailsRequest,
            Date fromDate, Date toDate, String indexName, String fieldName, String aggregationField) {
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest, indexName);
        if (indexName.equals(COLLECTION_INDEX_NAME))
            boolQuery = boolQuery
                    .filter(QueryBuilders.rangeQuery(RECEIPT_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                            .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                    .mustNot(QueryBuilders.matchQuery(STATUS, CANCELLED));

        AggregationBuilder aggregation = AggregationBuilders.terms(BY_CITY).field(aggregationField).size(120)
                .subAggregation(AggregationBuilders.count("total_count").field(fieldName));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(indexName).withQuery(boolQuery)
                .addAggregation(aggregation).build();

        Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        StringTerms cityAggr = collAggr.get(BY_CITY);
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
     * 
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
        String billCollectorNameNumber;
        String[] billCollectorNameNumberArr;
        /**
         * Fetch the Ward-wise data
         */
        List<CollTableData> wardWiseData = getResponseTableData(collectionDetailsRequest);
        for (CollTableData tableData : wardWiseData) {
            wardReceiptDetails.put(tableData.getWardName(), tableData);
        }

        /**
         * prepare bill collector wise collection table data
         */
        List<BillCollectorIndex> billCollectorsList = getBillCollectorDetails(collectionDetailsRequest);
        for (BillCollectorIndex billCollIndex : billCollectorsList) {
            if (wardReceiptDetails.get(billCollIndex.getRevenueWard()) != null
                    && StringUtils.isNotBlank(billCollIndex.getRevenueWard())) {
                billCollectorNameNumber = billCollIndex.getBillCollector().concat("~")
                        .concat(StringUtils.isBlank(billCollIndex.getMobileNumber()) ? StringUtils.EMPTY
                                : billCollIndex.getMobileNumber());
                if (billCollectorWiseMap.isEmpty()) {
                    collDetails.add(wardReceiptDetails.get(billCollIndex.getRevenueWard()));
                    billCollectorWiseMap.put(billCollectorNameNumber, collDetails);
                } else {
                    if (!billCollectorWiseMap.containsKey(billCollectorNameNumber)) {
                        collDetails = new ArrayList<>();
                        collDetails.add(wardReceiptDetails.get(billCollIndex.getRevenueWard()));
                        billCollectorWiseMap.put(billCollectorNameNumber, collDetails);
                    } else {
                        billCollectorWiseMap.get(billCollectorNameNumber)
                                .add(wardReceiptDetails.get(billCollIndex.getRevenueWard()));
                    }
                }
            }
        }

        for (Entry<String, List<CollTableData>> entry : billCollectorWiseMap.entrySet()) {
            collTableData = new CollTableData();
            currDayColl = BigDecimal.ZERO;
            cytdColl = BigDecimal.ZERO;
            lytdColl = BigDecimal.ZERO;
            cytdDmd = BigDecimal.ZERO;
            performance = BigDecimal.ZERO;
            totalDmd = BigDecimal.ZERO;
            variance = BigDecimal.ZERO;
            billCollectorNameNumberArr = entry.getKey().split("~");
            for (CollTableData tableData : entry.getValue()) {
                currDayColl = currDayColl
                        .add(tableData.getTodayColl() == null ? BigDecimal.ZERO : tableData.getTodayColl());
                cytdColl = cytdColl.add(tableData.getCytdColl() == null ? BigDecimal.ZERO : tableData.getCytdColl());
                cytdDmd = cytdDmd.add(tableData.getCytdDmd() == null ? BigDecimal.ZERO : tableData.getCytdDmd());
                totalDmd = totalDmd.add(tableData.getTotalDmd() == null ? BigDecimal.ZERO : tableData.getTotalDmd());
                lytdColl = lytdColl.add(tableData.getLytdColl() == null ? BigDecimal.ZERO : tableData.getLytdColl());
            }
            collTableData.setBillCollector(billCollectorNameNumberArr[0]);
            collTableData
                    .setMobileNumber(billCollectorNameNumberArr.length > 1 ? billCollectorNameNumberArr[1] : StringUtils.EMPTY);
            collTableData.setTodayColl(currDayColl);
            collTableData.setCytdColl(cytdColl);
            collTableData.setCytdDmd(cytdDmd);
            collTableData.setCytdBalDmd(cytdDmd.subtract(cytdColl));
            collTableData.setTotalDmd(totalDmd);
            collTableData.setLytdColl(lytdColl);
            if (cytdDmd != BigDecimal.valueOf(0)) {
                performance = (collTableData.getCytdColl().multiply(PropertyTaxConstants.BIGDECIMAL_100))
                        .divide(cytdDmd, 1, BigDecimal.ROUND_HALF_UP);
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
     * 
     * @param collectionDetailsRequest
     * @return List
     */
    public List<BillCollectorIndex> getBillCollectorDetails(CollectionDetailsRequest collectionDetailsRequest) {
        SearchQuery searchQueryColl = new NativeSearchQueryBuilder()
                .withIndices(PropertyTaxConstants.BILL_COLLECTOR_INDEX_NAME)
                .withFields("billCollector", "mobileNumber", REVENUE_WARD)
                .withQuery(QueryBuilders.boolQuery()
                        .filter(QueryBuilders.matchQuery(CITY_CODE, collectionDetailsRequest.getUlbCode())))
                .withSort(new FieldSortBuilder("billCollector").order(SortOrder.ASC))
                .withPageable(new PageRequest(0, 250)).build();
        List<BillCollectorIndex> billCollectorsList = elasticsearchTemplate.queryForList(searchQueryColl,
                BillCollectorIndex.class);
        return billCollectorsList;
    }

    /**
     * Fetches Ward wise Bill Colelctor details
     * @param collectionDetailsRequest
     * @return Map
     */
    public Map<String, BillCollectorIndex> getWardWiseBillCollectors(CollectionDetailsRequest collectionDetailsRequest) {
        Map<String, BillCollectorIndex> wardWiseBillCollectors = new HashMap<>();
        List<BillCollectorIndex> billCollectors = getBillCollectorDetails(collectionDetailsRequest);
        for (BillCollectorIndex billCollector : billCollectors) {
            wardWiseBillCollectors.put(billCollector.getRevenueWard(), billCollector);
        }
        return wardWiseBillCollectors;
    }

}