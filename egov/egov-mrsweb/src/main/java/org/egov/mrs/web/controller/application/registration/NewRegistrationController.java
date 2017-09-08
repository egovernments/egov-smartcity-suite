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

import static org.egov.mrs.application.MarriageConstants.ANONYMOUS_USER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.application.service.MarriageFeeCalculator;
import org.egov.mrs.application.service.workflow.RegistrationWorkflowService;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.masters.entity.MarriageRegistrationUnit;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Handles the Marriage Registration
 *
 * @author nayeem
 *
 */

@Controller
@RequestMapping(value = { "/registration" })
public class NewRegistrationController extends MarriageRegistrationController {

    private static final String IS_EMPLOYEE = "isEmployee";
    private static final String ACKOWLEDGEMENT = "acknowledgement";
    private static final String MESSAGE = "message";
    private static final String APPROVAL_POSITION = "approvalPosition";
    private static final String MARRIAGE_REGISTRATION = "marriageRegistration";
    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    private MarriageFormValidator marriageFormValidator;
    @Autowired
    private MarriageFeeCalculator marriageFeeCalculator;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private RegistrationWorkflowService registrationWorkFlowService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistration(final Model model, HttpServletRequest request) {
        /*
         * Assignment currentuser; final Integer loggedInUser = ApplicationThreadLocals.getUserId().intValue(); currentuser =
         * assignmentService.getPrimaryAssignmentForUser(loggedInUser.longValue()); if (null == currentuser) {
         * model.addAttribute(MESSAGE, "msg.superuser"); return "marriagecommon-error"; }
         */
        final MarriageRegistration marriageRegistration = new MarriageRegistration();

        User logedinUser = securityUtils.getCurrentUser();
        boolean loggedUserIsMeesevaUser = registrationWorkFlowService.isMeesevaUser(logedinUser);
        String meesevaApplicationNumber = null;
        if (request.getParameter("applicationNo") != null)
            meesevaApplicationNumber = request.getParameter("applicationNo").toString();

        if (loggedUserIsMeesevaUser) {

            if (meesevaApplicationNumber == null) {
                throw new ApplicationRuntimeException("MEESEVA.005");
            } else {
                marriageRegistration.setApplicationNo(meesevaApplicationNumber);
            }

        }
        model.addAttribute("citizenPortalUser", registrationWorkFlowService.isCitizenPortalUser(securityUtils.getCurrentUser()));
        model.addAttribute(IS_EMPLOYEE, !ANONYMOUS_USER.equalsIgnoreCase(logedinUser.getName())
                && registrationWorkFlowService.isEmployee(logedinUser));

        marriageRegistration.setFeePaid(calculateMarriageFee(new Date()));
        model.addAttribute(MARRIAGE_REGISTRATION, marriageRegistration);
        prepareWorkFlowForNewMarriageRegistration(marriageRegistration, model);
        return "registration-form";
    }

