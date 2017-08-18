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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pims.commons.Position;
import org.egov.wtms.application.entity.ApplicationDocuments;
import org.egov.wtms.application.entity.WaterConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.AdditionalConnectionService;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.application.service.WaterConnectionService;
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
public class AdditionalConnectionController extends GenericConnectionController {

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;
    @Autowired
    private ApplicationTypeService applicationTypeService;
    @Autowired
    private WaterConnectionService waterConnectionService;
    @Autowired
    private AdditionalConnectionService additionalConnectionService;
    @Autowired
    private WaterTaxUtils waterTaxUtils;
    @Autowired
    private SecurityUtils securityUtils;

    public @ModelAttribute("documentNamesList") List<DocumentNames> documentNamesList(
            @ModelAttribute final WaterConnectionDetails addConnection) {
        addConnection.setApplicationType(applicationTypeService.findByCode(WaterTaxConstants.ADDNLCONNECTION));
        return waterConnectionDetailsService.getAllActiveDocumentNames(addConnection.getApplicationType());
    }

    @RequestMapping(value = "/addconnection/{consumerCode}", method = RequestMethod.GET)
    public String showAdditionalApplicationForm(WaterConnectionDetails parentConnectionDetails,
            @ModelAttribute final WaterConnectionDetails addConnection, final Model model,
            @PathVariable final String consumerCode, final HttpServletRequest request) {

        final String meesevaApplicationNumber = request.getParameter("meesevaApplicationNumber");
        final WaterConnection connection = waterConnectionService.findByConsumerCode(consumerCode);
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        workflowContainer.setAdditionalRule(addConnection.getApplicationType().getCode());
        prepareWorkflow(model, addConnection, workflowContainer);
        parentConnectionDetails = waterConnectionDetailsService.getParentConnectionDetails(
                connection.getPropertyIdentifier(), ConnectionStatus.ACTIVE);
        loadBasicDetails(addConnection, model, parentConnectionDetails, meesevaApplicationNumber);
        return "addconnection-form";
    }

