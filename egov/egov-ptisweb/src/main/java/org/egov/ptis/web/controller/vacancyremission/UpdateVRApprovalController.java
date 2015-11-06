/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.ptis.web.controller.vacancyremission;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infstr.utils.StringUtils;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.entity.property.VacancyRemissionApproval;
import org.egov.ptis.domain.service.property.VacancyRemissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/vacancyremission")
public class UpdateVRApprovalController extends GenericWorkFlowController {

	
	private VacancyRemissionService vacancyRemissionService;
	
	private PropertyTaxUtil propertyTaxUtil;
	
	@Autowired
	public UpdateVRApprovalController(VacancyRemissionService vacancyRemissionService,PropertyTaxUtil propertyTaxUtil){
		this.propertyTaxUtil = propertyTaxUtil;
		this.vacancyRemissionService = vacancyRemissionService;
	}
	
	@ModelAttribute
	public VacancyRemissionApproval vacancyRemissionApprovalModel(@PathVariable Long id ) {
		VacancyRemissionApproval vacancyRemissionApproval = vacancyRemissionService.getVacancyRemissionApprovalById(id);
		return vacancyRemissionApproval;
	}
	
	@RequestMapping(value = "/approval/update/{id}", method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final Long id, final HttpServletRequest request) {
		VacancyRemissionApproval vacancyRemissionApproval = vacancyRemissionService.getVacancyRemissionApprovalById(id);
		if(vacancyRemissionApproval!=null){
			model.addAttribute("stateType", vacancyRemissionApproval.getClass().getSimpleName());
			model.addAttribute("currentState", vacancyRemissionApproval.getCurrentState().getValue());
			prepareWorkflow(model,vacancyRemissionApproval,new WorkflowContainer());
			BasicProperty basicProperty = vacancyRemissionApproval.getVacancyRemission().getBasicProperty();
			vacancyRemissionService.addModelAttributes(model, basicProperty);
			model.addAttribute("detailsHistory",vacancyRemissionService.getMonthlyDetailsHistory(vacancyRemissionApproval.getVacancyRemission()));
			model.addAttribute("workflowHistory", vacancyRemissionApproval.getStateHistory());
		}
        return "vacancyRemissionApproval-edit";
    }
	
	@RequestMapping(value = "/approval/update/{id}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute VacancyRemissionApproval vacancyRemissionApproval,
            final BindingResult resultBinder, final RedirectAttributes redirectAttributes,
            final HttpServletRequest request, final Model model) {
		
		String senderName = vacancyRemissionApproval.getCurrentState().getSenderName();
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

            vacancyRemissionService.saveVacancyRemissionApproval(vacancyRemissionApproval,approvalPosition,approvalComent,null,workFlowAction);

            if(StringUtils.isNotBlank(workFlowAction)){
            	if(workFlowAction.equalsIgnoreCase(PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE)){
            		successMsg = "Vacancy Remission Approved Successfully in the System";
            	}else if(workFlowAction.equalsIgnoreCase(PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT)){
            		successMsg = "Vacancy Remission rejected successfully and forwarded to : "+vacancyRemissionApproval.getCreatedBy().getUsername();
            	}else{
            		successMsg = "Vacancy Remission Saved Successfully in the System and forwarded to : "+propertyTaxUtil.getApproverUserName(approvalPosition);
            	}
            }
            
            model.addAttribute("successMessage", successMsg);
            if (StringUtils.isNotBlank(workFlowAction)
                    && workFlowAction.equalsIgnoreCase(PropertyTaxConstants.WFLOW_ACTION_STEP_NOTICE_GENERATE)){
            	String pathVars = vacancyRemissionApproval.getVacancyRemission().getBasicProperty().getUpicNo()+","+senderName;
            	return "redirect:/vacancyremission/rejectionacknowledgement?pathVar=" + pathVars;
            }
        }
		return "vacancyRemission-success";
	}
}
