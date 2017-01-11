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

package org.egov.ptis.web.controller.transactions.exemption;

import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_EXEMPTION;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.EXEMPTION;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_SPECIAL_NOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_WORKFLOW_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISACTIVE;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_WORKFLOW;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NEW;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_NOTICE_GENERATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_PREVIEW;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.TaxExemptionReason;
import org.egov.ptis.domain.service.exemption.TaxExemptionService;
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

@Controller
@RequestMapping(value = "/exemption/update/{id}")
public class UpdateTaxExemptionController extends GenericWorkFlowController {

    private static final String APPROVAL_POSITION = "approvalPosition";
	protected static final String TAX_EXEMPTION_FORM = "taxExemption-form";
    protected static final String TAX_EXEMPTION_SUCCESS = "taxExemption-success";
    protected static final String TAX_EXEMPTION_VIEW = "taxExemption-view";
    public static final String EDIT = "edit";
    public static final String VIEW = "view";

    private final TaxExemptionService taxExemptionService;

    @Autowired
    public UpdateTaxExemptionController(final TaxExemptionService taxExemptionService) {
        this.taxExemptionService = taxExemptionService;
    }

    private PropertyImpl property;
    private Boolean isExempted = Boolean.FALSE;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @ModelAttribute
    public Property propertyModel(@PathVariable final String id) {
        property = taxExemptionService.findByNamedQuery(QUERY_WORKFLOW_PROPERTYIMPL_BYID, Long.valueOf(id));
        if (property == null)
            property = taxExemptionService.findByNamedQuery(QUERY_PROPERTYIMPL_BYID, Long.valueOf(id));
        return property;
    }

    @SuppressWarnings("unchecked")
    @ModelAttribute("taxExemptionReasons")
    public List<TaxExemptionReason> getTaxExemptionReasons() {
        return taxExemptionService.getSession().createQuery("from TaxExemptionReason where isActive = true order by name").list();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final Long id, final HttpServletRequest request) {
        isExempted = property.getBasicProperty().getActiveProperty().getIsExemptedFromTax();
        String userDesignationList = "";
        final String currState = property.getState().getValue();
        final String nextAction = property.getState().getNextAction();
        userDesignationList = propertyTaxCommonUtils.getAllDesignationsForUser(securityUtils.getCurrentUser().getId());
        model.addAttribute("stateType", property.getClass().getSimpleName());
        model.addAttribute("currentState", property.getCurrentState().getValue());
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        workflowContainer.setPendingActions(nextAction);
        workflowContainer.setAdditionalRule(EXEMPTION);
        prepareWorkflow(model, property, workflowContainer);
        model.addAttribute("userDesignationList", userDesignationList);
        model.addAttribute("designation", COMMISSIONER_DESGN);
        model.addAttribute("isExempted", isExempted);
        model.addAttribute("pendingActions", nextAction);
        model.addAttribute("additionalRule", EXEMPTION);
        final String currentDesignation = taxExemptionService.getLoggedInUserDesignation(
                property.getCurrentState().getOwnerPosition().getId(),
                securityUtils.getCurrentUser());
        if (!(currState.endsWith(STATUS_REJECTED) || currState.endsWith(WFLOW_ACTION_NEW)))
            model.addAttribute("currentDesignation", currentDesignation);

        taxExemptionService.addModelAttributes(model, property.getBasicProperty());
        if (currState.endsWith(WF_STATE_REJECTED) || nextAction.equalsIgnoreCase(WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING)
                || currState.endsWith(WFLOW_ACTION_NEW)) {
            model.addAttribute("mode", EDIT);
            return TAX_EXEMPTION_FORM;
        } else {
            model.addAttribute("mode", VIEW);
            return TAX_EXEMPTION_VIEW;
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final Property property, final BindingResult errors,
            final RedirectAttributes redirectAttributes, final HttpServletRequest request, final Model model,
            @RequestParam String workFlowAction) {

        final Character status = STATUS_WORKFLOW;
        Long approvalPosition = 0l;
        String approvalComent = "";
        String taxExemptedReason = "";

        final Property oldProperty = property.getBasicProperty().getActiveProperty();

        if (request.getParameter("approvalComent") != null)
            approvalComent = request.getParameter("approvalComent");
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");
        if (request.getParameter(APPROVAL_POSITION) != null && !request.getParameter(APPROVAL_POSITION).isEmpty())
            approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));
        final Boolean propertyByEmployee = Boolean.valueOf(request.getParameter("propertyByEmployee"));
        if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE)) {
            property.setStatus(STATUS_ISACTIVE);
            oldProperty.setStatus(STATUS_ISHISTORY);
        }
        final String exemptioReason = property.getTaxExemptedReason().getCode();
        if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_NOTICE_GENERATE) ||
                WFLOW_ACTION_STEP_PREVIEW.equalsIgnoreCase(workFlowAction) ||
                WFLOW_ACTION_STEP_SIGN.equalsIgnoreCase(workFlowAction))
            return "redirect:/notice/propertyTaxNotice-generateExemptionNotice.action?basicPropId="
                    + property.getBasicProperty().getId() + "&noticeType=" + PropertyTaxConstants.NOTICE_TYPE_EXEMPTION
                    + "&noticeMode=" + NOTICE_TYPE_EXEMPTION + "&actionType=" + workFlowAction + "&exemptionReason=" + exemptioReason;
        else {

            if (request.getParameter("mode").equalsIgnoreCase(VIEW))
                taxExemptionService.updateProperty(property, approvalComent, workFlowAction, approvalPosition,
                        propertyByEmployee, EXEMPTION);
            else {
                if (request.getParameter("taxExemptedReason") != null)
                    taxExemptedReason = request.getParameter("taxExemptedReason");
                taxExemptionService.saveProperty(property, oldProperty, status, approvalComent, workFlowAction,
                        approvalPosition, taxExemptedReason, propertyByEmployee, EXEMPTION);
            }
            String successMessage = "";
            Assignment assignment = new Assignment();
            if (property != null && property.getCreatedBy() != null)
                assignment = assignmentService.getPrimaryAssignmentForUser(property.getCreatedBy().getId());
            if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE))
                successMessage = "Property Exemption approved successfully and forwarded to "
                        + propertyTaxUtil.getApproverUserName(((PropertyImpl) property).getState().getOwnerPosition().getId())
                        + " with assessment number "
                        + property.getBasicProperty().getUpicNo();
            else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {
                final PropertyImpl propertyImpl = (PropertyImpl) property;
                final List<StateHistory> history = propertyImpl.getStateHistory();
                Collections.reverse(history);
                final String designation = taxExemptionService.getLoggedInUserDesignation(
                        history.get(0).getOwnerPosition().getId(),
                        securityUtils.getCurrentUser());
                assignment = taxExemptionService.getUserAssignmentOnReject(designation, (PropertyImpl) property);
                if (assignment == null)
                    assignment = taxExemptionService.getWfInitiator((PropertyImpl) property);

                successMessage = "Property Exemption rejected successfully and forwared to "
                        + assignment.getEmployee().getName().concat("~").concat(assignment.getPosition().getName())
                        + " with application number "
                        + property.getApplicationNo();
            } else
                successMessage = "Successfully forwarded to " + propertyTaxUtil.getApproverUserName(approvalPosition)
                        + " with application number " + property.getApplicationNo();

            model.addAttribute("successMessage", successMessage);
            return TAX_EXEMPTION_SUCCESS;
        }
    }

}
