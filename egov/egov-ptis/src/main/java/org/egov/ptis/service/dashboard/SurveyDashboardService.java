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
package org.egov.ptis.service.dashboard;

import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_DISTRICTWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_REGIONWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_SERVICEWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_ULBWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_WARDWISE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.admin.master.entity.es.CityIndex;
import org.egov.infra.admin.master.service.es.CityIndexService;
import org.egov.ptis.bean.dashboard.CollectionDetailsRequest;
import org.egov.ptis.bean.dashboard.SurveyDashboardResponse;
import org.egov.ptis.bean.dashboard.SurveyRequest;
import org.egov.ptis.bean.dashboard.SurveyResponse;
import org.egov.ptis.domain.entity.es.BillCollectorIndex;
import org.egov.ptis.service.es.CollectionIndexElasticSearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

@Service
public class SurveyDashboardService {
    private static final String AGGREGATIONWISE = "aggregationwise";
    private static final String SENT_TO_THIRD_PARTY = "sentToThirdParty";
    private static final String PROPERTYSURVEYDETAILS_INDEX = "propertysurveydetails";
    private static final String CITY_NAME = "cityName";
    private static final String DISTRICT_NAME = "districtName";
    private static final String REGION_NAME = "regionName";
    private static final String REVENUE_WARD = "revenueWard";
    private static final String APPLICATION_TYPE = "applicationType";

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private CollectionIndexElasticSearchService collectionIndexElasticSearchService;
    
    @Autowired
    private CityIndexService cityIndexService;

    public List<SurveyDashboardResponse> getGisApplicationDetails(final SurveyRequest surveyDashboardRequest) {
        List<SurveyDashboardResponse> surveyList = new ArrayList<>();
        @SuppressWarnings("rawtypes")
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("by_ward").field("revenueWard").size(100);
        SearchResponse response = elasticsearchTemplate.getClient()
                .prepareSearch(PROPERTYSURVEYDETAILS_INDEX)
                .setQuery(prepareQuery(surveyDashboardRequest))
                .addAggregation(aggregationBuilder).setSize(10000)
                .execute().actionGet();
        getPropertySurveyList(surveyList, response);
        return surveyList;
    }

