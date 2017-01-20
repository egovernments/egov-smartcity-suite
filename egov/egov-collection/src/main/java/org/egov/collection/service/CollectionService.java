package org.egov.collection.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.OnlinePayment;
import org.egov.collection.entity.ReceiptDetail;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.integration.pgi.PaymentRequest;
import org.egov.collection.utils.CollectionCommon;
import org.egov.collection.utils.CollectionsUtil;
import org.egov.commons.entity.Source;
import org.egov.infstr.models.ServiceDetails;
import org.springframework.stereotype.Service;

@Service
public class CollectionService {

    private CollectionsUtil collectionsUtil;
    private ReceiptHeaderService receiptHeaderService;
    private CollectionCommon collectionCommon;

    public PaymentRequest populateAndPersistReceipts(final ServiceDetails paymentService,
            final ReceiptHeader receiptHeader, final List<ReceiptDetail> receiptDetailList,
            final BigDecimal paymentAmount, Character collectionType) {
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
            
            receiptHeader.addReceiptDetail(collectionCommon.addDebitAccountHeadDetails(debitAmount, receiptHeader,
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
