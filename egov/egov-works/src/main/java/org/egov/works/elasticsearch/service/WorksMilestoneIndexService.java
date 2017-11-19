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

package org.egov.works.elasticsearch.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.works.elasticsearch.model.WorksIndexsRequest;
import org.egov.works.elasticsearch.model.WorksMilestoneIndex;
import org.egov.works.elasticsearch.model.WorksMilestoneIndexResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.egov.works.utils.WorksConstants.APPROVED;
import static org.egov.works.utils.WorksConstants.WORKSMILESTONE_DISTNAME_COLUMN_NAME;
import static org.egov.works.utils.WorksConstants.WORKSMILESTONE_ESTIMATEDETAILID_COLUMN_NAME;
import static org.egov.works.utils.WorksConstants.WORKSMILESTONE_LOASTATUS_COLUMN_NAME;
import static org.egov.works.utils.WorksConstants.WORKSMILESTONE_MILESTONESTATUS_COLUMN_NAME;
import static org.egov.works.utils.WorksConstants.WORKSMILESTONE_TYPEOFWORKNAME_COLUMN_NAME;
import static org.egov.works.utils.WorksConstants.WORKSMILESTONE_ULBCODE_COLUMN_NAME;
import static org.egov.works.utils.WorksConstants.WORKSMILESTONE_ULBNAME_COLUMN_NAME;

/**
 * @author venki
 */

@Service
public class WorksMilestoneIndexService {

    private static final String JAN01TO15ACTUAL = "jan01to15actual";

    private static final String JAN01TO15TARGET = "jan01to15target";

    private static final String JAN16TO31ACTUAL = "jan16to31actual";

    private static final String JAN16TO31TARGET = "jan16to31target";

    private static final String FEB01TO15ACTUAL = "feb01to15actual";

    private static final String FEB01TO15TARGET = "feb01to15target";

    private static final String FEB16TO28OR29ACTUAL = "feb16to28or29actual";

    private static final String FEB16TO28OR29TARGET = "feb16to28or29target";

    private static final String MAR01TO15ACTUAL = "mar01to15actual";

    private static final String MAR01TO15TARGET = "mar01to15target";

    private static final String MAR16TO31ACTUAL = "mar16to31actual";

    private static final String MAR16TO31TARGET = "mar16to31target";

    private static final String APR01TO15ACTUAL = "apr01to15actual";

    private static final String APR01TO15TARGET = "apr01to15target";

    private static final String APR16TO30ACTUAL = "apr16to30actual";

    private static final String APR16TO30TARGET = "apr16to30target";

    private static final String MAY01TO15ACTUAL = "may01to15actual";

    private static final String MAY01TO15TARGET = "may01to15target";

    private static final String MAY16TO31ACTUAL = "may16to31actual";

    private static final String MAY16TO31TARGET = "may16to31target";

    private static final String JUN01TO15ACTUAL = "jun01to15actual";

    private static final String JUN01TO15TARGET = "jun01to15target";

    private static final String JUN16TO30ACTUAL = "jun16to30actual";

    private static final String JUN16TO30TARGET = "jun16to30target";

    private static final String JUL01TO15ACTUAL = "jul01to15actual";

    private static final String JUL01TO15TARGET = "jul01to15target";

    private static final String JUL16TO31ACTUAL = "jul16to31actual";

    private static final String JUL16TO31TARGET = "jul16to31target";

    private static final String AUG01TO15ACTUAL = "aug01to15actual";

    private static final String AUG01TO15TARGET = "aug01to15target";

    private static final String AUG16TO31ACTUAL = "aug16to31actual";

    private static final String AUG16TO31TARGET = "aug16to31target";

    private static final String SEP01TO15ACTUAL = "sep01to15actual";

    private static final String SEP01TO15TARGET = "sep01to15target";

    private static final String SEP16TO30ACTUAL = "sep16to30actual";

    private static final String SEP16TO30TARGET = "sep16to30target";

    private static final String OCT01TO15ACTUAL = "oct01to15actual";

    private static final String OCT01TO15TARGET = "oct01to15target";

    private static final String OCT16TO31ACTUAL = "oct16to31actual";

    private static final String OCT16TO31TARGET = "oct16to31target";

    private static final String NOV01TO15ACTUAL = "nov01to15actual";

    private static final String NOV01TO15TARGET = "nov01to15target";

    private static final String NOV16TO30ACTUAL = "nov16to30actual";

    private static final String NOV16TO30TARGET = "nov16to30target";

    private static final String DEC01TO15ACTUAL = "dec01to15actual";

    private static final String DEC01TO15TARGET = "dec01to15target";

    private static final String DEC16TO31ACTUAL = "dec16to31actual";

    private static final String DEC16TO31TARGET = "dec16to31target";

    private static final String BY_AGGREGATION_FIELD = "by_aggregationField";

