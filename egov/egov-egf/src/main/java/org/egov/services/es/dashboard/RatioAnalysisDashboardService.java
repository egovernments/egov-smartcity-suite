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

package org.egov.services.es.dashboard;

import org.egov.egf.bean.dashboard.FinancialsRatioAnalysisRequest;
import org.egov.egf.bean.dashboard.FinancialsRatioAnalysisResponse;
import org.egov.infra.utils.StringUtils;
import org.egov.utils.FinancialConstants;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RatioAnalysisDashboardService {

    private static final String DEBITAMOUNT = "gldebitamount";
    private static final String CREDITAMOUNT = "glcreditamount";
    private static final String AGGRFIELD = "aggrField";
    private static final String MAJOR_CODE = "majorcode";
    private static final String AGGRTYPE = "_type";
    private static final String VOUCHER_DATE = "voucherdate";
    private static final String MONTH = "month";
    private static final String TOPHIT = "topHit";
    private static final String MAJORCODEWISE = "majorCodeWise";
    private static final String TOPHITS = "tophit1";


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    public List<FinancialsRatioAnalysisResponse> getRatios(FinancialsRatioAnalysisRequest financialsRatioAnalysisRequest,
                                                           BoolQueryBuilder boolQuery, String aggrField) {

        List<FinancialsRatioAnalysisResponse> ratioAnalysisResponses = new ArrayList<>();
        SearchResponse finSearchResponse = getDataFromIndex(boolQuery, aggrField);
        StringTerms aggr = finSearchResponse.getAggregations().get(AGGRFIELD);


        for (final Terms.Bucket entry : aggr.getBuckets()) {
            StringTerms aggr1 = entry.getAggregations().get(MAJORCODEWISE);
            Double totalIncomeIAmount = 0.0;
            Double totalExpenseAmount = 0.0;
            String keyName = entry.getKeyAsString();
            final TopHits topHits = entry.getAggregations().get(TOPHIT);
            for (final Terms.Bucket majorCodeEntry : aggr1.getBuckets()) {
                final Sum debit = majorCodeEntry.getAggregations().get(DEBITAMOUNT);
                final Sum credit = majorCodeEntry.getAggregations().get(CREDITAMOUNT);
                final TopHits topHits1 = majorCodeEntry.getAggregations().get(TOPHITS);
                final SearchHit[] hit = topHits1.getHits().getHits();

                String majorCode = hit[0].field(MAJOR_CODE).getValue();
                if (majorCode.startsWith("1")) {
                    totalIncomeIAmount += (Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue()));
                }
                if (majorCode.startsWith("2")) {
                    totalExpenseAmount += (Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue()));
                }
            }
            FinancialsRatioAnalysisResponse response = new FinancialsRatioAnalysisResponse();
            for (final Terms.Bucket majorCodeEntry : aggr1.getBuckets()) {
                final Sum debit = majorCodeEntry.getAggregations().get(DEBITAMOUNT);
                final Sum credit = majorCodeEntry.getAggregations().get(CREDITAMOUNT);
                final TopHits topHits1 = majorCodeEntry.getAggregations().get(TOPHITS);
                final SearchHit[] hit = topHits1.getHits().getHits();

                String majorCode = hit[0].field(MAJOR_CODE).getValue();

                if (majorCode.equalsIgnoreCase("110")) {
                    response.setTaxRevenueToTotalIncomeRatio(((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) / totalIncomeIAmount) * 100);
                }
                if (majorCode.equalsIgnoreCase("120")) {
                    response.setAssignedRevenuesToTotalIncomeRatio(((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) / totalIncomeIAmount) * 100);
                }
                if (majorCode.equalsIgnoreCase("130")) {
                    response.setRentalIncomeToTotalIncomeRatio(((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) / totalIncomeIAmount) * 100);
                }
                if (majorCode.equalsIgnoreCase("140")) {
                    response.setFeeUserChargesToTotalIncomeRatio(((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) / totalIncomeIAmount) * 100);
                }
                if (majorCode.equalsIgnoreCase("160")) {
                    response.setRevenueGrantsToTotalIncomeRatio(((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) / totalIncomeIAmount) * 100);
                }
                if (majorCode.equalsIgnoreCase("210")) {
                    response.setEstablishmentExpensesToTotalReRatio(((Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue())) / totalExpenseAmount) * 100);
                }
                if (majorCode.equalsIgnoreCase("220")) {
                    response.setAdministrativeExpensesToTotalReRatio(((Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue())) / totalExpenseAmount) * 100);
                }
                if (majorCode.equalsIgnoreCase("230")) {
                    response.setOmExpensesToTotalReRatio(((Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue())) / totalExpenseAmount) * 100);
                }


            }
            response.setIncomeRatio(response.getTaxRevenueToTotalIncomeRatio() + response.getAssignedRevenuesToTotalIncomeRatio() +
                    response.getRentalIncomeToTotalIncomeRatio() + response.getFeeUserChargesToTotalIncomeRatio() + response.getRevenueGrantsToTotalIncomeRatio());
            response.setExpenseRatio(response.getEstablishmentExpensesToTotalReRatio() + response.getAdministrativeExpensesToTotalReRatio() +
                    response.getOmExpensesToTotalReRatio());
            setValues(financialsRatioAnalysisRequest, response, topHits, aggrField, keyName);
            ratioAnalysisResponses.add(response);

        }
        return ratioAnalysisResponses;

    }


    private SearchResponse getDataFromIndex(BoolQueryBuilder boolQry, String aggrField) {
        boolQry.filter(QueryBuilders.matchQuery("voucherstatusid", FinancialConstants.CREATEDVOUCHERSTATUS));
        if (StringUtils.isBlank(aggrField)) {
            aggrField = AGGRTYPE;
        }
        if (!MONTH.equalsIgnoreCase(aggrField)) {
            return elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME).setQuery(boolQry)
                    .addAggregation(AggregationBuilders.terms(AGGRFIELD).field(aggrField).size(5000)
                            .subAggregation(AggregationBuilders.terms(MAJORCODEWISE).field(MAJOR_CODE).size(5000)
                                    .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                                    .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT)).subAggregation(AggregationBuilders.topHits(TOPHITS).addField(MAJOR_CODE)))
                            .subAggregation(AggregationBuilders.topHits(TOPHIT).addField(FinancialConstants.DISTNAME)
                                    .addField(FinancialConstants.ULBNAME)
                                    .addField(FinancialConstants.ULBGRADE).addField(FinancialConstants.REGNAME)
                                    .addField(FinancialConstants.VOUCHERFUNDNAME).addField(FinancialConstants.VOUCHERMISFUNCTIONNAME)
                                    .addField(FinancialConstants.VOUCHERMISDEPARTMENTNAME).setSize(1)))
                    .execute().actionGet();
        } else {
            return elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME).setQuery(boolQry)
                    .addAggregation(AggregationBuilders.dateHistogram(AGGRFIELD).field(VOUCHER_DATE)
                            .subAggregation(AggregationBuilders.terms(MAJORCODEWISE).field(MAJOR_CODE).size(5000)
                                    .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                                    .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT))).subAggregation(AggregationBuilders.topHits(TOPHITS).addField(MAJOR_CODE)).interval(DateHistogramInterval.MONTH)
                            .subAggregation(AggregationBuilders.topHits(TOPHIT).addField(FinancialConstants.DISTNAME)
                                    .addField(FinancialConstants.ULBNAME)
                                    .addField(FinancialConstants.ULBGRADE).addField(FinancialConstants.REGNAME)
                                    .addField(FinancialConstants.VOUCHERFUNDNAME).addField(FinancialConstants.VOUCHERMISFUNCTIONNAME)
                                    .addField(FinancialConstants.VOUCHERMISDEPARTMENTNAME).setSize(1)))
                    .execute().actionGet();
        }
    }


    private void setValues(FinancialsRatioAnalysisRequest financialsRatioAnalysisRequest, FinancialsRatioAnalysisResponse response, TopHits topHits,
                           String aggrField, String keyName) {


        if (financialsRatioAnalysisRequest.getRegion() != null) {
            response.setRegion(financialsRatioAnalysisRequest.getRegion());
        }
        if (financialsRatioAnalysisRequest.getDistrict() != null) {
            response.setDistrict(financialsRatioAnalysisRequest.getDistrict());
        }
        if (financialsRatioAnalysisRequest.getUlbName() != null) {
            response.setUlbName(financialsRatioAnalysisRequest.getUlbName());
        }
        if (financialsRatioAnalysisRequest.getUlbCode() != null) {
            response.setUlbCode(financialsRatioAnalysisRequest.getUlbCode());
        }
        if (financialsRatioAnalysisRequest.getFunctionCode() != null) {
            response.setFunctionCode(financialsRatioAnalysisRequest.getFunctionCode());
        }
        if (financialsRatioAnalysisRequest.getFundCode() != null) {
            response.setFundCode(financialsRatioAnalysisRequest.getFundCode());
        }
        if (financialsRatioAnalysisRequest.getDepartmentCode() != null) {
            response.setDepartmentCode(financialsRatioAnalysisRequest.getDepartmentCode());
        }
        if (financialsRatioAnalysisRequest.getFinancialYear() != null) {
            response.setFinancialYear(financialsRatioAnalysisRequest.getFinancialYear());
        }
        if (financialsRatioAnalysisRequest.getGrade() != null) {
            response.setGrade(financialsRatioAnalysisRequest.getGrade());
        }
        if (financialsRatioAnalysisRequest.getAdmWard() != null) {
            response.setAdmWardName(financialsRatioAnalysisRequest.getAdmWard());
        }
        if (financialsRatioAnalysisRequest.getAdmZone() != null) {
            response.setAdmZoneName(financialsRatioAnalysisRequest.getAdmZone());
        }


        setAggreationField(response, aggrField, topHits, keyName);
    }

    private void setAggreationField(FinancialsRatioAnalysisResponse response, String aggrField, TopHits topHits, String keyName) {
        final SearchHit[] hit = topHits.getHits().getHits();

        String regName = hit[0].field(FinancialConstants.REGNAME).getValue();
        String disName = hit[0].field(FinancialConstants.DISTNAME).getValue();
        String ulbGrade = hit[0].field(FinancialConstants.ULBGRADE).getValue();
        String ulbName = hit[0].field(FinancialConstants.ULBNAME).getValue();
        String fund = hit[0].field(FinancialConstants.VOUCHERFUNDNAME) == null ? "" : hit[0].field(FinancialConstants.VOUCHERFUNDNAME).getValue();
        String function = hit[0].field(FinancialConstants.VOUCHERMISFUNCTIONNAME) == null ? "" : hit[0].field(FinancialConstants.VOUCHERMISFUNCTIONNAME).getValue();
        String department = hit[0].field(FinancialConstants.VOUCHERMISDEPARTMENTNAME) == null ? "" : hit[0].field(FinancialConstants.VOUCHERMISDEPARTMENTNAME).getValue();
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
            response.setUlbGrade(ulbGrade);
            response.setUlbCode(keyName);
        }
        if ("voucherfundcode".equalsIgnoreCase(aggrField)) {
            response.setFundCode(keyName);
            response.setFundName(fund);
        }
        if ("vouchermisfunctioncode".equalsIgnoreCase(aggrField)) {
            response.setFunctionCode(keyName);
            response.setFunctionName(function);
        }
        if ("vouchermisdepartmentcode".equalsIgnoreCase(aggrField)) {
            response.setDepartmentCode(keyName);
            response.setDepartmentName(department);
        }
        if ("financialyear".equalsIgnoreCase(aggrField)) {
            response.setFinancialYear(keyName);
        }
        if ("month".equalsIgnoreCase(aggrField)) {
            response.setFinancialYear(keyName);
        }
    }

}