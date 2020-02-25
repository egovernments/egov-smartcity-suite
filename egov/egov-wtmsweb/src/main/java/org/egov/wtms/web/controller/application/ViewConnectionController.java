/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.wtms.application.entity.EstimationNotice;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.EstimationNoticeService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.service.WaterEstimationChargesPaymentService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

import static org.egov.infra.config.core.ApplicationThreadLocals.getUserId;

@Controller
@RequestMapping(value = "/application")
public class ViewConnectionController {

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;
    @Autowired
    private ConnectionDemandService connectionDemandService;
    @Autowired
    private WaterEstimationChargesPaymentService estimationChargesPaymentService;
    @Autowired
    private WaterTaxUtils waterTaxUtils;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private EstimationNoticeService estimationNoticeService;

    @GetMapping(value = "/view/{applicationNumber}")
    public String view(@PathVariable String applicationNumber, Model model) {
        WaterConnectionDetails connectionDetails = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(
                applicationNumber,
                ConnectionStatus.ACTIVE);
        if (connectionDetails == null)
            connectionDetails = waterConnectionDetailsService.findByConsumerCodeAndConnectionStatus(applicationNumber,
                    ConnectionStatus.CLOSED);
        if (connectionDetails == null)
            connectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCode(applicationNumber);
		if (connectionDetails == null)
			connectionDetails = waterConnectionDetailsService
					.findByInactiveApplicationNumberOrConsumerCode(applicationNumber);
		
		EstimationNotice estimationNotice = null;
		if (connectionDetails != null)
			estimationNotice = estimationNoticeService.getNonHistoryEstimationNoticeForConnection(connectionDetails);
        model.addAttribute("applicationDocList",
                waterConnectionDetailsService.getApplicationDocForExceptClosureAndReConnection(connectionDetails));
        model.addAttribute("waterConnectionDetails", connectionDetails);
        model.addAttribute("connectionType",
                waterConnectionDetailsService.getConnectionTypesMap().get(connectionDetails.getConnectionType().name()));
        model.addAttribute("feeDetails", connectionDemandService.getSplitFee(connectionDetails));
        model.addAttribute("checkOperator", waterTaxUtils.checkCollectionOperatorRole());
        model.addAttribute("citizenRole", waterTaxUtils.getCitizenUserRole());
        final BigDecimal waterTaxDueforParent = waterConnectionDetailsService.getWaterTaxDueAmount(connectionDetails);
        model.addAttribute("waterTaxDueforParent", waterTaxDueforParent);
        BigDecimal estimationAmount = estimationChargesPaymentService.getEstimationDueAmount(connectionDetails);
        model.addAttribute("estimationAmount", estimationAmount.signum() >= 0 ? estimationAmount : BigDecimal.ZERO);
        model.addAttribute("estimationNumber", estimationNotice != null? estimationNotice.getEstimationNumber() : StringUtils.EMPTY);
        model.addAttribute("mode", "search");
        model.addAttribute("applicationHistory", waterConnectionDetailsService.getHistory(connectionDetails));
        model.addAttribute("citizenPortal", waterTaxUtils.isCitizenPortalUser(getUserId() == null
                ? securityUtils.getCurrentUser() : userService.getUserById(getUserId())));
        return "application-view";
    }

    @ModelAttribute("cscUserRole")
    public String getCurrentUserRole() {
        String cscUserRole = "";
        User currentUser;

        if (getUserId() == null)
            currentUser = securityUtils.getCurrentUser();
        else
            currentUser = userService.getUserById(getUserId());

        for (Role userrole : currentUser.getRoles())
            if (userrole.getName().equals(WaterTaxConstants.ROLE_CSCOPERTAOR)) {
                cscUserRole = userrole.getName();
                break;
            }
        return cscUserRole;
    }

    @ModelAttribute("ulbUserRole")
    public String getUlbOperatorUserRole() {
        String userRole = "";
        User currentUser;
        if (getUserId() == null)
            currentUser = securityUtils.getCurrentUser();
        else
            currentUser = userService.getUserById(getUserId());
        for (Role userrole : currentUser.getRoles())
            if (userrole.getName().equals(WaterTaxConstants.ROLE_ULBOPERATOR)) {
                userRole = userrole.getName();
                break;
            }
        return userRole;
    }
}
