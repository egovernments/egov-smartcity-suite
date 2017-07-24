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
package org.egov.works.web.controller.abstractestimate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.service.BudgetControlTypeService;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Designation;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimate.EstimateStatus;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.abstractestimate.service.MeasurementSheetService;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.masters.service.ScheduleOfRateService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/abstractestimate")
public class UpdateAbstractEstimateController extends GenericWorkFlowController {
    @Autowired
    private EstimateService estimateService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private ScheduleOfRateService scheduleOfRateService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private MeasurementSheetService measurementSheetService;

    @Autowired
    private CityService cityService;

    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    @Autowired
    private DesignationService designationService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<AbstractEstimate> abstractEstimateWorkflowService;

    @Autowired
    private BudgetControlTypeService budgetControlTypeService;

    @ModelAttribute
    public AbstractEstimate getAbstractEstimate(@PathVariable final String abstractEstimateId) {
        final AbstractEstimate abstractEstimate = estimateService.getAbstractEstimateById(Long.parseLong(abstractEstimateId));
        return abstractEstimate;
    }

    @RequestMapping(value = "/update/{abstractEstimateId}", method = RequestMethod.GET)
    public String updateAbstractEstimate(final Model model, @PathVariable final String abstractEstimateId,
            final HttpServletRequest request, @RequestParam(value = "mode", required = false) final String mode)
            throws ApplicationException {
        final AbstractEstimate abstractEstimate = getAbstractEstimate(abstractEstimateId);
        abstractEstimate.setEstimateValue(abstractEstimate.getEstimateValue().setScale(2, BigDecimal.ROUND_HALF_EVEN));
        splitSorAndNonSorActivities(abstractEstimate);
        abstractEstimate.setTempOverheadValues(abstractEstimate.getOverheadValues());
        abstractEstimate.setTempDeductionValues(abstractEstimate.getAbsrtractEstimateDeductions());
        abstractEstimate.setTempAssetValues(abstractEstimate.getAssetValues());
        if (mode != null && mode.equalsIgnoreCase(WorksConstants.SAVE_ACTION))
            model.addAttribute("message",
                    messageSource.getMessage("msg.estimate.saved", new String[] { abstractEstimate.getEstimateNumber() }, null));
        model.addAttribute("defaultDepartmentId", worksUtils.getDefaultDepartmentId());

        return loadViewData(model, request, abstractEstimate);
    }

    private void splitSorAndNonSorActivities(final AbstractEstimate abstractEstimate) {
        abstractEstimate.setSorActivities((List<Activity>) abstractEstimate.getSORActivities());
        abstractEstimate.setNonSorActivities((List<Activity>) abstractEstimate.getNonSORActivities());
    }

    @RequestMapping(value = "/update/{abstractEstimateId}", method = RequestMethod.POST)
    public String update(@ModelAttribute("abstractEstimate") final AbstractEstimate abstractEstimate,
            final BindingResult errors, final RedirectAttributes redirectAttributes,
            final Model model, final HttpServletRequest request, @RequestParam("file") final MultipartFile[] files,
            @RequestParam final String removedActivityIds)
            throws ApplicationException, IOException {

        String mode = "";
        String workFlowAction = "";
        AbstractEstimate updatedAbstractEstimate = null;
        String additionalRule = "";

        if (request.getParameter("mode") != null)
            mode = request.getParameter("mode");

        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");

        Long approvalPosition = 0l;
        String approvalComment = "";

        if (request.getParameter("approvalComent") != null)
            approvalComment = request.getParameter("approvalComent");

        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        if (request.getParameter(WorksConstants.ADDITIONAL_RULE) != null)
            additionalRule = request.getParameter(WorksConstants.ADDITIONAL_RULE);

        // For Get Configured ApprovalPosition from workflow history
        if (approvalPosition == null || approvalPosition.equals(Long.valueOf(0)))
            approvalPosition = estimateService.getApprovalPositionByMatrixDesignation(
                    abstractEstimate, approvalPosition, null,
                    mode, workFlowAction);

        if ((approvalPosition == null || approvalPosition.equals(Long.valueOf(0)))
                && request.getParameter("approvalPosition") != null
                && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        if ((abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.NEW.toString()) ||
                abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.REJECTED.toString()))
                && !workFlowAction.equals(WorksConstants.CANCEL_ACTION)) {
            estimateService.validateMultiYearEstimates(abstractEstimate, errors);
            estimateService.validateMandatory(abstractEstimate, errors);
            estimateService.validateAssetDetails(abstractEstimate, errors);
            estimateService.validateActivities(abstractEstimate, errors);
            estimateService.validateOverheads(abstractEstimate, errors);
            estimateService.validateBudgetHead(abstractEstimate, errors);
            if (!abstractEstimate.isSpillOverFlag())
                estimateService.validateWorkflowActionButton(abstractEstimate, errors, additionalRule, workFlowAction);

            if (!estimateService.checkForDuplicateAccountCodesInEstimateDeductions(abstractEstimate))
                errors.reject("error.abstractestimate.duplicate.accountcodes",
                        "error.abstractestimate.duplicate.accountcodes");
            if (!workFlowAction.equals(WorksConstants.SAVE_ACTION)) {
                if (abstractEstimate.getSorActivities().isEmpty() && abstractEstimate.getNonSorActivities().isEmpty())
                    errors.reject("error.sor.nonsor.required", "error.sor.nonsor.required");
                estimateService.validateLocationDetails(abstractEstimate, errors);
            }
        }

