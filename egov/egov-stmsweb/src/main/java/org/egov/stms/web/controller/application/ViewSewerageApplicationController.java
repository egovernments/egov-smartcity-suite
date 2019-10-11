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
package org.egov.stms.web.controller.application;

import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.stms.masters.entity.enums.SewerageConnectionStatus;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageConnectionService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.egov.stms.utils.SewerageTaxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static org.egov.stms.utils.constants.SewerageTaxConstants.ROLE_CSCOPERTAOR;
import static org.egov.stms.utils.constants.SewerageTaxConstants.ROLE_ULBOPERATOR;

@Controller
@RequestMapping(value = "/application")
public class ViewSewerageApplicationController {

    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;

    @Autowired
    private SewerageTaxUtils sewerageTaxUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;

    @Autowired
    private SewerageConnectionService sewerageConnectionService;

    @GetMapping("/view/{applicationNumber}")
    public String view(final Model model, @PathVariable final String applicationNumber, final HttpServletRequest request) {
        SewerageApplicationDetails details = sewerageApplicationDetailsService.findByApplicationNumberAndConnectionStatus(applicationNumber,
                SewerageConnectionStatus.ACTIVE);
        if (details == null)
            details = sewerageApplicationDetailsService.findByApplicationNumberAndConnectionStatus(applicationNumber,
                    SewerageConnectionStatus.CLOSED);
        if (details == null)
            details = sewerageApplicationDetailsService.findByApplicationNumber(applicationNumber);
        model.addAttribute("sewerageApplicationDetails", details);
        model.addAttribute("checkOperator", sewerageTaxUtils.checkCollectionOperatorRole());
        model.addAttribute("citizenRole", sewerageTaxUtils.hasCitizenRole());
        model.addAttribute("waterTaxDueforParent", sewerageApplicationDetailsService.getTotalAmount(details));
        model.addAttribute("mode", "search");
        return "application-view";
    }

    @GetMapping("/view/{consumernumber}/{assessmentnumber}")
    public ModelAndView view(@PathVariable final String consumernumber, @PathVariable final String assessmentnumber,
                             final Model model, final ModelMap modelMap, final HttpServletRequest request) {
        SewerageApplicationDetails sewerageApplicationDetails = sewerageApplicationDetailsService
                .findByApplicationNumber(consumernumber);
        final AssessmentDetails propertyOwnerDetails = sewerageThirdPartyServices.getPropertyDetails(assessmentnumber,
                request);
        if (propertyOwnerDetails != null)
            modelMap.addAttribute("propertyOwnerDetails", propertyOwnerDetails);
        model.addAttribute("applicationHistory",
                sewerageApplicationDetailsService.populateHistory(sewerageApplicationDetails));
        model.addAttribute("documentNamesList",
                sewerageConnectionService.getSewerageApplicationDoc(sewerageApplicationDetails));
        return new ModelAndView("viewseweragedetails", "sewerageApplicationDetails", sewerageApplicationDetails);
    }

    @ModelAttribute("cscUserRole")
    public String getCurrentUserRole() {
        return securityUtils.getCurrentUser().hasRole(ROLE_CSCOPERTAOR) ? ROLE_ULBOPERATOR : "";
    }

    @ModelAttribute("ulbUserRole")
    public String getUlbOperatorUserRole() {
        return securityUtils.getCurrentUser().hasRole(ROLE_ULBOPERATOR) ? ROLE_ULBOPERATOR : "";
    }
}
