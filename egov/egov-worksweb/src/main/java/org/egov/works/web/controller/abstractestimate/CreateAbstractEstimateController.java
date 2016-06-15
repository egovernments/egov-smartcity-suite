package org.egov.works.web.controller.abstractestimate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CFinancialYear;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.EgwTypeOfWorkHibernateDAO;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.dao.FunctionHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.dao.budget.BudgetGroupDAO;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.matrix.service.CustomizedWorkFlowService;
import org.egov.services.masters.SchemeService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimate.EstimateStatus;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.entity.MultiYearEstimate;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateDetailService;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.master.service.NatureOfWorkService;
import org.egov.works.master.service.OverheadService;
import org.egov.works.master.service.ScheduleCategoryService;
import org.egov.works.master.service.ScheduleOfRateService;
import org.egov.works.master.service.UOMService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/abstractestimate")
public class CreateAbstractEstimateController extends GenericWorkFlowController {

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private LineEstimateDetailService lineEstimateDetailService;

    @Autowired
    private OverheadService overheadService;

    @Autowired
    private ScheduleCategoryService scheduleCategoryService;

    @Autowired
    private NatureOfWorkService natureOfWorkService;

    @Autowired
    private EgwTypeOfWorkHibernateDAO egwTypeOfWorkHibernateDAO;

    @Autowired
    private FundHibernateDAO fundHibernateDAO;

    @Autowired
    private FunctionHibernateDAO functionHibernateDAO;

    @Autowired
    private BudgetGroupDAO budgetGroupDAO;

    @Autowired
    private SchemeService schemeService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private FinancialYearDAO financialYearDAO;

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private UOMService uomService;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;

    @Autowired
    protected CustomizedWorkFlowService customizedWorkFlowService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private ScheduleOfRateService scheduleOfRateService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showAbstractEstimateForm(@RequestParam final Long lineEstimateDetailId, final Model model) {
        final AbstractEstimate abstractEstimate = new AbstractEstimate();
        LineEstimateDetails lineEstimateDetails = lineEstimateDetailService.getById(lineEstimateDetailId);
        
        final MultiYearEstimate multiYearEstimate = new MultiYearEstimate();
        final List<MultiYearEstimate> multiYearEstimateList = new ArrayList<MultiYearEstimate>();
        multiYearEstimate.setFinancialYear(financialYearHibernateDAO.getFinancialYearByDate(new Date()));
        multiYearEstimate.setPercentage(100);
        multiYearEstimateList.add(multiYearEstimate);
        abstractEstimate.setMultiYearEstimates(multiYearEstimateList);

        loadViewData(model, abstractEstimate,lineEstimateDetails);

        return "newAbstractEstimate-form";
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("overheads", overheadService.getOverheadsByDate(new Date()));
        model.addAttribute("scheduleCategories", scheduleCategoryService.getAllScheduleCategories());
        model.addAttribute("funds", fundHibernateDAO.findAllActiveFunds());
        model.addAttribute("functions", functionHibernateDAO.getAllActiveFunctions());
        model.addAttribute("budgetHeads", budgetGroupDAO.getBudgetGroupList());
        model.addAttribute("schemes", schemeService.findAll());
        model.addAttribute("departments", lineEstimateService.getUserDepartments(securityUtils.getCurrentUser()));
        model.addAttribute("typeOfWork", egwTypeOfWorkHibernateDAO.getTypeOfWorkForPartyTypeContractor());
        model.addAttribute("natureOfWork", natureOfWorkService.findAll());
        model.addAttribute("finYear", financialYearDAO.findAll());
        model.addAttribute("uoms", uomService.getAllUOMs());
        model.addAttribute("budgetHeads", budgetGroupDAO.getBudgetGroupList());

        final List<AppConfigValues> values = appConfigValuesService.getConfigValuesByModuleAndKey(
                WorksConstants.WORKS_MODULE_NAME, WorksConstants.APPCONFIG_KEY_SHOW_SERVICE_FIELDS);
        final AppConfigValues value = values.get(0);
        if (value.getValue().equalsIgnoreCase("Yes"))
            model.addAttribute("isServiceVATRequired", true);
        else
            model.addAttribute("isServiceVATRequired", false);
    }

