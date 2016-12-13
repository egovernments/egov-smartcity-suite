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
package org.egov.ptis.web.controller.vacancyremission;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.VacancyRemission;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/vacancyremission")
public class UpdateVacancyRemissionController extends GenericWorkFlowController {

	private static final String VACANCYREMISSION_EDIT = "vacancyRemission-edit";
    private static final String VACANCYREMISSION_SUCCESS = "vacancyRemission-success";
    private VacancyRemissionService vacancyRemissionService;

    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    public UpdateVacancyRemissionController(VacancyRemissionService vacancyRemissionService,
            PropertyTaxUtil propertyTaxUtil) {
        this.propertyTaxUtil = propertyTaxUtil;
        this.vacancyRemissionService = vacancyRemissionService;
    }
    
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    
    @Autowired
    private SecurityUtils securityUtils;
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private PositionMasterService positionMasterService;

    @ModelAttribute
    public VacancyRemission vacancyRemissionModel(@PathVariable Long id) {
        return vacancyRemissionService.getVacancyRemissionById(id);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final Long id, final HttpServletRequest request) {
        VacancyRemission vacancyRemission = vacancyRemissionService.getVacancyRemissionById(id);
        String userDesignationList=propertyTaxCommonUtils.getAllDesignationsForUser(securityUtils.getCurrentUser().getId());
        if (vacancyRemission != null) {
            model.addAttribute("stateType", vacancyRemission.getClass().getSimpleName());
            model.addAttribute("currentState", vacancyRemission.getCurrentState().getValue());
            if(!vacancyRemission.getDocuments().isEmpty()){
                model.addAttribute("attachedDocuments", vacancyRemission.getDocuments());
            }
            model.addAttribute("userDesignationList", userDesignationList);
            model.addAttribute("designation", COMMISSIONER_DESGN);
            prepareWorkflow(model, vacancyRemission, new WorkflowContainer());
            BasicProperty basicProperty = vacancyRemission.getBasicProperty();
            vacancyRemissionService.addModelAttributes(model, basicProperty);
        }
        return VACANCYREMISSION_EDIT;
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute VacancyRemission vacancyRemission, final BindingResult resultBinder,
            final RedirectAttributes redirectAttributes, final HttpServletRequest request, final Model model) {

        String senderName = vacancyRemission.getCurrentState().getSenderName();
        Assignment currAsssignment=assignmentService.getPrimaryAssignmentForUser(securityUtils.getCurrentUser().getId());
        String currDesignation = currAsssignment.getDesignation().getName();
        senderName=senderName.substring(senderName.lastIndexOf(":") + 1);
        if (!resultBinder.hasErrors()) {
            String workFlowAction = "";
            if (request.getParameter("workFlowAction") != null)
                workFlowAction = request.getParameter("workFlowAction");

            Long approvalPosition = 0l;
            String approvalComent = "";
            String successMsg = "";
            if (request.getParameter("approvalComent") != null)
                approvalComent = request.getParameter("approvalComent");
            if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
                approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
            else if (COMMISSIONER_DESGN.equalsIgnoreCase(currDesignation)
                    && (workFlowAction.equalsIgnoreCase(PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD)
                            || workFlowAction.equalsIgnoreCase(PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT))) {
                approvalPosition = Long.valueOf(vacancyRemission.getState().getInitiatorPosition().getId());
            }
            Boolean propertyByEmployee = Boolean.valueOf(request.getParameter("propertyByEmployee"));
            vacancyRemissionService.saveVacancyRemission(vacancyRemission, approvalPosition, approvalComent, null,
                    workFlowAction,propertyByEmployee);

            if (StringUtils.isNotBlank(workFlowAction) && !workFlowAction.equalsIgnoreCase(PropertyTaxConstants.WFLOW_ACTION_STEP_NOTICE_GENERATE)) {
                if (workFlowAction.equalsIgnoreCase(PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE)) {
                    successMsg = "Vacancy Remission Approved Successfully in the System";
                } else if (workFlowAction.equalsIgnoreCase(PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT)) {
                    successMsg = "Vacancy Remission rejected successfully and forwarded to : "
                            + vacancyRemissionService.getInitiatorName(vacancyRemission);
                }else if(workFlowAction.equalsIgnoreCase(PropertyTaxConstants.WFLOW_ACTION_STEP_FORWARD) && COMMISSIONER_DESGN.equalsIgnoreCase(currDesignation)){
                    successMsg = "Vacancy Remission Approved successfully and forwarded to : "
                            + vacancyRemissionService.getInitiatorName(vacancyRemission);
                }              
                else {
                    successMsg = "Vacancy Remission Saved Successfully in the System and forwarded to : "
                            + propertyTaxUtil.getApproverUserName(approvalPosition)+" with application number : "+vacancyRemission.getApplicationNumber();
                }
            }

            model.addAttribute("successMessage", successMsg);
            if (StringUtils.isNotBlank(workFlowAction)
                    && workFlowAction.equalsIgnoreCase(PropertyTaxConstants.WFLOW_ACTION_STEP_NOTICE_GENERATE)) {
                String pathVars = vacancyRemission.getBasicProperty().getUpicNo() + "," + senderName;
                return "redirect:/vacancyremission/rejectionacknowledgement?pathVar=" + pathVars;
            }
        }
        return VACANCYREMISSION_SUCCESS;
    }
}
