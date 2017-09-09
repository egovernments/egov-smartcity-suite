/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.elasticsearch.repository;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.elasticsearch.entity.ComplaintIndex;
import org.egov.pgr.elasticsearch.entity.contract.ComplaintDashBoardRequest;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.pgr.elasticsearch.repository.ComplaintIndexAggregationBuilder.getAverageWithExclusion;
import static org.egov.pgr.elasticsearch.repository.ComplaintIndexAggregationBuilder.getAverageWithFilter;
import static org.egov.pgr.elasticsearch.repository.ComplaintIndexAggregationBuilder.getCount;
import static org.egov.pgr.elasticsearch.repository.ComplaintIndexAggregationBuilder.getCountBetweenSpecifiedDates;
import static org.egov.pgr.elasticsearch.repository.ComplaintIndexAggregationBuilder.getCountWithGrouping;
import static org.egov.pgr.elasticsearch.repository.ComplaintIndexAggregationBuilder.getCountWithGroupingAndOrder;
import static org.egov.pgr.utils.constants.PGRConstants.CITY_CODE;
import static org.egov.pgr.utils.constants.PGRConstants.CITY_NAME;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ALL_FUNCTIONARY;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ALL_LOCALITIES;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ALL_WARDS;
import static org.egov.pgr.utils.constants.PGRConstants.DISTRICT_NAME;
import static org.egov.pgr.utils.constants.PGRConstants.PGR_INDEX_DATE_FORMAT;
import static org.egov.pgr.utils.constants.PGRConstants.PGR_INDEX_NAME;
import static org.egov.pgr.utils.constants.PGRConstants.WARD_NAME;
import static org.egov.pgr.utils.constants.PGRConstants.WARD_NUMBER;

public class ComplaintIndexRepositoryImpl implements ComplaintIndexCustomRepository {

    public static final String INITIAL_FUNCTIONARY_NAME = "initialFunctionaryName";
    private static final String REMAINING_HOURS = "remainingHours";
    private static final String GROUP_BY_FIELD_AGEING_FOR_HOURS = "groupByFieldAgeingForHours";
    private static final String COMPLAINT_AGEING_FROM_DUE = "complaintAgeingFromDue";
    private static final String HOURWISE_COMPLAINT_TYPE_AGEING = "hourwiseComplaintTypeAgeing";
    private static final String INITIAL_FUNCTIONARY_MOBILE_NUMBER = "initialFunctionaryMobileNumber";
    private static final String DEPARTMENT_CODE = "departmentCode";
    private static final String DEPARTMENTWISE = "departmentwise";
    private static final String CITY_DISTRICT_NAME = "cityDistrictName";
    private static final String WARDWISE = "wardwise";
    private static final String GROUP_BY_FIELD = "groupByField";
    private static final String ULBWISE = "ulbwise";
    private static final String IF_SLA = "ifSLA";
    private static final String IF_CLOSED = "ifClosed";
    private static final String LOCALITY_NAME = "localityName";
    private static final String RE_OPENED = "reOpened";
    private static final String RE_OPENED_COMPLAINT_COUNT = "reOpenedComplaintCount";
    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern(PGR_INDEX_DATE_FORMAT);
    private static final String GROUP_BY_FUNCTIONARY = "groupByInitialFunctionary";
    private static final String AGGR_CLOSEDCOUNT = "closedcount";
    private static final String COMPLAINT_RECORD = "complaintrecord";
    private static final String DEPARTMENT_NAME = "departmentName";
    private static final String REMAINING_MONTHS = "remainingMonths";
    private static final String COMPLAINT_COUNT = "complaintCount";
    private static final String COMPLAINT_TYPEWISE_SOURCE = "complaintTypeWiseSource";
    private static final String SOURCE = "source";
    private static final String GROUP_BY_FIELD_SOURCE = "groupByFieldSource";
    private static final String CLOSED_COMPLAINT_COUNT = "closedComplaintCount";
    private static final String NOLOCALITY = "nolocality";
    private static final String GROUP_BY_FIELD_SLA = "groupByFieldSla";
    private static final String GROUP_BY_FIELD_AGEING = "groupByFieldAgeing";
    private static final String GROUP_FIELD_WISE_OPEN_AND_CLOSED_COUNT = "groupFieldWiseOpenAndClosedCount";
    private static final String GROUP_BY_FIELD_SATISFACTION_AVERAGE = "groupByFieldSatisfactionAverage";
    private static final String COMPLAINT_TYPE_SLA = "complaintTypeSla";
    private static final String TWELVE_HOURS = "12hours";
    private static final String THREE_MONTHS = "3months";
    private static final String ONE_MONTH = "1month";
    private static final String ONE_WEEK = "1week";
    private static final String COMPLAINT_TYPE_AGEING = "ComplaintTypeAgeing";
    private static final String COMPLAINT_TYPE_WISE_OPEN_AND_CLOSED_COUNT = "complaintTypeWiseOpenAndClosedCount";
    private static final String COMPLAINT_TYPE_SATISFACTION_AVERAGE = "complaintTypeSatisfactionAverage";
    private static final String COMPLAINT_TYPE_NAME = "complaintTypeName";
    private static final String COMPLAINT_TYPE_WISE = "complaintTypeWise";
    private static final String SATISFACTION_INDEX = "satisfactionIndex";
    private static final String SATISFACTION_AVERAGE = "satisfactionAverage";
    private static final String COMPLAINT_AGEINGDAYS_FROM_DUE = "complaintAgeingdaysFromDue";
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
                .addAggregation(getCountWithGrouping("closedCount", IF_CLOSED, 2))
                .addAggregation(getCountWithGrouping("slaCount", IF_SLA, 2))
                .addAggregation(getAverageWithFilter(IF_CLOSED, 1, "AgeingInWeeks", COMPLAINT_AGEINGDAYS_FROM_DUE))
                .addAggregation(getAverageWithExclusion(SATISFACTION_AVERAGE, SATISFACTION_INDEX))
                .addAggregation(getCountBetweenSpecifiedDates("currentYear", "createdDate",
                        currentYearFromDate.toString(formatter),
                        new DateTime().toString(formatter)))
                .addAggregation(getCountBetweenSpecifiedDates("todaysComplaintCount", "createdDate",
                        fromDate.toString(formatter), toDate.toString(formatter)))
                .execute().actionGet();

