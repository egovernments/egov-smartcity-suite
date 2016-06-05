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

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.Accountdetailtype;
import org.egov.commons.CChartOfAccounts;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.model.bills.EgBillPayeedetails;
import org.egov.model.bills.EgBilldetails;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.abstractestimate.service.EstimateService;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.contractorbill.service.ContractorBillNumberGenerator;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.models.workorder.WorkOrderEstimate;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorderestimate.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping(value = "/contractorbill")
public class CreateContractorBillController extends GenericWorkFlowController {

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    @Autowired
    private ContractorBillNumberGenerator contractorBillNumberGenerator;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;
    
    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;
    
    @Autowired
    private EstimateService estimateService;

    @RequestMapping(value = "/newform", method = RequestMethod.GET)
    public String showNewForm(
            @ModelAttribute("contractorBillRegister") final ContractorBillRegister contractorBillRegister,
            final Model model, final HttpServletRequest request) {
        final String loaNumber = request.getParameter("loaNumber");
        final WorkOrder workOrder = letterOfAcceptanceService.getApprovedWorkOrder(loaNumber);
        final LineEstimateDetails lineEstimateDetails = lineEstimateService.findByEstimateNumber(workOrder.getEstimateNumber());
        setDropDownValues(model);

        model.addAttribute("stateType", contractorBillRegister.getClass().getSimpleName());

        prepareWorkflow(model, contractorBillRegister, new WorkflowContainer());

        model.addAttribute("mode", "edit");

        //TODO: remove this condition to check if spillover
        if(!lineEstimateDetails.getLineEstimate().isSpillOverFlag())
            contractorBillRegister.setBilldate(new Date());
        
        model.addAttribute("workOrder", workOrder);
        model.addAttribute("lineEstimateDetails", lineEstimateDetails);
        model.addAttribute("contractorBillRegister", contractorBillRegister);
        return "contractorBill-form";
    }

