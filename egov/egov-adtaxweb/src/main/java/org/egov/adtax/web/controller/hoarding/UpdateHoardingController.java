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
package org.egov.adtax.web.controller.hoarding;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import javax.servlet.http.HttpServletRequest;

import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.exception.HoardingValidationError;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.adtax.web.controller.common.HoardingControllerSupport;
import org.egov.eis.entity.Assignment;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.utils.EgovThreadLocals;
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

    @ModelAttribute("advertisementPermitDetail")
    public AdvertisementPermitDetail advertisementPermitDetail(@PathVariable final String id) {
        return advertisementPermitDetailService.findBy(Long.valueOf(id));
    }

    @RequestMapping(value = "/update/{id}", method = GET)
    public String updateHoarding(@PathVariable final String id, final Model model) {
        final AdvertisementPermitDetail advertisementPermitDetail = advertisementPermitDetailService.findBy(Long.valueOf(id));
        model.addAttribute("dcPending", advertisementDemandService.anyDemandPendingForCollection(advertisementPermitDetail));
        model.addAttribute("advertisementPermitDetail", advertisementPermitDetail);
      //  model.addAttribute("additionalRule", AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE);
        if(advertisementPermitDetail!=null && advertisementPermitDetail.getPreviousapplicationid()!=null)
            model.addAttribute("additionalRule", AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE);
            else
                model.addAttribute("additionalRule", AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE);
        
        model.addAttribute("stateType", advertisementPermitDetail.getClass().getSimpleName());
        model.addAttribute("currentState", advertisementPermitDetail.getCurrentState().getValue());
        prepareWorkflow(model, advertisementPermitDetail, new WorkflowContainer());
        model.addAttribute("agency", advertisementPermitDetail.getAgency());
        model.addAttribute("advertisementDocuments", advertisementPermitDetail.getAdvertisement().getDocuments());
        return "hoarding-update";
    }

    @RequestMapping(value = "update/{id}", method = POST)
    public String updateHoarding(@ModelAttribute final AdvertisementPermitDetail advertisementPermitDetail,
            final BindingResult resultBinder, final RedirectAttributes redirAttrib, final HttpServletRequest request,
            final Model model,
            @RequestParam String workFlowAction) {

        validateHoardingDocsOnUpdate(advertisementPermitDetail, resultBinder, redirAttrib);

        if (resultBinder.hasErrors()) {
            prepareWorkflow(model, advertisementPermitDetail, new WorkflowContainer());
            
            if(advertisementPermitDetail!=null && advertisementPermitDetail.getPreviousapplicationid()!=null)
            model.addAttribute("additionalRule", AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE);
            else
                model.addAttribute("additionalRule", AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE);
                
            model.addAttribute("stateType", advertisementPermitDetail.getClass().getSimpleName());
            model.addAttribute("currentState", advertisementPermitDetail.getCurrentState().getValue());
            return "hoarding-update";
        }
        try {
            Long approvalPosition = 0l;
            String approvalComment = "";
            String approverName = "";
            String nextDesignation = "";
            if (request.getParameter("approverName") != null)
                approverName = request.getParameter("approverName");
            if (request.getParameter("nextDesignation") != null)
                nextDesignation = request.getParameter("nextDesignation");
            if (request.getParameter("approvalComent") != null)
                approvalComment = request.getParameter("approvalComent");
            if (request.getParameter("workFlowAction") != null)
                workFlowAction = request.getParameter("workFlowAction");
            if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
                approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
            if (AdvertisementTaxConstants.WF_DEMANDNOTICE_BUTTON.equalsIgnoreCase(workFlowAction))
                return "redirect:/advertisement/demandNotice?pathVar=" + advertisementPermitDetail.getId();
            else {
                updateHoardingDocuments(advertisementPermitDetail);
                
            advertisementPermitDetailService.updateAdvertisementPermitDetail(advertisementPermitDetail, approvalPosition,
                        approvalComment, (advertisementPermitDetail!=null && advertisementPermitDetail.getPreviousapplicationid()!=null)?AdvertisementTaxConstants.RENEWAL_ADDITIONAL_RULE: AdvertisementTaxConstants.CREATE_ADDITIONAL_RULE, workFlowAction);
                if (AdvertisementTaxConstants.WF_APPROVE_BUTTON.equals(workFlowAction))
                    redirAttrib.addFlashAttribute("message", "msg.success.approved");
                else if (AdvertisementTaxConstants.WF_REJECT_BUTTON.equalsIgnoreCase(workFlowAction)) {
                    final Assignment wfInitiator = advertisementPermitDetailService
                            .getWfInitiator(advertisementPermitDetail);
                    if (EgovThreadLocals.getUserId().equals(wfInitiator.getEmployee().getId()))
                        redirAttrib.addFlashAttribute("message", "msg.success.cancelled");
                    else {
                        redirAttrib.addFlashAttribute("message", "msg.success.reject");
                        redirAttrib.addFlashAttribute("approverName", wfInitiator.getEmployee().getName());
                        redirAttrib.addFlashAttribute("nextDesign", wfInitiator.getDesignation().getName());
                    }
                } else if (AdvertisementTaxConstants.WF_PERMITORDER_BUTTON.equalsIgnoreCase(workFlowAction))
                    return "redirect:/advertisement/permitOrder?pathVar=" + advertisementPermitDetail.getId();
                else {
                    redirAttrib.addFlashAttribute("message", "msg.success.forward.on.reject");
                    redirAttrib.addFlashAttribute("approverName", approverName);
                    redirAttrib.addFlashAttribute("nextDesign", nextDesignation);
                }
                return "redirect:/hoarding/success/" + advertisementPermitDetail.getId();
            }
        } catch (final HoardingValidationError e) {
            resultBinder.rejectValue(e.fieldName(), e.errorCode());
            return "hoarding-update";
        }
    }
}
