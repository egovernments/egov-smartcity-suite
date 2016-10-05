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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.egov.commons.CFinancialYear;
import org.egov.commons.service.CFinancialYearService;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.repository.elasticsearch.CollectionIndexESRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

@Service
public class CollectionIndexElasticSearchService {

	private CollectionIndexESRepository collectionIndexESRepository;
	
	@Autowired
	private CFinancialYearService cFinancialYearService;
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Autowired
	public CollectionIndexElasticSearchService(final CollectionIndexESRepository collectionIndexESRepository) {
        this.collectionIndexESRepository = collectionIndexESRepository;
    }
	
	public Map<String, BigDecimal> getConsolidatedCollection(String billingService){
		Map<String, BigDecimal> consolidatedCollValues = new HashMap<>();
		CFinancialYear currFinYear = cFinancialYearService.getFinancialYearByDate(new Date());
		//For current year results
		consolidatedCollValues.put("cytdColln", getConsolidatedCollForYears(currFinYear.getStartingDate(), new Date(), billingService));
		//For last year results
		consolidatedCollValues.put("lytdColln", getConsolidatedCollForYears(DateUtils.addYears(currFinYear.getStartingDate(), -1), 
				DateUtils.addYears(new Date(), -1), billingService));
		return consolidatedCollValues;
	}

	public BigDecimal getConsolidatedCollForYears(Date fromDate, Date toDate, String billingService) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("receiptdate").gte(sdf.format(fromDate)).lte(sdf.format(toDate)))
							.must(QueryBuilders.termQuery("billingservice", billingService));
		SearchQuery searchQueryColl = new NativeSearchQueryBuilder().withIndices("collection").withQuery(queryBuilder)
						.addAggregation(AggregationBuilders.sum("collectiontotal").field("totalamount"))
						.build();

		Aggregations collAggr = elasticsearchTemplate.query(searchQueryColl, new ResultsExtractor<Aggregations>() {
				@Override
				public Aggregations extract(SearchResponse response) {
					return response.getAggregations();
				}
		});

		Sum aggr = collAggr.get("collectiontotal");
		return BigDecimal.valueOf(aggr.getValue()).setScale(0, BigDecimal.ROUND_HALF_UP);
	}
}
