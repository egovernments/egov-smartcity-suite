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
package org.egov.adtax.web.controller.citizen;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.Serializable;
import java.util.List;

import org.egov.adtax.entity.Advertisement;
import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.entity.Agency;
import org.egov.adtax.entity.enums.AgencyStatus;
import org.egov.adtax.search.contract.HoardingSearch;
import org.egov.adtax.service.AdvertisementDemandService;
import org.egov.adtax.service.AdvertisementPermitDetailService;
import org.egov.adtax.service.AdvertisementReportService;
import org.egov.adtax.service.AgencyService;
import org.egov.adtax.service.collection.AdvertisementBillServiceImpl;
import org.egov.adtax.service.collection.AdvertisementBillable;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.infra.persistence.utils.SequenceNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/citizen")
public class AdtaxCitizenSupportController {
    private static final String COLLECT_ADVTAX_ERROR = "collectAdvtax-error";

    private static final String MESSAGE = "message";

    private static final String ONLINEPAYMENT_REDIRECTION = "onlinepayment-redirection";

    private static final String CITIZEN_CITIZEN_ONLINE_DCBVIEW = "citizen/citizen-online-dcbview";

    private static final String SEARCH_ADTAX_ONLINE_PAYMENT = "search-adtax-online-payment";
    private static final String ADVERTISEMENT_BILLNUMBER = "SEQ_advertisementbill_NUMBER";

    @Autowired
    private AgencyService agencyService;
    @Autowired
    private AdvertisementPermitDetailService advertisementPermitDetailService;
    @Autowired
    private AdvertisementReportService hoardingReportService;
    @Autowired
    private AdvertisementBillable advertisementBillable;
    @Autowired
    private AdvertisementBillServiceImpl advertisementBillServiceImpl;
    @Autowired
    private SequenceNumberGenerator sequenceNumberGenerator;
    @Autowired
    private AdvertisementDemandService advertisementDemandService;

    @RequestMapping(value = "/search/search-advertisement", method = GET)
    public String searchHoardingForm(@ModelAttribute final HoardingSearch hoardingSearch) {
        return SEARCH_ADTAX_ONLINE_PAYMENT;
    }

    @RequestMapping(value = "/search/active-agencies", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Agency> findActiveAgencies(@RequestParam final String name) {
        return agencyService.findAllActiveByName(name, AgencyStatus.ACTIVE);
    }

    @RequestMapping(value = "/search/getHoardingDcb/{id}")
    public String viewHoarding(@PathVariable final Long id, final Model model) {
        AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService.findBy(id);
        if (advertisementPermitDetail != null) {
            final Advertisement hoarding = advertisementPermitDetail.getAdvertisement();
            model.addAttribute("hoarding", hoarding);
            model.addAttribute("dcbResult", hoardingReportService.getHoardingWiseDCBResult(hoarding));
        }
        return CITIZEN_CITIZEN_ONLINE_DCBVIEW;
    }

    @RequestMapping(value = "/search/generateonlinebill/{id}", method = POST)
    public String payTax(@PathVariable final String id, final RedirectAttributes redirectAttributes, final Model model) {

        AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService.findById(Long.valueOf(id));
        if (advertisementPermitDetail != null) {
            Advertisement advertisement = advertisementPermitDetail.getAdvertisement();
            if (advertisement != null && advertisement.getDemandId() != null) {
                // CHECK ANY DEMAND PENDING OR NOT
                if (!advertisementDemandService.checkAnyTaxIsPendingToCollect(advertisement)) {
                    model.addAttribute(MESSAGE, "msg.collection.noPendingTax");
                    return COLLECT_ADVTAX_ERROR;
                }
                advertisementBillable.setCollectionType(AdvertisementTaxConstants.ADVERTISEMENT_COLLECTION_TYPE);
                advertisementBillable.setAdvertisement(advertisement);
                final Serializable referenceNumber = sequenceNumberGenerator.getNextSequence(ADVERTISEMENT_BILLNUMBER);
                advertisementBillable.setReferenceNumber(AdvertisementTaxConstants.SERVICE_CODE.concat(String.format(
                        "%s%06d", "", referenceNumber)));
                model.addAttribute("collectxml", advertisementBillServiceImpl.getBillXML(advertisementBillable));
                return ONLINEPAYMENT_REDIRECTION;
            } else {
                model.addAttribute(MESSAGE, "msg.collection.noPendingTax");
                return COLLECT_ADVTAX_ERROR;
            }
        }
        model.addAttribute(MESSAGE, "msg.invalied.request");
        return COLLECT_ADVTAX_ERROR;
    }
}
