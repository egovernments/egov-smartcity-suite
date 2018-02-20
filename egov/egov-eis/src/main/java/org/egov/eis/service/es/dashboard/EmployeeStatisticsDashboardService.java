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
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeStatisticsDashboardService {

    private static final String AGGRFIELD = "aggrField";
    private static final String AGGRTYPE = "_type";
    private static final String TOTALEMPLOYEE = "totalEmployee";
    private static final String TOTALMALE = "totalMale";
    private static final String TOTALFEMALE = "totalFemale";
    private static final String TOTALREGULAREMPLOYEE = "totalRegularEmployee";
    private static final String TOTALREGULARMALE = "totalRegularMale";
    private static final String TOTALREGULARFEMALE = "totalRegularFemale";
    private static final String EMPLOYEERECORDS = "employeerecords";
    private static final String MALE = "Male";
    private static final String FEMALE = "Female";

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public List<EmployeeCountResponse> getEmployeeCount(EmployeeDetailRequest employeesDetailRequest, BoolQueryBuilder boolQry, String aggrField) {
        List<EmployeeCountResponse> employeeCountResponses = new ArrayList<>();
        Map<String, SearchResponse> empSearchResponse = getResponseFromIndex(employeesDetailRequest, boolQry, aggrField);

        Map<String, EmployeeCountResponse> empCountRes = new HashMap<>();
        Map<String, EmployeeCountResponse> employeeCountRes ;

        StringTerms aggrEmp = empSearchResponse.get(TOTALEMPLOYEE).getAggregations().get(AGGRFIELD);
        StringTerms aggrMale = empSearchResponse.get(TOTALMALE).getAggregations().get(AGGRFIELD);
        StringTerms aggrFemale = empSearchResponse.get(TOTALFEMALE).getAggregations().get(AGGRFIELD);
        StringTerms aggrRegularEmp = empSearchResponse.get(TOTALREGULAREMPLOYEE).getAggregations().get(AGGRFIELD);
        StringTerms aggrRegularMale = empSearchResponse.get(TOTALREGULARMALE).getAggregations().get(AGGRFIELD);
        StringTerms aggrRegularFemale = empSearchResponse.get(TOTALREGULARFEMALE).getAggregations().get(AGGRFIELD);
        employeeCountRes = empCountRes;

        for (final Terms.Bucket entry : aggrEmp.getBuckets()) {
            Long maleCount = 0L;
            Long femaleCount = 0L;
            Long regularMaleCount = 0L;
            Long regularFemaleCount = 0L;
            Long regularEmpCount = 0L;

            String keyName = entry.getKeyAsString();
            final TopHits topHits = entry.getAggregations().get("employeerecords");

            EmployeeCountResponse empCountResponse = new EmployeeCountResponse();

            ValueCount aggregation = entry.getAggregations().get(EisConstants.EMPLOYEE_CODE);
            Long countEmp = aggregation.getValue();
            empCountResponse.setTotalEmployee(countEmp);
            empCountResponse.setTotalMale(getTotalCountForAggrField(aggrMale, keyName, maleCount));
            empCountResponse.setTotalFemale(getTotalCountForAggrField(aggrFemale, keyName, femaleCount));
            empCountResponse.setTotalRegularEmployee(getTotalCountForAggrField(aggrRegularEmp, keyName, regularEmpCount));
            empCountResponse.setTotalRegularMale(getTotalCountForAggrField(aggrRegularMale, keyName, regularMaleCount));
            empCountResponse.setTotalRegularFemale(getTotalCountForAggrField(aggrRegularFemale, keyName, regularFemaleCount));

            setValues(employeesDetailRequest, empCountResponse, topHits, aggrField, keyName);
            empCountRes.put(keyName, empCountResponse);
        }

        if (!employeeCountRes.isEmpty())
            for (EmployeeCountResponse response : employeeCountRes.values())
                employeeCountResponses.add(response);
        return employeeCountResponses;
    }

    private Long getTotalCountForAggrField(StringTerms aggrEmployeeData, String keyName, Long value) {
        for (final Terms.Bucket empEntry : aggrEmployeeData.getBuckets()) {
            if (keyName.equalsIgnoreCase(empEntry.getKeyAsString())) {
                ValueCount aggrValue = empEntry.getAggregations().get(EisConstants.EMPLOYEE_CODE);
                value = aggrValue.getValue();
            }
        }
        return value;
    }


    private Map<String, SearchResponse> getResponseFromIndex(EmployeeDetailRequest employeeDetailRequest, BoolQueryBuilder boolQuery, String aggrField) {

        Map<String, SearchResponse> empSearchResponse = new HashMap<>();
        if (StringUtils.isBlank(aggrField)) {
            aggrField = AGGRTYPE;
        }

        empSearchResponse.put(TOTALEMPLOYEE, getResponseFromIndexForTotalCount(boolQuery, aggrField));

        BoolQueryBuilder filterTypeQuery = boolQuery.must(QueryBuilders.matchQuery(EisConstants.EMPLOYEE_TYPE, EisConstants.EMPLOYEE_TYPE_PERMANENT));
        empSearchResponse.put(TOTALREGULAREMPLOYEE, getResponseFromIndexForTotalCount(filterTypeQuery, aggrField));

        filterTypeQuery.must(QueryBuilders.matchQuery(EisConstants.GENDER, MALE));
        empSearchResponse.put(TOTALREGULARMALE, getResponseFromIndexForTotalCount(filterTypeQuery, aggrField));

        BoolQueryBuilder boolQueryBuilder = EISDashBoardUtils.prepareWhereClause(employeeDetailRequest, QueryBuilders.boolQuery());
        boolQueryBuilder.must(QueryBuilders.matchQuery(EisConstants.EMPLOYEE_TYPE, EisConstants.EMPLOYEE_TYPE_PERMANENT))
                .must(QueryBuilders.matchQuery(EisConstants.GENDER, FEMALE));
        empSearchResponse.put(TOTALREGULARFEMALE, getResponseFromIndexForTotalCount(boolQueryBuilder, aggrField));

        BoolQueryBuilder filterMaleQuery = EISDashBoardUtils.prepareWhereClause(employeeDetailRequest, QueryBuilders.boolQuery());
        filterMaleQuery.must(QueryBuilders.matchQuery(EisConstants.GENDER, MALE));
        empSearchResponse.put(TOTALMALE, getResponseFromIndexForTotalCount(filterMaleQuery, aggrField));

        BoolQueryBuilder filterFemaleQuery = EISDashBoardUtils.prepareWhereClause(employeeDetailRequest, QueryBuilders.boolQuery());
        filterFemaleQuery.must(QueryBuilders.matchQuery(EisConstants.GENDER, FEMALE));
        empSearchResponse.put(TOTALFEMALE, getResponseFromIndexForTotalCount(filterFemaleQuery, aggrField));

        return empSearchResponse;
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
        if (employeeDetailRequest.getUlbCode() != null) {
            response.setUlbCode(employeeDetailRequest.getUlbCode());
        }

        setAggregationFieldValues(response, aggrField, topHits, keyName);
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
        if ("department".equalsIgnoreCase(aggrField)) {
            response.setDistrict(disName);
            response.setRegion(regName);
            response.setUlbName(ulbName);
            response.setGrade(ulbGrade);
            response.setUlbCode(keyName);
            response.setDepartment(department);
        }
    }
}