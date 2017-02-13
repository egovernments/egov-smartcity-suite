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

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.infra.utils.autonumber.AutonumberServiceBeanResolver;
import org.egov.infra.workflow.matrix.service.CustomizedWorkFlowService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.abstractestimate.service.MeasurementSheetService;
import org.egov.works.autonumber.LetterOfAcceptanceNumberGenerator;
import org.egov.works.letterofacceptance.entity.SearchRequestContractor;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.masters.service.ContractorGradeService;
import org.egov.works.masters.service.ContractorService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/letterofacceptance")
public class CreateLetterOfAcceptanceController extends GenericWorkFlowController {

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private AutonumberServiceBeanResolver beanResolver;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    @Qualifier("contractorGradeService")
    private ContractorGradeService contractorGradeService;

    @Autowired
    @Qualifier("contractorService")
    private ContractorService contractorService;

    @Autowired
    private EstimateService estimateService;

    @Autowired
    protected CustomizedWorkFlowService customizedWorkFlowService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private MeasurementSheetService measurementSheetService;

    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/newform", method = RequestMethod.GET)
    public String showNewForm(@ModelAttribute("workOrder") WorkOrder workOrder, final Model model,
            final HttpServletRequest request) {
        if (request.getParameter(WorksConstants.MODE) != null)
            model.addAttribute(WorksConstants.MODE, request.getParameter(WorksConstants.MODE));
        final String estimateNumber = request.getParameter("estimateNumber");
        final AbstractEstimate abstractEstimate = estimateService
                .getAbstractEstimateByEstimateNumberAndStatus(estimateNumber);
        workOrder = letterOfAcceptanceService.getWorkOrderByEstimateNumber(estimateNumber);
        if (workOrder == null)
            workOrder = new WorkOrder();

        loadViewData(model, abstractEstimate, workOrder, request);

        model.addAttribute("documentDetails", workOrder.getDocumentDetails());
        model.addAttribute("abstractEstimate", abstractEstimate);
        if (!(abstractEstimate.isSpillOverFlag() && abstractEstimate.isWorkOrderCreated()))
            workOrder.setWorkOrderDate(new Date());

        model.addAttribute("workOrder", workOrder);
        model.addAttribute("measurementsPresent", measurementSheetService.existsByEstimate(abstractEstimate.getId()));
        model.addAttribute("workflowHistory", worksUtils.getHistory(workOrder.getState(), workOrder.getStateHistory()));
        model.addAttribute("defaultDepartmentId", worksUtils.getDefaultDepartmentId());
        return "createLetterOfAcceptance-form";
    }

    private void setDropDownValues(final Model model, final AbstractEstimate abstractEstimate) {
        model.addAttribute("engineerInchargeList",
                letterOfAcceptanceService.getEngineerInchargeList(abstractEstimate.getExecutingDepartment().getId(),
                        letterOfAcceptanceService.getEngineerInchargeDesignationIds()));
    }

