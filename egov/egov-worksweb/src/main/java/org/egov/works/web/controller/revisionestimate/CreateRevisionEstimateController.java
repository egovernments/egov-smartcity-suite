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
package org.egov.works.web.controller.revisionestimate;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.workflow.matrix.service.CustomizedWorkFlowService;
import org.egov.works.abstractestimate.entity.AbstractEstimate.EstimateStatus;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.egov.works.revisionestimate.service.RevisionEstimateService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/revisionestimate")
public class CreateRevisionEstimateController extends GenericWorkFlowController {

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private RevisionEstimateService revisionEstimateService;

    @Autowired
    protected CustomizedWorkFlowService customizedWorkFlowService;

    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showForm(@ModelAttribute("revisionEstimate") final RevisionAbstractEstimate revisionEstimate,
            @RequestParam final Long workOrderEstimateId, final Model model) {

        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService.getWorkOrderEstimateById(workOrderEstimateId);
        revisionEstimate.setParent(workOrderEstimate.getEstimate());
        revisionEstimateService.loadViewData(revisionEstimate, workOrderEstimate, model);
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        prepareWorkflow(model, revisionEstimate, workflowContainer);
        List<String> validActions = Collections.emptyList();
        validActions = customizedWorkFlowService.getNextValidActions(revisionEstimate.getStateType(),
                workflowContainer.getWorkFlowDepartment(), workflowContainer.getAmountRule(),
                workflowContainer.getAdditionalRule(), WorksConstants.NEW, workflowContainer.getPendingActions(),
                revisionEstimate.getCreatedDate());
        if (revisionEstimate.getState() != null && revisionEstimate.getState().getNextAction() != null)
            model.addAttribute("nextAction", revisionEstimate.getState().getNextAction());
        model.addAttribute("validActionList", validActions);
        model.addAttribute("mode", null);

        return "revisionEstimate-form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String save(@ModelAttribute final RevisionAbstractEstimate revisionEstimate,
            final RedirectAttributes redirectAttributes, final Model model, final BindingResult bindErrors,
            final HttpServletRequest request, @RequestParam String workFlowAction) {
        RevisionAbstractEstimate savedRevisionEstimate;
        Long approvalPosition = 0l;
        String approvalComment = "";
        if (request.getParameter("approvalComment") != null)
            approvalComment = request.getParameter("approvalComent");
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");
        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService
                .getWorkOrderEstimateByAbstractEstimateId(revisionEstimate.getParent().getId());
        
        revisionEstimateService.validateChangeQuantityActivities(revisionEstimate, bindErrors);
        if (bindErrors.hasErrors()) {
            revisionEstimateService.loadViewData(revisionEstimate, workOrderEstimate, model);

            final WorkflowContainer workflowContainer = new WorkflowContainer();
            prepareWorkflow(model, revisionEstimate, workflowContainer);
            List<String> validActions = Collections.emptyList();
            validActions = customizedWorkFlowService.getNextValidActions(revisionEstimate.getStateType(),
                    workflowContainer.getWorkFlowDepartment(), workflowContainer.getAmountRule(),
                    workflowContainer.getAdditionalRule(), WorksConstants.NEW, workflowContainer.getPendingActions(),
                    revisionEstimate.getCreatedDate());
            if (revisionEstimate.getState() != null && revisionEstimate.getState().getNextAction() != null)
                model.addAttribute("nextAction", revisionEstimate.getState().getNextAction());
            model.addAttribute("validActionList", validActions);
            model.addAttribute("mode", null);
            model.addAttribute("approvalDesignation", request.getParameter("approvalDesignation"));
            model.addAttribute("approvalPosition", request.getParameter("approvalPosition"));
            model.addAttribute("designation", request.getParameter("designation"));
            model.addAttribute("approvedByValue", request.getParameter("approvedBy"));
            return "revisionEstimate-form";
        } else {

            if (revisionEstimate.getState() == null)
                if (WorksConstants.FORWARD_ACTION.equals(workFlowAction))
                    revisionEstimate.setEgwStatus(
                            worksUtils.getStatusByModuleAndCode(WorksConstants.REVISIONABSTRACTESTIMATE,
                                    EstimateStatus.CREATED.toString()));
                else
                    revisionEstimate.setEgwStatus(
                            worksUtils.getStatusByModuleAndCode(WorksConstants.REVISIONABSTRACTESTIMATE,
                                    EstimateStatus.NEW.toString()));

            savedRevisionEstimate = revisionEstimateService.createRevisionEstimate(revisionEstimate, approvalPosition,
                    approvalComment, null, workFlowAction);

        }

        if (EstimateStatus.NEW.toString().equals(savedRevisionEstimate.getEgwStatus().getCode()))
            return "redirect:/revisionestimate/update/" + savedRevisionEstimate.getId() + "?mode=save";

        return "redirect:/revisionestimate/revisionestimate-success?revisionEstimate=" + savedRevisionEstimate.getId()
                + "&approvalPosition=" + approvalPosition;
    }

