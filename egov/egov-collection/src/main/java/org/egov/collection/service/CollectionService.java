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
package org.egov.collection.service;

import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.OnlinePayment;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.integration.pgi.PaymentRequest;
import org.egov.collection.integration.services.DebitAccountHeadDetailsService;
import org.egov.collection.utils.CollectionCommon;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.commons.entity.Source;
import org.egov.infstr.models.ServiceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CollectionService {

    private CollectionsUtil collectionsUtil;
    private ReceiptHeaderService receiptHeaderService;
    private CollectionCommon collectionCommon;
    @Autowired
    private ApplicationContext beanProvider;

    public PaymentRequest populateAndPersistReceipts(final ServiceDetails paymentService,
            final ReceiptHeader receiptHeader, final List<ReceiptDetail> receiptDetailList,
            final BigDecimal paymentAmount, final Character collectionType) {
        // only newly created receipts need to be initialised with the data.
        // The cancelled receipt can be excluded from this processing.
        if (receiptHeader.getStatus() == null) {
            receiptHeader.setReceiptdate(new Date());
            receiptHeader.setReceipttype(CollectionConstants.RECEIPT_TYPE_BILL);
            receiptHeader.setIsModifiable(Boolean.FALSE);
            // recon flag should be set as false when the receipt is
            // actually
            // created on successful online transaction
            receiptHeader.setIsReconciled(Boolean.TRUE);
            receiptHeader.setCollectiontype(collectionType);
            receiptHeader.setStatus(collectionsUtil.getStatusForModuleAndCode(
                    CollectionConstants.MODULE_NAME_RECEIPTHEADER, CollectionConstants.RECEIPT_STATUS_CODE_PENDING));
            receiptHeader.setSource(Source.SYSTEM.toString());

            BigDecimal debitAmount = BigDecimal.ZERO;

            for (final ReceiptDetail creditChangeReceiptDetail : receiptDetailList) {
                // calculate sum of creditamounts as a debit value to
                // create a
                // debit account head and add to receipt details
                debitAmount = debitAmount.add(creditChangeReceiptDetail.getCramount());
                debitAmount = debitAmount.subtract(creditChangeReceiptDetail.getDramount());

                for (final ReceiptDetail receiptDetail : receiptHeader.getReceiptDetails())
                    if (creditChangeReceiptDetail.getReceiptHeader().getReferencenumber()
                            .equals(receiptDetail.getReceiptHeader().getReferencenumber())
                            && receiptDetail.getOrdernumber().equals(creditChangeReceiptDetail.getOrdernumber()))
                        receiptDetail.setCramount(creditChangeReceiptDetail.getCramount());
            }
            // end of outer for loop
            receiptHeader.setTotalAmount(paymentAmount);

            // Add Online Payment Details
            final OnlinePayment onlinePayment = new OnlinePayment();

            onlinePayment.setStatus(collectionsUtil.getStatusForModuleAndCode(
                    CollectionConstants.MODULE_NAME_ONLINEPAYMENT,
                    CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING));
            onlinePayment.setReceiptHeader(receiptHeader);
            onlinePayment.setService(paymentService);

            receiptHeader.setOnlinePayment(onlinePayment);
            DebitAccountHeadDetailsService debitAccountHeadService = (DebitAccountHeadDetailsService) beanProvider
                    .getBean(collectionsUtil.getBeanNameForDebitAccountHead());
            receiptHeader.addReceiptDetail(debitAccountHeadService.addDebitAccountHeadDetails(debitAmount, receiptHeader,
                    BigDecimal.ZERO, paymentAmount, CollectionConstants.INSTRUMENTTYPE_ONLINE));

        }
        receiptHeaderService.persistReceiptObject(receiptHeader);

        /**
         * Construct Request Object For The Payment Gateway
         */

        return collectionCommon.createPaymentRequest(paymentService, receiptHeader);

    }// end of method

    public void setCollectionsUtil(final CollectionsUtil collectionsUtil) {
        this.collectionsUtil = collectionsUtil;
    }

    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    public void setCollectionCommon(final CollectionCommon collectionCommon) {
        this.collectionCommon = collectionCommon;
    }

}
