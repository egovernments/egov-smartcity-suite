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
package org.egov.works.web.controller.lineestimate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CChartOfAccountDetail;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.EgwTypeOfWorkHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.dao.budget.BudgetDetailsDAO;
import org.egov.egf.budget.model.BudgetControlType;
import org.egov.egf.budget.service.BudgetControlTypeService;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.services.masters.SchemeService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.entity.enums.Beneficiary;
import org.egov.works.lineestimate.entity.enums.LineEstimateStatus;
import org.egov.works.lineestimate.entity.enums.WorkCategory;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.master.service.LineEstimateUOMService;
import org.egov.works.master.service.ModeOfAllotmentService;
import org.egov.works.master.service.NatureOfWorkService;
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
@RequestMapping(value = "/lineestimate")
public class UpdateLineEstimateController extends GenericWorkFlowController {
    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private FundHibernateDAO fundHibernateDAO;

    @Autowired
    private FunctionHibernateDAO functionHibernateDAO;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private NatureOfWorkService natureOfWorkService;

    @Autowired
    private EgwTypeOfWorkHibernateDAO egwTypeOfWorkHibernateDAO;

    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    private BudgetDetailsDAO budgetDetailsDAO;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private ModeOfAllotmentService modeOfAllotmentService;

    @Autowired
    private LineEstimateUOMService lineEstimateUOMService;

    @Autowired
    private BudgetControlTypeService budgetControlTypeService;

    @ModelAttribute
    public LineEstimate getLineEstimate(@PathVariable final String lineEstimateId) {
        final LineEstimate lineEstimate = lineEstimateService.getLineEstimateById(Long.parseLong(lineEstimateId));
        return lineEstimate;
    }

