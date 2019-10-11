
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
package org.egov.ptis.web.controller.transactions.writeOff;

import static org.egov.ptis.constants.PropertyTaxConstants.APPROVAL_COMMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.APPROVAL_POSITION;
import static org.egov.ptis.constants.PropertyTaxConstants.NOTICE_TYPE_WRITEOFFROCEEDINGS;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.SUCCESS_MSG;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_APPROVE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_NOTICE_GENERATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_PREVIEW;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_REJECT;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_STEP_SIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_ACTION;
import static org.egov.ptis.constants.PropertyTaxConstants.WF_STATE_REJECTED;
import static org.egov.ptis.constants.PropertyTaxConstants.WO_FORM;
import static org.egov.ptis.constants.PropertyTaxConstants.WO_SUCCESS_FORM;
import static org.egov.ptis.constants.PropertyTaxConstants.WO_VIEW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.Installment;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.dao.property.PropertyMutationMasterHibDAO;
import org.egov.ptis.domain.entity.enums.TransactionType;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.DocumentType;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.egov.ptis.domain.entity.property.WriteOff;
import org.egov.ptis.domain.entity.property.WriteOffReasons;
import org.egov.ptis.domain.repository.writeOff.WriteOffReasonRepository;
import org.egov.ptis.domain.service.writeOff.WriteOffService;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/writeoff/update/{id}")
public class UpdateWriteOffController extends GenericWorkFlowController {
    private static final String WRITOFF_DOC = "attachedDocuments";
    @Autowired
    private WriteOffService writeOffService;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private PropertyTaxUtil propertyTaxUtil;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;
    @Autowired
    private PropertyMutationMasterHibDAO propertyMutationMasterHibDAO;
    @Autowired
    private WriteOffReasonRepository writeOffReasonRepository;

    @ModelAttribute
    public WriteOff writeOffModel(@PathVariable Long id) {
        return writeOffService.getWriteOffById(id);

    }

    @ModelAttribute("documentsList")
    public List<DocumentType> documentsList(@ModelAttribute final WriteOff writeOff) {
        return writeOffService.getDocuments(TransactionType.WRITEOFF);
    }

    @GetMapping
    public String view(@ModelAttribute WriteOff writeOff, Model model, HttpServletRequest request) {
        String target = null;
        String currState = writeOff.getState().getValue();
        BasicProperty basicProperty = writeOff.getBasicProperty();
        String currentDesg = writeOffService.getLoggedInUserDesignation(
                writeOff.getCurrentState().getOwnerPosition().getId(), securityUtils.getCurrentUser());
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        List<Installment> installmentList = propertyTaxUtil.getInstallments(basicProperty.getActiveProperty());
        model.addAttribute(WRITOFF_DOC, "");
        if (!currState.endsWith(WF_STATE_REJECTED)) {
            writeOffService.addDemandDetails(writeOff);
            writeOffService.addModelAttributesupdate(model, writeOff);
            writeOffService.addModelAttributes(model, basicProperty.getUpicNo(), request, installmentList);
            if (!writeOff.getDocuments().isEmpty())
                writeOff.setWriteoffDocumentsProxy(writeOff.getDocuments());
            model.addAttribute(WRITOFF_DOC, writeOff.getWriteoffDocumentsProxy());
            model.addAttribute("demandDetailList", writeOff.getDemandDetailBeanList());
            model.addAttribute("currentDesignation", currentDesg);
            workflowContainer.setCurrentDesignation(currentDesg);
            prepareWorkflow(model, writeOff, workflowContainer);
            target = WO_VIEW;
        } else {
            writeOffService.addDemandDetails(writeOff);
            writeOffService.addModelAttributesupdate(model, writeOff);
            writeOffService.addModelAttributes(model, basicProperty.getUpicNo(), request, installmentList);
            if (!writeOff.getDocuments().isEmpty()) {
                writeOff.setWriteoffDocumentsProxy(writeOff.getDocuments());
                model.addAttribute(WRITOFF_DOC, writeOff.getWriteoffDocumentsProxy());
            }
            model.addAttribute("dmndDetails", writeOff.getDemandDetailBeanList());
            model.addAttribute("currentDesignation", currentDesg);
            workflowContainer.setCurrentDesignation(currentDesg);
            model.addAttribute("demandDetailList", writeOff.getDemandDetailBeanList());
            prepareWorkflow(model, writeOff, new WorkflowContainer());
            target = WO_FORM;
        }
        return target;

    }

