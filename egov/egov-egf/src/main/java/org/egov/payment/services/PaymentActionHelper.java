package org.egov.payment.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.workflow.WorkFlowMatrix;
import org.egov.model.advance.EgAdvanceRequisition;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.payment.Paymentheader;
import org.egov.model.voucher.CommonBean;
import org.egov.model.voucher.VoucherDetails;
import org.egov.model.voucher.WorkflowBean;
import org.egov.pims.commons.Position;
import org.egov.services.payment.MiscbilldetailService;
import org.egov.services.payment.PaymentService;
import org.egov.utils.FinancialConstants;
import org.elasticsearch.common.joda.time.DateTime;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PaymentActionHelper {

    public static final String ZERO = "0";
    private static final String FAILED = "Transaction failed";
    private static final String EXCEPTION_WHILE_SAVING_DATA = "Exception while saving data";
    @Autowired
    @Qualifier("miscbilldetailService")
    private MiscbilldetailService miscbilldetailService;

    private static final Logger LOGGER = Logger.getLogger(PaymentActionHelper.class);

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    private SimpleWorkflowService<Paymentheader> paymentHeaderWorkflowService;

    @Autowired
    @Qualifier("createVoucher")
    private CreateVoucher createVoucher;
    @Autowired
    @Qualifier("paymentService")
    private PaymentService paymentService;

    @Transactional
    public Paymentheader createDirectBankPayment(Paymentheader paymentheader, CVoucherHeader voucherHeader,
            CVoucherHeader billVhId, CommonBean commonBean,
            List<VoucherDetails> billDetailslist, List<VoucherDetails> subLedgerlist, WorkflowBean workflowBean)
    {
        try {
        voucherHeader = createVoucherAndledger(voucherHeader, commonBean, billDetailslist, subLedgerlist);
        paymentheader = paymentService.createPaymentHeader(voucherHeader,
                Integer.valueOf(commonBean.getAccountNumberId()), commonBean
                        .getModeOfPayment(), commonBean.getAmount());
        if (commonBean.getDocumentId() != null)
            billVhId = (CVoucherHeader) HibernateUtil.getCurrentSession().load(CVoucherHeader.class,
                    commonBean.getDocumentId());
        createMiscBillDetail(billVhId, commonBean, voucherHeader);
        paymentheader = sendForApproval(paymentheader,workflowBean);
        } catch (final Exception e) {
            e.printStackTrace();
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        return paymentheader;
    }

    @Transactional
    public Paymentheader sendForApproval(Paymentheader paymentheader, WorkflowBean workflowBean) {
        paymentService.transitionWorkFlow(paymentheader, workflowBean);
        paymentService.applyAuditing(paymentheader.getState());
        paymentService.persist(paymentheader);
        return paymentheader;
    }

    @Transactional
    public EgBillregister setbillRegisterFunction(EgBillregister bill, CFunction function)
    {
        bill.getEgBillregistermis().setFunction(function);
        return bill;
    }

    @Transactional
    public List<Miscbilldetail> getPaymentBills(Paymentheader paymentheader)
    {
        List<Miscbilldetail> miscBillList = null;
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside getPaymentBills");
        try {
            miscBillList = miscbilldetailService.findAllBy(
                    " from Miscbilldetail where payVoucherHeader.id = ? order by paidto",
                    paymentheader.getVoucherheader().getId());

        } catch (final Exception e) {
            throw new ValidationException("", "Total Paid Amount Exceeding Net Amount For This Bill");
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Retrived bill details fro the paymentheader");

        return miscBillList;
    }

    protected Assignment getWorkflowInitiator(final Paymentheader paymentheader) {
        Assignment wfInitiator = assignmentService.getPrimaryAssignmentForUser(paymentheader.getCreatedBy().getId());
        return wfInitiator;
    }

    @Transactional
    public void transitionWorkFlow(final Paymentheader paymentheader, WorkflowBean workflowBean) {
        final DateTime currentDate = new DateTime();
        final User user = securityUtils.getCurrentUser();
        final Assignment userAssignment = assignmentService.getPrimaryAssignmentForUser(user.getId());
        Position pos = null;
        Assignment wfInitiator = null;

        if (null != paymentheader.getId())
            wfInitiator = getWorkflowInitiator(paymentheader);

        if (FinancialConstants.BUTTONREJECT.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            if (wfInitiator.equals(userAssignment)) {
                paymentheader.transition(true).end().withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            } else {
                final String stateValue = FinancialConstants.WORKFLOW_STATE_REJECTED;
                paymentheader.transition(true).withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction(FinancialConstants.WF_STATE_EOA_Approval_Pending);
            }

        } else if (FinancialConstants.BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            paymentheader.getVoucherheader().setStatus(FinancialConstants.CREATEDVOUCHERSTATUS);
            paymentheader.transition(true).end().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                    .withDateInfo(currentDate.toDate());
        } else if (FinancialConstants.BUTTONCANCEL.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            paymentheader.getVoucherheader().setStatus(FinancialConstants.CANCELLEDVOUCHERSTATUS);
            paymentheader.transition(true).end().withStateValue(FinancialConstants.WORKFLOW_STATE_CANCELLED)
                    .withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                    .withDateInfo(currentDate.toDate());
        } else {
            if (null != workflowBean.getApproverPositionId() && workflowBean.getApproverPositionId() != -1)
                pos = (Position) persistenceService.find("from Position where id=?", workflowBean.getApproverPositionId());
            if (null == paymentheader.getState()) {
                final WorkFlowMatrix wfmatrix = paymentHeaderWorkflowService.getWfMatrix(paymentheader.getStateType(), null,
                        null, null, workflowBean.getCurrentState(), null);
                paymentheader.transition().start().withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            } else if (paymentheader.getCurrentState().getNextAction().equalsIgnoreCase("END"))
                paymentheader.transition(true).end().withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            else {
                final WorkFlowMatrix wfmatrix = paymentHeaderWorkflowService.getWfMatrix(paymentheader.getStateType(), null,
                        null, null, paymentheader.getCurrentState().getValue(), null);
                paymentheader.transition(true).withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withStateValue(wfmatrix.getNextState()).withDateInfo(currentDate.toDate()).withOwner(pos)
                        .withNextAction(wfmatrix.getNextAction());
            }
        }
    }

    @Transactional
    public List<EgAdvanceRequisition> getAdvanceRequisitionDetails(Paymentheader paymentheader) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Inside getAdvanceRequisitionDetails");
        return persistenceService.findAllBy("from EgAdvanceRequisition where egAdvanceReqMises.voucherheader.id=?", paymentheader
                .getVoucherheader().getId());
    }

    @Transactional
    private CVoucherHeader createVoucherAndledger(CVoucherHeader voucherHeader, CommonBean commonBean,
            List<VoucherDetails> billDetailslist, List<VoucherDetails> subLedgerlist) {
        try {
            final HashMap<String, Object> headerDetails = createHeaderAndMisDetails(voucherHeader);
            // update DirectBankPayment source path
            headerDetails.put(VoucherConstant.SOURCEPATH, "/EGF/payment/directBankPayment-beforeView.action?voucherHeader.id=");
            HashMap<String, Object> detailMap = null;
            HashMap<String, Object> subledgertDetailMap = null;
            final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
            final List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();

            detailMap = new HashMap<String, Object>();
            detailMap.put(VoucherConstant.CREDITAMOUNT, commonBean.getAmount().toString());
            detailMap.put(VoucherConstant.DEBITAMOUNT, ZERO);
            final Bankaccount account = (Bankaccount) HibernateUtil.getCurrentSession().load(Bankaccount.class,
                    Long.valueOf(commonBean.getAccountNumberId()));
            detailMap.put(VoucherConstant.GLCODE, account.getChartofaccounts().getGlcode());
            accountdetails.add(detailMap);
            final Map<String, Object> glcodeMap = new HashMap<String, Object>();
            for (final VoucherDetails voucherDetail : billDetailslist)

            {
                detailMap = new HashMap<String, Object>();
                if (voucherDetail.getFunctionIdDetail() != null) {
                    final CFunction function = (CFunction) HibernateUtil.getCurrentSession().load(CFunction.class,
                            voucherDetail.getFunctionIdDetail());
                    detailMap.put(VoucherConstant.FUNCTIONCODE, function.getCode());
                }
                if (voucherDetail.getCreditAmountDetail().compareTo(BigDecimal.ZERO) == 0) {

                    detailMap.put(VoucherConstant.DEBITAMOUNT, voucherDetail.getDebitAmountDetail().toString());
                    detailMap.put(VoucherConstant.CREDITAMOUNT, ZERO);
                    detailMap.put(VoucherConstant.GLCODE, voucherDetail.getGlcodeDetail());
                    accountdetails.add(detailMap);
                    glcodeMap.put(voucherDetail.getGlcodeDetail(), VoucherConstant.DEBIT);
                }
                else {
                    detailMap.put(VoucherConstant.CREDITAMOUNT, voucherDetail.getCreditAmountDetail().toString());
                    detailMap.put(VoucherConstant.DEBITAMOUNT, ZERO);
                    detailMap.put(VoucherConstant.GLCODE, voucherDetail.getGlcodeDetail());
                    accountdetails.add(detailMap);
                    glcodeMap.put(voucherDetail.getGlcodeDetail(), VoucherConstant.CREDIT);
                }

            }

            for (final VoucherDetails voucherDetail : subLedgerlist) {
                subledgertDetailMap = new HashMap<String, Object>();
                final String amountType = glcodeMap.get(voucherDetail.getSubledgerCode()) != null ? glcodeMap.get(
                        voucherDetail.getSubledgerCode()).toString() : null; // Debit or Credit.
                if (null != amountType && amountType.equalsIgnoreCase(VoucherConstant.DEBIT))
                    subledgertDetailMap.put(VoucherConstant.DEBITAMOUNT, voucherDetail.getAmount());
                else if (null != amountType)
                    subledgertDetailMap.put(VoucherConstant.CREDITAMOUNT, voucherDetail.getAmount());
                subledgertDetailMap.put(VoucherConstant.DETAILTYPEID, voucherDetail.getDetailType().getId());
                subledgertDetailMap.put(VoucherConstant.DETAILKEYID, voucherDetail.getDetailKeyId());
                subledgertDetailMap.put(VoucherConstant.GLCODE, voucherDetail.getSubledgerCode());
                subledgerDetails.add(subledgertDetailMap);
            }

            voucherHeader = createVoucher.createPreApprovedVoucher(headerDetails, accountdetails, subledgerDetails);

        } catch (final HibernateException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(EXCEPTION_WHILE_SAVING_DATA, FAILED)));
        } catch (final ApplicationRuntimeException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        } catch (final ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } catch (final Exception e) {
            // handle engine exception
            LOGGER.error(e.getMessage(), e);
            throw new ValidationException(Arrays.asList(new ValidationError(e.getMessage(), e.getMessage())));
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Posted to Ledger " + voucherHeader.getId());
        return voucherHeader;

    }

    protected HashMap<String, Object> createHeaderAndMisDetails(CVoucherHeader voucherHeader) throws ValidationException
    {
        final HashMap<String, Object> headerdetails = new HashMap<String, Object>();
        headerdetails.put(VoucherConstant.VOUCHERNAME, voucherHeader.getName());
        headerdetails.put(VoucherConstant.VOUCHERTYPE, voucherHeader.getType());
        headerdetails.put((String) VoucherConstant.VOUCHERSUBTYPE, voucherHeader.getVoucherSubType());
        headerdetails.put(VoucherConstant.VOUCHERNUMBER, voucherHeader.getVoucherNumber());
        headerdetails.put(VoucherConstant.VOUCHERDATE, voucherHeader.getVoucherDate());
        headerdetails.put(VoucherConstant.DESCRIPTION, voucherHeader.getDescription());

        if (voucherHeader.getVouchermis().getDepartmentid() != null)
            headerdetails.put(VoucherConstant.DEPARTMENTCODE, voucherHeader.getVouchermis().getDepartmentid().getCode());
        if (voucherHeader.getFundId() != null)
            headerdetails.put(VoucherConstant.FUNDCODE, voucherHeader.getFundId().getCode());
        if (voucherHeader.getVouchermis().getSchemeid() != null)
            headerdetails.put(VoucherConstant.SCHEMECODE, voucherHeader.getVouchermis().getSchemeid().getCode());
        if (voucherHeader.getVouchermis().getSubschemeid() != null)
            headerdetails.put(VoucherConstant.SUBSCHEMECODE, voucherHeader.getVouchermis().getSubschemeid().getCode());
        if (voucherHeader.getVouchermis().getFundsource() != null)
            headerdetails.put(VoucherConstant.FUNDSOURCECODE, voucherHeader.getVouchermis().getFundsource().getCode());
        if (voucherHeader.getVouchermis().getDivisionid() != null)
            headerdetails.put(VoucherConstant.DIVISIONID, voucherHeader.getVouchermis().getDivisionid().getId());
        if (voucherHeader.getVouchermis().getFunctionary() != null)
            headerdetails.put(VoucherConstant.FUNCTIONARYCODE, voucherHeader.getVouchermis().getFunctionary().getCode());
        if (voucherHeader.getVouchermis().getFunction() != null)
            headerdetails.put(VoucherConstant.FUNCTIONCODE, voucherHeader.getVouchermis().getFunction().getCode());
        return headerdetails;
    }

    @Transactional
    private void createMiscBillDetail(final CVoucherHeader billVhId, CommonBean commonBean, CVoucherHeader voucherHeader) {
        final Miscbilldetail miscbillDetail = new Miscbilldetail();
        miscbillDetail.setBillnumber(commonBean.getDocumentNumber());
        miscbillDetail.setBilldate(commonBean.getDocumentDate());
        miscbillDetail.setBillamount(commonBean.getAmount());
        miscbillDetail.setPaidamount(commonBean.getAmount());
        miscbillDetail.setPassedamount(commonBean.getAmount());
        miscbillDetail.setPayVoucherHeader(voucherHeader);
        miscbillDetail.setBillVoucherHeader(billVhId);
        miscbillDetail.setPaidto(commonBean.getPaidTo().trim());
        miscbilldetailService.persist(miscbillDetail);

    }
}
