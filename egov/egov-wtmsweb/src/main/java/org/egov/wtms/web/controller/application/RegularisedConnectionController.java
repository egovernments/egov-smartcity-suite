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

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.commons.entity.Source.ONLINE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CREATED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.METERED_CHARGES_REASON_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULETYPE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGULARIZE_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAXREASONCODE;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;

import org.egov.commons.entity.Source;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pims.commons.Position;
import org.egov.wtms.application.entity.ApplicationDocuments;
import org.egov.wtms.application.entity.RegularisedConnection;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.service.NewConnectionService;
import org.egov.wtms.application.service.RegularisedConnectionService;
import org.egov.wtms.application.service.ReportGenerationService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.utils.WaterTaxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/application")
public class RegularisedConnectionController extends GenericConnectionController {

    private static final String CURRENTUSER = "currentUser";
    private static final String STATETYPE = "stateType";
    private static final String ADDITIONALRULE = "additionalRule";
    private static final String VALIDIFPTDUEEXISTS = "validateIfPTDueExists";
    private static final String APPROVALPOSITION = "approvalPosition";
    private static final String CONNECTION_PROPERTYID = "connection.propertyIdentifier";
    private static final String CURRENT_DEMAND_REQUIRED = "Please add the current demand for this application using Add/Edit button";

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private WaterTaxUtils waterTaxUtils;

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @Autowired
    private RegularisedConnectionService regularisedConnectionService;

    @Autowired
    private NewConnectionService newConnectionService;

    @Autowired
    private ReportGenerationService reportGenerationService;

