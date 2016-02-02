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

import static org.egov.mrs.application.Constants.BOUNDARY_TYPE;
import static org.egov.mrs.application.Constants.REVENUE_HIERARCHY_TYPE;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.mrs.application.Utils;
import org.egov.mrs.domain.entity.Registration;
import org.egov.mrs.domain.entity.Witness;
import org.egov.mrs.domain.enums.ApplicationStatus;
import org.egov.mrs.domain.enums.MaritalStatus;
import org.egov.mrs.domain.enums.ReligionPractice;
import org.egov.mrs.domain.service.DocumentService;
import org.egov.mrs.domain.service.RegistrationService;
import org.egov.mrs.masters.service.ActService;
import org.egov.mrs.masters.service.FeeService;
import org.egov.mrs.masters.service.ReligionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles the Marriage Registration
 *
 * @author nayeem
 *
 */

@Controller
@RequestMapping(value = "/registration")
public class NewRegistrationController extends GenericWorkFlowController {

    private final ReligionService religionService;
    private final BoundaryService boundaryService;
    private final ActService actService;
    private final RegistrationService registrationService;

    @Autowired
    private FeeService feeService;

    @Autowired
    private Utils utils;

    @Autowired
    private DocumentService documentService;

    @Autowired
    public NewRegistrationController(final RegistrationService registrationService, final ReligionService religionService,
            final BoundaryService boundaryService, final ActService actService) {
        this.registrationService = registrationService;
        this.religionService = religionService;
        this.boundaryService = boundaryService;
        this.actService = actService;
    }

    @ModelAttribute
    public void prepareForm(final Model model) {
        model.addAttribute("zones",
                boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(BOUNDARY_TYPE, REVENUE_HIERARCHY_TYPE));
        model.addAttribute("religions", religionService.getReligions());
        model.addAttribute("acts", actService.getActs());
        model.addAttribute("religionPractice", Arrays.asList(ReligionPractice.values()));
        model.addAttribute("maritalStatusList", Arrays.asList(MaritalStatus.values()));
        model.addAttribute("feesList", feeService.getAll());
        model.addAttribute("documents", documentService.getAll());
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistration(@ModelAttribute final Registration registration, final Model model) {
        prepareWorkflow(model, registration, new WorkflowContainer());
        return "registration-form";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute final Registration registration,
            @ModelAttribute final WorkflowContainer workflowContainer,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors) {

        if (errors.hasErrors())
            return "registration-form";

        obtainWorkflowParameters(workflowContainer, request);
        final String appNo = registrationService.createRegistration(registration, workflowContainer);
        model.addAttribute("ackNumber", appNo);

        return "registration-ack";
    }

    @RequestMapping(value = "/{registrationId}", method = RequestMethod.GET)
    public String viewRegistration(@PathVariable final Long registrationId, @RequestParam(required = false) String mode,
            final Model model) throws IOException {
        final Registration registration = registrationService.get(registrationId);

        model.addAttribute("registration", registration);
        model.addAttribute("husbandPhoto", Base64.getEncoder().encodeToString(registration.getHusband().getPhoto()));
        model.addAttribute("wifePhoto", Base64.getEncoder().encodeToString(registration.getWife().getPhoto()));
        model.addAttribute("mode", mode);

        String screen = null;

        if (registration.getStatus() != ApplicationStatus.Approved) {
            if (mode == null)
                mode = utils.isLoggedInUserApprover() ? "view" : mode;

            screen = mode != null && mode.equalsIgnoreCase("view") ? "registration-view" : "registration-form";

        } else
            screen = "registration-view";

        int i = 0;
        for (final Witness witness : registration.getWitnesses())
            model.addAttribute("witness" + i++ + "Photo", Base64.getEncoder().encodeToString(witness.getPhoto()));

        prepareWorkflow(model, registration, new WorkflowContainer());
        return screen;
    }

    @RequestMapping(value = "/workflow", method = RequestMethod.POST)
    public String handleWorkflowAction(@RequestParam final Long id,
            @ModelAttribute final Registration registration,
            @ModelAttribute final WorkflowContainer workflowContainer,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors) {

        if (errors.hasErrors())
            return "registration-view";

        obtainWorkflowParameters(workflowContainer, request);
        Registration result = null;

        switch (workflowContainer.getWorkFlowAction()) {
        case "Forward":
            result = registrationService.forwardRegistration(id, registration, workflowContainer);
            break;
        case "Approve":
            result = registrationService.approveRegistration(id, workflowContainer);
            break;
        case "Reject":
            result = registrationService.rejectRegistration(id, workflowContainer);
            break;
        case "Close Registration":
            result = registrationService.rejectRegistration(id, workflowContainer);
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
}
