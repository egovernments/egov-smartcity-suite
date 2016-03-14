/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.collection.integration.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.integration.models.BillReceiptInfo;
import org.egov.collection.integration.models.BillReceiptInfoImpl;
import org.egov.collection.integration.pgi.PaymentResponse;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionCommon;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

public class ReconciliationService {
    private static final Logger LOGGER = Logger.getLogger(ReconciliationService.class);
    public ReceiptHeaderService receiptHeaderService;
    private CollectionsUtil collectionsUtil;
    @Autowired
    private EgwStatusHibernateDAO egwStatusDAO;
    private CollectionCommon collectionCommon;
    @Autowired
    private ApplicationContext beanProvider;
    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    /**
     * This method processes the success message arriving from the payment
     * gateway. The receipt status is changed from PENDING to APPROVED and the
     * online transaction status is changed from PENDING to SUCCCESS. The
     * authorization status for success(0300) for the online transaction is also
     * persisted. An instrument of type 'ONLINE' is created with the transaction
     * details and are persisted along with the receipt details. Voucher for the
     * receipt is created and the Financial System is updated. The billing
     * system is updated about the receipt creation. In case update to financial
     * systems/billing system fails, the receipt creation is rolled back and the
     * receipt/payment status continues to be in PENDING state ( and will be
     * reconciled manually).
     *
     * @param onlinePaymentReceiptHeader
     * @param paymentResponse
     */
    @Transactional
    public void processSuccessMsg(final ReceiptHeader onlinePaymentReceiptHeader, final PaymentResponse paymentResponse) {

        final BillingIntegrationService billingService = (BillingIntegrationService) beanProvider
                .getBean(onlinePaymentReceiptHeader.getService().getCode()
                        + CollectionConstants.COLLECTIONS_INTERFACE_SUFFIX);

        onlinePaymentReceiptHeader.getReceiptDetails().clear();
        receiptHeaderService.persist(onlinePaymentReceiptHeader);
        final List<ReceiptDetail> receiptDetailList = billingService.reconstructReceiptDetail(
                onlinePaymentReceiptHeader.getReferencenumber(), onlinePaymentReceiptHeader.getTotalAmount(),
                new ArrayList(onlinePaymentReceiptHeader.getReceiptDetails()));
        if (receiptDetailList != null) {
            LOGGER.debug("Reconstructed receiptDetailList : " + receiptDetailList.toString());
            for (final ReceiptDetail receiptDetail : receiptDetailList) {
                receiptDetail.setReceiptHeader(onlinePaymentReceiptHeader);
                onlinePaymentReceiptHeader.addReceiptDetail(receiptDetail);
            }
        }
        // Add debit account head
        onlinePaymentReceiptHeader.addReceiptDetail(collectionCommon.addDebitAccountHeadDetails(
                onlinePaymentReceiptHeader.getTotalAmount(), onlinePaymentReceiptHeader, BigDecimal.ZERO,
                onlinePaymentReceiptHeader.getTotalAmount(), CollectionConstants.INSTRUMENTTYPE_ONLINE));

        createSuccessPayment(onlinePaymentReceiptHeader, paymentResponse.getTxnDate(),
                paymentResponse.getTxnReferenceNo(), paymentResponse.getAuthStatus(), null);

        LOGGER.debug("Persisted receipt after receiving success message from the payment gateway");

        boolean updateToSystems = true;

        try {
            Boolean createVoucherForBillingService = collectionsUtil.checkVoucherCreation(onlinePaymentReceiptHeader);
            if(createVoucherForBillingService)
            receiptHeaderService.createVoucherForReceipt(onlinePaymentReceiptHeader);
            LOGGER.debug("Updated financial systems and created voucher.");
        } catch (final ApplicationRuntimeException ex) {
            updateToSystems = false;
            onlinePaymentReceiptHeader.getOnlinePayment().setRemarks("Update to financial systems failed");
            LOGGER.error("Update to financial systems failed");
        }

        try {
            if (!updateBillingSystem(onlinePaymentReceiptHeader.getService().getCode(), new BillReceiptInfoImpl(
                    onlinePaymentReceiptHeader, chartOfAccountsHibernateDAO), billingService))
                updateToSystems = false;
        } catch (final ApplicationRuntimeException ex) {
            onlinePaymentReceiptHeader.getOnlinePayment().setRemarks("update to billing system failed.");
        }
        if (updateToSystems) {
            onlinePaymentReceiptHeader.setIsReconciled(true);
            receiptHeaderService.persist(onlinePaymentReceiptHeader);
            LOGGER.debug("Updated billing system : " + onlinePaymentReceiptHeader.getService().getName());
        } else
            LOGGER.debug("Rolling back receipt creation transaction as update to billing system/financials failed.");
    }

