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
package org.egov.works.web.controller.contractorbill;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.bills.EgBilldetails;
import org.egov.works.config.properties.WorksApplicationProperties;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.lineestimate.entity.DocumentDetails;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.models.measurementbook.MBHeader;
import org.egov.works.models.workorder.WorkOrder;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/contractorbill")
public class UpdateContractorBillController extends GenericWorkFlowController {
    @Autowired
    private LineEstimateService lineEstimateService;

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
        return loadViewData(model, request, contractorBillRegister);
    }

    @RequestMapping(value = "/update/{contractorBillRegisterId}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("contractorBillRegister") ContractorBillRegister contractorBillRegister,
            final BindingResult errors, final RedirectAttributes redirectAttributes, final Model model,
            final HttpServletRequest request, @RequestParam("file") final MultipartFile[] files)
            throws ApplicationException, IOException {

        String mode = "";
        String workFlowAction = "";
        ContractorBillRegister updatedContractorBillRegister = null;

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
            approvalPosition = contractorBillRegisterService.getApprovalPositionByMatrixDesignation(
                    contractorBillRegister, approvalPosition, null, mode, workFlowAction);

        if ((approvalPosition == null || approvalPosition.equals(Long.valueOf(0)))
                && request.getParameter("approvalPosition") != null
                && !request.getParameter("approvalPosition").isEmpty())
            approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

        try {
            if (contractorBillRegister.getStatus().getCode()
                    .equals(ContractorBillRegister.BillStatus.REJECTED.toString())
                    && workFlowAction.equals(WorksConstants.FORWARD_ACTION))
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
                && workFlowAction.equals(WorksConstants.FORWARD_ACTION) && mode.equals("edit")) {
            final WorkOrder workOrder = contractorBillRegister.getWorkOrder();
            final LineEstimateDetails lineEstimateDetails = lineEstimateService
                    .findByEstimateNumber(workOrder.getEstimateNumber());

            validateInput(contractorBillRegister, lineEstimateDetails, errors, request);
            contractorBillRegister.getEgBilldetailes().clear();

            contractorBillRegister = addBillDetails(contractorBillRegister, lineEstimateDetails, errors, request);
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
                        contractorBillRegister, approvalPosition, approvalComment, null, workFlowAction, mode, files);

            redirectAttributes.addFlashAttribute("contractorBillRegister", updatedContractorBillRegister);

            final String pathVars = worksUtils.getPathVars(updatedContractorBillRegister.getStatus(),
                    updatedContractorBillRegister.getState(), updatedContractorBillRegister.getId(), approvalPosition);

            return "redirect:/contractorbill/contractorbill-success?pathVars=" + pathVars + "&billNumber="
                    + updatedContractorBillRegister.getBillnumber();
        }
    }

    private void validateInput(final ContractorBillRegister contractorBillRegister,
            final LineEstimateDetails lineEstimateDetails, final BindingResult resultBinder,
            final HttpServletRequest request) {

        BigDecimal totalBillAmountIncludingCurrentBill = contractorBillRegister.getBillamount();
        final BigDecimal totalBillAmount = contractorBillRegisterService
                .getTotalBillAmountByWorkOrderAndNotContractorBillRegister(contractorBillRegister.getWorkOrder(),
                        contractorBillRegister.getId());
        if (totalBillAmount != null)
            totalBillAmountIncludingCurrentBill = totalBillAmountIncludingCurrentBill.add(totalBillAmount);
        if (lineEstimateDetails.getLineEstimate().isBillsCreated()
                && lineEstimateDetails.getGrossAmountBilled() != null)
            totalBillAmountIncludingCurrentBill = totalBillAmountIncludingCurrentBill
                    .add(lineEstimateDetails.getGrossAmountBilled());
        if (totalBillAmountIncludingCurrentBill.doubleValue() > contractorBillRegister.getWorkOrder()
                .getWorkOrderAmount())
            resultBinder.reject("error.contractorbill.totalbillamount.exceeds.workorderamount",
                    new String[] { String.valueOf(totalBillAmountIncludingCurrentBill),
                            String.valueOf(contractorBillRegister.getWorkOrder().getWorkOrderAmount()) },
                    null);

        if (StringUtils.isBlank(contractorBillRegister.getBilltype()))
            resultBinder.rejectValue("billtype", "error.billtype.required");
        if (contractorBillRegister.getEgBillregistermis() != null
                && contractorBillRegister.getEgBillregistermis().getPartyBillDate() != null
                && contractorBillRegister.getEgBillregistermis().getPartyBillDate()
                        .before(contractorBillRegister.getWorkOrder().getWorkOrderDate()))
            resultBinder.rejectValue("egBillregistermis.partyBillDate",
                    "error.validate.partybilldate.lessthan.loadate");

        if (contractorBillRegister.getMbHeader() != null) {
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
                    .getMbDate().before(contractorBillRegister.getWorkOrder().getWorkOrderDate()))
                resultBinder.rejectValue("mbHeader.mbDate", "error.validate.mbdate.lessthan.loadate");
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
            if (workCompletionDate.after(currentDate))
                resultBinder.rejectValue("workOrderEstimate.workCompletionDate", "error.workcompletiondate.futuredate");
            if (workCompletionDate
                    .before(contractorBillRegister.getWorkOrderEstimate().getWorkOrder().getWorkOrderDate()))
                resultBinder.rejectValue("workOrderEstimate.workCompletionDate",
                        "error.workcompletiondate.workorderdate");
            if (workCompletionDate.after(contractorBillRegister.getBilldate()))
                resultBinder.rejectValue("workOrderEstimate.workCompletionDate", "error.workcompletiondate.billdate");
        }
    }

    private void setDropDownValues(final Model model) {
        final List<CChartOfAccounts> contractorPayableAccountList = chartOfAccountsHibernateDAO
                .getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_NETPAYABLE_PURPOSE);
        final List<CChartOfAccounts> contractorRefundAccountList = chartOfAccountsHibernateDAO
                .getAccountCodeByListOfPurposeName(WorksConstants.CONTRACTOR_REFUND_PURPOSE);
        model.addAttribute("netPayableAccounCodes", contractorPayableAccountList);
        model.addAttribute("refundAccounCodes", contractorRefundAccountList);
        model.addAttribute("billTypes", BillTypes.values());
    }

    private String loadViewData(final Model model, final HttpServletRequest request,
            final ContractorBillRegister contractorBillRegister) {

        model.addAttribute("stateType", contractorBillRegister.getClass().getSimpleName());

        if (contractorBillRegister.getState() != null)
            model.addAttribute("currentState", contractorBillRegister.getState().getValue());

        prepareWorkflow(model, contractorBillRegister, new WorkflowContainer());
        if (contractorBillRegister.getState() != null
                && contractorBillRegister.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            model.addAttribute("mode", "edit");
        else
            model.addAttribute("mode", "view");

        model.addAttribute("billDetailsMap", contractorBillRegisterService.getBillDetailsMap(contractorBillRegister, model));

        model.addAttribute("workflowHistory", lineEstimateService.getHistory(contractorBillRegister.getState(),
                contractorBillRegister.getStateHistory()));
        model.addAttribute("approvalDepartmentList", departmentService.getAllDepartments());
        model.addAttribute("approvalDesignation", request.getParameter("approvalDesignation"));
        model.addAttribute("approvalPosition", request.getParameter("approvalPosition"));

        final WorkOrder workOrder = contractorBillRegister.getWorkOrder();
        final LineEstimateDetails lineEstimateDetails = lineEstimateService
                .findByEstimateNumber(workOrder.getEstimateNumber());

        model.addAttribute("lineEstimateDetails", lineEstimateDetails);
        model.addAttribute("workOrder", workOrder);

        final ContractorBillRegister newcontractorBillRegister = getContractorBillDocuments(contractorBillRegister);
        model.addAttribute("contractorBillRegister", newcontractorBillRegister);
        final List<MBHeader> mbHeaders = mbHeaderService.getMBHeadersByContractorBill(newcontractorBillRegister);
        if (mbHeaders != null && !mbHeaders.isEmpty())
            newcontractorBillRegister.setMbHeader(mbHeaders.get(0));

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
        final String responsePage = loadViewData(model, request, contractorBillRegister);
        model.addAttribute("createdbybydesignation",
                worksUtils.getUserDesignation(contractorBillRegister.getCreatedBy()));
        model.addAttribute("mode", "readOnly");
        return responsePage;
    }

    private ContractorBillRegister addBillDetails(final ContractorBillRegister contractorBillRegister,
            final LineEstimateDetails lineEstimateDetails, final BindingResult resultBinder,
            final HttpServletRequest request) {
        if (contractorBillRegister.getBillDetailes() == null || contractorBillRegister.getBillDetailes().isEmpty())
            resultBinder.reject("error.contractorbill.accountdetails.required",
                    "error.contractorbill.accountdetails.required");
        for (final EgBilldetails egBilldetails : contractorBillRegister.getBillDetailes())
            if (!contractorBillRegister.getEgBilldetailes().isEmpty() && contractorBillRegister.getEgBilldetailes().size() == 1) {
                for (final EgBilldetails refundBill : contractorBillRegister.getRefundBillDetails())
                    if (refundBill.getGlcodeid() != null)
                        contractorBillRegister.addEgBilldetailes(
                                contractorBillRegisterService.getBillDetails(contractorBillRegister, refundBill,
                                        lineEstimateDetails, resultBinder, request));
                if (egBilldetails.getGlcodeid() != null)
                    contractorBillRegister
                            .addEgBilldetailes(contractorBillRegisterService.getBillDetails(contractorBillRegister, egBilldetails,
                                    lineEstimateDetails, resultBinder, request));
            } else if (egBilldetails.getGlcodeid() != null)
                contractorBillRegister
                        .addEgBilldetailes(contractorBillRegisterService.getBillDetails(contractorBillRegister, egBilldetails,
                                lineEstimateDetails, resultBinder, request));

        contractorBillRegisterService.validateZeroCreditAndDebitAmount(contractorBillRegister, resultBinder);
        final String netPayableAccountId = request.getParameter("netPayableAccountId");
        final String netPayableAccountCodeId = request.getParameter("netPayableAccountCode");
        final String netPayableAmount = request.getParameter("netPayableAmount");
        if (StringUtils.isNotBlank(netPayableAccountCodeId) && StringUtils.isNotBlank(netPayableAccountCodeId)
                && StringUtils.isNotBlank(netPayableAmount)) {
            final EgBilldetails billdetails = new EgBilldetails();
            billdetails.setId(Integer.valueOf(netPayableAccountId));
            billdetails.setGlcodeid(new BigDecimal(netPayableAccountCodeId));
            billdetails.setCreditamount(new BigDecimal(netPayableAmount));
            contractorBillRegister.addEgBilldetailes(
                    contractorBillRegisterService.getBillDetails(contractorBillRegister, billdetails, lineEstimateDetails,
                            resultBinder, request));
        }

        return contractorBillRegister;
    }

}
