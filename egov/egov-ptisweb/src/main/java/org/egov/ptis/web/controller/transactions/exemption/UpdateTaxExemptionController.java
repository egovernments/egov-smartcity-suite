package org.egov.ptis.web.controller.transactions.exemption;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pims.commons.Designation;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.TaxExeptionReason;
import org.egov.ptis.domain.service.exemption.TaxExemptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static org.egov.ptis.constants.PropertyTaxConstants.*;

@Controller
@RequestMapping(value = "/exemption/update/{id}")
public class UpdateTaxExemptionController extends GenericWorkFlowController {

    protected static final String TAX_EXEMPTION_FORM = "taxExemption-form";
    protected static final String TAX_EXEMPTION_SUCCESS = "taxExemption-success";
    protected static final String TAX_EXEMPTION_VIEW = "taxExemption-view";
    public static final String EDIT = "edit";
    public static final String VIEW = "view";

    private TaxExemptionService taxExemptionService;

    @Autowired
    public UpdateTaxExemptionController(TaxExemptionService taxExemptionService) {
        this.taxExemptionService = taxExemptionService;
    }

    private PropertyImpl property;

    @Autowired
    private PtDemandDao ptDemandDAO;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private SecurityUtils securityUtils;
    
    @Autowired
    protected AssignmentService assignmentService;

    @ModelAttribute
    public Property propertyModel(@PathVariable String id) {
        property = taxExemptionService.findByNamedQuery(QUERY_WORKFLOW_PROPERTYIMPL_BYID, Long.valueOf(id));
        if (null == property) {
            property = taxExemptionService.findByNamedQuery(QUERY_PROPERTYIMPL_BYID, Long.valueOf(id));
        }
        return property;
    }

    @SuppressWarnings("unchecked")
    @ModelAttribute("taxExemptionReasons")
    public List<TaxExeptionReason> getTaxExemptionReasons() {
        return taxExemptionService.getSession().createQuery("from TaxExeptionReason order by name").list();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String view(final Model model, @PathVariable final Long id, final HttpServletRequest request) {

        String userDesgn = "";
        final String currState = property.getState().getValue();
        final String nextAction = property.getState().getNextAction();
        final Designation designation = propertyTaxUtil.getDesignationForUser(securityUtils.getCurrentUser().getId());
        if (null != designation)
            userDesgn = designation.getName();
        model.addAttribute("stateType", property.getClass().getSimpleName());
        model.addAttribute("currentState", property.getCurrentState().getValue());
        model.addAttribute("userDesgn", userDesgn);
        prepareWorkflow(model, property, new WorkflowContainer());
        model.addAttribute("userDesgn", userDesgn);
        model.addAttribute("designation", COMMISSIONER_DESGN);
        taxExemptionService.addModelAttributes(model, property.getBasicProperty());
        if (currState.endsWith(WF_STATE_REJECTED) || nextAction.equalsIgnoreCase(WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING)
                || currState.endsWith(WFLOW_ACTION_NEW)) { 
            model.addAttribute("mode", EDIT);
            return TAX_EXEMPTION_FORM;
        } else {
            model.addAttribute("mode", VIEW);
            return TAX_EXEMPTION_VIEW;
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute Property property, final BindingResult errors,
            final RedirectAttributes redirectAttributes, final HttpServletRequest request, final Model model,
            @RequestParam String workFlowAction) {

        final Character status = STATUS_WORKFLOW;
        Long approvalPosition = 0l;
        String approvalComent = "";
        String taxExemptedReason = "";

        Property oldProperty = (PropertyImpl) property.getBasicProperty().getActiveProperty();

        if (request.getParameter("approvalComent") != null)
            approvalComent = request.getParameter("approvalComent");
        if (request.getParameter("workFlowAction") != null)
            workFlowAction = request.getParameter("workFlowAction");
        if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));
        Boolean propertyByEmployee = Boolean.valueOf(request.getParameter("propertyByEmployee"));
        if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE)) {
            property.setStatus(STATUS_ISACTIVE);
            oldProperty.setStatus(STATUS_ISHISTORY);
        }

        if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_NOTICE_GENERATE)) {
            return "redirect:/notice/propertyTaxNotice-generateSpecialNotice.action?basicPropId="
                    + property.getBasicProperty().getId() + "&noticeType=" + NOTICE_TYPE_SPECIAL_NOTICE
                    + "&noticeMode=" + APPLICATION_TYPE_DEMOLITION;
        } else {

            if (request.getParameter("mode").equalsIgnoreCase(VIEW)) {
                taxExemptionService.updateProperty(property, approvalComent, workFlowAction, approvalPosition,
                        propertyByEmployee, EXEMPTION);
            } else {
                if (request.getParameter("taxExemptedReason") != null)
                    taxExemptedReason = request.getParameter("taxExemptedReason");
                taxExemptionService.saveProperty(property, oldProperty, status, approvalComent, workFlowAction,
                        approvalPosition, taxExemptedReason, propertyByEmployee, EXEMPTION);
            }
            String successMessage = "";
            Assignment assignment = new Assignment();
            if(property!=null && property.getCreatedBy()!=null){
                assignment = assignmentService.getPrimaryAssignmentForUser(property.getCreatedBy().getId());
            }
            if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE)) {
                if (taxExemptionService.isPropertyByEmployee(property)) {
                    successMessage = "Property Exemption approved successfully and forwarded to  "
                            + assignment.getEmployee().getName().concat("~").concat(assignment.getPosition().getName()) + " with assessment number "
                            + property.getBasicProperty().getUpicNo();
                } else {
                    successMessage = "Property Exemption approved successfully and forwarded to  "
                            + propertyTaxUtil.getApproverUserName(((PropertyImpl) property).getStateHistory().get(0)
                                    .getOwnerPosition().getId()) + " with assessment number "
                            + property.getBasicProperty().getUpicNo();
                }
            } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {
                if (taxExemptionService.isPropertyByEmployee(property)) {
                    successMessage = "Property Exemption rejected successfully and forwared to initiator "
                            + assignment.getEmployee().getName().concat("~").concat(assignment.getPosition().getName()) + " with application number "
                            + property.getApplicationNo();
                } else {
                    successMessage = "Property Exemption rejected successfully and forwared to initiator "
                            + propertyTaxUtil.getApproverUserName(((PropertyImpl) property).getStateHistory().get(0)
                                    .getOwnerPosition().getId()) + " with application number "
                            + property.getApplicationNo();
                }
            } else
                successMessage = "Successfully forwarded to " + propertyTaxUtil.getApproverUserName(approvalPosition)
                        + " with application number " + property.getApplicationNo();

            model.addAttribute("successMessage", successMessage);
            return TAX_EXEMPTION_SUCCESS;
        }
    }

}