    @RequestMapping(value = "/loa-save", method = RequestMethod.POST)
    public String create(@ModelAttribute("workOrder") final WorkOrder workOrder, final Model model,
            final BindingResult resultBinder, final HttpServletRequest request,
            @RequestParam("file") final MultipartFile[] files, @RequestParam String workFlowAction,
            @RequestParam final String mode) throws IOException {
        final AbstractEstimate abstractEstimate = estimateService
                .getAbstractEstimateByEstimateNumber(workOrder.getEstimateNumber());

        if (mode == null || mode != null && !mode.equalsIgnoreCase("edit")) {
            final WorkOrder existingWorkOrder = letterOfAcceptanceService
                    .getWorkOrderByEstimateNumber(workOrder.getEstimateNumber());

            if (existingWorkOrder != null)
                resultBinder.reject("error.loa.exists.for.estimate",
                        new String[] { existingWorkOrder.getWorkOrderNumber(), workOrder.getEstimateNumber() },
                        "error.loa.exists.for.estimate");
        }
        validateInput(workOrder, resultBinder);
        validateWorkOrderFileDate(workOrder, resultBinder, abstractEstimate);

        if (abstractEstimate != null && abstractEstimate.isSpillOverFlag()
                && abstractEstimate.isWorkOrderCreated())
            validateSpillOverInput(workOrder, resultBinder);

        if (resultBinder.hasErrors()) {
            loadViewData(model, abstractEstimate, workOrder, request);
            model.addAttribute("abstractEstimate", abstractEstimate);
            model.addAttribute("contractorSearch", request.getParameter("contractorSearch"));
            model.addAttribute("contractorCode", request.getParameter("contractorCode"));
            model.addAttribute("engineerIncharge", request.getParameter("engineerIncharge"));
            model.addAttribute("approvalDesignation", request.getParameter("approvalDesignation"));
            model.addAttribute("approvalPosition", request.getParameter("approvalPosition"));
            return "createLetterOfAcceptance-form";
        } else {
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

            workOrder.setContractor(contractorService.getContractorById(workOrder.getContractor().getId()));
            final WorkOrderEstimate workOrderEstimate = letterOfAcceptanceService.createWorkOrderEstimate(workOrder);

            if (abstractEstimate != null
                    && abstractEstimate.isSpillOverFlag()
                    && !abstractEstimate.isWorkOrderCreated()
                    || !abstractEstimate.isSpillOverFlag()) {
                final LetterOfAcceptanceNumberGenerator l = beanResolver
                        .getAutoNumberServiceFor(LetterOfAcceptanceNumberGenerator.class);
                final String workOrderNumber = l.getNextNumber(workOrderEstimate);
                workOrder.setWorkOrderNumber(workOrderNumber);
            }
            final WorkOrder savedWorkOrder = letterOfAcceptanceService.create(workOrder, files, approvalPosition,
                    approvalComment, additionalRule, workFlowAction, abstractEstimate);

            String pathVars = "";

            if (abstractEstimate != null
                    && abstractEstimate.isSpillOverFlag()
                    && abstractEstimate.isWorkOrderCreated())
                pathVars = savedWorkOrder.getId().toString();
            else
                pathVars = worksUtils.getPathVars(savedWorkOrder.getEgwStatus(), savedWorkOrder.getState(),
                        savedWorkOrder.getId(), approvalPosition);

            return "redirect:/letterofacceptance/letterofacceptance-success?pathVars=" + pathVars;
        }
    }

