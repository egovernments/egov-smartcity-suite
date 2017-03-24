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
import org.egov.adtax.entity.enums.AdvertisementStatus;
import org.egov.adtax.entity.enums.AdvertisementApplicationType;
import org.egov.adtax.exception.HoardingValidationError;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.adtax.web.controller.common.HoardingControllerSupport;
import org.egov.eis.web.contract.WorkflowContainer;
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
@RequestMapping("/advertisement")
public class AdvertisementRenewalController extends HoardingControllerSupport {
	
    private static final String RENEWAL_NEWFORM = "renewal-newform";
    private static final String MESSAGE = "message";
    private static final String RENEWAL_ERROR = "renewal-error";
    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @RequestMapping(value = "/renewal/{id}", method = GET)
    public String renewForm(@PathVariable final String id, final Model model,
            @ModelAttribute final AdvertisementPermitDetail renewalPermitDetail) {
        final AdvertisementPermitDetail parentPermitDetail = advertisementPermitDetailService.findBy(Long.valueOf(id));
        
        if (parentPermitDetail!=null && parentPermitDetail.getAdvertisement() != null && parentPermitDetail.getAdvertisement().getDemandId() != null) {
            // CHECK ANY DEMAND PENDING for selected year.
            if (!advertisementDemandService.checkAnyTaxPendingForSelectedFinancialYear(parentPermitDetail.getAdvertisement(),parentPermitDetail.getAdvertisement().getDemandId().getEgInstallmentMaster())) {
                model.addAttribute(MESSAGE, "msg.renewal.taxNotPending");
                return RENEWAL_ERROR;
            }
        }
        if (parentPermitDetail!=null && parentPermitDetail.getAdvertisement() != null && parentPermitDetail.getAdvertisement().getStatus()!=null && parentPermitDetail.getAdvertisement().getStatus().equals(AdvertisementStatus.WORKFLOW_IN_PROGRESS)) {
             model.addAttribute(MESSAGE, "msg.renewal.alreadyInWorkFlow");
                return RENEWAL_ERROR;
           
        }
        //If curernt status of permit is approved, then payment is pending for the selected record.
        if(parentPermitDetail!=null && parentPermitDetail.getAdvertisement() != null && parentPermitDetail.getAdvertisement().getStatus()!=null && parentPermitDetail.getAdvertisement().getStatus().equals(AdvertisementStatus.ACTIVE)  &&
                parentPermitDetail.getStatus()!=null && parentPermitDetail.getStatus().getCode().equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_APPROVED))
        {
            model.addAttribute(MESSAGE, "msg.renewal.paymentPending");
            return RENEWAL_ERROR;
        }
        
        
        loadBasicData(model, parentPermitDetail, renewalPermitDetail);
        model.addAttribute("renewalPermitDetail", renewalPermitDetail);
        model.addAttribute("additionalRule", AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE);
        model.addAttribute("stateType", renewalPermitDetail.getClass().getSimpleName());
        model.addAttribute("currentState", "NEW");
        WorkflowContainer workFlowContainer = new WorkflowContainer();
        workFlowContainer.setAdditionalRule(AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE);
        prepareWorkflow(model, renewalPermitDetail, workFlowContainer);
 