    @PostMapping
    public String wfApproveReject(@ModelAttribute("writeOff") WriteOff writeOff, BindingResult resultBinder,
            final RedirectAttributes redirectAttributes, final Model model, final HttpServletRequest request,
            @RequestParam String workFlowAction) {
        Long approvalPosition = 0l;
        String approvalComent = "";
        String target = "";
        Map<String, String> errorMessages = new HashMap<>();
        new ArrayList<>();
        User loggedInUser = securityUtils.getCurrentUser();
        String successMessage = null;
        String currentDesg = writeOffService.getLoggedInUserDesignation(
                writeOff.getCurrentState().getOwnerPosition().getId(), securityUtils.getCurrentUser());
        propertyTaxCommonUtils.setSourceOfProperty(loggedInUser, false);

        if (request.getParameterValues("checkbox") != null && request.getParameterValues("checkbox").length > 0)
            writeOff.setPropertyDeactivateFlag(true);
        else
            writeOff.setPropertyDeactivateFlag(false);
        String value = request.getParameter("propertyDeactivateFlag");
        if (Boolean.valueOf(value))
            writeOff.setPropertyDeactivateFlag(Boolean.valueOf(value));
        if (request.getParameter(APPROVAL_COMMENT) != null)
            approvalComent = request.getParameter(APPROVAL_COMMENT);
        if (request.getParameter(WF_ACTION) != null)
            workFlowAction = request.getParameter(WF_ACTION);
        if (request.getParameter(APPROVAL_POSITION) != null && !request.getParameter(APPROVAL_POSITION).isEmpty())
            approvalPosition = Long.valueOf(request.getParameter(APPROVAL_POSITION));
        if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_NOTICE_GENERATE)
                || WFLOW_ACTION_STEP_PREVIEW.equalsIgnoreCase(workFlowAction)
                || WFLOW_ACTION_STEP_SIGN.equalsIgnoreCase(workFlowAction))
            return "redirect:/writeoff/generatenotice?assessmentno=" + writeOff.getBasicProperty().getUpicNo()
                    + "&noticeType=" + NOTICE_TYPE_WRITEOFFROCEEDINGS + "&noticeMode="
                    + NOTICE_TYPE_WRITEOFFROCEEDINGS + "&actionType=" + workFlowAction + "&approverUser="
                    + loggedInUser.getName();
        else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_APPROVE)) {
            writeOffService.saveWriteOff(writeOff, approvalPosition, approvalComent, null, workFlowAction);

            successMessage = "Write Off is approved Successfully by : "
                    + propertyTaxUtil.getApproverUserName(writeOff.getState().getOwnerPosition().getId())
                    + " with application number : " + writeOff.getApplicationNumber();
            model.addAttribute(SUCCESS_MSG, successMessage);

            target = WO_SUCCESS_FORM;
        } else if (workFlowAction.equalsIgnoreCase(WFLOW_ACTION_STEP_REJECT)) {

            writeOffService.saveWriteOff(writeOff, approvalPosition, approvalComent, null, workFlowAction);
            if (currentDesg.contains(REVENUE_OFFICER_DESGN))
                successMessage = "Write Off is completely rejected by :"
                        + propertyTaxUtil.getApproverUserName(writeOff.getState().getOwnerPosition().getId())
                        + " with application number " + writeOff.getApplicationNumber();
            else
                successMessage = "Write Off rejected and forwarded to :"
                        + propertyTaxUtil.getApproverUserName(writeOff.getState().getOwnerPosition().getId())
                        + " with application number " + writeOff.getApplicationNumber();

            model.addAttribute(SUCCESS_MSG, successMessage);

            target = WO_SUCCESS_FORM;
        } else {
            if (currentDesg.contains(REVENUE_OFFICER_DESGN)) {
                if (writeOff.getWriteOffType() != null && !writeOff.getWriteOffType().getCode().isEmpty()) {
                    final PropertyMutationMaster propMutMstr = propertyMutationMasterHibDAO
                            .getPropertyMutationMasterByCode(writeOff.getWriteOffType().getCode());
                    writeOff.setWriteOffType(propMutMstr);
                }
                if (writeOff.getWriteOffReasons() != null && !writeOff.getWriteOffReasons().getName().isEmpty()) {
                    final WriteOffReasons writeOffReasons = writeOffReasonRepository
                            .findByCode(writeOff.getWriteOffReasons().getCode());
                    writeOff.setWriteOffReasons(writeOffReasons);
                }
                if (!resultBinder.hasErrors())
                    resultBinder = writeOffService.validate(writeOff, resultBinder);
                errorMessages = writeOffService.displayValidation(writeOff, request);
                if (resultBinder != null && resultBinder.hasErrors() || !errorMessages.isEmpty()) {
                    target = writeOffService.displayErrors(writeOff, model, errorMessages, request);
                    return target;
                }
                writeOffService.processAndStoreApplicationDocuments(writeOff);
                writeOffService.updateDemandDetailVariation(writeOff);
            }
            writeOffService.saveWriteOff(writeOff, approvalPosition, approvalComent, null, workFlowAction);
            successMessage = "write Off Saved Successfully in the System and forwarded to : "
                    + propertyTaxUtil.getApproverUserName(writeOff.getState().getOwnerPosition().getId())
                    + " with application number " + writeOff.getApplicationNumber();
            model.addAttribute(SUCCESS_MSG, successMessage);

            target = WO_SUCCESS_FORM;
        }
        return target;
    }
}