        if (!BudgetControlType.BudgetCheckOption.NONE.toString()
                .equalsIgnoreCase(budgetControlTypeService.getConfigValue())
                && !worksApplicationProperties.lineEstimateRequired()
                && (WorksConstants.CREATE_AND_APPROVE.equals(workFlowAction)
                        || WorksConstants.APPROVE_ACTION.equals(workFlowAction)))
            estimateService.validateBudgetAmount(abstractEstimate,
                    errors);

        if (errors.hasErrors()) {
            for (final Activity activity : abstractEstimate.getSorActivities())
                activity.setSchedule(scheduleOfRateService.getScheduleOfRateById(activity.getSchedule().getId()));
            model.addAttribute("removedActivityIds", removedActivityIds);

            return loadViewData(model, request, abstractEstimate);
        } else {
            if (null != workFlowAction)
                try {
                    updatedAbstractEstimate = estimateService.updateAbstractEstimateDetails(abstractEstimate, approvalPosition,
                            approvalComment, additionalRule, workFlowAction, files, removedActivityIds);
                } catch (final ValidationException e) {
                    final String errorMessage = messageSource.getMessage("error.budgetappropriation.insufficient.amount",
                            new String[] {}, null);
                    model.addAttribute("message", errorMessage);
                    return abstractEstimate.isSpillOverFlag() ? "newAbstractEstimate-spilloverform" : "newAbstractEstimate-form";
                }
            redirectAttributes.addFlashAttribute("abstractEstimate", updatedAbstractEstimate);

            if (updatedAbstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.NEW.toString()))
                return "redirect:/abstractestimate/update/" + updatedAbstractEstimate.getId() + "?mode=save";

