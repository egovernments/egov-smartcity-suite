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
package org.egov.works.web.controller.contractorbill;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.service.ChartOfAccountsService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.ApplicationConstant;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.bills.EgBilldetails;
import org.egov.pims.commons.Position;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus;
import org.egov.works.contractoradvance.service.ContractorAdvanceService;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.service.MBForCancelledBillService;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.models.tender.OfflineStatus;
import org.egov.works.offlinestatus.service.OfflineStatusService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrder.OfflineStatuses;
import org.egov.works.workorder.entity.WorkOrderEstimate;
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
@RequestMapping(value = "/contractorbill")
public class UpdateContractorBillController extends BaseContractorBillController {
    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    @Autowired
    private MBHeaderService mbHeaderService;
    
    @Autowired
    private WorksApplicationProperties worksApplicationProperties;

    @Autowired
    private MBForCancelledBillService mbForCancelledBillService;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private OfflineStatusService offlineStatusService;

    @Autowired
    private ContractorAdvanceService contractorAdvanceService;

    @Autowired
    private ChartOfAccountsService chartOfAccountsService;

    @Autowired
    private CityService cityService;

    @ModelAttribute
    public ContractorBillRegister getContractorBillRegister(@PathVariable final String contractorBillRegisterId) {
        final ContractorBillRegister contractorBillRegister = contractorBillRegisterService
                .getContractorBillById(Long.parseLong(contractorBillRegisterId));
        return contractorBillRegister;
    }

    @RequestMapping(value = "/update/{contractorBillRegisterId}", method = RequestMethod.GET)
    public String updateContractorBillRegister(final Model model, @PathVariable final String contractorBillRegisterId,
            final HttpServletRequest request) throws ApplicationException {
        final ContractorBillRegister contractorBillRegister = getContractorBillRegister(contractorBillRegisterId);
        // if
        // (contractorBillRegister.getStatus().getCode().equals(ContractorBillRegister.BillStatus.REJECTED.toString()))
        setDropDownValues(model);
        model.addAttribute("createdbybydesignation",
                worksUtils.getUserDesignation(contractorBillRegister.getCreatedBy()));
        final OfflineStatus offlineStatus = offlineStatusService.getOfflineStatusByObjectIdAndObjectTypeAndStatus(
                contractorBillRegister.getWorkOrderEstimate().getWorkOrder().getId(), WorksConstants.WORKORDER,
                OfflineStatuses.WORK_COMMENCED.toString().toUpperCase());
        model.addAttribute("offlinestatusWorkCommencedDate",
                offlineStatus != null ? offlineStatus.getStatusDate() : "");
        return loadViewData(model, request, contractorBillRegister);
    }

