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

package org.egov.works.elasticsearch.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.works.elasticsearch.model.WorksMilestoneIndexRequest;
import org.egov.works.elasticsearch.model.WorksMilestoneIndexResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class WorksMilestoneIndexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorksMilestoneIndexService.class);

    public static final String WORKSMILESTONE_INDEX_NAME = "worksmilestone";

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * Returns Type of work wise grouping and total amount aggregation
     *
     * @param collectionDetailsRequest
     * @param indexName
     * @param order
     * @param orderingAggregationName
     * @return
     */
    public List<WorksMilestoneIndexResponse> returnTypeOfWorkWiseAggregationResults(
            final WorksMilestoneIndexRequest worksMilestoneIndexRequest, final Boolean order,
            final String orderingAggregationName) {
        final List<WorksMilestoneIndexResponse> worksMilestoneIndexResponses = new ArrayList<>();
        final BoolQueryBuilder boolQuery = prepareWhereClause(worksMilestoneIndexRequest);

        // orderingAggregationName is the aggregation name by which we have to
        // order the results
        Long startTime = System.currentTimeMillis();
        final AggregationBuilder aggregation = AggregationBuilders.terms("by_aggregationField").field(orderingAggregationName)
                .subAggregation(AggregationBuilders.sum("totalestimatedcostinlakhs").field("estimatevalue"))
                .subAggregation(AggregationBuilders.sum("totalworkordervalueinlakhs").field("loaamount"))
                .subAggregation(AggregationBuilders.sum("totalbillamountinlakhs").field("loatotalbillamt"))
                .subAggregation(AggregationBuilders.sum("totalpaidamountinlakhs").field("loatotalpaidamt"))
                .subAggregation(AggregationBuilders.avg("oct01to15actual").field("oct01to15actual"))
                .subAggregation(AggregationBuilders.avg("oct01to15target").field("oct01to15target"))
                .subAggregation(AggregationBuilders.avg("oct16to31actual").field("oct16to31actual"))
                .subAggregation(AggregationBuilders.avg("oct16to31target").field("oct16to31target"));

        final SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(WORKSMILESTONE_INDEX_NAME)
                .withQuery(boolQuery)
                .withSort(SortBuilders.fieldSort(orderingAggregationName).order(SortOrder.DESC))
                .addAggregation(aggregation).build();

        final Aggregations worksMilestoneAggr = elasticsearchTemplate.query(searchQueryColl,
                response -> response.getAggregations());

        Long timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken by type of work WiseAggregations is : " + timeTaken + " (millisecs) ");

        WorksMilestoneIndexResponse wmIndexResponse;
        startTime = System.currentTimeMillis();
        final StringTerms totalAmountAggr = worksMilestoneAggr.get("by_aggregationField");
        for (final Terms.Bucket entry : totalAmountAggr.getBuckets()) {
            wmIndexResponse = new WorksMilestoneIndexResponse();
            final String fieldName = String.valueOf(entry.getKey());
            wmIndexResponse.setName(fieldName);
            final Sum totalEstimatedCostInLakhsAggregation = entry.getAggregations().get("totalestimatedcostinlakhs");
            final Sum totalWorkorderValueInLakhsAggregation = entry.getAggregations().get("totalworkordervalueinlakhs");
            final Sum totalBillAmountInLakhsAggregation = entry.getAggregations().get("totalbillamountinlakhs");
            final Sum totalPaidAmountInLakhsAggregation = entry.getAggregations().get("totalworkordervalueinlakhs");
            final InternalAvg oct01to15actual = entry.getAggregations().get("oct01to15actual");
            final InternalAvg oct01to15target = entry.getAggregations().get("oct01to15target");
            final InternalAvg oct16to31actual = entry.getAggregations().get("oct16to31actual");
            final InternalAvg oct16to31target = entry.getAggregations().get("oct16to31target");
            wmIndexResponse.setTotalnoofworks(entry.getDocCount());
            wmIndexResponse
                    .setTotalestimatedcostinlakhs(totalEstimatedCostInLakhsAggregation.getValue());
            wmIndexResponse
                    .setTotalworkordervalueinlakhs(totalWorkorderValueInLakhsAggregation.getValue());
            wmIndexResponse.setTotalbillamountinlakhs(totalBillAmountInLakhsAggregation.getValue());
            wmIndexResponse.setTotalpaidamountinlakhs(totalPaidAmountInLakhsAggregation.getValue());
            wmIndexResponse.setOct01to15actual(oct01to15actual.getValue());
            wmIndexResponse.setOct01to15target(oct01to15target.getValue());
            wmIndexResponse.setOct16to31actual(oct16to31actual.getValue());
            wmIndexResponse.setOct16to31target(oct16to31target.getValue());
            worksMilestoneIndexResponses.add(wmIndexResponse);
        }
        timeTaken = System.currentTimeMillis() - startTime;
        LOGGER.debug("Time taken for setting values in returnUlbWiseAggregationResults() is : " + timeTaken
                + " (millisecs) ");
        return worksMilestoneIndexResponses;
    }

    private BoolQueryBuilder prepareWhereClause(final WorksMilestoneIndexRequest worksMilestoneIndexRequest) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(worksMilestoneIndexRequest.getTypeofwork()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery("lineestimatetypeofwork", worksMilestoneIndexRequest.getTypeofwork()));
        if (StringUtils.isNotBlank(worksMilestoneIndexRequest.getDistname()))
            boolQuery = boolQuery
                    .filter(QueryBuilders.matchQuery("distname", worksMilestoneIndexRequest.getDistname()));
        if (StringUtils.isNotBlank(worksMilestoneIndexRequest.getUlbcode()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("ulbcode", worksMilestoneIndexRequest.getUlbcode()));
        if (worksMilestoneIndexRequest.getUlbcodes() != null && !worksMilestoneIndexRequest.getUlbcodes().isEmpty())
            boolQuery.filter(QueryBuilders.termsQuery("ulbcode", worksMilestoneIndexRequest.getUlbcodes()));
        return boolQuery;
    }

}