    private static final Logger LOGGER = LoggerFactory.getLogger(WorksMilestoneIndexService.class);

    public static final String WORKSMILESTONE_INDEX_NAME = "worksmilestone";
    public static final String WORKSTRANSACTION_INDEX_NAME = "workstransaction";

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public List<WorksMilestoneIndexResponse> getAggregationResults(final WorksIndexsRequest worksIndexsRequest,
            final String orderingAggregationName) {

        Long startTime;
        Long timeTaken;
        AggregationBuilder aggregation;
        BoolQueryBuilder boolQuery;
        SearchQuery searchQuery;
        Aggregations worksAggr;
        final List<WorksMilestoneIndexResponse> finalResponses = new ArrayList<>();
        final List<WorksMilestoneIndexResponse> worksTransactionResponses = new ArrayList<>();
        final List<WorksMilestoneIndexResponse> worksMilestoneResponses = new ArrayList<>();
        final Map<String, WorksMilestoneIndexResponse> worksMilestoneResponseMap = new HashMap<>();
        WorksMilestoneIndexResponse wmIndexResponse;
        List<Terms.Bucket> resultBuckets;
        StringTerms saggr;
        LongTerms laggr;

        /* orderingAggregationName is the aggregation name by which we have to order the results */

        startTime = System.currentTimeMillis();
        boolQuery = prepareWhereClauseForTransaction(worksIndexsRequest);
        aggregation = AggregationBuilders.terms(BY_AGGREGATION_FIELD).field(orderingAggregationName).size(1000)
                .subAggregation(AggregationBuilders.sum("totalestimatedcostinlakhs").field("estimatevalue"))
                .subAggregation(AggregationBuilders.sum("totalworkordervalueinlakhs").field("loaamount"))
                .subAggregation(AggregationBuilders.sum("totalbillamountinlakhs").field("loatotalbillamt"))
                .subAggregation(AggregationBuilders.sum("totalpaidamountinlakhs").field("loatotalpaidamt"));

        searchQuery = new NativeSearchQueryBuilder().withIndices(WORKSTRANSACTION_INDEX_NAME)
                .withQuery(boolQuery)
                .withSort(SortBuilders.fieldSort(orderingAggregationName).order(SortOrder.DESC))
                .addAggregation(aggregation).build();

        worksAggr = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());

        if (!orderingAggregationName.equals(WORKSMILESTONE_ESTIMATEDETAILID_COLUMN_NAME)) {
            saggr = worksAggr.get(BY_AGGREGATION_FIELD);
            resultBuckets = saggr.getBuckets();
        } else {
            laggr = worksAggr.get(BY_AGGREGATION_FIELD);
            resultBuckets = laggr.getBuckets();
        }

        for (final Terms.Bucket entry : resultBuckets) {
            wmIndexResponse = new WorksMilestoneIndexResponse();
            wmIndexResponse.setReporttype(worksIndexsRequest.getReportType());
            final String fieldName = String.valueOf(entry.getKey());
            wmIndexResponse.setName(fieldName);
            final Sum totalEstimatedCostInLakhsAggregation = entry.getAggregations().get("totalestimatedcostinlakhs");
            final Sum totalWorkorderValueInLakhsAggregation = entry.getAggregations().get("totalworkordervalueinlakhs");
            final Sum totalBillAmountInLakhsAggregation = entry.getAggregations().get("totalbillamountinlakhs");
            final Sum totalPaidAmountInLakhsAggregation = entry.getAggregations().get("totalpaidamountinlakhs");
            wmIndexResponse.setTotalnoofworks(entry.getDocCount());
            wmIndexResponse.setTotalestimatedcostinlakhs(totalEstimatedCostInLakhsAggregation.getValue());
            wmIndexResponse.setTotalworkordervalueinlakhs(totalWorkorderValueInLakhsAggregation.getValue());
            wmIndexResponse.setTotalbillamountinlakhs(totalBillAmountInLakhsAggregation.getValue());
            wmIndexResponse.setTotalpaidamountinlakhs(totalPaidAmountInLakhsAggregation.getValue());
            worksTransactionResponses.add(wmIndexResponse);
        }

        boolQuery = prepareWhereClauseForMilestone(worksIndexsRequest);

