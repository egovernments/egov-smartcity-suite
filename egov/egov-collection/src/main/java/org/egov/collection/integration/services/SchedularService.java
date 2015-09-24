package org.egov.collection.integration.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.OnlinePayment;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.integration.pgi.AxisAdaptor;
import org.egov.collection.integration.pgi.PaymentResponse;
import org.egov.collection.service.ReceiptHeaderService;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class SchedularService {

    private static final Logger LOGGER = Logger.getLogger(SchedularService.class);
    protected PersistenceService persistenceService;
    private ReceiptHeaderService receiptHeaderService;
    private ReceiptHeader onlinePaymentReceiptHeader;
    private PaymentResponse paymentResponse;
    private ReconciliationService reconciliationService;

    public void reconcileAXIS() {

        LOGGER.debug("Inside reconcileAXIS");
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -30);
        final Query qry = persistenceService
                .getSession()
                .createQuery(
                        "select receipt from org.egov.collection.entity.OnlinePayment as receipt where receipt.status.code=:onlinestatuscode"
                                + "  and receipt.service.code=:paymentservicecode and receipt.createdDate<:thirtyminslesssysdate ")
                                .setMaxResults(50);
        qry.setString("onlinestatuscode", CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING);
        qry.setString("paymentservicecode", CollectionConstants.SERVICECODE_AXIS);
        qry.setParameter("thirtyminslesssysdate", new Date(cal.getTimeInMillis()));
        final List<OnlinePayment> reconcileList = qry.list();

        LOGGER.debug("Thread ID = " + Thread.currentThread().getId() + ": got " + reconcileList.size() + " results.");
        if (reconcileList.size() > 0) {
            final ServiceDetails paymentService = (ServiceDetails) persistenceService.findByNamedQuery(
                    CollectionConstants.QUERY_SERVICE_BY_CODE, CollectionConstants.SERVICECODE_AXIS);
            for (final OnlinePayment onlinePaymentObj : reconcileList) {
                final long startTimeInMilis = System.currentTimeMillis();
                paymentResponse = null;
                final AxisAdaptor axisAdaptor = new AxisAdaptor();
                LOGGER.info("AXIS Receiptid::::" + onlinePaymentObj.getReceiptHeader().getId());
                paymentResponse = axisAdaptor.createOfflinePaymentRequest(paymentService, onlinePaymentObj);
                if (null != paymentResponse) {
                    LOGGER.info("paymentResponse.getReceiptId():" + paymentResponse.getReceiptId());
                    LOGGER.info("paymentResponse.getAdditionalInfo6():" + paymentResponse.getAdditionalInfo6());
                    onlinePaymentReceiptHeader = receiptHeaderService.findByNamedQuery(
                            CollectionConstants.QUERY_RECEIPT_BY_ID_AND_CONSUMERCODE,
                            Long.valueOf(paymentResponse.getReceiptId()), paymentResponse.getAdditionalInfo6());
                    if (onlinePaymentReceiptHeader != null) {
                        if (CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS.equals(paymentResponse.getAuthStatus()))
                            reconciliationService.processSuccessMsg(onlinePaymentReceiptHeader, paymentResponse);
                        else {
                            onlinePaymentReceiptHeader.getOnlinePayment().setRemarks(
                                    paymentResponse.getErrorDescription());
                            reconciliationService.processFailureMsg(onlinePaymentReceiptHeader, paymentResponse);
                        }

                        final long elapsedTimeInMillis = System.currentTimeMillis() - startTimeInMilis;
                        LOGGER.info("$$$$$$ Online Receipt Persisted with Receipt Number: "
                                + onlinePaymentReceiptHeader.getReceiptnumber()
                                + (onlinePaymentReceiptHeader.getConsumerCode() != null ? " and consumer code: "
                                        + onlinePaymentReceiptHeader.getConsumerCode() : "") + "; Time taken(ms) = "
                                        + elapsedTimeInMillis);
                    } else
                        LOGGER.info("onlinePaymentReceiptHeader object is null");
                }
            }
        }
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setReceiptHeaderService(final ReceiptHeaderService receiptHeaderService) {
        this.receiptHeaderService = receiptHeaderService;
    }

    public void setReconciliationService(final ReconciliationService reconciliationService) {
        this.reconciliationService = reconciliationService;
    }

}
