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

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.stms.masters.entity.enums.SewerageConnectionStatus;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.utils.SewerageTaxUtils;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

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
    private UserService userService;

    @RequestMapping(value = "/view/{applicationNumber}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final String applicationNumber, final HttpServletRequest request) {
        SewerageApplicationDetails details = null;
        details = sewerageApplicationDetailsService.findByApplicationNumberAndConnectionStatus(applicationNumber,
                SewerageConnectionStatus.ACTIVE);
        if (details == null)
            details = sewerageApplicationDetailsService.findByApplicationNumberAndConnectionStatus(applicationNumber,
                    SewerageConnectionStatus.CLOSED);
        if (details == null)
            details = sewerageApplicationDetailsService.findByApplicationNumber(applicationNumber);
        model.addAttribute("sewerageApplicationDetails", details);
//        model.addAttribute("connectionType",
//                sewerageApplicationDetailsService.getConnectionTypesMap().get(details.getConnectionType().name()));
//        model.addAttribute("feeDetails", connectionDemandService.getSplitFee(details));
        model.addAttribute("checkOperator", sewerageTaxUtils.checkCollectionOperatorRole());
        model.addAttribute("citizenRole", sewerageTaxUtils.getCitizenUserRole());
        final BigDecimal waterTaxDueforParent = sewerageApplicationDetailsService.getTotalAmount(details);
        model.addAttribute("waterTaxDueforParent", waterTaxDueforParent);
        model.addAttribute("mode", "search");
        return "application-view";
    }

    @ModelAttribute("cscUserRole")
    public String getCurrentUserRole() {
        String cscUserRole = "";
        User currentUser = null;

        if (ApplicationThreadLocals.getUserId() != null)
            currentUser = userService.getUserById(ApplicationThreadLocals.getUserId());
        else
            currentUser = securityUtils.getCurrentUser();

        for (final Role userrole : currentUser.getRoles())
            if (userrole.getName().equals(SewerageTaxConstants.ROLE_CSCOPERTAOR)) {
                cscUserRole = userrole.getName();
                break;
            }
        return cscUserRole;
    }

    @ModelAttribute("ulbUserRole")
    public String getUlbOperatorUserRole() {
        String userRole = "";
        User currentUser = null;
        if (ApplicationThreadLocals.getUserId() != null)
            currentUser = userService.getUserById(ApplicationThreadLocals.getUserId());
        else
            currentUser = securityUtils.getCurrentUser();
        for (final Role userrole : currentUser.getRoles())
            if (userrole.getName().equals(SewerageTaxConstants.ROLE_ULBOPERATOR)) {
                userRole = userrole.getName();
                break;
            }
        return userRole;
    }
}
