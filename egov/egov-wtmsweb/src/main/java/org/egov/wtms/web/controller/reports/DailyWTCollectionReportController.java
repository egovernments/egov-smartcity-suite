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
package org.egov.wtms.web.controller.reports;

import static java.util.Arrays.asList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.egov.collection.constants.CollectionConstants;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.search.domain.Document;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.egov.search.service.SearchService;
import org.egov.wtms.application.service.DailyWTCollectionReportSearch;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
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
    private SearchService searchService;
    
    @Autowired
    public EgwStatusHibernateDAO egwStatusHibernateDAO;
    
    @Autowired
    public AssignmentService assignmentService;

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
    	final String operatorDesignation = appConfigValueService
                .getAppConfigValueByDate(CollectionConstants.MODULE_NAME_COLLECTIONS_CONFIG,
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
    public List<Document> searchCollection(@ModelAttribute final DailyWTCollectionReportSearch searchRequest) {

    	SearchResult consumerIndexSearchResult = null;
        SearchResult collectionIndexSearchResult = null;
        List<String> consumerCodes = new ArrayList<String>();
        final List<Document> searchResultFomatted = new ArrayList<Document>(0);
        final Sort sortByAssessment = Sort.by().field("clauses.ward", SortOrder.ASC);
        final City cityWebsite = cityService.getCityByURL(EgovThreadLocals.getDomainName());
        searchRequest.setUlbName(cityWebsite.getName());
        
        if (StringUtils.isNotBlank(searchRequest.getRevenueWard())) {
        	consumerIndexSearchResult = searchService.search(asList(Index.WATERCHARGES.toString()),
                    asList(IndexType.CONNECTIONSEARCH.toString()), searchRequest.searchQuery(),
                    searchRequest.searchConnectionForWardFilters(), sortByAssessment, Page.NULL);
            for (Document consumerDocument : consumerIndexSearchResult.getDocuments()) {
                Map<String, String> consumerCommonMap = (Map<String, String>) consumerDocument.getResource().get("clauses");
                consumerCodes.add(consumerCommonMap.get("consumercode"));
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

    private SearchResult getCollectionIndex(final DailyWTCollectionReportSearch searchRequest) {
        final Sort sortByReceiptDate = Sort.by().field("searchable.receiptdate", SortOrder.ASC);
        return searchService.search(asList(Index.COLLECTION.toString()),
                asList(IndexType.COLLECTION_BIFURCATION.toString()), searchRequest.searchQuery(),
                searchRequest.searchCollectionFilters(), sortByReceiptDate, Page.NULL);
    }
}
