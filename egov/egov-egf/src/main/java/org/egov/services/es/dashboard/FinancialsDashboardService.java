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
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
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
    private static final String VOUCHER_DATE = "voucherdate";
    private static final String MONTH = "month";


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    public List<FinancialsDetailResponse> getFinancialsData(final FinancialsDetailsRequest financialsDetailsRequest,
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
        response.put(CURRENT_YEAR, getDateFromIndex(aggrField, boolQry, CY_RECOREDS));

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

        response.put(LAST_YEAR, getDateFromIndex(aggrField, boolQry, LY_RECORDS));
        financialsDetailsRequest.setFromDate(fromDate);
        financialsDetailsRequest.setToDate(toDate);

        return response;
    }

    private SearchResponse getDateFromIndex(String aggrField, BoolQueryBuilder boolQry, String year) {
        if (!MONTH.equalsIgnoreCase(aggrField)) {
            return elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME).setQuery(boolQry)
                    .addAggregation(AggregationBuilders.terms(AGGRFIELD).field(aggrField)
                            .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                            .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT))
                            .subAggregation(AggregationBuilders.topHits(year).addField(FinancialConstants.DISTNAME)
                                    .addField(FinancialConstants.ULBNAME)
                                    .addField(FinancialConstants.ULBGRADE).addField(FinancialConstants.REGNAME).addField(FinancialConstants.MAJORCODEDESCRIPTION)
                                    .addField(FinancialConstants.MINORCODEDESCRIPTION).addField(FinancialConstants.DETAILEDCODEDESCRIPTION)
                                    .addField(FinancialConstants.VOUCHERFUNDNAME).addField(FinancialConstants.VOUCHERMISFUNCTIONNAME)
                                    .addField(FinancialConstants.VOUCHERMISDEPARTMENTNAME).addField(FinancialConstants.VOUCHERMISSCHEMENAME)
                                    .addField(FinancialConstants.VOUCHERMISSUBSCHEMENAME).setSize(1)))
                    .execute().actionGet();
        } else {
            return elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME).setQuery(boolQry)
                    .addAggregation(AggregationBuilders.dateHistogram(AGGRFIELD).field(VOUCHER_DATE)
                            .interval(DateHistogramInterval.MONTH)
                            .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                            .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT))
                            .subAggregation(AggregationBuilders.topHits(year).addField(FinancialConstants.DISTNAME)
                                    .addField(FinancialConstants.ULBNAME)
                                    .addField(FinancialConstants.ULBGRADE).addField(FinancialConstants.REGNAME).addField(FinancialConstants.MAJORCODEDESCRIPTION)
                                    .addField(FinancialConstants.MINORCODEDESCRIPTION).addField(FinancialConstants.DETAILEDCODEDESCRIPTION)
                                    .addField(FinancialConstants.VOUCHERFUNDNAME).addField(FinancialConstants.VOUCHERMISFUNCTIONNAME)
                                    .addField(FinancialConstants.VOUCHERMISDEPARTMENTNAME).addField(FinancialConstants.VOUCHERMISSCHEMENAME)
                                    .addField(FinancialConstants.VOUCHERMISSUBSCHEMENAME).setSize(1)))
                    .execute().actionGet();
        }
    }

    private SearchResponse getOpeningBlncSearchResponse(final BoolQueryBuilder boolQuery,
                                                        String aggrField) {
        if (StringUtils.isBlank(aggrField) || MONTH.equalsIgnoreCase(aggrField)) {
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
        Histogram monthAggr;
        String monthName;

        for (String key : finSearchResponse.keySet()) {
            if (CURRENT_YEAR.equalsIgnoreCase(key)) {
                if (ASSETS.equalsIgnoreCase(coaType) || LIABILITIES.equalsIgnoreCase(coaType)) {
                    openingBalanceLiablity = getOpeningBalance(financialsDetailsRequest, null, LIABILITIES, CURRENT_YEAR);
                    openingBalanceAssets = getOpeningBalance(financialsDetailsRequest, null, ASSETS, CURRENT_YEAR);
                }
                if (MONTH.equalsIgnoreCase(aggrField)) {
                    monthAggr = finSearchResponse.get(CURRENT_YEAR).getAggregations().get(AGGRFIELD);
                    for (final Histogram.Bucket monthWoseEntry : monthAggr.getBuckets()) {
                        final Sum aggrDebit = monthWoseEntry.getAggregations().get(DEBITAMOUNT);
                        final Sum aggrCredit = monthWoseEntry.getAggregations().get(CREDITAMOUNT);
                        final TopHits topHits = monthWoseEntry.getAggregations().get(CY_RECOREDS);
                        String keyName = monthWoseEntry.getKeyAsString();
                        monthName = getMonthName(keyName);

                        setCurrentYearResponse(coaType, aggrField, openingBalanceLiablity, openingBalanceAssets,
                                finMap, aggrDebit, aggrCredit, topHits, monthName, financialsDetailsRequest);
                    }
                } else {
                    aggr = finSearchResponse.get(CURRENT_YEAR).getAggregations().get(AGGRFIELD);
                    for (final Terms.Bucket entry : aggr.getBuckets()) {
                        final Sum aggrDebit = entry.getAggregations().get(DEBITAMOUNT);
                        final Sum aggrCredit = entry.getAggregations().get(CREDITAMOUNT);
                        final TopHits topHits = entry.getAggregations().get(CY_RECOREDS);
                        String keyName = entry.getKeyAsString();

                        setCurrentYearResponse(coaType, aggrField, openingBalanceLiablity, openingBalanceAssets,
                                finMap, aggrDebit, aggrCredit, topHits, keyName, financialsDetailsRequest);

                    }
                }

            } else if (LAST_YEAR.equalsIgnoreCase(key)) {
                if (ASSETS.equalsIgnoreCase(coaType) || LIABILITIES.equalsIgnoreCase(coaType)) {
                    openingBalanceLiablity = getOpeningBalance(financialsDetailsRequest, null, LIABILITIES, LAST_YEAR);
                    openingBalanceAssets = getOpeningBalance(financialsDetailsRequest, null, ASSETS, LAST_YEAR);
                }

                if (MONTH.equalsIgnoreCase(aggrField)) {
                    monthAggr = finSearchResponse.get(LAST_YEAR).getAggregations().get(AGGRFIELD);
                    for (final Histogram.Bucket monthWiseEntry : monthAggr.getBuckets()) {
                        FinancialsDetailResponse financialsDetail = new FinancialsDetailResponse();
                        final Sum aggrDebit = monthWiseEntry.getAggregations().get(DEBITAMOUNT);
                        final Sum aggrCredit = monthWiseEntry.getAggregations().get(CREDITAMOUNT);
                        String keyName = monthWiseEntry.getKeyAsString();
                        final TopHits topHits = monthWiseEntry.getAggregations().get(LY_RECORDS);
                        monthName = getMonthName(keyName);
                        setLastYearResponse(financialsDetailsRequest, coaType, aggrField, openingBalanceLiablity,
                                openingBalanceAssets, finMap, financialsDetail, aggrDebit, aggrCredit, monthName, topHits);

                    }
                } else {
                    aggr = finSearchResponse.get(LAST_YEAR).getAggregations().get(AGGRFIELD);
                    for (final Terms.Bucket entry : aggr.getBuckets()) {
                        FinancialsDetailResponse financialsDetail = new FinancialsDetailResponse();
                        final Sum aggrDebit = entry.getAggregations().get(DEBITAMOUNT);
                        final Sum aggrCredit = entry.getAggregations().get(CREDITAMOUNT);
                        String keyName = entry.getKeyAsString();
                        final TopHits topHits = entry.getAggregations().get(LY_RECORDS);

                        setLastYearResponse(financialsDetailsRequest, coaType, aggrField, openingBalanceLiablity,
                                openingBalanceAssets, finMap, financialsDetail, aggrDebit, aggrCredit, keyName, topHits
                        );
                    }
                }
            }

        }
        return finMap;
    }

    private String getMonthName(String keyName) {
        Map<Integer, String> monthValuesMap = DateUtils.getAllMonthsWithFullNames();

        String[] dateArr = keyName.split("T");
        Integer month = Integer.valueOf(dateArr[0].split("-", 3)[1]);
        return monthValuesMap.get(month);
    }

    private void setCurrentYearResponse(String coaType, String aggrField, Map<String, FinancialsDetailResponse> openingBalanceLiablity,
                                        Map<String, FinancialsDetailResponse> openingBalanceAssets, Map<String, FinancialsDetailResponse> finMap,
                                        Sum aggrDebit, Sum aggrCredit, TopHits topHits, String keyName, FinancialsDetailsRequest financialsDetailsRequest) {
        FinancialsDetailResponse financialsDetails = new FinancialsDetailResponse();
        if (!finMap.isEmpty()) {
            financialsDetails = finMap.get(keyName) == null ? new FinancialsDetailResponse() : finMap.get(keyName);
        }
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
            calculateNetLiabilityForCurrentYear(keyName, openingBalanceLiablity, aggrDebit, aggrCredit, financialsDetails);

        } else if (ASSETS.equalsIgnoreCase(coaType)) {
            calculateNetAssetForCurrentYear(keyName, openingBalanceAssets, aggrDebit, aggrCredit, financialsDetails);
        }

        financialsDetails.setCyIeNetAmount(financialsDetails.getCyIncomeNetAmount().subtract(financialsDetails.getCyExpenseNetAmount()));
        financialsDetails.setLyIeNetAmount(financialsDetails.getLyIncomeNetAmount().subtract(financialsDetails.getLyExpenseNetAmount()));
        financialsDetails.setCyAlNetAmount(financialsDetails.getCyLiabilitiesNetAmount().subtract(financialsDetails.getCyAssetsNetAmount()));
        financialsDetails.setLyAlNetAmount(financialsDetails.getLyLiabilitiesNetAmount().subtract(financialsDetails.getLyAssetsNetAmount()));
        FinancialsDashBoardUtils.setValues(keyName, financialsDetails, aggrField, setResponseDetails(topHits));

        if (!finMap.isEmpty() && finMap.containsKey(keyName)) {
            finMap.remove(keyName);
            finMap.put(keyName, financialsDetails);
        } else {
            setFinancialsDetails(financialsDetailsRequest, financialsDetails, setResponseDetails(topHits), keyName);
            finMap.put(keyName, financialsDetails);
        }


    }

    private void setLastYearResponse(FinancialsDetailsRequest financialsDetailsRequest, String coaType, String aggrField,
                                     Map<String, FinancialsDetailResponse> openingBalanceLiablity, Map<String, FinancialsDetailResponse> openingBalanceAssets,
                                     Map<String, FinancialsDetailResponse> finMap, FinancialsDetailResponse financialsDetail, Sum aggrDebit,
                                     Sum aggrCredit, String keyName, TopHits topHits) {
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
            calculateNetLiabilityForLastYear(keyName, openingBalanceLiablity, aggrDebit, aggrCredit, financialsDetail);

        } else if (ASSETS.equalsIgnoreCase(coaType)) {
            calculateNetAssetForLastYear(keyName, openingBalanceAssets, aggrDebit, aggrCredit, financialsDetail);
        }

        FinancialsDetailResponse finResponse = setResponseDetails(topHits);
        FinancialsDashBoardUtils.setValues(keyName, financialsDetail, aggrField, finResponse);
        setFinancialsDetails(financialsDetailsRequest, financialsDetail, finResponse, keyName);
        finMap.put(keyName, financialsDetail);
    }


    private String verifyCoaType(String keyName) {
        if (keyName.startsWith("1"))
            return INCOME;
        else if (keyName.startsWith("2"))
            return EXPENSE;
        else if (keyName.startsWith("3"))
            return LIABILITIES;
        else
            return ASSETS;
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
        Histogram monthWiseAggr;
        Map openingBalanceLiability = new HashMap();
        Map openingBalanceAsset = new HashMap();
        Map<String, FinancialsDetailResponse> response = new HashMap<>();
        String monthName;
        for (String key : finSearchResponse.keySet()) {
            if (CURRENT_YEAR.equalsIgnoreCase(key)) {
                if (LIABILITIES.equalsIgnoreCase(coaType) || ASSETS.equalsIgnoreCase(coaType)) {
                    openingBalanceLiability = getOpeningBalance(financialsDetailsRequest, FinancialsDashBoardUtils.prepareOpeningBlncWhereClause(financialsDetailsRequest)
                            .filter(QueryBuilders.prefixQuery(DETAILED_CODE, "3")), LIABILITIES, CURRENT_YEAR);
                    openingBalanceAsset = getOpeningBalance(financialsDetailsRequest, FinancialsDashBoardUtils.prepareOpeningBlncWhereClause(financialsDetailsRequest)
                            .filter(QueryBuilders.prefixQuery(DETAILED_CODE, "4")), ASSETS, CURRENT_YEAR);
                }
                if (MONTH.equalsIgnoreCase(aggrField)) {
                    monthWiseAggr = finSearchResponse.get(CURRENT_YEAR).getAggregations().get(AGGRFIELD);
                    for (final Histogram.Bucket monthWiseEntry : monthWiseAggr.getBuckets()) {
                        final Sum aggrDebit = monthWiseEntry.getAggregations().get(DEBITAMOUNT);
                        final Sum aggrCredit = monthWiseEntry.getAggregations().get(CREDITAMOUNT);
                        final TopHits topHits = monthWiseEntry.getAggregations().get(CY_RECOREDS);
                        String keyName = monthWiseEntry.getKeyAsString();
                        monthName = getMonthName(keyName);
                        setCurrentYearFinalResponse(coaType, aggrField, openingBalanceLiability, openingBalanceAsset, response,
                                aggrDebit, aggrCredit, topHits, monthName);


                    }
                } else {
                    aggr = finSearchResponse.get(CURRENT_YEAR).getAggregations().get(AGGRFIELD);
                    for (final Terms.Bucket entry : aggr.getBuckets()) {
                        final Sum aggrDebit = entry.getAggregations().get(DEBITAMOUNT);
                        final Sum aggrCredit = entry.getAggregations().get(CREDITAMOUNT);
                        final TopHits topHits = entry.getAggregations().get(CY_RECOREDS);
                        String keyName = entry.getKeyAsString();
                        setCurrentYearFinalResponse(coaType, aggrField, openingBalanceLiability, openingBalanceAsset, response,
                                aggrDebit, aggrCredit, topHits, keyName);


                    }
                }
            } else if (LAST_YEAR.equalsIgnoreCase(key)) {

                if (LIABILITIES.equalsIgnoreCase(coaType) || ASSETS.equalsIgnoreCase(coaType)) {
                    openingBalanceLiability = getOpeningBalance(financialsDetailsRequest, FinancialsDashBoardUtils.prepareOpeningBlncWhereClause(financialsDetailsRequest)
                            .filter(QueryBuilders.prefixQuery(DETAILED_CODE, "3")), LIABILITIES, LAST_YEAR);
                    openingBalanceAsset = getOpeningBalance(financialsDetailsRequest, FinancialsDashBoardUtils.prepareOpeningBlncWhereClause(financialsDetailsRequest)
                            .filter(QueryBuilders.prefixQuery(DETAILED_CODE, "4")), ASSETS, LAST_YEAR);
                }
                response = result;
                if (MONTH.equalsIgnoreCase(aggrField)) {
                    monthWiseAggr = finSearchResponse.get(LAST_YEAR).getAggregations().get(AGGRFIELD);
                    if (!monthWiseAggr.getBuckets().isEmpty()) {
                        for (final Histogram.Bucket monthWiseEntry : monthWiseAggr.getBuckets()) {
                            final Sum aggrDebit = monthWiseEntry.getAggregations().get(DEBITAMOUNT);
                            final Sum aggrCredit = monthWiseEntry.getAggregations().get(CREDITAMOUNT);
                            final TopHits topHits = monthWiseEntry.getAggregations().get(LY_RECORDS);
                            String keyName = monthWiseEntry.getKeyAsString();
                            monthName = getMonthName(keyName);
                            setLastYearFinalResponse(result, coaType, aggrField, openingBalanceLiability,
                                    openingBalanceAsset, response, aggrDebit, aggrCredit, topHits, monthName);


                        }
                    }
                } else {
                    aggr = finSearchResponse.get(LAST_YEAR).getAggregations().get(AGGRFIELD);
                    if (!aggr.getBuckets().isEmpty()) {
                        for (final Terms.Bucket entry : aggr.getBuckets()) {
                            final Sum aggrDebit = entry.getAggregations().get(DEBITAMOUNT);
                            final Sum aggrCredit = entry.getAggregations().get(CREDITAMOUNT);
                            final TopHits topHits = entry.getAggregations().get(LY_RECORDS);
                            String keyName = entry.getKeyAsString();
                            setLastYearFinalResponse(result, coaType, aggrField, openingBalanceLiability,
                                    openingBalanceAsset, response, aggrDebit, aggrCredit, topHits, keyName);

                        }
                    }
                }
            }
        }
        return response;
    }

    private void setCurrentYearFinalResponse(String coaType, String aggrField, Map openingBalanceLiability, Map openingBalanceAsset, Map<String, FinancialsDetailResponse> response, Sum aggrDebit, Sum aggrCredit, TopHits topHits, String keyName) {
        if (!response.isEmpty()) {
            FinancialsDetailResponse finDetail = response.get(keyName);
            if (aggrField.equalsIgnoreCase(DETAILED_CODE) || aggrField.equalsIgnoreCase(MAJOR_CODE) || aggrField.equalsIgnoreCase(MINOR_CODE))
                coaType = verifyCoaType(keyName);
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
                calculateNetLiabilityForCurrentYear(keyName, openingBalanceLiability, aggrDebit, aggrCredit, finDetail);

            } else if (ASSETS.equalsIgnoreCase(coaType)) {
                calculateNetAssetForCurrentYear(keyName, openingBalanceAsset, aggrDebit, aggrCredit, finDetail);
            }
            FinancialsDashBoardUtils.setValues(keyName, finDetail, aggrField, setResponseDetails(topHits));
            if (response.containsKey(keyName)) {
                response.remove(keyName);
                response.put(keyName, finDetail);
            } else
                response.put(keyName, finDetail);
        }
    }

    private void setLastYearFinalResponse(Map<String, FinancialsDetailResponse> result, String coaType, String aggrField,
                                          Map openingBalanceLiability, Map openingBalanceAsset, Map<String, FinancialsDetailResponse> response,
                                          Sum aggrDebit, Sum aggrCredit, TopHits topHits, String keyName) {
        if (!result.isEmpty()) {
            FinancialsDetailResponse finDetail = result.get(keyName);
            if (finDetail == null) {
                finDetail = new FinancialsDetailResponse();
            }
            if (aggrField.equalsIgnoreCase(DETAILED_CODE) || aggrField.equalsIgnoreCase(MAJOR_CODE) || aggrField.equalsIgnoreCase(MINOR_CODE))
                coaType = verifyCoaType(keyName);
            if (EXPENSE.equalsIgnoreCase(coaType)) {
                finDetail.setLyExpenseDebitAmount(
                        BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                finDetail.setLyExpenseCreditAmount(
                        BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                finDetail.setLyExpenseNetAmount(finDetail.getLyExpenseDebitAmount().subtract(finDetail.getLyExpenseCreditAmount()));
                finDetail.setLyIeNetAmount(finDetail.getLyIncomeNetAmount().subtract(finDetail.getLyExpenseNetAmount()));


            } else if (LIABILITIES.equalsIgnoreCase(coaType)) {
                calculateNetLiabilityForLastYear(keyName, openingBalanceLiability, aggrDebit, aggrCredit, finDetail);

            } else if (ASSETS.equalsIgnoreCase(coaType)) {
                calculateNetAssetForLastYear(keyName, openingBalanceAsset, aggrDebit, aggrCredit, finDetail);
            }
            FinancialsDashBoardUtils.setValues(keyName, finDetail, aggrField, setResponseDetails(topHits));
            if (response.containsKey(keyName)) {
                response.remove(keyName);
                response.put(keyName, finDetail);
            } else
                response.put(keyName, finDetail);

        }
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
        finResponse.setFundName(hit[0].field(FinancialConstants.VOUCHERFUNDNAME) == null ? "" : hit[0].field(FinancialConstants.VOUCHERFUNDNAME).getValue());
        finResponse.setFunctionName(hit[0].field(FinancialConstants.VOUCHERMISFUNCTIONNAME) == null ? "" : hit[0].field(FinancialConstants.VOUCHERMISFUNCTIONNAME).getValue());
        finResponse.setDepartmentName(hit[0].field(FinancialConstants.VOUCHERMISDEPARTMENTNAME) == null ? "" : hit[0].field(FinancialConstants.VOUCHERMISDEPARTMENTNAME).getValue());
        finResponse.setSchemeName(hit[0].field(FinancialConstants.VOUCHERMISSCHEMENAME) == null ? "" : hit[0].field(FinancialConstants.VOUCHERMISSCHEMENAME).getValue());
        finResponse.setSubschemeName(hit[0].field(FinancialConstants.VOUCHERMISSUBSCHEMENAME) == null ? "" : hit[0].field(FinancialConstants.VOUCHERMISSUBSCHEMENAME).getValue());

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
                                      final FinancialsDetailResponse financialsDetail, final FinancialsDetailResponse financialsDetailResponse, String monthName) {

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

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFunctionCode())) {
            financialsDetail.setFunctionCode(financialsDetailsRequest.getFunctionCode());
            financialsDetail.setFunctionName(financialsDetailResponse.getFunctionName());
        }

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFundCode())) {
            financialsDetail.setFundCode(financialsDetailsRequest.getFundCode());
            financialsDetail.setFundName(financialsDetailResponse.getFundName());
        }

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFundSource()))
            financialsDetail.setFundSource(financialsDetailsRequest.getFundSource());

        if (StringUtils.isNotBlank(financialsDetailsRequest.getDepartmentCode())) {
            financialsDetail.setDepartmentCode(financialsDetailsRequest.getDepartmentCode());
            financialsDetail.setDepartmentName(financialsDetailResponse.getDepartmentName());
        }

        if (StringUtils.isNotBlank(financialsDetailsRequest.getDetailedCode())) {
            financialsDetail.setDetailedCode(financialsDetailsRequest.getDetailedCode());
            financialsDetail.setDetailedCodeDescription(financialsDetailResponse.getDetailedCodeDescription());
        }

        if (StringUtils.isNotBlank(financialsDetailsRequest.getSchemeCode())) {
            financialsDetail.setSchemeCode(financialsDetailsRequest.getSchemeCode());
            financialsDetail.setSchemeName(financialsDetailResponse.getSchemeName());
        }

        if (StringUtils.isNotBlank(financialsDetailsRequest.getSubschemeCode())) {
            financialsDetail.setSubschemeCode(financialsDetailsRequest.getSubschemeCode());
            financialsDetail.setSubschemeName(financialsDetailResponse.getSubschemeName());
        }

        if (StringUtils.isNotBlank(financialsDetailsRequest.getMajorCode())) {
            financialsDetail.setMajorCode(financialsDetailsRequest.getMajorCode());
            financialsDetail.setMajorCodeDescription(financialsDetailResponse.getMajorCodeDescription());
        }

        if (StringUtils.isNotBlank(financialsDetailsRequest.getMinorCode())) {
            financialsDetail.setMinorCode(financialsDetailsRequest.getMinorCode());
            financialsDetail.setMinorCodeDescription(financialsDetailResponse.getMinorCodeDescription());
        }
        if (MONTH.equalsIgnoreCase(financialsDetailsRequest.getAggregationLevel()))
            financialsDetail.setMonth(monthName);


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
                                .addField(FinancialConstants.MINORCODEDESCRIPTION.toLowerCase()).addField(FinancialConstants.DETAILEDCODEDESCRIPTION.toLowerCase())
                                .addField(FinancialConstants.FUND_NAME.toLowerCase()).addField(FinancialConstants.FUNCTION_NAME.toLowerCase())
                                .addField(FinancialConstants.DEPARTMENT_NAME.toLowerCase()).addField(FinancialConstants.SCHEME_NAME.toLowerCase())
                                .addField(FinancialConstants.SUBSCHEME_NAME.toLowerCase())
                                .setSize(1)))
                .execute().actionGet();
    }

    private FinancialsBudgetDetailResponse setBudgetResponseDetails(final TopHits topHits) {
        FinancialsBudgetDetailResponse finBudgetResponse = new FinancialsBudgetDetailResponse();
        final SearchHit[] hit = topHits.getHits().getHits();
        SearchHitField fund = hit[0].field(FinancialConstants.FUND_NAME.toLowerCase());
        SearchHitField function = hit[0].field(FinancialConstants.FUNCTION_NAME.toLowerCase());
        SearchHitField department = hit[0].field(FinancialConstants.DEPARTMENT_NAME.toLowerCase());
        SearchHitField scheme = hit[0].field(FinancialConstants.SCHEME_NAME.toLowerCase());
        SearchHitField subScheme = hit[0].field(FinancialConstants.SUBSCHEME_NAME.toLowerCase());

        finBudgetResponse.setRegion(hit[0].field(FinancialConstants.REGNAME).getValue());
        finBudgetResponse.setDistrict(hit[0].field(FinancialConstants.DISTNAME).getValue());
        finBudgetResponse.setGrade(hit[0].field(FinancialConstants.ULBGRADE).getValue());
        finBudgetResponse.setUlbName(hit[0].field(FinancialConstants.ULBNAME).getValue());
        finBudgetResponse.setMajorCodeDescription(hit[0].field(FinancialConstants.MAJORCODEDESCRIPTION.toLowerCase()).getValue());
        finBudgetResponse.setMinorCodeDescription(hit[0].field(FinancialConstants.MINORCODEDESCRIPTION.toLowerCase()).getValue());
        finBudgetResponse.setDetailedCodeDescription(hit[0].field(FinancialConstants.DETAILEDCODEDESCRIPTION.toLowerCase()).getValue());
        finBudgetResponse.setFundName(fund == null ? "" : fund.getValue());
        finBudgetResponse.setFunctionName(function == null ? "" : function.getValue());
        finBudgetResponse.setDepartmentName(department == null ? "" : department.getValue());
        finBudgetResponse.setSchemeName(scheme == null ? "" : scheme.getValue());
        finBudgetResponse.setSubschemeName(subScheme == null ? "" : subScheme.getValue());

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

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFunctionCode())) {
            budgetDetailResponse.setFunctionCode(financialsDetailsRequest.getFunctionCode());
            budgetDetailResponse.setFunctionName(finBudgetResponse.getFunctionName());
        }

        if (StringUtils.isNotBlank(financialsDetailsRequest.getFundCode())) {
            budgetDetailResponse.setFundCode(financialsDetailsRequest.getFundCode());
            budgetDetailResponse.setFundName(finBudgetResponse.getFundName());
        }

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

        if (StringUtils.isNotBlank(financialsDetailsRequest.getDepartmentCode())) {
            budgetDetailResponse.setDepartmentCode(financialsDetailsRequest.getDepartmentCode());
            budgetDetailResponse.setDepartmentName(budgetDetailResponse.getDepartmentName());
        }

        if (StringUtils.isNotBlank(financialsDetailsRequest.getSchemeCode())) {
            budgetDetailResponse.setSchemeCode(financialsDetailsRequest.getSchemeCode());
            budgetDetailResponse.setSchemeName(budgetDetailResponse.getSchemeName());
        }

        if (StringUtils.isNotBlank(financialsDetailsRequest.getSubschemeCode())) {
            budgetDetailResponse.setSubschemeCode(financialsDetailsRequest.getSubschemeCode());
            budgetDetailResponse.setSubschemeName(budgetDetailResponse.getSubschemeName());
        }
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