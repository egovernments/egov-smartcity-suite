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
package org.egov.collection.integration.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.integration.pgi.PaymentResponse;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.collection.utils.CollectionCommon;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.collection.utils.FinancialsUtil;
import org.egov.commons.EgwStatus;
import org.egov.commons.dao.ChartOfAccountsHibernateDAO;
import org.egov.infstr.services.PersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReconciliationService {
    private static final Logger LOGGER = Logger.getLogger(ReconciliationService.class);

    @Autowired
    public ReceiptHeaderService receiptHeaderService;

    @Autowired
    private CollectionsUtil collectionsUtil;

    @Autowired
    private ChartOfAccountsHibernateDAO chartOfAccountsHibernateDAO;

    @Autowired
    private CollectionCommon collectionCommon;

    @Autowired
    private PersistenceService persistenceService;

    @Autowired
    private ApplicationContext beanProvider;

    /**
     * This method processes the success message arriving from the payment gateway. The receipt status is changed from PENDING to
     * APPROVED and the online transaction status is changed from PENDING to SUCCCESS. The authorization status for success(0300)
     * for the online transaction is also persisted. An instrument of type 'ONLINE' is created with the transaction details and
     * are persisted along with the receipt details. Voucher for the receipt is created and the Financial System is updated. The
     * billing system is updated about the receipt creation. In case update to financial systems/billing system fails, the receipt
     * creation is rolled back and the receipt/payment status continues to be in PENDING state ( and will be reconciled manually).
     *
     * @param onlinePaymentReceiptHeader
     * @param paymentResponse
     */
    @Transactional
    public void processSuccessMsg(final ReceiptHeader onlinePaymentReceiptHeader, final PaymentResponse paymentResponse) {

        final List<ReceiptDetail> existingReceiptDetails = new ArrayList<ReceiptDetail>(0);

        for (final ReceiptDetail receiptDetail : onlinePaymentReceiptHeader.getReceiptDetails())
            if (!FinancialsUtil.isRevenueAccountHead(receiptDetail.getAccounthead(),
                    chartOfAccountsHibernateDAO.getBankChartofAccountCodeList(), persistenceService)) {
                final ReceiptDetail newReceiptDetail = new ReceiptDetail();
                if (receiptDetail.getOrdernumber() != null)
                    newReceiptDetail.setOrdernumber(receiptDetail.getOrdernumber());
                if (receiptDetail.getDescription() != null)
                    newReceiptDetail.setDescription(receiptDetail.getDescription());
                if (receiptDetail.getIsActualDemand() != null)
                    newReceiptDetail.setIsActualDemand(receiptDetail.getIsActualDemand());
                if (receiptDetail.getFunction() != null)
                    newReceiptDetail.setFunction(receiptDetail.getFunction());
                if (receiptDetail.getCramountToBePaid() != null)
                    newReceiptDetail.setCramountToBePaid(receiptDetail.getCramountToBePaid());
                newReceiptDetail.setCramount(receiptDetail.getCramount());
                newReceiptDetail.setAccounthead(receiptDetail.getAccounthead());
                newReceiptDetail.setDramount(receiptDetail.getDramount());
                newReceiptDetail.setPurpose(receiptDetail.getPurpose());
                existingReceiptDetails.add(newReceiptDetail);
            }

        final List<ReceiptDetail> reapportionedList = collectionsUtil.reconstructReceiptDetail(onlinePaymentReceiptHeader,
                existingReceiptDetails);

        List<ReceiptDetail> reconstructedList = new ArrayList<ReceiptDetail>();
        if (reapportionedList != null && !reapportionedList.isEmpty()) {
            reconstructedList = reapportionedList;
        } else {
            reconstructedList = existingReceiptDetails;
        }

        ReceiptDetail debitAccountDetail = null;
        if (reconstructedList != null) {
            DebitAccountHeadDetailsService debitAccountHeadService = (DebitAccountHeadDetailsService) beanProvider
                    .getBean(collectionsUtil.getBeanNameForDebitAccountHead());
            debitAccountDetail = debitAccountHeadService.addDebitAccountHeadDetails(
                    onlinePaymentReceiptHeader.getTotalAmount(), onlinePaymentReceiptHeader, BigDecimal.ZERO,
                    onlinePaymentReceiptHeader.getTotalAmount(), CollectionConstants.INSTRUMENTTYPE_ONLINE);
        }

        receiptHeaderService.reconcileOnlineSuccessPayment(onlinePaymentReceiptHeader, paymentResponse.getTxnDate(),
                paymentResponse.getTxnReferenceNo(), paymentResponse.getTxnAmount(), paymentResponse.getAuthStatus(),
                reconstructedList,
                debitAccountDetail);
        LOGGER.debug("Persisted receipt after receiving success message from the payment gateway");

    }

    /**
     * This method processes the failure message arriving from the payment gateway. The receipt and the online transaction are
     * both cancelled. The authorization status for reason of failure is also persisted. The reason for payment failure is
     * displayed back to the user
     *
     * @param onlinePaymentReceiptHeader
     * @param paymentResponse
     */
    @Transactional
    public void processFailureMsg(final ReceiptHeader receiptHeader, final PaymentResponse paymentResponse) {
        receiptHeader.setStatus(collectionsUtil
                .getReceiptStatusForCode(CollectionConstants.RECEIPT_STATUS_CODE_FAILED));
        EgwStatus paymentStatus;
        if (receiptHeader.getOnlinePayment().getService().getCode().equals(CollectionConstants.SERVICECODE_AXIS) 
                && CollectionConstants.AXIS_ABORTED_STATUS_CODE.equals(paymentResponse.getAuthStatus()))
            paymentStatus = collectionsUtil.getStatusForModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
                    CollectionConstants.ONLINEPAYMENT_STATUS_CODE_ABORTED);
        else if (receiptHeader.getOnlinePayment().getService().getCode().equals(CollectionConstants.SERVICECODE_ATOM) 
                && CollectionConstants.ATOM_AUTHORISATION_CODE_REFUNDED.equals(paymentResponse.getAuthStatus()))
            paymentStatus = collectionsUtil.getStatusForModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
                    CollectionConstants.ONLINEPAYMENT_STATUS_CODE_REFUNDED);
        else
            paymentStatus = collectionsUtil.getStatusForModuleAndCode(CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
                    CollectionConstants.ONLINEPAYMENT_STATUS_CODE_FAILURE);
        receiptHeader.getOnlinePayment().setStatus(paymentStatus);
        receiptHeader.getOnlinePayment().setAuthorisationStatusCode(paymentResponse.getAuthStatus());
        receiptHeader.getOnlinePayment().setRemarks(paymentResponse.getErrorDescription());
        receiptHeaderService.persist(receiptHeader);

        LOGGER.debug("Cancelled receipt after receiving failure message from the payment gateway");
    }
}
