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

import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.service.BudgetControlTypeService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.service.CustomizedWorkFlowService;
import org.egov.pims.commons.Designation;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimate.EstimateStatus;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.lineestimate.entity.EstimateAppropriation;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.EstimateAppropriationService;
import org.egov.works.lineestimate.service.LineEstimateDetailService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/abstractestimate")
public class CreateAbstractEstimateController extends GenericWorkFlowController {

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private LineEstimateDetailService lineEstimateDetailService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    protected CustomizedWorkFlowService customizedWorkFlowService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private ScheduleOfRateService scheduleOfRateService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private CityService cityService;

    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private BudgetControlTypeService budgetControlTypeService;

    @Autowired
    private EstimateAppropriationService estimateAppropriationService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showAbstractEstimateForm(@ModelAttribute("abstractEstimate") final AbstractEstimate abstractEstimate,
            @RequestParam(required = false) final Long lineEstimateDetailId, final Model model) {
        LineEstimateDetails lineEstimateDetails = null;
        if (worksApplicationProperties.lineEstimateRequired() && lineEstimateDetailId == null)
            return "redirect:/lineestimate/searchlineestimateforabstractestimate-form";
        if (worksApplicationProperties.lineEstimateRequired())
            lineEstimateDetails = lineEstimateDetailService.getById(lineEstimateDetailId);
        model.addAttribute("defaultDepartmentId", worksUtils.getDefaultDepartmentId());
        populateDataForAbstractEstimate(lineEstimateDetails, model, abstractEstimate);
        abstractEstimate.setEstimateDate(new Date());
        loadViewData(model, abstractEstimate, lineEstimateDetails);

        return "newAbstractEstimate-form";
    }

    @RequestMapping(value = "/createspillover", method = RequestMethod.GET)
    public String showSpillOverAbstractEstimateForm(@ModelAttribute("abstractEstimate") final AbstractEstimate abstractEstimate,
            @RequestParam(required = false) final Long lineEstimateDetailId, final Model model) {
        LineEstimateDetails lineEstimateDetails = null;
        if (worksApplicationProperties.lineEstimateRequired() && lineEstimateDetailId == null)
            return "redirect:/lineestimate/searchlineestimateforabstractestimate-form";
        if (worksApplicationProperties.lineEstimateRequired())
            lineEstimateDetails = lineEstimateDetailService.getById(lineEstimateDetailId);
        abstractEstimate.setSpillOverFlag(true);
        populateDataForAbstractEstimate(lineEstimateDetails, model, abstractEstimate);
        abstractEstimate.setEstimateDate(new Date());
        loadViewData(model, abstractEstimate, lineEstimateDetails);
        model.addAttribute("cuttOffDate", worksUtils.getCutOffDate());
        model.addAttribute("currFinDate", worksUtils.getFinancialYearByDate(new Date()).getStartingDate());

        return "newAbstractEstimate-spilloverform";
    }

