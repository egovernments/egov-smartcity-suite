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
package org.egov.wtms.masters.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import org.egov.ptis.domain.model.ErrorDetails;

@SuppressWarnings("serial")
public class WaterReceiptDetails implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6603088499173356582L;
    private String receiptNo;
    private String receiptDate;
    private String payeeName;
    private String payeeAddress;
    private String billReferenceNo;
    private String serviceName;
    private String description;
    private String paidBy;
    private BigDecimal paymentAmount;
    private String collectionType;
    private String transactionId;
    private ErrorDetails errorDetails;

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(final String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(final String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(final String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeAddress() {
        return payeeAddress;
    }

    public void setPayeeAddress(final String payeeAddress) {
        this.payeeAddress = payeeAddress;
    }

    public String getBillReferenceNo() {
        return billReferenceNo;
    }

    public void setBillReferenceNo(final String billReferenceNo) {
        this.billReferenceNo = billReferenceNo;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(final String paidBy) {
        this.paidBy = paidBy;
    }

    public BigDecimal getTotalAmountPaid() {
        return paymentAmount;
    }

    public void setTotalAmountPaid(final BigDecimal totalAmountPaid) {
        this.paymentAmount = totalAmountPaid;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(final String collectionType) {
        this.collectionType = collectionType;
    }

    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(final ErrorDetails errorDetails) {
        this.errorDetails = errorDetails;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(final String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "ReceiptDetails [receiptNo=" + receiptNo + ", receiptDate=" + receiptDate + ", payeeName=" + payeeName
                + ", payeeAddress=" + payeeAddress + ", billReferenceNo=" + billReferenceNo + ", serviceName="
                + serviceName + ", description=" + description + ", paidBy=" + paidBy + ", paymentAmount="
                + paymentAmount + ", collectionType=" + collectionType + ", errorDetails=" + errorDetails + "]";
    }

}
