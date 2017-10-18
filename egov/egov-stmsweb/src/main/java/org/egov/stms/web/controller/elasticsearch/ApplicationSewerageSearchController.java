/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *      Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *      For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *      For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.stms.web.controller.elasticsearch;

import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.REVENUE_WARD;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.stms.elasticsearch.entity.SewerageConnSearchRequest;
import org.egov.stms.elasticsearch.entity.SewerageSearchResult;
import org.egov.stms.entity.es.SewerageIndex;
import org.egov.stms.service.es.SeweragePaginationService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageConnectionService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private SewerageConnectionService sewerageConnectionService;
    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;
    @Autowired
    private SeweragePaginationService seweragePaginationService;

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
        model.addAttribute("legacy", true);
        return "sewerageTaxSearch-form";
    }

    @ModelAttribute("revenueWards")
    public List<Boundary> revenueWardList() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                REVENUE_WARD, REVENUE_HIERARCHY_TYPE);
    }

    @RequestMapping(value = "/view/{consumernumber}/{assessmentnumber}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable final String consumernumber, @PathVariable final String assessmentnumber,
            final Model model, final ModelMap modelMap, final HttpServletRequest request) {
        SewerageApplicationDetails sewerageApplicationDetails = sewerageApplicationDetailsService
                .findByApplicationNumber(consumernumber);
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
    public DataTable<SewerageSearchResult> searchApplication(@ModelAttribute final SewerageConnSearchRequest searchRequest) {
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        if (cityWebsite != null)
            searchRequest.setUlbName(cityWebsite.getName());
        final List<SewerageSearchResult> searchResultFomatted = new ArrayList<>();
        final Pageable pageable = new PageRequest(searchRequest.pageNumber(),
                searchRequest.pageSize(), searchRequest.orderDir(), searchRequest.orderBy());
        Page<SewerageIndex> searchResult = seweragePaginationService.searchResultObj(searchRequest, searchResultFomatted);
        return new DataTable<>(new PageImpl<>(searchResultFomatted, pageable, searchResult.getTotalElements()),
                searchRequest.draw());
    }
}