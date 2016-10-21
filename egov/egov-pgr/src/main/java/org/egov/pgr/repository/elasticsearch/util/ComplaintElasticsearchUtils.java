package org.egov.pgr.repository.elasticsearch.util;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.entity.elasticsearch.ComplaintDashBoardRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountBuilder;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ComplaintElasticsearchUtils {

	public static AggregationBuilder getCountWithGrouping(String aggregationName, String fieldName){
		return AggregationBuilders.terms(aggregationName).field(fieldName);
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
        	aggregationField = "cityName";
        if(StringUtils.isNotBlank(complaintDashBoardRequest.getUlbCode()))
        	aggregationField = "wardNo";
        if (StringUtils.isNotBlank(complaintDashBoardRequest.getUlbCode()) &&
        		StringUtils.isNotBlank(complaintDashBoardRequest.getWardNo()))
        	aggregationField = "assigneeName";
        if (StringUtils.isNotBlank(complaintDashBoardRequest.getUlbCode()) &&
        		StringUtils.isNotBlank(complaintDashBoardRequest.getDepartmentCode()))
        	aggregationField = "assigneeName";
        
        return aggregationField;
	}
}
