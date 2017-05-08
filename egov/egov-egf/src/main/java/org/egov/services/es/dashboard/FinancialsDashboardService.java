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
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class FinancialsDashboardService {

    private static final String DEBITAMOUNT = "gldebitamount";
    private static final String CREDITAMOUNT = "glcreditamount";
    private static final String INCOME = "I";
    private static final String EXPENSE = "E";
    private static final String DETAILED_CODE = "glcode";
    private static final String FIN_RECORDS = "finRecords";
    private static final String DISTRICT_NAME = "distname";
    private static final String ULB_NAME = "ulbname";
    private static final String AGGRFIELD = "aggrField";

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public List<FinancialsDetail> getIncomeExpDetails(final FinancialsDetailsRequest financialsDetailsRequest,
                                                      final BoolQueryBuilder boolQuery, final String aggrField) {
        return getFinancialsDetails(financialsDetailsRequest, boolQuery, aggrField);

    }

    private List<FinancialsDetail> getFinancialsDetails(final FinancialsDetailsRequest financialsDetailsRequest,
                                                        final BoolQueryBuilder boolQuery, final String aggrField) {

        List<FinancialsDetail> result = new ArrayList<>();
        final Aggregations finAggrIncome;
        final Aggregations finAggrExpense;
        String coaType;
        BoolQueryBuilder boolQry = boolQuery;
        if (StringUtils.isBlank(financialsDetailsRequest.getDetailedCode())) {
            // calculating income
            coaType = INCOME;
            boolQry.filter(QueryBuilders.prefixQuery(DETAILED_CODE, "1"));
            finAggrIncome = getFinAggr(boolQry, aggrField);
            result = getResponse(financialsDetailsRequest, finAggrIncome, coaType);
            finAggrExpense = getFinAggr(FinancialsDashBoardUtils.prepareWhereClause(financialsDetailsRequest).filter(QueryBuilders.prefixQuery(DETAILED_CODE, "2")), aggrField);
            return getFinalResponse(finAggrExpense, result);
        } else if (financialsDetailsRequest.getDetailedCode().startsWith("1")) {
            coaType = INCOME;
            finAggrIncome = getFinAggr(boolQuery, aggrField);
            result = getResponse(financialsDetailsRequest, finAggrIncome, coaType);
        } else if (financialsDetailsRequest.getDetailedCode().startsWith("2")) {
            coaType = EXPENSE;
            finAggrExpense = getFinAggr(boolQuery, aggrField);
            result = getResponse(financialsDetailsRequest, finAggrExpense, coaType);
        }

        return result;
    }

    private Aggregations getFinAggr(final BoolQueryBuilder boolQuery,
                                    final String aggrField) {
        boolQuery.filter(QueryBuilders.matchQuery("voucherstatusid", FinancialConstants.CREATEDVOUCHERSTATUS));
        final SearchQuery searchQueryFinDebit = new NativeSearchQueryBuilder()
                .withIndices(FinancialConstants.FINANCIAL_VOUCHER_INDEX_NAME)
                .withQuery(boolQuery).addAggregation(AggregationBuilders.terms(AGGRFIELD).field(aggrField)
                        .subAggregation(AggregationBuilders.sum(DEBITAMOUNT).field(DEBITAMOUNT))
                        .subAggregation(AggregationBuilders.sum(CREDITAMOUNT).field(CREDITAMOUNT))
                        .subAggregation(
                                AggregationBuilders.topHits(FIN_RECORDS).addField(DISTRICT_NAME).addField(ULB_NAME).setSize(1)))
                .build();
        return elasticsearchTemplate.query(searchQueryFinDebit,
                response -> response.getAggregations());

    }

    private List<FinancialsDetail> getResponse(final FinancialsDetailsRequest financialsDetailsRequest,
                                               final Aggregations finAggrDebit, final String coaType) {

        final StringTerms distAggr = finAggrDebit.get(AGGRFIELD);
        final List<FinancialsDetail> finList = new ArrayList<>();
        for (final Terms.Bucket entry : distAggr.getBuckets()) {
            FinancialsDetail financialsDetail = new FinancialsDetail();
            final Sum aggrDebit = entry.getAggregations().get(DEBITAMOUNT);
            final Sum aggrCredit = entry.getAggregations().get(CREDITAMOUNT);

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
            financialsDetail.setNetAmount(financialsDetail.getExpenseNetAmount().subtract(financialsDetail.getIncomeNetAmount()));
            final TopHits topHits = entry.getAggregations().get(FIN_RECORDS);
            final SearchHit[] hit = topHits.getHits().getHits();
            financialsDetail.setDistrict(hit[0].field(DISTRICT_NAME).getValue());
            financialsDetail.setUlbName(hit[0].field(ULB_NAME).getValue());
            setFinancialsDetails(financialsDetailsRequest, financialsDetail);
            finList.add(financialsDetail);

        }
        return finList;
    }

    private List<FinancialsDetail> getFinalResponse(final Aggregations finAggrExpense, final List<FinancialsDetail> result) {

        final StringTerms distAggr = finAggrExpense.get(AGGRFIELD);
        String distName;
        String ulbName;
        final List<FinancialsDetail> finList = new ArrayList<>();
        for (final Terms.Bucket entry : distAggr.getBuckets()) {
            final Sum aggrDebit = entry.getAggregations().get(DEBITAMOUNT);
            final Sum aggrCredit = entry.getAggregations().get(CREDITAMOUNT);
            final TopHits topHits = entry.getAggregations().get(FIN_RECORDS);
            final SearchHit[] hit = topHits.getHits().getHits();
            distName = hit[0].field(DISTRICT_NAME).getValue();
            ulbName = hit[0].field(ULB_NAME).getValue();

            for (final FinancialsDetail finDetail : result)
                if (finDetail.getUlbName().equalsIgnoreCase(ulbName) && finDetail.getDistrict().equalsIgnoreCase(distName)) {
                    finDetail.setExpenseDebitAmount(
                            BigDecimal.valueOf(aggrDebit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail.setExpenseCreditAmount(
                            BigDecimal.valueOf(aggrCredit.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP));
                    finDetail.setExpenseNetAmount(finDetail.getExpenseDebitAmount().subtract(finDetail.getExpenseCreditAmount()));
                    finDetail.setIncomeNetAmount(finDetail.getExpenseNetAmount().subtract(finDetail.getIncomeNetAmount()));
                    finList.add(finDetail);
                }

        }
        return finList;
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
