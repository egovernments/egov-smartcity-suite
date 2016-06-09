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
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.matrix.service.CustomizedWorkFlowService;
import org.egov.services.masters.SchemeService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.MultiYearEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimate.EstimateStatus;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateDetailService;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.master.service.NatureOfWorkService;
import org.egov.works.master.service.OverheadService;
import org.egov.works.master.service.ScheduleCategoryService;
import org.egov.works.master.service.UOMService;
import org.egov.works.utils.WorksConstants;
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

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showAbstractEstimateForm(@RequestParam final Long lineEstimateDetailId, final Model model) {
        final AbstractEstimate abstractEstimate = new AbstractEstimate();
        LineEstimateDetails lineEstimateDetails = lineEstimateDetailService.getById(lineEstimateDetailId);
        estimateService.populateDataForAbstractEstimate(lineEstimateDetails, model, abstractEstimate);
        final MultiYearEstimate multiYearEstimate = new MultiYearEstimate();
        final List<MultiYearEstimate> multiYearEstimateList = new ArrayList<MultiYearEstimate>();
        multiYearEstimate.setFinancialYear(financialYearHibernateDAO.getFinancialYearByDate(new Date()));
        multiYearEstimate.setPercentage(100);
        multiYearEstimateList.add(multiYearEstimate);
        abstractEstimate.setMultiYearEstimates(multiYearEstimateList);
        model.addAttribute("estimateTemplateConfirmMsg",
                messageSource.getMessage("masg.estimate.template.confirm.reset", null, null));
        setDropDownValues(model);
        
        model.addAttribute("stateType", abstractEstimate.getClass().getSimpleName());

        WorkflowContainer workflowContainer = new WorkflowContainer();
        prepareWorkflow(model, abstractEstimate, workflowContainer);
        
        List<String> validActions = Collections.emptyList();
        
        validActions = customizedWorkFlowService.getNextValidActions(abstractEstimate.getStateType(), workflowContainer
                .getWorkFlowDepartment(), workflowContainer.getAmountRule(), workflowContainer.getAdditionalRule(),
                WorksConstants.NEW, workflowContainer.getPendingActions(), abstractEstimate.getCreatedDate());
        
        model.addAttribute("validActionList", validActions);

        model.addAttribute("mode", null);
        
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
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String saveAbstractEstimate(@ModelAttribute final AbstractEstimate abstractEstimate,
            final RedirectAttributes redirectAttributes, final Model model, final BindingResult bindErrors,
            @RequestParam("file") final MultipartFile[] files, final HttpServletRequest request,
            @RequestParam String workFlowAction) throws IOException {
        validateMultiYearEstimates(abstractEstimate, bindErrors);
        validateMandatory(abstractEstimate, bindErrors);
        estimateService.validateAssetDetails(abstractEstimate, bindErrors);
        if (bindErrors.hasErrors()) {
            setDropDownValues(model);
            estimateService.populateDataForAbstractEstimate(abstractEstimate.getLineEstimateDetails(), model,
                    abstractEstimate);
            
            model.addAttribute("stateType", abstractEstimate.getClass().getSimpleName());

            prepareWorkflow(model, abstractEstimate, new WorkflowContainer());

            model.addAttribute("mode", null);
            
            return "newAbstractEstimate-form";
        } else {
            Long approvalPosition = 0l;
            String approvalComment = "";
            if (request.getParameter("approvalComment") != null)
                approvalComment = request.getParameter("approvalComent");
            if (request.getParameter("workFlowAction") != null)
                workFlowAction = request.getParameter("workFlowAction");
            if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
                approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
            
            if (abstractEstimate.getState() == null) {
                if(workFlowAction.equals(WorksConstants.FORWARD_ACTION))
                    abstractEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.ABSTRACTESTIMATE,
                            EstimateStatus.CREATED.toString()));
                else
                    abstractEstimate.setEgwStatus(egwStatusHibernateDAO.getStatusByModuleAndCode(WorksConstants.ABSTRACTESTIMATE,
                            EstimateStatus.NEW.toString()));
            }
            estimateService.createAbstractEstimate(abstractEstimate, files, approvalPosition,
                    approvalComment, null, workFlowAction);
            model.addAttribute("message", "Abstract Estimate created successfully");
            return "abstractEstimate-success";
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
        if (abstractEstimate.getEstimateValue() != null && abstractEstimate.getEstimateValue().compareTo(lineEstimateDetails.getEstimateAmount()) == 1) {
            BigDecimal diffValue = abstractEstimate.getEstimateValue()
                    .subtract(lineEstimateDetails.getEstimateAmount());
            bindErrors.reject("error.estimatevalue.greater", new String[] { diffValue.toString() },
                    "error.estimatevalue.greater");
        }
    }
    
    @RequestMapping(value = "/abstractestimate-success", method = RequestMethod.GET)
    public ModelAndView successView(@ModelAttribute AbstractEstimate abstractEstimate,
            final HttpServletRequest request, final Model model, final ModelMap modelMap) {

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
            abstractEstimate = estimateService
                    .getAbstractEstimateById(id);
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

        if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.NEW.toString())
                && !abstractEstimate.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            message = "Abstract Estimate has been saved successfully";
        else if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.CREATED.toString())
                && !abstractEstimate.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.lineestimate.create.success",
                    new String[] { approverName, nextDesign, abstractEstimate.getEstimateNumber() }, null);
        else if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.ADMIN_SANCTIONED.toString()))
            message = messageSource.getMessage(
                    "msg.lineestimate.adminsanction.success",
                    new String[] { abstractEstimate.getEstimateNumber(), approverName, nextDesign },
                    null);
        else if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.TECH_SANCTIONED.toString()))
            message = messageSource.getMessage("msg.lineestimate.techsanction.success",
                    new String[] { abstractEstimate.getEstimateNumber() }, null);
        else if (abstractEstimate.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.lineestimate.reject",
                    new String[] { abstractEstimate.getEstimateNumber(), approverName, nextDesign }, null);
        else if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.CANCELLED.toString()))
            message = messageSource.getMessage("msg.lineestimate.cancel",
                    new String[] { abstractEstimate.getEstimateNumber() }, null);

        return message;
    }

}
