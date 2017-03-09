/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
package org.egov.tl.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.es.CollectionDocument;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.infra.utils.DateUtils;
import org.egov.tl.entity.dto.DCRSearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
@Service
public class DCRService {

    private static final String RECEIPT_COUNT = "receipt_count";
    private static final String COLLECTION_INDEX_NAME="receipts";
    private static final String COLLECION_BILLING_SERVICE_TL = "Trade License";
    private static final DateTimeFormatter YYYY_MM_DD_FORMAT = DateTimeFormat.forPattern(ApplicationConstant.ES_DATE_FORMAT);

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public AssignmentService assignmentService;

    @Autowired
    public AppConfigValueService appConfigValueService;

    public Set<User> getCollectionOperators() {
        String operatorDesignation = appConfigValueService.getAppConfigValueByDate(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.COLLECTION_DESIGNATIONFORCSCOPERATOR, new Date()).getValue();
        return assignmentService.getUsersByDesignations(operatorDesignation.split(","));
    }

    public List<CollectionDocument> searchDailyCollection(DCRSearchRequest dcrSearchRequest) {
        BoolQueryBuilder dcrSearchCriteria = getCollectionFilterQuery(dcrSearchRequest);
        SearchQuery dcrSearchQuery = new NativeSearchQueryBuilder().
                withIndices(COLLECTION_INDEX_NAME).
                withQuery(dcrSearchCriteria).
                addAggregation(AggregationBuilders.count(RECEIPT_COUNT).field("consumerCode")).
                build();
        ValueCount receiptCount = elasticsearchTemplate.
                query(dcrSearchQuery, SearchResponse::getAggregations).
                get(RECEIPT_COUNT);
        dcrSearchQuery = new NativeSearchQueryBuilder().
                withIndices(COLLECTION_INDEX_NAME).
                withQuery(dcrSearchCriteria).
                addAggregation(AggregationBuilders.count(RECEIPT_COUNT).field("consumerCode")).
                withPageable(
                        new PageRequest(0, Long.valueOf(receiptCount.getValue()).intValue() == 0 ? 1
                                : Long.valueOf(receiptCount.getValue()).intValue())).
                build();
        return elasticsearchTemplate.queryForList(dcrSearchQuery, CollectionDocument.class);
    }

    public BoolQueryBuilder getCollectionFilterQuery(DCRSearchRequest searchRequest) {
        Date fromDate = null;
        Date toDate = null;
        if (searchRequest.getFromDate()!= null) 
            fromDate = DateUtils.startOfDay(searchRequest.getFromDate());
        
        if (searchRequest.getToDate() != null)
            toDate = DateUtils.endOfDay(searchRequest.getToDate());
        BoolQueryBuilder boolQuery = QueryBuilders.
                boolQuery().
                filter(QueryBuilders.matchQuery("billingService",COLLECION_BILLING_SERVICE_TL)).
                filter(QueryBuilders.rangeQuery("receiptDate").
                        gte(YYYY_MM_DD_FORMAT.print(new DateTime(fromDate))).
                        lte(YYYY_MM_DD_FORMAT.print(new DateTime(toDate))).
                        includeUpper(false));

        boolQuery = boolQuery.filter(QueryBuilders.matchQuery("cityName", ApplicationThreadLocals.getCityName()));
        if (StringUtils.isNotBlank(searchRequest.getCollectionOperator()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("receiptCreator", searchRequest.getCollectionOperator()));
        if (StringUtils.isNotBlank(searchRequest.getStatus()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("status", searchRequest.getStatus()));
        if (StringUtils.isNotBlank(searchRequest.getRevenueWard()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("revenueWard", searchRequest.getRevenueWard()));

        return boolQuery;
    }

}