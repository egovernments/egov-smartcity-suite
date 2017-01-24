/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.repository.es;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.pgr.repository.es.util.ComplaintElasticsearchUtils.getAverageWithExclusion;
import static org.egov.pgr.repository.es.util.ComplaintElasticsearchUtils.getAverageWithFilter;
import static org.egov.pgr.repository.es.util.ComplaintElasticsearchUtils.getCount;
import static org.egov.pgr.repository.es.util.ComplaintElasticsearchUtils.getCountBetweenSpecifiedDates;
import static org.egov.pgr.repository.es.util.ComplaintElasticsearchUtils.getCountWithGrouping;
import static org.egov.pgr.repository.es.util.ComplaintElasticsearchUtils.getCountWithGroupingAndOrder;
import static org.egov.pgr.utils.constants.PGRConstants.CITY_CODE;
import static org.egov.pgr.utils.constants.PGRConstants.CITY_NAME;
import static org.egov.pgr.utils.constants.PGRConstants.DISTRICT_NAME;
import static org.egov.pgr.utils.constants.PGRConstants.PGR_INDEX_DATE_FORMAT;
import static org.egov.pgr.utils.constants.PGRConstants.PGR_INDEX_NAME;
import static org.egov.pgr.utils.constants.PGRConstants.WARD_NAME;
import static org.egov.pgr.utils.constants.PGRConstants.WARD_NUMBER;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.entity.es.ComplaintDashBoardRequest;
import org.egov.pgr.entity.es.ComplaintIndex;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

public class ComplaintIndexRepositoryImpl implements ComplaintIndexCustomRepository {

    private static final String LOCALITY_NAME = "localityName";

    private static final String RE_OPENED = "reOpened";

