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

import org.egov.egf.bean.dashboard.FinancialsBudgetDetailResponse;
import org.egov.egf.bean.dashboard.FinancialsDetailResponse;
import org.egov.egf.bean.dashboard.FinancialsDetailsRequest;
import org.egov.egf.es.utils.FinancialsDashBoardUtils;
import org.egov.infra.utils.DateUtils;
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
    private static final String AGGRFIELD = "aggrField";
    private static final String OBAGGRFIELD = "obAggrField";
    private static final String CURRENT_YEAR = "currentYear";
    private static final String LAST_YEAR = "lastYear";
    private static final String FINANCIALYEAR = "financialyear";
    private static final String MAJOR_CODE = "majorcode";
    private static final String MINOR_CODE = "minorcode";
    private static final String OPENINGBALANCE = "openingbalance";
    private static final String AGGRTYPE = "_type";
    private static final String CY_RECOREDS = "finRecordsCy";
    private static final String LY_RECORDS = "finRecordsLy";


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public List<FinancialsDetailResponse> getFinancialsData(final FinancialsDetailsRequest financialsDetailsRequest,
                                                            final BoolQueryBuilder boolQuery, final String aggrField) {
        return getfinDetails(financialsDetailsRequest, boolQuery, aggrField);

    }

    private List<FinancialsDetailResponse> getfinDetails(final FinancialsDetailsRequest financialsDetailsRequest,
                                                         final BoolQueryBuilder boolQuery, final String aggrField) {

        List<FinancialsDetailResponse> result = new ArrayList<>();
        Map<String, FinancialsDetailResponse> resultMap;
        Map<String, SearchResponse> finSearchResponse;
        String coaType;
        BoolQueryBuilder boolQry = boolQuery;
        if (StringUtils.isBlank(financialsDetailsRequest.getDetailedCode()) && StringUtils.isBlank(financialsDetailsRequest.getMajorCode())
                && StringUtils.isBlank(financialsDetailsRequest.getMinorCode())) {
            // calculating income
            coaType = INCOME;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQry, coaType, aggrField);
            resultMap = getResponse(financialsDetailsRequest, finSearchResponse, coaType, aggrField);
            coaType = EXPENSE;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQry, coaType, aggrField);
            resultMap = getFinalResponse(financialsDetailsRequest, finSearchResponse, resultMap, coaType, aggrField);
            coaType = LIABILITIES;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQry, coaType, aggrField);
            resultMap = getFinalResponse(financialsDetailsRequest, finSearchResponse, resultMap, coaType, aggrField);
            coaType = ASSETS;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQry, coaType, aggrField);
            resultMap = getFinalResponse(financialsDetailsRequest, finSearchResponse, resultMap, coaType, aggrField);
            result = getFinalListOfData(resultMap);
        } else if (financialsDetailsRequest.getDetailedCode().startsWith("1") || financialsDetailsRequest.getMajorCode().startsWith("1") || financialsDetailsRequest.getMinorCode()
                .startsWith("1")) {
            coaType = INCOME;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQuery, coaType, aggrField);
            resultMap = getResponse(financialsDetailsRequest, finSearchResponse, coaType, aggrField);
            result = getFinalListOfData(resultMap);
        } else if (financialsDetailsRequest.getDetailedCode().startsWith("2") || financialsDetailsRequest.getMajorCode().startsWith("2") || financialsDetailsRequest.getMinorCode()
                .startsWith("2")) {
            coaType = EXPENSE;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQuery, coaType, aggrField);
            resultMap = getResponse(financialsDetailsRequest, finSearchResponse, coaType, aggrField);
            result = getFinalListOfData(resultMap);
        } else if (financialsDetailsRequest.getDetailedCode().startsWith("3") || financialsDetailsRequest.getMajorCode().startsWith("3") || financialsDetailsRequest.getMinorCode()
                .startsWith("3")) {
            coaType = LIABILITIES;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQuery, coaType, aggrField);
            resultMap = getResponse(financialsDetailsRequest, finSearchResponse, coaType, aggrField);
            result = getFinalListOfData(resultMap);
        } else if (financialsDetailsRequest.getDetailedCode().startsWith("4") || financialsDetailsRequest.getMajorCode().startsWith("4") || financialsDetailsRequest.getMinorCode()
                .startsWith("4")) {
            coaType = ASSETS;
            finSearchResponse = getVoucherSearchResponse(financialsDetailsRequest, boolQuery, coaType, aggrField);
            resultMap = getResponse(financialsDetailsRequest, finSearchResponse, coaType, aggrField);
            result = getFinalListOfData(resultMap);
        }

        return result;
    }


    private Map<String, SearchResponse> getVoucherSearchResponse(FinancialsDetailsRequest financialsDetailsRequest, BoolQueryBuilder boolQuery, String coaType,
                                                                 String aggrField) {
        Map<String, SearchResponse> response = new HashMap<>();
        BoolQueryBuilder boolQry = boolQuery;
        String fromDate;
        String toDate;
        if (!aggrField.equalsIgnoreCase(DETAILED_CODE) && !aggrField.equalsIgnoreCase(MAJOR_CODE) && !aggrField.equalsIgnoreCase(MINOR_CODE)
                && StringUtils.isBlank(financialsDetailsRequest.getDetailedCode()) && StringUtils.isBlank(financialsDetailsRequest.getMajorCode())
                && StringUtils.isBlank(financialsDetailsRequest.getMinorCode()))
            boolQry = prepareQuery(financialsDetailsRequest, coaType);
        boolQry.filter(QueryBuilders.matchQuery("voucherstatusid", FinancialConstants.CREATEDVOUCHERSTATUS));
        if (StringUtils.isBlank(aggrField)) {
            aggrField = AGGRTYPE;
        }
        SearchResponse currentYearResponse = elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME).setQuery(boolQry)
                .addAggregation(AggregationBuilders.terms(AGGRFIELD).field(aggrField)
                        .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                        .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT))
                        .subAggregation(AggregationBuilders.topHits(CY_RECOREDS).addField(FinancialConstants.DISTNAME)
                                .addField(FinancialConstants.ULBNAME)
                                .addField(FinancialConstants.ULBGRADE).addField(FinancialConstants.REGNAME).addField(FinancialConstants.MAJORCODEDESCRIPTION)
                                .addField(FinancialConstants.MINORCODEDESCRIPTION).addField(FinancialConstants.DETAILEDCODEDESCRIPTION).setSize(1)))
                .execute().actionGet();

        response.put(CURRENT_YEAR, currentYearResponse);

        fromDate = financialsDetailsRequest.getFromDate();
        toDate = financialsDetailsRequest.getToDate();
        financialsDetailsRequest.setFromDate(FinancialConstants.DATEFORMATTER_YYYY_MM_DD.format(
                DateUtils.addYears(DateUtils.getDate(financialsDetailsRequest.getFromDate(), "yyyy-MM-dd"), -1)));
        financialsDetailsRequest.setToDate(FinancialConstants.DATEFORMATTER_YYYY_MM_DD.format(
                DateUtils.addYears(DateUtils.getDate(financialsDetailsRequest.getToDate(), "yyyy-MM-dd"), -1)));

        if (!aggrField.equalsIgnoreCase(DETAILED_CODE) && !aggrField.equalsIgnoreCase(MAJOR_CODE) && !aggrField.equalsIgnoreCase(MINOR_CODE) &&
                StringUtils.isBlank(financialsDetailsRequest.getDetailedCode()) && StringUtils.isBlank(financialsDetailsRequest.getMajorCode())
                && StringUtils.isBlank(financialsDetailsRequest.getMinorCode())) {
            boolQry = prepareQuery(financialsDetailsRequest, coaType);
        } else
            boolQry = FinancialsDashBoardUtils.prepareWhereClause(financialsDetailsRequest);
        boolQry.filter(QueryBuilders.matchQuery("voucherstatusid", FinancialConstants.CREATEDVOUCHERSTATUS));
        SearchResponse lastYearResponse = elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME).setQuery(boolQry)
                .addAggregation(AggregationBuilders.terms(AGGRFIELD).field(aggrField).subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                        .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT))
                        .subAggregation(AggregationBuilders.topHits(LY_RECORDS).addField(FinancialConstants.DISTNAME)
                                .addField(FinancialConstants.ULBNAME)
                                .addField(FinancialConstants.ULBGRADE).addField(FinancialConstants.REGNAME).addField(FinancialConstants.MAJORCODEDESCRIPTION)
                                .addField(FinancialConstants.MINORCODEDESCRIPTION).addField(FinancialConstants.DETAILEDCODEDESCRIPTION).setSize(1)))
                .execute().actionGet();

        response.put(LAST_YEAR, lastYearResponse);
        financialsDetailsRequest.setFromDate(fromDate);
        financialsDetailsRequest.setToDate(toDate);

        return response;
    }

    private SearchResponse getOpeningBlncSearchResponse(final BoolQueryBuilder boolQuery,
                                                        String aggrField) {
        if (StringUtils.isBlank(aggrField)) {
            aggrField = AGGRTYPE;
        }
        return elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_OPENINGBALANCE_INDEX_NAME).setQuery(boolQuery)
                .addAggregation(AggregationBuilders.terms(OBAGGRFIELD).field(aggrField)
                        .subAggregation(AggregationBuilders.sum(OBDEBITAMOUNT).field(OBDEBITAMOUNT))
                        .subAggregation(AggregationBuilders.sum(OBCREDITAMOUNT).field(OBCREDITAMOUNT)))
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

    private List<FinancialsDetailResponse> getFinalListOfData(Map<String, FinancialsDetailResponse> resultMap) {


        List<FinancialsDetailResponse> list = new ArrayList<>();
        if (!resultMap.isEmpty())
            for (FinancialsDetailResponse response : resultMap.values())
                list.add(response);

        return list;
    }


    private Map<String, FinancialsDetailResponse> getResponse(final FinancialsDetailsRequest financialsDetailsRequest,
                                                              final Map<String, SearchResponse> finSearchResponse, String coaType, String aggrField) {


        Map<String, FinancialsDetailResponse> openingBalanceLiablity = new HashMap<>();
        Map<String, FinancialsDetailResponse> openingBalanceAssets = new HashMap<>();
        Map<String, FinancialsDetailResponse> finMap = new HashMap<>();
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
                    final TopHits topHits = entry.getAggregations().get(CY_RECOREDS);

                    if (!finMap.isEmpty()) {
                        FinancialsDetailResponse financialsDetails = finMap.get(entry.getKeyAsString());
                        if (financialsDetails == null)
                            financialsDetails = new FinancialsDetailResponse();
                        if (aggrField.equalsIgnoreCase(DETAILED_CODE) || aggrField.equalsIgnoreCase(MAJOR_CODE) || aggrField.equalsIgnoreCase(MINOR_CODE))
                            coaType = verifyCoaType(entry.getKeyAsString());
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
                            calculateNetLiabilityForCurrentYear(entry.getKeyAsString(), openingBalanceLiablity, aggrDebit, aggrCredit, financialsDetails);

                        } else if (ASSETS.equalsIgnoreCase(coaType)) {
                            calculateNetAssetForCurrentYear(entry.getKeyAsString(), openingBalanceAssets, aggrDebit, aggrCredit, financialsDetails);
                        }

                        financialsDetails.setCyIeNetAmount(financialsDetails.getCyIncomeNetAmount().subtract(financialsDetails.getCyExpenseNetAmount()));
                        financialsDetails.setLyIeNetAmount(financialsDetails.getLyIncomeNetAmount().subtract(financialsDetails.getLyExpenseNetAmount()));
                        financialsDetails.setCyAlNetAmount(financialsDetails.getCyLiabilitiesNetAmount().subtract(financialsDetails.getCyAssetsNetAmount()));
                        financialsDetails.setLyAlNetAmount(financialsDetails.getLyLiabilitiesNetAmount().subtract(financialsDetails.getLyAssetsNetAmount()));
                        FinancialsDashBoardUtils.setValues(entry.getKeyAsString(), financialsDetails, aggrField, setResponseDetails(topHits));

                        if (finMap.containsKey(entry.getKeyAsString())) {
                            finMap.remove(entry.getKeyAsString());
                            finMap.put(entry.getKeyAsString(), financialsDetails);
                        } else
                            finMap.put(entry.getKeyAsString(), financialsDetails);

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
                    String keyName = entry.getKeyAsString();
                    final TopHits topHits = entry.getAggregations().get(LY_RECORDS);

                    if (aggrField.equalsIgnoreCase(DETAILED_CODE) || aggrField.equalsIgnoreCase(MAJOR_CODE) || aggrField.equalsIgnoreCase(MINOR_CODE))
                        coaType = verifyCoaType(keyName);
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
                        calculateNetLiabilityForLastYear(entry.getKeyAsString(), openingBalanceLiablity, aggrDebit, aggrCredit, financialsDetail);

                    } else if (ASSETS.equalsIgnoreCase(coaType)) {
                        calculateNetAssetForLastYear(entry.getKeyAsString(), openingBalanceAssets, aggrDebit, aggrCredit, financialsDetail);
                    }

                    FinancialsDetailResponse finResponse = setResponseDetails(topHits);
                    FinancialsDashBoardUtils.setValues(keyName, financialsDetail, aggrField, finResponse);
                    setFinancialsDetails(financialsDetailsRequest, financialsDetail, finResponse);
                    finMap.put(entry.getKeyAsString(), financialsDetail);
                }
            }

        }
        return finMap;
    }


    private String verifyCoaType(String keyName) {
        String coaType;
        if (keyName.startsWith("1"))
            coaType = INCOME;
        else if (keyName.startsWith("2"))
            coaType = EXPENSE;
        else if (keyName.startsWith("3"))
            coaType = LIABILITIES;
        else
            coaType = ASSETS;
        return coaType;
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

                if (LIABILITIES.equalsIgnoreCase(coaType))
                    financialsDetail.setCyLiabilitiesOpbAmount(BigDecimal.valueOf(debit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.valueOf(credit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)));
                if (ASSETS.equalsIgnoreCase(coaType))
                    financialsDetail.setCyAssetsOpbAmount(BigDecimal.valueOf(debit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.valueOf(credit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)));
                finList.put(entry.getKeyAsString(), financialsDetail);
            }
        } else if (LAST_YEAR.equalsIgnoreCase(financialYear)) {
            boolQuery.filter(QueryBuilders.matchQuery(FINANCIALYEAR, financialsDetailsRequest.getLastFinancialYear()));

            SearchResponse openingBalncSearchResponse = getOpeningBlncSearchResponse(boolQuery, aggrField);
            final StringTerms distAggr = openingBalncSearchResponse.getAggregations().get(OBAGGRFIELD);
            for (final Terms.Bucket entry : distAggr.getBuckets()) {
                FinancialsDetailResponse financialsDetail = new FinancialsDetailResponse();
                final Sum debit = entry.getAggregations().get(OBDEBITAMOUNT);
                final Sum credit = entry.getAggregations().get(OBCREDITAMOUNT);

                if (LIABILITIES.equalsIgnoreCase(coaType))
                    financialsDetail.setLyLiabilitiesOpbAmount(BigDecimal.valueOf(debit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.valueOf(credit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)));
                if (ASSETS.equalsIgnoreCase(coaType))
                    financialsDetail.setLyAssetsOpbAmount(BigDecimal.valueOf(debit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.valueOf(credit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP)));
                finList.put(entry.getKeyAsString(), financialsDetail);
            }
        }

        return finList;
    }

    private Map<String, FinancialsDetailResponse> getFinalResponse(final FinancialsDetailsRequest financialsDetailsRequest, final Map<String, SearchResponse> finSearchResponse, final Map<String, FinancialsDetailResponse> result, String coaType, String aggrField) {

        StringTerms aggr;
        Map openingBalanceLiability = new HashMap();
        Map openingBalanceAsset = new HashMap();
        Map<String, FinancialsDetailResponse> response = new HashMap<>();
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
                    final TopHits topHits = entry.getAggregations().get(CY_RECOREDS);
                    if (!response.isEmpty()) {
                        FinancialsDetailResponse finDetail = response.get(entry.getKeyAsString());
                        if (aggrField.equalsIgnoreCase(DETAILED_CODE) || aggrField.equalsIgnoreCase(MAJOR_CODE) || aggrField.equalsIgnoreCase(MINOR_CODE))
                            coaType = verifyCoaType(entry.getKeyAsString());
                        if (finDetail == null) {
                            finDetail = new FinancialsDetailResponse();
                        }
                        if (EXPENSE.equalsIgnoreCase(coaType)) {
                            finDetail.setCyExpenseDebitAmount(
                                    BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                            finDetail.setCyExpenseCreditAmount(
                                    BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                            finDetail.setCyExpenseNetAmount(finDetail.getCyExpenseDebitAmount().subtract(finDetail.getCyExpenseCreditAmount()));
                            finDetail.setCyIeNetAmount(finDetail.getCyIncomeNetAmount().subtract(finDetail.getCyExpenseNetAmount()));


                        } else if (LIABILITIES.equalsIgnoreCase(coaType)) {
                            calculateNetLiabilityForCurrentYear(entry.getKeyAsString(), openingBalanceLiability, aggrDebit, aggrCredit, finDetail);

                        } else if (ASSETS.equalsIgnoreCase(coaType)) {
                            calculateNetAssetForCurrentYear(entry.getKeyAsString(), openingBalanceAsset, aggrDebit, aggrCredit, finDetail);
                        }
                        FinancialsDashBoardUtils.setValues(entry.getKeyAsString(), finDetail, aggrField, setResponseDetails(topHits));
                        if (response.containsKey(entry.getKeyAsString())) {
                            response.remove(entry.getKeyAsString());
                            response.put(entry.getKeyAsString(), finDetail);
                        } else
                            response.put(entry.getKeyAsString(), finDetail);
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
                response = result;
                if (!aggr.getBuckets().isEmpty()) {
                    for (final Terms.Bucket entry : aggr.getBuckets()) {
                        final Sum aggrDebit = entry.getAggregations().get(DEBITAMOUNT);
                        final Sum aggrCredit = entry.getAggregations().get(CREDITAMOUNT);
                        final TopHits topHits = entry.getAggregations().get(LY_RECORDS);


                        if (!result.isEmpty()) {
                            FinancialsDetailResponse finDetail = result.get(entry.getKeyAsString());
                            if (finDetail == null) {
                                finDetail = new FinancialsDetailResponse();
                            }
                            if (aggrField.equalsIgnoreCase(DETAILED_CODE) || aggrField.equalsIgnoreCase(MAJOR_CODE) || aggrField.equalsIgnoreCase(MINOR_CODE))
                                coaType = verifyCoaType(entry.getKeyAsString());
                            if (EXPENSE.equalsIgnoreCase(coaType)) {
                                finDetail.setLyExpenseDebitAmount(
                                        BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                                finDetail.setLyExpenseCreditAmount(
                                        BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                                finDetail.setLyExpenseNetAmount(finDetail.getLyExpenseDebitAmount().subtract(finDetail.getLyExpenseCreditAmount()));
                                finDetail.setLyIeNetAmount(finDetail.getLyIncomeNetAmount().subtract(finDetail.getLyExpenseNetAmount()));


                            } else if (LIABILITIES.equalsIgnoreCase(coaType)) {
                                calculateNetLiabilityForLastYear(entry.getKeyAsString(), openingBalanceLiability, aggrDebit, aggrCredit, finDetail);

                            } else if (ASSETS.equalsIgnoreCase(coaType)) {
                                calculateNetAssetForLastYear(entry.getKeyAsString(), openingBalanceAsset, aggrDebit, aggrCredit, finDetail);
                            }
                            FinancialsDashBoardUtils.setValues(entry.getKeyAsString(), finDetail, aggrField, setResponseDetails(topHits));
                            if (response.containsKey(entry.getKeyAsString())) {
                                response.remove(entry.getKeyAsString());
                                response.put(entry.getKeyAsString(), finDetail);
                            } else
                                response.put(entry.getKeyAsString(), finDetail);

                        }

                    }
                }

            }
        }
        return response;
    }

    private FinancialsDetailResponse setResponseDetails(final TopHits topHits) {
        FinancialsDetailResponse finResponse = new FinancialsDetailResponse();
        final SearchHit[] hit = topHits.getHits().getHits();
        finResponse.setRegion(hit[0].field(FinancialConstants.REGNAME).getValue());
        finResponse.setDistrict(hit[0].field(FinancialConstants.DISTNAME).getValue());
        finResponse.setGrade(hit[0].field(FinancialConstants.ULBGRADE).getValue());
        finResponse.setUlbName(hit[0].field(FinancialConstants.ULBNAME).getValue());
        finResponse.setMajorCodeDescription(hit[0].field(FinancialConstants.MAJORCODEDESCRIPTION).getValue());
        finResponse.setMinorCodeDescription(hit[0].field(FinancialConstants.MINORCODEDESCRIPTION).getValue());
        finResponse.setDetailedCodeDescription(hit[0].field(FinancialConstants.DETAILEDCODEDESCRIPTION).getValue());
        return finResponse;

    }

    private void calculateNetAssetForCurrentYear(String keyName, Map<String, FinancialsDetailResponse> openingBalanceAsset, Sum aggrDebit, Sum aggrCredit, FinancialsDetailResponse finDetail) {

        FinancialsDetailResponse finDetails;
        if (openingBalanceAsset.keySet().contains(OPENINGBALANCE)) {
            finDetails = openingBalanceAsset.get(OPENINGBALANCE);
        } else finDetails = openingBalanceAsset.get(keyName);
        if (finDetail == null)
            finDetail = new FinancialsDetailResponse();
        if (finDetails != null) {
            finDetail
                    .setCyAssetsDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail
                    .setCyAssetsCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail.setCyAssetsNetAmount(
                    finDetail.getCyAssetsDebitAmount().subtract(finDetail.getCyAssetsCreditAmount()).add(finDetails.getCyAssetsOpbAmount()));
            finDetail.setCyAlNetAmount(finDetail.getCyLiabilitiesNetAmount().subtract(finDetail.getCyAssetsNetAmount()));
            finDetail.setCyAssetsOpbAmount(finDetails.getCyAssetsOpbAmount());

        } else {
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

    private void calculateNetLiabilityForCurrentYear(String keyName, Map<String, FinancialsDetailResponse> openingBalanceLiability, Sum aggrDebit, Sum aggrCredit, FinancialsDetailResponse finDetail) {
        FinancialsDetailResponse finDetails;
        if (openingBalanceLiability.keySet().contains(OPENINGBALANCE)) {
            finDetails = openingBalanceLiability.get(OPENINGBALANCE);
        } else finDetails = openingBalanceLiability.get(keyName);
        if (finDetail == null)
            finDetail = new FinancialsDetailResponse();
        if (finDetails != null) {

            finDetail
                    .setCyLiabilitiesDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail
                    .setCyLiabilitiesCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail.setCyLiabilitiesNetAmount(
                    finDetail.getCyLiabilitiesCreditAmount().subtract(finDetail.getCyLiabilitiesDebitAmount())
                            .add(finDetails.getCyLiabilitiesOpbAmount()).add(finDetail.getCyIeNetAmount()));
            finDetail.setCyLiabilitiesOpbAmount(finDetails.getCyLiabilitiesOpbAmount());

        } else {
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

    private void calculateNetAssetForLastYear(String keyName, Map<String, FinancialsDetailResponse> openingBalanceAsset, Sum aggrDebit, Sum aggrCredit, FinancialsDetailResponse finDetail) {

        FinancialsDetailResponse finDetails;
        if (openingBalanceAsset.keySet().contains(OPENINGBALANCE)) {
            finDetails = openingBalanceAsset.get(OPENINGBALANCE);
        } else finDetails = openingBalanceAsset.get(keyName);

        if (finDetail == null)
            finDetail = new FinancialsDetailResponse();
        if (finDetails != null) {
            finDetail
                    .setLyAssetsDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail
                    .setLyAssetsCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail.setLyAssetsNetAmount(
                    (finDetail.getLyAssetsDebitAmount().subtract(finDetail.getLyAssetsCreditAmount())).add(finDetails.getLyAssetsOpbAmount()));
            finDetail.setLyAlNetAmount(finDetail.getLyLiabilitiesNetAmount().subtract(finDetail.getLyAssetsNetAmount()));
            finDetail.setLyAssetsOpbAmount(finDetails.getLyAssetsOpbAmount());

        } else {
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

    private void calculateNetLiabilityForLastYear(String keyName, Map<String, FinancialsDetailResponse> openingBalanceLiability, Sum aggrDebit, Sum aggrCredit, FinancialsDetailResponse finDetail) {
        FinancialsDetailResponse finDetails;
        if (openingBalanceLiability.keySet().contains(OPENINGBALANCE)) {
            finDetails = openingBalanceLiability.get(OPENINGBALANCE);
        } else finDetails = openingBalanceLiability.get(keyName);
        if (finDetail == null)
            finDetail = new FinancialsDetailResponse();
        if (finDetails != null) {
            finDetail
                    .setLyLiabilitiesDebitAmount(BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail
                    .setLyLiabilitiesCreditAmount(BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
            finDetail.setLyLiabilitiesNetAmount(
                    finDetail.getLyLiabilitiesCreditAmount().subtract(finDetail.getLyLiabilitiesDebitAmount())
                            .add(finDetails.getLyLiabilitiesOpbAmount()).add(finDetail.getLyIeNetAmount()));
            finDetail.setLyLiabilitiesOpbAmount(finDetails.getLyLiabilitiesOpbAmount());

        } else {
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
                                      final FinancialsDetailResponse financialsDetail, final FinancialsDetailResponse financialsDetailResponse) {

        if (StringUtils.isNotBlank(financialsDetailsRequest.getRegion()))
            financialsDetail.setRegion(financialsDetailsRequest.getRegion());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getDistrict()))
            financialsDetail.setDistrict(financialsDetailsRequest.getDistrict());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getUlbCode()))
            financialsDetail.setUlbCode(financialsDetailsRequest.getUlbCode());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getUlbName()))
            financialsDetail.setUlbName(financialsDetailsRequest.getUlbName());

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

        if (StringUtils.isNotBlank(financialsDetailsRequest.getDetailedCode())) {
            financialsDetail.setDetailedCode(financialsDetailsRequest.getDetailedCode());
            financialsDetail.setDetailedCodeDescription(financialsDetailResponse.getDetailedCodeDescription());
        }

        if (StringUtils.isNotBlank(financialsDetailsRequest.getSchemeCode()))
            financialsDetail.setSchemeCode(financialsDetailsRequest.getSchemeCode());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getSubschemeCode()))
            financialsDetail.setSubschemeCode(financialsDetailsRequest.getSubschemeCode());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getMajorCode())) {
            financialsDetail.setMajorCode(financialsDetailsRequest.getMajorCode());
            financialsDetail.setMajorCodeDescription(financialsDetailResponse.getMajorCodeDescription());
        }

        if (StringUtils.isNotBlank(financialsDetailsRequest.getMinorCode())) {
            financialsDetail.setMinorCode(financialsDetailsRequest.getMinorCode());
            financialsDetail.setMinorCodeDescription(financialsDetailResponse.getMinorCodeDescription());
        }


    }

    public List<FinancialsBudgetDetailResponse> getBudgetData(FinancialsDetailsRequest financialsDetailsRequest,
                                                              BoolQueryBuilder boolQuery, String aggrField) {

        List<FinancialsBudgetDetailResponse> budgetDetailResponses = new ArrayList<>();
        SearchResponse finSearchResponse = getResponseFromIndex(boolQuery, aggrField);
        StringTerms aggr = finSearchResponse.getAggregations().get(AGGRFIELD);

        for (final Terms.Bucket entry : aggr.getBuckets()) {
            FinancialsBudgetDetailResponse financialsBudgetDetailResponse = populateBudgetDetailResponse(aggrField, entry);
            setFinancialsDetailsForBudget(financialsDetailsRequest, financialsBudgetDetailResponse, setBudgetResponseDetails(entry.getAggregations().get("finRecordsBudget")));
            budgetDetailResponses.add(financialsBudgetDetailResponse);

        }

        return budgetDetailResponses;

    }

    private FinancialsBudgetDetailResponse populateBudgetDetailResponse(String aggrField, final Terms.Bucket entry) {
        FinancialsBudgetDetailResponse financialsBudgetDetailResponse = new FinancialsBudgetDetailResponse();

        final Sum aggrBudgetApprovedAmount = entry.getAggregations().get(FinancialConstants.BUDGETAPPROVEDAMOUNT);
        final Sum aggrReAppropriationAmount = entry.getAggregations().get(FinancialConstants.REAPPROPRIATIONAMOUNT);
        final Sum aggrTotalBudget = entry.getAggregations().get(FinancialConstants.TOTALBUDGET);
        final Sum aggrActualAmount = entry.getAggregations().get(FinancialConstants.ACTUALAMOUNT);
        final Sum aggrPreviousYearActualAmount = entry.getAggregations().get(FinancialConstants.PREVIOUYEARACTUALAMOUNT);
        final Sum aggrCommitedExpenditure = entry.getAggregations().get(FinancialConstants.COMMITTEDEXPENDITURE);
        final Sum aggrBudgetVariance = entry.getAggregations().get(FinancialConstants.BUDGETVARIANCE);
        final TopHits topHits = entry.getAggregations().get("finRecordsBudget");
        financialsBudgetDetailResponse
                .setBudgetApprovedAmount(BigDecimal.valueOf(aggrBudgetApprovedAmount.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
        financialsBudgetDetailResponse
                .setReAppropriationAmount(BigDecimal.valueOf(aggrReAppropriationAmount.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
        financialsBudgetDetailResponse
                .setAllocatedBudget(BigDecimal.valueOf(aggrTotalBudget.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
        financialsBudgetDetailResponse
                .setActualAmount(BigDecimal.valueOf(aggrActualAmount.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
        financialsBudgetDetailResponse.setPreviouYearActualAmount(
                BigDecimal.valueOf(aggrPreviousYearActualAmount.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
        financialsBudgetDetailResponse.setCommittedExpenditure(
                BigDecimal.valueOf(aggrCommitedExpenditure.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
        financialsBudgetDetailResponse
                .setBudgetVariance(BigDecimal.valueOf(aggrBudgetVariance.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));

        FinancialsDashBoardUtils.setValuesForBudget(entry.getKeyAsString(), financialsBudgetDetailResponse, aggrField,
                setBudgetResponseDetails(topHits));
        return financialsBudgetDetailResponse;
    }

    private SearchResponse getResponseFromIndex(BoolQueryBuilder boolQuery, String aggrField) {
        if (StringUtils.isBlank(aggrField)) {
            aggrField = AGGRTYPE;
        }
        return elasticsearchTemplate.getClient()
                .prepareSearch(FinancialConstants.FINANCIAL_BUDGET_INDEX_DATA).setQuery(boolQuery)
                .addAggregation(AggregationBuilders.terms(AGGRFIELD).field(aggrField)
                        .subAggregation(
                                AggregationBuilders.sum(FinancialConstants.BUDGETAPPROVEDAMOUNT).field(FinancialConstants.BUDGETAPPROVEDAMOUNT))
                        .subAggregation(
                                AggregationBuilders.sum(FinancialConstants.REAPPROPRIATIONAMOUNT).field(FinancialConstants.REAPPROPRIATIONAMOUNT))
                        .subAggregation(
                                AggregationBuilders.sum(FinancialConstants.TOTALBUDGET).field(FinancialConstants.TOTALBUDGET))
                        .subAggregation(
                                AggregationBuilders.sum(FinancialConstants.ACTUALAMOUNT).field(FinancialConstants.ACTUALAMOUNT))
                        .subAggregation(AggregationBuilders.sum(FinancialConstants.PREVIOUYEARACTUALAMOUNT)
                                .field(FinancialConstants.PREVIOUYEARACTUALAMOUNT))
                        .subAggregation(AggregationBuilders.sum(FinancialConstants.COMMITTEDEXPENDITURE)
                                .field(FinancialConstants.COMMITTEDEXPENDITURE))
                        .subAggregation(AggregationBuilders.sum(FinancialConstants.BUDGETVARIANCE)
                                .field(FinancialConstants.BUDGETVARIANCE))
                        .subAggregation(AggregationBuilders.topHits("finRecordsBudget").addField(FinancialConstants.DISTNAME)
                                .addField(FinancialConstants.ULBNAME)
                                .addField(FinancialConstants.ULBGRADE).addField(FinancialConstants.REGNAME).addField(FinancialConstants.MAJORCODEDESCRIPTION.toLowerCase())
                                .addField(FinancialConstants.MINORCODEDESCRIPTION.toLowerCase()).addField(FinancialConstants.DETAILEDCODEDESCRIPTION.toLowerCase()).setSize(1)))
                .execute().actionGet();
    }

    private FinancialsBudgetDetailResponse setBudgetResponseDetails(final TopHits topHits) {
        FinancialsBudgetDetailResponse finBudgetResponse = new FinancialsBudgetDetailResponse();
        final SearchHit[] hit = topHits.getHits().getHits();
        finBudgetResponse.setRegion(hit[0].field(FinancialConstants.REGNAME).getValue());
        finBudgetResponse.setDistrict(hit[0].field(FinancialConstants.DISTNAME).getValue());
        finBudgetResponse.setGrade(hit[0].field(FinancialConstants.ULBGRADE).getValue());
        finBudgetResponse.setUlbName(hit[0].field(FinancialConstants.ULBNAME).getValue());
        finBudgetResponse.setMajorCodeDescription(hit[0].field(FinancialConstants.MAJORCODEDESCRIPTION.toLowerCase()).getValue());
        finBudgetResponse.setMinorCodeDescription(hit[0].field(FinancialConstants.MINORCODEDESCRIPTION.toLowerCase()).getValue());
        finBudgetResponse.setDetailedCodeDescription(hit[0].field(FinancialConstants.DETAILEDCODEDESCRIPTION.toLowerCase()).getValue());
        return finBudgetResponse;

    }

    private void setFinancialsDetailsForBudget(final FinancialsDetailsRequest financialsDetailsRequest,
                                               final FinancialsBudgetDetailResponse budgetDetailResponse, final FinancialsBudgetDetailResponse finBudgetResponse) {

        populateHeaderDataToResponse(financialsDetailsRequest, budgetDetailResponse);

        populateFinancialDataToResponse(financialsDetailsRequest, budgetDetailResponse);

        populateCOAToResponse(financialsDetailsRequest, budgetDetailResponse, finBudgetResponse);

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFromDate()))
            budgetDetailResponse.setFromDate(financialsDetailsRequest.getFromDate());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getToDate()))
            budgetDetailResponse.setToDate(financialsDetailsRequest.getToDate());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFunctionCode()))
            budgetDetailResponse.setFunctionCode(financialsDetailsRequest.getFunctionCode());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFundCode()))
            budgetDetailResponse.setFundCode(financialsDetailsRequest.getFundCode());

    }

    private void populateHeaderDataToResponse(final FinancialsDetailsRequest financialsDetailsRequest,
                                              final FinancialsBudgetDetailResponse budgetDetailResponse) {
        if (StringUtils.isNotBlank(financialsDetailsRequest.getRegion()))
            budgetDetailResponse.setRegion(financialsDetailsRequest.getRegion());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getGrade()))
            budgetDetailResponse.setGrade(financialsDetailsRequest.getGrade());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getDistrict()))
            budgetDetailResponse.setDistrict(financialsDetailsRequest.getDistrict());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getUlbCode()))
            budgetDetailResponse.setUlbCode(financialsDetailsRequest.getUlbCode());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getUlbName()))
            budgetDetailResponse.setUlbName(financialsDetailsRequest.getUlbName());

    }

    private void populateFinancialDataToResponse(final FinancialsDetailsRequest financialsDetailsRequest,
                                                 final FinancialsBudgetDetailResponse budgetDetailResponse) {
        if (StringUtils.isNotBlank(financialsDetailsRequest.getFundSource()))
            budgetDetailResponse.setFundSource(financialsDetailsRequest.getFundSource());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getDepartmentCode()))
            budgetDetailResponse.setDepartmentCode(financialsDetailsRequest.getDepartmentCode());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getSchemeCode()))
            budgetDetailResponse.setSchemeCode(financialsDetailsRequest.getSchemeCode());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getSubschemeCode()))
            budgetDetailResponse.setSubschemeCode(financialsDetailsRequest.getSubschemeCode());
    }

    private void populateCOAToResponse(final FinancialsDetailsRequest financialsDetailsRequest,
                                       final FinancialsBudgetDetailResponse budgetDetailResponse, final FinancialsBudgetDetailResponse finBudgetResponse) {
        if (StringUtils.isNotBlank(financialsDetailsRequest.getDetailedCode())) {
            budgetDetailResponse.setDetailedCode(financialsDetailsRequest.getDetailedCode());
            budgetDetailResponse.setDetailedCodeDescription(finBudgetResponse.getDetailedCodeDescription());
        }


        if (StringUtils.isNotBlank(financialsDetailsRequest.getMajorCode())) {
            budgetDetailResponse.setMajorCode(financialsDetailsRequest.getMajorCode());
            budgetDetailResponse.setMajorCodeDescription(finBudgetResponse.getMajorCodeDescription());
        }


        if (StringUtils.isNotBlank(financialsDetailsRequest.getMinorCode())) {
            budgetDetailResponse.setMinorCode(financialsDetailsRequest.getMinorCode());
            budgetDetailResponse.setMinorCodeDescription(finBudgetResponse.getMinorCodeDescription());
        }

    }

}