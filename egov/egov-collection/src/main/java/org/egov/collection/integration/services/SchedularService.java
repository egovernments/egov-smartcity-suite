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

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.OnlinePayment;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.collection.integration.pgi.AxisAdaptor;
import org.egov.collection.integration.pgi.PaymentResponse;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infstr.services.PersistenceService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class SchedularService {

    private static final Logger LOGGER = Logger.getLogger(SchedularService.class);
    private PersistenceService persistenceService;
    private ReconciliationService reconciliationService;

    @Autowired
    private AxisAdaptor axisAdaptor;

    @Transactional
    public void reconcileAXIS() {

        LOGGER.debug("Inside reconcileAXIS");
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -30);
        final Query qry = persistenceService
                .getSession()
                .createQuery(
                        "select receipt from org.egov.collection.entity.OnlinePayment as receipt where receipt.status.code=:onlinestatuscode"
                                + " and receipt.service.code=:paymentservicecode and receipt.createdDate<:thirtyminslesssysdate")
                                .setMaxResults(50);
        qry.setString("onlinestatuscode", CollectionConstants.ONLINEPAYMENT_STATUS_CODE_PENDING);
        qry.setString("paymentservicecode", CollectionConstants.SERVICECODE_AXIS);
        qry.setParameter("thirtyminslesssysdate", new Date(cal.getTimeInMillis()));
        final List<OnlinePayment> reconcileList = qry.list();

        LOGGER.debug("Thread ID = " + Thread.currentThread().getId() + ": got " + reconcileList.size() + " results.");
        if (!reconcileList.isEmpty()) {
            for (final OnlinePayment onlinePaymentObj : reconcileList) {
                final long startTimeInMilis = System.currentTimeMillis();
                LOGGER.info("AXIS Receiptid::::" + onlinePaymentObj.getReceiptHeader().getId());
                PaymentResponse paymentResponse = axisAdaptor.createOfflinePaymentRequest(onlinePaymentObj);

                if (paymentResponse != null && isNotBlank(paymentResponse.getReceiptId())) {
                    LOGGER.info("paymentResponse.getReceiptId():" + paymentResponse.getReceiptId());
                    LOGGER.info("paymentResponse.getAdditionalInfo6():" + paymentResponse.getAdditionalInfo6());
                    LOGGER.info("paymentResponse.getAuthStatus():" + paymentResponse.getAuthStatus());
                    ReceiptHeader onlinePaymentReceiptHeader = (ReceiptHeader) persistenceService.findByNamedQuery(
                            CollectionConstants.QUERY_RECEIPT_BY_ID_AND_CITYCODE, Long.valueOf(paymentResponse.getReceiptId()),
                            ApplicationThreadLocals.getCityCode());
                    if (CollectionConstants.PGI_AUTHORISATION_CODE_SUCCESS.equals(paymentResponse.getAuthStatus()))
                        reconciliationService.processSuccessMsg(onlinePaymentReceiptHeader, paymentResponse);
                    else
                        reconciliationService.processFailureMsg(onlinePaymentReceiptHeader, paymentResponse);

                    final long elapsedTimeInMillis = System.currentTimeMillis() - startTimeInMilis;
                    LOGGER.info("$$$$$$ Online Receipt Persisted with Receipt Number: "
                            + onlinePaymentReceiptHeader.getReceiptnumber()
                            + (onlinePaymentReceiptHeader.getConsumerCode() != null ? " and consumer code: "
                                    + onlinePaymentReceiptHeader.getConsumerCode() : "") + "; Time taken(ms) = "
                                    + elapsedTimeInMillis);
                }
            }
        }
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void setReconciliationService(final ReconciliationService reconciliationService) {
        this.reconciliationService = reconciliationService;
    }

}
