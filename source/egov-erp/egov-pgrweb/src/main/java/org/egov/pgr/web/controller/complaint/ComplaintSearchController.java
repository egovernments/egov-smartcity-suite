package org.egov.pgr.web.controller.complaint;

import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.pgr.web.contract.ComplaintSearchRequest;
import org.egov.search.domain.Document;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.egov.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static java.util.Arrays.asList;

@Controller
@RequestMapping(value = "/complaint/citizen/anonymous/search")
public class ComplaintSearchController {

    private SearchService searchService;

    @Autowired
    public ComplaintSearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @ModelAttribute
    public ComplaintSearchRequest searchRequest() {
        return new ComplaintSearchRequest();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showSearchForm() {
        return "complaint-search";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public List<Document> searchComplaints(@ModelAttribute ComplaintSearchRequest searchRequest) {
        SearchResult searchResult = searchService.search(asList(Index.PGR.toString()),
                asList(IndexType.COMPLAINT.toString()),
                searchRequest.searchQuery(),
                searchRequest.searchFilters(),
                Sort.NULL,
                Page.NULL);
        return searchResult.getDocuments();
    }
}
