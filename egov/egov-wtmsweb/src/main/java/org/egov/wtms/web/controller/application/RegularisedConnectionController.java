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

package org.egov.wtms.web.controller.application;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.wtms.application.entity.RegularisedConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.RegularisedConnectionService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGULARIZE_CONNECTION;

@Controller
@RequestMapping(value = "/application")
public class RegularisedConnectionController extends GenericConnectionController {

    private static final String CURRENTUSER = "currentUser";
    private static final String STATETYPE = "stateType";

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private RegularisedConnectionService regularisedConnectionService;

    @GetMapping("/regulariseconnection-form/{id}")
    public String getApplicationForm(@ModelAttribute final WaterConnectionDetails waterConnectionDetails,
            @PathVariable final String id, final Model model,
            final HttpServletRequest request) {
        RegularisedConnection regularisedConnection = null;
        if (isNotBlank(id))
            regularisedConnection = regularisedConnectionService.findById(Long.valueOf(id));
        waterConnectionDetails.setApplicationDate(new Date());
        waterConnectionDetails.setConnectionStatus(ConnectionStatus.INPROGRESS);
        waterConnectionDetails.setApplicationType(applicationTypeService.findByCode(REGULARIZE_CONNECTION));
        if (regularisedConnection != null)
            model.addAttribute("propertyId", regularisedConnection.getPropertyIdentifier());
        model.addAttribute("allowIfPTDueExists", waterTaxUtils.isNewConnectionAllowedIfPTDuePresent());
        final WorkflowContainer workFlowContainer = new WorkflowContainer();
        prepareWorkflow(model, waterConnectionDetails, workFlowContainer);
        model.addAttribute(CURRENTUSER, waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
        model.addAttribute(STATETYPE, waterConnectionDetails.getClass().getSimpleName());
        model.addAttribute("documentName", waterTaxUtils.documentRequiredForBPLCategory());
        model.addAttribute("typeOfConnection", WaterTaxConstants.NEWCONNECTION);
        model.addAttribute("citizenPortalUser", waterTaxUtils.isCitizenPortalUser(securityUtils.getCurrentUser()));
        model.addAttribute("isAnonymousUser", waterTaxUtils.isAnonymousUser(securityUtils.getCurrentUser()));
        return "regulariseconnection-form";
    }

    @PostMapping("/regulariseconnection-form")
    public String createRegularisedConnection(@ModelAttribute final WaterConnectionDetails waterConnectionDetails,
            final BindingResult errors, final Model model) {

        return "";
    }
}
