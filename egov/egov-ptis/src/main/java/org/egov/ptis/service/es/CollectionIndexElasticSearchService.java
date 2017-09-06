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

import static org.egov.ptis.constants.PropertyTaxConstants.BIGDECIMAL_100;
import static org.egov.ptis.constants.PropertyTaxConstants.COLLECION_BILLING_SERVICE_PT;
import static org.egov.ptis.constants.PropertyTaxConstants.COLLECION_BILLING_SERVICE_VLT;
import static org.egov.ptis.constants.PropertyTaxConstants.COLLECION_BILLING_SERVICE_WTMS;
import static org.egov.ptis.constants.PropertyTaxConstants.COLLECTION_INDEX_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_BUILT_UP_PROPERTY_TYPES;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_BILLCOLLECTORWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_DISTRICTWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_GRADEWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_REGIONWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_ULBWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_WARDWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_PROPERTY_TYPE_BUILT_UP;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_PROPERTY_TYPE_CENTRAL_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_PROPERTY_TYPE_CENTRAL_GOVT_LIST;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_PROPERTY_TYPE_COURTCASES;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_PROPERTY_TYPE_PRIVATE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_USAGE_TYPE_ALL;
import static org.egov.ptis.constants.PropertyTaxConstants.DATEFORMATTER_YYYY_MM_DD;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_YYYYMMDD;
import static org.egov.ptis.constants.PropertyTaxConstants.DAY;
import static org.egov.ptis.constants.PropertyTaxConstants.MONTH;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_EWSHS;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_TAX_INDEX_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.WEEK;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.egov.infra.admin.master.entity.es.CityIndex;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.bean.dashboard.CollReceiptDetails;
import org.egov.ptis.bean.dashboard.CollTableData;
import org.egov.ptis.bean.dashboard.CollectionAnalysis;
import org.egov.ptis.bean.dashboard.CollectionDetails;
import org.egov.ptis.bean.dashboard.CollectionDetailsRequest;
import org.egov.ptis.bean.dashboard.CollectionTrend;
import org.egov.ptis.bean.dashboard.DayWiseCollection;
import org.egov.ptis.bean.dashboard.DemandCollectionMIS;
import org.egov.ptis.bean.dashboard.MonthlyDCB;
import org.egov.ptis.bean.dashboard.ReceiptTableData;
import org.egov.ptis.bean.dashboard.ReceiptsTrend;
import org.egov.ptis.bean.dashboard.WeeklyDCB;
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

    private static final String DECEMBER = "December";
    private static final String NOVEMBER = "November";
    private static final String OCTOBER = "October";
    private static final String SEPTEMBER = "September";
    private static final String AUGUST = "August";
    private static final String JULY = "July";
    private static final String JUNE = "June";
    private static final String MAY = "May";
    private static final String APRIL = "April";
    private static final String MARCH = "March";
    private static final String FEBRUARY = "Feburary";
    private static final String JANUARY = "January";
    private static final String LY_ADVANCE = "lyAdvance";
    private static final String LY_REBATE = "lyRebate";
    private static final String LY_CURRENT_CESS = "lyCurrentCess";
    private static final String LY_CURRENT_AMOUNT = "lyCurrentAmount";
    private static final String LY_ARREAR_CESS = "lyArrearCess";
    private static final String LY_ARREAR_AMOUNT = "lyArrearAmount";
    private static final String LY_PENALTY = "lyPenalty";
    private static final String LY_LATE_PAYMENT_PENALTY = "lyLatePaymentPenalty";
    private static final String CY_ADVANCE = "cyAdvance";
    private static final String CY_REBATE = "cyRebate";
    private static final String CY_PENALTY = "cyPenalty";
    private static final String CY_LATE_PAYMENT_PENALTY = "cyLatePaymentPenalty";
    private static final String CY_CURRENT_CESS = "cyCurrentCess";
    private static final String CY_CURRENT_AMOUNT = "cyCurrentAmount";
    private static final String CY_ARREAR_CESS = "cyArrearCess";
    private static final String CY_ARREAR_AMOUNT = "cyArrearAmount";
    private static final String CURRENT_INT_DMD = "currentIntDmd";
    private static final String ARREAR_INT_DMD = "arrearIntDmd";
    private static final String IS_EXEMPTED = "isExempted";
    private static final String IS_ACTIVE = "isActive";
    private static final String IS_UNDER_COURTCASE = "isUnderCourtcase";
    private static final String CONSUMER_TYPE = "consumerType";
    private static final String LATE_PAYMENT_CHARGES = "latePaymentCharges";
    private static final String CURR_CESS = "curr_cess";
    private static final String ARREAR_CESS_CONST = "arrear_cess";
    private static final String CURR_AMOUNT = "curr_amount";
    private static final String ARREAR_AMOUNT_CONST = "arrear_amount";
    private static final String CURRENT_DMD = "currentDmd";
    private static final String ARREAR_DMD = "arrearDmd";
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
        if (StringUtils.isNotBlank(collectionDetailsRequest.getPropertyType()))
            boolQuery = queryForPropertyType(collectionDetailsRequest, boolQuery, indexName);
        if (PROPERTY_TAX_INDEX_NAME.equalsIgnoreCase(indexName)
                && StringUtils.isNotBlank(collectionDetailsRequest.getUsageType()))
            boolQuery = queryForUsageType(collectionDetailsRequest, boolQuery);

        return boolQuery;
    }

    private BoolQueryBuilder queryForUsageType(CollectionDetailsRequest collectionDetailsRequest, BoolQueryBuilder boolQuery) {
        BoolQueryBuilder usageTypeQuery = boolQuery;
        if (!DASHBOARD_USAGE_TYPE_ALL.equalsIgnoreCase(collectionDetailsRequest.getUsageType()))
            usageTypeQuery = usageTypeQuery
                    .filter(QueryBuilders.matchQuery("propertyUsage", collectionDetailsRequest.getUsageType()));
        return usageTypeQuery;
    }

    public BoolQueryBuilder queryForPropertyType(CollectionDetailsRequest collectionDetailsRequest, BoolQueryBuilder boolQuery, String indexName) {
        BoolQueryBuilder propTypeQuery = boolQuery;
        /**
         * If property type is courtcases, consider court case assessments, 
         * if property type is Private, consider Private and EWSHS properties
         * if property type is BuiltUp, consider all properties except vacant land properties
         */
        if (DASHBOARD_PROPERTY_TYPE_COURTCASES.equalsIgnoreCase(collectionDetailsRequest.getPropertyType())){
            if(PROPERTY_TAX_INDEX_NAME.equalsIgnoreCase(indexName))
                propTypeQuery = propTypeQuery.filter(QueryBuilders.matchQuery(IS_UNDER_COURTCASE, true));
            else
                propTypeQuery = propTypeQuery.filter(QueryBuilders.matchQuery("conflict", 1));
        }
        else {
            if (DASHBOARD_PROPERTY_TYPE_CENTRAL_GOVT.equalsIgnoreCase(collectionDetailsRequest.getPropertyType()))
                propTypeQuery = propTypeQuery
                        .filter(QueryBuilders.termsQuery(CONSUMER_TYPE, DASHBOARD_PROPERTY_TYPE_CENTRAL_GOVT_LIST));
            else if (DASHBOARD_PROPERTY_TYPE_PRIVATE.equalsIgnoreCase(collectionDetailsRequest.getPropertyType()))
                propTypeQuery = propTypeQuery
                        .filter(QueryBuilders.termsQuery(CONSUMER_TYPE,
                                Arrays.asList(collectionDetailsRequest.getPropertyType(), OWNERSHIP_TYPE_EWSHS)));
            else if (DASHBOARD_PROPERTY_TYPE_BUILT_UP.equalsIgnoreCase(collectionDetailsRequest.getPropertyType()))
                propTypeQuery = propTypeQuery
                        .filter(QueryBuilders.termsQuery(CONSUMER_TYPE, DASHBOARD_BUILT_UP_PROPERTY_TYPES));
            else
                propTypeQuery = propTypeQuery
                        .filter(QueryBuilders.matchQuery(CONSUMER_TYPE, collectionDetailsRequest.getPropertyType()));

            if (!DASHBOARD_PROPERTY_TYPE_BUILT_UP.equalsIgnoreCase(collectionDetailsRequest.getPropertyType())) {
                if (PROPERTY_TAX_INDEX_NAME.equalsIgnoreCase(indexName))
                    propTypeQuery = propTypeQuery.filter(QueryBuilders.matchQuery(IS_UNDER_COURTCASE, false));
                else
                    propTypeQuery = propTypeQuery.filter(QueryBuilders.matchQuery("conflict", 0));
            }
        }
        return propTypeQuery;
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
        final CFinancialYear financialYear = cFinancialYearService.getFinancialYearByDate(new Date());
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
            fromDate =new Date();
            toDate = DateUtils.addDays(new Date(), 1);
        }
        // Today’s collection
        todayColl = getCollectionBetweenDates(collectionDetailsRequest, fromDate, toDate, null, TOTAL_AMOUNT);
        collectionIndexDetails.setTodayColl(todayColl);

        // Last year Today’s day collection
        todayColl = getCollectionBetweenDates(collectionDetailsRequest, DateUtils.addYears(fromDate, -1),
                DateUtils.addYears(toDate, -1), null, TOTAL_AMOUNT);
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
            fromDate = DateUtils.startOfDay(financialYear.getStartingDate());
            toDate = DateUtils.addDays(new Date(), 1);
        }
        // Current Year till today collection
        tillDateColl = getCollectionBetweenDates(collectionDetailsRequest, fromDate, toDate, null, TOTAL_AMOUNT);
        collectionIndexDetails.setCytdColl(tillDateColl);

        BigDecimal demandColl = calculateDemandCollection(collectionDetailsRequest, fromDate, toDate);
        collectionIndexDetails.setDmdColl(demandColl);

        collectionIndexDetails.setPntlyColl(getCollectionBetweenDates(collectionDetailsRequest, fromDate,
                toDate, null, LATE_PAYMENT_CHARGES));

        // Last year till same date of today’s date collection
        tillDateColl = getCollectionBetweenDates(collectionDetailsRequest, DateUtils.addYears(fromDate, -1),
                DateUtils.addYears(toDate, -1), null, TOTAL_AMOUNT);
        collectionIndexDetails.setLytdColl(tillDateColl);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getCompleteCollectionIndexDetails() is : " + timeTaken + MILLISECS);
    }

    /**
     * API provides the demand collection
     * @param collectionDetailsRequest
     * @param fromDate
     * @param toDate
     * @return BigDecimal
     */
    private BigDecimal calculateDemandCollection(CollectionDetailsRequest collectionDetailsRequest, Date fromDate, Date toDate) {
        return getCollectionBetweenDates(collectionDetailsRequest, fromDate, toDate, null, ARREAR_AMOUNT)
                .add(getCollectionBetweenDates(collectionDetailsRequest, fromDate, toDate, null, CURRENT_AMOUNT))
                .add(getCollectionBetweenDates(collectionDetailsRequest, fromDate, toDate, null, ARREAR_CESS))
                .add(getCollectionBetweenDates(collectionDetailsRequest, fromDate, toDate, null, CURRENT_CESS));
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
            Date toDate, String cityName, String fieldName) {
        Long startTime = System.currentTimeMillis();
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest, COLLECTION_INDEX_NAME);
        boolQuery = boolQuery
                .filter(QueryBuilders.rangeQuery(RECEIPT_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                .mustNot(QueryBuilders.matchQuery(STATUS, CANCELLED));
        if (StringUtils.isNotBlank(cityName))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(CITY_NAME, cityName));
        
        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(boolQuery).addAggregation(AggregationBuilders.sum(COLLECTIONTOTAL).field(fieldName))
                .build();

        Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());

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
    public List<CollTableData> getResponseTableData(CollectionDetailsRequest collectionDetailsRequest, boolean isForMISReports) {
        List<CollTableData> collIndDataList = new ArrayList<>();
        Date fromDate;
        Date toDate;
        Date endDate;
        String aggregationField = StringUtils.EMPTY;
        if(!isForMISReports)
            aggregationField = REGION_NAME;
        
        Map<String, BillCollectorIndex> wardWiseBillCollectors = new HashMap<>();
        Map<String, Map<String, BigDecimal>> currYrTillDateCollDivisionMap = new HashMap<>();
        Map<String, Map<String, BigDecimal>> lastYrTillDateCollDivisionMap = new HashMap<>();
        Map<String, Map<String, BigDecimal>> lastFinYrCollDivisionMap = new HashMap<>();
        Map<String, CollTableData> tableDataMap = new HashMap<>();
        final CFinancialYear financialYear = cFinancialYearService.getFinancialYearByDate(new Date());

        /**
         * Select the grouping based on the type parameter, by default the grouping is done based on Regions. If type is region,
         * group by Region, if type is district, group by District, if type is ulb, group by ULB
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType())) {
            aggregationField = getAggregrationField(collectionDetailsRequest);
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
            toDate = DateUtils.addDays(new Date(), 1);
        }

        Long startTime = System.currentTimeMillis();
        Long timeTaken;
        int noOfMonths;
        // For total demand
        Map<String, BigDecimal> totalDemandMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate,
                toDate, PROPERTY_TAX_INDEX_NAME, TOTAL_DEMAND, aggregationField);
        // For current year demand
        Map<String, BigDecimal> currYrTotalDemandMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate,
                toDate, PROPERTY_TAX_INDEX_NAME, TOTAL_DEMAND, aggregationField);
        // For fetching individual demands
        StringTerms individualDmdDetails = getIndividualDemands(collectionDetailsRequest, PROPERTY_TAX_INDEX_NAME,
                aggregationField, false);
        Map<String, Map<String, BigDecimal>> demandDivisionMap = new HashMap<>();
        prepareIndividualDemandsMap(individualDmdDetails, demandDivisionMap);
        
        Map<String, BigDecimal> assessmentsCountMap = getCollectionAndDemandCountResults(collectionDetailsRequest,
                null, null, PROPERTY_TAX_INDEX_NAME, CONSUMER_CODE,
                aggregationField);
        
        //Fetch collections for property type other than Courtcases
        // For today's collection
        Map<String, BigDecimal> todayCollMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate, toDate,
                COLLECTION_INDEX_NAME, TOTAL_AMOUNT, aggregationField);

        Map<String, BigDecimal> lyTodayCollMap = getCollectionAndDemandValues(collectionDetailsRequest,
                DateUtils.addYears(fromDate, -1), DateUtils.addYears(toDate, -1),
                COLLECTION_INDEX_NAME, TOTAL_AMOUNT, aggregationField);

        /**
         * For collection and demand between the date ranges if dates are sent in the request, consider fromDate and toDate+1 ,
         * else calculate from current year start date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            endDate = DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(endDate, 1);
            
        } else {
            fromDate = DateUtils.startOfDay(financialYear.getStartingDate());
            endDate = new Date();
            toDate = DateUtils.addDays(endDate, 1);
        }
        noOfMonths = DateUtils.noOfMonthsBetween(fromDate, endDate) + 1;

        Map<String, BigDecimal> cytdCollMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate, toDate,
                COLLECTION_INDEX_NAME, TOTAL_AMOUNT, aggregationField);

        // For fetching individual collections
        StringTerms collBreakupForCurrYearTillDate = getIndividualCollections(collectionDetailsRequest, fromDate, toDate,
                COLLECTION_INDEX_NAME, aggregationField);
        StringTerms collBreakupForLastYearTillDate = getIndividualCollections(collectionDetailsRequest, DateUtils.addYears(fromDate, -1),
                DateUtils.addYears(toDate, -1),
                COLLECTION_INDEX_NAME, aggregationField);
        StringTerms collBreakupForLastFinYear = getIndividualCollections(collectionDetailsRequest, DateUtils.addYears(financialYear.getStartingDate(), -1),
                DateUtils.addYears(DateUtils.addDays(financialYear.getEndingDate(), 1), -1),
                COLLECTION_INDEX_NAME, aggregationField);
        prepareIndividualCollMap(collBreakupForCurrYearTillDate, currYrTillDateCollDivisionMap, true);
        prepareIndividualCollMap(collBreakupForLastYearTillDate, lastYrTillDateCollDivisionMap, false);
        prepareIndividualCollMap(collBreakupForLastFinYear, lastFinYrCollDivisionMap, false);

        // For last year's till today's date collections
        Map<String, BigDecimal> lytdCollMap = getCollectionAndDemandValues(collectionDetailsRequest,
                DateUtils.addYears(fromDate, -1), DateUtils.addYears(toDate, -1), COLLECTION_INDEX_NAME, TOTAL_AMOUNT,
                aggregationField);
        
        // For last financial year's collections
        Map<String, BigDecimal> lastFinYrCollMap = getCollectionAndDemandValues(collectionDetailsRequest,
                DateUtils.addYears(financialYear.getStartingDate(), -1),
                DateUtils.addYears(DateUtils.addDays(financialYear.getEndingDate(), 1), -1), COLLECTION_INDEX_NAME, TOTAL_AMOUNT, aggregationField);

        // Fetch ward wise Bill Collector details for ward based grouping
        if (DASHBOARD_GROUPING_WARDWISE.equalsIgnoreCase(collectionDetailsRequest.getType()))
            wardWiseBillCollectors = getWardWiseBillCollectors(collectionDetailsRequest);

        timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getCollectionAndDemandValues() is : " + timeTaken + MILLISECS);

        startTime = System.currentTimeMillis();
        if(StringUtils.isBlank(collectionDetailsRequest.getPropertyType()))
            setDataForCollectionTab(collectionDetailsRequest, tableDataMap, aggregationField, wardWiseBillCollectors,
                    currYrTillDateCollDivisionMap, lastYrTillDateCollDivisionMap, lastFinYrCollDivisionMap, noOfMonths,
                    totalDemandMap, currYrTotalDemandMap, demandDivisionMap, assessmentsCountMap, todayCollMap, lyTodayCollMap,
                    cytdCollMap, lytdCollMap, lastFinYrCollMap);
        else
            setDataForDCBTab(collectionDetailsRequest, tableDataMap, aggregationField, wardWiseBillCollectors,
                    currYrTillDateCollDivisionMap, lastYrTillDateCollDivisionMap, lastFinYrCollDivisionMap, noOfMonths,
                    totalDemandMap, currYrTotalDemandMap, demandDivisionMap, assessmentsCountMap, todayCollMap, lyTodayCollMap,
                    cytdCollMap, lytdCollMap, lastFinYrCollMap);

        List<String> cityDetails = getCityDetails(collectionDetailsRequest, aggregationField);
        if (cityDetails.size() != tableDataMap.size()) {
            CollTableData collTableData;
            for (String name : cityDetails) {
                if (tableDataMap.get(name) == null) {
                    collTableData = new CollTableData();
                    setBoundaryDateForTable(collectionDetailsRequest, name, collTableData, aggregationField,
                            wardWiseBillCollectors);
                    collIndDataList.add(collTableData);
                } else
                    collIndDataList.add(tableDataMap.get(name));
            }
        } else
            for (Map.Entry<String, CollTableData> entry : tableDataMap.entrySet())
                collIndDataList.add(entry.getValue());
        
        timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken for setting values in getResponseTableData() is : " + timeTaken + MILLISECS);
        return collIndDataList;
    }

    private void setDataForCollectionTab(CollectionDetailsRequest collectionDetailsRequest, Map<String, CollTableData> tableDataMap,
            String aggregationField, Map<String, BillCollectorIndex> wardWiseBillCollectors,
            Map<String, Map<String, BigDecimal>> currYrTillDateCollDivisionMap,
            Map<String, Map<String, BigDecimal>> lastYrTillDateCollDivisionMap,
            Map<String, Map<String, BigDecimal>> lastFinYrCollDivisionMap, int noOfMonths, Map<String, BigDecimal> totalDemandMap,
            Map<String, BigDecimal> currYrTotalDemandMap, Map<String, Map<String, BigDecimal>> demandDivisionMap,
            Map<String, BigDecimal> assessmentsCountMap, Map<String, BigDecimal> todayCollMap,
            Map<String, BigDecimal> lyTodayCollMap, Map<String, BigDecimal> cytdCollMap, Map<String, BigDecimal> lytdCollMap,
            Map<String, BigDecimal> lastFinYrCollMap) {
        String name;
        CollTableData collIndData;
        BigDecimal cyArrearColl;
        BigDecimal cyCurrentColl;
        BigDecimal cyPenaltyColl;
        BigDecimal cyRebate;
        BigDecimal cyAdvance;
        BigDecimal lyArrearColl;
        BigDecimal lyCurrentColl;
        BigDecimal lyPenaltyColl;
        BigDecimal lyRebate;
        BigDecimal lyAdvance;
        BigDecimal arrearDemand;
        BigDecimal currentDemand;
        BigDecimal arrearInterestDemand;
        BigDecimal currentInterestDemand;
        BigDecimal totalAssessments;
        BigDecimal currentYearTotalDemand;
        BigDecimal lyTotalArrearColl;
        BigDecimal lyTotalCurrentColl;
        BigDecimal lyTotalPenaltyColl;
        BigDecimal lyTotalRebate;
        BigDecimal lyTotalAdvance;
        for (Map.Entry<String, BigDecimal> entry : cytdCollMap.entrySet()) {
            collIndData = new CollTableData();
            totalAssessments = BigDecimal.ZERO;
            arrearDemand = BigDecimal.ZERO;
            currentDemand = BigDecimal.ZERO;
            arrearInterestDemand = BigDecimal.ZERO;
            currentInterestDemand = BigDecimal.ZERO;
            cyArrearColl = BigDecimal.ZERO;
            cyCurrentColl = BigDecimal.ZERO;
            cyPenaltyColl = BigDecimal.ZERO;
            cyRebate = BigDecimal.ZERO;
            cyAdvance = BigDecimal.ZERO;
            lyArrearColl = BigDecimal.ZERO;
            lyCurrentColl = BigDecimal.ZERO;
            lyPenaltyColl = BigDecimal.ZERO;
            lyRebate = BigDecimal.ZERO;
            lyAdvance = BigDecimal.ZERO;
            lyTotalArrearColl = BigDecimal.ZERO;
            lyTotalCurrentColl = BigDecimal.ZERO;
            lyTotalPenaltyColl = BigDecimal.ZERO;
            lyTotalRebate = BigDecimal.ZERO;
            lyTotalAdvance = BigDecimal.ZERO;
            
            name = entry.getKey();
            if (!assessmentsCountMap.isEmpty() && assessmentsCountMap.get(name) != null)
                totalAssessments = assessmentsCountMap.get(name) == null ? BigDecimal.ZERO : assessmentsCountMap.get(name);

            if (!currYrTillDateCollDivisionMap.isEmpty() && currYrTillDateCollDivisionMap.get(name) != null) {
                cyArrearColl = (currYrTillDateCollDivisionMap.get(name).get(CY_ARREAR_AMOUNT) == null ? BigDecimal.ZERO
                        : currYrTillDateCollDivisionMap.get(name).get(CY_ARREAR_AMOUNT))
                                .add(currYrTillDateCollDivisionMap.get(name).get(CY_ARREAR_CESS) == null ? BigDecimal.ZERO
                                        : currYrTillDateCollDivisionMap.get(name).get(CY_ARREAR_CESS));
                cyCurrentColl = (currYrTillDateCollDivisionMap.get(name).get(CY_CURRENT_AMOUNT) == null ? BigDecimal.ZERO
                        : currYrTillDateCollDivisionMap.get(name).get(CY_CURRENT_AMOUNT))
                                .add(currYrTillDateCollDivisionMap.get(name).get(CY_CURRENT_CESS) == null ? BigDecimal.ZERO
                                        : currYrTillDateCollDivisionMap.get(name).get(CY_CURRENT_CESS));
                cyPenaltyColl = (currYrTillDateCollDivisionMap.get(name).get(CY_PENALTY) == null ? BigDecimal.ZERO
                        : currYrTillDateCollDivisionMap.get(name).get(CY_PENALTY))
                                .add(currYrTillDateCollDivisionMap.get(name).get(CY_LATE_PAYMENT_PENALTY) == null ? BigDecimal.ZERO
                                        : currYrTillDateCollDivisionMap.get(name).get(CY_LATE_PAYMENT_PENALTY));
                cyRebate = currYrTillDateCollDivisionMap.get(name).get(CY_REBATE) == null ? BigDecimal.ZERO
                        : currYrTillDateCollDivisionMap.get(name).get(CY_REBATE);
                cyAdvance = currYrTillDateCollDivisionMap.get(name).get(CY_ADVANCE) == null ? BigDecimal.ZERO
                        : currYrTillDateCollDivisionMap.get(name).get(CY_ADVANCE);
            }
            //For last year till date collections
            if (!lastYrTillDateCollDivisionMap.isEmpty() && lastYrTillDateCollDivisionMap.get(name) != null) {
                lyArrearColl = (lastYrTillDateCollDivisionMap.get(name).get(LY_ARREAR_AMOUNT) == null ? BigDecimal.ZERO
                        : lastYrTillDateCollDivisionMap.get(name).get(LY_ARREAR_AMOUNT))
                                .add(lastYrTillDateCollDivisionMap.get(name).get(LY_ARREAR_CESS) == null ? BigDecimal.ZERO
                                        : lastYrTillDateCollDivisionMap.get(name).get(LY_ARREAR_CESS));
                lyCurrentColl = (lastYrTillDateCollDivisionMap.get(name).get(LY_CURRENT_AMOUNT) == null ? BigDecimal.ZERO
                        : lastYrTillDateCollDivisionMap.get(name).get(LY_CURRENT_AMOUNT))
                                .add(lastYrTillDateCollDivisionMap.get(name).get(LY_CURRENT_CESS) == null ? BigDecimal.ZERO
                                        : lastYrTillDateCollDivisionMap.get(name).get(LY_CURRENT_CESS));
                lyPenaltyColl = (lastYrTillDateCollDivisionMap.get(name).get(LY_PENALTY) == null ? BigDecimal.ZERO
                        : lastYrTillDateCollDivisionMap.get(name).get(LY_PENALTY))
                                .add(lastYrTillDateCollDivisionMap.get(name).get(LY_LATE_PAYMENT_PENALTY) == null ? BigDecimal.ZERO
                                        : lastYrTillDateCollDivisionMap.get(name).get(LY_LATE_PAYMENT_PENALTY));
                lyRebate = lastYrTillDateCollDivisionMap.get(name).get(LY_REBATE) == null ? BigDecimal.ZERO
                        : lastYrTillDateCollDivisionMap.get(name).get(LY_REBATE);
                lyAdvance = lastYrTillDateCollDivisionMap.get(name).get(LY_ADVANCE) == null ? BigDecimal.ZERO
                        : lastYrTillDateCollDivisionMap.get(name).get(LY_ADVANCE);
            }
            //For last financial year complete collections
            if (!lastFinYrCollDivisionMap.isEmpty() && lastFinYrCollDivisionMap.get(name) != null) {
                lyTotalArrearColl = (lastFinYrCollDivisionMap.get(name).get(LY_ARREAR_AMOUNT) == null ? BigDecimal.ZERO
                        : lastFinYrCollDivisionMap.get(name).get(LY_ARREAR_AMOUNT))
                                .add(lastFinYrCollDivisionMap.get(name).get(LY_ARREAR_CESS) == null ? BigDecimal.ZERO
                                        : lastFinYrCollDivisionMap.get(name).get(LY_ARREAR_CESS));
                lyTotalCurrentColl = (lastFinYrCollDivisionMap.get(name).get(LY_CURRENT_AMOUNT) == null ? BigDecimal.ZERO
                        : lastFinYrCollDivisionMap.get(name).get(LY_CURRENT_AMOUNT))
                                .add(lastFinYrCollDivisionMap.get(name).get(LY_CURRENT_CESS) == null ? BigDecimal.ZERO
                                        : lastFinYrCollDivisionMap.get(name).get(LY_CURRENT_CESS));
                lyTotalPenaltyColl = (lastFinYrCollDivisionMap.get(name).get(LY_PENALTY) == null ? BigDecimal.ZERO
                        : lastFinYrCollDivisionMap.get(name).get(LY_PENALTY))
                                .add(lastFinYrCollDivisionMap.get(name).get(LY_LATE_PAYMENT_PENALTY) == null ? BigDecimal.ZERO
                                        : lastFinYrCollDivisionMap.get(name).get(LY_LATE_PAYMENT_PENALTY));
                lyTotalRebate = lastFinYrCollDivisionMap.get(name).get(LY_REBATE) == null ? BigDecimal.ZERO
                        : lastFinYrCollDivisionMap.get(name).get(LY_REBATE);
                lyTotalAdvance = lastFinYrCollDivisionMap.get(name).get(LY_ADVANCE) == null ? BigDecimal.ZERO
                        : lastFinYrCollDivisionMap.get(name).get(LY_ADVANCE);
            }

            if (!demandDivisionMap.isEmpty() && demandDivisionMap.get(name) != null) {
                arrearDemand = demandDivisionMap.get(name).get(ARREAR_DMD) == null ? BigDecimal.ZERO
                        : demandDivisionMap.get(name).get(ARREAR_DMD);
                currentDemand = demandDivisionMap.get(name).get(CURRENT_DMD) == null ? BigDecimal.ZERO
                        : demandDivisionMap.get(name).get(CURRENT_DMD);
                arrearInterestDemand = demandDivisionMap.get(name).get(ARREAR_INT_DMD) == null ? BigDecimal.ZERO
                        : demandDivisionMap.get(name).get(ARREAR_INT_DMD);
                currentInterestDemand = demandDivisionMap.get(name).get(CURRENT_INT_DMD) == null ? BigDecimal.ZERO
                        : demandDivisionMap.get(name).get(CURRENT_INT_DMD);

            }
            // Proportional Demand = (totalDemand/12)*noOfmonths
            currentYearTotalDemand = currYrTotalDemandMap.get(name) == null ? BigDecimal.valueOf(0)
                    : currYrTotalDemandMap.get(name);

            setBoundaryDateForTable(collectionDetailsRequest, name, collIndData, aggregationField, wardWiseBillCollectors);
            setCollAmountsForTableData(collIndData, todayCollMap.get(name) == null ? BigDecimal.ZERO : todayCollMap.get(name),
                    lyTodayCollMap.get(name) == null ? BigDecimal.ZERO : lyTodayCollMap.get(name), entry.getValue(),
                    lytdCollMap.get(name) == null ? BigDecimal.ZERO : lytdCollMap.get(name),
                    lastFinYrCollMap.get(name) == null ? BigDecimal.ZERO : lastFinYrCollMap.get(name));
            setCurrYearTillDateCollBreakUpForTableData(collIndData, cyArrearColl, cyCurrentColl, cyPenaltyColl, cyRebate, cyAdvance);
            setLastYearTillDateCollBreakUpForTableData(collIndData, lyArrearColl, lyCurrentColl, lyPenaltyColl, lyRebate, lyAdvance);
            setLastFinYearCollBreakUpForTableData(collIndData, lyTotalArrearColl, lyTotalCurrentColl, lyTotalPenaltyColl,
                    lyTotalRebate, lyTotalAdvance);
            setDemandBreakUpForTableData(collIndData, arrearDemand, currentDemand, arrearInterestDemand,
                    currentInterestDemand, noOfMonths);
            setDemandAmountsForTableData(name, collIndData, totalAssessments, currentYearTotalDemand, noOfMonths,
                    totalDemandMap);
            tableDataMap.put(name, collIndData);
        }
    }
    
    private void setDataForDCBTab(CollectionDetailsRequest collectionDetailsRequest, Map<String, CollTableData> tableDataMap,
            String aggregationField, Map<String, BillCollectorIndex> wardWiseBillCollectors,
            Map<String, Map<String, BigDecimal>> currYrTillDateCollDivisionMap,
            Map<String, Map<String, BigDecimal>> lastYrTillDateCollDivisionMap,
            Map<String, Map<String, BigDecimal>> lastFinYrCollDivisionMap, int noOfMonths, Map<String, BigDecimal> totalDemandMap,
            Map<String, BigDecimal> currYrTotalDemandMap, Map<String, Map<String, BigDecimal>> demandDivisionMap,
            Map<String, BigDecimal> assessmentsCountMap, Map<String, BigDecimal> todayCollMap,
            Map<String, BigDecimal> lyTodayCollMap, Map<String, BigDecimal> cytdCollMap, Map<String, BigDecimal> lytdCollMap,
            Map<String, BigDecimal> lastFinYrCollMap) {
        String name;
        CollTableData collIndData;
        BigDecimal cyArrearColl;
        BigDecimal cyCurrentColl;
        BigDecimal cyPenaltyColl;
        BigDecimal cyRebate;
        BigDecimal cyAdvance;
        BigDecimal lyArrearColl;
        BigDecimal lyCurrentColl;
        BigDecimal lyPenaltyColl;
        BigDecimal lyRebate;
        BigDecimal lyAdvance;
        BigDecimal arrearDemand;
        BigDecimal currentDemand;
        BigDecimal arrearInterestDemand;
        BigDecimal currentInterestDemand;
        BigDecimal totalAssessments;
        BigDecimal cytdColl;
        BigDecimal lyTotalArrearColl;
        BigDecimal lyTotalCurrentColl;
        BigDecimal lyTotalPenaltyColl;
        BigDecimal lyTotalRebate;
        BigDecimal lyTotalAdvance;
        for (Map.Entry<String, BigDecimal> entry : currYrTotalDemandMap.entrySet()) {
            collIndData = new CollTableData();
            totalAssessments = BigDecimal.ZERO;
            arrearDemand = BigDecimal.ZERO;
            currentDemand = BigDecimal.ZERO;
            arrearInterestDemand = BigDecimal.ZERO;
            currentInterestDemand = BigDecimal.ZERO;
            cyArrearColl = BigDecimal.ZERO;
            cyCurrentColl = BigDecimal.ZERO;
            cyPenaltyColl = BigDecimal.ZERO;
            cyRebate = BigDecimal.ZERO;
            cyAdvance = BigDecimal.ZERO;
            lyArrearColl = BigDecimal.ZERO;
            lyCurrentColl = BigDecimal.ZERO;
            lyPenaltyColl = BigDecimal.ZERO;
            lyRebate = BigDecimal.ZERO;
            lyAdvance = BigDecimal.ZERO;
            lyTotalArrearColl = BigDecimal.ZERO;
            lyTotalCurrentColl = BigDecimal.ZERO;
            lyTotalPenaltyColl = BigDecimal.ZERO;
            lyTotalRebate = BigDecimal.ZERO;
            lyTotalAdvance = BigDecimal.ZERO;
            
            name = entry.getKey();
            if (!assessmentsCountMap.isEmpty() && assessmentsCountMap.get(name) != null)
                totalAssessments = assessmentsCountMap.get(name) == null ? BigDecimal.ZERO : assessmentsCountMap.get(name);

            if (!currYrTillDateCollDivisionMap.isEmpty() && currYrTillDateCollDivisionMap.get(name) != null) {
                cyArrearColl = (currYrTillDateCollDivisionMap.get(name).get(CY_ARREAR_AMOUNT) == null ? BigDecimal.ZERO
                        : currYrTillDateCollDivisionMap.get(name).get(CY_ARREAR_AMOUNT))
                                .add(currYrTillDateCollDivisionMap.get(name).get(CY_ARREAR_CESS) == null ? BigDecimal.ZERO
                                        : currYrTillDateCollDivisionMap.get(name).get(CY_ARREAR_CESS));
                cyCurrentColl = (currYrTillDateCollDivisionMap.get(name).get(CY_CURRENT_AMOUNT) == null ? BigDecimal.ZERO
                        : currYrTillDateCollDivisionMap.get(name).get(CY_CURRENT_AMOUNT))
                                .add(currYrTillDateCollDivisionMap.get(name).get(CY_CURRENT_CESS) == null ? BigDecimal.ZERO
                                        : currYrTillDateCollDivisionMap.get(name).get(CY_CURRENT_CESS));
                cyPenaltyColl = (currYrTillDateCollDivisionMap.get(name).get(CY_PENALTY) == null ? BigDecimal.ZERO
                        : currYrTillDateCollDivisionMap.get(name).get(CY_PENALTY))
                                .add(currYrTillDateCollDivisionMap.get(name).get(CY_LATE_PAYMENT_PENALTY) == null ? BigDecimal.ZERO
                                        : currYrTillDateCollDivisionMap.get(name).get(CY_LATE_PAYMENT_PENALTY));
                cyRebate = currYrTillDateCollDivisionMap.get(name).get(CY_REBATE) == null ? BigDecimal.ZERO
                        : currYrTillDateCollDivisionMap.get(name).get(CY_REBATE);
                cyAdvance = currYrTillDateCollDivisionMap.get(name).get(CY_ADVANCE) == null ? BigDecimal.ZERO
                        : currYrTillDateCollDivisionMap.get(name).get(CY_ADVANCE);
            }
            //For last year till date collections
            if (!lastYrTillDateCollDivisionMap.isEmpty() && lastYrTillDateCollDivisionMap.get(name) != null) {
                lyArrearColl = (lastYrTillDateCollDivisionMap.get(name).get(LY_ARREAR_AMOUNT) == null ? BigDecimal.ZERO
                        : lastYrTillDateCollDivisionMap.get(name).get(LY_ARREAR_AMOUNT))
                                .add(lastYrTillDateCollDivisionMap.get(name).get(LY_ARREAR_CESS) == null ? BigDecimal.ZERO
                                        : lastYrTillDateCollDivisionMap.get(name).get(LY_ARREAR_CESS));
                lyCurrentColl = (lastYrTillDateCollDivisionMap.get(name).get(LY_CURRENT_AMOUNT) == null ? BigDecimal.ZERO
                        : lastYrTillDateCollDivisionMap.get(name).get(LY_CURRENT_AMOUNT))
                                .add(lastYrTillDateCollDivisionMap.get(name).get(LY_CURRENT_CESS) == null ? BigDecimal.ZERO
                                        : lastYrTillDateCollDivisionMap.get(name).get(LY_CURRENT_CESS));
                lyPenaltyColl = (lastYrTillDateCollDivisionMap.get(name).get(LY_PENALTY) == null ? BigDecimal.ZERO
                        : lastYrTillDateCollDivisionMap.get(name).get(LY_PENALTY))
                                .add(lastYrTillDateCollDivisionMap.get(name).get(LY_LATE_PAYMENT_PENALTY) == null ? BigDecimal.ZERO
                                        : lastYrTillDateCollDivisionMap.get(name).get(LY_LATE_PAYMENT_PENALTY));
                lyRebate = lastYrTillDateCollDivisionMap.get(name).get(LY_REBATE) == null ? BigDecimal.ZERO
                        : lastYrTillDateCollDivisionMap.get(name).get(LY_REBATE);
                lyAdvance = lastYrTillDateCollDivisionMap.get(name).get(LY_ADVANCE) == null ? BigDecimal.ZERO
                        : lastYrTillDateCollDivisionMap.get(name).get(LY_ADVANCE);
            }
            //For last financial year complete collections
            if (!lastFinYrCollDivisionMap.isEmpty() && lastFinYrCollDivisionMap.get(name) != null) {
                lyTotalArrearColl = (lastFinYrCollDivisionMap.get(name).get(LY_ARREAR_AMOUNT) == null ? BigDecimal.ZERO
                        : lastFinYrCollDivisionMap.get(name).get(LY_ARREAR_AMOUNT))
                                .add(lastFinYrCollDivisionMap.get(name).get(LY_ARREAR_CESS) == null ? BigDecimal.ZERO
                                        : lastFinYrCollDivisionMap.get(name).get(LY_ARREAR_CESS));
                lyTotalCurrentColl = (lastFinYrCollDivisionMap.get(name).get(LY_CURRENT_AMOUNT) == null ? BigDecimal.ZERO
                        : lastFinYrCollDivisionMap.get(name).get(LY_CURRENT_AMOUNT))
                                .add(lastFinYrCollDivisionMap.get(name).get(LY_CURRENT_CESS) == null ? BigDecimal.ZERO
                                        : lastFinYrCollDivisionMap.get(name).get(LY_CURRENT_CESS));
                lyTotalPenaltyColl = (lastFinYrCollDivisionMap.get(name).get(LY_PENALTY) == null ? BigDecimal.ZERO
                        : lastFinYrCollDivisionMap.get(name).get(LY_PENALTY))
                                .add(lastFinYrCollDivisionMap.get(name).get(LY_LATE_PAYMENT_PENALTY) == null ? BigDecimal.ZERO
                                        : lastFinYrCollDivisionMap.get(name).get(LY_LATE_PAYMENT_PENALTY));
                lyTotalRebate = lastFinYrCollDivisionMap.get(name).get(LY_REBATE) == null ? BigDecimal.ZERO
                        : lastFinYrCollDivisionMap.get(name).get(LY_REBATE);
                lyTotalAdvance = lastFinYrCollDivisionMap.get(name).get(LY_ADVANCE) == null ? BigDecimal.ZERO
                        : lastFinYrCollDivisionMap.get(name).get(LY_ADVANCE);
            }

            if (!demandDivisionMap.isEmpty() && demandDivisionMap.get(name) != null) {
                arrearDemand = demandDivisionMap.get(name).get(ARREAR_DMD) == null ? BigDecimal.ZERO
                        : demandDivisionMap.get(name).get(ARREAR_DMD);
                currentDemand = demandDivisionMap.get(name).get(CURRENT_DMD) == null ? BigDecimal.ZERO
                        : demandDivisionMap.get(name).get(CURRENT_DMD);
                arrearInterestDemand = demandDivisionMap.get(name).get(ARREAR_INT_DMD) == null ? BigDecimal.ZERO
                        : demandDivisionMap.get(name).get(ARREAR_INT_DMD);
                currentInterestDemand = demandDivisionMap.get(name).get(CURRENT_INT_DMD) == null ? BigDecimal.ZERO
                        : demandDivisionMap.get(name).get(CURRENT_INT_DMD);

            }
            cytdColl = cytdCollMap.get(name) == null ? BigDecimal.valueOf(0)
                    : cytdCollMap.get(name);

            setBoundaryDateForTable(collectionDetailsRequest, name, collIndData, aggregationField, wardWiseBillCollectors);
            setCollAmountsForTableData(collIndData, todayCollMap.get(name) == null ? BigDecimal.ZERO : todayCollMap.get(name),
                    lyTodayCollMap.get(name) == null ? BigDecimal.ZERO : lyTodayCollMap.get(name), cytdColl,
                    lytdCollMap.get(name) == null ? BigDecimal.ZERO : lytdCollMap.get(name),
                    lastFinYrCollMap.get(name) == null ? BigDecimal.ZERO : lastFinYrCollMap.get(name));
            setCurrYearTillDateCollBreakUpForTableData(collIndData, cyArrearColl, cyCurrentColl, cyPenaltyColl, cyRebate, cyAdvance);
            setLastYearTillDateCollBreakUpForTableData(collIndData, lyArrearColl, lyCurrentColl, lyPenaltyColl, lyRebate, lyAdvance);
            setLastFinYearCollBreakUpForTableData(collIndData, lyTotalArrearColl, lyTotalCurrentColl, lyTotalPenaltyColl,
                    lyTotalRebate, lyTotalAdvance);
            setDemandBreakUpForTableData(collIndData, arrearDemand, currentDemand, arrearInterestDemand,
                    currentInterestDemand, noOfMonths);
            setDemandAmountsForTableData(name, collIndData, totalAssessments, entry.getValue(), noOfMonths,
                    totalDemandMap);
            tableDataMap.put(name, collIndData);
        }
    }

    private void setDemandAmountsForTableData(String name, CollTableData collIndData, BigDecimal totalAssessments,
            BigDecimal currentYearTotalDemand, int noOfMonths, Map<String, BigDecimal> totalDemandMap) {
        BigDecimal variance;
        // Proportional Demand = (totalDemand/12)*noOfmonths
        BigDecimal cytdDmd = (currentYearTotalDemand.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP))
                .multiply(BigDecimal.valueOf(noOfMonths));
        collIndData.setCytdDmd(cytdDmd);
        if (cytdDmd != BigDecimal.valueOf(0)) {
            BigDecimal balance = cytdDmd.subtract(collIndData.getCytdColl());
            BigDecimal performance = (collIndData.getCytdColl().multiply(PropertyTaxConstants.BIGDECIMAL_100)).divide(cytdDmd,
                    1, BigDecimal.ROUND_HALF_UP);
            collIndData.setPerformance(performance);
            collIndData.setCytdBalDmd(balance);
        }
        collIndData.setTotalDmd(totalDemandMap.get(name) == null ? BigDecimal.ZERO : totalDemandMap.get(name));
        collIndData.setDayTargetDemand(collIndData.getTotalDmd().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                : collIndData.getTotalDmd().divide(new BigDecimal("365"), 0,
                        BigDecimal.ROUND_HALF_UP));
        collIndData.setTotalAssessments(totalAssessments);
        // variance = ((currentYearCollection - lastYearCollection)*100)/lastYearCollection
        if (collIndData.getLytdColl().compareTo(BigDecimal.ZERO) == 0)
            variance = PropertyTaxConstants.BIGDECIMAL_100;
        else
            variance = ((collIndData.getCytdColl().subtract(collIndData.getLytdColl()))
                    .multiply(PropertyTaxConstants.BIGDECIMAL_100)).divide(collIndData.getLytdColl(), 1,
                            BigDecimal.ROUND_HALF_UP);
        collIndData.setLyVar(variance);
    }

    private void setDemandBreakUpForTableData(CollTableData collIndData, BigDecimal arrearDemand, BigDecimal currentDemand,
            BigDecimal arrearInterestDemand, BigDecimal currentInterestDemand, int noOfMonths) {
        collIndData.setArrearDemand(arrearDemand);
        collIndData.setCurrentDemand(currentDemand);
        collIndData.setArrearInterestDemand(arrearInterestDemand);
        collIndData.setCurrentInterestDemand(currentInterestDemand);

        BigDecimal proportionalArrearDmd = (collIndData.getArrearDemand().divide(BigDecimal.valueOf(12),
                BigDecimal.ROUND_HALF_UP))
                        .multiply(BigDecimal.valueOf(noOfMonths));
        BigDecimal proportionalCurrDmd = (collIndData.getCurrentDemand().divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP))
                .multiply(BigDecimal.valueOf(noOfMonths));
        collIndData.setProportionalArrearDemand(proportionalArrearDmd);
        collIndData.setProportionalCurrentDemand(proportionalCurrDmd);
    }

    private void setCurrYearTillDateCollBreakUpForTableData(CollTableData collIndData, BigDecimal arrearColl, BigDecimal currentColl,
            BigDecimal penaltyColl, BigDecimal rebate, BigDecimal advanceColl) {
        collIndData.setCyArrearColl(arrearColl);
        collIndData.setCyCurrentColl(currentColl);
        collIndData.setCyPenaltyColl(penaltyColl);
        collIndData.setCyRebate(rebate.abs());
        collIndData.setCyAdvanceColl(advanceColl);
    }
    
    private void setLastYearTillDateCollBreakUpForTableData(CollTableData collIndData, BigDecimal arrearColl, BigDecimal currentColl,
            BigDecimal penaltyColl, BigDecimal rebate, BigDecimal advanceColl) {
        collIndData.setLyArrearColl(arrearColl);
        collIndData.setLyCurrentColl(currentColl);
        collIndData.setLyPenaltyColl(penaltyColl);
        collIndData.setLyRebate(rebate.abs());
        collIndData.setLyAdvanceColl(advanceColl);
    }
    
    private void setLastFinYearCollBreakUpForTableData(CollTableData collIndData, BigDecimal lyTotalArrearColl, BigDecimal lyTotalCurrentColl,
            BigDecimal lyTotalPenaltyColl, BigDecimal lyTotalRebate, BigDecimal lyTotalAdvanceColl) {
        collIndData.setLyTotalArrearsColl(lyTotalArrearColl);
        collIndData.setLyTotalCurrentColl(lyTotalCurrentColl);
        collIndData.setLyTotalPenaltyColl(lyTotalPenaltyColl);
        collIndData.setLyTotalRebate(lyTotalRebate.abs());
        collIndData.setLyTotalAdvanceColl(lyTotalAdvanceColl);
    }
    
    private void setCollAmountsForTableData(CollTableData collIndData, 
            BigDecimal todayColl, BigDecimal lyTodayColl, BigDecimal cytdColl, BigDecimal lytdColl, BigDecimal lyTotalColl) {
        collIndData.setTodayColl(todayColl);
        collIndData.setLyTodayColl(lyTodayColl);
        collIndData.setCytdColl(cytdColl);
        collIndData.setLytdColl(lytdColl);
        collIndData.setLyTotalColl(lyTotalColl);
    }

    private void setBoundaryDateForTable(CollectionDetailsRequest collectionDetailsRequest, String name,
            CollTableData collIndData, String aggregationField, Map<String, BillCollectorIndex> wardWiseBillCollectors) {
        if (REGION_NAME.equals(aggregationField))
            collIndData.setRegionName(name);
        else if (DISTRICT_NAME.equals(aggregationField)) {
            collIndData.setRegionName(collectionDetailsRequest.getRegionName());
            collIndData.setDistrictName(name);
        } else if (CITY_NAME.equals(aggregationField)) {
            collIndData.setUlbName(name);
            collIndData.setDistrictName(collectionDetailsRequest.getDistrictName());
            collIndData.setUlbGrade(collectionDetailsRequest.getUlbGrade());
        } else if (CITY_GRADE.equals(aggregationField))
            collIndData.setUlbGrade(name);
        else if (REVENUE_WARD.equals(aggregationField)) {
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
        collIndData.setBoundaryName(name);
    }

    public String getAggregrationField(CollectionDetailsRequest collectionDetailsRequest) {
        String aggregationField = REGION_NAME;
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
        return aggregationField;
    }

    /**
     * Prepares the map for individual demands
     * @param individualDmdDetails
     * @param demandDivisionMap
     */
    public void prepareIndividualDemandsMap(StringTerms individualDmdDetails,
            Map<String, Map<String, BigDecimal>> demandDivisionMap) {
        Map<String, BigDecimal> individualDmdMap;
        Sum arrearDmd;
        Sum currentDmd;
        Sum arrearInterestDmd;
        Sum currentInterestDmd;
        if (individualDmdDetails != null) {
            for (Terms.Bucket entry : individualDmdDetails.getBuckets()) {
                individualDmdMap = new HashMap<>();
                arrearDmd = entry.getAggregations().get("arrear_dmd");
                currentDmd = entry.getAggregations().get("curr_dmd");
                arrearInterestDmd = entry.getAggregations().get("arrear_interest_dmd");
                currentInterestDmd = entry.getAggregations().get("curr_interest_dmd");      

                individualDmdMap.put(ARREAR_DMD,
                        BigDecimal.valueOf(arrearDmd.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualDmdMap.put(CURRENT_DMD,
                        BigDecimal.valueOf(currentDmd.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualDmdMap.put(ARREAR_INT_DMD,
                        BigDecimal.valueOf(arrearInterestDmd.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualDmdMap.put(CURRENT_INT_DMD,
                        BigDecimal.valueOf(currentInterestDmd.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));

                demandDivisionMap.put(String.valueOf(entry.getKey()), individualDmdMap);
            }
        }
    }

    /**
     * Prepares the map for individual collections
     * @param individualCollDetails
     * @param collectionDivisionMap
     */
    public void prepareIndividualCollMap(StringTerms collBreakup,
            Map<String, Map<String, BigDecimal>> collectionDivisionMap, boolean isForCurrYear) {
        if (collBreakup != null) 
            prepareCollBreakupMap(collBreakup, collectionDivisionMap, isForCurrYear);
    }

    private void prepareCollBreakupMap(StringTerms collBreakup,
            Map<String, Map<String, BigDecimal>> collectionDivisionMap, boolean isForCurrYear) {
        Map<String, BigDecimal> individualCollMap;
        Sum arrearAmt;
        Sum currentAmt;
        Sum arrearCess;
        Sum currentCess;
        Sum penaltyAmt;
        Sum latePaymentPenaltyAmt;
        Sum rebate;
        Sum advance;
        String arrearAmtKey;
        String currentAmtKey;
        String arrearCessKey;
        String currentCessKey;
        String penaltyAmtKey;
        String latePaymentPenaltyAmtKey;
        String rebateAmtKey;
        String advanceAmtKey;
        if(isForCurrYear){
            arrearAmtKey = CY_ARREAR_AMOUNT;
            currentAmtKey = CY_CURRENT_AMOUNT;
            arrearCessKey = CY_ARREAR_CESS;
            currentCessKey = CY_CURRENT_CESS;
            penaltyAmtKey = CY_PENALTY;
            latePaymentPenaltyAmtKey = CY_LATE_PAYMENT_PENALTY;
            rebateAmtKey = CY_REBATE;
            advanceAmtKey = CY_ADVANCE;
        } else {
            arrearAmtKey = LY_ARREAR_AMOUNT;
            currentAmtKey = LY_CURRENT_AMOUNT;
            arrearCessKey = LY_ARREAR_CESS;
            currentCessKey = LY_CURRENT_CESS;
            penaltyAmtKey = LY_PENALTY;
            latePaymentPenaltyAmtKey = LY_LATE_PAYMENT_PENALTY;
            rebateAmtKey = LY_REBATE;
            advanceAmtKey = LY_ADVANCE;
        }
        for (Terms.Bucket entry : collBreakup.getBuckets()) {
            individualCollMap = new HashMap<>();
            
            arrearAmt = entry.getAggregations().get("arrear_amount");
            currentAmt = entry.getAggregations().get("curr_amount");
            arrearCess = entry.getAggregations().get("arrear_cess");
            currentCess = entry.getAggregations().get("curr_cess");
            penaltyAmt = entry.getAggregations().get("penalty");
            latePaymentPenaltyAmt = entry.getAggregations().get("latePaymentPenalty");
            rebate = entry.getAggregations().get("rebate");
            advance = entry.getAggregations().get("advance");

            individualCollMap.put(arrearAmtKey,
                    BigDecimal.valueOf(arrearAmt.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            individualCollMap.put(currentAmtKey,
                    BigDecimal.valueOf(currentAmt.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            individualCollMap.put(arrearCessKey,
                    BigDecimal.valueOf(arrearCess.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            individualCollMap.put(currentCessKey,
                    BigDecimal.valueOf(currentCess.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            individualCollMap.put(penaltyAmtKey,
                    BigDecimal.valueOf(penaltyAmt.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            individualCollMap.put(latePaymentPenaltyAmtKey,
                    BigDecimal.valueOf(latePaymentPenaltyAmt.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            individualCollMap.put(rebateAmtKey,
                    BigDecimal.valueOf(rebate.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            individualCollMap.put(advanceAmtKey,
                    BigDecimal.valueOf(advance.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));

            collectionDivisionMap.put(String.valueOf(entry.getKey()), individualCollMap);
        }
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
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(IS_ACTIVE, true))
                    .filter(QueryBuilders.matchQuery(IS_EXEMPTED, false));

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
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(IS_ACTIVE, true))
                    .filter(QueryBuilders.matchQuery(IS_EXEMPTED, false));

        AggregationBuilder aggregation = AggregationBuilders.terms(BY_CITY).field(aggregationField).size(120)
                    .subAggregation(AggregationBuilders.sum(ARREAR_AMOUNT_CONST).field(ARREAR_AMOUNT))
                    .subAggregation(AggregationBuilders.sum(CURR_AMOUNT).field(CURRENT_AMOUNT))
                    .subAggregation(AggregationBuilders.sum(ARREAR_CESS_CONST).field(ARREAR_CESS))
                    .subAggregation(AggregationBuilders.sum(CURR_CESS).field(CURRENT_CESS))
                    .subAggregation(AggregationBuilders.sum("penalty").field("penaltyAmount"))
                    .subAggregation(AggregationBuilders.sum("latePaymentPenalty").field(LATE_PAYMENT_CHARGES))
                    .subAggregation(AggregationBuilders.sum("rebate").field("reductionAmount"))
                    .subAggregation(AggregationBuilders.sum("advance").field("advanceAmount"));
        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(indexName).withQuery(boolQuery)
                .addAggregation(aggregation).build();

            Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());

        return collAggr.get(BY_CITY);
    }
    
    /**
     * Provides demand break-up of total amount
     * @param collectionDetailsRequest
     * @param indexName
     * @param aggregationField
     * @return StringTerms
     */
    public StringTerms getIndividualDemands(CollectionDetailsRequest collectionDetailsRequest,
            String indexName, String aggregationField, boolean isForMis) {
        AggregationBuilder aggregation;
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest, indexName);
        boolQuery = boolQuery.filter(QueryBuilders.matchQuery(IS_ACTIVE, true))
                .filter(QueryBuilders.matchQuery(IS_EXEMPTED, false));
        
        if(isForMis)
            aggregation = AggregationBuilders.terms(BY_CITY).field(aggregationField).size(120)
                    .subAggregation(AggregationBuilders.sum("arrear_dmd").field("arrearDemand"))
                    .subAggregation(AggregationBuilders.sum("curr_dmd").field("annualDemand"))
                    .subAggregation(AggregationBuilders.sum("arrear_interest_dmd").field("arrearInterestDemand"))
                    .subAggregation(AggregationBuilders.sum("curr_interest_dmd").field("currentInterestDemand"))
                    .subAggregation(AggregationBuilders.sum("total_dmd").field("totalDemand"))
                    .subAggregation(AggregationBuilders.sum("adjustment").field("adjustment"))
                    .subAggregation(AggregationBuilders.sum("arrear_coll").field("arrearCollection"))
                    .subAggregation(AggregationBuilders.sum("curr_coll").field("annualCollection"))
                    .subAggregation(AggregationBuilders.sum("arrear_interest_coll").field("arrearInterestCollection"))
                    .subAggregation(AggregationBuilders.sum("curr_interest_coll").field("currentInterestCollection"))
                    .subAggregation(AggregationBuilders.sum("advance").field("advance"))
                    .subAggregation(AggregationBuilders.sum("rebate").field("rebate"))
                    .subAggregation(AggregationBuilders.sum("total_coll").field("totalCollection"));
        else
            aggregation = AggregationBuilders.terms(BY_CITY).field(aggregationField).size(120)
                    .subAggregation(AggregationBuilders.sum("arrear_dmd").field("arrearDemand"))
                    .subAggregation(AggregationBuilders.sum("curr_dmd").field("annualDemand"))
                    .subAggregation(AggregationBuilders.sum("arrear_interest_dmd").field("arrearInterestDemand"))
                    .subAggregation(AggregationBuilders.sum("curr_interest_dmd").field("currentInterestDemand"));

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
        Date fromDate;
        Date toDate;
        Date dateForMonth;
        String[] dateArr;
        Integer month;
        Sum aggregateSum;
        String aggregationField = StringUtils.EMPTY;
        CFinancialYear financialYear;
        Map<Integer, String> monthValuesMap = DateUtils.getAllMonthsWithFullNames();
        String monthName;
        List<Map<String, BigDecimal>> yearwiseMonthlyCollList = new ArrayList<>();
        Map<String, BigDecimal> monthwiseColl;
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType())) 
            aggregationField = getAggregrationField(collectionDetailsRequest);
        
        /**
         * For month-wise collections between the date ranges if dates are sent in the request, consider fromDate and toDate+1 ,
         * else calculate from current year start date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
            financialYear = cFinancialYearService.getFinancialYearByDate(fromDate);
        } else {
            financialYear = cFinancialYearService.getFinancialYearByDate(new Date());
            fromDate = DateUtils.startOfDay(financialYear.getStartingDate());
            toDate = DateUtils.addDays(new Date(), 1);
        }

        Date finYearStartDate = financialYear.getStartingDate();
        Date finYearEndDate = financialYear.getEndingDate();
        Long startTime = System.currentTimeMillis();
        for (int count = 0; count <= 2; count++) {
            monthwiseColl = new LinkedHashMap<>();
            Aggregations collAggr = getMonthwiseCollectionsForConsecutiveYears(collectionDetailsRequest, fromDate,
                    toDate, false, null, aggregationField);
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
                toDate = DateUtils.addYears(DateUtils.addDays(finYearEndDate, 1), -1);
            }
            finYearStartDate = DateUtils.addYears(finYearStartDate, -1);
            finYearEndDate = DateUtils.addYears(finYearEndDate, -1);
        }
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getMonthwiseCollectionsForConsecutiveYears() for 3 consecutive years is : "
                + timeTaken + MILLISECS);

        startTime = System.currentTimeMillis();
        populateCollTrends(collectionDetailsRequest, collTrendsList, yearwiseMonthlyCollList);
        timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug(
                "Time taken setting values in getMonthwiseCollectionDetails() is : " + timeTaken + MILLISECS);
        return collTrendsList;
    }

    private void populateCollTrends(CollectionDetailsRequest collectionDetailsRequest, List<CollectionTrend> collTrendsList,
            List<Map<String, BigDecimal>> yearwiseMonthlyCollList) {
        CollectionTrend collTrend;
        /**
         * If dates are passed in request, get result for the date range, else get results for all 12 months
         */
        if (StringUtils.isBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isBlank(collectionDetailsRequest.getToDate())) {
            for (Map.Entry<Integer, String> entry : DateUtils.getAllFinancialYearMonthsWithFullNames().entrySet()) {
                collTrend = new CollectionTrend();
                setCollTrends(collTrend, entry.getValue(),
                        yearwiseMonthlyCollList.get(0).get(entry.getValue()) == null ? BigDecimal.ZERO
                                : yearwiseMonthlyCollList.get(0).get(entry.getValue()),
                        yearwiseMonthlyCollList.get(1).get(entry.getValue()) == null ? BigDecimal.ZERO
                                : yearwiseMonthlyCollList.get(1).get(entry.getValue()),
                        yearwiseMonthlyCollList.get(2).get(entry.getValue()) == null ? BigDecimal.ZERO
                                : yearwiseMonthlyCollList.get(2).get(entry.getValue()));

                collTrendsList.add(collTrend);
            }
        } else {
            for (Map.Entry<String, BigDecimal> entry : yearwiseMonthlyCollList.get(0).entrySet()) {
                collTrend = new CollectionTrend();
                setCollTrends(collTrend, entry.getKey(), entry.getValue(),
                        yearwiseMonthlyCollList.get(1).get(entry.getKey()) == null ? BigDecimal.ZERO
                                : yearwiseMonthlyCollList.get(1).get(entry.getKey()),
                        yearwiseMonthlyCollList.get(2).get(entry.getKey()) == null ? BigDecimal.ZERO
                                : yearwiseMonthlyCollList.get(2).get(entry.getKey()));
                collTrendsList.add(collTrend);
            }
        }
    }

   

    private void setCollTrends(CollectionTrend collTrend,
            String month, BigDecimal cyColl, BigDecimal lyColl, BigDecimal pyColl) {
        collTrend.setMonth(month);
        collTrend.setCyColl(cyColl);
        collTrend.setLyColl(lyColl);
        collTrend.setPyColl(pyColl);
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
            Date fromDate, Date toDate, boolean isForMISReports, String intervalType, String aggregationField) {
        AggregationBuilder aggregationBuilder;
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest, COLLECTION_INDEX_NAME);
        boolQuery = boolQuery.filter(QueryBuilders.rangeQuery(RECEIPT_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                .mustNot(QueryBuilders.matchQuery(STATUS, CANCELLED));
        //In case of MIS APIs, grouping will be done based on interval type
        if(isForMISReports){
            DateHistogramInterval interval = null;
            if(StringUtils.isNotBlank(intervalType)){
                if(MONTH.equalsIgnoreCase(intervalType))
                    interval = DateHistogramInterval.MONTH;
                else if(WEEK.equalsIgnoreCase(intervalType))
                    interval = DateHistogramInterval.WEEK;
                else if(DAY.equalsIgnoreCase(intervalType))
                    interval = DateHistogramInterval.DAY;
            }
            aggregationBuilder = AggregationBuilders.terms(BY_CITY).field(aggregationField).size(120)
                    .subAggregation(AggregationBuilders.dateHistogram(DATE_AGG).field(RECEIPT_DATE)
                    .interval(interval)
                    .subAggregation(AggregationBuilders.sum("current_total").field(TOTAL_AMOUNT)));
        }
        else
            aggregationBuilder = AggregationBuilders.dateHistogram(DATE_AGG).field(RECEIPT_DATE)
                .interval(DateHistogramInterval.MONTH)
                .subAggregation(AggregationBuilders.sum("current_total").field(TOTAL_AMOUNT));

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(boolQuery)
                .addAggregation(aggregationBuilder).build();

        return elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());
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
        CFinancialYear financialYear = cFinancialYearService.getFinancialYearByDate(new Date());

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
            fromDate =new Date();
            toDate = DateUtils.addDays(new Date(), 1);
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
            fromDate = DateUtils.startOfDay(financialYear.getStartingDate());
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
            fromDate = DateUtils.startOfDay(financialYear.getStartingDate());
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
        final CFinancialYear financialYear = cFinancialYearService.getFinancialYearByDate(new Date());

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
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        } else {
            fromDate = new Date();
            toDate = DateUtils.addDays(new Date(), 1);
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
            fromDate = DateUtils.startOfDay(financialYear.getStartingDate());
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
        else if (indexName.equals(PROPERTY_TAX_INDEX_NAME))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(IS_ACTIVE, true))
                .filter(QueryBuilders.matchQuery(IS_EXEMPTED, false));

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
        CollTableData collTableData;
        String billCollectorNameNumber;
        /**
         * Fetch the Ward-wise data
         */
        List<CollTableData> wardWiseData = getResponseTableData(collectionDetailsRequest, false);
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
            setTableValuesForBillCollector(collTableData, entry);
            billCollectorWiseTableData.add(collTableData);
        }
        return billCollectorWiseTableData;
    }

    private void setTableValuesForBillCollector(CollTableData collTableData, Entry<String, List<CollTableData>> entry) {
        BigDecimal currDayColl = BigDecimal.ZERO;
        BigDecimal cytdColl = BigDecimal.ZERO;
        BigDecimal lytdColl = BigDecimal.ZERO;
        BigDecimal cytdDmd = BigDecimal.ZERO;
        BigDecimal performance;
        BigDecimal totalDmd = BigDecimal.ZERO;
        BigDecimal variance;
        BigDecimal cyArrearColl = BigDecimal.ZERO;
        BigDecimal cyCurrentColl = BigDecimal.ZERO;
        BigDecimal cyPenaltyColl = BigDecimal.ZERO;
        BigDecimal cyRebate = BigDecimal.ZERO;
        BigDecimal cyAdvance = BigDecimal.ZERO;
        BigDecimal lyArrearColl = BigDecimal.ZERO;
        BigDecimal lyCurrentColl = BigDecimal.ZERO;
        BigDecimal lyPenaltyColl = BigDecimal.ZERO;
        BigDecimal lyRebate = BigDecimal.ZERO;
        BigDecimal lyAdvance = BigDecimal.ZERO;
        BigDecimal arrearDmd = BigDecimal.ZERO;
        BigDecimal currentDmd = BigDecimal.ZERO;
        BigDecimal proportionalArrearDmd = BigDecimal.ZERO;
        BigDecimal proportionalCurrentDmd = BigDecimal.ZERO;
        BigDecimal dayTargetDmd = BigDecimal.ZERO;
        BigDecimal lyTodayColl = BigDecimal.ZERO;
        BigDecimal totalAssessments = BigDecimal.ZERO;
        BigDecimal arrearInterestDemand = BigDecimal.ZERO;
        BigDecimal currentInterestDemand = BigDecimal.ZERO;
        BigDecimal lyTotalArrearColl = BigDecimal.ZERO;
        BigDecimal lyTotalCurrentColl = BigDecimal.ZERO;
        BigDecimal lyTotalPenaltyColl = BigDecimal.ZERO;
        BigDecimal lyTotalRebate = BigDecimal.ZERO;
        BigDecimal lyTotalAdvance = BigDecimal.ZERO;
        BigDecimal lyTotalColl = BigDecimal.ZERO;
        
        String[] billCollectorNameNumberArr = entry.getKey().split("~");
        for (CollTableData tableData : entry.getValue()) {
            currDayColl = currDayColl
                    .add(tableData.getTodayColl() == null ? BigDecimal.ZERO : tableData.getTodayColl());
            cytdColl = cytdColl.add(tableData.getCytdColl() == null ? BigDecimal.ZERO : tableData.getCytdColl());
            cytdDmd = cytdDmd.add(tableData.getCytdDmd() == null ? BigDecimal.ZERO : tableData.getCytdDmd());
            totalDmd = totalDmd.add(tableData.getTotalDmd() == null ? BigDecimal.ZERO : tableData.getTotalDmd());
            lytdColl = lytdColl.add(tableData.getLytdColl() == null ? BigDecimal.ZERO : tableData.getLytdColl());
            lyTotalColl = lyTotalColl.add(tableData.getLyTotalColl() == null ? BigDecimal.ZERO : tableData.getLyTotalColl());
            
            cyArrearColl = cyArrearColl.add(tableData.getCyArrearColl() == null ? BigDecimal.ZERO : tableData.getCyArrearColl());
            cyCurrentColl = cyCurrentColl.add(tableData.getCyCurrentColl() == null ? BigDecimal.ZERO : tableData.getCyCurrentColl());
            cyPenaltyColl = cyPenaltyColl.add(tableData.getCyPenaltyColl() == null ? BigDecimal.ZERO : tableData.getCyPenaltyColl());
            cyRebate = cyRebate.add(tableData.getCyRebate() == null ? BigDecimal.ZERO : tableData.getCyRebate());
            cyAdvance = cyAdvance.add(tableData.getCyAdvanceColl() == null ? BigDecimal.ZERO : tableData.getCyAdvanceColl());
            
            lyArrearColl = lyArrearColl.add(tableData.getLyArrearColl() == null ? BigDecimal.ZERO : tableData.getLyArrearColl());
            lyCurrentColl = lyCurrentColl.add(tableData.getLyCurrentColl() == null ? BigDecimal.ZERO : tableData.getLyCurrentColl());
            lyPenaltyColl = lyPenaltyColl.add(tableData.getLyPenaltyColl() == null ? BigDecimal.ZERO : tableData.getLyPenaltyColl());
            lyRebate = lyRebate.add(tableData.getLyRebate() == null ? BigDecimal.ZERO : tableData.getLyRebate());
            lyAdvance = lyAdvance.add(tableData.getLyAdvanceColl() == null ? BigDecimal.ZERO : tableData.getLyAdvanceColl());
            
            lyTotalArrearColl = lyTotalArrearColl.add(tableData.getLyTotalArrearsColl() == null ? BigDecimal.ZERO : tableData.getLyTotalArrearsColl());
            lyTotalCurrentColl = lyTotalCurrentColl.add(tableData.getLyTotalCurrentColl() == null ? BigDecimal.ZERO : tableData.getLyTotalCurrentColl());
            lyTotalPenaltyColl = lyTotalPenaltyColl.add(tableData.getLyTotalPenaltyColl() == null ? BigDecimal.ZERO : tableData.getLyTotalPenaltyColl());
            lyTotalRebate = lyTotalRebate.add(tableData.getLyTotalRebate() == null ? BigDecimal.ZERO : tableData.getLyTotalRebate());
            lyTotalAdvance = lyTotalAdvance.add(tableData.getLyTotalAdvanceColl() == null ? BigDecimal.ZERO : tableData.getLyTotalAdvanceColl());
            
            arrearDmd = arrearDmd.add(tableData.getArrearDemand() == null ? BigDecimal.ZERO : tableData.getArrearDemand());
            currentDmd = currentDmd.add(tableData.getCurrentDemand() == null ? BigDecimal.ZERO : tableData.getCurrentDemand());
            proportionalArrearDmd = proportionalArrearDmd.add(tableData.getProportionalArrearDemand() == null ? BigDecimal.ZERO : tableData.getProportionalArrearDemand());
            proportionalCurrentDmd = proportionalCurrentDmd.add(tableData.getProportionalCurrentDemand() == null ? BigDecimal.ZERO : tableData.getProportionalCurrentDemand());
            dayTargetDmd = dayTargetDmd.add(tableData.getDayTargetDemand() == null ? BigDecimal.ZERO : tableData.getDayTargetDemand());
            lyTodayColl = lyTodayColl.add(tableData.getLyTodayColl() == null ? BigDecimal.ZERO : tableData.getLyTodayColl());
            totalAssessments = totalAssessments.add(tableData.getTotalAssessments());
            arrearInterestDemand = arrearInterestDemand.add(tableData.getArrearInterestDemand() == null ? BigDecimal.ZERO : tableData.getArrearInterestDemand());
            currentInterestDemand = currentInterestDemand.add(tableData.getCurrentInterestDemand() == null ? BigDecimal.ZERO : tableData.getCurrentInterestDemand());
            
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
        setCurrYearTillDateCollBreakUpForTableData(collTableData, cyArrearColl, cyCurrentColl, cyPenaltyColl, cyRebate, cyAdvance);
        setLastYearTillDateCollBreakUpForTableData(collTableData, lyArrearColl, lyCurrentColl, lyPenaltyColl, lyRebate, lyAdvance);
        setLastFinYearCollBreakUpForTableData(collTableData, lyTotalArrearColl, lyTotalCurrentColl, lyTotalPenaltyColl, lyTotalRebate, lyTotalAdvance);
        collTableData.setArrearDemand(arrearDmd);
        collTableData.setCurrentDemand(currentDmd);
        collTableData.setProportionalArrearDemand(proportionalArrearDmd);
        collTableData.setProportionalCurrentDemand(proportionalCurrentDmd);
        collTableData.setDayTargetDemand(dayTargetDmd);
        collTableData.setLyTodayColl(lyTodayColl);
        collTableData.setArrearInterestDemand(arrearInterestDemand);
        collTableData.setCurrentInterestDemand(currentInterestDemand);
        collTableData.setTotalAssessments(totalAssessments);
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
    
    /**
     * Gives the total count of assessments
     * @param collectionDetailsRequest
     * @return assessments count
     */
    public Long getTotalAssessmentsCount(CollectionDetailsRequest collectionDetailsRequest, String aggregationField) {
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest, PROPERTY_TAX_INDEX_NAME)
                .filter(QueryBuilders.matchQuery(IS_ACTIVE, true))
                .filter(QueryBuilders.matchQuery(IS_EXEMPTED, false));
       
        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(PROPERTY_TAX_INDEX_NAME)
                .withQuery(boolQuery).addAggregation(AggregationBuilders.count("assessment_count").field(CONSUMER_CODE))
                .build();

        Aggregations collCountAggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());

        ValueCount aggr = collCountAggr.get("assessment_count");
        return Long.valueOf(aggr.getValue());
    }
    
    /**
     * Provides city wise collection details
     * @param collectionDetailsRequest
     * @param intervalType
     * @return List
     */
    public CollectionAnalysis getCollectionsForInterval(CollectionDetailsRequest collectionDetailsRequest,
            String intervalType) {
        Date fromDate = null;
        Date toDate = null;
        String aggregationField = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType())) 
            aggregationField = getAggregrationField(collectionDetailsRequest);
        
        Map<Integer, String> monthValuesMap = DateUtils.getAllMonthsWithFullNames();
        Map<String, Map<String, BigDecimal>> intervalwiseCollMap = new HashMap<>();
        CollectionAnalysis collectionAnalysis = new CollectionAnalysis();
        /**
         * For collections between 2 dates, consider fromDate and toDate+1 
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        }
        
        CFinancialYear financialYear = cFinancialYearService.getFinancialYearByDate(fromDate);
        Long startTime = System.currentTimeMillis();
        Aggregations collAggr = getMonthwiseCollectionsForConsecutiveYears(collectionDetailsRequest, fromDate,
                toDate, true, intervalType, aggregationField);
        StringTerms cityaggr = collAggr.get(BY_CITY);
        if (MONTH.equalsIgnoreCase(intervalType)) {
            prepareMonthlyCollMap(financialYear.getStartingDate(), financialYear.getEndingDate(), monthValuesMap,
                    intervalwiseCollMap, cityaggr);
            setCollectionsForMonths(intervalwiseCollMap, collectionAnalysis);
        } else if (WEEK.equalsIgnoreCase(intervalType)) {
            prepareWeeklyCollMap(intervalwiseCollMap, cityaggr);
            setCollectionsForWeeks(intervalwiseCollMap, collectionAnalysis);
        } else if (DAY.equalsIgnoreCase(intervalType)) {
            prepareDailyCollMap(intervalwiseCollMap, cityaggr, fromDate);
            setCollectionsForDays(intervalwiseCollMap, collectionAnalysis);
        }
        
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(
                    "Time taken setting values in getCollectionsForInterval() is : " + timeTaken + MILLISECS);
        return collectionAnalysis;
    }

    /**
     * Prepares month wise collection map  
     * @param finYearStartDate
     * @param finYearEndDate
     * @param monthValuesMap
     * @param yearwiseMonthlyCollMap
     * @param cityaggr
     */
    private void prepareMonthlyCollMap(Date finYearStartDate, Date finYearEndDate, Map<Integer, String> monthValuesMap,
            Map<String, Map<String, BigDecimal>> yearwiseMonthlyCollMap, StringTerms cityaggr) {
        Date dateForMonth;
        String[] dateArr;
        Integer month;
        Sum aggregateSum;
        String monthName;
        Map<String, BigDecimal> monthwiseColl;
        for (Terms.Bucket cityDetailsentry : cityaggr.getBuckets()) {
            String ulbName = cityDetailsentry.getKeyAsString();
            monthwiseColl = new LinkedHashMap<>();
            Histogram dateaggs = cityDetailsentry.getAggregations().get(DATE_AGG);
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
                                .compareTo(BigDecimal.ZERO) > 0) {
                    monthwiseColl.put(monthName,
                            BigDecimal.valueOf(aggregateSum.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                }
            }
            yearwiseMonthlyCollMap.put(ulbName, monthwiseColl);
        }
    }
    
    /**
     * Prepares weekwise collection map
     * @param intervalwiseCollMap
     * @param cityaggr
     */
    private void prepareWeeklyCollMap(Map<String, Map<String, BigDecimal>> intervalwiseCollMap, StringTerms cityaggr) {
        String weekName;
        int noOfWeeks;
        String ulbName;
        Sum aggregateSum;
        Map<String, BigDecimal> weekwiseColl;
        for (final Terms.Bucket cityDetailsentry : cityaggr.getBuckets()) {
            weekwiseColl = new LinkedHashMap<>();
            ulbName = cityDetailsentry.getKeyAsString();
            noOfWeeks = 0;
            Histogram dateaggs = cityDetailsentry.getAggregations().get(DATE_AGG);
            for (Histogram.Bucket entry : dateaggs.getBuckets()) {
                if (noOfWeeks == 0)
                    noOfWeeks = 1;
                weekName = "Week " + noOfWeeks;
                aggregateSum = entry.getAggregations().get("current_total");
                if (BigDecimal.valueOf(aggregateSum.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)
                        .compareTo(BigDecimal.ZERO) > 0) {
                    weekwiseColl.put(weekName, BigDecimal.valueOf(aggregateSum.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                }
                noOfWeeks++;
            }
            intervalwiseCollMap.put(ulbName, weekwiseColl);
        }
    }
    
    /**
     * Prepares collection details for each day in a week
     * @param intervalwiseCollMap
     * @param cityaggr
     * @param fromDate
     */
    private void prepareDailyCollMap(Map<String, Map<String, BigDecimal>> intervalwiseCollMap, StringTerms cityaggr, Date fromDate) {
        String day;
        int noOfDays;
        String ulbName;
        Sum aggregateSum;
        Map<String, BigDecimal> dayWiseCollFromResult;
        Map<String, BigDecimal> finalDayWiseColl;
        String resultDateStr;
        List<String> daysInWeek = new ArrayList<>();
        Date weekStartDate = fromDate;
        for (int count = 0; count < 7; count++) {
            daysInWeek.add(PropertyTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(weekStartDate));
            weekStartDate = DateUtils.addDays(weekStartDate, 1);
        }

        for (final Terms.Bucket cityDetailsentry : cityaggr.getBuckets()) {
            dayWiseCollFromResult = new LinkedHashMap<>();
            finalDayWiseColl = new LinkedHashMap<>();
            ulbName = cityDetailsentry.getKeyAsString();
            noOfDays = 0;
            Histogram dateaggs = cityDetailsentry.getAggregations().get(DATE_AGG);
            for (Histogram.Bucket entry : dateaggs.getBuckets()) {
                resultDateStr = entry.getKeyAsString().split("T")[0];
                aggregateSum = entry.getAggregations().get("current_total");
                if (BigDecimal.valueOf(aggregateSum.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)
                        .compareTo(BigDecimal.ZERO) > 0)
                    dayWiseCollFromResult.put(resultDateStr,
                            BigDecimal.valueOf(aggregateSum.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            }
            // Check collections for all days in a week, if collection is not present, 0 will be shown for the corresponding day
            for (String dates : daysInWeek) {
                if (noOfDays == 0)
                    noOfDays = 1;
                day = "Day " + noOfDays;
                if (dayWiseCollFromResult.get(dates) == null)
                    finalDayWiseColl.put(day, BigDecimal.ZERO);
                else
                    finalDayWiseColl.put(day, dayWiseCollFromResult.get(dates));

                noOfDays++;
            }
            intervalwiseCollMap.put(ulbName, finalDayWiseColl);
        }
    }
    
    /**
     * Provides week wise DCB details across all ULBs
     * @param collectionDetailsRequest
     * @param intervalType
     * @return list
     */
    public List<WeeklyDCB> getWeekwiseDCBDetailsAcrossCities(final CollectionDetailsRequest collectionDetailsRequest,
            final String intervalType) {
        final List<WeeklyDCB> ulbWiseDetails = new ArrayList<>();
        Date fromDate = null;
        Date toDate = null;
        String weekName;
        Sum aggregateSum;
        String aggregationField = StringUtils.EMPTY;
        Map<String, Object[]> weekwiseColl;
        final Map<String, Map<String, Object[]>> weeklyCollMap = new LinkedHashMap<>();
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(
                    DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        }
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType())) 
            aggregationField = getAggregrationField(collectionDetailsRequest);
        
        final Map<String, BigDecimal> totalDemandMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate,
                toDate, PROPERTY_TAX_INDEX_NAME, TOTAL_DEMAND,aggregationField);
        final Aggregations collAggr = getMonthwiseCollectionsForConsecutiveYears(collectionDetailsRequest, fromDate,
                toDate, true, intervalType, aggregationField);
        final StringTerms cityaggr = collAggr.get(BY_CITY);
        BigDecimal totalDemand;
        BigDecimal weeklyDemand;
        int noOfWeeks;
        Object[] demandCollValues;
        for (final Terms.Bucket cityDetailsentry : cityaggr.getBuckets()) {
            weekwiseColl = new LinkedHashMap<>();
            final String ulbName = cityDetailsentry.getKeyAsString();
            noOfWeeks = 0;
            totalDemand = totalDemandMap.get(ulbName) == null ? BigDecimal.ZERO : totalDemandMap.get(ulbName);
            
            final Histogram dateaggs = cityDetailsentry.getAggregations().get(DATE_AGG);
            for (final Histogram.Bucket entry : dateaggs.getBuckets()) {
                if (noOfWeeks == 0)
                    noOfWeeks = 1;
                demandCollValues = new Object[53];
                if(totalDemand.compareTo(BigDecimal.ZERO) == 0)
                    weeklyDemand = BigDecimal.ZERO;
                else
                    weeklyDemand = totalDemand.divide(BigDecimal.valueOf(52), BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(noOfWeeks));
                weekName = "Week " + noOfWeeks;
                aggregateSum = entry.getAggregations().get("current_total");
                if (BigDecimal.valueOf(aggregateSum.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)
                        .compareTo(BigDecimal.ZERO) > 0) {
                    demandCollValues[0] = BigDecimal.valueOf(aggregateSum.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
                    demandCollValues[1] = weeklyDemand;
                    weekwiseColl.put(weekName, demandCollValues);
                }
                noOfWeeks++;
            }
            weeklyCollMap.put(ulbName, weekwiseColl);
        }
        setWeeklyDCBValues(ulbWiseDetails, weeklyCollMap);
        return ulbWiseDetails;

    }

    /**
     * Sets the Demand and Collection values
     * @param ulbWiseDetails
     * @param weeklyCollMap
     */
    private void setWeeklyDCBValues(final List<WeeklyDCB> ulbWiseDetails,
            final Map<String, Map<String, Object[]>> weeklyCollMap) {
        WeeklyDCB weeklyDCB;
        DemandCollectionMIS demandCollectionMIS;
        int count;
        for (final Map.Entry<String, Map<String, Object[]>> entry : weeklyCollMap.entrySet()) {
            weeklyDCB = new WeeklyDCB();
            count=1;
            weeklyDCB.setBoundaryName(entry.getKey());
            for (final Map.Entry<String, Object[]> weeklyMap : entry.getValue().entrySet()) {
                demandCollectionMIS = new DemandCollectionMIS();
                demandCollectionMIS.setCollection(new BigDecimal(weeklyMap.getValue()[0].toString()));
                demandCollectionMIS.setDemand(new BigDecimal(weeklyMap.getValue()[1].toString()));
                if (demandCollectionMIS.getDemand().compareTo(BigDecimal.ZERO) > 0)
                    demandCollectionMIS.setPercent(demandCollectionMIS.getCollection().divide(demandCollectionMIS.getDemand(), 2, RoundingMode.CEILING).multiply(BIGDECIMAL_100));
                if(count == 1)
                    weeklyDCB.setWeek1DCB(demandCollectionMIS);
                else if(count == 2)
                    weeklyDCB.setWeek2DCB(demandCollectionMIS);
                else if(count == 3)
                    weeklyDCB.setWeek3DCB(demandCollectionMIS);
                else if(count == 4)
                    weeklyDCB.setWeek4DCB(demandCollectionMIS);
                else if(count == 5)
                    weeklyDCB.setWeek5DCB(demandCollectionMIS);
                count++;
                
            }
            ulbWiseDetails.add(weeklyDCB);
        }
    }

    /**
     * Provides month wise DCB details across all ULBs
     * @param collectionDetailsRequest
     * @param intervalType
     * @return list
     */
    public List<MonthlyDCB> getMonthwiseDCBDetailsAcrossCities(final CollectionDetailsRequest collectionDetailsRequest,
            final String intervalType) {
        final List<MonthlyDCB> ulbWiseDetails = new ArrayList<>();
        Date fromDate = null;
        Date toDate = null;
        String monthName;
        Sum aggregateSum;
        Integer month;
        String aggregationField = StringUtils.EMPTY;
        Map<String, Object[]> monthwiseColl;
        Map<Integer, String> monthValuesMap = DateUtils.getAllMonthsWithFullNames();
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType()))
            aggregationField = getAggregrationField(collectionDetailsRequest);
        
        final Map<String, Map<String, Object[]>> ulbwiseMonthlyCollMap = new HashMap<>();
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.addDays(
                    DateUtils.getDate(collectionDetailsRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                    1);
        }
        
        final Map<String, BigDecimal> totalDemandMap = getCollectionAndDemandValues(collectionDetailsRequest, fromDate,
                toDate, PROPERTY_TAX_INDEX_NAME, TOTAL_DEMAND,aggregationField);
        final Aggregations collAggr = getMonthwiseCollectionsForConsecutiveYears(collectionDetailsRequest, fromDate,
                toDate, true, intervalType, aggregationField);
        final StringTerms cityaggr = collAggr.get(BY_CITY);
        BigDecimal totalDemand;
        BigDecimal monthlyDemand;
        int noOfMonths;
        Object[] demandCollValues;
        
        for (final Terms.Bucket cityDetailsentry : cityaggr.getBuckets()) {
            monthwiseColl = new LinkedHashMap<>();
            final String ulbName = cityDetailsentry.getKeyAsString();
            noOfMonths = 0;
            totalDemand = totalDemandMap.get(ulbName);

            if (totalDemand == null)
                totalDemand = BigDecimal.ZERO;
            final Histogram dateaggs = cityDetailsentry.getAggregations().get(DATE_AGG);
            for (final Histogram.Bucket entry : dateaggs.getBuckets()) {
                if (noOfMonths == 0)
                    noOfMonths = 1;
                demandCollValues = new Object[12];
                String[] dateArr =entry.getKeyAsString().split("T");
                month = Integer.valueOf(dateArr[0].split("-", 3)[1]);
                monthName = monthValuesMap.get(month);
                monthlyDemand = totalDemand.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(noOfMonths));
                aggregateSum = entry.getAggregations().get("current_total");
               
                if (BigDecimal.valueOf(aggregateSum.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)
                        .compareTo(BigDecimal.ZERO) > 0) {
                    demandCollValues[0] = BigDecimal.valueOf(aggregateSum.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
                    demandCollValues[1] = monthlyDemand;
                    monthwiseColl.put(monthName, demandCollValues);
                }
                noOfMonths++;
            }
            ulbwiseMonthlyCollMap.put(ulbName, monthwiseColl);
        }
        setMonthlyDCBValues(ulbWiseDetails, ulbwiseMonthlyCollMap);
        return ulbWiseDetails;
    }
    
    /**
     * Sets the Demand and Collection values
     * @param ulbWiseDetails
     * @param monthlyCollMap
     */
    private void setMonthlyDCBValues(final List<MonthlyDCB> ulbWiseDetails,
            final Map<String, Map<String, Object[]>> yearwiseMonthlyCollMap) {
        MonthlyDCB monthlyDCB;
        DemandCollectionMIS demandCollectionMIS;
        String month;
        for (final Map.Entry<String, Map<String, Object[]>> entry : yearwiseMonthlyCollMap.entrySet()) {
            monthlyDCB = new MonthlyDCB();
            monthlyDCB.setBoundaryName(entry.getKey());
            for (final Map.Entry<String, Object[]> monthMap : entry.getValue().entrySet()) {
                demandCollectionMIS = new DemandCollectionMIS();
                month = monthMap.getKey();
                demandCollectionMIS.setCollection(new BigDecimal(monthMap.getValue()[0].toString()));
                demandCollectionMIS.setDemand(new BigDecimal(monthMap.getValue()[1].toString()));
                if (demandCollectionMIS.getDemand().compareTo(BigDecimal.ZERO) > 0)
                    demandCollectionMIS.setPercent(demandCollectionMIS.getCollection()
                            .divide(demandCollectionMIS.getDemand(), 2, RoundingMode.CEILING).multiply(BIGDECIMAL_100));
                setDCBForMonth(monthlyDCB, demandCollectionMIS, month);
            }
            ulbWiseDetails.add(monthlyDCB);
        }
    }
    
    /**
     * Sets Collections for each month
     * @param collectionMap
     * @param collectionAnalysis
     */
    private void setCollectionsForMonths(Map<String, Map<String, BigDecimal>> collectionMap,
            CollectionAnalysis collectionAnalysis) {
        DemandCollectionMIS demandCollectionMIS;
        List<MonthlyDCB> monthlyCollDetails = new ArrayList<>();
        MonthlyDCB monthlyDCB;
        String month;
        for (final Map.Entry<String, Map<String, BigDecimal>> entry : collectionMap.entrySet()) {
            monthlyDCB = new MonthlyDCB();
            monthlyDCB.setBoundaryName(entry.getKey());
            for (final Map.Entry<String, BigDecimal> monthMap : entry.getValue().entrySet()) {
                demandCollectionMIS = new DemandCollectionMIS();
                month = monthMap.getKey();
                demandCollectionMIS.setCollection(monthMap.getValue());
                
                setDCBForMonth(monthlyDCB, demandCollectionMIS, month);
            }
            monthlyCollDetails.add(monthlyDCB);
        }
        collectionAnalysis.setMonthlyDCBDetails(monthlyCollDetails);
    }

    /**
     * Sets collections for each week
     * @param collectionMap
     * @param collectionAnalysis
     */
    private void setCollectionsForWeeks(Map<String, Map<String, BigDecimal>> collectionMap,
            CollectionAnalysis collectionAnalysis) {
        int intervalCount;
        DemandCollectionMIS demandCollectionMIS;
        WeeklyDCB weeklyDCB;
        List<WeeklyDCB> weeklyCollDetails = new ArrayList<>();
        for (Map.Entry<String, Map<String, BigDecimal>> citywise : collectionMap.entrySet()) {
            weeklyDCB = new WeeklyDCB();
            intervalCount = 1;
            weeklyDCB.setBoundaryName(citywise.getKey());
            for (final Map.Entry<String, BigDecimal> weeklyMap : citywise.getValue().entrySet()) {
                demandCollectionMIS = new DemandCollectionMIS();
                demandCollectionMIS.setCollection(weeklyMap.getValue());
                demandCollectionMIS.setIntervalCount(intervalCount);

                if (intervalCount == 1)
                    weeklyDCB.setWeek1DCB(demandCollectionMIS);
                else if (intervalCount == 2)
                    weeklyDCB.setWeek2DCB(demandCollectionMIS);
                else if (intervalCount == 3)
                    weeklyDCB.setWeek3DCB(demandCollectionMIS);
                else if (intervalCount == 4)
                    weeklyDCB.setWeek4DCB(demandCollectionMIS);
                else if (intervalCount == 5)
                    weeklyDCB.setWeek5DCB(demandCollectionMIS);
                intervalCount++;
            }
            weeklyCollDetails.add(weeklyDCB);
        }
        collectionAnalysis.setWeeklyDCBDetails(weeklyCollDetails);
    }

    /**
     * Sets collections for each day in a week
     * @param collectionMap
     * @param collectionAnalysis
     */
    private void setCollectionsForDays(Map<String, Map<String, BigDecimal>> collectionMap,
            CollectionAnalysis collectionAnalysis) {
        int intervalCount;
        DemandCollectionMIS demandCollectionMIS;
        DayWiseCollection dayWiseCollection;
        List<DayWiseCollection> dailyCollDetails = new ArrayList<>();
        for (Map.Entry<String, Map<String, BigDecimal>> citywise : collectionMap.entrySet()) {
            dayWiseCollection = new DayWiseCollection();
            intervalCount = 1;
            dayWiseCollection.setBoundaryName(citywise.getKey());
            for (final Map.Entry<String, BigDecimal> weeklyMap : citywise.getValue().entrySet()) {
                demandCollectionMIS = new DemandCollectionMIS();
                demandCollectionMIS.setCollection(weeklyMap.getValue());
                demandCollectionMIS.setIntervalCount(intervalCount);

                if (intervalCount == 1)
                    dayWiseCollection.setDay1DCB(demandCollectionMIS);
                else if (intervalCount == 2)
                    dayWiseCollection.setDay2DCB(demandCollectionMIS);
                else if (intervalCount == 3)
                    dayWiseCollection.setDay3DCB(demandCollectionMIS);
                else if (intervalCount == 4)
                    dayWiseCollection.setDay4DCB(demandCollectionMIS);
                else if (intervalCount == 5)
                    dayWiseCollection.setDay5DCB(demandCollectionMIS);
                else if (intervalCount == 6)
                    dayWiseCollection.setDay6DCB(demandCollectionMIS);
                else if (intervalCount == 7)
                    dayWiseCollection.setDay7DCB(demandCollectionMIS);
                intervalCount++;
            }
            dailyCollDetails.add(dayWiseCollection);
        }
        collectionAnalysis.setDailyCollDetails(dailyCollDetails);
    }
    
    /**
     * Sets DCB bean for each month
     * @param monthlyDCB
     * @param demandCollectionMIS
     * @param month
     */
    private void setDCBForMonth(MonthlyDCB monthlyDCB, DemandCollectionMIS demandCollectionMIS, String month) {
        switch (month) {
            case APRIL:
                monthlyDCB.setAprilDCB(demandCollectionMIS);
                break;
            case MAY:
                monthlyDCB.setMayDCB(demandCollectionMIS);
                break;
            case JUNE:
                monthlyDCB.setJuneDCB(demandCollectionMIS);
                break;
            case JULY:
                monthlyDCB.setJulyDCB(demandCollectionMIS);
                break;
            case AUGUST:
                monthlyDCB.setAugustDCB(demandCollectionMIS);
                break;
            case SEPTEMBER:
                monthlyDCB.setSeptemberDCB(demandCollectionMIS);
                break;
            case OCTOBER:
                monthlyDCB.setOctoberDCB(demandCollectionMIS);
                break;
            case NOVEMBER:
                monthlyDCB.setNovemberDCB(demandCollectionMIS);
                break;
            case DECEMBER:
                monthlyDCB.setDecemberDCB(demandCollectionMIS);
                break;
            case JANUARY:
                monthlyDCB.setJanuaryDCB(demandCollectionMIS);
                break;
            case FEBRUARY:
                monthlyDCB.setFebruaryDCB(demandCollectionMIS);
                break;
            case MARCH:
                monthlyDCB.setMarchDCB(demandCollectionMIS);
                break;
            default:
                break;
        }
    }
 
    /**
     * Provides ward wise details for all cities
     * @param collectionDetailsRequest
     * @param cities
     * @return List
     */
    public List<CollTableData> getWardWiseTableDataAcrossCities(CollectionDetailsRequest collectionDetailsRequest,
            Iterable<CityIndex> cities) {
        List<CollTableData> citywiseTableData = new ArrayList<>();
        List<CollTableData> wardWiseData;
        String cityName;
        String regionName;
        String districtName;
        String ulbGrade;
        for (CityIndex city : cities) {
            cityName = city.getName();
            collectionDetailsRequest.setUlbCode(city.getCitycode());
            collectionDetailsRequest.setType(DASHBOARD_GROUPING_WARDWISE);
            regionName = city.getRegionname();
            districtName = city.getDistrictname();
            ulbGrade = city.getCitygrade();
            wardWiseData = getResponseTableData(collectionDetailsRequest, false);
            for (CollTableData wardData : wardWiseData){
                wardData.setUlbName(cityName);
                wardData.setRegionName(regionName);
                wardData.setDistrictName(districtName);
                wardData.setUlbGrade(ulbGrade);
            }

            citywiseTableData.addAll(wardWiseData);
        }
        return citywiseTableData;
    }
    
    public List<String> getCityDetails(CollectionDetailsRequest collectionDetailsRequest, String aggregationField) {
        List<String> nameList = new ArrayList<>();
        String fieldName = StringUtils.EMPTY;
        int size = 0;
        if (REGION_NAME.equalsIgnoreCase(aggregationField)) {
            fieldName = "regionname";
            size = 4;
        } else if (DISTRICT_NAME.equalsIgnoreCase(aggregationField)) {
            fieldName = "districtname";
            size = 13;
        } else if (CITY_NAME.equalsIgnoreCase(aggregationField)) {
            fieldName = "name";
            size = 112;
        } else if (CITY_GRADE.equalsIgnoreCase(aggregationField)) {
            fieldName = "citygrade";
            size = 7;
        }

        if (!REVENUE_WARD.equalsIgnoreCase(aggregationField)) {
            BoolQueryBuilder boolQuery=prepareQueryForAgg(collectionDetailsRequest);
            AggregationBuilder agg = AggregationBuilders.terms("uniqueAggr").field(fieldName).size(size);
            SearchResponse cityResponse = elasticsearchTemplate.getClient().prepareSearch("city")
                    .setQuery(boolQuery)
                    .setSize(size)
                    .addAggregation(agg)
                    .execute().actionGet();
            Terms aggTerms = cityResponse.getAggregations().get("uniqueAggr");
            for (Terms.Bucket bucket : aggTerms.getBuckets())
                nameList.add(bucket.getKeyAsString());
        } else {
            List<BillCollectorIndex> billCollectors = getBillCollectorDetails(collectionDetailsRequest);
            for (BillCollectorIndex billCollector : billCollectors)
                nameList.add(billCollector.getRevenueWard());
        }
        return nameList;
    }

    private BoolQueryBuilder prepareQueryForAgg(CollectionDetailsRequest collectionDetailsRequest) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        if (StringUtils.isNotBlank(collectionDetailsRequest.getDistrictName())) {
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("districtname", collectionDetailsRequest.getDistrictName()));
        }
        if (StringUtils.isNotBlank(collectionDetailsRequest.getRegionName())) {
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("regionname", collectionDetailsRequest.getRegionName()));
        }
        if (StringUtils.isNotBlank(collectionDetailsRequest.getUlbGrade()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("citygrade", collectionDetailsRequest.getUlbGrade()));
        return boolQuery;
    }
}