    @RequestMapping(value = "/revisionestimate-success", method = RequestMethod.GET)
    public ModelAndView successView(@ModelAttribute RevisionAbstractEstimate revisionEstimate,
            @RequestParam("revisionEstimate") Long id, @RequestParam("approvalPosition") final Long approvalPosition,
            final HttpServletRequest request, final Model model) {

        if (id != null)
            revisionEstimate = revisionEstimateService.getRevisionEstimateById(id);

        final String pathVars = worksUtils.getPathVars(revisionEstimate.getEgwStatus(), revisionEstimate.getState(),
                revisionEstimate.getId(), approvalPosition);

        final String[] keyNameArray = pathVars.split(",");
        String approverName = "";
        String currentUserDesgn = "";
        String nextDesign = "";
        if (keyNameArray.length != 0 && keyNameArray.length > 0)
            if (keyNameArray.length == 1 && keyNameArray[0] != null)
                id = Long.parseLong(keyNameArray[0]);
            else if (keyNameArray.length == 3) {
                if (keyNameArray[0] != null)
                    id = Long.parseLong(keyNameArray[0]);
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
            } else {
                if (keyNameArray[0] != null)
                    id = Long.parseLong(keyNameArray[0]);
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
                nextDesign = keyNameArray[3];
            }

        model.addAttribute("approverName", approverName);
        model.addAttribute("currentUserDesgn", currentUserDesgn);
        model.addAttribute("nextDesign", nextDesign);

        final String message = getMessageByStatus(revisionEstimate, approverName, nextDesign);

        model.addAttribute("message", message);

        return new ModelAndView("revisionEstimate-success", "revisionEstimate", revisionEstimate);
    }

    private String getMessageByStatus(final RevisionAbstractEstimate revisionEstimate, final String approverName,
            final String nextDesign) {
        String message = "";
        if (EstimateStatus.NEW.toString().equals(revisionEstimate.getEgwStatus().getCode()))
            message = messageSource.getMessage("msg.revisionestimate.saved",
                    new String[] { revisionEstimate.getEstimateNumber() }, null);
        else if (EstimateStatus.CREATED.toString().equals(revisionEstimate.getEgwStatus().getCode())
                && !WorksConstants.WF_STATE_REJECTED.equals(revisionEstimate.getState().getValue()))
            message = messageSource.getMessage("msg.revisionestimate.created",
                    new String[] { approverName, nextDesign, revisionEstimate.getEstimateNumber() }, null);
        else if (EstimateStatus.RESUBMITTED.toString().equals(revisionEstimate.getEgwStatus().getCode())
                && !WorksConstants.WF_STATE_REJECTED.equals(revisionEstimate.getState().getValue()))
            message = messageSource.getMessage("msg.revisionestimate.resubmitted",
                    new String[] { approverName, nextDesign, revisionEstimate.getEstimateNumber() }, null);
        else if (revisionEstimate.getState() != null
                && WorksConstants.WF_STATE_REJECTED.equals(revisionEstimate.getState().getValue()))
            message = messageSource.getMessage("msg.revisionestimate.rejected",
                    new String[] { revisionEstimate.getEstimateNumber(), approverName, nextDesign }, null);
        else if (EstimateStatus.CANCELLED.toString().equals(revisionEstimate.getEgwStatus().getCode()))
            message = messageSource.getMessage("msg.revisionestimate.cancelled",
                    new String[] { revisionEstimate.getEstimateNumber() }, null);
        else if (EstimateStatus.APPROVED.toString().equalsIgnoreCase(revisionEstimate.getEgwStatus().getCode()))
            message = messageSource.getMessage("msg.revisionestimate.approved",
                    new String[] { revisionEstimate.getEstimateNumber() }, null);
        return message;

    }
}
