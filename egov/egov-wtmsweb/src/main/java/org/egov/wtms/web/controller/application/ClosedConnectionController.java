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

package org.egov.wtms.web.controller.application;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.ClosedConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/application")
public class ClosedConnectionController {

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @RequestMapping(value = "/closedConsumerCode/{consumerCode}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final String consumerCode, final HttpServletRequest request) {
        final ClosedConnection closedConnection = new ClosedConnection();
        closedConnection.setConsumerNo(consumerCode);
        model.addAttribute("closedConnection", closedConnection);
        return "closed-consumercode";
    }

    @RequestMapping(value = "/closedConsumerCode/{consumerCode}", method = RequestMethod.POST)
    public String closeConnection(@Valid @ModelAttribute final ClosedConnection closedConnection,
            @PathVariable final String consumerCode, final BindingResult resultBinder,
            final RedirectAttributes redirectAttrs, final Model model) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(consumerCode, ConnectionStatus.ACTIVE);
        if (waterConnectionDetails == null)
            throw new ValidationException("err.hsc.no.inactive");
        waterConnectionDetails.setCloseApprovalDate(closedConnection.getClosedDate());
        waterConnectionDetails.setReferenceNumber(closedConnection.getReferenceNo());
        waterConnectionDetails.setDeactivateReason(closedConnection.getDeactivateReason());
        waterConnectionDetails.setConnectionStatus(ConnectionStatus.INACTIVE);
        waterConnectionDetailsService.save(waterConnectionDetails);
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        waterConnectionDetailsService.createWaterChargeIndex(waterConnectionDetails, assessmentDetails,
                waterConnectionDetailsService.getTotalAmount(waterConnectionDetails));
        model.addAttribute("consumerCode", consumerCode);
        return "closedconnection-success";
    }
}
