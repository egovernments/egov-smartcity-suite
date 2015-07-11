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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.wtms.application.entity.ApplicationDocuments;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.DocumentNames;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.utils.constants.WaterTaxConstants;
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
public class ChangeOfUseController extends GenericConnectionController {

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;
    @Autowired
    private ApplicationTypeService applicationTypeService;

    public @ModelAttribute("documentNamesList") List<DocumentNames> documentNamesList(
            @ModelAttribute final WaterConnectionDetails changeOfUse) {
        changeOfUse.setApplicationType(applicationTypeService.findByCode(WaterTaxConstants.CHANGEOFUSE));
        return waterConnectionDetailsService.getAllActiveDocumentNames(changeOfUse.getApplicationType());
    }

    @RequestMapping(value = "/changeOfUse/{consumerCode}", method = RequestMethod.GET)
    public String showForm(@ModelAttribute final WaterConnectionDetails changeOfUse, final Model model,
            @PathVariable final String consumerCode) {
        final WaterConnectionDetails parentConnectionDetails = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(consumerCode, ConnectionStatus.ACTIVE);
        if (parentConnectionDetails == null) {
            // TODO - error handling
        } else {
            changeOfUse.setConnectionStatus(ConnectionStatus.INPROGRESS);
            changeOfUse.setConnectionType(parentConnectionDetails.getConnectionType());
            changeOfUse.setUsageType(parentConnectionDetails.getUsageType());
            changeOfUse.setCategory(parentConnectionDetails.getCategory());
            changeOfUse.setPropertyType(parentConnectionDetails.getPropertyType());
            changeOfUse.setPipeSize(parentConnectionDetails.getPipeSize());
            changeOfUse.setSumpCapacity(parentConnectionDetails.getSumpCapacity());

            model.addAttribute("waterConnectionDetails", parentConnectionDetails);
            model.addAttribute("connectionType", waterConnectionDetailsService.getConnectionTypesMap()
                    .get(parentConnectionDetails.getConnectionType().name()));
            model.addAttribute("changeOfUse", changeOfUse);
            model.addAttribute("mode", "changeOfUse");
        }
        return "changeOfUse-form";
    }

    //TODO - Basic save. Still working on
    @RequestMapping(value = "/changeOfUse/changeOfUse-create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final WaterConnectionDetails changeOfUse,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes,
            final HttpServletRequest request) {

        final List<ApplicationDocuments> applicationDocs = new ArrayList<ApplicationDocuments>();
        int i = 0;
        if (!changeOfUse.getApplicationDocs().isEmpty())
            for (final ApplicationDocuments applicationDocument : changeOfUse.getApplicationDocs()) {
                if (applicationDocument.getDocumentNumber() == null && applicationDocument.getDocumentDate() != null) {
                    final String fieldError = "applicationDocs[" + i + "].documentNumber";
                    resultBinder.rejectValue(fieldError, "documentNumber.required");
                }
                if (applicationDocument.getDocumentNumber() != null && applicationDocument.getDocumentDate() == null) {
                    final String fieldError = "applicationDocs[" + i + "].documentDate";
                    resultBinder.rejectValue(fieldError, "documentDate.required");
                } else if (validApplicationDocument(applicationDocument))
                    applicationDocs.add(applicationDocument);
                i++;
            }

        if (resultBinder.hasErrors())
            return "changeOfUse-form";

        changeOfUse.getApplicationDocs().clear();
        changeOfUse.setApplicationDocs(applicationDocs);

        processAndStoreApplicationDocuments(changeOfUse);

        Long approvalPosition = 0l;
        String approvalComent = "";

        if (request.getParameter("approvalComent") != null)
            approvalComent = request.getParameter("approvalComent");

        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        changeOfUse.setApplicationDate(new Date());
        waterConnectionDetailsService.createNewWaterConnection(changeOfUse, approvalPosition, approvalComent);
        return "redirect:/application/application-success?applicationNumber=" + changeOfUse.getApplicationNumber();
    }
}
