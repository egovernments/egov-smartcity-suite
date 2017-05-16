/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
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
 *           Further, all user interfaces, including but not limited to citizen facing interfaces,
 *           Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *           derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	       For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	       For any further queries on attribution, including queries on brand guidelines,
 *           please contact contact@egovernments.org
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

package org.egov.egf.es.utils;

import org.apache.commons.lang3.StringUtils;
import org.egov.egf.bean.dashboard.FinancialsDetailResponse;
import org.egov.egf.bean.dashboard.FinancialsDetailsRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class FinancialsDashBoardUtils {

    public static final String FUND = "fund";
    public static final String FUNCTION = "function";
    private static final String ADM_WARD = "admWard";
    private static final String REGION = "regname";
    private static final String DISTRICT = "distname";
    private static final String GRADE = "ulbgrade";
    private static final String ULBCODE = "ulbcode";
    private static final String VOUCHER_DATE = "vouchereffectivedate";
    private static final String ADM_ZONE = "admZone";
    private static final String FUND_NAME = "voucherfundname";
    private static final String DEPARTMENT_CODE = "vouchermisdepartmentname";
    private static final String FUNCTION_CODE = "vouchermisfunctionname";
    private static final String FUNDSOURCE_CODE = "vouchermisfundsourcename";
    private static final String SCHEME_CODE = "vouchermisschemename";
    private static final String SUBSCHEME_CODE = "vouchermissubschemename";
    private static final String MAJOR_CODE = "majorcode";
    private static final String MINOR_CODE = "minorcode";
    private static final String DETAILED_CODE = "glcode";
    private static final String DEPARTMENT = "department";

    public static String getAggregationGroupingField(final FinancialsDetailsRequest financialsDetailsRequest) {
        String aggregationField = DISTRICT;

        if (StringUtils.isNotBlank(financialsDetailsRequest.getAggregationLevel())) {
            aggregationField = setAggregateLevel(financialsDetailsRequest);
            if (FUND.equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = FUND_NAME;
            if (DEPARTMENT.equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = DEPARTMENT_CODE;
            if (FUNCTION.equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = FUNCTION_CODE;
            if ("fundsource".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = FUNDSOURCE_CODE;
            if ("scheme".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = SCHEME_CODE;
            if ("subscheme".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = SUBSCHEME_CODE;


        }

        return aggregationField;
    }

    public static BoolQueryBuilder prepareWhereClause(final FinancialsDetailsRequest financialsDetailsRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery = prepareWhereCluase(financialsDetailsRequest, boolQuery);
        if (StringUtils.isNotBlank(financialsDetailsRequest.getDepartmentCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(DEPARTMENT_CODE, financialsDetailsRequest.getDepartmentCode()));


        if (StringUtils.isNotBlank(financialsDetailsRequest.getFromDate()) || StringUtils.isNotBlank(financialsDetailsRequest.getToDate()))
            boolQuery
                    .filter(QueryBuilders.rangeQuery(VOUCHER_DATE)
                            .from(financialsDetailsRequest.getFromDate()).to(financialsDetailsRequest.getToDate()));

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFunctionCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(FUNCTION_CODE, financialsDetailsRequest.getFunctionCode()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getFundCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(FUND_NAME, financialsDetailsRequest.getFundCode()));

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFundSource()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(FUNDSOURCE_CODE, financialsDetailsRequest.getFundSource()));

        if (StringUtils.isNotBlank(financialsDetailsRequest.getSchemeCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(SCHEME_CODE, financialsDetailsRequest.getSchemeCode()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getSubschemeCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(SUBSCHEME_CODE, financialsDetailsRequest.getSubschemeCode()));


        return boolQuery;
    }

    public static BoolQueryBuilder prepareOpeningBlncWhereClause(final FinancialsDetailsRequest financialsDetailsRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery = prepareWhereCluase(financialsDetailsRequest, boolQuery);
        if (StringUtils.isNotBlank(financialsDetailsRequest.getFundCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery("fundname", financialsDetailsRequest.getFundCode()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getDepartmentCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery("departmentname", financialsDetailsRequest.getDepartmentCode()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getFunctionCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery("functionname", financialsDetailsRequest.getFunctionCode()));
        return boolQuery;
    }

    private static BoolQueryBuilder prepareWhereCluase(FinancialsDetailsRequest financialsDetailsRequest, BoolQueryBuilder boolQuery) {
        if (StringUtils.isNotBlank(financialsDetailsRequest.getAdmWard()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(ULBCODE, financialsDetailsRequest.getUlbCode()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getDistrict()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(DISTRICT, financialsDetailsRequest.getDistrict()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getRegion()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(REGION, financialsDetailsRequest.getRegion()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getGrade()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(GRADE, financialsDetailsRequest.getGrade()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getDetailedCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(DETAILED_CODE, financialsDetailsRequest.getDetailedCode()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getMajorCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(MAJOR_CODE, financialsDetailsRequest.getMajorCode()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getMinorCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(MINOR_CODE, financialsDetailsRequest.getMinorCode()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getAdmWard()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(ADM_WARD, financialsDetailsRequest.getAdmWard()));

        if (StringUtils.isNotBlank(financialsDetailsRequest.getAdmZone()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(ADM_ZONE, financialsDetailsRequest.getAdmZone()));
        return boolQuery;
    }

    public static String getOpeningBlncAggrGroupingField(final FinancialsDetailsRequest financialsDetailsRequest) {
        String aggregationField = DISTRICT;
        if (StringUtils.isNotBlank(financialsDetailsRequest.getAggregationLevel())) {
            aggregationField = setAggregateLevel(financialsDetailsRequest);
            if (FUND.equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = "fundname";
            if (DEPARTMENT.equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = "departmentname";
            if (FUNCTION.equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = "functionname";


        }

        return aggregationField;
    }

    private static String setAggregateLevel(FinancialsDetailsRequest financialsDetailsRequest) {
        String aggregationField = DISTRICT;
        if ("district".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            aggregationField = DISTRICT;
        if ("region".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            aggregationField = REGION;
        if ("grade".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            aggregationField = GRADE;
        if ("ulb".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            aggregationField = ULBCODE;
        if ("majorCode".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            aggregationField = MAJOR_CODE;
        if ("minorCode".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            aggregationField = MINOR_CODE;
        if ("detailedCode".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            aggregationField = DETAILED_CODE;
        if ("admz".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            aggregationField = ADM_ZONE;
        if ("admw".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            aggregationField = ADM_WARD;
        return aggregationField;
    }

    public static void setValues(String keyName, FinancialsDetailResponse financialsDetail, String aggrField) {

        if (DISTRICT.equalsIgnoreCase(aggrField))
            financialsDetail.setDistrict(keyName);
        if (REGION.equalsIgnoreCase(aggrField))
            financialsDetail.setRegion(keyName);
        if (GRADE.equalsIgnoreCase(aggrField))
            financialsDetail.setGrade(keyName);
        if (ULBCODE.equalsIgnoreCase(aggrField))
            financialsDetail.setUlbName(keyName);
        if (MAJOR_CODE.equalsIgnoreCase(aggrField))
            financialsDetail.setMajorCode(keyName);
        if (MINOR_CODE.equalsIgnoreCase(aggrField))
            financialsDetail.setMinorCode(keyName);
        if (DETAILED_CODE.equalsIgnoreCase(aggrField))
            financialsDetail.setDetailedCode(keyName);
        if (ADM_ZONE.equalsIgnoreCase(aggrField))
            financialsDetail.setAdmZoneName(keyName);
        if (ADM_WARD.equalsIgnoreCase(aggrField))
            financialsDetail.setAdmWardName(keyName);
        if (FUND_NAME.equalsIgnoreCase(aggrField))
            financialsDetail.setFundCode(keyName);
        if (DEPARTMENT_CODE.equalsIgnoreCase(aggrField))
            financialsDetail.setDepartmentCode(keyName);
        if (FUNCTION_CODE.equalsIgnoreCase(aggrField))
            financialsDetail.setFunctionCode(keyName);
        if (FUNDSOURCE_CODE.equalsIgnoreCase(aggrField))
            financialsDetail.setFundSource(keyName);
        if (SCHEME_CODE.equalsIgnoreCase(aggrField))
            financialsDetail.setSchemeCode(keyName);
        if (SUBSCHEME_CODE.equalsIgnoreCase(aggrField))
            financialsDetail.setSubschemeCode(keyName);
    }
}
