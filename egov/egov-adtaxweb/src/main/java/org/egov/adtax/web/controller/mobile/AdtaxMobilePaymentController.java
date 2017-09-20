/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
 */
package org.egov.adtax.web.controller.mobile;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.egov.adtax.entity.Advertisement;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.service.AdvertisementService;
import org.egov.adtax.service.collection.AdvertisementBillServiceImpl;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.collection.integration.pgi.PaymentRequest;
import org.egov.ptis.client.integration.utils.SpringBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = { "/mobile" })
public class AdtaxMobilePaymentController {

    private static final String MESSAGE = "message";
    @Autowired
    private AdvertisementBillServiceImpl advertisementBillServiceImpl;
    @Autowired
    private AdvertisementDemandService advertisementDemandService;
    @Autowired
    private AdvertisementService advertisementService;

    private static final String PAYTAX_FORM = "mobilePayment-form";
    private static final String COLLECT_ADVTAX_ERROR = "collectAdvtax-error";

    /**
     * API to process payments from Mobile App
     *
     * @param model
     * @param advertisementNo
     * @param ulbCode
     * @param amountToBePaid
     * @param request
     * @return
     */
    @RequestMapping(value = "/payAdtax/{advertisementNo},{ulbCode},{amountToBePaid},{mobileNumber},{emailId}", method = RequestMethod.GET)
    public String collectTax(final Model model, @PathVariable final String advertisementNo,
            @PathVariable final String ulbCode, @PathVariable final BigDecimal amountToBePaid,
            @PathVariable final String mobileNumber, @PathVariable final String emailId,
            final HttpServletRequest request) {
        String redirectUrl = StringUtils.EMPTY;
        Advertisement advertisement = null;
        if (advertisementNo != null)
            advertisement = advertisementService.findByAdvertisementNumber(advertisementNo);

        if (amountToBePaid.compareTo(advertisementDemandService.getPendingTaxAmount(advertisement)) > 0) {
            model.addAttribute(MESSAGE, "msg.PaidAmount.greaterthan.Total");
            return COLLECT_ADVTAX_ERROR;
        }

        final BillInfoImpl billInfo = advertisementBillServiceImpl.getBillInfo(amountToBePaid, advertisement);
        if (billInfo != null) {
            final PaymentRequest paymentRequest = SpringBeanUtil.getCollectionIntegrationService()
                    .processMobilePayments(billInfo);
            if (paymentRequest != null) {
                for (final Object obj : paymentRequest.getRequestParameters().values())
                    redirectUrl = obj.toString();
                model.addAttribute("redirectUrl", redirectUrl);
            }
        } else {
            model.addAttribute(MESSAGE, "Bill data is incorrect");
            return COLLECT_ADVTAX_ERROR;
        }
        return PAYTAX_FORM;
    }

}
