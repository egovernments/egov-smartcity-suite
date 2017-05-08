/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2016>  eGovernments Foundation
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

package org.egov.tl.web.controller;

import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.tl.entity.LicenseStatus;
import org.egov.tl.entity.dto.DemandnoticeForm;
import org.egov.tl.entity.dto.SearchForm;
import org.egov.tl.service.LicenseCategoryService;
import org.egov.tl.service.LicenseStatusService;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.utils.Constants;
import org.egov.tl.web.response.adaptor.DemandNoticeAdaptor;
import org.egov.tl.web.response.adaptor.SearchTradeResultHelperAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.tl.utils.Constants.LOCALITY;
import static org.egov.tl.utils.Constants.LOCATION_HIERARCHY_TYPE;
import static org.egov.tl.utils.Constants.STATUS_CANCELLED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
public class SearchTradeController {

    @Autowired
    private LicenseCategoryService licenseCategoryService;

    @Autowired
    private TradeLicenseService tradeLicenseService;

    @Autowired
    private LicenseStatusService licenseStatusService;

    @Autowired
    private BoundaryService boundaryService;

    @ModelAttribute("searchForm")
    public SearchForm searchForm() {
        return new SearchForm();
    }

    @GetMapping("/search/searchtrade-form")
    public String searchForm(final Model model) {
        model.addAttribute("categoryList", licenseCategoryService.getCategories());
        model.addAttribute("subCategoryList", Collections.emptyList());
        model.addAttribute("statusList", licenseStatusService.findAll());
        return "searchtrade-license";
    }

    @GetMapping(value = "/search/searchtrade-search", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String search(final SearchForm searchForm) {
        return new DataTable<>(tradeLicenseService.searchTradeLicense(searchForm), searchForm.draw())
                .toJson(SearchTradeResultHelperAdaptor.class);
    }

    @GetMapping(value = "/search/tradeLicense", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> searchautocomplete(@RequestParam final String searchParamValue,
                                           @RequestParam final String searchParamType) {
        return tradeLicenseService.getTradeLicenseForGivenParam(searchParamValue, searchParamType);
    }

    @GetMapping(value = "/search/demandnotice")
    public String searchFormforNotice(final Model model) {
        model.addAttribute("demandnoticesearchForm", new DemandnoticeForm());
        model.addAttribute("categoryList", licenseCategoryService.getCategories());
        model.addAttribute("subCategoryList", Collections.emptyList());
        final List<LicenseStatus> statuslist = licenseStatusService.findAll();
        statuslist.remove(licenseStatusService.getLicenseStatusByCode(STATUS_CANCELLED));
        model.addAttribute("statusList", statuslist);
        model.addAttribute("localityList", boundaryService
                .getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(LOCALITY, LOCATION_HIERARCHY_TYPE));
        model.addAttribute("wardList", boundaryService.getBoundariesByBndryTypeNameAndHierarchyTypeName(
                Constants.REVENUE_WARD, Constants.REVENUE_HIERARCHY_TYPE));
        return "search-demandnotice";
    }

    @GetMapping(value = "/search/demandnotice-result", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchResult(@ModelAttribute final DemandnoticeForm demandnoticeForm) throws IOException {
        return new StringBuilder("{ \"data\":")
                .append(toJSON(tradeLicenseService.getLicenseDemandNotices(demandnoticeForm),
                        DemandnoticeForm.class, DemandNoticeAdaptor.class))
                .append("}").toString();
    }

}