    private static final String RE_OPENED_COMPLAINT_COUNT = "reOpenedComplaintCount";

    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern(PGR_INDEX_DATE_FORMAT);

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public Map<String, SearchResponse> findAllGrievanceByFilter(final ComplaintDashBoardRequest complaintDashBoardRequest,
            final BoolQueryBuilder query, final String grouByField) {
        /**
         * For Current day's complaint count if dates are sent in the request, consider the toDate, else take date range between
         * current date +1 day
         */
        DateTime fromDate = new DateTime();
        DateTime toDate;
        if (isNotBlank(complaintDashBoardRequest.getFromDate())
                && isNotBlank(complaintDashBoardRequest.getToDate())) {
            fromDate = new DateTime(complaintDashBoardRequest.getFromDate());
            toDate = fromDate.plusDays(1);
        } else
            toDate = fromDate.plusDays(1);

        // This is size used to fetch those many number of documents
        int size = 120;
        if (complaintDashBoardRequest.getSize() >= 0)
            size = complaintDashBoardRequest.getSize();

        DateTime currentYearFromDate = new DateTime();
        if (fromDate.getMonthOfYear() < 4)
            currentYearFromDate = currentYearFromDate.minusYears(1).withMonthOfYear(4).dayOfMonth().withMinimumValue();
        else
            currentYearFromDate = currentYearFromDate.withMonthOfYear(4).dayOfMonth().withMinimumValue();

        final SearchResponse consolidatedResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                .setQuery(query).setSize(0)
                .addAggregation(getCount("countAggregation", "crn"))
                .addAggregation(getCountWithGrouping("closedCount", "ifClosed", 2))
                .addAggregation(getCountWithGrouping("slaCount", "ifSLA", 2))
                .addAggregation(getAverageWithFilter("ifClosed", 1, "AgeingInWeeks", "complaintAgeingdaysFromDue"))
                .addAggregation(getAverageWithExclusion("satisfactionAverage", "satisfactionIndex"))
                .addAggregation(getCountBetweenSpecifiedDates("currentYear", "createdDate",
                        currentYearFromDate.toString(formatter),
                        new DateTime().toString(formatter)))
                .addAggregation(getCountBetweenSpecifiedDates("todaysComplaintCount", "createdDate",
                        fromDate.toString(formatter), toDate.toString(formatter)))
                .execute().actionGet();

        final SearchResponse tableResponse = elasticsearchTemplate
                .getClient()
                .prepareSearch(PGR_INDEX_NAME)
                .setQuery(query)
                .setSize(0)
                .addAggregation(
                        getCountWithGroupingAndOrder("complaintTypeWise", "complaintTypeName", size,
                                complaintDashBoardRequest.getSortField(), complaintDashBoardRequest.getSortDirection())
                                        .subAggregation(
                                                getAverageWithExclusion("complaintTypeSatisfactionAverage", "satisfactionIndex"))
                                        .subAggregation(
                                                getCountWithGrouping("complaintTypeWiseOpenAndClosedCount", "ifClosed", 2)
                                                        .subAggregation(
                                                                AggregationBuilders.range("ComplaintTypeAgeing")
                                                                        .field("complaintAgeingdaysFromDue")
                                                                        .addRange("1week", 0, 8).addRange("1month", 8, 32)
                                                                        .addRange("3months", 32, 91)
                                                                        .addUnboundedFrom("remainingMonths", 91))
                                                        .subAggregation(getCountWithGrouping("complaintTypeSla", "ifSLA", 2))))
                .addAggregation(
                        getCountWithGroupingAndOrder("groupByField", grouByField, size,
                                complaintDashBoardRequest.getSortField(), complaintDashBoardRequest.getSortDirection())
                                        .subAggregation(
                                                getAverageWithExclusion("groupByFieldSatisfactionAverage", "satisfactionIndex"))
                                        .subAggregation(
                                                getCountWithGrouping("groupFieldWiseOpenAndClosedCount", "ifClosed", 2)
                                                        .subAggregation(
                                                                AggregationBuilders.range("groupByFieldAgeing")
                                                                        .field("complaintAgeingdaysFromDue")
                                                                        .addRange("1week", 0, 8).addRange("1month", 8, 32)
                                                                        .addRange("3months", 32, 91)
                                                                        .addUnboundedFrom("remainingMonths", 91))
                                                        .subAggregation(getCountWithGrouping("groupByFieldSla", "ifSLA", 2))))
                .execute().actionGet();

        final HashMap<String, SearchResponse> result = new HashMap<>();

        if (grouByField.equals(LOCALITY_NAME)) {
            final SearchResponse localityMissingResponse = elasticsearchTemplate
                    .getClient()
                    .prepareSearch(PGR_INDEX_NAME)
                    .setQuery(query)
                    .setSize(0)
                    .addAggregation(AggregationBuilders.missing("nolocality").field(LOCALITY_NAME)
                            .subAggregation(
                                    getAverageWithExclusion("groupByFieldSatisfactionAverage", "satisfactionIndex"))
                            .subAggregation(
                                    getCountWithGrouping("groupFieldWiseOpenAndClosedCount", "ifClosed", 2)
                                            .subAggregation(
                                                    AggregationBuilders.range("groupByFieldAgeing")
                                                            .field("complaintAgeingdaysFromDue")
                                                            .addRange("1week", 0, 8).addRange("1month", 8, 32)
                                                            .addRange("3months", 32, 91)
                                                            .addUnboundedFrom("remainingMonths", 91))
                                            .subAggregation(getCountWithGrouping("groupByFieldSla", "ifSLA", 2))))
                    .execute().actionGet();
            result.put("noLocality", localityMissingResponse);
        }

        result.put("consolidatedResponse", consolidatedResponse);
        result.put("tableResponse", tableResponse);

        return result;
    }

    @Override
    public String getWardName(final String wardNo) {
        final SearchResponse response = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                .setSize(1)
                .setQuery(QueryBuilders.matchQuery(WARD_NUMBER, wardNo))
                .execute().actionGet();

        for (final SearchHit hit : response.getHits()) {
            final Map<String, Object> fields = hit.getSource();
            return fields.get(WARD_NAME).toString();
        }

        return StringUtils.EMPTY;
    }