        aggregation = AggregationBuilders.terms(BY_AGGREGATION_FIELD).field(orderingAggregationName).size(1000)
                .subAggregation(AggregationBuilders.avg(JAN01TO15ACTUAL).field(JAN01TO15ACTUAL))
                .subAggregation(AggregationBuilders.avg(JAN01TO15TARGET).field(JAN01TO15TARGET))
                .subAggregation(AggregationBuilders.avg(JAN16TO31ACTUAL).field(JAN16TO31ACTUAL))
                .subAggregation(AggregationBuilders.avg(JAN16TO31TARGET).field(JAN16TO31TARGET))
                .subAggregation(AggregationBuilders.avg(FEB01TO15ACTUAL).field(FEB01TO15ACTUAL))
                .subAggregation(AggregationBuilders.avg(FEB01TO15TARGET).field(FEB01TO15TARGET))
                .subAggregation(AggregationBuilders.avg(FEB16TO28OR29ACTUAL).field(FEB16TO28OR29ACTUAL))
                .subAggregation(AggregationBuilders.avg(FEB16TO28OR29TARGET).field(FEB16TO28OR29TARGET))
                .subAggregation(AggregationBuilders.avg(MAR01TO15ACTUAL).field(MAR01TO15ACTUAL))
                .subAggregation(AggregationBuilders.avg(MAR01TO15TARGET).field(MAR01TO15TARGET))
                .subAggregation(AggregationBuilders.avg(MAR16TO31ACTUAL).field(MAR16TO31ACTUAL))
                .subAggregation(AggregationBuilders.avg(MAR16TO31TARGET).field(MAR16TO31TARGET))
                .subAggregation(AggregationBuilders.avg(APR01TO15ACTUAL).field(APR01TO15ACTUAL))
                .subAggregation(AggregationBuilders.avg(APR01TO15TARGET).field(APR01TO15TARGET))
                .subAggregation(AggregationBuilders.avg(APR16TO30ACTUAL).field(APR16TO30ACTUAL))
                .subAggregation(AggregationBuilders.avg(APR16TO30TARGET).field(APR16TO30TARGET))
                .subAggregation(AggregationBuilders.avg(MAY01TO15ACTUAL).field(MAY01TO15ACTUAL))
                .subAggregation(AggregationBuilders.avg(MAY01TO15TARGET).field(MAY01TO15TARGET))
                .subAggregation(AggregationBuilders.avg(MAY16TO31ACTUAL).field(MAY16TO31ACTUAL))
                .subAggregation(AggregationBuilders.avg(MAY16TO31TARGET).field(MAY16TO31TARGET))
                .subAggregation(AggregationBuilders.avg(JUN01TO15ACTUAL).field(JUN01TO15ACTUAL))
                .subAggregation(AggregationBuilders.avg(JUN01TO15TARGET).field(JUN01TO15TARGET))
                .subAggregation(AggregationBuilders.avg(JUN16TO30ACTUAL).field(JUN16TO30ACTUAL))
                .subAggregation(AggregationBuilders.avg(JUN16TO30TARGET).field(JUN16TO30TARGET))
                .subAggregation(AggregationBuilders.avg(JUL01TO15ACTUAL).field(JUL01TO15ACTUAL))
                .subAggregation(AggregationBuilders.avg(JUL01TO15TARGET).field(JUL01TO15TARGET))
                .subAggregation(AggregationBuilders.avg(JUL16TO31ACTUAL).field(JUL16TO31ACTUAL))
                .subAggregation(AggregationBuilders.avg(JUL16TO31TARGET).field(JUL16TO31TARGET))
                .subAggregation(AggregationBuilders.avg(AUG01TO15ACTUAL).field(AUG01TO15ACTUAL))
                .subAggregation(AggregationBuilders.avg(AUG01TO15TARGET).field(AUG01TO15TARGET))
                .subAggregation(AggregationBuilders.avg(AUG16TO31ACTUAL).field(AUG16TO31ACTUAL))
                .subAggregation(AggregationBuilders.avg(AUG16TO31TARGET).field(AUG16TO31TARGET))
                .subAggregation(AggregationBuilders.avg(SEP01TO15ACTUAL).field(SEP01TO15ACTUAL))
                .subAggregation(AggregationBuilders.avg(SEP01TO15TARGET).field(SEP01TO15TARGET))
                .subAggregation(AggregationBuilders.avg(SEP16TO30ACTUAL).field(SEP16TO30ACTUAL))
                .subAggregation(AggregationBuilders.avg(SEP16TO30TARGET).field(SEP16TO30TARGET))
                .subAggregation(AggregationBuilders.avg(OCT01TO15ACTUAL).field(OCT01TO15ACTUAL))
                .subAggregation(AggregationBuilders.avg(OCT01TO15TARGET).field(OCT01TO15TARGET))
                .subAggregation(AggregationBuilders.avg(OCT16TO31ACTUAL).field(OCT16TO31ACTUAL))
                .subAggregation(AggregationBuilders.avg(OCT16TO31TARGET).field(OCT16TO31TARGET))
                .subAggregation(AggregationBuilders.avg(NOV01TO15ACTUAL).field(NOV01TO15ACTUAL))
                .subAggregation(AggregationBuilders.avg(NOV01TO15TARGET).field(NOV01TO15TARGET))
                .subAggregation(AggregationBuilders.avg(NOV16TO30ACTUAL).field(NOV16TO30ACTUAL))
                .subAggregation(AggregationBuilders.avg(NOV16TO30TARGET).field(NOV16TO30TARGET))
                .subAggregation(AggregationBuilders.avg(DEC01TO15ACTUAL).field(DEC01TO15ACTUAL))
                .subAggregation(AggregationBuilders.avg(DEC01TO15TARGET).field(DEC01TO15TARGET))
                .subAggregation(AggregationBuilders.avg(DEC16TO31ACTUAL).field(DEC16TO31ACTUAL))
                .subAggregation(AggregationBuilders.avg(DEC16TO31TARGET).field(DEC16TO31TARGET));

