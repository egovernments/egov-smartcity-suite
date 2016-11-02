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

import javax.servlet.http.HttpServletRequest;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.service.MarriageApplicantService;
import org.egov.mrs.domain.service.MarriageDocumentService;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.egov.mrs.domain.service.ReIssueService;
import org.egov.mrs.masters.entity.MarriageFee;
import org.egov.mrs.masters.service.MarriageFeeService;
import org.egov.mrs.masters.service.MarriageRegistrationUnitService;
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
 * Handles Marriage Registration ReIssue transaction
 * 
 * @author nayeem
 *
 */
@Controller
@RequestMapping(value = "/reissue")
public class NewReIssueController extends GenericWorkFlowController {

    @Autowired
    private ReIssueService reIssueService;

    @Autowired
    private MarriageFeeService marriageFeeService;

    @Autowired
    private MarriageRegistrationService marriageRegistrationService;

    @Autowired
    private MarriageApplicantService marriageApplicantService;

    @Autowired
    private MarriageDocumentService marriageDocumentService;
  
    @Autowired
    private ReIssueService reissueService;
    
    @Autowired
    private MarriageRegistrationUnitService marriageRegistrationUnitService;
    
    @Autowired
    private BoundaryService boundaryService;
    
	public void prepareNewForm(final Model model) {
		model.addAttribute("marriageRegistrationUnit",
				marriageRegistrationUnitService.getActiveRegistrationunit());
	}
    
    @RequestMapping(value = "/create/{registrationId}", method = RequestMethod.GET)
    public String showReIssueForm(@PathVariable final Long registrationId, final Model model) {

        final MarriageRegistration registration = marriageRegistrationService.get(registrationId);

		if (registration == null) {
			model.addAttribute("message", "msg.invalid.request");
			return "marriagecommon-error";
		} // CHECK ANY RECORD ALREADY IN ISSUE CERTIFICATE WORKFLOW FOR THE SELECTED REGISTRATION.IF YES, DO NOT ALLOW THEM TO PROCESS THE
			// REQUEST.
		else if (reIssueService.checkAnyWorkFlowInProgressForRegistration(registration)) {
			model.addAttribute("message", "msg.workflow.alreadyPresent");
			return "marriagecommon-error";
		}
	   //  marriageRegistrationService.prepareDocumentsForView(registration);
        marriageApplicantService.prepareDocumentsForView(registration.getHusband());
        marriageApplicantService.prepareDocumentsForView(registration.getWife());
          /*
         * registration.getWitnesses() .stream() .filter(witness -> witness.getPhoto() != null && witness.getPhoto().length > 0)
         * .forEach(witness -> witness.setEncodedPhoto(Base64.getEncoder().encodeToString(witness.getPhoto())));
         */

        final ReIssue reIssue = new ReIssue();
        MarriageFee marriageFee=marriageFeeService.getFeeForCriteria(MarriageConstants.REISSUE_FEECRITERIA);
        if(marriageFee!=null){
        reIssue.setFeeCriteria(marriageFee);
        reIssue.setFeePaid(marriageFee.getFees());
        } 
        reIssue.setRegistration(registration);
        
        prepareWorkFlowForNewMarriageRegistration(reIssue, model);
        
        model.addAttribute("reIssue", reIssue);
        model.addAttribute("documents", marriageDocumentService.getIndividualDocuments());
        prepareNewForm(model);
        prepareWorkflow(model, reIssue, new WorkflowContainer());
        return "reissue-form";
    }

    private void prepareWorkFlowForNewMarriageRegistration(final ReIssue reIssue, final Model model) {
        WorkflowContainer workFlowContainer = new WorkflowContainer();
        workFlowContainer.setAdditionalRule(MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        prepareWorkflow(model, reIssue, workFlowContainer);
        model.addAttribute("additionalRule", MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        model.addAttribute("stateType", reIssue.getClass().getSimpleName());
        model.addAttribute("currentState", "NEW");
    }
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createReIssue(@ModelAttribute final ReIssue reIssue,
            @ModelAttribute final WorkflowContainer workflowContainer,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors) {

		if (errors.hasErrors()) {
			prepareWorkFlowForNewMarriageRegistration(reIssue, model);
			return "reissue-form";
		}
        reIssue.setRegistration(marriageRegistrationService.get(reIssue.getRegistration().getId()));
        obtainWorkflowParameters(workflowContainer, request);
        final String appNo = reIssueService.createReIssueApplication(reIssue, workflowContainer);
        model.addAttribute("ackNumber", appNo);

        return "reissue-ack";
    }

    @RequestMapping(value = "/workflow", method = RequestMethod.POST)
    public String handleWorkflowAction(@RequestParam final Long id,
            @ModelAttribute ReIssue reIssue,
            @ModelAttribute final WorkflowContainer workflowContainer,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors) throws IOException {
        
        if (errors.hasErrors())
            return "reissue-view";
       
        obtainWorkflowParameters(workflowContainer, request);
        switch (workflowContainer.getWorkFlowAction()) {
        case "Forward":
            reIssue = reissueService.forwardReIssue(id, reIssue, workflowContainer);
            break;
        case "Cancel ReIssue":
            reIssue = reIssueService.get(id);
            reIssue = reissueService.rejectReIssue(reIssue, workflowContainer,request);
            break;
        }
        // On Cancel, output rejection certificate 
        if (workflowContainer.getWorkFlowAction() != null && !workflowContainer.getWorkFlowAction().isEmpty()
                && workflowContainer.getWorkFlowAction().equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_CANCEL_REISSUE))
            return "redirect:/certificate/reissue?id="
                    + reIssue.getId();
        model.addAttribute("ackNumber", reIssue.getApplicationNo());
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
        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            workflowContainer.setApproverPositionId(Long.valueOf(request.getParameter("approvalPosition")));
    }
}
