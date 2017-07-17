/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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

package org.egov.stms.web.controller.collection;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.egov.infra.persistence.utils.SequenceNumberGenerator;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.transactions.service.SewerageApplicationDetailsService;
import org.egov.stms.transactions.service.SewerageDemandService;
import org.egov.stms.transactions.service.SewerageThirdPartyServices;
import org.egov.stms.transactions.service.collection.SewerageBillServiceImpl;
import org.egov.stms.transactions.service.collection.SewerageBillable;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/collection")
public class SewerageBillGeneratorController {
    @Autowired
    private SewerageApplicationDetailsService sewerageApplicationDetailsService;

    @Autowired
    private SewerageBillServiceImpl sewerageBillServiceImpl;

    @Autowired
    private SequenceNumberGenerator sequenceNumberGenerator;

    @Autowired
    private SewerageDemandService sewerageDemandService;

    @Autowired
    private SewerageBillable sewerageBillable;

    @Autowired
    private SewerageThirdPartyServices sewerageThirdPartyServices;

    @RequestMapping(value = "/generatebill/{consumernumber}/{assessmentnumber}", method = RequestMethod.GET)
    public String payTax(@PathVariable final String consumernumber, @PathVariable final String assessmentnumber,
            final Model model, final ModelMap modelMap,
            @ModelAttribute SewerageApplicationDetails sewerageApplicationDetails, final HttpServletRequest request) {
     
        if (consumernumber != null)
            sewerageApplicationDetails = sewerageApplicationDetailsService.findByApplicationNumber(consumernumber);
        if (sewerageApplicationDetails != null) {
            if (sewerageApplicationDetails.getCurrentDemand() != null
                    && !sewerageDemandService.checkAnyTaxIsPendingToCollect(sewerageApplicationDetails.getCurrentDemand())) {
                model.addAttribute("message", "msg.collection.noPendingTax");
                return "collectSwtTax-error";
            }

            if (sewerageApplicationDetails.getCurrentDemand() != null && assessmentnumber != null) {
                final AssessmentDetails assessmentDetails = sewerageThirdPartyServices.getPropertyDetails(
                        assessmentnumber, request);
                final Serializable referenceNumber = sequenceNumberGenerator.getNextSequence(SewerageTaxConstants.SEWERAGE_BILLNUMBER);

                sewerageBillable.setSewerageApplicationDetails(sewerageApplicationDetails);
                sewerageBillable.setAssessmentDetails(assessmentDetails);
                sewerageBillable.setReferenceNumber(String.format("%s%06d", "", referenceNumber));
                // todo: check any pending tax ? theN redirect.

                model.addAttribute("collectxml", sewerageBillServiceImpl.getBillXML(sewerageBillable));
            } else {
                model.addAttribute("message", "msg.collection.noPendingTax");
                return "collectAdvtax-error";
            }
        }
        return "collecttax-redirection";
    }
}