        searchQuery = new NativeSearchQueryBuilder().withIndices(WORKSMILESTONE_INDEX_NAME)
                .withQuery(boolQuery)
                .withSort(SortBuilders.fieldSort(orderingAggregationName).order(SortOrder.DESC))
                .addAggregation(aggregation).build();

        worksAggr = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());

        if (!orderingAggregationName.equals(WORKSMILESTONE_ESTIMATEDETAILID_COLUMN_NAME)) {
            saggr = worksAggr.get(BY_AGGREGATION_FIELD);
            resultBuckets = saggr.getBuckets();
        } else {
            laggr = worksAggr.get(BY_AGGREGATION_FIELD);
            resultBuckets = laggr.getBuckets();
        }

        for (final Terms.Bucket entry : resultBuckets) {
            wmIndexResponse = new WorksMilestoneIndexResponse();
            wmIndexResponse.setReporttype(worksIndexsRequest.getReportType());
            final String fieldName = String.valueOf(entry.getKey());
            wmIndexResponse.setName(fieldName);
            final Avg jan01to15actual = entry.getAggregations().get(JAN01TO15ACTUAL);
            final Avg jan01to15target = entry.getAggregations().get(JAN01TO15TARGET);
            final Avg jan16to31actual = entry.getAggregations().get(JAN16TO31ACTUAL);
            final Avg jan16to31target = entry.getAggregations().get(JAN16TO31TARGET);
            final Avg feb01to15actual = entry.getAggregations().get(FEB01TO15ACTUAL);
            final Avg feb01to15target = entry.getAggregations().get(FEB01TO15TARGET);
            final Avg feb16to28or29actual = entry.getAggregations().get(FEB16TO28OR29ACTUAL);
            final Avg feb16to28or29target = entry.getAggregations().get(FEB16TO28OR29TARGET);
            final Avg mar01to15actual = entry.getAggregations().get(MAR01TO15ACTUAL);
            final Avg mar01to15target = entry.getAggregations().get(MAR01TO15TARGET);
            final Avg mar16to31actual = entry.getAggregations().get(MAR16TO31ACTUAL);
            final Avg mar16to31target = entry.getAggregations().get(MAR16TO31TARGET);
            final Avg apr01to15actual = entry.getAggregations().get(APR01TO15ACTUAL);
            final Avg apr01to15target = entry.getAggregations().get(APR01TO15TARGET);
            final Avg apr16to30actual = entry.getAggregations().get(APR16TO30ACTUAL);
            final Avg apr16to30target = entry.getAggregations().get(APR16TO30TARGET);
            final Avg may01to15actual = entry.getAggregations().get(MAY01TO15ACTUAL);
            final Avg may01to15target = entry.getAggregations().get(MAY01TO15TARGET);
            final Avg may16to31actual = entry.getAggregations().get(MAY16TO31ACTUAL);
            final Avg may16to31target = entry.getAggregations().get(MAY16TO31TARGET);
            final Avg jun01to15actual = entry.getAggregations().get(JUN01TO15ACTUAL);
            final Avg jun01to15target = entry.getAggregations().get(JUN01TO15TARGET);
            final Avg jun16to30actual = entry.getAggregations().get(JUN16TO30ACTUAL);
            final Avg jun16to30target = entry.getAggregations().get(JUN16TO30TARGET);
            final Avg jul01to15actual = entry.getAggregations().get(JUL01TO15ACTUAL);
            final Avg jul01to15target = entry.getAggregations().get(JUL01TO15TARGET);
            final Avg jul16to31actual = entry.getAggregations().get(JUL16TO31ACTUAL);
            final Avg jul16to31target = entry.getAggregations().get(JUL16TO31TARGET);
            final Avg aug01to15actual = entry.getAggregations().get(AUG01TO15ACTUAL);
            final Avg aug01to15target = entry.getAggregations().get(AUG01TO15TARGET);
            final Avg aug16to31actual = entry.getAggregations().get(AUG16TO31ACTUAL);
            final Avg aug16to31target = entry.getAggregations().get(AUG16TO31TARGET);
            final Avg sep01to15actual = entry.getAggregations().get(SEP01TO15ACTUAL);
            final Avg sep01to15target = entry.getAggregations().get(SEP01TO15TARGET);
            final Avg sep16to30actual = entry.getAggregations().get(SEP16TO30ACTUAL);
            final Avg sep16to30target = entry.getAggregations().get(SEP16TO30TARGET);
            final Avg oct01to15actual = entry.getAggregations().get(OCT01TO15ACTUAL);
            final Avg oct01to15target = entry.getAggregations().get(OCT01TO15TARGET);
            final Avg oct16to31actual = entry.getAggregations().get(OCT16TO31ACTUAL);
            final Avg oct16to31target = entry.getAggregations().get(OCT16TO31TARGET);
            final Avg nov01to15actual = entry.getAggregations().get(NOV01TO15ACTUAL);
            final Avg nov01to15target = entry.getAggregations().get(NOV01TO15TARGET);
            final Avg nov16to30actual = entry.getAggregations().get(NOV16TO30ACTUAL);
            final Avg nov16to30target = entry.getAggregations().get(NOV16TO30TARGET);
            final Avg dec01to15actual = entry.getAggregations().get(DEC01TO15ACTUAL);
            final Avg dec01to15target = entry.getAggregations().get(DEC01TO15TARGET);
            final Avg dec16to31actual = entry.getAggregations().get(DEC16TO31ACTUAL);
            final Avg dec16to31target = entry.getAggregations().get(DEC16TO31TARGET);
            wmIndexResponse.setTotalnoofworks(entry.getDocCount());
            wmIndexResponse.setJan01to15actual(jan01to15actual.getValue());
            wmIndexResponse.setJan01to15target(jan01to15target.getValue());
            wmIndexResponse.setJan16to31actual(jan16to31actual.getValue());
            wmIndexResponse.setJan16to31target(jan16to31target.getValue());
            wmIndexResponse.setFeb01to15actual(feb01to15actual.getValue());
            wmIndexResponse.setFeb01to15target(feb01to15target.getValue());
            wmIndexResponse.setFeb16to28or29actual(feb16to28or29actual.getValue());
            wmIndexResponse.setFeb16to28or29target(feb16to28or29target.getValue());
            wmIndexResponse.setMar01to15actual(mar01to15actual.getValue());
            wmIndexResponse.setMar01to15target(mar01to15target.getValue());
            wmIndexResponse.setMar16to31actual(mar16to31actual.getValue());
            wmIndexResponse.setMar16to31target(mar16to31target.getValue());
            wmIndexResponse.setApr01to15actual(apr01to15actual.getValue());
            wmIndexResponse.setApr01to15target(apr01to15target.getValue());
            wmIndexResponse.setApr16to30actual(apr16to30actual.getValue());
            wmIndexResponse.setApr16to30target(apr16to30target.getValue());
            wmIndexResponse.setMay01to15actual(may01to15actual.getValue());
            wmIndexResponse.setMay01to15target(may01to15target.getValue());
            wmIndexResponse.setMay16to31actual(may16to31actual.getValue());
            wmIndexResponse.setMay16to31target(may16to31target.getValue());
            wmIndexResponse.setJun01to15actual(jun01to15actual.getValue());
            wmIndexResponse.setJun01to15target(jun01to15target.getValue());
            wmIndexResponse.setJun16to30actual(jun16to30actual.getValue());
            wmIndexResponse.setJun16to30target(jun16to30target.getValue());
            wmIndexResponse.setJul01to15actual(jul01to15actual.getValue());
            wmIndexResponse.setJul01to15target(jul01to15target.getValue());
            wmIndexResponse.setJul16to31actual(jul16to31actual.getValue());
            wmIndexResponse.setJul16to31target(jul16to31target.getValue());
            wmIndexResponse.setAug01to15actual(aug01to15actual.getValue());
            wmIndexResponse.setAug01to15target(aug01to15target.getValue());
            wmIndexResponse.setAug16to31actual(aug16to31actual.getValue());
            wmIndexResponse.setAug16to31target(aug16to31target.getValue());
            wmIndexResponse.setSep01to15actual(sep01to15actual.getValue());
            wmIndexResponse.setSep01to15target(sep01to15target.getValue());
            wmIndexResponse.setSep16to30actual(sep16to30actual.getValue());
            wmIndexResponse.setSep16to30target(sep16to30target.getValue());
            wmIndexResponse.setOct01to15actual(oct01to15actual.getValue());
            wmIndexResponse.setOct01to15target(oct01to15target.getValue());
            wmIndexResponse.setOct16to31actual(oct16to31actual.getValue());
            wmIndexResponse.setOct16to31target(oct16to31target.getValue());
            wmIndexResponse.setNov01to15actual(nov01to15actual.getValue());
            wmIndexResponse.setNov01to15target(nov01to15target.getValue());
            wmIndexResponse.setNov16to30actual(nov16to30actual.getValue());
            wmIndexResponse.setNov16to30target(nov16to30target.getValue());
            wmIndexResponse.setDec01to15actual(dec01to15actual.getValue());
            wmIndexResponse.setDec01to15target(dec01to15target.getValue());
            wmIndexResponse.setDec16to31actual(dec16to31actual.getValue());
            wmIndexResponse.setDec16to31target(dec16to31target.getValue());
            worksMilestoneResponses.add(wmIndexResponse);
        }

        prepareResponseMap(worksIndexsRequest, worksMilestoneResponses, worksMilestoneResponseMap);
        prepareResultList(worksIndexsRequest, worksTransactionResponses, worksMilestoneResponseMap, finalResponses);

        timeTaken = System.currentTimeMillis() - startTime;

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken for setting values in getAggregationResults() is : " + timeTaken
                    + " (millisecs) ");

        return finalResponses;

    }

    public List<WorksMilestoneIndexResponse> getAggregationResultsForUlb(final WorksIndexsRequest worksIndexsRequest,
            List<WorksMilestoneIndexResponse> resultList) {

        Long startTime;
        Long timeTaken;
        BoolQueryBuilder boolQuery;
        SearchQuery searchQuery;
        final List<WorksMilestoneIndex> worksMilestoneIndexs;
        final Map<Integer, WorksMilestoneIndexResponse> resultMap = new HashMap<>();
        for (final WorksMilestoneIndexResponse response : resultList)
            resultMap.put(response.getLineestimatedetailid(), response);

        startTime = System.currentTimeMillis();
        boolQuery = prepareWhereClauseForMilestone(worksIndexsRequest);

        searchQuery = new NativeSearchQueryBuilder().withIndices(WORKSMILESTONE_INDEX_NAME)
                .withPageable(new PageRequest(0, resultList.size()))
                .withQuery(boolQuery)
                .build();

        worksMilestoneIndexs = elasticsearchTemplate.queryForList(searchQuery, WorksMilestoneIndex.class);

        for (final WorksMilestoneIndex response : worksMilestoneIndexs) {
            resultMap.get(response.getLineestimatedetailid()).setJan01to15actual(response.getJan01to15actual());
            resultMap.get(response.getLineestimatedetailid()).setJan01to15target(response.getJan01to15target());
            resultMap.get(response.getLineestimatedetailid()).setJan16to31actual(response.getJan16to31actual());
            resultMap.get(response.getLineestimatedetailid()).setJan16to31target(response.getJan16to31target());
            resultMap.get(response.getLineestimatedetailid()).setFeb01to15actual(response.getFeb01to15actual());
            resultMap.get(response.getLineestimatedetailid()).setFeb01to15target(response.getFeb01to15target());
            resultMap.get(response.getLineestimatedetailid()).setFeb16to28or29actual(response.getFeb16to28or29actual());
            resultMap.get(response.getLineestimatedetailid()).setFeb16to28or29target(response.getFeb16to28or29target());
            resultMap.get(response.getLineestimatedetailid()).setMar01to15actual(response.getMar01to15actual());
            resultMap.get(response.getLineestimatedetailid()).setMar01to15target(response.getMar01to15target());
            resultMap.get(response.getLineestimatedetailid()).setMar16to31actual(response.getMar16to31actual());
            resultMap.get(response.getLineestimatedetailid()).setMar16to31target(response.getMar16to31target());
            resultMap.get(response.getLineestimatedetailid()).setApr01to15actual(response.getApr01to15actual());
            resultMap.get(response.getLineestimatedetailid()).setApr01to15target(response.getApr01to15target());
            resultMap.get(response.getLineestimatedetailid()).setApr16to30actual(response.getApr16to30actual());
            resultMap.get(response.getLineestimatedetailid()).setApr16to30target(response.getApr16to30target());
            resultMap.get(response.getLineestimatedetailid()).setMay01to15actual(response.getMay01to15actual());
            resultMap.get(response.getLineestimatedetailid()).setMay01to15target(response.getMay01to15target());
            resultMap.get(response.getLineestimatedetailid()).setMay16to31actual(response.getMay16to31actual());
            resultMap.get(response.getLineestimatedetailid()).setMay16to31target(response.getMay16to31target());
            resultMap.get(response.getLineestimatedetailid()).setJun01to15actual(response.getJun01to15actual());
            resultMap.get(response.getLineestimatedetailid()).setJun01to15target(response.getJun01to15target());
            resultMap.get(response.getLineestimatedetailid()).setJun16to30actual(response.getJun16to30actual());
            resultMap.get(response.getLineestimatedetailid()).setJun16to30target(response.getJun16to30target());
            resultMap.get(response.getLineestimatedetailid()).setJul01to15actual(response.getJul01to15actual());
            resultMap.get(response.getLineestimatedetailid()).setJul01to15target(response.getJul01to15target());
            resultMap.get(response.getLineestimatedetailid()).setJul16to31actual(response.getJul16to31actual());
            resultMap.get(response.getLineestimatedetailid()).setJul16to31target(response.getJul16to31target());
            resultMap.get(response.getLineestimatedetailid()).setAug01to15actual(response.getAug01to15actual());
            resultMap.get(response.getLineestimatedetailid()).setAug01to15target(response.getAug01to15target());
            resultMap.get(response.getLineestimatedetailid()).setAug16to31actual(response.getAug16to31actual());
            resultMap.get(response.getLineestimatedetailid()).setAug16to31target(response.getAug16to31target());
            resultMap.get(response.getLineestimatedetailid()).setSep01to15actual(response.getSep01to15actual());
            resultMap.get(response.getLineestimatedetailid()).setSep01to15target(response.getSep01to15target());
            resultMap.get(response.getLineestimatedetailid()).setSep16to30actual(response.getSep16to30actual());
            resultMap.get(response.getLineestimatedetailid()).setSep16to30target(response.getSep16to30target());
            resultMap.get(response.getLineestimatedetailid()).setOct01to15actual(response.getOct01to15actual());
            resultMap.get(response.getLineestimatedetailid()).setOct01to15target(response.getOct01to15target());
            resultMap.get(response.getLineestimatedetailid()).setOct16to31actual(response.getOct16to31actual());
            resultMap.get(response.getLineestimatedetailid()).setOct16to31target(response.getOct16to31target());
            resultMap.get(response.getLineestimatedetailid()).setNov01to15actual(response.getNov01to15actual());
            resultMap.get(response.getLineestimatedetailid()).setNov01to15target(response.getNov01to15target());
            resultMap.get(response.getLineestimatedetailid()).setNov16to30actual(response.getNov16to30actual());
            resultMap.get(response.getLineestimatedetailid()).setNov16to30target(response.getNov16to30target());
            resultMap.get(response.getLineestimatedetailid()).setDec01to15actual(response.getDec01to15actual());
            resultMap.get(response.getLineestimatedetailid()).setDec01to15target(response.getDec01to15target());
            resultMap.get(response.getLineestimatedetailid()).setDec16to31actual(response.getDec16to31actual());
            resultMap.get(response.getLineestimatedetailid()).setDec16to31target(response.getDec16to31target());
            resultMap.get(response.getLineestimatedetailid()).setIsmilestonecreated("Yes");
        }
        resultList = new ArrayList<>();
        for (final Integer key : resultMap.keySet())
            resultList.add(resultMap.get(key));
        timeTaken = System.currentTimeMillis() - startTime;

        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Time taken for setting values in getAggregationResultsForUlb() is : " + timeTaken
                    + " (millisecs) ");

        return resultList;

    }

    private void prepareResultList(final WorksIndexsRequest worksIndexsRequest,
            final List<WorksMilestoneIndexResponse> worksTransactionResponses,
            final Map<String, WorksMilestoneIndexResponse> worksMilestoneResponseMap,
            final List<WorksMilestoneIndexResponse> finalResponses) {
        WorksMilestoneIndexResponse finalResponse;
        for (final WorksMilestoneIndexResponse transactionResponse : worksTransactionResponses) {
            if (worksMilestoneResponseMap.get(prepareGroupingKey(worksIndexsRequest, transactionResponse)) != null) {
                final WorksMilestoneIndexResponse milestoneResponse = worksMilestoneResponseMap
                        .get(prepareGroupingKey(worksIndexsRequest, transactionResponse));
                finalResponse = milestoneResponse;
                finalResponse.setTotalbillamountinlakhs(transactionResponse.getTotalbillamountinlakhs());
                finalResponse.setTotalestimatedcostinlakhs(transactionResponse.getTotalestimatedcostinlakhs());
                finalResponse.setTotalpaidamountinlakhs(transactionResponse.getTotalpaidamountinlakhs());
                finalResponse.setTotalworkordervalueinlakhs(transactionResponse.getTotalworkordervalueinlakhs());
                finalResponse.setMilestonenotcreatedcount(
                        transactionResponse.getTotalnoofworks() - milestoneResponse.getTotalnoofworks());
                finalResponse.setTotalnoofworks(transactionResponse.getTotalnoofworks());
            } else {
                finalResponse = transactionResponse;
                finalResponse.setMilestonenotcreatedcount(transactionResponse.getTotalnoofworks());
            }
            finalResponses.add(finalResponse);

        }

    }

    private void prepareResponseMap(final WorksIndexsRequest worksIndexsRequest,
            final List<WorksMilestoneIndexResponse> worksTransactionResponses,
            final Map<String, WorksMilestoneIndexResponse> worksTransactionResponseMap) {

        for (final WorksMilestoneIndexResponse response : worksTransactionResponses)
            worksTransactionResponseMap.put(prepareGroupingKey(worksIndexsRequest, response), response);

    }

    private String prepareGroupingKey(final WorksIndexsRequest worksMilestoneIndexRequest,
            final WorksMilestoneIndexResponse response) {

        final StringBuilder groupingKey = new StringBuilder();

        if (StringUtils.isNotBlank(worksMilestoneIndexRequest.getTypeofwork()))
            groupingKey.append(worksMilestoneIndexRequest.getTypeofwork());

        if (StringUtils.isNotBlank(worksMilestoneIndexRequest.getDistname()))
            groupingKey.append(worksMilestoneIndexRequest.getDistname());

        if (StringUtils.isNotBlank(worksMilestoneIndexRequest.getUlbname()))
            groupingKey.append(worksMilestoneIndexRequest.getUlbname());

        if (worksMilestoneIndexRequest.getUlbcodes() != null && !worksMilestoneIndexRequest.getUlbcodes().isEmpty())
            for (final String ulbcode : worksMilestoneIndexRequest.getUlbcodes())
                groupingKey.append(ulbcode);

        groupingKey.append(response.getName());

        return groupingKey.toString();
    }

    private BoolQueryBuilder prepareWhereClauseForMilestone(final WorksIndexsRequest worksIndexsRequest) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (StringUtils.isNotBlank(worksIndexsRequest.getTypeofwork()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(WORKSMILESTONE_TYPEOFWORKNAME_COLUMN_NAME,
                    worksIndexsRequest.getTypeofwork()));

        if (StringUtils.isNotBlank(worksIndexsRequest.getDistname()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(WORKSMILESTONE_DISTNAME_COLUMN_NAME, worksIndexsRequest.getDistname()));

        if (StringUtils.isNotBlank(worksIndexsRequest.getUlbname()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery(WORKSMILESTONE_ULBNAME_COLUMN_NAME, worksIndexsRequest.getUlbname()));

        if (worksIndexsRequest.getUlbcodes() != null && !worksIndexsRequest.getUlbcodes().isEmpty())
            boolQuery.filter(QueryBuilders.termsQuery(WORKSMILESTONE_ULBCODE_COLUMN_NAME, worksIndexsRequest.getUlbcodes()));

        if (worksIndexsRequest.getLineestimatedetailid() != null)
            boolQuery.filter(QueryBuilders.matchQuery(WORKSMILESTONE_ESTIMATEDETAILID_COLUMN_NAME,
                    worksIndexsRequest.getLineestimatedetailid()));

        if (worksIndexsRequest.getLineestimatedetailids() != null && !worksIndexsRequest.getLineestimatedetailids().isEmpty())
            boolQuery.filter(
                    QueryBuilders.termsQuery(WORKSMILESTONE_ESTIMATEDETAILID_COLUMN_NAME,
                            worksIndexsRequest.getLineestimatedetailids()));

        boolQuery.filter(QueryBuilders.matchQuery(WORKSMILESTONE_MILESTONESTATUS_COLUMN_NAME, APPROVED));

        return boolQuery;
    }

    private BoolQueryBuilder prepareWhereClauseForTransaction(final WorksIndexsRequest worksIndexsRequest) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (StringUtils.isNotBlank(worksIndexsRequest.getTypeofwork()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(WORKSMILESTONE_TYPEOFWORKNAME_COLUMN_NAME,
                    worksIndexsRequest.getTypeofwork()));

        if (StringUtils.isNotBlank(worksIndexsRequest.getDistname()))
            boolQuery = boolQuery.filter(
                    QueryBuilders.matchQuery(WORKSMILESTONE_DISTNAME_COLUMN_NAME, worksIndexsRequest.getDistname()));

        if (StringUtils.isNotBlank(worksIndexsRequest.getUlbname()))
            boolQuery = boolQuery.filter(
                    QueryBuilders.matchQuery(WORKSMILESTONE_ULBNAME_COLUMN_NAME, worksIndexsRequest.getUlbname()));

        if (worksIndexsRequest.getUlbcodes() != null && !worksIndexsRequest.getUlbcodes().isEmpty())
            boolQuery.filter(
                    QueryBuilders.termsQuery(WORKSMILESTONE_ULBCODE_COLUMN_NAME, worksIndexsRequest.getUlbcodes()));

        if (worksIndexsRequest.getLineestimatedetailid() != null)
            boolQuery.filter(QueryBuilders.matchQuery(WORKSMILESTONE_ESTIMATEDETAILID_COLUMN_NAME,
                    worksIndexsRequest.getLineestimatedetailid()));

        boolQuery.filter(QueryBuilders.matchQuery(WORKSMILESTONE_LOASTATUS_COLUMN_NAME, APPROVED));

        return boolQuery;
    }

}