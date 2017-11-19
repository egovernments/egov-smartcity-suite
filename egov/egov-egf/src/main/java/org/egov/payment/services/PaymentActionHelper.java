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

package org.egov.payment.services;

import org.apache.log4j.Logger;
import org.egov.billsaccounting.services.CreateVoucher;
import org.egov.billsaccounting.services.VoucherConstant;
import org.egov.commons.Bankaccount;
import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.deduction.model.EgRemittance;
import org.egov.deduction.model.EgRemittanceDetail;
import org.egov.deduction.model.EgRemittanceGldtl;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.advance.EgAdvanceRequisition;
import org.egov.model.bills.EgBillregister;
import org.egov.model.bills.Miscbilldetail;
import org.egov.model.deduction.RemittanceBean;
import org.egov.model.payment.Paymentheader;
import org.egov.model.recoveries.Recovery;
import org.egov.model.voucher.CommonBean;
import org.egov.model.voucher.VoucherDetails;
import org.egov.model.voucher.WorkflowBean;
import org.egov.pims.commons.Position;
import org.egov.services.payment.MiscbilldetailService;
import org.egov.services.payment.PaymentService;
import org.egov.utils.FinancialConstants;
import org.hibernate.HibernateException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Transactional(readOnly = true)
@Service
public class PaymentActionHelper {

    public static final String ZERO = "0";
    private static final String FAILED = "Transaction failed";
    private static final String EXCEPTION_WHILE_SAVING_DATA = "Exception while saving data";
    @Autowired
    @Qualifier("miscbilldetailService")
    private MiscbilldetailService miscbilldetailService;
    @Autowired
    private FinancialYearHibernateDAO financialYearDAO;

    private static final Logger LOGGER = Logger.getLogger(PaymentActionHelper.class);

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    protected AssignmentService assignmentService;

    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<Paymentheader> paymentHeaderWorkflowService;

    @Autowired
    @Qualifier("createVoucher")
    private CreateVoucher createVoucher;
    @Autowired
    @Qualifier("paymentService")
    private PaymentService paymentService;

