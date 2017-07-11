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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.service.BudgetControlTypeService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.works.abstractestimate.entity.AbstractEstimate.EstimateStatus;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.entity.FinancialDetail;
import org.egov.works.abstractestimate.service.ActivityService;
import org.egov.works.letterofacceptance.service.WorkOrderActivityService;
import org.egov.works.lineestimate.entity.EstimateAppropriation;
import org.egov.works.lineestimate.service.EstimateAppropriationService;
import org.egov.works.masters.service.ScheduleOfRateService;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate;
import org.egov.works.revisionestimate.entity.RevisionAbstractEstimate.RevisionEstimateStatus;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.revisionestimate.service.RevisionEstimateService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrderActivity;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import com.google.gson.JsonObject;

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
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private BudgetDetailsDAO budgetDetailsDAO;

    @Autowired
    private EstimateAppropriationService estimateAppropriationService;

    @Autowired
    private BudgetControlTypeService budgetControlTypeService;

    @Autowired
    private ScheduleOfRateService scheduleOfRateService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private WorkOrderActivityService workOrderActivityService;

    @Autowired
    private MBHeaderService mbHeaderService;

    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showForm(@ModelAttribute("revisionEstimate") final RevisionAbstractEstimate revisionEstimate,
            @RequestParam final Long workOrderEstimateId, final Model model) {

        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService
                .getWorkOrderEstimateById(workOrderEstimateId);
        revisionEstimate.setParent(workOrderEstimate.getEstimate());
        revisionEstimateService.loadViewData(revisionEstimate, workOrderEstimate, model);
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        workflowContainer.setAdditionalRule(
                (String) cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
        model.addAttribute(WorksConstants.ADDITIONAL_RULE,
                cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
        if (revisionEstimate.getState() != null)
            workflowContainer.setPendingActions("");
        workflowContainer.setAmountRule(
                revisionEstimate.getEstimateValue() != null ? revisionEstimate.getEstimateValue() : BigDecimal.ZERO);
        prepareWorkflow(model, revisionEstimate, workflowContainer);
        final List<String> validActions = new ArrayList<String>();
        validActions.add(WorksConstants.SAVE_ACTION);
        validActions.add(WorksConstants.FORWARD_ACTION.toString());
        validActions.add(WorksConstants.CREATE_AND_APPROVE);
        if (revisionEstimate.getState() != null && revisionEstimate.getState().getNextAction() != null)
            model.addAttribute("nextAction", revisionEstimate.getState().getNextAction());
        model.addAttribute("validActionList", validActions);
        model.addAttribute(WorksConstants.MODE, null);
        model.addAttribute("defaultDepartmentId", worksUtils.getDefaultDepartmentId());
        return "revisionEstimate-form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String save(@ModelAttribute("revisionEstimate") final RevisionAbstractEstimate revisionEstimate,
            final RedirectAttributes redirectAttributes, final Model model, final BindingResult bindErrors,
            final HttpServletRequest request, @RequestParam String workFlowAction) {
        RevisionAbstractEstimate savedRevisionEstimate;
        Long approvalPosition = 0l;
        String approvalComment = "";
        String additionalRule = "";
        if (request.getParameter("approvalComent") != null)
            approvalComment = request.getParameter("approvalComent");
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");
        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
        if (request.getParameter(WorksConstants.ADDITIONAL_RULE) != null)
            additionalRule = request.getParameter(WorksConstants.ADDITIONAL_RULE);
        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService
                .getWorkOrderEstimateByAbstractEstimateId(revisionEstimate.getParent().getId());

        final JsonObject jsonObject = new JsonObject();
        revisionEstimateService.validateREInDrafts(workOrderEstimate.getEstimate().getId(), jsonObject, bindErrors);
        revisionEstimateService.validateREInWorkFlow(workOrderEstimate.getEstimate().getId(), jsonObject, bindErrors);
        revisionEstimateService.validateChangeQuantityActivities(revisionEstimate, bindErrors);
        revisionEstimateService.validateNontenderedActivities(revisionEstimate, bindErrors);
        revisionEstimateService.validateLumpsumActivities(revisionEstimate, bindErrors);
        revisionEstimateService.validateWorkflowActionButton(revisionEstimate, bindErrors, additionalRule, workFlowAction);
        if (!BudgetControlType.BudgetCheckOption.NONE.toString()
                .equalsIgnoreCase(budgetControlTypeService.getConfigValue()))
            validateBudgetAmount(revisionEstimate,
                    bindErrors);
        if (bindErrors.hasErrors()) {
            revisionEstimateService.loadViewData(revisionEstimate, workOrderEstimate, model);

            for (final Activity activity : revisionEstimate.getNonTenderedActivities())
                activity.setSchedule(scheduleOfRateService.getScheduleOfRateById(activity.getSchedule().getId()));
            for (final Activity activity : revisionEstimate.getChangeQuantityActivities()) {
                activity.setParent(activityService.findOne(activity.getParent().getId()));
                final WorkOrderActivity workOrderActivity = workOrderActivityService
                        .getWorkOrderActivityByActivity(activity.getParent().getId());
                if (workOrderActivity != null) {
                    final Double consumedQuantity = mbHeaderService.getPreviousCumulativeQuantity(-1L,
                            workOrderActivity.getId());
                    activity.setConsumedQuantity(consumedQuantity == null ? 0 : consumedQuantity);
                    activity.setEstimateQuantity(workOrderActivity.getApprovedQuantity());
                    if ("-".equalsIgnoreCase(activity.getSignValue()))
                        activity.setRevisionType(RevisionType.REDUCED_QUANTITY);
                    else
                        activity.setRevisionType(RevisionType.ADDITIONAL_QUANTITY);
                }
            }

            final WorkflowContainer workflowContainer = new WorkflowContainer();
            workflowContainer.setAdditionalRule(
                    (String) cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
            model.addAttribute(WorksConstants.ADDITIONAL_RULE,
                    cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
            workflowContainer.setAmountRule(revisionEstimate.getEstimateValue());
            prepareWorkflow(model, revisionEstimate, workflowContainer);
            final List<String> validActions = new ArrayList<String>();
            validActions.add(WorksConstants.SAVE_ACTION);
            validActions.add(WorksConstants.FORWARD_ACTION.toString());
            if (revisionEstimate.getState() != null && revisionEstimate.getState().getNextAction() != null) {
                model.addAttribute("nextAction", revisionEstimate.getState().getNextAction());
                model.addAttribute("pendingActions", revisionEstimate.getState().getNextAction());
            }

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
                    revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(
                            WorksConstants.REVISIONABSTRACTESTIMATE, EstimateStatus.CREATED.toString()));
                else if (WorksConstants.CREATE_AND_APPROVE.equals(workFlowAction))
                    revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(
                            WorksConstants.REVISIONABSTRACTESTIMATE, EstimateStatus.APPROVED.toString()));
                else
                    revisionEstimate.setEgwStatus(worksUtils.getStatusByModuleAndCode(
                            WorksConstants.REVISIONABSTRACTESTIMATE, EstimateStatus.NEW.toString()));

            try {
                savedRevisionEstimate = revisionEstimateService.createRevisionEstimate(revisionEstimate,
                        approvalPosition, approvalComment, additionalRule, workFlowAction);
            } catch (final ValidationException e) {
                final String errorMessage = messageSource.getMessage("error.budgetappropriation.insufficient.amount",
                        new String[] {}, null);
                model.addAttribute("message", errorMessage);
                return "revisionEstimate-success";
            }
        }

        if (EstimateStatus.NEW.toString().equals(savedRevisionEstimate.getEgwStatus().getCode()))
            return "redirect:/revisionestimate/update/" + savedRevisionEstimate.getId() + "?mode=save";

        return "redirect:/revisionestimate/revisionestimate-success?revisionEstimate=" + savedRevisionEstimate.getId()
                + "&approvalPosition=" + approvalPosition;
    }

    private void validateBudgetAmount(final RevisionAbstractEstimate revisionAbstractEstimate, final BindingResult errors) {
        final List<Long> budgetheadid = new ArrayList<Long>();
        final FinancialDetail financialDetail = revisionAbstractEstimate.getParent().getFinancialDetails().get(0);
        budgetheadid.add(financialDetail.getBudgetGroup().getId());

        try {
            final BigDecimal budgetAvailable = budgetDetailsDAO.getPlanningBudgetAvailable(
                    worksUtils.getFinancialYearByDate(new Date()).getId(),
                    Integer.parseInt(revisionAbstractEstimate.getParent().getExecutingDepartment().getId().toString()),
                    financialDetail.getFunction().getId(), null,
                    financialDetail.getScheme() == null ? null
                            : Integer.parseInt(financialDetail.getScheme().getId().toString()),
                    financialDetail.getSubScheme() == null ? null
                            : Integer.parseInt(financialDetail.getSubScheme().getId().toString()),
                    null, budgetheadid, Integer.parseInt(financialDetail.getFund().getId().toString()));

            if (BudgetControlType.BudgetCheckOption.MANDATORY.toString()
                    .equalsIgnoreCase(budgetControlTypeService.getConfigValue())
                    && budgetAvailable.compareTo(revisionAbstractEstimate.getEstimateValue()) == -1)
                errors.reject("error.budgetappropriation.insufficient.amount", null);
        } catch (final ValidationException e) {
            // TODO: Used ApplicationRuntimeException for time being since there is issue in session after
            // budgetDetailsDAO.getPlanningBudgetAvailable API call
            // TODO: needs to replace with errors.reject
            for (final ValidationError error : e.getErrors())
                throw new ApplicationRuntimeException(error.getKey());
        }
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
        String message = StringUtils.EMPTY;
        if (RevisionEstimateStatus.NEW.toString().equals(revisionEstimate.getEgwStatus().getCode()))
            message = messageSource.getMessage("msg.revisionestimate.saved",
                    new String[] { revisionEstimate.getEstimateNumber() }, null);
        else if (RevisionEstimateStatus.CREATED.toString().equals(revisionEstimate.getEgwStatus().getCode())
                && !WorksConstants.WF_STATE_REJECTED.equals(revisionEstimate.getState().getValue()))
            message = messageSource.getMessage("msg.revisionestimate.created",
                    new String[] { approverName, nextDesign, revisionEstimate.getEstimateNumber() }, null);
        else if (RevisionEstimateStatus.RESUBMITTED.toString().equals(revisionEstimate.getEgwStatus().getCode())
                && !WorksConstants.WF_STATE_REJECTED.equals(revisionEstimate.getState().getValue()))
            message = messageSource.getMessage("msg.revisionestimate.resubmitted",
                    new String[] { approverName, nextDesign, revisionEstimate.getEstimateNumber() }, null);
        else if (revisionEstimate.getState() != null
                && WorksConstants.WF_STATE_REJECTED.equals(revisionEstimate.getState().getValue()))
            message = messageSource.getMessage("msg.revisionestimate.rejected",
                    new String[] { revisionEstimate.getEstimateNumber(), approverName, nextDesign }, null);
        else if (RevisionEstimateStatus.CANCELLED.toString().equals(revisionEstimate.getEgwStatus().getCode()))
            message = messageSource.getMessage("msg.revisionestimate.cancel",
                    new String[] { revisionEstimate.getEstimateNumber() }, null);
        else if (RevisionEstimateStatus.APPROVED.toString().equalsIgnoreCase(revisionEstimate.getEgwStatus().getCode())) {
            final EstimateAppropriation lea = estimateAppropriationService
                    .findLatestByAbstractEstimate(revisionEstimate);
            if(lea != null && lea.getBudgetUsage() != null)
                message = messageSource.getMessage("msg.revisionestimate.approved",
                        new String[] { revisionEstimate.getEstimateNumber(), lea.getBudgetUsage().getAppropriationnumber() }, null);
            else
                message = messageSource.getMessage("msg.revisionestimate.approved.success",
                        new String[] { revisionEstimate.getEstimateNumber() }, null);
        } else if (RevisionEstimateStatus.CHECKED.toString().equalsIgnoreCase(revisionEstimate.getEgwStatus().getCode()))
            message = messageSource.getMessage("msg.revisionestimate.checked",
                    new String[] { revisionEstimate.getEstimateNumber(), approverName, nextDesign }, null);
        return message;

    }
}