            if (approvalPosition == null)
                return "redirect:/abstractestimate/abstractestimate-success?estimate=" + updatedAbstractEstimate.getId()
                        + "&approvalPosition=";
            else
                return "redirect:/abstractestimate/abstractestimate-success?estimate=" + updatedAbstractEstimate.getId()
                        + "&approvalPosition=" + approvalPosition;
        }
    }

    private String loadViewData(final Model model, final HttpServletRequest request,
            final AbstractEstimate abstractEstimate) {
        WorkFlowMatrix wfmatrix = null;
        estimateService.setDropDownValues(model);
        model.addAttribute("stateType", abstractEstimate.getClass().getSimpleName());
        if (abstractEstimate.getCurrentState() != null
                && !abstractEstimate.getCurrentState().getValue().equals(WorksConstants.NEW))
            model.addAttribute("currentState", abstractEstimate.getCurrentState().getValue());

        if (abstractEstimate.getLineEstimateDetails() != null
                && abstractEstimate.getLineEstimateDetails().getLineEstimate().isAbstractEstimateCreated()
                || abstractEstimate.isSpillOverFlag()) {
            final List<Designation> designations = new ArrayList<Designation>();

            final List<AppConfigValues> configValues = appConfigValuesService.getConfigValuesByModuleAndKey(
                    WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_DESIGNATION_TECHSANCTION_AUTHORITY);

            for (final AppConfigValues valuesFordesignation : configValues)
                designations.add(designationService.getDesignationByName(valuesFordesignation.getValue()));
            model.addAttribute("designations", designations);

        }
        if (!abstractEstimate.getEstimateTechnicalSanctions().isEmpty()
                && abstractEstimate.getEstimateTechnicalSanctions().get(0).getTechnicalSanctionBy() != null) {
            final Designation designation = assignmentService.findByEmployeeAndGivenDate(
                    abstractEstimate.getEstimateTechnicalSanctions().get(0).getTechnicalSanctionBy().getId(), new Date()).get(0)
                    .getDesignation();
            model.addAttribute("designation", designation.getId());
            model.addAttribute("technicalSanctionBy",
                    abstractEstimate.getEstimateTechnicalSanctions().get(0).getTechnicalSanctionBy().getId());
        }

        final WorkflowContainer workflowContainer = new WorkflowContainer();
        if (abstractEstimate.isSpillOverFlag()) {
            workflowContainer.setAdditionalRule(WorksConstants.SPILLOVER);
            model.addAttribute(WorksConstants.ADDITIONAL_RULE,
                    WorksConstants.SPILLOVER);
        } else {
            workflowContainer.setAdditionalRule(
                    (String) cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
            model.addAttribute(WorksConstants.ADDITIONAL_RULE,
                    cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
        }
        workflowContainer.setAmountRule(abstractEstimate.getEstimateValue());
        workflowContainer.setPendingActions(abstractEstimate.getState().getNextAction());
        if (abstractEstimate.getState() != null && abstractEstimate.getState().getNextAction() != null) {
            model.addAttribute("pendingActions", abstractEstimate.getState().getNextAction());
            wfmatrix = abstractEstimateWorkflowService.getWfMatrix(abstractEstimate.getStateType(), null,
                    abstractEstimate.getEstimateValue(), workflowContainer.getAdditionalRule(),
                    abstractEstimate.getCurrentState().getValue(),
                    abstractEstimate.getCurrentState().getNextAction());
            if (wfmatrix != null && wfmatrix.getNextStatus() != null)
                model.addAttribute("nextStatus", wfmatrix.getNextStatus().toUpperCase());
        }
        prepareWorkflow(model, abstractEstimate, workflowContainer);
        if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.NEW.toString())) {
            List<String> validActions = Collections.emptyList();

            validActions = customizedWorkFlowService.getNextValidActions(abstractEstimate.getStateType(), workflowContainer
                    .getWorkFlowDepartment(), workflowContainer.getAmountRule(), workflowContainer.getAdditionalRule(),
                    WorksConstants.NEW, workflowContainer.getPendingActions(), abstractEstimate.getCreatedDate());
            model.addAttribute("validActionList", validActions);
        }

        final List<AppConfigValues> values = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_SHOW_SERVICE_FIELDS);
        final AppConfigValues value = values.get(0);
        if (value.getValue().equalsIgnoreCase("Yes"))
            model.addAttribute("isServiceVATRequired", true);
        else
            model.addAttribute("isServiceVATRequired", false);
        final List<AppConfigValues> showDeductions = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_SHOW_DEDUCTION_GRID);
        final AppConfigValues showDeduction = showDeductions.get(0);
        if (showDeduction.getValue().equalsIgnoreCase("Yes"))
            model.addAttribute("isEstimateDeductionGrid", true);
        else
            model.addAttribute("isEstimateDeductionGrid", false);

        estimateService.loadLocationAppConfigValue(model);

        if (!worksApplicationProperties.lineEstimateRequired()) {
            final List<AppConfigValues> nominationLimit = appConfigValuesService.getConfigValuesByModuleAndKey(
                    WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_NOMINATION_AMOUNT);
            final AppConfigValues configValues = nominationLimit.get(0);
            if (!configValues.getValue().isEmpty())
                model.addAttribute("nominationLimit", configValues.getValue());
            final List<AppConfigValues> nominationName = appConfigValuesService.getConfigValuesByModuleAndKey(
                    WorksConstants.WORKS_MODULE_NAME, WorksConstants.NOMINATION_NAME);
            model.addAttribute("nominationName", !nominationName.isEmpty() ? nominationName.get(0).getValue() : "");
        }

        model.addAttribute("workflowHistory",
                worksUtils.getHistory(abstractEstimate.getState(), abstractEstimate.getStateHistory()));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        model.addAttribute("approvalDesignation", request.getParameter("approvalDesignation"));
        model.addAttribute("approvalPosition", request.getParameter("approvalPosition"));
        model.addAttribute("exceptionaluoms", worksUtils.getExceptionalUOMS());
        getEstimateDocuments(abstractEstimate);
        model.addAttribute("abstractEstimate", abstractEstimate);
        model.addAttribute("documentDetails", abstractEstimate.getDocumentDetails());
        model.addAttribute("measurementsPresent", measurementSheetService.existsByEstimate(abstractEstimate.getId()));
        model.addAttribute("amountRule", abstractEstimate.getEstimateValue());
        model.addAttribute("lineEstimateRequired", worksApplicationProperties.lineEstimateRequired());
        model.addAttribute("budgetControlType",budgetControlTypeService.getConfigValue());

        if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.NEW.toString())
                || abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.REJECTED.toString())) {
            model.addAttribute(WorksConstants.MODE, WorksConstants.EDIT);
            return abstractEstimate.isSpillOverFlag() ? "newAbstractEstimate-spilloverform" : "newAbstractEstimate-form";
        } else {
            model.addAttribute(WorksConstants.MODE, "workflowView");
            model.addAttribute("amountRule", abstractEstimate.getEstimateValue());
            return "abstractestimate-view";
        }
    }

    private void getEstimateDocuments(final AbstractEstimate abstractEstimate) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(abstractEstimate.getId(),
                WorksConstants.ABSTRACTESTIMATE);
        abstractEstimate.setDocumentDetails(documentDetailsList);
    }
}
