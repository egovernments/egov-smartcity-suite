package org.egov.wtms.web.controller.elasticSearch;

import static java.util.Arrays.asList;

import java.util.List;

import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.search.elastic.entity.ApplicationIndex;
import org.egov.search.domain.Document;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.egov.search.service.SearchService;
import org.egov.wtms.elasticSearch.service.ApplicationSearchService;
import org.egov.wtms.web.contract.ApplicationSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/elastic/appSearch/")
public class ApplicationSearchController {

	private final ApplicationSearchService applicationSearchService;
	 private final SearchService searchService;
	@Autowired
	public ApplicationSearchController(final ApplicationSearchService applicationSearchService,final SearchService searchService) {
		this.applicationSearchService = applicationSearchService;
		this.searchService=searchService;
	}
	
	@ModelAttribute
    public ApplicationSearchRequest searchRequest() {
        return new ApplicationSearchRequest();
    }
	
	@RequestMapping(method = RequestMethod.GET)
	public String newSearchForm()
	{
	return "applicationSearch-newForm";
	}
	
	@ModelAttribute(value = "modulesList")
    public List<ApplicationIndex> findApplicationIndexModules() {
        return applicationSearchService.findApplicationIndexModules();
    } 
	@ModelAttribute(value = "applicationTypeList")
    public List<ApplicationIndex> findApplicationIndexApplicationTypes() {
        return applicationSearchService.findApplicationIndexApplicationTypes();
    } 
	
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public List<Document> searchComplaints(@ModelAttribute final ApplicationSearchRequest searchRequest) {
        final SearchResult searchResult = searchService.search(asList(Index.APPLICATION.toString()),
                asList(IndexType.APPLICATIONSEARCH.toString()), searchRequest.searchQuery(), searchRequest.searchFilters(),
                Sort.NULL, Page.NULL);
        return searchResult.getDocuments();
        
    }
}
