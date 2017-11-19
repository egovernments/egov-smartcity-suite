/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.ptis.web.controller.transactions.exemption;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.TaxExemptionReason;
import org.egov.ptis.domain.service.exemption.TaxExemptionService;
import org.egov.ptis.domain.service.property.PropertyService;
import org.egov.ptis.domain.service.reassign.ReassignService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.egov.ptis.constants.PropertyTaxConstants.*;

@Controller
@RequestMapping(value = "/exemption/update/{id}")
public class UpdateTaxExemptionController extends GenericWorkFlowController {

    private static final String APPROVAL_POSITION = "approvalPosition";
    protected static final String TAX_EXEMPTION_FORM = "taxExemption-form";
    protected static final String TAX_EXEMPTION_SUCCESS = "taxExemption-success";
    protected static final String TAX_EXEMPTION_VIEW = "taxExemption-view";
    public static final String EDIT = "edit";
    public static final String VIEW = "view";
    private static final String SUCCESSMESSAGE = "successMessage";
    private static final String TAXEXEMPTIONREASON = "taxExemptedReason";
    private static final String PROPERTY_MODIFY_REJECT_FAILURE = "Initiator is not active so can not do rejection with the Assessment number :";
    private static final String CHOULTRY_DOC = "choultryDocs";
    private static final String EDUINST_DOC = "eduinstDocs";
    private static final String NGO_DOC = "ngoDocs";
    private static final String WORSHIP_DOC = "worshipDocs";
    private static final String EXSERVICE_DOC = "exserviceDocs";
    
    private final TaxExemptionService taxExemptionService;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    
    @Autowired
    private ReassignService reassignService;
    
    @Autowired
    private PropertyService propService;

    @Autowired
    public UpdateTaxExemptionController(final TaxExemptionService taxExemptionService) {
        this.taxExemptionService = taxExemptionService;
    }

    @ModelAttribute
    public PropertyImpl property(@PathVariable final String id) {
        PropertyImpl property = taxExemptionService.findByNamedQuery(QUERY_WORKFLOW_PROPERTYIMPL_BYID, Long.valueOf(id));
        if (property == null)
            property = taxExemptionService.findByNamedQuery(QUERY_PROPERTYIMPL_BYID, Long.valueOf(id));
        return property;
    }

    @SuppressWarnings("unchecked")
    @ModelAttribute("taxExemptionReasons")
    public List<TaxExemptionReason> getTaxExemptionReasons() {
        return taxExemptionService.getSession().createQuery("from TaxExemptionReason where isActive = true order by name").list();
    }
    
    @ModelAttribute("documentsEduInst")
    public List<DocumentType> documentsEduInst(TransactionType transactionType) {
        return taxExemptionService.getDocuments(TransactionType.TE_EDU_INST);
    }
    
    @ModelAttribute("documentsWorship")
    public List<DocumentType> documentsWorship(TransactionType transactionType) {
        return taxExemptionService.getDocuments(TransactionType.TE_PUBLIC_WORSHIP);
    }
    
    @ModelAttribute("documentsNGO")
    public List<DocumentType> documentsNGO(TransactionType transactionType) {
        return taxExemptionService.getDocuments(TransactionType.TE_PENSIONER_NGO);
    }
    
    @ModelAttribute("documentsExService")
    public List<DocumentType> documentsExService(TransactionType transactionType) {
        return taxExemptionService.getDocuments(TransactionType.TE_EXSERVICE);
    }
    
