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
package org.egov.wtms.web.controller.application;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.utils.EgovThreadLocals;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/application")
public class ViewConnectionController {

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;
    @Autowired
    private ConnectionDemandService connectionDemandService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/view/{applicationNumber}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final String applicationNumber, final HttpServletRequest request) {
        final WaterConnectionDetails details = waterConnectionDetailsService
                .findByApplicationNumberOrConsumerCode(applicationNumber);
        model.addAttribute("waterConnectionDetails", details);
        model.addAttribute("connectionType",
                waterConnectionDetailsService.getConnectionTypesMap().get(details.getConnectionType().name()));
        model.addAttribute("feeDetails", connectionDemandService.getSplitFee(details));
        model.addAttribute("mode", "search");
        return "application-view";
    }

    @ModelAttribute(value = "userRole")
    public Boolean checkCollectionOperatorRole() {
        Boolean isCSCOperator = false;
        final User userObj = userService.getUserById(EgovThreadLocals.getUserId());
        if (userObj != null) {
            for (final Role role : userObj.getRoles())
                if (role != null && role.getName().contains(WaterTaxConstants.CSCOPERTAORROLE)){
                    isCSCOperator = true;
                    break;
                }
            
        }
        return isCSCOperator;
    }
}
