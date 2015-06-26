/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wtms.web.controller.application;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.validation.Valid;

import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/application")
public class NewConnectionController extends GenericConnectionController {

    private final WaterConnectionDetailsService waterConnectionDetailsService;
    private final ApplicationTypeService applicationTypeService;

    @Autowired
    public NewConnectionController(final WaterConnectionDetailsService waterConnectionDetailsService,
            final ApplicationTypeService applicationTypeService, final SmartValidator validator) {
        this.waterConnectionDetailsService = waterConnectionDetailsService;
        this.applicationTypeService = applicationTypeService;

    }

    @RequestMapping(value = "/newConnection-newform", method = GET)
    public String showNewApplicationForm(@ModelAttribute final WaterConnectionDetails waterConnectionDetails,
            final Model model) {
        waterConnectionDetails.setApplicationType(applicationTypeService.findByCode(WaterTaxConstants.NEWCONNECTION));
        waterConnectionDetails.setConnectionStatus(ConnectionStatus.INPROGRESS);
        model.addAttribute("documentNamesList",
                waterConnectionDetailsService.getAllActiveDocumentNames(waterConnectionDetails.getApplicationType()));
        return "newconnection-form";
    }

    @RequestMapping(value = "/newConnection-create", method = POST)
    public String createNewConnection(@Valid @ModelAttribute final WaterConnectionDetails waterConnectionDetails,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes) {
        if (resultBinder.hasErrors())
            return "newconnection-form";
        processAndStoreApplicationDocuments(waterConnectionDetails);
        waterConnectionDetailsService.createNewWaterConnection(waterConnectionDetails);
        redirectAttributes.addFlashAttribute("waterConnectionDetails", waterConnectionDetails);
        /*redirectAttributes.addFlashAttribute("connection", waterConnectionDetailsService.getConnectionTypesMap()
                .get(waterConnectionDetails.getConnectionType().name()));*/
        return "application-success";
    }

}