    @Override
    public SearchResponse findAllGrievanceByComplaintType(final ComplaintDashBoardRequest complaintDashBoardRequest,
            final BoolQueryBuilder query, final String grouByField) {

        return elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                .setQuery(query).setSize(0)
                .addAggregation(getCountWithGrouping("groupByField", grouByField, 120)
                        .subAggregation(getCountWithGrouping("closedComplaintCount", "ifClosed", 2))
                        .subAggregation(getCountWithGrouping(RE_OPENED_COMPLAINT_COUNT, RE_OPENED, 2)))
                .execute().actionGet();
    }

    @Override
    public SearchResponse findAllGrievanceBySource(final ComplaintDashBoardRequest complaintDashBoardRequest,
            final BoolQueryBuilder query, final String grouByField) {

        return elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                .setQuery(query).setSize(0)
                .addAggregation(getCountWithGrouping("groupByField", grouByField, 120)
                        .subAggregation(getCountWithGrouping("groupByFieldSource", "source", 30)))
                .addAggregation(getCountWithGrouping("complaintTypeWise", "complaintTypeName", 120)
                        .subAggregation(getCountWithGrouping("complaintTypeWiseSource", "source", 30)))
                .execute().actionGet();
    }

    @Override
    public String getFunctionryMobileNumber(final String functionaryName) {
        final SearchResponse response = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                .setSize(1)
                .setQuery(QueryBuilders.matchQuery("initialFunctionaryName", functionaryName))
                .execute().actionGet();

        for (final SearchHit hit : response.getHits()) {
            final Map<String, Object> fields = hit.getSource();
            return fields.get("initialFunctionaryMobileNumber").toString();
        }
        return StringUtils.EMPTY;
    }

    @Override
    public SearchResponse findByAllFunctionary(final ComplaintDashBoardRequest complaintDashBoardRequest,
            final BoolQueryBuilder query) {
        int size = 1000;
        if (complaintDashBoardRequest.getSize() >= 0)
            size = complaintDashBoardRequest.getSize();

        return elasticsearchTemplate
                .getClient()
                .prepareSearch(PGR_INDEX_NAME)
                .setQuery(query).setSize(0)
                .addAggregation(
                        AggregationBuilders
                                .terms("ulbwise")
                                .field(CITY_CODE)
                                .size(120)
                                .subAggregation(
                                        AggregationBuilders
                                                .terms("departmentwise")
                                                .field("departmentCode")
                                                .size(size)
                                                .subAggregation(
                                                        AggregationBuilders
                                                                .terms("functionarywise")
                                                                .field("initialFunctionaryName")
                                                                .size(size)
                                                                .subAggregation(
                                                                        AggregationBuilders.topHits("complaintrecord")
                                                                                .addField(CITY_NAME)
                                                                                .addField(CITY_CODE).addField(DISTRICT_NAME)
                                                                                .addField("departmentName")
                                                                                .addField("initialFunctionaryMobileNumber")
                                                                                .setSize(1))
                                                                .subAggregation(
                                                                        getCountWithGrouping("closedComplaintCount", "ifClosed",
                                                                                2))
                                                                .subAggregation(getCountWithGrouping(RE_OPENED_COMPLAINT_COUNT,
                                                                        RE_OPENED, 2)))))
                .execute().actionGet();

    }

