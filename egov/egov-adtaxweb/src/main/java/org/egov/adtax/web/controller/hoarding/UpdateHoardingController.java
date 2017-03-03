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
package org.egov.adtax.web.controller.hoarding;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;

import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.exception.HoardingValidationError;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.adtax.web.controller.common.HoardingControllerSupport;
import org.egov.adtax.workflow.AdvertisementWorkFlowService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hoarding")
public class UpdateHoardingController extends HoardingControllerSupport {

    private static final String ADDITIONAL_RULE = "additionalRule";

    private static final String HOARDING_UPDATE = "hoarding-update";

    private static final String MESSAGE = "message";

    private static final String APPROVAL_POSITION = "approvalPosition";

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private AdvertisementWorkFlowService advertisementWorkFlowService;

    @ModelAttribute("advertisementPermitDetail")
    public AdvertisementPermitDetail advertisementPermitDetail(@PathVariable final String id) {
        return advertisementPermitDetailService.findBy(Long.valueOf(id));
    }

    @RequestMapping(value = "/update/{id}", method = GET)
    public String updateHoarding(@PathVariable final String id, final Model model) {
        final WorkflowContainer workFlowContainer = new WorkflowContainer();
        final AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService.findBy(Long.valueOf(id));
        model.addAttribute("dcPending", advertisementDemandService.anyDemandPendingForCollection(advertisementPermitDetail));
        model.addAttribute("advertisementPermitDetail", advertisementPermitDetail);
       if (advertisementPermitDetail != null && advertisementPermitDetail.getPreviousapplicationid() != null) {
            model.addAttribute(ADDITIONAL_RULE, AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE);
            workFlowContainer.setAdditionalRule(AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE);
        } else {
            model.addAttribute(ADDITIONAL_RULE, AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE);
            workFlowContainer.setAdditionalRule(AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE);
        }

       if(advertisementPermitDetail!=null) {
        model.addAttribute("stateType", advertisementPermitDetail.getClass().getSimpleName());
        model.addAttribute("currentState", advertisementPermitDetail.getCurrentState().getValue());
        model.addAttribute("agency", advertisementPermitDetail.getAgency());
        model.addAttribute("advertisementDocuments", advertisementPermitDetail.getAdvertisement().getDocuments());
        prepareWorkflow(model, advertisementPermitDetail, workFlowContainer);
       }
        return HOARDING_UPDATE;
    }

    @RequestMapping(value = "update/{id}", method = POST)
    public String updateHoarding(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail,
            final BindingResult resultBinder, final RedirectAttributes redirAttrib, final HttpServletRequest request,
            final Model model,
            @RequestParam String workFlowAction) {

        validateHoardingDocsOnUpdate(advertisementPermitDetail, resultBinder, redirAttrib);
        validateAdvertisementDetails(advertisementPermitDetail, resultBinder);
        if (resultBinder.hasErrors()) {
            final WorkflowContainer workFlowContainer = new WorkflowContainer();

            if (advertisementPermitDetail != null && advertisementPermitDetail.getPreviousapplicationid() != null) {
                model.addAttribute(ADDITIONAL_RULE, AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE);
                workFlowContainer.setAdditionalRule(AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE);
            } else {
                model.addAttribute(ADDITIONAL_RULE, AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE);
                workFlowContainer.setAdditionalRule(AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE);
            }
            if (advertisementPermitDetail != null){
            prepareWorkflow(model, advertisementPermitDetail, workFlowContainer);
            model.addAttribute("stateType", advertisementPermitDetail.getClass().getSimpleName());
            model.addAttribute("currentState", advertisementPermitDetail.getCurrentState().getValue());
            }
            return HOARDING_UPDATE;
        }
        try {
            Long approvalPosition = 0l;
            String approvalComment = "";
            String approverName = "";
            String nextDesignation = "";
            String message;
            if (request.getParameter("approverName") != null)
                approverName = request.getParameter("approverName");
            if (request.getParameter("nextDesignation") != null)
                nextDesignation = request.getParameter("nextDesignation");
            if (request.getParameter("approvalComent") != null)
                approvalComment = request.getParameter("approvalComent");
            if (request.getParameter("workFlowAction") != null)
                workFlowAction = request.getParameter("workFlowAction");
            if (request.getParameter(APPROVAL_POSITION) != null && !request.getParameter(APPROVAL_POSITION).isEmpty())
                approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));