    private void prepareWorkFlowForNewMarriageRegistration(final MarriageRegistration registration, final Model model) {
        final WorkflowContainer workFlowContainer = new WorkflowContainer();
        workFlowContainer.setAdditionalRule(MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        prepareWorkflow(model, registration, workFlowContainer);
        model.addAttribute("additionalRule", MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        model.addAttribute("stateType", registration.getClass().getSimpleName());
        model.addAttribute("currentState", "NEW");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(final WorkflowContainer workflowContainer,
            @ModelAttribute(MARRIAGE_REGISTRATION) final MarriageRegistration marriageRegistration,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors, final RedirectAttributes redirectAttributes) {
        User logedinUser = securityUtils.getCurrentUser();
        validateApplicationDate(marriageRegistration, errors, request);
        marriageFormValidator.validate(marriageRegistration, errors, "registration");
        boolean loggedUserIsMeesevaUser = registrationWorkFlowService.isMeesevaUser(logedinUser);
        boolean citizenPortalUser = registrationWorkFlowService.isCitizenPortalUser(securityUtils.getCurrentUser());
        final Boolean isEmployee = !ANONYMOUS_USER.equalsIgnoreCase(logedinUser.getName())
                && registrationWorkFlowService.isEmployee(logedinUser);
        boolean isAssignmentPresent = registrationWorkFlowService.validateAssignmentForCscUser(marriageRegistration, null,
                isEmployee);
        if (!isAssignmentPresent) {
            return buildFormOnValidation(marriageRegistration, isEmployee, model);
        }
        if (errors.hasErrors()) {
            return buildFormOnValidation(marriageRegistration, isEmployee, model);
        }
        String message;
        String approverName = null;
        String nextDesignation = null;
        if (!isEmployee || citizenPortalUser) {
            final Assignment assignment = registrationWorkFlowService.getMappedAssignmentForCscOperator(marriageRegistration,
                    null);
            if (assignment != null) {
                workflowContainer.setApproverPositionId(assignment.getPosition().getId());
                approverName = assignment.getEmployee().getName();
                nextDesignation = assignment.getDesignation().getName();
            }
        } else {
            approverName = request.getParameter("approverName");
            nextDesignation = request.getParameter("nextDesignation");
            obtainWorkflowParameters(workflowContainer, request);
        }

        /*
         * final Integer loggedInUser = ApplicationThreadLocals.getUserId().intValue(); currentuser =
         * assignmentService.getPrimaryAssignmentForUser(loggedInUser.longValue()); if (null == currentuser) {
         * model.addAttribute(MESSAGE, "msg.superuser"); return "marriagecommon-error"; }
         */
        final String appNo;
        if (loggedUserIsMeesevaUser) {
            marriageRegistration.setSource(MarriageConstants.SOURCE_MEESEVA);
            appNo = marriageRegistrationService
                    .createMeesevaRegistration(marriageRegistration, workflowContainer, loggedUserIsMeesevaUser,
                            citizenPortalUser)
                    .getApplicationNo();
        } else {
            appNo = marriageRegistrationService
                    .createRegistration(marriageRegistration, workflowContainer, loggedUserIsMeesevaUser, citizenPortalUser)
                    .getApplicationNo();
        }
        message = messageSource.getMessage("msg.success.forward",
                new String[] { approverName.concat("~").concat(nextDesignation), appNo }, null);
        model.addAttribute(MESSAGE, message);
        model.addAttribute("applnNo", appNo);
        model.addAttribute(IS_EMPLOYEE, isEmployee);
        if (!isEmployee && !loggedUserIsMeesevaUser) {
            redirectAttributes.addFlashAttribute(MESSAGE, message);
            return "redirect:/registration/new-mrgregistration-ackowledgement/" + appNo;
        } else if (loggedUserIsMeesevaUser) {
            return "redirect:/registration/generate-meesevareceipt?transactionServiceNumber="
                    + marriageRegistration.getApplicationNo();
        } else
            return "registration-ack";
    }

    private String buildFormOnValidation(final MarriageRegistration marriageRegistration,
            final Boolean isEmployee, final Model model) {
        model.addAttribute(IS_EMPLOYEE, isEmployee);
        model.addAttribute("message", messageSource.getMessage("notexists.position",
                new String[] {}, null));
        model.addAttribute(MARRIAGE_REGISTRATION, marriageRegistration);
        registrationWorkFlowService.validateAssignmentForCscUser(marriageRegistration, null, isEmployee);
        prepareWorkFlowForNewMarriageRegistration(marriageRegistration, model);
        return "registration-form";
    }

    @RequestMapping(value = "/generate-meesevareceipt", method = RequestMethod.GET)
    public RedirectView generateMeesevaReceipt(final HttpServletRequest request, final Model model) {
        final String keyNameArray = request.getParameter("transactionServiceNumber");

        final RedirectView redirect = new RedirectView(MarriageConstants.MEESEVA_REDIRECT_URL + keyNameArray, false);
        final FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
        if (outputFlashMap != null)
            outputFlashMap.put("url", request.getRequestURL());
        return redirect;
    }

    @RequestMapping(value = "/workflow", method = RequestMethod.POST)
    public String handleWorkflowAction(@RequestParam final Long id,
            @ModelAttribute final MarriageRegistration marriageRegistration,
            @ModelAttribute final WorkflowContainer workflowContainer,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors) throws IOException {

        if (errors.hasErrors())
            return "registration-view";

        obtainWorkflowParameters(workflowContainer, request);
        MarriageRegistration result = null;

        switch (workflowContainer.getWorkFlowAction()) {
        case "Forward":
            result = marriageRegistrationService.forwardRegistration(marriageRegistration, workflowContainer);
            break;
        case "Approve":
            result = marriageRegistrationService.approveRegistration(marriageRegistration, workflowContainer, request);
            break;
        case "Reject":
            result = marriageRegistrationService.rejectRegistration(marriageRegistration, workflowContainer);
            break;
        case "Cancel Registration":
            result = marriageRegistrationService.rejectRegistration(marriageRegistration, workflowContainer);
            break;
        }

        model.addAttribute(MARRIAGE_REGISTRATION, result);
        return "registration-ack";
    }

    /**
     * Obtains the workflow paramaters from the HttpServletRequest
     *
     * @param workflowContainer
     * @param request
     */
    private void obtainWorkflowParameters(
            final WorkflowContainer workflowContainer, final HttpServletRequest request) {
        if (request.getParameter("approvalComent") != null)
            workflowContainer.setApproverComments(request.getParameter("approvalComent"));
        if (request.getParameter("workFlowAction") != null)
            workflowContainer.setWorkFlowAction(request.getParameter("workFlowAction"));
        if (request.getParameter(APPROVAL_POSITION) != null && !request.getParameter(APPROVAL_POSITION).isEmpty())
            workflowContainer.setApproverPositionId(Long.valueOf(request.getParameter(APPROVAL_POSITION)));
    }

    /**
     *
     * @param feeId
     * @return
     */
    @RequestMapping(value = "/getmrregistrationunitzone", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Boundary getregistrationunitzone(@RequestParam final Long registrationUnitId) {
        final MarriageRegistrationUnit marriageRegistrationUnit = marriageRegistrationUnitService.findById(registrationUnitId);
        if (marriageRegistrationUnit != null)
            return marriageRegistrationUnit.getZone();
        return null;
    }

    @RequestMapping(value = "/calculatemarriagefee", method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public Double calculateMarriageFee(@RequestParam final Date dateOfMarriage) {
        return marriageFeeCalculator.calculateMarriageRegistrationFee(null, dateOfMarriage);
    }

    @RequestMapping(value = "/new-mrgregistration-ackowledgement/{applnNo}", method = GET)
    public String showAcknowledgemnt(@PathVariable final String applnNo, final Model model) {
        User logedinUser = securityUtils.getCurrentUser();
        model.addAttribute("applicationNo", applnNo);
        model.addAttribute("applnType", "NEW");
        model.addAttribute("isOnlineApplication", ANONYMOUS_USER.equalsIgnoreCase(logedinUser.getName()));
        return ACKOWLEDGEMENT;
    }

    @RequestMapping(value = "/printmarriageregistrationack", method = GET)
    @ResponseBody
    public ResponseEntity<byte[]> printAck(@RequestParam("applnNo") final String applnNo, final Model model,
            final HttpServletRequest request) {
        byte[] reportOutput;
        final String cityMunicipalityName = (String) request.getSession()
                .getAttribute("citymunicipalityname");
        final String cityName = (String) request.getSession().getAttribute("cityname");
        final MarriageRegistration marriageRegistration = marriageRegistrationService.findByApplicationNo(applnNo);

        if (marriageRegistration != null) {
            reportOutput = marriageRegistrationService
                    .getReportParamsForAcknowdgementForMrgReg(marriageRegistration, cityMunicipalityName, cityName)
                    .getReportOutputData();
            if (reportOutput != null) {
                final HttpHeaders headers = new HttpHeaders();

                headers.setContentType(MediaType.parseMediaType(MarriageConstants.APPLICATION_PDF));
                headers.add("content-disposition", "inline;filename=new-marriage-registration-ack.pdf");
                return new ResponseEntity<>(reportOutput, headers, HttpStatus.CREATED);
            }
        }

        return null;

    }

}