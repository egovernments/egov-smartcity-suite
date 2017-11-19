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
package org.egov.ptis.web.controller.vacancyremission;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.entity.property.VacancyRemissionApproval;
import org.egov.ptis.domain.entity.property.VacancyRemissionDetails;
import org.egov.ptis.domain.service.property.VacancyRemissionService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_VACANCY_REMISSION;

@Controller
@RequestMapping(value = "/vacancyremissionapproval/create/{assessmentNo}")
public class VacanyRemissionApprovalController extends GenericWorkFlowController {

    private PropertyTaxUtil propertyTaxUtil;

    private VacancyRemissionService vacancyRemissionService;
    private static final String APPROVAL_POS = "approvalPosition";

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    private SecurityUtils securityUtils;
    
    @Autowired
    public VacanyRemissionApprovalController(VacancyRemissionService vacancyRemissionService,
            PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
        this.vacancyRemissionService = vacancyRemissionService;
    }

    @ModelAttribute
    public VacancyRemissionApproval getVacancyRemissionApproval(@PathVariable final String assessmentNo) {
        VacancyRemissionApproval vacancyRemissionApproval = new VacancyRemissionApproval();
        VacancyRemission vacancyRemission = vacancyRemissionService.getApprovedVacancyRemissionForProperty(assessmentNo);
        if (vacancyRemission != null) {
            vacancyRemissionApproval.setVacancyRemission(vacancyRemission);
        }
        return vacancyRemissionApproval;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String newform(final Model model,@ModelAttribute VacancyRemissionApproval vacancyRemissionApproval, @PathVariable final String assessmentNo, final HttpServletRequest request) {
        VacancyRemission vacancyRemission=vacancyRemissionApproval.getVacancyRemission();
        if (vacancyRemission != null) {
            vacancyRemissionService.addModelAttributes(model, vacancyRemission.getBasicProperty());
            model.addAttribute("stateType", vacancyRemissionApproval.getClass().getSimpleName());
            prepareWorkflow(model, vacancyRemissionApproval, new WorkflowContainer());
            model.addAttribute("detailsHistory", vacancyRemissionService.getMonthlyDetailsHistory(vacancyRemission));
            if(!vacancyRemission.getDocuments().isEmpty()){
                model.addAttribute("attachedDocuments", vacancyRemission.getDocuments());
            }
        model.addAttribute("transactionType", APPLICATION_TYPE_VACANCY_REMISSION);
        model.addAttribute("endorsementRequired", propertyTaxCommonUtils.getEndorsementGenerate(securityUtils.getCurrentUser().getId(),
                vacancyRemission.getCurrentState()));
        model.addAttribute("ownersName", vacancyRemission.getBasicProperty().getFullOwnerName());
        model.addAttribute("applicationNo", vacancyRemission.getApplicationNumber());
        model.addAttribute("endorsementNotices", propertyTaxCommonUtils.getEndorsementNotices(vacancyRemission.getApplicationNumber()));
        }
        return "vacancyRemissionApproval-form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveVacancyRemissionApproval(
            @Valid @ModelAttribute VacancyRemissionApproval vacancyRemissionApproval, final BindingResult resultBinder,
            RedirectAttributes redirectAttributes, final Model model, final HttpServletRequest request,
            @RequestParam String workFlowAction) {
        VacancyRemission vacancyRemission=vacancyRemissionApproval.getVacancyRemission();
        BasicProperty basicProperty = vacancyRemissionApproval.getVacancyRemission().getBasicProperty();
        if (resultBinder.hasErrors()) {
            if (vacancyRemission != null) {
                prepareWorkflow(model, vacancyRemissionApproval, new WorkflowContainer());
                model.addAttribute("stateType", vacancyRemission.getClass().getSimpleName());
                vacancyRemissionService.addModelAttributes(model, basicProperty);
                model.addAttribute("detailsHistory", vacancyRemissionService.getMonthlyDetailsHistory(vacancyRemission));
            }
            return "vacancyRemissionApproval-form";
        } else {
            Long approvalPosition = 0l;
            String approvalComent = "";

            if (request.getParameter("approvalComent") != null)
                approvalComent = request.getParameter("approvalComent");
            if (request.getParameter("workFlowAction") != null)
                workFlowAction = request.getParameter("workFlowAction");
            if (request.getParameter(APPROVAL_POS) != null && !request.getParameter(APPROVAL_POS).isEmpty())
                approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POS));

            List<VacancyRemissionApproval> remissionApprovalList = new ArrayList<>();
            if (vacancyRemission.getVacancyRemissionApproval() == null
                    || vacancyRemission.getVacancyRemissionApproval().isEmpty()) {
                remissionApprovalList.add(vacancyRemissionApproval);
            } else {
                remissionApprovalList = vacancyRemission.getVacancyRemissionApproval();
                remissionApprovalList.add(vacancyRemissionApproval);
            }
            vacancyRemission.setVacancyRemissionApproval(remissionApprovalList);
            VacancyRemissionDetails remissionDetails = new VacancyRemissionDetails();
            remissionDetails.setComments(vacancyRemissionApproval.getComments());
            remissionDetails.setCheckinDate(new Date());
            remissionDetails.setVacancyRemission(vacancyRemission);
            vacancyRemission.getVacancyRemissionDetails().add(remissionDetails);
            vacancyRemissionApproval.setCheckinDate(new Date());

            vacancyRemissionService.saveVacancyRemissionApproval(vacancyRemissionApproval, approvalPosition,
                    approvalComent, null, workFlowAction);
            vacancyRemissionService.closeVacancyRemission(vacancyRemission);

            String successMsg = "Vacancy Remission Final Approval saved and forwarded to : "
                    + propertyTaxUtil.getApproverUserName(approvalPosition);
            model.addAttribute("successMessage", successMsg);
        }

        return "vacancyRemission-success";
    }
}
