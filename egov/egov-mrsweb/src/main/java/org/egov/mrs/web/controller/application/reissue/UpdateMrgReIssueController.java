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

import javax.servlet.http.HttpServletRequest;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.utils.StringUtils;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.service.MarriageApplicantService;
import org.egov.mrs.domain.service.MarriageDocumentService;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.mrs.domain.service.ReIssueService;
import org.egov.mrs.masters.service.MarriageRegistrationUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller to correct the registration data
 *
 * @author nayeem
 *
 */

@Controller
@RequestMapping(value = "/reissue")
public class UpdateMrgReIssueController extends GenericWorkFlowController {

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

    public void prepareNewForm(final Model model) {
        model.addAttribute("marriageRegistrationUnit",
                marriageRegistrationUnitService.getActiveRegistrationunit());
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
        workFlowContainer.setAdditionalRule(MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        prepareWorkflow(model, reIssue, workFlowContainer);
        model.addAttribute("additionalRule", MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        model.addAttribute("stateType", reIssue.getClass().getSimpleName());
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateReIssue(@RequestParam final Long id, @ModelAttribute final ReIssue reIssue,
            @ModelAttribute final WorkflowContainer workflowContainer,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors) throws IOException {

        String workFlowAction = StringUtils.EMPTY;
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");

        if (errors.hasErrors())
            return "reissue-view";

        String message = StringUtils.EMPTY;
        String approverName;
        String nextDesignation;

        if (workFlowAction != null && !workFlowAction.isEmpty()) {
            workflowContainer.setWorkFlowAction(workFlowAction);
            final Assignment wfInitiator = assignmentService.getPrimaryAssignmentForUser(reIssue.getCreatedBy().getId());
            approverName = wfInitiator.getEmployee().getName();
            nextDesignation = wfInitiator.getDesignation().getName();
            workflowContainer.setApproverComments(request.getParameter("approvalComent"));
            if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_REJECT)) {

                reIssueService.rejectReIssue(reIssue, workflowContainer, request);
                message = messageSource.getMessage("msg.rejected.reissue", new String[] { reIssue.getApplicationNo(),
                        approverName.concat("~").concat(nextDesignation) }, null);
            } else if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_CANCEL)) {
                reIssueService.rejectReIssue(reIssue, workflowContainer, request);
                message = messageSource.getMessage("msg.cancelled.reissue", new String[] { reIssue.getApplicationNo(), null },
                        null);
            } else if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_APPROVE)) {
                reIssueService.approveReIssue(reIssue, workflowContainer);
                message = messageSource.getMessage("msg.approved.reissue", new String[] { reIssue.getApplicationNo(),
                        approverName.concat("~").concat(nextDesignation) }, null);
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
}