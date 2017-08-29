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

package org.egov.mrs.web.controller.application.reissue;

import java.io.IOException;
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
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.application.MarriageUtils;
import org.egov.mrs.application.service.MarriageCertificateService;
import org.egov.mrs.application.service.workflow.RegistrationWorkflowService;
import org.egov.mrs.domain.entity.MarriageCertificate;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.enums.MarriageCertificateType;
import org.egov.mrs.domain.service.MarriageApplicantService;
import org.egov.mrs.domain.service.MarriageDocumentService;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.mrs.domain.service.ReIssueService;
import org.egov.mrs.masters.service.MarriageRegistrationUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller to correct the registration data
 *
 * @author nayeem
 *
 */

@Controller
@RequestMapping(value = "/reissue")
public class UpdateMrgReIssueController extends GenericWorkFlowController {

    private static final String IS_EMPLOYEE = "isEmployee";

    @Autowired
    private ReIssueService reIssueService;
    @Autowired
    private MarriageApplicantService marriageApplicantService;
    @Autowired
    private MarriageDocumentService marriageDocumentService;
    @Autowired
    protected ResourceBundleMessageSource messageSource;
    @Autowired
    private MarriageRegistrationUnitService marriageRegistrationUnitService;
    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    private MarriageRegistrationService marriageRegistrationService;
    @Autowired
    private MarriageUtils marriageUtils;
    @Autowired
    private RegistrationWorkflowService registrationWorkflowService;
    @Autowired
    private MarriageCertificateService marriageCertificateService;

    @Autowired
    private SecurityUtils securityUtils;

    private static final Logger LOGGER = Logger.getLogger(UpdateMrgReIssueController.class);

