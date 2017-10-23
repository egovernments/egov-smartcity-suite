/* eGov suite of products aim to improve the internal efficiency,transparency,
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

package org.egov.mrs.web.controller.application.registration;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.mrs.application.MarriageConstants.ADDITIONAL_RULE_REGISTRATION;
import static org.egov.mrs.application.MarriageConstants.APPLICATION_NUMBER;
import static org.egov.mrs.application.MarriageConstants.APPROVAL_COMMENT;
import static org.egov.mrs.application.MarriageConstants.APPROVED;
import static org.egov.mrs.application.MarriageConstants.CREATED;
import static org.egov.mrs.application.MarriageConstants.FILESTORE_MODULECODE;
import static org.egov.mrs.application.MarriageConstants.FILE_STORE_ID_APPLICATION_NUMBER;
import static org.egov.mrs.application.MarriageConstants.JUNIOR_SENIOR_ASSISTANCE_APPROVAL_PENDING;
import static org.egov.mrs.application.MarriageConstants.MARRIAGEREGISTRATION_DAYS_VALIDATION;
import static org.egov.mrs.application.MarriageConstants.MODULE_NAME;
import static org.egov.mrs.application.MarriageConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.mrs.application.MarriageConstants.WFLOW_ACTION_STEP_CANCEL;
import static org.egov.mrs.application.MarriageConstants.WFLOW_ACTION_STEP_DIGISIGN;
import static org.egov.mrs.application.MarriageConstants.WFLOW_ACTION_STEP_PRINTCERTIFICATE;
import static org.egov.mrs.application.MarriageConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.mrs.application.MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN;
import static org.egov.mrs.application.MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_PRINTCERT;
import static org.egov.mrs.application.MarriageConstants.WFLOW_PENDINGACTION_DIGISIGNPENDING;
import static org.egov.mrs.application.MarriageConstants.WFLOW_PENDINGACTION_PRINTCERTIFICATE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.repository.FileStoreMapperRepository;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.mrs.application.MarriageUtils;
import org.egov.mrs.application.service.MarriageCertificateService;
import org.egov.mrs.application.service.workflow.RegistrationWorkflowService;
import org.egov.mrs.domain.entity.MarriageCertificate;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.enums.MarriageCertificateType;
import org.egov.mrs.service.es.MarriageRegistrationUpdateIndexesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller to correct the registration data
 *
 * @author nayeem
 */

@Controller
@RequestMapping(value = "/registration")
public class UpdateMarriageRegistrationController extends MarriageRegistrationController {

    private static final String PENDING_ACTIONS = "pendingActions";
    private static final String MARRIAGE_REGISTRATION = "marriageRegistration";
    private static final Logger LOG = Logger.getLogger(UpdateMarriageRegistrationController.class);
    private static final String MRG_REGISTRATION_EDIT = "registration-correction";
    private static final String MRG_REGISTRATION_EDIT_APPROVED = "registration-update-approved";
    private static final String MRG_REGISTRATION_SUCCESS = "registration-ack";

    @Autowired
    private FileStoreService fileStoreService;
    @Autowired
    private MarriageFormValidator marriageFormValidator;
    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    protected MarriageUtils marriageUtils;
    @Autowired
    protected FileStoreMapperRepository fileStoreMapperRepository;
    @Autowired
    private MarriageRegistrationUpdateIndexesService marriageRegistrationUpdateIndexesService;
    @Autowired
    private RegistrationWorkflowService registrationWorkflowService;
    @Autowired
    private MarriageCertificateService marriageCertificateService;

    private static final Logger LOGGER = Logger.getLogger(UpdateMarriageRegistrationController.class);

    @RequestMapping(value = "/update/{id}", method = GET)
    public String showRegistration(@PathVariable final Long id, final Model model) {
        final MarriageRegistration marriageRegistration = marriageRegistrationService.get(id);
        buildMrgRegistrationUpdateResult(marriageRegistration, model);
        return MRG_REGISTRATION_EDIT;
    }

