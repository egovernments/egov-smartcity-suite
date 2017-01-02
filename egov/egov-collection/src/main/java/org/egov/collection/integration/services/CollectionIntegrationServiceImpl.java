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
package org.egov.collection.integration.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.OnlinePayment;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.entity.ReceiptVoucher;
import org.egov.collection.integration.models.BillInfo;
import org.egov.collection.integration.models.BillInfoImpl;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.models.PaymentInfo;
import org.egov.collection.integration.models.PaymentInfoBank;
import org.egov.collection.integration.models.PaymentInfoCash;
import org.egov.collection.integration.models.PaymentInfoChequeDD;
import org.egov.collection.integration.models.PaymentInfoRequest;
import org.egov.collection.integration.models.PaymentInfoSearchRequest;
import org.egov.collection.integration.models.RestAggregatePaymentInfo;
import org.egov.collection.integration.models.RestReceiptInfo;
import org.egov.collection.integration.pgi.PaymentRequest;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionCommon;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.commons.CVoucherHeader;
import org.egov.commons.Fund;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.FundHibernateDAO;
import org.egov.commons.entity.Source;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infra.validation.exception.ValidationException;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentHeader;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * Collections integration service implementation - exposes APIs that can be
 * used by other applications (typically billing systems) to interact with the
 * collections module.
 */
