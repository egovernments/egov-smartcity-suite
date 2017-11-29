/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
package org.egov.tl.service;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.es.CollectionDocument;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.DateUtils;
import org.egov.tl.entity.contracts.DCRSearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.egov.infra.utils.ApplicationConstant.ES_DATE_FORMAT;
import static org.egov.infra.utils.DateUtils.getFormattedDate;

@Service
public class DailyCollectionReportService {

    private static final String RECEIPT_COUNT = "receipt_count";
    private static final String COLLECTION_INDEX_NAME = "receipts";
    private static final String COLLECION_BILLING_SERVICE_TL = "Trade License";

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AppConfigValueService appConfigValueService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

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
                        new PageRequest(0, receiptCount.getValue() == 0 ? 1 : (int) receiptCount.getValue())).
                build();
        return elasticsearchTemplate.queryForList(dcrSearchQuery, CollectionDocument.class);
    }

    public BoolQueryBuilder getCollectionFilterQuery(DCRSearchRequest searchRequest) {
        Date fromDate = null;
        Date toDate = null;
        if (searchRequest.getFromDate() != null)
            fromDate = DateUtils.startOfDay(searchRequest.getFromDate());

        if (searchRequest.getToDate() != null)
            toDate = DateUtils.endOfDay(searchRequest.getToDate());
        BoolQueryBuilder boolQuery = QueryBuilders.
                boolQuery().
                filter(QueryBuilders.matchQuery("billingService", COLLECION_BILLING_SERVICE_TL)).
                filter(QueryBuilders.rangeQuery("receiptDate").
                        gte(getFormattedDate(fromDate, ES_DATE_FORMAT)).
                        lte(getFormattedDate(toDate, ES_DATE_FORMAT)).
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