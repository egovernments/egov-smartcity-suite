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

import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.COLLECTDONATIONCHARHGES;
import static org.egov.stms.utils.constants.SewerageTaxConstants.REVENUE_WARD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.stms.elasticSearch.entity.SewerageConnSearchRequest;
import org.egov.stms.elasticSearch.entity.SewerageSearchResult;
import org.egov.stms.entity.es.SewerageIndex;
import org.egov.stms.service.es.SewerageIndexService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageConnectionService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.egov.stms.utils.SewerageActionDropDownUtil;
import org.egov.stms.utils.SewerageTaxUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/existing/sewerage")
public class ApplicationSewerageSearchController {

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

    @Autowired
    private SewerageIndexService sewerageIndexService;

    @ModelAttribute
    public SewerageConnSearchRequest searchRequest() {
        return new SewerageConnSearchRequest();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String newSearchForm(final Model model) {
        return "sewerageTaxSearch-form";
    }

    @RequestMapping(value = "/legacysearch", method = RequestMethod.GET)
    public String newLeagacySearchForm(final Model model) {
        model.addAttribute("isLegacy", true);
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

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public List<SewerageSearchResult> searchApplication(@ModelAttribute final SewerageConnSearchRequest searchRequest) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        if (cityWebsite != null)
            searchRequest.setUlbName(cityWebsite.getName());

        final BoolQueryBuilder boolQuery = sewerageIndexService.getQueryFilter(searchRequest);
        final List<SewerageSearchResult> searchResultFomatted = new ArrayList<>();
        List<SewerageIndex> searchResult;
        SewerageApplicationDetails sewerageApplicationDetails = null;
        SewerageSearchResult searchActions = null;
        final Map<String, String> actionMap = new HashMap<>();
        final List<String> roleList = new ArrayList<>();
        for (final Role userRole : sewerageTaxUtils.getLoginUserRoles())
            roleList.add(userRole.getName());
        final FieldSortBuilder sort = new FieldSortBuilder("shscNumber").order(SortOrder.DESC);
        searchResult = sewerageIndexService.getSearchResultByBoolQuery(boolQuery, sort);
        for (final SewerageIndex sewerageIndexObject : searchResult) {
            final SewerageSearchResult searchResultObject = new SewerageSearchResult();
            searchResultObject.setApplicationNumber(sewerageIndexObject.getApplicationNumber());
            searchResultObject.setAssessmentNumber(sewerageIndexObject.getPropertyIdentifier());
            searchResultObject.setShscNumber(sewerageIndexObject.getShscNumber());
            searchResultObject.setApplicantName(sewerageIndexObject.getConsumerName());
            searchResultObject.setApplicationType(sewerageIndexObject.getApplicationType());
            searchResultObject.setPropertyType(sewerageIndexObject.getPropertyType().replace("_", " "));
            searchResultObject.setRevenueWard(sewerageIndexObject.getWard());
            searchResultObject.setAddress(sewerageIndexObject.getAddress());
            searchResultObject.setApplicationStatus(sewerageIndexObject.getApplicationStatus());

            if (sewerageIndexObject.getApplicationNumber() != null)
                sewerageApplicationDetails = sewerageApplicationDetailsService
                        .findByApplicationNumber(sewerageIndexObject.getApplicationNumber());
            if (sewerageApplicationDetails != null)
                searchActions = SewerageActionDropDownUtil.getSearchResultWithActions(roleList,
                        sewerageIndexObject.getApplicationStatus(),
                        sewerageApplicationDetails);
            if (searchActions != null && searchActions.getActions() != null)
                getActions(searchActions, actionMap);
            searchResultObject.setActions(actionMap);
            searchResultFomatted.add(searchResultObject);
        }
        return searchResultFomatted;
    }

    private void getActions(final SewerageSearchResult searchActions, final Map<String, String> actionMap) {
        for (final Map.Entry<String, String> entry : searchActions.getActions().entrySet())
            if (!entry.getValue().equals(COLLECTDONATIONCHARHGES))
                actionMap.put(entry.getKey(), entry.getValue());
    }

}