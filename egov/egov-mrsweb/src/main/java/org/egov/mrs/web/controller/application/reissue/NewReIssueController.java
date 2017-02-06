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

package org.egov.mrs.web.controller.application.reissue;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.utils.StringUtils;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.application.service.MarriageFeeCalculator;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.service.MarriageApplicantService;
import org.egov.mrs.domain.service.MarriageDocumentService;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.mrs.domain.service.ReIssueService;
import org.egov.mrs.masters.entity.MarriageFee;
import org.egov.mrs.masters.service.MarriageRegistrationUnitService;
import org.egov.mrs.web.controller.application.registration.MarriageFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles Marriage Registration ReIssue transaction
 * 
 * @author nayeem
 *
 */
@Controller
@RequestMapping(value = "/reissue")
public class NewReIssueController extends GenericWorkFlowController {

    private static final String APPROVAL_POSITION = "approvalPosition";
    private static final String MESSAGE = "message";

    @Autowired
    private ReIssueService reIssueService;
    @Autowired
    private MarriageRegistrationService marriageRegistrationService;
    @Autowired
    private MarriageApplicantService marriageApplicantService;
    @Autowired
    private MarriageDocumentService marriageDocumentService;
    @Autowired
    protected ResourceBundleMessageSource messageSource;
    @Autowired
    protected AppConfigValueService appConfigValuesService;
    @Autowired
    private MarriageFormValidator marriageFormValidator;
    @Autowired
    private MarriageRegistrationUnitService marriageRegistrationUnitService;
    @Autowired
    private MarriageFeeCalculator marriageFeeCalculator;

    public void prepareNewForm(final Model model, final ReIssue reIssue) {
        model.addAttribute("marriageRegistrationUnit",
                marriageRegistrationUnitService.getActiveRegistrationunit());
        marriageRegistrationService.prepareDocumentsForView(reIssue.getRegistration());
        marriageApplicantService.prepareDocumentsForView(reIssue.getRegistration().getHusband());
        marriageApplicantService.prepareDocumentsForView(reIssue.getRegistration().getWife());
        MarriageFee marriageFee = marriageFeeCalculator.calculateMarriageReissueFee(null, MarriageConstants.REISSUE_FEECRITERIA);
        if (marriageFee != null) {
            reIssue.setFeeCriteria(marriageFee);
            reIssue.setFeePaid(marriageFee.getFees());
        }
        prepareWorkFlowForReIssue(reIssue, model);
        model.addAttribute("reIssue", reIssue);
        model.addAttribute("feepaid", reIssue.getFeePaid().intValue());
        model.addAttribute("documents", marriageDocumentService.getIndividualDocuments());
    }

    @RequestMapping(value = "/create/{registrationId}", method = RequestMethod.GET)
    public String showReIssueForm(@PathVariable final Long registrationId, final Model model) {

        final MarriageRegistration registration = marriageRegistrationService.get(registrationId);
        if (registration == null) {
            model.addAttribute(MESSAGE, "msg.invalid.request");
            return "marriagecommon-error";
        } // CHECK ANY RECORD ALREADY IN ISSUE CERTIFICATE WORKFLOW FOR THE SELECTED REGISTRATION.IF YES, DO NOT ALLOW THEM TO
          // PROCESS THE
          // REQUEST.
        else if (reIssueService.checkAnyWorkFlowInProgressForRegistration(registration)) {
            model.addAttribute(MESSAGE, "msg.workflow.alreadyPresent");
            return "marriagecommon-error";
        }
        ReIssue reIssue = new ReIssue();
        reIssue.setRegistration(registration);
        prepareNewForm(model, reIssue);

        return "reissue-form";
    }

