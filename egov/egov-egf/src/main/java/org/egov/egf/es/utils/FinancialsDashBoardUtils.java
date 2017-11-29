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

package org.egov.egf.es.utils;

import org.apache.commons.lang3.StringUtils;
import org.egov.egf.bean.dashboard.FinancialsBudgetDetailResponse;
import org.egov.egf.bean.dashboard.FinancialsDetailResponse;
import org.egov.egf.bean.dashboard.FinancialsDetailsRequest;
import org.egov.utils.FinancialConstants;
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
    private static final String ULBNAME = "ulbname";
    private static final String VOUCHER_DATE = "voucherdate";
    private static final String ADM_ZONE = "admZone";
    private static final String FUND_CODE = "voucherfundcode";
    private static final String DEPARTMENT_CODE = "vouchermisdepartmentcode";
    private static final String FUNCTION_CODE = "vouchermisfunctioncode";
    private static final String FUNDSOURCE_NAME = "vouchermisfundsourcename";
    private static final String SCHEME_CODE = "vouchermisschemecode";
    private static final String SUBSCHEME_CODE = "vouchermissubschemecode";
    private static final String MAJOR_CODE = "majorcode";
    private static final String MINOR_CODE = "minorcode";
    private static final String DETAILED_CODE = "glcode";
    private static final String DEPARTMENT = "department";
    private static final String MONTH = "month";
    private static final String FINANCIALYEAR = "financialyear";

    public static String getAggregationGroupingField(final FinancialsDetailsRequest financialsDetailsRequest) {
        String aggregationField = "";

        if (StringUtils.isNotBlank(financialsDetailsRequest.getAggregationLevel())) {
            aggregationField = setAggregateLevel(financialsDetailsRequest);
            if (FUND.equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = FUND_CODE;
            if (DEPARTMENT.equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = DEPARTMENT_CODE;
            if (FUNCTION.equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = FUNCTION_CODE;
            if ("fundsource".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = FUNDSOURCE_NAME;
            if ("scheme".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = SCHEME_CODE;
            if ("subscheme".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = SUBSCHEME_CODE;
            if ("month".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = MONTH;
            if ("financialyear".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = FINANCIALYEAR;


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
                    .filter(QueryBuilders.matchQuery(FUND_CODE, financialsDetailsRequest.getFundCode()));

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFundSource()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(FUNDSOURCE_NAME, financialsDetailsRequest.getFundSource()));

        if (StringUtils.isNotBlank(financialsDetailsRequest.getSchemeCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(SCHEME_CODE, financialsDetailsRequest.getSchemeCode()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getSubschemeCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(SUBSCHEME_CODE, financialsDetailsRequest.getSubschemeCode()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getFinancialYear()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(FINANCIALYEAR, financialsDetailsRequest.getFinancialYear()));


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
        if (StringUtils.isNotBlank(financialsDetailsRequest.getUlbCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(ULBCODE, financialsDetailsRequest.getUlbCode()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getUlbName()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(ULBNAME, financialsDetailsRequest.getUlbName()));
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
                    .filter(QueryBuilders.matchQuery(MAJOR_CODE, financialsDetailsRequest.getMajorCode().toLowerCase()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getMinorCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(MINOR_CODE, financialsDetailsRequest.getMinorCode().toLowerCase()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getAdmWard()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(ADM_WARD.toLowerCase(), financialsDetailsRequest.getAdmWard()));

        if (StringUtils.isNotBlank(financialsDetailsRequest.getAdmZone()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(ADM_ZONE.toLowerCase(), financialsDetailsRequest.getAdmZone()));
        return boolQuery;
    }

    public static String getOpeningBlncAggrGroupingField(final FinancialsDetailsRequest financialsDetailsRequest) {
        String aggregationField = "";
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
        String aggregationField = "";
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
            aggregationField = ADM_ZONE.toLowerCase();
        if ("admw".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            aggregationField = ADM_WARD.toLowerCase();
        if ("month".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            aggregationField = MONTH;
        return aggregationField;
    }

    public static void setValues(final String keyName, FinancialsDetailResponse financialsDetail, final String aggrField, final FinancialsDetailResponse finResponse) {

        if (DISTRICT.equalsIgnoreCase(aggrField)) {
            financialsDetail.setDistrict(keyName);
            financialsDetail.setRegion(finResponse.getRegion());
        }
        if (REGION.equalsIgnoreCase(aggrField)) {
            financialsDetail.setRegion(keyName);
        }
        if (GRADE.equalsIgnoreCase(aggrField)) {
            financialsDetail.setGrade(keyName);
        }
        if (ULBCODE.equalsIgnoreCase(aggrField)) {
            financialsDetail.setUlbCode(keyName);
            financialsDetail.setUlbName(finResponse.getUlbName());
            financialsDetail.setDistrict(finResponse.getDistrict());
            financialsDetail.setGrade(finResponse.getGrade());
            financialsDetail.setRegion(finResponse.getRegion());
        }
        if (MAJOR_CODE.equalsIgnoreCase(aggrField)) {
            financialsDetail.setMajorCode(keyName);
            financialsDetail.setMajorCodeDescription(finResponse.getMajorCodeDescription());
        }
        if (MINOR_CODE.equalsIgnoreCase(aggrField)) {
            financialsDetail.setMinorCode(keyName);
            financialsDetail.setMinorCodeDescription(finResponse.getMinorCodeDescription());
        }
        if (DETAILED_CODE.equalsIgnoreCase(aggrField)) {
            financialsDetail.setDetailedCode(keyName);
            financialsDetail.setDetailedCodeDescription(finResponse.getDetailedCodeDescription());
        }
        if (ADM_ZONE.equalsIgnoreCase(aggrField))
            financialsDetail.setAdmZoneName(keyName);
        if (ADM_WARD.equalsIgnoreCase(aggrField))
            financialsDetail.setAdmWardName(keyName);
        if (FUND_CODE.equalsIgnoreCase(aggrField)) {
            financialsDetail.setFundCode(keyName);
            financialsDetail.setFundName(finResponse.getFundName());
        }
        if (DEPARTMENT_CODE.equalsIgnoreCase(aggrField)) {
            financialsDetail.setDepartmentCode(keyName);
            financialsDetail.setDepartmentName(finResponse.getDepartmentName());
        }
        if (FUNCTION_CODE.equalsIgnoreCase(aggrField)) {
            financialsDetail.setFunctionCode(keyName);
            financialsDetail.setFunctionName(finResponse.getFunctionName());
        }
        if (FUNDSOURCE_NAME.equalsIgnoreCase(aggrField))
            financialsDetail.setFundSource(keyName);
        if (SCHEME_CODE.equalsIgnoreCase(aggrField)) {
            financialsDetail.setSchemeCode(keyName);
            financialsDetail.setSchemeName(finResponse.getSchemeName());
        }
        if (SUBSCHEME_CODE.equalsIgnoreCase(aggrField)) {
            financialsDetail.setSubschemeCode(keyName);
            financialsDetail.setSubschemeName(finResponse.getSubschemeName());
        }
    }

    public static BoolQueryBuilder prepareWhereClauseForBudget(final FinancialsDetailsRequest financialsDetailsRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery = prepareWhereCluase(financialsDetailsRequest, boolQuery);
        prepairWhereClauseForScheme(financialsDetailsRequest, boolQuery);
        if (StringUtils.isNotBlank(financialsDetailsRequest.getDepartmentCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(FinancialConstants.DEPARTMENT_CODE.toLowerCase(),
                            financialsDetailsRequest.getDepartmentCode()));

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFromDate())
                || StringUtils.isNotBlank(financialsDetailsRequest.getToDate()))
            boolQuery
                    .filter(QueryBuilders.rangeQuery(FinancialConstants.BUDGETDETAILCREATEDDATE.toLowerCase())
                            .from(financialsDetailsRequest.getFromDate()).to(financialsDetailsRequest.getToDate()));

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFunctionCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(FinancialConstants.FUNCTION_CODE.toLowerCase(),
                            financialsDetailsRequest.getFunctionCode()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getFundCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(FinancialConstants.FUND_CODE.toLowerCase(), financialsDetailsRequest.getFundCode()));

        return boolQuery;
    }

    private static void prepairWhereClauseForScheme(final FinancialsDetailsRequest financialsDetailsRequest,
                                                    BoolQueryBuilder boolQuery) {
        if (StringUtils.isNotBlank(financialsDetailsRequest.getSchemeCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(FinancialConstants.SCHEME_CODE.toLowerCase(), financialsDetailsRequest.getSchemeCode()));
        if (StringUtils.isNotBlank(financialsDetailsRequest.getSubschemeCode()))
            boolQuery
                    .filter(QueryBuilders.matchQuery(FinancialConstants.SUBSCHEME_CODE.toLowerCase(),
                            financialsDetailsRequest.getSubschemeCode()));
    }

    public static void setValuesForBudget(final String keyName, FinancialsBudgetDetailResponse financialsBudgetDetailResponse,
                                          final String aggrField, final FinancialsBudgetDetailResponse finResponse) {

        setCommonDataToResponse(keyName, financialsBudgetDetailResponse, aggrField, finResponse);
        setFinancialsDataToResponse(keyName, financialsBudgetDetailResponse, aggrField, finResponse);
        setChartOfAccountToResponse(keyName, financialsBudgetDetailResponse, aggrField, finResponse);
        if (ADM_ZONE.equalsIgnoreCase(aggrField))
            financialsBudgetDetailResponse.setAdmZoneName(keyName);
        if (ADM_WARD.equalsIgnoreCase(aggrField))
            financialsBudgetDetailResponse.setAdmWardName(keyName);
        if (FinancialConstants.DEPARTMENT_CODE.equalsIgnoreCase(aggrField)) {
            financialsBudgetDetailResponse.setDepartmentCode(keyName);
            financialsBudgetDetailResponse.setDepartmentName(finResponse.getDepartmentName());
        }
        if (FinancialConstants.SCHEME_CODE.equalsIgnoreCase(aggrField)) {
            financialsBudgetDetailResponse.setSchemeCode(keyName);
            financialsBudgetDetailResponse.setSchemeName(finResponse.getSchemeName());
        }
    }

    private static void setFinancialsDataToResponse(final String keyName,
                                                    FinancialsBudgetDetailResponse financialsBudgetDetailResponse, final String aggrField,
                                                    final FinancialsBudgetDetailResponse finResponse) {
        if (FinancialConstants.FUND_CODE.equalsIgnoreCase(aggrField)) {
            financialsBudgetDetailResponse.setFundCode(keyName);
            financialsBudgetDetailResponse.setFundName(finResponse.getFundName());
        }
        if (FinancialConstants.FUNCTION_CODE.equalsIgnoreCase(aggrField)) {
            financialsBudgetDetailResponse.setFunctionCode(keyName);
            financialsBudgetDetailResponse.setFunctionName(finResponse.getFunctionName());
        }
        if (FinancialConstants.SUBSCHEME_CODE.equalsIgnoreCase(aggrField)) {
            financialsBudgetDetailResponse.setSubschemeCode(keyName);
            financialsBudgetDetailResponse.setSubschemeName(finResponse.getSubschemeName());
        }
    }

    private static void setChartOfAccountToResponse(final String keyName,
                                                    FinancialsBudgetDetailResponse financialsBudgetDetailResponse, final String aggrField
            , final FinancialsBudgetDetailResponse finBudResponse) {
        if (MAJOR_CODE.equalsIgnoreCase(aggrField)) {
            financialsBudgetDetailResponse.setMajorCode(keyName);
            financialsBudgetDetailResponse.setMajorCodeDescription(finBudResponse.getMajorCodeDescription());
        }
        if (MINOR_CODE.equalsIgnoreCase(aggrField)) {
            financialsBudgetDetailResponse.setMinorCode(keyName);
            financialsBudgetDetailResponse.setMinorCodeDescription(finBudResponse.getMinorCodeDescription());
        }
        if (DETAILED_CODE.equalsIgnoreCase(aggrField)) {
            financialsBudgetDetailResponse.setDetailedCode(keyName);
            financialsBudgetDetailResponse.setDetailedCodeDescription(finBudResponse.getDetailedCodeDescription());
        }
    }

    private static void setCommonDataToResponse(final String keyName,
                                                FinancialsBudgetDetailResponse financialsBudgetDetailResponse, final String aggrField,
                                                final FinancialsBudgetDetailResponse finResponse) {
        if (DISTRICT.equalsIgnoreCase(aggrField)) {
            financialsBudgetDetailResponse.setDistrict(keyName);
            financialsBudgetDetailResponse.setRegion(finResponse.getRegion());
        }
        if (REGION.equalsIgnoreCase(aggrField)) {
            financialsBudgetDetailResponse.setRegion(keyName);
        }
        if (GRADE.equalsIgnoreCase(aggrField)) {
            financialsBudgetDetailResponse.setGrade(keyName);
        }
        if (ULBCODE.equalsIgnoreCase(aggrField)) {
            financialsBudgetDetailResponse.setUlbCode(keyName);
            financialsBudgetDetailResponse.setUlbName(finResponse.getUlbName());
            financialsBudgetDetailResponse.setDistrict(finResponse.getDistrict());
            financialsBudgetDetailResponse.setGrade(finResponse.getGrade());
            financialsBudgetDetailResponse.setRegion(finResponse.getRegion());
        }
    }

    public static String getAggregationGroupingFieldForBudget(final FinancialsDetailsRequest financialsDetailsRequest) {
        String aggregationField = "";
        if (StringUtils.isNotBlank(financialsDetailsRequest.getAggregationLevel())) {
            aggregationField = setAggregateLevel(financialsDetailsRequest);
            if (DEPARTMENT.equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
                aggregationField = FinancialConstants.DEPARTMENT_CODE.toLowerCase();
            aggregationField = aggregateFinancialFields(financialsDetailsRequest, aggregationField);

        }
        return aggregationField;
    }

    private static String aggregateFinancialFields(final FinancialsDetailsRequest financialsDetailsRequest,
                                                   String aggregationField) {
        if (FUND.equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            aggregationField = FinancialConstants.FUND_CODE.toLowerCase();
        if (FUNCTION.equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            aggregationField = FinancialConstants.FUNCTION_CODE.toLowerCase();
        if ("scheme".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            aggregationField = FinancialConstants.SCHEME_CODE.toLowerCase();
        if ("subscheme".equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            aggregationField = FinancialConstants.SUBSCHEME_CODE.toLowerCase();
        return aggregationField;
    }

}
