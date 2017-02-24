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

import static org.egov.wtms.utils.constants.WaterTaxConstants.CONNECTION_RECTIFICATION;

import java.util.ArrayList;
import java.util.List;

import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.ConnectionDetails;
import org.egov.wtms.application.entity.ConnectionRectification;
import org.egov.wtms.application.entity.PropertyAssessmentDetails;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/application")
public class ConnectionRectificationController {

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private BasicPropertyDAO basicPropertyDAO;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @RequestMapping(value = "/connectionrectification", method = RequestMethod.GET)
    public String viewForm(final Model model) {
        final ConnectionRectification connectionRectification = new ConnectionRectification();
        model.addAttribute("connectionRectification", connectionRectification);
        model.addAttribute("mode", "disable");
        return CONNECTION_RECTIFICATION;
    }

    @RequestMapping(value = "/connectionrectification", method = RequestMethod.POST)
    public String searchConnections(@ModelAttribute final ConnectionRectification connectionRectification,
            final BindingResult resultBinder, final RedirectAttributes redirectAttrs, final Model model) {

        final List<WaterConnectionDetails> waterConnectionDetailsList = new ArrayList<WaterConnectionDetails>(0);

        if (connectionRectification.getConsumerNo() != null) {
            final WaterConnectionDetails waterconnectionDetails = waterConnectionDetailsService
                    .findByConsumerCodeAndConnectionStatus(connectionRectification.getConsumerNo(),
                            ConnectionStatus.ACTIVE);
            if (waterconnectionDetails != null) {
                waterConnectionDetailsList.add(waterconnectionDetails);
                connectionRectification.setAssessmentNo(waterconnectionDetails.getConnection().getPropertyIdentifier());

            }

        } else
            waterConnectionDetailsList.addAll(waterConnectionDetailsService
                    .getAllConnectionDetailsByPropertyID(connectionRectification.getAssessmentNo()));
        final PropertyAssessmentDetails propertyAssessmentDetails = new PropertyAssessmentDetails();
        final List<ConnectionDetails> connectionDetailsList = new ArrayList<ConnectionDetails>();

        if (waterConnectionDetailsList != null && waterConnectionDetailsList.size() > 0) {
            final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(
                    connectionRectification.getAssessmentNo(), PropertyExternalService.FLAG_FULL_DETAILS,
                    BasicPropertyStatus.ALL);
            propertyAssessmentDetails.setAssessmentNumber(connectionRectification.getAssessmentNo());
            propertyAssessmentDetails.setStatus(assessmentDetails.isStatus() ? "ACTIVE" : "INACTIVE");
            propertyAssessmentDetails
                    .setOwnerName(new ArrayList<>(assessmentDetails.getOwnerNames()).get(0).getOwnerName().toString());
            propertyAssessmentDetails.setAddress(assessmentDetails.getPropertyAddress());
            for (final WaterConnectionDetails waterConnectionDetails : waterConnectionDetailsList) {
                final ConnectionDetails connectionDetails = new ConnectionDetails();
                connectionDetails.setConsumerNumber(waterConnectionDetails.getConnection().getConsumerCode());
                connectionDetails.setIsPrimary(
                        waterConnectionDetails.getConnection().getParentConnection() != null ? "NO" : "YES");
                connectionDetails.setDemandDue(waterConnectionDetailsService
                        .getTotalDemandTillCurrentFinYear(waterConnectionDetails).doubleValue());
                connectionDetails.setStatus(waterConnectionDetails.getConnectionStatus().toString());
                connectionDetailsList.add(connectionDetails);
            }
            connectionRectification.setPropertyAssessmentDetails(propertyAssessmentDetails);
            connectionRectification.setConnectionDetailsList(connectionDetailsList);
        } else {

        }
        model.addAttribute("connectionRectification", connectionRectification);
        model.addAttribute("mode", "enable");
        return CONNECTION_RECTIFICATION;
    }
}