    /**
     * @param receipts
     *            - list of receipts which have to be processed as successful
     *            payments. For payments created as a response from TECHPRO,
     *            size of the array will be 1.
     */
    @Transactional
    private void createSuccessPayment(final ReceiptHeader receipt, final Date transactionDate,
            final String transactionId, final String authStatusCode, final String remarks) {
        final EgwStatus receiptStatus = collectionsUtil
                .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_APPROVED);
        receipt.setStatus(receiptStatus);

        receipt.setReceiptInstrument(receiptHeaderService.createOnlineInstrument(transactionDate, transactionId,
                receipt.getTotalAmount()));

        receiptHeaderService.setReceiptNumber(receipt);
        receipt.setIsReconciled(Boolean.FALSE);
        receipt.getOnlinePayment().setAuthorisationStatusCode(authStatusCode);
        receipt.getOnlinePayment().setTransactionNumber(transactionId);
        receipt.getOnlinePayment().setTransactionDate(transactionDate);
        receipt.getOnlinePayment().setRemarks(remarks);

        // set online payment status as SUCCESS
        receipt.getOnlinePayment().setStatus(
                collectionsUtil.getStatusForModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
                        CollectionConstants.ONLINEPAYMENT_STATUS_CODE_SUCCESS));

        receiptHeaderService.persist(receipt);
    }

    /**
     * This method processes the failure message arriving from the payment
     * gateway. The receipt and the online transaction are both cancelled. The
     * authorization status for reason of failure is also persisted. The reason
     * for payment failure is displayed back to the user
     *
     * @param onlinePaymentReceiptHeader
     * @param paymentResponse
     */
    @Transactional
    public void processFailureMsg(final ReceiptHeader receiptHeader, final PaymentResponse paymentResponse) {

        final EgwStatus receiptStatus = collectionsUtil
                .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED);
        receiptHeader.setStatus(receiptStatus);
        EgwStatus paymentStatus;
        if (CollectionConstants.AXIS_ABORTED_STATUS_CODE.equals(paymentResponse.getAuthStatus()))
            paymentStatus = egwStatusDAO.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
                    CollectionConstants.ONLINEPAYMENT_STATUS_CODE_ABORTED);
        else
            paymentStatus = egwStatusDAO.getStatusByModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
                    CollectionConstants.ONLINEPAYMENT_STATUS_CODE_FAILURE);
        receiptHeader.getOnlinePayment().setStatus(paymentStatus);
        receiptHeader.getOnlinePayment().setAuthorisationStatusCode(paymentResponse.getAuthStatus());
        receiptHeader.getOnlinePayment().setRemarks(paymentResponse.getErrorDescription());
        receiptHeaderService.persist(receiptHeader);

        LOGGER.debug("Cancelled receipt after receiving failure message from the payment gateway");
    }

    /**
     * This method looks up the bean to communicate with the billing system and
     * updates the billing system.
     */
    public Boolean updateBillingSystem(final String serviceCode, final BillReceiptInfo billReceipt,
            final BillingIntegrationService billingService) {
        if (billingService == null)
            return false;
        else
            try {
                final Set<BillReceiptInfo> billReceipts = new HashSet<BillReceiptInfo>();
                billReceipts.add(billReceipt);
                LOGGER.info("$$$$$$ Update Billing System for BillReceiptInfo:" + billReceipt.toString());
                billingService.updateReceiptDetails(billReceipts);
                return true;
            } catch (final Exception e) {
                final String errMsg = "Exception while updating billing system [" + serviceCode
                        + "] with receipt details!";
                LOGGER.debug(errMsg);
                LOGGER.error(errMsg, e);
                throw new ApplicationRuntimeException(errMsg, e);
            }
    }

    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    public void setCollectionCommon(final CollectionCommon collectionCommon) {
        this.collectionCommon = collectionCommon;
    }

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

}
