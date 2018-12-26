/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

package org.egov.wtms.service.es;

import org.apache.commons.collections.map.HashedMap;
import org.egov.infra.admin.master.entity.es.CityIndex;
import org.egov.infra.admin.master.service.es.CityIndexService;
import org.egov.wtms.entity.es.WaterChargeConnectionCount;
import org.egov.wtms.entity.es.WaterChargeConnectionCountMonthWise;
import org.egov.wtms.entity.es.WaterChargeSurveyDashboardRequest;
import org.egov.wtms.entity.es.WaterChargeSurveyDashboardResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CATEGORY_BPL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATER_CHARGES_SCHEME_INDEX;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service
public class WaterChargeSurveyDashboardService {

    private static final String CITY_NAME = "ulbName";
    private static final String CITY_CODE = "ulbCode";
    private static final String DISTRICT_NAME = "districtName";
    private static final String REGION_NAME = "regionName";
    private static final String FUNCTIONARY_NAME = "functionaryName";
    private static final String ELECTION_WARD = "electionWard";
    private static final String BPL_CONNECTION = "bplConnection";
    private static final String NONBPL_CONNECTION = "nonBplConnection";
    private static final String BPL_SANCTION_ISSUED = "bplSanctionOrder";
    private static final String NONBPL_SANCTION_ISSUED = "nonBplSanctionOrder";
    private static final String BPL_EXECUTED = "bplExecution";
    private static final String NON_BPL_EXECUTED = "nonBplExecution";
    private static final String APPLICATION_CREATED_DATE = "applicationCreatedDate";
    private static final String AGGREGATION_WISE = "aggregationWise";
    private static final String SANCTION_ISSUED = "workOrderIssued";
    private static final String CONNECTION_CATEGORY = "connectionCategory";
    private static final String CONNECTION_EXECUTED = "connectionExecuted";
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String YES = "YES";
    private static final String WORKORDER_DATE = "workOrderDate";
    private static final String EXECUTION_DATE = "executionDate";
    private static final String APPLICATION_DATE = "applicationDate";
    private static final String INACTIVE_CONNECTION="cancelledConnection";

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private CityIndexService cityIndexService;

    public List<WaterChargeSurveyDashboardResponse> getAggregatedSurveyDetails(WaterChargeSurveyDashboardRequest surveyDashboardRequest) {
        String aggregationField = REGION_NAME;
        if (isNotBlank(surveyDashboardRequest.getAggregationLevel()))
            aggregationField = surveyDashboardRequest.getAggregationLevel();

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms(AGGREGATION_WISE).field(aggregationField).size(100)
                .subAggregation(AggregationBuilders.topHits("cityDetails").addField(CITY_NAME).addField(CITY_CODE))
                .subAggregation(AggregationBuilders.sum(BPL_CONNECTION).field(BPL_CONNECTION))
                .subAggregation(AggregationBuilders.sum(NONBPL_CONNECTION).field(NONBPL_CONNECTION))
                .subAggregation(AggregationBuilders.sum(BPL_SANCTION_ISSUED).field(BPL_SANCTION_ISSUED))
                .subAggregation(AggregationBuilders.sum(NONBPL_SANCTION_ISSUED).field(NONBPL_SANCTION_ISSUED))
                .subAggregation(AggregationBuilders.sum(BPL_EXECUTED).field(BPL_EXECUTED))
                .subAggregation(AggregationBuilders.sum(NON_BPL_EXECUTED).field(NON_BPL_EXECUTED))
                .subAggregation(AggregationBuilders.sum(INACTIVE_CONNECTION).field(INACTIVE_CONNECTION));


        SearchResponse response = elasticsearchTemplate.getClient().prepareSearch(WATER_CHARGES_SCHEME_INDEX).setSize(0)
                .addAggregation(aggregationBuilder)
                .setQuery(prepareQuery(surveyDashboardRequest))
                .execute().actionGet();

        return setSurveyResponse(aggregationField, response, surveyDashboardRequest);
    }