    @Override
    public SearchResponse findByAllUlb(final ComplaintDashBoardRequest complaintDashBoardRequest, final BoolQueryBuilder query) {
        int size = 120;
        if (complaintDashBoardRequest.getSize() >= 0)
            size = complaintDashBoardRequest.getSize();

        return elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME).setSize(0).setQuery(query)
                .addAggregation(AggregationBuilders.terms("ulbwise").field(CITY_CODE).size(size)
                        .subAggregation(AggregationBuilders.topHits("complaintrecord").addField(CITY_CODE)
                                .addField(DISTRICT_NAME).addField(CITY_NAME).setSize(1))
                        .subAggregation(getCountWithGrouping("complaintCount", "ifClosed", 2))
                        .subAggregation(getCountWithGrouping(RE_OPENED_COMPLAINT_COUNT, RE_OPENED, 2)))
                .execute().actionGet();
    }

    @Override
    public SearchResponse findBYAllWards(final ComplaintDashBoardRequest complaintDashBoardRequest,
            final BoolQueryBuilder query) {
        int size = 1000;
        if (complaintDashBoardRequest.getSize() >= 0)
            size = complaintDashBoardRequest.getSize();

        return elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME).setSize(0).setQuery(query)
                .addAggregation(AggregationBuilders.terms("ulbwise").field(CITY_CODE).size(120)
                        .subAggregation(AggregationBuilders.terms("wardwise").field(WARD_NUMBER).size(size)
                                .subAggregation(AggregationBuilders.topHits("complaintrecord").addField(CITY_CODE)
                                        .addField(DISTRICT_NAME).addField(CITY_NAME).addField(WARD_NAME).setSize(1))
                                .subAggregation(getCountWithGrouping("complaintCount", "ifClosed", 2))
                                .subAggregation(getCountWithGrouping(RE_OPENED_COMPLAINT_COUNT, RE_OPENED, 2))))
                .execute().actionGet();
    }

    @Override
    public SearchResponse findBYAllLocalities(final ComplaintDashBoardRequest complaintDashBoardRequest,
            final BoolQueryBuilder query) {
        int size = 1000;
        if (complaintDashBoardRequest.getSize() >= 0)
            size = complaintDashBoardRequest.getSize();

        return elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME).setSize(0).setQuery(query)
                .addAggregation(AggregationBuilders.terms("ulbwise").field(CITY_CODE).size(120)
                        .subAggregation(AggregationBuilders.terms("wardwise").field(WARD_NUMBER).size(size)
                                .subAggregation(AggregationBuilders.terms("localitywise").field(LOCALITY_NAME).size(size)
                                        .subAggregation(AggregationBuilders.topHits("complaintrecord").addField(CITY_CODE)
                                                .addField("cityDistrictName").addField(CITY_NAME)
                                                .addField(WARD_NAME).addField(LOCALITY_NAME).setSize(1))
                                        .subAggregation(getCountWithGrouping("complaintCount", "ifClosed", 2)))
                                .subAggregation(getCountWithGrouping(RE_OPENED_COMPLAINT_COUNT, RE_OPENED, 2))))
                .addAggregation(AggregationBuilders.missing("nolocality").field(LOCALITY_NAME)
                        .subAggregation(getCountWithGrouping("noLocalityComplaintCount", "ifClosed", 2)))
                .execute().actionGet();
    }

    @Override
    public List<ComplaintIndex> findAllComplaintsBySource(final String fieldName, final String paramValue) {
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery(fieldName, paramValue))
                .withPageable(new PageRequest(0, 5000))
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, ComplaintIndex.class);
    }

    @Override
    public List<ComplaintIndex> findAllComplaintsByField(final ComplaintDashBoardRequest complaintDashBoardRequest,
            final BoolQueryBuilder query) {
        final SortOrder sortOrder = complaintDashBoardRequest.getSortDirection().equals("ASC") ? SortOrder.ASC : SortOrder.DESC;
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query)
                .withSort(new FieldSortBuilder(complaintDashBoardRequest.getSortField()).order(sortOrder))
                .withPageable(new PageRequest(0, complaintDashBoardRequest.getSize()))
                .build();
        return elasticsearchTemplate.queryForList(searchQuery, ComplaintIndex.class);
    }
}