    private void setDropDownValues(final Model model) {
        final List<CChartOfAccounts> contractorPayableAccountList = chartOfAccountsHibernateDAO
                .getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_NETPAYABLE_PURPOSE);
        model.addAttribute("netPayableAccounCodes", contractorPayableAccountList);
        model.addAttribute("billTypes", BillTypes.values());
    }

    @RequestMapping(value = "/contractorbill-save", method = RequestMethod.POST)
    public String create(@ModelAttribute("contractorBillRegister") ContractorBillRegister contractorBillRegister,
            final Model model, final BindingResult resultBinder, final HttpServletRequest request,
            @RequestParam String workFlowAction, @RequestParam("file") final MultipartFile[] files) throws IOException {

        final String loaNumber = request.getParameter("loaNumber");
        final WorkOrder workOrder = letterOfAcceptanceService.getApprovedWorkOrder(loaNumber);
        final LineEstimateDetails lineEstimateDetails = lineEstimateService.findByEstimateNumber(workOrder.getEstimateNumber());
        final AbstractEstimate abstractEstimate = estimateService
                .getAbstractEstimateByEstimateNumberAndStatus(lineEstimateDetails.getEstimateNumber());
        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService.getEstimateByWorkOrderAndEstimateAndStatus(
                workOrder.getId(), abstractEstimate.getId());
        contractorBillRegister.setWorkOrder(workOrder);
        contractorBillRegister.setWorkOrderEstimate(workOrderEstimate);

        validateInput(contractorBillRegister, lineEstimateDetails, resultBinder, request);

        contractorBillRegister = addBillDetails(contractorBillRegister, lineEstimateDetails, resultBinder, request);

        if (!checkForDuplicateAccountCodes(contractorBillRegister))
            resultBinder.reject("error.contractorbill.duplicate.accountcodes", "error.contractorbill.duplicate.accountcodes");
        validateTotalDebitAndCreditAmount(contractorBillRegister, resultBinder);

        if (resultBinder.hasErrors()) {
            setDropDownValues(model);
            model.addAttribute("lineEstimateDetails", lineEstimateDetails);
            model.addAttribute("workOrder", workOrder);
            model.addAttribute("netPayableAmount", request.getParameter("netPayableAmount"));
            model.addAttribute("netPayableAccountCode", request.getParameter("netPayableAccountCode"));
            model.addAttribute("stateType", contractorBillRegister.getClass().getSimpleName());

            model.addAttribute("approvalDesignation", request.getParameter("approvalDesignation"));
            model.addAttribute("approvalPosition", request.getParameter("approvalPosition"));

            prepareWorkflow(model, contractorBillRegister, new WorkflowContainer());

            model.addAttribute("mode", "edit");

            model.addAttribute("billDetailsMap", getBillDetailsMap(contractorBillRegister));

            return "contractorBill-form";
        } else {

            Long approvalPosition = 0l;
            String approvalComment = "";
            if (request.getParameter("approvalComment") != null)
                approvalComment = request.getParameter("approvalComent");
            if (request.getParameter("workFlowAction") != null)
                workFlowAction = request.getParameter("workFlowAction");
            if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
                approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

            Integer partBillCount = contractorBillRegisterService
                    .getMaxSequenceNumberByWorkOrder(workOrderEstimate);
            if (partBillCount == null || partBillCount == 0)
                partBillCount = 1;
            else
                partBillCount++;
            contractorBillRegister.setBillSequenceNumber(partBillCount);
            contractorBillRegister.setBillnumber(
                    contractorBillNumberGenerator.generateContractorBillNumber(contractorBillRegister));

            contractorBillRegister.setPassedamount(contractorBillRegister.getBillamount());
            ContractorBillRegister savedContractorBillRegister = null;
            try {
                savedContractorBillRegister = contractorBillRegisterService
                        .create(contractorBillRegister, lineEstimateDetails, files, approvalPosition,
                                approvalComment, null, workFlowAction);
            } catch (final ValidationException e) {
                // TODO: Used ApplicationRuntimeException for time being since there is issue in session after
                // checkBudgetAndGenerateBANumber API call. Needs to replace with errors.reject
                throw new ApplicationRuntimeException("error.contractorbill.budgetcheck.insufficient.amount");
                /*
                 * for (final ValidationError error : e.getErrors()) { if(error.getMessage().contains("Budget Check failed for "))
                 * { errors.reject(messageSource.getMessage("error.contractorbill.budgetcheck.insufficient.amount",null,null)+". "
                 * +error.getMessage()); } else errors.reject(error.getMessage()); }
                 */
            }
            final String pathVars = worksUtils.getPathVars(savedContractorBillRegister.getStatus(),
                    savedContractorBillRegister.getState(), savedContractorBillRegister.getId(), approvalPosition);

            return "redirect:/contractorbill/contractorbill-success?pathVars=" + pathVars + "&billNumber="
                    + savedContractorBillRegister.getBillnumber();
        }
    }

    @RequestMapping(value = "/contractorbill-success", method = RequestMethod.GET)
    public String showContractorBillSuccessPage(@RequestParam("billNumber") final String billNumber, final Model model,
            final HttpServletRequest request) {

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
            model.addAttribute("approverName", approverName);
        model.addAttribute("currentUserDesgn", currentUserDesgn);
        model.addAttribute("nextDesign", nextDesign);

        final ContractorBillRegister contractorBillRegister = contractorBillRegisterService
                .getContractorBillByBillNumber(billNumber);

        final String message = getMessageByStatus(contractorBillRegister, approverName, nextDesign);

        model.addAttribute("message", message);

        model.addAttribute("contractorBillRegister", contractorBillRegister);
        return "contractorBill-success";
    }

    private void validateInput(final ContractorBillRegister contractorBillRegister, final LineEstimateDetails lineEstimateDetails,
            final BindingResult resultBinder,
            final HttpServletRequest request) {
        final boolean validateBillInWorkflow = letterOfAcceptanceService
                .validateContractorBillInWorkflowForWorkorder(contractorBillRegister.getWorkOrder().getId());
        if (!validateBillInWorkflow)
            resultBinder.reject("error.contractorbill.in.workflow.for.workorder",
                    new String[] { contractorBillRegister.getWorkOrder().getWorkOrderNumber() }, null);

        BigDecimal totalBillAmountIncludingCurrentBill = contractorBillRegister.getBillamount();
        final BigDecimal totalBillAmount = contractorBillRegisterService
                .getTotalBillAmountByWorkOrder(contractorBillRegister.getWorkOrder());
        if (totalBillAmount != null)
            totalBillAmountIncludingCurrentBill = totalBillAmountIncludingCurrentBill.add(totalBillAmount);
        if (lineEstimateDetails.getLineEstimate().isBillsCreated() && lineEstimateDetails.getGrossAmountBilled() != null)
            totalBillAmountIncludingCurrentBill = totalBillAmountIncludingCurrentBill
                    .add(lineEstimateDetails.getGrossAmountBilled());
        if (totalBillAmountIncludingCurrentBill.doubleValue() > contractorBillRegister.getWorkOrder().getWorkOrderAmount())
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
            resultBinder.rejectValue("egBillregistermis.partyBillDate", "error.validate.partybilldate.lessthan.loadate");

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
                    && contractorBillRegister.getMbHeader().getToPageNo() != null
                    && contractorBillRegister.getMbHeader().getFromPageNo() > contractorBillRegister.getMbHeader().getToPageNo())
                resultBinder.reject("error.validate.mb.frompagenumber.greaterthan.topagenumber",
                        "error.validate.mb.frompagenumber.greaterthan.topagenumber");

            if (contractorBillRegister.getMbHeader().getMbDate() != null
                    && contractorBillRegister.getMbHeader().getMbDate()
                            .before(contractorBillRegister.getWorkOrder().getWorkOrderDate()))
                resultBinder.rejectValue("mbHeader.mbDate", "error.validate.mbdate.lessthan.loadate");
        }

        if (StringUtils.isBlank(request.getParameter("netPayableAccountCode")))
            resultBinder.reject("error.netpayable.accountcode.required", "error.netpayable.accountcode.required");
        if (StringUtils.isBlank(request.getParameter("netPayableAmount"))
                || Double.valueOf(request.getParameter("netPayableAmount").toString()) < 0)
            resultBinder.reject("error.netpayable.amount.required", "error.netpayable.amount.required");
        
        //TODO: from this line code should be removed after user data entry is finished.
        if(contractorBillRegister.getEgBillregistermis() != null
                && contractorBillRegister.getEgBillregistermis().getPartyBillDate() != null
                && contractorBillRegister.getEgBillregistermis().getPartyBillDate()
                        .after(contractorBillRegister.getBilldate())) {
            resultBinder.rejectValue("egBillregistermis.partyBillDate", "error.partybilldate.billdate");
        }
        
        if (lineEstimateDetails.getLineEstimate().isSpillOverFlag()) {
            final Date currentDate = new Date();
            final Date currentFinYear = lineEstimateService.getCurrentFinancialYear(currentDate).getStartingDate();
            if (contractorBillRegister.getBilldate().after(currentDate)) {
                resultBinder.rejectValue("billdate", "error.billdate.futuredate");
            }
            if (contractorBillRegister.getBilldate().before(contractorBillRegister.getWorkOrder().getWorkOrderDate())) {
                resultBinder.rejectValue("billdate", "error.billdate.workorderdate");
            }
            if (contractorBillRegister.getBilldate().before(currentFinYear)) {
                resultBinder.rejectValue("billdate", "error.billdate.finyear");
            }
        }
    }

    private void validateTotalDebitAndCreditAmount(final ContractorBillRegister contractorBillRegister,
            final BindingResult resultBinder) {
        BigDecimal totalDebitAmount = BigDecimal.ZERO;
        BigDecimal totalCreditAmount = BigDecimal.ZERO;
        for (final EgBilldetails egBilldetails : contractorBillRegister.getEgBilldetailes()) {
            if (egBilldetails.getDebitamount() != null && !(BigDecimal.ZERO.compareTo(egBilldetails.getDebitamount()) == 0))
                totalDebitAmount = totalDebitAmount.add(egBilldetails.getDebitamount());
            if (egBilldetails.getCreditamount() != null && !(BigDecimal.ZERO.compareTo(egBilldetails.getCreditamount()) == 0))
                totalCreditAmount = totalCreditAmount.add(egBilldetails.getCreditamount());
        }
        if (!(totalDebitAmount.compareTo(totalCreditAmount) == 0))
            resultBinder.reject("error.total.debitamount.creditamount.notequal", "error.total.debitamount.creditamount.notequal");
    }

    private boolean checkForDuplicateAccountCodes(final ContractorBillRegister contractorBillRegister) {
        final Set<Long> glCodeIdSet = new HashSet<Long>();
        for (final EgBilldetails egBilldetails : contractorBillRegister.getEgBilldetailes())
            if (egBilldetails.getGlcodeid() != null) {
                if (glCodeIdSet.contains(egBilldetails.getGlcodeid().longValue()))
                    return false;
                glCodeIdSet.add(egBilldetails.getGlcodeid().longValue());
            }
        return true;
    }

    private String getMessageByStatus(final ContractorBillRegister contractorBillRegister, final String approverName,
            final String nextDesign) {
        String message = "";

        if (contractorBillRegister.getStatus().getCode().equals(ContractorBillRegister.BillStatus.CREATED.toString())) {
            if (StringUtils.isNotBlank(contractorBillRegister.getEgBillregistermis().getBudgetaryAppnumber()))
                message = messageSource.getMessage("msg.contractorbill.create.success.with.budgetappropriation",
                        new String[] { contractorBillRegister.getBillnumber(), approverName, nextDesign,
                                contractorBillRegister.getEgBillregistermis().getBudgetaryAppnumber() },
                        null);
            else
                message = messageSource.getMessage("msg.contractorbill.create.success",
                        new String[] { contractorBillRegister.getBillnumber(), approverName, nextDesign }, null);

        } else if (contractorBillRegister.getStatus().getCode().equals(ContractorBillRegister.BillStatus.APPROVED.toString()))
            message = messageSource.getMessage("msg.contractorbill.approved.success",
                    new String[] { contractorBillRegister.getBillnumber() }, null);
        else if (contractorBillRegister.getState().getValue().equals(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.contractorbill.reject",
                    new String[] { contractorBillRegister.getBillnumber(), approverName, nextDesign }, null);
        else if (contractorBillRegister.getState().getValue().equals(WorksConstants.WF_STATE_CANCELLED))
            message = messageSource.getMessage("msg.contractorbill.cancel",
                    new String[] { contractorBillRegister.getBillnumber() }, null);

        return message;
    }

    private ContractorBillRegister addBillDetails(final ContractorBillRegister contractorBillRegister,
            final LineEstimateDetails lineEstimateDetails, final BindingResult resultBinder, final HttpServletRequest request) {
        if (contractorBillRegister.getBillDetailes() == null || contractorBillRegister.getBillDetailes().isEmpty())
            resultBinder.reject("error.contractorbill.accountdetails.required", "error.contractorbill.accountdetails.required");
        for (final EgBilldetails egBilldetails : contractorBillRegister.getBillDetailes())
            if (egBilldetails.getGlcodeid() != null)
                contractorBillRegister
                        .addEgBilldetailes(
                                getBillDetails(contractorBillRegister, egBilldetails, lineEstimateDetails, resultBinder,
                                        request));
        final String netPayableAccountCodeId = request.getParameter("netPayableAccountCode");
        final String netPayableAmount = request.getParameter("netPayableAmount");
        if (StringUtils.isNotBlank(netPayableAccountCodeId) && StringUtils.isNotBlank(netPayableAmount)) {
            final EgBilldetails billdetails = new EgBilldetails();
            billdetails.setGlcodeid(new BigDecimal(netPayableAccountCodeId));
            billdetails.setCreditamount(new BigDecimal(netPayableAmount));
            contractorBillRegister
                    .addEgBilldetailes(
                            getBillDetails(contractorBillRegister, billdetails, lineEstimateDetails, resultBinder, request));
        }

        return contractorBillRegister;
    }

    private EgBilldetails getBillDetails(final ContractorBillRegister billregister, final EgBilldetails egBilldetails,
            final LineEstimateDetails lineEstimateDetails, final BindingResult resultBinder, final HttpServletRequest request) {
        egBilldetails.setFunctionid(new BigDecimal(lineEstimateDetails.getLineEstimate().getFunction().getId()));
        boolean isDebit = false;
        CChartOfAccounts coa = null;
        if (!(BigDecimal.ZERO.compareTo(egBilldetails.getGlcodeid()) == 0))
            coa = chartOfAccountsHibernateDAO.findById(egBilldetails.getGlcodeid().longValue(), false);
        if (coa != null && coa.getId() != null)
            egBilldetails.setGlcodeid(BigDecimal.valueOf(coa.getId()));
        if (egBilldetails.getDebitamount() != null && !(BigDecimal.ZERO.compareTo(egBilldetails.getDebitamount()) == 0)) {
            egBilldetails.setDebitamount(egBilldetails.getDebitamount());
            isDebit = true;
        } else if (egBilldetails.getCreditamount() != null && !(BigDecimal.ZERO.compareTo(egBilldetails.getCreditamount()) == 0))
            egBilldetails.setCreditamount(egBilldetails.getCreditamount());
        else if (!StringUtils.isBlank(request.getParameter("netPayableAccountCode"))
                && request.getParameter("netPayableAccountCode").toString().equals(egBilldetails.getGlcodeid()))
            resultBinder.reject("error.contractorbill.accountdetails.amount.required",
                    "error.contractorbill.accountdetails.amount.required");

        egBilldetails.setEgBillregister(billregister);
        if (coa != null && coa.getGlcode() != null) {
            Accountdetailtype projectCodeAccountDetailType = null;
            Accountdetailtype contractorAccountDetailType = null;
            if (isDebit) {
                projectCodeAccountDetailType = chartOfAccountsHibernateDAO.getAccountDetailTypeIdByName(coa.getGlcode(),
                        WorksConstants.PROJECTCODE);
                if (projectCodeAccountDetailType == null)
                    resultBinder.reject("error.contractorBill.validate.glcode.for.projectcode.subledger",
                            new String[] { coa.getGlcode() }, null);
            }
            final List<Accountdetailtype> detailCode = chartOfAccountsHibernateDAO
                    .getAccountdetailtypeListByGLCode(coa.getGlcode());
            if (detailCode != null && !detailCode.isEmpty()) {
                if (isDebit) {
                    if (projectCodeAccountDetailType != null)
                        egBilldetails.addEgBillPayeedetail(getEgPayeeDetails(
                                egBilldetails, projectCodeAccountDetailType.getId(), egBilldetails.getDebitamount(), isDebit,
                                Integer.valueOf(lineEstimateDetails.getProjectCode().getId()
                                        .toString())));
                } else {
                    contractorAccountDetailType = chartOfAccountsHibernateDAO.getAccountDetailTypeIdByName(coa.getGlcode(),
                            WorksConstants.ACCOUNTDETAIL_TYPE_CONTRACTOR);
                    if (contractorAccountDetailType != null)
                        egBilldetails.getEgBillPaydetailes().add(
                                getEgPayeeDetails(egBilldetails, contractorAccountDetailType.getId(),
                                        egBilldetails.getCreditamount(), isDebit,
                                        Integer.valueOf(billregister.getWorkOrder().getContractor().getId().toString())));

                }
                if (projectCodeAccountDetailType == null && contractorAccountDetailType == null)
                    resultBinder.reject("error.contractorbill.validate.glcode.for.subledger", new String[] { coa.getGlcode() },
                            null);
            }
        }
        egBilldetails.setLastupdatedtime(new Date());
        return egBilldetails;
    }

    private EgBillPayeedetails getEgPayeeDetails(final EgBilldetails billDetails, final Integer accountsDetailTypeId,
            final BigDecimal amount, final boolean isDebit, final Integer accountsDetailKeyId) {
        final EgBillPayeedetails egBillPaydetail = new EgBillPayeedetails();
        egBillPaydetail.setAccountDetailKeyId(accountsDetailKeyId);
        egBillPaydetail.setAccountDetailTypeId(accountsDetailTypeId);
        if (isDebit)
            egBillPaydetail.setDebitAmount(amount);
        else
            egBillPaydetail.setCreditAmount(amount);
        egBillPaydetail.setEgBilldetailsId(billDetails);
        egBillPaydetail.setLastUpdatedTime(new Date());
        return egBillPaydetail;
    }

    public List<Map<String, Object>> getBillDetailsMap(final ContractorBillRegister contractorBillRegister) {
        final List<Map<String, Object>> billDetailsList = new ArrayList<Map<String, Object>>();
        Map<String, Object> billDetails = new HashMap<String, Object>();

        final List<CChartOfAccounts> contractorPayableAccountList = chartOfAccountsHibernateDAO
                .getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_NETPAYABLE_PURPOSE);
        for (final EgBilldetails egBilldetails : contractorBillRegister.getEgBilldetailes()) {
            if (egBilldetails.getDebitamount() != null) {
                billDetails = new HashMap<String, Object>();
                final CChartOfAccounts coa = chartOfAccountsHibernateDAO.findById(egBilldetails.getGlcodeid().longValue(), false);
                billDetails.put("glcodeId", coa.getId());
                billDetails.put("glcode", coa.getGlcode());
                billDetails.put("accountHead", coa.getName());
                billDetails.put("amount", egBilldetails.getDebitamount());
                billDetails.put("isDebit", true);
                billDetails.put("isNetPayable", false);
            } else if (egBilldetails.getCreditamount() != null) {
                billDetails = new HashMap<String, Object>();
                final CChartOfAccounts coa = chartOfAccountsHibernateDAO.findById(egBilldetails.getGlcodeid().longValue(), false);
                billDetails.put("glcodeId", coa.getId());
                billDetails.put("glcode", coa.getGlcode());
                billDetails.put("accountHead", coa.getName());
                billDetails.put("amount", egBilldetails.getCreditamount());
                billDetails.put("isDebit", false);
                if (contractorPayableAccountList != null && !contractorPayableAccountList.isEmpty()
                        && contractorPayableAccountList.contains(coa))
                    billDetails.put("isNetPayable", true);
                else
                    billDetails.put("isNetPayable", false);

            }
            billDetailsList.add(billDetails);
        }
        return billDetailsList;
    }

}
