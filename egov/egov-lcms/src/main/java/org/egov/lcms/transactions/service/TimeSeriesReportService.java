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
package org.egov.lcms.transactions.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.infra.utils.DateUtils;
import org.egov.lcms.reports.entity.TimeSeriesReportResult;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

@Service
public class TimeSeriesReportService {

    private static final String AGGREGATION_BY_FIELD = "aggregationField";
    private static final String COURTNAME = "courtName";
    private static final String PETITIONTYPE = "petitionType";
    private static final String CASETYPE = "caseType";
    private static final String COURTTYPE = "courtType";
    private static final String CASESTATUS = "status";
    private static final String OFFICERINCHRGE = "officerIncharge";
    private static final String STANDINGCOUNSEL = "advocateName";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String MONTHLY = "monthly";
    public static final String YEARLY = "yearly";
    private static final String CASE_DATE = "caseDate";
    private static final String TOTAL_COUNT = "total_count";

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public List<TimeSeriesReportResult> getTimeSeriesReportsResults(final TimeSeriesReportResult timeSeriesReportResult)
            throws ParseException {
        final String aggregationField = getAggregationFiledByType(timeSeriesReportResult);
        final Map<Integer, String> monthValuesMap = DateUtils.getAllMonths();
        Integer month;
        final SearchResponse timeSeriesReport = findAllLegalcaseDocumentByFilter(
                timeSeriesReportResult,
                getFilterQuery(timeSeriesReportResult),
                aggregationField);

        String aggregationName = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(timeSeriesReportResult.getPeriod()))
            if (MONTH.equalsIgnoreCase(timeSeriesReportResult.getPeriod()))
                aggregationName = MONTHLY;
            else if (YEAR.equalsIgnoreCase(timeSeriesReportResult.getPeriod()))
                aggregationName = YEARLY;
        TimeSeriesReportResult responseDetail;
        final List<TimeSeriesReportResult> responseDetailsList = new ArrayList<>();
        final Terms terms = timeSeriesReport.getAggregations().get(AGGREGATION_BY_FIELD);
        for (final Bucket bucket : terms.getBuckets()) {

            final Histogram agg = bucket.getAggregations().get(aggregationName);
            for (final Histogram.Bucket entry : agg.getBuckets()) {

                final String[] dateArr = entry.getKeyAsString().split("T");
                final ValueCount valueCount = entry.getAggregations().get(TOTAL_COUNT);
                if (valueCount.getValue() > 0) {
                    responseDetail = new TimeSeriesReportResult();
                    responseDetail.setAggregatedBy(bucket.getKeyAsString());
                    responseDetail.setYear(dateArr[0].split("-", 3)[0]);
                    month = Integer.valueOf(dateArr[0].split("-", 3)[1]);
                    final String monthName = monthValuesMap.get(month);
                    responseDetail.setMonth(monthName);
                    responseDetail.setCount(valueCount.getValue());
                    responseDetailsList.add(responseDetail);
                }
            }

        }

        return responseDetailsList;
    }

    public SearchResponse findAllLegalcaseDocumentByFilter(final TimeSeriesReportResult timeSeriesReportResult,
            final BoolQueryBuilder query, final String aggregationField) {
        DateHistogramInterval interval = null;
        String aggregationName = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(timeSeriesReportResult.getPeriod()))
            if (MONTH.equalsIgnoreCase(timeSeriesReportResult.getPeriod())) {
                interval = DateHistogramInterval.MONTH;
                aggregationName = MONTHLY;
            } else if (YEAR.equalsIgnoreCase(timeSeriesReportResult.getPeriod())) {
                interval = DateHistogramInterval.YEAR;
                aggregationName = YEARLY;
            }

        return elasticsearchTemplate.getClient().prepareSearch(LcmsConstants.LEGALCASE_INDEX_NAME)
                .setQuery(query)
                .addAggregation(getCountWithGrouping(AGGREGATION_BY_FIELD, aggregationField)
                        .subAggregation(AggregationBuilders.dateHistogram(aggregationName).field(CASE_DATE)
                                .interval(interval)
                                .subAggregation(AggregationBuilders.count(TOTAL_COUNT).field("lcNumber"))))
                .execute().actionGet();
    }

    private String getAggregationFiledByType(final TimeSeriesReportResult timeSeriesReportResult) {
        String aggregationField = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(timeSeriesReportResult.getAggregatedBy()))
            if (timeSeriesReportResult.getAggregatedBy().equals(LcmsConstants.COURTNAME))
                aggregationField = COURTNAME;
            else if (timeSeriesReportResult.getAggregatedBy().equals(LcmsConstants.PETITIONTYPE))
                aggregationField = PETITIONTYPE;
            else if (timeSeriesReportResult.getAggregatedBy().equals(LcmsConstants.CASECATEGORY))
                aggregationField = CASETYPE;
            else if (timeSeriesReportResult.getAggregatedBy().equals(LcmsConstants.CASESTATUS))
                aggregationField = CASESTATUS;
            else if (timeSeriesReportResult.getAggregatedBy().equals(LcmsConstants.COURTTYPE))
                aggregationField = COURTTYPE;
            else if (timeSeriesReportResult.getAggregatedBy().equals(LcmsConstants.OFFICERINCHRGE))
                aggregationField = OFFICERINCHRGE;
            else if (timeSeriesReportResult.getAggregatedBy().equals(LcmsConstants.STANDINGCOUNSEL))
                aggregationField = STANDINGCOUNSEL;
        return aggregationField;
    }

    private BoolQueryBuilder getFilterQuery(final TimeSeriesReportResult searchRequest) throws ParseException {
        final SimpleDateFormat formatter = new SimpleDateFormat(LcmsConstants.DATE_FORMAT);
        final SimpleDateFormat newFormat = new SimpleDateFormat(ApplicationConstant.ES_DATE_FORMAT);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.termQuery("cityName", ApplicationThreadLocals.getCityName()));

        if (StringUtils.isNotBlank(searchRequest.getCaseFromDate()))
            boolQuery = boolQuery.filter(QueryBuilders.rangeQuery(CASE_DATE)
                    .gte(newFormat.format(formatter.parse(searchRequest.getCaseFromDate())))
                    .lte(new DateTime(newFormat.format(formatter.parse(searchRequest.getCaseToDate())))));

        return boolQuery;
    }

    @SuppressWarnings("rawtypes")
    public static AggregationBuilder getCountWithGrouping(final String aggregationName, final String fieldName) {
        return AggregationBuilders.terms(aggregationName).field(fieldName);
    }

}
