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
package org.egov.ptis.web.controller.demolition;

import static org.egov.ptis.constants.PropertyTaxConstants.ADDITIONAL_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.APPLICATION_TYPE_DEMOLITION;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMOLITION;
import static org.egov.ptis.constants.PropertyTaxConstants.DEPUTY_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_SPECIAL_NOTICE;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_WORKFLOW_PROPERTYIMPL_BYID;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
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
import static org.egov.ptis.constants.PropertyTaxConstants.ZONAL_COMMISSIONER_DESIGN;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.service.demolition.PropertyDemolitionService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.exceptions.TaxCalculatorExeption;
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
@RequestMapping(value = "/demolition/update/{id}")
public class UpdatePropertyDemolitionController extends GenericWorkFlowController {

    private static final String DEMOLITION_STATE_NEW = "Demolition:NEW";
    protected static final String DEMOLITION_FORM = "demolition-form";
    protected static final String DEMOLITION_VIEW = "demolition-view";
    protected static final String DEMOLITION_SUCCESS = "demolition-success";
    public static final String EDIT = "edit";
    public static final String VIEW = "view";
    private static final String APPROVAL_POSITION = "approvalPosition";
    private static final String SUCCESSMESSAGE = "successMessage";
    private static final String PROPERTY_MODIFY_REJECT_FAILURE = "Initiator is not active so can not do rejection with the Assessment number :";

    PropertyDemolitionService propertyDemolitionService;

    @Autowired
    private PersistenceService<Property, Long> persistenceService;

    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    public UpdatePropertyDemolitionController(final PropertyDemolitionService propertyDemolitionService) {
        this.propertyDemolitionService = propertyDemolitionService;
    }

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    @Autowired
    private transient PropertyService propService;

    @ModelAttribute
    public PropertyImpl property(@PathVariable final String id) {
        PropertyImpl property = propertyDemolitionService.findByNamedQuery(QUERY_WORKFLOW_PROPERTYIMPL_BYID, Long.valueOf(id));
        if (property == null)
            property = propertyDemolitionService.findByNamedQuery(QUERY_PROPERTYIMPL_BYID, Long.valueOf(id));
        return property;

    }