@Transactional(readOnly = true)
public class CollectionIntegrationServiceImpl extends PersistenceService<ReceiptHeader, Long> implements
CollectionIntegrationService {

    private static final Logger LOGGER = Logger.getLogger(CollectionIntegrationServiceImpl.class);

    @Autowired
    private FundHibernateDAO fundHibernateDAO;
    private PersistenceService persistenceService;

    private CollectionsUtil collectionsUtil;

    private ReceiptHeaderService receiptHeaderService;

    @Autowired
    private EgwStatusHibernateDAO statusDAO;

    private final List<ValidationError> errors = new ArrayList<>(0);

    private CollectionCommon collectionCommon;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    @Autowired
    private FundHibernateDAO fundDAO;

    @Autowired
    private ApplicationContext beanProvider;

    public CollectionIntegrationServiceImpl() {
        super(ReceiptHeader.class);
    }

    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    /*
     * (non-Javadoc)
     * @seeorg.egov.infstr.collections.integration.ICollectionInterface#
     * getBillReceiptInfo(java.lang.String, java.lang.String)
     */
    @Override
    public List<BillReceiptInfo> getBillReceiptInfo(final String serviceCode, final String refNum) {
        final ArrayList<BillReceiptInfo> receipts = new ArrayList<>(0);

        // Get all receipt headers for given reference number
        final List<ReceiptHeader> receiptHeaders = findAllByNamedQuery(
                CollectionConstants.QUERY_RECEIPTS_BY_REFNUM_AND_SERVICECODE, refNum, serviceCode);
        if (receiptHeaders == null || receiptHeaders.isEmpty())
            return Collections.emptyList();

        for (final ReceiptHeader receiptHeader : receiptHeaders)
            receipts.add(new BillReceiptInfoImpl(receiptHeader, chartOfAccountsHibernateDAO, persistenceService, null));
        return receipts;
    }

    /*
     * (non-Javadoc)
     * @seeorg.egov.infstr.collections.integration.ICollectionInterface#
     * getBillReceiptInfo(java.lang.String, java.util.Set)
     */
    @Override
    public Map<String, List<BillReceiptInfo>> getBillReceiptInfo(final String serviceCode, final Set<String> refNums) {
        final HashMap<String, List<BillReceiptInfo>> receipts = new HashMap<>(0);

        for (final String refNum : refNums)
            receipts.put(refNum, getBillReceiptInfo(serviceCode, refNum));

        return receipts;
    }

    /*
     * (non-Javadoc)
     * @seeorg.egov.infstr.collections.integration.ICollectionInterface#
     * getInstrumentReceiptInfo(java.lang.String, java.lang.String)
     */
    @Override
    public List<BillReceiptInfo> getInstrumentReceiptInfo(final String serviceCode, final String instrumentNum) {
        final ArrayList<BillReceiptInfo> receipts = new ArrayList<>(0);

        final List<ReceiptHeader> receiptHeaders = findAllByNamedQuery(
                CollectionConstants.QUERY_RECEIPTS_BY_INSTRUMENTNO_AND_SERVICECODE, instrumentNum, serviceCode);
        if (receiptHeaders == null || receiptHeaders.isEmpty())
            return Collections.emptyList();

        for (final ReceiptHeader receiptHeader : receiptHeaders)
            receipts.add(new BillReceiptInfoImpl(receiptHeader, chartOfAccountsHibernateDAO, persistenceService, null));
        return receipts;
    }

    /*
     * (non-Javadoc)
     * @seeorg.egov.infstr.collections.integration.ICollectionInterface#
     * getInstrumentReceiptInfo(java.lang.String, java.util.Set)
     */
    @Override
    public Map<String, List<BillReceiptInfo>> getInstrumentReceiptInfo(final String serviceCode,
            final Set<String> instrumentNums) {
        final HashMap<String, List<BillReceiptInfo>> receipts = new HashMap<>(0);

        for (final String instrumentNum : instrumentNums)
            receipts.put(instrumentNum, getInstrumentReceiptInfo(serviceCode, instrumentNum));

        return receipts;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.egov.infstr.collections.integration.CollectionIntegrationService#
     * getReceiptInfo (java.lang.String, java.lang.String)
     */
    @Override
    public BillReceiptInfo getReceiptInfo(final String serviceCode, final String receiptNum) {
        final ReceiptHeader receiptHeader = findByNamedQuery(
                CollectionConstants.QUERY_RECEIPT_BY_RECEIPTNUM_AND_SERVICECODE, serviceCode, receiptNum, receiptNum);
        if (receiptHeader == null)
            return null;
        else
            // Create bill receipt info
            return new BillReceiptInfoImpl(receiptHeader, chartOfAccountsHibernateDAO, persistenceService, null);
    }

    @Override
    public RestReceiptInfo getDetailsByTransactionId(final PaymentInfoSearchRequest paymentInfoSearchRequest) {
        LOGGER.info(paymentInfoSearchRequest.getSource());
        final ReceiptHeader header = find("from ReceiptHeader r where r.manualreceiptnumber=? and r.source=? ",
                paymentInfoSearchRequest.getTransactionId(), paymentInfoSearchRequest.getSource());
        if (header == null)
            throw new ApplicationRuntimeException("No data found");
        final RestReceiptInfo restReceiptInfo = new RestReceiptInfo(header);
        final PaymentInfoService paymentInfoService = (PaymentInfoService) beanProvider.getBean("paymentInfoService");
        paymentInfoService.setPaymentInfo(restReceiptInfo, header);
        return restReceiptInfo;

    }

    /*
     * (non-Javadoc)
     * @see
     * org.egov.infstr.collections.integration.CollectionIntegrationService#
     * getReceiptInfo (java.lang.String, java.util.Set)
     */
    @Override
    public Map<String, BillReceiptInfo> getReceiptInfo(final String serviceCode, final Set<String> receiptNums) {
        final HashMap<String, BillReceiptInfo> receipts = new HashMap<>(0);

        for (final String receiptNum : receiptNums)
            receipts.put(receiptNum, getReceiptInfo(serviceCode, receiptNum));

        return receipts;
    }

    /*
     * @see
     * org.egov.infstr.collections.integration.CollectionIntegrationService#
     * createReceipt (BillInfo bill, List<PaymentInfo> paymentInfoList)
     */
    @Override
    public BillReceiptInfo createReceipt(final BillInfo bill, final List<PaymentInfo> paymentInfoList) {
        LOGGER.info("Logs for CreateReceipt : Receipt Creation Started....");
        final Fund fund = fundHibernateDAO.fundByCode(bill.getFundCode());
        if (fund == null)
            throw new ApplicationRuntimeException("Fund not present for the fund code [" + bill.getFundCode() + "].");

        final Department dept = (Department) persistenceService.findByNamedQuery(
                CollectionConstants.QUERY_DEPARTMENT_BY_CODE, bill.getDepartmentCode());

        if (dept == null)
            throw new ApplicationRuntimeException("Department not present for the department code ["
                    + bill.getDepartmentCode() + "].");
        final ReceiptHeader receiptHeader = collectionCommon.initialiseReceiptModelWithBillInfo(bill, fund, dept);

        receiptHeader.setCreatedDate(new Date());
        receiptHeader.setReceiptdate(new Date());
        receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_BILL);
        receiptHeader.setIsModifiable(Boolean.TRUE);
        receiptHeader.setIsReconciled(Boolean.FALSE);
        receiptHeader.setCollectiontype(CollectionConstants.COLLECTION_TYPE_FIELDCOLLECTION);
        receiptHeader.setSource(bill.getSource() != null ? bill.getSource() : "");

        receiptHeader.setStatus(collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_APPROVED));

        receiptHeader.setPaidBy(bill.getPaidBy());

        if (ApplicationThreadLocals.getUserId() != null) {
            final User user = collectionsUtil.getUserById(ApplicationThreadLocals.getUserId());
            receiptHeader.setCreatedBy(user);
            receiptHeader.setLastModifiedBy(user);
            receiptHeader.setLastModifiedDate(new Date());
        }

        BigDecimal chequeDDInstrumenttotal = BigDecimal.ZERO;
        BigDecimal otherInstrumenttotal = BigDecimal.ZERO;

        // populate instrument details
        final List<InstrumentHeader> instrumentHeaderList = new ArrayList<>(0);

        for (final PaymentInfo paytInfo : paymentInfoList) {
            final String instrType = paytInfo.getInstrumentType().toString();

            if (CollectionConstants.INSTRUMENTTYPE_CASH.equals(instrType)) {
                final PaymentInfoCash paytInfoCash = (PaymentInfoCash) paytInfo;

                instrumentHeaderList.add(collectionCommon.validateAndConstructCashInstrument(paytInfoCash));
                otherInstrumenttotal = paytInfo.getInstrumentAmount();
            }

            if (CollectionConstants.INSTRUMENTTYPE_BANK.equals(instrType)) {
                final PaymentInfoBank paytInfoBank = (PaymentInfoBank) paytInfo;

                instrumentHeaderList.add(collectionCommon.validateAndConstructBankInstrument(paytInfoBank));

                otherInstrumenttotal = paytInfoBank.getInstrumentAmount();
            }

            if (CollectionConstants.INSTRUMENTTYPE_CHEQUE.equals(instrType)
                    || CollectionConstants.INSTRUMENTTYPE_DD.equals(instrType)) {

                final PaymentInfoChequeDD paytInfoChequeDD = (PaymentInfoChequeDD) paytInfo;

                instrumentHeaderList.add(collectionCommon.validateAndConstructChequeDDInstrument(paytInfoChequeDD));

                chequeDDInstrumenttotal = chequeDDInstrumenttotal.add(paytInfoChequeDD.getInstrumentAmount());
            }
        }
        BigDecimal debitAmount = BigDecimal.ZERO;

        for (final ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails()) {
            debitAmount = debitAmount.add(receiptDetail.getCramount());
            debitAmount = debitAmount.subtract(receiptDetail.getDramount());
        }

        receiptHeader.addReceiptDetail(collectionCommon.addDebitAccountHeadDetails(debitAmount, receiptHeader,
                chequeDDInstrumenttotal, otherInstrumenttotal, paymentInfoList.get(0).getInstrumentType().toString()));

        receiptHeaderService.persistFieldReceipt(receiptHeader, instrumentHeaderList);
        LOGGER.info("Logs for CreateReceipt : Receipt Creation Finished....");
        return new BillReceiptInfoImpl(receiptHeader, chartOfAccountsHibernateDAO, persistenceService, null);
    }

    /*
     * (non-Javadoc)
     * @seeorg.egov.infstr.collections.integration.ICollectionInterface#
     * getPendingReceiptsInfo(java.lang.String, java.lang.String)
     */
    @Override
    public List<BillReceiptInfo> getOnlinePendingReceipts(final String serviceCode, final String consumerCode) {
        final ArrayList<BillReceiptInfo> receipts = new ArrayList<>(0);
        final List<ReceiptHeader> receiptHeaders = findAllByNamedQuery(
                CollectionConstants.QUERY_ONLINE_PENDING_RECEIPTS_BY_CONSUMERCODE_AND_SERVICECODE, serviceCode,
                consumerCode, CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING);
        if (receiptHeaders == null || receiptHeaders.isEmpty())
            return Collections.emptyList();
        else {
            for (final ReceiptHeader receiptHeader : receiptHeaders)
                receipts.add(new BillReceiptInfoImpl(receiptHeader, chartOfAccountsHibernateDAO, persistenceService,
                        null));
            return receipts;
        }

    }

    /*
     * @see
     * org.egov.infstr.collections.integration.CollectionIntegrationService#
     * createMiscellaneousReceipt (BillInfo bill, List<PaymentInfo>
     * paymentInfoList)
     */
    @Override
    public BillReceiptInfo createMiscellaneousReceipt(final BillInfo bill, final List<PaymentInfo> paymentInfoList) {
        LOGGER.info("Logs For Miscellaneous Receipt : Receipt Creation Started....");
        final Fund fund = fundHibernateDAO.fundByCode(bill.getFundCode());
        if (fund == null)
            throw new ApplicationRuntimeException("Fund not present for the fund code [" + bill.getFundCode() + "].");

        final Department dept = (Department) persistenceService.findByNamedQuery(
                CollectionConstants.QUERY_DEPARTMENT_BY_CODE, bill.getDepartmentCode());

        if (dept == null)
            throw new ApplicationRuntimeException("Department not present for the department code ["
                    + bill.getDepartmentCode() + "].");

        final ReceiptHeader receiptHeader = collectionCommon.initialiseReceiptModelWithBillInfo(bill, fund, dept);

        receiptHeader.setCreatedDate(new Date());
        receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_ADHOC);
        receiptHeader.setIsModifiable(Boolean.TRUE);
        receiptHeader.setIsReconciled(Boolean.TRUE);
        receiptHeader.setCollectiontype(CollectionConstants.COLLECTION_TYPE_COUNTER);

        receiptHeader.setStatus(collectionsUtil.getStatusForModuleAndCode(
                CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_APPROVED));

        receiptHeader.setPaidBy(bill.getPaidBy());

        if (ApplicationThreadLocals.getUserId() != null)
            receiptHeader.setCreatedBy(collectionsUtil.getUserById(ApplicationThreadLocals.getUserId()));

        final BigDecimal chequeDDInstrumenttotal = BigDecimal.ZERO;
        final BigDecimal otherInstrumenttotal = BigDecimal.ZERO;

        /*
         * // populate instrument details List<InstrumentHeader>
         * instrumentHeaderList = new ArrayList<InstrumentHeader>(); for
         * (PaymentInfo paytInfo : paymentInfoList) { String instrType =
         * paytInfo.getInstrumentType().toString(); if
         * (CollectionConstants.INSTRUMENTTYPE_CASH.equals(instrType)) {
         * PaymentInfoCash paytInfoCash = (PaymentInfoCash) paytInfo;
         * instrumentHeaderList
         * .add(collectionCommon.validateAndConstructCashInstrument
         * (paytInfoCash)); otherInstrumenttotal =
         * paytInfo.getInstrumentAmount(); } if
         * (CollectionConstants.INSTRUMENTTYPE_BANK.equals(instrType)) {
         * PaymentInfoBank paytInfoBank = (PaymentInfoBank) paytInfo;
         * instrumentHeaderList
         * .add(collectionCommon.validateAndConstructBankInstrument
         * (paytInfoBank)); otherInstrumenttotal =
         * paytInfoBank.getInstrumentAmount(); } if
         * (CollectionConstants.INSTRUMENTTYPE_CHEQUE.equals(instrType) ||
         * CollectionConstants.INSTRUMENTTYPE_DD.equals(instrType)) {
         * PaymentInfoChequeDD paytInfoChequeDD = (PaymentInfoChequeDD)
         * paytInfo; instrumentHeaderList.add(collectionCommon.
         * validateAndConstructChequeDDInstrument(paytInfoChequeDD));
         * chequeDDInstrumenttotal =
         * chequeDDInstrumenttotal.add(paytInfoChequeDD.getInstrumentAmount());
         * } } instrumentHeaderList =
         * receiptHeaderService.createInstrument(instrumentHeaderList);
         * LOGGER.info("        Instrument List created ");
         * receiptHeader.setReceiptInstrument(new
         * HashSet(instrumentHeaderList));
         */

        BigDecimal debitAmount = BigDecimal.ZERO;

        for (final ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails()) {
            debitAmount = debitAmount.add(receiptDetail.getCramount());
            debitAmount = debitAmount.subtract(receiptDetail.getDramount());
        }

        receiptHeader.addReceiptDetail(collectionCommon.addDebitAccountHeadDetails(debitAmount, receiptHeader,
                chequeDDInstrumenttotal, otherInstrumenttotal, paymentInfoList.get(0).getInstrumentType().toString()));

        receiptHeaderService.persist(receiptHeader);
        receiptHeaderService.getSession().flush();
        LOGGER.info("Miscellaneous Receipt Created with receipt number: " + receiptHeader.getReceiptnumber());

        try {
            receiptHeaderService.createVoucherForReceipt(receiptHeader);
            LOGGER.debug("Updated financial systems and created voucher.");
        } catch (final ApplicationRuntimeException ex) {
            errors.add(new ValidationError(
                    "Miscellaneous Receipt creation transaction rolled back as update to financial system failed.",
                    "Miscellaneous Receipt creation transaction rolled back as update to financial system failed."));
            LOGGER.error("Update to financial systems failed" + ex);
        }

        // Create Vouchers
        final List<CVoucherHeader> voucherHeaderList = new ArrayList<>();

        LOGGER.info("Receipt Voucher created with vouchernumber:        " + receiptHeader.getVoucherNum());

        for (final ReceiptVoucher receiptVoucher : receiptHeader.getReceiptVoucher())
            voucherHeaderList.add(receiptVoucher.getVoucherheader());

        /*
         * if (voucherHeaderList != null && !instrumentHeaderList.isEmpty()) {
         * receiptHeaderService.updateInstrument(voucherHeaderList,
         * instrumentHeaderList); }
         */
        LOGGER.info("Logs For Miscellaneous Receipt : Receipt Creation Finished....");
        return new BillReceiptInfoImpl(receiptHeader, chartOfAccountsHibernateDAO, persistenceService, null);
    }

    /*
     * @see
     * org.egov.infstr.collections.integration.CollectionIntegrationService#
     * getAggregateReceiptTotal (Date fromDate, Date toDate)
     */
    @Override
    public List<RestAggregatePaymentInfo> getAggregateReceiptTotal(final PaymentInfoSearchRequest aggrReq) {

        final List<RestAggregatePaymentInfo> listAggregatePaymentInfo = new ArrayList<>(0);
        final StringBuilder queryBuilder = new StringBuilder(
                "select  sum(recordcount) as records,ulb, sum(total) as total,service  from public.receipt_aggr_view "
                        + " where receipt_date>=:fromDate and receipt_date<=:toDate and service=:serviceCode "
                        + " and source=:source and ulb=:ulbCode  group by ulb,service  ");

        final Query query = getSession().createSQLQuery(queryBuilder.toString());
        query.setDate("fromDate", aggrReq.getFromdate());
        query.setDate("toDate", aggrReq.getTodate());
        query.setString("serviceCode", aggrReq.getServicecode());
        query.setString("source", aggrReq.getSource());
        query.setString("ulbCode", aggrReq.getUlbCode());

        LOGGER.debug(aggrReq.getSource());

        final List<Object[]> queryResults = query.list();

        for (final Object[] objectArray : queryResults) {
            final RestAggregatePaymentInfo aggregatePaymentInfo = new RestAggregatePaymentInfo();
            aggregatePaymentInfo.setTxncount(Integer.parseInt(objectArray[0].toString()));
            aggregatePaymentInfo.setUlbcode(objectArray[1].toString());
            aggregatePaymentInfo.setTxnamount(new BigDecimal(objectArray[2].toString()));
            aggregatePaymentInfo.setServiceCode(objectArray[3].toString());
            listAggregatePaymentInfo.add(aggregatePaymentInfo);
        }
        if (listAggregatePaymentInfo.isEmpty())
            listAggregatePaymentInfo.add(new RestAggregatePaymentInfo());
        return listAggregatePaymentInfo;
    }

    /*
     * @see
     * org.egov.infstr.collections.integration.CollectionIntegrationService#
     * getReceiptDetailsByDateAndService(final Date fromDate, final Date toDate,
     * final String serviceCode)
     */
    @Override
    public List<RestReceiptInfo> getReceiptDetailsByDateAndService(final PaymentInfoSearchRequest aggrReq) {
        final ArrayList<RestReceiptInfo> receipts = new ArrayList<>(0);
        RestReceiptInfo restReceiptInfo;
        final PaymentInfoService paymentInfoService = (PaymentInfoService) beanProvider.getBean("paymentInfoService");
        final List<ReceiptHeader> receiptHeaders = findAllByNamedQuery(
                CollectionConstants.QUERY_RECEIPTS_BY_DATE_AND_SERVICECODE, aggrReq.getFromdate(), aggrReq.getTodate(),
                aggrReq.getServicecode(), aggrReq.getSource());
        if (receiptHeaders == null || receiptHeaders.isEmpty()) {
            receipts.add(new RestReceiptInfo());
            return receipts;
        } else {
            for (final ReceiptHeader receiptHeader : receiptHeaders) {
                restReceiptInfo = new RestReceiptInfo(receiptHeader);
                paymentInfoService.setPaymentInfo(restReceiptInfo, receiptHeader);
                receipts.add(restReceiptInfo);
            }
            return receipts;
        }
    }

    @Override
    public String cancelReceipt(final PaymentInfoSearchRequest cancelReq) {
        String statusMessage;
        String instrumentType = "";
        boolean isInstrumentDeposited = false;
        final ReceiptHeader receiptHeaderToBeCancelled = (ReceiptHeader) persistenceService.findByNamedQuery(
                CollectionConstants.QUERY_RECEIPTS_BY_RECEIPTNUM, cancelReq.getReceiptNo());
        if (receiptHeaderToBeCancelled == null)
            throw new ApplicationRuntimeException("Invalid receiptNumber:" + cancelReq.getReceiptNo());
        else if (!cancelReq.getTransactionId().equals(receiptHeaderToBeCancelled.getManualreceiptnumber()))
            throw new ApplicationRuntimeException("transactionId doesnot match with receiptNo  "
                    + cancelReq.getReceiptNo());
        else if (CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED.equalsIgnoreCase(receiptHeaderToBeCancelled
                .getStatus().getCode()))
            throw new ApplicationRuntimeException("Receipt is already Cancelled  " + cancelReq.getReceiptNo());

        LOGGER.info("Receipt Header to be Cancelled : " + receiptHeaderToBeCancelled.getReceiptnumber());

        for (final InstrumentHeader instrumentHeader : receiptHeaderToBeCancelled.getReceiptInstrument())
            if (instrumentHeader.getInstrumentType().getType().equals(CollectionConstants.INSTRUMENTTYPE_CASH)) {
                if (instrumentHeader.getStatusId().getDescription()
                        .equals(CollectionConstants.INSTRUMENT_RECONCILED_STATUS)) {
                    isInstrumentDeposited = true;
                    break;
                }
            } else if (instrumentHeader.getStatusId().getDescription()
                    .equals(CollectionConstants.INSTRUMENT_DEPOSITED_STATUS)) {
                isInstrumentDeposited = true;
                break;
            }

        if (isInstrumentDeposited)
            statusMessage = CollectionConstants.RECEIPT_DEPOSITED_CANCELLED;
        else {
            // if instrument has not been deposited, cancel the old instrument,
            // reverse the
            // voucher and persist
            receiptHeaderToBeCancelled.setStatus(statusDAO.getStatusByModuleAndCode(
                    CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED));
            receiptHeaderToBeCancelled.setIsReconciled(false);
            receiptHeaderToBeCancelled.setReasonForCancellation(CollectionConstants.RECEIPT_CANCELLED_REASON);

            for (final InstrumentHeader instrumentHeader : receiptHeaderToBeCancelled.getReceiptInstrument()) {
                instrumentHeader.setStatusId(statusDAO.getStatusByModuleAndCode(
                        CollectionConstants.MODULE_NAME_INSTRUMENTHEADER,
                        CollectionConstants.INSTRUMENTHEADER_STATUS_CANCELLED));
                instrumentType = instrumentHeader.getInstrumentType().getType();

            }
            for (final ReceiptVoucher receiptVoucher : receiptHeaderToBeCancelled.getReceiptVoucher())
                receiptHeaderService.createReversalVoucher(receiptVoucher, instrumentType);

            receiptHeaderService.persist(receiptHeaderToBeCancelled);

            // End work-flow for the cancelled receipt
            if (receiptHeaderToBeCancelled.getState() != null
                    && !receiptHeaderToBeCancelled.getState().getValue().equals(CollectionConstants.WF_STATE_END))
                receiptHeaderService.endReceiptWorkFlowOnCancellation(receiptHeaderToBeCancelled);

            LOGGER.info("Receipt Cancelled with Receipt Number(saveOnCancel): "
                    + receiptHeaderToBeCancelled.getReceiptnumber() + "; Consumer Code: "
                    + receiptHeaderToBeCancelled.getConsumerCode());

            statusMessage = CollectionConstants.RECEIPT_CANCELLED_SUCCESS;

        }

        return statusMessage;
    }

    @Override
    public List<ReceiptDetail> getReceiptDetailListByReceiptNumber(final String receiptNumber) {
        final List<ReceiptDetail> receiptDetList = persistenceService.findAllByNamedQuery(
                CollectionConstants.QUERY_RECEIPTDETAIL_BY_RECEIPTNUMBER, receiptNumber);

        return receiptDetList;
    }

    @Override
    @Transactional
    public PaymentRequest processMobilePayments(final BillInfoImpl billInfo) {
        ReceiptHeader receiptHeader;
        BigDecimal totalAmountToBeCollected = BigDecimal.ZERO;

        final Fund fund = fundDAO.fundByCode(billInfo.getFundCode());

        if (fund == null)
            throw new ValidationException(Arrays.asList(new ValidationError("billreceipt.improperbilldata.missingfund",
                    "billreceipt.improperbilldata.missingfund")));

        final Department dept = (Department) persistenceService.findByNamedQuery(
                CollectionConstants.QUERY_DEPARTMENT_BY_CODE, billInfo.getDepartmentCode());

        if (dept == null)
            throw new ValidationException(
                    Arrays.asList(new ValidationError("billreceipt.improperbilldata.missingdepartment",
                            "billreceipt.improperbilldata.missingdepartment")));

        receiptHeader = collectionCommon.initialiseReceiptModelWithBillInfo(billInfo, fund, dept);
        totalAmountToBeCollected = totalAmountToBeCollected.add(receiptHeader.getTotalAmountToBeCollected());
        for (final ReceiptDetail rDetails : receiptHeader.getReceiptDetails())
            rDetails.getCramountToBePaid().setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT, BigDecimal.ROUND_UP);

        if (totalAmountToBeCollected.compareTo(BigDecimal.ZERO) == -1) {
            LOGGER.info("Amount to be collected is less than zero, hence cannot proceed.");
            throw new ValidationException(Arrays.asList(new ValidationError(
                    "billreceipt.totalamountlessthanzero.error", "billreceipt.totalamountlessthanzero.error")));
        } else
            receiptHeader.setTotalAmount(totalAmountToBeCollected.setScale(
                    CollectionConstants.AMOUNT_PRECISION_DEFAULT, BigDecimal.ROUND_UP));

        final ServiceDetails paymentService = (ServiceDetails) persistenceService.findByNamedQuery(
                CollectionConstants.QUERY_SERVICE_BY_CODE, CollectionConstants.SERVICECODE_AXIS);

        if (receiptHeader.getStatus() == null) {
            receiptHeader.setReceiptdate(new Date());
            // receiptHeader.setReferencenumber(billInfo.getTransactionReferenceNumber());
            receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_BILL);
            receiptHeader.setIsModifiable(Boolean.FALSE);
            // recon flag should be set as false when the receipt is
            // actually
            // created on successful online transaction
            receiptHeader.setIsReconciled(Boolean.TRUE);
            receiptHeader.setCollectiontype(CollectionConstants.COLLECTION_TYPE_ONLINECOLLECTION);
            receiptHeader.setStatus(collectionsUtil
                    .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_PENDING));
            receiptHeader.setSource(Source.MOBILE.toString());
            BigDecimal debitAmount = BigDecimal.ZERO;
            // TODO: Clarification
            // calculate sum of creditamounts as a debit value to
            // create a
            // debit account head and add to receipt details
            for (final ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails()) {
                receiptDetail.setCramount(receiptDetail.getCramount());
                debitAmount = debitAmount.add(receiptDetail.getCramount());
                debitAmount = debitAmount.subtract(receiptDetail.getDramount());
            }
            // end of outer for loop
            receiptHeader.addReceiptDetail(collectionCommon.addDebitAccountHeadDetails(debitAmount, receiptHeader,
                    BigDecimal.ZERO, receiptHeader.getTotalAmount(), CollectionConstants.INSTRUMENTTYPE_ONLINE));

            // Add Online Payment Details
            final OnlinePayment onlinePayment = new OnlinePayment();

            onlinePayment.setStatus(collectionsUtil.getStatusForModuleAndCode(
                    CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
                    CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING));
            onlinePayment.setReceiptHeader(receiptHeader);
            onlinePayment.setService(paymentService);

            receiptHeader.setOnlinePayment(onlinePayment);
        }
        receiptHeaderService.persistReceiptObject(receiptHeader);

        return collectionCommon.createPaymentRequest(paymentService, receiptHeader);
    }// end of method

    @Override
    public List<RestReceiptInfo> getDetailsByUserServiceAndConsumerCode(final PaymentInfoRequest paymentInfoRequest) {
        final ArrayList<RestReceiptInfo> receipts = new ArrayList<>(0);
        RestReceiptInfo restReceiptInfo;
        final List<ReceiptHeader> receiptHeaders;

        final StringBuilder queryString = new StringBuilder("from ReceiptHeader rh where 1=1 ");
        if (!paymentInfoRequest.getUserName().isEmpty())
            queryString.append(" and upper(rh.createdBy.username) = :userName");
        if (!paymentInfoRequest.getServiceName().isEmpty())
            queryString.append(" and rh.service.name = :serviceName");
        if (!paymentInfoRequest.getConsumerCode().isEmpty())
            queryString.append(" and rh.consumerCode = :consumerCode");
        final Query listQuery = getSession().createQuery(queryString.toString());
        if (!paymentInfoRequest.getUserName().isEmpty())
            listQuery.setString("userName", paymentInfoRequest.getUserName().toUpperCase());
        if (!paymentInfoRequest.getServiceName().isEmpty())
            listQuery.setString("serviceName", paymentInfoRequest.getServiceName());
        if (!paymentInfoRequest.getConsumerCode().isEmpty())
            listQuery.setString("consumerCode", paymentInfoRequest.getConsumerCode());
        receiptHeaders = listQuery.list();
        if (receiptHeaders == null || receiptHeaders.isEmpty()) {
            receipts.add(new RestReceiptInfo());
            return receipts;
        } else {
            for (final ReceiptHeader receiptHeader : receiptHeaders) {
                restReceiptInfo = new RestReceiptInfo(receiptHeader);
                receipts.add(restReceiptInfo);
            }
            return receipts;
        }
    }

    @Override
    public byte[] downloadReceiptByReceiptAndConsumerNo(final String receiptNumber, final String consumerCode) {
        final ReceiptHeader receiptHeader = (ReceiptHeader) persistenceService.findByNamedQuery(
                CollectionConstants.QUERY_RECEIPT_BY_SERVICE_RECEIPTNUMBER_CONSUMERCODE, receiptNumber,receiptNumber, consumerCode);
        return collectionsUtil.createReport(receiptHeaderService.getReportRequest(receiptHeader)).getReportOutputData();
    }

    public void setCollectionCommon(final CollectionCommon collectionCommon) {
        this.collectionCommon = collectionCommon;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

}