/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.lcms.transactions.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.lcms.reports.entity.LegalCommonReportResult;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

@Service
public class GenericSubReportService {

    private static final String AGGREGATION_BY_FIELD = "aggregationField";
    private static final String COURTNAME = "courtName";
    private static final String PETITIONTYPE = "petitionType";
    private static final String CASETYPE = "caseType";
    private static final String COURTTYPE = "courtType";
    private static final String CASESTATUS = "status";
    private static final String OFFICERINCHRGE = "officerIncharge";
    private static final String JUDGEMENTOUTCOME = "judgmentOutcome";

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private LegalCommonReportService legalCommonReportService;

    public List<LegalCommonReportResult> getGenericSubReports(final LegalCommonReportResult genericSubReport)
            throws ParseException {
        List<LegalCommonReportResult> finalResult = new ArrayList<>();
        if (genericSubReport.getAggregatedBy() != null)
            finalResult = getGenericReportwithAggregatedValues(genericSubReport);
        else
            finalResult = legalCommonReportService.getLegalCommonReportsResults(genericSubReport, StringUtils.EMPTY);
        return finalResult;

    }

    private List<LegalCommonReportResult> getGenericReportwithAggregatedValues(final LegalCommonReportResult genericSubReport) {
        final String aggregationField = getaggregationFiledByType(genericSubReport);
        final SearchResponse timeSeriesReport = findAllLegalcaseIndexByFilter(
                genericSubReport,
                getFilterQuery(genericSubReport),
                aggregationField);

        LegalCommonReportResult responseDetail;
        final List<LegalCommonReportResult> responseDetailsList = new ArrayList<>();
        final Terms terms = timeSeriesReport.getAggregations().get(AGGREGATION_BY_FIELD);
        for (final Bucket bucket : terms.getBuckets()) {

            final ValueCount valueCount = bucket.getAggregations().get("total_count");
            if (valueCount.getValue() > 0) {
                responseDetail = new LegalCommonReportResult();
                responseDetail.setAggregatedBy(bucket.getKeyAsString());
                responseDetail.setCount(valueCount.getValue());
                responseDetailsList.add(responseDetail);
            }

        }

        return responseDetailsList;
    }

    public SearchResponse findAllLegalcaseIndexByFilter(final LegalCommonReportResult genericSubReport,
            final BoolQueryBuilder query, final String aggregationField) {

        return elasticsearchTemplate.getClient().prepareSearch(LcmsConstants.LEGALCASE_INDEX_NAME)
                .setQuery(query)
                .addAggregation(AggregationBuilders.terms(AGGREGATION_BY_FIELD).field(aggregationField)
                        .subAggregation(AggregationBuilders.count("total_count").field("lcNumber")))
                .execute().actionGet();
    }

    private String getaggregationFiledByType(final LegalCommonReportResult genericSubReport) {
        String aggregationField = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(genericSubReport.getAggregatedBy()))
            if (genericSubReport.getAggregatedBy().equals(LcmsConstants.COURTNAME))
                aggregationField = COURTNAME;
            else if (genericSubReport.getAggregatedBy().equals(LcmsConstants.PETITIONTYPE))
                aggregationField = PETITIONTYPE;
            else if (genericSubReport.getAggregatedBy().equals(LcmsConstants.CASECATEGORY))
                aggregationField = CASETYPE;
            else if (genericSubReport.getAggregatedBy().equals(LcmsConstants.CASESTATUS))
                aggregationField = CASESTATUS;
            else if (genericSubReport.getAggregatedBy().equals(LcmsConstants.COURTTYPE))
                aggregationField = COURTTYPE;
            else if (genericSubReport.getAggregatedBy().equals(LcmsConstants.OFFICERINCHRGE))
                aggregationField = OFFICERINCHRGE;
            else if (genericSubReport.getAggregatedBy().equals(LcmsConstants.JUDGEMENTOUTCOME))
                aggregationField = JUDGEMENTOUTCOME;
        return aggregationField;
    }

    private BoolQueryBuilder getFilterQuery(final LegalCommonReportResult searchRequest) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.termQuery("cityName", ApplicationThreadLocals.getCityName()));
        if (StringUtils.isNotBlank(searchRequest.getOfficerIncharge()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery(OFFICERINCHRGE, searchRequest.getOfficerIncharge().split("@")[0]));
        if (StringUtils.isNotBlank(searchRequest.getCaseCategory()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery(CASETYPE, searchRequest.getCaseCategory()));
        if (StringUtils.isNotBlank(searchRequest.getCourtName()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery(COURTNAME, searchRequest.getCourtName()));
        if (StringUtils.isNotBlank(searchRequest.getCaseStatus()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery(CASESTATUS, searchRequest.getCaseStatus()));
        if (StringUtils.isNotBlank(searchRequest.getCourtType()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery(COURTTYPE, searchRequest.getCourtType()));
        if (StringUtils.isNotBlank(searchRequest.getPetitionType()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery(PETITIONTYPE, searchRequest.getPetitionType()));
        if (StringUtils.isNotBlank(searchRequest.getJudgmentType()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery(JUDGEMENTOUTCOME, searchRequest.getJudgmentType()));
        if (StringUtils.isNotBlank(searchRequest.getStandingCounsel()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery("advocateName", searchRequest.getStandingCounsel()));
        if (StringUtils.isNotBlank(searchRequest.getReportStatus()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.termQuery("subStatus", searchRequest.getReportStatus()));

        return boolQuery;
    }

}