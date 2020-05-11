/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2019  eGovernments Foundation
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

package org.egov.ptis.web.controller.search;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATIONTYPES;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_STATUS_LIST;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import java.util.ArrayList;
import java.util.List;

import org.egov.ptis.actions.reports.SearchWriteoffCourtCaseAdapter;
import org.egov.ptis.domain.entity.property.view.SearchCourtCaseWriteoffRequest;
import org.egov.ptis.domain.service.search.SearchCourtCaseWriteOffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/search/courtcasewriteoffapplication")
public class SearchWriteoffCourtCaseApplicationController {
    public static final String APPLICATION_VIEW_URL = "/ptis/view/viewProperty-viewForm.action?applicationNo=%s&applicationType=%s";
    @Autowired
    private SearchCourtCaseWriteOffService searchCourtCaseWriteOffService;

    @ModelAttribute("statusList")
    public List<String> getStatusList() {
        return APPLICATION_STATUS_LIST;
    }

    @ModelAttribute("serviceTypes")
    public List<String> getApplicationTypes() {
        return APPLICATIONTYPES;
    }

    @ModelAttribute
    public SearchCourtCaseWriteoffRequest searchRequest() {
        return new SearchCourtCaseWriteoffRequest();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getSearchCourtCaseApplication(final Model model) {
        model.addAttribute("SearchCourtCaseWriteoffRequest", new SearchCourtCaseWriteoffRequest());
        return "searchwriteoffcourtcaseapplication";
    }

    @RequestMapping(value = "/result", method = RequestMethod.POST, produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getSearchResultSet(@ModelAttribute final SearchCourtCaseWriteoffRequest searchCourtCaseWriteoffRequest) {
        List<SearchCourtCaseWriteoffRequest> finalResult = new ArrayList<>();
        finalResult = searchCourtCaseWriteOffService.getResult(searchCourtCaseWriteoffRequest);
        return new StringBuilder("{ \"data\":").append(toJSON(finalResult, SearchCourtCaseWriteoffRequest.class,
                SearchWriteoffCourtCaseAdapter.class)).append("}").toString();
    }

}
