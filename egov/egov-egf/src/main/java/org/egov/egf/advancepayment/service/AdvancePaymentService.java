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
package org.egov.egf.advancepayment.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.EgPartytype;
import org.egov.commons.service.BankAccountService;
import org.egov.commons.utils.EntityType;
import org.egov.egf.advancepayment.SearchAdvanceRequisition;
import org.egov.egf.advancepayment.repository.AdvancePaymentRepository;
import org.egov.egf.expensebill.service.ExpenseBillService;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.model.advance.EgAdvanceReqPayeeDetails;
import org.egov.model.advance.EgAdvanceRequisition;
import org.egov.model.advance.EgAdvanceRequisitionDetails;
import org.egov.model.advance.EgAdvanceRequisitionMis;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.payment.Paymentheader;
import org.egov.pims.commons.Position;
import org.egov.services.masters.EgPartyTypeService;
import org.egov.services.payment.MiscbilldetailService;
import org.egov.services.payment.PaymentService;
import org.egov.utils.FinancialConstants;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@Transactional(readOnly = true)
public class AdvancePaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(AdvancePaymentService.class);
    @Autowired
    private AdvancePaymentRepository advancePaymentRepository;

    @Autowired
    private MiscbilldetailService miscbilldetailService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<Paymentheader> advancePaymentWorkflowService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    protected PaymentService paymentService;

    @Autowired
    private ExpenseBillService expenseBillService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private CreateVoucher createVoucher;

    @Autowired
    private EgPartyTypeService egPartyTypeService;

    public List<String> getArfNumber(final String advanceRequisitionNumber) {
        final List<String> advanceRequisitionNotPaid = new ArrayList<>();
        final List<EgAdvanceRequisition> advanceRequisition = advancePaymentRepository
                .findARFNumberToSearchAdvanceBill("%" + advanceRequisitionNumber + "%", "APPROVED");
        for (final EgAdvanceRequisition ear : advanceRequisition)
            if (ear.getEgAdvanceReqMises().getEgBillregister() != null
                    && ear.getEgAdvanceReqMises().getEgBillregister().getEgBillregistermis().getVoucherHeader() != null
                    && FinancialConstants.CREATEDVOUCHERSTATUS.equals(ear.getEgAdvanceReqMises().getEgBillregister()
                            .getEgBillregistermis().getVoucherHeader().getStatus())) {
                final Miscbilldetail miscBill = miscbilldetailService.findByBillvhId(
                        ear.getEgAdvanceReqMises().getEgBillregister().getEgBillregistermis().getVoucherHeader().getId());
                if (miscBill == null
                        || FinancialConstants.CANCELLEDVOUCHERSTATUS.equals(miscBill.getPayVoucherHeader().getStatus()))
                    advanceRequisitionNotPaid.add(ear.getAdvanceRequisitionNumber());
            }

        return advanceRequisitionNotPaid;
    }

    public List<EgPartytype> getPartytypeByArfNumber(final String arfNumber) {
        final List<EgPartytype> egPartytype = new ArrayList<>();
        final EgAdvanceRequisition advanceRequisition = advancePaymentRepository.findByAdvanceRequisitionNumber(arfNumber);
        if (advanceRequisition != null)
            egPartytype.add(egPartyTypeService.findByCode(advanceRequisition.getArftype()));
        return egPartytype;
    }

    public List<EgAdvanceRequisition> searchAdvanceBill(final SearchAdvanceRequisition searchAdvanceRequisition) {
        final List<EgAdvanceRequisition> advanceRequisitionNotPaid = new ArrayList<>();
        final StringBuilder queryStr = new StringBuilder(500);
        buildWhereClause(searchAdvanceRequisition, queryStr);
        final Query query = setParameterForSearchAdvanceBill(searchAdvanceRequisition, queryStr);
        final List<EgAdvanceRequisition> advanceRequisitionList = query.getResultList();
        for (final EgAdvanceRequisition ear : advanceRequisitionList)
            if (ear.getEgAdvanceReqMises().getEgBillregister() != null
                    && ear.getEgAdvanceReqMises().getEgBillregister().getEgBillregistermis().getVoucherHeader() != null
                    && FinancialConstants.CREATEDVOUCHERSTATUS.equals(ear.getEgAdvanceReqMises().getEgBillregister()
                            .getEgBillregistermis().getVoucherHeader().getStatus())) {
                final Miscbilldetail miscBill = miscbilldetailService.findByBillvhId(
                        ear.getEgAdvanceReqMises().getEgBillregister().getEgBillregistermis().getVoucherHeader().getId());
                if (miscBill == null
                        || FinancialConstants.CANCELLEDVOUCHERSTATUS.equals(miscBill.getPayVoucherHeader().getStatus()))
                    advanceRequisitionNotPaid.add(ear);
            }
        return advanceRequisitionNotPaid;
    }

    private void buildWhereClause(final SearchAdvanceRequisition searchAdvanceRequisition,
            final StringBuilder queryStr) {

        queryStr.append(
                "from EgAdvanceRequisition  ear where ");

        if (StringUtils.isNotBlank(searchAdvanceRequisition.getArfNumber())) {
            queryStr.append(
                    " upper(ear.advanceRequisitionNumber) = :advanceRequisitionNumber ");

            if (searchAdvanceRequisition.getPartyType() != null)
                queryStr.append(
                        " and upper(ear.arftype) = :partyType");
        } else if (searchAdvanceRequisition.getPartyType() != null)
            queryStr.append(
                    " upper(ear.arftype) = :partyType");

        if (searchAdvanceRequisition.getFund() != null)
            queryStr.append(
                    " and ear.egAdvanceReqMises.fund.id = :fund");

        if (searchAdvanceRequisition.getFromDate() != null)
            queryStr.append(
                    " and ear.egAdvanceReqMises.egBillregister.createdDate >= :fromDate");

        if (searchAdvanceRequisition.getToDate() != null)
            queryStr.append(
                    " and ear.egAdvanceReqMises.egBillregister.createdDate <= :toDate");
        queryStr.append(
                " and ear.egAdvanceReqMises.egBillregister.egBillregistermis.voucherHeader != null and ear.egAdvanceReqMises.egBillregister.egBillregistermis.voucherHeader.status=0");

    }

    private Query setParameterForSearchAdvanceBill(
            final SearchAdvanceRequisition searchAdvanceRequisition,
            final StringBuilder queryStr) {
        final Query qry = entityManager.createQuery(queryStr.toString());

        if (searchAdvanceRequisition != null) {
            if (StringUtils.isNotBlank(searchAdvanceRequisition.getArfNumber()))
                qry.setParameter("advanceRequisitionNumber",
                        searchAdvanceRequisition.getArfNumber().toUpperCase());
            if (searchAdvanceRequisition.getPartyType() != null)
                qry.setParameter("partyType",
                        egPartyTypeService.findById(Integer.parseInt(searchAdvanceRequisition.getPartyType()), false).getCode()
                                .toUpperCase());
            if (searchAdvanceRequisition.getFund() != null)
                qry.setParameter("fund",
                        searchAdvanceRequisition.getFund().getId());
            if (searchAdvanceRequisition.getFromDate() != null)
                qry.setParameter("fromDate", searchAdvanceRequisition.getFromDate());
            if (searchAdvanceRequisition.getToDate() != null)
                qry.setParameter("toDate", searchAdvanceRequisition.getToDate());
        }

        return qry;
    }

    public EgAdvanceRequisition getAdvaceRequisitionByBillRegId(final Long egBillRegisterId) {
        return advancePaymentRepository.findByEgAdvanceReqMises_EgBillregister_Id(egBillRegisterId);
    }

    public EgAdvanceRequisition validateVoucherDetails(final EgBillregister egBillRegister, final String modeOfPay,
            final BindingResult resultBinder) throws ApplicationException {
        final EgBillregister billRegister = expenseBillService
                .getByVoucherHeaderId(egBillRegister.getEgBillregistermis().getVoucherHeader().getId());
        final EgAdvanceRequisition egAdvanceRequisition = getAdvaceRequisitionByBillRegId(billRegister.getId());
        validateAdvancePaymentExists(resultBinder, egAdvanceRequisition);
        if (modeOfPay.equalsIgnoreCase(FinancialConstants.MODEOFPAYMENT_RTGS))
            validateRTGS(egAdvanceRequisition, resultBinder);
        return egAdvanceRequisition;
    }

    @Transactional
    public Paymentheader createAdvancePayment(final EgBillregister egBillRegister, final CVoucherHeader voucherHeader,
            final EgAdvanceRequisition egAdvanceRequisition,
            final String bankAccount,
            final String modeOfPay, final String workFlowAction, final String approvalComment, final Long approvalPosition,
            final BigDecimal totalAmount)
            throws ApplicationException {
        setVoucherHeaderValues(voucherHeader);
        final CVoucherHeader voucherhdr = createVoucherAndledger(egAdvanceRequisition, voucherHeader, bankAccount,
                totalAmount);
        final Paymentheader payHeader = setPaymentHeader(voucherhdr, totalAmount,
                modeOfPay, bankAccount);
        final Paymentheader paymentHeader = createPaymentHeader(payHeader, workFlowAction, approvalPosition,
                approvalComment, null);
        createMiscBill(egBillRegister, paymentHeader);
        return paymentHeader;
    }

    private void setVoucherHeaderValues(final CVoucherHeader voucherheader) {
        voucherheader.setType(FinancialConstants.STANDARD_VOUCHER_TYPE_PAYMENT);
        voucherheader.setName(FinancialConstants.PAYMENTVOUCHER_NAME_ADVANCE);

    }

    public CVoucherHeader createVoucherAndledger(final EgAdvanceRequisition egAdvanceRequisition,
            final CVoucherHeader voucherHeader, final String bankAccount, final BigDecimal totalAmount)
            throws ApplicationException {

        final CVoucherHeader cVoucherHeader;
        final HashMap<String, Object> headerDetails = createHeaderAndMisDetails(voucherHeader, egAdvanceRequisition);
        HashMap<String, Object> detailMap = new HashMap<>();
        HashMap<String, Object> subledgertDetailMap;
        final List<HashMap<String, Object>> accountdetails = new ArrayList<>();
        final List<HashMap<String, Object>> subledgerDetails = new ArrayList<>();
        final Bankaccount account = bankAccountService.findById(Long.parseLong(bankAccount), false);
        final Map<String, Object> glcodeMap = new HashMap<>();

        detailMap.put(VoucherConstant.CREDITAMOUNT, egAdvanceRequisition.getAdvanceRequisitionAmount());
        detailMap.put(VoucherConstant.DEBITAMOUNT, BigDecimal.ZERO);
        detailMap.put(VoucherConstant.GLCODE, account.getChartofaccounts().getGlcode());
        accountdetails.add(detailMap);
        detailMap = new HashMap<>();

        detailMap.put(VoucherConstant.DEBITAMOUNT, egAdvanceRequisition.getAdvanceRequisitionAmount());
        detailMap.put(VoucherConstant.CREDITAMOUNT, BigDecimal.ZERO);
        for (final EgAdvanceRequisitionDetails advanceRequisitionDetails : egAdvanceRequisition
                .getEgAdvanceReqDetailses())
            detailMap.put(VoucherConstant.GLCODE, advanceRequisitionDetails.getChartofaccounts().getGlcode());
        accountdetails.add(detailMap);
        glcodeMap.put(detailMap.get(VoucherConstant.GLCODE).toString(), VoucherConstant.DEBIT);

        for (final EgAdvanceRequisitionDetails advanceDetail : egAdvanceRequisition.getEgAdvanceReqDetailses())
            for (final EgAdvanceReqPayeeDetails payeeDetail : advanceDetail.getEgAdvanceReqpayeeDetailses()) {
                subledgertDetailMap = new HashMap<>();
                final BigDecimal debitAmount = payeeDetail.getDebitAmount() == null ? BigDecimal.ZERO
                        : payeeDetail.getDebitAmount();
                final BigDecimal creditAmount = payeeDetail.getCreditAmount() == null ? BigDecimal.ZERO
                        : payeeDetail.getCreditAmount();
                subledgertDetailMap.put(VoucherConstant.DEBITAMOUNT, creditAmount);
                subledgertDetailMap.put(VoucherConstant.CREDITAMOUNT, debitAmount);
                subledgertDetailMap.put(VoucherConstant.DETAILTYPEID, payeeDetail.getAccountDetailType().getId());
                subledgertDetailMap.put(VoucherConstant.DETAILKEYID, payeeDetail.getAccountdetailKeyId());
                subledgertDetailMap.put(VoucherConstant.GLCODE, advanceDetail.getChartofaccounts().getGlcode());
                subledgerDetails.add(subledgertDetailMap);
            }
        cVoucherHeader = createVoucher.createPreApprovedVoucher(headerDetails, accountdetails, subledgerDetails);
        return cVoucherHeader;

    }

    protected HashMap<String, Object> createHeaderAndMisDetails(final CVoucherHeader voucherHeader,
            final EgAdvanceRequisition advanceRequisition) {
        final HashMap<String, Object> headerdetails = new HashMap<>();
        headerdetails.put(VoucherConstant.VOUCHERNAME, voucherHeader.getName());
        headerdetails.put(VoucherConstant.VOUCHERTYPE, voucherHeader.getType());
        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherHeader.getVoucherDate());
        headerdetails.put(VoucherConstant.DESCRIPTION, voucherHeader.getDescription());

        final EgAdvanceRequisitionMis egAdvanceReqMises = advanceRequisition.getEgAdvanceReqMises();
        if (egAdvanceReqMises != null) {
            if (egAdvanceReqMises.getFund() != null && egAdvanceReqMises.getFund().getId() != null)
                headerdetails.put(VoucherConstant.FUNDCODE, egAdvanceReqMises.getFund().getCode());
            if (egAdvanceReqMises.getEgDepartment() != null && egAdvanceReqMises.getEgDepartment().getId() != null)
                headerdetails.put(VoucherConstant.DEPARTMENTCODE, egAdvanceReqMises.getEgDepartment().getCode());
            if (egAdvanceReqMises.getFundsource() != null && egAdvanceReqMises.getFundsource().getId() != null)
                headerdetails.put(VoucherConstant.FUNDSOURCECODE, egAdvanceReqMises.getFundsource().getCode());
            if (egAdvanceReqMises.getScheme() != null && egAdvanceReqMises.getScheme().getId() != null)
                headerdetails.put(VoucherConstant.SCHEMECODE, egAdvanceReqMises.getScheme().getCode());
            if (egAdvanceReqMises.getSubScheme() != null && egAdvanceReqMises.getSubScheme().getId() != null)
                headerdetails.put(VoucherConstant.SUBSCHEMECODE, egAdvanceReqMises.getSubScheme().getCode());
            if (egAdvanceReqMises.getFunctionaryId() != null && egAdvanceReqMises.getFunctionaryId().getId() != null)
                headerdetails.put(VoucherConstant.FUNCTIONARYCODE, egAdvanceReqMises.getFunctionaryId().getCode());
            if (egAdvanceReqMises.getFunction() != null && egAdvanceReqMises.getFunction().getId() != null)
                headerdetails.put(VoucherConstant.FUNCTIONCODE, egAdvanceReqMises.getFunction().getCode());
            if (egAdvanceReqMises.getFieldId() != null && egAdvanceReqMises.getFieldId().getId() != null)
                headerdetails.put(VoucherConstant.DIVISIONID, egAdvanceReqMises.getFieldId().getId());
        }
        return headerdetails;
    }

    @Transactional
    protected void createMiscBill(final EgBillregister egBillRegister, final Paymentheader paymentheader) {
        final EgAdvanceRequisition advanceRequisition = getAdvaceRequisitionByBillRegId(egBillRegister.getId());
        final Miscbilldetail miscbilldetail = new Miscbilldetail();
        miscbilldetail.setBillnumber(advanceRequisition.getEgAdvanceReqMises().getEgBillregister().getBillnumber());
        miscbilldetail.setBilldate(advanceRequisition.getEgAdvanceReqMises().getEgBillregister().getBilldate());
        miscbilldetail.setBillamount(advanceRequisition.getEgAdvanceReqMises().getEgBillregister().getBillamount());
        miscbilldetail.setPassedamount(advanceRequisition.getEgAdvanceReqMises().getEgBillregister().getPassedamount());
        miscbilldetail.setPaidamount(advanceRequisition.getEgAdvanceReqMises().getEgBillregister().getBillamount());
        miscbilldetail
                .setPaidto(advanceRequisition.getEgAdvanceReqMises().getEgBillregister().getEgBillregistermis().getPayto());
        miscbilldetail.setPayVoucherHeader(paymentheader.getVoucherheader());
        miscbilldetail.setPaidby(securityUtils.getCurrentUser());
        miscbilldetail.setBillVoucherHeader(
                advanceRequisition.getEgAdvanceReqMises().getEgBillregister().getEgBillregistermis().getVoucherHeader());
        miscbilldetailService.persist(miscbilldetail);
    }

    public Paymentheader setPaymentHeader(final CVoucherHeader voucherHeader, final BigDecimal amount, final String modeOfPayment,
            final String bankAccount) {
        final Paymentheader paymentHeader = new Paymentheader();
        paymentHeader.setType(modeOfPayment);
        paymentHeader.setVoucherheader(voucherHeader);
        paymentHeader.setBankaccount(bankAccountService.findById(Long.parseLong(bankAccount), false));
        paymentHeader.setPaymentAmount(amount);
        paymentHeader.setCreatedBy(securityUtils.getCurrentUser());
        return paymentHeader;
    }

    private void validateAdvancePaymentExists(final BindingResult resultBinder,
            final EgAdvanceRequisition egAdvanceRequisition) {
        final Miscbilldetail miscbillDetails = miscbilldetailService
                .findByBillvhId(egAdvanceRequisition.getEgAdvanceReqMises().getEgBillregister().getEgBillregistermis()
                        .getVoucherHeader().getId());
        if (miscbillDetails != null && miscbillDetails.getPayVoucherHeader() != null && !FinancialConstants.CANCELLEDVOUCHERSTATUS
                .equals(miscbillDetails.getPayVoucherHeader().getStatus()))
            resultBinder.reject("msg.arf.payment.uniqueCheck.validate.message",
                    new String[] { egAdvanceRequisition.getAdvanceRequisitionNumber() }, null);
    }

    private void validateRTGS(final EgAdvanceRequisition egAdvanceRequisition, final BindingResult resultBinder)
            throws ApplicationException {
        EntityType entity;
        for (final EgAdvanceRequisitionDetails advanceDetail : egAdvanceRequisition.getEgAdvanceReqDetailses())
            if (!advanceDetail.getEgAdvanceReqpayeeDetailses().isEmpty())
                for (final EgAdvanceReqPayeeDetails payeeDetail : advanceDetail.getEgAdvanceReqpayeeDetailses())
                    try {
                        entity = paymentService.getEntity(payeeDetail.getAccountDetailType().getId(),
                                payeeDetail.getAccountdetailKeyId());
                        if (entity == null) {
                            resultBinder.reject("no.entity.for.detailkey", null, null);
                            break;
                        } else if (StringUtils.isBlank(entity.getPanno()) || StringUtils.isBlank(entity.getBankname())
                                || StringUtils.isBlank(entity.getBankaccount()) || StringUtils.isBlank(entity.getIfsccode()))
                            resultBinder.reject("validate.paymentMode.rtgs.subledger.details",
                                    new String[] { entity.getCode() + "-"
                                            + entity.getName() },
                                    null);
                    } catch (final ValidationException e) {
                        resultBinder.reject("Exception to get EntityType", null, null);
                    }
            else
                resultBinder.reject("no.subledger.cannot.create.rtgs.payment", null, null);
    }

    public void workflowTransition(final Paymentheader paymentheader,
            final Long approvalPosition, final String approvalComent, final String additionalRule,
            final String workFlowAction) {
        if (LOG.isDebugEnabled())
            LOG.debug(" Create WorkFlow Transition Started  ...");
        final User user = securityUtils.getCurrentUser();
        final DateTime currentDate = new DateTime();
        Position pos = null;
        Assignment wfInitiator = null;
        final String currState = "";
        if (null != paymentheader.getId())
            wfInitiator = assignmentService.getPrimaryAssignmentForUser(paymentheader.getCreatedBy().getId());
        if (FinancialConstants.BUTTONREJECT.toString().equalsIgnoreCase(workFlowAction)) {
            final String stateValue = FinancialConstants.WORKFLOW_STATE_REJECTED;
            paymentheader.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                    .withComments(approvalComent)
                    .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                    .withOwner(wfInitiator.getPosition())
                    .withNextAction("")
                    .withNatureOfTask(FinancialConstants.WORKFLOWTYPE_ADVANCE_PAYMENT_DISPLAYNAME);
        } else {
            if (null != approvalPosition && approvalPosition != -1 && !approvalPosition.equals(Long.valueOf(0)))
                pos = positionMasterService.getPositionById(approvalPosition);
            WorkFlowMatrix wfmatrix;
            if (null == paymentheader.getState()) {
                wfmatrix = advancePaymentWorkflowService.getWfMatrix(paymentheader.getStateType(), null,
                        null, additionalRule, currState, null);
                paymentheader.transition().start().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(FinancialConstants.WORKFLOWTYPE_ADVANCE_PAYMENT_DISPLAYNAME);
            } else if (FinancialConstants.BUTTONCANCEL.toString().equalsIgnoreCase(workFlowAction)) {
                final String stateValue = FinancialConstants.WORKFLOW_STATE_CANCELLED;
                paymentheader.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction("")
                        .withNatureOfTask(FinancialConstants.WORKFLOWTYPE_ADVANCE_PAYMENT_DISPLAYNAME);
            } else {
                wfmatrix = advancePaymentWorkflowService.getWfMatrix(paymentheader.getStateType(), null,
                        null, additionalRule, paymentheader.getCurrentState().getValue(), null);
                paymentheader.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments(approvalComent)
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(new Date()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(FinancialConstants.WORKFLOWTYPE_ADVANCE_PAYMENT_DISPLAYNAME);
            }
        }
        if (LOG.isDebugEnabled())
            LOG.debug(" WorkFlow Transition Completed  ...");
    }

    @Transactional
    public Paymentheader createPaymentHeader(final Paymentheader paymentHeader, final String workFlowAction,
            final Long approvalPosition,
            final String approvalComent, final String additionalRule) {
        if (LOG.isDebugEnabled())
            LOG.debug("Starting createPaymentHeader...");
        if (FinancialConstants.CREATEANDAPPROVE.equalsIgnoreCase(workFlowAction)
                && paymentHeader.getVoucherheader().getState() == null)
            paymentHeader.getVoucherheader().setStatus(FinancialConstants.CREATEDVOUCHERSTATUS);
        final Paymentheader savedPaymentHeader = paymentService.create(paymentHeader);
        savedPaymentHeader
                .getVoucherheader()
                .getVouchermis()
                .setSourcePath(
                        "/EGF/advancepayment/view/" + savedPaymentHeader.getId());
        if (!workFlowAction.equals(FinancialConstants.CREATEANDAPPROVE))
            workflowTransition(savedPaymentHeader, approvalPosition,
                    approvalComent, additionalRule,
                    workFlowAction);
        if (LOG.isDebugEnabled())
            LOG.debug("Completed createPaymentHeader.");
        return paymentHeader;
    }

    @Transactional
    public Paymentheader updatePaymentHeader(final Paymentheader paymentheader, final String workFlowAction,
            final Long approvalPosition,
            final String approvalComment, final String additionalRule) {
        final Paymentheader updatedPaymentHeader = paymentService.update(paymentheader);
        workflowTransition(updatedPaymentHeader, approvalPosition,
                approvalComment, null, workFlowAction);
        return paymentheader;
    }

    public EgBillregister getBillRegisterFromPayVhid(final Paymentheader paymentheader) {
        final Miscbilldetail miscbilldetail = miscbilldetailService.findByPayvhId(paymentheader.getVoucherheader().getId());
        return expenseBillService
                .getByVoucherHeaderId(miscbilldetail.getBillVoucherHeader().getId());
    }
}
