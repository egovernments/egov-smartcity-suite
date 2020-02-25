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
package org.egov.tl.web.controller.transactions.payment;

import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.integration.service.ThirdPartyService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.tl.entity.TradeLicense;
import org.egov.tl.entity.contracts.OnlineSearchRequest;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.service.integration.LicenseBillService;
import org.egov.tl.web.response.adaptor.OnlineSearchResponseAdaptor;
import org.egov.tl.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import static org.egov.infra.utils.JsonUtils.toJSON;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
@RequestMapping("/pay/online")
public class LicenseFeeOnlinePaymentController {

    @Autowired
    @Qualifier("TLCollectionsInterface")
    private LicenseBillService licenseBillService;

    @Autowired
    @Qualifier("tradeLicenseService")
    private TradeLicenseService tradeLicenseService;

    @Autowired
    protected transient SecurityUtils securityUtils;
    
    @Autowired
    private transient ThirdPartyService thirdPartyService;

    @ModelAttribute("onlineSearchRequest")
    public OnlineSearchRequest onlineSearchRequest() {
        return new OnlineSearchRequest();
    }

    @GetMapping("{id}")
    public String showPaymentForm(@PathVariable final Long id, Model model) throws IOException {
        final TradeLicense license = tradeLicenseService.getLicenseById(id);
        if (license.isPaid()) {
            model.addAttribute("paymentdone", "License Fee already collected");
            return "license-online-payment";
        }

        model.addAttribute("collectXML", URLEncoder.encode(licenseBillService.createLicenseBillXML(license), "UTF-8"));
        return "license-online-payment";
    }

    @GetMapping
    public String searchForPayment(final Model model, final HttpServletRequest request) {
        return searchPaymentForm(model, request);
    }
    
    @RequestMapping("/form")
    public String searchForPaymentForm(final Model model, final HttpServletRequest request) {
        String wsPortalRequest = request.getParameter(Constants.WARDSECRETARY_WSPORTAL_REQUEST);
        if (!thirdPartyService.isValidWardSecretaryRequest(wsPortalRequest != null && Boolean.valueOf(wsPortalRequest))) {
            throw new ApplicationRuntimeException("WS.002");
        }
        return searchPaymentForm(model, request);
    }

    /**
     * @param model
     * @param request
     * @return
     */
    public String searchPaymentForm(final Model model, final HttpServletRequest request) {
        String wsPortalRequest = request.getParameter(Constants.WARDSECRETARY_WSPORTAL_REQUEST);
        
        if (thirdPartyService.isWardSecretaryRequest(wsPortalRequest != null && Boolean.valueOf(wsPortalRequest))) {
            String wsTransactionId = request.getParameter(Constants.WARDSECRETARY_TRANSACTIONID_CODE);
            String wsSource = request.getParameter(Constants.WARDSECRETARY_SOURCE_CODE);
            if (ThirdPartyService.validateWardSecretaryRequest(wsTransactionId, wsSource))
                throw new ApplicationRuntimeException("WS.001");
            else {
                model.addAttribute(Constants.WARDSECRETARY_TRANSACTIONID_CODE, wsTransactionId);
                model.addAttribute(Constants.WARDSECRETARY_SOURCE_CODE, wsSource);
                model.addAttribute(Constants.WARDSECRETARY_WSPORTAL_REQUEST, wsPortalRequest);
            }
        }
        return "searchtrade-licenseforpay";
    }

    @PostMapping(produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchLicense(OnlineSearchRequest onlineSearchRequest) {
        return new StringBuilder("{ \"data\":").append(toJSON(tradeLicenseService.onlineSearchTradeLicense(onlineSearchRequest),
                OnlineSearchRequest.class, OnlineSearchResponseAdaptor.class)).append("}").toString();
    }
}
