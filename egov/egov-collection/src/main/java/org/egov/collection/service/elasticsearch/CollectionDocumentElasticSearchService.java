/* eGov suite of products aim to improve the internal efficiency,transparency,
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

package org.egov.collection.service.elasticsearch;

import static org.egov.collection.constants.CollectionConstants.COLLECTION_INDEX_NAME;
import static org.egov.collection.constants.CollectionConstants.DASHBOARD_GROUPING_DISTRICTWISE;
import static org.egov.collection.constants.CollectionConstants.DASHBOARD_GROUPING_GRADEWISE;
import static org.egov.collection.constants.CollectionConstants.DASHBOARD_GROUPING_REGIONWISE;
import static org.egov.collection.constants.CollectionConstants.DASHBOARD_GROUPING_ULBWISE;
import static org.egov.collection.constants.CollectionConstants.DASHBOARD_GROUPING_WARDWISE;
import static org.egov.collection.constants.CollectionConstants.DATEFORMATTER_YYYY_MM_DD;
import static org.egov.collection.constants.CollectionConstants.DATE_FORMAT_YYYYMMDD;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.bean.dashboard.CollectionDashBoardRequest;
import org.egov.collection.bean.dashboard.CollectionDashBoardTrend;
import org.egov.collection.bean.dashboard.CollectionDocumentDetails;
import org.egov.collection.bean.dashboard.CollectionTableData;
import org.egov.collection.bean.dashboard.TaxPayerDashBoardDetails;
import org.egov.collection.bean.dashboard.TaxPayerDashBoardResponseDetails;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.es.CollectionDocument;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.infra.utils.DateUtils;
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
public class CollectionDocumentElasticSearchService {

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
    private static final String TOTAL_AMOUNT = "totalAmount";
    private static final String COLLECTIONTOTAL = "collectiontotal";
    private static final String CANCELLED = "Cancelled";
    private static final String STATUS = "status";
    private static final String RECEIPT_DATE = "receiptDate";
    private static final String TOTAL_COLLECTION = "total_collection";
    private static final String BY_AGGREGATION_FIELD = "by_aggregationField";

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionDocumentElasticSearchService.class);

    @Autowired
    private CFinancialYearService cFinancialYearService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public Set<String> getServices() {

        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .addAggregation(AggregationBuilders.count("services_count").field(BILLING_SERVICE)).build();
        final Aggregations collCountAggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());

        final ValueCount aggr = collCountAggr.get("services_count");
        searchQueryColl = new NativeSearchQueryBuilder()
        .withIndices(COLLECTION_INDEX_NAME)
        .withFields(BILLING_SERVICE)
        .withPageable(
                new PageRequest(0, Long.valueOf(aggr.getValue()).intValue() == 0 ? 1 : Long.valueOf(
                        aggr.getValue()).intValue())).build();
        final List<CollectionDocument> list = elasticsearchTemplate.queryForList(searchQueryColl,
                CollectionDocument.class);
        final Set<String> services = new TreeSet<>();
        for (final CollectionDocument colDoc : list)
            services.add(colDoc.getBillingService());
        return services;
    }

    /**
     * Gives the consolidated collection for the dates and billing service
     *
     * @param fromDate
     * @param toDate
     * @param serviceDetails
     * @return BigDecimal
     */
    public BigDecimal getConsolidatedCollForYears(final Date fromDate, final Date toDate,
            final List<String> serviceDetails) {
        BoolQueryBuilder boolQuery = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.rangeQuery(RECEIPT_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false))
                        .mustNot(QueryBuilders.matchQuery(STATUS, CANCELLED));
        if (!serviceDetails.isEmpty())
            boolQuery = boolQuery.must(QueryBuilders.termsQuery(BILLING_SERVICE, serviceDetails));
        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(boolQuery).addAggregation(AggregationBuilders.sum(COLLECTIONTOTAL).field(TOTAL_AMOUNT))
                .build();

        final Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());

        final Sum aggr = collAggr.get(COLLECTIONTOTAL);
        return BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Gives the consolidated collection for the current Fin year and last fin
     * year
     *
     * @param serviceDetails
     * @return Map
     */
    public Map<String, BigDecimal> getFinYearsCollByService(final List<String> serviceDetails) {
        /**
         * As per Elastic Search functionality, to get the total collections
         * between 2 dates, add a day to the endDate and fetch the results
         */
        final Map<String, BigDecimal> consolidatedCollValues = new HashMap<>();
        final CFinancialYear currFinYear = cFinancialYearService.getFinancialYearByDate(new Date());
        // For current year results
        consolidatedCollValues.put(
                "cytdColln",
                getConsolidatedCollForYears(currFinYear.getStartingDate(),
                        org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1), serviceDetails));
        // For last year results
        consolidatedCollValues.put(
                "lytdColln",
                getConsolidatedCollForYears(
                        org.apache.commons.lang3.time.DateUtils.addYears(currFinYear.getStartingDate(), -1),
                        org.apache.commons.lang3.time.DateUtils.addDays(
                                org.apache.commons.lang3.time.DateUtils.addYears(new Date(), -1), 1), serviceDetails));
        return consolidatedCollValues;
    }

    /**
     * Builds query based on the input parameters sent
     *
     * @param collectionDashBoardRequest
     * @param indexName
     * @param ulbCodeField
     * @return BoolQueryBuilder
     */
    private BoolQueryBuilder prepareWhereClause(final CollectionDashBoardRequest collectionDashBoardRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().filter(
                QueryBuilders.rangeQuery(TOTAL_AMOUNT).from(0).to(null));
        if (StringUtils.isNotBlank(collectionDashBoardRequest.getRegionName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(REGION_NAME,
                    collectionDashBoardRequest.getRegionName()));
        if (StringUtils.isNotBlank(collectionDashBoardRequest.getDistrictName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(DISTRICT_NAME,
                    collectionDashBoardRequest.getDistrictName()));
        if (StringUtils.isNotBlank(collectionDashBoardRequest.getUlbGrade()))
            boolQuery = boolQuery
            .filter(QueryBuilders.matchQuery(CITY_GRADE, collectionDashBoardRequest.getUlbGrade()));
        if (StringUtils.isNotBlank(collectionDashBoardRequest.getUlbCode()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(CITY_CODE, collectionDashBoardRequest.getUlbCode()));

        return boolQuery;
    }

    /**
     * API sets the consolidated collections for single day and between the 2
     * dates
     *
     * @param collectionDashBoardRequest
     * @param collectionIndexDetails
     */
    public CollectionDocumentDetails getCompleteCollectionIndexDetails(
            final CollectionDashBoardRequest collectionDashBoardRequest, final List<String> serviceDetail) {
        Date fromDate;
        Date toDate;
        BigDecimal todayColl;
        BigDecimal tillDateColl;
        BigDecimal variance;
        final Long startTime = System.currentTimeMillis();
        final CollectionDocumentDetails collectionDocumentDetails = new CollectionDocumentDetails();
        /**
         * As per Elastic Search functionality, to get the total collections
         * between 2 dates, add a day to the endDate and fetch the results For
         * Current day's collection if dates are sent in the request, consider
         * the toDate, else take date range between current date +1 day
         */
        if (StringUtils.isNotBlank(collectionDashBoardRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDashBoardRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDashBoardRequest.getToDate(), DATE_FORMAT_YYYYMMDD);
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(
                    DateUtils.getDate(collectionDashBoardRequest.getToDate(), DATE_FORMAT_YYYYMMDD), 1);
        } else {
            fromDate = new Date();
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(fromDate, 1);
        }
        // Today’s collection
        todayColl = getCollectionBetweenDates(collectionDashBoardRequest, fromDate, toDate, null, serviceDetail, false);
        collectionDocumentDetails.setTodayColl(todayColl);

        // Last year Today’s day collection
        todayColl = getCollectionBetweenDates(collectionDashBoardRequest,
                org.apache.commons.lang3.time.DateUtils.addYears(fromDate, -1),
                org.apache.commons.lang3.time.DateUtils.addYears(toDate, -1), null, serviceDetail, false);
        collectionDocumentDetails.setLyTodayColl(todayColl);

        /**
         * For collections between the date ranges if dates are sent in the
         * request, consider the same, else calculate from current year start
         * date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDashBoardRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDashBoardRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDashBoardRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(
                    DateUtils.getDate(collectionDashBoardRequest.getToDate(), DATE_FORMAT_YYYYMMDD), 1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1);
        }
        // Current Year till today collection
        tillDateColl = getCollectionBetweenDates(collectionDashBoardRequest, fromDate, toDate, null, serviceDetail,
                false);
        collectionDocumentDetails.setCytdColl(tillDateColl);

        // Last year till same date of today’s date collection
        tillDateColl = getCollectionBetweenDates(collectionDashBoardRequest,
                org.apache.commons.lang3.time.DateUtils.addYears(fromDate, -1),
                org.apache.commons.lang3.time.DateUtils.addYears(toDate, -1), null, serviceDetail, false);
        collectionDocumentDetails.setLytdColl(tillDateColl);
        if (collectionDocumentDetails.getLytdColl().compareTo(BigDecimal.ZERO) == 0)
            variance = CollectionConstants.BIGDECIMAL_100;
        else
            variance = collectionDocumentDetails.getCytdColl().subtract(collectionDocumentDetails.getLytdColl())
            .multiply(CollectionConstants.BIGDECIMAL_100)
            .divide(collectionDocumentDetails.getLytdColl(), 1, BigDecimal.ROUND_HALF_UP);
        collectionDocumentDetails.setLyVar(variance);
        final Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken by getCompleteCollectionIndexDetails() is : " + timeTaken + MILLISECS);
        return collectionDocumentDetails;
    }

    /**
     * Returns the consolidated collections for single day and between the 2
     * dates
     *
     * @param collectionDashBoardRequest
     * @param fromDate
     * @param toDate
     * @param cityName
     * @return BigDecimal
     */
    public BigDecimal getCollectionBetweenDates(final CollectionDashBoardRequest collectionDashBoardRequest,
            final Date fromDate, final Date toDate, final String cityName, final List<String> serviceDetails,
            final boolean isWard) {
        final Long startTime = System.currentTimeMillis();
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDashBoardRequest);
        boolQuery = boolQuery.filter(
                QueryBuilders.rangeQuery(RECEIPT_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false)).mustNot(
                        QueryBuilders.matchQuery(STATUS, CANCELLED));
        if (StringUtils.isNotBlank(cityName))
            if (!isWard)
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery(CITY_NAME, cityName));
            else
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery(REVENUE_WARD, cityName));
        if (!serviceDetails.isEmpty())
            boolQuery = boolQuery.filter(QueryBuilders.termsQuery(BILLING_SERVICE, serviceDetails));

        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(boolQuery).addAggregation(AggregationBuilders.sum(COLLECTIONTOTAL).field(TOTAL_AMOUNT))
                .build();

        final Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());

        final Sum aggr = collAggr.get(COLLECTIONTOTAL);
        final Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken by getCollectionBetweenDates() is : " + timeTaken + MILLISECS);
        return BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Prepares Collection Table Data
     *
     * @param collectionDashBoardRequest
     * @return List
     */
    public List<CollectionTableData> getResponseTableData(final CollectionDashBoardRequest collectionDashBoardRequest,
            final List<String> serviceDetail) {
        final List<CollectionTableData> collIndDataList = new ArrayList<>();
        Date fromDate;
        Date toDate;
        String name;
        CollectionTableData collTableData;
        String aggregationField = REGION_NAME;

        /**
         * Select the grouping based on the type parameter, by default the
         * grouping is done based on Regions. If type is region, group by
         * Region, if type is district, group by District, if type is ulb, group
         * by ULB
         */
        if (StringUtils.isNotBlank(collectionDashBoardRequest.getType()))
            if (collectionDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_REGIONWISE))
                aggregationField = REGION_NAME;
            else if (collectionDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_DISTRICTWISE))
                aggregationField = DISTRICT_NAME;
            else if (collectionDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ULBWISE))
                aggregationField = CITY_NAME;
            else if (collectionDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_GRADEWISE))
                aggregationField = CITY_GRADE;
            else if (collectionDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_WARDWISE))
                aggregationField = REVENUE_WARD;

        /**
         * As per Elastic Search functionality, to get the total collections
         * between 2 dates, add a day to the endDate and fetch the results For
         * Current day's collection if dates are sent in the request, consider
         * the toDate, else take date range between current date +1 day
         */
        if (StringUtils.isNotBlank(collectionDashBoardRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDashBoardRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDashBoardRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(
                    DateUtils.getDate(collectionDashBoardRequest.getToDate(), DATE_FORMAT_YYYYMMDD), 1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1);
        }

        Long startTime = System.currentTimeMillis();

        getCollectionAndDemandValues(collectionDashBoardRequest, fromDate, toDate, TOTAL_AMOUNT, aggregationField,
                serviceDetail);
        // total
        final Map<String, BigDecimal> totalCollMap = getCollectionAndDemandValues(collectionDashBoardRequest, fromDate,
                toDate, TOTAL_AMOUNT, aggregationField, serviceDetail);
        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken by getCollectionAndDemandValues() is : " + timeTaken + MILLISECS);

        startTime = System.currentTimeMillis();
        for (final Map.Entry<String, BigDecimal> entry : totalCollMap.entrySet()) {
            collTableData = new CollectionTableData();
            name = entry.getKey();
            if (aggregationField.equals(REGION_NAME))
                collTableData.setRegionName(name);
            else if (aggregationField.equals(DISTRICT_NAME)) {
                collTableData.setRegionName(collectionDashBoardRequest.getRegionName());
                collTableData.setDistrictName(name);
            } else if (aggregationField.equals(CITY_NAME)) {
                collTableData.setUlbName(name);
                collTableData.setDistrictName(collectionDashBoardRequest.getDistrictName());
                collTableData.setUlbGrade(collectionDashBoardRequest.getUlbGrade());
            } else if (aggregationField.equals(CITY_GRADE))
                collTableData.setUlbGrade(name);
            else if (aggregationField.equals(REVENUE_WARD))
                collTableData.setWardName(name);
            collTableData.setTotalCollection(totalCollMap.get(name) == null ? BigDecimal.ZERO : totalCollMap.get(name));
            collIndDataList.add(collTableData);
        }
        timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken for setting values in getResponseTableData() is : " + timeTaken + MILLISECS);
        return collIndDataList;
    }

    /**
     * Provides collection and demand results
     *
     * @param collectionDashBoardRequest
     * @param fromDate
     * @param toDate
     * @param indexName
     * @param fieldName
     * @param ulbCodeField
     * @param aggregationField
     * @return Map
     */
    public Map<String, BigDecimal> getCollectionAndDemandValues(
            final CollectionDashBoardRequest collectionDashBoardRequest, final Date fromDate, final Date toDate,
            final String fieldName, final String aggregationField, final List<String> serviceDetails) {
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDashBoardRequest);
        if (!serviceDetails.isEmpty())
            boolQuery = boolQuery.filter(QueryBuilders.termsQuery(BILLING_SERVICE, serviceDetails));

        boolQuery = boolQuery.filter(
                QueryBuilders.rangeQuery(RECEIPT_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false)).mustNot(
                        QueryBuilders.matchQuery(STATUS, CANCELLED));

        final AggregationBuilder aggregation = AggregationBuilders.terms(BY_CITY).field(aggregationField).size(120)
                .subAggregation(AggregationBuilders.sum("total").field(fieldName));

        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(boolQuery).addAggregation(aggregation).build();

        final Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());

        final StringTerms cityAggr = collAggr.get(BY_CITY);
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
     * @param collectionDashBoardRequest
     * @return List
     */
    public List<CollectionDashBoardTrend> getMonthwiseCollectionDetails(
            final CollectionDashBoardRequest collectionDashBoardRequest, final List<String> serviceDetail) {
        final List<CollectionDashBoardTrend> collTrendsList = new ArrayList<>();
        CollectionDashBoardTrend collTrend;
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
        if (StringUtils.isNotBlank(collectionDashBoardRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDashBoardRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDashBoardRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(
                    DateUtils.getDate(collectionDashBoardRequest.getToDate(), DATE_FORMAT_YYYYMMDD), 1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1);
        }
        Long startTime = System.currentTimeMillis();
        for (int count = 0; count <= 2; count++) {
            monthwiseColl = new LinkedHashMap<>();
            final Aggregations collAggr = getMonthwiseCollectionsForConsecutiveYears(collectionDashBoardRequest,
                    fromDate, toDate, serviceDetail);
            final Histogram dateaggs = collAggr.get(DATE_AGG);

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
            if (StringUtils.isNotBlank(collectionDashBoardRequest.getFromDate())
                    && StringUtils.isNotBlank(collectionDashBoardRequest.getToDate())) {
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
            LOGGER.debug("Time taken by getMonthwiseCollectionsForConsecutiveYears() for 3 consecutive years is : "
                    + timeTaken + MILLISECS);

        startTime = System.currentTimeMillis();
        /**
         * If dates are passed in request, get result for the date range, else
         * get results for all 12 months
         */
        if (StringUtils.isBlank(collectionDashBoardRequest.getFromDate())
                && StringUtils.isBlank(collectionDashBoardRequest.getToDate()))
            for (final Map.Entry<Integer, String> entry : DateUtils.getAllFinancialYearMonthsWithFullNames().entrySet()) {
                collTrend = new CollectionDashBoardTrend();
                collTrend.setMonth(entry.getValue());
                collTrend.setCyColl(yearwiseMonthlyCollList.get(0).get(collTrend.getMonth()) == null ? BigDecimal.ZERO
                        : yearwiseMonthlyCollList.get(0).get(collTrend.getMonth()));
                collTrend.setLyColl(yearwiseMonthlyCollList.get(1).get(collTrend.getMonth()) == null ? BigDecimal.ZERO
                        : yearwiseMonthlyCollList.get(1).get(collTrend.getMonth()));
                collTrend.setPyColl(yearwiseMonthlyCollList.get(2).get(collTrend.getMonth()) == null ? BigDecimal.ZERO
                        : yearwiseMonthlyCollList.get(2).get(collTrend.getMonth()));
                collTrendsList.add(collTrend);
            }
        else
            for (final Map.Entry<String, BigDecimal> entry : yearwiseMonthlyCollList.get(0).entrySet()) {
                collTrend = new CollectionDashBoardTrend();
                collTrend.setMonth(entry.getKey());
                collTrend.setCyColl(entry.getValue());
                collTrend.setLyColl(yearwiseMonthlyCollList.get(1).get(collTrend.getMonth()) == null ? BigDecimal.ZERO
                        : yearwiseMonthlyCollList.get(1).get(collTrend.getMonth()));
                collTrend.setPyColl(yearwiseMonthlyCollList.get(2).get(collTrend.getMonth()) == null ? BigDecimal.ZERO
                        : yearwiseMonthlyCollList.get(2).get(collTrend.getMonth()));
                collTrendsList.add(collTrend);
            }
        timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken setting values in getMonthwiseCollectionDetails() is : " + timeTaken + MILLISECS);
        return collTrendsList;
    }

    /**
     * Provides month-wise collections for consecutive years
     *
     * @param collectionDashBoardRequest
     * @param fromDate
     * @param toDate
     * @return SearchResponse
     */
    private Aggregations getMonthwiseCollectionsForConsecutiveYears(
            final CollectionDashBoardRequest collectionDashBoardRequest, final Date fromDate, final Date toDate,
            final List<String> serviceDetail) {
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDashBoardRequest);
        boolQuery = boolQuery.filter(
                QueryBuilders.rangeQuery(RECEIPT_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false)).mustNot(
                        QueryBuilders.matchQuery(STATUS, CANCELLED));
        if (serviceDetail.isEmpty())
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(BILLING_SERVICE, serviceDetail));

        final AggregationBuilder monthAggregation = AggregationBuilders.dateHistogram(DATE_AGG).field(RECEIPT_DATE)
                .interval(DateHistogramInterval.MONTH)
                .subAggregation(AggregationBuilders.sum("current_total").field(TOTAL_AMOUNT));

        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME)
                .withQuery(boolQuery).addAggregation(monthAggregation).build();

        return elasticsearchTemplate.query(searchQueryColl, response -> response.getAggregations());
    }

    public List<TaxPayerDashBoardDetails> returnUlbWiseAggregationResults(
            final CollectionDashBoardRequest collectionDashBoardRequest, final String indexName, final Boolean order,
            final String orderingAggregationName, final int size, final List<String> serviceDetails) {
        final List<TaxPayerDashBoardDetails> taxPayers = new ArrayList<>();
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDashBoardRequest);
        if (!serviceDetails.isEmpty())
            boolQuery = boolQuery.filter(QueryBuilders.termsQuery(BILLING_SERVICE, serviceDetails));
        String groupingField;
        if (StringUtils.isNotBlank(collectionDashBoardRequest.getUlbCode())
                || StringUtils.isNotBlank(collectionDashBoardRequest.getType())
                && collectionDashBoardRequest.getType().equals(DASHBOARD_GROUPING_WARDWISE))
            groupingField = REVENUE_WARD;
        else
            groupingField = CITY_NAME;

        Long startTime = System.currentTimeMillis();
        AggregationBuilder aggregation;
        SearchQuery searchQueryColl;
        aggregation = AggregationBuilders.terms(BY_AGGREGATION_FIELD).field(groupingField).size(size)
                .order(Terms.Order.aggregation(orderingAggregationName, order))
                .subAggregation(AggregationBuilders.sum(TOTAL_COLLECTION).field(TOTAL_AMOUNT));
        searchQueryColl = new NativeSearchQueryBuilder().withIndices(indexName).withQuery(boolQuery)
                .addAggregation(aggregation).build();
        final Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());

        Long timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken by ulbWiseAggregations is : " + timeTaken + MILLISECS);

        TaxPayerDashBoardDetails taxDetail;
        boolean isWard = false;
        startTime = System.currentTimeMillis();
        final Date fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
        final Date toDate = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1);
        final Date lastYearFromDate = org.apache.commons.lang3.time.DateUtils.addYears(fromDate, -1);
        final Date lastYearToDate = org.apache.commons.lang3.time.DateUtils.addYears(toDate, -1);
        final StringTerms totalAmountAggr = collAggr.get(BY_AGGREGATION_FIELD);
        for (final Terms.Bucket entry : totalAmountAggr.getBuckets()) {
            taxDetail = new TaxPayerDashBoardDetails();
            taxDetail.setRegionName(collectionDashBoardRequest.getRegionName());
            taxDetail.setDistrictName(collectionDashBoardRequest.getDistrictName());
            taxDetail.setUlbGrade(collectionDashBoardRequest.getUlbGrade());
            final String fieldName = String.valueOf(entry.getKey());
            if (groupingField.equals(REVENUE_WARD)) {
                taxDetail.setWardName(fieldName);
                isWard = true;
            } else
                taxDetail.setUlbName(fieldName);
            final Sum totalCollectionAggregation = entry.getAggregations().get(TOTAL_COLLECTION);
            final BigDecimal totalCollections = BigDecimal.valueOf(totalCollectionAggregation.getValue()).setScale(0,
                    BigDecimal.ROUND_HALF_UP);
            taxDetail.setCytdColl(totalCollections);
            final BigDecimal lastYearCollection = getCollectionBetweenDates(collectionDashBoardRequest,
                    lastYearFromDate, lastYearToDate, fieldName, serviceDetails, isWard);
            taxDetail.setLytdColl(lastYearCollection);
            BigDecimal variation;
            if (lastYearCollection.compareTo(BigDecimal.ZERO) == 0)
                variation = CollectionConstants.BIGDECIMAL_100;
            else
                variation = totalCollections.subtract(lastYearCollection).multiply(CollectionConstants.BIGDECIMAL_100)
                .divide(lastYearCollection, 1, BigDecimal.ROUND_HALF_UP);
            taxDetail.setLyVar(variation);
            taxPayers.add(taxDetail);
        }
        timeTaken = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken for setting values in returnUlbWiseAggregationResults() is : " + timeTaken
                    + MILLISECS);
        return returnTopResults(taxPayers, size, order);
    }

    private List<TaxPayerDashBoardDetails> returnTopResults(final List<TaxPayerDashBoardDetails> taxPayers,
            final int size, final Boolean order) {
        if (size >= 10) {
            if (order)
                Collections.sort(taxPayers);
            else
                Collections.sort(taxPayers, Collections.reverseOrder());

            return taxPayers.subList(0, taxPayers.size() < 10 ? taxPayers.size() : 10);
        }
        return taxPayers;
    }

    public TaxPayerDashBoardResponseDetails getBottomTenTaxPerformers(
            final CollectionDashBoardRequest collectionDashBoardRequest, final List<String> serviceList) {
        final TaxPayerDashBoardResponseDetails topTaxPerformers = new TaxPayerDashBoardResponseDetails();
        List<TaxPayerDashBoardDetails> taxProducers;

        taxProducers = returnUlbWiseAggregationResults(collectionDashBoardRequest, COLLECTION_INDEX_NAME, true,
                TOTAL_COLLECTION, 10, serviceList);
        topTaxPerformers.setProducers(taxProducers);
        return topTaxPerformers;
    }

    public TaxPayerDashBoardResponseDetails getTopTenTaxPerformers(
            final CollectionDashBoardRequest collectionDashBoardRequest, final List<String> serviceList) {

        final TaxPayerDashBoardResponseDetails topTaxPerformers = new TaxPayerDashBoardResponseDetails();
        List<TaxPayerDashBoardDetails> taxProducers;

        taxProducers = returnUlbWiseAggregationResults(collectionDashBoardRequest, COLLECTION_INDEX_NAME, false,
                TOTAL_COLLECTION, 10, serviceList);
        topTaxPerformers.setProducers(taxProducers);

        return topTaxPerformers;
    }
}