    @RequestMapping(value = "/update/{lineEstimateId}", method = RequestMethod.GET)
    public String updateLineEstimate(final Model model, @PathVariable final String lineEstimateId,
            final HttpServletRequest request)
            throws ApplicationException {
        final LineEstimate lineEstimate = getLineEstimate(lineEstimateId);
        lineEstimate.setTempLineEstimateDetails(lineEstimate.getLineEstimateDetails());
        if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.REJECTED.toString()))
            setDropDownValues(model);
        return loadViewData(model, request, lineEstimate);
    }

    @RequestMapping(value = "/view/{lineEstimateId}", method = RequestMethod.GET)
    public String viewLineEstimate(final Model model, @PathVariable final String lineEstimateId,
            final HttpServletRequest request)
            throws ApplicationException {
        final LineEstimate lineEstimate = getLineEstimate(lineEstimateId);

        final String responsePage = loadViewData(model, request, lineEstimate);
        model.addAttribute("adminsanctionbydesignation", lineEstimate.getAdminSanctionBy());
        model.addAttribute("technicalsanctionbydesignation",
                worksUtils.getUserDesignation(lineEstimate.getTechnicalSanctionBy()));
        model.addAttribute("createdbybydesignation", worksUtils.getUserDesignation(lineEstimate.getCreatedBy()));
        model.addAttribute("mode", "readOnly");
        return responsePage;
    }

    @RequestMapping(value = "/update/{lineEstimateId}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("lineEstimate") final LineEstimate lineEstimate, final BindingResult errors,
            final RedirectAttributes redirectAttributes, final Model model, final HttpServletRequest request,
            @RequestParam final String removedLineEstimateDetailsIds, @RequestParam("file") final MultipartFile[] files)
            throws ApplicationException, IOException {

        String mode = "";
        String workFlowAction = "";
        LineEstimate newLineEstimate = null;

        validateBudgetHead(lineEstimate, errors);

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

        // For Get Configured ApprovalPosition from workflow history
        if (approvalPosition == null || approvalPosition.equals(Long.valueOf(0)))
            approvalPosition = lineEstimateService.getApprovalPositionByMatrixDesignation(
                    lineEstimate, approvalPosition, null,
                    mode, workFlowAction);

        if ((approvalPosition == null || approvalPosition.equals(Long.valueOf(0)))
                && request.getParameter("approvalPosition") != null
                && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.ADMINISTRATIVE_SANCTIONED.toString()))
            validateTechSanctionDetails(lineEstimate, errors);

        if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.BUDGET_SANCTIONED.toString())
                && !workFlowAction.equalsIgnoreCase(WorksConstants.REJECT_ACTION.toString()))
            validateAdminSanctionDetail(lineEstimate, errors);

        if (lineEstimate.getStatus().getCode().equals(LineEstimateStatus.CHECKED.toString())
                && !workFlowAction.equalsIgnoreCase(WorksConstants.REJECT_ACTION.toString()))
            if (!BudgetControlType.BudgetCheckOption.NONE.toString()
                    .equalsIgnoreCase(budgetControlTypeService.getConfigValue()))
                validateBudgetAmount(lineEstimate, errors);
        if (errors.hasErrors()) {
            setDropDownValues(model);
            model.addAttribute("removedLineEstimateDetailsIds", removedLineEstimateDetailsIds);
            return loadViewData(model, request, lineEstimate);
        } else {
            if (null != workFlowAction)
                try {
                    final CFinancialYear financialYear = lineEstimateService
                            .getCurrentFinancialYear(lineEstimate.getLineEstimateDate());
                    newLineEstimate = lineEstimateService.updateLineEstimateDetails(lineEstimate, approvalPosition,
                            approvalComment, null, workFlowAction,
                            mode, null, removedLineEstimateDetailsIds, files, financialYear);
                } catch (final ValidationException e) {
                    final List<Long> budgetheadid = new ArrayList<Long>();
                    budgetheadid.add(lineEstimate.getBudgetHead().getId());

                    final BigDecimal budgetAvailable = budgetDetailsDAO
                            .getPlanningBudgetAvailable(
                                    lineEstimateService.getCurrentFinancialYear(new Date()).getId(),
                                    Integer.parseInt(lineEstimate
                                            .getExecutingDepartment().getId().toString()),
                                    lineEstimate.getFunction().getId(),
                                    null,
                                    lineEstimate.getScheme() == null ? null : Integer.parseInt(lineEstimate.getScheme().getId()
                                            .toString()),
                                    lineEstimate.getSubScheme() == null ? null
                                            : Integer.parseInt(lineEstimate.getSubScheme().getId()
                                                    .toString()),
                                    null, budgetheadid, Integer.parseInt(lineEstimate.getFund()
                                            .getId().toString()));
                    BigDecimal totalEstimateAmount = BigDecimal.ZERO;

                    for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails())
                        totalEstimateAmount = led.getEstimateAmount().add(totalEstimateAmount);

                    final String errorMessage = messageSource.getMessage("error.budgetappropriation.amount",
                            new String[] { budgetAvailable.toString(), totalEstimateAmount.toString() }, null);
                    model.addAttribute("message", errorMessage);
                    return "lineestimate-success";
                }
            redirectAttributes.addFlashAttribute("lineEstimate", newLineEstimate);

            final String pathVars = worksUtils.getPathVars(newLineEstimate.getStatus(), newLineEstimate.getState(),
                    newLineEstimate.getId(), approvalPosition);

            return "redirect:/lineestimate/lineestimate-success?pathVars=" + pathVars;
        }
    }

    private void validateBudgetHead(LineEstimate lineEstimate, BindingResult errors) {
        if (lineEstimate.getBudgetHead() != null) {
            Boolean check = false;
            List<CChartOfAccountDetail> accountDetails = new ArrayList<CChartOfAccountDetail>();
            accountDetails.addAll(lineEstimate.getBudgetHead().getMaxCode().getChartOfAccountDetails());
            for (CChartOfAccountDetail detail : accountDetails) {
                if (detail.getDetailTypeId() != null
                        && detail.getDetailTypeId().getName().equalsIgnoreCase(WorksConstants.PROJECTCODE))
                    check = true;
            }
            if (!check) {
                errors.reject("error.budgethead.validate", "error.budgethead.validate");
            }

        }

    }

    private void validateBudgetAmount(final LineEstimate lineEstimate, final BindingResult errors) {

        final List<Long> budgetheadid = new ArrayList<Long>();
        budgetheadid.add(lineEstimate.getBudgetHead().getId());

        try {
            final BigDecimal budgetAvailable = budgetDetailsDAO
                    .getPlanningBudgetAvailable(
                            lineEstimateService.getCurrentFinancialYear(new Date()).getId(),
                            Integer.parseInt(lineEstimate
                                    .getExecutingDepartment().getId().toString()),
                            lineEstimate.getFunction().getId(),
                            null,
                            lineEstimate.getScheme() == null ? null : Integer.parseInt(lineEstimate.getScheme().getId()
                                    .toString()),
                            lineEstimate.getSubScheme() == null ? null : Integer.parseInt(lineEstimate.getSubScheme().getId()
                                    .toString()),
                            null, budgetheadid, Integer.parseInt(lineEstimate.getFund()
                                    .getId().toString()));

            BigDecimal totalEstimateAmount = BigDecimal.ZERO;

            for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails())
                totalEstimateAmount = led.getEstimateAmount().add(totalEstimateAmount);

            if (BudgetControlType.BudgetCheckOption.MANDATORY.toString()
                    .equalsIgnoreCase(budgetControlTypeService.getConfigValue()) && budgetAvailable.compareTo(totalEstimateAmount) == -1)
                errors.reject("error.budgetappropriation.amount",
                        new String[] { budgetAvailable.toString(), totalEstimateAmount.toString() }, null);
        } catch (final ValidationException e) {
            // TODO: Used ApplicationRuntimeException for time being since there is issue in session after
            // budgetDetailsDAO.getPlanningBudgetAvailable API call
            // TODO: needs to replace with errors.reject
            for (final ValidationError error : e.getErrors())
                throw new ApplicationRuntimeException(error.getKey());
            /*
             * for (final ValidationError error : e.getErrors()) errors.reject(error.getMessage());
             */
        }
    }

    private void validateTechSanctionDetails(final LineEstimate lineEstimate, final BindingResult errors) {
        if (lineEstimate.getTechnicalSanctionDate() == null)
            errors.rejectValue("technicalSanctionDate", "error.techdate.notnull");
        if (lineEstimate.getTechnicalSanctionDate() != null
                && lineEstimate.getTechnicalSanctionDate().before(lineEstimate.getAdminSanctionDate()))
            errors.rejectValue("technicalSanctionDate", "error.technicalsanctiondate");
        if (lineEstimate.getTechnicalSanctionNumber() == null)
            errors.rejectValue("technicalSanctionNumber", "error.technumber.notnull");
        if (lineEstimate.getTechnicalSanctionNumber() != null) {
            final LineEstimate existingLineEstimate = lineEstimateService.getLineEstimateByTechnicalSanctionNumber(lineEstimate
                    .getTechnicalSanctionNumber());
            if (existingLineEstimate != null)
                errors.rejectValue("technicalSanctionNumber", "error.technumber.unique");
        }
    }

    private void validateAdminSanctionDetail(final LineEstimate lineEstimate, final BindingResult errors) {
        if (lineEstimate.getCouncilResolutionDate() != null
                && lineEstimate.getCouncilResolutionDate().before(lineEstimate.getLineEstimateDate()))
            errors.rejectValue("councilResolutionDate", "error.councilresolutiondate");
        if (StringUtils.isBlank(lineEstimate.getAdminSanctionNumber()))
            errors.rejectValue("adminSanctionNumber", "error.adminsanctionnumber.notnull");
        if (lineEstimate.getAdminSanctionNumber() != null) {
            final LineEstimate checkLineEstimate = lineEstimateService.getLineEstimateByAdminSanctionNumber(lineEstimate
                    .getAdminSanctionNumber());

            if (checkLineEstimate != null)
                errors.rejectValue("adminSanctionNumber", "error.adminsanctionnumber.unique");
        }
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("funds", fundHibernateDAO.findAllActiveFunds());
        model.addAttribute("functions", functionHibernateDAO.getAllActiveFunctions());
        model.addAttribute("schemes", schemeService.findAll());
        model.addAttribute("departments", lineEstimateService.getUserDepartments(securityUtils.getCurrentUser()));
        model.addAttribute("beneficiary", Beneficiary.values());
        model.addAttribute("modeOfAllotment", modeOfAllotmentService.findAll());
        model.addAttribute("lineEstimateUOMs", lineEstimateUOMService.findAll());
        model.addAttribute("typeOfWork", egwTypeOfWorkHibernateDAO.getTypeOfWorkForPartyTypeContractor());
        model.addAttribute("natureOfWork", natureOfWorkService.findAll());
        model.addAttribute("workCategory", WorkCategory.values());
        model.addAttribute("locations", boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                WorksConstants.LOCATION_BOUNDARYTYPE, WorksConstants.LOCATION_HIERARCHYTYPE));
    }

    private String loadViewData(final Model model, final HttpServletRequest request,
            final LineEstimate lineEstimate) {

        model.addAttribute("stateType", lineEstimate.getClass().getSimpleName());

        if (lineEstimate.getCurrentState() != null)
            model.addAttribute("currentState", lineEstimate.getCurrentState().getValue());

        prepareWorkflow(model, lineEstimate, new WorkflowContainer());
        if (lineEstimate.getState() != null && lineEstimate.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            model.addAttribute("mode", "edit");
        else
            model.addAttribute("mode", "view");

        model.addAttribute("workflowHistory",
                lineEstimateService.getHistory(lineEstimate.getState(), lineEstimate.getStateHistory()));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        model.addAttribute("approvalDesignation", request.getParameter("approvalDesignation"));
        model.addAttribute("approvalPosition", request.getParameter("approvalPosition"));

        final LineEstimate newLineEstimate = getEstimateDocuments(lineEstimate);
        model.addAttribute("lineEstimate", newLineEstimate);
        if (request != null && request.getParameter("message") != null && request.getParameter("message").equals("update"))
            model.addAttribute("message", WorksConstants.LINEESTIMATE_UPDATE);
        return "newLineEstimate-edit";
    }

    private LineEstimate getEstimateDocuments(final LineEstimate lineEstimate) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(lineEstimate.getId(),
                WorksConstants.MODULE_NAME_LINEESTIMATE);
        lineEstimate.setDocumentDetails(documentDetailsList);
        return lineEstimate;
    }
}
