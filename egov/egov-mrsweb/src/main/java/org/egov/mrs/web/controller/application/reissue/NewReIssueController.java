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

import static org.egov.mrs.application.MarriageConstants.ANONYMOUS_USER;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.eis.entity.Assignment;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.application.service.MarriageFeeCalculator;
import org.egov.mrs.application.service.workflow.RegistrationWorkflowService;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles Marriage Registration ReIssue transaction
 *
 * @author nayeem
 *
 */
@Controller
@RequestMapping(value = { "/reissue" })
public class NewReIssueController extends GenericWorkFlowController {

    private static final String IS_EMPLOYEE = "isEmployee";
    private static final String APPROVAL_POSITION = "approvalPosition";
    private static final String MESSAGE = "message";
    private static final String ACKOWLEDGEMENT = "acknowledgement";

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

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private RegistrationWorkflowService registrationWorkFlowService;

    public void prepareNewForm(final Model model, final ReIssue reIssue) {
        model.addAttribute("marriageRegistrationUnit",
                marriageRegistrationUnitService.getActiveRegistrationunit());
        marriageRegistrationService.prepareDocumentsForView(reIssue.getRegistration());
        marriageApplicantService.prepareDocumentsForView(reIssue.getRegistration().getHusband());
        marriageApplicantService.prepareDocumentsForView(reIssue.getRegistration().getWife());
        final MarriageFee marriageFee = marriageFeeCalculator.calculateMarriageReissueFee(null,
                MarriageConstants.REISSUE_FEECRITERIA);
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
        final User logedinUser = securityUtils.getCurrentUser();
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
        model.addAttribute(IS_EMPLOYEE,
                !ANONYMOUS_USER.equalsIgnoreCase(logedinUser.getName()) && registrationWorkFlowService.isEmployee(logedinUser));
        model.addAttribute("citizenPortalUser", registrationWorkFlowService.isCitizenPortalUser(securityUtils.getCurrentUser()));
        final ReIssue reIssue = new ReIssue();
        reIssue.setRegistration(registration);
        prepareNewForm(model, reIssue);

        return "reissue-form";
    }

    private void prepareWorkFlowForReIssue(final ReIssue reIssue, final Model model) {
        final WorkflowContainer workFlowContainer = new WorkflowContainer();
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
            final BindingResult errors,
            final RedirectAttributes redirectAttributes) {
        final User logedinUser = securityUtils.getCurrentUser();
        boolean citizenPortalUser = registrationWorkFlowService.isCitizenPortalUser(securityUtils.getCurrentUser());
        final Boolean isEmployee = !ANONYMOUS_USER.equalsIgnoreCase(logedinUser.getName())
                && registrationWorkFlowService.isEmployee(logedinUser);
        marriageFormValidator.validateReIssue(reIssue, errors);
        boolean isAssignmentPresent = registrationWorkFlowService.validateAssignmentForCscUser(null, reIssue, isEmployee);
        if (!isAssignmentPresent) {
            model.addAttribute("message", messageSource.getMessage("notexists.position",
                    new String[] {}, null));

            return buildFormOnValidation(reIssue, isEmployee, model);
        }
        if (errors.hasErrors()) {
            return buildFormOnValidation(reIssue, isEmployee, model);
        }
        String approverName = null;
        String nextDesignation = null;
        String message = null;
        reIssue.setRegistration(marriageRegistrationService.get(reIssue.getRegistration().getId()));
        obtainWorkflowParameters(workflowContainer, request);

        if (!isEmployee ||citizenPortalUser ) {
            final Assignment assignment = registrationWorkFlowService.getMappedAssignmentForCscOperator(null, reIssue);
            if (assignment != null) {
                workflowContainer.setApproverPositionId(assignment.getPosition().getId());
                approverName = assignment.getEmployee().getName();
                nextDesignation = assignment.getDesignation().getName();
            }
        } else {
            approverName = request.getParameter("approverName");
            nextDesignation = request.getParameter("nextDesignation");
        }
        final String appNo = reIssueService.createReIssueApplication(reIssue, workflowContainer);

        if (approverName != null)
            message = messageSource.getMessage("msg.reissue.forward",
                    new String[] { approverName.concat("~").concat(nextDesignation), appNo }, null);
        model.addAttribute(MESSAGE, message);

        model.addAttribute("ackNumber", appNo);
        model.addAttribute("feepaid", reIssue.getFeePaid().doubleValue());
        if (!isEmployee) {
            redirectAttributes.addFlashAttribute(MESSAGE, message);
            return "redirect:/reissue/reissue-certificate-ackowledgement/" + appNo;

        } else
            return "reissue-ack";
    }

