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
package org.egov.works.web.controller.letterofacceptance;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.FinancialDetail;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.abstractestimate.service.MeasurementSheetService;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/letterofacceptance")
public class UpdateLetterOfAcceptanceController extends GenericWorkFlowController {

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private BudgetDetailsDAO budgetDetailsDAO;

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private MeasurementSheetService measurementSheetService;

    @Autowired
    private CityService cityService;

    @ModelAttribute
    public WorkOrder getWorkOrder(@PathVariable final String id) {
        final WorkOrder workOrder = letterOfAcceptanceService.getWorkOrderById(Long.parseLong(id));
        return workOrder;
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String beforeUpdate(final Model model, @PathVariable final String id, final HttpServletRequest request)
            throws ApplicationException {
        final WorkOrder workOrder = letterOfAcceptanceService.getWorkOrderById(Long.valueOf(id));
        final AbstractEstimate abstractEstimate = workOrder.getWorkOrderEstimates().get(0).getEstimate();
        if (workOrder.getEgwStatus().getCode().equals(WorksConstants.REJECTED)) {
            model.addAttribute(WorksConstants.ADDITIONAL_RULE,
                    cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
            return "redirect:/letterofacceptance/newform?estimateNumber=" + abstractEstimate.getEstimateNumber()
                    + "&mode=edit";
        } else {
            model.addAttribute("stateType", workOrder.getClass().getSimpleName());
            if (workOrder.getCurrentState() != null
                    && !workOrder.getCurrentState().getValue().equalsIgnoreCase(WorksConstants.NEW))
                model.addAttribute("currentState", workOrder.getCurrentState().getValue());
            if (workOrder.getState() != null && workOrder.getState().getNextAction() != null) {
                model.addAttribute("nextAction", workOrder.getState().getNextAction());
                model.addAttribute("pendingActions", workOrder.getState().getNextAction());
            }
            final WorkflowContainer workflowContainer = new WorkflowContainer();
            workflowContainer.setAmountRule(new BigDecimal(workOrder.getWorkOrderAmount()));
            workflowContainer.setPendingActions(workOrder.getState().getNextAction());
            workflowContainer.setAdditionalRule(
                    (String) cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
            prepareWorkflow(model, workOrder, workflowContainer);
            List<String> validActions = Collections.emptyList();
            validActions = customizedWorkFlowService.getNextValidActions(workOrder.getStateType(),
                    workflowContainer.getWorkFlowDepartment(), workflowContainer.getAmountRule(),
                    workflowContainer.getAdditionalRule(), workOrder.getState().getValue(),
                    workflowContainer.getPendingActions(), workOrder.getCreatedDate());
            model.addAttribute("validActionList", validActions);
            model.addAttribute("mode", "workflowView");
            final WorkOrder newWorkOrder = letterOfAcceptanceService.getWorkOrderDocuments(workOrder);
            model.addAttribute("documentDetails", newWorkOrder.getDocumentDetails());
            model.addAttribute("workOrder", newWorkOrder);
            model.addAttribute("abstractEstimate", abstractEstimate);
            model.addAttribute("loggedInUser", securityUtils.getCurrentUser().getName());
            model.addAttribute("measurementsPresent",
                    measurementSheetService.existsByEstimate(abstractEstimate.getId()));
            model.addAttribute("workflowHistory",
                    worksUtils.getHistory(workOrder.getState(), workOrder.getStateHistory()));
            model.addAttribute("amountRule", workOrder.getWorkOrderAmount());
            model.addAttribute("defaultDepartmentId", worksUtils.getDefaultDepartmentId());
            return "letterOfAcceptance-view";
        }
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String forward(@PathVariable final String id, final Model model, @RequestParam String workFlowAction,
            final HttpServletRequest request) {

        final WorkOrder workOrder = letterOfAcceptanceService.getWorkOrderById(Long.valueOf(id));
        Long approvalPosition = 0l;
        String approvalComment = org.apache.commons.lang.StringUtils.EMPTY;
        String additionalRule = org.apache.commons.lang.StringUtils.EMPTY;

        if (request.getParameter("approvalComent") != null)
            approvalComment = request.getParameter("approvalComent");
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");
        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
        if (request.getParameter(WorksConstants.ADDITIONAL_RULE) != null)
            additionalRule = request.getParameter(WorksConstants.ADDITIONAL_RULE);

        final WorkOrder savedWorkOrder = letterOfAcceptanceService.forward(workOrder, approvalPosition, approvalComment,
                additionalRule, workFlowAction);

        if (savedWorkOrder.getEgwStatus().getCode().equals(WorksConstants.APPROVED)) {

            final WorkOrderEstimate workOrderEstimate = savedWorkOrder.getWorkOrderEstimates().get(0);

            letterOfAcceptanceService.sendSmsToContractor(workOrderEstimate);
            letterOfAcceptanceService.sendEmailToContractor(workOrderEstimate);
        }

        final String pathVars = worksUtils.getPathVars(savedWorkOrder.getEgwStatus(), savedWorkOrder.getState(),
                savedWorkOrder.getId(), approvalPosition);

        return "redirect:/letterofacceptance/letterofacceptance-success?pathVars=" + pathVars;
    }

    @RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
    public String viewLOA(@PathVariable final String id, final Model model, final HttpServletRequest request)
            throws ApplicationException {
        final WorkOrder workOrder = letterOfAcceptanceService.getWorkOrderById(Long.parseLong(id));
        final AbstractEstimate abstractEstimate = estimateService
                .getAbstractEstimateByEstimateNumber(workOrder.getEstimateNumber());
        final WorkOrder newWorkOrder = letterOfAcceptanceService.getWorkOrderDocuments(workOrder);
        model.addAttribute("documentDetails", newWorkOrder.getDocumentDetails());
        model.addAttribute("workOrder", newWorkOrder);
        model.addAttribute("abstractEstimate", abstractEstimate);
        model.addAttribute("measurementsPresent", measurementSheetService.existsByEstimate(abstractEstimate.getId()));
        model.addAttribute("loggedInUser", securityUtils.getCurrentUser().getName());
        model.addAttribute("mode", "modify");
        model.addAttribute("workflowHistory", worksUtils.getHistory(workOrder.getState(), workOrder.getStateHistory()));
        return "letterOfAcceptance-modify";
    }

    @RequestMapping(value = "/modify/{id}", method = RequestMethod.POST)
    public String modify(@ModelAttribute("workOrder") final WorkOrder workOrder, final Model model,
            final BindingResult resultBinder, final HttpServletRequest request) throws ApplicationException {
        final List<String> workOrderNumbers = letterOfAcceptanceService
                .getApprovedWorkOrdersForCreateContractorBill(workOrder.getWorkOrderNumber());
        if (workOrderNumbers.isEmpty())
            resultBinder.rejectValue("", "error.modify.loa.finalbill.exists");

        final AbstractEstimate abstractEstimate = estimateService
                .getAbstractEstimateByEstimateNumber(workOrder.getEstimateNumber());
        final Double revisedWorkOrderAmount = Double.valueOf(request.getParameter("revisedWorkOrderAmount"));
        final Double revisedValue = Double.valueOf(request.getParameter("revisedValue"));
        Double balanceAmount = 0.0;
        Double grossBillAmount = 0.0;
        final DecimalFormat df = new DecimalFormat("0.00");
        grossBillAmount = letterOfAcceptanceService.getGrossBillAmountOfBillsCreated(workOrder.getWorkOrderNumber(),
                WorksConstants.APPROVED, ContractorBillRegister.BillStatus.CANCELLED.toString());
        if (grossBillAmount == null)
            grossBillAmount = 0.0;
        final BigDecimal ledGrossBillAmount = abstractEstimate.getGrossAmountBilled();
            if (revisedWorkOrderAmount >= 0 && "-".equals(workOrder.getPercentageSign())) {
            if (abstractEstimate != null
                    && abstractEstimate.isSpillOverFlag())
                balanceAmount = ledGrossBillAmount != null  ? workOrder.getWorkOrderAmount() - grossBillAmount - revisedValue
                       - ledGrossBillAmount.doubleValue() : workOrder.getWorkOrderAmount() - grossBillAmount - revisedValue;
            else
                balanceAmount = workOrder.getWorkOrderAmount() - grossBillAmount - revisedValue;
            if (balanceAmount < 0) {
                if (abstractEstimate != null
                        && abstractEstimate.isSpillOverFlag())
                    grossBillAmount += abstractEstimate.getGrossAmountBilled().doubleValue();

                resultBinder.rejectValue(
                        "", "error.modify.loa.appropriation.amount", new String[] {
                                df.format(grossBillAmount), df.format(revisedWorkOrderAmount) },
                        null);
            }
        } else if (revisedWorkOrderAmount >= 0 && "+".equals(workOrder.getPercentageSign()))
            balanceAmount = revisedWorkOrderAmount - workOrder.getWorkOrderAmount();

        if (revisedWorkOrderAmount == 0)
            resultBinder.rejectValue(StringUtils.EMPTY, "error.modify.loa.agreement.amount");

        if (resultBinder.hasErrors()) {
            model.addAttribute("abstractEstimate", abstractEstimate);
            model.addAttribute("mode", "modify");
            model.addAttribute("revisedValue", request.getParameter("revisedValue"));
            return "letterOfAcceptance-modify";
        } else {
            WorkOrder savedWorkOrder = null;
            try {
                savedWorkOrder = letterOfAcceptanceService.update(workOrder, abstractEstimate, revisedValue,
                        revisedWorkOrderAmount);
            } catch (final ValidationException e) {
                final List<Long> budgetheadid = new ArrayList<>();
                BigDecimal budgetAvailable = null;
                if (abstractEstimate != null) {
                    final FinancialDetail financialDetail = abstractEstimate.getFinancialDetails().get(0);
                    budgetheadid.add(financialDetail.getBudgetGroup().getId());
                    try {
                        budgetAvailable = budgetDetailsDAO.getPlanningBudgetAvailable(
                                worksUtils.getFinancialYearByDate(new Date()).getId(),
                                Integer.parseInt(abstractEstimate.getExecutingDepartment().getId().toString()),
                                financialDetail.getFunction().getId(), null,
                                financialDetail.getScheme() == null ? null
                                        : Integer.parseInt(financialDetail.getScheme().getId().toString()),
                                financialDetail.getSubScheme() == null ? null
                                        : Integer.parseInt(financialDetail.getSubScheme().getId().toString()),
                                null, budgetheadid, Integer.parseInt(financialDetail.getFund().getId().toString()));
                        } catch (final ValidationException v) {
                            model.addAttribute("errorMessage", v.getErrors().get(0).getMessage());
                        }
                }

                final String errorMessage = messageSource.getMessage("error.budgetappropriation.amount",
                        new String[] { budgetAvailable.toString(), df.format(balanceAmount).toString() }, null);
                model.addAttribute("message", errorMessage);
                return "lineestimate-success";
            }
            return "redirect:/letterofacceptance/loa-success?loaNumber=" + savedWorkOrder.getWorkOrderNumber()
                    + "&isModify=true";
        }
    }

}