    public void prepareNewForm(final Model model) {
        model.addAttribute("marriageRegistrationUnit",
                marriageRegistrationUnitService.getActiveRegistrationunit());
        model.addAttribute(IS_EMPLOYEE, registrationWorkflowService.isEmployee(securityUtils.getCurrentUser()));
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String showReIssueForm(@PathVariable final Long id, final Model model) {
        final ReIssue reIssue = reIssueService.get(id);
        model.addAttribute("documents", marriageDocumentService.getIndividualDocuments());

        marriageRegistrationService.prepareDocumentsForView(reIssue.getRegistration());
        marriageApplicantService.prepareDocumentsForView(reIssue.getRegistration().getHusband());
        marriageApplicantService.prepareDocumentsForView(reIssue.getRegistration().getWife());
        marriageApplicantService.prepareDocumentsForView(reIssue.getApplicant());
        model.addAttribute("applicationHistory",
                reIssueService.getHistory(reIssue));
        prepareNewForm(model);
        prepareWorkFlowForReIssue(reIssue, model);
        model.addAttribute("reIssue", reIssue);
        if (reIssue.getStatus().getCode().equalsIgnoreCase(ReIssue.ReIssueStatus.REJECTED.toString()))
            return "reissue-form";
        return "reissue-view";
    }

    private void prepareWorkFlowForReIssue(final ReIssue reIssue, final Model model) {
        final WorkflowContainer workFlowContainer = new WorkflowContainer();

        // Set pending actions based on digitalsignature configuration value
        if (reIssue.getStatus().getCode().equalsIgnoreCase(MarriageConstants.APPROVED))
            if (marriageUtils.isDigitalSignEnabled()) {
                model.addAttribute("pendingActions", MarriageConstants.WFLOW_PENDINGACTION_DIGISIGNPENDING);
                workFlowContainer.setPendingActions(MarriageConstants.WFLOW_PENDINGACTION_DIGISIGNPENDING);
            } else {
                model.addAttribute("pendingActions", MarriageConstants.WFLOW_PENDINGACTION_PRINTCERTIFICATE);
                workFlowContainer.setPendingActions(MarriageConstants.WFLOW_PENDINGACTION_PRINTCERTIFICATE);
            }
        workFlowContainer.setAdditionalRule(MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        prepareWorkflow(model, reIssue, workFlowContainer);
        model.addAttribute("additionalRule", MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        model.addAttribute("stateType", reIssue.getClass().getSimpleName());
        if (reIssue.getStatus().getCode().equalsIgnoreCase(MarriageConstants.CREATED)
                && MarriageConstants.JUNIOR_SENIOR_ASSISTANCE_APPROVAL_PENDING
                        .equalsIgnoreCase(reIssue.getState().getNextAction()))
            model.addAttribute("nextActn", MarriageConstants.JUNIOR_SENIOR_ASSISTANCE_APPROVAL_PENDING);
          model.addAttribute("isReassignEnabled", marriageUtils.isReassignEnabled());
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateReIssue(@RequestParam final Long id, @ModelAttribute final ReIssue reIssue,
            @ModelAttribute final WorkflowContainer workflowContainer,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors) throws IOException {

        String workFlowAction = org.apache.commons.lang.StringUtils.EMPTY;
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");

        if (errors.hasErrors())
            return "reissue-view";

        String message = org.apache.commons.lang.StringUtils.EMPTY;
        String approverName;
        String nextDesignation;

        if (workFlowAction != null && !workFlowAction.isEmpty()) {
            workflowContainer.setWorkFlowAction(workFlowAction);
            final Assignment wfInitiator = registrationWorkflowService.getWorkFlowInitiatorForReissue(reIssue);
            approverName = wfInitiator.getEmployee().getName();
            nextDesignation = wfInitiator.getDesignation().getName();
            workflowContainer.setApproverComments(request.getParameter("approvalComent"));
            if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_REJECT)) {

                reIssueService.rejectReIssue(reIssue, workflowContainer, request);
                message = messageSource.getMessage("msg.rejected.reissue", new String[] { reIssue.getApplicationNo(),
                        approverName.concat("~").concat(nextDesignation) }, null);
            } else if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_CANCEL_REISSUE)) {
                reIssueService.rejectReIssue(reIssue, workflowContainer, request);
                message = messageSource.getMessage("msg.cancelled.reissue", new String[] { reIssue.getApplicationNo(), null },
                        null);
            } else if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_APPROVE)) {
                // If digital signature is configured, after approve appl shld remain in commissioner inbox for digital signature
                // otherwise gets fwded to creator for print certificate.
                if (marriageUtils.isDigitalSignEnabled()) {
                    model.addAttribute("pendingActions", MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN);
                    workflowContainer.setPendingActions(MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN);
                    reIssueService.approveReIssue(reIssue, workflowContainer);
                    message = messageSource.getMessage("msg.approved.reissue",
                            new String[] { reIssue.getApplicationNo() }, null);
                } else {
                    model.addAttribute("pendingActions", MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_PRINTCERT);
                    workflowContainer.setPendingActions(MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_PRINTCERT);
                    reIssueService.approveReIssue(reIssue, workflowContainer);
                    message = messageSource.getMessage(
                            "msg.approved.forwarded.reissue",
                            new String[] { reIssue.getApplicationNo(),
                                    approverName.concat("~").concat(nextDesignation) },
                            null);
                }
            } else if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_DIGISIGN)) {
                // Generates certificate, sends for digital sign and calls callback url for workflow transition.
                MarriageCertificate marriageCertificate = null;
                final List<MarriageCertificate> certificateReIssued = marriageCertificateService
                        .getGeneratedReIssueCertificate(reIssue);
                if (certificateReIssued != null && !certificateReIssued.isEmpty())
                    for (final MarriageCertificate certificateobj : certificateReIssued)
                        marriageCertificate = certificateobj;
                else
                    marriageCertificate = reIssueService
                            .generateReIssueCertificate(reIssue, workflowContainer, request);

                model.addAttribute("fileStoreIds", marriageCertificate.getFileStore().getFileStoreId());
                model.addAttribute("ulbCode", ApplicationThreadLocals.getCityCode());
                // Adding applicationNo and its filestoreid to be digitally signed to session
                final HttpSession session = request.getSession();
                session.setAttribute(MarriageConstants.APPROVAL_COMMENT, request.getParameter("approvalComent"));
                session.setAttribute(MarriageConstants.APPLICATION_NUMBER, marriageCertificate.getReIssue()
                        .getApplicationNo());
                final Map<String, String> fileStoreIdsApplicationNoMap = new HashMap<String, String>();
                fileStoreIdsApplicationNoMap.put(marriageCertificate.getFileStore().getFileStoreId(),
                        marriageCertificate.getReIssue().getApplicationNo());
                session.setAttribute(MarriageConstants.FILE_STORE_ID_APPLICATION_NUMBER, fileStoreIdsApplicationNoMap);
                model.addAttribute("isDigitalSignatureEnabled", marriageUtils.isDigitalSignEnabled());
                return "reissue-digitalsignature";
            } else if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_PRINTCERTIFICATE)) {
                reIssueService.printCertificate(reIssue, workflowContainer, request);
                message = messageSource.getMessage("msg.printcerificate.reissue", null, null);
            } else {
                approverName = request.getParameter("approverName");
                nextDesignation = request.getParameter("nextDesignation");
                workflowContainer.setApproverPositionId(Long.valueOf(request.getParameter("approvalPosition")));
                reIssueService.forwardReIssue(id, reIssue, workflowContainer);
                message = messageSource.getMessage("msg.forward.reissue",
                        new String[] { approverName.concat("~").concat(nextDesignation), reIssue.getApplicationNo() }, null);
            }
        }
        // On print certificate, output registration certificate
        if (workFlowAction != null && !workFlowAction.isEmpty()
                && workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_PRINTCERTIFICATE))
            return "redirect:/certificate/reissue?id="
                    + reIssue.getId();

        model.addAttribute("message", message);
        return "reissue-ack";
    }

    @RequestMapping(value = "/digiSignWorkflow")
    public String digiSignTransitionWorkflow(final HttpServletRequest request, final Model model) throws IOException {
        LOGGER.debug("..........Inside Digital Signature Transition : ReIssue........");
        final String fileStoreIds = request.getParameter("fileStoreId");
        LOGGER.debug("........fileStoreIds.........." + fileStoreIds);
        final String[] fileStoreIdArr = fileStoreIds.split(",");
        LOGGER.debug("........fileStoreIdArr.........." + fileStoreIdArr.length);
        final HttpSession session = request.getSession();
        final String approvalComent = (String) session.getAttribute(MarriageConstants.APPROVAL_COMMENT);
        // Gets the digitally signed applicationNo and its filestoreid from session
        final Map<String, String> appNoFileStoreIdsMap = (Map<String, String>) session
                .getAttribute(MarriageConstants.FILE_STORE_ID_APPLICATION_NUMBER);
        LOGGER.debug("........appNoFileStoreIdsMap....size......" + appNoFileStoreIdsMap.size());
        ReIssue reIssueObj = null;
        WorkflowContainer workflowContainer;
        for (final String fileStoreId : fileStoreIdArr) {
            LOGGER.debug("........Inside for loop......");
            final String applicationNumber = appNoFileStoreIdsMap.get(fileStoreId);
            LOGGER.debug("........applicationNumber......" + applicationNumber);
            if (null != applicationNumber && !applicationNumber.isEmpty()) {
                workflowContainer = new WorkflowContainer();
                workflowContainer.setApproverComments(approvalComent);
                workflowContainer.setWorkFlowAction(MarriageConstants.WFLOW_ACTION_STEP_DIGISIGN);
                workflowContainer.setPendingActions(MarriageConstants.WFLOW_PENDINGACTION_DIGISIGNPENDING);
                reIssueObj = reIssueService.findByApplicationNo(applicationNumber);
                reIssueService.digiSignCertificate(reIssueObj, workflowContainer, request);
            }
        }
        LOGGER.debug("........outside for loop......");
        final Assignment wfInitiator = assignmentService.getPrimaryAssignmentForUser(reIssueObj
                .getCreatedBy().getId());
        final String message = messageSource.getMessage("msg.digisign.success.reissue", new String[] { wfInitiator
                .getEmployee().getName().concat("~").concat(wfInitiator.getDesignation().getName()) }, null);
        model.addAttribute("successMessage", message);
        model.addAttribute("objectType", MarriageCertificateType.REISSUE.toString());
        model.addAttribute("fileStoreId", fileStoreIdArr.length == 1 ? fileStoreIdArr[0] : "");
        LOGGER.debug("..........End of Digital Signature Transition : ReIssue........");
        return "mrdigitalsignature-success";
    }

    /**
     * @description download digitally signed certificate.
     * @param request
     * @param response
     */
    @RequestMapping(value = "/downloadSignedCertificate")
    public void downloadReIssueDigiSignedCertificate(final HttpServletRequest request, final HttpServletResponse response) {
        final String signedFileStoreId = request.getParameter("signedFileStoreId");
        try {
            marriageUtils.downloadSignedCertificate(signedFileStoreId, response);
        } catch (final ApplicationRuntimeException ex) {
            throw new ApplicationRuntimeException("Exception while downloading file : " + ex);
        }
    }

    /**
     * @description To show preview of reissue certificate before digital sign
     * @param id
     * @param session
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/viewCertificate/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> viewReIssueReport(@PathVariable final Long id, final HttpSession session,
            final HttpServletRequest request) throws IOException {
        return marriageUtils.viewReport(id, MarriageCertificateType.REISSUE.toString(), session, request);
    }
}