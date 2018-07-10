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

package org.egov.eis.service.es.dashboard;

import org.egov.eis.es.dashboard.EmployeeCountResponse;
import org.egov.eis.es.dashboard.EmployeeDetailRequest;
import org.egov.eis.es.utils.EISDashBoardUtils;
import org.egov.eis.utils.constants.EisConstants;
import org.egov.infra.utils.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeStatisticsDashboardService {

    private static final String AGGRFIELD = "aggrField";
    private static final String AGGRTYPE = "_type";
    private static final String TOTALEMPLOYEE = "totalEmployee";
    private static final String TOTALMALE = "totalMale";
    private static final String TOTALFEMALE = "totalFemale";
    private static final String TOTALPERMANENTEMPLOYEE = "totalPermanentEmployee";
    private static final String TOTALPERMANENTMALE = "totalPermanentMale";
    private static final String TOTALPERMANENTFEMALE = "totalPermanentFemale";
    private static final String TOTALDEPEMPLOYEE = "totalDepEmployee";
    private static final String TOTALDEPMALE = "totalDepMale";
    private static final String TOTALDEPFEMALE = "totalDepFemale";
    private static final String TOTALTEMPEMPLOYEE = "totalTemporaryEmployee";
    private static final String TOTALTEMPMALE = "totalTemporaryMale";
    private static final String TOTALTEMPFEMALE = "totalTemporaryFemale";
    private static final String TOTALOUTSOURCEDEMPLOYEE = "totalOutsourcedEmployee";
    private static final String TOTALOUTSOURCEDMALE = "totalOutsourcedMale";
    private static final String TOTALOUTSOURCEDFEMALE = "totalOutsourcedFemale";
    private static final String DEPUTATION = "Deputation";
    private static final String TEMPORARY = "Temporary";
    private static final String OUTSOURCED = "Outsourced";
    private static final String SANCTIONED = "sanctioned";
    private static final String WORKING = "working";
    private static final String VACANT = "vacant";

    private static final String EMPLOYEERECORDS = "employeerecords";
    private static final String MALE = "Male";
    private static final String FEMALE = "Female";

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public List<EmployeeCountResponse> getEmployeeCount(EmployeeDetailRequest employeesDetailRequest, BoolQueryBuilder boolQry, String aggrField) {
        List<EmployeeCountResponse> employeeCountResponses = new ArrayList<>();
        Map<String, SearchResponse> empSearchResponse = getResponseFromIndex(employeesDetailRequest, boolQry, aggrField);

        Map<String, EmployeeCountResponse> empCountRes = new HashMap<>();
        Map<String, EmployeeCountResponse> employeeCountRes;

        StringTerms aggrEmp = empSearchResponse.get(TOTALEMPLOYEE).getAggregations().get(AGGRFIELD);
        StringTerms aggrMale = empSearchResponse.get(TOTALMALE).getAggregations().get(AGGRFIELD);
        StringTerms aggrFemale = empSearchResponse.get(TOTALFEMALE).getAggregations().get(AGGRFIELD);
        StringTerms aggrPermanentEmp = empSearchResponse.get(TOTALPERMANENTEMPLOYEE).getAggregations().get(AGGRFIELD);
        StringTerms aggrPermanentMale = empSearchResponse.get(TOTALPERMANENTMALE).getAggregations().get(AGGRFIELD);
        StringTerms aggrPermanentFemale = empSearchResponse.get(TOTALPERMANENTFEMALE).getAggregations().get(AGGRFIELD);

        StringTerms aggrDepEmp = empSearchResponse.get(TOTALDEPEMPLOYEE).getAggregations().get(AGGRFIELD);
        StringTerms aggrDepMale = empSearchResponse.get(TOTALDEPMALE).getAggregations().get(AGGRFIELD);
        StringTerms aggrDepFemale = empSearchResponse.get(TOTALDEPFEMALE).getAggregations().get(AGGRFIELD);

        StringTerms aggrTempEmp = empSearchResponse.get(TOTALTEMPEMPLOYEE).getAggregations().get(AGGRFIELD);
        StringTerms aggrTempMale = empSearchResponse.get(TOTALTEMPMALE).getAggregations().get(AGGRFIELD);
        StringTerms aggrTempFemale = empSearchResponse.get(TOTALTEMPFEMALE).getAggregations().get(AGGRFIELD);

        StringTerms aggrOutsourceEmp = empSearchResponse.get(TOTALOUTSOURCEDEMPLOYEE).getAggregations().get(AGGRFIELD);
        StringTerms aggrOutsourceMale = empSearchResponse.get(TOTALOUTSOURCEDMALE).getAggregations().get(AGGRFIELD);
        StringTerms aggrOutsourceFemale = empSearchResponse.get(TOTALOUTSOURCEDFEMALE).getAggregations().get(AGGRFIELD);

        StringTerms aggrSanctionedPosts = empSearchResponse.get(SANCTIONED).getAggregations().get(AGGRFIELD);

        employeeCountRes = empCountRes;

        for (final Terms.Bucket entry : aggrEmp.getBuckets()) {

            String keyName = entry.getKeyAsString();
            final TopHits topHits = entry.getAggregations().get("employeerecords");

            EmployeeCountResponse empCountResponse = new EmployeeCountResponse();

            ValueCount aggregation = entry.getAggregations().get(EisConstants.EMPLOYEE_CODE);
            Long countEmp = aggregation.getValue();
            empCountResponse.setTotalEmployee(countEmp);
            empCountResponse.setTotalMale(getTotalCountForAggrField(aggrMale, keyName));
            empCountResponse.setTotalFemale(getTotalCountForAggrField(aggrFemale, keyName));
            empCountResponse.setTotalRegularEmployee(getTotalCountForAggrField(aggrPermanentEmp, keyName) + getTotalCountForAggrField(aggrDepEmp, keyName));
            empCountResponse.setTotalRegularMale(getTotalCountForAggrField(aggrPermanentMale, keyName) + getTotalCountForAggrField(aggrDepMale, keyName));
            empCountResponse.setTotalRegularFemale(getTotalCountForAggrField(aggrPermanentFemale, keyName) + getTotalCountForAggrField(aggrDepFemale, keyName));
            empCountResponse.setTotalContractEmployee(getTotalCountForAggrField(aggrTempEmp, keyName) + getTotalCountForAggrField(aggrOutsourceEmp, keyName));
            empCountResponse.setTotalContractMale(getTotalCountForAggrField(aggrTempMale, keyName) + getTotalCountForAggrField(aggrOutsourceMale, keyName));
            empCountResponse.setTotalContractFemale(getTotalCountForAggrField(aggrTempFemale, keyName) + getTotalCountForAggrField(aggrOutsourceFemale, keyName));
            setTotalSumForAggrField(empCountResponse, aggrSanctionedPosts, keyName);

            setValues(employeesDetailRequest, empCountResponse, topHits, aggrField, keyName);
            empCountRes.put(keyName, empCountResponse);
        }

        if (!employeeCountRes.isEmpty())
            for (EmployeeCountResponse response : employeeCountRes.values())
                employeeCountResponses.add(response);
        return employeeCountResponses;
    }

    private Long getTotalCountForAggrField(StringTerms aggrEmployeeData, String keyName) {
        Long count = 0L;
        for (final Terms.Bucket empEntry : aggrEmployeeData.getBuckets()) {
            if (keyName.equalsIgnoreCase(empEntry.getKeyAsString())) {
                ValueCount aggrValue = empEntry.getAggregations().get(EisConstants.EMPLOYEE_CODE);
                count = aggrValue.getValue();
            }
        }
        return count;
    }

    private void setTotalSumForAggrField(EmployeeCountResponse empCountResponse, StringTerms aggrSanctionedPosts, String keyName) {

        for (final Terms.Bucket entry : aggrSanctionedPosts.getBuckets()) {
            if (keyName.equalsIgnoreCase(entry.getKeyAsString())) {

                final Sum aggrSanction = entry.getAggregations().get(SANCTIONED);
                final Sum aggrWorking = entry.getAggregations().get(WORKING);
                final Sum aggrVacant = entry.getAggregations().get(VACANT);

                empCountResponse.setTotalSanctioned(Double.valueOf(aggrSanction.getValue()).longValue());
                empCountResponse.setTotalWorking(Double.valueOf(aggrWorking.getValue()).longValue());
                empCountResponse.setTotalVacant(Double.valueOf(aggrVacant.getValue()).longValue());

            }
        }
    }


    private Map<String, SearchResponse> getResponseFromIndex(EmployeeDetailRequest employeeDetailRequest, BoolQueryBuilder boolQuery, String aggrField) {

        Map<String, SearchResponse> empSearchResponse = new HashMap<>();
        if (StringUtils.isBlank(aggrField)) {
            aggrField = AGGRTYPE;
        }

        empSearchResponse.put(TOTALEMPLOYEE, getResponseFromIndexForTotalCount(boolQuery, aggrField));

        BoolQueryBuilder filterTypeQuery = boolQuery.must(QueryBuilders.matchQuery(EisConstants.EMPLOYEE_TYPE, EisConstants.EMPLOYEE_TYPE_PERMANENT));
        empSearchResponse.put(TOTALPERMANENTEMPLOYEE, getResponseFromIndexForTotalCount(filterTypeQuery, aggrField));

        filterTypeQuery.must(QueryBuilders.matchQuery(EisConstants.GENDER, MALE));
        empSearchResponse.put(TOTALPERMANENTMALE, getResponseFromIndexForTotalCount(filterTypeQuery, aggrField));

        BoolQueryBuilder boolQueryBuilder = EISDashBoardUtils.prepareWhereClauseForEmployees(employeeDetailRequest);
        boolQueryBuilder.must(QueryBuilders.matchQuery(EisConstants.EMPLOYEE_TYPE, EisConstants.EMPLOYEE_TYPE_PERMANENT))
                .must(QueryBuilders.matchQuery(EisConstants.GENDER, FEMALE));
        empSearchResponse.put(TOTALPERMANENTFEMALE, getResponseFromIndexForTotalCount(boolQueryBuilder, aggrField));

        BoolQueryBuilder filterMaleQuery = EISDashBoardUtils.prepareWhereClauseForEmployees(employeeDetailRequest);
        filterMaleQuery.must(QueryBuilders.matchQuery(EisConstants.GENDER, MALE));
        empSearchResponse.put(TOTALMALE, getResponseFromIndexForTotalCount(filterMaleQuery, aggrField));

        BoolQueryBuilder filterFemaleQuery = EISDashBoardUtils.prepareWhereClauseForEmployees(employeeDetailRequest);
        filterFemaleQuery.must(QueryBuilders.matchQuery(EisConstants.GENDER, FEMALE));
        empSearchResponse.put(TOTALFEMALE, getResponseFromIndexForTotalCount(filterFemaleQuery, aggrField));

        getDeputationTypeResonse(employeeDetailRequest, empSearchResponse, aggrField);
        getTemporaryTypeResonse(employeeDetailRequest, empSearchResponse, aggrField);
        getOutsourcedTypeResonse(employeeDetailRequest, empSearchResponse, aggrField);
        getSanctionedPostsResponse(employeeDetailRequest, empSearchResponse, aggrField);

        return empSearchResponse;
    }

    private void getSanctionedPostsResponse(EmployeeDetailRequest employeeDetailRequest, Map<String, SearchResponse> empSearchResponse, String aggrField) {
        BoolQueryBuilder filterPostsQuery = EISDashBoardUtils.prepareWhereClause(employeeDetailRequest, QueryBuilders.boolQuery());
        empSearchResponse.put(SANCTIONED, getResponseFromIndexForSanctionedPosts(filterPostsQuery, aggrField));
    }

    private void getDeputationTypeResonse(EmployeeDetailRequest employeeDetailRequest, Map<String, SearchResponse> empSearchResponse, String aggrField) {
        BoolQueryBuilder filterDepTypeQuery = EISDashBoardUtils.prepareWhereClauseForEmployees(employeeDetailRequest);

        filterDepTypeQuery.must(QueryBuilders.matchQuery(EisConstants.EMPLOYEE_TYPE, DEPUTATION));
        empSearchResponse.put(TOTALDEPEMPLOYEE, getResponseFromIndexForTotalCount(filterDepTypeQuery, aggrField));

        filterDepTypeQuery.must(QueryBuilders.matchQuery(EisConstants.GENDER, MALE));
        empSearchResponse.put(TOTALDEPMALE, getResponseFromIndexForTotalCount(filterDepTypeQuery, aggrField));

        BoolQueryBuilder filterDepFemaleQuery = EISDashBoardUtils.prepareWhereClauseForEmployees(employeeDetailRequest);
        filterDepFemaleQuery.must(QueryBuilders.matchQuery(EisConstants.EMPLOYEE_TYPE, DEPUTATION))
                .must(QueryBuilders.matchQuery(EisConstants.GENDER, FEMALE));
        empSearchResponse.put(TOTALDEPFEMALE, getResponseFromIndexForTotalCount(filterDepFemaleQuery, aggrField));
    }

    private void getTemporaryTypeResonse(EmployeeDetailRequest employeeDetailRequest, Map<String, SearchResponse> empSearchResponse, String aggrField) {
        BoolQueryBuilder filterDepTypeQuery = EISDashBoardUtils.prepareWhereClauseForEmployees(employeeDetailRequest);

        filterDepTypeQuery.must(QueryBuilders.matchQuery(EisConstants.EMPLOYEE_TYPE, TEMPORARY));
        empSearchResponse.put(TOTALTEMPEMPLOYEE, getResponseFromIndexForTotalCount(filterDepTypeQuery, aggrField));

        filterDepTypeQuery.must(QueryBuilders.matchQuery(EisConstants.GENDER, MALE));
        empSearchResponse.put(TOTALTEMPMALE, getResponseFromIndexForTotalCount(filterDepTypeQuery, aggrField));

        BoolQueryBuilder filterDepFemaleQuery = EISDashBoardUtils.prepareWhereClauseForEmployees(employeeDetailRequest);
        filterDepFemaleQuery.must(QueryBuilders.matchQuery(EisConstants.EMPLOYEE_TYPE, TEMPORARY))
                .must(QueryBuilders.matchQuery(EisConstants.GENDER, FEMALE));
        empSearchResponse.put(TOTALTEMPFEMALE, getResponseFromIndexForTotalCount(filterDepFemaleQuery, aggrField));
    }

    private void getOutsourcedTypeResonse(EmployeeDetailRequest employeeDetailRequest, Map<String, SearchResponse> empSearchResponse, String aggrField) {
        BoolQueryBuilder filterDepTypeQuery = EISDashBoardUtils.prepareWhereClauseForEmployees(employeeDetailRequest);

        filterDepTypeQuery.must(QueryBuilders.matchQuery(EisConstants.EMPLOYEE_TYPE, OUTSOURCED));
        empSearchResponse.put(TOTALOUTSOURCEDEMPLOYEE, getResponseFromIndexForTotalCount(filterDepTypeQuery, aggrField));

        filterDepTypeQuery.must(QueryBuilders.matchQuery(EisConstants.GENDER, MALE));
        empSearchResponse.put(TOTALOUTSOURCEDMALE, getResponseFromIndexForTotalCount(filterDepTypeQuery, aggrField));

        BoolQueryBuilder filterDepFemaleQuery = EISDashBoardUtils.prepareWhereClauseForEmployees(employeeDetailRequest);
        filterDepFemaleQuery.must(QueryBuilders.matchQuery(EisConstants.EMPLOYEE_TYPE, OUTSOURCED))
                .must(QueryBuilders.matchQuery(EisConstants.GENDER, FEMALE));
        empSearchResponse.put(TOTALOUTSOURCEDFEMALE, getResponseFromIndexForTotalCount(filterDepFemaleQuery, aggrField));
    }

    private SearchResponse getResponseFromIndexForTotalCount(BoolQueryBuilder boolQuery, String aggrField) {
        return elasticsearchTemplate.getClient()
                .prepareSearch(EisConstants.EMPLOYEE_INDEX_NAME).setQuery(boolQuery)
                .addAggregation(AggregationBuilders.terms(AGGRFIELD).field(aggrField).size(5000)
                        .subAggregation(
                                AggregationBuilders.count(EisConstants.EMPLOYEE_CODE).field(EisConstants.EMPLOYEE_CODE))
                        .subAggregation(AggregationBuilders.topHits(EMPLOYEERECORDS).addField(EisConstants.DISTNAME)
                                .addField(EisConstants.ULBNAME)
                                .addField(EisConstants.ULBGRADE).addField(EisConstants.REGNAME)
                                .addField(EisConstants.DEPARTMENT_NAME.toLowerCase())
                                .setSize(1)))
                .execute().actionGet();
    }

    private SearchResponse getResponseFromIndexForSanctionedPosts(BoolQueryBuilder boolQuery, String aggrField) {
        return elasticsearchTemplate.getClient()
                .prepareSearch(EisConstants.EMPLOYEE_SANCTIONEDPOSTS_INDEX_NAME).setQuery(boolQuery)
                .addAggregation(AggregationBuilders.terms(AGGRFIELD).field(aggrField).size(5000)
                        .subAggregation(
                                AggregationBuilders.sum(SANCTIONED).field(SANCTIONED))
                        .subAggregation(
                                AggregationBuilders.sum(WORKING).field(WORKING))
                        .subAggregation(
                                AggregationBuilders.sum(VACANT).field(VACANT))
                        .subAggregation(AggregationBuilders.topHits(EMPLOYEERECORDS).addField(EisConstants.DISTNAME)
                                .addField(EisConstants.ULBNAME)
                                .addField(EisConstants.ULBGRADE).addField(EisConstants.REGNAME)
                                .addField(EisConstants.DEPARTMENT_NAME.toLowerCase())
                                .setSize(1)))
                .execute().actionGet();
    }

    private void setValues(EmployeeDetailRequest employeeDetailRequest, EmployeeCountResponse response, TopHits topHits,
                           String aggrField, String keyName) {

        if (employeeDetailRequest.getRegion() != null) {
            response.setRegion(employeeDetailRequest.getRegion());
        }
        if (employeeDetailRequest.getDistrict() != null) {
            response.setDistrict(employeeDetailRequest.getDistrict());
        }
        if (employeeDetailRequest.getGrade() != null) {
            response.setUlbName(employeeDetailRequest.getGrade());
        }
        if (employeeDetailRequest.getDepartmentName() != null) {
            response.setDepartment(employeeDetailRequest.getDepartmentName());
        }
        if (employeeDetailRequest.getUlbCode() != null) {
            response.setUlbCode(employeeDetailRequest.getUlbCode());
            setDepartmentAggrValues(response, aggrField, topHits);
        }

        setAggregationFieldValues(response, aggrField, topHits, keyName);
    }

    private void setDepartmentAggrValues(EmployeeCountResponse response, String aggrField, TopHits topHits) {
        final SearchHit[] hit = topHits.getHits().getHits();

        String regionName = hit[0].field(EisConstants.REGNAME).getValue();
        String districtName = hit[0].field(EisConstants.DISTNAME).getValue();
        String grade = hit[0].field(EisConstants.ULBGRADE).getValue();
        String ulb = hit[0].field(EisConstants.ULBNAME).getValue();

        if ("department".equalsIgnoreCase(aggrField)) {
            response.setDistrict(districtName);
            response.setRegion(regionName);
            response.setUlbName(ulb);
            response.setGrade(grade);
        }
    }

    private void setAggregationFieldValues(EmployeeCountResponse response, String aggrField, TopHits topHits, String keyName) {
        final SearchHit[] hit = topHits.getHits().getHits();

        String regName = hit[0].field(EisConstants.REGNAME).getValue();
        String disName = hit[0].field(EisConstants.DISTNAME).getValue();
        String ulbGrade = hit[0].field(EisConstants.ULBGRADE).getValue();
        String ulbName = hit[0].field(EisConstants.ULBNAME).getValue();
        String department = hit[0].field(EisConstants.DEPARTMENT_NAME) == null ? "" : hit[0].field(EisConstants.DEPARTMENT_NAME).getValue();
        if ("regname".equalsIgnoreCase(aggrField)) {
            response.setRegion(regName);
        }
        if ("distname".equalsIgnoreCase(aggrField)) {
            response.setRegion(regName);
            response.setDistrict(keyName);
        }
        if ("ulbcode".equalsIgnoreCase(aggrField)) {
            response.setDistrict(disName);
            response.setRegion(regName);
            response.setUlbName(ulbName);
            response.setGrade(ulbGrade);
            response.setUlbCode(keyName);
        }
        if ("ulbgrade".equalsIgnoreCase(aggrField)) {
            response.setGrade(ulbGrade);
        }
        if ("department".equalsIgnoreCase(aggrField)) {
            response.setDepartment(department);
        }
    }
}