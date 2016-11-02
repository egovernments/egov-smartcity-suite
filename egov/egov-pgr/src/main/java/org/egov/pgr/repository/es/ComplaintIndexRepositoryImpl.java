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

import static org.egov.pgr.utils.constants.PGRConstants.PGR_INDEX_DATE_FORMAT;
import static org.egov.pgr.utils.constants.PGRConstants.PGR_INDEX_NAME;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.es.CityIndexService;
import org.egov.pgr.entity.es.ComplaintDashBoardRequest;
import org.egov.pgr.repository.es.util.ComplaintElasticsearchUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

public class ComplaintIndexRepositoryImpl implements ComplaintIndexCustomRepository{

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Autowired
	private CityIndexService cityIndexService;
	
	@Autowired
	private DepartmentService departmentService;

	@Override
	public HashMap<String, SearchResponse> findAllGrievanceByFilter(ComplaintDashBoardRequest complaintDashBoardRequest, BoolQueryBuilder query,String grouByField) {

		DateTimeFormatter formatter = DateTimeFormat.forPattern(PGR_INDEX_DATE_FORMAT);
		DateTime currentYearfromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue();
		DateTime currentYeartoDate = new DateTime();
		
		/**
         * For Current day's complaint count if dates are sent in the request,
         * consider the toDate, else take date range between current date +1 day
         */
		DateTime fromDate = new DateTime();
		DateTime toDate = new DateTime();
        if (StringUtils.isNotBlank(complaintDashBoardRequest.getFromDate())
                && StringUtils.isNotBlank(complaintDashBoardRequest.getToDate())) {
        	fromDate = new DateTime(complaintDashBoardRequest.getFromDate());
        	toDate = fromDate.plusDays(1);
        } else {
            toDate = fromDate.plusDays(1);
        }
		
		//This is size used to fetch those many number of documents
		int size = 120;
		if(complaintDashBoardRequest.getSize() >= 0)
			size = complaintDashBoardRequest.getSize();

		SearchResponse consolidatedResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
				.setQuery(QueryBuilders.matchAllQuery()).setSize(0)
				.addAggregation(ComplaintElasticsearchUtils.getCount("countAggregation", "crn"))
				.addAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("closedCount", "ifClosed",2))
				.addAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("slaCount", "ifSLA",2))
				.addAggregation(ComplaintElasticsearchUtils.getAverageWithFilter("ifClosed",1,"AgeingInWeeks", "complaintAgeingdaysFromDue"))
				.addAggregation(ComplaintElasticsearchUtils.getAverageWithExclusion("satisfactionAverage", "satisfactionIndex"))
				.addAggregation(ComplaintElasticsearchUtils.getCountBetweenSpecifiedDates("currentYear", 
								"createdDate", currentYearfromDate.toString(formatter), currentYeartoDate.toString(formatter)))
				.addAggregation(ComplaintElasticsearchUtils.getCountBetweenSpecifiedDates("todaysComplaintCount", 
								"createdDate", fromDate.toString(formatter), toDate.toString(formatter)))
				.execute().actionGet();
		
		SearchResponse tableResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
									   .setQuery(query).setSize(0)
									   .addAggregation(ComplaintElasticsearchUtils.getCountWithGroupingAndOrder("complaintTypeWise", "complaintTypeName",size,
											   													complaintDashBoardRequest.getSortField(),complaintDashBoardRequest.getSortDirection())
											   		   .subAggregation(ComplaintElasticsearchUtils.getAverageWithExclusion("complaintTypeSatisfactionAverage", "satisfactionIndex"))
											   		   .subAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("complaintTypeWiseOpenAndClosedCount", "ifClosed",2)
											   				   			.subAggregation(AggregationBuilders.range("ComplaintTypeAgeing").field("complaintAgeingdaysFromDue")
											   				   							.addRange("1week", 0, 8).addRange("1month",8 ,32)
											   				   							.addRange("3months", 32, 91).addUnboundedFrom("remainingMonths", 91))
											   				   			.subAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("complaintTypeSla", "ifSLA",2))))
									   .addAggregation(ComplaintElasticsearchUtils.getCountWithGroupingAndOrder("groupByField", grouByField,size,
											   								complaintDashBoardRequest.getSortField(),complaintDashBoardRequest.getSortDirection())
											   			.subAggregation(ComplaintElasticsearchUtils.getAverageWithExclusion("groupByFieldSatisfactionAverage", "satisfactionIndex"))
											   			.subAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("groupFieldWiseOpenAndClosedCount", "ifClosed",2)
											   							.subAggregation(AggregationBuilders.range("groupByFieldAgeing").field("complaintAgeingdaysFromDue")
											   											.addRange("1week", 0, 8).addRange("1month",8 ,32)
											   											.addRange("3months", 32, 91).addUnboundedFrom("remainingMonths", 91))
											   							.subAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("groupByFieldSla", "ifSLA",2))))
									   .execute().actionGet();
		
		HashMap<String, SearchResponse> result = new HashMap<>();
		result.put("consolidatedResponse", consolidatedResponse);
		result.put("tableResponse", tableResponse);
		
		return result;
	}
	
	@Override
	public String getWardName(String wardNo){
		SearchResponse response = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
											  .setSize(1)
											  .setQuery(QueryBuilders.matchQuery("wardNo", wardNo))
											  .execute().actionGet();
		
		for (SearchHit hit : response.getHits()) {
			final Map<String,Object> fields = hit.getSource();
			return fields.get("wardName").toString();
	    }		
		
		return StringUtils.EMPTY;
	}

	@Override
	public SearchResponse findAllGrievanceByComplaintType(ComplaintDashBoardRequest complaintDashBoardRequest,
			BoolQueryBuilder query, String grouByField) {
		
		SearchResponse complaintTypeResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
				   .setQuery(query).setSize(0)
				   .addAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("groupByField", grouByField,120)
						           .subAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("closedComplaintCount", "ifClosed",2)))
				   .execute().actionGet();
		
		return complaintTypeResponse;
	}
	
	@Override
	public SearchResponse findAllGrievanceBySource(ComplaintDashBoardRequest complaintDashBoardRequest, 
			BoolQueryBuilder query,String grouByField){
		
		SearchResponse sourceResponse = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
				   .setQuery(query).setSize(0)
				   .addAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("groupByField", grouByField,120)
						   		   .subAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("groupByFieldSource", "source",30)))
				   .addAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("complaintTypeWise", "complaintTypeName",120)
						   		   .subAggregation(ComplaintElasticsearchUtils.getCountWithGrouping("complaintTypeWiseSource", "source",30)))
				   .execute().actionGet();
		
		return sourceResponse;
	}

	@Override
	public String getFunctionryMobileNumber(String functionaryName) {
		SearchResponse response = elasticsearchTemplate.getClient().prepareSearch(PGR_INDEX_NAME)
				.setSize(1)
				.setQuery(QueryBuilders.matchQuery("currentFunctionaryName", functionaryName))
				.execute().actionGet();

		for (SearchHit hit : response.getHits()) {
			final Map<String,Object> fields = hit.getSource();
			return fields.get("currentFunctionaryMobileNumber").toString();
		}		
		return StringUtils.EMPTY;
	}
}