    private void loadViewData(final Model model, final AbstractEstimate abstractEstimate, WorkOrder workOrder,
            final HttpServletRequest request) {
        setDropDownValues(model, abstractEstimate);
        model.addAttribute("stateType", workOrder.getClass().getSimpleName());
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        workflowContainer.setAmountRule(new BigDecimal(workOrder.getWorkOrderAmount()));
        workflowContainer.setAdditionalRule(
                (String) cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
        prepareWorkflow(model, workOrder, workflowContainer);
        List<String> validActions = Collections.emptyList();
        if (workOrder.getId() != null) {
            validActions = customizedWorkFlowService.getNextValidActions(workOrder.getStateType(),
                    workflowContainer.getWorkFlowDepartment(), workflowContainer.getAmountRule(),
                    workflowContainer.getAdditionalRule(), workOrder.getState().getValue(),
                    workflowContainer.getPendingActions(), workOrder.getCreatedDate());
            model.addAttribute("contractorSearch", workOrder.getContractor().getName());
            model.addAttribute("contractorCode", workOrder.getContractor().getCode());
        } else
            validActions = customizedWorkFlowService.getNextValidActions(workOrder.getStateType(),
                    workflowContainer.getWorkFlowDepartment(), workflowContainer.getAmountRule(),
                    workflowContainer.getAdditionalRule(), WorksConstants.NEW, workflowContainer.getPendingActions(),
                    workOrder.getCreatedDate());
        workOrder = letterOfAcceptanceService.getWorkOrderDocuments(workOrder);
        if (workOrder.getState() != null && workOrder.getState().getNextAction() != null) {
            model.addAttribute("nextAction", workOrder.getState().getNextAction());
            model.addAttribute("pendingActions", workOrder.getState().getNextAction());
        }

        final List<AppConfigValues> values = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_PERCENTAGE_ON_ESTIMATERATE_OR_WORKVALUE);
        final AppConfigValues value = values.get(0);
        if (StringUtils.isNotBlank(workOrder.getPercentageSign()) && workOrder.getPercentageSign().equals("-"))
            workOrder.setTenderFinalizedPercentage(workOrder.getTenderFinalizedPercentage() * -1);
        model.addAttribute("percentage_on_estimaterate_or_workvalue", value.getValue());
        model.addAttribute("documentDetails", workOrder.getDocumentDetails());
        model.addAttribute("validActionList", validActions);
        model.addAttribute("loggedInUser", securityUtils.getCurrentUser().getName());
        model.addAttribute("amountRule", workOrder.getWorkOrderAmount());
        final List<AppConfigValues> nominationName = estimateService.getNominationName();
        model.addAttribute("nominationName", !nominationName.isEmpty() ? nominationName.get(0).getValue() : "");
        model.addAttribute(WorksConstants.ADDITIONAL_RULE,
                cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
    }

    @RequestMapping(value = "/letterofacceptance-success", method = RequestMethod.GET)
    public ModelAndView successView(@ModelAttribute WorkOrder workOrder, final HttpServletRequest request,
            final Model model, final ModelMap modelMap) {

        final String[] keyNameArray = request.getParameter("pathVars").split(",");
        Long id = 0L;
        String approverName = "";
        String currentUserDesgn = "";
        String nextDesign = "";
        if (keyNameArray.length != 0 && keyNameArray.length > 0)
            if (keyNameArray.length == 1)
                id = Long.parseLong(keyNameArray[0]);
            else if (keyNameArray.length == 3) {
                id = Long.parseLong(keyNameArray[0]);
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
            } else {
                id = Long.parseLong(keyNameArray[0]);
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
                nextDesign = keyNameArray[3];
            }

        if (id != null)
            workOrder = letterOfAcceptanceService.getWorkOrderById(id);
        model.addAttribute("approverName", approverName);
        model.addAttribute("currentUserDesgn", currentUserDesgn);
        model.addAttribute("nextDesign", nextDesign);

        final String message = getMessageByStatus(workOrder, approverName, nextDesign);

        model.addAttribute("message", message);

        return new ModelAndView("letterOfAcceptance-success", "workOrder", workOrder);
    }

    private String getMessageByStatus(final WorkOrder workOrder, final String approverName, final String nextDesign) {
        String message = "";

        if (workOrder.getEgwStatus().getCode().equals(WorksConstants.CREATED_STATUS))
            message = messageSource.getMessage("msg.letterofacceptance.saved",
                    new String[] { approverName, nextDesign, workOrder.getWorkOrderNumber() }, null);
        if (workOrder.getEgwStatus().getCode().equals(WorksConstants.RESUBMITTED_STATUS))
            message = messageSource.getMessage("msg.letterofacceptance.resubmitted",
                    new String[] { approverName, nextDesign, workOrder.getWorkOrderNumber() }, null);
        else if (workOrder.getEgwStatus().getCode().equals(WorksConstants.REJECTED))
            message = messageSource.getMessage("msg.letterofacceptance.rejected",
                    new String[] { workOrder.getWorkOrderNumber(), approverName, nextDesign }, null);
        else if (workOrder.getEgwStatus().getCode().equals(WorksConstants.APPROVED))
            message = messageSource.getMessage("msg.letterofacceptance.approved",
                    new String[] { workOrder.getWorkOrderNumber() }, null);
        else if (workOrder.getEgwStatus().getCode().equals(WorksConstants.CANCELLED_STATUS))
            message = messageSource.getMessage("msg.letterofacceptance.cancelled",
                    new String[] { workOrder.getWorkOrderNumber() }, null);
        else if (WorksConstants.CHECKED_STATUS.equalsIgnoreCase(workOrder.getEgwStatus().getCode()))
            message = messageSource.getMessage("msg.loa.check.success",
                    new String[] { workOrder.getWorkOrderNumber(), approverName, nextDesign, }, null);

        return message;
    }

    @RequestMapping(value = "/loa-success", method = RequestMethod.GET)
    public String showLetterOfAcceptanceSuccessPage(@RequestParam("loaNumber") final String loaNumber,
            final Model model, @RequestParam(value = "isModify", required = false) final boolean isModify) {
        final WorkOrder workOrder = letterOfAcceptanceService.getWorkOrderByWorkOrderNumber(loaNumber);
        model.addAttribute("workOrder", workOrder);
        if (isModify)
            model.addAttribute("mode", "modify");
        return "letterofacceptance-success";
    }

    private void validateInput(final WorkOrder workOrder, final BindingResult resultBinder) {
        if (StringUtils.isBlank(workOrder.getFileNumber()))
            resultBinder.rejectValue("fileNumber", "error.fileno.required");
        if (workOrder.getFileDate() == null)
            resultBinder.rejectValue("fileDate", "error.filedate.required");
        if (workOrder.getWorkOrderAmount() <= 0)
            resultBinder.rejectValue("workOrderAmount", "error.workorderamount.required");
        if (workOrder.getContractor() == null || workOrder.getContractor().getId() == null)
            resultBinder.rejectValue("contractor", "error.contractor.required");
        if (workOrder.getContractPeriod() == null || workOrder.getContractPeriod() <= 0)
            resultBinder.rejectValue("contractPeriod", "error.contractorperiod.required");
        if (workOrder.getDefectLiabilityPeriod() <= 0)
            resultBinder.rejectValue("defectLiabilityPeriod", "error.defectliabilityperiod.required");
        if (workOrder.getEngineerIncharge() == null || workOrder.getEngineerIncharge().getId() == null)
            resultBinder.rejectValue("engineerIncharge", "error.engineerincharge.required");

    }

    private void validateSpillOverInput(final WorkOrder workOrder, final BindingResult resultBinder) {
        if (StringUtils.isBlank(workOrder.getWorkOrderNumber()))
            resultBinder.rejectValue("workOrderNumber", "error.workordernumber.required");
        final WorkOrder wo = letterOfAcceptanceService.getWorkOrderByWorkOrderNumber(workOrder.getWorkOrderNumber());
        if (wo != null)
            resultBinder.rejectValue("workOrderNumber", "error.workordernumber.unique");
    }

    private void validateWorkOrderFileDate(final WorkOrder workOrder, final BindingResult resultBinder,
            final AbstractEstimate abstractEstimate) {
        if (workOrder.getFileDate().before(abstractEstimate.getApprovedDate())) {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            resultBinder.rejectValue("fileDate", "error.loa.filedate",
                    new String[] { formatter.format(abstractEstimate.getApprovedDate()) }, null);
        }
        if (workOrder.getWorkOrderDate() == null)
            resultBinder.rejectValue("workOrderDate", "error.loa.workorderdate.requires");
        if (workOrder.getWorkOrderDate() != null && workOrder.getWorkOrderDate().before(workOrder.getFileDate()))
            resultBinder.rejectValue("fileDate", "error.loa.workorderdate");
    }

    @RequestMapping(value = "/contractorsearchform", method = RequestMethod.GET)
    public String showSearchContractorForm(@ModelAttribute final SearchRequestContractor searchRequestContractor,
            final Model model) throws ApplicationException {
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("contractorClasses", contractorGradeService.getAllContractorGrades());
        model.addAttribute("searchRequestContractor", searchRequestContractor);
        return "contractor-search";
    }

}
