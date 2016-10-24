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

package org.egov.pgr.repository.es.util;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.entity.es.ComplaintDashBoardRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountBuilder;

public class ComplaintElasticsearchUtils {

	public static AggregationBuilder getCountWithGrouping(String aggregationName, String fieldName, int size){
		return AggregationBuilders.terms(aggregationName).field(fieldName).size(size);
	}
	
	public static ValueCountBuilder getCount(String aggregationName, String fieldName){
		return AggregationBuilders.count(aggregationName).field(fieldName);
	}
	
	public static MetricsAggregationBuilder getAverage(String aggregationName, String fieldName){
		return AggregationBuilders.avg(aggregationName).field(fieldName);
	}

	public static AggregationBuilder getCountBetweenSpecifiedDates(String aggregationName, String fieldName, String fromDate, String toDate){

		return AggregationBuilders.dateRange(aggregationName)
				.field(fieldName)
				.addRange(fromDate, toDate);
	}
	
	public static AggregationBuilder getAverageWithFilter(String filterField,int filterValue, String aggregationName, String fieldName){
		return AggregationBuilders.filter("agg")
				.filter(QueryBuilders.termQuery(filterField,filterValue))
				.subAggregation(AggregationBuilders.avg(aggregationName).field(fieldName));
	}
	
	public static AggregationBuilder getAverageWithExclusion(String aggregationName, String fieldName){
		return AggregationBuilders.range("excludeZero").field("satisfactionIndex")
						.addUnboundedFrom("exclusionValue", 1)
						.subAggregation(AggregationBuilders.avg(aggregationName).field(fieldName));
	}
	
	public static String getAggregationGroupingField(ComplaintDashBoardRequest complaintDashBoardRequest){
		String aggregationField = "cityDistrictCode";
        if (StringUtils.isNotBlank(complaintDashBoardRequest.getDistrictCode()))
        	aggregationField = "cityCode";
        if(StringUtils.isNotBlank(complaintDashBoardRequest.getUlbCode()))
        	aggregationField = "wardName";
        if (StringUtils.isNotBlank(complaintDashBoardRequest.getUlbCode()) &&
        		StringUtils.isNotBlank(complaintDashBoardRequest.getWardNo()))
        	aggregationField = "assigneeName";
        if (StringUtils.isNotBlank(complaintDashBoardRequest.getUlbCode()) &&
        		StringUtils.isNotBlank(complaintDashBoardRequest.getDepartmentCode()))
        	aggregationField = "assigneeName";
        
        return aggregationField;
	}
}