    private void prepareWorkFlowForReIssue(final ReIssue reIssue, final Model model) {
        WorkflowContainer workFlowContainer = new WorkflowContainer();
        workFlowContainer.setAdditionalRule(MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        prepareWorkflow(model, reIssue, workFlowContainer);
        model.addAttribute("additionalRule", MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        model.addAttribute("stateType", reIssue.getClass().getSimpleName());
        model.addAttribute("currentState", "NEW");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createReIssue(@ModelAttribute final WorkflowContainer workflowContainer,
            @ModelAttribute final ReIssue reIssue,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors) {

        marriageFormValidator.validateReIssue(reIssue, errors);
        if (errors.hasErrors()) {
            final MarriageRegistration registration = marriageRegistrationService.get(reIssue.getRegistration().getId());
            reIssue.setRegistration(registration);
            Double fees = reIssue.getFeePaid();
            prepareNewForm(model, reIssue);
            reIssue.setFeePaid(fees);
            return "reissue-form";
        }
        reIssue.setRegistration(marriageRegistrationService.get(reIssue.getRegistration().getId()));
        obtainWorkflowParameters(workflowContainer, request);
        final String appNo = reIssueService.createReIssueApplication(reIssue, workflowContainer);
        String message;
        String approverName = request.getParameter("approverName");
        String nextDesignation = request.getParameter("nextDesignation");
        message = messageSource.getMessage("msg.reissue.forward",
                new String[] { approverName.concat("~").concat(nextDesignation), appNo }, null);
        model.addAttribute(MESSAGE, message);
        model.addAttribute("ackNumber", appNo);
        model.addAttribute("feepaid", reIssue.getFeePaid().doubleValue());
        return "reissue-ack";
    }

    @RequestMapping(value = "/workflow", method = RequestMethod.POST)
    public String handleWorkflowAction(@ModelAttribute ReIssue reIssue,
            @ModelAttribute final WorkflowContainer workflowContainer,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors) throws IOException {
        String message = StringUtils.EMPTY;
        ReIssue reIssueResult = null;
        if (errors.hasErrors())
            return "reissue-view";

        obtainWorkflowParameters(workflowContainer, request);

        if ("Forward".equals(workflowContainer.getWorkFlowAction())) {
            reIssueResult = reIssueService.forwardReIssue(reIssue.getId(), reIssue, workflowContainer);
        } else if ("Cancel ReIssue".equals(workflowContainer.getWorkFlowAction())) {
            reIssueResult = reIssueService.rejectReIssue(reIssue, workflowContainer, request);
            message = messageSource.getMessage("msg.cancelled.reissue", null, null);
        }

        // On Cancel, output rejection certificate
        if (workflowContainer.getWorkFlowAction() != null && !workflowContainer.getWorkFlowAction().isEmpty()
                && workflowContainer.getWorkFlowAction().equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_CANCEL_REISSUE)) {
            List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(
                    MarriageConstants.MODULE_NAME, MarriageConstants.REISSUE_PRINTREJECTIONCERTIFICATE);
            if (appConfigValues != null && !appConfigValues.isEmpty()
                    && "YES".equalsIgnoreCase(appConfigValues.get(0).getValue())) {
                return "redirect:/certificate/reissue?id="
                        + reIssueResult.getId();
            }
        }
        model.addAttribute("ackNumber", reIssueResult.getApplicationNo());
        model.addAttribute(MESSAGE, message);
        return "reissue-ack";
    }

    /**
     * Obtains the workflow paramaters from the HttpServletRequest
     *
     * @param workflowContainer
     * @param request
     */
    private void obtainWorkflowParameters(final WorkflowContainer workflowContainer, final HttpServletRequest request) {
        if (request.getParameter("approvalComent") != null)
            workflowContainer.setApproverComments(request.getParameter("approvalComent"));
        if (request.getParameter("workFlowAction") != null)
            workflowContainer.setWorkFlowAction(request.getParameter("workFlowAction"));
        if (request.getParameter(APPROVAL_POSITION) != null && !request.getParameter(APPROVAL_POSITION).isEmpty())
            workflowContainer.setApproverPositionId(Long.valueOf(request.getParameter(APPROVAL_POSITION)));
    }
}