    @GetMapping("/regulariseconnection-form/{id}")
    public String getApplicationForm(@ModelAttribute final WaterConnectionDetails waterConnectionDetails,
            @PathVariable final String id, final Model model,
            final HttpServletRequest request) {
        RegularisedConnection regularisedConnection = null;
        Boolean loggedUserIsMeesevaUser;
        if (isNotBlank(id))
            regularisedConnection = regularisedConnectionService.findById(Long.valueOf(id));
        waterConnectionDetails.setApplicationDate(new Date());
        waterConnectionDetails.setConnectionStatus(ConnectionStatus.INPROGRESS);
        waterConnectionDetails.setApplicationType(applicationTypeService.findByCode(REGULARIZE_CONNECTION));
        if (regularisedConnection != null)
            model.addAttribute("propertyId", regularisedConnection.getPropertyIdentifier());
        model.addAttribute("allowIfPTDueExists", waterTaxUtils.isNewConnectionAllowedIfPTDuePresent());
        final WorkflowContainer workFlowContainer = new WorkflowContainer();
        prepareWorkflow(model, waterConnectionDetails, workFlowContainer);
        model.addAttribute(CURRENTUSER, waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
        model.addAttribute(STATETYPE, waterConnectionDetails.getClass().getSimpleName());
        model.addAttribute(ADDITIONALRULE, waterConnectionDetails.getApplicationType().getCode());
        model.addAttribute("documentName", waterTaxUtils.documentRequiredForBPLCategory());
        model.addAttribute("typeOfConnection", REGULARIZE_CONNECTION);
        model.addAttribute("citizenPortalUser", waterTaxUtils.isCitizenPortalUser(securityUtils.getCurrentUser()));
        model.addAttribute("isAnonymousUser", waterTaxUtils.isAnonymousUser(securityUtils.getCurrentUser()));
        loggedUserIsMeesevaUser = waterTaxUtils.isMeesevaUser(securityUtils.getCurrentUser());
        if (loggedUserIsMeesevaUser)
            waterConnectionDetails.setApplicationNumber(request.getParameter("applicationNo"));
        return "regulariseconnection-form";
    }

    @PostMapping("/regulariseconnection-form/{id}")
    public String createRegularisedConnection(@ModelAttribute final WaterConnectionDetails waterConnectionDetails,
            @PathVariable final String id, final BindingResult errors, final Model model, final HttpServletRequest request) {

        final Boolean citizenPortalUser = waterTaxUtils.isCitizenPortalUser(securityUtils.getCurrentUser());
        final Boolean isCSCOperator = waterTaxUtils.isCSCoperator(securityUtils.getCurrentUser());
        final Boolean isAnonymousUser = waterTaxUtils.isAnonymousUser(securityUtils.getCurrentUser());
        final Boolean loggedUserIsMeesevaUser = waterTaxUtils.isMeesevaUser(securityUtils.getCurrentUser());
        if (!isCSCOperator && !citizenPortalUser && !loggedUserIsMeesevaUser && !isAnonymousUser) {
            final Boolean isJuniorAsstOrSeniorAsst = waterTaxUtils
                    .isLoggedInUserJuniorOrSeniorAssistant(securityUtils.getCurrentUser().getId());
            if (!isJuniorAsstOrSeniorAsst)
                throw new ValidationException("err.creator.application");
        }
        RegularisedConnection connection = null;
        if (id != null)
            connection = regularisedConnectionService.findById(Long.parseLong(id));
        if (connection != null) {
            waterConnectionDetails.setApplicationDate(connection.getApplicationDate());
            waterConnectionDetails.setApplicationNumber(connection.getApplicationNumber());
            if ("SURVEY".equals(connection.getSource()))
                waterConnectionDetails.setSource(Source.SURVEY);
        }
        if (ConnectionType.NON_METERED.equals(waterConnectionDetails.getConnectionType()))
            waterConnectionDetailsService.validateWaterRateAndDonationHeader(waterConnectionDetails);
        final List<ApplicationDocuments> applicationDocuments = new ArrayList<>();
        final String documentsRequired = waterTaxUtils.documentRequiredForBPLCategory();
        final int i = 0;
        if (!waterConnectionDetails.getApplicationDocs().isEmpty())
            for (final ApplicationDocuments applicationDocument : waterConnectionDetails.getApplicationDocs())
                newConnectionService.validateDocuments(applicationDocuments, applicationDocument, i, errors,
                        waterConnectionDetails.getCategory().getId(), documentsRequired);

        if (waterConnectionDetails.getState() == null)
            waterConnectionDetails.setStatus(waterTaxUtils.getStatusByCodeAndModuleType(APPLICATION_STATUS_CREATED, MODULETYPE));
        if (errors.hasErrors()) {
            waterConnectionDetails.setApplicationDate(new Date());
            model.addAttribute(VALIDIFPTDUEEXISTS, waterTaxUtils.isNewConnectionAllowedIfPTDuePresent());
            final WorkflowContainer workflowContainer = new WorkflowContainer();
            workflowContainer.setAdditionalRule(waterConnectionDetails.getApplicationType().getCode());
            prepareWorkflow(model, waterConnectionDetails, workflowContainer);
            model.addAttribute(ADDITIONALRULE, waterConnectionDetails.getApplicationType().getCode());
            model.addAttribute(CURRENTUSER, waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
            model.addAttribute("approvalPosOnValidate", request.getParameter(APPROVALPOSITION));
            model.addAttribute("typeOfConnection", REGULARIZE_CONNECTION);
            model.addAttribute(STATETYPE, waterConnectionDetails.getClass().getSimpleName());
            model.addAttribute("documentName", waterTaxUtils.documentRequiredForBPLCategory());
            return "regulariseconnection-form";
        }
        waterConnectionDetails.getApplicationDocs().clear();
        waterConnectionDetails.setApplicationDocs(applicationDocuments);
        processAndStoreApplicationDocuments(waterConnectionDetails);
        Long approvalPosition = 0l;
        String approvalComment = "";
        String workFlowAction = "";
        if (request.getParameter("approvalComent") != null)
            approvalComment = request.getParameter("approvalComent");
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");
        if (isNotBlank(request.getParameter(APPROVALPOSITION)))
            approvalPosition = Long.valueOf(request.getParameter(APPROVALPOSITION));

        final Boolean applicationByOthers = waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser());
        if (applicationByOthers != null && applicationByOthers.equals(true) || citizenPortalUser || isAnonymousUser) {
            final Position userPosition = waterTaxUtils.getZonalLevelClerkForLoggedInUser(
                    waterConnectionDetails.getConnection().getPropertyIdentifier());

            if (userPosition == null) {
                model.addAttribute(VALIDIFPTDUEEXISTS, waterTaxUtils.isNewConnectionAllowedIfPTDuePresent());
                final WorkflowContainer workflowContainer = new WorkflowContainer();
                prepareWorkflow(model, waterConnectionDetails, workflowContainer);
                model.addAttribute(ADDITIONALRULE, waterConnectionDetails.getApplicationType().getCode());
                model.addAttribute("approvalPosOnValidate", request.getParameter(APPROVALPOSITION));
                model.addAttribute(CURRENTUSER, waterTaxUtils.getCurrentUserRole(securityUtils.getCurrentUser()));
                model.addAttribute(STATETYPE, waterConnectionDetails.getClass().getSimpleName());
                errors.rejectValue(CONNECTION_PROPERTYID, "err.validate.connection.user.mapping",
                        "err.validate.connection.user.mapping");
                model.addAttribute("noJAORSAMessage", "No JA/SA exists to forward the application.");
            } else
                approvalPosition = userPosition.getId();
        }

        if (isAnonymousUser)
            waterConnectionDetails.setSource(ONLINE);

        if (citizenPortalUser && waterConnectionDetails.getSource() == null)
            waterConnectionDetails.setSource(waterTaxUtils.setSourceOfConnection(securityUtils.getCurrentUser()));

        if (loggedUserIsMeesevaUser) {
            final HashMap<String, String> meesevaParams = new HashMap<>();
            meesevaParams.put("APPLICATIONNUMBER", waterConnectionDetails.getMeesevaApplicationNumber());
            if (waterConnectionDetails.getApplicationNumber() == null) {
                waterConnectionDetails.setApplicationNumber(waterConnectionDetails.getMeesevaApplicationNumber());
                waterConnectionDetails.setSource(Source.MEESEVA);
            }
        }
        final String sourceChannel = request.getParameter("Source");
        regularisedConnectionService.updateCurrentWorkFlow(connection);
        waterConnectionDetailsService.createNewWaterConnection(waterConnectionDetails,
                approvalPosition, approvalComment, waterConnectionDetails.getApplicationType().getCode(),
                workFlowAction, sourceChannel);

        if (loggedUserIsMeesevaUser)
            return "redirect:/application/generate-meesevareceipt?transactionServiceNumber="
                    + waterConnectionDetails.getApplicationNumber();
        else
            return "redirect:/application/citizeenAcknowledgement?pathVars=" + waterConnectionDetails.getApplicationNumber();
    }

    @GetMapping(value = "/regulariseconnection/demandnote-view/{applicationNumber}")
    @ResponseBody
    public ResponseEntity<byte[]> viewDemandNote(@PathVariable final String applicationNumber, final HttpSession session) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByApplicationNumber(applicationNumber);
        if (waterConnectionDetails == null)
            throw new ApplicationRuntimeException("err.application.not.exist");
        final EgDemand demand = waterTaxUtils.getCurrentDemand(waterConnectionDetails).getDemand();
        boolean isDemandPresent = false;
        for (final EgDemandDetails demandDetails : demand.getEgDemandDetails())
            if (WATERTAXREASONCODE.equalsIgnoreCase(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()) ||
                    METERED_CHARGES_REASON_CODE
                            .equalsIgnoreCase(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                isDemandPresent = true;
        if (!isDemandPresent) {
            String errorMessage = "";
            errorMessage = "<html><body><p style='color:red;border:1px solid gray;padding:15x;'>" + CURRENT_DEMAND_REQUIRED
                    + "</p></body></html>";
            final byte[] byteData = errorMessage.getBytes();
            return new ResponseEntity<>(byteData, HttpStatus.CREATED);
        }
        final ReportOutput reportOutput = reportGenerationService.generateRegulariseConnDemandNote(waterConnectionDetails);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline:filename=" + waterConnectionDetails.getEstimationNumber() + ".pdf");
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/regulariseconnection/proceedings-view/{applicationNumber}")
    @ResponseBody
    public ResponseEntity<byte[]> viewProceedings(@PathVariable("applicationNumber") final String applicationNumber) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService
                .findByApplicationNumber(applicationNumber);
        ReportOutput reportOutput = null;
        if (waterConnectionDetails == null)
            throw new ApplicationRuntimeException("err.application.not.exist");
        else
            reportOutput = reportGenerationService.generateRegulariseConnProceedings(waterConnectionDetails);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("content-disposition", "inline:filename=" + waterConnectionDetails.getWorkOrderNumber() + ".pdf");
        return new ResponseEntity<>(reportOutput.getReportOutputData(), headers, HttpStatus.CREATED);
    }

}
