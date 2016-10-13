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

import static org.egov.ptis.constants.PropertyTaxConstants.BIGDECIMAL_100;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_TAX_INDEX_NAME;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.bean.dashboard.CollectionDetailsRequest;
import org.egov.ptis.bean.dashboard.CollectionIndexDetails;
import org.egov.ptis.bean.dashboard.PropertyTaxDefaultersRequest;
import org.egov.ptis.bean.dashboard.TaxDefaulters;
import org.egov.ptis.bean.dashboard.TaxPayerDetails;
import org.egov.ptis.bean.dashboard.TaxPayerResponseDetails;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.elasticsearch.model.PropertyTaxIndex;
import org.egov.ptis.repository.elasticsearch.PropertyTaxIndexRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
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

	private PropertyTaxIndexRepository propertyTaxIndexRepository;
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Autowired
	private Client client;
	
	@Autowired
	private CollectionIndexElasticSearchService collectionIndexElasticSearchService;
	
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
		
		//performance = (current year tilldate collection * 100)/(proportional demand)
		collectionIndexDetails.setPerformance((collectionIndexDetails.getCytdColl().multiply(PropertyTaxConstants.BIGDECIMAL_100)).divide(proportionalDemand, 1, BigDecimal.ROUND_HALF_UP));
		//variance = ((currentYearCollection - lastYearCollection)*100)/lastYearCollection
		BigDecimal variation = BigDecimal.ZERO;
		if(collectionIndexDetails.getLytdColl().compareTo(BigDecimal.ZERO) ==0)
			variation = PropertyTaxConstants.BIGDECIMAL_100;
		else
		variation = ((collectionIndexDetails.getCytdColl().subtract(collectionIndexDetails.getLytdColl()))
								.multiply(PropertyTaxConstants.BIGDECIMAL_100)).divide(collectionIndexDetails.getLytdColl(), 1, BigDecimal.ROUND_HALF_UP);
		collectionIndexDetails.setLyVar(variation);
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
	 * Returns List of Top Ten Tax Performers
	 * @param collectionDetailsRequest
	 * @return
	 */
	public TaxPayerResponseDetails getTopTenTaxPerformers(CollectionDetailsRequest collectionDetailsRequest){
		
	    TaxPayerResponseDetails topTaxPerformers = new TaxPayerResponseDetails(); 
		List<TaxPayerDetails> taxProducers = returnUlbWiseAggregationResults(collectionDetailsRequest, PROPERTY_TAX_INDEX_NAME, false,"total_collection",10);
		List<TaxPayerDetails> taxAchievers = returnUlbWiseAggregationResults(collectionDetailsRequest, PROPERTY_TAX_INDEX_NAME, false,"total_collection",120);
		
		topTaxPerformers.setProducers(taxProducers);
		topTaxPerformers.setAchievers(taxAchievers);
		
		return topTaxPerformers;
	}
	
	/**
	 * Returns List of Bottom Ten Tax Performers
	 * @param collectionDetailsRequest
	 * @return
	 */
	public TaxPayerResponseDetails getBottomTenTaxPerformers(CollectionDetailsRequest collectionDetailsRequest){
		TaxPayerResponseDetails topTaxPerformers = new TaxPayerResponseDetails(); 
		List<TaxPayerDetails> taxProducers = returnUlbWiseAggregationResults(collectionDetailsRequest, PROPERTY_TAX_INDEX_NAME, true,"total_collection",10);
		List<TaxPayerDetails> taxAchievers = returnUlbWiseAggregationResults(collectionDetailsRequest, PROPERTY_TAX_INDEX_NAME, true,"total_collection",120);
		
		topTaxPerformers.setProducers(taxProducers);
		topTaxPerformers.setAchievers(taxAchievers);
		
		return topTaxPerformers;
	}
	
	/**
	 * Returns Top Ten with ULB wise grouping and total amount aggregation
	 * @param collectionDetailsRequest
	 * @param indexName
	 * @param order
	 * @param orderingAggregationName 
	 * @return
	 */
	public List<TaxPayerDetails> returnUlbWiseAggregationResults(CollectionDetailsRequest collectionDetailsRequest,
			String indexName,Boolean order,String orderingAggregationName,int size){
		List<TaxPayerDetails> taxPayers = new ArrayList<>();
		BoolQueryBuilder boolQuery = prepareWhereClause(collectionDetailsRequest);

		//orderingAggregationName is the aggregation name by which we have to order the results 
		//IN this case can be one of "totaldemand" or "total_collection" or "avg_achievement"
		AggregationBuilder aggregation = AggregationBuilders.terms("by_aggregationField").field("cityname")
				.size(size)
				.order(Terms.Order.aggregation(orderingAggregationName,order))
				.subAggregation(AggregationBuilders.sum("totaldemand").field("totaldemand"))
			    .subAggregation(AggregationBuilders.sum("total_collection").field("totalcollection"));
		
		
		SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices(indexName)
				.withQuery(boolQuery)
				.addAggregation(aggregation)
				.build();
		
		
		Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
			@Override
			public Aggregations extract(SearchResponse response) {
				return response.getAggregations();
			}
		});
		
		TaxPayerDetails taxDetail;
		Date fromDate = new DateTime().withMonthOfYear(4).dayOfMonth().withMinimumValue().toDate();
		Date toDate = DateUtils.addDays(new Date(), 1);
		Date lastYearFromDate = DateUtils.addYears(fromDate, -1);
		Date lastYearToDate = DateUtils.addYears(toDate, -1);
		StringTerms totalAmountAggr = collAggr.get("by_aggregationField");
		for (Terms.Bucket entry : totalAmountAggr.getBuckets()) {
			taxDetail = new TaxPayerDetails();
			taxDetail.setRegionName(collectionDetailsRequest.getRegionName());
			taxDetail.setDistrictName(collectionDetailsRequest.getDistrictName());
			taxDetail.setUlbGrade(collectionDetailsRequest.getUlbGrade());
			String fieldName = String.valueOf(entry.getKey());
			taxDetail.setUlbName(fieldName);
			//Proportional Demand = (totalDemand/12)*noOfmonths
			int noOfMonths = DateUtils.noOfMonths(fromDate, toDate);
			if(noOfMonths == 0)
				noOfMonths = 1;
			Sum totalDemandAggregation = entry.getAggregations().get("totaldemand");
			Sum totalCollectionAggregation = entry.getAggregations().get("total_collection");
			BigDecimal totalDemandValue = BigDecimal.valueOf(totalDemandAggregation.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
			BigDecimal totalCollections = BigDecimal.valueOf(totalCollectionAggregation.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
			BigDecimal proportionalDemand = (totalDemandValue.divide(BigDecimal.valueOf(12),BigDecimal.ROUND_HALF_UP)).multiply(BigDecimal.valueOf(noOfMonths));
			taxDetail.setTotalDmd(totalDemandValue);
			taxDetail.setCytdColl(totalCollections);
			taxDetail.setCytdDmd(proportionalDemand);
			taxDetail.setAchievement(totalCollections.multiply(BIGDECIMAL_100).divide(proportionalDemand, 1, BigDecimal.ROUND_HALF_UP));
			taxDetail.setCytdBalDmd(proportionalDemand.subtract(totalCollections));
			BigDecimal lastYearCollection = collectionIndexElasticSearchService.getTotalCollectionsForDatesForUlb(collectionDetailsRequest, lastYearFromDate, lastYearToDate,fieldName);
			taxDetail.setLytdColl(lastYearCollection);
			//variance = ((currentYearCollection - lastYearCollection)*100)/lastYearCollection
			BigDecimal variation = BigDecimal.ZERO;
			if(lastYearCollection.compareTo(BigDecimal.ZERO) == 0)
				variation = PropertyTaxConstants.BIGDECIMAL_100;
			else
			    variation = (((totalCollections.subtract(lastYearCollection)).multiply(PropertyTaxConstants.BIGDECIMAL_100)).divide(lastYearCollection, 1, BigDecimal.ROUND_HALF_UP));
			taxDetail.setLyVar(variation);
			taxPayers.add(taxDetail);
		}
		return returnTopResults(taxPayers,size,order);
	}
	
	private List<TaxPayerDetails> returnTopResults(List<TaxPayerDetails> taxPayers,int size,Boolean order){
		if(size > 10){
			if(order)
				Collections.sort(taxPayers);
			else
				Collections.sort(taxPayers,Collections.reverseOrder());
			
			return taxPayers.subList(0, 10);
		}
		return taxPayers;
	}
	
	
	/**
	 * Returns top 100 tax defaulters
	 * @param propertyTaxDefaultersRequest
	 * @return
	 */
	public List<TaxDefaulters> getTopDefaulters(PropertyTaxDefaultersRequest propertyTaxDefaultersRequest){
		
		BoolQueryBuilder boolQuery = filterBasedOnRequest(propertyTaxDefaultersRequest);
		
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
		        .withIndices(PROPERTY_TAX_INDEX_NAME)
				.withQuery(boolQuery)
				.withSort(new FieldSortBuilder("totalbalance").order(SortOrder.DESC))
				.withPageable(new PageRequest(0, 100))
				.build();
		
		final Page<PropertyTaxIndex> propertyTaxRecords = elasticsearchTemplate.queryForPage(searchQuery, PropertyTaxIndex.class);
		
		List<TaxDefaulters> taxDefaulters = new ArrayList<>();
		TaxDefaulters taxDfaulter;
		for(PropertyTaxIndex property : propertyTaxRecords){
			taxDfaulter = new TaxDefaulters();
			taxDfaulter.setOwnerName(property.getConsumername());
			taxDfaulter.setPropertyType(property.getPropertytype());
			taxDfaulter.setUlbName(property.getCityname());
			taxDfaulter.setBalance(BigDecimal.valueOf(property.getTotalbalance()));
			taxDfaulter.setPeriod(StringUtils.EMPTY);
			taxDefaulters.add(taxDfaulter);
		}
		return taxDefaulters;
	}
	
	/**
	 * This is used for top 100 defaulter's since ward level filtering is also present
	 * Query which filters documents from index based on request
	 * @param propertyTaxDefaultersRequest
	 * @return
	 */
	private BoolQueryBuilder filterBasedOnRequest(PropertyTaxDefaultersRequest propertyTaxDefaultersRequest){
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("annualdemand").from(0).to(null));
		if(StringUtils.isNotBlank(propertyTaxDefaultersRequest.getRegionName()))
			boolQuery = boolQuery.must(QueryBuilders.matchQuery("regionname", propertyTaxDefaultersRequest.getRegionName()));
		if(StringUtils.isNotBlank(propertyTaxDefaultersRequest.getDistrictName()))
			boolQuery = boolQuery.must(QueryBuilders.matchQuery("districtname", propertyTaxDefaultersRequest.getDistrictName()));
		if(StringUtils.isNotBlank(propertyTaxDefaultersRequest.getUlbName()))
			boolQuery = boolQuery.must(QueryBuilders.matchQuery("cityname", propertyTaxDefaultersRequest.getUlbName()));
		if(StringUtils.isNotBlank(propertyTaxDefaultersRequest.getWardName()))
			boolQuery = boolQuery.must(QueryBuilders.matchQuery("revwardname", propertyTaxDefaultersRequest.getWardName()));
		return boolQuery;
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