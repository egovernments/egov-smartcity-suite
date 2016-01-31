/* eGov suite of products aim to improve the internal efficiency,transparency,
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

package org.egov.mrs.web.controller.application.registration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.mrs.application.Constants;
import org.egov.mrs.domain.entity.SearchModel;
import org.egov.mrs.domain.entity.SearchResult;
import org.egov.mrs.domain.enums.ApplicationStatus;
import org.egov.mrs.domain.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles the Registration search
 *
 * @author nayeem
 *
 */
@Controller
@RequestMapping(value = "/registration")
public class SearchRegistrationController {

    private final RegistrationService registrationService;
    private final SecurityUtils securityUtils;

    @Autowired
    public SearchRegistrationController(final RegistrationService registrationService, final SecurityUtils securityUtils) {
        this.registrationService = registrationService;
        this.securityUtils = securityUtils;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String showSearch(@ModelAttribute final SearchModel searchModel, final Model model) {
        final Role collectionOperator = securityUtils.getCurrentUser().getRoles().stream()
                .filter(role -> role.getName().equalsIgnoreCase("Collection Operator")).collect(Collectors.toList()).get(0);
        final boolean isCollectionOperator = collectionOperator == null ? false : true;
        model.addAttribute("isCollectionOperator", isCollectionOperator);
        return "registration-search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public @ResponseBody Map<String, List<SearchResult>> search(@RequestBody final SearchModel searchModel) {
        final Map<String, List<SearchResult>> registrations = new HashMap<String, List<SearchResult>>();
        registrations.put("data", prepareSearchResult(searchModel));
        return registrations;
    }

    private List<SearchResult> prepareSearchResult(final SearchModel searchModel) {
        final List<SearchResult> results = new ArrayList<SearchResult>();
        final DateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_DDMMYYYY);
        registrationService.searchRegistration(searchModel).stream().forEach(registration -> {
            final SearchResult searchResult = new SearchResult();
            searchResult.setRegistrationId(registration.getId());
            searchResult.setRegistrationNo(registration.getRegistrationNo() == null ? "NA" : registration.getRegistrationNo());
            searchResult.setRegistrationDate(formatter.format(registration.getCreatedDate()));
            searchResult.setDateOfMarriage(formatter.format(registration.getDateOfMarriage()));
            searchResult.setWifeName(registration.getWife().getName().getFirstName());
            searchResult.setHusbandName(registration.getHusband().getName().getFirstName());
            searchResult.setCertificateIssued(registration.isCertificateIssued());
            searchResult.setStatus(registration.getStatus().name());
            searchResult.setFeePaid(registration.getFeePaid());

            if (!registration.isFeeCollected())
                if (registration.getStatus() == ApplicationStatus.Approved
                && registration.getCurrentState().getNextAction().equalsIgnoreCase("Fee Collection Pending"))
                    searchResult.setFeeCollectionPending(true);

            results.add(searchResult);
        });

        return results;

    }

}