    private void loadViewData(Model model, AbstractEstimate abstractEstimate,LineEstimateDetails lineEstimateDetails) {
        
        estimateService.populateDataForAbstractEstimate(lineEstimateDetails, model, abstractEstimate);
        setDropDownValues(model);

        model.addAttribute("stateType", abstractEstimate.getClass().getSimpleName());

        WorkflowContainer workflowContainer = new WorkflowContainer();
        prepareWorkflow(model, abstractEstimate, workflowContainer);

        List<String> validActions = Collections.emptyList();

        validActions = customizedWorkFlowService.getNextValidActions(abstractEstimate.getStateType(),
                workflowContainer.getWorkFlowDepartment(), workflowContainer.getAmountRule(),
                workflowContainer.getAdditionalRule(), WorksConstants.NEW, workflowContainer.getPendingActions(),
                abstractEstimate.getCreatedDate());

        model.addAttribute("validActionList", validActions);
        model.addAttribute("mode", null);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String saveAbstractEstimate(@ModelAttribute final AbstractEstimate abstractEstimate,
            final RedirectAttributes redirectAttributes, final Model model, final BindingResult bindErrors,
            @RequestParam("file") final MultipartFile[] files, final HttpServletRequest request,
            @RequestParam String workFlowAction) throws IOException {

        Long approvalPosition = 0l;
        String approvalComment = "";
        if (request.getParameter("approvalComment") != null)
            approvalComment = request.getParameter("approvalComent");
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");
        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        validateMultiYearEstimates(abstractEstimate, bindErrors);
        validateMandatory(abstractEstimate, bindErrors);
        estimateService.validateAssetDetails(abstractEstimate, bindErrors);
        if (!workFlowAction.equals(WorksConstants.SAVE_ACTION))
            estimateService.validateActivities(abstractEstimate, bindErrors);
        if (bindErrors.hasErrors()) {
            for (final Activity activity : abstractEstimate.getSorActivities()) {
                activity.setSchedule(scheduleOfRateService.getScheduleOfRateById(activity.getSchedule().getId()));
            }

            loadViewData(model, abstractEstimate,abstractEstimate.getLineEstimateDetails());

            model.addAttribute("approvalDesignation", request.getParameter("approvalDesignation"));
            model.addAttribute("approvalPosition", request.getParameter("approvalPosition"));

            return "newAbstractEstimate-form";
        } else {
            if (abstractEstimate.getState() == null) {
                if (workFlowAction.equals(WorksConstants.FORWARD_ACTION))
                    abstractEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(
                            WorksConstants.ABSTRACTESTIMATE, EstimateStatus.CREATED.toString()));
                else
                    abstractEstimate.setEgwStatus(egwStatusHibernateDAO
                            .getStatusByModuleAndCode(WorksConstants.ABSTRACTESTIMATE, EstimateStatus.NEW.toString()));
            }
            final AbstractEstimate savedAbstractEstimate = estimateService.createAbstractEstimate(abstractEstimate, files,
                    approvalPosition, approvalComment, null,
                    workFlowAction);

            final String pathVars = worksUtils.getPathVars(savedAbstractEstimate.getEgwStatus(), savedAbstractEstimate.getState(),
                    savedAbstractEstimate.getId(), approvalPosition);

            return "redirect:/abstractestimate/abstractestimate-success?pathVars=" + pathVars;
        }

    }

