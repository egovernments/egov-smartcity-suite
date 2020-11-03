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
package org.egov.stms.web.controller.elasticsearch;

import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.stms.masters.entity.enums.SewerageConnectionStatus.ACTIVE;
import static org.egov.stms.masters.entity.enums.SewerageConnectionStatus.CLOSED;
import static org.egov.stms.masters.entity.enums.SewerageConnectionStatus.INPROGRESS;
import static org.egov.stms.utils.constants.SewerageTaxConstants.REVENUE_WARD;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WARDSECRETARY_SOURCE_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WARDSECRETARY_TRANSACTIONID_CODE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.WARDSECRETARY_WSPORTAL_REQUEST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.integration.service.ThirdPartyService;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.stms.elasticsearch.entity.SewerageConnSearchRequest;
import org.egov.stms.elasticsearch.entity.SewerageSearchResult;
import org.egov.stms.entity.es.SewerageIndex;
import org.egov.stms.service.es.SeweragePaginationService;
import org.egov.stms.transactions.entity.SewerageConnection;
import org.egov.stms.transactions.service.SewerageConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/existing/sewerage")
public class ApplicationSewerageSearchController {

    private static final String YES = "yes";
	@Autowired
    private CityService cityService;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private SewerageConnectionService sewerageConnectionService;
    @Autowired
    private SeweragePaginationService seweragePaginationService;
    @Autowired
    private ThirdPartyService thirdPartyService;

    @ModelAttribute("revenueWards")
    public List<Boundary> revenueWardList() {
        return boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                REVENUE_WARD, REVENUE_HIERARCHY_TYPE);
    }

    @GetMapping
    public String newSearchForm(final Model model) {
        SewerageConnSearchRequest sewerageConnSearchRequest = new SewerageConnSearchRequest();
        sewerageConnSearchRequest.setSearchType("searchConnection");
        model.addAttribute("sewerageConnSearchRequest", sewerageConnSearchRequest);
        return "sewerageTaxSearch-form";
    }

    @GetMapping("/legacysearch")
    public String newLeagacySearchForm(final Model model) {
        SewerageConnSearchRequest sewerageConnSearchRequest = new SewerageConnSearchRequest();
        sewerageConnSearchRequest.setSearchType("legacySearch");
        model.addAttribute("sewerageConnSearchRequest", sewerageConnSearchRequest);
        return "sewerageTaxSearch-form";
    }

    @PostMapping
    @ResponseBody
    public DataTable<SewerageSearchResult> searchApplication(@ModelAttribute final SewerageConnSearchRequest searchRequest, HttpServletRequest request) {
		boolean isWardSecretaryUser = YES.equalsIgnoreCase(searchRequest.getValidWardSecretaryRequest()) ? true : false;
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        if (cityWebsite != null)
            searchRequest.setUlbName(cityWebsite.getName());
        final List<SewerageSearchResult> searchResultFormatted = new ArrayList<>();
        if ("legacySearch".equals(searchRequest.getSearchType()))
            searchRequest.setLegacy(true);
        else if (searchRequest.getShscNumber() != null) {
            SewerageConnection sewerageConnection = sewerageConnectionService
                    .findByShscNumberAndStatusList(searchRequest.getShscNumber(),
                            Arrays.asList(INPROGRESS, ACTIVE, CLOSED));
            if(sewerageConnection != null)
            	searchRequest.setLegacy(sewerageConnection.getLegacy());
        }

        final Pageable pageable = new PageRequest(searchRequest.pageNumber(),
                searchRequest.pageSize(), searchRequest.orderDir(), searchRequest.orderBy());
        Page<SewerageIndex> searchResult = seweragePaginationService.searchResultObj(searchRequest, searchResultFormatted, isWardSecretaryUser);
        return new DataTable<>(new PageImpl<>(searchResultFormatted, pageable, searchResult.getTotalElements()),
                searchRequest.draw());
    }
    
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String searchForWardSecretary(Model model, HttpServletRequest request) {
		boolean wsPortalRequest = Boolean.valueOf(request.getParameter(WARDSECRETARY_WSPORTAL_REQUEST));
		if (!thirdPartyService.isValidWardSecretaryRequest(wsPortalRequest))
			throw new ApplicationRuntimeException("WS.001");

		boolean isWardSecretaryUser = thirdPartyService.isWardSecretaryRequest(wsPortalRequest);
		String wsTransactionId = request.getParameter("transactionId");
		String wsSource = request.getParameter("source");
		if (isWardSecretaryUser && ThirdPartyService.validateWardSecretaryRequest(wsTransactionId, wsSource))
			throw new ApplicationRuntimeException("WS.001");

		SewerageConnSearchRequest sewerageConnSearchRequest = new SewerageConnSearchRequest();
		sewerageConnSearchRequest.setSearchType("searchConnection");
		sewerageConnSearchRequest.setValidWardSecretaryRequest(YES);
		model.addAttribute("sewerageConnSearchRequest", sewerageConnSearchRequest);
		model.addAttribute(WARDSECRETARY_WSPORTAL_REQUEST, wsPortalRequest);
		model.addAttribute(WARDSECRETARY_TRANSACTIONID_CODE, wsTransactionId);
		model.addAttribute(WARDSECRETARY_SOURCE_CODE, wsSource);
		model.addAttribute("isAnonymousOrWardSecretaryUser", YES);
		return "sewerageTaxSearch-form";
	}
}