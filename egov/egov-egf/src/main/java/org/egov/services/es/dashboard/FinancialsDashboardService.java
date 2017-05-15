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

import org.egov.egf.bean.dashboard.FinancialsDetail;
import org.egov.egf.bean.dashboard.FinancialsDetailsRequest;
import org.egov.egf.es.utils.FinancialsDashBoardUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.utils.FinancialConstants;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinancialsDashboardService {

    private static final String DEBITAMOUNT = "gldebitamount";
    private static final String CREDITAMOUNT = "glcreditamount";
    private static final String OBDEBITAMOUNT = "openingdebitbalance";
    private static final String OBCREDITAMOUNT = "openingcreditbalance";
    private static final String INCOME = "I";
    private static final String EXPENSE = "E";
    private static final String ASSETS = "A";
    private static final String LIABILITIES = "L";
    private static final String DETAILED_CODE = "glcode";
    private static final String FIN_RECORDS = "finRecords";
    private static final String DISTRICT_NAME = "distname";
    private static final String ULB_NAME = "ulbname";
    private static final String AGGRFIELD = "aggrField";
    private static final String OBAGGRFIELD = "obAggrField";

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public List<FinancialsDetail> getFinancialsData(final FinancialsDetailsRequest financialsDetailsRequest,
                                                    final BoolQueryBuilder boolQuery, final String aggrField) {
        return getfinDetails(financialsDetailsRequest, boolQuery, aggrField);

    }

    private List<FinancialsDetail> getfinDetails(final FinancialsDetailsRequest financialsDetailsRequest,
                                                 final BoolQueryBuilder boolQuery, final String aggrField) {

        List<FinancialsDetail> result = new ArrayList<>();
        SearchResponse finSearchResponse;
        String coaType;
        BoolQueryBuilder boolQry = boolQuery;
        if (StringUtils.isBlank(financialsDetailsRequest.getDetailedCode()) && StringUtils.isBlank(financialsDetailsRequest.getMajorCode())
                && StringUtils.isBlank(financialsDetailsRequest.getMinorCode())) {
            // calculating income
            coaType = INCOME;
            boolQry.filter(QueryBuilders.prefixQuery(DETAILED_CODE, "1"));
            finSearchResponse = getVoucherSearchResponse(boolQry, aggrField);
            result = getResponse(financialsDetailsRequest, finSearchResponse, coaType);
            finSearchResponse = getVoucherSearchResponse(FinancialsDashBoardUtils.prepareWhereClause(financialsDetailsRequest).filter(QueryBuilders.prefixQuery(DETAILED_CODE, "2")), aggrField);
            coaType = EXPENSE;
            result = getFinalResponse(financialsDetailsRequest, finSearchResponse, result, coaType);
            finSearchResponse = getVoucherSearchResponse(FinancialsDashBoardUtils.prepareWhereClause(financialsDetailsRequest).filter(QueryBuilders.prefixQuery(DETAILED_CODE, "3")), aggrField);
            coaType = LIABILITIES;
            result = getFinalResponse(financialsDetailsRequest, finSearchResponse, result, coaType);
            finSearchResponse = getVoucherSearchResponse(FinancialsDashBoardUtils.prepareWhereClause(financialsDetailsRequest).filter(QueryBuilders.prefixQuery(DETAILED_CODE, "4")), aggrField);
            coaType = ASSETS;
            result = getFinalResponse(financialsDetailsRequest, finSearchResponse, result, coaType);
        } else if (financialsDetailsRequest.getDetailedCode().startsWith("1") || financialsDetailsRequest.getMajorCode().startsWith("1") || financialsDetailsRequest.getMinorCode()
                .startsWith("1")) {
            coaType = INCOME;
            finSearchResponse = getVoucherSearchResponse(boolQuery, aggrField);
            result = getResponse(financialsDetailsRequest, finSearchResponse, coaType);
        } else if (financialsDetailsRequest.getDetailedCode().startsWith("2") || financialsDetailsRequest.getMajorCode().startsWith("2") || financialsDetailsRequest.getMinorCode()
                .startsWith("2")) {
            coaType = EXPENSE;
            finSearchResponse = getVoucherSearchResponse(boolQuery, aggrField);
            result = getResponse(financialsDetailsRequest, finSearchResponse, coaType);
        } else if (financialsDetailsRequest.getDetailedCode().startsWith("3") || financialsDetailsRequest.getMajorCode().startsWith("3") || financialsDetailsRequest.getMinorCode()
                .startsWith("3")) {
            coaType = LIABILITIES;
            finSearchResponse = getVoucherSearchResponse(boolQuery, aggrField);
            result = getResponse(financialsDetailsRequest, finSearchResponse, coaType);
        } else if (financialsDetailsRequest.getDetailedCode().startsWith("4") || financialsDetailsRequest.getMajorCode().startsWith("4") || financialsDetailsRequest.getMinorCode()
                .startsWith("4")) {
            coaType = ASSETS;
            finSearchResponse = getVoucherSearchResponse(boolQuery, aggrField);
            result = getResponse(financialsDetailsRequest, finSearchResponse, coaType);
        }

        return result;
    }


    private SearchResponse getVoucherSearchResponse(final BoolQueryBuilder boolQuery,
                                                    final String aggrField) {
        boolQuery.filter(QueryBuilders.matchQuery("voucherstatusid", FinancialConstants.CREATEDVOUCHERSTATUS));
        return elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME).setQuery(boolQuery)
                .addAggregation(AggregationBuilders.terms(AGGRFIELD).field(aggrField)
                        .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                        .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT))
                        .subAggregation(
                                AggregationBuilders.topHits(FIN_RECORDS).addField(DISTRICT_NAME).addField(ULB_NAME).setSize(1)))
                .execute().actionGet();
    }

    private SearchResponse getOpeningBlncSearchResponse(final BoolQueryBuilder boolQuery,
                                                        final String aggrField) {
        return elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_OPENINGBALANCE_INDEX_NAME).setQuery(boolQuery)
                .addAggregation(AggregationBuilders.terms(OBAGGRFIELD).field(aggrField)
                        .subAggregation(AggregationBuilders.sum(OBDEBITAMOUNT).field(OBDEBITAMOUNT))
                        .subAggregation(AggregationBuilders.sum(OBCREDITAMOUNT).field(OBCREDITAMOUNT)).subAggregation(
                                AggregationBuilders.topHits(FIN_RECORDS).addField(DISTRICT_NAME).addField(ULB_NAME).setSize(1)))
                .execute().actionGet();
    }

    private List<FinancialsDetail> getResponse(final FinancialsDetailsRequest financialsDetailsRequest,
                                               final SearchResponse finSearchResponse, final String coaType) {


        Map<String, FinancialsDetail> openingBalanceLiablity = new HashMap<>();
        Map<String, FinancialsDetail> openingBalanceAssets = new HashMap<>();
        String distName;
        String ulbName;
        if (ASSETS.equalsIgnoreCase(coaType) || LIABILITIES.equalsIgnoreCase(coaType)) {
            openingBalanceLiablity = getOpeningBalance(financialsDetailsRequest, null, LIABILITIES);
            openingBalanceAssets = getOpeningBalance(financialsDetailsRequest, null, ASSETS);
        }
        final StringTerms distAggr = finSearchResponse.getAggregations().get(AGGRFIELD);
        final List<FinancialsDetail> finList = new ArrayList<>();
        for (final Terms.Bucket entry : distAggr.getBuckets()) {
            FinancialsDetail financialsDetail = new FinancialsDetail();
            final Sum aggrDebit = entry.getAggregations().get(DEBITAMOUNT);
            final Sum aggrCredit = entry.getAggregations().get(CREDITAMOUNT);
            final TopHits topHits = entry.getAggregations().get(FIN_RECORDS);
            final SearchHit[] hit = topHits.getHits().getHits();
            distName = hit[0].field(DISTRICT_NAME).getValue();
            ulbName = hit[0].field(ULB_NAME).getValue();

            if (INCOME.equalsIgnoreCase(coaType)) {
                financialsDetail
                        .setIncomeDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                financialsDetail
                        .setIncomeCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                financialsDetail.setIncomeNetAmount(
                        financialsDetail.getIncomeCreditAmount().subtract(financialsDetail.getIncomeDebitAmount()));

            } else if (EXPENSE.equalsIgnoreCase(coaType)) {
                financialsDetail
                        .setExpenseDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                financialsDetail
                        .setExpenseCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                financialsDetail.setExpenseNetAmount(
                        financialsDetail.getExpenseDebitAmount().subtract(financialsDetail.getExpenseCreditAmount()));
            }

            if (LIABILITIES.equalsIgnoreCase(coaType)) {
                calculateNetLiability(distName, ulbName, openingBalanceLiablity, aggrDebit, aggrCredit, financialsDetail);

            } else if (ASSETS.equalsIgnoreCase(coaType)) {
                calculateNetAsset(distName, ulbName, openingBalanceAssets, aggrDebit, aggrCredit, financialsDetail);
            }
            financialsDetail.setIeNetAmount(financialsDetail.getExpenseNetAmount().subtract(financialsDetail.getIncomeNetAmount()));
            financialsDetail.setAlNetAmount(financialsDetail.getAssetsNetAmount().subtract(financialsDetail.getLiabilitiesNetAmount()));
            financialsDetail.setDistrict(distName);
            financialsDetail.setUlbName(ulbName);
            setFinancialsDetails(financialsDetailsRequest, financialsDetail);
            finList.add(financialsDetail);

        }
        return finList;
    }

    private Map<String, FinancialsDetail> getOpeningBalance(FinancialsDetailsRequest financialsDetailsRequest, BoolQueryBuilder query, String coaType) {


        BoolQueryBuilder boolquery = query;
        if (query == null)
            boolquery = FinancialsDashBoardUtils.prepareOpeningBlncWhereClause(financialsDetailsRequest);
        String aggrField = FinancialsDashBoardUtils.getOpeningBlncAggrGroupingField(financialsDetailsRequest);
        SearchResponse openingBalncSearchResponse = getOpeningBlncSearchResponse(boolquery, aggrField);
        final StringTerms distAggr = openingBalncSearchResponse.getAggregations().get(OBAGGRFIELD);
        final Map finList = new HashMap();
        for (final Terms.Bucket entry : distAggr.getBuckets()) {
            FinancialsDetail financialsDetail = new FinancialsDetail();
            final Sum debit = entry.getAggregations().get(OBDEBITAMOUNT);
            final Sum credit = entry.getAggregations().get(OBCREDITAMOUNT);
            final TopHits topHits = entry.getAggregations().get(FIN_RECORDS);
            final SearchHit[] hit = topHits.getHits().getHits();
            financialsDetail.setDistrict(hit[0].field(DISTRICT_NAME).getValue());
            financialsDetail.setUlbName(hit[0].field(ULB_NAME).getValue());
            if (LIABILITIES.equalsIgnoreCase(coaType))
                financialsDetail.setLiabilitiesOpbAmount(BigDecimal.valueOf(debit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.valueOf(credit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)));
            if (ASSETS.equalsIgnoreCase(coaType))
                financialsDetail.setAssetsOpbAmount(BigDecimal.valueOf(debit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.valueOf(credit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)));
            finList.put(financialsDetail.getDistrict(), financialsDetail);
        }

        return finList;
    }

    private List<FinancialsDetail> getFinalResponse(final FinancialsDetailsRequest financialsDetailsRequest, final SearchResponse finSearchResponse, final List<FinancialsDetail> result, String coaType) {

        final StringTerms distAggr = finSearchResponse.getAggregations().get(AGGRFIELD);
        String distName;
        String ulbName;
        Map openingBalanceLiability = new HashMap();
        Map openingBalanceAsset = new HashMap();
        if (LIABILITIES.equalsIgnoreCase(coaType) || ASSETS.equalsIgnoreCase(coaType)) {
            openingBalanceLiability = getOpeningBalance(financialsDetailsRequest, FinancialsDashBoardUtils.prepareOpeningBlncWhereClause(financialsDetailsRequest)
                    .filter(QueryBuilders.prefixQuery(DETAILED_CODE, "3")), LIABILITIES);
            openingBalanceAsset = getOpeningBalance(financialsDetailsRequest, FinancialsDashBoardUtils.prepareOpeningBlncWhereClause(financialsDetailsRequest)
                    .filter(QueryBuilders.prefixQuery(DETAILED_CODE, "4")), ASSETS);
        }
        for (final Terms.Bucket entry : distAggr.getBuckets()) {
            final Sum aggrDebit = entry.getAggregations().get(DEBITAMOUNT);
            final Sum aggrCredit = entry.getAggregations().get(CREDITAMOUNT);
            final TopHits topHits = entry.getAggregations().get(FIN_RECORDS);
            final SearchHit[] hit = topHits.getHits().getHits();
            distName = hit[0].field(DISTRICT_NAME).getValue();
            ulbName = hit[0].field(ULB_NAME).getValue();

            for (final FinancialsDetail finDetail : result) {
                if (EXPENSE.equalsIgnoreCase(coaType) && finDetail.getUlbName().equalsIgnoreCase(ulbName) && finDetail.getDistrict().equalsIgnoreCase(distName)) {
                    finDetail.setExpenseDebitAmount(
                            BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail.setExpenseCreditAmount(
                            BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail.setExpenseNetAmount(finDetail.getExpenseDebitAmount().subtract(finDetail.getExpenseCreditAmount()));
                    finDetail.setIeNetAmount(finDetail.getExpenseNetAmount().subtract(finDetail.getIncomeNetAmount()));

                } else if (LIABILITIES.equalsIgnoreCase(coaType) && finDetail.getUlbName().equalsIgnoreCase(ulbName) && finDetail.getDistrict().equalsIgnoreCase(distName)) {
                    calculateNetLiability(distName, ulbName, openingBalanceLiability, aggrDebit, aggrCredit, finDetail);

                } else if (ASSETS.equalsIgnoreCase(coaType) && finDetail.getUlbName().equalsIgnoreCase(ulbName) && finDetail.getDistrict().equalsIgnoreCase(distName)) {
                    calculateNetAsset(distName, ulbName, openingBalanceAsset, aggrDebit, aggrCredit, finDetail);
                }


            }

        }
        return result;
    }

    private void calculateNetAsset(String distName, String ulbName, Map<String, FinancialsDetail> openingBalanceAsset, Sum aggrDebit, Sum aggrCredit, FinancialsDetail finDetail) {

        if (!openingBalanceAsset.isEmpty() && openingBalanceAsset.containsKey(distName))
            for (FinancialsDetail f : openingBalanceAsset.values()) {
                if (f.getUlbName().equalsIgnoreCase(ulbName) && f.getDistrict().equalsIgnoreCase(distName)) {
                    finDetail
                            .setAssetsDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail
                            .setAssetsCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail.setAssetsNetAmount(
                            (finDetail.getAssetsDebitAmount().subtract(finDetail.getAssetsCreditAmount())).add(f.getAssetsOpbAmount()));
                    finDetail.setAlNetAmount(finDetail.getAssetsNetAmount().subtract(finDetail.getLiabilitiesNetAmount()));
                    finDetail.setAssetsOpbAmount(f.getAssetsOpbAmount());
                }
            }
        else {
            finDetail
                    .setAssetsDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail
                    .setAssetsCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail.setAssetsNetAmount(
                    (finDetail.getAssetsDebitAmount().subtract(finDetail.getAssetsCreditAmount())).add(BigDecimal.ZERO));
            finDetail.setAlNetAmount(finDetail.getAssetsNetAmount().subtract(finDetail.getLiabilitiesNetAmount()));
            finDetail.setAssetsOpbAmount(BigDecimal.ZERO);
        }
    }

    private void calculateNetLiability(String distName, String ulbName, Map<String, FinancialsDetail> openingBalanceLiability, Sum aggrDebit, Sum aggrCredit, FinancialsDetail finDetail) {
        if (!openingBalanceLiability.isEmpty() && openingBalanceLiability.containsKey(distName))
            for (FinancialsDetail f : openingBalanceLiability.values()) {
                if (f.getUlbName().equalsIgnoreCase(ulbName) && f.getDistrict().equalsIgnoreCase(distName)) {
                    finDetail
                            .setLiabilitiesDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail
                            .setLiabilitiesCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail.setLiabilitiesNetAmount(
                            (finDetail.getLiabilitiesCreditAmount().subtract(finDetail.getLiabilitiesDebitAmount()))
                                    .add(f.getLiabilitiesOpbAmount()).add(finDetail.getIeNetAmount()));
                    finDetail.setLiabilitiesOpbAmount(f.getLiabilitiesOpbAmount());
                }
            }

        else {
            finDetail
                    .setLiabilitiesDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail
                    .setLiabilitiesCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail.setLiabilitiesNetAmount(
                    (finDetail.getLiabilitiesCreditAmount().subtract(finDetail.getLiabilitiesDebitAmount()))
                            .add(BigDecimal.ZERO).add(finDetail.getIeNetAmount()));
            finDetail.setLiabilitiesOpbAmount(BigDecimal.ZERO);
        }

    }

    private void setFinancialsDetails(final FinancialsDetailsRequest financialsDetailsRequest,
                                      final FinancialsDetail financialsDetail) {

        if (StringUtils.isNotBlank(financialsDetailsRequest.getRegion()))
            financialsDetail.setRegion(financialsDetailsRequest.getRegion());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getGrade()))
            financialsDetail.setGrade(financialsDetailsRequest.getGrade());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFromDate()))
            financialsDetail.setFromDate(financialsDetailsRequest.getFromDate());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getToDate()))
            financialsDetail.setToDate(financialsDetailsRequest.getToDate());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFunctionCode()))
            financialsDetail.setFunctionCode(financialsDetailsRequest.getFunctionCode());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFundCode()))
            financialsDetail.setFundCode(financialsDetailsRequest.getFundCode());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFundSource()))
            financialsDetail.setFundSource(financialsDetailsRequest.getFundSource());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getDepartmentCode()))
            financialsDetail.setDepartmentCode(financialsDetailsRequest.getDepartmentCode());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getDetailedCode()))
            financialsDetail.setDetailedCode(financialsDetailsRequest.getDetailedCode());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getSchemeCode()))
            financialsDetail.setSchemeCode(financialsDetailsRequest.getSchemeCode());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getSubschemeCode()))
            financialsDetail.setSubschemeCode(financialsDetailsRequest.getSubschemeCode());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getMajorCode()))
            financialsDetail.setMajorCode(financialsDetailsRequest.getMajorCode());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getMinorCode()))
            financialsDetail.setMinorCode(financialsDetailsRequest.getMinorCode());

    }
}