    private String buildFormOnValidation(final ReIssue reIssue, final Boolean isEmployee, final Model model) {
        final MarriageRegistration registration = marriageRegistrationService.get(reIssue.getRegistration().getId());
        reIssue.setRegistration(registration);
        prepareNewForm(model, reIssue);
        model.addAttribute(IS_EMPLOYEE, isEmployee);
        final Double fees = reIssue.getFeePaid();
        reIssue.setFeePaid(fees);
        return "reissue-form";
    }

    @RequestMapping(value = "/workflow", method = RequestMethod.POST)
    public String handleWorkflowAction(@ModelAttribute final ReIssue reIssue,
            @ModelAttribute final WorkflowContainer workflowContainer,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors) throws IOException {
        String message = org.apache.commons.lang.StringUtils.EMPTY;
        ReIssue reIssueResult = null;
        String approverName;
        String nextDesignation;
        if (errors.hasErrors())
            return "reissue-view";

        obtainWorkflowParameters(workflowContainer, request);

        if ("Forward".equals(workflowContainer.getWorkFlowAction())) {
            reIssueResult = reIssueService.forwardReIssue(reIssue.getId(), reIssue, workflowContainer);
            approverName = request.getParameter("approverName");
            nextDesignation = request.getParameter("nextDesignation");
            message = messageSource.getMessage("msg.reissue.forward",
                    new String[] { approverName.concat("~").concat(nextDesignation), reIssueResult.getApplicationNo() }, null);
        } else if ("Cancel ReIssue".equals(workflowContainer.getWorkFlowAction())) {
            reIssueResult = reIssueService.rejectReIssue(reIssue, workflowContainer, request);
            message = messageSource.getMessage("msg.cancelled.reissue", null, null);
        }

        // On Cancel, output rejection certificate
        if (workflowContainer.getWorkFlowAction() != null && !workflowContainer.getWorkFlowAction().isEmpty()
                && workflowContainer.getWorkFlowAction().equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_CANCEL_REISSUE)) {
            final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(
                    MarriageConstants.MODULE_NAME, MarriageConstants.REISSUE_PRINTREJECTIONCERTIFICATE);
            if (reIssueResult != null && appConfigValues != null && !appConfigValues.isEmpty()
                    && "YES".equalsIgnoreCase(appConfigValues.get(0).getValue()))
                return "redirect:/certificate/reissue?id="
                        + reIssueResult.getId();
        }
        if (reIssueResult != null)
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

    @RequestMapping(value = "/reissue-certificate-ackowledgement/{applnNo}", method = RequestMethod.GET)
    public String showAcknowledgemnt(@PathVariable final String applnNo, final Model model) {
        final User logedinUser = securityUtils.getCurrentUser();
        model.addAttribute("applicationNo", applnNo);
        model.addAttribute("applnType", "REISSUE");
        model.addAttribute("isOnlineApplication", ANONYMOUS_USER.equalsIgnoreCase(logedinUser.getName()));
        return ACKOWLEDGEMENT;
    }

    @RequestMapping(value = "/printreissuecertificateack", method = GET)
    @ResponseBody
    public ResponseEntity<byte[]> printAck(@RequestParam("applnNo") final String applnNo, final Model model,
            final HttpServletRequest request) {
        byte[] reportOutput;
        final String cityMunicipalityName = (String) request.getSession()
                .getAttribute("citymunicipalityname");
        final String cityName = (String) request.getSession().getAttribute("cityname");
        final ReIssue reIssue = reIssueService.findByApplicationNo(applnNo);

        if (reIssue != null) {
            reportOutput = marriageRegistrationService
                    .getReportParamsForAcknowdgementForMrgReissue(reIssue, cityMunicipalityName, cityName)
                    .getReportOutputData();
            if (reportOutput != null) {
                final HttpHeaders headers = new HttpHeaders();

                headers.setContentType(MediaType.parseMediaType(MarriageConstants.APPLICATION_PDF));
                headers.add("content-disposition", "inline;filename=marriage-duplicate-certificate-ack.pdf");
                return new ResponseEntity<>(reportOutput, headers, HttpStatus.CREATED);
            }
        }

        return null;

    }
}
