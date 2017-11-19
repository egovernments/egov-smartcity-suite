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
package org.egov.stms.web.controller.citizen;

import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.persistence.utils.SequenceNumberGenerator;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.stms.elasticsearch.entity.SewerageConnSearchRequest;
import org.egov.stms.elasticsearch.entity.SewerageSearchResult;
import org.egov.stms.entity.es.SewerageIndex;
import org.egov.stms.service.es.SeweragePaginationService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageDCBReporService;
import org.egov.stms.transactions.service.SewerageDemandService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.egov.stms.transactions.service.collection.SewerageBillServiceImpl;
import org.egov.stms.transactions.service.collection.SewerageBillable;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class SewerageCitizenSupportController {

    private static final String SEARCH_STMS_ONLINE_PAYMENT = "search-sewerage-online-payment";
    private static final String MESSAGE = "message";
    private static final String COLLECT_SEWERAGETAX_ERROR = "collectSwtTax-error";
    private static final String SEWERAGE_ONLINEPAYMENT_REDIRECTION = "sewerage-onlinepayment-redirection";
    private static final String SEWERAGE_CITIZEN_ONLINE_DCBVIEW = "citizen/sewerage-citizen-dcbview";

    @Autowired
    private CityService cityService;
    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;
    @Autowired
    private SewerageDemandService sewerageDemandService;
    @Autowired
    private SewerageBillable sewerageBillable;
    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;
    @Autowired
    private SequenceNumberGenerator sequenceNumberGenerator;
    @Autowired
    private SewerageBillServiceImpl sewerageBillServiceImpl;
    @Autowired
    private SewerageDCBReporService sewerageDCBReporService;
    @Autowired
    private SeweragePaginationService seweragePaginationService;

    @RequestMapping(value = "/citizen/search/search-sewerage", method = GET)
    public String newSearchForm(final Model model) {
        model.addAttribute("sewerage", new SewerageConnSearchRequest());
        return SEARCH_STMS_ONLINE_PAYMENT;
    }

    @RequestMapping(value = "/citizen/search/onlinepayment", method = RequestMethod.POST)
    @ResponseBody
    public DataTable<SewerageSearchResult> searchApplication(
            @ModelAttribute final SewerageConnSearchRequest sewerageConnSearchRequest) {
        final List<SewerageSearchResult> searchResultList = new ArrayList<>();
        Page<SewerageIndex> resultList;
        final Pageable pageable = new PageRequest(sewerageConnSearchRequest.pageNumber(),
                sewerageConnSearchRequest.pageSize(), sewerageConnSearchRequest.orderDir(), sewerageConnSearchRequest.orderBy());
        final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());
        if (cityWebsite != null)
            sewerageConnSearchRequest.setUlbName(cityWebsite.getName());
        resultList = seweragePaginationService.buildPaymentSearch(sewerageConnSearchRequest, searchResultList);
        return new DataTable<>(new PageImpl<>(searchResultList, pageable, resultList.getTotalElements()),
                sewerageConnSearchRequest.draw());

    }

    @RequestMapping(value = "/citizen/search/sewerageRateReportView/{consumerno}/{assessmentno}", method = RequestMethod.GET)
    public ModelAndView getSewerageRateReport(@PathVariable final String consumerno,
            @PathVariable final String assessmentno, final Model model,
            final HttpServletRequest request) {
        SewerageApplicationDetails sewerageApplicationDetails = null;
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        final String currentDate = formatter.format(new Date());
        if (consumerno != null) {
            sewerageApplicationDetails = sewerageApplicationDetailsService.findByApplicationNumber(consumerno);
            if (sewerageApplicationDetails != null) {
                final AssessmentDetails propertyOwnerDetails = sewerageThirdPartyServices.getPropertyDetails(
                        assessmentno, request);
                model.addAttribute("applicationNumber", sewerageApplicationDetails.getApplicationNumber());
                model.addAttribute("propertyOwnerDetails", propertyOwnerDetails);
                model.addAttribute("assessmentnumber", assessmentno);
                model.addAttribute("currentDate", currentDate);
                model.addAttribute("dcbResultList",
                        sewerageDCBReporService.getSewerageRateDCBReport(sewerageApplicationDetails));
            }
        }
        return new ModelAndView(SEWERAGE_CITIZEN_ONLINE_DCBVIEW, "sewerageApplicationDetails", sewerageApplicationDetails);
    }

    @RequestMapping(value = "/citizen/search/sewerageGenerateonlinebill/{consumerno}/{assessmentno}", method = {
            RequestMethod.POST, RequestMethod.GET })
    public String payTax(@PathVariable final String consumerno, @PathVariable final String assessmentno,
            final RedirectAttributes redirectAttributes, final Model model, final HttpServletRequest request) {
        SewerageApplicationDetails sewerageApplicationDetails = new SewerageApplicationDetails();
        if (consumerno != null)
            sewerageApplicationDetails = sewerageApplicationDetailsService.findByApplicationNumber(consumerno);
        if (sewerageApplicationDetails != null) {
            if (sewerageApplicationDetails.getCurrentDemand() != null
                    && !sewerageDemandService.checkAnyTaxIsPendingToCollect(sewerageApplicationDetails.getCurrentDemand())) {
                model.addAttribute(MESSAGE, "msg.collection.noPendingTax");
                return COLLECT_SEWERAGETAX_ERROR;
            }
            if (sewerageApplicationDetails.getCurrentDemand() != null && assessmentno != null) {
                final AssessmentDetails assessmentDetails = sewerageThirdPartyServices.getPropertyDetails(
                        assessmentno, request);
                final Serializable referenceNumber = sequenceNumberGenerator
                        .getNextSequence(SewerageTaxConstants.SEWERAGE_BILLNUMBER);

                sewerageBillable.setSewerageApplicationDetails(sewerageApplicationDetails);
                sewerageBillable.setAssessmentDetails(assessmentDetails);
                sewerageBillable.setReferenceNumber(String.format("%s%06d", "", referenceNumber));
                model.addAttribute("collectxml", sewerageBillServiceImpl.getBillXML(sewerageBillable));
                return SEWERAGE_ONLINEPAYMENT_REDIRECTION;

            } else {
                model.addAttribute("message", "msg.collection.noPendingTax");
                return COLLECT_SEWERAGETAX_ERROR;
            }
        }
        model.addAttribute(MESSAGE, "msg.invalid.request");
        return COLLECT_SEWERAGETAX_ERROR;
    }

}
