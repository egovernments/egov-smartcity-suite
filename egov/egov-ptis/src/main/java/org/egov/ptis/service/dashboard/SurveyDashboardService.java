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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.bean.dashboard.SurveyRequest;
import org.egov.ptis.bean.dashboard.SurveyResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

@Service
public class SurveyDashboardService {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public List<SurveyResponse> getGisApplicationDetails(final SurveyRequest surveyDashboardRequest) {
        List<SurveyResponse> surveyList = new ArrayList<>();
        @SuppressWarnings("rawtypes")
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms("by_ward").field("revenueWard").size(100);
        SearchResponse response = elasticsearchTemplate.getClient()
                .prepareSearch("propertysurveydetails")
                .setQuery(prepareQuery(surveyDashboardRequest))
                .addAggregation(aggregationBuilder).setSize(100000)
                .execute().actionGet();
        getPropertySurveyList(surveyList, response);
        return surveyList;
    }

    private void getPropertySurveyList(List<SurveyResponse> surveyList, SearchResponse response) {
        SurveyResponse surveyResponse;
        SearchHits hits = response.getHits();
        for (SearchHit hit : hits) {
            surveyResponse = new SurveyResponse();
            Map<String, Object> sourceAsMap = hit.sourceAsMap();
            String applicationNo = sourceAsMap.get("applicationNo").toString();
            String cityUrl = ApplicationThreadLocals.getDomainURL();
            String ptUrl = "/ptis/view/viewProperty-viewForm.action?";
            String applicationType = sourceAsMap.get("applicationType").toString();
            String appViewURL = cityUrl.concat(ptUrl).concat("applicationNo=").concat(applicationNo).concat("&applicationType")
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
            surveyResponse.setIsreffered((boolean) sourceAsMap.get("sentToThirdParty"));
            surveyResponse.setIsVarified((boolean) sourceAsMap.get("thirdPrtyFlag"));
            surveyResponse.setAppViewURL(appViewURL);
            surveyList.add(surveyResponse);
        }
    }

    private BoolQueryBuilder prepareQuery(final SurveyRequest surveydashboardRequest) {

        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        if (StringUtils.isNotBlank(surveydashboardRequest.getUlbCode())) {
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("cityCode", surveydashboardRequest.getUlbCode()));
        }
        if (StringUtils.isNotBlank(surveydashboardRequest.getWardName())) {
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("wardName", surveydashboardRequest.getWardName()));
        }

        if (StringUtils.isNotBlank(surveydashboardRequest.getLocalityName())) {
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("localityName", surveydashboardRequest.getLocalityName()));
        }
        return boolQuery;
    }

}
