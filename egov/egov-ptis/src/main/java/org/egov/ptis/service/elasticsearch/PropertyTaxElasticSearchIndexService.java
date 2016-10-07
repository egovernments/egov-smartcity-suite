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

package org.egov.ptis.service.elasticsearch;

import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_TAX_INDEX_NAME;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.bean.dashboard.CollectionDetailsRequest;
import org.egov.ptis.bean.dashboard.CollectionIndexDetails;
import org.egov.ptis.elasticsearch.model.PropertyTaxIndex;
import org.egov.ptis.repository.elasticsearch.PropertyTaxIndexRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class PropertyTaxElasticSearchIndexService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionIndexElasticSearchService.class);

	private PropertyTaxIndexRepository propertyTaxIndexRepository;
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Autowired
	public PropertyTaxElasticSearchIndexService(final PropertyTaxIndexRepository propertyTaxIndexRepository) {
        this.propertyTaxIndexRepository = propertyTaxIndexRepository;
    }
	
	public Page<PropertyTaxIndex> findByConsumercode(String consumerCode, PageRequest pageRequest){
		return  propertyTaxIndexRepository.findByConsumercode(consumerCode, new PageRequest(0, 10));
	}
	
	/**
	 * API returns the current year total demand from Property Tax index
	 * @return BigDecimal
	 */
	public BigDecimal getTotalDemand(){
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withIndices(PROPERTY_TAX_INDEX_NAME)
				.addAggregation(AggregationBuilders.sum("totaldemand").field("annualdemand"))
				.build();

		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});

		Sum aggr = aggregations.get("totaldemand");
		return BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * Populates the consolidated demand information in CollectionIndexDetails
	 * @param collectionDetailsRequest
	 * @param collectionIndexDetails
	 */
	public void getConsolidatedDemandInfo(CollectionDetailsRequest collectionDetailsRequest, 
			CollectionIndexDetails collectionIndexDetails){
		LOGGER.info("---- Entered getConsolidatedDemandInfo ---- ");
		Long startTime = System.currentTimeMillis();
		Date fromDate;
		Date toDate;
		/**
		 * For fetching total demand between the date ranges
		 * if dates are sent in the request, consider fromDate and toDate+1 ,
		 * else calculate from current year start date till current date+1 day
		 */
		if(StringUtils.isNotBlank(collectionDetailsRequest.getFromDate()) && StringUtils.isNotBlank(collectionDetailsRequest.getToDate())){
			fromDate = DateUtils.getDate(collectionDetailsRequest.getFromDate(), "yyyy-MM-dd");
			toDate =  DateUtils.addDays(DateUtils.getDate(collectionDetailsRequest.getToDate(), "yyyy-MM-dd"), 1);
		} else {
			fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
			toDate = DateUtils.addDays(new Date(), 1);
		}
		BigDecimal totalDemand = getTotalDemandBasedOnInputFilters(collectionDetailsRequest);
		collectionIndexDetails.setTotalDmd(totalDemand);
		
		int noOfMonths = DateUtils.noOfMonths(fromDate, toDate);
		//Proportional Demand = (totalDemand/12)*noOfmonths
		if(noOfMonths == 0)
			noOfMonths = 1;
		BigDecimal proportionalDemand = (totalDemand.divide(BigDecimal.valueOf(12), BigDecimal.ROUND_HALF_UP))
				.multiply(BigDecimal.valueOf(noOfMonths));
		collectionIndexDetails.setCytdDmd(proportionalDemand.setScale(0, BigDecimal.ROUND_HALF_UP));
		Long timeTaken = System.currentTimeMillis() - startTime;
		LOGGER.info("getConsolidatedDemandInfo ----> calculations done in " + timeTaken / 1000 + " (secs)");
	}
	
	/**
	 * Returns total demand from Property tax index, based on input filters
	 * @param collectionDetailsRequest
	 * @return
	 */
	public BigDecimal getTotalDemandBasedOnInputFilters(CollectionDetailsRequest collectionDetailsRequest){
		BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest);
		SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(PROPERTY_TAX_INDEX_NAME)
				.withQuery(boolQuery)
				.addAggregation(AggregationBuilders.sum("totaldemand").field("annualdemand"))
				.build();
		
		Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});
		
		Sum aggr = collAggr.get("totaldemand");
		return BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * Builds query based on the input parameters sent
	 * @param collectionDetailsRequest
	 * @param indexName
	 * @param ulbCodeField
	 * @return BoolQueryBuilder
	 */
	private BoolQueryBuilder prepareWhereClause(CollectionDetailsRequest collectionDetailsRequest){
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("annualdemand").from(0).to(null));
		if(StringUtils.isNotBlank(collectionDetailsRequest.getRegionName()))
			boolQuery = boolQuery.must(QueryBuilders.matchQuery("regionname", collectionDetailsRequest.getRegionName()));
		if(StringUtils.isNotBlank(collectionDetailsRequest.getDistrictName()))
			boolQuery = boolQuery.must(QueryBuilders.matchQuery("districtname", collectionDetailsRequest.getDistrictName()));
		if(StringUtils.isNotBlank(collectionDetailsRequest.getUlbGrade()))
			boolQuery = boolQuery.must(QueryBuilders.matchQuery("citygrade", collectionDetailsRequest.getUlbGrade()));
		//To be enabled later
		/*if(StringUtils.isNotBlank(collectionDetailsRequest.getUlbCode()))
			boolQuery = boolQuery.must(QueryBuilders.matchQuery("ulbcode", collectionDetailsRequest.getUlbCode()));*/
		return boolQuery;
	}

}