package org.egov.works.web.controller.abstractestimate;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.workflow.matrix.service.CustomizedWorkFlowService;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.entity.AbstractEstimate.EstimateStatus;
import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateDetailService;
import org.egov.works.master.service.ScheduleOfRateService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    private MessageSource messageSource;

    @Autowired
    protected CustomizedWorkFlowService customizedWorkFlowService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private ScheduleOfRateService scheduleOfRateService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showAbstractEstimateForm(@ModelAttribute("abstractEstimate") final AbstractEstimate abstractEstimate,
            @RequestParam final Long lineEstimateDetailId, final Model model) {
        LineEstimateDetails lineEstimateDetails = lineEstimateDetailService.getById(lineEstimateDetailId);
        populateDataForAbstractEstimate(lineEstimateDetails, model, abstractEstimate);
        loadViewData(model, abstractEstimate, lineEstimateDetails);

        return "newAbstractEstimate-form";
    }

    private void loadViewData(Model model, AbstractEstimate abstractEstimate, LineEstimateDetails lineEstimateDetails) {
        estimateService.setDropDownValues(model);
        model.addAttribute("documentDetails", abstractEstimate.getDocumentDetails());
        model.addAttribute("stateType", abstractEstimate.getClass().getSimpleName());
        WorkflowContainer workflowContainer = new WorkflowContainer();
        prepareWorkflow(model, abstractEstimate, workflowContainer);
        List<String> validActions = Collections.emptyList();
        validActions = customizedWorkFlowService.getNextValidActions(abstractEstimate.getStateType(),
                workflowContainer.getWorkFlowDepartment(), workflowContainer.getAmountRule(),
                workflowContainer.getAdditionalRule(), WorksConstants.NEW, workflowContainer.getPendingActions(),
                abstractEstimate.getCreatedDate());
        if (abstractEstimate.getState() != null  && abstractEstimate.getState().getNextAction()!=null )
            model.addAttribute("nextAction", abstractEstimate.getState().getNextAction());
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
        estimateService.validateMultiYearEstimates(abstractEstimate, bindErrors);
        estimateService.validateMandatory(abstractEstimate, bindErrors);
        estimateService.validateAssetDetails(abstractEstimate, bindErrors);
        estimateService.validateActivities(abstractEstimate, bindErrors);
        if (!workFlowAction.equals(WorksConstants.SAVE_ACTION)) {
            if (abstractEstimate.getSorActivities().isEmpty() && abstractEstimate.getNonSorActivities().isEmpty())
                bindErrors.reject("error.sor.nonsor.required", "error.sor.nonsor.required");
        }
        if (bindErrors.hasErrors()) {
            for (final Activity activity : abstractEstimate.getSorActivities()) {
                activity.setSchedule(scheduleOfRateService.getScheduleOfRateById(activity.getSchedule().getId()));
            }
            estimateService.loadModelValues(abstractEstimate.getLineEstimateDetails(), model, abstractEstimate);
            loadViewData(model, abstractEstimate, abstractEstimate.getLineEstimateDetails());
            model.addAttribute("approvalDesignation", request.getParameter("approvalDesignation"));
            model.addAttribute("approvalPosition", request.getParameter("approvalPosition"));
            model.addAttribute("mode", "edit");
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
            final AbstractEstimate savedAbstractEstimate = estimateService.createAbstractEstimate(abstractEstimate,
                    files, approvalPosition, approvalComment, null, workFlowAction);

            if (savedAbstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.NEW.toString()))
                return "redirect:/abstractestimate/update/" + savedAbstractEstimate.getId() + "?mode=save";

            return "redirect:/abstractestimate/abstractestimate-success?estimate=" + savedAbstractEstimate.getId()
                    + "&approvalPosition=" + approvalPosition;
        }

    }

    @RequestMapping(value = "/abstractestimate-success", method = RequestMethod.GET)
    public ModelAndView successView(@ModelAttribute AbstractEstimate abstractEstimate, @RequestParam("estimate") Long id,
            @RequestParam("approvalPosition") Long approvalPosition, final HttpServletRequest request,
            final Model model) {

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

        if (abstractEstimate.getEgwStatus().getCode().equals(EstimateStatus.NEW.toString()))
            message = messageSource.getMessage("msg.estimate.saved", new String[] { abstractEstimate.getEstimateNumber() }, null);
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

    /**
     * Method called to populate data from lineestimatedetails to create abstract estimate
     * @param lineEstimateDetails
     * @param model
     * @param abstractEstimate
     */
    public void populateDataForAbstractEstimate(final LineEstimateDetails lineEstimateDetails, final Model model,
            final AbstractEstimate abstractEstimate) {
        final LineEstimate lineEstimate = lineEstimateDetails.getLineEstimate();
        abstractEstimate.setLineEstimateDetails(lineEstimateDetails);
        abstractEstimate.setExecutingDepartment(lineEstimateDetails.getLineEstimate().getExecutingDepartment());
        abstractEstimate.setWard(lineEstimateDetails.getLineEstimate().getWard());
        abstractEstimate.setNatureOfWork(lineEstimate.getNatureOfWork());
        abstractEstimate.setParentCategory(lineEstimate.getTypeOfWork());
        abstractEstimate.setCategory(lineEstimate.getSubTypeOfWork());
        abstractEstimate.setProjectCode(lineEstimateDetails.getProjectCode());
        abstractEstimate.addMultiYearEstimate(estimateService.populateMultiYearEstimate(abstractEstimate));
        abstractEstimate.addFinancialDetails(estimateService.populateEstimateFinancialDetails(abstractEstimate));
        estimateService.loadModelValues(lineEstimateDetails, model, abstractEstimate);
    }

}