    @ModelAttribute("documentsChoultries")
    public List<DocumentType> documentsChoultries(TransactionType transactionType) {
        return taxExemptionService.getDocuments(TransactionType.TE_CHOULTRY);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String view(@ModelAttribute PropertyImpl property, final Model model, @PathVariable final Long id, final HttpServletRequest request) {
        boolean isExempted = property.getBasicProperty().getActiveProperty().getIsExemptedFromTax();
        String userDesignationList;
        boolean endorsementRequired = Boolean.FALSE;
        List<HashMap<String, Object>> historyMap;
        final String currState = property.getState().getValue();
        final String nextAction = property.getState().getNextAction();
        User loggedInUser = securityUtils.getCurrentUser();
        final boolean citizenPortalUser = propertyTaxCommonUtils.isCitizenPortalUser(loggedInUser);
        userDesignationList = propertyTaxCommonUtils.getAllDesignationsForUser(loggedInUser.getId());
        model.addAttribute("stateType", property.getClass().getSimpleName());
        model.addAttribute("currentState", property.getCurrentState().getValue());
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        workflowContainer.setPendingActions(nextAction);
        workflowContainer.setAdditionalRule(EXEMPTION);
        prepareWorkflow(model, property, workflowContainer);
        if (property.getState() != null)
            endorsementRequired = propertyTaxCommonUtils.getEndorsementGenerate(securityUtils.getCurrentUser().getId(),
                    property.getCurrentState());
        model.addAttribute("endorsementRequired", endorsementRequired);
        model.addAttribute("ownersName", property.getBasicProperty().getFullOwnerName());
        model.addAttribute("applicationNo", property.getApplicationNo());
        model.addAttribute("endorsementNotices", propertyTaxCommonUtils.getEndorsementNotices(property.getApplicationNo()));
        model.addAttribute("userDesignationList", userDesignationList);
        model.addAttribute("designation", COMMISSIONER_DESGN);
        model.addAttribute("isExempted", isExempted);
        model.addAttribute("pendingActions", nextAction);
        model.addAttribute("additionalRule", EXEMPTION);
        model.addAttribute("isAlert", false);
        model.addAttribute(CHOULTRY_DOC, "");
        model.addAttribute(WORSHIP_DOC, "");
        model.addAttribute(EDUINST_DOC, "");
        model.addAttribute(EXSERVICE_DOC, "");
        model.addAttribute(NGO_DOC, "");
        model.addAttribute("citizenPortalUser", citizenPortalUser);
        model.addAttribute("transactionType", APPLICATION_TYPE_TAX_EXEMTION);
        model.addAttribute("stateAwareId", property.getId());
        model.addAttribute("isReassignEnabled", reassignService.isReassignEnabled());
        final String currentDesignation = taxExemptionService.getLoggedInUserDesignation(
                property.getCurrentState().getOwnerPosition().getId(),
                securityUtils.getCurrentUser());
        model.addAttribute("userDesignation", currentDesignation);
        if (!(currState.endsWith(STATUS_REJECTED) || currState.endsWith(WFLOW_ACTION_NEW)))
            model.addAttribute("currentDesignation", currentDesignation);

        taxExemptionService.addModelAttributes(model, property.getBasicProperty());
        if(property.getTaxExemptedReason() == null)
            property.getTaxExemptionDocuments().clear();
        if (!property.getTaxExemptionDocuments().isEmpty()) {
            property.setTaxExemptionDocumentsProxy(property.getTaxExemptionDocuments());
            if (property.getTaxExemptedReason().getCode().equals(PropertyTaxConstants.EXEMPTION_CHOULTRY)) {
                model.addAttribute(CHOULTRY_DOC, property.getTaxExemptionDocumentsProxy());
            } else if (property.getTaxExemptedReason().getCode().equals(PropertyTaxConstants.EXEMPTION_EDU_INST)) {
                model.addAttribute(EDUINST_DOC, property.getTaxExemptionDocumentsProxy());
            } else if (property.getTaxExemptedReason().getCode().equals(PropertyTaxConstants.EXEMPTION_EXSERVICE)) {
                model.addAttribute(EXSERVICE_DOC, property.getTaxExemptionDocumentsProxy());
            } else if (property.getTaxExemptedReason().getCode()
                    .equals(PropertyTaxConstants.EXEMPTION_PUBLIC_WORSHIP)) {
                model.addAttribute(WORSHIP_DOC, property.getTaxExemptionDocumentsProxy());
            } else {
                model.addAttribute(NGO_DOC, property.getTaxExemptionDocumentsProxy());
            }
        }
        if (property != null && property.getId() != null && property.getState() != null) {
            historyMap = propService.populateHistory(property);
            model.addAttribute("historyMap", historyMap);
            model.addAttribute("state", property.getState());
        }
        model.addAttribute("property", property);
        if (currState.endsWith(WF_STATE_REJECTED) || nextAction.equalsIgnoreCase(WF_STATE_UD_REVENUE_INSPECTOR_APPROVAL_PENDING)
                || currState.endsWith(WFLOW_ACTION_NEW)) {
            model.addAttribute("mode", EDIT);
            return TAX_EXEMPTION_FORM;
        } else {
            if (!property.getTaxExemptionDocuments().isEmpty())
                model.addAttribute("attachedDocuments", property.getTaxExemptionDocuments());  
            model.addAttribute("mode", VIEW);
            return TAX_EXEMPTION_VIEW;
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute final PropertyImpl property, final BindingResult errors,
            final RedirectAttributes redirectAttributes, final HttpServletRequest request, final Model model,
            @RequestParam final String workFlowAction) {

        final Character status = STATUS_WORKFLOW;
        Long approvalPosition = 0l;
        String approvalComent = "";
        String workFlowAct = workFlowAction;
        final Property oldProperty = property.getBasicProperty().getActiveProperty();

        if (request.getParameter("approvalComent") != null)
            approvalComent = request.getParameter("approvalComent");
        if (request.getParameter("workFlowAction") != null)
            workFlowAct = request.getParameter("workFlowAction");
        if (request.getParameter(APPROVAL_POSITION) != null && !request.getParameter(APPROVAL_POSITION).isEmpty())
            approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));

        if (workFlowAct.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE)) {
            property.setStatus(STATUS_ISACTIVE);
            oldProperty.setStatus(STATUS_ISHISTORY);
        }
        if (workFlowAct.equalsIgnoreCase(WFLOW_ACTION_STEP_NOTICE_GENERATE)
                || WFLOW_ACTION_STEP_PREVIEW.equalsIgnoreCase(workFlowAct)
                || WFLOW_ACTION_STEP_SIGN.equalsIgnoreCase(workFlowAct))
            if (property.getTaxExemptedReason() == null)
                return "redirect:/notice/propertyTaxNotice-generateNotice.action?basicPropId="
                        + property.getBasicProperty().getId() + "&noticeType="
                        + PropertyTaxConstants.NOTICE_TYPE_SPECIAL_NOTICE + "&noticeMode="
                        + PropertyTaxConstants.APPLICATION_TYPE_TAX_EXEMTION + "&actionType=" + workFlowAct;
            else
                return "redirect:/notice/propertyTaxNotice-generateExemptionNotice.action?basicPropId="
                        + property.getBasicProperty().getId() + "&noticeType=" + NOTICE_TYPE_EXEMPTION + "&noticeMode="
                        + NOTICE_TYPE_EXEMPTION + "&actionType=" + workFlowAct;
        else
            return wfApproveReject(property, request, model, status, approvalPosition, approvalComent,
                    workFlowAct);
    }

    private String wfApproveReject(final Property property, final HttpServletRequest request, final Model model,
            final Character status, final Long approvalPosition, final String approvalComent, final String workFlowAct) {
        final Property oldProperty = property.getBasicProperty().getActiveProperty();
        final Boolean propertyByEmployee = Boolean.valueOf(request.getParameter("propertyByEmployee"));
        String taxExemptedReason;
        if (!workFlowAct.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT))
            if (request.getParameter("mode").equalsIgnoreCase(VIEW))
                taxExemptionService.updateProperty(property, approvalComent, workFlowAct, approvalPosition,
                        propertyByEmployee, EXEMPTION);
            else if (request.getParameter(TAXEXEMPTIONREASON) != null) {

                taxExemptedReason = request.getParameter(TAXEXEMPTIONREASON);
                taxExemptionService.saveProperty(property, oldProperty, status, approvalComent, workFlowAct,
                        approvalPosition, taxExemptedReason, propertyByEmployee, EXEMPTION);
            }
        String successMessage;
        if (property.getCreatedBy() != null)
            assignmentService.getPrimaryAssignmentForUser(property.getCreatedBy().getId());
        if (workFlowAct.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE))
            successMessage = "Property Exemption approved successfully and forwarded to "
                    + propertyTaxUtil.getApproverUserName(((PropertyImpl) property).getState().getOwnerPosition().getId())
                    + " with assessment number "
                    + property.getBasicProperty().getUpicNo();
        else if (workFlowAct.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT))
            successMessage = wFReject(property, request, status, approvalPosition, approvalComent,
                    workFlowAct);
        else
            successMessage = "Successfully forwarded to " + propertyTaxUtil.getApproverUserName(approvalPosition)
                    + " with application number " + property.getApplicationNo();

        model.addAttribute(SUCCESSMESSAGE, successMessage);
        return TAX_EXEMPTION_SUCCESS;
    }

    private String wFReject(final Property property, final HttpServletRequest request, final Character status,
            final Long approvalPosition, final String approvalComent, final String workFlowAct) {
        final Boolean propertyByEmployee = Boolean.valueOf(request.getParameter("propertyByEmployee"));
        String taxExemptedReason = null;
        final Property oldProperty = property.getBasicProperty().getActiveProperty();
        String successMessage = null;
        Assignment assignment = null;
        final PropertyImpl propertyImpl = (PropertyImpl) property;
        boolean isNotToReject = true;
        final User user = securityUtils.getCurrentUser();
        String loggedInUserDesignation;
        loggedInUserDesignation = loggedInUserDesignation(propertyImpl, user);
        if (loggedInUserDesignation != null && isRoOrCommissioner(loggedInUserDesignation))
            assignment = taxExemptionService.getUserAssignmentOnReject(loggedInUserDesignation, (PropertyImpl) property);
        if (loggedInUserDesignation != null && !isRoOrCommissioner(loggedInUserDesignation))
            assignment = taxExemptionService.getWfInitiator((PropertyImpl) property);
        if (assignment != null && assignment.getId() != null) {
            if (request.getParameter("mode").equalsIgnoreCase(VIEW))
                taxExemptionService.updateProperty(property, approvalComent, workFlowAct, approvalPosition,
                        propertyByEmployee, EXEMPTION);
            else{
                taxExemptedReason = request.getParameter(TAXEXEMPTIONREASON);
            taxExemptionService.saveProperty(property, oldProperty, status, approvalComent, workFlowAct,
                    approvalPosition, taxExemptedReason, propertyByEmployee, EXEMPTION);
            }

            successMessage = "Property Exemption rejected successfully and forwarded to "
                    + assignment.getEmployee().getName().concat("~").concat(assignment.getPosition().getName())
                    + " with application number "
                    + property.getApplicationNo();
            isNotToReject = false;
        }
        if (isNotToReject)
            successMessage = PROPERTY_MODIFY_REJECT_FAILURE+property.getBasicProperty().getUpicNo();
        return successMessage;
    }

    private String loggedInUserDesignation(final PropertyImpl propertyImpl, final User user) {
        String loggedInUserDesignation = null;
        List<Assignment> loggedInUserAssign;
        if (propertyImpl.getState() != null) {
            loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    propertyImpl.getCurrentState().getOwnerPosition().getId(), user.getId(), new Date());
            if(!loggedInUserAssign.isEmpty())
            loggedInUserDesignation = loggedInUserAssign.get(0).getDesignation().getName();
        }
        return loggedInUserDesignation;
    }

    private boolean isRoOrCommissioner(final String loggedInUserDesignation) {
        boolean isany;
        if (!REVENUE_OFFICER_DESGN.equalsIgnoreCase(loggedInUserDesignation))
            isany = isCommissioner(loggedInUserDesignation);
        else
            isany = true;
        return isany;
    }

    private boolean isCommissioner(final String loggedInUserDesignation) {
        boolean isanyone;
        if (!ASSISTANT_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
                || !ADDITIONAL_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
            isanyone = isDeputyOrAbove(loggedInUserDesignation);
        else
            isanyone = true;
        return isanyone;
    }

    private boolean isDeputyOrAbove(final String loggedInUserDesignation) {
        boolean isanyone = false;
        if (DEPUTY_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation)
                || COMMISSIONER_DESGN.equalsIgnoreCase(loggedInUserDesignation)
                || ZONAL_COMMISSIONER_DESIGN.equalsIgnoreCase(loggedInUserDesignation))
            isanyone = true;
        return isanyone;
    }
}
