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
import org.egov.pgr.elasticsearch.entity.contract.ComplaintDashBoardRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountBuilder;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ALL_FUNCTIONARY;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ALL_LOCALITIES;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ALL_ULB;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ALL_WARDS;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_CITY;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_DEPARTMENTWISE;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_DISTRICT;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_REGION;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_ULBGRADE;
import static org.egov.pgr.utils.constants.PGRConstants.DASHBOARD_GROUPING_WARDWISE;

public final class ComplaintIndexAggregationBuilder {

    private static final String INITIAL_FUNCTIONARY_NAME = "initialFunctionaryName";
    private static final String LOCALITY_NAME = "localityName";
    private static final String CITY_CODE = "cityCode";

    private ComplaintIndexAggregationBuilder() {
        //Only static API's
    }

    public static AggregationBuilder getCountWithGrouping(final String aggregationName, final String fieldName, final int size) {
        return AggregationBuilders.terms(aggregationName).field(fieldName).size(size);
    }

    public static AggregationBuilder getCountWithGroupingAndOrder(final String aggregationName, final String fieldName,
                                                                  final int size, final String sortField, final String sortOrder) {
        final TermsBuilder aggregation = AggregationBuilders.terms(aggregationName).field(fieldName).size(size);
        if (isNotBlank(sortField) && StringUtils.isNotEmpty(sortField)
                && sortField.equalsIgnoreCase("complaintCount")) {
            Boolean order = true;
            if (StringUtils.isNotEmpty(sortOrder) && isNotBlank(sortOrder)
                    && StringUtils.equalsIgnoreCase(sortOrder, "desc"))
                order = false;
            aggregation.order(Terms.Order.aggregation("_count", order));
        }

        return aggregation;
    }

    public static ValueCountBuilder getCount(final String aggregationName, final String fieldName) {
        return AggregationBuilders.count(aggregationName).field(fieldName);
    }

    public static MetricsAggregationBuilder getAverage(final String aggregationName, final String fieldName) {
        return AggregationBuilders.avg(aggregationName).field(fieldName);
    }

    public static AggregationBuilder getCountBetweenSpecifiedDates(final String aggregationName, final String fieldName,
                                                                   final String fromDate,
                                                                   final String toDate) {

        return AggregationBuilders.dateRange(aggregationName)
                .field(fieldName)
                .addRange(fromDate, toDate);
    }

    public static AggregationBuilder getAverageWithFilter(final String filterField, final int filterValue,
                                                          final String aggregationName,
                                                          final String fieldName) {
        return AggregationBuilders.filter("agg")
                .filter(QueryBuilders.termQuery(filterField, filterValue))
                .subAggregation(AggregationBuilders.avg(aggregationName).field(fieldName));
    }

    public static AggregationBuilder getAverageWithExclusion(final String aggregationName, final String fieldName) {
        return AggregationBuilders.range("excludeZero").field("satisfactionIndex")
                .addUnboundedFrom("exclusionValue", 1)
                .subAggregation(AggregationBuilders.avg(aggregationName).field(fieldName));
    }

    public static String getAggregationGroupingField(final ComplaintDashBoardRequest complaintDashBoardRequest) {
        String aggregationField = "cityDistrictCode";

        if (isNotBlank(complaintDashBoardRequest.getType())) {
            if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_REGION))
                aggregationField = "cityRegionName";
            if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_DISTRICT))
                aggregationField = "cityDistrictCode";
            if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ULBGRADE))
                aggregationField = "cityGrade";
            if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_CITY))
                aggregationField = CITY_CODE;
            if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_DEPARTMENTWISE))
                aggregationField = "departmentName";
            if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_WARDWISE))
                if (isNotBlank(complaintDashBoardRequest.getWardNo()))
                    aggregationField = LOCALITY_NAME;
                else
                    aggregationField = "wardName";
            if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_ULB))
                aggregationField = CITY_CODE;
            if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_WARDS))
                aggregationField = "wardNo";
            if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_LOCALITIES))
                aggregationField = LOCALITY_NAME;
            if (complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_ALL_FUNCTIONARY))
                aggregationField = INITIAL_FUNCTIONARY_NAME;
            return aggregationField;
        }

        if (isNotBlank(complaintDashBoardRequest.getDistrictName())
                || isNotBlank(complaintDashBoardRequest.getUlbGrade()))
            aggregationField = CITY_CODE;
        if (isNotBlank(complaintDashBoardRequest.getUlbCode())) {
            aggregationField = "departmentName";
            if (isNotBlank(complaintDashBoardRequest.getType()) &&
                    complaintDashBoardRequest.getType().equalsIgnoreCase(DASHBOARD_GROUPING_WARDWISE))
                aggregationField = "wardName";
        }
        if (isNotBlank(complaintDashBoardRequest.getUlbCode()) &&
                isNotBlank(complaintDashBoardRequest.getWardNo()))
            aggregationField = LOCALITY_NAME;
        if (isNotBlank(complaintDashBoardRequest.getUlbCode()) &&
                isNotBlank(complaintDashBoardRequest.getDepartmentCode()))
            aggregationField = INITIAL_FUNCTIONARY_NAME;
        if (isNotBlank(complaintDashBoardRequest.getLocalityName()))
            aggregationField = INITIAL_FUNCTIONARY_NAME;

        return aggregationField;
    }
}