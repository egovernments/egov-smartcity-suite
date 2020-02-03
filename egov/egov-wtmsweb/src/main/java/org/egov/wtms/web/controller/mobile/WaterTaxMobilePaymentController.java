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
package org.egov.wtms.web.controller.mobile;

import org.apache.commons.lang3.StringUtils;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.service.mobile.WaterTaxMobilePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;

import static org.egov.wtms.utils.constants.WaterTaxConstants.MOBILE_PAYMENT_INCORRECT_BILL_DATA;
import static org.egov.wtms.utils.constants.WaterTaxConstants.THIRD_PARTY_DEMAND_AMOUNT_GREATER_MSG;
import static org.egov.wtms.utils.constants.WaterTaxConstants.THIRD_PARTY_ERR_MSG_CONSUMER_NO_NOT_FOUND;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATER_VALIDATION;

@Controller
@RequestMapping(value = { "/public/mobile", "/mobile" })
public class WaterTaxMobilePaymentController {
	private static final Logger LOGGER = Logger.getLogger(WaterTaxMobilePaymentController.class);

    private static final String PAYTAX_FORM = "mobilePayment-form";
    private static final String ERROR_MSG = "errorMsg";
    private static final String ERROR_MSG_LIST="errorMsgList";

    @Autowired
    private WaterTaxMobilePaymentService waterTaxMobilePaymentService;
    
    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    /**
     * API to process payments from Mobile App
     *
     * @param model
     * @param consumerNo
     * @param ulbCode
     * @param amountToBePaid
     * @param request
     * @return
     * @throws ParseException
     */
	@RequestMapping(value = "/payWatertax/{consumerNo},{ulbCode},{amountToBePaid},{mobileNumber},{emailId}", method = RequestMethod.GET)
	public String collectTax(final Model model, @PathVariable final String consumerNo,
			@PathVariable final String ulbCode, @PathVariable final BigDecimal amountToBePaid,
			@PathVariable final String mobileNumber, @PathVariable final String emailId,
			final HttpServletRequest request) throws ParseException {
		LOGGER.info("WaterCharges Mobile payment request URL: consumerNo = " + consumerNo + ", ulbCode = " + ulbCode
				+ ", amountToBePaid = " + amountToBePaid + ", mobileNumber = " + mobileNumber + ", emailId = "
				+ emailId);

		String redirectUrl = "";
		WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
				.findByConsumerCodeAndConnectionStatus(consumerNo, ConnectionStatus.ACTIVE);
		BigDecimal totalTaxDue = BigDecimal.ZERO;
		if (waterConnectionDetails == null || !ApplicationThreadLocals.getCityCode().equals(ulbCode)) {
			model.addAttribute(ERROR_MSG, THIRD_PARTY_ERR_MSG_CONSUMER_NO_NOT_FOUND);
			return WATER_VALIDATION;
		}
		if (waterConnectionDetails != null) {
			totalTaxDue = waterConnectionDetailsService.getWaterTaxDueAmount(waterConnectionDetails);
			if (totalTaxDue != null && amountToBePaid.compareTo(totalTaxDue) > 0) {
				model.addAttribute(ERROR_MSG, THIRD_PARTY_DEMAND_AMOUNT_GREATER_MSG);
				return WATER_VALIDATION;
			}
		}

		try {
			redirectUrl = waterTaxMobilePaymentService.mobileBillPayment(consumerNo, amountToBePaid,
					waterConnectionDetails);
			if (StringUtils.isNotBlank(redirectUrl))
				model.addAttribute("redirectUrl", redirectUrl);
			else {
				model.addAttribute(ERROR_MSG, MOBILE_PAYMENT_INCORRECT_BILL_DATA);
				return WATER_VALIDATION;
			}
		} catch (final ValidationException e) {
			model.addAttribute(ERROR_MSG_LIST, e.getErrors());
			return WATER_VALIDATION;
		} catch (final Exception e) {

			return WATER_VALIDATION;
		}
		return PAYTAX_FORM;
	}
}
