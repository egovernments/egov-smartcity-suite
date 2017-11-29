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

package org.egov.ptis.domain.service.report;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.domain.entity.property.ServiceTypeReportResponse;
import org.egov.ptis.domain.entity.property.WardWiseServiceReponse;
import org.egov.ptis.domain.entity.property.WardWiseServiceTypeRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.egov.ptis.constants.PropertyTaxConstants.DATEFORMATTER_YYYY_MM_DD;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_YYYYMMDD;

@Service
public class ServiceTypeReportService {
    private static final String TAX_BEFORE_AFFECTED = "taxBeforeAffctd";
    private static final String TAX_AFTER_AFFECTED = "taxAfterAffctd";
    private static final String APPLICATION_TYPE = "applicationType";
    private static final String APPLICATION_DATE = "applicationDate";
    private static final String REVENUE_WARD = "ward";
    private static final String WARDGROUPING = "wardgrouping";
    private static final String STATUSGROUPING = "statusgrouping";
    private static final String PROPERTYTYPE = "propertyType";
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    public ServiceTypeReportResponse getDetails(final WardWiseServiceTypeRequest serviceRequest) {
        ServiceTypeReportResponse wardResponse = new ServiceTypeReportResponse();
        Date fromDate = null;
        Date toDate = null;
        if (StringUtils.isNotBlank(serviceRequest.getFromDate())
                && StringUtils.isNotBlank(serviceRequest.getToDate())) {
            fromDate = DateUtils.getDate(serviceRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
            toDate = DateUtils.getDate(serviceRequest.getToDate(), DATE_FORMAT_YYYYMMDD);
        }
        Map<String, Map<String, List<Long>>> map = getDocMap(fromDate, toDate, serviceRequest);
        Set<String> wardNames = map.keySet();
        List<WardWiseServiceReponse> responseList = new ArrayList<>();
        wardNames.stream().forEach( ward->{
            WardWiseServiceReponse serviceWiseResponse = new WardWiseServiceReponse();
            Map<String, List<Long>> statusMap = map.get(ward);
            if (statusMap != null) {
                serviceWiseResponse.setCountFieldValues(statusMap);
            }
            serviceWiseResponse.setRevenueWard(ward);
            responseList.add(serviceWiseResponse);
        });
        wardResponse.setServiceWiseResponse(responseList);
        return wardResponse;
    }

    private BoolQueryBuilder getBoolQuery(Date fromDate, Date toDate, WardWiseServiceTypeRequest serviceRequest) {
        BoolQueryBuilder boolQuery = getSearchFilterQuery(serviceRequest, fromDate, toDate);
        String propertyType = serviceRequest.getPropertyType();
        if (StringUtils.isNotBlank(propertyType))
            boolQuery = getQueryForProperties(propertyType, boolQuery);
        String serviceType = serviceRequest.getServiceType();
        if (StringUtils.isNotBlank(serviceType))
            boolQuery.filter(QueryBuilders.matchQuery(APPLICATION_TYPE, serviceType));
        return boolQuery;
    }

    private Map<String, Map<String, List<Long>>> getDocMap(Date fromDate, Date toDate,
            WardWiseServiceTypeRequest serviceRequest) {
        @SuppressWarnings("rawtypes")
        AggregationBuilder aggregationBuilder = AggregationBuilders.terms(WARDGROUPING).field(REVENUE_WARD).size(120)
                .subAggregation(AggregationBuilders.terms(STATUSGROUPING).field("appStatus")
                        .subAggregation(AggregationBuilders.sum(TAX_BEFORE_AFFECTED).field(TAX_BEFORE_AFFECTED))
                        .subAggregation(AggregationBuilders.sum(TAX_AFTER_AFFECTED).field(TAX_AFTER_AFFECTED)));
        SearchResponse response = elasticsearchTemplate.getClient()
                .prepareSearch("ptservicetype")
                .setQuery(getBoolQuery(fromDate, toDate, serviceRequest))
                .addAggregation(aggregationBuilder)
                .execute().actionGet();

        Terms aggTerms = response.getAggregations().get(WARDGROUPING);
        Map<String, Map<String, List<Long>>> statusMap = new HashMap<>();
        aggTerms.getBuckets().forEach(bucket -> {
            Map<String, List<Long>> statusMap1 = new HashMap<>();
            Terms aggregations = bucket.getAggregations().get(STATUSGROUPING);
            aggregations.getBuckets().forEach(bucket1 -> {
                List<Long> list = new ArrayList<>();
                list.add(bucket1.getDocCount());
                Sum agg1 = bucket1.getAggregations().get(TAX_BEFORE_AFFECTED);
                long value = (long) agg1.getValue();
                list.add(value);
                Sum agg2 = bucket1.getAggregations().get(TAX_AFTER_AFFECTED);
                long value1 = (long) agg2.getValue();
                list.add(value1);
                statusMap1.put(bucket1.getKeyAsString(), list);
            });
            statusMap.put(bucket.getKeyAsString(), statusMap1);
        });
        return statusMap;
    }

    public BoolQueryBuilder getSearchFilterQuery(WardWiseServiceTypeRequest wardWiseRequest, Date fromDate, Date toDate) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.matchQuery("cityCode", ApplicationThreadLocals.getCityCode()))
                .filter(QueryBuilders.rangeQuery(APPLICATION_DATE).gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)));
        if (StringUtils.isNotBlank(wardWiseRequest.getRevenueWard()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery(REVENUE_WARD, wardWiseRequest.getRevenueWard()));
        return boolQuery;
    }

    public BoolQueryBuilder getQueryForProperties(String propertyType, BoolQueryBuilder boolQuery) {
        BoolQueryBuilder propertyTypeQuery = boolQuery;
        if ("VLT".equalsIgnoreCase(propertyType))
            propertyTypeQuery = propertyTypeQuery.filter(QueryBuilders.matchQuery(PROPERTYTYPE, 1));
        if ("PT".equalsIgnoreCase(propertyType))
            propertyTypeQuery = propertyTypeQuery.mustNot(QueryBuilders.matchQuery(PROPERTYTYPE, 1));
        return propertyTypeQuery;
    }
}
