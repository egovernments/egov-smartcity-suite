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

package org.egov.mrs.web.controller.application.reissue;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.mrs.application.MarriageConstants.ADDITIONAL_RULE_REGISTRATION;
import static org.egov.mrs.application.MarriageConstants.APPLICATION_NUMBER;
import static org.egov.mrs.application.MarriageConstants.APPROVAL_COMMENT;
import static org.egov.mrs.application.MarriageConstants.APPROVED;
import static org.egov.mrs.application.MarriageConstants.CREATED;
import static org.egov.mrs.application.MarriageConstants.FILE_STORE_ID_APPLICATION_NUMBER;
import static org.egov.mrs.application.MarriageConstants.JUNIOR_SENIOR_ASSISTANCE_APPROVAL_PENDING;
import static org.egov.mrs.application.MarriageConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.mrs.application.MarriageConstants.WFLOW_ACTION_STEP_CANCEL_REISSUE;
import static org.egov.mrs.application.MarriageConstants.WFLOW_ACTION_STEP_DIGISIGN;
import static org.egov.mrs.application.MarriageConstants.WFLOW_ACTION_STEP_PRINTCERTIFICATE;
import static org.egov.mrs.application.MarriageConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.mrs.application.MarriageConstants.WFLOW_PENDINGACTION_APPROVAL_APPROVEPENDING;
import static org.egov.mrs.application.MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN;
import static org.egov.mrs.application.MarriageConstants.WFLOW_PENDINGACTION_APPRVLPENDING_PRINTCERT;
import static org.egov.mrs.application.MarriageConstants.WFLOW_PENDINGACTION_DIGISIGNPENDING;
import static org.egov.mrs.application.MarriageConstants.WFLOW_PENDINGACTION_PRINTCERTIFICATE;
import static org.egov.mrs.application.MarriageConstants.WFLOW_PENDINGACTION_REV_CLERK_APPRVLPENDING;
import static org.egov.mrs.application.MarriageConstants.WFSTATE_APPROVER_REJECTED;
import static org.egov.mrs.application.MarriageConstants.WFSTATE_CMOH_APPROVED;
import static org.egov.mrs.application.MarriageConstants.WFSTATE_MHO_APPROVED;
import static org.egov.mrs.application.MarriageConstants.WFSTATE_REV_CLRK_APPROVED;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.enums.EmployeeStatus;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
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
 * Controller to correct the reIssue data
 *
 * @author nayeem
 *
 */

@Controller
@RequestMapping(value = "/reissue")
public class UpdateMrgReIssueController extends GenericWorkFlowController {

    private static final String WORK_FLOW_ACTION = "workFlowAction";
    private static final String REISSUE_VIEW = "reissue-view";
    private static final String PENDING_ACTIONS = "pendingActions";
    private static final String IS_EMPLOYEE = "isEmployee";
    private static final String APPROVAL_POSITION = "approvalPosition";

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