    private void getPropertySurveyList(List<SurveyDashboardResponse> surveyList, SearchResponse response) {
        SurveyDashboardResponse surveyResponse;
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            surveyResponse = new SurveyDashboardResponse();
            Map<String, Object> sourceAsMap = hit.sourceAsMap();
            String applicationNo = sourceAsMap.get("applicationNo").toString();
            String ptUrl = "/ptis/view/viewProperty-viewForm.action?";
            String applicationType = sourceAsMap.get("applicationType").toString();
            String appViewURL = ptUrl.concat("applicationNo=").concat(applicationNo).concat("&applicationType")
                    .concat(applicationType);
            surveyResponse.setApplicationNo(applicationNo);
            surveyResponse.setAssessmentNo(
                    sourceAsMap.get("assessmentNo") == null ? "N/A" : sourceAsMap.get("assessmentNo").toString());
            surveyResponse.setDoorNo(sourceAsMap.get("doorNo").toString());
            surveyResponse.setRevenueWard(sourceAsMap.get("revenueWard").toString());
            surveyResponse.setRevenueBlock(sourceAsMap.get("blockName").toString());
            surveyResponse.setLocality(sourceAsMap.get("localityName").toString());
            surveyResponse.setElectionWard(sourceAsMap.get("electionWard").toString());
            surveyResponse.setSystemTax((double) sourceAsMap.get("systemTax"));
            surveyResponse.setGisTax((double) sourceAsMap.get("gisTax"));
            surveyResponse.setApplicationTax((double) sourceAsMap.get("applicationTax"));
            surveyResponse.setAppStatus(sourceAsMap.get("applicationStatus").toString());
            surveyResponse.setAssistantName(
                    sourceAsMap.get("assistantName") == null ? "N/A" : sourceAsMap.get("assistantName").toString());
            surveyResponse.setRiName(sourceAsMap.get("riName") == null ? "N/A" : sourceAsMap.get("riName").toString());
            surveyResponse.setIsreffered((boolean) sourceAsMap.get(SENT_TO_THIRD_PARTY));
            surveyResponse.setIsVarified((boolean) sourceAsMap.get("thirdPrtyFlag"));
            surveyResponse.setAppViewURL(appViewURL);
            surveyResponse.setUlbCode(sourceAsMap.get("cityCode").toString());
            surveyList.add(surveyResponse);
        }
    }

    private BoolQueryBuilder prepareQuery(final SurveyRequest surveyRequest) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        if (StringUtils.isNotBlank(surveyRequest.getRegionName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("regionName", surveyRequest.getRegionName()));
        if (StringUtils.isNotBlank(surveyRequest.getDistrictName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("districtName", surveyRequest.getDistrictName()));
        if (StringUtils.isNotBlank(surveyRequest.getUlbName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("cityName", surveyRequest.getUlbName()));
        if (StringUtils.isNotBlank(surveyRequest.getServiceName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("applicationType", surveyRequest.getServiceName()));
        if (surveyRequest.getThirdParty() != null) {
            if (surveyRequest.getThirdParty() == 'Y')
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery(SENT_TO_THIRD_PARTY, true));
            else
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery(SENT_TO_THIRD_PARTY, false));
        }
        if (StringUtils.isNotBlank(surveyRequest.getUlbCode())) {
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("cityCode", surveyRequest.getUlbCode()));
        }
        if (StringUtils.isNotBlank(surveyRequest.getWardName())) {
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("wardName", surveyRequest.getWardName()));
        }
        if (StringUtils.isNotBlank(surveyRequest.getLocalityName())) {
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("localityName", surveyRequest.getLocalityName()));
        }
        return boolQuery;
    }

    public static AggregationBuilder getCountWithGrouping(final String aggregationName, final String fieldName, final int size) {
        return AggregationBuilders.terms(aggregationName).field(fieldName).size(size);
    }

    public String getAggregrationField(SurveyRequest surveyRequest) {
        String aggregationField = REGION_NAME;
        if (DASHBOARD_GROUPING_REGIONWISE.equalsIgnoreCase(surveyRequest.getAggregationLevel()))
            aggregationField = REGION_NAME;
        else if (DASHBOARD_GROUPING_DISTRICTWISE.equalsIgnoreCase(surveyRequest.getAggregationLevel()))
            aggregationField = DISTRICT_NAME;
        else if (DASHBOARD_GROUPING_ULBWISE.equalsIgnoreCase(surveyRequest.getAggregationLevel()))
            aggregationField = CITY_NAME;
        else if (DASHBOARD_GROUPING_WARDWISE.equalsIgnoreCase(surveyRequest.getAggregationLevel()))
            aggregationField = REVENUE_WARD;
        else if (DASHBOARD_GROUPING_SERVICEWISE.equalsIgnoreCase(surveyRequest.getAggregationLevel()))
            aggregationField = APPLICATION_TYPE;
        return aggregationField;
    }

    public List<SurveyResponse> getAggregatedSurveyDetails(SurveyRequest surveyRequest) {
        List<SurveyResponse> responseList;
        String aggregationField = REGION_NAME;
        if (StringUtils.isNotBlank(surveyRequest.getAggregationLevel()))
            aggregationField = getAggregrationField(surveyRequest);

        AggregationBuilder aggregationBuilder = AggregationBuilders.terms(AGGREGATIONWISE).field(aggregationField).size(100);
        SearchResponse response = elasticsearchTemplate.getClient().prepareSearch(PROPERTYSURVEYDETAILS_INDEX).setSize(0)
                .setQuery(prepareQuery(surveyRequest))
                .addAggregation(aggregationBuilder
                        .subAggregation(getCountWithGrouping("verificationDone", "thirdPrtyFlag", 2))
                        .subAggregation(getCountWithGrouping("sentForReference", SENT_TO_THIRD_PARTY, 2))
                        .subAggregation(AggregationBuilders.sum("gisTotal").field("gisTax"))
                        .subAggregation(AggregationBuilders.sum("systemTotal").field("systemTax"))
                        .subAggregation(AggregationBuilders.sum("approvedTotal").field("approvedTax")))
                .execute().actionGet();

        SearchResponse completedResponse = elasticsearchTemplate.getClient().prepareSearch(PROPERTYSURVEYDETAILS_INDEX).setSize(0)
                .setQuery(prepareQuery(surveyRequest).filter(QueryBuilders.matchQuery("applicationStatus", "Closed")))
                .addAggregation(aggregationBuilder).execute().actionGet();
        Terms completedAggr = completedResponse.getAggregations().get(AGGREGATIONWISE);
        Map<String, Long> completedApplicationsMap = new HashMap<>();
        Map<String, BillCollectorIndex> wardWiseBillCollectors = new HashMap<>();
        for (Bucket bucket : completedAggr.getBuckets())
            completedApplicationsMap.put(bucket.getKeyAsString(), bucket.getDocCount());

        if (DASHBOARD_GROUPING_WARDWISE.equalsIgnoreCase(surveyRequest.getAggregationLevel())) {
            CollectionDetailsRequest collectionDetailsRequest = new CollectionDetailsRequest();
            collectionDetailsRequest.setUlbCode(surveyRequest.getUlbCode());
            wardWiseBillCollectors = collectionIndexElasticSearchService.getWardWiseBillCollectors(collectionDetailsRequest);
        }

        responseList = setSurveyResponse(surveyRequest, aggregationField, response, completedApplicationsMap,
                wardWiseBillCollectors);
        return responseList;
    }

    private List<SurveyResponse> setSurveyResponse(SurveyRequest surveyRequest, String aggregationField,
            SearchResponse response, Map<String, Long> completedApplicationsMap,
            Map<String, BillCollectorIndex> wardWiseBillCollectors) {
        SurveyResponse surveyResponse;
        List<SurveyResponse> responseList = new ArrayList<>();
        Terms ulbTerms = response.getAggregations().get(AGGREGATIONWISE);
        Terms verfTerms;
        Terms sentForRefTerms;
        Sum gisSumAggr;
        Sum systemSumAggr;
        Sum approvedSumAggr;
        BigDecimal totalGisTax;
        BigDecimal totalSystemTax;
        BigDecimal totalApprovedTax;
        String name;
        Map<String, String> cityInfoMap = new HashMap<>();
        Iterable<CityIndex> cities = cityIndexService.findAll();
        for (CityIndex city : cities) 
            cityInfoMap.put(city.getName(), city.getCitycode());

        for (Bucket bucket : ulbTerms.getBuckets()) {
            surveyResponse = new SurveyResponse();
            name = bucket.getKeyAsString();

            if (REGION_NAME.equals(aggregationField))
                surveyResponse.setRegionName(name);
            else if (DISTRICT_NAME.equals(aggregationField))
                surveyResponse.setDistrictName(name);
            else if (CITY_NAME.equals(aggregationField)){
                surveyResponse.setUlbName(name);
                surveyResponse.setUlbCode(cityInfoMap.get(name) == null ? StringUtils.EMPTY
                        : cityInfoMap.get(name));
            }
            else if (REVENUE_WARD.equals(aggregationField)) {
                surveyResponse.setWardName(name);
                if (DASHBOARD_GROUPING_WARDWISE.equalsIgnoreCase(surveyRequest.getAggregationLevel())
                        && !wardWiseBillCollectors.isEmpty()) {
                    surveyResponse.setBillCollector(wardWiseBillCollectors.get(name) == null ? StringUtils.EMPTY
                            : wardWiseBillCollectors.get(name).getBillCollector());
                    surveyResponse.setBillCollMobile(wardWiseBillCollectors.get(name) == null ? StringUtils.EMPTY
                            : wardWiseBillCollectors.get(name).getBillCollectorMobileNo());
                }

            } else if (APPLICATION_TYPE.equalsIgnoreCase(aggregationField))
                surveyResponse.setServiceName(name);

            surveyResponse.setTotalReceived(bucket.getDocCount());
            verfTerms = bucket.getAggregations().get("verificationDone");
            for (Bucket verfBucket : verfTerms.getBuckets()) {
                if (verfBucket.getKeyAsNumber().intValue() == 1)
                    surveyResponse.setVerifyDone(verfBucket.getDocCount());
            }

            sentForRefTerms = bucket.getAggregations().get("sentForReference");
            for (Bucket refBucket : sentForRefTerms.getBuckets()) {
                if (refBucket.getKeyAsNumber().intValue() == 1)
                    surveyResponse.setVerifyPending(refBucket.getDocCount() - surveyResponse.getVerifyDone());
            }

            gisSumAggr = bucket.getAggregations().get("gisTotal");
            totalGisTax = BigDecimal.valueOf(gisSumAggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
            systemSumAggr = bucket.getAggregations().get("systemTotal");
            totalSystemTax = BigDecimal.valueOf(systemSumAggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
            approvedSumAggr = bucket.getAggregations().get("approvedTotal");
            totalApprovedTax = BigDecimal.valueOf(approvedSumAggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);

            surveyResponse.setExptdIncr((totalGisTax.subtract(totalSystemTax)).doubleValue());
            surveyResponse.setActlIncr((totalApprovedTax.subtract(totalSystemTax)).doubleValue());
            surveyResponse.setDifference(surveyResponse.getExptdIncr() - surveyResponse.getActlIncr());
            if(completedApplicationsMap.get(name) != null)
                surveyResponse.setTotalCompleted(completedApplicationsMap.get(name));
            surveyResponse.setTotalPending(surveyResponse.getTotalReceived() - surveyResponse.getTotalCompleted());

            responseList.add(surveyResponse);
        }
        return responseList;
    }
}
