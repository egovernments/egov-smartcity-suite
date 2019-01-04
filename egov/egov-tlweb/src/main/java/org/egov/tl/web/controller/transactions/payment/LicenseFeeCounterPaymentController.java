/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2019  eGovernments Foundation
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

import org.egov.tl.entity.TradeLicense;
import org.egov.tl.service.TradeLicenseService;
import org.egov.tl.service.integration.LicenseBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static java.nio.charset.StandardCharsets.UTF_8;

@Controller
@RequestMapping("/license/fee/collect/{uid}")
public class LicenseFeeCounterPaymentController {

    @Autowired
    @Qualifier("tradeLicenseService")
    private TradeLicenseService tradeLicenseService;

    @Autowired
    @Qualifier("TLCollectionsInterface")
    private transient LicenseBillService licenseBillService;

    @ModelAttribute
    public TradeLicense tradeLicense(@PathVariable String uid) {
        return tradeLicenseService.getLicenseByUID(uid);
    }

    @GetMapping
    public String showCollectFee(@ModelAttribute TradeLicense tradeLicense, Model model) {
        if (tradeLicense == null) {
            model.addAttribute("error", "error.license.not.found");
        } else if (tradeLicense.isPaid()) {
            model.addAttribute("error", "error.license.fee.collected");
        } else {
            model.addAttribute("outstandingFee", licenseBillService.getPaymentFee(tradeLicense));
        }
        return "license-fee-details";
    }

    @PostMapping
    public String collectFee(@ModelAttribute TradeLicense tradeLicense, Model model) throws UnsupportedEncodingException {
        model.addAttribute("collectXML", URLEncoder.encode(licenseBillService.createLicenseBillXML(tradeLicense), UTF_8.name()));
        return "license-counter-payment";
    }
}
