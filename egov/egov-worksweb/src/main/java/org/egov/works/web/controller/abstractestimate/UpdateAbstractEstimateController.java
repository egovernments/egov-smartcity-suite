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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.EgwTypeOfWorkHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.services.masters.SchemeService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimate.EstimateStatus;
import org.egov.works.abstractestimate.entity.MultiYearEstimate;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.master.service.NatureOfWorkService;
import org.egov.works.master.service.OverheadService;
import org.egov.works.master.service.ScheduleCategoryService;
import org.egov.works.master.service.UOMService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
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
    private LineEstimateService lineEstimateService;

    @Autowired
    private FundHibernateDAO fundHibernateDAO;

    @Autowired
    private FunctionHibernateDAO functionHibernateDAO;

    @Autowired
    private BudgetGroupDAO budgetGroupDAO;

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
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private OverheadService overheadService;

    @Autowired
    private ScheduleCategoryService scheduleCategoryService;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private UOMService uomService;

    @ModelAttribute
    public AbstractEstimate getAbstractEstimate(@PathVariable final String abstractEstimateId) {
        final AbstractEstimate abstractEstimate = estimateService.getAbstractEstimateById(Long.parseLong(abstractEstimateId));
        return abstractEstimate;
    }

    @RequestMapping(value = "/update/{abstractEstimateId}", method = RequestMethod.GET)
    public String updateAbstractEstimate(final Model model, @PathVariable final String abstractEstimateId,
            final HttpServletRequest request)
            throws ApplicationException {
        final AbstractEstimate abstractEstimate = getAbstractEstimate(abstractEstimateId);
        final LineEstimateDetails lineEstimateDetails = abstractEstimate.getLineEstimateDetails();
        estimateService.populateDataForAbstractEstimate(lineEstimateDetails, model, abstractEstimate);
        model.addAttribute("estimateTemplateConfirmMsg",
                messageSource.getMessage("masg.estimate.template.confirm.reset", null, null));
        setDropDownValues(model);
        return loadViewData(model, request, abstractEstimate);
    }

    @RequestMapping(value = "/view/{abstractEstimateId}", method = RequestMethod.GET)
    public String viewAbstractEstimate(final Model model, @PathVariable final String abstractEstimateId,
            final HttpServletRequest request)
            throws ApplicationException {
        final AbstractEstimate abstractEstimate = getAbstractEstimate(abstractEstimateId);

        final String responsePage = loadViewData(model, request, abstractEstimate);
        model.addAttribute("createdbybydesignation", worksUtils.getUserDesignation(abstractEstimate.getCreatedBy()));
        model.addAttribute("mode", "readOnly");
        return responsePage;
    }

    @RequestMapping(value = "/update/{abstractEstimateId}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("abstractEstimate") final AbstractEstimate abstractEstimate,
            final BindingResult errors, final RedirectAttributes redirectAttributes,
            final Model model, final HttpServletRequest request, @RequestParam("file") final MultipartFile[] files)
            throws ApplicationException, IOException {

        validateMultiYearEstimates(abstractEstimate, errors);

        String mode = "";
        String workFlowAction = "";
        AbstractEstimate newAbstractEstimate = null;

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
            approvalPosition = estimateService.getApprovalPositionByMatrixDesignation(
                    abstractEstimate, approvalPosition, null,
                    mode, workFlowAction);

        if ((approvalPosition == null || approvalPosition.equals(Long.valueOf(0)))
                && request.getParameter("approvalPosition") != null
                && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.CREATED.toString()))
            validateTechSanctionDetails(abstractEstimate, errors);

        if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.TECH_SANCTIONED.toString())
                && !workFlowAction.equalsIgnoreCase(WorksConstants.REJECT_ACTION.toString()))
            validateAdminSanctionDetail(abstractEstimate, errors);

        if (errors.hasErrors()) {
            setDropDownValues(model);
            return loadViewData(model, request, abstractEstimate);
        } else {
            if (null != workFlowAction)
                newAbstractEstimate = estimateService.updateAbstractEstimateDetails(abstractEstimate, approvalPosition,
                        approvalComment, null, workFlowAction,
                        mode, null, files);
            redirectAttributes.addFlashAttribute("abstractEstimate", newAbstractEstimate);

            final String pathVars = worksUtils.getPathVars(newAbstractEstimate.getEgwStatus(), newAbstractEstimate.getState(),
                    newAbstractEstimate.getId(), approvalPosition);

            return "redirect:/abstractestimate/abstractestimate-success?pathVars=" + pathVars;
        }
    }

    private void validateTechSanctionDetails(final AbstractEstimate abstractEstimate, final BindingResult errors) {
        // TODO Validate details here
    }

    private void validateAdminSanctionDetail(final AbstractEstimate abstractEstimate, final BindingResult errors) {
        // TODO Validate details here
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("funds", fundHibernateDAO.findAllActiveFunds());
        model.addAttribute("functions", functionHibernateDAO.getAllActiveFunctions());
        model.addAttribute("schemes", schemeService.findAll());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("typeOfWork", egwTypeOfWorkHibernateDAO.getTypeOfWorkForPartyTypeContractor());
        model.addAttribute("natureOfWork", natureOfWorkService.findAll());
        model.addAttribute("locations", boundaryService.getActiveBoundariesByBndryTypeNameAndHierarchyTypeName(
                WorksConstants.LOCATION_BOUNDARYTYPE, WorksConstants.LOCATION_HIERARCHYTYPE));
        model.addAttribute("overheads", overheadService.getOverheadsByDate(new Date()));
        model.addAttribute("scheduleCategories", scheduleCategoryService.getAllScheduleCategories());
        model.addAttribute("budgetHeads", budgetGroupDAO.getBudgetGroupList());
        model.addAttribute("finYear", financialYearDAO.findAll());
        model.addAttribute("uoms", uomService.getAllUOMs());
        model.addAttribute("budgetHeads", budgetGroupDAO.getBudgetGroupList());
    }

    private String loadViewData(final Model model, final HttpServletRequest request,
            final AbstractEstimate abstractEstimate) {

        model.addAttribute("stateType", abstractEstimate.getClass().getSimpleName());

        if (abstractEstimate.getCurrentState() != null
                && !abstractEstimate.getCurrentState().getValue().equals(WorksConstants.NEW))
            model.addAttribute("currentState", abstractEstimate.getCurrentState().getValue());

        final WorkflowContainer workflowContainer = new WorkflowContainer();
        prepareWorkflow(model, abstractEstimate, workflowContainer);
        if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.NEW.toString())) {
            List<String> validActions = Collections.emptyList();

            validActions = customizedWorkFlowService.getNextValidActions(abstractEstimate.getStateType(), workflowContainer
                    .getWorkFlowDepartment(), workflowContainer.getAmountRule(), workflowContainer.getAdditionalRule(),
                    WorksConstants.NEW, workflowContainer.getPendingActions(), abstractEstimate.getCreatedDate());
            model.addAttribute("validActionList", validActions);
        }
        if (abstractEstimate.getState() != null
                && abstractEstimate.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            model.addAttribute("mode", "edit");
        else
            model.addAttribute("mode", "view");

        model.addAttribute("workflowHistory",
                lineEstimateService.getHistory(abstractEstimate.getState(), abstractEstimate.getStateHistory()));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        model.addAttribute("approvalDesignation", request.getParameter("approvalDesignation"));
        model.addAttribute("approvalPosition", request.getParameter("approvalPosition"));

        final AbstractEstimate newAbstractEstimate = getEstimateDocuments(abstractEstimate);
        model.addAttribute("abstractEstimate", newAbstractEstimate);
        if (request != null && request.getParameter("message") != null && request.getParameter("message").equals("update"))
            model.addAttribute("message", WorksConstants.LINEESTIMATE_UPDATE);
        return "newAbstractEstimate-form";
    }

    private AbstractEstimate getEstimateDocuments(final AbstractEstimate abstractEstimate) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(abstractEstimate.getId(),
                WorksConstants.ABSTRACTESTIMATE);
        abstractEstimate.setDocumentDetails(documentDetailsList);
        return abstractEstimate;
    }

    private void validateMultiYearEstimates(final AbstractEstimate abstractEstimate, final BindingResult bindErrors) {
        CFinancialYear cFinancialYear = null;
        Double totalPercentage = 0d;
        Integer index = 0;
        for (final MultiYearEstimate multiYearEstimate : abstractEstimate.getMultiYearEstimates()) {
            totalPercentage = totalPercentage + multiYearEstimate.getPercentage();

            if (multiYearEstimate.getFinancialYear() == null)
                bindErrors.rejectValue("multiYearEstimates[" + index + "].financialYear", "error.finyear.required");
            if (multiYearEstimate.getPercentage() == 0)
                bindErrors.rejectValue("multiYearEstimates[" + index + "].percentage", "error.percentage.required");
            if (cFinancialYear != null && cFinancialYear.equals(multiYearEstimate.getFinancialYear()))
                bindErrors.rejectValue("multiYearEstimates[" + index + "].financialYear", "error.financialYear.unique");
            if (totalPercentage > 100)
                bindErrors.rejectValue("multiYearEstimates[" + index + "].percentage", "error.percentage.greater");
            cFinancialYear = multiYearEstimate.getFinancialYear();
            index++;
        }

    }
}