            if (AdvertisementTaxConstants.WF_DEMANDNOTICE_BUTTON.equalsIgnoreCase(workFlowAction))
                return "redirect:/advertisement/demandNotice?pathVar=" + advertisementPermitDetail.getId();
            else {
                updateHoardingDocuments(advertisementPermitDetail);

                advertisementPermitDetailService.updateAdvertisementPermitDetail(advertisementPermitDetail, approvalPosition,
                        approvalComment,
                        advertisementPermitDetail != null && advertisementPermitDetail.getPreviousapplicationid() != null
                                ? AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE
                                : AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE,
                        workFlowAction);
                redirAttrib.addFlashAttribute("advertisementNumber",
                        advertisementPermitDetail.getAdvertisement().getAdvertisementNumber());

                if (AdvertisementTaxConstants.WF_APPROVE_BUTTON.equals(workFlowAction)) {
                    if (advertisementPermitDetail.getPreviousapplicationid() != null)
                        message = messageSource.getMessage("msg.renewal.success.approved",
                                new String[] { advertisementPermitDetail.getAdvertisement().getAdvertisementNumber(),
                                        advertisementPermitDetail.getPermissionNumber() },
                                null);
                    else
                        message = messageSource.getMessage("msg.success.approved",
                                new String[] { advertisementPermitDetail.getAdvertisement().getAdvertisementNumber(),
                                        advertisementPermitDetail.getPermissionNumber() },
                                null);
                    redirAttrib.addFlashAttribute(MESSAGE, message);
                } else if (AdvertisementTaxConstants.WF_REJECT_BUTTON.equalsIgnoreCase(workFlowAction) ||
                        AdvertisementTaxConstants.WF_CANCELAPPLICATION_BUTTON.equalsIgnoreCase(workFlowAction) ||
                        AdvertisementTaxConstants.WF_CANCELRENEWAL_BUTTON.equalsIgnoreCase(workFlowAction)) {
                    final Assignment wfInitiator = advertisementWorkFlowService.getWorkFlowInitiator(advertisementPermitDetail);
                    if (ApplicationThreadLocals.getUserId().equals(wfInitiator.getEmployee().getId())) {
                        message = messageSource.getMessage("msg.success.cancelled",
                                new String[] { advertisementPermitDetail.getApplicationNumber() }, null);
                        redirAttrib.addFlashAttribute(MESSAGE, message);
                    } else {
                        approverName = wfInitiator.getEmployee().getName();
                        nextDesignation = wfInitiator.getDesignation().getName();
                        message = messageSource.getMessage("msg.success.reject",
                                new String[] { advertisementPermitDetail.getApplicationNumber(),
                                        approverName.concat("~").concat(nextDesignation) },
                                null);
                        redirAttrib.addFlashAttribute(MESSAGE, message);
                    }
                } else if (AdvertisementTaxConstants.WF_PERMITORDER_BUTTON.equalsIgnoreCase(workFlowAction))
                    return "redirect:/advertisement/permitOrder?pathVar=" + advertisementPermitDetail.getId();
                else {
                    message = messageSource.getMessage("msg.success.forward.on.reject",
                            new String[] { approverName.concat("~").concat(nextDesignation),
                                    advertisementPermitDetail.getApplicationNumber() },
                            null);
                    redirAttrib.addFlashAttribute(MESSAGE, message);
                }

                return "redirect:/hoarding/success/" + advertisementPermitDetail.getId();
            }
        } catch (final HoardingValidationError e) {
            resultBinder.rejectValue(e.fieldName(), e.errorCode());
            return HOARDING_UPDATE;
        }
    }
}
