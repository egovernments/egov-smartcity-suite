package org.egov.ptis.web.controller.reports;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.DailyCollectionReportSearchVLT;
import org.egov.ptis.domain.service.report.ReportService;
import org.egov.search.domain.Document;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.egov.search.service.SearchService;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ReportService reportService;

    @Autowired
    private EgwStatusHibernateDAO egwStatushibernateDAO;

    @Autowired
    private CityService cityService;

    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private SearchService searchService;

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
    public List<Document> searchCollection(@ModelAttribute final DailyCollectionReportSearchVLT searchRequest) {

        SearchResult propertyIndexSearchResult = null;
        SearchResult collectionIndexSearchResult = null;
        List<String> consumerCodes = new ArrayList<String>();
        final List<Document> searchResultFomatted = new ArrayList<Document>(0);
        final Sort sortByAssessment = Sort.by().field("clauses.revwardname", SortOrder.ASC);
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        searchRequest.setUlbName(cityWebsite.getName());
        
        if (StringUtils.isNotBlank(searchRequest.getRevenueWard())) {
            propertyIndexSearchResult = searchService.search(asList(Index.APPTIS.toString()),
                    asList(IndexType.PTISDETAILS.toString()), searchRequest.searchQuery(),
                    searchRequest.searchProperyForWardFilters(), sortByAssessment, Page.NULL);
            for (Document PTDocument : propertyIndexSearchResult.getDocuments()) {
                Map<String, String> PTCommonMap = (Map<String, String>) PTDocument.getResource().get("common");
                consumerCodes.add(PTCommonMap.get("consumercode"));
            }
            searchRequest.setConsumerCode(consumerCodes);
            collectionIndexSearchResult = getCollectionIndex(searchRequest);
        } else {
            collectionIndexSearchResult = getCollectionIndex(searchRequest);
        }

        for (final Document collectionIndexDocument : collectionIndexSearchResult.getDocuments()) {
            searchResultFomatted.add(collectionIndexDocument);
        }
        return searchResultFomatted;
    }

    private SearchResult getCollectionIndex(final DailyCollectionReportSearchVLT searchRequest) {
        final Sort sortByReceiptDate = Sort.by().field("searchable.receiptdate", SortOrder.ASC);
        return searchService.search(asList(Index.COLLECTION.toString()),
                asList(IndexType.COLLECTION_BIFURCATION.toString()), searchRequest.searchQuery(),
                searchRequest.searchCollectionFilters(), sortByReceiptDate, Page.NULL);
    }
}
