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

import static java.lang.String.format;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_DISTRICTWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_FUNCTIONARYWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_REGIONWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_SERVICEWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_ULBWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.DASHBOARD_GROUPING_WARDWISE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_APPROVED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_CLOSED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_DIGITALLY_SIGNED;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.egov.infra.admin.master.entity.es.CityIndex;
import org.egov.infra.admin.master.service.es.CityIndexService;
import org.egov.infra.utils.DateUtils;
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
    private static final String CITY_CODE = "cityCode";
    private static final String DISTRICT_NAME = "districtName";
    private static final String REGION_NAME = "regionName";
    private static final String REVENUE_WARD = "revenueWard";
    private static final String APPLICATION_TYPE = "applicationType";
    private static final String APP_VIEW_URL = "/ptis/view/viewProperty-viewForm.action?applicationNo=%s&applicationType=%s";
    private static final String THIRD_PARTY_FLAG = "thirdPrtyFlag";
    private static final String FUNCTIONARY_NAME = "functionaryName";
    private static final String APPLICATION_STATUS = "applicationStatus";
    private static final String WF_STATUS_CANCELLED = "Cancelled";
    private static final String WF_STATUS_UNDER_WF = "Under Workflow";
    private static final String STATUS_IS_APPROVED = "isApproved";
    private static final String STATUS_IS_CANCELLED = "isCancelled";
    private static final String APPLICATION_TAX = "applicationTax";
    private static final String APPROVED_TAX = "approvedTax";
    private static final String SYSTEM_TAX = "systemTax";
    private static final String GIS_TAX = "gisTax";

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private CollectionIndexElasticSearchService collectionIndexElasticSearchService;

    @Autowired
    private CityIndexService cityIndexService;

    public List<SurveyDashboardResponse> getGisApplicationDetails(final SurveyRequest surveyDashboardRequest) {
        List<SurveyDashboardResponse> surveyList = new ArrayList<>();
        @SuppressWarnings("rawtypes")
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("by_ward").field(REVENUE_WARD).size(100);
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
            String applicationType = sourceAsMap.get(APPLICATION_TYPE).toString();
            surveyResponse.setApplicationNo(applicationNo);
            surveyResponse.setAssessmentNo(
                    sourceAsMap.get("assessmentNo") == null ? "N/A" : sourceAsMap.get("assessmentNo").toString());
            surveyResponse.setDoorNo(sourceAsMap.get("doorNo").toString());
            surveyResponse.setRevenueWard(sourceAsMap.get(REVENUE_WARD).toString());
            surveyResponse.setRevenueBlock(sourceAsMap.get("blockName").toString());
            surveyResponse.setLocality(sourceAsMap.get("localityName").toString());
            surveyResponse.setElectionWard(sourceAsMap.get("electionWard").toString());
            surveyResponse.setSystemTax((double) sourceAsMap.get(SYSTEM_TAX));
            surveyResponse.setGisTax((double) sourceAsMap.get(GIS_TAX));
            surveyResponse.setApplicationTax((double) sourceAsMap.get(APPLICATION_TAX));
            surveyResponse.setAppStatus(sourceAsMap.get(APPLICATION_STATUS).toString());
            surveyResponse.setAssistantName(
                    sourceAsMap.get("assistantName") == null ? "N/A" : sourceAsMap.get("assistantName").toString());
            surveyResponse.setRiName(sourceAsMap.get("riName") == null ? "N/A" : sourceAsMap.get("riName").toString());
            surveyResponse.setIsreffered((boolean) sourceAsMap.get(SENT_TO_THIRD_PARTY));
            surveyResponse.setIsVarified((boolean) sourceAsMap.get(THIRD_PARTY_FLAG));
            surveyResponse.setServiceName(applicationType);
            surveyResponse.setAppViewURL(format(APP_VIEW_URL, applicationNo, applicationType));
            surveyResponse.setUlbCode(sourceAsMap.get(CITY_CODE).toString());
            Date applictionDate = DateUtils.getDate(sourceAsMap.get("applicationDate").toString(), "yyyy-MM-dd");
            surveyResponse.setAgeing(DateUtils.daysBetween(applictionDate, DateUtils.now()));
            surveyResponse.setFunctionaryName(sourceAsMap.get(FUNCTIONARY_NAME).equals(StringUtils.EMPTY) ? "N/A"
                    : sourceAsMap.get(FUNCTIONARY_NAME).toString());
            surveyResponse.setWfStatus(fetchWorkflowStatus(sourceAsMap));
            surveyList.add(surveyResponse);
        }
    }

    private String fetchWorkflowStatus(Map<String, Object> sourceAsMap) {
        String wfStatus;
        String appStatus = sourceAsMap.get(APPLICATION_STATUS).toString();
        if (sourceAsMap.get(STATUS_IS_APPROVED).equals(true))
            wfStatus = STATUS_APPROVED;
        else if (sourceAsMap.get(STATUS_IS_CANCELLED).equals(true))
            wfStatus = WF_STATUS_CANCELLED;
        else if (appStatus.endsWith(WF_STATE_CLOSED) && sourceAsMap.get(STATUS_IS_APPROVED).equals(true))
            wfStatus = WF_STATE_DIGITALLY_SIGNED;
        else
            wfStatus = WF_STATUS_UNDER_WF;
        return wfStatus;
    }

    private BoolQueryBuilder prepareQuery(final SurveyRequest surveyRequest) {
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        if (StringUtils.isNotBlank(surveyRequest.getRegionName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(REGION_NAME, surveyRequest.getRegionName()));
        if (StringUtils.isNotBlank(surveyRequest.getDistrictName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(DISTRICT_NAME, surveyRequest.getDistrictName()));
        if (StringUtils.isNotBlank(surveyRequest.getUlbName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(CITY_NAME, surveyRequest.getUlbName()));
        if (surveyRequest.getThirdParty() != null) {
            if (surveyRequest.getThirdParty() == 'Y')
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery(SENT_TO_THIRD_PARTY, true));
            else
                boolQuery = boolQuery.filter(QueryBuilders.matchQuery(SENT_TO_THIRD_PARTY, false));
        }
        if (StringUtils.isNotBlank(surveyRequest.getUlbCode())) {
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(CITY_CODE, surveyRequest.getUlbCode()));
        }
        if (StringUtils.isNotBlank(surveyRequest.getWardName())) {
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(REVENUE_WARD, surveyRequest.getWardName()));
        }
        if (StringUtils.isNotBlank(surveyRequest.getLocalityName())) {
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("localityName", surveyRequest.getLocalityName()));
        }
        if (StringUtils.isNotBlank(surveyRequest.getFunctionaryName()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(FUNCTIONARY_NAME, surveyRequest.getFunctionaryName()));

        boolQuery = prepareApplicationStatusQuery(surveyRequest, boolQuery);

        return boolQuery;
    }

    private BoolQueryBuilder prepareApplicationStatusQuery(final SurveyRequest surveyRequest, BoolQueryBuilder boolQuery) {
        BoolQueryBuilder statusQuery = boolQuery;
        if (StringUtils.isNotBlank(surveyRequest.getServiceName()))
            statusQuery = statusQuery.filter(QueryBuilders.matchQuery(APPLICATION_TYPE, surveyRequest.getServiceName()));
        if (StringUtils.isNotBlank(surveyRequest.getApproved()))
            statusQuery = statusQuery.filter(QueryBuilders.matchQuery(STATUS_IS_APPROVED, surveyRequest.getApproved()));

        if (StringUtils.isNotBlank(surveyRequest.getCancelled()))
            statusQuery = statusQuery.filter(QueryBuilders.matchQuery(STATUS_IS_CANCELLED, surveyRequest.getCancelled()));

        if (StringUtils.isNotBlank(surveyRequest.getThirdPartyReffered()))
            statusQuery = statusQuery
                    .filter(QueryBuilders.matchQuery(SENT_TO_THIRD_PARTY, surveyRequest.getThirdPartyReffered()));

        if (StringUtils.isNotBlank(surveyRequest.getVerified()))
            statusQuery = statusQuery.filter(QueryBuilders.matchQuery(THIRD_PARTY_FLAG, surveyRequest.getVerified()));
        return statusQuery;
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
            aggregationField = CITY_CODE;
        else if (DASHBOARD_GROUPING_WARDWISE.equalsIgnoreCase(surveyRequest.getAggregationLevel()))
            aggregationField = REVENUE_WARD;
        else if (DASHBOARD_GROUPING_SERVICEWISE.equalsIgnoreCase(surveyRequest.getAggregationLevel()))
            aggregationField = APPLICATION_TYPE;
        else if (DASHBOARD_GROUPING_FUNCTIONARYWISE.equalsIgnoreCase(surveyRequest.getAggregationLevel()))
            aggregationField = FUNCTIONARY_NAME;
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
                        .subAggregation(AggregationBuilders.sum("gisTotal").field(GIS_TAX))
                        .subAggregation(AggregationBuilders.sum("systemTotal").field(SYSTEM_TAX))
                        .subAggregation(AggregationBuilders.sum(APPLICATION_TAX).field(APPLICATION_TAX)))
                .execute().actionGet();

        SearchResponse completedResponse = elasticsearchTemplate.getClient().prepareSearch(PROPERTYSURVEYDETAILS_INDEX).setSize(0)
                .setQuery(prepareQuery(surveyRequest).filter(QueryBuilders.matchQuery(APPLICATION_STATUS, "Closed")))
                .addAggregation(aggregationBuilder).execute().actionGet();

        SearchResponse approvedResponse = elasticsearchTemplate.getClient().prepareSearch(PROPERTYSURVEYDETAILS_INDEX).setSize(0)
                .setQuery(prepareQuery(surveyRequest).filter(QueryBuilders.matchQuery(STATUS_IS_APPROVED, true)))
                .addAggregation(aggregationBuilder
                        .subAggregation(AggregationBuilders.sum("approvedSystemTotal").field(SYSTEM_TAX))
                        .subAggregation(AggregationBuilders.sum("approvedTotal").field(APPROVED_TAX)))
                .execute().actionGet();
        Terms approvedAggr = approvedResponse.getAggregations().get(AGGREGATIONWISE);
        Map<String, BigDecimal> approvedSystemMap = new HashMap<>();
        Map<String, BigDecimal> approvedTotalMap = new HashMap<>();
        List<Map<String, BigDecimal>> list = new ArrayList<>();
        Map<String, List<Map<String, BigDecimal>>> totalMap = new HashMap<>();
        Sum approvedSystemSumAggr;
        Sum totalApprovedAggr;
        BigDecimal totalApprovedSysTax;
        BigDecimal totalApprovedTax;
        String aggName=StringUtils.EMPTY;
        for (Bucket appBucket : approvedAggr.getBuckets()) {
            aggName=appBucket.getKeyAsString();
            approvedSystemSumAggr = appBucket.getAggregations().get("approvedSystemTotal");
            totalApprovedSysTax = BigDecimal.valueOf(approvedSystemSumAggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
            totalApprovedAggr = appBucket.getAggregations().get("approvedTotal");
            totalApprovedTax = BigDecimal.valueOf(totalApprovedAggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
            approvedSystemMap.put("approvedSystemTax", totalApprovedSysTax);
            approvedTotalMap.put("totalApprovedTax", totalApprovedTax);
            list.add(approvedSystemMap);
            list.add(approvedTotalMap);
        }
        totalMap.put(aggName, list);

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

        responseList = setSurveyResponse(surveyRequest, aggregationField, response, completedApplicationsMap, totalMap,
                wardWiseBillCollectors);
        return responseList;
    }

    private List<SurveyResponse> setSurveyResponse(SurveyRequest surveyRequest, String aggregationField,
            SearchResponse response, Map<String, Long> completedApplicationsMap,
            Map<String, List<Map<String, BigDecimal>>> approvedTotalMap,
            Map<String, BillCollectorIndex> wardWiseBillCollectors) {
        SurveyResponse surveyResponse;
        List<SurveyResponse> responseList = new ArrayList<>();
        Terms ulbTerms = response.getAggregations().get(AGGREGATIONWISE);
        Terms verfTerms;
        Terms sentForRefTerms;
        String name;
        Map<String, String> cityInfoMap = new HashMap<>();
        Iterable<CityIndex> cities = cityIndexService.findAll();
        for (CityIndex city : cities)
            cityInfoMap.put(city.getCitycode(), city.getName());

        for (Bucket bucket : ulbTerms.getBuckets()) {
            surveyResponse = new SurveyResponse();
            name = bucket.getKeyAsString();
            getAggregationNames(surveyRequest, aggregationField, wardWiseBillCollectors, surveyResponse, name, cityInfoMap);
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
            getTaxDifferences(completedApplicationsMap, approvedTotalMap, surveyResponse, name, bucket);
            responseList.add(surveyResponse);
        }
        return responseList;
    }

    private void getAggregationNames(SurveyRequest surveyRequest, String aggregationField,
            Map<String, BillCollectorIndex> wardWiseBillCollectors, SurveyResponse surveyResponse, String name,
            Map<String, String> cityInfoMap) {

        if (REGION_NAME.equals(aggregationField))
            surveyResponse.setRegionName(name);
        else if (DISTRICT_NAME.equals(aggregationField))
            surveyResponse.setDistrictName(name);
        else if (CITY_CODE.equals(aggregationField)) {
            surveyResponse.setUlbCode(name);
            surveyResponse.setUlbName(cityInfoMap.get(name) == null ? StringUtils.EMPTY : cityInfoMap.get(name));
        } else if (REVENUE_WARD.equals(aggregationField)) {
            getWardWiseBillCollector(surveyRequest, wardWiseBillCollectors, surveyResponse, name);
        } else if (APPLICATION_TYPE.equalsIgnoreCase(aggregationField)) {
            surveyResponse.setServiceName(name);
        } else if (FUNCTIONARY_NAME.equalsIgnoreCase(aggregationField)) {
            surveyResponse.setFunctionaryName(name);
        }
    }

    private void getWardWiseBillCollector(SurveyRequest surveyRequest, Map<String, BillCollectorIndex> wardWiseBillCollectors,
            SurveyResponse surveyResponse, String name) {
        surveyResponse.setWardName(name);
        surveyResponse.setUlbCode(surveyRequest.getUlbCode());
        if (DASHBOARD_GROUPING_WARDWISE.equalsIgnoreCase(surveyRequest.getAggregationLevel())
                && !wardWiseBillCollectors.isEmpty()) {
            surveyResponse.setBillCollector(wardWiseBillCollectors.get(name) == null ? StringUtils.EMPTY
                    : wardWiseBillCollectors.get(name).getBillCollector());
            surveyResponse.setBillCollMobile(wardWiseBillCollectors.get(name) == null ? StringUtils.EMPTY
                    : wardWiseBillCollectors.get(name).getBillCollectorMobileNo());
        }
    }

    private void getTaxDifferences(Map<String, Long> completedApplicationsMap,
            Map<String, List<Map<String, BigDecimal>>> approvedTotalMap, SurveyResponse surveyResponse, String name,
            Bucket bucket) {
        Sum gisSumAggr;
        Sum systemSumAggr;
        Sum appTaxSumAggr;
        BigDecimal totalGisTax;
        BigDecimal totalSystemTax;
        BigDecimal totalApplicationTax;
        BigDecimal approvedSysTax; 
        BigDecimal approvedTotalTax;
        gisSumAggr = bucket.getAggregations().get("gisTotal");
        totalGisTax = BigDecimal.valueOf(gisSumAggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
        systemSumAggr = bucket.getAggregations().get("systemTotal");
        totalSystemTax = BigDecimal.valueOf(systemSumAggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
        appTaxSumAggr = bucket.getAggregations().get(APPLICATION_TAX);
        totalApplicationTax = BigDecimal.valueOf(appTaxSumAggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
        surveyResponse.setExptdIncr((totalGisTax.subtract(totalSystemTax)).doubleValue());
        surveyResponse.setDiffFromSurveytax((totalGisTax.subtract(totalApplicationTax)).doubleValue());
        if (completedApplicationsMap.get(name) != null)
            surveyResponse.setTotalCompleted(completedApplicationsMap.get(name));
        surveyResponse.setTotalPending(surveyResponse.getTotalReceived() - surveyResponse.getTotalCompleted());
        if (approvedTotalMap.containsKey(name)) {
            approvedSysTax = approvedTotalMap.get(name).get(0).get("approvedSystemTax");
            approvedTotalTax = approvedTotalMap.get(name).get(1).get("totalApprovedTax");
            surveyResponse.setActlIncr(approvedTotalTax.subtract(approvedSysTax).doubleValue());
        }
        surveyResponse.setDifference(surveyResponse.getExptdIncr()-surveyResponse.getActlIncr());
    }
}
