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

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.utils.StringUtils;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
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
@RequestMapping(value = "/registration")
public class UpdateMarriageRegistrationController extends MarriageRegistrationController {

    private static final Logger LOG = Logger.getLogger(UpdateMarriageRegistrationController.class);
    private static final String MRG_REGISTRATION_EDIT = "registration-correction";
    private static final String MRG_REGISTRATION_EDIT_APPROVED = "registration-update-approved";
    private static final String MRG_REGISTRATION_SUCCESS = "registration-ack";

    @Autowired
    private FileStoreService fileStoreService;

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String showRegistration(@PathVariable final Long id, final Model model) {
        buildMrgRegistrationUpdateResult(id, model);
        return MRG_REGISTRATION_EDIT;
    }

    @RequestMapping(value = "/update-approved/{id}", method = RequestMethod.GET)
    public String editApprovedRegistration(@PathVariable final Long id, final Model model) {
        buildMrgRegistrationUpdateResult(id, model);
        return MRG_REGISTRATION_EDIT_APPROVED;
    }

    private void buildMrgRegistrationUpdateResult(final Long id, final Model model) {
        final MarriageRegistration registration = marriageRegistrationService.get(id);
        marriageRegistrationService.prepareDocumentsForView(registration);
        marriageApplicantService.prepareDocumentsForView(registration.getHusband());
        marriageApplicantService.prepareDocumentsForView(registration.getWife());
        model.addAttribute("applicationHistory",
                marriageRegistrationService.getHistory(registration));
        prepareWorkFlowForNewMarriageRegistration(registration, model);
        registration.getWitnesses().forEach(
                witness -> {
                    try {
                        if (witness.getPhotoFileStore() != null) {
                            final File file = fileStoreService.fetch(witness.getPhotoFileStore().getFileStoreId(),
                                    MarriageConstants.FILESTORE_MODULECODE);
                            witness.setEncodedPhoto(Base64.getEncoder().encodeToString(FileCopyUtils.copyToByteArray(file)));
                        }
                    } catch (final Exception e) {
                        LOG.error("Error while preparing the document for view", e);
                    }
                });
        model.addAttribute("registration", registration);
    }

    private void prepareWorkFlowForNewMarriageRegistration(final MarriageRegistration registration, final Model model) {
        final WorkflowContainer workFlowContainer = new WorkflowContainer();
        workFlowContainer.setAdditionalRule(MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        prepareWorkflow(model, registration, workFlowContainer);
        model.addAttribute("additionalRule", MarriageConstants.ADDITIONAL_RULE_REGISTRATION);
        model.addAttribute("stateType", registration.getClass().getSimpleName());
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateRegistration(@ModelAttribute MarriageRegistration marriageRegistration,
            @ModelAttribute final WorkflowContainer workflowContainer,
            final Model model,
            final HttpServletRequest request,
            final BindingResult errors) throws IOException {

        String workFlowAction = "";
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");

        if (errors.hasErrors())
            return MRG_REGISTRATION_EDIT;
        String message = StringUtils.EMPTY;
        if (workFlowAction != null && !workFlowAction.isEmpty()) {
            workflowContainer.setWorkFlowAction(workFlowAction);
            workflowContainer.setApproverComments(request.getParameter("approvalComent"));
            if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_REJECT) ||
                    workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_CANCEL)) {
                marriageRegistrationService.rejectRegistration(marriageRegistration, workflowContainer);
                message = messageSource.getMessage("msg.rejected.registration", null, null);
            }
            else if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_APPROVE)) {
                marriageRegistrationService.approveRegistration(marriageRegistration, workflowContainer);
                message = messageSource.getMessage("msg.approved.registration", null, null);
            }
            else if (workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_PRINTCERTIFICATE)) {
                marriageRegistrationService.printCertificate(marriageRegistration, workflowContainer, request);
                message = messageSource.getMessage("msg.printcertificate.registration", null, null);
            }
            else {
                workflowContainer.setApproverPositionId(Long.valueOf(request.getParameter("approvalPosition")));
                marriageRegistrationService.forwardRegistration(marriageRegistration, workflowContainer);
                message = messageSource.getMessage("msg.forward.registration", null, null);
            }
        }
        // On print certificate, output registration certificate
        if (workFlowAction != null && !workFlowAction.isEmpty()
                && workFlowAction.equalsIgnoreCase(MarriageConstants.WFLOW_ACTION_STEP_PRINTCERTIFICATE))
            return "redirect:/certificate/registration?id="
            + marriageRegistration.getId();
        // + registration.getMarriageCertificate().get(0).getFileStore().getId();

        model.addAttribute("message", message);
        return MRG_REGISTRATION_SUCCESS;
    }

    @RequestMapping(value = "/update-approved", method = RequestMethod.POST)
    public String updateApprovedRegistration(@RequestParam final Long id,
            @ModelAttribute final MarriageRegistration registration,
            final Model model, final HttpServletRequest request, final BindingResult errors) {
        if (errors.hasErrors())
            return MRG_REGISTRATION_EDIT_APPROVED;

        marriageRegistrationService.updateRegistration(registration);
        model.addAttribute("message", messageSource.getMessage("msg.update.registration", null, null));
        return MRG_REGISTRATION_SUCCESS;
    }
}