    private BoolQueryBuilder prepareQuery(WaterChargeSurveyDashboardRequest surveyDashboardRequest) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        if (isNotBlank(surveyDashboardRequest.getRegionName()))
            boolQuery = boolQuery.filter(termQuery(REGION_NAME, surveyDashboardRequest.getRegionName()));
        if (isNotBlank(surveyDashboardRequest.getDistrictName()))
            boolQuery = boolQuery.filter(matchQuery(DISTRICT_NAME, surveyDashboardRequest.getDistrictName()));
        if (isNotBlank(surveyDashboardRequest.getUlbName()))
            boolQuery = boolQuery.filter(matchQuery(CITY_NAME, surveyDashboardRequest.getUlbName()));
        if (isNotBlank(surveyDashboardRequest.getUlbCode()))
            boolQuery = boolQuery.filter(matchQuery(CITY_CODE, surveyDashboardRequest.getUlbCode()));
        if (isNotBlank(surveyDashboardRequest.getWardName()))
            boolQuery = boolQuery.filter(termQuery(ELECTION_WARD, surveyDashboardRequest.getWardName()));
        if (isNotBlank(surveyDashboardRequest.getFunctionaryName()))
            boolQuery = boolQuery.filter(matchQuery(FUNCTIONARY_NAME, surveyDashboardRequest.getFunctionaryName()));
        if (isNotBlank(surveyDashboardRequest.getFromDate()))
            boolQuery = boolQuery.filter(rangeQuery(APPLICATION_CREATED_DATE).gte(surveyDashboardRequest.getFromDate()));
        if (isNotBlank(surveyDashboardRequest.getToDate()))
            boolQuery = boolQuery.filter(rangeQuery(APPLICATION_CREATED_DATE).lte(surveyDashboardRequest.getToDate()));

