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
package org.egov.infra.search.elastic.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.infra.exception.ApplicationRuntimeException;

/**
 * Builder class for Application Index
 *
 */
public class CollectionIndexBuilder {

    private final CollectionIndex collectionIndex;

    public CollectionIndexBuilder(final Date receiptDate, final String receiptNumber, final String billingService,
            final String paymentMode, final BigDecimal totalAmount, final String channel, final String status) {

        collectionIndex = new CollectionIndex();
        collectionIndex.setReceiptDate(receiptDate);
        collectionIndex.setReceiptNumber(receiptNumber);
        collectionIndex.setBillingService(billingService);
        collectionIndex.setPaymentMode(paymentMode);
        collectionIndex.setTotalAmount(totalAmount);
        collectionIndex.setChannel(channel);
        collectionIndex.setStatus(status);
    }

    public CollectionIndexBuilder consumerCode(final String consumerCode) {
        collectionIndex.setConsumerCode(consumerCode);
        return this;
    }

    public CollectionIndexBuilder arrearAmount(final BigDecimal arrearAmount) {
        collectionIndex.setArrearAmount(arrearAmount);
        return this;
    }

    public CollectionIndexBuilder penaltyAmount(final BigDecimal penaltyAmount) {
        collectionIndex.setPenaltyAmount(penaltyAmount);
        return this;
    }

    public CollectionIndexBuilder currentAmount(final BigDecimal currentAmount) {
        collectionIndex.setCurrentAmount(currentAmount);
        return this;
    }

    public CollectionIndexBuilder advanceAmount(final BigDecimal advanceAmount) {
        collectionIndex.setAdvanceAmount(advanceAmount);
        return this;
    }

    public CollectionIndexBuilder paymentGateway(final String paymentGateway) {
        collectionIndex.setPaymentGateway(paymentGateway);
        return this;
    }

    public CollectionIndexBuilder billNumber(final String billNumber) {
        collectionIndex.setBillNumber(billNumber);
        return this;
    }

    public CollectionIndexBuilder latePaymentChargesAmount(final BigDecimal latePaymentCharges) {
        collectionIndex.setLatePaymentCharges(latePaymentCharges);
        return this;
    }

    public CollectionIndexBuilder arrearCess(final BigDecimal arrearCess) {
        collectionIndex.setArrearCess(arrearCess);
        return this;
    }

    public CollectionIndexBuilder currentCess(final BigDecimal currentCess) {
        collectionIndex.setCurrentCess(currentCess);
        return this;
    }

    public CollectionIndexBuilder installmentFrom(final String installmentFrom) {
        collectionIndex.setInstallmentFrom(installmentFrom);
        return this;
    }

    public CollectionIndexBuilder installmentTo(final String installmentTo) {
        collectionIndex.setInstallmentTo(installmentTo);
        return this;
    }

    public CollectionIndexBuilder payeeName(final String payeeName) {
        collectionIndex.setPayeeName(payeeName);
        return this;
    }

    public CollectionIndex build() throws ApplicationRuntimeException {
        validate();
        return collectionIndex;
    }

    private void validate() throws ApplicationRuntimeException {
        if (collectionIndex.getReceiptDate() == null)
            throw new ApplicationRuntimeException("Receipt Date is mandatory");
        if (collectionIndex.getReceiptNumber() == null)
            throw new ApplicationRuntimeException("Receipt Number is mandatory");
        if (collectionIndex.getPaymentMode() == null)
            throw new ApplicationRuntimeException("Payment Mode is mandatory");
        if (collectionIndex.getTotalAmount() == null)
            throw new ApplicationRuntimeException("Total Amount is mandatory");
        if (collectionIndex.getChannel() == null)
            throw new ApplicationRuntimeException("Channel is mandatory");
        if (collectionIndex.getStatus() == null)
            throw new ApplicationRuntimeException("Receipt Status is mandatory");
    }
}