    private void loadBasicDetails(final WaterConnectionDetails addConnection, final Model model,
            final WaterConnectionDetails parentConnectionDetails, final String meesevaApplicationNumber) {
        Boolean loggedUserIsMeesevaUser;
        addConnection.setConnectionStatus(ConnectionStatus.INPROGRESS);
        model.addAttribute("parentConnection", parentConnectionDetails.getConnection());
        model.addAttribute("waterConnectionDetails", parentConnectionDetails);
        model.addAttribute(
                "connectionType",
                waterConnectionDetailsService.getConnectionTypesMap().get(
                        parentConnectionDetails.getConnectionType().name()));
        model.addAttribute("addConnection", addConnection);
        model.addAttribute("stateType", parentConnectionDetails.getClass().getSimpleName());
        model.addAttribute("currentUser", waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
        model.addAttribute("additionalRule", addConnection.getApplicationType().getCode());
        model.addAttribute("mode", "addconnection");
        model.addAttribute("validationMessage",
                additionalConnectionService.validateAdditionalConnection(parentConnectionDetails)); 
        final BigDecimal waterTaxDueforParent = waterConnectionDetailsService.getTotalAmount(parentConnectionDetails);
        model.addAttribute("waterTaxDueforParent", waterTaxDueforParent);

        loggedUserIsMeesevaUser = waterTaxUtils.isMeesevaUser(securityUtils.getCurrentUser());
        if (loggedUserIsMeesevaUser)
            if (meesevaApplicationNumber == null && meesevaApplicationNumber != "")
                throw new ApplicationRuntimeException("MEESEVA.005");
            else
                addConnection.setMeesevaApplicationNumber(meesevaApplicationNumber);

        model.addAttribute("typeOfConnection", WaterTaxConstants.ADDNLCONNECTION);
        model.addAttribute("citizenPortalUser", waterTaxUtils.isCitizenPortalUser(securityUtils.getCurrentUser()));
    }

    @RequestMapping(value = "/addconnection/addConnection-create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute final WaterConnectionDetails addConnection,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes, final Model model,
            @RequestParam final String workFlowAction, final HttpServletRequest request, final BindingResult errors) {
        final Boolean isCSCOperator = waterTaxUtils.isCSCoperator(securityUtils.getCurrentUser());
        final Boolean citizenPortalUser = waterTaxUtils.isCitizenPortalUser(securityUtils.getCurrentUser());
        final Boolean loggedUserIsMeesevaUser = waterTaxUtils.isMeesevaUser(securityUtils.getCurrentUser());
        if (loggedUserIsMeesevaUser && request.getParameter("meesevaApplicationNumber") != null)
            addConnection.setMeesevaApplicationNumber(request.getParameter("meesevaApplicationNumber"));
        model.addAttribute("citizenPortalUser", citizenPortalUser);
        if (!isCSCOperator && !citizenPortalUser && !loggedUserIsMeesevaUser) {
            final Boolean isJuniorAsstOrSeniorAsst = waterTaxUtils
                    .isLoggedInUserJuniorOrSeniorAssistant(securityUtils.getCurrentUser().getId());
            if (!isJuniorAsstOrSeniorAsst)
                throw new ValidationException("err.creator.application");
        }

        final String sourceChannel = request.getParameter("Source");
        final WaterConnectionDetails parent = waterConnectionDetailsService.getActiveConnectionDetailsByConnection(addConnection
                .getConnection().getParentConnection());
        final String message = additionalConnectionService.validateAdditionalConnection(parent);
        if (!message.isEmpty() && !"".equals(message))
            return "redirect:/application/addconnection/"
            + addConnection.getConnection().getParentConnection().getConsumerCode();

        final List<ApplicationDocuments> applicationDocs = new ArrayList<>();
        int i = 0;
        if (!addConnection.getApplicationDocs().isEmpty())
            for (final ApplicationDocuments applicationDocument : addConnection.getApplicationDocs()) {
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
        waterConnectionDetailsService.validateWaterRateAndDonationHeader(addConnection);
        if (addConnection.getState() == null)
            addConnection.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(
                    WaterTaxConstants.APPLICATION_STATUS_CREATED, WaterTaxConstants.MODULETYPE));

        if (resultBinder.hasErrors()) {
            final WaterConnectionDetails parentConnectionDetails = waterConnectionDetailsService
                    .getActiveConnectionDetailsByConnection(addConnection.getConnection());
            loadBasicDetails(addConnection, model, parentConnectionDetails, addConnection.getMeesevaApplicationNumber());
            final WorkflowContainer workflowContainer = new WorkflowContainer();
            workflowContainer.setAdditionalRule(addConnection.getApplicationType().getCode());
            prepareWorkflow(model, addConnection, workflowContainer);
            model.addAttribute("approvalPosOnValidate", request.getParameter("approvalPosition"));
            model.addAttribute("additionalRule", addConnection.getApplicationType().getCode());
            model.addAttribute("stateType", addConnection.getClass().getSimpleName());
            model.addAttribute("currentUser", waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
            return "addconnection-form";
        }
        addConnection.setApplicationDate(new Date());
        addConnection.getApplicationDocs().clear();
        addConnection.setApplicationDocs(applicationDocs);

        processAndStoreApplicationDocuments(addConnection);

        Long approvalPosition = 0l;
        String approvalComment = "";
        String workFlowActionValue = "";

        if (request.getParameter("approvalComent") != null)
            approvalComment = request.getParameter("approvalComent");

        if (request.getParameter("workFlowAction") != null)
            workFlowActionValue = request.getParameter("workFlowAction");

        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        final Boolean applicationByOthers = waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser());

        if (applicationByOthers != null && applicationByOthers.equals(true) || citizenPortalUser) {
            final Position userPosition = waterTaxUtils.getZonalLevelClerkForLoggedInUser(addConnection.getConnection()
                    .getPropertyIdentifier());
            if (userPosition != null)
                approvalPosition = userPosition.getId();
            else {
                final WaterConnectionDetails parentConnectionDetails = waterConnectionDetailsService
                        .getActiveConnectionDetailsByConnection(addConnection.getConnection());
                loadBasicDetails(addConnection, model, parentConnectionDetails, null);
                final WorkflowContainer workflowContainer = new WorkflowContainer();
                workflowContainer.setAdditionalRule(addConnection.getApplicationType().getCode());
                prepareWorkflow(model, addConnection, workflowContainer);
                model.addAttribute("additionalRule", addConnection.getApplicationType().getCode());
                model.addAttribute("stateType", addConnection.getClass().getSimpleName());
                model.addAttribute("currentUser", waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
                errors.rejectValue("connection.propertyIdentifier", "err.validate.connection.user.mapping",
                        "err.validate.connection.user.mapping");
                model.addAttribute("noJAORSAMessage", "No JA/SA exists to forward the application.");
                return "addconnection-form";

            }
        }
        if (citizenPortalUser)
            if (addConnection.getSource() == null || StringUtils.isBlank(addConnection.getSource().toString()))
                addConnection.setSource(waterTaxUtils.setSourceOfConnection(securityUtils.getCurrentUser()));

        if (loggedUserIsMeesevaUser) {
            final HashMap<String, String> meesevaParams = new HashMap<>();
            meesevaParams.put("APPLICATIONNUMBER", addConnection.getMeesevaApplicationNumber());
            if (addConnection.getApplicationNumber() == null) {
                addConnection.setApplicationNumber(addConnection.getMeesevaApplicationNumber());
                addConnection.setSource(MEESEVA);

                waterConnectionDetailsService.createNewWaterConnection(addConnection, approvalPosition, approvalComment,
                        addConnection.getApplicationType().getCode(), workFlowActionValue, meesevaParams, sourceChannel);
            }
        } else
            waterConnectionDetailsService.createNewWaterConnection(addConnection, approvalPosition, approvalComment,
                    addConnection.getApplicationType().getCode(), workFlowActionValue, sourceChannel);
        final Assignment currentUserAssignment = assignmentService.getPrimaryAssignmentForGivenRange(securityUtils
                .getCurrentUser().getId(), new Date(), new Date());
        String nextDesign = "";
        Assignment assignObj = null;
        List<Assignment> asignList = null;
        if (approvalPosition != null)
            assignObj = assignmentService.getPrimaryAssignmentForPositon(approvalPosition);
        if (assignObj != null) {
            asignList = new ArrayList<>();
            asignList.add(assignObj);
        } else if (assignObj == null && approvalPosition != null)
            asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());
        nextDesign = !asignList.isEmpty() ? asignList.get(0).getDesignation().getName() : "";
        addConnection.getApplicationNumber();
        waterTaxUtils.getApproverName(approvalPosition);
        if(currentUserAssignment!=null)
            currentUserAssignment.getDesignation().getName();

        if (loggedUserIsMeesevaUser)
            return "redirect:/application/generate-meesevareceipt?transactionServiceNumber="
                    + addConnection.getApplicationNumber();
        else
            return "redirect:/application/citizeenAcknowledgement?pathVars=" + addConnection.getApplicationNumber();
    }

}
