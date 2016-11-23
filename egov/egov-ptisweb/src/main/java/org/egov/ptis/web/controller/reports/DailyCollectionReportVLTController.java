package org.egov.ptis.web.controller.reports;

import static org.egov.ptis.constants.PropertyTaxConstants.COLLECION_BILLING_SERVICE_VLT;
import static org.egov.ptis.constants.PropertyTaxConstants.COLLECTION_INDEX_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.DATEFORMATTER_YYYY_MM_DD;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_FORMAT_YYYYMMDD;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
import org.egov.ptis.domain.entity.property.DailyCollectionReportSearchVLT;
import org.egov.ptis.domain.service.report.ReportService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.jboss.logging.Logger;
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
@RequestMapping(value = "/report/dailyCollectionVLT")
public class DailyCollectionReportVLTController {

    private static final String DAILY_COLLECTION_FORM = "dailyCollectionVLT-form";
    private static final Logger LOGGER = Logger.getLogger(DailyCollectionReportVLTController.class);
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
        final DailyCollectionReportSearchVLT dailyCollectionReportResutVLT = new DailyCollectionReportSearchVLT();
        model.addAttribute("dailyCollectionReportResutVLT", dailyCollectionReportResutVLT);
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
                .filter(QueryBuilders.matchQuery("billingService", COLLECION_BILLING_SERVICE_VLT))
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