    @RequestMapping(method = RequestMethod.GET)
    public String view(@ModelAttribute PropertyImpl property, final Model model, @PathVariable final Long id, final HttpServletRequest request) {

        String currentDesignation = null;
        final String currState = property.getState().getValue();
        final String nextAction = property.getState().getNextAction();

        String userDesignationList = propertyTaxCommonUtils.getAllDesignationsForUser(securityUtils.getCurrentUser().getId());
        model.addAttribute("property", property);
        model.addAttribute("stateType", property.getClass().getSimpleName());
        model.addAttribute("currentState", property.getCurrentState().getValue());
        model.addAttribute("pendingActions", nextAction);
        model.addAttribute("additionalRule", DEMOLITION);
        if (!DEMOLITION_STATE_NEW.equals(currState))
            currentDesignation = propertyDemolitionService.getLoggedInUserDesignation(
                    property.getCurrentState().getOwnerPosition().getId(),
                    securityUtils.getCurrentUser());
        if (!currState.endsWith(STATUS_REJECTED))
            model.addAttribute("currentDesignation", currentDesignation);
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        workflowContainer.setPendingActions(nextAction);
        workflowContainer.setAdditionalRule(DEMOLITION);
        prepareWorkflow(model, property, workflowContainer);
        propertyDemolitionService.addModelAttributes(model, property.getBasicProperty());

        model.addAttribute("userDesignationList", userDesignationList);
        model.addAttribute("designation", COMMISSIONER_DESGN);
        if (currState.endsWith(WF_STATE_REJECTED) || nextAction.equalsIgnoreCase(WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING)
                || currState.endsWith(WFLOW_ACTION_NEW)) {
            model.addAttribute("isEmployee", propService.isEmployee(securityUtils.getCurrentUser()));
            model.addAttribute("mode", EDIT);
            return DEMOLITION_FORM;
        } else {
            model.addAttribute("mode", VIEW);
            return DEMOLITION_VIEW;
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final PropertyImpl property, final BindingResult errors,
                         final RedirectAttributes redirectAttributes, final HttpServletRequest request, final Model model,
                         @RequestParam final String workFlowAction) throws TaxCalculatorExeption {
        String workFlowAct = workFlowAction;
        propertyDemolitionService.validateProperty(property, errors, request);
        if (errors.hasErrors()) {
            prepareWorkflow(model, (PropertyImpl) property, new WorkflowContainer());
            model.addAttribute("stateType", property.getClass().getSimpleName());
            return DEMOLITION_FORM;
        } else {

            final Character status = STATUS_WORKFLOW;
            Long approvalPosition = 0l;
            String approvalComent = "";
            final Property oldProperty = property.getBasicProperty().getActiveProperty();

            if (isApprovalCommentNotNull(request))
                approvalComent = request.getParameter("approvalComent");
            if (isWorkFlowActionNotNull(request))
                workFlowAct = request.getParameter("workFlowAction");
            if (isApprovalPosNotNull(request))
                approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));
            approveAction(property, workFlowAct, oldProperty);
            if (isWfNoticeGenOrPrevOrSign(workFlowAct))
                return "redirect:/notice/propertyTaxNotice-generateNotice.action?basicPropId="
                        + property.getBasicProperty().getId() + "&noticeType=" + NOTICE_TYPE_SPECIAL_NOTICE
                        + "&noticeMode=" + APPLICATION_TYPE_DEMOLITION + "&actionType=" + workFlowAct;
            else
                return ifNotNoticeGenAndModeViewOrSave(property, request, model, workFlowAct, status, approvalPosition,
                        approvalComent);
        }
    }

    private void approveAction(final Property property, final String workFlowAct, final Property oldProperty) {
        if (isActionApprove(property, workFlowAct)) {
            if (isOldPropStatActive(oldProperty)) {
                oldProperty.setStatus(STATUS_ISHISTORY);
                persistenceService.persist(oldProperty);
            }
            if (isPropStatWF(property)) {
                property.setStatus(STATUS_ISACTIVE);
                persistenceService.persist(property);
            }
        }
    }

    private boolean isWfNoticeGenOrPrevOrSign(final String workFlowAct) {
        return workFlowAct.equalsIgnoreCase(WFLOW_ACTION_STEP_NOTICE_GENERATE) ||
                WFLOW_ACTION_STEP_PREVIEW.equalsIgnoreCase(workFlowAct) ||
                WFLOW_ACTION_STEP_SIGN.equalsIgnoreCase(workFlowAct);
    }

    private boolean isPropStatWF(final Property property) {
        return property.getStatus().equals(STATUS_WORKFLOW);
    }

    private boolean isOldPropStatActive(final Property oldProperty) {
        return oldProperty.getStatus().equals(STATUS_ISACTIVE);
    }

    private boolean isActionApprove(final Property property, final String workFlowAct) {
        return WFLOW_ACTION_STEP_APPROVE.equalsIgnoreCase(workFlowAct) && isPropStatWF(property);
    }

    private boolean isApprovalPosNotNull(final HttpServletRequest request) {
        return request.getParameter(APPROVAL_POSITION) != null && !request.getParameter(APPROVAL_POSITION).isEmpty();
    }

    private boolean isWorkFlowActionNotNull(final HttpServletRequest request) {
        return request.getParameter("workFlowAction") != null;
    }

    private boolean isApprovalCommentNotNull(final HttpServletRequest request) {
        return request.getParameter("approvalComent") != null;
    }

    private String ifNotNoticeGenAndModeViewOrSave(final Property property, final HttpServletRequest request, final Model model,
                                                   final String workFlowAction, final Character status, final Long approvalPosition, final String approvalComent)
            throws TaxCalculatorExeption {
        final Property oldProperty = property.getBasicProperty().getActiveProperty();
        Long approvalPos;
        ifNotRejectViewOrSave(property, request, workFlowAction, status, approvalPosition, approvalComent, oldProperty);
        if (isWfReject(workFlowAction))
            model.addAttribute(SUCCESSMESSAGE, "Property Demolition approved successfully and forwarded to  "
                    + propertyTaxUtil.getApproverUserName(((PropertyImpl) property).getState().getOwnerPosition().getId())
                    + " with assessment number "
                    + property.getBasicProperty().getUpicNo());
        else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT))
            wFReject(property, request, model, workFlowAction, status, approvalPosition, approvalComent);
        else {
            final Assignment cscAssignment = getCscUserAssignment(property);
            approvalPos = cscAssignment != null ? cscAssignment.getPosition().getId() : approvalPosition;

            model.addAttribute(SUCCESSMESSAGE,
                    "Successfully forwarded to " + propertyTaxUtil.getApproverUserName(approvalPos)
                            + " with application number " + property.getApplicationNo());
        }
        return DEMOLITION_SUCCESS;
    }

    private void wFReject(final Property property, final HttpServletRequest request, final Model model,
                          final String workFlowAction, final Character status, final Long approvalPosition, final String approvalComent)
            throws TaxCalculatorExeption {
        final Property oldProperty = property.getBasicProperty().getActiveProperty();
        Assignment assignment = null;
        final PropertyImpl propertyImpl = (PropertyImpl) property;
        final List<StateHistory> history = propertyImpl.getStateHistory();
        Collections.reverse(history);
        final User user = securityUtils.getCurrentUser();
        String loggedInUserDesignation;
        loggedInUserDesignation = loggedInUserDesignation(propertyImpl, user);

        boolean isNotrejection = true;
        if (loggedInUserDesignation != null && isRoOrCommissioner(loggedInUserDesignation))
            assignment = propertyDemolitionService.getUserAssignmentOnReject(loggedInUserDesignation, (PropertyImpl) property);
        if (loggedInUserDesignation != null && !isRoOrCommissioner(loggedInUserDesignation))
            assignment = propertyDemolitionService.getWfInitiator((PropertyImpl) property);
        if (assignment != null && assignment.getId() != null) {
            if (request.getParameter("mode").equalsIgnoreCase(VIEW))
                propertyDemolitionService.updateProperty(property, approvalComent, workFlowAction,
                        approvalPosition, DEMOLITION);
            else
                propertyDemolitionService.saveProperty(oldProperty, property, status, approvalComent,
                        workFlowAction, approvalPosition, DEMOLITION);
            model.addAttribute(SUCCESSMESSAGE, "Property Demolition rejected successfully and forwared to "
                    + assignment.getEmployee().getName().concat("~").concat(assignment.getPosition().getName())
                    + " with application number "
                    + property.getApplicationNo());
            isNotrejection = false;
        }
        if (isNotrejection)
            model.addAttribute(SUCCESSMESSAGE,PROPERTY_MODIFY_REJECT_FAILURE+property.getBasicProperty().getUpicNo());
    }

    private String loggedInUserDesignation(final PropertyImpl propertyImpl, final User user) {
        String loggedInUserDesignation = null;
        List<Assignment> loggedInUserAssign;
        if (propertyImpl.getState() != null) {
            loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    propertyImpl.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
            loggedInUserDesignation = !loggedInUserAssign.isEmpty() ? loggedInUserAssign.get(0).getDesignation().getName() : null;
        }
        return loggedInUserDesignation;
    }

    private boolean isRoOrCommissioner(final String loggedInUserDesignation) {
        boolean isany;
        if (!REVENUE_OFFICER_DESGN.equalsIgnoreCase(loggedInUserDesignation))
            isany = isCommissioner(loggedInUserDesignation);
        else
            isany = true;
        return isany;
    }

    private boolean isCommissioner(final String loggedInUserDesignation) {
        boolean isanyone;
        if (!ASSISTANT_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
                || !ADDITIONAL_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
            isanyone = isDeputyOrAbove(loggedInUserDesignation);
        else
            isanyone = true;
        return isanyone;
    }

    private boolean isDeputyOrAbove(final String loggedInUserDesignation) {
        boolean isanyone = false;
        if (DEPUTY_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
                || COMMISSIONER_DESGN.equalsIgnoreCase(loggedInUserDesignation)
                || ZONAL_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
            isanyone = true;
        return isanyone;
    }

    private boolean isWfReject(final String workFlowAction) {
        return workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE);
    }

    private void ifNotRejectViewOrSave(final Property property, final HttpServletRequest request, final String workFlowAction,
                                       final Character status, final Long approvalPosition, final String approvalComent, final Property oldProperty)
            throws TaxCalculatorExeption {
        if (request.getParameter("mode").equalsIgnoreCase(VIEW)) {
            if (!workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT))
                propertyDemolitionService.updateProperty(property, approvalComent, workFlowAction,
                        approvalPosition, DEMOLITION);
        } else if (!workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT))
            propertyDemolitionService.saveProperty(oldProperty, property, status, approvalComent,
                    workFlowAction, approvalPosition, DEMOLITION);
    }

    private Assignment getCscUserAssignment(final Property property) {
        Assignment cscAssignment = null;
        if (!propService.isEmployee(securityUtils.getCurrentUser()))
            cscAssignment = propertyDemolitionService.getUserAssignment(securityUtils.getCurrentUser(), property);
        return cscAssignment;
    }

}
