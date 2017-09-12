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

import static org.egov.commons.entity.Source.MEESEVA;
import static org.egov.commons.entity.Source.ONLINE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SOURCECHANNEL_ONLINE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pims.commons.Position;
import org.egov.wtms.application.entity.ApplicationDocuments;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.ChangeOfUseService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/application")
public class ChangeOfUseController extends GenericConnectionController {

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private WaterTaxUtils waterTaxUtils;
    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ChangeOfUseService changeOfUseService;

    public @ModelAttribute("documentNamesList") List<DocumentNames> documentNamesList(
            @ModelAttribute final WaterConnectionDetails changeOfUse) {
        changeOfUse.setApplicationType(applicationTypeService.findByCode(WaterTaxConstants.CHANGEOFUSE));
        return waterConnectionDetailsService.getAllActiveDocumentNames(changeOfUse.getApplicationType());
    }

    @RequestMapping(value = "/changeOfUse/{consumerCode}", method = RequestMethod.GET)
    public String showForm(WaterConnectionDetails parentConnectionDetails,
            @ModelAttribute final WaterConnectionDetails changeOfUse, final Model model,
            @PathVariable final String consumerCode, final HttpServletRequest request) {
        final String meesevaApplicationNumber = request.getParameter("meesevaApplicationNumber");

        final WaterConnectionDetails connectionUnderChange = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(consumerCode, ConnectionStatus.ACTIVE);
        if (null != connectionUnderChange.getConnection().getParentConnection())
            parentConnectionDetails = waterConnectionDetailsService
                    .getParentConnectionDetailsForParentConnectionNotNull(consumerCode, ConnectionStatus.ACTIVE);

        else
            parentConnectionDetails = waterConnectionDetailsService.getParentConnectionDetails(connectionUnderChange
                    .getConnection().getPropertyIdentifier(), ConnectionStatus.ACTIVE);
        if (parentConnectionDetails == null) {
            // TODO - error handling
        } else
            loadBasicData(model, parentConnectionDetails, changeOfUse, connectionUnderChange, meesevaApplicationNumber);
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        workflowContainer.setAdditionalRule(changeOfUse.getApplicationType().getCode());
        prepareWorkflow(model, changeOfUse, workflowContainer);
        return "changeOfUse-form";
    }

