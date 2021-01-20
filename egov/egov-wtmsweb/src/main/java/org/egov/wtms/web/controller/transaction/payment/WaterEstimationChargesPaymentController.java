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

package org.egov.wtms.web.controller.transaction.payment;

import static org.egov.wtms.masters.entity.enums.ConnectionStatus.ACTIVE;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.INPROGRESS;

import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.service.WaterEstimationChargesPaymentService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.web.validator.WaterEstimationChargesPaymentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.infra.utils.StringUtils.encodeURL;

@Controller
@RequestMapping(value = "/estimationcharges")
public class WaterEstimationChargesPaymentController {

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private WaterEstimationChargesPaymentValidator waterEstimationChargesPaymentValidator;

    @Autowired
    private WaterEstimationChargesPaymentService estimationChargesPaymentService;

    @GetMapping("/search")
    public String searchForm(Model model) {
        model.addAttribute("waterConnectionDetails", new WaterConnectionDetails());
        return "estimationpayment-searchform";
    }

    @GetMapping("/verification")
    public String showVerificationForm(@RequestParam(required = false) String applicationNumber,
                                       @RequestParam(required = false) String consumerNumber,
                                       WaterConnectionDetails waterConnectionDetails, BindingResult bindingResult,
                                       Model model) {

        if (waterEstimationChargesPaymentValidator.validate(applicationNumber, consumerNumber, bindingResult)) {
            model.addAttribute("message", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "estimationpayment-verificationform";
        }
        WaterConnectionDetails connectionDetails;
		if (isNotBlank(applicationNumber))
			connectionDetails = waterConnectionDetailsService.findByApplicationNumber(applicationNumber);
		else {
			connectionDetails = waterConnectionDetailsService
					.findByApplicationNumberOrConsumerCodeAndStatus(consumerNumber, INPROGRESS);
			if (connectionDetails == null)
				connectionDetails = waterConnectionDetailsService
						.findByApplicationNumberOrConsumerCodeAndStatus(consumerNumber, ACTIVE);
		}
        model.addAttribute("waterConnectionDetails", connectionDetails);
        model.addAttribute("estimationAmount", estimationChargesPaymentService.getEstimationDueAmount(connectionDetails));
        return "estimationpayment-verificationform";
    }

    @GetMapping("/collection/{applicationNumber}")
    public String collectEstimationCharges(@PathVariable String applicationNumber, Model model) {
        model.addAttribute("collectxml", encodeURL(estimationChargesPaymentService.generateBill(applicationNumber)));
        model.addAttribute("citizenrole", waterTaxUtils.getCitizenUserRole());
        return "collecttax-redirection";
    }
}
