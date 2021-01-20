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

import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.WaterTaxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.infra.config.core.ApplicationThreadLocals.getUserId;
import static org.egov.infra.config.core.ApplicationThreadLocals.setUserId;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.ACTIVE;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.INPROGRESS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.USERNAME_ANONYMOUS;

@Controller
@RequestMapping(value = "/application")
public class GenericBillGeneratorController {

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @GetMapping(value = "/generatebill/{applicationCode}")
    public String showCollectFeeForm(@PathVariable String applicationCode) {
        return "redirect:/application/collecttax-view?applicationCode=" + applicationCode;
    }

    @GetMapping(value = "/collecttax-view")
    public ModelAndView collectTaxView(@ModelAttribute WaterConnectionDetails waterConnectionDetails,
                                       HttpServletRequest request, Model model) {
        String consumerNumber = request.getParameter("applicationCode");
        if (isNotBlank(consumerNumber))
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
                    consumerNumber, ACTIVE);
        if (waterConnectionDetails == null)
            waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCode(consumerNumber);
        model.addAttribute("connectionType",
                waterConnectionDetailsService.getConnectionTypesMap().get(waterConnectionDetails.getConnectionType().name()));
        model.addAttribute("mode", "waterTaxCollection");
        model.addAttribute("checkOperator", waterTaxUtils.checkCollectionOperatorRole());
        model.addAttribute("feeDetails", connectionDemandService.getSplitFee(waterConnectionDetails));
        model.addAttribute("applicationDocList",
                waterConnectionDetailsService.getApplicationDocForExceptClosureAndReConnection(waterConnectionDetails));
        BigDecimal waterTaxDueAmount = waterConnectionDetailsService.getWaterTaxDueAmount(waterConnectionDetails);
        model.addAttribute("waterTaxDueforParent", waterTaxDueAmount.signum() >= 0 ? waterTaxDueAmount : ZERO);
        return new ModelAndView("application/collecttax-view", "waterConnectionDetails", waterConnectionDetails);
    }

    @PostMapping(value = "/generatebill/{applicationCode}")
    public String payTax(@PathVariable String applicationCode,
                         @RequestParam String applicationTypeCode, Model model) {
        WaterConnectionDetails waterconnectionDetails = null;
        AssessmentDetails assessmentDetails = null;
        if (isNotBlank(applicationCode))
        	waterconnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
            		applicationCode, INPROGRESS);
        if (waterconnectionDetails == null)
        	waterconnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCodeAndStatus(
            		applicationCode, ACTIVE);
		if (waterconnectionDetails != null)
			assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
					waterconnectionDetails.getConnection().getPropertyIdentifier(),
					PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        
        if (assessmentDetails == null)
            throw new ValidationException("invalid.property");
        else
            return generateBillAndRedirectToCollection(applicationCode, applicationTypeCode, model);
    }

    @GetMapping(value = "/generatebillOnline/{applicationCode}")
    public String payTaxOnline(@ModelAttribute WaterConnectionDetails waterConnectionDetails, @PathVariable String applicationCode,
                               @RequestParam String applicationTypeCode, Model model) {
        WaterConnectionDetails waterconnectionDetails = waterConnectionDetailsService
                .findByApplicationNumberOrConsumerCodeAndStatus(applicationCode, ACTIVE);
        AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterconnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        if (assessmentDetails == null)
            throw new ValidationException("invalid.property");
        else
            return generateBillAndRedirectToCollection(applicationCode, applicationTypeCode, model);

    }

    private String generateBillAndRedirectToCollection(String applicationCode, String applicationTypeCode, Model model) {
        if (getUserId() == null && securityUtils.getCurrentUser().getUsername().equals("anonymous"))
            setUserId(userService.getUserByUsername(USERNAME_ANONYMOUS).getId());
        model.addAttribute("collectxml", connectionDemandService.generateBill(applicationCode, applicationTypeCode));
        model.addAttribute("citizenrole", waterTaxUtils.getCitizenUserRole());
        return "collecttax-redirection";
    }

}