        model.addAttribute("agency", parentPermitDetail.getAgency());
        model.addAttribute("advertisementDocuments", parentPermitDetail.getAdvertisement().getDocuments());
        return RENEWAL_NEWFORM;
    }

    private void loadBasicData(final Model model, final AdvertisementPermitDetail parentPermitDetail,
            final AdvertisementPermitDetail renewalPermitDetail) {
        renewalPermitDetail.setAdvertisement(parentPermitDetail.getAdvertisement());
        renewalPermitDetail.setAdvertisementDuration(parentPermitDetail.getAdvertisementDuration());
        renewalPermitDetail.setAdvertisementParticular(parentPermitDetail.getAdvertisementParticular());
        renewalPermitDetail.setAdvertiser(parentPermitDetail.getAdvertiser());
        renewalPermitDetail.setApplicationDate(parentPermitDetail.getApplicationDate());
        renewalPermitDetail.setApplicationNumber(parentPermitDetail.getApplicationNumber());
        renewalPermitDetail.setPermissionNumber(parentPermitDetail.getPermissionNumber());
        renewalPermitDetail.setAgency(parentPermitDetail.getAgency());
        renewalPermitDetail.setEncroachmentFee(parentPermitDetail.getEncroachmentFee());
        renewalPermitDetail.setOwnerDetail(parentPermitDetail.getOwnerDetail());
        renewalPermitDetail.setPermissionstartdate(parentPermitDetail.getPermissionstartdate());
        renewalPermitDetail.setPermissionenddate(parentPermitDetail.getPermissionenddate());
        renewalPermitDetail.setBreadth(parentPermitDetail.getBreadth());
        renewalPermitDetail.setMeasurement(parentPermitDetail.getMeasurement());
        renewalPermitDetail.setLength(parentPermitDetail.getLength());
        renewalPermitDetail.setTaxAmount(parentPermitDetail.getTaxAmount());
        renewalPermitDetail.setPreviousapplicationid(parentPermitDetail);
        renewalPermitDetail.setWidth(parentPermitDetail.getWidth());
        renewalPermitDetail.setUnitOfMeasure(parentPermitDetail.getUnitOfMeasure());
    }

    @RequestMapping(value = "/renewal/{id}", method = POST)
    public String renewSave(@ModelAttribute final AdvertisementPermitDetail renewalPermitDetail,
            final BindingResult resultBinder, final RedirectAttributes redirAttrib, final HttpServletRequest request,
            final Model model,
            @RequestParam String workFlowAction, @PathVariable final String id) {

        validateHoardingDocsOnUpdate(renewalPermitDetail, resultBinder, redirAttrib);
        if (renewalPermitDetail.getState() == null)
            renewalPermitDetail.setStatus(advertisementPermitDetailService
                    .getStatusByModuleAndCode(AdvertisementTaxConstants.APPLICATION_STATUS_CREATED));
        renewalPermitDetail.getAdvertisement().setStatus(AdvertisementStatus.WORKFLOW_IN_PROGRESS);
        renewalPermitDetail.setApplicationtype(AdvertisementApplicationType.RENEW);
        if (resultBinder.hasErrors()) {
            WorkflowContainer workFlowContainer = new WorkflowContainer();
            workFlowContainer.setAdditionalRule(AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE);
            prepareWorkflow(model, renewalPermitDetail, workFlowContainer);
            model.addAttribute("additionalRule", AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE);
            model.addAttribute("stateType", renewalPermitDetail.getClass().getSimpleName());
            model.addAttribute("currentState", "NEW");
            return RENEWAL_NEWFORM;
        }
        try {
            updateHoardingDocuments(renewalPermitDetail);
            Long approvalPosition = 0l;
            String approvalComment = "";
            String approverName = "";
            String nextDesignation = "";
            if (request.getParameter("approvalComent") != null)
                approvalComment = request.getParameter("approvalComent");
            if (request.getParameter("workFlowAction") != null)
                workFlowAction = request.getParameter("workFlowAction");
            if (request.getParameter("approverName") != null)
                approverName = request.getParameter("approverName");
            if (request.getParameter("nextDesignation") != null)
                nextDesignation = request.getParameter("nextDesignation");
            if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
                approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
            advertisementPermitDetailService.renewal(renewalPermitDetail, approvalPosition,
                    approvalComment, AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE, workFlowAction);
            redirAttrib.addFlashAttribute("advertisementPermitDetail", renewalPermitDetail);
            String message = messageSource.getMessage("msg.success.forward",
                    new String[] { approverName.concat("~").concat(nextDesignation), renewalPermitDetail.getApplicationNumber() }, null);
            redirAttrib.addFlashAttribute(MESSAGE, message);
            return "redirect:/hoarding/success/" + renewalPermitDetail.getId();
        } catch (final HoardingValidationError e) {
            resultBinder.rejectValue(e.fieldName(), e.errorCode());
            return RENEWAL_NEWFORM;
        }
    }
}
