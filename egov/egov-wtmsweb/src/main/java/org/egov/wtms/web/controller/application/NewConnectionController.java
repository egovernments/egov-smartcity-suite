/**
 * eGov suite of products aim to improve the internal efficiency,transparency, accountability and the service delivery of the
 * government organizations.
 *
 * Copyright (C) <2015> eGovernments Foundation
 *
 * The updated version of eGov suite of products as by eGovernments Foundation is available at http://www.egovernments.org
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * http://www.gnu.org/licenses/ or http://www.gnu.org/licenses/gpl.html .
 *
 * In addition to the terms of the GPL license to be adhered to in using this program, the following additional terms are to be
 * complied with:
 *
 * 1) All versions of this program, verbatim or modified must carry this Legal Notice.
 *
 * 2) Any misrepresentation of the origin of the material is prohibited. It is required that all modified versions of this
 * material be marked in reasonable ways as different from the original version.
 *
 * 3) This license does not grant any rights to any user of the program with regards to rights under trademark law for use of the
 * trade names or trademarks of eGovernments Foundation.
 *
 * In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wtms.web.controller.application;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.wtms.application.entity.ApplicationDocuments;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.application.service.NewConnectionService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.masters.entity.DocumentNames;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/application")
public class NewConnectionController extends GenericConnectionController {

    private final WaterConnectionDetailsService waterConnectionDetailsService;
    private final ApplicationTypeService applicationTypeService;
    private final ConnectionDemandService connectionDemandService;
    private final WaterTaxUtils waterTaxUtils;
    private final NewConnectionService newConnectionService;

    @Autowired
    public NewConnectionController(final WaterConnectionDetailsService waterConnectionDetailsService,
            final ApplicationTypeService applicationTypeService, final ConnectionDemandService connectionDemandService,
            final WaterTaxUtils waterTaxUtils, final NewConnectionService newConnectionService, final SmartValidator validator) {
        this.waterConnectionDetailsService = waterConnectionDetailsService;
        this.applicationTypeService = applicationTypeService;
        this.connectionDemandService = connectionDemandService;
        this.waterTaxUtils = waterTaxUtils;
        this.newConnectionService = newConnectionService;
    }

    public @ModelAttribute("documentNamesList") List<DocumentNames> documentNamesList(
            @ModelAttribute final WaterConnectionDetails waterConnectionDetails) {
        waterConnectionDetails.setApplicationType(applicationTypeService.findByCode(WaterTaxConstants.NEWCONNECTION));
        return waterConnectionDetailsService.getAllActiveDocumentNames(waterConnectionDetails.getApplicationType());
    }

    @RequestMapping(value = "/newConnection-newform", method = GET)
    public String showNewApplicationForm(@ModelAttribute final WaterConnectionDetails waterConnectionDetails) {
        waterConnectionDetails.setApplicationDate(new Date());
        waterConnectionDetails.setConnectionStatus(ConnectionStatus.INPROGRESS);
        return "newconnection-form";
    }

    @RequestMapping(value = "/newConnection-create", method = POST)
    public String createNewConnection(@Valid @ModelAttribute final WaterConnectionDetails waterConnectionDetails,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes,
            final HttpServletRequest request) {

        validatePropertyID(waterConnectionDetails, resultBinder);

        final List<ApplicationDocuments> applicationDocs = new ArrayList<ApplicationDocuments>();
        int i = 0;
        if (!waterConnectionDetails.getApplicationDocs().isEmpty())
            for (final ApplicationDocuments applicationDocument : waterConnectionDetails.getApplicationDocs()) {
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
            return "newconnection-form";

        waterConnectionDetails.getApplicationDocs().clear();
        waterConnectionDetails.setApplicationDocs(applicationDocs);

        processAndStoreApplicationDocuments(waterConnectionDetails);

        Long approvalPosition = 0l;
        String approvalComent = "";

        if (request.getParameter("approvalComent") != null)
            approvalComent = request.getParameter("approvalComent");

        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        waterConnectionDetailsService.createNewWaterConnection(waterConnectionDetails, approvalPosition,
                approvalComent);

        return "redirect:/application/application-success?applicationNumber="
                + waterConnectionDetails.getApplicationNumber();
    }

    @RequestMapping(value = "/application-success", method = GET)
    public ModelAndView successView(@ModelAttribute WaterConnectionDetails waterConnectionDetails,
            final HttpServletRequest request, final Model model) {
        if (request.getParameter("applicationNumber") != null)
            waterConnectionDetails = waterConnectionDetailsService
                    .findByApplicationNumber(request.getParameter("applicationNumber"));
        model.addAttribute("connectionType", waterConnectionDetailsService.getConnectionTypesMap()
                .get(waterConnectionDetails.getConnectionType().name()));
        model.addAttribute("cityName", waterTaxUtils.getCityName());
        model.addAttribute("feeDetails", connectionDemandService.getSplitFee(waterConnectionDetails));
        return new ModelAndView("application/application-success", "waterConnectionDetails", waterConnectionDetails);

    }

    private void validatePropertyID(final WaterConnectionDetails waterConnectionDetails, final BindingResult errors) {
        if (waterConnectionDetails.getConnection() != null
                && waterConnectionDetails.getConnection().getPropertyIdentifier() != null
                && !waterConnectionDetails.getConnection().getPropertyIdentifier().equals("")) {
            final String errorMessage = newConnectionService
                    .checkValidPropertyAssessmentNumber(waterConnectionDetails.getConnection().getPropertyIdentifier());
            if (errorMessage != null && !errorMessage.equals(""))
                errors.rejectValue("connection.propertyIdentifier", errorMessage, errorMessage);
        }
    }

    @RequestMapping(value = "/generatebill/{consumerCode}", method = GET)
    public String showCollectFeeForm(final Model model, @PathVariable final String consumerCode) {
        return "redirect:/application/collecttax-view?consumerCode=" + consumerCode;
    }

    @RequestMapping(value = "/collecttax-view", method = GET)
    public ModelAndView collectTaxView(@ModelAttribute WaterConnectionDetails waterConnectionDetails,
            final HttpServletRequest request, final Model model) {
        if (request.getParameter("consumerCode") != null)
            waterConnectionDetails = waterConnectionDetailsService
                    .findByApplicationNumberOrConsumerCode(request.getParameter("consumerCode"));
        model.addAttribute("connectionType", waterConnectionDetailsService.getConnectionTypesMap()
                .get(waterConnectionDetails.getConnectionType().name()));
        model.addAttribute("feeDetails", connectionDemandService.getSplitFee(waterConnectionDetails));
        return new ModelAndView("application/collecttax-view", "waterConnectionDetails", waterConnectionDetails);
    }

    @RequestMapping(value = "/generatebill/{consumerCode}", method = POST)
    public String payTax(@ModelAttribute WaterConnectionDetails waterConnectionDetails,
            final RedirectAttributes redirectAttributes, @PathVariable final String consumerCode, final Model model) {
        waterConnectionDetails = waterConnectionDetailsService.findByApplicationNumberOrConsumerCode(consumerCode);
        model.addAttribute("collectxml", connectionDemandService.generateBill(consumerCode));
        return "collecttax-redirection";
    }
}
