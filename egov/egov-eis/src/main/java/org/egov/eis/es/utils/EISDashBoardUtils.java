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

package org.egov.eis.es.utils;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.es.dashboard.EmployeeDetailRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Date;

import static org.egov.infra.utils.DateUtils.getFormattedDate;

public class EISDashBoardUtils {

    private static final String REGION = "regname";
    private static final String DISTRICT = "distname";
    private static final String GRADE = "ulbgrade";
    private static final String ULBCODE = "ulbcode";
    private static final String DEPARTMENT = "department";
    private static final String ASSIGN_DATE_FORMAT = "yyyy-MM-dd";


    public static String getAggregationGroupingField(final EmployeeDetailRequest employeeDetailsRequest) {
        String aggregationField = REGION;

        if (StringUtils.isNotBlank(employeeDetailsRequest.getAggregationLevel())) {
            if ("district".equalsIgnoreCase(employeeDetailsRequest.getAggregationLevel()))
                aggregationField = DISTRICT;
            if ("region".equalsIgnoreCase(employeeDetailsRequest.getAggregationLevel()))
                aggregationField = REGION;
            if ("grade".equalsIgnoreCase(employeeDetailsRequest.getAggregationLevel()))
                aggregationField = GRADE;
            if ("ulb".equalsIgnoreCase(employeeDetailsRequest.getAggregationLevel()))
                aggregationField = ULBCODE;
            if ("department".equalsIgnoreCase(employeeDetailsRequest.getAggregationLevel()))
                aggregationField = DEPARTMENT;
        }
        return aggregationField;
    }

    public static BoolQueryBuilder prepareWhereClauseForEmployees(final EmployeeDetailRequest employeesDetailRequest) {
        Date date = new Date();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery = prepareWhereClause(employeesDetailRequest, boolQuery);
        boolQuery.must(QueryBuilders.matchQuery("primaryassignment", true))
                .must(QueryBuilders.rangeQuery("fromdate").lte(getFormattedDate(date, ASSIGN_DATE_FORMAT)).includeUpper(false))
                .must(QueryBuilders.rangeQuery("todate").gte(getFormattedDate(date, ASSIGN_DATE_FORMAT)).includeUpper(false));

        return boolQuery;
    }

    public static BoolQueryBuilder prepareWhereClause(EmployeeDetailRequest employeesDetailRequest, BoolQueryBuilder boolQuery) {
        if (StringUtils.isNotBlank(employeesDetailRequest.getUlbCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(ULBCODE, employeesDetailRequest.getUlbCode()));
        if (StringUtils.isNotBlank(employeesDetailRequest.getDistrict()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(DISTRICT, employeesDetailRequest.getDistrict()));
        if (StringUtils.isNotBlank(employeesDetailRequest.getRegion()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(REGION, employeesDetailRequest.getRegion()));
        if (StringUtils.isNotBlank(employeesDetailRequest.getGrade()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(GRADE, employeesDetailRequest.getGrade()));
        if (StringUtils.isNotBlank(employeesDetailRequest.getDepartmentName())) {
            boolQuery
                    .filter(QueryBuilders.matchQuery(DEPARTMENT, employeesDetailRequest.getDepartmentName()));
        }
        return boolQuery;
    }
}
