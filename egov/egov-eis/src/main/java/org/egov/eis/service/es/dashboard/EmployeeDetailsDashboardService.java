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

import org.egov.eis.es.dashboard.EmployeeDetailResponse;
import org.egov.eis.utils.constants.EisConstants;
import org.egov.infra.utils.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeDetailsDashboardService {

    private static final String EMPNAME = "name";
    private static final String MOBILENUMBER = "mobilenumber";
    private static final String DESIGNATION = "designation";
    private static final String FROMDATE = "fromdate";
    private static final String TODATE = "todate";
    private static final String DATEOFAPPOINTMENT = "dateofappointment";
    private static final String DATEOFRETIREMENT = "dateofretirement";

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public List<EmployeeDetailResponse> getEmployeeDetails(BoolQueryBuilder boolQuery) {
        List<EmployeeDetailResponse> employeeDetailResponses = new ArrayList<>();
        List<Map> empDetails = new ArrayList<>();
        EmployeeDetailResponse employeeDetailResponse;

        SearchResponse response = elasticsearchTemplate.getClient()
                .prepareSearch(EisConstants.EMPLOYEE_INDEX_NAME)
                .setQuery(boolQuery)
                .execute().actionGet();

        int size = (int) response.getHits().totalHits();
        response = elasticsearchTemplate.getClient()
                .prepareSearch(EisConstants.EMPLOYEE_INDEX_NAME)
                .setQuery(boolQuery).setSize(size)
                .setFetchSource(new String[]{EisConstants.EMPLOYEE_CODE, EMPNAME, EisConstants.EMPLOYEE_TYPE, MOBILENUMBER, EisConstants.GENDER, EisConstants.DEPARTMENT_NAME,
                        DESIGNATION, FROMDATE, TODATE, DATEOFAPPOINTMENT, DATEOFRETIREMENT,
                        EisConstants.REGNAME, EisConstants.DISTNAME, EisConstants.ULBGRADE, EisConstants.ULBNAME, EisConstants.ULBCODE}, null)
                .execute().actionGet();

        for (SearchHit hit : response.getHits())
            empDetails.add(hit.sourceAsMap());

        if (!empDetails.isEmpty()) {
            for (Map details : empDetails) {
                employeeDetailResponse = new EmployeeDetailResponse();
                employeeDetailResponse.setRegion(details.get(EisConstants.REGNAME) == null ? StringUtils.EMPTY : details.get(EisConstants.REGNAME).toString());
                employeeDetailResponse.setDistrict(details.get(EisConstants.DISTNAME) == null ? StringUtils.EMPTY : details.get(EisConstants.DISTNAME).toString());
                employeeDetailResponse.setGrade(details.get(EisConstants.ULBGRADE) == null ? StringUtils.EMPTY : details.get(EisConstants.ULBGRADE).toString());
                employeeDetailResponse.setUlbName(details.get(EisConstants.ULBNAME) == null ? StringUtils.EMPTY : details.get(EisConstants.ULBNAME).toString());
                employeeDetailResponse.setUlbCode(details.get(EisConstants.ULBCODE) == null ? StringUtils.EMPTY : details.get(EisConstants.ULBCODE).toString());
                employeeDetailResponse.setEmployeeName(details.get(EMPNAME).toString());
                employeeDetailResponse.setEmployeeCode(details.get(EisConstants.EMPLOYEE_CODE).toString());
                employeeDetailResponse.setEmployeeType(details.get(EisConstants.EMPLOYEE_TYPE).toString());
                employeeDetailResponse.setEmployeeMobile(details.get(MOBILENUMBER) == null ? StringUtils.EMPTY : details.get(MOBILENUMBER).toString());
                employeeDetailResponse.setGender(details.get(EisConstants.GENDER) == null ? StringUtils.EMPTY : details.get(EisConstants.GENDER).toString());
                employeeDetailResponse.setDepartment(details.get(EisConstants.DEPARTMENT_NAME) == null ? StringUtils.EMPTY : details.get(EisConstants.DEPARTMENT_NAME).toString());
                employeeDetailResponse.setDesignation(details.get(DESIGNATION) == null ? StringUtils.EMPTY : details.get(DESIGNATION).toString());
                employeeDetailResponse.setFromDate(details.get(FROMDATE).toString());
                employeeDetailResponse.setToDate(details.get(TODATE).toString());
                employeeDetailResponse.setDateOfJoining(details.get(DATEOFAPPOINTMENT) == null ? StringUtils.EMPTY : details.get(DATEOFAPPOINTMENT).toString());
                employeeDetailResponse.setDateOfRetirement(details.get(DATEOFRETIREMENT) == null ? StringUtils.EMPTY : details.get(DATEOFRETIREMENT).toString());
                employeeDetailResponses.add(employeeDetailResponse);
            }
        }
        return employeeDetailResponses;
    }

}