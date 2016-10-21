package org.egov.pgr.repository.elasticsearch;

import static org.egov.pgr.utils.constants.PGRConstants.PGR_INDEX_DATE_FORMAT;
import static org.egov.pgr.utils.constants.PGRConstants.PGR_INDEX_NAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.pgr.entity.elasticsearch.ComplaintDashBoardRequest;
import org.egov.pgr.entity.elasticsearch.ComplaintDashBoardResponse;
import org.egov.pgr.repository.elasticsearch.util.ComplaintElasticsearchUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

public class ComplaintIndexRepositoryImpl implements ComplaintIndexCustomRepository{

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Override
	public HashMap<String, Object> findAllGrievanceByFilter(ComplaintDashBoardRequest complaintDashBoardRequest, BoolQueryBuilder query,String grouByField) {

		DateTimeFormatter formatter = DateTimeFormat.forPattern(PGR_INDEX_DATE_FORMAT);
		DateTime fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue();
		DateTime toDate = new DateTime();

		SearchResponse consolidatedResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
				.setQuery(QueryBuilders.matchAllQuery()).setSize(0)
				.addAggregation(ComplaintElasticsearchUtils.getCount("countAggregation", "crn"))
				.addAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("closedCount", "ifClosed"))
				.addAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("slaCount", "ifSLA"))
				.addAggregation(ComplaintElasticsearchUtils.getAverageWithFilter("ifClosed",1,"AgeingInWeeks", "complaintAgeingdaysFromDue"))
				.addAggregation(ComplaintElasticsearchUtils.getAverageWithExclusion("satisfactionAverage", "satisfactionIndex"))
				.addAggregation(ComplaintElasticsearchUtils.getCountBetweenSpecifiedDates("currentYear", 
								"createdDate", fromDate.toString(formatter), toDate.toString(formatter)))
				.execute().actionGet();
		
		SearchResponse tableResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
									   .setQuery(query).setSize(0)
									   .addAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("complaintTypeWise", "complaintTypeName")
											   		   .subAggregation(AggregationBuilders.range("ComplaintTypeAgeing").field("complaintAgeingdaysFromDue")
											   				           .addRange("1week", 0, 8).addRange("1month",8 ,32)
											   				           .addRange("3months", 32, 91).addUnboundedFrom("remainingMonths", 91))
											   		   .subAggregation(ComplaintElasticsearchUtils.getAverageWithExclusion("complaintTypeSatisfactionAverage", "satisfactionIndex"))
											   		   .subAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("complaintTypeWiseOpenAndClosedCount", "ifClosed")
											   				   			.subAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("complaintTypeSla", "ifSLA"))))
									   .addAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("groupByField", grouByField)
											   			.subAggregation(AggregationBuilders.range("groupByFieldAgeing").field("complaintAgeingdaysFromDue")
											   							.addRange("1week", 0, 8).addRange("1month",8 ,32)
											   							.addRange("3months", 32, 91).addUnboundedFrom("remainingMonths", 91))
											   			.subAggregation(ComplaintElasticsearchUtils.getAverageWithExclusion("groupByFieldSatisfactionAverage", "satisfactionIndex"))
											   			.subAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("groupFieldWiseOpenAndClosedCount", "ifClosed")
											   							.subAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("groupByFieldSla", "ifSLA"))))
									   .execute().actionGet();

		HashMap<String, Object> result = new HashMap<>();
		List<ComplaintDashBoardResponse> responseDetailsList = new ArrayList<>();

		ValueCount totalCount = consolidatedResponse.getAggregations().get("countAggregation");
		result.put("TotalComplaint", totalCount.getValue());

		Filter filter = consolidatedResponse.getAggregations().get("agg");
		Avg averageAgeing =  filter.getAggregations().get("AgeingInWeeks");
		result.put("AvgAgeingInWeeks",averageAgeing.getValue()/7);
		
		Range satisfactionAverage = consolidatedResponse.getAggregations().get("excludeZero");
		Avg averageSatisfaction = satisfactionAverage.getBuckets().get(0).getAggregations().get("satisfactionAverage");
		result.put("AvgCustomeSatisfactionIndex", averageSatisfaction.getValue());
		
		//To get the count of closed and open complaints
		Terms terms = consolidatedResponse.getAggregations().get("closedCount");
		for (Bucket bucket : terms.getBuckets()) {
			if(bucket.getKeyAsNumber().intValue() == 1)
				result.put("OpenComplaints",bucket.getDocCount());
			else
				result.put("ClosedComplaints",bucket.getDocCount());
		}

		//To get the count of closed and open complaints
		terms = consolidatedResponse.getAggregations().get("slaCount");
		for (Bucket bucket : terms.getBuckets()) {
			if(bucket.getKeyAsNumber().intValue() == 1)
				result.put("WithinSLACount",bucket.getDocCount());
			else
				result.put("OutSideSLACount",bucket.getDocCount());
		}
		Range currentYearCount = consolidatedResponse.getAggregations().get("currentYear");
		result.put("CYTDComplaint", currentYearCount.getBuckets().get(0).getDocCount());

		//For Dynamic results based on grouping fields
		terms = tableResponse.getAggregations().get("groupByField");
		for (Bucket bucket : terms.getBuckets()) {
			ComplaintDashBoardResponse responseDetail = new ComplaintDashBoardResponse();
			responseDetail.setDistrictName(StringUtils.EMPTY);
			responseDetail.setUlbName(StringUtils.EMPTY);
			responseDetail.setWardName(StringUtils.EMPTY);
			responseDetail.setDepartmentName(StringUtils.EMPTY);
			responseDetail.setFunctionaryName(StringUtils.EMPTY);
			responseDetail.setComplaintTypeName(StringUtils.EMPTY);
			responseDetail.setUlbGrade(StringUtils.EMPTY);
			
			if(StringUtils.isNotBlank(complaintDashBoardRequest.getDistrictCode()))
				responseDetail.setUlbName(bucket.getKeyAsString());
			
			satisfactionAverage = bucket.getAggregations().get("excludeZero");
			Avg groupByFieldAverageSatisfaction = satisfactionAverage.getBuckets().get(0).getAggregations().get("groupByFieldSatisfactionAverage");
			if(Double.isNaN(groupByFieldAverageSatisfaction.getValue()))
				responseDetail.setAvgSatisfactionIndex(0);
			else
				responseDetail.setAvgSatisfactionIndex(groupByFieldAverageSatisfaction.getValue());

			Terms openAndClosedTerms = bucket.getAggregations().get("groupFieldWiseOpenAndClosedCount");
			for (Bucket closedCountbucket : openAndClosedTerms.getBuckets()) {
				if(closedCountbucket.getKeyAsNumber().intValue() == 1){
					responseDetail.setOpenComplaintCount(closedCountbucket.getDocCount());
					Terms slaTerms = closedCountbucket.getAggregations().get("groupByFieldSla");
					for(Bucket slaBucket : slaTerms.getBuckets()){
						if(slaBucket.getKeyAsNumber().intValue() == 1)
							responseDetail.setOpenWithinSLACount(slaBucket.getDocCount());
						else
							responseDetail.setOpenOutSideSLACount(slaBucket.getDocCount());
					}
				}
				else{
					responseDetail.setClosedComplaintCount(closedCountbucket.getDocCount());
					Terms slaTerms = closedCountbucket.getAggregations().get("groupByFieldSla");
					for(Bucket slaBucket : slaTerms.getBuckets()){
						if(slaBucket.getKeyAsNumber().intValue() == 1)
							responseDetail.setClosedWithinSLACount(slaBucket.getDocCount());
						else
							responseDetail.setClosedOutSideSLACount(slaBucket.getDocCount());
					}
				}

			}
			responseDetailsList.add(responseDetail);
		}
		result.put("responseDetails",responseDetailsList);

		List<ComplaintDashBoardResponse> complaintTypeList = new ArrayList<>();
		//For complaintTypeWise result
		terms = tableResponse.getAggregations().get("complaintTypeWise");
		for (Bucket bucket : terms.getBuckets()) {
			ComplaintDashBoardResponse complaintType = new ComplaintDashBoardResponse();
			complaintType.setDistrictName(StringUtils.EMPTY);
			complaintType.setUlbName(StringUtils.EMPTY);
			complaintType.setWardName(StringUtils.EMPTY);
			complaintType.setDepartmentName(StringUtils.EMPTY);
			complaintType.setFunctionaryName(StringUtils.EMPTY);
			complaintType.setComplaintTypeName(bucket.getKey().toString());
			complaintType.setTotalComplaintCount(bucket.getDocCount());
			complaintType.setUlbGrade(StringUtils.EMPTY);
			
			satisfactionAverage = bucket.getAggregations().get("excludeZero");
			Avg complaintTypeAverageSatisfaction = satisfactionAverage.getBuckets().get(0).getAggregations().get("complaintTypeSatisfactionAverage");
			if(Double.isNaN(complaintTypeAverageSatisfaction.getValue()))
				complaintType.setAvgSatisfactionIndex(0);
			else
				complaintType.setAvgSatisfactionIndex(complaintTypeAverageSatisfaction.getValue());

			Terms openAndClosedTerms = bucket.getAggregations().get("complaintTypeWiseOpenAndClosedCount");
			for (Bucket closedCountbucket : openAndClosedTerms.getBuckets()) {
				if(closedCountbucket.getKeyAsNumber().intValue() == 1){
					complaintType.setOpenComplaintCount(closedCountbucket.getDocCount());
					Terms slaTerms = closedCountbucket.getAggregations().get("complaintTypeSla");
					for(Bucket slaBucket : slaTerms.getBuckets()){
						if(slaBucket.getKeyAsNumber().intValue() == 1)
							complaintType.setOpenWithinSLACount(slaBucket.getDocCount());
						else
							complaintType.setOpenOutSideSLACount(slaBucket.getDocCount());
					}
				}
				else{
					complaintType.setClosedComplaintCount(closedCountbucket.getDocCount());
					Terms slaTerms = closedCountbucket.getAggregations().get("complaintTypeSla");
					for(Bucket slaBucket : slaTerms.getBuckets()){
						if(slaBucket.getKeyAsNumber().intValue() == 1)
							complaintType.setClosedWithinSLACount(slaBucket.getDocCount());
						else
							complaintType.setClosedOutSideSLACount(slaBucket.getDocCount());
					}
				}

			}
			complaintTypeList.add(complaintType);
		}
		result.put("complaintTypes",complaintTypeList);
		return result;
	}
}