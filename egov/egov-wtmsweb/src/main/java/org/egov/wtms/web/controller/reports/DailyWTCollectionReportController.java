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
package org.egov.wtms.web.controller.reports;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.es.CollectionDocument;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.wtms.application.entity.DailyWTCollectionReportSearch;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/report/dailyWTCollectionReport/search/")
public class DailyWTCollectionReportController {

    @Autowired
    public EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    public AssignmentService assignmentService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    public AppConfigValueService appConfigValueService;

    @Autowired
    private CityService cityService;
    @Autowired
    private BoundaryService boundaryService;

    @ModelAttribute
    public void reportModel(final Model model) {
        final DailyWTCollectionReportSearch dailyCollectionReportResut = new DailyWTCollectionReportSearch();
        model.addAttribute("dailyWTCollectionReport", dailyCollectionReportResut);
    }

    @ModelAttribute("collectionMode")
    public Map<String, String> loadInstrumentTypes() {
        final Map<String, String> collectionModeMap = new LinkedHashMap<String, String>(0);
        collectionModeMap.put(Source.ESEVA.toString(), Source.ESEVA.toString());
        collectionModeMap.put(Source.MEESEVA.toString(), Source.MEESEVA.toString());
        collectionModeMap.put(Source.APONLINE.toString(), Source.APONLINE.toString());
        collectionModeMap.put(Source.SOFTTECH.toString(), Source.SOFTTECH.toString());
        collectionModeMap.put(Source.SYSTEM.toString(), Source.SYSTEM.toString());
        return collectionModeMap;
    }

    @ModelAttribute("operators")
    public Set<User> loadCollectionOperators() {
        final String operatorDesignation = appConfigValueService.getAppConfigValueByDate(
                CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
                CollectionConstants.COLLECTION_DESIGNATIONFORCSCOPERATOR, new Date()).getValue();
        return assignmentService.getUsersByDesignations(operatorDesignation.split(","));
    }

    @ModelAttribute("status")
    public List<EgwStatus> loadStatus() {

        return egwStatusHibernateDAO.getStatusByModule(CollectionConstants.MODULE_NAME_RECEIPTHEADER);
    }

    @ModelAttribute("wards")
    public List<Boundary> wardBoundaries() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(PropertyTaxConstants.WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @RequestMapping(method = GET)
    public String search(final Model model) {
        model.addAttribute("currentDate", new Date());
        return "dailyWTCollection-search";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public List<CollectionDocument> searchCollection(@ModelAttribute final DailyWTCollectionReportSearch searchRequest) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        searchRequest.setUlbName(cityWebsite.getName());
        String receiptCount="receipt_count";
        final BoolQueryBuilder boolQuery = getCollectionFilterQuery(searchRequest);

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(WaterTaxConstants.COLLECTION_INDEX_NAME).withQuery(boolQuery)
                .addAggregation(AggregationBuilders.count(receiptCount).field("consumerCode"))
                .build();
        final Aggregations collCountAggr = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());

        final ValueCount aggr = collCountAggr.get(receiptCount);
        searchQuery = new NativeSearchQueryBuilder().withIndices(WaterTaxConstants.COLLECTION_INDEX_NAME).withQuery(boolQuery)
                .addAggregation(AggregationBuilders.count(receiptCount).field("consumerCode"))
                .withPageable(new PageRequest(0,
                        Long.valueOf(aggr.getValue()).intValue() == 0 ? 1 : Long.valueOf(aggr.getValue()).intValue()))
                .build();
        return  elasticsearchTemplate.queryForList(searchQuery, CollectionDocument.class);


    }

    private BoolQueryBuilder getCollectionFilterQuery(final DailyWTCollectionReportSearch searchRequest) {
         Date fromDate=null;
          Date toDate=null;
        if(searchRequest.getFromDate()!=null){
          fromDate = DateUtils.getDate(searchRequest.getFromDate(), WaterTaxConstants.DATE_FORMAT_YYYYMMDD);
        }
          if(searchRequest.getToDate()!=null){
          toDate = org.apache.commons.lang3.time.DateUtils.addDays(
                DateUtils.getDate(searchRequest.getToDate(), WaterTaxConstants.DATE_FORMAT_YYYYMMDD),
                1);
            }
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.matchQuery("billingService", WaterTaxConstants.COLLECION_BILLING_SERVICE_WTMS))
                .filter(QueryBuilders.rangeQuery("receiptDate").gte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(WaterTaxConstants.DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false));

        if (StringUtils.isNotBlank(searchRequest.getCollectionMode()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("channel", searchRequest.getCollectionMode()));
        if (StringUtils.isNotBlank(searchRequest.getCollectionOperator()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("receiptCreator", searchRequest.getCollectionOperator()));
        if (StringUtils.isNotBlank(searchRequest.getStatus()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("status", searchRequest.getStatus()));
        if (StringUtils.isNotBlank(searchRequest.getUlbName()))
           boolQuery = boolQuery.filter(QueryBuilders.matchQuery("cityName", searchRequest.getUlbName()));
        if (StringUtils.isNotBlank(searchRequest.getRevenueWard()))
            boolQuery = boolQuery.filter(QueryBuilders.matchQuery("revenueWard", searchRequest.getRevenueWard()));

        return boolQuery;
    }

}
