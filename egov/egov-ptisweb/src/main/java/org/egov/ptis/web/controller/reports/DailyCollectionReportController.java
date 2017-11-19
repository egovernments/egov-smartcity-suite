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
package org.egov.ptis.web.controller.reports;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.entity.es.CollectionDocument;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.DailyCollectionReportSearch;
import org.egov.ptis.domain.service.report.ReportService;
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

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.egov.ptis.constants.PropertyTaxConstants.COLLECION_BILLING_SERVICE_PT;
import static org.egov.ptis.constants.PropertyTaxConstants.COLLECTION_INDEX_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.DATEFORMATTER_YYYY_MM_DD;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_YYYYMMDD;

@Controller
@RequestMapping(value = "/report/dailyCollection")
public class DailyCollectionReportController {

    private static final String DAILY_COLLECTION_FORM = "dailyCollection-form";

    @Autowired
    private ReportService reportService;

    @Autowired
    private EgwStatusHibernateDAO egwStatushibernateDAO;

    @Autowired
    private CityService cityService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @ModelAttribute
    public void getReportModel(final Model model) {
        final DailyCollectionReportSearch dailyCollectionReportResut = new DailyCollectionReportSearch();
        model.addAttribute("dailyCollectionReportResut", dailyCollectionReportResut);
    }

    @ModelAttribute("operators")
    public Set<User> loadCollectionOperators() {
        return reportService.getCollectionOperators();
    }

    @ModelAttribute("status")
    public List<EgwStatus> loadStatus() {
        return egwStatushibernateDAO.getStatusByModule("ReceiptHeader");
    }

    @ModelAttribute("wards")
    public List<Boundary> wardBoundaries() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(PropertyTaxConstants.WARD,
                PropertyTaxConstants.REVENUE_HIERARCHY_TYPE);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String seachForm(final Model model) {
        model.addAttribute("currDate", new Date());
        model.addAttribute("collectionMode", Source.values());
        return DAILY_COLLECTION_FORM;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public List<CollectionDocument> searchCollection(@ModelAttribute final DailyCollectionReportSearch searchRequest) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        searchRequest.setUlbName(cityWebsite.getName());
        final BoolQueryBuilder boolQuery = getQueryBasedOnInput(searchRequest);

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME).withQuery(boolQuery)
                .addAggregation(AggregationBuilders.count("receipt_count").field("consumerCode"))
                .build();
        final Aggregations collCountAggr = elasticsearchTemplate.query(searchQuery, response -> response.getAggregations());

        final ValueCount aggr = collCountAggr.get("receipt_count");
        searchQuery = new NativeSearchQueryBuilder().withIndices(COLLECTION_INDEX_NAME).withQuery(boolQuery)
                .addAggregation(AggregationBuilders.count("receipt_count").field("consumerCode"))
                .withPageable(new PageRequest(0,
                        Long.valueOf(aggr.getValue()).intValue() == 0 ? 1 : Long.valueOf(aggr.getValue()).intValue()))
                .build();
        final List<CollectionDocument> collIndexList = elasticsearchTemplate.queryForList(searchQuery, CollectionDocument.class);

        return collIndexList;

    }

    private BoolQueryBuilder getQueryBasedOnInput(final DailyCollectionReportSearch searchRequest) {
        final Date fromDate = DateUtils.getDate(searchRequest.getFromDate(), DATE_FORMAT_YYYYMMDD);
        final Date toDate = org.apache.commons.lang3.time.DateUtils.addDays(
                DateUtils.getDate(searchRequest.getToDate(), DATE_FORMAT_YYYYMMDD),
                1);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.matchQuery("billingService", COLLECION_BILLING_SERVICE_PT))
                .filter(QueryBuilders.rangeQuery("receiptDate").gte(DATEFORMATTER_YYYY_MM_DD.format(fromDate))
                        .lte(DATEFORMATTER_YYYY_MM_DD.format(toDate)).includeUpper(false));
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