    private void validateMultiYearEstimates(final AbstractEstimate abstractEstimate, final BindingResult bindErrors) {
        CFinancialYear cFinancialYear = null;
        Double totalPercentage = 0d;
        Integer index = 0;
        for (final MultiYearEstimate multiYearEstimate : abstractEstimate.getMultiYearEstimates()) {
            totalPercentage = totalPercentage + multiYearEstimate.getPercentage();

            if (multiYearEstimate.getFinancialYear() == null) {
                bindErrors.rejectValue("multiYearEstimates[" + index + "].financialYear", "error.finyear.required");
            }
            if (multiYearEstimate.getPercentage() == 0) {
                bindErrors.rejectValue("multiYearEstimates[" + index + "].percentage", "error.percentage.required");
            }
            if (cFinancialYear != null && cFinancialYear.equals(multiYearEstimate.getFinancialYear())) {
                bindErrors.rejectValue("multiYearEstimates[" + index + "].financialYear", "error.financialYear.unique");
            }
            if (totalPercentage > 100) {
                bindErrors.rejectValue("multiYearEstimates[" + index + "].percentage", "error.percentage.greater");
            }
            cFinancialYear = multiYearEstimate.getFinancialYear();
            index++;
        }

    }

    private void validateMandatory(final AbstractEstimate abstractEstimate, final BindingResult bindErrors) {
        if (StringUtils.isBlank(abstractEstimate.getDescription())) {
            bindErrors.rejectValue("description", "error.description.required");
        }
        LineEstimateDetails lineEstimateDetails = abstractEstimate.getLineEstimateDetails();
        if (abstractEstimate.getEstimateValue() != null
                && abstractEstimate.getEstimateValue().compareTo(lineEstimateDetails.getEstimateAmount()) == 1) {
            BigDecimal diffValue = abstractEstimate.getEstimateValue()
                    .subtract(lineEstimateDetails.getEstimateAmount());
            bindErrors.reject("error.estimatevalue.greater", new String[] { diffValue.toString() },
                    "error.estimatevalue.greater");
        }
    }

    @RequestMapping(value = "/abstractestimate-success", method = RequestMethod.GET)
    public ModelAndView successView(@ModelAttribute AbstractEstimate abstractEstimate, final HttpServletRequest request,
            final Model model, final ModelMap modelMap) {

        final String[] keyNameArray = request.getParameter("pathVars").split(",");
        Long id = 0L;
        String approverName = "";
        String currentUserDesgn = "";
        String nextDesign = "";
        if (keyNameArray.length != 0 && keyNameArray.length > 0)
            if (keyNameArray.length == 1 && !keyNameArray[0].equals("null"))
                id = Long.parseLong(keyNameArray[0]);
            else if (keyNameArray.length == 3) {
                if (!keyNameArray[0].equals("null"))
                    id = Long.parseLong(keyNameArray[0]);
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
            } else {
                if (!keyNameArray[0].equals("null"))
                    id = Long.parseLong(keyNameArray[0]);
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
                nextDesign = keyNameArray[3];
            }

        if (id != null)
            abstractEstimate = estimateService.getAbstractEstimateById(id);
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

        if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.NEW.toString()))
            message = messageSource.getMessage("msg.estimate.saved", new String[] {}, null);
        else if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.CREATED.toString())
                && !abstractEstimate.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.estimate.created",
                    new String[] { approverName, nextDesign, abstractEstimate.getEstimateNumber() }, null);
        else if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.ADMIN_SANCTIONED.toString())
                && !abstractEstimate.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.estimate.adminsanctioned",
                    new String[] { abstractEstimate.getEstimateNumber() }, null);
        else if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.TECH_SANCTIONED.toString())
                && !abstractEstimate.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED)) {
            message = messageSource.getMessage("msg.estimate.techsanctioned",
                    new String[] { abstractEstimate.getEstimateNumber(), approverName, nextDesign,
                            abstractEstimate.getEstimateTechnicalSanctions()
                                    .get(abstractEstimate.getEstimateTechnicalSanctions().size() - 1)
                                    .getTechnicalSanctionNumber() },
                    null);
        } else if (abstractEstimate.getState() != null
                && abstractEstimate.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.estimate.rejected",
                    new String[] { abstractEstimate.getEstimateNumber(), approverName, nextDesign }, null);
        else if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.CANCELLED.toString()))
            message = messageSource.getMessage("msg.estimate.cancelled",
                    new String[] { abstractEstimate.getEstimateNumber() }, null);

        return message;
    }

}
