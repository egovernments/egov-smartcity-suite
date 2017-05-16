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

import org.egov.egf.bean.dashboard.FinancialsDetailResponse;
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
    private static final String CURRENT_YEAR = "currentYear";
    private static final String LAST_YEAR = "lastYear";
    private static final String FINANCIALYEAR = "financialyear";


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public List<FinancialsDetailResponse> getFinancialsData(final FinancialsDetailsRequest financialsDetailsRequest,
                                                            final BoolQueryBuilder boolQuery, final String aggrField) {
        return getfinDetails(financialsDetailsRequest, boolQuery, aggrField);

    }

    private List<FinancialsDetailResponse> getfinDetails(final FinancialsDetailsRequest financialsDetailsRequest,
                                                         final BoolQueryBuilder boolQuery, final String aggrField) {

        List<FinancialsDetailResponse> result = new ArrayList<>();
        Map<String, SearchResponse> finSearchResponse;
        String coaType;
        BoolQueryBuilder boolQry = boolQuery;
        if (StringUtils.isBlank(financialsDetailsRequest.getDetailedCode()) && StringUtils.isBlank(financialsDetailsRequest.getMajorCode())
                && StringUtils.isBlank(financialsDetailsRequest.getMinorCode())) {
            // calculating income
            coaType = INCOME;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQry, coaType, aggrField);
            result = getResponse(financialsDetailsRequest, finSearchResponse, coaType);
            coaType = EXPENSE;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQry, coaType, aggrField);
            result = getFinalResponse(financialsDetailsRequest, finSearchResponse, result, coaType);
            coaType = LIABILITIES;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQry, coaType, aggrField);
            result = getFinalResponse(financialsDetailsRequest, finSearchResponse, result, coaType);
            coaType = ASSETS;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQry, coaType, aggrField);
            result = getFinalResponse(financialsDetailsRequest, finSearchResponse, result, coaType);
        } else if (financialsDetailsRequest.getDetailedCode().startsWith("1") || financialsDetailsRequest.getMajorCode().startsWith("1") || financialsDetailsRequest.getMinorCode()
                .startsWith("1")) {
            coaType = INCOME;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQuery, coaType, aggrField);
            result = getResponse(financialsDetailsRequest, finSearchResponse, coaType);
        } else if (financialsDetailsRequest.getDetailedCode().startsWith("2") || financialsDetailsRequest.getMajorCode().startsWith("2") || financialsDetailsRequest.getMinorCode()
                .startsWith("2")) {
            coaType = EXPENSE;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQuery, coaType, aggrField);
            result = getResponse(financialsDetailsRequest, finSearchResponse, coaType);
        } else if (financialsDetailsRequest.getDetailedCode().startsWith("3") || financialsDetailsRequest.getMajorCode().startsWith("3") || financialsDetailsRequest.getMinorCode()
                .startsWith("3")) {
            coaType = LIABILITIES;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQuery, coaType, aggrField);
            result = getResponse(financialsDetailsRequest, finSearchResponse, coaType);
        } else if (financialsDetailsRequest.getDetailedCode().startsWith("4") || financialsDetailsRequest.getMajorCode().startsWith("4") || financialsDetailsRequest.getMinorCode()
                .startsWith("4")) {
            coaType = ASSETS;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQuery, coaType, aggrField);
            result = getResponse(financialsDetailsRequest, finSearchResponse, coaType);
        }

        return result;
    }


    private Map<String, SearchResponse> getVoucherSearchResponse(FinancialsDetailsRequest financialsDetailsRequest, BoolQueryBuilder boolQuery, String coaType,
                                                                 final String aggrField) {
        Map<String, SearchResponse> response = new HashMap<>();
        if (StringUtils.isBlank(financialsDetailsRequest.getDetailedCode()) && StringUtils.isBlank(financialsDetailsRequest.getMajorCode())
                && StringUtils.isBlank(financialsDetailsRequest.getMinorCode()))
            boolQuery = prepareQuery(financialsDetailsRequest, coaType);
        boolQuery.filter(QueryBuilders.matchQuery("voucherstatusid", FinancialConstants.CREATEDVOUCHERSTATUS));
        SearchResponse currentYearResponse = elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME).setQuery(boolQuery)
                .addAggregation(AggregationBuilders.terms(AGGRFIELD).field(aggrField)
                        .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                        .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT))
                        .subAggregation(
                                AggregationBuilders.topHits(FIN_RECORDS).addField(DISTRICT_NAME).addField(ULB_NAME).setSize(1)))
                .execute().actionGet();
        response.put(CURRENT_YEAR, currentYearResponse);

        financialsDetailsRequest.setFromDate("");
        financialsDetailsRequest.setToDate("");
        BoolQueryBuilder boolQry = prepareQuery(financialsDetailsRequest, coaType);
        boolQry.filter(QueryBuilders.matchQuery(FINANCIALYEAR, financialsDetailsRequest.getLastFinancialYear()));
        SearchResponse lastYearResponse = elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME).setQuery(boolQry)
                .addAggregation(AggregationBuilders.terms(AGGRFIELD).field(aggrField).subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                        .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT))
                        .subAggregation(
                                AggregationBuilders.topHits(FIN_RECORDS).addField(DISTRICT_NAME).addField(ULB_NAME).setSize(1)))
                .execute().actionGet();
        response.put(LAST_YEAR, lastYearResponse);

        return response;
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

    private BoolQueryBuilder prepareQuery(FinancialsDetailsRequest financialsDetailsRequest, String coaType) {
        BoolQueryBuilder boolquery = new BoolQueryBuilder();
        if (INCOME.equalsIgnoreCase(coaType))
            boolquery = FinancialsDashBoardUtils.prepareWhereClause(financialsDetailsRequest).filter(QueryBuilders.prefixQuery(DETAILED_CODE, "1"));
        if (EXPENSE.equalsIgnoreCase(coaType))
            boolquery = FinancialsDashBoardUtils.prepareWhereClause(financialsDetailsRequest).filter(QueryBuilders.prefixQuery(DETAILED_CODE, "2"));

        if (LIABILITIES.equalsIgnoreCase(coaType))
            boolquery = FinancialsDashBoardUtils.prepareWhereClause(financialsDetailsRequest).filter(QueryBuilders.prefixQuery(DETAILED_CODE, "3"));
        if (ASSETS.equalsIgnoreCase(coaType))
            boolquery = FinancialsDashBoardUtils.prepareWhereClause(financialsDetailsRequest).filter(QueryBuilders.prefixQuery(DETAILED_CODE, "4"));


        return boolquery;

    }

    private List<FinancialsDetailResponse> getResponse(final FinancialsDetailsRequest financialsDetailsRequest,
                                                       final Map<String, SearchResponse> finSearchResponse, final String coaType) {


        Map<String, FinancialsDetailResponse> openingBalanceLiablity = new HashMap<>();
        Map<String, FinancialsDetailResponse> openingBalanceAssets = new HashMap<>();
        String distName;
        String ulbName;

        final List<FinancialsDetailResponse> finList = new ArrayList<>();
        StringTerms aggr;

        for (String key : finSearchResponse.keySet()) {
            if (CURRENT_YEAR.equalsIgnoreCase(key)) {
                if (ASSETS.equalsIgnoreCase(coaType) || LIABILITIES.equalsIgnoreCase(coaType)) {
                    openingBalanceLiablity = getOpeningBalance(financialsDetailsRequest, null, LIABILITIES, CURRENT_YEAR);
                    openingBalanceAssets = getOpeningBalance(financialsDetailsRequest, null, ASSETS, CURRENT_YEAR);
                }
                aggr = finSearchResponse.get(CURRENT_YEAR).getAggregations().get(AGGRFIELD);
                for (final Terms.Bucket entry : aggr.getBuckets()) {
                    final Sum aggrDebit = entry.getAggregations().get(DEBITAMOUNT);
                    final Sum aggrCredit = entry.getAggregations().get(CREDITAMOUNT);
                    final TopHits topHits = entry.getAggregations().get(FIN_RECORDS);
                    final SearchHit[] hit = topHits.getHits().getHits();
                    distName = hit[0].field(DISTRICT_NAME).getValue();
                    ulbName = hit[0].field(ULB_NAME).getValue();

                    if (!finList.isEmpty())
                        for (FinancialsDetailResponse financialsDetails : finList) {
                            if (distName.equalsIgnoreCase(financialsDetails.getDistrict()) && ulbName.equalsIgnoreCase(financialsDetails.getUlbName())) {
                                if (INCOME.equalsIgnoreCase(coaType)) {
                                    financialsDetails
                                            .setCyIncomeDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                                    financialsDetails
                                            .setCyIncomeCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                                    financialsDetails.setCyIncomeNetAmount(
                                            financialsDetails.getCyIncomeCreditAmount().subtract(financialsDetails.getCyIncomeDebitAmount()));

                                } else if (EXPENSE.equalsIgnoreCase(coaType)) {
                                    financialsDetails
                                            .setCyExpenseDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                                    financialsDetails
                                            .setCyExpenseCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                                    financialsDetails.setCyExpenseNetAmount(
                                            financialsDetails.getCyExpenseDebitAmount().subtract(financialsDetails.getCyExpenseCreditAmount()));
                                }

                                if (LIABILITIES.equalsIgnoreCase(coaType)) {
                                    calculateNetLiabilityForCurrentYear(distName, ulbName, openingBalanceLiablity, aggrDebit, aggrCredit, financialsDetails);

                                } else if (ASSETS.equalsIgnoreCase(coaType)) {
                                    calculateNetAssetForCurrentYear(distName, ulbName, openingBalanceAssets, aggrDebit, aggrCredit, financialsDetails);
                                }
                            }
                            financialsDetails.setCyIeNetAmount(financialsDetails.getCyIncomeNetAmount().subtract(financialsDetails.getCyExpenseNetAmount()));
                            financialsDetails.setLyIeNetAmount(financialsDetails.getLyIncomeNetAmount().subtract(financialsDetails.getLyExpenseNetAmount()));
                            financialsDetails.setCyAlNetAmount(financialsDetails.getCyLiabilitiesNetAmount().subtract(financialsDetails.getCyAssetsNetAmount()));
                            financialsDetails.setLyAlNetAmount(financialsDetails.getLyLiabilitiesNetAmount().subtract(financialsDetails.getLyAssetsNetAmount()));
                        }

                }

            } else if (LAST_YEAR.equalsIgnoreCase(key)) {
                if (ASSETS.equalsIgnoreCase(coaType) || LIABILITIES.equalsIgnoreCase(coaType)) {
                    openingBalanceLiablity = getOpeningBalance(financialsDetailsRequest, null, LIABILITIES, LAST_YEAR);
                    openingBalanceAssets = getOpeningBalance(financialsDetailsRequest, null, ASSETS, LAST_YEAR);
                }
                aggr = finSearchResponse.get(LAST_YEAR).getAggregations().get(AGGRFIELD);
                for (final Terms.Bucket entry : aggr.getBuckets()) {
                    FinancialsDetailResponse financialsDetail = new FinancialsDetailResponse();
                    final Sum aggrDebit = entry.getAggregations().get(DEBITAMOUNT);
                    final Sum aggrCredit = entry.getAggregations().get(CREDITAMOUNT);
                    final TopHits topHits = entry.getAggregations().get(FIN_RECORDS);
                    final SearchHit[] hit = topHits.getHits().getHits();
                    distName = hit[0].field(DISTRICT_NAME).getValue();
                    ulbName = hit[0].field(ULB_NAME).getValue();

                    if (INCOME.equalsIgnoreCase(coaType)) {
                        financialsDetail
                                .setLyIncomeDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                        financialsDetail
                                .setLyIncomeCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                        financialsDetail.setLyIncomeNetAmount(
                                financialsDetail.getLyIncomeCreditAmount().subtract(financialsDetail.getLyIncomeDebitAmount()));

                    } else if (EXPENSE.equalsIgnoreCase(coaType)) {
                        financialsDetail
                                .setLyExpenseDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                        financialsDetail
                                .setLyExpenseCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                        financialsDetail.setLyExpenseNetAmount(
                                financialsDetail.getLyExpenseDebitAmount().subtract(financialsDetail.getLyExpenseCreditAmount()));
                    }

                    if (LIABILITIES.equalsIgnoreCase(coaType)) {
                        calculateNetLiabilityForLastYear(distName, ulbName, openingBalanceLiablity, aggrDebit, aggrCredit, financialsDetail);

                    } else if (ASSETS.equalsIgnoreCase(coaType)) {
                        calculateNetAssetForLastYear(distName, ulbName, openingBalanceAssets, aggrDebit, aggrCredit, financialsDetail);
                    }
                    financialsDetail.setDistrict(distName);
                    financialsDetail.setUlbName(ulbName);
                    setFinancialsDetails(financialsDetailsRequest, financialsDetail);
                    finList.add(financialsDetail);
                }
            }

        }
        return finList;
    }

    private Map<String, FinancialsDetailResponse> getOpeningBalance(FinancialsDetailsRequest financialsDetailsRequest, BoolQueryBuilder query, String coaType, String financialYear) {


        BoolQueryBuilder boolQuery = query;
        String aggrField = FinancialsDashBoardUtils.getOpeningBlncAggrGroupingField(financialsDetailsRequest);
        final Map finList = new HashMap();
        if (query == null)
            boolQuery = FinancialsDashBoardUtils.prepareOpeningBlncWhereClause(financialsDetailsRequest);

        if (CURRENT_YEAR.equalsIgnoreCase(financialYear)) {
            boolQuery.filter(QueryBuilders.matchQuery(FINANCIALYEAR, financialsDetailsRequest.getCurrentFinancialYear()));

            SearchResponse openingBalncSearchResponse = getOpeningBlncSearchResponse(boolQuery, aggrField);
            final StringTerms distAggr = openingBalncSearchResponse.getAggregations().get(OBAGGRFIELD);

            for (final Terms.Bucket entry : distAggr.getBuckets()) {
                FinancialsDetailResponse financialsDetail = new FinancialsDetailResponse();
                final Sum debit = entry.getAggregations().get(OBDEBITAMOUNT);
                final Sum credit = entry.getAggregations().get(OBCREDITAMOUNT);
                final TopHits topHits = entry.getAggregations().get(FIN_RECORDS);
                final SearchHit[] hit = topHits.getHits().getHits();
                financialsDetail.setDistrict(hit[0].field(DISTRICT_NAME).getValue());
                financialsDetail.setUlbName(hit[0].field(ULB_NAME).getValue());
                if (LIABILITIES.equalsIgnoreCase(coaType))
                    financialsDetail.setCyLiabilitiesOpbAmount(BigDecimal.valueOf(debit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.valueOf(credit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)));
                if (ASSETS.equalsIgnoreCase(coaType))
                    financialsDetail.setCyAssetsOpbAmount(BigDecimal.valueOf(debit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.valueOf(credit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)));
                finList.put(financialsDetail.getDistrict(), financialsDetail);
            }
        } else if (LAST_YEAR.equalsIgnoreCase(financialYear)) {
            boolQuery.filter(QueryBuilders.matchQuery(FINANCIALYEAR, financialsDetailsRequest.getLastFinancialYear()));

            SearchResponse openingBalncSearchResponse = getOpeningBlncSearchResponse(boolQuery, aggrField);
            final StringTerms distAggr = openingBalncSearchResponse.getAggregations().get(OBAGGRFIELD);
            for (final Terms.Bucket entry : distAggr.getBuckets()) {
                FinancialsDetailResponse financialsDetail = new FinancialsDetailResponse();
                final Sum debit = entry.getAggregations().get(OBDEBITAMOUNT);
                final Sum credit = entry.getAggregations().get(OBCREDITAMOUNT);
                final TopHits topHits = entry.getAggregations().get(FIN_RECORDS);
                final SearchHit[] hit = topHits.getHits().getHits();
                financialsDetail.setDistrict(hit[0].field(DISTRICT_NAME).getValue());
                financialsDetail.setUlbName(hit[0].field(ULB_NAME).getValue());
                if (LIABILITIES.equalsIgnoreCase(coaType))
                    financialsDetail.setLyLiabilitiesOpbAmount(BigDecimal.valueOf(debit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.valueOf(credit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)));
                if (ASSETS.equalsIgnoreCase(coaType))
                    financialsDetail.setLyAssetsOpbAmount(BigDecimal.valueOf(debit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.valueOf(credit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)));
                finList.put(financialsDetail.getDistrict(), financialsDetail);
            }
        }

        return finList;
    }

    private List<FinancialsDetailResponse> getFinalResponse(final FinancialsDetailsRequest financialsDetailsRequest, final Map<String, SearchResponse> finSearchResponse, final List<FinancialsDetailResponse> result, String coaType) {

        StringTerms aggr;
        String distName;
        String ulbName;
        Map openingBalanceLiability = new HashMap();
        Map openingBalanceAsset = new HashMap();
        for (String key : finSearchResponse.keySet()) {
            if (CURRENT_YEAR.equalsIgnoreCase(key)) {
                aggr = finSearchResponse.get(CURRENT_YEAR).getAggregations().get(AGGRFIELD);
                if (LIABILITIES.equalsIgnoreCase(coaType) || ASSETS.equalsIgnoreCase(coaType)) {
                    openingBalanceLiability = getOpeningBalance(financialsDetailsRequest, FinancialsDashBoardUtils.prepareOpeningBlncWhereClause(financialsDetailsRequest)
                            .filter(QueryBuilders.prefixQuery(DETAILED_CODE, "3")), LIABILITIES, CURRENT_YEAR);
                    openingBalanceAsset = getOpeningBalance(financialsDetailsRequest, FinancialsDashBoardUtils.prepareOpeningBlncWhereClause(financialsDetailsRequest)
                            .filter(QueryBuilders.prefixQuery(DETAILED_CODE, "4")), ASSETS, CURRENT_YEAR);
                }
                for (final Terms.Bucket entry : aggr.getBuckets()) {
                    final Sum aggrDebit = entry.getAggregations().get(DEBITAMOUNT);
                    final Sum aggrCredit = entry.getAggregations().get(CREDITAMOUNT);
                    final TopHits topHits = entry.getAggregations().get(FIN_RECORDS);
                    final SearchHit[] hit = topHits.getHits().getHits();
                    distName = hit[0].field(DISTRICT_NAME).getValue();
                    ulbName = hit[0].field(ULB_NAME).getValue();

                    for (final FinancialsDetailResponse finDetail : result) {
                        if (EXPENSE.equalsIgnoreCase(coaType) && finDetail.getUlbName().equalsIgnoreCase(ulbName) && finDetail.getDistrict().equalsIgnoreCase(distName)) {
                            finDetail.setCyExpenseDebitAmount(
                                    BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                            finDetail.setCyExpenseCreditAmount(
                                    BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                            finDetail.setCyExpenseNetAmount(finDetail.getCyExpenseDebitAmount().subtract(finDetail.getCyExpenseCreditAmount()));
                            finDetail.setCyIeNetAmount(finDetail.getCyIncomeNetAmount().subtract(finDetail.getCyExpenseNetAmount()));

                        } else if (LIABILITIES.equalsIgnoreCase(coaType) && finDetail.getUlbName().equalsIgnoreCase(ulbName) && finDetail.getDistrict().equalsIgnoreCase(distName)) {
                            calculateNetLiabilityForCurrentYear(distName, ulbName, openingBalanceLiability, aggrDebit, aggrCredit, finDetail);

                        } else if (ASSETS.equalsIgnoreCase(coaType) && finDetail.getUlbName().equalsIgnoreCase(ulbName) && finDetail.getDistrict().equalsIgnoreCase(distName)) {
                            calculateNetAssetForCurrentYear(distName, ulbName, openingBalanceAsset, aggrDebit, aggrCredit, finDetail);
                        }


                    }

                }
            } else if (LAST_YEAR.equalsIgnoreCase(key)) {
                aggr = finSearchResponse.get(LAST_YEAR).getAggregations().get(AGGRFIELD);
                if (LIABILITIES.equalsIgnoreCase(coaType) || ASSETS.equalsIgnoreCase(coaType)) {
                    openingBalanceLiability = getOpeningBalance(financialsDetailsRequest, FinancialsDashBoardUtils.prepareOpeningBlncWhereClause(financialsDetailsRequest)
                            .filter(QueryBuilders.prefixQuery(DETAILED_CODE, "3")), LIABILITIES, LAST_YEAR);
                    openingBalanceAsset = getOpeningBalance(financialsDetailsRequest, FinancialsDashBoardUtils.prepareOpeningBlncWhereClause(financialsDetailsRequest)
                            .filter(QueryBuilders.prefixQuery(DETAILED_CODE, "4")), ASSETS, LAST_YEAR);
                }
                for (final Terms.Bucket entry : aggr.getBuckets()) {
                    final Sum aggrDebit = entry.getAggregations().get(DEBITAMOUNT);
                    final Sum aggrCredit = entry.getAggregations().get(CREDITAMOUNT);
                    final TopHits topHits = entry.getAggregations().get(FIN_RECORDS);
                    final SearchHit[] hit = topHits.getHits().getHits();
                    distName = hit[0].field(DISTRICT_NAME).getValue();
                    ulbName = hit[0].field(ULB_NAME).getValue();

                    for (final FinancialsDetailResponse finDetail : result) {
                        if (EXPENSE.equalsIgnoreCase(coaType) && finDetail.getUlbName().equalsIgnoreCase(ulbName) && finDetail.getDistrict().equalsIgnoreCase(distName)) {
                            finDetail.setLyExpenseDebitAmount(
                                    BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                            finDetail.setLyExpenseCreditAmount(
                                    BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                            finDetail.setLyExpenseNetAmount(finDetail.getLyExpenseDebitAmount().subtract(finDetail.getLyExpenseCreditAmount()));
                            finDetail.setLyIeNetAmount(finDetail.getLyIncomeNetAmount().subtract(finDetail.getLyExpenseNetAmount()));

                        } else if (LIABILITIES.equalsIgnoreCase(coaType) && finDetail.getUlbName().equalsIgnoreCase(ulbName) && finDetail.getDistrict().equalsIgnoreCase(distName)) {
                            calculateNetLiabilityForLastYear(distName, ulbName, openingBalanceLiability, aggrDebit, aggrCredit, finDetail);

                        } else if (ASSETS.equalsIgnoreCase(coaType) && finDetail.getUlbName().equalsIgnoreCase(ulbName) && finDetail.getDistrict().equalsIgnoreCase(distName)) {
                            calculateNetAssetForLastYear(distName, ulbName, openingBalanceAsset, aggrDebit, aggrCredit, finDetail);
                        }


                    }

                }
            }
        }
        return result;
    }

    private void calculateNetAssetForCurrentYear(String distName, String ulbName, Map<String, FinancialsDetailResponse> openingBalanceAsset, Sum aggrDebit, Sum aggrCredit, FinancialsDetailResponse finDetail) {

        if (!openingBalanceAsset.isEmpty() && openingBalanceAsset.containsKey(distName))
            for (FinancialsDetailResponse f : openingBalanceAsset.values()) {
                if (f.getUlbName().equalsIgnoreCase(ulbName) && f.getDistrict().equalsIgnoreCase(distName)) {
                    finDetail
                            .setCyAssetsDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail
                            .setCyAssetsCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail.setCyAssetsNetAmount(
                            finDetail.getCyAssetsDebitAmount().subtract(finDetail.getCyAssetsCreditAmount()).add(f.getCyAssetsOpbAmount()));
                    finDetail.setCyAlNetAmount(finDetail.getCyLiabilitiesNetAmount().subtract(finDetail.getCyAssetsNetAmount()));
                    finDetail.setCyAssetsOpbAmount(f.getCyAssetsOpbAmount());
                }
            }
        else {
            finDetail
                    .setCyAssetsDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail
                    .setCyAssetsCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail.setCyAssetsNetAmount(
                    finDetail.getCyAssetsDebitAmount().subtract(finDetail.getCyAssetsCreditAmount()).add(BigDecimal.ZERO));
            finDetail.setCyAlNetAmount(finDetail.getCyLiabilitiesNetAmount().subtract(finDetail.getCyAssetsNetAmount()));
            finDetail.setCyAssetsOpbAmount(BigDecimal.ZERO);
        }
    }

    private void calculateNetLiabilityForCurrentYear(String distName, String ulbName, Map<String, FinancialsDetailResponse> openingBalanceLiability, Sum aggrDebit, Sum aggrCredit, FinancialsDetailResponse finDetail) {
        if (!openingBalanceLiability.isEmpty() && openingBalanceLiability.containsKey(distName))
            for (FinancialsDetailResponse f : openingBalanceLiability.values()) {
                if (f.getUlbName().equalsIgnoreCase(ulbName) && f.getDistrict().equalsIgnoreCase(distName)) {
                    finDetail
                            .setCyLiabilitiesDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail
                            .setCyLiabilitiesCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail.setCyLiabilitiesNetAmount(
                            finDetail.getCyLiabilitiesCreditAmount().subtract(finDetail.getCyLiabilitiesDebitAmount())
                                    .add(f.getCyLiabilitiesOpbAmount()).add(finDetail.getCyIeNetAmount()));
                    finDetail.setCyLiabilitiesOpbAmount(f.getCyLiabilitiesOpbAmount());
                }
            }

        else {
            finDetail
                    .setCyLiabilitiesDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail
                    .setCyLiabilitiesCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail.setCyLiabilitiesNetAmount(
                    finDetail.getCyLiabilitiesCreditAmount().subtract(finDetail.getCyLiabilitiesDebitAmount())
                            .add(BigDecimal.ZERO).add(finDetail.getCyIeNetAmount()));
            finDetail.setCyLiabilitiesOpbAmount(BigDecimal.ZERO);
        }

    }

    private void calculateNetAssetForLastYear(String distName, String ulbName, Map<String, FinancialsDetailResponse> openingBalanceAsset, Sum aggrDebit, Sum aggrCredit, FinancialsDetailResponse finDetail) {

        if (!openingBalanceAsset.isEmpty() && openingBalanceAsset.containsKey(distName))
            for (FinancialsDetailResponse f : openingBalanceAsset.values()) {
                if (f.getUlbName().equalsIgnoreCase(ulbName) && f.getDistrict().equalsIgnoreCase(distName)) {
                    finDetail
                            .setLyAssetsDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail
                            .setLyAssetsCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail.setLyAssetsNetAmount(
                            (finDetail.getLyAssetsDebitAmount().subtract(finDetail.getLyAssetsCreditAmount())).add(f.getLyAssetsOpbAmount()));
                    finDetail.setLyAlNetAmount(finDetail.getLyLiabilitiesNetAmount().subtract(finDetail.getLyAssetsNetAmount()));
                    finDetail.setLyAssetsOpbAmount(f.getLyAssetsOpbAmount());
                }
            }
        else {
            finDetail
                    .setLyAssetsDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail
                    .setLyAssetsCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail.setLyAssetsNetAmount(
                    (finDetail.getLyAssetsDebitAmount().subtract(finDetail.getLyAssetsCreditAmount())).add(BigDecimal.ZERO));
            finDetail.setLyAlNetAmount(finDetail.getLyLiabilitiesNetAmount().subtract(finDetail.getLyAssetsNetAmount()));
            finDetail.setLyAssetsOpbAmount(BigDecimal.ZERO);
        }
    }

    private void calculateNetLiabilityForLastYear(String distName, String ulbName, Map<String, FinancialsDetailResponse> openingBalanceLiability, Sum aggrDebit, Sum aggrCredit, FinancialsDetailResponse finDetail) {
        if (!openingBalanceLiability.isEmpty() && openingBalanceLiability.containsKey(distName))
            for (FinancialsDetailResponse f : openingBalanceLiability.values()) {
                if (f.getUlbName().equalsIgnoreCase(ulbName) && f.getDistrict().equalsIgnoreCase(distName)) {
                    finDetail
                            .setLyLiabilitiesDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail
                            .setLyLiabilitiesCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail.setLyLiabilitiesNetAmount(
                            finDetail.getLyLiabilitiesCreditAmount().subtract(finDetail.getLyLiabilitiesDebitAmount())
                                    .add(f.getLyLiabilitiesOpbAmount()).add(finDetail.getLyIeNetAmount()));
                    finDetail.setLyLiabilitiesOpbAmount(f.getLyLiabilitiesOpbAmount());
                }
            }

        else {
            finDetail
                    .setLyLiabilitiesDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail
                    .setLyLiabilitiesCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail.setLyLiabilitiesNetAmount(
                    finDetail.getLyLiabilitiesCreditAmount().subtract(finDetail.getLyLiabilitiesDebitAmount())
                            .add(BigDecimal.ZERO).add(finDetail.getLyIeNetAmount()));
            finDetail.setLyLiabilitiesOpbAmount(BigDecimal.ZERO);
        }

    }

    private void setFinancialsDetails(final FinancialsDetailsRequest financialsDetailsRequest,
                                      final FinancialsDetailResponse financialsDetail) {

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