    @RequestMapping(value = "/modify-approved/{id}", method = GET)
    public String editApprovedRegistration(@PathVariable final Long id, final Model model) {
        final MarriageRegistration marriageRegistration = marriageRegistrationService.get(id);
        buildMrgRegistrationUpdateResult(marriageRegistration, model);
        if (LOGGER.isInfoEnabled())
            LOGGER.info(".........finished build marriage registration for update........ ");
        return MRG_REGISTRATION_EDIT_APPROVED;
    }

    private void buildMrgRegistrationUpdateResult(final MarriageRegistration marriageRegistration, final Model model) {
        if (LOGGER.isInfoEnabled())
            LOGGER.info("..........InsidebuildMrgRegistrationUpdateResult........ " + marriageRegistration.getApplicationNo());
        if (!marriageRegistration.isLegacy()) {
            if (LOGGER.isInfoEnabled())
                LOGGER.info("..........No legacy  entry........ ");
            final AppConfigValues allowValidation = marriageFeeService.getDaysValidationAppConfValue(
                    MODULE_NAME, MARRIAGEREGISTRATION_DAYS_VALIDATION);
            model.addAttribute("allowDaysValidation",
                    allowValidation != null && !allowValidation.getValue().isEmpty() ? allowValidation.getValue()
                            : "NO");
        } else
            model.addAttribute("allowDaysValidation", "NO");
        if (LOGGER.isInfoEnabled())
            LOGGER.info(".........prepareDocumentsForView........ ");
        marriageRegistrationService.prepareDocumentsForView(marriageRegistration);
        marriageApplicantService.prepareDocumentsForView(marriageRegistration.getHusband());
        marriageApplicantService.prepareDocumentsForView(marriageRegistration.getWife());
        model.addAttribute("applicationHistory", marriageRegistrationService.getHistory(marriageRegistration));
        if (LOGGER.isInfoEnabled())
            LOGGER.info(".........before prepareWorkFlowForNewMarriageRegistration........ ");
        prepareWorkFlowForNewMarriageRegistration(marriageRegistration, model);

        marriageRegistration.getWitnesses().forEach(
                witness -> {
                    try {
                        if (witness.getPhotoFileStore() != null) {
                            final File file = fileStoreService.fetch(witness.getPhotoFileStore().getFileStoreId(),
                                    FILESTORE_MODULECODE);
                            if (file != null)
                                witness.setEncodedPhoto(Base64.getEncoder().encodeToString(
                                        FileCopyUtils.copyToByteArray(file)));
                        }
                    } catch (final IOException e) {
                        LOG.error("Error while preparing the document for view", e);
                    }
                });
        if (LOGGER.isInfoEnabled())
            LOGGER.info(".........after prepare Witnesses........ ");
        model.addAttribute(MARRIAGE_REGISTRATION, marriageRegistration);
    }

    private void prepareWorkFlowForNewMarriageRegistration(final MarriageRegistration registration, final Model model) {
        final WorkflowContainer workFlowContainer = new WorkflowContainer();
        // Set pending actions based on digitalsignature configuration value
        if (registration.getStatus().getCode().equalsIgnoreCase(APPROVED))
            if (marriageUtils.isDigitalSignEnabled()) {
                model.addAttribute(PENDING_ACTIONS, WFLOW_PENDINGACTION_DIGISIGNPENDING);
                workFlowContainer.setPendingActions(WFLOW_PENDINGACTION_DIGISIGNPENDING);
            } else {
                model.addAttribute(PENDING_ACTIONS, WFLOW_PENDINGACTION_PRINTCERTIFICATE);
                workFlowContainer.setPendingActions(WFLOW_PENDINGACTION_PRINTCERTIFICATE);
            }
        workFlowContainer.setAdditionalRule(ADDITIONAL_RULE_REGISTRATION);
        prepareWorkflow(model, registration, workFlowContainer);
        model.addAttribute("additionalRule", ADDITIONAL_RULE_REGISTRATION);
        model.addAttribute("stateType", registration.getClass().getSimpleName());
        if (registration.getCurrentState() != null)
            model.addAttribute("currentState", registration.getCurrentState().getValue());
        model.addAttribute("isDigitalSignEnabled", marriageUtils.isDigitalSignEnabled());

        if (registration.getStatus().getCode().equalsIgnoreCase(CREATED)
                && JUNIOR_SENIOR_ASSISTANCE_APPROVAL_PENDING.equalsIgnoreCase(registration.getState().getNextAction())) {
            model.addAttribute("nextActn", JUNIOR_SENIOR_ASSISTANCE_APPROVAL_PENDING);
            model.addAttribute("isReassignEnabled", marriageUtils.isReassignEnabled());
        }

    }

