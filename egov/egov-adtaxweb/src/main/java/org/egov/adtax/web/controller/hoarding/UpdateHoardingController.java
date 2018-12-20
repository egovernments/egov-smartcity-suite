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
package org.egov.adtax.web.controller.hoarding;

import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.ANONYMOUS_USER;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.exception.HoardingValidationError;
import org.egov.adtax.service.ReassignAdvertisementService;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.adtax.web.controller.common.HoardingControllerSupport;
import org.egov.adtax.workflow.AdvertisementWorkFlowService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.security.utils.SecurityUtils;
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

    private static final String NOT_AUTHORIZED = "notAuthorized";

    private static final String MESSAGE = "message";

    private static final String APPROVAL_POSITION = "approvalPosition";

    private static final String INVALID_APPROVER = "invalid.approver";

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AdvertisementWorkFlowService advertisementWorkFlowService;

    @Autowired
    private ReassignAdvertisementService reassignAdvertisementService;

    @ModelAttribute("advertisementPermitDetail")
    public AdvertisementPermitDetail advertisementPermitDetail(@PathVariable final String id) {
        return advertisementPermitDetailService.findBy(Long.valueOf(id));
    }

    @RequestMapping(value = "/update/{id}", method = GET)
    public String updateHoarding(@PathVariable final String id, final Model model) {

        User currentUser = securityUtils.getCurrentUser();
        final AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService.findBy(Long.valueOf(id));

        if (!advertisementWorkFlowService.isApplicationOwner(currentUser, advertisementPermitDetail))
            return NOT_AUTHORIZED;

        final WorkflowContainer workFlowContainer = new WorkflowContainer();
        model.addAttribute("dcPending", advertisementDemandService.anyDemandPendingForCollection(advertisementPermitDetail));
        model.addAttribute("advertisementPermitDetail", advertisementPermitDetail);
        model.addAttribute("isEmployee",
                advertisementWorkFlowService.isEmployee(currentUser) && !ANONYMOUS_USER.equalsIgnoreCase(currentUser.getName()));
        populateAdditionalRuleAndPendingActions(model, workFlowContainer, advertisementPermitDetail);

        if (advertisementPermitDetail != null) {
            model.addAttribute("stateType", advertisementPermitDetail.getClass().getSimpleName());
            model.addAttribute("currentState", advertisementPermitDetail.getCurrentState().getValue());
            model.addAttribute("agency", advertisementPermitDetail.getAgency());
            model.addAttribute("advertisementDocuments", advertisementPermitDetail.getAdvertisement().getDocuments());
            model.addAttribute("isReassignEnabled", reassignAdvertisementService.isReassignEnabled());
            model.addAttribute("applicationType", advertisementPermitDetail.getApplicationtype().name());
            model.addAttribute("applicationHistory", advertisementWorkFlowService.getHistory(advertisementPermitDetail));
            prepareWorkflow(model, advertisementPermitDetail, workFlowContainer);
        }
        return HOARDING_UPDATE;
    }

    private void populateAdditionalRuleAndPendingActions(Model model, WorkflowContainer workFlowContainer, AdvertisementPermitDetail advertisementPermitDetail) {
        if (advertisementPermitDetail != null && advertisementPermitDetail.getPreviousapplicationid() != null) {
            model.addAttribute(ADDITIONAL_RULE, AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE);
            workFlowContainer.setAdditionalRule(AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE);
            workFlowContainer.setPendingActions(advertisementPermitDetail.getState().getNextAction());
        } else {
            model.addAttribute(ADDITIONAL_RULE, AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE);
            workFlowContainer.setAdditionalRule(AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE);
            if (advertisementPermitDetail != null)
                workFlowContainer.setPendingActions(advertisementPermitDetail.getState().getNextAction());
        }
    }

    @RequestMapping(value = "update/{id}", method = POST)
    public String updateHoarding(@Valid @ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail,
            final BindingResult resultBinder, final RedirectAttributes redirAttrib, final HttpServletRequest request,
            final Model model,
            @RequestParam String workFlowAction) {

        validateHoardingDocsOnUpdate(advertisementPermitDetail, resultBinder, redirAttrib);
        validateAdvertisementDetails(advertisementPermitDetail, resultBinder);
        if (resultBinder.hasErrors()) {
            populateModelOnErrors(advertisementPermitDetail, model);
            return HOARDING_UPDATE;
        }
        try {
            StringBuilder approverName = getApproverName(request);
            String nextDesignation = getNextDesignation(request);
            String approvalComment = getApprovalComment(request);
            Long approvalPosition = getApprovalPosition(request);
            if (request.getParameter("workFlowAction") != null)
                workFlowAction = request.getParameter("workFlowAction");

            if (AdvertisementTaxConstants.WF_DEMANDNOTICE_BUTTON.equalsIgnoreCase(workFlowAction))
                return "redirect:/advertisement/demandNotice?pathVar=".concat(advertisementPermitDetail.getId().toString());
            else {
                updateHoardingDocuments(advertisementPermitDetail);

                advertisementPermitDetailService.updateAdvertisementPermitDetail(advertisementPermitDetail, approvalPosition,
                        approvalComment,
                        advertisementPermitDetail != null && advertisementPermitDetail.getPreviousapplicationid() != null
                                ? AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE
                                : AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE,
                        workFlowAction);
                if (!advertisementPermitDetail.isValidApprover()) {
                    model.addAttribute("message", INVALID_APPROVER);
                    populateModelOnErrors(advertisementPermitDetail, model);
                    return HOARDING_UPDATE;
                }
                populateAdvertisementNumber(advertisementPermitDetail, redirAttrib);

                if (AdvertisementTaxConstants.WF_APPROVE_BUTTON.equals(workFlowAction))
                    populateMessageOnApprove(advertisementPermitDetail, redirAttrib);
                else if (AdvertisementTaxConstants.WF_REJECT_BUTTON.equalsIgnoreCase(workFlowAction) ||
                        AdvertisementTaxConstants.WF_CANCELAPPLICATION_BUTTON.equalsIgnoreCase(workFlowAction) ||
                        AdvertisementTaxConstants.WF_CANCELRENEWAL_BUTTON.equalsIgnoreCase(workFlowAction))
                    populateMessageOnRejectOrCancel(advertisementPermitDetail, redirAttrib);
                else if (AdvertisementTaxConstants.WF_PERMITORDER_BUTTON.equalsIgnoreCase(workFlowAction))
                    return redirectToPermitOrder(advertisementPermitDetail);
                else
                    populateMessage(advertisementPermitDetail, redirAttrib, approverName, nextDesignation);

                return redirectToSuccess(advertisementPermitDetail);
            }
        } catch (final HoardingValidationError e) {
            resultBinder.rejectValue(e.fieldName(), e.errorCode());
            return HOARDING_UPDATE;
        }
    }

    private String redirectToSuccess(final AdvertisementPermitDetail advertisementPermitDetail) {
        return new StringBuilder("redirect:/hoarding/success/")
                .append(advertisementPermitDetail != null ? advertisementPermitDetail.getId() : null).toString();
    }

    private String redirectToPermitOrder(final AdvertisementPermitDetail advertisementPermitDetail) {
        return new StringBuilder("redirect:/advertisement/permitOrder?pathVar=")
                .append(advertisementPermitDetail != null
                        ? advertisementPermitDetail.getId()
                        : null)
                .toString();
    }

    private void populateAdvertisementNumber(final AdvertisementPermitDetail advertisementPermitDetail,
            final RedirectAttributes redirAttrib) {
        if (advertisementPermitDetail != null)
            redirAttrib.addFlashAttribute("advertisementNumber",
                    advertisementPermitDetail.getAdvertisement().getAdvertisementNumber());
    }

    private Long getApprovalPosition(final HttpServletRequest request) {
        Long approvalPosition = 0l;
        if (request.getParameter(APPROVAL_POSITION) != null && !request.getParameter(APPROVAL_POSITION).isEmpty())
            approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));
        return approvalPosition;
    }

    private String getApprovalComment(final HttpServletRequest request) {
        String approvalComment = "";
        if (request.getParameter("approvalComent") != null)
            approvalComment = request.getParameter("approvalComent");
        return approvalComment;
    }

    private StringBuilder getApproverName(final HttpServletRequest request) {
        StringBuilder approverName = new StringBuilder();
        if (request.getParameter("approverName") != null)
            approverName = new StringBuilder(request.getParameter("approverName"));
        return approverName;
    }

    private String getNextDesignation(final HttpServletRequest request) {
        String nextDesignation = "";
        if (request.getParameter("nextDesignation") != null)
            nextDesignation = request.getParameter("nextDesignation");
        return nextDesignation;
    }

    private void populateMessage(final AdvertisementPermitDetail advertisementPermitDetail, final RedirectAttributes redirAttrib,
            StringBuilder approverName, String nextDesignation) {
        if (advertisementPermitDetail != null) {
            String message;
            message = messageSource.getMessage("msg.success.forward.on.reject",
                    new String[] { approverName.append("~").append(nextDesignation).toString(),
                            advertisementPermitDetail.getApplicationNumber() },
                    null);
            redirAttrib.addFlashAttribute(MESSAGE, message);
        }
    }

    private void populateMessageOnRejectOrCancel(final AdvertisementPermitDetail advertisementPermitDetail,
            final RedirectAttributes redirAttrib) {
        if (advertisementPermitDetail != null) {
            StringBuilder approverName = new StringBuilder();
            String nextDesignation;
            String message;
            final Assignment wfInitiator = advertisementWorkFlowService.getWorkFlowInitiator(advertisementPermitDetail);
            if (ApplicationThreadLocals.getUserId().equals(wfInitiator.getEmployee().getId())) {
                message = messageSource.getMessage("msg.success.cancelled",
                        new String[] { advertisementPermitDetail.getApplicationNumber() }, null);
                redirAttrib.addFlashAttribute(MESSAGE, message);
            } else {
                approverName.append(wfInitiator.getEmployee().getName());
                nextDesignation = wfInitiator.getDesignation().getName();
                message = messageSource.getMessage("msg.success.reject",
                        new String[] { advertisementPermitDetail.getApplicationNumber(),
                                approverName.append("~").append(nextDesignation).toString() },
                        null);
                redirAttrib.addFlashAttribute(MESSAGE, message);
            }
        }
    }

    private void populateMessageOnApprove(final AdvertisementPermitDetail advertisementPermitDetail,
            final RedirectAttributes redirAttrib) {
        String message;
        if (advertisementPermitDetail != null) {
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
        }
    }

    private void populateModelOnErrors(final AdvertisementPermitDetail advertisementPermitDetail, final Model model) {
        final WorkflowContainer workFlowContainer = new WorkflowContainer();

        populateAdditionalRuleAndPendingActions(model, workFlowContainer, advertisementPermitDetail);

        if (advertisementPermitDetail != null) {
            prepareWorkflow(model, advertisementPermitDetail, workFlowContainer);
            model.addAttribute("isEmployee",
                    advertisementWorkFlowService.isEmployee(securityUtils.getCurrentUser())
                            && !ANONYMOUS_USER.equalsIgnoreCase(securityUtils.getCurrentUser().getName()));
            model.addAttribute("stateType", advertisementPermitDetail.getClass().getSimpleName());
            model.addAttribute("currentState", advertisementPermitDetail.getCurrentState().getValue());
            model.addAttribute("isReassignEnabled", reassignAdvertisementService.isReassignEnabled());
            model.addAttribute("applicationType", advertisementPermitDetail.getApplicationtype().name());

        }
    }
}
