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
import javax.validation.ValidationException;

import org.egov.wtms.application.entity.ActivateConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.WaterRatesDetails;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
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
public class ActivateConsumerCodeController {

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ConnectionDemandService connectionDemandService;

    @RequestMapping(value = "/activateConsumerCode/{consumerCode}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final String consumerCode, final HttpServletRequest request) {
        final ActivateConnection activateConnection = new ActivateConnection();
        activateConnection.setConsumerNo(consumerCode);
        model.addAttribute("activateConnection", activateConnection);
        return "activate-consumercode";
    }

    @RequestMapping(value = "/activateConsumerCode/{consumerCode}", method = RequestMethod.POST)
    public String activateConnection(@ModelAttribute final ActivateConnection activateConnection,
            @PathVariable final String consumerCode, final BindingResult resultBinder,
            final RedirectAttributes redirectAttrs, final Model model) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(consumerCode, ConnectionStatus.INACTIVE);
        if (waterConnectionDetails == null)
            throw new ValidationException("err.hsc.no.active");
        final WaterRatesDetails waterRatesDetails = connectionDemandService
                .getWaterRatesDetailsForDemandUpdate(waterConnectionDetails);
        if (waterRatesDetails == null)
            throw new ValidationException("err.water.rate.not.found");
        waterConnectionDetails.setExecutionDate(activateConnection.getActivateDate());
        waterConnectionDetails.setReferenceNumber(activateConnection.getReferenceNo());
        waterConnectionDetails.setConnectionStatus(ConnectionStatus.ACTIVE);
        waterConnectionDetailsService.save(waterConnectionDetails);
        model.addAttribute("consumerCode", consumerCode);
        return "activateconnection-success";

    }

}