        SearchResponse tableResponse;
        if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_WARDS))
            tableResponse = elasticsearchTemplate
                    .getClient()
                    .prepareSearch(PGR_INDEX_NAME)
                    .setQuery(query)
                    .setSize(0)
                    .addAggregation(
                            getCountWithGroupingAndOrder(COMPLAINT_TYPE_WISE, COMPLAINT_TYPE_NAME, size,
                                    complaintDashBoardRequest.getSortField(), complaintDashBoardRequest.getSortDirection())
                                    .subAggregation(
                                            getAverageWithExclusion(COMPLAINT_TYPE_SATISFACTION_AVERAGE,
                                                    SATISFACTION_INDEX))
                                    .subAggregation(
                                            getCountWithGrouping(COMPLAINT_TYPE_WISE_OPEN_AND_CLOSED_COUNT, IF_CLOSED, 2)
                                                    .subAggregation(
                                                            AggregationBuilders.range(COMPLAINT_TYPE_AGEING)
                                                                    .field(COMPLAINT_AGEINGDAYS_FROM_DUE)
                                                                    .addRange(ONE_WEEK, 0, 8).addRange(ONE_MONTH, 8, 32)
                                                                    .addRange(THREE_MONTHS, 32, 91)
                                                                    .addUnboundedFrom(REMAINING_MONTHS, 91))
                                                    .subAggregation(
                                                            AggregationBuilders.range(HOURWISE_COMPLAINT_TYPE_AGEING)
                                                                    .field(COMPLAINT_AGEING_FROM_DUE)
                                                                    .addRange(TWELVE_HOURS, 0, 13)
                                                                    .addRange("1day", 13, 25)
                                                                    .addRange(ONE_WEEK, 25, 169)
                                                                    .addUnboundedFrom(REMAINING_HOURS, 169))
                                                    .subAggregation(
                                                            getCountWithGrouping(COMPLAINT_TYPE_SLA, IF_SLA, 2))))
                    .addAggregation(AggregationBuilders.terms(ULBWISE).field(CITY_CODE).size(120)
                            .subAggregation(
                                    getCountWithGroupingAndOrder(GROUP_BY_FIELD, grouByField, size,
                                            complaintDashBoardRequest.getSortField(),
                                            complaintDashBoardRequest.getSortDirection())
                                            .subAggregation(
                                                    getAverageWithExclusion(GROUP_BY_FIELD_SATISFACTION_AVERAGE,
                                                            SATISFACTION_INDEX))
                                            .subAggregation(
                                                    AggregationBuilders.topHits(COMPLAINT_RECORD).addField(CITY_CODE)
                                                            .addField(DISTRICT_NAME).addField(CITY_NAME)
                                                            .addField(WARD_NAME).setSize(1))
                                            .subAggregation(
                                                    getCountWithGrouping(GROUP_FIELD_WISE_OPEN_AND_CLOSED_COUNT, IF_CLOSED,
                                                            2)
                                                            .subAggregation(
                                                                    AggregationBuilders
                                                                            .range(GROUP_BY_FIELD_AGEING)
                                                                            .field(COMPLAINT_AGEINGDAYS_FROM_DUE)
                                                                            .addRange(ONE_WEEK, 0, 8)
                                                                            .addRange(ONE_MONTH, 8, 32)
                                                                            .addRange(THREE_MONTHS, 32, 91)
                                                                            .addUnboundedFrom(REMAINING_MONTHS,
                                                                                    91))
                                                            .subAggregation(
                                                                    AggregationBuilders.range(GROUP_BY_FIELD_AGEING_FOR_HOURS)
                                                                            .field(COMPLAINT_AGEING_FROM_DUE)
                                                                            .addRange(TWELVE_HOURS, 0, 13)
                                                                            .addRange("1day", 13, 25)
                                                                            .addRange(ONE_WEEK, 25, 169)
                                                                            .addUnboundedFrom(REMAINING_HOURS, 169))
                                                            .subAggregation(getCountWithGrouping(
                                                                    GROUP_BY_FIELD_SLA, IF_SLA, 2)))))

                    .execute().actionGet();
        else if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_LOCALITIES))
            tableResponse = elasticsearchTemplate
                    .getClient()
                    .prepareSearch(PGR_INDEX_NAME)
                    .setQuery(query)
                    .setSize(0)
                    .addAggregation(
                            getCountWithGroupingAndOrder(COMPLAINT_TYPE_WISE, COMPLAINT_TYPE_NAME, size,
                                    complaintDashBoardRequest.getSortField(), complaintDashBoardRequest.getSortDirection())
                                    .subAggregation(
                                            getAverageWithExclusion(COMPLAINT_TYPE_SATISFACTION_AVERAGE,
                                                    SATISFACTION_INDEX))
                                    .subAggregation(
                                            getCountWithGrouping(COMPLAINT_TYPE_WISE_OPEN_AND_CLOSED_COUNT, IF_CLOSED, 2)
                                                    .subAggregation(
                                                            AggregationBuilders.range(COMPLAINT_TYPE_AGEING)
                                                                    .field(COMPLAINT_AGEINGDAYS_FROM_DUE)
                                                                    .addRange(ONE_WEEK, 0, 8).addRange(ONE_MONTH, 8, 32)
                                                                    .addRange(THREE_MONTHS, 32, 91)
                                                                    .addUnboundedFrom(REMAINING_MONTHS, 91))
                                                    .subAggregation(
                                                            AggregationBuilders.range(HOURWISE_COMPLAINT_TYPE_AGEING)
                                                                    .field(COMPLAINT_AGEING_FROM_DUE)
                                                                    .addRange(TWELVE_HOURS, 0, 13)
                                                                    .addRange("1day", 13, 25)
                                                                    .addRange(ONE_WEEK, 25, 169)
                                                                    .addUnboundedFrom(REMAINING_HOURS, 169))
                                                    .subAggregation(
                                                            getCountWithGrouping(COMPLAINT_TYPE_SLA, IF_SLA, 2))))
                    .addAggregation(AggregationBuilders.terms(ULBWISE).field(CITY_CODE).size(120)
                            .subAggregation(AggregationBuilders.terms(WARDWISE).field(WARD_NUMBER).size(size)
                                    .subAggregation(
                                            getCountWithGroupingAndOrder(GROUP_BY_FIELD, grouByField, size,
                                                    complaintDashBoardRequest.getSortField(),
                                                    complaintDashBoardRequest.getSortDirection())
                                                    .subAggregation(
                                                            getAverageWithExclusion(GROUP_BY_FIELD_SATISFACTION_AVERAGE,
                                                                    SATISFACTION_INDEX))
                                                    .subAggregation(AggregationBuilders.topHits(COMPLAINT_RECORD)
                                                            .addField(CITY_CODE)
                                                            .addField(CITY_DISTRICT_NAME).addField(CITY_NAME)
                                                            .addField(WARD_NAME).addField(LOCALITY_NAME).setSize(1))
                                                    .subAggregation(
                                                            getCountWithGrouping(GROUP_FIELD_WISE_OPEN_AND_CLOSED_COUNT,
                                                                    IF_CLOSED, 2)
                                                                    .subAggregation(
                                                                            AggregationBuilders
                                                                                    .range(GROUP_BY_FIELD_AGEING)
                                                                                    .field(COMPLAINT_AGEINGDAYS_FROM_DUE)
                                                                                    .addRange(ONE_WEEK, 0, 8)
                                                                                    .addRange(ONE_MONTH, 8, 32)
                                                                                    .addRange(THREE_MONTHS, 32, 91)
                                                                                    .addUnboundedFrom(
                                                                                            REMAINING_MONTHS,
                                                                                            91))
                                                                    .subAggregation(
                                                                            AggregationBuilders.range(GROUP_BY_FIELD_AGEING_FOR_HOURS)
                                                                                    .field(COMPLAINT_AGEING_FROM_DUE)
                                                                                    .addRange(TWELVE_HOURS, 0, 13)
                                                                                    .addRange("1day", 13, 25)
                                                                                    .addRange(ONE_WEEK, 25, 169)
                                                                                    .addUnboundedFrom(REMAINING_HOURS, 169))
                                                                    .subAggregation(getCountWithGrouping(
                                                                            GROUP_BY_FIELD_SLA, IF_SLA, 2))))))
                    .execute().actionGet();
        else if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_FUNCTIONARY))
            tableResponse = elasticsearchTemplate
                    .getClient()
                    .prepareSearch(PGR_INDEX_NAME)
                    .setQuery(query)
                    .setSize(0)
                    .addAggregation(
                            getCountWithGroupingAndOrder(COMPLAINT_TYPE_WISE, COMPLAINT_TYPE_NAME, size,
                                    complaintDashBoardRequest.getSortField(), complaintDashBoardRequest.getSortDirection())
                                    .subAggregation(
                                            getAverageWithExclusion(COMPLAINT_TYPE_SATISFACTION_AVERAGE,
                                                    SATISFACTION_INDEX))
                                    .subAggregation(
                                            getCountWithGrouping(COMPLAINT_TYPE_WISE_OPEN_AND_CLOSED_COUNT, IF_CLOSED, 2)
                                                    .subAggregation(
                                                            AggregationBuilders.range(COMPLAINT_TYPE_AGEING)
                                                                    .field(COMPLAINT_AGEINGDAYS_FROM_DUE)
                                                                    .addRange(ONE_WEEK, 0, 8).addRange(ONE_MONTH, 8, 32)
                                                                    .addRange(THREE_MONTHS, 32, 91)
                                                                    .addUnboundedFrom(REMAINING_MONTHS, 91))
                                                    .subAggregation(
                                                            AggregationBuilders.range(HOURWISE_COMPLAINT_TYPE_AGEING)
                                                                    .field(COMPLAINT_AGEING_FROM_DUE)
                                                                    .addRange(TWELVE_HOURS, 0, 13)
                                                                    .addRange("1day", 13, 25)
                                                                    .addRange(ONE_WEEK, 25, 169)
                                                                    .addUnboundedFrom(REMAINING_HOURS, 169))
                                                    .subAggregation(
                                                            getCountWithGrouping(COMPLAINT_TYPE_SLA, IF_SLA, 2))))
                    .addAggregation(AggregationBuilders.terms(ULBWISE).field(CITY_CODE).size(120)
                            .subAggregation(AggregationBuilders.terms(DEPARTMENTWISE).field(DEPARTMENT_CODE).size(size)
                                    .subAggregation(
                                            getCountWithGroupingAndOrder(GROUP_BY_FIELD, grouByField, size,
                                                    complaintDashBoardRequest.getSortField(),
                                                    complaintDashBoardRequest.getSortDirection())
                                                    .subAggregation(
                                                            getAverageWithExclusion(GROUP_BY_FIELD_SATISFACTION_AVERAGE,
                                                                    SATISFACTION_INDEX))
                                                    .subAggregation(AggregationBuilders.topHits(COMPLAINT_RECORD)
                                                            .addField(CITY_CODE)
                                                            .addField(CITY_DISTRICT_NAME).addField(CITY_NAME)
                                                            .addField(DEPARTMENT_NAME)
                                                            .addField(INITIAL_FUNCTIONARY_MOBILE_NUMBER).setSize(1))
                                                    .subAggregation(
                                                            getCountWithGrouping(GROUP_FIELD_WISE_OPEN_AND_CLOSED_COUNT,
                                                                    IF_CLOSED, 2)
                                                                    .subAggregation(
                                                                            AggregationBuilders
                                                                                    .range(GROUP_BY_FIELD_AGEING)
                                                                                    .field(COMPLAINT_AGEINGDAYS_FROM_DUE)
                                                                                    .addRange(ONE_WEEK, 0, 8)
                                                                                    .addRange(ONE_MONTH, 8, 32)
                                                                                    .addRange(THREE_MONTHS, 32, 91)
                                                                                    .addUnboundedFrom(
                                                                                            REMAINING_MONTHS,
                                                                                            91))
                                                                    .subAggregation(
                                                                            AggregationBuilders.range(GROUP_BY_FIELD_AGEING_FOR_HOURS)
                                                                                    .field(COMPLAINT_AGEING_FROM_DUE)
                                                                                    .addRange(TWELVE_HOURS, 0, 13)
                                                                                    .addRange("1day", 13, 25)
                                                                                    .addRange(ONE_WEEK, 25, 169)
                                                                                    .addUnboundedFrom(REMAINING_HOURS, 169))
                                                                    .subAggregation(getCountWithGrouping(
                                                                            GROUP_BY_FIELD_SLA, IF_SLA, 2))))))
                    .execute().actionGet();
        else
            tableResponse = elasticsearchTemplate
                    .getClient()
                    .prepareSearch(PGR_INDEX_NAME)
                    .setQuery(query)
                    .setSize(0)
                    .addAggregation(
                            getCountWithGroupingAndOrder(COMPLAINT_TYPE_WISE, COMPLAINT_TYPE_NAME, size,
                                    complaintDashBoardRequest.getSortField(), complaintDashBoardRequest.getSortDirection())
                                    .subAggregation(
                                            getAverageWithExclusion(COMPLAINT_TYPE_SATISFACTION_AVERAGE,
                                                    SATISFACTION_INDEX))
                                    .subAggregation(
                                            getCountWithGrouping(COMPLAINT_TYPE_WISE_OPEN_AND_CLOSED_COUNT, IF_CLOSED, 2)
                                                    .subAggregation(
                                                            AggregationBuilders.range(COMPLAINT_TYPE_AGEING)
                                                                    .field(COMPLAINT_AGEINGDAYS_FROM_DUE)
                                                                    .addRange(ONE_WEEK, 0, 8).addRange(ONE_MONTH, 8, 32)
                                                                    .addRange(THREE_MONTHS, 32, 91)
                                                                    .addUnboundedFrom(REMAINING_MONTHS, 91))
                                                    .subAggregation(
                                                            AggregationBuilders.range(HOURWISE_COMPLAINT_TYPE_AGEING)
                                                                    .field(COMPLAINT_AGEING_FROM_DUE)
                                                                    .addRange(TWELVE_HOURS, 0, 13)
                                                                    .addRange("1day", 13, 25)
                                                                    .addRange(ONE_WEEK, 25, 169)
                                                                    .addUnboundedFrom(REMAINING_HOURS, 169))
                                                    .subAggregation(
                                                            getCountWithGrouping(COMPLAINT_TYPE_SLA, IF_SLA, 2))))
                    .addAggregation(
                            getCountWithGroupingAndOrder(GROUP_BY_FIELD, grouByField, size,
                                    complaintDashBoardRequest.getSortField(), complaintDashBoardRequest.getSortDirection())
                                    .subAggregation(
                                            getAverageWithExclusion(GROUP_BY_FIELD_SATISFACTION_AVERAGE,
                                                    SATISFACTION_INDEX))
                                    .subAggregation(
                                            getCountWithGrouping(GROUP_FIELD_WISE_OPEN_AND_CLOSED_COUNT, IF_CLOSED, 2)
                                                    .subAggregation(
                                                            AggregationBuilders.range(GROUP_BY_FIELD_AGEING)
                                                                    .field(COMPLAINT_AGEINGDAYS_FROM_DUE)
                                                                    .addRange(ONE_WEEK, 0, 8).addRange(ONE_MONTH, 8, 32)
                                                                    .addRange(THREE_MONTHS, 32, 91)
                                                                    .addUnboundedFrom(REMAINING_MONTHS, 91))
                                                    .subAggregation(
                                                            AggregationBuilders.range(GROUP_BY_FIELD_AGEING_FOR_HOURS)
                                                                    .field(COMPLAINT_AGEING_FROM_DUE)
                                                                    .addRange(TWELVE_HOURS, 0, 13)
                                                                    .addRange("1day", 13, 25)
                                                                    .addRange(ONE_WEEK, 25, 169)
                                                                    .addUnboundedFrom(REMAINING_HOURS, 169))
                                                    .subAggregation(getCountWithGrouping(GROUP_BY_FIELD_SLA, IF_SLA, 2))))
                    .execute().actionGet();

        final HashMap<String, SearchResponse> result = new HashMap<>();
        if (grouByField.equals(LOCALITY_NAME)) {
            final SearchResponse localityMissingResponse = elasticsearchTemplate
                    .getClient()
                    .prepareSearch(PGR_INDEX_NAME)
                    .setQuery(query)
                    .setSize(0)
                    .addAggregation(AggregationBuilders.missing(NOLOCALITY).field(LOCALITY_NAME)
                            .subAggregation(
                                    getAverageWithExclusion(GROUP_BY_FIELD_SATISFACTION_AVERAGE, SATISFACTION_INDEX))
                            .subAggregation(
                                    getCountWithGrouping(GROUP_FIELD_WISE_OPEN_AND_CLOSED_COUNT, IF_CLOSED, 2)
                                            .subAggregation(
                                                    AggregationBuilders.range(GROUP_BY_FIELD_AGEING)
                                                            .field(COMPLAINT_AGEINGDAYS_FROM_DUE)
                                                            .addRange(ONE_WEEK, 0, 8).addRange(ONE_MONTH, 8, 32)
                                                            .addRange(THREE_MONTHS, 32, 91)
                                                            .addUnboundedFrom(REMAINING_MONTHS, 91))
                                            .subAggregation(
                                                    AggregationBuilders.range(GROUP_BY_FIELD_AGEING_FOR_HOURS)
                                                            .field(COMPLAINT_AGEING_FROM_DUE)
                                                            .addRange(TWELVE_HOURS, 0, 13)
                                                            .addRange("1day", 13, 25)
                                                            .addRange(ONE_WEEK, 25, 169)
                                                            .addUnboundedFrom(REMAINING_HOURS, 169))
                                            .subAggregation(getCountWithGrouping(GROUP_BY_FIELD_SLA, IF_SLA, 2))))
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
        Iterator<SearchHit> searchHits = response.getHits().iterator();
        return searchHits.hasNext() ? searchHits.next().getSource().get(WARD_NAME).toString() : StringUtils.EMPTY;
    }

    @Override
    public Map<String, SearchResponse> findAllGrievanceByComplaintType(final ComplaintDashBoardRequest complaintDashBoardRequest,
                                                                       final BoolQueryBuilder query, final String grouByField) {

        Map<String, SearchResponse> response = new HashMap<>();
        SearchResponse tableResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                .setQuery(query).setSize(0)
                .addAggregation(getCountWithGrouping(GROUP_BY_FIELD, grouByField, 120)
                        .subAggregation(getCountWithGrouping(CLOSED_COMPLAINT_COUNT, IF_CLOSED, 2))
                        .subAggregation(getCountWithGrouping(RE_OPENED_COMPLAINT_COUNT, RE_OPENED, 2)))
                .execute().actionGet();
        response.put("tableResponse", tableResponse);

        //This is in case of drill down to show other localities information
        if (grouByField.equals(LOCALITY_NAME)) {
            SearchResponse otherLocalitiesResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                    .setQuery(query).setSize(0)
                    .addAggregation(AggregationBuilders.missing(NOLOCALITY).field(LOCALITY_NAME)
                            .subAggregation(getCountWithGrouping(CLOSED_COMPLAINT_COUNT, IF_CLOSED, 2))
                            .subAggregation(getCountWithGrouping(RE_OPENED_COMPLAINT_COUNT, RE_OPENED, 2)))
                    .execute().actionGet();
            response.put("otherLocalities", otherLocalitiesResponse);
        }
        return response;
    }

    @Override
    public SearchResponse findAllGrievanceBySource(final ComplaintDashBoardRequest complaintDashBoardRequest,
                                                   final BoolQueryBuilder query, final String grouByField) {

        SearchResponse tableResponse;
        if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_WARDS))
            tableResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                    .setQuery(query).setSize(0)
                    .addAggregation(AggregationBuilders.terms(ULBWISE).field(CITY_CODE).size(120)
                            .subAggregation(getCountWithGrouping(GROUP_BY_FIELD, grouByField, 120)
                                    .subAggregation(getCountWithGrouping(GROUP_BY_FIELD_SOURCE, SOURCE, 30))
                                    .subAggregation(AggregationBuilders.topHits(COMPLAINT_RECORD)
                                            .addField(WARD_NAME)
                                            .addField(CITY_NAME)
                                            .addField(DISTRICT_NAME)
                                            .addField(INITIAL_FUNCTIONARY_NAME)
                                            .addField(INITIAL_FUNCTIONARY_MOBILE_NUMBER)
                                            .setSize(1))))
                    .addAggregation(getCountWithGrouping(COMPLAINT_TYPE_WISE, COMPLAINT_TYPE_NAME, 120)
                            .subAggregation(getCountWithGrouping(COMPLAINT_TYPEWISE_SOURCE, SOURCE, 30)))
                    .execute().actionGet();
        else if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_LOCALITIES))
            tableResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                    .setQuery(query).setSize(0)
                    .addAggregation(AggregationBuilders.terms(ULBWISE).field(CITY_CODE).size(120)
                            .subAggregation(AggregationBuilders.terms(WARDWISE).field(WARD_NUMBER).size(120)
                                    .subAggregation(getCountWithGrouping(GROUP_BY_FIELD, grouByField, 120)
                                            .subAggregation(getCountWithGrouping(GROUP_BY_FIELD_SOURCE, SOURCE, 30))
                                            .subAggregation(AggregationBuilders.topHits(COMPLAINT_RECORD)
                                                    .addField(WARD_NAME)
                                                    .addField(CITY_NAME)
                                                    .addField(DISTRICT_NAME)
                                                    .addField(INITIAL_FUNCTIONARY_NAME)
                                                    .addField(INITIAL_FUNCTIONARY_MOBILE_NUMBER)
                                                    .addField(DEPARTMENT_NAME)
                                                    .setSize(1)))))
                    .addAggregation(getCountWithGrouping(COMPLAINT_TYPE_WISE, COMPLAINT_TYPE_NAME, 120)
                            .subAggregation(getCountWithGrouping(COMPLAINT_TYPEWISE_SOURCE, SOURCE, 30)))
                    .execute().actionGet();
        else if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_FUNCTIONARY))
            tableResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                    .setQuery(query).setSize(0)
                    .addAggregation(AggregationBuilders.terms(ULBWISE).field(CITY_CODE).size(120)
                            .subAggregation(AggregationBuilders.terms(DEPARTMENTWISE).field(DEPARTMENT_CODE).size(120)
                                    .subAggregation(getCountWithGrouping(GROUP_BY_FIELD, grouByField, 120)
                                            .subAggregation(getCountWithGrouping(GROUP_BY_FIELD_SOURCE, SOURCE, 30))
                                            .subAggregation(AggregationBuilders.topHits(COMPLAINT_RECORD)
                                                    .addField(WARD_NAME)
                                                    .addField(CITY_NAME)
                                                    .addField(DISTRICT_NAME)
                                                    .addField(INITIAL_FUNCTIONARY_NAME)
                                                    .addField(INITIAL_FUNCTIONARY_MOBILE_NUMBER)
                                                    .addField(DEPARTMENT_NAME)
                                                    .setSize(1)))))
                    .addAggregation(getCountWithGrouping(COMPLAINT_TYPE_WISE, COMPLAINT_TYPE_NAME, 120)
                            .subAggregation(getCountWithGrouping(COMPLAINT_TYPEWISE_SOURCE, SOURCE, 30)))
                    .execute().actionGet();
        else
            tableResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                    .setQuery(query).setSize(0)
                    .addAggregation(getCountWithGrouping(GROUP_BY_FIELD, grouByField, 120)
                            .subAggregation(getCountWithGrouping(GROUP_BY_FIELD_SOURCE, SOURCE, 30))
                            .subAggregation(AggregationBuilders.topHits(COMPLAINT_RECORD)
                                    .addField(WARD_NAME)
                                    .addField(CITY_NAME)
                                    .addField(DISTRICT_NAME)
                                    .addField(INITIAL_FUNCTIONARY_NAME)
                                    .addField(INITIAL_FUNCTIONARY_MOBILE_NUMBER)
                                    .setSize(1)))
                    .addAggregation(getCountWithGrouping(COMPLAINT_TYPE_WISE, COMPLAINT_TYPE_NAME, 120)
                            .subAggregation(getCountWithGrouping(COMPLAINT_TYPEWISE_SOURCE, SOURCE, 30)))
                    .execute().actionGet();
        return tableResponse;

    }

    @Override
    public String getFunctionryMobileNumber(final String functionaryName) {
        final SearchResponse response = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                .setSize(1)
                .setQuery(QueryBuilders.matchQuery(INITIAL_FUNCTIONARY_NAME, functionaryName))
                .execute().actionGet();

        Iterator<SearchHit> searchHits = response.getHits().iterator();
        return searchHits.hasNext() ? searchHits.next().getSource().get(INITIAL_FUNCTIONARY_MOBILE_NUMBER).toString()
                : StringUtils.EMPTY;
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
                                .terms(ULBWISE)
                                .field(CITY_CODE)
                                .size(120)
                                .subAggregation(
                                        AggregationBuilders
                                                .terms(DEPARTMENTWISE)
                                                .field(DEPARTMENT_CODE)
                                                .size(size)
                                                .subAggregation(
                                                        AggregationBuilders
                                                                .terms("functionarywise")
                                                                .field(INITIAL_FUNCTIONARY_NAME)
                                                                .size(size)
                                                                .subAggregation(
                                                                        AggregationBuilders.topHits(COMPLAINT_RECORD)
                                                                                .addField(CITY_NAME)
                                                                                .addField(CITY_CODE).addField(DISTRICT_NAME)
                                                                                .addField(DEPARTMENT_NAME)
                                                                                .addField(INITIAL_FUNCTIONARY_MOBILE_NUMBER)
                                                                                .setSize(1))
                                                                .subAggregation(
                                                                        getCountWithGrouping(CLOSED_COMPLAINT_COUNT, IF_CLOSED,
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
                .addAggregation(AggregationBuilders.terms(ULBWISE).field(CITY_CODE).size(size)
                        .subAggregation(AggregationBuilders.topHits(COMPLAINT_RECORD).addField(CITY_CODE)
                                .addField(DISTRICT_NAME).addField(CITY_NAME).setSize(1))
                        .subAggregation(
                                getAverageWithExclusion(GROUP_BY_FIELD_SATISFACTION_AVERAGE, SATISFACTION_INDEX))
                        .subAggregation(getCountWithGrouping(COMPLAINT_COUNT, IF_CLOSED, 2))
                        .subAggregation(
                                getCountWithGrouping(GROUP_FIELD_WISE_OPEN_AND_CLOSED_COUNT, IF_CLOSED, 2)
                                        .subAggregation(
                                                AggregationBuilders.range(GROUP_BY_FIELD_AGEING)
                                                        .field(COMPLAINT_AGEINGDAYS_FROM_DUE)
                                                        .addRange(ONE_WEEK, 0, 8).addRange(ONE_MONTH, 8, 32)
                                                        .addRange(THREE_MONTHS, 32, 91)
                                                        .addUnboundedFrom(REMAINING_MONTHS, 91))
                                        .subAggregation(
                                                AggregationBuilders.range(GROUP_BY_FIELD_AGEING_FOR_HOURS)
                                                        .field(COMPLAINT_AGEING_FROM_DUE)
                                                        .addRange(TWELVE_HOURS, 0, 13)
                                                        .addRange("1day", 13, 25)
                                                        .addRange(ONE_WEEK, 25, 169)
                                                        .addUnboundedFrom(REMAINING_HOURS, 169))
                                        .subAggregation(getCountWithGrouping(GROUP_BY_FIELD_SLA, IF_SLA, 2)))
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
                .addAggregation(AggregationBuilders.terms(ULBWISE).field(CITY_CODE).size(120)
                        .subAggregation(AggregationBuilders.terms(WARDWISE).field(WARD_NUMBER).size(size)
                                .subAggregation(AggregationBuilders.topHits(COMPLAINT_RECORD).addField(CITY_CODE)
                                        .addField(DISTRICT_NAME).addField(CITY_NAME).addField(WARD_NAME).setSize(1))
                                .subAggregation(getCountWithGrouping(COMPLAINT_COUNT, IF_CLOSED, 2))
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
                .addAggregation(AggregationBuilders.terms(ULBWISE).field(CITY_CODE).size(120)
                        .subAggregation(AggregationBuilders.terms(WARDWISE).field(WARD_NUMBER).size(size)
                                .subAggregation(AggregationBuilders.terms("localitywise").field(LOCALITY_NAME).size(size)
                                        .subAggregation(AggregationBuilders.topHits(COMPLAINT_RECORD).addField(CITY_CODE)
                                                .addField(CITY_DISTRICT_NAME).addField(CITY_NAME)
                                                .addField(WARD_NAME).addField(LOCALITY_NAME).setSize(1))
                                        .subAggregation(getCountWithGrouping(COMPLAINT_COUNT, IF_CLOSED, 2)))
                                .subAggregation(getCountWithGrouping(RE_OPENED_COMPLAINT_COUNT, RE_OPENED, 2))))
                .addAggregation(AggregationBuilders.missing(NOLOCALITY).field(LOCALITY_NAME)
                        .subAggregation(getCountWithGrouping("noLocalityComplaintCount", IF_CLOSED, 2)))
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

    @Override
    public SearchResponse findRatingByGroupByField(final ComplaintDashBoardRequest complaintDashBoardRequest,
                                                   final BoolQueryBuilder query, final String grouByField) {

        SearchResponse tableResponse;
        if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_WARDS))
            tableResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                    .setQuery(query).setSize(0)
                    .addAggregation(AggregationBuilders.terms(ULBWISE).field(CITY_CODE).size(120)
                            .subAggregation(getCountWithGrouping(GROUP_BY_FIELD, grouByField, 120)
                                    .subAggregation(getCountWithGrouping(GROUP_BY_FUNCTIONARY, INITIAL_FUNCTIONARY_NAME, 1000))
                                    .subAggregation(AggregationBuilders.topHits(COMPLAINT_RECORD)
                                            .addField(WARD_NAME)
                                            .addField(CITY_NAME)
                                            .addField(DISTRICT_NAME)
                                            .addField(INITIAL_FUNCTIONARY_NAME)
                                            .addField(INITIAL_FUNCTIONARY_MOBILE_NUMBER)
                                            .setSize(1)).subAggregation(getCountWithGrouping(AGGR_CLOSEDCOUNT, IF_CLOSED, 2)
                                            .subAggregation(getAverageWithExclusion(SATISFACTION_AVERAGE, SATISFACTION_INDEX)))))

                    .execute().actionGet();
        else if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_LOCALITIES))
            tableResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                    .setQuery(query).setSize(0)
                    .addAggregation(AggregationBuilders.terms(ULBWISE).field(CITY_CODE).size(120)
                            .subAggregation(AggregationBuilders.terms(WARDWISE).field(WARD_NUMBER).size(120)
                                    .subAggregation(getCountWithGrouping(GROUP_BY_FIELD, grouByField, 120)
                                            .subAggregation(getCountWithGrouping(GROUP_BY_FUNCTIONARY, INITIAL_FUNCTIONARY_NAME, 1000))
                                            .subAggregation(AggregationBuilders.topHits(COMPLAINT_RECORD)
                                                    .addField(WARD_NAME)
                                                    .addField(CITY_NAME)
                                                    .addField(DISTRICT_NAME)
                                                    .addField(INITIAL_FUNCTIONARY_NAME)
                                                    .addField(INITIAL_FUNCTIONARY_MOBILE_NUMBER)
                                                    .addField(DEPARTMENT_NAME)
                                                    .setSize(1))
                                            .subAggregation(getCountWithGrouping(AGGR_CLOSEDCOUNT, IF_CLOSED, 2)
                                                    .subAggregation(getAverageWithExclusion(SATISFACTION_AVERAGE, SATISFACTION_INDEX))))))
                    .execute().actionGet();
        else if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_FUNCTIONARY))
            tableResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                    .setQuery(query).setSize(0)
                    .addAggregation(AggregationBuilders.terms(ULBWISE).field(CITY_CODE).size(120)
                            .subAggregation(AggregationBuilders.terms(DEPARTMENTWISE).field(DEPARTMENT_CODE).size(120)
                                    .subAggregation(getCountWithGrouping(GROUP_BY_FIELD, grouByField, 120)
                                            .subAggregation(getCountWithGrouping(GROUP_BY_FUNCTIONARY, INITIAL_FUNCTIONARY_NAME, 1000))
                                            .subAggregation(AggregationBuilders.topHits(COMPLAINT_RECORD)
                                                    .addField(WARD_NAME)
                                                    .addField(CITY_NAME)
                                                    .addField(DISTRICT_NAME)
                                                    .addField(INITIAL_FUNCTIONARY_NAME)
                                                    .addField(INITIAL_FUNCTIONARY_MOBILE_NUMBER)
                                                    .addField(DEPARTMENT_NAME)
                                                    .setSize(1)).subAggregation(getCountWithGrouping(AGGR_CLOSEDCOUNT, IF_CLOSED, 2)
                                                    .subAggregation(getAverageWithExclusion(SATISFACTION_AVERAGE, SATISFACTION_INDEX))))))
                    .execute().actionGet();
        else
            tableResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
                    .setQuery(query).setSize(0)
                    .addAggregation(getCountWithGrouping(GROUP_BY_FIELD, grouByField, 30)
                            .subAggregation(getCountWithGrouping(GROUP_BY_FUNCTIONARY, INITIAL_FUNCTIONARY_NAME, 1000))
                            .subAggregation(AggregationBuilders.topHits(COMPLAINT_RECORD)
                                    .addField(DISTRICT_NAME)
                                    .addField(CITY_CODE)
                                    .addField(CITY_NAME)
                                    .addField(WARD_NAME)
                                    .setSize(1))
                            .subAggregation(getCountWithGrouping(AGGR_CLOSEDCOUNT, IF_CLOSED, 2)
                                    .subAggregation(getAverageWithExclusion(SATISFACTION_AVERAGE, SATISFACTION_INDEX)))).
                            execute().actionGet();
        return tableResponse;

    }
}