    // TODO - Basic save. Still working on
    @RequestMapping(value = "/changeOfUse/changeOfUse-create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final WaterConnectionDetails changeOfUse,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes,
            final HttpServletRequest request, final Model model, @RequestParam final String workFlowAction,
            final BindingResult errors) {
        final Boolean isCSCOperator = waterTaxUtils.isCSCoperator(securityUtils.getCurrentUser());
        final Boolean citizenPortalUser = waterTaxUtils.isCitizenPortalUser(securityUtils.getCurrentUser());
        final Boolean loggedInMeesevaUser = waterTaxUtils.isMeesevaUser(securityUtils.getCurrentUser());
        final Boolean isAnonymousUser = waterTaxUtils.isAnonymousUser(securityUtils.getCurrentUser());
        model.addAttribute("isAnonymousUser", isAnonymousUser);
        if (loggedInMeesevaUser && request.getParameter("meesevaApplicationNumber") != null)
            changeOfUse.setMeesevaApplicationNumber(request.getParameter("meesevaApplicationNumber"));
        model.addAttribute("citizenPortalUser", citizenPortalUser);
        if (!isCSCOperator && !citizenPortalUser && !loggedInMeesevaUser && !isAnonymousUser) {
            final Boolean isJuniorAsstOrSeniorAsst = waterTaxUtils
                    .isLoggedInUserJuniorOrSeniorAssistant(securityUtils.getCurrentUser().getId());
            if (!isJuniorAsstOrSeniorAsst)
                throw new ValidationException("err.creator.application");
        }
        final List<ApplicationDocuments> applicationDocs = new ArrayList<>();
        final WaterConnectionDetails connectionUnderChange = waterConnectionDetailsService
                .findByConsumerCodeAndConnectionStatus(changeOfUse.getConnection().getConsumerCode(),
                        ConnectionStatus.ACTIVE);
        final WaterConnectionDetails parent = waterConnectionDetailsService.getParentConnectionDetails(
                connectionUnderChange.getConnection().getPropertyIdentifier(), ConnectionStatus.ACTIVE);
        String message = "";
        if (parent != null)
            message = changeOfUseService.validateChangeOfUseConnection(parent);
        String sourceChannel = request.getParameter("Source");
        String consumerCode = "";
        if (!message.isEmpty() && !"".equals(message)) {
            if (changeOfUse.getConnection().getParentConnection() != null)
                consumerCode = changeOfUse.getConnection().getParentConnection().getConsumerCode();
            else
                consumerCode = changeOfUse.getConnection().getConsumerCode();
            return "redirect:/application/changeOfUse/" + consumerCode;
        }
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
        waterConnectionDetailsService.validateWaterRateAndDonationHeader(changeOfUse);
        if (resultBinder.hasErrors()) {
            final WaterConnectionDetails parentConnectionDetails = waterConnectionDetailsService
                    .getActiveConnectionDetailsByConnection(changeOfUse.getConnection());
            loadBasicData(model, parentConnectionDetails, changeOfUse, changeOfUse, changeOfUse.getMeesevaApplicationNumber());
            final WorkflowContainer workflowContainer = new WorkflowContainer();
            workflowContainer.setAdditionalRule(changeOfUse.getApplicationType().getCode());
            prepareWorkflow(model, changeOfUse, workflowContainer);
            model.addAttribute("approvalPosOnValidate", request.getParameter("approvalPosition"));
            model.addAttribute("additionalRule", changeOfUse.getApplicationType().getCode());
            model.addAttribute("validationmessage", resultBinder.getFieldErrors().get(0).getField() + " = "
                    + resultBinder.getFieldErrors().get(0).getDefaultMessage());
            model.addAttribute("stateType", changeOfUse.getClass().getSimpleName());
            model.addAttribute("currentUser", waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));

            return "changeOfUse-form";

        }
        if (changeOfUse.getState() == null)
            changeOfUse.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    WaterTaxConstants.APPLICATION_STATUS_CREATED, WaterTaxConstants.MODULETYPE));

        changeOfUse.getApplicationDocs().clear();
        changeOfUse.setApplicationDocs(applicationDocs);

        processAndStoreApplicationDocuments(changeOfUse);

        Long approvalPosition = 0l;
        String approvalComent = "";

        if (request.getParameter("approvalComent") != null)
            approvalComent = request.getParameter("approvalComent");

        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
        final Boolean applicationByOthers = waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser());

        if (applicationByOthers != null && applicationByOthers.equals(true) || citizenPortalUser || isAnonymousUser) {
            final Position userPosition = waterTaxUtils.getZonalLevelClerkForLoggedInUser(changeOfUse.getConnection()
                    .getPropertyIdentifier());
            if (userPosition != null)
                approvalPosition = userPosition.getId();
            else {
                final WaterConnectionDetails parentConnectionDetails = waterConnectionDetailsService
                        .getActiveConnectionDetailsByConnection(changeOfUse.getConnection());
                loadBasicData(model, parentConnectionDetails, changeOfUse, changeOfUse, null);
                final WorkflowContainer workflowContainer = new WorkflowContainer();
                workflowContainer.setAdditionalRule(changeOfUse.getApplicationType().getCode());
                prepareWorkflow(model, changeOfUse, workflowContainer);
                model.addAttribute("additionalRule", changeOfUse.getApplicationType().getCode());
                model.addAttribute("stateType", changeOfUse.getClass().getSimpleName());
                model.addAttribute("currentUser", waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
                errors.rejectValue("connection.propertyIdentifier", "err.validate.connection.user.mapping",
                        "err.validate.connection.user.mapping");
                model.addAttribute("noJAORSAMessage", "No JA/SA exists to forward the application.");
                return "changeOfUse-form";
            }
        }
        changeOfUse.setApplicationDate(new Date());
        if (isAnonymousUser) {
            changeOfUse.setSource(ONLINE);
            sourceChannel = SOURCECHANNEL_ONLINE;
        }
        if (citizenPortalUser && (changeOfUse.getSource() == null || StringUtils.isBlank(changeOfUse.getSource().toString())))
            changeOfUse.setSource(waterTaxUtils.setSourceOfConnection(securityUtils.getCurrentUser()));

        if (loggedInMeesevaUser) {
            final HashMap<String, String> meesevaParams = new HashMap<>();
            meesevaParams.put("APPLICATIONNUMBER", changeOfUse.getMeesevaApplicationNumber());
            if (changeOfUse.getApplicationNumber() == null) {
                changeOfUse.setApplicationNumber(changeOfUse.getMeesevaApplicationNumber());
                changeOfUse.setSource(MEESEVA);
            }
            changeOfUseService.createChangeOfUseApplication(changeOfUse, approvalPosition, approvalComent, changeOfUse
                    .getApplicationType().getCode(), workFlowAction, meesevaParams, sourceChannel);
        } else
            changeOfUseService.createChangeOfUseApplication(changeOfUse, approvalPosition, approvalComent, changeOfUse
                    .getApplicationType().getCode(), workFlowAction, sourceChannel);

        if (loggedInMeesevaUser)
            return "redirect:/application/generate-meesevareceipt?transactionServiceNumber=" + changeOfUse.getApplicationNumber();
        else
            return "redirect:/application/citizeenAcknowledgement?pathVars=" + changeOfUse.getApplicationNumber();
    }

    private void loadBasicData(final Model model, final WaterConnectionDetails parentConnectionDetails,
            final WaterConnectionDetails changeOfUse, final WaterConnectionDetails connectionUnderChange,
            final String meesevaApplicationNumber) {
        Boolean loggedUserIsMeesevaUser;
        changeOfUse.setConnectionStatus(ConnectionStatus.INPROGRESS);
        changeOfUse.setConnectionType(connectionUnderChange.getConnectionType());
        changeOfUse.setUsageType(connectionUnderChange.getUsageType());
        changeOfUse.setCategory(connectionUnderChange.getCategory());
        changeOfUse.setPropertyType(connectionUnderChange.getPropertyType());
        changeOfUse.setPipeSize(connectionUnderChange.getPipeSize());
        changeOfUse.setSumpCapacity(connectionUnderChange.getSumpCapacity());
        changeOfUse.setConnection(connectionUnderChange.getConnection());
        changeOfUse.setWaterSource(connectionUnderChange.getWaterSource());
        changeOfUse.setNumberOfPerson(connectionUnderChange.getNumberOfPerson());
        changeOfUse.setNumberOfRooms(connectionUnderChange.getNumberOfRooms());

        loggedUserIsMeesevaUser = waterTaxUtils.isMeesevaUser(securityUtils.getCurrentUser());
        if (loggedUserIsMeesevaUser)
            if (meesevaApplicationNumber == null)
                throw new ApplicationRuntimeException("MEESEVA.005");
            else
                changeOfUse.setMeesevaApplicationNumber(meesevaApplicationNumber);
        model.addAttribute("waterConnectionDetails", parentConnectionDetails);
        model.addAttribute(
                "connectionType",
                waterConnectionDetailsService.getConnectionTypesMap().get(
                        connectionUnderChange.getConnectionType().name()));
        model.addAttribute("changeOfUse", changeOfUse);
        model.addAttribute("stateType", connectionUnderChange.getClass().getSimpleName());
        model.addAttribute("mode", "changeOfUse");
        model.addAttribute("currentUser", waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
        model.addAttribute("validationMessage", changeOfUseService.validateChangeOfUseConnection(connectionUnderChange));
        final BigDecimal waterTaxDueforParent = waterConnectionDetailsService.getTotalAmount(connectionUnderChange);
        model.addAttribute("waterTaxDueforParent", waterTaxDueforParent);
        model.addAttribute("typeOfConnection", WaterTaxConstants.CHANGEOFUSE);
        model.addAttribute("usageTypes", usageTypeService.getActiveUsageTypes());
        model.addAttribute("connectionCategories", connectionCategoryService.getAllActiveConnectionCategory());
        model.addAttribute("pipeSizes", pipeSizeService.getAllActivePipeSize());
        model.addAttribute("citizenPortalUser", waterTaxUtils.isCitizenPortalUser(securityUtils.getCurrentUser()));
        model.addAttribute("isAnonymousUser", waterTaxUtils.isAnonymousUser(securityUtils.getCurrentUser()));
    }

}