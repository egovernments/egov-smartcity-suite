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
package org.egov.works.web.controller.contractoradvance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.service.ChartOfAccountsService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationException;
import org.egov.model.advance.EgAdvanceRequisitionDetails;
import org.egov.model.bills.EgBillregister;
import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition;
import org.egov.works.contractoradvance.service.ContractorAdvanceService;
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/contractoradvance")
public class UpdateContractorAdvanceController extends GenericWorkFlowController {

    @Autowired
    private ContractorAdvanceService contractorAdvanceService;

    @Autowired
    private ChartOfAccountsService chartOfAccountsService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    @ModelAttribute
    public ContractorAdvanceRequisition getContractorAdvanceRequisition(
            @PathVariable final String contractorAdvanceRequisitionId) {
        final ContractorAdvanceRequisition contractorAdvanceRequisition = contractorAdvanceService
                .getContractorAdvanceRequisitionById(Long.parseLong(contractorAdvanceRequisitionId));
        return contractorAdvanceRequisition;
    }

    @RequestMapping(value = "/update/{contractorAdvanceRequisitionId}", method = RequestMethod.GET)
    public String updateContractorAdvance(final Model model, @PathVariable final String contractorAdvanceRequisitionId,
            final HttpServletRequest request) throws ApplicationException {
        final ContractorAdvanceRequisition contractorAdvanceRequisition = getContractorAdvanceRequisition(
                contractorAdvanceRequisitionId);
        setDropDownValues(model);
        model.addAttribute("defaultDepartmentId", worksUtils.getDefaultDepartmentId());
        return loadViewData(model, request, contractorAdvanceRequisition);
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("debitAccounts",
                chartOfAccountsService.getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_ADVANCE_PURPOSE));
        model.addAttribute("creditAccounts",
                chartOfAccountsService.getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_NETPAYABLE_PURPOSE));
    }

    private String loadViewData(final Model model, final HttpServletRequest request,
            final ContractorAdvanceRequisition contractorAdvanceRequisition) {
        model.addAttribute("stateType", contractorAdvanceRequisition.getClass().getSimpleName());
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        if (contractorAdvanceRequisition.getState() != null) {
            model.addAttribute("currentState", contractorAdvanceRequisition.getState().getValue());
            workflowContainer.setPendingActions(contractorAdvanceRequisition.getState().getNextAction());
            model.addAttribute("pendingActions", contractorAdvanceRequisition.getState().getNextAction());
            model.addAttribute("amountRule", contractorAdvanceRequisition.getAdvanceRequisitionAmount());
        }

        final Double advancePaidTillNow = contractorAdvanceService.getTotalAdvancePaid(
                contractorAdvanceRequisition.getId() == null ? -1L : contractorAdvanceRequisition.getId(),
                contractorAdvanceRequisition.getWorkOrderEstimate().getId(),
                ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.APPROVED.toString());
        final Double totalPartBillsAmount = contractorBillRegisterService.getTotalPartBillsAmount(
                Long.valueOf(contractorAdvanceRequisition.getWorkOrderEstimate().getId()),
                ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CANCELLED.toString(),
                BillTypes.Part_Bill.toString());
        model.addAttribute("advancePaidTillNow", advancePaidTillNow);
        model.addAttribute("totalPartBillsAmount", totalPartBillsAmount);

        workflowContainer.setAmountRule(contractorAdvanceRequisition.getAdvanceRequisitionAmount());
        prepareWorkflow(model, contractorAdvanceRequisition, workflowContainer);
        if (contractorAdvanceRequisition.getState() != null
                && contractorAdvanceRequisition.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            model.addAttribute(WorksConstants.MODE, WorksConstants.EDIT);
        else
            model.addAttribute(WorksConstants.MODE, WorksConstants.VIEW);
        model.addAttribute("workflowHistory", worksUtils.getHistory(contractorAdvanceRequisition.getState(),
                contractorAdvanceRequisition.getStateHistory()));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        model.addAttribute("approvalDesignation", request.getParameter("approvalDesignation"));
        model.addAttribute("approvalPosition", request.getParameter("approvalPosition"));
        model.addAttribute("workOrderEstimate", contractorAdvanceRequisition.getWorkOrderEstimate());

        final ContractorAdvanceRequisition newContractorAdvanceRequisition = getContractorAdvanceDocuments(
                contractorAdvanceRequisition);
        model.addAttribute("contractorAdvanceRequisition", newContractorAdvanceRequisition);
        model.addAttribute("documentDetails", contractorAdvanceRequisition.getDocumentDetails());

        return "contractorAdvance-update";
    }

    private ContractorAdvanceRequisition getContractorAdvanceDocuments(
            final ContractorAdvanceRequisition contractorAdvanceRequisition) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(contractorAdvanceRequisition.getId(),
                WorksConstants.CONTRACTOR_ADVANCE);
        contractorAdvanceRequisition.setDocumentDetails(documentDetailsList);
        return contractorAdvanceRequisition;
    }

    @RequestMapping(value = "/update/{contractorAdvanceRequisitionId}", method = RequestMethod.POST)
    public String update(
            @ModelAttribute("contractorAdvanceRequisition") final ContractorAdvanceRequisition contractorAdvanceRequisition,
            final BindingResult errors, final RedirectAttributes redirectAttributes, final Model model,
            final HttpServletRequest request, @RequestParam("file") final MultipartFile[] files)
            throws ApplicationException, IOException {

        String mode = "";
        String workFlowAction = "";
        ContractorAdvanceRequisition updatedContractorAdvanceRequisition = null;

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

        if (contractorAdvanceRequisition.getStatus().getCode()
                .equals(ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.REJECTED.toString())
                && WorksConstants.FORWARD_ACTION.equals(workFlowAction) && WorksConstants.EDIT.equals(mode)) {
            contractorAdvanceService.validateInput(contractorAdvanceRequisition, errors);
            for (final EgAdvanceRequisitionDetails details : contractorAdvanceRequisition.getEgAdvanceReqDetailses())
                contractorAdvanceService.getEgAdvanceRequisitionDetails(contractorAdvanceRequisition, details, errors);
        }

        if (workFlowAction.equalsIgnoreCase(WorksConstants.ACTION_APPROVE))
            populateAndValidateAdvanceBill(contractorAdvanceRequisition, errors);

        if (errors.hasErrors()) {
            setDropDownValues(model);
            return loadViewData(model, request, contractorAdvanceRequisition);
        } else {
            if (null != workFlowAction)
                updatedContractorAdvanceRequisition = contractorAdvanceService.updateContractorAdvanceRequisition(
                        contractorAdvanceRequisition, approvalPosition, approvalComment, null, workFlowAction, mode, files);

            redirectAttributes.addFlashAttribute("contractorAdvanceRequisition", updatedContractorAdvanceRequisition);

            final String pathVars = worksUtils.getPathVars(contractorAdvanceRequisition.getStatus(),
                    contractorAdvanceRequisition.getState(), contractorAdvanceRequisition.getId(), approvalPosition);

            return "redirect:/contractoradvance/contractoradvance-success?pathVars=" + pathVars + "&arfNumber="
                    + contractorAdvanceRequisition.getAdvanceRequisitionNumber();
        }
    }

    private void populateAndValidateAdvanceBill(final ContractorAdvanceRequisition contractorAdvanceRequisition,
            final BindingResult errors) {
        contractorAdvanceRequisition.setApprovedDate(new Date());
        final EgBillregister egBillregister = new EgBillregister();
        contractorAdvanceService.generateAdvanceBills(contractorAdvanceRequisition, egBillregister, errors);
        final List<String> errorMessages = new ArrayList<>();
        contractorAdvanceService.validateLedgerAndSubledger(egBillregister, errorMessages);
        if (errorMessages.size() == 0)
            contractorAdvanceRequisition.getEgAdvanceReqMises().setEgBillregister(egBillregister);
        else
            for (final String error : errorMessages)
                errors.reject("", error);
    }
}
