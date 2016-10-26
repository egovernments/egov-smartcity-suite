/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.stms.web.controller.elasticSearch;

import org.apache.log4j.Logger;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.stms.elasticSearch.entity.SewerageConnSearchRequest;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageConnectionService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.egov.stms.utils.SewerageTaxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.REVENUE_WARD;

@Controller
@RequestMapping(value = "/existing/sewerage")
public class ApplicationSewerageSearchController {

    private static final Logger LOGGER = Logger.getLogger(ApplicationSewerageSearchController.class);
    @Autowired
    private CityService cityService;
    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;
    @Autowired
    private SewerageConnectionService sewerageConnectionService;
    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;

    @ModelAttribute
    public SewerageConnSearchRequest searchRequest() {
        return new SewerageConnSearchRequest();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String newSearchForm(final Model model) {
        return "sewerageTaxSearch-form";
    }

    @ModelAttribute("revenueWards")
    public List<Boundary> revenueWardList() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                REVENUE_WARD, REVENUE_HIERARCHY_TYPE);
    }

    @RequestMapping(value = "/view/{consumernumber}/{assessmentnumber}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable final String consumernumber, @PathVariable final String assessmentnumber,
                             final Model model, final ModelMap modelMap,
                             @ModelAttribute SewerageApplicationDetails sewerageApplicationDetails, final HttpServletRequest request) {

        if (consumernumber != null)
            sewerageApplicationDetails = sewerageApplicationDetailsService.findByApplicationNumber(consumernumber);
        final AssessmentDetails propertyOwnerDetails = sewerageThirdPartyServices.getPropertyDetails(assessmentnumber,
                request);
        if (propertyOwnerDetails != null)
            modelMap.addAttribute("propertyOwnerDetails", propertyOwnerDetails);
        model.addAttribute("applicationHistory",
                sewerageApplicationDetailsService.getHistory(sewerageApplicationDetails));
        model.addAttribute("documentNamesList",
                sewerageConnectionService.getSewerageApplicationDoc(sewerageApplicationDetails));
        return new ModelAndView("viewseweragedetails", "sewerageApplicationDetails", sewerageApplicationDetails);
    }

    /*@SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public List<SewerageSearchResult> searchApplication(@ModelAttribute final SewerageConnSearchRequest searchRequest) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        searchRequest.setUlbName(cityWebsite.getName());
        final Sort sort = Sort.by().field(SEARCHABLE_SHSCNO, SortOrder.DESC);
        final SearchResult searchResult = searchService.search(asList(Index.SEWARAGE.toString()),
                asList(IndexType.SEWARAGESEARCH.toString()), searchRequest.searchQuery(),
                searchRequest.searchFilters(), sort, Page.NULL);

        final List<String> roleList = new ArrayList<String>();
        for (final Role userrole : sewerageTaxUtils.getLoginUserRoles())
            roleList.add(userrole.getName());

        final List<SewerageSearchResult> searchResultFomatted = new ArrayList<SewerageSearchResult>(0);
        SewerageApplicationDetails sewerageApplicationDetails = new SewerageApplicationDetails();
        for (final Document document : searchResult.getDocuments()) {
            final Map<String, String> actionMap = new HashMap<>();
            final Map<String, String> searchableObjects = (Map<String, String>) document.getResource()
                    .get("searchable");
            if (searchableObjects != null) {
                final String consumernumber = searchableObjects.get("consumernumber");
                sewerageApplicationDetails = sewerageApplicationDetailsService.findByApplicationNumber(consumernumber);
                final SewerageSearchResult searchActions = SewerageActionDropDownUtil.getSearchResultWithActions(
                        roleList, searchableObjects.get("status"), sewerageApplicationDetails);
                if (searchActions == null) {
                    return searchResultFomatted;
                }
                    for (final Map.Entry<String, String> entry : searchActions.getActions().entrySet())
                        if (!entry.getValue().equals(COLLECTDONATIONCHARHGES))
                            actionMap.put(entry.getKey(), entry.getValue());
                    searchActions.setActions(actionMap);
                    searchActions.setDocument(document);
                    searchResultFomatted.add(searchActions);
            }
        }
        return searchResultFomatted;
    }*/

}