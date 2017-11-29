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

package org.egov.wtms.web.controller.application;

import org.apache.commons.lang.StringUtils;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.DuplicateConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

import static org.egov.wtms.masters.entity.enums.ConnectionStatus.ACTIVE;
import static org.egov.wtms.masters.entity.enums.ConnectionStatus.INACTIVE;

@Controller
@RequestMapping(value = "/application")
public class DuplicateConnectionController {
    public static final String DUPLICATECONSUMERCODE = "duplicate-consumercode";
    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @RequestMapping(value = "/duplicateConsumerCode/{consumerCode}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final String consumerCode, final HttpServletRequest request) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(consumerCode, ACTIVE);

        if (waterConnectionDetails == null)
            throw new ValidationException("err.hsc.no.inactive");

        final List<WaterConnectionDetails> waterConnectionDetailsList = waterConnectionDetailsService
                .getAllConnectionDetailsByPropertyID(waterConnectionDetails.getConnection().getPropertyIdentifier());
        final List<String> consumerCodes = new ArrayList<>(0);
        for (final WaterConnectionDetails waterconnectionDetails : waterConnectionDetailsList)
            if (waterconnectionDetails.getConnection().getConsumerCode() != null
                    && !waterconnectionDetails.getConnection().getConsumerCode().equalsIgnoreCase(consumerCode))
                consumerCodes.add(waterconnectionDetails.getConnection().getConsumerCode());

        final DuplicateConnection duplicateConnection = new DuplicateConnection();
        duplicateConnection.setConsumerCodes(consumerCodes);
        duplicateConnection.setConsumerNo(consumerCode);
        model.addAttribute("duplicateConnection", duplicateConnection);
        return DUPLICATECONSUMERCODE;
    }

    @RequestMapping(value = "/duplicateConsumerCode/{consumerCode}", method = RequestMethod.POST)
    public String closeConnection(@ModelAttribute final DuplicateConnection duplicateConnection,
            @PathVariable final String consumerCode, final BindingResult resultBinder,
            final RedirectAttributes redirectAttrs, final Model model) {

        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(consumerCode, ACTIVE);

        if (waterConnectionDetails == null)
            throw new ValidationException("err.hsc.no.inactive");
        waterConnectionDetails.setReferenceNumber(duplicateConnection.getReferenceNumber());
        waterConnectionDetails.setDeactivateReason(duplicateConnection.getDeactivateReason());
        waterConnectionDetails.setConnectionStatus(INACTIVE);
        if (!duplicateConnection.getConsumerCodes().isEmpty())
            waterConnectionDetails.getConnection().setOldConsumerNumber(duplicateConnection.getConsumerCodes().get(0));
        waterConnectionDetailsService.save(waterConnectionDetails);
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                waterConnectionDetails.getConnection().getPropertyIdentifier(),
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        waterConnectionDetailsService.createWaterChargeIndex(waterConnectionDetails, assessmentDetails,
                waterConnectionDetailsService.getTotalAmount(waterConnectionDetails));
        model.addAttribute("consumerCode", consumerCode);
        return "duplicateconnection-success";

    }

    @RequestMapping(value = "/originalconnectionnumber-validate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Boolean validateConsumerNumber(@RequestParam final String originalConsumerNumber,
            @RequestParam final String duplicateConsumerNo) {
        WaterConnectionDetails waterConnectionDetails = null;
        WaterConnectionDetails duplicateConnectionDetails = null;
        if (StringUtils.isNotBlank(originalConsumerNumber))
            waterConnectionDetails = waterConnectionDetailsService
                    .findByApplicationNumberOrConsumerCodeAndStatus(originalConsumerNumber, ACTIVE);
        if (StringUtils.isNotBlank(duplicateConsumerNo))
            duplicateConnectionDetails = waterConnectionDetailsService
                    .findByApplicationNumberOrConsumerCodeAndStatus(duplicateConsumerNo, ACTIVE);
        if (waterConnectionDetails != null && duplicateConnectionDetails != null &&
                duplicateConnectionDetails.getConnection().getPropertyIdentifier()
                        .equals(waterConnectionDetails.getConnection().getPropertyIdentifier()))
            return true;
        return false;
    }

}
