/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.wtms.web.controller.search;

import static java.util.Arrays.asList;

import java.util.List;

import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.search.domain.Document;
import org.egov.search.domain.Page;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.egov.search.service.SearchService;
import org.egov.wtms.elasticSearch.entity.ConnectionSearchRequest;
import org.egov.wtms.utils.WaterTaxUtils;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/search/waterSearch/")
public class WaterTaxSearchController {

    private final SearchService searchService;
    private final CityService cityService;
    
    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    public WaterTaxSearchController(final SearchService searchService, final CityService cityService) {
        this.searchService = searchService;
        this.cityService = cityService;
    }

    @ModelAttribute
    public ConnectionSearchRequest searchRequest() {
        return new ConnectionSearchRequest();
    }

    /**
     * Assumptions: assuming appconfig Key "RolesForSearchWaterTaxConnection"
     * List Contals 1st Entry "CSC Operator" order by value asc
     * 
     * @return String if Logged in User is CSC Operattor
     */
    @ModelAttribute("cscUserRole")
    public String getCurrentUserRole() {
        final User currentUser = securityUtils.getCurrentUser();
        String cscUserRole = "";
        for (final Role userrole : currentUser.getRoles())
            if (userrole != null
            && userrole.getName().equals(
                            waterTaxUtils.getUserRolesForLoggedInUser().get(0) != null ? waterTaxUtils
                                    .getUserRolesForLoggedInUser().get(0).getValue() : "")) {
                cscUserRole = userrole.getName();
                break;
            }
        return cscUserRole;
    }

    /**
     * Assumptions: assuming appconfig Key "RolesForSearchWaterTaxConnection"
     * List Contals 4th Entry "ULB Operator" order by value asc
     * 
     * @return String if Logged in User is ULB Operattor
     */
    @ModelAttribute("ulbUserRole")
    public String getUlbOperatorUserRole() {
        String userRole = "";
        final User currentUser = securityUtils.getCurrentUser();
        for (final Role userrole : currentUser.getRoles())
            if (userrole != null
            && userrole.getName().equals(
                            waterTaxUtils.getUserRolesForLoggedInUser().get(3) != null ? waterTaxUtils
                                    .getUserRolesForLoggedInUser().get(3).getValue() : "")) {
                userRole = userrole.getName();
                break;
            }
        return userRole;
    }

    /**
     * Assumptions: assuming appconfig Key "RolesForSearchWaterTaxConnection"
     * List Contals 2nd Entry "Super User" order by value asc
     * 
     * @return String if Logged in User is SUPER USER
     */
    @ModelAttribute("superUserRole")
    public String getSuperUserRole() {
        String userRole = "";
        final User currentUser = securityUtils.getCurrentUser();
        for (final Role userrole : currentUser.getRoles())
            if (userrole != null
            && userrole.getName().equals(
                            waterTaxUtils.getUserRolesForLoggedInUser().get(2) != null ? waterTaxUtils
                                    .getUserRolesForLoggedInUser().get(2).getValue() : "")) {
                userRole = userrole.getName();
                break;
            }
        return userRole;
    }

    /**
     * Assumptions: assuming appconfig Key "RolesForSearchWaterTaxConnection"
     * List Contals 3th Entry "Water Tax Approver" order by value asc
     * 
     * @return String if Logged in User is Water Tax Approver
     */
    @ModelAttribute("approverUserRole")
    public String getApproverUserRole() {
        String userRole = "";
        final User currentUser = securityUtils.getCurrentUser();
        for (final Role userrole : currentUser.getRoles())
            if (userrole != null
            && userrole.getName().equals(
                            waterTaxUtils.getUserRolesForLoggedInUser().get(4) != null ? waterTaxUtils
                                    .getUserRolesForLoggedInUser().get(4).getValue() : "")) {
                userRole = userrole.getName();
                break;
            }
        return userRole;
    }

    /**
     * Assumptions: assuming appconfig Key "RolesForSearchWaterTaxConnection"
     * List Contals 5th Entry "Operator"
     * 
     * @return String if Logged in User is Operator
     */
    @ModelAttribute("operatorRole")
    public String getOperatorUserRole() {
        String userRole = "";
        final User currentUser = securityUtils.getCurrentUser();
        for (final Role userrole : currentUser.getRoles())
            if (userrole != null
            && userrole.getName().equals(
                            waterTaxUtils.getUserRolesForLoggedInUser().get(1) != null ? waterTaxUtils
                                    .getUserRolesForLoggedInUser().get(1).getValue() : "")) {
                userRole = userrole.getName();
                break;
            }
        return userRole;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String newSearchForm(final Model model) {
        return "waterTaxSearch-newForm";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public List<Document> searchConnection(@ModelAttribute final ConnectionSearchRequest searchRequest) {
        final City cityWebsite = cityService.getCityByURL(EgovThreadLocals.getDomainName());
        searchRequest.setUlbName(cityWebsite.getName());

        Sort sort = Sort.by().field("common.createdDate", SortOrder.DESC);
        final SearchResult searchResult = searchService.search(asList(Index.WATERTAX.toString()),
                asList(IndexType.CONNECTIONSEARCH.toString()), searchRequest.searchQuery(),
                searchRequest.searchFilters(), sort, Page.NULL);
        return searchResult.getDocuments();

    }

}