    @RequestMapping(value = "/update/{contractorBillRegisterId}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("contractorBillRegister") ContractorBillRegister contractorBillRegister,
            final BindingResult errors, final RedirectAttributes redirectAttributes, final Model model,
            final HttpServletRequest request, @RequestParam("file") final MultipartFile[] files)
            throws ApplicationException, IOException {

        String mode = StringUtils.EMPTY;
        String workFlowAction = StringUtils.EMPTY;
        ContractorBillRegister updatedContractorBillRegister = null;
        Boolean isEditable = false;
        String additionalRule = StringUtils.EMPTY;
        if (request.getParameter(WorksConstants.ADDITIONAL_RULE) != null)
            additionalRule = request.getParameter(WorksConstants.ADDITIONAL_RULE);

        if (request.getParameter(WorksConstants.MODE) != null)
            mode = request.getParameter(WorksConstants.MODE);

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
            approvalPosition = contractorBillRegisterService.getApprovalPositionByMatrixDesignation(
                    contractorBillRegister, approvalPosition, null, mode, workFlowAction);

        if ((approvalPosition == null || approvalPosition.equals(Long.valueOf(0)))
                && request.getParameter("approvalPosition") != null
                && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        if (contractorBillRegister.getState() != null) {
            final Position position = contractorBillRegister.getState().getOwnerPosition();
            final List<AppConfigValues> billEditDesignations = appConfigValuesService.getConfigValuesByModuleAndKey(
                    WorksConstants.EGF_MODULE_NAME, WorksConstants.BILL_EDIT_DESIGNATIONS);
            if (position != null && !billEditDesignations.isEmpty() && (billEditDesignations.get(0).getValue()
                    .equalsIgnoreCase(position.getDeptDesig().getDesignation().getName())
                    || billEditDesignations.get(1).getValue()
                            .equalsIgnoreCase(position.getDeptDesig().getDesignation().getCode())))
                isEditable = true;
        }

        try {
            if (contractorBillRegister.getStatus().getCode()
                    .equals(ContractorBillRegister.BillStatus.REJECTED.toString())
                    && workFlowAction.equals(WorksConstants.FORWARD_ACTION) || isEditable)
                contractorBillRegisterService.checkBudgetAndGenerateBANumber(contractorBillRegister);
        } catch (final ValidationException e) {
            // TODO: Used ApplicationRuntimeException for time being since there
            // is issue in session after
            // checkBudgetAndGenerateBANumber API call. Needs to replace with
            // errors.reject
            throw new ApplicationRuntimeException("error.contractorbill.budgetcheck.insufficient.amount");
            /*
             * for (final ValidationError error : e.getErrors()) { if(error.getMessage().contains("Budget Check failed for ")) {
             * errors.reject(messageSource.getMessage( "error.contractorbill.budgetcheck.insufficient.amount",null,null) +". "
             * +error.getMessage()); } else errors.reject(error.getMessage()); }
             */
        }

        if (contractorBillRegister.getStatus().getCode().equals(ContractorBillRegister.BillStatus.REJECTED.toString())
                && WorksConstants.FORWARD_ACTION.equals(workFlowAction) && WorksConstants.EDIT.equals(mode) || isEditable) {

            validateInput(contractorBillRegister, contractorBillRegister.getWorkOrderEstimate(), errors, request);
            contractorBillRegister.getEgBilldetailes().clear();
            contractorBillRegisterService.mergeDeductionDetails(contractorBillRegister);
            contractorBillRegister = addBillDetails(contractorBillRegister,
                    contractorBillRegister.getWorkOrderEstimate(), errors, request);
            contractorBillRegister.setPassedamount(contractorBillRegister.getBillamount());

            if (!contractorBillRegisterService.checkForDuplicateAccountCodes(contractorBillRegister))
                errors.reject("error.contractorbill.duplicate.accountcodes", "error.contractorbill.duplicate.accountcodes");

            if (!contractorBillRegisterService.validateDuplicateRefundAccountCodes(contractorBillRegister))
                errors.reject("error.contractorbill.duplicate.refund.accountcodes",
                        "error.contractorbill.duplicate.refund.accountcodes");

            contractorBillRegisterService.validateTotalDebitAndCreditAmount(contractorBillRegister, errors);

            contractorBillRegisterService.validateRefundAmount(contractorBillRegister, errors);

            contractorBillRegisterService.validateMileStonePercentage(contractorBillRegister, errors);
        }

        if (errors.hasErrors()) {
            setDropDownValues(model);
            return loadViewData(model, request, contractorBillRegister);
        } else {
            if (null != workFlowAction)
                updatedContractorBillRegister = contractorBillRegisterService.updateContractorBillRegister(
                        contractorBillRegister, approvalPosition, approvalComment, additionalRule, workFlowAction, mode, files);

            redirectAttributes.addFlashAttribute("contractorBillRegister", updatedContractorBillRegister);

            final String pathVars = worksUtils.getPathVars(updatedContractorBillRegister.getStatus(),
                    updatedContractorBillRegister.getState(), updatedContractorBillRegister.getId(), approvalPosition);

            return "redirect:/contractorbill/contractorbill-success?pathVars=" + pathVars + "&billNumber="
                    + updatedContractorBillRegister.getBillnumber();
        }
    }

    private void validateInput(final ContractorBillRegister contractorBillRegister,
            final WorkOrderEstimate workOrderEstimate, final BindingResult resultBinder,
            final HttpServletRequest request) {

        BigDecimal totalBillAmountIncludingCurrentBill = contractorBillRegister.getBillamount();
        final BigDecimal totalBillAmount = contractorBillRegisterService
                .getTotalBillAmountByWorkOrderAndNotContractorBillRegister(
                        contractorBillRegister.getWorkOrderEstimate(), contractorBillRegister.getId());
        final AbstractEstimate ae = workOrderEstimate.getEstimate();
        if (totalBillAmount != null)
            totalBillAmountIncludingCurrentBill = totalBillAmountIncludingCurrentBill.add(totalBillAmount);

        if (ae.isBillsCreated() && ae.getGrossAmountBilled() != null)
            totalBillAmountIncludingCurrentBill = totalBillAmountIncludingCurrentBill.add(ae.getGrossAmountBilled());
        if (totalBillAmountIncludingCurrentBill.doubleValue() > workOrderEstimate.getWorkOrder().getWorkOrderAmount())

            resultBinder.reject("error.contractorbill.totalbillamount.exceeds.workorderamount",
                    new String[] { String.valueOf(totalBillAmountIncludingCurrentBill),
                            String.valueOf(workOrderEstimate.getWorkOrder().getWorkOrderAmount()) },
                    null);

        if (StringUtils.isBlank(contractorBillRegister.getBilltype()))
            resultBinder.rejectValue("billtype", "error.billtype.required");
        if (contractorBillRegister.getEgBillregistermis() != null
                && contractorBillRegister.getEgBillregistermis().getPartyBillDate() != null
                && contractorBillRegister.getEgBillregistermis().getPartyBillDate()
                        .before(workOrderEstimate.getWorkOrder().getWorkOrderDate()))
            resultBinder.rejectValue("egBillregistermis.partyBillDate",
                    "error.validate.partybilldate.lessthan.loadate");

        if (contractorBillRegister.getWorkOrderEstimate() != null
                && contractorBillRegister.getWorkOrderEstimate().getWorkOrderActivities().isEmpty()
                && contractorBillRegister.getMbHeader() != null) {
            if (StringUtils.isBlank(contractorBillRegister.getMbHeader().getMbRefNo()))
                resultBinder.rejectValue("mbHeader.mbRefNo", "error.mbrefno.required");

            if (contractorBillRegister.getMbHeader().getMbDate() == null)
                resultBinder.rejectValue("mbHeader.mbDate", "error.mbdate.required");

            if (contractorBillRegister.getMbHeader().getFromPageNo() == null)
                resultBinder.rejectValue("mbHeader.fromPageNo", "error.frompageno.required");

            if (contractorBillRegister.getMbHeader().getToPageNo() == null)
                resultBinder.rejectValue("mbHeader.toPageNo", "error.topageno.required");

            if (contractorBillRegister.getMbHeader().getFromPageNo() == 0
                    || contractorBillRegister.getMbHeader().getToPageNo() == 0)
                resultBinder.reject("error.validate.mb.pagenumbers.zero", "error.validate.mb.pagenumbers.zero");

            if (contractorBillRegister.getMbHeader().getFromPageNo() != null
                    && contractorBillRegister.getMbHeader().getToPageNo() != null && contractorBillRegister
                            .getMbHeader().getFromPageNo() > contractorBillRegister.getMbHeader().getToPageNo())
                resultBinder.reject("error.validate.mb.frompagenumber.greaterthan.topagenumber",
                        "error.validate.mb.frompagenumber.greaterthan.topagenumber");

            if (contractorBillRegister.getMbHeader().getMbDate() != null && contractorBillRegister.getMbHeader()
                    .getMbDate().before(workOrderEstimate.getWorkOrder().getWorkOrderDate()))

                resultBinder.rejectValue("mbHeader.mbDate", "error.validate.mbdate.lessthan.loadate");

            if (contractorBillRegister.getMbHeader().getMbDate() != null
                    && contractorBillRegister.getBilldate()
                            .before(contractorBillRegister.getMbHeader().getMbDate()))
                resultBinder.rejectValue("mbHeader.mbDate", "error.billdate.mbdate");
        }

        if (StringUtils.isBlank(request.getParameter("netPayableAccountCode")))
            resultBinder.reject("error.netpayable.accountcode.required", "error.netpayable.accountcode.required");
        if (StringUtils.isBlank(request.getParameter("netPayableAmount"))
                || Double.valueOf(request.getParameter("netPayableAmount").toString()) <= 0)
            resultBinder.reject("error.netpayable.amount.required", "error.netpayable.amount.required");

        if (contractorBillRegister.getBilltype().equals(BillTypes.Final_Bill.toString())
                && contractorBillRegister.getWorkOrderEstimate().getWorkCompletionDate() == null)
            resultBinder.rejectValue("workOrderEstimate.workCompletionDate", "error.workcompletiondate.required");

        final Date currentDate = new Date();
        final Date workCompletionDate = contractorBillRegister.getWorkOrderEstimate().getWorkCompletionDate();
        if (workCompletionDate != null) {
            final OfflineStatus offlineStatus = offlineStatusService.getOfflineStatusByObjectIdAndObjectTypeAndStatus(
                    workOrderEstimate.getWorkOrder().getId(), WorksConstants.WORKORDER,
                    OfflineStatuses.WORK_COMMENCED.toString().toUpperCase());
            if (workCompletionDate.after(currentDate))
                resultBinder.rejectValue("workOrderEstimate.workCompletionDate", "error.workcompletiondate.futuredate");
            if (offlineStatus != null) {
                if (workCompletionDate.before(offlineStatus.getStatusDate()))
                    resultBinder.rejectValue("workOrderEstimate.workCompletionDate",
                            "error.workcompletiondate.workcommenceddate");
            } else if (workCompletionDate
                    .before(contractorBillRegister.getWorkOrderEstimate().getWorkOrder().getWorkOrderDate()))
                resultBinder.rejectValue("workOrderEstimate.workCompletionDate",
                        "error.workcompletiondate.workorderdate");
            if (workCompletionDate.after(contractorBillRegister.getBilldate()))
                resultBinder.rejectValue("workOrderEstimate.workCompletionDate", "error.workcompletiondate.billdate");
        }

        final MBHeader mBHeader = mbHeaderService.getLatestMBHeaderToValidateBillDate(
                contractorBillRegister.getWorkOrderEstimate().getId(), contractorBillRegister.getBilldate());
        if (mBHeader != null && contractorBillRegister.getBilldate().before(mBHeader.getMbDate()))
            resultBinder.rejectValue("mbHeader.mbDate", "error.billdate.mbdate");

        validateContractorAdvanceDetails(contractorBillRegister, workOrderEstimate, resultBinder);
    }

    private void setDropDownValues(final Model model) {
        final List<CChartOfAccounts> contractorPayableAccountList = chartOfAccountsService
                .getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_NETPAYABLE_PURPOSE);
        final List<CChartOfAccounts> contractorRefundAccountList = chartOfAccountsHibernateDAO
                .getAccountCodeByListOfPurposeName(WorksConstants.CONTRACTOR_REFUND_PURPOSE);
        model.addAttribute("netPayableAccounCodes", contractorPayableAccountList);
        model.addAttribute("statutoryDeductionAccounCodes",
                chartOfAccountsService.getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_DEDUCTIONS_PURPOSE));
        model.addAttribute("retentionMoneyDeductionAccounCodes", chartOfAccountsService
                .getAccountCodeByPurposeName(WorksConstants.RETENTION_MONEY_DEDUCTIONS_PURPOSE));
        model.addAttribute("refundAccounCodes", contractorRefundAccountList);
        model.addAttribute("billTypes", BillTypes.values());
    }

    private String loadViewData(final Model model, final HttpServletRequest request,
            final ContractorBillRegister contractorBillRegister) {
        Boolean isEditable = false;

        model.addAttribute("stateType", contractorBillRegister.getClass().getSimpleName());
        final WorkflowContainer workflowContainer = new WorkflowContainer();
        if (contractorBillRegister.getState() != null) {
            model.addAttribute("currentState", contractorBillRegister.getState().getValue());
            workflowContainer.setPendingActions(contractorBillRegister.getState().getNextAction());
            model.addAttribute("pendingActions", contractorBillRegister.getState().getNextAction());
            model.addAttribute("amountRule", contractorBillRegister.getBillamount());
        }

        if (contractorBillRegister.getState() != null) {
            final Position position = contractorBillRegister.getState().getOwnerPosition();
            final List<AppConfigValues> billEditDesignations = appConfigValuesService.getConfigValuesByModuleAndKey(
                    WorksConstants.EGF_MODULE_NAME, WorksConstants.BILL_EDIT_DESIGNATIONS);
            if (position != null && !billEditDesignations.isEmpty() && (billEditDesignations.get(0).getValue()
                    .equalsIgnoreCase(position.getDeptDesig().getDesignation().getName())
                    || billEditDesignations.get(1).getValue()
                            .equalsIgnoreCase(position.getDeptDesig().getDesignation().getCode())))
                isEditable = true;
        }
        workflowContainer.setAdditionalRule((String) cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
        model.addAttribute(WorksConstants.ADDITIONAL_RULE,
                cityService.cityDataAsMap().get(ApplicationConstant.CITY_CORP_GRADE_KEY));
        workflowContainer.setAmountRule(contractorBillRegister.getBillamount());
        prepareWorkflow(model, contractorBillRegister, workflowContainer);
        if (contractorBillRegister.getState() != null
                && contractorBillRegister.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED) || isEditable)
            model.addAttribute(WorksConstants.MODE, WorksConstants.EDIT);
        else
            model.addAttribute(WorksConstants.MODE, WorksConstants.VIEW);

        model.addAttribute("isBillEditable", isEditable);

        model.addAttribute("billDetailsMap", contractorBillRegisterService.getBillDetailsMap(contractorBillRegister, model));

        model.addAttribute("workflowHistory", worksUtils.getHistory(contractorBillRegister.getState(),
                contractorBillRegister.getStateHistory()));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        model.addAttribute("approvalDesignation", request.getParameter("approvalDesignation"));
        model.addAttribute("approvalPosition", request.getParameter("approvalPosition"));
        model.addAttribute("assetValues", contractorBillRegister.getWorkOrderEstimate().getAssetValues());
        model.addAttribute("workOrderEstimate", contractorBillRegister.getWorkOrderEstimate());
        final List<AppConfigValues> retentionMoneyPerForPartBillApp = appConfigValuesService
                .getConfigValuesByModuleAndKey(WorksConstants.WORKS_MODULE_NAME,
                        WorksConstants.APPCONFIG_KEY_RETENTION_MONEY_PER_FOR_PART_BILL);
        final List<AppConfigValues> retentionMoneyPerForFinalBillApp = appConfigValuesService
                .getConfigValuesByModuleAndKey(WorksConstants.WORKS_MODULE_NAME,
                        WorksConstants.APPCONFIG_KEY_RETENTION_MONEY_PER_FOR_FINAL_BILL);
        model.addAttribute("retentionMoneyPerForPartBill", retentionMoneyPerForPartBillApp.get(0).getValue());
        model.addAttribute("retentionMoneyPerForFinalBill", retentionMoneyPerForFinalBillApp.get(0).getValue());

        final Double advancePaidTillNow = contractorAdvanceService.getTotalAdvanceBillsPaid(
                contractorBillRegister.getWorkOrderEstimate().getId(),
                ContractorAdvanceRequisitionStatus.APPROVED.toString());
        model.addAttribute("advancePaidTillNow", advancePaidTillNow);

        final List<CChartOfAccounts> contractorAdvanceAccountCodes = chartOfAccountsService
                .getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_ADVANCE_PURPOSE);
        final Double advanceAdjustedSoFar = contractorBillRegisterService.getAdvanceAdjustedSoFar(
                contractorBillRegister.getWorkOrderEstimate().getId(),
                contractorBillRegister.getId(), contractorAdvanceAccountCodes);
        model.addAttribute("contractorAdvanceAccountCodes", contractorAdvanceAccountCodes);
        model.addAttribute("advanceAdjustedSoFar", advanceAdjustedSoFar);

        final ContractorBillRegister newcontractorBillRegister = getContractorBillDocuments(contractorBillRegister);
        if (newcontractorBillRegister.getAssetDetailsList() != null
                && !newcontractorBillRegister.getAssetDetailsList().isEmpty())
            model.addAttribute("billAssetValue", newcontractorBillRegister.getAssetDetailsList().get(0));
        model.addAttribute("contractorBillRegister", newcontractorBillRegister);
        model.addAttribute("documentDetails", contractorBillRegister.getDocumentDetails());
        final List<MBHeader> mbHeaders = mbHeaderService.getMBHeadersByContractorBill(newcontractorBillRegister);
        if (mbHeaders != null && !mbHeaders.isEmpty())
            newcontractorBillRegister.setMbHeader(mbHeaders.get(0));
        if (newcontractorBillRegister.getStatus() != null
                && newcontractorBillRegister.getStatus().getCode().equalsIgnoreCase(WorksConstants.CANCELLED_STATUS))
            model.addAttribute("mbDetails", mbForCancelledBillService
                    .getMBHeadersForCancelledBillListByContractorBillRegister(newcontractorBillRegister));
        else
            model.addAttribute("mbDetails", mbHeaders);

        // Set MBHeaders in edit
        model.addAttribute("mbHeaders", mbHeaders);

        contractorBillRegister.setApprovalDepartment(worksUtils.getDefaultDepartmentId());
        model.addAttribute("defaultCutOffDate", worksApplicationProperties.getContractorBillCutOffDate());

        return "contractorBill-update";
    }

    private ContractorBillRegister getContractorBillDocuments(final ContractorBillRegister contractorBillRegister) {
        List<DocumentDetails> documentDetailsList = new ArrayList<DocumentDetails>();
        documentDetailsList = worksUtils.findByObjectIdAndObjectType(contractorBillRegister.getId(),
                WorksConstants.CONTRACTORBILL);
        contractorBillRegister.setDocumentDetails(documentDetailsList);
        return contractorBillRegister;
    }

    @RequestMapping(value = "/view/{contractorBillRegisterId}", method = RequestMethod.GET)
    public String viewContractorBillRegister(final Model model, @PathVariable final String contractorBillRegisterId,
            final HttpServletRequest request) throws ApplicationException {
        final ContractorBillRegister contractorBillRegister = getContractorBillRegister(contractorBillRegisterId);
        loadViewData(model, request, contractorBillRegister);
        model.addAttribute("createdbybydesignation",
                worksUtils.getUserDesignation(contractorBillRegister.getCreatedBy()));
        model.addAttribute("mode", "readOnly");
        model.addAttribute("workflowHistory", worksUtils.getHistory(contractorBillRegister.getState(),
                contractorBillRegister.getStateHistory()));
        return "contractorBill-view";
    }

    private ContractorBillRegister addBillDetails(final ContractorBillRegister contractorBillRegister,
            final WorkOrderEstimate workOrderEstimate, final BindingResult resultBinder,
            final HttpServletRequest request) {

        if (contractorBillRegister.getBillDetailes() == null || contractorBillRegister.getBillDetailes().isEmpty())
            resultBinder.reject("error.contractorbill.accountdetails.required",
                    "error.contractorbill.accountdetails.required");
        for (final EgBilldetails egBilldetails : contractorBillRegister.getBillDetailes())
            if (!contractorBillRegister.getEgBilldetailes().isEmpty()
                    && contractorBillRegister.getEgBilldetailes().size() == 1) {
                for (final EgBilldetails refundBill : contractorBillRegister.getRefundBillDetails())
                    if (refundBill.getGlcodeid() != null)
                        contractorBillRegister.addEgBilldetailes(contractorBillRegisterService.getBillDetails(
                                contractorBillRegister, refundBill, workOrderEstimate, resultBinder, request));
                if (egBilldetails.getGlcodeid() != null)
                    contractorBillRegister.addEgBilldetailes(contractorBillRegisterService.getBillDetails(
                            contractorBillRegister, egBilldetails, workOrderEstimate, resultBinder, request));
            } else if (egBilldetails.getGlcodeid() != null)
                contractorBillRegister.addEgBilldetailes(contractorBillRegisterService.getBillDetails(
                        contractorBillRegister, egBilldetails, workOrderEstimate, resultBinder, request));

        contractorBillRegisterService.validateZeroCreditAndDebitAmount(contractorBillRegister, resultBinder);

        final String netPayableAccountCodeId = request.getParameter("netPayableAccountCode");
        final String netPayableAmount = request.getParameter("netPayableAmount");
        if (StringUtils.isNotBlank(netPayableAccountCodeId)
                && StringUtils.isNotBlank(netPayableAccountCodeId)
                && StringUtils.isNotBlank(netPayableAmount)) {
            final EgBilldetails billdetails = new EgBilldetails();
            // billdetails.setId(new Integer(netPayableAccountId));
            billdetails.setGlcodeid(new BigDecimal(netPayableAccountCodeId));
            billdetails.setCreditamount(new BigDecimal(netPayableAmount));

            contractorBillRegister.addEgBilldetailes(contractorBillRegisterService.getBillDetails(
                    contractorBillRegister, billdetails, workOrderEstimate, resultBinder, request));
        }

        return contractorBillRegister;
    }

}
