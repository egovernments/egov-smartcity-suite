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

import org.egov.egf.bean.dashboard.FinancialsDetailsRequest;
import org.egov.egf.bean.dashboard.FinancialsRatioAnalysisResponse;
import org.egov.egf.bean.dashboard.RatioAnalysisGlcode;
import org.egov.egf.es.utils.FinancialsDashBoardUtils;
import org.egov.infra.utils.DateUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RatioAnalysisDashboardService {

    private static final String DEBITAMOUNT = "gldebitamount";
    private static final String CREDITAMOUNT = "glcreditamount";
    private static final String AGGRFIELD = "aggrField";
    private static final String MAJOR_CODE = "majorcode";
    private static final String AGGRTYPE = "_type";
    private static final String VOUCHER_DATE = "voucherdate";
    private static final String MONTH = "month";
    private static final String SUBAGGREGATION = "subAggregation";
    private static final String TOPHITS = "tophit";
    private static final String CURRENT_YEAR = "currentYear";
    private static final String LAST_YEAR = "lastYear";
    private static final String CY_RECORDS = "finRecordsCy";
    private static final String LY_RECORDS = "finRecordsLy";
    private static final String DEPT_NAME = "vouchermisdepartmentname";
    private static final String DEPTWISEAGG = "deptWiseAgg";
    private static final String CYDEPTWISE = "cyDeptWise";
    private static final String LYDEPTWISE = "lyDeptWise";
    private static final String CYGRANTRECEIPT = "cyGrantReceipt";
    private static final String LYGRANTRECEIPT = "lyGrantReceipt";


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    public List<FinancialsRatioAnalysisResponse> getRatios(FinancialsDetailsRequest financialsDetailsRequest,
                                                           BoolQueryBuilder boolQuery, String aggrField) {

        List<FinancialsRatioAnalysisResponse> ratioAnalysisResponses = new ArrayList<>();
        Map<String, SearchResponse> finSearchResponse = getData(boolQuery, aggrField, financialsDetailsRequest);
        Map<String, FinancialsRatioAnalysisResponse> finRatioResponse = new HashMap<>();
        Map<String, FinancialsRatioAnalysisResponse> finalRatioResponse = new HashMap<>();
        RatioAnalysisGlcode ratioAnalysisGlcode = new RatioAnalysisGlcode();

        for (String key : finSearchResponse.keySet()) {
            if (CURRENT_YEAR.equalsIgnoreCase(key)) {
                StringTerms aggr = finSearchResponse.get(CURRENT_YEAR).getAggregations().get(AGGRFIELD);
                StringTerms aggrDeaptWise = finSearchResponse.get(CYDEPTWISE).getAggregations().get(AGGRFIELD);
                StringTerms aggrGrantReceipts = finSearchResponse.get(CYGRANTRECEIPT).getAggregations().get(AGGRFIELD);
                FinancialsRatioAnalysisResponse response = new FinancialsRatioAnalysisResponse();
                finalRatioResponse = finRatioResponse;
                for (final Terms.Bucket entry : aggr.getBuckets()) {
                    StringTerms aggr1 = entry.getAggregations().get(SUBAGGREGATION);
                    Double totalIncomeIAmount = 0.0;
                    Double totalExpenseAmount = 0.0;
                    Double totalLiabilityAmount = 0.0;
                    Double totalCapitalExpenditure = 0.0;
                    Double totalGrantReceiptsForGlcode = 0.0;

                    String keyName = entry.getKeyAsString();
                    final TopHits topHits = entry.getAggregations().get(TOPHITS);

                    for (final Terms.Bucket majorCodeEntry : aggr1.getBuckets()) {
                        final Sum debit = majorCodeEntry.getAggregations().get(DEBITAMOUNT);
                        final Sum credit = majorCodeEntry.getAggregations().get(CREDITAMOUNT);
                        final TopHits topHits1 = majorCodeEntry.getAggregations().get(CY_RECORDS);
                        final SearchHit[] hit = topHits1.getHits().getHits();

                        String majorCode = hit[0].field(MAJOR_CODE).getValue();
                        if (majorCode.startsWith("1")) {
                            totalIncomeIAmount += (Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue()));
                        }
                        if (majorCode.startsWith("2")) {
                            totalExpenseAmount += (Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue()));
                        }
                        if (majorCode.startsWith(ratioAnalysisGlcode.getCapitalGrantsReceiptsMajorCode())) {
                            totalLiabilityAmount += (Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue()));
                        }
                        if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getCapitalExpenditureFourTen()) ||
                                majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getCapitalExpenditureFourTwelve())) {
                            totalCapitalExpenditure += (Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue()));
                        }
                    }
                    totalGrantReceiptsForGlcode = getGrantReceiptAmountForGlcode(aggrGrantReceipts, totalGrantReceiptsForGlcode, keyName);

                    if (!finRatioResponse.isEmpty())
                        for (String ratioKey : finRatioResponse.keySet()) {
                            if (ratioKey.equalsIgnoreCase(keyName) && !"financialyear".equalsIgnoreCase(aggrField))
                                response = finRatioResponse.get(ratioKey) == null ? new FinancialsRatioAnalysisResponse() : finRatioResponse.get(ratioKey);

                            Map<String, Double> departmentWiseExpense = new HashMap<>();

                            for (final Terms.Bucket majorCodeEntry : aggr1.getBuckets()) {
                                final Sum debit = majorCodeEntry.getAggregations().get(DEBITAMOUNT);
                                final Sum credit = majorCodeEntry.getAggregations().get(CREDITAMOUNT);
                                final TopHits topHits1 = majorCodeEntry.getAggregations().get(CY_RECORDS);
                                final SearchHit[] hit = topHits1.getHits().getHits();

                                String majorCode = hit[0].field(MAJOR_CODE).getValue();

                                if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getIncomeTaxRevenue())) {
                                    response.setCyTaxRevenueToTotalIncomeRatio(((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) / totalIncomeIAmount) * 100);
                                }
                                if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getIncomeAssignedRevenues())) {
                                    response.setCyAssignedRevenuesToTotalIncomeRatio(((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) / totalIncomeIAmount) * 100);
                                }
                                if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getIncomeRental())) {
                                    response.setCyRentalIncomeToTotalIncomeRatio(((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) / totalIncomeIAmount) * 100);
                                }
                                if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getIncomeFeeAndUserCharges())) {
                                    response.setCyFeeUserChargesToTotalIncomeRatio(((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) / totalIncomeIAmount) * 100);
                                }
                                if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getIncomeRevenueGrants())) {
                                    response.setCyRevenueGrantsToTotalIncomeRatio(((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) / totalIncomeIAmount) * 100);
                                }
                                if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getExpenseEstablishment())) {
                                    response.setCyEstablishmentExpensesToTotalReRatio(((Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue())) / totalExpenseAmount) * 100);
                                }
                                if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getExpenseAdministrative())) {
                                    response.setCyAdministrativeExpensesToTotalReRatio(((Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue())) / totalExpenseAmount) * 100);
                                }
                                if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getExpenseOperationAndMaintenance())) {
                                    response.setCyOmExpensesToTotalReRatio(((Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue())) / totalExpenseAmount) * 100);
                                }

                                if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getCapitalGrantsReceiptsMajorCode()) ||
                                        majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getCapitalGrantsReceiptsGlcode())) {
                                    response.setCyGrantsReceiptsToTotalReceipts((((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) + totalGrantReceiptsForGlcode) /
                                            (totalIncomeIAmount + totalGrantReceiptsForGlcode + totalLiabilityAmount)) * 100);

                                }

                                if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getCapitalExpenditureFourTen()) || majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getCapitalExpenditureFourTwelve())) {
                                    response.setCyCapitalExpenditureToTotalExpenditure(((Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue())) / (totalExpenseAmount + totalCapitalExpenditure)) * 100);

                                }
                            }


                            response.setCyIncomeRatio(response.getCyTaxRevenueToTotalIncomeRatio() + response.getCyAssignedRevenuesToTotalIncomeRatio() +
                                    response.getCyRentalIncomeToTotalIncomeRatio() + response.getCyFeeUserChargesToTotalIncomeRatio() + response.getCyRevenueGrantsToTotalIncomeRatio());
                            response.setCyExpenseRatio(response.getCyEstablishmentExpensesToTotalReRatio() + response.getCyAdministrativeExpensesToTotalReRatio() +
                                    response.getCyOmExpensesToTotalReRatio());
                            response.setCyCapitalRatio(response.getCyGrantsReceiptsToTotalReceipts() + response.getCyCapitalExpenditureToTotalExpenditure());

                            getDepartmentWiseExpenseData(aggrDeaptWise, totalExpenseAmount, totalCapitalExpenditure, keyName, departmentWiseExpense);
                            response.setCyDepartmentWiseRevenueExpenses(departmentWiseExpense);
                        }
                    if (!finalRatioResponse.isEmpty() && finalRatioResponse.containsKey(keyName)) {
                        finalRatioResponse.remove(keyName);
                        setValues(financialsDetailsRequest, response, topHits, aggrField, keyName);
                        finalRatioResponse.put(keyName, response);
                    } else {
                        setValues(financialsDetailsRequest, response, topHits, aggrField, keyName);
                        finalRatioResponse.put(keyName, response);
                    }


                }
            } else if (LAST_YEAR.equalsIgnoreCase(key)) {

                StringTerms aggr = finSearchResponse.get(LAST_YEAR).getAggregations().get(AGGRFIELD);
                StringTerms aggrDeaptWise = finSearchResponse.get(LYDEPTWISE).getAggregations().get(AGGRFIELD);
                StringTerms aggrGrantReceipts = finSearchResponse.get(LYGRANTRECEIPT).getAggregations().get(AGGRFIELD);

                for (final Terms.Bucket entry : aggr.getBuckets()) {
                    StringTerms aggr1 = entry.getAggregations().get(SUBAGGREGATION);
                    Double totalIncomeIAmount = 0.0;
                    Double totalExpenseAmount = 0.0;
                    Double totalLiabilityAmount = 0.0;
                    Double totalCapitalExpenditure = 0.0;
                    Double totalGrantReceiptsForGlcode = 0.0;
                    String keyName = entry.getKeyAsString();
                    final TopHits topHits = entry.getAggregations().get(TOPHITS);
                    Map<String, Double> departmentWiseExpenseLastYear = new HashMap<>();

                    for (final Terms.Bucket majorCodeEntry : aggr1.getBuckets()) {
                        final Sum debit = majorCodeEntry.getAggregations().get(DEBITAMOUNT);
                        final Sum credit = majorCodeEntry.getAggregations().get(CREDITAMOUNT);
                        final TopHits topHits1 = majorCodeEntry.getAggregations().get(LY_RECORDS);
                        final SearchHit[] hit = topHits1.getHits().getHits();

                        String majorCode = hit[0].field(MAJOR_CODE).getValue();
                        if (majorCode.startsWith("1")) {
                            totalIncomeIAmount += (Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue()));
                        }
                        if (majorCode.startsWith("2")) {
                            totalExpenseAmount += (Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue()));
                        }
                        if (majorCode.startsWith(ratioAnalysisGlcode.getCapitalGrantsReceiptsMajorCode())) {
                            totalLiabilityAmount += (Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue()));
                        }
                        if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getCapitalExpenditureFourTen()) ||
                                majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getCapitalExpenditureFourTwelve())) {
                            totalCapitalExpenditure += (Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue()));
                        }
                    }
                    totalGrantReceiptsForGlcode = getGrantReceiptAmountForGlcode(aggrGrantReceipts, totalGrantReceiptsForGlcode, keyName);

                    FinancialsRatioAnalysisResponse response = new FinancialsRatioAnalysisResponse();
                    for (final Terms.Bucket majorCodeEntry : aggr1.getBuckets()) {
                        final Sum debit = majorCodeEntry.getAggregations().get(DEBITAMOUNT);
                        final Sum credit = majorCodeEntry.getAggregations().get(CREDITAMOUNT);
                        final TopHits topHits1 = majorCodeEntry.getAggregations().get(LY_RECORDS);
                        final SearchHit[] hit = topHits1.getHits().getHits();

                        String majorCode = hit[0].field(MAJOR_CODE).getValue();

                        if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getIncomeTaxRevenue())) {
                            response.setLyTaxRevenueToTotalIncomeRatio(((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) / totalIncomeIAmount) * 100);
                        }
                        if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getIncomeAssignedRevenues())) {
                            response.setLyAssignedRevenuesToTotalIncomeRatio(((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) / totalIncomeIAmount) * 100);
                        }
                        if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getIncomeRental())) {
                            response.setLyRentalIncomeToTotalIncomeRatio(((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) / totalIncomeIAmount) * 100);
                        }
                        if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getIncomeFeeAndUserCharges())) {
                            response.setLyFeeUserChargesToTotalIncomeRatio(((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) / totalIncomeIAmount) * 100);
                        }
                        if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getIncomeRevenueGrants())) {
                            response.setLyRevenueGrantsToTotalIncomeRatio(((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) / totalIncomeIAmount) * 100);
                        }
                        if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getExpenseEstablishment())) {
                            response.setLyEstablishmentExpensesToTotalReRatio(((Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue())) / totalExpenseAmount) * 100);
                        }
                        if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getExpenseAdministrative())) {
                            response.setLyAdministrativeExpensesToTotalReRatio(((Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue())) / totalExpenseAmount) * 100);
                        }
                        if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getExpenseOperationAndMaintenance())) {
                            response.setLyOmExpensesToTotalReRatio(((Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue())) / totalExpenseAmount) * 100);
                        }

                        if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getCapitalGrantsReceiptsMajorCode()) ||
                                majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getCapitalGrantsReceiptsGlcode())) {
                            response.setLyGrantsReceiptsToTotalReceipts((((Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue())) + totalGrantReceiptsForGlcode) / (totalIncomeIAmount + totalGrantReceiptsForGlcode + totalLiabilityAmount)) * 100);

                        }

                        if (majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getCapitalExpenditureFourTen()) || majorCode.equalsIgnoreCase(ratioAnalysisGlcode.getCapitalExpenditureFourTwelve())) {
                            response.setLyCapitalExpenditureToTotalExpenditure(((Double.valueOf(debit.getValue()) - Double.valueOf(credit.getValue())) / (totalExpenseAmount + totalCapitalExpenditure)) * 100);

                        }


                    }
                    response.setLyIncomeRatio(response.getLyTaxRevenueToTotalIncomeRatio() + response.getLyAssignedRevenuesToTotalIncomeRatio() +
                            response.getLyRentalIncomeToTotalIncomeRatio() + response.getLyFeeUserChargesToTotalIncomeRatio() + response.getLyRevenueGrantsToTotalIncomeRatio());
                    response.setLyExpenseRatio(response.getLyEstablishmentExpensesToTotalReRatio() + response.getLyAdministrativeExpensesToTotalReRatio() +
                            response.getLyOmExpensesToTotalReRatio());
                    response.setLyCapitalRatio(response.getLyGrantsReceiptsToTotalReceipts() + response.getLyCapitalExpenditureToTotalExpenditure());

                    getDepartmentWiseExpenseData(aggrDeaptWise, totalExpenseAmount, totalCapitalExpenditure, keyName, departmentWiseExpenseLastYear);

                    response.setLyDepartmentWiseRevenueExpenses(departmentWiseExpenseLastYear);

                    setValues(financialsDetailsRequest, response, topHits, aggrField, keyName);
                    finRatioResponse.put(keyName, response);
                }

            }

        }
        if (!finalRatioResponse.isEmpty())
            for (FinancialsRatioAnalysisResponse response : finalRatioResponse.values())
                ratioAnalysisResponses.add(response);
        return ratioAnalysisResponses;

    }

    private Double getGrantReceiptAmountForGlcode(StringTerms aggrGrantReceipts, Double totalGrantReceiptsForGlcode, String keyName) {
        for (final Terms.Bucket grantReceiptEntry : aggrGrantReceipts.getBuckets()) {
            if (keyName.equalsIgnoreCase(grantReceiptEntry.getKeyAsString())) {
                final Sum debit = grantReceiptEntry.getAggregations().get(DEBITAMOUNT);
                final Sum credit = grantReceiptEntry.getAggregations().get(CREDITAMOUNT);
                totalGrantReceiptsForGlcode += (Double.valueOf(credit.getValue()) - Double.valueOf(debit.getValue()));
            }

        }
        return totalGrantReceiptsForGlcode;
    }

    private void getDepartmentWiseExpenseData(StringTerms aggrDeaptWise, Double totalExpenseAmount, Double totalCapitalExpenditure, String keyName, Map<String, Double> departmentWiseExpense) {
        for (final Terms.Bucket deptWise : aggrDeaptWise.getBuckets()) {

            if (keyName.equalsIgnoreCase(deptWise.getKeyAsString())) {
                StringTerms aggrDeaptWise1 = deptWise.getAggregations().get(DEPTWISEAGG);
                for (final Terms.Bucket deptWise1 : aggrDeaptWise1.getBuckets()) {
                    final Sum debitAmount = deptWise1.getAggregations().get(DEBITAMOUNT);
                    final Sum creditAmount = deptWise1.getAggregations().get(CREDITAMOUNT);
                    departmentWiseExpense.put(deptWise1.getKeyAsString().toString(), ((Double.valueOf(debitAmount.getValue()) - Double.valueOf(creditAmount.getValue())) /
                            (totalExpenseAmount + totalCapitalExpenditure)) * 100);
                }
            }
        }
    }

    private Map<String, SearchResponse> getData(BoolQueryBuilder boolQry, String aggrField, FinancialsDetailsRequest financialsDetailsRequest) {
        Map<String, SearchResponse> response = new HashMap<>();
        response.put(CURRENT_YEAR, getDataFromIndex(boolQry, aggrField, CY_RECORDS, MAJOR_CODE));
        response.put(CYDEPTWISE, getDataFromIndexForDeptWise(boolQry, aggrField));
        BoolQueryBuilder boolQry1 = FinancialsDashBoardUtils.prepareWhereClause(financialsDetailsRequest);
        response.put(CYGRANTRECEIPT, getDataFromIndexForGrantReceiptGlcode(boolQry1, aggrField));
        financialsDetailsRequest.setFinancialYear(financialsDetailsRequest.getLastFinancialYear());
        String fromDate = financialsDetailsRequest.getFromDate();
        String toDate = financialsDetailsRequest.getToDate();
        financialsDetailsRequest.setFromDate(FinancialConstants.DATEFORMATTER_YYYY_MM_DD.format(
                DateUtils.addYears(DateUtils.getDate(financialsDetailsRequest.getFromDate(), "yyyy-MM-dd"), -1)));
        financialsDetailsRequest.setToDate(FinancialConstants.DATEFORMATTER_YYYY_MM_DD.format(
                DateUtils.addYears(DateUtils.getDate(financialsDetailsRequest.getToDate(), "yyyy-MM-dd"), -1)));
        BoolQueryBuilder boolQry2 = FinancialsDashBoardUtils.prepareWhereClause(financialsDetailsRequest);
        response.put(LAST_YEAR, getDataFromIndex(boolQry2, aggrField, LY_RECORDS, MAJOR_CODE));
        response.put(LYDEPTWISE, getDataFromIndexForDeptWise(boolQry2, aggrField));
        BoolQueryBuilder boolQry3 = FinancialsDashBoardUtils.prepareWhereClause(financialsDetailsRequest);
        response.put(LYGRANTRECEIPT, getDataFromIndexForGrantReceiptGlcode(boolQry3, aggrField));
        financialsDetailsRequest.setFromDate(fromDate);
        financialsDetailsRequest.setToDate(toDate);
        financialsDetailsRequest.setFinancialYear(financialsDetailsRequest.getCurrentFinancialYear());

        return response;
    }

    private SearchResponse getDataFromIndex(BoolQueryBuilder boolQry, String aggrField, String yearRecords, String subAggrFieldName) {

        boolQry.filter(QueryBuilders.matchQuery("voucherstatusid", FinancialConstants.CREATEDVOUCHERSTATUS));
        if (StringUtils.isBlank(aggrField)) {
            aggrField = AGGRTYPE;
        }
        if (!MONTH.equalsIgnoreCase(aggrField)) {
            return elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME).setQuery(boolQry)
                    .addAggregation(AggregationBuilders.terms(AGGRFIELD).field(aggrField).size(5000)
                            .subAggregation(AggregationBuilders.terms(SUBAGGREGATION).field(subAggrFieldName).size(5000)
                                    .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                                    .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT)).subAggregation(AggregationBuilders.topHits(yearRecords).addField(MAJOR_CODE)))
                            .subAggregation(AggregationBuilders.terms(DEPTWISEAGG).field(DEPT_NAME).size(5000)
                                    .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                                    .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT)))

                            .subAggregation(AggregationBuilders.topHits(TOPHITS).addField(FinancialConstants.DISTNAME)
                                    .addField(FinancialConstants.ULBNAME)
                                    .addField(FinancialConstants.ULBGRADE).addField(FinancialConstants.REGNAME)
                                    .addField(FinancialConstants.VOUCHERFUNDNAME).addField(FinancialConstants.VOUCHERMISFUNCTIONNAME)
                                    .addField(FinancialConstants.VOUCHERMISDEPARTMENTNAME).setSize(1)))
                    .execute().actionGet();
        } else {
            return elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME).setQuery(boolQry)
                    .addAggregation(AggregationBuilders.dateHistogram(AGGRFIELD).field(VOUCHER_DATE)
                            .subAggregation(AggregationBuilders.terms(SUBAGGREGATION).field(subAggrFieldName).size(5000)
                                    .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                                    .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT))).subAggregation(AggregationBuilders.topHits(yearRecords).addField(MAJOR_CODE)).interval(DateHistogramInterval.MONTH)
                            .subAggregation(AggregationBuilders.terms(DEPTWISEAGG).field(DEPT_NAME).size(5000)
                                    .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                                    .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT)))

                            .subAggregation(AggregationBuilders.topHits(TOPHITS).addField(FinancialConstants.DISTNAME)
                                    .addField(FinancialConstants.ULBNAME)
                                    .addField(FinancialConstants.ULBGRADE).addField(FinancialConstants.REGNAME)
                                    .addField(FinancialConstants.VOUCHERFUNDNAME).addField(FinancialConstants.VOUCHERMISFUNCTIONNAME)
                                    .addField(FinancialConstants.VOUCHERMISDEPARTMENTNAME).setSize(1)))
                    .execute().actionGet();
        }
    }

    private SearchResponse getDataFromIndexForDeptWise(BoolQueryBuilder boolQry, String aggrField) {
        RatioAnalysisGlcode ratioAnalysisGlcode = new RatioAnalysisGlcode();
        boolQry.should((QueryBuilders.prefixQuery("majorcode", "2")))
                .should(QueryBuilders.multiMatchQuery("majorcode", ratioAnalysisGlcode.getCapitalExpenditureFourTen()))
                .should(QueryBuilders.multiMatchQuery("majorcode", ratioAnalysisGlcode.getCapitalExpenditureFourTwelve()));
        if (StringUtils.isBlank(aggrField)) {
            aggrField = AGGRTYPE;
        }
        if (!MONTH.equalsIgnoreCase(aggrField)) {
            return elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME).setQuery(boolQry)
                    .addAggregation(AggregationBuilders.terms(AGGRFIELD).field(aggrField).size(5000)
                            .subAggregation(AggregationBuilders.terms(DEPTWISEAGG).field(DEPT_NAME).size(5000)
                                    .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                                    .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT))))
                    .execute().actionGet();
        } else {
            return elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME).setQuery(boolQry)
                    .addAggregation(AggregationBuilders.dateHistogram(AGGRFIELD).field(VOUCHER_DATE)
                            .subAggregation(AggregationBuilders.terms(DEPTWISEAGG).field(DEPT_NAME).size(5000)
                                    .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                                    .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT))))
                    .execute().actionGet();
        }
    }

    private SearchResponse getDataFromIndexForGrantReceiptGlcode(BoolQueryBuilder boolQry, String aggrField) {

        RatioAnalysisGlcode ratioAnalysisGlcode = new RatioAnalysisGlcode();
        boolQry.filter(QueryBuilders.matchQuery("glcode", ratioAnalysisGlcode.getCapitalGrantsReceiptsGlcode()));
        if (StringUtils.isBlank(aggrField)) {
            aggrField = AGGRTYPE;
        }
        if (!MONTH.equalsIgnoreCase(aggrField)) {
            return elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME).setQuery(boolQry)
                    .addAggregation(AggregationBuilders.terms(AGGRFIELD).field(aggrField).size(5000)
                            .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                            .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT)))
                    .execute().actionGet();
        } else {
            return elasticsearchTemplate.getClient().prepareSearch(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME).setQuery(boolQry)
                    .addAggregation(AggregationBuilders.dateHistogram(AGGRFIELD).field(VOUCHER_DATE)
                            .subAggregation(AggregationBuilders.terms(DEPTWISEAGG).field(DEPT_NAME).size(5000)
                                    .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                                    .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT))))
                    .execute().actionGet();
        }
    }

    private void setValues(FinancialsDetailsRequest financialsDetailsRequest, FinancialsRatioAnalysisResponse response, TopHits topHits,
                           String aggrField, String keyName) {


        if (financialsDetailsRequest.getRegion() != null) {
            response.setRegion(financialsDetailsRequest.getRegion());
        }
        if (financialsDetailsRequest.getDistrict() != null) {
            response.setDistrict(financialsDetailsRequest.getDistrict());
        }
        if (financialsDetailsRequest.getUlbName() != null) {
            response.setUlbName(financialsDetailsRequest.getUlbName());
        }
        if (financialsDetailsRequest.getUlbCode() != null) {
            response.setUlbCode(financialsDetailsRequest.getUlbCode());
        }
        if (financialsDetailsRequest.getFunctionCode() != null) {
            response.setFunctionCode(financialsDetailsRequest.getFunctionCode());
        }
        if (financialsDetailsRequest.getFundCode() != null) {
            response.setFundCode(financialsDetailsRequest.getFundCode());
        }
        if (financialsDetailsRequest.getDepartmentCode() != null) {
            response.setDepartmentCode(financialsDetailsRequest.getDepartmentCode());
        }
        if (financialsDetailsRequest.getFinancialYear() != null) {
            response.setFinancialYear(financialsDetailsRequest.getFinancialYear());
        }
        if (financialsDetailsRequest.getGrade() != null) {
            response.setGrade(financialsDetailsRequest.getGrade());
        }
        if (financialsDetailsRequest.getAdmWard() != null) {
            response.setAdmWardName(financialsDetailsRequest.getAdmWard());
        }
        if (financialsDetailsRequest.getAdmZone() != null) {
            response.setAdmZoneName(financialsDetailsRequest.getAdmZone());
        }
        if (financialsDetailsRequest.getFromDate() != null) {
            response.setFromDate(financialsDetailsRequest.getFromDate());
        }
        if (financialsDetailsRequest.getToDate() != null) {
            response.setToDate(financialsDetailsRequest.getToDate());
        }

        setAggregationFieldValues(response, aggrField, topHits, keyName);
    }

    private void setAggregationFieldValues(FinancialsRatioAnalysisResponse response, String aggrField, TopHits topHits, String keyName) {
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