    public void prepareNewForm(final ReIssue reIssue, final Model model) {
        model.addAttribute("marriageRegistrationUnit",
                marriageRegistrationUnitService.getActiveRegistrationunit());
        model.addAttribute(IS_EMPLOYEE, registrationWorkflowService.isEmployee(securityUtils.getCurrentUser()));
        model.addAttribute("documents", marriageDocumentService.getIndividualDocuments());

        marriageRegistrationService.prepareDocumentsForView(reIssue.getRegistration());
        marriageApplicantService.prepareDocumentsForView(reIssue.getRegistration().getHusband());
        marriageApplicantService.prepareDocumentsForView(reIssue.getRegistration().getWife());
        marriageApplicantService.prepareDocumentsForView(reIssue.getApplicant());
        model.addAttribute("applicationHistory",
                reIssueService.getHistory(reIssue));
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String showReIssueForm(@PathVariable final Long id, final Model model) {
        final ReIssue reIssue = reIssueService.get(id);
        
        prepareNewForm(reIssue, model);
        prepareWorkFlowForReIssue(reIssue, model);
        model.addAttribute("reIssue", reIssue);
        if (reIssue.getStatus().getCode().equalsIgnoreCase(ReIssue.ReIssueStatus.REJECTED.toString()))
            return "reissue-form";
        return REISSUE_VIEW;
    }

    private void prepareWorkFlowForReIssue(final ReIssue reIssue, final Model model) {
        final WorkflowContainer workFlowContainer = new WorkflowContainer();
        workFlowContainer.setPendingActions(reIssue.getState().getNextAction());
        model.addAttribute(PENDING_ACTIONS, reIssue.getState().getNextAction());

        // Set pending actions based on digitalsignature configuration value
        if (reIssue.getStatus().getCode().equalsIgnoreCase(APPROVED))
            if (marriageUtils.isDigitalSignEnabled()) {
                model.addAttribute(PENDING_ACTIONS, WFLOW_PENDINGACTION_DIGISIGNPENDING);
                workFlowContainer.setPendingActions(WFLOW_PENDINGACTION_DIGISIGNPENDING);
            } else {
                model.addAttribute(PENDING_ACTIONS, WFLOW_PENDINGACTION_PRINTCERTIFICATE);
                workFlowContainer.setPendingActions(WFLOW_PENDINGACTION_PRINTCERTIFICATE);
            }
        workFlowContainer.setAdditionalRule(ADDITIONAL_RULE_REGISTRATION);
        if ((WFSTATE_REV_CLRK_APPROVED.equals(reIssue.getState().getValue())
                || WFSTATE_MHO_APPROVED.equals(reIssue.getState().getValue())
                || WFSTATE_CMOH_APPROVED.equals(reIssue.getState().getValue()))
                && WFLOW_PENDINGACTION_APPROVAL_APPROVEPENDING.equals(reIssue.getState().getNextAction())) {
            workFlowContainer.setPendingActions(WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN);
            model.addAttribute(PENDING_ACTIONS, WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN);
        } else if (WFSTATE_APPROVER_REJECTED.equals(reIssue.getCurrentState().getValue())) {
            workFlowContainer.setPendingActions(null);
            model.addAttribute(PENDING_ACTIONS, null);
        }

        prepareWorkflow(model, reIssue, workFlowContainer);
        model.addAttribute("additionalRule", ADDITIONAL_RULE_REGISTRATION);
        model.addAttribute("stateType", reIssue.getClass().getSimpleName());
        if (reIssue.getCurrentState() != null)
            model.addAttribute("currentState", reIssue.getCurrentState().getValue());
        if (reIssue.getStatus().getCode().equalsIgnoreCase(CREATED)
                && JUNIOR_SENIOR_ASSISTANCE_APPROVAL_PENDING
                        .equalsIgnoreCase(reIssue.getState().getNextAction())
                || WFLOW_PENDINGACTION_REV_CLERK_APPRVLPENDING.equalsIgnoreCase(reIssue.getState().getNextAction())) {
            model.addAttribute("nextActn", reIssue.getState().getNextAction());
            model.addAttribute("isReassignEnabled", marriageUtils.isReassignEnabled());
        } else {
            model.addAttribute("nextActn", reIssue.getState().getNextAction());
        }

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateReIssue(@RequestParam final Long id, @ModelAttribute final ReIssue reIssue,
            @ModelAttribute final WorkflowContainer workflowContainer,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors) throws IOException {

        String workFlowAction = EMPTY;
        if (request.getParameter(WORK_FLOW_ACTION) != null)
            workFlowAction = request.getParameter(WORK_FLOW_ACTION);
        Assignment approverAssign = null;
        if (isNotBlank(request.getParameter(APPROVAL_POSITION)))
            approverAssign = assignmentService
                    .getPrimaryAssignmentForPositon(Long.valueOf(request.getParameter(APPROVAL_POSITION)));
        if (isNotBlank(request.getParameter(APPROVAL_POSITION)) && approverAssign == null)
            approverAssign = assignmentService.getAssignmentsForPosition(Long.valueOf(request.getParameter(APPROVAL_POSITION)))
                    .get(0);
        if (errors.hasErrors() || (approverAssign != null
                && !EmployeeStatus.EMPLOYED.equals(approverAssign.getEmployee().getEmployeeStatus()))) {

            if (approverAssign != null && !EmployeeStatus.EMPLOYED.equals(approverAssign.getEmployee().getEmployeeStatus()))
                model.addAttribute("employeeAssgnNotValid", messageSource.getMessage("msg.emp.not.valid", new String[] {
                        approverAssign.getEmployee().getName().concat("~").concat(approverAssign.getDesignation().getName()),
                        approverAssign.getEmployee().getEmployeeStatus().name() },
                        null));
            
            prepareNewForm(reIssue, model);
            prepareWorkFlowForReIssue(reIssue, model);
            model.addAttribute("reIssue", reIssue);
            return REISSUE_VIEW;
        }

        String message = EMPTY;
        String approverName;
        String nextDesignation;

        if (isNotBlank(workFlowAction)) {
            workflowContainer.setWorkFlowAction(workFlowAction);
            final Assignment wfInitiator = registrationWorkflowService.getWorkFlowInitiatorForReissue(reIssue);
            approverName = wfInitiator.getEmployee().getName();
            nextDesignation = wfInitiator.getDesignation().getName();
            workflowContainer.setApproverComments(request.getParameter(APPROVAL_COMMENT));
            if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {
                reIssueService.rejectReIssue(reIssue, workflowContainer);
                message = messageSource.getMessage("msg.rejected.reissue", new String[] { reIssue.getApplicationNo(),
                        approverName.concat("~").concat(nextDesignation) }, null);
            } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_CANCEL_REISSUE)) {
                reIssueService.rejectReIssue(reIssue, workflowContainer);
                message = messageSource.getMessage("msg.cancelled.reissue", new String[] { reIssue.getApplicationNo() },
                        null);
            } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE)) {
                // If digital signature is configured, after approve appl shld remain in commissioner inbox for digital signature
                // otherwise gets fwded to creator for print certificate.
                if (marriageUtils.isDigitalSignEnabled()) {
                    model.addAttribute(PENDING_ACTIONS, WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN);
                    workflowContainer.setPendingActions(WFLOW_PENDINGACTION_APPRVLPENDING_DIGISIGN);
                    reIssueService.approveReIssue(reIssue, workflowContainer);
                    message = messageSource.getMessage("msg.approved.reissue",
                            new String[] { reIssue.getApplicationNo() }, null);
                } else {
                    model.addAttribute(PENDING_ACTIONS, WFLOW_PENDINGACTION_APPRVLPENDING_PRINTCERT);
                    workflowContainer.setPendingActions(WFLOW_PENDINGACTION_APPRVLPENDING_PRINTCERT);
                    reIssueService.approveReIssue(reIssue, workflowContainer);
                    message = messageSource.getMessage(
                            "msg.approved.reissue",
                            new String[] { reIssue.getApplicationNo()
                            },
                            null);
                }
            } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_DIGISIGN)) {
                // Generates certificate, sends for digital sign and calls callback url for workflow transition.
                MarriageCertificate marriageCertificate = null;
                final List<MarriageCertificate> certificateReIssued = marriageCertificateService
                        .getGeneratedReIssueCertificate(reIssue);
                if (certificateReIssued != null && !certificateReIssued.isEmpty())
                    for (final MarriageCertificate certificateobj : certificateReIssued)
                        marriageCertificate = certificateobj;
                else
                    marriageCertificate = reIssueService
                            .generateReIssueCertificate(reIssue);

                model.addAttribute("fileStoreIds", marriageCertificate.getFileStore().getFileStoreId());
                model.addAttribute("ulbCode", ApplicationThreadLocals.getCityCode());
                // Adding applicationNo and its filestoreid to be digitally signed to session
                final HttpSession session = request.getSession();
                session.setAttribute(APPROVAL_COMMENT, request.getParameter(APPROVAL_COMMENT));
                session.setAttribute(APPLICATION_NUMBER, marriageCertificate.getReIssue()
                        .getApplicationNo());
                final Map<String, String> fileStoreIdsApplicationNoMap = new HashMap<>();
                fileStoreIdsApplicationNoMap.put(marriageCertificate.getFileStore().getFileStoreId(),
                        marriageCertificate.getReIssue().getApplicationNo());
                session.setAttribute(FILE_STORE_ID_APPLICATION_NUMBER, fileStoreIdsApplicationNoMap);
                model.addAttribute("isDigitalSignatureEnabled", marriageUtils.isDigitalSignEnabled());
                return "reissue-digitalsignature";
            } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_PRINTCERTIFICATE)) {
                reIssueService.printCertificate(reIssue, workflowContainer);
                message = messageSource.getMessage("msg.printcerificate.reissue", null, null);
            } else {
                approverName = request.getParameter("approverName");
                nextDesignation = request.getParameter("nextDesignation");
                workflowContainer.setApproverPositionId(Long.valueOf(request.getParameter(APPROVAL_POSITION)));
                reIssueService.forwardReIssue(id, reIssue, workflowContainer);
                message = messageSource.getMessage("msg.forward.reissue",
                        new String[] { approverName.concat("~").concat(nextDesignation), reIssue.getApplicationNo() }, null);
            }
        }
        // On print certificate, output reIssue certificate
        if (workFlowAction != null && !workFlowAction.isEmpty()
                && workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_PRINTCERTIFICATE))
            return "redirect:/certificate/reissue?id="
                    + reIssue.getId();

        model.addAttribute("message", message);
        return "reissue-ack";
    }

    @RequestMapping(value = "/digiSignWorkflow")
    public String digiSignTransitionWorkflow(final HttpServletRequest request, final Model model) {
        LOGGER.debug("..........Inside Digital Signature Transition : ReIssue........");
        final String fileStoreIds = request.getParameter("fileStoreId");
        LOGGER.debug("........fileStoreIds.........." + fileStoreIds);
        final String[] fileStoreIdArr = fileStoreIds.split(",");
        LOGGER.debug("........fileStoreIdArr.........." + fileStoreIdArr.length);
        final HttpSession session = request.getSession();
        final String approvalComent = (String) session.getAttribute(APPROVAL_COMMENT);
        // Gets the digitally signed applicationNo and its filestoreid from session
        final Map<String, String> appNoFileStoreIdsMap = (Map<String, String>) session
                .getAttribute(FILE_STORE_ID_APPLICATION_NUMBER);
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
                workflowContainer.setWorkFlowAction(WFLOW_ACTION_STEP_DIGISIGN);
                workflowContainer.setPendingActions(WFLOW_PENDINGACTION_DIGISIGNPENDING);
                reIssueObj = reIssueService.findByApplicationNo(applicationNumber);
                reIssueService.digiSignCertificate(reIssueObj, workflowContainer);
            }
        }
        LOGGER.debug("........outside for loop......");
        final String message = messageSource.getMessage("msg.digisign.success.reissue", null, null);             
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
        return marriageUtils.viewReport(id, MarriageCertificateType.REISSUE.name(), session, request);
    }
}