    private void loadViewData(final Model model, final AbstractEstimate abstractEstimate,
            final LineEstimateDetails lineEstimateDetails) {
        estimateService.setDropDownValues(model);
        final List<Department> departments = worksUtils.getUserDepartments(securityUtils.getCurrentUser());
        if (departments != null && !departments.isEmpty())
            abstractEstimate.setExecutingDepartment(departments.get(0));

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

        final WorkflowContainer workflowContainer = new WorkflowContainer();
        workflowContainer.setAmountRule(abstractEstimate.getEstimateValue() != null
                ? abstractEstimate.getEstimateValue() : BigDecimal.ZERO);
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
        model.addAttribute("amountRule",
                abstractEstimate.getEstimateValue() != null ? abstractEstimate.getEstimateValue() : BigDecimal.ZERO);
        prepareWorkflow(model, abstractEstimate, workflowContainer);
        List<String> validActions = Collections.emptyList();
        validActions = customizedWorkFlowService.getNextValidActions(abstractEstimate.getStateType(),
                workflowContainer.getWorkFlowDepartment(), workflowContainer.getAmountRule(),
                workflowContainer.getAdditionalRule(), WorksConstants.NEW, workflowContainer.getPendingActions(),
                abstractEstimate.getCreatedDate());
        if (abstractEstimate.getState() != null && abstractEstimate.getState().getNextAction() != null) {
            model.addAttribute("nextAction", abstractEstimate.getState().getNextAction());
            model.addAttribute("pendingActions", abstractEstimate.getState().getNextAction());
        }
        model.addAttribute("validActionList", validActions);
        model.addAttribute("mode", null);
        model.addAttribute("stateType", abstractEstimate.getClass().getSimpleName());
        model.addAttribute("documentDetails", abstractEstimate.getDocumentDetails());
        model.addAttribute("lineEstimateRequired", worksApplicationProperties.lineEstimateRequired());

        final List<AppConfigValues> nominationLimit = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_NOMINATION_AMOUNT);
        final AppConfigValues value = nominationLimit.get(0);
        if (!value.getValue().isEmpty())
            model.addAttribute("nominationLimit", value.getValue());
        final List<AppConfigValues> nominationName = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.NOMINATION_NAME);
        model.addAttribute("nominationName", !nominationName.isEmpty() ? nominationName.get(0).getValue() : "");
        model.addAttribute("budgetControlType",budgetControlTypeService.getConfigValue());
    }

    @RequestMapping(value = { "/create", "/createspillover" }, method = RequestMethod.POST)
    public String saveAbstractEstimate(@ModelAttribute final AbstractEstimate abstractEstimate,
            final RedirectAttributes redirectAttributes, final Model model, final BindingResult bindErrors,
            @RequestParam("file") final MultipartFile[] files, final HttpServletRequest request,
            @RequestParam String workFlowAction) throws IOException {
        AbstractEstimate savedAbstractEstimate;
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
        estimateService.validateMultiYearEstimates(abstractEstimate, bindErrors);
        estimateService.validateMandatory(abstractEstimate, bindErrors);
        estimateService.validateAssetDetails(abstractEstimate, bindErrors);
        estimateService.validateActivities(abstractEstimate, bindErrors);
        estimateService.validateOverheads(abstractEstimate, bindErrors);
        estimateService.validateBudgetHead(abstractEstimate, bindErrors);
        if (!abstractEstimate.isSpillOverFlag())
            estimateService.validateWorkflowActionButton(abstractEstimate, bindErrors, additionalRule, workFlowAction);

        // Added server side validation for selected abstract estimate created flag
        if (abstractEstimate.getLineEstimateDetails() != null
                && abstractEstimate.getLineEstimateDetails().getLineEstimate().isAbstractEstimateCreated()) {
            estimateService.validateTechnicalSanctionDetail(abstractEstimate, bindErrors);
            if (!estimateService.checkForDuplicateAccountCodesInEstimateDeductions(abstractEstimate))
                bindErrors.reject("error.abstractestimate.duplicate.accountcodes",
                        "error.abstractestimate.duplicate.accountcodes");

            estimateService.setTechnicalSanctionDetails(abstractEstimate);

        }

        if (!WorksConstants.SAVE_ACTION.equals(workFlowAction)) {
            if (abstractEstimate.getSorActivities().isEmpty() && abstractEstimate.getNonSorActivities().isEmpty())
                bindErrors.reject("error.sor.nonsor.required", "error.sor.nonsor.required");
            estimateService.validateLocationDetails(abstractEstimate, bindErrors);
        }

        if (!BudgetControlType.BudgetCheckOption.NONE.toString()
                .equalsIgnoreCase(budgetControlTypeService.getConfigValue())
                && !worksApplicationProperties.lineEstimateRequired()
                && WorksConstants.CREATE_AND_APPROVE.equals(workFlowAction))
            estimateService.validateBudgetAmount(abstractEstimate,
                    bindErrors);
        if (bindErrors.hasErrors()) {
            for (final Activity activity : abstractEstimate.getSorActivities())
                activity.setSchedule(scheduleOfRateService.getScheduleOfRateById(activity.getSchedule().getId()));
            estimateService.loadModelValues(abstractEstimate.getLineEstimateDetails(), model, abstractEstimate);
            if (abstractEstimate.getLineEstimateDetails() != null)
                abstractEstimate.setProjectCode(abstractEstimate.getLineEstimateDetails().getProjectCode());
            loadViewData(model, abstractEstimate, abstractEstimate.getLineEstimateDetails());
            model.addAttribute("approvalDesignation", request.getParameter("approvalDesignation"));
            model.addAttribute("approvalPosition", request.getParameter("approvalPosition"));
            model.addAttribute("designation", request.getParameter("designation"));
            model.addAttribute("technicalSanctionBy",
                    request.getParameter("estimateTechnicalSanctions[0].technicalSanctionBy"));
            model.addAttribute("approvedByValue", request.getParameter("approvedBy"));
            model.addAttribute(WorksConstants.ADDITIONAL_RULE,
                    cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
            return abstractEstimate.isSpillOverFlag() ? "newAbstractEstimate-spilloverform" : "newAbstractEstimate-form";
        } else {
            if (abstractEstimate.getState() == null)
                if (WorksConstants.FORWARD_ACTION.equals(workFlowAction))
                    abstractEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(
                            WorksConstants.ABSTRACTESTIMATE, EstimateStatus.CREATED.toString()));
                else if (WorksConstants.CREATE_AND_APPROVE.equalsIgnoreCase(workFlowAction)) {
                    abstractEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(
                            WorksConstants.ABSTRACTESTIMATE, EstimateStatus.APPROVED.toString()));
                    estimateService.saveTechnicalSanctionDetails(abstractEstimate);
                    estimateService.saveAdminSanctionDetails(abstractEstimate);
                } else
                    abstractEstimate.setEgwStatus(egwStatusHibernateDAO
                            .getStatusByModuleAndCode(WorksConstants.ABSTRACTESTIMATE, EstimateStatus.NEW.toString()));

            try {
                savedAbstractEstimate = estimateService.createAbstractEstimate(abstractEstimate,
                        files, approvalPosition, approvalComment, additionalRule, workFlowAction);
            } catch (final ValidationException e) {
                final String errorMessage = messageSource.getMessage("error.budgetappropriation.insufficient.amount",
                        new String[] {}, null);
                model.addAttribute("message", errorMessage);
                return abstractEstimate.isSpillOverFlag() ? "newAbstractEstimate-spilloverform" : "newAbstractEstimate-form";
            }

            if (EstimateStatus.NEW.toString().equals(savedAbstractEstimate.getEgwStatus().getCode()))
                return "redirect:/abstractestimate/update/" + savedAbstractEstimate.getId() + "?mode=save";

            if (abstractEstimate.getLineEstimateDetails() != null
                    && abstractEstimate.getLineEstimateDetails().getLineEstimate().isAbstractEstimateCreated())
                return "redirect:/abstractestimate/abstractestimate-success?estimate=" + savedAbstractEstimate.getId()
                        + "&approvalPosition=";
            else
                return "redirect:/abstractestimate/abstractestimate-success?estimate=" + savedAbstractEstimate.getId()
                        + "&approvalPosition=" + approvalPosition;
        }

    }

    @RequestMapping(value = "/abstractestimate-success", method = RequestMethod.GET)
    public ModelAndView successView(@ModelAttribute AbstractEstimate abstractEstimate,
            @RequestParam("estimate") Long id, @RequestParam("approvalPosition") final Long approvalPosition,
            final HttpServletRequest request, final Model model) {

        if (id != null)
            abstractEstimate = estimateService.getAbstractEstimateById(id);

        final String pathVars = worksUtils.getPathVars(abstractEstimate.getEgwStatus(), abstractEstimate.getState(),
                abstractEstimate.getId(), approvalPosition);

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

        final String message = getMessageByStatus(abstractEstimate, approverName, nextDesign);

        model.addAttribute("message", message);

        return new ModelAndView("abstractEstimate-success", "abstractEstimate", abstractEstimate);
    }

    private String getMessageByStatus(final AbstractEstimate abstractEstimate, final String approverName,
            final String nextDesign) {
        String message = "";
        if (EstimateStatus.NEW.toString().equals(abstractEstimate.getEgwStatus().getCode()))
            message = messageSource.getMessage("msg.estimate.saved",
                    new String[] { abstractEstimate.getEstimateNumber() }, null);
        else if (EstimateStatus.CREATED.toString().equals(abstractEstimate.getEgwStatus().getCode())
                && !WorksConstants.WF_STATE_REJECTED.equals(abstractEstimate.getState().getValue()))
            message = messageSource.getMessage("msg.estimate.created",
                    new String[] { approverName, nextDesign, abstractEstimate.getEstimateNumber() }, null);
        else if (EstimateStatus.RESUBMITTED.toString().equals(abstractEstimate.getEgwStatus().getCode())
                && !WorksConstants.WF_STATE_REJECTED.equals(abstractEstimate.getState().getValue()))
            message = messageSource.getMessage("msg.estimate.resubmitted",
                    new String[] { approverName, nextDesign, abstractEstimate.getEstimateNumber() }, null);
        else if (EstimateStatus.APPROVED.toString().equals(abstractEstimate.getEgwStatus().getCode())
                && !WorksConstants.WF_STATE_REJECTED.equals(abstractEstimate.getState().getValue())) {
            if (!worksApplicationProperties.lineEstimateRequired()) {
                final EstimateAppropriation lea = estimateAppropriationService
                        .findLatestByAbstractEstimate(abstractEstimate);
                message = messageSource.getMessage("msg.estimate.techsanctioned.budget.appropriation",
                        new String[] { abstractEstimate.getEstimateNumber(),
                                abstractEstimate.getEstimateTechnicalSanctions()
                                        .get(abstractEstimate.getEstimateTechnicalSanctions().size() - 1)
                                        .getTechnicalSanctionNumber(),
                                lea.getBudgetUsage().getAppropriationnumber() },
                        null);
            } else
                message = messageSource.getMessage("msg.estimate.techsanctioned",
                        new String[] { abstractEstimate.getEstimateNumber(),
                                abstractEstimate.getEstimateTechnicalSanctions()
                                        .get(abstractEstimate.getEstimateTechnicalSanctions().size() - 1)
                                        .getTechnicalSanctionNumber() },
                        null);
        } else if (abstractEstimate.getState() != null
                && WorksConstants.WF_STATE_REJECTED.equals(abstractEstimate.getState().getValue()))
            message = messageSource.getMessage("msg.estimate.rejected",
                    new String[] { abstractEstimate.getEstimateNumber(), approverName, nextDesign }, null);
        else if (EstimateStatus.CANCELLED.toString().equals(abstractEstimate.getEgwStatus().getCode()))
            message = messageSource.getMessage("msg.estimate.cancelled",
                    new String[] { abstractEstimate.getEstimateNumber() }, null);

        else if (EstimateStatus.CHECKED.toString().equals(abstractEstimate.getEgwStatus().getCode())
                && !WorksConstants.WF_STATE_REJECTED.equals(abstractEstimate.getState().getValue()))
            message = messageSource.getMessage("msg.abstractestimate.check.success",
                    new String[] { abstractEstimate.getEstimateNumber(), approverName, nextDesign }, null);
        return message;

    }

    /**
     * Method called to populate data from lineestimatedetails to create abstract estimate
     *
     * @param lineEstimateDetails
     * @param model
     * @param abstractEstimate
     */
    public void populateDataForAbstractEstimate(final LineEstimateDetails lineEstimateDetails, final Model model,
            final AbstractEstimate abstractEstimate) {
        if (lineEstimateDetails != null) {
            final LineEstimate lineEstimate = lineEstimateDetails.getLineEstimate();
            abstractEstimate.setLineEstimateDetails(lineEstimateDetails);
            abstractEstimate.setExecutingDepartment(lineEstimate.getExecutingDepartment());
            abstractEstimate.setWorkCategory(lineEstimate.getWorkCategory());
            abstractEstimate.setWard(lineEstimateDetails.getLineEstimate().getWard());
            abstractEstimate.setNatureOfWork(lineEstimate.getNatureOfWork());
            abstractEstimate.setParentCategory(lineEstimate.getTypeOfWork());
            abstractEstimate.setCategory(lineEstimate.getSubTypeOfWork());
            abstractEstimate.setProjectCode(lineEstimateDetails.getProjectCode());
            abstractEstimate.setBeneficiary(lineEstimate.getBeneficiary());
            abstractEstimate.setLocality(lineEstimate.getLocation());
            abstractEstimate.setModeOfAllotment(lineEstimate.getModeOfAllotment());
        }
        abstractEstimate.addMultiYearEstimate(estimateService.populateMultiYearEstimate(abstractEstimate));
        abstractEstimate.addFinancialDetails(estimateService.populateEstimateFinancialDetails(abstractEstimate));
        estimateService.loadModelValues(lineEstimateDetails, model, abstractEstimate);
    }

}