    @Autowired
    PositionMasterService positionMasterService;

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
                billVhId = (CVoucherHeader) persistenceService.getSession().load(CVoucherHeader.class,
                        commonBean.getDocumentId());
            createMiscBillDetail(billVhId, commonBean, voucherHeader);
            paymentheader = sendForApproval(paymentheader, workflowBean);
        } catch (final ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        return paymentheader;
    }

    @Transactional
    public Paymentheader createRemittancePayment(Paymentheader paymentheader, CVoucherHeader voucherHeader,
            Integer accountNumberId, String modeOfPayment, BigDecimal totalAmount, List<RemittanceBean> listRemitBean,
            Recovery recovery, RemittanceBean remittanceBean, String remittedTo, WorkflowBean workflowBean,
            HashMap<String, Object> headerDetails, CommonBean commonBean) {
        try {
            voucherHeader = createVoucherAndLedger(voucherHeader, remittanceBean, recovery, commonBean, headerDetails,
                    listRemitBean);
            paymentheader = paymentService.createPaymentHeader(voucherHeader, accountNumberId,
                    modeOfPayment, totalAmount);
            updateEgRemittanceglDtl(paymentheader.getVoucherheader(), listRemitBean, recovery);
            createMiscBillDetail(paymentheader.getVoucherheader(), remittanceBean, remittedTo);
            paymentheader = sendForApproval(paymentheader, workflowBean);
        } catch (final ValidationException e) {
            LOGGER.error(e.getMessage(), e);
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
        } catch (final Exception e) {

            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getMessage()));
            throw new ValidationException(errors);
        }
        return paymentheader;
    }

    @Transactional
    private CVoucherHeader createVoucherAndLedger(CVoucherHeader voucherHeader, RemittanceBean remittanceBean, Recovery recovery,
            CommonBean commonBean, HashMap<String, Object> headerDetails, List<RemittanceBean> listRemitBean) {
        headerDetails.put(VoucherConstant.SOURCEPATH, "/EGF/deduction/remitRecovery-beforeView.action?voucherHeader.id=");
        HashMap<String, Object> detailMap = null;
        final List<HashMap<String, Object>> accountdetails = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> subledgerDetails = new ArrayList<HashMap<String, Object>>();

        detailMap = new HashMap<String, Object>();
        detailMap.put(VoucherConstant.CREDITAMOUNT, remittanceBean.getTotalAmount().toString());
        detailMap.put(VoucherConstant.DEBITAMOUNT, ZERO);
        final Bankaccount account = (Bankaccount) persistenceService.find("from Bankaccount where id=?",
                Long.valueOf(commonBean.getAccountNumberId()));
        detailMap.put(VoucherConstant.GLCODE, account.getChartofaccounts().getGlcode());
        accountdetails.add(detailMap);
        detailMap = new HashMap<String, Object>();
        detailMap.put(VoucherConstant.CREDITAMOUNT, ZERO);
        detailMap.put(VoucherConstant.DEBITAMOUNT, remittanceBean.getTotalAmount().toString());
        recovery = (Recovery) persistenceService.find("from Recovery where id=?", remittanceBean.getRecoveryId());
        detailMap.put(VoucherConstant.GLCODE, recovery.getChartofaccounts().getGlcode());
        accountdetails.add(detailMap);
        subledgerDetails = addSubledgerGroupBy(subledgerDetails, recovery.getChartofaccounts().getGlcode(), listRemitBean);
        voucherHeader = createVoucher.createPreApprovedVoucher(headerDetails, accountdetails, subledgerDetails);
        return voucherHeader;
    }

    private List<HashMap<String, Object>> addSubledgerGroupBy(final List<HashMap<String, Object>> subledgerDetails,
            final String glcode, List<RemittanceBean> listRemitBean) {
        final Map<Integer, List<Integer>> detailTypesMap = new HashMap<Integer, List<Integer>>();
        Integer detailTypeId = null;
        final List<Integer> detailTypeList = new ArrayList<Integer>();
        HashMap<String, Object> subledgertDetailMap = null;
        for (final RemittanceBean rbean : listRemitBean)
        {
            detailTypeId = rbean.getDetailTypeId();
            if (detailTypeList.contains(detailTypeId))
            {
                if (detailTypesMap.get(detailTypeId).contains(rbean.getDetailKeyid()))
                    continue;
                else
                    detailTypesMap.get(detailTypeId).add(rbean.getDetailKeyid());

            }
            else
            {
                detailTypeList.add(detailTypeId);
                detailTypesMap.put(detailTypeId, new ArrayList<Integer>());
                detailTypesMap.get(detailTypeId).add(rbean.getDetailKeyid());

            }

        }
        final Set<Entry<Integer, List<Integer>>> entrySet = detailTypesMap.entrySet();
        final List<RemittanceBean> tempRemitBean = listRemitBean;
        for (final Entry<Integer, List<Integer>> o : entrySet)
        {
            final List<Integer> value = o.getValue();
            for (final Integer detailKey : value)
            {
                BigDecimal sumPerDetailKey = BigDecimal.ZERO;
                // int lastIndexOf = tempRemitBean.lastIndexOf(detailKey);
                for (final RemittanceBean remittanceBean2 : tempRemitBean)
                    if (remittanceBean2.getDetailKeyid() != null && remittanceBean2.getDetailKeyid().equals(detailKey))
                        sumPerDetailKey = sumPerDetailKey.add(remittanceBean2.getPartialAmount());
                subledgertDetailMap = new HashMap<String, Object>();
                subledgertDetailMap.put(VoucherConstant.DEBITAMOUNT, sumPerDetailKey);
                subledgertDetailMap.put(VoucherConstant.CREDITAMOUNT, BigDecimal.ZERO);
                subledgertDetailMap.put(VoucherConstant.DETAILTYPEID, o.getKey().toString());
                subledgertDetailMap.put(VoucherConstant.DETAILKEYID, detailKey);
                subledgertDetailMap.put(VoucherConstant.GLCODE, glcode);
                subledgerDetails.add(subledgertDetailMap);
            }

        }
        return subledgerDetails;
    }

    @Transactional
    public Paymentheader sendForApproval(Paymentheader paymentheader, WorkflowBean workflowBean) {

        if (paymentheader.getState()!=null && !validateOwner(paymentheader.getState())) {
            throw new ValidationException("exp", "Application does not belongs to this inbox");
        }
        if (FinancialConstants.CREATEANDAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())
                && paymentheader.getState() == null)
        {
            paymentheader.getVoucherheader().setStatus(
                    FinancialConstants.CREATEDVOUCHERSTATUS);
        }
        else
        {
            paymentService.transitionWorkFlow(paymentheader, workflowBean);
            paymentService.applyAuditing(paymentheader.getState());
        }
        paymentService.persist(paymentheader);
        return paymentheader;
    }

    private Boolean validateOwner(State state) {
        List<Position> positionsForUser = positionMasterService.getPositionsForEmployee(securityUtils.getCurrentUser().getId());
        return positionsForUser.contains(state.getOwnerPosition());
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
    private void updateEgRemittanceglDtl(final CVoucherHeader vh,
            List<RemittanceBean> listRemitBean, Recovery recovery) {
        final EgRemittance remit = new EgRemittance();
        remit.setFund(vh.getFundId());
        remit.setRecovery(recovery);
        final CFinancialYear financialYearByDate = financialYearDAO.getFinancialYearByDate(vh.getVoucherDate());
        remit.setFinancialyear(financialYearByDate);
        remit.setCreateddate(new Date());
        remit.setCreatedby(BigDecimal.valueOf(ApplicationThreadLocals.getUserId()));
        remit.setLastmodifiedby(BigDecimal.valueOf(ApplicationThreadLocals.getUserId()));
        remit.setLastmodifieddate(new Date());
        remit.setMonth(BigDecimal.valueOf(new Date().getMonth()));
        remit.setVoucherheader(vh);
        remit.setAsOnDate(vh.getVoucherDate());
        final Set<EgRemittanceDetail> egRemittanceDetail = new HashSet<EgRemittanceDetail>();
        EgRemittanceDetail remitDetail = null;
        final Date currDate = new Date();
        for (final RemittanceBean rbean : listRemitBean)
        {
            final EgRemittanceGldtl remittancegldtl = (EgRemittanceGldtl) persistenceService.find(
                    "from EgRemittanceGldtl where id=?",
                    rbean.getRemittance_gl_dtlId());
            remittancegldtl.setRemittedamt(rbean.getPartialAmount());
            persistenceService.persist(remittancegldtl);

            remitDetail = new EgRemittanceDetail();
            remitDetail.setEgRemittance(remit);
            remitDetail.setEgRemittanceGldtl(remittancegldtl);
            remitDetail.setRemittedamt(rbean.getPartialAmount());
            remitDetail.setLastmodifieddate(currDate);
            egRemittanceDetail.add(remitDetail);

        }
        remit.setEgRemittanceDetail(egRemittanceDetail);
        persistenceService.persist(remit);
    }

    @Transactional
    private void createMiscBillDetail(CVoucherHeader voucherHeader, RemittanceBean remittanceBean, String remittedTo) {
        final Miscbilldetail miscbillDetail = new Miscbilldetail();
        // miscbillDetail.setBillnumber(commonBean.getDocumentNumber());
        // miscbillDetail.setBilldate(commonBean.getDocumentDate());
        miscbillDetail.setBillamount(remittanceBean.getTotalAmount());
        miscbillDetail.setPaidamount(remittanceBean.getTotalAmount());
        miscbillDetail.setPassedamount(remittanceBean.getTotalAmount());
        miscbillDetail.setPayVoucherHeader(voucherHeader);
        // miscbillDetail.setBillVoucherHeader(vhid);
        miscbillDetail.setPaidto(remittedTo);
        persistenceService.persist(miscbillDetail);

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
                paymentheader.transition().end().withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            } else {
                final String stateValue = FinancialConstants.WORKFLOW_STATE_REJECTED;
                paymentheader.transition().progressWithStateCopy().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                        .withStateValue(stateValue).withDateInfo(currentDate.toDate())
                        .withOwner(wfInitiator.getPosition()).withNextAction(FinancialConstants.WF_STATE_EOA_Approval_Pending);
            }

        } else if (FinancialConstants.BUTTONAPPROVE.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            paymentheader.getVoucherheader().setStatus(FinancialConstants.CREATEDVOUCHERSTATUS);
            paymentheader.transition().end().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
                    .withDateInfo(currentDate.toDate());
        } else if (FinancialConstants.BUTTONCANCEL.equalsIgnoreCase(workflowBean.getWorkFlowAction())) {
            paymentheader.getVoucherheader().setStatus(FinancialConstants.CANCELLEDVOUCHERSTATUS);
            paymentheader.transition().end().withStateValue(FinancialConstants.WORKFLOW_STATE_CANCELLED)
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
                paymentheader.transition().end().withSenderName(user.getName())
                        .withComments(workflowBean.getApproverComments())
                        .withDateInfo(currentDate.toDate());
            else {
                final WorkFlowMatrix wfmatrix = paymentHeaderWorkflowService.getWfMatrix(paymentheader.getStateType(), null,
                        null, null, paymentheader.getCurrentState().getValue(), null);
                paymentheader.transition().progressWithStateCopy().withSenderName(user.getName()).withComments(workflowBean.getApproverComments())
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
            final Bankaccount account = (Bankaccount) persistenceService.getSession().load(Bankaccount.class,
                    Long.valueOf(commonBean.getAccountNumberId()));
            detailMap.put(VoucherConstant.GLCODE, account.getChartofaccounts().getGlcode());
            accountdetails.add(detailMap);
            final Map<String, Object> glcodeMap = new HashMap<String, Object>();
            for (final VoucherDetails voucherDetail : billDetailslist)

            {
                detailMap = new HashMap<String, Object>();
                if (voucherDetail.getFunctionIdDetail() != null) {
                    final CFunction function = (CFunction) persistenceService.getSession().load(CFunction.class,
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
            final List<ValidationError> errors = new ArrayList<ValidationError>();
            errors.add(new ValidationError("exp", e.getErrors().get(0).getMessage()));
            throw new ValidationException(errors);
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