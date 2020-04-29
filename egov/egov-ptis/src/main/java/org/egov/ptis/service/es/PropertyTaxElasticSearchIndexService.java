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

package org.egov.ptis.service.es;

import static org.egov.ptis.constants.PropertyTaxConstants.BIGDECIMAL_100;
import static org.egov.ptis.constants.PropertyTaxConstants.COLLECTION_ACHIEVEMENTS_INDEX_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_BILLCOLLECTORWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_CITYWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_DISTRICTWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_GRADEWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_REGIONWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_REVENUEINSPECTORWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_REVENUEOFFICERWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_WARDWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_YYYYMMDD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_TAX_INDEX_NAME;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.infra.admin.master.entity.es.CityIndex;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.bean.dashboard.CollectionDetails;
import org.egov.ptis.bean.dashboard.CollectionDetailsRequest;
import org.egov.ptis.bean.dashboard.DCBDetails;
import org.egov.ptis.bean.dashboard.DemandVariance;
import org.egov.ptis.bean.dashboard.DemandVariationDetails;
import org.egov.ptis.bean.dashboard.PropertyTaxDefaultersRequest;
import org.egov.ptis.bean.dashboard.TaxDefaulters;
import org.egov.ptis.bean.dashboard.TaxPayerDetails;
import org.egov.ptis.bean.dashboard.TaxPayerResponseDetails;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.es.BillCollectorIndex;
import org.egov.ptis.domain.entity.es.CollectionAchievementsIndex;
import org.egov.ptis.domain.entity.es.PropertyTaxIndex;
import org.egov.ptis.repository.es.PropertyTaxIndexRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class PropertyTaxElasticSearchIndexService {

    private static final String REBATE_STR = "rebate";
    private static final String TOTAL_COLL_STR = "totalColl";
    private static final String ADVANCE_COLL_STR = "advanceColl";
    private static final String CURRENT_INT_COLL = "currentIntColl";
    private static final String ARREAR_INT_COLL = "arrearIntColl";
    private static final String CURRENT_COLL_STR = "currentColl";
    private static final String ARREAR_COLL_STR = "arrearColl";
    private static final String TOTAL_DMD_STR = "totalDmd";
    private static final String CURRENT_INT_DMD = "currentIntDmd";
    private static final String ARREAR_INT_DMD = "arrearIntDmd";
    private static final String CURRENT_DMD = "currentDmd";
    private static final String ARREAR_DMD = "arrearDmd";
    private static final String CURR_INTEREST_DMD = "curr_interest_dmd";
    private static final String ARREAR_INTEREST_DMD = "arrear_interest_dmd";
    private static final String CURR_DMD = "curr_dmd";
    private static final String ARREAR_DMD_STR = "arrear_dmd";
    private static final String CURRENT_INTEREST_COLLECTION = "currentInterestCollection";
    private static final String ARREAR_INTEREST_COLLECTION = "arrearInterestCollection";
    private static final String ANNUAL_COLLECTION = "annualCollection";
    private static final String ARREAR_COLLECTION = "arrearCollection";
    private static final String IS_ACTIVE = "isActive";
    private static final String IS_EXEMPTED = "isExempted";
    private static final String CITY_GRADE = "cityGrade";
    private static final String CITY_CODE = "cityCode";
    private static final String DISTRICT_NAME = "districtName";
    private static final String REGION_NAME = "regionName";
    private static final String BY_AGGREGATION_FIELD = "by_aggregationField";
    private static final String CITY_NAME = "cityName";
    private static final String REVENUE_WARD = "revenueWard";
    private static final String TOTAL_COLLECTION = "total_collection";
    private static final String TOTAL_DEMAND = "totalDemand";
    private static final String ARREAR_DEMAND = "arrearDemand";
    private static final String CURRENT_DEMAND = "currentDemand";
    private static final String TOTALDEMAND = "totaldemand";
    private static final String BY_TYPE = "ByType";
    private static final String BY_APPLICATION = "By_Applicationtype";
    private static final String APPLICATION_TYPE = "applicationType";
    private static final String DEMAND_DATE = "demandDate";
    private static final String PRIMARY_DEMAND = "Primary Demand";
    private static final String DEMAND_VARIATION = "demandvariation";
    private static final String PT_NEW_ASSMNT = "New_Assessment";
    private static final String PT_ALTER_ASSMNT = "Alter_Assessment";
    private static final Logger LOGGER = Logger.getLogger(PropertyTaxElasticSearchIndexService.class);


    private final PropertyTaxIndexRepository propertyTaxIndexRepository;

    @Autowired
    private CFinancialYearService cFinancialYearService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private CollectionIndexElasticSearchService collectionIndexElasticSearchService;

    @Autowired
    public PropertyTaxElasticSearchIndexService(final PropertyTaxIndexRepository propertyTaxIndexRepository) {
        this.propertyTaxIndexRepository = propertyTaxIndexRepository;
    }

    public Page<PropertyTaxIndex> findByConsumercode(final String consumerCode) {
        return propertyTaxIndexRepository.findByConsumerCode(consumerCode, new PageRequest(0, 10));
    }

    /**
     * API returns the current year total demand from Property Tax index
     *
     * @return BigDecimal
     */
    public BigDecimal getTotalDemand() {
        final BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.rangeQuery(TOTAL_DEMAND).from(0).to(null))
                .filter(QueryBuilders.matchQuery(IS_ACTIVE, true))
                .filter(QueryBuilders.matchQuery(IS_EXEMPTED, false));
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(PROPERTY_TAX_INDEX_NAME)
                .withQuery(boolQuery)
                .addAggregation(AggregationBuilders.sum(TOTALDEMAND).field(TOTAL_DEMAND)).build();

        final Aggregations aggregations = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());

        final Sum aggr = aggregations.get(TOTALDEMAND);
        return BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Populates the consolidated demand information in CollectionIndexDetails
     *
     * @param collectionDetailsRequest
     * @param collectionIndexDetails
     */
    public void getConsolidatedDemandInfo(final CollectionDetailsRequest collectionDetailsRequest,
            final CollectionDetails collectionIndexDetails) {
        Date fromDate;
        Date toDate;
        final CFinancialYear currFinYear = cFinancialYearService.getFinancialYearByDate(new Date());

        /**
         * For fetching total demand between the date ranges if dates are sent in the request, consider fromDate and toDate+1 ,
         * else calculate from current year start date till current date+1 day
         */
        if (StringUtils.isNotBlank(collectionDetailsRequest.getFromDate())
                && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())) {
            fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), "yyyy-MM-dd");
            toDate = DateUtils.getDate(collectionDetailsRequest.getToDate(), "yyyy-MM-dd");
        } else {
            fromDate = DateUtils.startOfDay(currFinYear.getStartingDate());
            toDate = new Date();
        }
        final BigDecimal totalDemand = getTotalDemandBasedOnInputFilters(collectionDetailsRequest);
        final int noOfMonths = DateUtils.noOfMonthsBetween(fromDate, toDate) + 1;
        collectionIndexDetails.setTotalDmd(totalDemand);

        // Proportional Demand = (totalDemand/12)*noOfmonths
        final BigDecimal proportionalDemand = totalDemand.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(noOfMonths));
        collectionIndexDetails.setCytdDmd(proportionalDemand.setScale(0, BigDecimal.ROUND_HALF_UP));

        collectionIndexDetails.setTotalAssessments(
                collectionIndexElasticSearchService.getTotalAssessmentsCount(collectionDetailsRequest));

        if (proportionalDemand.compareTo(BigDecimal.ZERO) > 0)
            // performance = (current year tilldate collection * 100)/(proportional demand)
            collectionIndexDetails
                    .setPerformance(collectionIndexDetails.getCytdColl().multiply(PropertyTaxConstants.BIGDECIMAL_100)
                            .divide(proportionalDemand, 1, BigDecimal.ROUND_HALF_UP));
        // variance = ((currentYearCollection - lastYearCollection)*100)/lastYearCollection
        BigDecimal variation;
        if (collectionIndexDetails.getLytdColl().compareTo(BigDecimal.ZERO) == 0)
            variation = BIGDECIMAL_100;
        else
            variation = collectionIndexDetails.getCytdColl().subtract(collectionIndexDetails.getLytdColl())
                    .multiply(BIGDECIMAL_100).divide(collectionIndexDetails.getLytdColl(), 1,
                            BigDecimal.ROUND_HALF_UP);
        collectionIndexDetails.setLyVar(variation);
    }

    /**
     * Returns total demand from Property tax index, based on input filters
     *
     * @param collectionDetailsRequest
     * @return
     */
    public BigDecimal getTotalDemandBasedOnInputFilters(final CollectionDetailsRequest collectionDetailsRequest) {
        final BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest)
                .filter(QueryBuilders.matchQuery(IS_ACTIVE, true))
                .filter(QueryBuilders.matchQuery(IS_EXEMPTED, false));

        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(PROPERTY_TAX_INDEX_NAME)
                .withQuery(boolQuery).addAggregation(AggregationBuilders.sum(TOTALDEMAND).field(TOTAL_DEMAND))
                .build();
        final Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, response -> response.getAggregations());

        final Sum aggr = collAggr.get(TOTALDEMAND);
        return BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * API prepares the data to push into CollectionAchievements index
     * @param collectionDetailsRequest
     * @param currFinYear
     * @param city
     * @return list
     */
    public List<TaxPayerDetails> prepareDataForAchievementsIndex(CollectionDetailsRequest collectionDetailsRequest,
            CFinancialYear currFinYear, CityIndex city) {
        List<TaxPayerDetails> userWiseTaxPayerDetails = new ArrayList<>();
        try {
            List<TaxPayerDetails> wardWiseTaxProducers;
            Map<String, TaxPayerDetails> wardWiseTaxPayersDetails = new HashMap<>();
            Map<String, List<TaxPayerDetails>> billCollectorWiseMap = new LinkedHashMap<>();
            List<TaxPayerDetails> taxPayerDetailsList = new ArrayList<>();
            wardWiseTaxProducers = returnUlbWiseAggregationResults(collectionDetailsRequest,
                    PROPERTY_TAX_INDEX_NAME, false, TOTAL_COLLECTION, 250, true, currFinYear);
            // Get ward wise tax payers details
            prepareWardWiseTaxPayerDetails(wardWiseTaxProducers, wardWiseTaxPayersDetails);
            // Group the revenue ward details by bill collector
            prepareBillCollectorWiseMapData(collectionDetailsRequest, wardWiseTaxPayersDetails, billCollectorWiseMap,
                    taxPayerDetailsList);
            // Prepare Bill Collector wise tax payers details
            prepareTaxersInfoForBillCollectors(collectionDetailsRequest, billCollectorWiseMap,
                    userWiseTaxPayerDetails);
            for (TaxPayerDetails taxPayerDetails : userWiseTaxPayerDetails) {
                taxPayerDetails.setRegionName(city.getRegionname());
                taxPayerDetails.setDistrictName(city.getDistrictname());
                taxPayerDetails.setUlbGrade(city.getCitygrade());
                taxPayerDetails.setUlbName(city.getName());
                taxPayerDetails.setUlbCode(city.getCitycode());
            }
        } catch (Exception e) {
            LOGGER.error("Error while preparing Data For Achievements Index " + e.getMessage());
        }
        return userWiseTaxPayerDetails;
    }

    public Map<String, List<TaxPayerDetails>> getCollectionRankings(CollectionDetailsRequest collectionDetailsRequest) {
        Map<String, List<TaxPayerDetails>> rankingsMap = new LinkedHashMap<>();
        List<TaxPayerDetails> taxPayerDetailsList = new ArrayList<>();
        TaxPayerDetails taxPayerDetails;

        String groupingField = StringUtils.EMPTY;

        BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest);

        if (DASHBOARD_GROUPING_BILLCOLLECTORWISE.equalsIgnoreCase(collectionDetailsRequest.getType()))
            groupingField = "billCollector";
        else if (DASHBOARD_GROUPING_REVENUEINSPECTORWISE.equalsIgnoreCase(collectionDetailsRequest.getType()))
            groupingField = "revenueInspector";
        else if (DASHBOARD_GROUPING_REVENUEOFFICERWISE.equalsIgnoreCase(collectionDetailsRequest.getType()))
            groupingField = "revenueOfficer";

        if (StringUtils.isNotBlank(groupingField))
            boolQuery = boolQuery.mustNot(QueryBuilders.matchQuery(groupingField, StringUtils.EMPTY));
        SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(COLLECTION_ACHIEVEMENTS_INDEX_NAME)
                .withQuery(boolQuery)
                .withSort(new FieldSortBuilder("achievement").order(SortOrder.DESC))
                .withPageable(new PageRequest(0, 3000))
                .addAggregation(AggregationBuilders.terms(BY_AGGREGATION_FIELD).field(groupingField).size(3000))
                .build();

        List<CollectionAchievementsIndex> achievementsList = elasticsearchTemplate.queryForList(searchQueryColl,
                CollectionAchievementsIndex.class);

        for (CollectionAchievementsIndex achievement : achievementsList) {
            taxPayerDetails = new TaxPayerDetails();
            setAchieversData(collectionDetailsRequest, taxPayerDetails, achievement);
            taxPayerDetailsList.add(taxPayerDetails);
        }

        // Sort the final list based on the achievements, in desc and asc order to get the top 10 and bottom 10 achievers
        // respectively
        List<TaxPayerDetails> topTenList = new ArrayList(taxPayerDetailsList);
        if (!topTenList.isEmpty())
            Collections.sort(topTenList, (detail1, detail2) -> detail2.getAchievement().compareTo(detail1.getAchievement()));

        rankingsMap.put("topTenRankings", topTenList.subList(0, topTenList.size() < 10 ? topTenList.size() : 10));

        List<TaxPayerDetails> bottomTenList = new ArrayList(taxPayerDetailsList);
        if (!bottomTenList.isEmpty())
            Collections.sort(bottomTenList, (detail1, detail2) -> detail1.getAchievement().compareTo(detail2.getAchievement()));

        rankingsMap.put("bottomTenRankings", bottomTenList.subList(0, bottomTenList.size() < 10 ? bottomTenList.size() : 10));
        return rankingsMap;
    }

    private void setAchieversData(CollectionDetailsRequest collectionDetailsRequest, TaxPayerDetails taxPayerDetails,
            CollectionAchievementsIndex achievement) {
        taxPayerDetails.setRegionName(achievement.getRegionName());
        taxPayerDetails.setDistrictName(achievement.getDistrictName());
        taxPayerDetails.setUlbGrade(achievement.getCityGrade());
        taxPayerDetails.setUlbName(achievement.getCityName());
        taxPayerDetails.setUlbCode(achievement.getCityCode());
        if (DASHBOARD_GROUPING_BILLCOLLECTORWISE.equalsIgnoreCase(collectionDetailsRequest.getType())) {
            taxPayerDetails.setBillCollector(achievement.getBillCollector());
            taxPayerDetails.setBillCollMobNo(achievement.getBillCollectorMobileNo());
            taxPayerDetails.setBillCollectorCode(achievement.getBillCollectorCode());
        } else if (DASHBOARD_GROUPING_REVENUEINSPECTORWISE.equalsIgnoreCase(collectionDetailsRequest.getType())) {
            taxPayerDetails.setRevenueInspector(achievement.getRevenueInspector());
            taxPayerDetails.setRevInspectorMobNo(achievement.getRevenueInspectorMobileNo());
            taxPayerDetails.setRevenueInspectorCode(achievement.getRevenueInspectorCode());
        } else if (DASHBOARD_GROUPING_REVENUEOFFICERWISE.equalsIgnoreCase(collectionDetailsRequest.getType())) {
            taxPayerDetails.setRevenueOfficer(achievement.getRevenueOfficer());
            taxPayerDetails.setRevOfficerMobNo(achievement.getRevenueOfficerMobileNo());
            taxPayerDetails.setRevenueOfficerCode(achievement.getRevenueOfficerCode());
        }
        taxPayerDetails.setTotalDmd(BigDecimal.valueOf(achievement.getTotalDemand()));
        taxPayerDetails.setCytdDmd(BigDecimal.valueOf(achievement.getCytdDemand()));
        taxPayerDetails.setCytdColl(BigDecimal.valueOf(achievement.getCytdColl()));
        taxPayerDetails.setCytdBalDmd(BigDecimal.valueOf(achievement.getCytdBalDemand()));
        taxPayerDetails.setAchievement(BigDecimal.valueOf(achievement.getAchievement()));
        taxPayerDetails.setLytdColl(BigDecimal.valueOf(achievement.getLytdColl()));
        taxPayerDetails.setLyVar(BigDecimal.valueOf(achievement.getLyVar()));
    }

    /**
     * Returns List of Top Ten Tax Performers
     *
     * @param collectionDetailsRequest
     * @return
     */
    public TaxPayerResponseDetails getTopTenTaxPerformers(final CollectionDetailsRequest collectionDetailsRequest,
            CFinancialYear currFinYear, boolean isForRanking) {

        final TaxPayerResponseDetails topTaxPerformers = new TaxPayerResponseDetails();
        List<TaxPayerDetails> taxProducers;
        List<TaxPayerDetails> taxAchievers;
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType()) && (collectionDetailsRequest.getType()
                .equalsIgnoreCase(PropertyTaxConstants.DASHBOARD_GROUPING_BILLCOLLECTORWISE)
                || DASHBOARD_GROUPING_REVENUEINSPECTORWISE.equalsIgnoreCase(collectionDetailsRequest.getType())
                || DASHBOARD_GROUPING_REVENUEOFFICERWISE.equalsIgnoreCase(collectionDetailsRequest.getType()))) {
            // Fetch the ward wise data for the filters
            final List<TaxPayerDetails> wardWiseTaxProducers = returnUlbWiseAggregationResults(collectionDetailsRequest,
                    PROPERTY_TAX_INDEX_NAME, false, TOTAL_COLLECTION, 250, true, currFinYear);
            final Map<String, TaxPayerDetails> wardWiseTaxPayersDetails = new HashMap<>();
            final Map<String, List<TaxPayerDetails>> billCollectorWiseMap = new LinkedHashMap<>();
            final List<TaxPayerDetails> taxPayerDetailsList = new ArrayList<>();
            final List<TaxPayerDetails> billCollectorWiseTaxPayerDetails = new ArrayList<>();
            // Get ward wise tax payers details
            prepareWardWiseTaxPayerDetails(wardWiseTaxProducers, wardWiseTaxPayersDetails);
            // Group the revenue ward details by bill collector
            prepareBillCollectorWiseMapData(collectionDetailsRequest, wardWiseTaxPayersDetails, billCollectorWiseMap,
                    taxPayerDetailsList);
            // Prepare Bill Collector wise tax payers details
            prepareTaxersInfoForBillCollectors(collectionDetailsRequest, billCollectorWiseMap,
                    billCollectorWiseTaxPayerDetails);
            taxProducers = getTaxPayersForBillCollector(false,
                    billCollectorWiseTaxPayerDetails, true, isForRanking);
            taxAchievers = getTaxPayersForBillCollector(false,
                    billCollectorWiseTaxPayerDetails, false, isForRanking);
        } else {
            taxProducers = returnUlbWiseAggregationResults(collectionDetailsRequest, PROPERTY_TAX_INDEX_NAME, false,
                    TOTAL_COLLECTION, 10, false, currFinYear);
            taxAchievers = returnUlbWiseAggregationResults(collectionDetailsRequest, PROPERTY_TAX_INDEX_NAME, false,
                    TOTAL_COLLECTION, 120, false, currFinYear);
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
    public TaxPayerResponseDetails getBottomTenTaxPerformers(final CollectionDetailsRequest collectionDetailsRequest,
            CFinancialYear currFinYear, boolean isForRanking) {
        final TaxPayerResponseDetails topTaxPerformers = new TaxPayerResponseDetails();
        List<TaxPayerDetails> taxProducers;
        List<TaxPayerDetails> taxAchievers;
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType()) && (collectionDetailsRequest.getType()
                .equalsIgnoreCase(PropertyTaxConstants.DASHBOARD_GROUPING_BILLCOLLECTORWISE)
                || DASHBOARD_GROUPING_REVENUEINSPECTORWISE.equalsIgnoreCase(collectionDetailsRequest.getType())
                || DASHBOARD_GROUPING_REVENUEOFFICERWISE.equalsIgnoreCase(collectionDetailsRequest.getType()))) {
            final List<TaxPayerDetails> wardWiseTaxProducers = returnUlbWiseAggregationResults(collectionDetailsRequest,
                    PROPERTY_TAX_INDEX_NAME, false, TOTAL_COLLECTION, 250, true, currFinYear);
            final Map<String, TaxPayerDetails> wardWiseTaxPayersDetails = new HashMap<>();
            final Map<String, List<TaxPayerDetails>> billCollectorWiseMap = new LinkedHashMap<>();
            final List<TaxPayerDetails> taxPayerDetailsList = new ArrayList<>();
            final List<TaxPayerDetails> billCollectorWiseTaxPayerDetails = new ArrayList<>();
            // Get ward wise tax payers details
            prepareWardWiseTaxPayerDetails(wardWiseTaxProducers, wardWiseTaxPayersDetails);
            // Group the revenue ward details by bill collector
            prepareBillCollectorWiseMapData(collectionDetailsRequest, wardWiseTaxPayersDetails, billCollectorWiseMap,
                    taxPayerDetailsList);
            // Prepare Bill Collector wise tax payers details
            prepareTaxersInfoForBillCollectors(collectionDetailsRequest, billCollectorWiseMap,
                    billCollectorWiseTaxPayerDetails);
            taxProducers = getTaxPayersForBillCollector(true, billCollectorWiseTaxPayerDetails, true, isForRanking);
            taxAchievers = getTaxPayersForBillCollector(true, billCollectorWiseTaxPayerDetails, false, isForRanking);
        } else {
            taxProducers = returnUlbWiseAggregationResults(collectionDetailsRequest, PROPERTY_TAX_INDEX_NAME, true,
                    TOTAL_COLLECTION, 10, false, currFinYear);
            taxAchievers = returnUlbWiseAggregationResults(collectionDetailsRequest, PROPERTY_TAX_INDEX_NAME, true,
                    TOTAL_COLLECTION, 120, false, currFinYear);
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
    public List<TaxPayerDetails> returnUlbWiseAggregationResults(final CollectionDetailsRequest collectionDetailsRequest,
            final String indexName, final Boolean order, final String orderingAggregationName, final int size,
            final boolean isBillCollectorWise, CFinancialYear currFinYear) {
        BigDecimal proportionalArrearDmd;
        BigDecimal proportionalCurrDmd;
        BigDecimal variation;
        Sum totalDemandAggregation;
        Sum totalCollectionAggregation;
        Sum arrearDmd;
        Sum currentDmd;
        Sum arrearInterestDmd;
        Sum currentInterestDmd;
        Sum arrearCollAmt;
        Sum currentCollAmt;
        Sum arrearIntColl;
        Sum currentIntColl;
        final List<TaxPayerDetails> taxPayers = new ArrayList<>();
        Map<String, BillCollectorIndex> wardWiseBillCollectors = new HashMap<>();
        final BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest)
                .filter(QueryBuilders.matchQuery(IS_ACTIVE, true))
                .filter(QueryBuilders.matchQuery(IS_EXEMPTED, false));

        // orderingAggregationName is the aggregation name by which we have to
        // order the results
        // IN this case can be one of "totaldemand" or "total_collection" or
        // "avg_achievement"
        String groupingField;
        if (StringUtils.isNotBlank(collectionDetailsRequest.getUlbCode())
                || StringUtils.isNotBlank(collectionDetailsRequest.getType())
                        && (DASHBOARD_GROUPING_WARDWISE.equalsIgnoreCase(collectionDetailsRequest.getType())
                                || DASHBOARD_GROUPING_BILLCOLLECTORWISE.equalsIgnoreCase(collectionDetailsRequest.getType())
                                || DASHBOARD_GROUPING_REVENUEINSPECTORWISE.equalsIgnoreCase(collectionDetailsRequest.getType())
                                || DASHBOARD_GROUPING_REVENUEOFFICERWISE.equalsIgnoreCase(collectionDetailsRequest.getType())))
            groupingField = REVENUE_WARD;
        else
            groupingField = CITY_NAME;

        AggregationBuilder aggregation;
        SearchQuery searchQueryColl;
        // Apply the ordering and max results size only if the type is not
        // billcollector
        if (!isBillCollectorWise) {
            aggregation = prepareAggregationForTaxPayers(size, groupingField)
                    .order(Terms.Order.aggregation(orderingAggregationName, order));
            searchQueryColl = new NativeSearchQueryBuilder().withIndices(indexName).withQuery(boolQuery)
                    .addAggregation(aggregation).build();
        } else {
            aggregation = prepareAggregationForTaxPayers(250, groupingField);
            searchQueryColl = new NativeSearchQueryBuilder().withIndices(indexName).withQuery(boolQuery)
                    .withPageable(new PageRequest(0, 250)).addAggregation(aggregation).build();
        }

        final Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, response -> response.getAggregations());

        final Date fromDate = DateUtils.startOfDay(currFinYear.getStartingDate());
        final Date toDate = org.apache.commons.lang3.time.DateUtils.addDays(new Date(), 1);
        final Date lastYearFromDate = org.apache.commons.lang3.time.DateUtils.addYears(fromDate, -1);
        final Date lastYearToDate = org.apache.commons.lang3.time.DateUtils.addYears(toDate, -1);

        // Fetch ward wise Bill Collector details for ward based grouping
        if (DASHBOARD_GROUPING_WARDWISE.equalsIgnoreCase(collectionDetailsRequest.getType()))
            wardWiseBillCollectors = collectionIndexElasticSearchService.getWardWiseBillCollectors(collectionDetailsRequest);

        try {
            TaxPayerDetails taxDetail;
            final StringTerms totalAmountAggr = collAggr.get(BY_AGGREGATION_FIELD);
            BigDecimal lastYearCollection;
            for (final Terms.Bucket entry : totalAmountAggr.getBuckets()) {
                taxDetail = new TaxPayerDetails();
                taxDetail.setRegionName(collectionDetailsRequest.getRegionName());
                taxDetail.setDistrictName(collectionDetailsRequest.getDistrictName());
                taxDetail.setUlbGrade(collectionDetailsRequest.getUlbGrade());
                final String fieldName = String.valueOf(entry.getKey());
                // If the grouping is based on ward, set the Bill Collector name and number
                if (groupingField.equals(REVENUE_WARD)) {
                    taxDetail.setWardName(fieldName);
                    if (DASHBOARD_GROUPING_WARDWISE.equalsIgnoreCase(collectionDetailsRequest.getType())
                            && !wardWiseBillCollectors.isEmpty()) {
                        taxDetail.setBillCollector(wardWiseBillCollectors.get(fieldName) == null ? StringUtils.EMPTY
                                : wardWiseBillCollectors.get(fieldName).getBillCollector());
                        taxDetail.setBillCollMobNo(wardWiseBillCollectors.get(fieldName) == null ? StringUtils.EMPTY
                                : wardWiseBillCollectors.get(fieldName).getBillCollectorMobileNo());
                        taxDetail.setBillCollectorCode(wardWiseBillCollectors.get(fieldName) == null ? StringUtils.EMPTY
                                : wardWiseBillCollectors.get(fieldName).getBillCollectorCode());
                    }
                } else
                    taxDetail.setUlbName(fieldName);

                // Proportional Demand = (totalDemand/12)*noOfmonths
                final int noOfMonths = DateUtils.noOfMonthsBetween(fromDate, toDate) + 1;
                totalDemandAggregation = entry.getAggregations().get(TOTALDEMAND);
                totalCollectionAggregation = entry.getAggregations().get(TOTAL_COLLECTION);
                arrearDmd = entry.getAggregations().get(ARREAR_DMD_STR);
                currentDmd = entry.getAggregations().get(CURR_DMD);
                arrearInterestDmd = entry.getAggregations().get(ARREAR_INTEREST_DMD);
                currentInterestDmd = entry.getAggregations().get(CURR_INTEREST_DMD);
                arrearCollAmt = entry.getAggregations().get(ARREAR_COLLECTION);
                currentCollAmt = entry.getAggregations().get(ANNUAL_COLLECTION);
                arrearIntColl = entry.getAggregations().get(ARREAR_INTEREST_COLLECTION);
                currentIntColl = entry.getAggregations().get(CURRENT_INTEREST_COLLECTION);

                final BigDecimal totalDemandValue = BigDecimal.valueOf(totalDemandAggregation.getValue()).setScale(0,
                        BigDecimal.ROUND_HALF_UP);
                final BigDecimal totalCollections = BigDecimal.valueOf(totalCollectionAggregation.getValue()).setScale(0,
                        BigDecimal.ROUND_HALF_UP);
                final BigDecimal proportionalDemand = totalDemandValue.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(noOfMonths));
                taxDetail.setTotalDmd(totalDemandValue);
                taxDetail.setCytdColl(totalCollections);
                taxDetail.setCytdDmd(proportionalDemand);
                if (proportionalDemand.compareTo(BigDecimal.ZERO) > 0)
                    taxDetail.setAchievement(
                            totalCollections.multiply(BIGDECIMAL_100).divide(proportionalDemand, 1, BigDecimal.ROUND_HALF_UP));
                taxDetail.setCytdBalDmd(proportionalDemand.subtract(totalCollections));
                if (StringUtils.isNotBlank(taxDetail.getUlbName()))
                    lastYearCollection = collectionIndexElasticSearchService
                            .getCollectionBetweenDates(collectionDetailsRequest, lastYearFromDate, lastYearToDate, fieldName, null,
                                    "totalAmount");
                else
                    lastYearCollection = collectionIndexElasticSearchService
                            .getCollectionBetweenDates(collectionDetailsRequest, lastYearFromDate, lastYearToDate, null, fieldName,
                                    "totalAmount");
                taxDetail.setLytdColl(lastYearCollection);
                taxDetail.setArrearColl(BigDecimal.valueOf(arrearCollAmt.getValue()).setScale(0,
                        BigDecimal.ROUND_HALF_UP));
                taxDetail.setCurrentColl(BigDecimal.valueOf(currentCollAmt.getValue()).setScale(0,
                        BigDecimal.ROUND_HALF_UP));
                taxDetail.setInterestColl(BigDecimal.valueOf(arrearIntColl.getValue()).setScale(0,
                        BigDecimal.ROUND_HALF_UP).add(BigDecimal.valueOf(currentIntColl.getValue()).setScale(0,
                                BigDecimal.ROUND_HALF_UP)));
                taxDetail.setArrearDemand(BigDecimal.valueOf(arrearDmd.getValue()).setScale(0,
                        BigDecimal.ROUND_HALF_UP));
                taxDetail.setCurrentDemand(BigDecimal.valueOf(currentDmd.getValue()).setScale(0,
                        BigDecimal.ROUND_HALF_UP));
                taxDetail.setArrearInterestDemand(BigDecimal.valueOf(arrearInterestDmd.getValue()).setScale(0,
                        BigDecimal.ROUND_HALF_UP));
                taxDetail.setCurrentInterestDemand(BigDecimal.valueOf(currentInterestDmd.getValue()).setScale(0,
                        BigDecimal.ROUND_HALF_UP));
                proportionalArrearDmd = taxDetail.getArrearDemand().divide(BigDecimal.valueOf(12),
                        BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(noOfMonths));
                proportionalCurrDmd = taxDetail.getCurrentDemand().divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(noOfMonths));
                taxDetail.setProportionalArrearDemand(proportionalArrearDmd);
                taxDetail.setProportionalCurrentDemand(proportionalCurrDmd);

                // variance = ((currentYearCollection -
                // lastYearCollection)*100)/lastYearCollection

                if (lastYearCollection.compareTo(BigDecimal.ZERO) == 0)
                    variation = BIGDECIMAL_100;
                else
                    variation = totalCollections.subtract(lastYearCollection)
                            .multiply(BIGDECIMAL_100).divide(lastYearCollection, 1,
                                    BigDecimal.ROUND_HALF_UP);
                taxDetail.setLyVar(variation);
                taxPayers.add(taxDetail);
            }
        } catch (Exception e) {
            LOGGER.error("Error while returning Ulb Wise Aggregation Results " + e.getMessage());
        }
        // If for Bill Collector, then fetch details for all wards, else limit
        // the results size
        if (isBillCollectorWise)
            return taxPayers;
        else
            return returnTopResults(taxPayers, size, order);
    }

    private TermsBuilder prepareAggregationForTaxPayers(final int size, final String groupingField) {
        return AggregationBuilders.terms(BY_AGGREGATION_FIELD).field(groupingField).size(size)
                .subAggregation(AggregationBuilders.sum(TOTALDEMAND).field(TOTAL_DEMAND))
                .subAggregation(AggregationBuilders.sum(TOTAL_COLLECTION).field("totalCollection"))
                .subAggregation(AggregationBuilders.sum(ARREAR_COLLECTION).field(ARREAR_COLLECTION))
                .subAggregation(AggregationBuilders.sum(ANNUAL_COLLECTION).field(ANNUAL_COLLECTION))
                .subAggregation(AggregationBuilders.sum(ARREAR_INTEREST_COLLECTION).field(ARREAR_INTEREST_COLLECTION))
                .subAggregation(AggregationBuilders.sum(CURRENT_INTEREST_COLLECTION).field(CURRENT_INTEREST_COLLECTION))
                .subAggregation(AggregationBuilders.sum(ARREAR_DMD_STR).field(ARREAR_DEMAND))
                .subAggregation(AggregationBuilders.sum(CURR_DMD).field("annualDemand"))
                .subAggregation(AggregationBuilders.sum(ARREAR_INTEREST_DMD).field("arrearInterestDemand"))
                .subAggregation(AggregationBuilders.sum(CURR_INTEREST_DMD).field("currentInterestDemand"));
    }

    private List<TaxPayerDetails> returnTopResults(final List<TaxPayerDetails> taxPayers, final int size, final Boolean order) {
        if (size > 10) {
            if (order)
                Collections.sort(taxPayers);
            else
                Collections.sort(taxPayers, Collections.reverseOrder());

            return taxPayers.subList(0, taxPayers.size() < 10 ? taxPayers.size() : 10);
        }
        return taxPayers;
    }

    /**
     * Returns top 100 tax defaulters
     *
     * @param propertyTaxDefaultersRequest
     * @return
     */
    public List<TaxDefaulters> getTopDefaulters(final PropertyTaxDefaultersRequest propertyTaxDefaultersRequest) {
        BoolQueryBuilder boolQuery = filterBasedOnRequest(propertyTaxDefaultersRequest);
        boolQuery = boolQuery.filter(QueryBuilders.matchQuery(IS_ACTIVE, true))
                .filter(QueryBuilders.matchQuery(IS_EXEMPTED, false));

        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(PROPERTY_TAX_INDEX_NAME)
                .withQuery(boolQuery).withSort(new FieldSortBuilder("totalBalance").order(SortOrder.DESC))
                .withPageable(new PageRequest(0, 100)).build();

        final Page<PropertyTaxIndex> propertyTaxRecords = elasticsearchTemplate.queryForPage(searchQuery,
                PropertyTaxIndex.class);

        final List<TaxDefaulters> taxDefaulters = new ArrayList<>();
        TaxDefaulters taxDefaulter;
        for (final PropertyTaxIndex property : propertyTaxRecords) {
            taxDefaulter = new TaxDefaulters();
            taxDefaulter.setOwnerName(property.getConsumerName());
            taxDefaulter.setPropertyType(property.getConsumerType());
            taxDefaulter.setUlbName(property.getCityName());
            taxDefaulter.setBalance(BigDecimal.valueOf(property.getTotalBalance()));
            taxDefaulter.setPeriod(StringUtils.isBlank(property.getDuePeriod()) ? StringUtils.EMPTY : property.getDuePeriod());
            taxDefaulters.add(taxDefaulter);
        }
        return taxDefaulters;
    }

    /**
     * This is used for top 100 defaulter's since ward level filtering is also present Query which filters documents from index
     * based on request
     *
     * @param propertyTaxDefaultersRequest
     * @return
     */
    private BoolQueryBuilder filterBasedOnRequest(final PropertyTaxDefaultersRequest propertyTaxDefaultersRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.rangeQuery(TOTAL_DEMAND).from(0).to(null));
        if (StringUtils.isNotBlank(propertyTaxDefaultersRequest.getRegionName()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(REGION_NAME, propertyTaxDefaultersRequest.getRegionName()));
        if (StringUtils.isNotBlank(propertyTaxDefaultersRequest.getDistrictName()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(DISTRICT_NAME, propertyTaxDefaultersRequest.getDistrictName()));
        if (StringUtils.isNotBlank(propertyTaxDefaultersRequest.getUlbCode()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(CITY_CODE, propertyTaxDefaultersRequest.getUlbCode()));
        if (StringUtils.isNotBlank(propertyTaxDefaultersRequest.getUlbGrade()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(CITY_GRADE, propertyTaxDefaultersRequest.getUlbGrade()));
        if (StringUtils.isNotBlank(propertyTaxDefaultersRequest.getWardName()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(REVENUE_WARD, propertyTaxDefaultersRequest.getWardName()));
        if (StringUtils.isNotBlank(propertyTaxDefaultersRequest.getType()))
            if (propertyTaxDefaultersRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_REGIONWISE)
                    && StringUtils.isNotBlank(propertyTaxDefaultersRequest.getRegionName()))
                boolQuery = boolQuery
                        .filter(QueryBuilders.matchQuery(REGION_NAME, propertyTaxDefaultersRequest.getRegionName()));
            else if (propertyTaxDefaultersRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_DISTRICTWISE)
                    && StringUtils.isNotBlank(propertyTaxDefaultersRequest.getDistrictName()))
                boolQuery = boolQuery.filter(
                        QueryBuilders.matchQuery(DISTRICT_NAME, propertyTaxDefaultersRequest.getDistrictName()));
            else if (propertyTaxDefaultersRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_CITYWISE)
                    && StringUtils.isNotBlank(propertyTaxDefaultersRequest.getUlbCode()))
                boolQuery = boolQuery
                        .filter(QueryBuilders.matchQuery(CITY_CODE, propertyTaxDefaultersRequest.getUlbCode()));
            else if (propertyTaxDefaultersRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_GRADEWISE)
                    && StringUtils.isNotBlank(propertyTaxDefaultersRequest.getUlbGrade()))
                boolQuery = boolQuery
                        .filter(QueryBuilders.matchQuery(CITY_GRADE, propertyTaxDefaultersRequest.getUlbGrade()));

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
    private BoolQueryBuilder prepareWhereClause(final CollectionDetailsRequest collectionDetailsRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.rangeQuery(TOTAL_DEMAND).from(0).to(null));
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
            boolQuery = collectionIndexElasticSearchService.queryForPropertyType(collectionDetailsRequest, boolQuery,
                    PROPERTY_TAX_INDEX_NAME);
        return boolQuery;
    }

    /**
     * Prepare ward wise tax payers details - Map of ward name and tax payers bean
     *
     * @param wardWiseTaxProducers
     * @param wardWiseTaxPayersDetails
     */
    private void prepareWardWiseTaxPayerDetails(final List<TaxPayerDetails> wardWiseTaxProducers,
            final Map<String, TaxPayerDetails> wardWiseTaxPayersDetails) {
        for (final TaxPayerDetails taxPayers : wardWiseTaxProducers)
            wardWiseTaxPayersDetails.put(taxPayers.getWardName(), taxPayers);
    }

    /**
     * Prepare a Map of Bill Collector names and the tax payers list for their respective wards
     *
     * @param collectionDetailsRequest
     * @param wardWiseTaxPayersDetails
     * @param billCollectorWiseMap
     * @param taxPayerDetailsList
     */
    private void prepareBillCollectorWiseMapData(final CollectionDetailsRequest collectionDetailsRequest,
            final Map<String, TaxPayerDetails> wardWiseTaxPayersDetails,
            final Map<String, List<TaxPayerDetails>> billCollectorWiseMap, List<TaxPayerDetails> taxPayerDetailsList) {
        try {
            String userNameAndNumber = StringUtils.EMPTY;
            final List<BillCollectorIndex> billCollectorsList = collectionIndexElasticSearchService
                    .getBillCollectorDetails(collectionDetailsRequest);
            if (wardWiseTaxPayersDetails != null && !wardWiseTaxPayersDetails.isEmpty())
                for (final BillCollectorIndex billCollIndex : billCollectorsList)
                    if (StringUtils.isNotBlank(billCollIndex.getRevenueWard())
                            && wardWiseTaxPayersDetails.get(billCollIndex.getRevenueWard()) != null) {
                        if (DASHBOARD_GROUPING_BILLCOLLECTORWISE.equalsIgnoreCase(collectionDetailsRequest.getType()))
                            userNameAndNumber = billCollIndex.getBillCollector().concat("~")
                                    .concat(StringUtils.isBlank(billCollIndex.getBillCollectorMobileNo()) ? StringUtils.EMPTY
                                            : billCollIndex.getBillCollectorMobileNo())
                                    .concat("~").concat(billCollIndex.getBillCollectorCode());
                        else if (DASHBOARD_GROUPING_REVENUEINSPECTORWISE.equalsIgnoreCase(collectionDetailsRequest.getType()))
                            userNameAndNumber = billCollIndex.getRevenueInspector().concat("~")
                                    .concat(StringUtils.isBlank(billCollIndex.getRevenueInspectorMobileNo()) ? StringUtils.EMPTY
                                            : billCollIndex.getRevenueInspectorMobileNo())
                                    .concat("~").concat(billCollIndex.getRevenueInspectorCode());
                        else if (DASHBOARD_GROUPING_REVENUEOFFICERWISE.equalsIgnoreCase(collectionDetailsRequest.getType()))
                            userNameAndNumber = billCollIndex.getRevenueOfficer().concat("~")
                                    .concat(StringUtils.isBlank(billCollIndex.getRevenueOfficerMobileNo()) ? StringUtils.EMPTY
                                            : billCollIndex.getRevenueOfficerMobileNo())
                                    .concat("~").concat(billCollIndex.getRevenueOfficerCode());
                        if (billCollectorWiseMap.isEmpty()) {
                            taxPayerDetailsList.add(wardWiseTaxPayersDetails.get(billCollIndex.getRevenueWard()));
                            billCollectorWiseMap.put(userNameAndNumber, taxPayerDetailsList);
                        } else if (!billCollectorWiseMap.containsKey(userNameAndNumber)) {
                            taxPayerDetailsList = new ArrayList<>();
                            taxPayerDetailsList.add(wardWiseTaxPayersDetails.get(billCollIndex.getRevenueWard()));
                            billCollectorWiseMap.put(userNameAndNumber, taxPayerDetailsList);
                        } else
                            billCollectorWiseMap.get(userNameAndNumber)
                                    .add(wardWiseTaxPayersDetails.get(billCollIndex.getRevenueWard()));
                    }
        } catch (Exception e) {
            LOGGER.error("Error while preparing Bill CollectorWise Map Data " + e.getMessage());
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
    private List<TaxPayerDetails> getTaxPayersForBillCollector(final boolean order,
            final List<TaxPayerDetails> billCollectorWiseTaxPayerDetails,
            final boolean isForProducers, boolean isForRanking) {
        final Map<BigDecimal, TaxPayerDetails> sortedTaxersMap = new HashMap<>();
        // For producers, prepare sorted list of totalCollection
        // For achievers, prepare sorted list of achievement
        if (isForProducers)
            for (final TaxPayerDetails payerDetails : billCollectorWiseTaxPayerDetails)
                sortedTaxersMap.put(payerDetails.getCytdColl(), payerDetails);
        else
            for (final TaxPayerDetails payerDetails : billCollectorWiseTaxPayerDetails)
                sortedTaxersMap.put(payerDetails.getAchievement(), payerDetails);

        final List<BigDecimal> sortedList = new ArrayList<>(sortedTaxersMap.keySet());
        // Decides whether API should return in ascending or descending order
        if (order)
            Collections.sort(sortedList);
        else
            Collections.sort(sortedList, Collections.reverseOrder());

        final List<TaxPayerDetails> taxersResult = new ArrayList<>();
        for (final BigDecimal amount : sortedList)
            taxersResult.add(sortedTaxersMap.get(amount));

        if (!isForRanking && taxersResult.size() > 10)
            return taxersResult.subList(0, taxersResult.size() < 10 ? taxersResult.size() : 10);
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
    private void prepareTaxersInfoForBillCollectors(final CollectionDetailsRequest collectionDetailsRequest,
            final Map<String, List<TaxPayerDetails>> billCollectorWiseMap,
            final List<TaxPayerDetails> billCollectorWiseTaxPayerDetails) {
        try {
            BigDecimal cytdColl;
            BigDecimal lytdColl;
            BigDecimal cytdDmd;
            BigDecimal totalDmd;
            TaxPayerDetails taxPayerDetails;
            String[] userNameNumberArr;
            for (final Entry<String, List<TaxPayerDetails>> entry : billCollectorWiseMap.entrySet()) {
                taxPayerDetails = new TaxPayerDetails();
                cytdColl = BigDecimal.ZERO;
                lytdColl = BigDecimal.ZERO;
                cytdDmd = BigDecimal.ZERO;
                totalDmd = BigDecimal.ZERO;
                userNameNumberArr = entry.getKey().split("~");
                for (final TaxPayerDetails taxPayer : entry.getValue()) {
                    totalDmd = totalDmd.add(taxPayer.getTotalDmd() == null ? BigDecimal.ZERO : taxPayer.getTotalDmd());
                    cytdColl = cytdColl.add(taxPayer.getCytdColl() == null ? BigDecimal.ZERO : taxPayer.getCytdColl());
                    cytdDmd = cytdDmd.add(taxPayer.getCytdDmd() == null ? BigDecimal.ZERO : taxPayer.getCytdDmd());
                    lytdColl = lytdColl.add(taxPayer.getLytdColl() == null ? BigDecimal.ZERO : taxPayer.getLytdColl());
                }

                if (DASHBOARD_GROUPING_BILLCOLLECTORWISE.equalsIgnoreCase(collectionDetailsRequest.getType())) {
                    taxPayerDetails.setBillCollector(userNameNumberArr.length > 0 ? userNameNumberArr[0] : StringUtils.EMPTY);
                    taxPayerDetails.setBillCollMobNo(userNameNumberArr.length > 1 ? userNameNumberArr[1] : StringUtils.EMPTY);
                    taxPayerDetails.setBillCollectorCode(userNameNumberArr.length > 2 ? userNameNumberArr[2] : StringUtils.EMPTY);
                } else if (DASHBOARD_GROUPING_REVENUEINSPECTORWISE.equalsIgnoreCase(collectionDetailsRequest.getType())) {
                    taxPayerDetails.setRevenueInspector(userNameNumberArr.length > 0 ? userNameNumberArr[0] : StringUtils.EMPTY);
                    taxPayerDetails.setRevInspectorMobNo(userNameNumberArr.length > 1 ? userNameNumberArr[1] : StringUtils.EMPTY);
                    taxPayerDetails.setRevenueInspectorCode(userNameNumberArr.length > 2 ? userNameNumberArr[2] : StringUtils.EMPTY);
                } else if (DASHBOARD_GROUPING_REVENUEOFFICERWISE.equalsIgnoreCase(collectionDetailsRequest.getType())) {
                    taxPayerDetails.setRevenueOfficer(userNameNumberArr.length > 0 ? userNameNumberArr[0] : StringUtils.EMPTY);
                    taxPayerDetails.setRevOfficerMobNo(userNameNumberArr.length > 1 ? userNameNumberArr[1] : StringUtils.EMPTY);
                    taxPayerDetails.setRevenueOfficerCode(userNameNumberArr.length > 2 ? userNameNumberArr[2] : StringUtils.EMPTY);
                }

                taxPayerDetails.setRegionName(collectionDetailsRequest.getRegionName());
                taxPayerDetails.setDistrictName(collectionDetailsRequest.getDistrictName());
                taxPayerDetails.setUlbGrade(collectionDetailsRequest.getUlbGrade());
                taxPayerDetails.setCytdColl(cytdColl);
                taxPayerDetails.setCytdDmd(cytdDmd);
                taxPayerDetails.setCytdBalDmd(cytdDmd.subtract(cytdColl));
                taxPayerDetails.setTotalDmd(totalDmd);
                taxPayerDetails.setLytdColl(lytdColl);
                if (cytdDmd.compareTo(BigDecimal.ZERO) > 0)
                    taxPayerDetails
                            .setAchievement(cytdColl.multiply(BIGDECIMAL_100).divide(cytdDmd, 1, BigDecimal.ROUND_HALF_UP));
                if (lytdColl.compareTo(BigDecimal.ZERO) == 0)
                    taxPayerDetails.setLyVar(BIGDECIMAL_100);
                else
                    taxPayerDetails.setLyVar(cytdColl.subtract(lytdColl).multiply(BIGDECIMAL_100)
                            .divide(lytdColl, 1, BigDecimal.ROUND_HALF_UP));

                billCollectorWiseTaxPayerDetails.add(taxPayerDetails);
            }
        } catch (Exception e) {
            LOGGER.error("Error while preparing Taxers Info For BillCollectors " + e.getMessage());
        }
    }

    /**
     * Prepares DCB Details
     * @param individualDmdDetails
     * @param demandDivisionMap
     */
    private void prepareDCBDetailsMap(final StringTerms individualDmdDetails,
            final Map<String, Map<String, BigDecimal>> demandDivisionMap) {
        Map<String, BigDecimal> individualDmdMap;
        Sum arrearDmd;
        Sum currentDmd;
        Sum arrearInterestDmd;
        Sum currentInterestDmd;
        Sum totalDmd;
        Sum adjustment;
        Sum arrearColl;
        Sum currentColl;
        Sum arrearInterestColl;
        Sum currentInterestColl;
        Sum advanceColl;
        Sum rebate;
        Sum totalColl;

        if (individualDmdDetails != null)
            for (final Terms.Bucket entry : individualDmdDetails.getBuckets()) {
                individualDmdMap = new HashMap<>();
                arrearDmd = entry.getAggregations().get(ARREAR_DMD_STR);
                currentDmd = entry.getAggregations().get(CURR_DMD);
                arrearInterestDmd = entry.getAggregations().get(ARREAR_INTEREST_DMD);
                currentInterestDmd = entry.getAggregations().get(CURR_INTEREST_DMD);
                totalDmd = entry.getAggregations().get("total_dmd");
                adjustment = entry.getAggregations().get("adjustment");
                arrearColl = entry.getAggregations().get("arrear_coll");
                currentColl = entry.getAggregations().get("curr_coll");
                arrearInterestColl = entry.getAggregations().get("arrear_interest_coll");
                currentInterestColl = entry.getAggregations().get("curr_interest_coll");
                advanceColl = entry.getAggregations().get("advance");
                rebate = entry.getAggregations().get(REBATE_STR);
                totalColl = entry.getAggregations().get("total_coll");

                individualDmdMap.put(ARREAR_DMD,
                        BigDecimal.valueOf(arrearDmd.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualDmdMap.put(CURRENT_DMD,
                        BigDecimal.valueOf(currentDmd.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualDmdMap.put(ARREAR_INT_DMD,
                        BigDecimal.valueOf(arrearInterestDmd.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualDmdMap.put(CURRENT_INT_DMD,
                        BigDecimal.valueOf(currentInterestDmd.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualDmdMap.put(TOTAL_DMD_STR,
                        BigDecimal.valueOf(totalDmd.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualDmdMap.put("adjustment",
                        BigDecimal.valueOf(adjustment.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualDmdMap.put(ARREAR_COLL_STR,
                        BigDecimal.valueOf(arrearColl.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualDmdMap.put(CURRENT_COLL_STR,
                        BigDecimal.valueOf(currentColl.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualDmdMap.put(ARREAR_INT_COLL,
                        BigDecimal.valueOf(arrearInterestColl.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualDmdMap.put(CURRENT_INT_COLL,
                        BigDecimal.valueOf(currentInterestColl.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualDmdMap.put(ADVANCE_COLL_STR,
                        BigDecimal.valueOf(advanceColl.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualDmdMap.put(REBATE_STR,
                        BigDecimal.valueOf(rebate.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                individualDmdMap.put(TOTAL_COLL_STR,
                        BigDecimal.valueOf(totalColl.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));

                demandDivisionMap.put(String.valueOf(entry.getKey()), individualDmdMap);
            }
    }

    /**
     * Provides citywise DCB details
     * @param collectionDetailsRequest
     * @return list
     */
    public List<DCBDetails> getDCBDetails(final CollectionDetailsRequest collectionDetailsRequest) {
        final List<DCBDetails> dcbDetailsList = new ArrayList<>();
        DCBDetails dCBDetails;
        String name;
        final Map<String, Map<String, BigDecimal>> demandDivisionMap = new HashMap<>();
        final Map<String, BigDecimal> assessmentsCountMap = collectionIndexElasticSearchService
                .getCollectionAndDemandCountResults(collectionDetailsRequest,
                        null, null, PROPERTY_TAX_INDEX_NAME, "consumerCode", CITY_NAME);
        final StringTerms individualDmdDetails = collectionIndexElasticSearchService.getIndividualDemands(
                collectionDetailsRequest,
                PROPERTY_TAX_INDEX_NAME, CITY_NAME, true);
        prepareDCBDetailsMap(individualDmdDetails, demandDivisionMap);
        for (final Map.Entry<String, Map<String, BigDecimal>> entry : demandDivisionMap.entrySet()) {
            dCBDetails = new DCBDetails();
            name = entry.getKey();
            dCBDetails.setBoundaryName(name);
            if (!assessmentsCountMap.isEmpty() && assessmentsCountMap.get(name) != null)
                dCBDetails.setTotalAssessments(
                        assessmentsCountMap.get(name) == null ? BigDecimal.ZERO : assessmentsCountMap.get(name));

            dCBDetails.setArrearDemand(demandDivisionMap.get(name).get(ARREAR_DMD) == null ? BigDecimal.ZERO
                    : demandDivisionMap.get(name).get(ARREAR_DMD));
            dCBDetails.setArrearPenalty(demandDivisionMap.get(name).get(ARREAR_INT_DMD) == null ? BigDecimal.ZERO
                    : demandDivisionMap.get(name).get(ARREAR_INT_DMD));
            dCBDetails.setCurrentDemand(demandDivisionMap.get(name).get(CURRENT_DMD) == null ? BigDecimal.ZERO
                    : demandDivisionMap.get(name).get(CURRENT_DMD));
            dCBDetails.setCurrentPenalty(demandDivisionMap.get(name).get(CURRENT_INT_DMD) == null ? BigDecimal.ZERO
                    : demandDivisionMap.get(name).get(CURRENT_INT_DMD));
            dCBDetails.setTotalDemand(demandDivisionMap.get(name).get(TOTAL_DMD_STR) == null ? BigDecimal.ZERO
                    : demandDivisionMap.get(name).get(TOTAL_DMD_STR));
            dCBDetails.setAdjustment(BigDecimal.ZERO);
            dCBDetails.setArrearColl(demandDivisionMap.get(name).get(ARREAR_COLL_STR) == null ? BigDecimal.ZERO
                    : demandDivisionMap.get(name).get(ARREAR_COLL_STR));
            dCBDetails.setCurrentColl(demandDivisionMap.get(name).get(CURRENT_COLL_STR) == null ? BigDecimal.ZERO
                    : demandDivisionMap.get(name).get(CURRENT_COLL_STR));
            dCBDetails.setArrearPenaltyColl(demandDivisionMap.get(name).get(ARREAR_INT_COLL) == null ? BigDecimal.ZERO
                    : demandDivisionMap.get(name).get(ARREAR_INT_COLL));
            dCBDetails.setCurrentPenaltyColl(demandDivisionMap.get(name).get(CURRENT_INT_COLL) == null ? BigDecimal.ZERO
                    : demandDivisionMap.get(name).get(CURRENT_INT_COLL));
            dCBDetails.setAdvanceColl(demandDivisionMap.get(name).get(ADVANCE_COLL_STR) == null ? BigDecimal.ZERO
                    : demandDivisionMap.get(name).get(ADVANCE_COLL_STR));
            dCBDetails.setRebate(demandDivisionMap.get(name).get(REBATE_STR) == null ? BigDecimal.ZERO
                    : demandDivisionMap.get(name).get(REBATE_STR));
            dCBDetails.setTotalColl(demandDivisionMap.get(name).get(TOTAL_COLL_STR) == null ? BigDecimal.ZERO
                    : demandDivisionMap.get(name).get(TOTAL_COLL_STR));
            if (dCBDetails.getTotalDemand().compareTo(BigDecimal.ZERO) > 0)
                dCBDetails.setPercentage(dCBDetails.getTotalColl().multiply(BIGDECIMAL_100).divide(dCBDetails.getTotalDemand(),
                        BigDecimal.ROUND_HALF_EVEN));

            dcbDetailsList.add(dCBDetails);
        }
        return dcbDetailsList;
    }

    public List<DemandVariance> prepareDemandVariationDetails(CollectionDetailsRequest collectionDetailsRequest) {
        DemandVariance demandDetails = new DemandVariance();
        final List<DemandVariance> demandVarianceList = new ArrayList<>();
        DemandVariationDetails demandVariationDetails;
        boolean exists;
        /**
         * To prepare details of primary demand
         */
        Aggregations aggr = getDemandVarianceAggregations(true, collectionDetailsRequest);
        StringTerms wards = aggr.get(BY_TYPE);

        for (final Terms.Bucket entry : wards.getBuckets()) {
            demandDetails = new DemandVariance();
            demandDetails.setName(entry.getKeyAsString());
            demandVariationDetails = new DemandVariationDetails();
            demandVariationDetails.setApplicationType(PRIMARY_DEMAND);
            demandVariationDetails.setTotalApplication(entry.getDocCount());
            populateDemandInfo(demandVariationDetails, entry);
            demandDetails.setPrimary(demandVariationDetails);
            demandVarianceList.add(demandDetails);
        }
        /**
         * To prepare details of demands for different application types
         */
        aggr = getDemandVarianceAggregations(false, collectionDetailsRequest);
        wards = aggr.get(BY_TYPE);
        for (final Terms.Bucket entry : wards.getBuckets()) {
            exists = false;
            for (final DemandVariance demandVariance : demandVarianceList)
                if (entry.getKeyAsString().equals(demandVariance.getName())) {
                    demandDetails = demandVariance;
                    exists = true;
                    break;
                }
            if (!exists) {
                demandDetails = new DemandVariance();
                demandDetails.setName(entry.getKeyAsString());
            }
            setDemandsForApplicationTypes(demandDetails, entry);

            if (!exists)
                demandVarianceList.add(demandDetails);
        }
        return demandVarianceList;
    }

    private void setDemandsForApplicationTypes(final DemandVariance demandDetails, final Terms.Bucket entry) {
        DemandVariationDetails demandVariationDetails;
        final StringTerms appType = entry.getAggregations().get(BY_APPLICATION);
        for (final Terms.Bucket appBucket : appType.getBuckets()) {
            demandVariationDetails = new DemandVariationDetails();
            if (PT_NEW_ASSMNT.equals(appBucket.getKeyAsString())) {
                populateDataForApplicationTypes(demandVariationDetails, appBucket);
                demandDetails.setNewAssessment(demandVariationDetails);
            } else if (PT_ALTER_ASSMNT.equals(appBucket.getKeyAsString())) {
                populateDataForApplicationTypes(demandVariationDetails, appBucket);
                demandDetails.setAdditionAlteration(demandVariationDetails);
            }
        }
    }

    private void populateDataForApplicationTypes(final DemandVariationDetails demandVariationDetails, final Terms.Bucket entry) {
        demandVariationDetails.setApplicationType(entry.getKeyAsString());
        demandVariationDetails.setTotalApplication(entry.getDocCount());
        populateDemandInfo(demandVariationDetails, entry);
    }

    private void populateDemandInfo(final DemandVariationDetails demandVariationDetails, final Terms.Bucket entry) {
        Sum sum = entry.getAggregations().get(ARREAR_DEMAND);
        Double demand = sum.getValue();
        demandVariationDetails.setArrearDemand(demand);
        sum = entry.getAggregations().get(CURRENT_DEMAND);
        demand = sum.getValue();
        demandVariationDetails.setCurrentDemand(demand);
        sum = entry.getAggregations().get(TOTAL_DEMAND);
        demand = sum.getValue();
        demandVariationDetails.setTotalDemand(demand);
    }

    public Aggregations getDemandVarianceAggregations(final Boolean isprimaryDemand,
            CollectionDetailsRequest collectionDetailsRequest) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
        AggregationBuilder aggregation;
        BoolQueryBuilder boolQuery = filterByTypeForAggregation(collectionDetailsRequest);
        final CFinancialYear financialYear = cFinancialYearService.getFinancialYearByDate(new Date());
        String aggregationField = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(collectionDetailsRequest.getType())) {
            aggregationField = collectionIndexElasticSearchService.getAggregrationField(collectionDetailsRequest);
        }
        if (!isprimaryDemand) {
            boolQuery = boolQuery
                    .filter(QueryBuilders.rangeQuery(DEMAND_DATE)
                            .gte(dateFormat.format(financialYear.getStartingDate()))
                            .lte(dateFormat.format(financialYear.getEndingDate())).includeUpper(false));
            aggregation = AggregationBuilders.terms(BY_TYPE).field(aggregationField)
                    .subAggregation(AggregationBuilders.terms(BY_APPLICATION).field(APPLICATION_TYPE)
                            .subAggregation(AggregationBuilders.sum(ARREAR_DEMAND).field(ARREAR_DEMAND))
                            .subAggregation(AggregationBuilders.sum(CURRENT_DEMAND).field(CURRENT_DEMAND))
                            .subAggregation(AggregationBuilders.sum(TOTAL_DEMAND).field(TOTAL_DEMAND)))
                    .size(250);
        } else {
            boolQuery = boolQuery
                    .filter(QueryBuilders.rangeQuery(DEMAND_DATE)
                            .lt(dateFormat.format(financialYear.getStartingDate())).includeUpper(false));
            aggregation = AggregationBuilders.terms(BY_TYPE).field(aggregationField)
                    .subAggregation(AggregationBuilders.sum(ARREAR_DEMAND).field(ARREAR_DEMAND))
                    .subAggregation(AggregationBuilders.sum(CURRENT_DEMAND).field(CURRENT_DEMAND))
                    .subAggregation(AggregationBuilders.sum(TOTAL_DEMAND).field(TOTAL_DEMAND))
                    .size(250);
        }

        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(DEMAND_VARIATION)
                .withQuery(boolQuery)
                .addAggregation(aggregation).build();
        return elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());
    }

    private BoolQueryBuilder filterByTypeForAggregation(CollectionDetailsRequest collectionDetailsRequest) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        if (DASHBOARD_GROUPING_WARDWISE.equalsIgnoreCase(collectionDetailsRequest.getType()))
            boolQuery = boolQuery.must(QueryBuilders.matchQuery(CITY_CODE, collectionDetailsRequest.getUlbCode()));

        return boolQuery;
    }
}