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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.masters.entity.MarriageFee;
import org.egov.mrs.masters.entity.MarriageRegistrationUnit;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Handles the Marriage Registration
 *
 * @author nayeem
 *
 */

@Controller
@RequestMapping(value = "/registration")
public class NewRegistrationController extends MarriageRegistrationController {

    @Autowired
    protected AssignmentService assignmentService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistration(final Model model) {
        Assignment currentuser = null;
        final Integer loggedInUser = ApplicationThreadLocals.getUserId().intValue();
        currentuser = assignmentService.getPrimaryAssignmentForUser(loggedInUser.longValue());
        if (null == currentuser) {
            model.addAttribute("message", "msg.superuser");
            return "marriagecommon-error";
        }
        final MarriageRegistration registration = new MarriageRegistration();
        model.addAttribute("registration", registration);
        prepareWorkFlowForNewMarriageRegistration(registration, model);
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
            @ModelAttribute("registration") final MarriageRegistration registration,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors, final RedirectAttributes redirectAttributes) {

        validateApplicationDate(registration, errors, request);

        if (errors.hasErrors()) {
            model.addAttribute("registration", registration);
            prepareWorkFlowForNewMarriageRegistration(registration, model);
            return "registration-form";

        }
        Assignment currentuser = null;
        final Integer loggedInUser = ApplicationThreadLocals.getUserId().intValue();
        currentuser = assignmentService.getPrimaryAssignmentForUser(loggedInUser.longValue());
        if (null == currentuser) {
            model.addAttribute("message", "msg.superuser");
            return "marriagecommon-error";

        }

        obtainWorkflowParameters(workflowContainer, request);
        final String appNo = marriageRegistrationService.createRegistration(registration, workflowContainer);
        model.addAttribute("ackNumber", appNo);

        return "registration-ack";
    }

    @RequestMapping(value = "/workflow", method = RequestMethod.POST)
    public String handleWorkflowAction(@RequestParam final Long id,
            @ModelAttribute final MarriageRegistration registration,
            @ModelAttribute final WorkflowContainer workflowContainer,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors) {

        if (errors.hasErrors())
            return "registration-view";

        obtainWorkflowParameters(workflowContainer, request);
        MarriageRegistration result = null;

        switch (workflowContainer.getWorkFlowAction()) {
        case "Forward":
            result = marriageRegistrationService.forwardRegistration(registration, workflowContainer);
            break;
        case "Approve":
            result = marriageRegistrationService.approveRegistration(registration, workflowContainer);
            break;
        case "Reject":
            result = marriageRegistrationService.rejectRegistration(registration, workflowContainer);
            break;
        case "Cancel Registration":
            result = marriageRegistrationService.rejectRegistration(registration, workflowContainer);
            break;
        }

        model.addAttribute("registration", result);
        return "registration-ack";
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
        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            workflowContainer.setApproverPositionId(Long.valueOf(request.getParameter("approvalPosition")));
    }

    /**
     *
     * @param feeId
     * @return
     */
    /*
     * @RequestMapping(value = "/calculatemarriagefee", method = GET, produces = APPLICATION_JSON_VALUE)
     * @ResponseBody public Double calculateMarriageFee(@RequestParam final Long feeId) { MarriageFee marriageFee =
     * marriageFeeService.getFee(feeId); if (marriageFee != null) return marriageFee.getFees(); return null; }
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
        final AppConfigValues allowValidation = getDaysValidationAppConfValue(
                MarriageConstants.MODULE_NAME, MarriageConstants.MARRIAGEREGISTRATION_DAYS_VALIDATION);
        final int days = Days.daysBetween(new DateTime(dateOfMarriage), new DateTime(new Date())).getDays();
        if (allowValidation != null && !allowValidation.getValue().isEmpty())
            if (allowValidation.getValue().equalsIgnoreCase("NO")) {
                final List<MarriageFee> fee = marriageFeeService.getActiveGeneralTypeFeeses();
                for (final MarriageFee marriageFee : fee)
                    if (days >= marriageFee.getFromDays() && (marriageFee.getToDays() == null || days <= marriageFee.getToDays()))
                        return marriageFee.getFees();
            }
            else if (allowValidation.getValue().equalsIgnoreCase("YES") && days <= 90) {
                final List<MarriageFee> fee = marriageFeeService.getActiveGeneralTypeFeeses();
                for (final MarriageFee marriageFee : fee)
                    if (days >= marriageFee.getFromDays() && (marriageFee.getToDays() == null || days <= marriageFee.getToDays()))
                        return marriageFee.getFees();
            }
        return null;
    }
}