        return boolQuery;
    }

    private List<WaterChargeSurveyDashboardResponse> setSurveyResponse(String aggregationField, SearchResponse response,
                                                                       WaterChargeSurveyDashboardRequest request) {

        Terms ulbTerms = response.getAggregations().get(AGGREGATION_WISE);
        List<CityIndex> city = new ArrayList<>(0);
        if (isNotBlank(request.getUlbCode()) && (ELECTION_WARD.equals(aggregationField) || FUNCTIONARY_NAME.equals(aggregationField))) {
            city.add(cityIndexService.findByCitycode(request.getUlbCode()));
        } else {
            Iterable<CityIndex> cities = cityIndexService.findAll();
            cities.forEach(city::add);
        }

        List<WaterChargeSurveyDashboardResponse> responseList = new ArrayList<>();
        for (Bucket bucket : ulbTerms.getBuckets()) {
            WaterChargeSurveyDashboardResponse surveyResponse = new WaterChargeSurveyDashboardResponse();
            getAggregationNames(aggregationField, surveyResponse, city, bucket);
            surveyResponse.setApplicaitonReceived(getApplicationReceivedCount(bucket));
            surveyResponse.setSanctionOrderIssued(getSanctionOrderIssueCount(bucket));
            surveyResponse.setConnectionExecuted(getConnectionExecutedCount(bucket));
            double applicationReceived = surveyResponse.getApplicaitonReceived().getTotalConnection();
            double totalSanctionIssued = surveyResponse.getSanctionOrderIssued().getTotalConnection();
            Sum inActiveConnection = bucket.getAggregations().get(INACTIVE_CONNECTION);
            surveyResponse.setPendingSanction(applicationReceived - (totalSanctionIssued + inActiveConnection.getValue()));
            surveyResponse.setPendingExecution(totalSanctionIssued - surveyResponse.getConnectionExecuted().getTotalConnection());
            responseList.add(surveyResponse);
        }
        return responseList;
    }


    private void getAggregationNames(String aggregationField, WaterChargeSurveyDashboardResponse surveyResponse,
                                     List<CityIndex> cities, Bucket bucket) {

        String name = bucket.getKeyAsString();
        if (REGION_NAME.equals(aggregationField))
            surveyResponse.setRegionName(name);
        else if (DISTRICT_NAME.equals(aggregationField))
            cities.stream().forEach(cityIndex -> {
                if (cityIndex.getDistrictname().equals(name)) {
                    surveyResponse.setRegionName(cityIndex.getRegionname());
                    surveyResponse.setDistrictName(cityIndex.getDistrictname());
                }
            });
        else if (CITY_CODE.equals(aggregationField) || CITY_NAME.equals(aggregationField)) {
            cities.stream().forEach(cityIndex -> {
                if (cityIndex.getCitycode().equals(name)) {
                    setCityDetails(surveyResponse, cityIndex);
                }
            });
        } else if (ELECTION_WARD.equals(aggregationField)) {
            surveyResponse.setWardName(name);
            cities.stream().forEach(cityIndex ->
                    setCityDetails(surveyResponse, cityIndex));
        } else if (FUNCTIONARY_NAME.equals(aggregationField)) {
            surveyResponse.setFunctionaryName(name);
            cities.stream().forEach(cityIndex ->
                    setCityDetails(surveyResponse, cityIndex));
        }
    }

    private void setCityDetails(WaterChargeSurveyDashboardResponse surveyResponse, CityIndex cityIndex) {
        surveyResponse.setUlbCode(cityIndex.getCitycode());
        surveyResponse.setUlbGrade(cityIndex.getCitygrade());
        surveyResponse.setUlbName(cityIndex.getName());
        surveyResponse.setRegionName(cityIndex.getRegionname());
        surveyResponse.setDistrictName(cityIndex.getDistrictname());
    }

    private WaterChargeConnectionCount getApplicationReceivedCount(Bucket bucket) {
        return getWaterChargeConnectionCount(bucket, BPL_CONNECTION, NONBPL_CONNECTION);
    }

    private WaterChargeConnectionCount getSanctionOrderIssueCount(Bucket bucket) {
        return getWaterChargeConnectionCount(bucket, BPL_SANCTION_ISSUED, NONBPL_SANCTION_ISSUED);
    }

    private WaterChargeConnectionCount getConnectionExecutedCount(Bucket bucket) {
        return getWaterChargeConnectionCount(bucket, BPL_EXECUTED, NON_BPL_EXECUTED);
    }

    private WaterChargeConnectionCount getWaterChargeConnectionCount(Bucket bucket, String bplConnection, String nonBPLConnection) {
        WaterChargeConnectionCount connectionCount = new WaterChargeConnectionCount();
        Sum bplCount = bucket.getAggregations().get(bplConnection);
        Sum nonBPLCount = bucket.getAggregations().get(nonBPLConnection);
        connectionCount.setBplConnection(bplCount.getValue());
        connectionCount.setNonBPLConnection(nonBPLCount.getValue());
        connectionCount.setTotalConnection(bplCount.getValue() + nonBPLCount.getValue());
        return connectionCount;
    }

    public List<WaterChargeSurveyDashboardResponse> getApplicationDetails(WaterChargeSurveyDashboardRequest request) {
        String aggregationField = ELECTION_WARD;
        if (isNotBlank(request.getAggregationLevel()))
            aggregationField = request.getAggregationLevel();

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms(AGGREGATION_WISE).field(aggregationField).size(100);
        SearchResponse response = elasticsearchTemplate.getClient().prepareSearch(WATER_CHARGES_SCHEME_INDEX).setSize(0)
                .setQuery(prepareQuery(request).filter(prepareQueryGetApplication(request)))
                .addAggregation(aggregationBuilder).setSize(5000)
                .execute().actionGet();

        List<WaterChargeSurveyDashboardResponse> responseList = new ArrayList<>();
        SearchHits hitFields = response.getHits();
        for (SearchHit hit : hitFields) {
            Map<String, Object> sourceAsMap = hit.sourceAsMap();
            WaterChargeSurveyDashboardResponse surveyResponse = new WaterChargeSurveyDashboardResponse();
            surveyResponse.setApplicationNumber(sourceAsMap.get("applicationNumber").toString());
            surveyResponse.setApplicationDate(
                    new DateTime(sourceAsMap.get(APPLICATION_CREATED_DATE).toString()).toDate());
            surveyResponse.setApplicantName(sourceAsMap.get("applicantName").toString());
            surveyResponse.setAddress(sourceAsMap.get("address").toString());
            surveyResponse.setConnectionStatus(sourceAsMap.get("connectionStatus").toString());
            surveyResponse.setApplicationURL(sourceAsMap.get("applicationUrl").toString());
            responseList.add(surveyResponse);
        }
        return responseList;

    }

    private QueryBuilder prepareQueryGetApplication(WaterChargeSurveyDashboardRequest request) {
        BoolQueryBuilder boolQuery;
        if (YES.equals(request.getPendingExecution()) || YES.equals(request.getPendingSanction())) {
            boolQuery = YES.equals(request.getPendingSanction())
                    ? boolQuery().must(matchQuery(SANCTION_ISSUED, FALSE))
                    : boolQuery().must(matchQuery(SANCTION_ISSUED, TRUE)).must(matchQuery(CONNECTION_EXECUTED, FALSE));
            if (YES.equals(request.getPendingSanction()))
                boolQuery = boolQuery.mustNot(matchQuery("connectionStatus", "INACTIVE"));
        } else {
            boolQuery = YES.equals(request.getBpl())
                    ? boolQuery().must(matchQuery(CONNECTION_CATEGORY, CATEGORY_BPL))
                    : boolQuery().mustNot(matchQuery(CONNECTION_CATEGORY, CATEGORY_BPL));

            if (YES.equals(request.getSanctionIssued())) {
                boolQuery = boolQuery.filter(matchQuery(SANCTION_ISSUED, TRUE));
            } else if (YES.equals(request.getExecutionIssued())) {
                boolQuery = boolQuery.filter(matchQuery(CONNECTION_EXECUTED, TRUE));
            }
        }
        return boolQuery;
    }

    public Map<String, Map<String, WaterChargeConnectionCountMonthWise>> getMonthWiseCount(WaterChargeSurveyDashboardRequest request) {

        SearchResponse count = elasticsearchTemplate.getClient().prepareSearch(WATER_CHARGES_SCHEME_INDEX).setSize(0)
                .setQuery(prepareQuery(request))
                .execute().actionGet();

        SearchResponse response = elasticsearchTemplate.getClient().prepareSearch(WATER_CHARGES_SCHEME_INDEX).setSize(0)
                .setQuery(prepareQuery(request))
                .addAggregation(AggregationBuilders.terms(AGGREGATION_WISE).field(APPLICATION_DATE).size(100))
                .setSize((int) count.getHits().totalHits())
                .execute().actionGet();

        Map<String, Map<String, WaterChargeConnectionCountMonthWise>> countMonthWise = new HashMap<>();

        SearchHits hitFields = response.getHits();
        for (SearchHit hit : hitFields) {
            String year;
            String month;
            Map<String, Object> sourceAsMap = hit.sourceAsMap();
            year = getYearOfGivenDate(sourceAsMap.get(APPLICATION_DATE).toString());
            month = getMonthOfGivenDate(sourceAsMap.get(APPLICATION_DATE).toString());
            setMonthYearWiseCount(countMonthWise, year, month, APPLICATION_DATE);

            String workOrderDate = sourceAsMap.get(WORKORDER_DATE) == null ? EMPTY : sourceAsMap.get(WORKORDER_DATE).toString();
            if (isNotBlank(workOrderDate)) {
                year = getYearOfGivenDate(workOrderDate);
                month = getMonthOfGivenDate(workOrderDate);
                setMonthYearWiseCount(countMonthWise, year, month, WORKORDER_DATE);
            }

            String executionDate = sourceAsMap.get(EXECUTION_DATE) == null ? EMPTY : sourceAsMap.get(EXECUTION_DATE).toString();
            if (isNotBlank(executionDate)) {
                year = getYearOfGivenDate(executionDate);
                month = getMonthOfGivenDate(executionDate);
                setMonthYearWiseCount(countMonthWise, year, month, EXECUTION_DATE);
            }
        }
        return countMonthWise;
    }

    private void setMonthYearWiseCount(Map<String, Map<String, WaterChargeConnectionCountMonthWise>> countMonthWise,
                                       String year, String month, String countCondition) {

        if (!countMonthWise.isEmpty() && countMonthWise.containsKey(year)) {
            if (!countMonthWise.get(year).isEmpty() && countMonthWise.get(year).containsKey(month)) {
                WaterChargeConnectionCountMonthWise connectionCountMonthWise = countMonthWise.get(year).get(month);
                getApplicationCount(countCondition, connectionCountMonthWise);
            } else {
                WaterChargeConnectionCountMonthWise connectionCountMonthWise = new WaterChargeConnectionCountMonthWise();
                getApplicationCount(countCondition, connectionCountMonthWise);
                countMonthWise.get(year).put(month, connectionCountMonthWise);
            }
        } else {
            Map<String, WaterChargeConnectionCountMonthWise> monthWise = new HashedMap();
            WaterChargeConnectionCountMonthWise connectionCountMonthWise = new WaterChargeConnectionCountMonthWise();
            getApplicationCount(countCondition, connectionCountMonthWise);
            monthWise.put(month, connectionCountMonthWise);
            countMonthWise.put(year, monthWise);
        }
    }

    private void getApplicationCount(String countCondition, WaterChargeConnectionCountMonthWise countMonthWise) {
        if (countCondition.equals(WORKORDER_DATE))
            countMonthWise.setSanctionIssued(countMonthWise.getSanctionIssued() + 1);
        else if (countCondition.equals(EXECUTION_DATE))
            countMonthWise.setConnectionExecuted(countMonthWise.getConnectionExecuted() + 1);
        else
            countMonthWise.setApplicationReceived(countMonthWise.getApplicationReceived() + 1);
    }

    private String getMonthOfGivenDate(String date) {
        return String.valueOf(new DateTime(date).getMonthOfYear());
    }

    private String getYearOfGivenDate(String date) {
        return String.valueOf(new DateTime(date).getYear());
    }

    public Map<String, String> applicationCountSummary(WaterChargeSurveyDashboardRequest request) {

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms(AGGREGATION_WISE).field(REGION_NAME).size(100);

        SearchResponse applicationCount = elasticsearchTemplate.getClient().prepareSearch(WATER_CHARGES_SCHEME_INDEX).setSize(0)
                .addAggregation(aggregationBuilder)
                .setQuery(prepareQuery(request, APPLICATION_CREATED_DATE))
                .execute().actionGet();

        SearchResponse sanctionCount = elasticsearchTemplate.getClient().prepareSearch(WATER_CHARGES_SCHEME_INDEX).setSize(0)
                .addAggregation(aggregationBuilder)
                .setQuery(prepareQuery(request, WORKORDER_DATE))
                .execute().actionGet();

        SearchResponse executionCount = elasticsearchTemplate.getClient().prepareSearch(WATER_CHARGES_SCHEME_INDEX).setSize(0)
                .addAggregation(aggregationBuilder)
                .setQuery(prepareQuery(request, EXECUTION_DATE))
                .execute().actionGet();

        Map<String, String> connectionSummary = new HashMap<>();
        connectionSummary.put("yesterdayApplicationCount", String.valueOf(applicationCount.getHits().totalHits()));
        connectionSummary.put("yesterdaySanctionCount", String.valueOf(sanctionCount.getHits().totalHits()));
        connectionSummary.put("yesterdayExecutionCount", String.valueOf(executionCount.getHits().totalHits()));
        return connectionSummary;
    }

    private BoolQueryBuilder prepareQuery(WaterChargeSurveyDashboardRequest request, String date) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        if (isNotBlank(request.getRegionName()))
            boolQuery = boolQuery.filter(termQuery(REGION_NAME, request.getRegionName()));
        if (isNotBlank(request.getDistrictName()))
            boolQuery = boolQuery.filter(matchQuery(DISTRICT_NAME, request.getDistrictName()));
        if (isNotBlank(request.getUlbCode()))
            boolQuery = boolQuery.filter(matchQuery(CITY_CODE, request.getUlbCode()));
        if (isNotBlank(request.getFromDate()))
            boolQuery = boolQuery.filter(rangeQuery(date).gte(request.getFromDate()));
        if (isNotBlank(request.getFromDate()))
            boolQuery = boolQuery.filter(rangeQuery(date).lt(request.getToDate()));
        return boolQuery;
    }
}