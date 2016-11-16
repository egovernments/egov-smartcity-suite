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
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_BILLCOLLECTORWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_CITYWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_DISTRICTWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_GRADEWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_REGIONWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_WARDWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_TAX_INDEX_NAME;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.bean.dashboard.CollectionDetails;
import org.egov.ptis.bean.dashboard.CollectionDetailsRequest;
import org.egov.ptis.bean.dashboard.PropertyTaxDefaultersRequest;
import org.egov.ptis.bean.dashboard.TaxDefaulters;
import org.egov.ptis.bean.dashboard.TaxPayerDetails;
import org.egov.ptis.bean.dashboard.TaxPayerResponseDetails;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.es.BillCollectorIndex;
import org.egov.ptis.domain.entity.es.PropertyTaxIndex;
import org.egov.ptis.repository.es.PropertyTaxIndexRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class PropertyTaxElasticSearchIndexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyTaxElasticSearchIndexService.class);

    private PropertyTaxIndexRepository propertyTaxIndexRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private CollectionIndexElasticSearchService collectionIndexElasticSearchService;

    @Autowired
    public PropertyTaxElasticSearchIndexService(final PropertyTaxIndexRepository propertyTaxIndexRepository) {
        this.propertyTaxIndexRepository = propertyTaxIndexRepository;
    }

    public Page<PropertyTaxIndex> findByConsumercode(String consumerCode, PageRequest pageRequest) {
        return propertyTaxIndexRepository.findByConsumerCode(consumerCode, new PageRequest(0, 10));
    }

    /**
     * API returns the current year total demand from Property Tax index
     * 
     * @return BigDecimal
     */
    public BigDecimal getTotalDemand() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(PROPERTY_TAX_INDEX_NAME)
                .addAggregation(AggregationBuilders.sum("totaldemand").field("totalDemand")).build();

        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        Sum aggr = aggregations.get("totaldemand");
        return BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Populates the consolidated demand information in CollectionIndexDetails
     * 
     * @param collectionDetailsRequest
     * @param collectionIndexDetails
     */
    public void getConsolidatedDemandInfo(CollectionDetailsRequest collectionDetailsRequest,
            CollectionDetails collectionIndexDetails) {
        Date fromDate;
        Date toDate;
        /**
         * For fetching total demand between the date ranges if dates are sent in the request, consider fromDate and toDate+1 ,
         * else calculate from current year start date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), "yyyy-MM-dd");
            toDate = DateUtils.addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), "yyyy-MM-dd"), 1);
        } else {
            fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
            toDate = DateUtils.addDays(new Date(), 1);
        }
        Long startTime = System.currentTimeMillis();
        BigDecimal totalDemand = getTotalDemandBasedOnInputFilters(collectionDetailsRequest);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by getTotalDemandBasedOnInputFilters() is : " + timeTaken + " (millisecs) ");

        startTime = System.currentTimeMillis();
        int noOfMonths = DateUtils.noOfMonths(fromDate, toDate) + 1;
        collectionIndexDetails.setTotalDmd(totalDemand);

        // Proportional Demand = (totalDemand/12)*noOfmonths
        BigDecimal proportionalDemand = (totalDemand.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP))
                .multiply(BigDecimal.valueOf(noOfMonths));
        collectionIndexDetails.setCytdDmd(proportionalDemand.setScale(0, BigDecimal.ROUND_HALF_UP));

        // performance = (current year tilldate collection * 100)/(proportional
        // demand)
        collectionIndexDetails
                .setPerformance((collectionIndexDetails.getCytdColl().multiply(PropertyTaxConstants.BIGDECIMAL_100))
                        .divide(proportionalDemand, 1, BigDecimal.ROUND_HALF_UP));
        // variance = ((currentYearCollection -
        // lastYearCollection)*100)/lastYearCollection
        BigDecimal variation = BigDecimal.ZERO;
        if (collectionIndexDetails.getLytdColl().compareTo(BigDecimal.ZERO) == 0)
            variation = PropertyTaxConstants.BIGDECIMAL_100;
        else
            variation = (collectionIndexDetails.getCytdColl().subtract(collectionIndexDetails.getLytdColl())
                    .multiply(PropertyTaxConstants.BIGDECIMAL_100)).divide(collectionIndexDetails.getLytdColl(), 1,
                            BigDecimal.ROUND_HALF_UP);
        collectionIndexDetails.setLyVar(variation);
        timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug(
                "Time taken for setting values in getConsolidatedDemandInfo() is : " + timeTaken + " (millisecs) ");
    }

    /**
     * Returns total demand from Property tax index, based on input filters
     * 
     * @param collectionDetailsRequest
     * @return
     */
    public BigDecimal getTotalDemandBasedOnInputFilters(CollectionDetailsRequest collectionDetailsRequest) {
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest);
        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(PROPERTY_TAX_INDEX_NAME)
                .withQuery(boolQuery).addAggregation(AggregationBuilders.sum("totaldemand").field("totalDemand"))
                .build();

        Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        Sum aggr = collAggr.get("totaldemand");
        return BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Returns List of Top Ten Tax Performers
     * 
     * @param collectionDetailsRequest
     * @return
     */
    public TaxPayerResponseDetails getTopTenTaxPerformers(CollectionDetailsRequest collectionDetailsRequest) {

        TaxPayerResponseDetails topTaxPerformers = new TaxPayerResponseDetails();
        List<TaxPayerDetails> taxProducers;
        List<TaxPayerDetails> taxAchievers;
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType()) && collectionDetailsRequest.getType()
                .equalsIgnoreCase(PropertyTaxConstants.DASHBOARD_GROUPING_BILLCOLLECTORWISE)) {
            // Fetch the ward wise data for the filters
            List<TaxPayerDetails> wardWiseTaxProducers = returnUlbWiseAggregationResults(collectionDetailsRequest,
                    PROPERTY_TAX_INDEX_NAME, false, "total_collection", 250, true);
            Map<String, TaxPayerDetails> wardWiseTaxPayersDetails = new HashMap<>();
            Map<String, List<TaxPayerDetails>> billCollectorWiseMap = new LinkedHashMap<>();
            List<TaxPayerDetails> taxPayerDetailsList = new ArrayList<>();
            List<TaxPayerDetails> billCollectorWiseTaxPayerDetails = new ArrayList<>();
            // Get ward wise tax payers details
            prepareWardWiseTaxPayerDetails(wardWiseTaxProducers, wardWiseTaxPayersDetails);
            // Group the revenue ward details by bill collector
            prepareBillCollectorWiseMapData(collectionDetailsRequest, wardWiseTaxPayersDetails, billCollectorWiseMap,
                    taxPayerDetailsList);
            // Prepare Bill Collector wise tax payers details
            prepareTaxersInfoForBillCollectors(collectionDetailsRequest, billCollectorWiseMap,
                    billCollectorWiseTaxPayerDetails);
            taxProducers = getTaxPayersForBillCollector(collectionDetailsRequest, false, wardWiseTaxProducers,
                    billCollectorWiseTaxPayerDetails, true);
            taxAchievers = getTaxPayersForBillCollector(collectionDetailsRequest, false, wardWiseTaxProducers,
                    billCollectorWiseTaxPayerDetails, false);
        } else {
            taxProducers = returnUlbWiseAggregationResults(collectionDetailsRequest, PROPERTY_TAX_INDEX_NAME, false,
                    "total_collection", 10, false);
            taxAchievers = returnUlbWiseAggregationResults(collectionDetailsRequest, PROPERTY_TAX_INDEX_NAME, false,
                    "total_collection", 120, false);
        }

        topTaxPerformers.setProducers(taxProducers);
        topTaxPerformers.setAchievers(taxAchievers);

        return topTaxPerformers;
    }

    /**
     * Returns List of Bottom Ten Tax Performers
     * 
     * @param collectionDetailsRequest
     * @return
     */
    public TaxPayerResponseDetails getBottomTenTaxPerformers(CollectionDetailsRequest collectionDetailsRequest) {
        TaxPayerResponseDetails topTaxPerformers = new TaxPayerResponseDetails();
        List<TaxPayerDetails> taxProducers;
        List<TaxPayerDetails> taxAchievers;
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType()) && collectionDetailsRequest.getType()
                .equalsIgnoreCase(PropertyTaxConstants.DASHBOARD_GROUPING_BILLCOLLECTORWISE)) {
            List<TaxPayerDetails> wardWiseTaxProducers = returnUlbWiseAggregationResults(collectionDetailsRequest,
                    PROPERTY_TAX_INDEX_NAME, false, "total_collection", 250, true);
            Map<String, TaxPayerDetails> wardWiseTaxPayersDetails = new HashMap<>();
            Map<String, List<TaxPayerDetails>> billCollectorWiseMap = new LinkedHashMap<>();
            List<TaxPayerDetails> taxPayerDetailsList = new ArrayList<>();
            List<TaxPayerDetails> billCollectorWiseTaxPayerDetails = new ArrayList<>();
            // Get ward wise tax payers details
            prepareWardWiseTaxPayerDetails(wardWiseTaxProducers, wardWiseTaxPayersDetails);
            // Group the revenue ward details by bill collector
            prepareBillCollectorWiseMapData(collectionDetailsRequest, wardWiseTaxPayersDetails, billCollectorWiseMap,
                    taxPayerDetailsList);
            // Prepare Bill Collector wise tax payers details
            prepareTaxersInfoForBillCollectors(collectionDetailsRequest, billCollectorWiseMap,
                    billCollectorWiseTaxPayerDetails);
            taxProducers = getTaxPayersForBillCollector(collectionDetailsRequest, true, wardWiseTaxProducers,
                    billCollectorWiseTaxPayerDetails, true);
            taxAchievers = getTaxPayersForBillCollector(collectionDetailsRequest, true, wardWiseTaxProducers,
                    billCollectorWiseTaxPayerDetails, false);
        } else {
            taxProducers = returnUlbWiseAggregationResults(collectionDetailsRequest, PROPERTY_TAX_INDEX_NAME, true,
                    "total_collection", 10, false);
            taxAchievers = returnUlbWiseAggregationResults(collectionDetailsRequest, PROPERTY_TAX_INDEX_NAME, true,
                    "total_collection", 120, false);
        }
        topTaxPerformers.setProducers(taxProducers);
        topTaxPerformers.setAchievers(taxAchievers);

        return topTaxPerformers;
    }

    /**
     * Returns Top Ten with ULB wise grouping and total amount aggregation
     * 
     * @param collectionDetailsRequest
     * @param indexName
     * @param order
     * @param orderingAggregationName
     * @return
     */
    public List<TaxPayerDetails> returnUlbWiseAggregationResults(CollectionDetailsRequest collectionDetailsRequest,
            String indexName, Boolean order, String orderingAggregationName, int size, boolean isBillCollectorWise) {
        List<TaxPayerDetails> taxPayers = new ArrayList<>();
        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest);

        // orderingAggregationName is the aggregation name by which we have to
        // order the results
        // IN this case can be one of "totaldemand" or "total_collection" or
        // "avg_achievement"
        String groupingField;
        if (StringUtils.isNotBlank(collectionDetailsRequest.getUlbCode())
                || (StringUtils.isNotBlank(collectionDetailsRequest.getType())
                        && (collectionDetailsRequest.getType().equals(DASHBOARD_GROUPING_WARDWISE)
                                || collectionDetailsRequest.getType().equals(DASHBOARD_GROUPING_BILLCOLLECTORWISE)))) {
            groupingField = "revenueWard";
        } else
            groupingField = "cityName";

        Long startTime = System.currentTimeMillis();
        AggregationBuilder aggregation;
        SearchQuery searchQueryColl = null;
        // Apply the ordering and max results size only if the type is not
        // billcollector
        if (!isBillCollectorWise) {
            aggregation = AggregationBuilders.terms("by_aggregationField").field(groupingField).size(size)
                    .order(Terms.Order.aggregation(orderingAggregationName, order))
                    .subAggregation(AggregationBuilders.sum("totaldemand").field("totalDemand"))
                    .subAggregation(AggregationBuilders.sum("total_collection").field("totalCollection"));
            searchQueryColl = new NativeSearchQueryBuilder().withIndices(indexName).withQuery(boolQuery)
                    .addAggregation(aggregation).build();
        } else {
            aggregation = AggregationBuilders.terms("by_aggregationField").field(groupingField).size(250)
                    .subAggregation(AggregationBuilders.sum("totaldemand").field("totalDemand"))
                    .subAggregation(AggregationBuilders.sum("total_collection").field("totalCollection"));
            searchQueryColl = new NativeSearchQueryBuilder().withIndices(indexName).withQuery(boolQuery)
                    .withPageable(new PageRequest(0, 250)).addAggregation(aggregation).build();
        }

        Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by ulbWiseAggregations is : " + timeTaken + " (millisecs) ");

        TaxPayerDetails taxDetail;
        startTime = System.currentTimeMillis();
        Date fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
        Date toDate = DateUtils.addDays(new Date(), 1);
        Date lastYearFromDate = DateUtils.addYears(fromDate, -1);
        Date lastYearToDate = DateUtils.addYears(toDate, -1);
        StringTerms totalAmountAggr = collAggr.get("by_aggregationField");
        for (Terms.Bucket entry : totalAmountAggr.getBuckets()) {
            taxDetail = new TaxPayerDetails();
            taxDetail.setRegionName(collectionDetailsRequest.getRegionName());
            taxDetail.setDistrictName(collectionDetailsRequest.getDistrictName());
            taxDetail.setUlbGrade(collectionDetailsRequest.getUlbGrade());
            String fieldName = String.valueOf(entry.getKey());
            if (groupingField.equals("revenueWard"))
                taxDetail.setWardName(fieldName);
            else
                taxDetail.setUlbName(fieldName);
            // Proportional Demand = (totalDemand/12)*noOfmonths
            int noOfMonths = DateUtils.noOfMonths(fromDate, toDate) + 1;
            Sum totalDemandAggregation = entry.getAggregations().get("totaldemand");
            Sum totalCollectionAggregation = entry.getAggregations().get("total_collection");
            BigDecimal totalDemandValue = BigDecimal.valueOf(totalDemandAggregation.getValue()).setScale(0,
                    BigDecimal.ROUND_HALF_UP);
            BigDecimal totalCollections = BigDecimal.valueOf(totalCollectionAggregation.getValue()).setScale(0,
                    BigDecimal.ROUND_HALF_UP);
            BigDecimal proportionalDemand = (totalDemandValue.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP))
                    .multiply(BigDecimal.valueOf(noOfMonths));
            taxDetail.setTotalDmd(totalDemandValue);
            taxDetail.setCytdColl(totalCollections);
            taxDetail.setCytdDmd(proportionalDemand);
            taxDetail.setAchievement(
                    totalCollections.multiply(BIGDECIMAL_100).divide(proportionalDemand, 1, BigDecimal.ROUND_HALF_UP));
            taxDetail.setCytdBalDmd(proportionalDemand.subtract(totalCollections));
            BigDecimal lastYearCollection = collectionIndexElasticSearchService
                    .getCollectionBetweenDates(collectionDetailsRequest, lastYearFromDate, lastYearToDate, fieldName);
            // TotalCollectionsForDatesForUlb(collectionDetailsRequest,
            // lastYearFromDate, lastYearToDate,fieldName);
            taxDetail.setLytdColl(lastYearCollection);
            // variance = ((currentYearCollection -
            // lastYearCollection)*100)/lastYearCollection
            BigDecimal variation = BigDecimal.ZERO;
            if (lastYearCollection.compareTo(BigDecimal.ZERO) == 0)
                variation = PropertyTaxConstants.BIGDECIMAL_100;
            else
                variation = (((totalCollections.subtract(lastYearCollection))
                        .multiply(PropertyTaxConstants.BIGDECIMAL_100)).divide(lastYearCollection, 1,
                                BigDecimal.ROUND_HALF_UP));
            taxDetail.setLyVar(variation);
            taxPayers.add(taxDetail);
        }
        timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken for setting values in returnUlbWiseAggregationResults() is : " + timeTaken
                + " (millisecs) ");
        // If for Bill Collector, then fetch details for all wards, else limit
        // the results size
        if (isBillCollectorWise)
            return taxPayers;
        else
            return returnTopResults(taxPayers, size, order);
    }

    private List<TaxPayerDetails> returnTopResults(List<TaxPayerDetails> taxPayers, int size, Boolean order) {
        if (size > 10) {
            if (order)
                Collections.sort(taxPayers);
            else
                Collections.sort(taxPayers, Collections.reverseOrder());

            return taxPayers.subList(0, (taxPayers.size() < 10) ? taxPayers.size() : 10);
        }
        return taxPayers;
    }

    /**
     * Returns top 100 tax defaulters
     * 
     * @param propertyTaxDefaultersRequest
     * @return
     */
    public List<TaxDefaulters> getTopDefaulters(PropertyTaxDefaultersRequest propertyTaxDefaultersRequest) {
        Long startTime = System.currentTimeMillis();
        BoolQueryBuilder boolQuery = filterBasedOnRequest(propertyTaxDefaultersRequest);
        boolQuery = boolQuery.mustNot(QueryBuilders.matchQuery("cityName", "Guntur"))
                .mustNot(QueryBuilders.matchQuery("cityName", "Vijayawada"))
                .mustNot(QueryBuilders.matchQuery("cityName", "Visakhapatnam"));

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(PROPERTY_TAX_INDEX_NAME)
                .withQuery(boolQuery).withSort(new FieldSortBuilder("totalBalance").order(SortOrder.DESC))
                .withPageable(new PageRequest(0, 100)).build();

        final Page<PropertyTaxIndex> propertyTaxRecords = elasticsearchTemplate.queryForPage(searchQuery,
                PropertyTaxIndex.class);
        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by defaulters aggregation is : " + timeTaken + " (millisecs) ");

        List<TaxDefaulters> taxDefaulters = new ArrayList<>();
        TaxDefaulters taxDfaulter;
        startTime = System.currentTimeMillis();
        for (PropertyTaxIndex property : propertyTaxRecords) {
            taxDfaulter = new TaxDefaulters();
            taxDfaulter.setOwnerName(property.getConsumerName());
            taxDfaulter.setPropertyType(property.getPropertyType());
            taxDfaulter.setUlbName(property.getCityName());
            taxDfaulter.setBalance(BigDecimal.valueOf(property.getTotalBalance()));
            taxDfaulter.setPeriod(StringUtils.EMPTY);
            taxDefaulters.add(taxDfaulter);
        }
        timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken for setting values in getTopDefaulters() is : " + timeTaken + " (millisecs) ");
        return taxDefaulters;
    }

    /**
     * This is used for top 100 defaulter's since ward level filtering is also present Query which filters documents from index
     * based on request
     * 
     * @param propertyTaxDefaultersRequest
     * @return
     */
    private BoolQueryBuilder filterBasedOnRequest(PropertyTaxDefaultersRequest propertyTaxDefaultersRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.rangeQuery("totalDemand").from(0).to(null));
        if (StringUtils.isNotBlank(propertyTaxDefaultersRequest.getRegionName()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery("regionName", propertyTaxDefaultersRequest.getRegionName()));
        if (StringUtils.isNotBlank(propertyTaxDefaultersRequest.getDistrictName()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery("districtName", propertyTaxDefaultersRequest.getDistrictName()));
        if (StringUtils.isNotBlank(propertyTaxDefaultersRequest.getUlbCode()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery("cityCode", propertyTaxDefaultersRequest.getUlbCode()));
        if (StringUtils.isNotBlank(propertyTaxDefaultersRequest.getUlbGrade()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery("cityGrade", propertyTaxDefaultersRequest.getUlbGrade()));
        if (StringUtils.isNotBlank(propertyTaxDefaultersRequest.getWardName()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery("revenueWard", propertyTaxDefaultersRequest.getWardName()));
        if (StringUtils.isNotBlank(propertyTaxDefaultersRequest.getType())) {
            if (propertyTaxDefaultersRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_REGIONWISE)
                    && StringUtils.isNotBlank(propertyTaxDefaultersRequest.getRegionName()))
                boolQuery = boolQuery
                        .filter(QueryBuilders.matchQuery("regionName", propertyTaxDefaultersRequest.getRegionName()));
            else if (propertyTaxDefaultersRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_DISTRICTWISE)
                    && StringUtils.isNotBlank(propertyTaxDefaultersRequest.getDistrictName()))
                boolQuery = boolQuery.filter(
                        QueryBuilders.matchQuery("districtName", propertyTaxDefaultersRequest.getDistrictName()));
            else if (propertyTaxDefaultersRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_CITYWISE)
                    && StringUtils.isNotBlank(propertyTaxDefaultersRequest.getUlbCode()))
                boolQuery = boolQuery
                        .filter(QueryBuilders.matchQuery("cityCode", propertyTaxDefaultersRequest.getUlbCode()));
            else if (propertyTaxDefaultersRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_GRADEWISE)
                    && StringUtils.isNotBlank(propertyTaxDefaultersRequest.getUlbGrade()))
                boolQuery = boolQuery
                        .filter(QueryBuilders.matchQuery("cityGrade", propertyTaxDefaultersRequest.getUlbGrade()));
        }

        return boolQuery;
    }

    /**
     * Builds query based on the input parameters sent
     * 
     * @param collectionDetailsRequest
     * @param indexName
     * @param ulbCodeField
     * @return BoolQueryBuilder
     */
    private BoolQueryBuilder prepareWhereClause(CollectionDetailsRequest collectionDetailsRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.rangeQuery("totalDemand").from(0).to(null));
        if (StringUtils.isNotBlank(collectionDetailsRequest.getRegionName()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery("regionName", collectionDetailsRequest.getRegionName()));
        if (StringUtils.isNotBlank(collectionDetailsRequest.getDistrictName()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery("districtName", collectionDetailsRequest.getDistrictName()));
        if (StringUtils.isNotBlank(collectionDetailsRequest.getUlbGrade()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("cityGrade", collectionDetailsRequest.getUlbGrade()));
        if (StringUtils.isNotBlank(collectionDetailsRequest.getUlbCode()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("cityCode", collectionDetailsRequest.getUlbCode()));
        return boolQuery;
    }

    /**
     * Prepare ward wise tax payers details - Map of ward name and tax paers bean
     * 
     * @param wardWiseTaxProducers
     * @param wardWiseTaxPayersDetails
     */
    private void prepareWardWiseTaxPayerDetails(List<TaxPayerDetails> wardWiseTaxProducers,
            Map<String, TaxPayerDetails> wardWiseTaxPayersDetails) {
        for (TaxPayerDetails taxPayers : wardWiseTaxProducers) {
            wardWiseTaxPayersDetails.put(taxPayers.getWardName(), taxPayers);
        }
    }

    /**
     * Prepare a Map of Bill Collector names and the tax payers list for their respective wards
     * 
     * @param collectionDetailsRequest
     * @param wardWiseTaxPayersDetails
     * @param billCollectorWiseMap
     * @param taxPayerDetailsList
     */
    private void prepareBillCollectorWiseMapData(CollectionDetailsRequest collectionDetailsRequest,
            Map<String, TaxPayerDetails> wardWiseTaxPayersDetails,
            Map<String, List<TaxPayerDetails>> billCollectorWiseMap, List<TaxPayerDetails> taxPayerDetailsList) {

        List<BillCollectorIndex> billCollectorsList = collectionIndexElasticSearchService
                .getBillCollectorDetails(collectionDetailsRequest);
        for (BillCollectorIndex billCollIndex : billCollectorsList) {
            if (wardWiseTaxPayersDetails.get(billCollIndex.getRevenueWard()) != null) {
                if (billCollectorWiseMap.isEmpty()) {
                    taxPayerDetailsList.add(wardWiseTaxPayersDetails.get(billCollIndex.getRevenueWard()));
                    billCollectorWiseMap.put(billCollIndex.getBillCollector(), taxPayerDetailsList);
                } else {
                    if (!billCollectorWiseMap.containsKey(billCollIndex.getBillCollector())) {
                        taxPayerDetailsList = new ArrayList<>();
                        taxPayerDetailsList.add(wardWiseTaxPayersDetails.get(billCollIndex.getRevenueWard()));
                        billCollectorWiseMap.put(billCollIndex.getBillCollector(), taxPayerDetailsList);
                    } else {
                        billCollectorWiseMap.get(billCollIndex.getBillCollector())
                                .add(wardWiseTaxPayersDetails.get(billCollIndex.getRevenueWard()));
                    }
                }
            }
        }
    }

    /**
     * Prepares the Producers and Acheivers list - Bill Collector wise
     * 
     * @param collectionDetailsRequest
     * @param order
     * @param wardWiseTaxProducers
     * @param billCollectorWiseTaxPayerDetails
     * @param isForProducers
     * @return
     */
    private List<TaxPayerDetails> getTaxPayersForBillCollector(CollectionDetailsRequest collectionDetailsRequest,
            boolean order, List<TaxPayerDetails> wardWiseTaxProducers,
            List<TaxPayerDetails> billCollectorWiseTaxPayerDetails, boolean isForProducers) {
        Map<BigDecimal, TaxPayerDetails> sortedTaxersMap = new HashMap<>();
        // For propducers, prepare sorted list of totalCollection
        // For achievers, prepare sorted list of achievement
        if (isForProducers) {
            for (TaxPayerDetails payerDetails : billCollectorWiseTaxPayerDetails)
                sortedTaxersMap.put(payerDetails.getCytdColl(), payerDetails);
        } else {
            for (TaxPayerDetails payerDetails : billCollectorWiseTaxPayerDetails)
                sortedTaxersMap.put(payerDetails.getAchievement(), payerDetails);
        }

        List<BigDecimal> sortedList = new ArrayList<>(sortedTaxersMap.keySet());
        // Decides whether API should return in ascending or descending order
        if (order)
            Collections.sort(sortedList);
        else
            Collections.sort(sortedList, Collections.reverseOrder());

        List<TaxPayerDetails> taxersResult = new ArrayList<>();
        for (BigDecimal amount : sortedList)
            taxersResult.add(sortedTaxersMap.get(amount));

        if (taxersResult.size() > 10)
            return taxersResult.subList(0, (taxersResult.size() < 10) ? taxersResult.size() : 10);
        else
            return taxersResult;
    }

    /**
     * Prepare list of TaxPayerDetails for each bill collector by summing up the values in each ward for the respective bil
     * collector
     * 
     * @param collectionDetailsRequest
     * @param billCollectorWiseMap
     * @param billCollectorWiseTaxPayerDetails
     */
    private void prepareTaxersInfoForBillCollectors(CollectionDetailsRequest collectionDetailsRequest,
            Map<String, List<TaxPayerDetails>> billCollectorWiseMap,
            List<TaxPayerDetails> billCollectorWiseTaxPayerDetails) {
        BigDecimal cytdColl = BigDecimal.ZERO;
        BigDecimal lytdColl = BigDecimal.ZERO;
        BigDecimal cytdDmd = BigDecimal.ZERO;
        BigDecimal totalDmd = BigDecimal.ZERO;
        BigDecimal variance = BigDecimal.ZERO;
        TaxPayerDetails taxPayerDetails;
        for (Entry<String, List<TaxPayerDetails>> entry : billCollectorWiseMap.entrySet()) {
            taxPayerDetails = new TaxPayerDetails();
            cytdColl = BigDecimal.ZERO;
            lytdColl = BigDecimal.ZERO;
            cytdDmd = BigDecimal.ZERO;
            totalDmd = BigDecimal.ZERO;
            variance = BigDecimal.ZERO;
            for (TaxPayerDetails taxPayer : entry.getValue()) {
                totalDmd = totalDmd.add(taxPayer.getTotalDmd() == null ? BigDecimal.ZERO : taxPayer.getTotalDmd());
                cytdColl = cytdColl.add(taxPayer.getCytdColl() == null ? BigDecimal.ZERO : taxPayer.getCytdColl());
                cytdDmd = cytdDmd.add(taxPayer.getCytdDmd() == null ? BigDecimal.ZERO : taxPayer.getCytdDmd());
                lytdColl = lytdColl.add(taxPayer.getLytdColl() == null ? BigDecimal.ZERO : taxPayer.getLytdColl());
            }
            taxPayerDetails.setBillCollector(entry.getKey());
            taxPayerDetails.setRegionName(collectionDetailsRequest.getRegionName());
            taxPayerDetails.setDistrictName(collectionDetailsRequest.getDistrictName());
            taxPayerDetails.setUlbGrade(collectionDetailsRequest.getUlbGrade());
            taxPayerDetails.setCytdColl(cytdColl);
            taxPayerDetails.setCytdDmd(cytdDmd);
            taxPayerDetails.setCytdBalDmd(cytdDmd.subtract(cytdColl));
            taxPayerDetails.setTotalDmd(totalDmd);
            taxPayerDetails.setLytdColl(lytdColl);
            taxPayerDetails
                    .setAchievement(cytdColl.multiply(BIGDECIMAL_100).divide(cytdDmd, 1, BigDecimal.ROUND_HALF_UP));
            if (lytdColl.compareTo(BigDecimal.ZERO) == 0)
                variance = PropertyTaxConstants.BIGDECIMAL_100;
            else
                variance = (((cytdColl.subtract(lytdColl)).multiply(PropertyTaxConstants.BIGDECIMAL_100))
                        .divide(lytdColl, 1, BigDecimal.ROUND_HALF_UP));

            billCollectorWiseTaxPayerDetails.add(taxPayerDetails);
        }
    }

}