    @RequestMapping(value = "/update", method = POST)
    public String updateRegistration(final WorkflowContainer workflowContainer,
            @ModelAttribute final MarriageRegistration marriageRegistration, final Model model,
            final HttpServletRequest request, final BindingResult errors) throws IOException {

        String workFlowAction = EMPTY;
        if (isNotBlank(request.getParameter("workFlowAction")))
            workFlowAction = request.getParameter("workFlowAction");

        validateApplicationDate(marriageRegistration, errors);
        marriageFormValidator.validate(marriageRegistration, errors, "registration");
        MarriageRegistration serialNoExist = null;
        if (CREATED.equalsIgnoreCase(marriageRegistration.getStatus().getCode())
                && isNotBlank(marriageRegistration.getSerialNo())) {
            serialNoExist = marriageRegistrationService.findBySerialNo(marriageRegistration.getSerialNo());
        }
        if (errors.hasErrors() || serialNoExist != null) {
            buildMrgRegistrationUpdateResult(marriageRegistration, model);
            if (serialNoExist != null) {
                model.addAttribute("serialNoExists",
                        "Entered serial number is already exists, please check and enter valid value and serial number must be unique.");
            }
            return MRG_REGISTRATION_EDIT;
        }

        String message = EMPTY;
        String approverName;
        String nextDesignation;
        if (isNotBlank(workFlowAction)) {
            workflowContainer.setWorkFlowAction(workFlowAction);
            final Assignment wfInitiator = registrationWorkflowService.getWorkFlowInitiator(marriageRegistration);
            approverName = wfInitiator.getEmployee().getName();
            nextDesignation = wfInitiator.getDesignation().getName();
            workflowContainer.setApproverComments(request.getParameter("approvalComent"));
            if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {
                marriageRegistrationService.rejectRegistration(marriageRegistration, workflowContainer);
                message = messageSource.getMessage(
                        "msg.rejected.registration",
                        new String[] { marriageRegistration.getApplicationNo(),
                                approverName.concat("~").concat(nextDesignation) },
                        null);
            } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_CANCEL)) {
                marriageRegistrationService.rejectRegistration(marriageRegistration, workflowContainer);
                message = messageSource.getMessage("msg.cancelled.registration",
                        new String[] { marriageRegistration.getApplicationNo(), null }, null);
            } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE)) {
                // If digital signature is configured, after approve appl shld remain in commissioner inbox for digital signature
                // otherwise gets fwded to creator for print certificate.
                if (marriageUtils.isDigitalSignEnabled()) {
                    model.addAttribute(PENDING_ACTIONS, WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN);
                    workflowContainer.setPendingActions(WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN);
                    marriageRegistrationService.approveRegistration(marriageRegistration, workflowContainer, request);
                    message = messageSource.getMessage("msg.approved.registration",
                            new String[] { marriageRegistration.getRegistrationNo() }, null);
                } else {
                    model.addAttribute(PENDING_ACTIONS, WFLOW_PENDINGACTION_APPRVLPENDING_PRINTCERT);
                    workflowContainer.setPendingActions(WFLOW_PENDINGACTION_APPRVLPENDING_PRINTCERT);
                    marriageRegistrationService.approveRegistration(marriageRegistration, workflowContainer, request);
                    message = messageSource.getMessage(
                            "msg.approved.registration",
                            new String[] { marriageRegistration.getRegistrationNo()
                            },
                            null);
                }
            } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_DIGISIGN)) {
                // Generates certificate, sends for digital sign and calls callback url for workflow transition.
                MarriageCertificate marriageCertificate = null;
                final List<MarriageCertificate> certificateIssued = marriageCertificateService
                        .getGeneratedRegCertificate(marriageRegistration);
                if (!certificateIssued.isEmpty()) {
                    for (final MarriageCertificate certificateobj : certificateIssued) {
                        marriageCertificate = certificateobj;
                    }
                } else {
                    marriageCertificate = marriageRegistrationService
                            .generateMarriageCertificate(marriageRegistration, workflowContainer, request);
                }
                model.addAttribute("fileStoreIds", marriageCertificate.getFileStore().getFileStoreId());
                model.addAttribute("ulbCode", ApplicationThreadLocals.getCityCode());
                // Adding applicationNo and its filestoreid to be digitally signed to session
                final HttpSession session = request.getSession();
                session.setAttribute(APPROVAL_COMMENT, request.getParameter("approvalComent"));
                session.setAttribute(APPLICATION_NUMBER, marriageCertificate.getRegistration()
                        .getApplicationNo());
                final Map<String, String> fileStoreIdsApplicationNoMap = new HashMap<>();
                fileStoreIdsApplicationNoMap.put(marriageCertificate.getFileStore().getFileStoreId(),
                        marriageCertificate.getRegistration().getApplicationNo());
                session.setAttribute(FILE_STORE_ID_APPLICATION_NUMBER, fileStoreIdsApplicationNoMap);
                model.addAttribute("isDigitalSignatureEnabled", marriageUtils.isDigitalSignEnabled());
                return "marriagereg-digitalsignature";
            } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_PRINTCERTIFICATE)) {
                marriageRegistrationService.printCertificate(marriageRegistration, workflowContainer, request);
                message = messageSource.getMessage("msg.printcertificate.registration", null, null);
            } else {
                approverName = request.getParameter("approverName");
                nextDesignation = request.getParameter("nextDesignation");
                workflowContainer.setApproverPositionId(Long.valueOf(request.getParameter("approvalPosition")));
                marriageRegistrationService.forwardRegistration(marriageRegistration, workflowContainer);
                message = messageSource.getMessage("msg.forward.registration", new String[] {
                        approverName.concat("~").concat(nextDesignation), marriageRegistration.getApplicationNo() },
                        null);
            }
        }
        // On print certificate, output registration certificate
        if (isNotBlank(workFlowAction) && workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_PRINTCERTIFICATE))
            return "redirect:/certificate/registration?id=" + marriageRegistration.getId();

        model.addAttribute("message", message);
        return MRG_REGISTRATION_SUCCESS;
    }

    /**
     * @description call back url to do workflow transition after certificate is digitally signed
     * @param request
     * @param model
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/digiSignWorkflow")
    public String digiSignTransitionWorkflow(final HttpServletRequest request, final Model model) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("..........Inside Digital Signature Transition : Registration........");
        final String fileStoreIds = request.getParameter("fileStoreId");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("........fileStoreIds.........." + fileStoreIds);
        final String[] fileStoreIdArr = fileStoreIds.split(",");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("........fileStoreIdArr.........." + fileStoreIdArr.length);
        final HttpSession session = request.getSession();
        final String approvalComent = (String) session.getAttribute(APPROVAL_COMMENT);
        // Gets the digitally signed applicationNo and its filestoreid from session
        final Map<String, String> appNoFileStoreIdsMap = (Map<String, String>) session
                .getAttribute(FILE_STORE_ID_APPLICATION_NUMBER);
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("........appNoFileStoreIdsMap....size......" + appNoFileStoreIdsMap.size());
        MarriageRegistration marriageRegistrationObj = null;
        WorkflowContainer workflowContainer;
        for (final String fileStoreId : fileStoreIdArr) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("........Inside for loop......");
            final String applicationNumber = appNoFileStoreIdsMap.get(fileStoreId);
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("........applicationNumber......" + applicationNumber);
            if (isNotBlank(applicationNumber)) {
                workflowContainer = new WorkflowContainer();
                workflowContainer.setApproverComments(approvalComent);
                workflowContainer.setWorkFlowAction(WFLOW_ACTION_STEP_DIGISIGN);
                workflowContainer.setPendingActions(WFLOW_PENDINGACTION_DIGISIGNPENDING);
                marriageRegistrationObj = marriageRegistrationService.findByApplicationNo(applicationNumber);
                marriageRegistrationService.digiSignCertificate(marriageRegistrationObj, workflowContainer, request);
            }
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("........outside for loop......");

        if (marriageRegistrationObj != null) {
            final String message = messageSource.getMessage("msg.digisign.success.registration", null, null);
            model.addAttribute("successMessage", message);
        }
        model.addAttribute("objectType", MarriageCertificateType.REGISTRATION.toString());
        model.addAttribute("fileStoreId", fileStoreIdArr.length == 1 ? fileStoreIdArr[0] : "");
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("..........END of Digital Signature Transition : Registration........");
        return "mrdigitalsignature-success";
    }

    /**
     * @description download digitally signed certificate.
     * @param request
     * @param response
     */
    @RequestMapping(value = "/downloadSignedCertificate")
    public void downloadRegDigiSignedCertificate(final HttpServletRequest request, final HttpServletResponse response) {
        final String signedFileStoreId = request.getParameter("signedFileStoreId");
        try {
            marriageUtils.downloadSignedCertificate(signedFileStoreId, response);
        } catch (final ApplicationRuntimeException ex) {
            throw new ApplicationRuntimeException("Exception while downloading file : " + ex);
        }
    }

    /**
     * @description Modify registered marriage applications
     * @param id
     * @param registration
     * @param model
     * @param request
     * @param errors
     * @return
     */
    @RequestMapping(value = "/modify-approved", method = POST)
    public String modifyRegisteredApplication(@RequestParam final Long id,
            @ModelAttribute final MarriageRegistration registration, final Model model,
            final HttpServletRequest request, final BindingResult errors) {

        validateApplicationDate(registration, errors);
        if (errors.hasErrors()) {
            model.addAttribute(MARRIAGE_REGISTRATION, registration);
            return MRG_REGISTRATION_EDIT_APPROVED;
        }
        final MarriageRegistration marriageRegistration = marriageRegistrationService.updateRegistration(registration);
        marriageRegistrationUpdateIndexesService.updateIndexes(marriageRegistration);
        model.addAttribute("message", messageSource.getMessage("msg.update.registration", new String[] {
                registration.getApplicationNo(), registration.getRegistrationNo() }, null));
        return MRG_REGISTRATION_SUCCESS;
    }

    /**
     * @param serialNo
     * @return
     */
    @RequestMapping(value = "/checkunique-serialno", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public boolean uniqueSerialNo(@RequestParam final String serialNo) {
        return isNotBlank(serialNo) && marriageRegistrationService.findBySerialNo(serialNo) != null ? true : false;
    }

    /**
     * @description To show preview of marriage certificate before digital sign
     * @param id
     * @param session
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/viewCertificate/{id}", method = GET)
    @ResponseBody
    public ResponseEntity<byte[]> viewMarriageRegistrationReport(@PathVariable final Long id, final HttpSession session,
            final HttpServletRequest request) throws IOException {
        return marriageUtils.viewReport(id, MarriageCertificateType.REGISTRATION.toString(), session, request);
    }
}